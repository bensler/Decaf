package com.bensler.decaf.swing.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.prefs.Preferences;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.bensler.decaf.swing.EntityComponent;
import com.bensler.decaf.swing.selection.EntitySelectionListener;
import com.bensler.decaf.swing.selection.EntitySelectionListener.Nop;
import com.bensler.decaf.swing.view.NoSelectionModel;
import com.bensler.decaf.swing.view.SelectionMode;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


/**
 */
public class EntityTable<E> extends Object
implements ListSelectionListener, FocusListener, EntityComponent<E> {

  private   final static  String              COL_KEY     = "cols";

  private   final static  String              SIZE_KEY    = "sizes";

  private   final static  String              SORT_KEY    = "sort";

  private   final         Set<FocusListener>  focusListeners_;

  protected final         JScrollPane         scrollPane_;

  protected final         TableComponent<E>   table_;

  private   final         TableModel<E>       model_;

  protected final         ColumnModel<E>      columnModel_;

  private   final         TableView<E>        view_;

  private   final         Map<PopupListener, PopupListenerWrapper> popupListeners_;

  private   final         List<E>             selection_;

  private   final         List<E>             savedSelection_;

  private                 EntitySelectionListener<E> selectionListener_;

//  private                 ActionImpl          customizeAction_;

  private                 boolean             sortable_;

  protected               Preferences         prefs_;

  private                 boolean             autoColumnResize_;

  private                 boolean             silentSelectionChange_;

  private                 SelectionMode       selectionMode_;

  private                 ListSelectionModel  defSelModel_;

  private                 boolean             enabled_;

  private                 Color               enabledBgColor_;

  private                 boolean             informFocusedBoManager_;

  /** May be null! If not null it is shown when the table empty. */
  private                 Background          emptyBackgroundComp_;

  public EntityTable(TableView<E> view) {
    this(view, null);
  }

  public EntityTable(TableView<E> view, Preferences prefs) {
    autoColumnResize_ = true;
    sortable_ = true;
    view_ = view;
    focusListeners_ = new HashSet<>();
    selection_ = new ArrayList<>(2);
    savedSelection_ = new ArrayList<>(2);
    model_ = new TableModel<>(view_, this);
    table_ = new TableComponent<>(this, model_, view_);
    table_.addFocusListener(this);
    columnModel_ = (ColumnModel)table_.getColumnModel();
    columnModel_.init();
    scrollPane_ = createScrollPane(table_);
    initCustAction();
    setSelectionListener(null);
    popupListeners_ = new HashMap<>(1);
    silentSelectionChange_ = false;
    scrollPane_.getViewport().setBackground(table_.getBackground());
    defSelModel_ = table_.getSelectionModel();
    setSelectionMode(SelectionMode.SINGLE);
    defSelModel_.addListSelectionListener(this);
    enabled_ = true;
    showDefaultVisibleColumns();
    setPreferences(prefs);
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

  public void setPreferences(Preferences prefs) {
    prefs_ = prefs;
    loadState();
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
    setData(new ArrayList<>());
  }

  public void setData(Collection<E> newData) {
    if (!new HashSet<>(newData).equals(new HashSet<>(getValues()))) {
      saveSelection();
      model_.setData(newData);
      dataSizeChanged();
    }
  }

  void saveColumnState() {
    autoColumnResize_ = false;
    if (prefs_ != null) {
//      PreferencesUtil.saveCollection(new PrefKey(prefs_, COL_KEY), columnModel_.getPropertyKeyList());
//      PreferencesUtil.saveCollection(new PrefKey(prefs_, SIZE_KEY), columnModel_.getSizeList());
    }
  }

  void saveSortState() {
    if (prefs_ != null) {
//      PreferencesUtil.saveCollection(new PrefKey(prefs_, SORT_KEY), table_.getSortPrefs());
    }
  }

  private void showDefaultVisibleColumns() {
//    setProperties(view_.getDefaultVisibleViews().toArray(new String[0]));
    table_.setSizesFromHeaderLabel();
  }

  /** Call setPreferences(Preferences) before! */
  public void loadState() {
    if (prefs_ != null) {
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
//      table_.loadSortPrefs(PreferencesUtil.loadStrings(new PrefKey(prefs_, SORT_KEY)));
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

  public SelectionMode getSelectionMode() {
    return selectionMode_;
  }

  public void setSelectionMode(SelectionMode mode) {
    if (selectionMode_ != mode) {
      setSelectionMode_(mode);
      selectionMode_ = mode;
    }
  }

  private void setSelectionMode_(SelectionMode mode) {
    if (selectionMode_ == SelectionMode.NONE) {
      table_.setSelectionModel(defSelModel_);
    }
    if (mode == SelectionMode.NONE) {
      table_.setSelectionModel(NoSelectionModel.createTableListModel());
    } else {
      table_.setSelectionModel(defSelModel_);
      table_.setSelectionMode(mode.getTableConstant());
    }
  }

  @Override
  public List<E> getSelection() {
    return table_.getSelectedValues();
  }

  @Override
  public E getSingleSelection() {
    final List<E> selection = table_.getSelectedValues();

    return ((selection.isEmpty() ? null : selection.get(0)));
  }

  public List<E> getValues() {
    return model_.getValues();
  }

  private void saveSelection() {
    savedSelection_.clear();
    savedSelection_.addAll(selection_);
  }

  private void applySavedSelection() {
    silentSelectionChange_ = true;

    try {
      select(savedSelection_);
    } finally {
      silentSelectionChange_ = false;
    }
  }

  /**
   * Refilters the entries in the table by the current filter.
   */
  public void fireRowFilterChanged() {
    saveSelection();
    model_.fireRowFilterChanged();
    applySavedSelection();
  }

  public void updateData(E subject) {
    saveSelection();
    model_.updateData(subject);
    applySavedSelection();
  }

  public void updateData(Collection<E> bos) {
    saveSelection();
    model_.updateData(bos);
    applySavedSelection();
  }

  public void addData(E subject) {
    saveSelection();
    model_.addData(subject);
    dataSizeChanged();
  }

  public void addData(Collection<E> bos) {
    saveSelection();
    model_.addData(bos);
    dataSizeChanged();
  }

  public void removeData(Object subject) {
    saveSelection();
    model_.removeData(subject);
    dataSizeChanged();
  }

  public void removeData(Collection<?> bos) {
    saveSelection();
    model_.removeData(bos);
    dataSizeChanged();
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
    selectionListener_ = ((listener != null) ? listener : new Nop<>());
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
  public void valueChanged(ListSelectionEvent evt) {
    if (!evt.getValueIsAdjusting()) {
      selection_.clear();
      selection_.addAll(getSelection());
      fireSelectionChanged();
    }
  }

  @Override
  public void select(Object subject) {
    select(Arrays.asList(new Object[] {subject}));
  }

  @Override
  public void select(Collection<E> subject) {
    table_.setSelectedValues(subject);
    fireSelectionChanged();
    scrollSelectionVisible();
  }

  /** Works only if there is one entry is selected */
  public void scrollSelectionVisible() {
    if (selection_.size() == 1) {
      scrollIndexVisible(model_.indexOf(selection_.get(0)));
    }
  }

  /** Works only if exactly one entry if selected. */
  protected void scrollIndexVisible(final int index) {
    table_.scrollRectToVisible(
      table_.getCellRect(index, 0, true)
    );
  }

  private void fireSelectionChanged() {
    if (!silentSelectionChange_ && enabled_) {
      selectionListener_.selectionChanged(this, new ArrayList<>(selection_));
    }
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

  List<E> setSelectionSilent() {
    silentSelectionChange_ = true;
    return new ArrayList<>(selection_);
  }

  void setSelectionUnsilent(List<E> selectionBefore) {
    if (!new HashSet<>(selectionBefore).equals(new HashSet<>(selection_))) {
      table_.setSelectedValues(selectionBefore);
      silentSelectionChange_ = false;
      fireSelectionChanged();
      table_.repaint();
    }
    silentSelectionChange_ = false;
  }

  /** Convenience wrapper for setVisibleRows(visibleRows,visibleRows);*/
  public void setVisibleRows(int visibleRows) {
    setVisibleRows(visibleRows, visibleRows);
  }

  /** Sets the row count for calc the preferred viewports size when
   * residing in a scrollpane.<p>
   * (min>=1)<=<=max (default: min:10,max:10). */
  public void setVisibleRows(int min, int max) {
    table_.setVisibleRows(min, max);
  }

  private void dataSizeChanged() {
    applySavedSelection();
    table_.invalidateIfNeeded();
    if (scrollPane_.getParent() != null) {
      updateBackground();
      ((JComponent)scrollPane_.getParent()).revalidate();
    }
  }

  public void sortByColumn(int column, boolean ascending) {
    table_.sortByColumn(column, ascending);
  }

  /** Set if this table should be sortable (default) or not by
   * clicking on the table headers. */
  public void setSortable(boolean flag) {
    if (sortable_ != flag) {
      sortable_ = flag;
      if (!sortable_) {
        model_.clearSorting();
        table_.getTableHeader().repaint();
      }
    }
  }

  /** @return if this table is sortable (default) or not by
   * clicking on the table headers. */
  public boolean isSortable() {
    return sortable_;
  }

  public void setSelectionBackground(Color color) {
    table_.setSelectionBackground(color);
  }

  @Override
  public void setBackground(Color background) {
  	table_.setBackground(background);
  }

  public void requestFocus() {
    table_.requestFocus();
  }

  public void setEnabled(boolean enabled) {
    if (enabled_ ^ enabled) {
      if (enabled) {
        scrollPane_.getViewport().setBackground(enabledBgColor_);
        table_.setBackground(enabledBgColor_);
        setSelectionMode_(selectionMode_);
      } else {
        enabledBgColor_ = table_.getBackground();
        scrollPane_.getViewport().setBackground(null);
        scrollPane_.setBackground(null);
        table_.setBackground(null);
        setSelectionMode_(SelectionMode.NONE);
      }
      enabled_ = enabled;
    }
  }

  @Override
  public boolean isEnabled() {
    return enabled_;
  }

  public E getValue(Point point) {
    return model_.getValueAt(table_.rowAtPoint(point));
  }

  @Override
  public void clearSelection() {
    table_.clearSelection();
  }

  @Override
  public boolean contains(Object entity) {
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

  @Override
  public void setToolTipText(String hint) {
    table_.setToolTipText(hint);
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

  public void setInformFocusedBoManager(boolean inform) {
    informFocusedBoManager_ = inform;
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

  public void setRowView(TableRowView<E> rowView) {
    table_.setRowView(rowView);
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
