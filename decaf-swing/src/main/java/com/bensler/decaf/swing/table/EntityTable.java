package com.bensler.decaf.swing.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;

import com.bensler.decaf.swing.EntityComponent;
import com.bensler.decaf.swing.selection.EntitySelectionListener;
import com.bensler.decaf.swing.selection.SelectionMode;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


/**
 */
public class EntityTable<E> extends Object implements FocusListener, EntityComponent<E> {

  private   final         Set<FocusListener>  focusListeners_;

  protected final         JScrollPane         scrollPane_;

  protected final         TableComponent<E>   table_;

  private   final         TableModel<E>       model_;

  protected final         ColumnModel<E>      columnModel_;

  private   final         TableView<E>        view_;

  private   final         Map<PopupListener, PopupListenerWrapper> popupListeners_;

  //  private                 ActionImpl          customizeAction_;

  private                 boolean             autoColumnResize_;

  private                 Color               enabledBgColor_;

  /** May be null! If not null it is shown when the table empty. */
  private                 Background          emptyBackgroundComp_;

  public EntityTable(TableView<E> view) {
    autoColumnResize_ = true;
    view_ = view;
    focusListeners_ = new HashSet<>();
    model_ = new TableModel<>(view_);
    table_ = new TableComponent<>(this, model_, view_);
    table_.addFocusListener(this);
    columnModel_ = (ColumnModel)table_.getColumnModel();
    columnModel_.init();
    scrollPane_ = createScrollPane(table_);
    initCustAction();
    popupListeners_ = new HashMap<>(1);
    scrollPane_.getViewport().setBackground(table_.getBackground());
//    defSelModel_ = table_.getSelectionModel();
    setSelectionMode(SelectionMode.SINGLE);
//    defSelModel_.addListSelectionListener(this);
    showDefaultVisibleColumns();
  }

  protected JScrollPane createScrollPane(TableComponent tableComponent) {
    return new JScrollPane(tableComponent);
  }

  private void initCustAction() {
//    final JButton customizeButton;
//
//    customizeAction_ = new ActionImpl(
//      GeneralKeys.CUSTOMIZE, null, BinResKeys.CUSTOMIZE, null
//    );
//    customizeAction_.setDialogAction();
//    customizeAction_.enable(this);
//    customizeButton = new JButton(customizeAction_);
//    customizeButton.setText("");
//    customizeButton.setToolTipText(customizeAction_.getName());
//    customizeButton.setFocusable(false);
////    customizeButton.setBorder(null);
//    scrollPane_.setCorner(JScrollPane.UPPER_RIGHT_CORNER, customizeButton);
  }

  /** @return the JTable component wrapped by a JScrollpane
   *  @see com.bensler.flob.gui.EntityComponent#getComponent()
   */
  @Override
  public TableComponent<E> getComponent() {
    return table_;
  }

  @Override
  public JScrollPane getScrollPane() {
    return scrollPane_;
  }

  TableView<E> getView() {
    return view_;
  }

  public void setCustomizeButtonAlwaysVisible(boolean flag) {
    scrollPane_.setVerticalScrollBarPolicy(
      flag ? JScrollPane.VERTICAL_SCROLLBAR_ALWAYS : JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
    );
  }

//  public void doAction(ActionEx action) throws CanceledException {
//    if (action == customizeAction_) {
//      customize(columnModel_.getShownProperties());
//    }
//  }
//
//  protected PropertyDialog createPropertyDialog(Collection<? extends Named> properties) {
//    return new PropertyDialog(properties);
//  }
//
//  protected void customize(Object dlgData) throws UserCanceledException {
//    final PropertyDialog dlg = createPropertyDialog(getView().getViews());
//
//    dlg.setTitle(Client.getRes().getString(GeneralKeys.CONFIG_COLUMNS));
//    columnModel_.setShownProperties(
//      new HashSet((Collection)dlg.show(dlgData))
//    );
//    table_.setResizingState();
//    saveColumnState();
//  }

  public void clear() {
    removeData(getValues());
  }

