package com.bensler.decaf.swing.view;

import static com.bensler.decaf.util.cmp.CollatorComparator.COLLATOR_COMPARATOR;

import java.awt.Component;
import java.util.Optional;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

import com.bensler.decaf.util.Named;


public class PropertyViewImpl<E, P> extends Object implements PropertyView<E, P> {

  public final static  PropertyViewImpl<Object, String> OBJECT = new PropertyViewImpl<>(
    new SimplePropertyGetter<>(entity -> entity.toString(), COLLATOR_COMPARATOR)
  );

  public    final static  PropertyViewImpl<Named, String> NAMED = new PropertyViewImpl<>(
    new SimplePropertyGetter<>(Named::getName, COLLATOR_COMPARATOR)
  );

  private   final         RenderComponentFactory  compFactory_;

  private   final         CellRenderer<E, P>      renderer_;

  private   final         PropertyGetter<E, P>    getter_;

  private   final         NullPolicy<E>           nullPolicy_;

  public PropertyViewImpl(
    PropertyGetter<E, P> getter
  ) {
    this(new SimpleCellRenderer<>(), getter);
  }

  public PropertyViewImpl(
    Icon icon, PropertyGetter<E, P> getter
  ) {
    this(new SimpleCellRenderer<>(icon), getter);
  }

  public PropertyViewImpl(
    CellRenderer<E, P> cellRenderer, PropertyGetter<E, P> propertyGetter
  ) {
    this(cellRenderer, propertyGetter, RenderComponentFactory.DEFAULT_INSTANCE);
  }

  public PropertyViewImpl(
    CellRenderer<E, P> cellRenderer,
    PropertyGetter<E, P> propertyGetter, RenderComponentFactory componentFactory
  ) {
    renderer_ = cellRenderer;
    getter_ = propertyGetter;
    compFactory_ = componentFactory;
    nullPolicy_ = new DefaultNullPolicy<>();
  }

  @Override
  public TreeCellRenderer createTreeCellRenderer() {
    return new PropertyTreeCellRenderer();
  }

  class PropertyTreeCellRenderer implements TreeCellRenderer {

    private final TreeRenderComponent treeRenderComponent_;

    PropertyTreeCellRenderer() {
      treeRenderComponent_ = compFactory_.createTreeComponent();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Component getTreeCellRendererComponent(
      JTree tree, Object value, boolean selected,
      boolean expanded, boolean leaf, int row, boolean hasFocus
    ) {
      final JLabel label = treeRenderComponent_.prepareForTree(tree, selected, expanded, leaf, row, hasFocus);

      nullPolicy_.render((E)value, label, renderer_, getter_);
      return label;
    }
  }

  @Override
  public Component getCellRendererComponent(
    JTable table, E viewable, P cellValue, boolean selected,
    boolean hasFocus, int row, int column
  ) {
    final RenderComponent tableComponent = compFactory_.getListTableComponent();
    final JLabel label = tableComponent.prepareForTable(table, selected, row, column, hasFocus);

    renderer_.render(viewable, cellValue, label);
    return label;
  }

  @Override
  public Component getListCellRendererComponent(
    JList<? extends E> list, E value, int index,
    boolean selected, boolean hasFocus
  ) {
    final RenderComponent   listComponent   = compFactory_.getListTableComponent();
    final JLabel            label = listComponent.prepareForList(list, selected, index, hasFocus);
    nullPolicy_.render(value, label, renderer_, getter_);
    return label;
  }

  @Override
  public String getPropertyString(E entity) {
    return Optional.of(getProperty(entity)).map(P::toString).orElse("");
  }

  @Override
  public JLabel renderLabel(JLabel label, E viewable) {
    nullPolicy_.render(viewable, label, renderer_, getter_);
    return label;
  }

  @Override
  public P getProperty(E viewable) {
    return getter_.getProperty(viewable);
  }

  @Override
  public int compare(E v1, E v2) {
    return getter_.compare(v1, v2);
  }

  @Override
  public RenderComponentFactory getRenderComponentFactory() {
    return compFactory_;
  }

}
