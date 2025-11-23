package com.bensler.decaf.swing.view;

import static com.bensler.decaf.swing.view.SimplePropertyGetter.createGetterComparator;
import static com.bensler.decaf.util.cmp.CollatorComparator.COLLATOR_COMPARATOR;

import java.awt.Component;
import java.util.Optional;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

import com.bensler.decaf.swing.tree.DefaultTreeCellRenderComponent;
import com.bensler.decaf.swing.tree.SynthRoot;
import com.bensler.decaf.util.Named;


public class PropertyViewImpl<E, P> extends Object implements PropertyView<E, P> {

  public final static  PropertyViewImpl<Object, String> OBJECT = new PropertyViewImpl<>(
    createGetterComparator(Object::toString, COLLATOR_COMPARATOR)
  );

  public  final static  PropertyViewImpl<Named, String> NAMED = new PropertyViewImpl<>(
    createGetterComparator(Named::getName, COLLATOR_COMPARATOR)
  );

  private final RenderComponent listComp_;

  private final CellRenderer<E, P, JLabel> renderer_;

  private final PropertyGetter<E, P> getter_;

  private final SimpleCellRenderer<E, String> nullRenderer_;

  public PropertyViewImpl(
    PropertyGetter<E, P> getter
  ) {
    this(new SimpleCellRenderer<>(null, null), getter);
  }

  public PropertyViewImpl(
    Icon icon, PropertyGetter<E, P> getter
  ) {
    this(new SimpleCellRenderer<>(null, (_, _) -> icon), getter);
  }

  public PropertyViewImpl(
    CellRenderer<E, P, JLabel> cellRenderer, PropertyGetter<E, P> propertyGetter
  ) {
    this(cellRenderer, propertyGetter, new DefaultCellRenderComponent());
  }

  public PropertyViewImpl(
    CellRenderer<E, P, JLabel> cellRenderer,
    PropertyGetter<E, P> propertyGetter, RenderComponent renderComponent
  ) {
    renderer_ = cellRenderer;
    getter_ = propertyGetter;
    listComp_ = renderComponent;
    nullRenderer_ = new SimpleCellRenderer<>(null, null);
  }

  @Override
  public TreeCellRenderer createTreeCellRenderer() {
    return new PropertyTreeCellRenderer();
  }

  class PropertyTreeCellRenderer implements TreeCellRenderer {

    private final TreeRenderComponent<RendererLabel> treeRenderComponent_;

    PropertyTreeCellRenderer() {
      treeRenderComponent_ = new DefaultTreeCellRenderComponent();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Component getTreeCellRendererComponent(
      JTree tree, Object value, boolean selected,
      boolean expanded, boolean leaf, int row, boolean hasFocus
    ) {
      final JLabel label = treeRenderComponent_.prepareForTree(tree, selected, expanded, leaf, row, hasFocus);

      value = (value instanceof SynthRoot) ? null : value;
      return renderNullSafe((E)value, label, renderer_, getter_);
    }
  }

  @Override
  public Component getCellRendererComponent(
    JTable table, E viewable, P cellValue, boolean selected,
    boolean hasFocus, int row, int column
  ) {
    final JLabel label = listComp_.prepareForTable(table, selected, row, column, hasFocus);

    renderer_.render(viewable, cellValue, label);
    return label;
  }

  @Override
  public Component getListCellRendererComponent(
    JList<? extends E> list, E value, int index,
    boolean selected, boolean hasFocus
  ) {
    final JLabel label = listComp_.prepareForList(list, selected, index, hasFocus);

    return renderNullSafe(value, label, renderer_, getter_);
  }

  private JLabel renderNullSafe(
    E value, JLabel label, CellRenderer<E, P, JLabel> renderer, PropertyGetter<E, P> getter
  ) {
    if (value == null) {
      nullRenderer_.render(null, " ", label);
    } else {
      try {
        renderer.render(value, getter.getProperty(value), label);
      } catch (Exception e) {
        e.printStackTrace(); // TODO
        nullRenderer_.render(value, " ", label);
      }
    }
    return label;
  }

  @Override
  public String getPropertyString(E entity) {
    return Optional.of(getProperty(entity)).map(P::toString).orElse("");
  }

  @Override
  public P getProperty(E viewable) {
    return getter_.getProperty(viewable);
  }

  @Override
  public int compare(E v1, E v2) {
    return getter_.compare(v1, v2);
  }

}