  private void showDefaultVisibleColumns() {
//    setProperties(view_.getDefaultVisibleViews().toArray(new String[0]));
    table_.setSizesFromHeaderLabel();
  }

  /** Call setPreferences(Preferences) before! */
  public void loadState() {
    {
//      final List<TablePropertyView> views = setProperties(PreferencesUtil.loadStrings(new PrefKey(prefs_, COL_KEY)));
//      final String[] sizeStrings = PreferencesUtil.loadStrings(new PrefKey(prefs_, SIZE_KEY));
//
//      if ((sizeStrings.length > 0) && (sizeStrings.length == views.size())) {
//        try {
//          setSizes(sizeStrings);
//          autoColumnResize_ = false;
//        } catch (NumberFormatException nfe) {}
//      }
//      if (autoColumnResize_) {
//        table_.setSizesFromHeaderLabel();
//      }
//      if (isSortable()) {
//        table_.loadSortPrefs(PreferencesUtil.loadStrings(new PrefKey(prefs_, SORT_KEY)));
//      }
    }
  }

  public void setSizesFromData() {
    if (autoColumnResize_) {
      table_.setSizesFromData();
    }
  }

  private void setSizes(String[] sizeStrings) {
    final int[] sizes   = new int[sizeStrings.length];

    for (int i = 0; i < sizeStrings.length; i++) {
      sizes[i] = Integer.parseInt(sizeStrings[i].trim());
      if (sizes[i] < 1) {
        throw new NumberFormatException("size must be positive");
      }
    }
    columnModel_.setPrefSizes(sizes);
  }

//  private List<TablePropertyView> setProperties(String[] propNames) {
//    final List<TablePropertyView>   views = new ArrayList<TablePropertyView>(propNames.length);
//
//    for (int i = 0; i < propNames.length; i++) {
//      final TablePropertyView view = view_.resolve(
//        new StaticRef(TablePropertyView.class, propNames[i])
//      );
//
//      if (view != null) {
//        views.add(view);
//      }
//    }
//    columnModel_.setProperties(views);
//    return views;
//  }

  public void setSelectionMode(SelectionMode mode) {
    table_.selectionCtrl_.setSelectionMode(mode);
  }

  @Override
  public List<E> getSelection() {
    return table_.selectionCtrl_.getSelection();
  }

  @Override
  public E getSingleSelection() {
    final List<E> selection = table_.getSelectedValues();

    return ((selection.isEmpty() ? null : selection.get(0)));
  }

  public List<E> getValues() {
    return model_.getValues();
  }

  public void addOrUpdateData(Collection<E> entities) {
    try (var s = table_.selectionCtrl_.new SelectionKeeper()) {
      model_.addOrUpdateData(entities);
    }
  }

  public void removeData(Collection<?> entities) {
    try (var s = table_.selectionCtrl_.new SelectionKeeper()) {
      model_.removeData(entities);
    }
  }

  public void showGrid(boolean horizontal, boolean vertikal) {
    final Dimension spacing = table_.getIntercellSpacing();

    table_.setShowHorizontalLines(horizontal);
    table_.setShowVerticalLines(vertikal);
    table_.setIntercellSpacing(new Dimension(
      (vertikal ? spacing.width : 0),
      (horizontal ? spacing.height : 0)
    ));
  }

  @Override
  public void setSelectionListener(EntitySelectionListener<E> listener) {
    table_.selectionCtrl_.addSelectionListener(listener);
  }

  public void addPopupListener(PopupListener listener) {
    addPopupListener(listener, false);
  }

  public void addPopupListener(PopupListener listener, boolean includeViewport) {
    if (listener != null) {
      popupListeners_.put(listener, new PopupListenerWrapper(listener, includeViewport));
    }
  }

  public void removePopupListener(PopupListener listener) {
    popupListeners_.remove(listener);
  }

  @Override
  public void select(Object subject) {
    select(List.of(subject));
  }

  @Override
  public void select(Collection<E> subject) {
    throw new UnsupportedOperationException();    // TODO
//    table_.setSelectedValues(subject);
//    fireSelectionChanged();
//    scrollSelectionVisible();
  }

