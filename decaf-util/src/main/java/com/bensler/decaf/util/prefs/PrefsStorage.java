package com.bensler.decaf.util.prefs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.bensler.decaf.util.tree.Hierarchy;

public class PrefsStorage {

  public static final String DTD_SYSTEM_ID_1_0 = "com.bensler.decaf.Prefs.1.0";

  public static final String DTD_RESOURCE_1_0 = "prefs-1.0.dtd";

  public static final String ROOT_ELEMENT_NAME = "prefs";
  public static final String NODE_ELEMENT_NAME = "node";
  public static final String NODE_ELEMENT_ATTRIBUTE_NAME = "name";
  public static final String NODE_ELEMENT_ATTRIBUTE_VALUE = "value";

  private static final EntityResolver ENTITY_RESOLVER = new EntityResolverImpl();

  private static final ErrorHandler ERROR_HANDLER = new ErrorHandlerImpl();

  private final File storageLocation_;

  private final Map<PrefKey, String> prefs_;

  public PrefsStorage(File storageLocation) throws PrefsReadException {
    storageLocation_ = storageLocation;
    prefs_ = new HashMap<>();

    try {
      read();
    } catch (FileNotFoundException fnfe) {
      // no prob, stay empty
    }
  }

  private void read() throws FileNotFoundException, PrefsReadException {
    try {
      final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newDefaultInstance();
      builderFactory.setValidating(true);
      final DocumentBuilder builder = builderFactory.newDocumentBuilder();
      builder.setEntityResolver(ENTITY_RESOLVER);
      builder.setErrorHandler(ERROR_HANDLER);
      Document document = builder.parse(storageLocation_);
      readElement(PrefKey.ROOT, document.getDocumentElement().getChildNodes());
    } catch (FileNotFoundException fnfe) {
      throw fnfe;
    } catch (ParserConfigurationException | SAXException | IOException | RuntimeException e) {
      throw new PrefsReadException(e);
    }
  }

  private void readElement(PrefKey parent, NodeList nodes) {
    for (int i = 0; i < nodes.getLength(); i++) {
      final Node node = nodes.item(i);

      if (node.getNodeType() == Node.ELEMENT_NODE) {
        final Element element = (Element) node;

        if (element.getTagName().equals(NODE_ELEMENT_NAME)) {
          final PrefKey prefKey = new PrefKey(parent, element.getAttribute(NODE_ELEMENT_ATTRIBUTE_NAME));
          final String value = element.getAttribute(NODE_ELEMENT_ATTRIBUTE_VALUE);

          if (!value.isEmpty()) {
            put(prefKey, value);
          }
          readElement(prefKey, element.getChildNodes());
        }
      }
    }
  }

  public String put(PrefKey prefKey, String value) {
    return prefs_.put(prefKey, value);
  }

  public String get(PrefKey prefKey, String defaultValue) {
    return get(prefKey).orElse(defaultValue);
  }

  public Optional<String> get(PrefKey prefKey) {
    return Optional.ofNullable(prefs_.get(prefKey));
  }

  public boolean contains(PrefKey prefKey) {
    return prefs_.containsKey(prefKey);
  }

  private void prependParent(PrefKey prefKey, LinkedList<PrefKey> path) {
    final PrefKey parent = prefKey.getParent();

    path.addFirst(prefKey);
    if (parent != null) {
      prependParent(parent, path);
    }
  }

  public static <E extends Enum<E>> Optional<E> tryParseEnum(Class<E> enumClass, String value) {
    try {
      return Optional.of(Enum.valueOf(enumClass, value));
    } catch (IllegalArgumentException iae) {
      return Optional.empty();
    }
  }

  public static Optional<Integer> tryParseInt(String value) {
    try {
        return Optional.of(Integer.parseInt(value));
    } catch (NumberFormatException nfe) {
        return Optional.empty();
    }
  }

  private Stream<PrefKey> getParentPath(PrefKey prefKey) {
    final LinkedList<PrefKey> path = new LinkedList<>();

    prependParent(prefKey, path);
    return path.stream();
  }

  public void store() throws Exception {
    final DocumentBuilder builder = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder();
    final Document document = builder.newDocument();
    final Element rootElement = document.createElement(ROOT_ELEMENT_NAME);

    document.appendChild(rootElement);

    createNodes(rootElement, PrefKey.ROOT, new Hierarchy<>(prefs_.keySet()
      .stream()
      .flatMap(this::getParentPath)
      .distinct()
      .collect(Collectors.toSet())
    ));

    final TransformerFactory transformerFactory = TransformerFactory.newInstance();
    final Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, PrefsStorage.DTD_SYSTEM_ID_1_0);
    transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "");
    transformer.transform(new DOMSource(document), new StreamResult(storageLocation_));
  }

  private void createNodes(Element parentElement, PrefKey parentKey, Hierarchy<PrefKey> prefsHierarchy) {
    final List<PrefKey> childPrefs = new ArrayList<>(prefsHierarchy.getChildren(parentKey));

    Collections.sort(childPrefs, (pref1, pref2) -> pref1.getName().compareToIgnoreCase(pref2.getName()));
    childPrefs.forEach(prefKey -> {
      final Element childElement = parentElement.getOwnerDocument().createElement(NODE_ELEMENT_NAME);
      final Optional<String> prefValue = get(prefKey);

      childElement.setAttribute(NODE_ELEMENT_ATTRIBUTE_NAME, prefKey.getName());
      prefValue.ifPresent(value -> childElement.setAttribute(NODE_ELEMENT_ATTRIBUTE_VALUE, value));
      parentElement.appendChild(childElement);
      createNodes(childElement, prefKey, prefsHierarchy);
    });
  }

  static class EntityResolverImpl implements EntityResolver {

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
      return (PrefsStorage.DTD_SYSTEM_ID_1_0.equals(publicId)
        ? new InputSource(getClass().getResourceAsStream(PrefsStorage.DTD_RESOURCE_1_0))
        : null
      );
    }

  }

  static class ErrorHandlerImpl implements ErrorHandler {

    @Override
    public void warning(SAXParseException exception) throws SAXException {
      throw exception;
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
      throw exception;
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
      throw exception;
    }

  }

}