  /** Works only if there is one entry is selected */
  public void scrollSelectionVisible() {
    throw new UnsupportedOperationException();    // TODO
//    if (selection_.size() == 1) {
//      scrollIndexVisible(model_.indexOf(selection_.get(0)));
//    }
  }

  /** Works only if exactly one entry if selected. */
  protected void scrollIndexVisible(final int index) {
    table_.scrollRectToVisible(
      table_.getCellRect(index, 0, true)
    );
  }

  public void doubleclickTriggered(MouseEvent evt) {
    if (scrollPane_.getViewport().getView() == table_) {
      if (evt.getSource() == table_) {
//        final ActionImpl action = createContextActionGroup().getPrimaryAction();
//
//        if (action != null) {
//          action.actionPerformed(null);
//        }
      }
    }
  }

  public static interface PopupListener {

    public void popupTriggered(EntityTable source, JTable table, MouseEvent evt);

  }

  private final static class PopupListenerWrapper extends Object {

    private   final         PopupListener   listener_;

    private   final         boolean         includeViewport_;

    private PopupListenerWrapper(
      PopupListener listener, boolean includeViewport
    ) {
      listener_ = listener;
      includeViewport_ = includeViewport;
    }

  }

  public void setSelectionBackground(Color color) {
    table_.setSelectionBackground(color);
  }

  public void setBackground(Color color) {
  	table_.setBackground(color);
    // in case the tree is smaller than the scrollpane
    scrollPane_.getViewport().setBackground(color);
  }

  public void requestFocus() {
    table_.requestFocus();
  }

  public E getValue(Point point) {
    return model_.getValueAt(table_.rowAtPoint(point));
  }

  @Override
  public void clearSelection() {
    table_.clearSelection();
  }

  @Override
  public Optional<E> contains(Object entity) {
    return model_.contains(entity);
  }

  public boolean isEmpty() {
    return (model_.getRowCount() == 0);
  }

  /** Selects the first item of this table or does nothing
   * if it is empty.
   */
  public void selectFirstItem() {
    if (!isEmpty()) {
      select(model_.getValueAt(0));
    }
  }

  public int getIndexOf(Object object) {
    return model_.indexOf(object);
  }

  ColumnModel<E> getColumnModel() {
    return columnModel_;
  }

  public int getRowCount() {
    return model_.getRowCount();
  }

  public void setEmptyBackgroundComp(JComponent emptyBackgroundComp) {
    emptyBackgroundComp_ = ((emptyBackgroundComp == null) ? null : new Background(emptyBackgroundComp));
    updateBackground();
  }

  private void updateBackground() {
    final JViewport vp      = scrollPane_.getViewport();
    final Component oldView = vp.getView();

    if (emptyBackgroundComp_ != null) {
      if ((model_.getRowCount() < 1) && (oldView != emptyBackgroundComp_)) {
        vp.setView(emptyBackgroundComp_);
        vp.setBackground(emptyBackgroundComp_.getBackground());
      }
    }
    if ((model_.getRowCount() > 0) && (oldView != table_)) {
      vp.setView(table_);
      vp.setBackground(table_.getBackground());
    }
  }

  private final class Background extends JPanel {

    private Background(JComponent comp) {
      super(new FormLayout("c:p:g", "c:p:g"));
      add(comp, new CellConstraints(1, 1));
    }

    @Override
    public Dimension getPreferredSize() {
      return super.getPreferredSize();
    }

  }

  @Override
  public void focusGained(FocusEvent e) {
    fireFocusGained();
    table_.repaintSelection();
  }

  @Override
  public void focusLost(FocusEvent e) { }

  public void focusLost() {
    table_.repaintSelection();
  }

  private void fireFocusGained() {
    for (FocusListener listener : focusListeners_) {
      listener.focusGained(this);
    }
  }

  public void addFocusListener(FocusListener listener) {
    focusListeners_.add(listener);
  }

  public void removeFocusListener(FocusListener listener) {
    focusListeners_.remove(listener);
  }

}
