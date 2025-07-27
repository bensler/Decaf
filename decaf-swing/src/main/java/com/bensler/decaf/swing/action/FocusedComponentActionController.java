package com.bensler.decaf.swing.action;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.bensler.decaf.swing.EntityComponent;
import com.bensler.decaf.swing.EntityComponent.FocusListener;
import com.bensler.decaf.swing.selection.EntitySelectionListener;
import com.bensler.decaf.swing.selection.SelectionHolder;
import com.bensler.decaf.util.Pair;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class FocusedComponentActionController implements FocusListener, EntitySelectionListener<Object> {

  private final List<EntityComponent<?>> entityComponents_;
  private final ActionGroup actions_;
  private final List<Pair<JComponent, Action>> toolbarComponents_;

  private List<?> currentSelection_;
  private EntityComponent<?> focusedComp_;

  public FocusedComponentActionController(ActionGroup actions, Collection<EntityComponent<?>> components) {
    entityComponents_ = List.copyOf(components);
    actions_ = actions;
    toolbarComponents_ = new ArrayList<>();
    entityComponents_.forEach(comp -> comp.addFocusListener(this));
    entityComponents_.forEach(comp -> ((EntityComponent<Object>)comp).addSelectionListener(this));
    focusGained(entityComponents_.iterator().next());
  }

  public void triggerPrimaryAction() {
    computeStates().getPrimaryAction().ifPresent(action -> action.doAction(focusedComp_, currentSelection_));
  }

  public void showPopupMenu(MouseEvent evt) {
    final ActionStateMap states = computeStates();

    if (states.getState(actions_) != ActionState.HIDDEN) {
      final JPopupMenu menu = new JPopupMenu();
      actions_.createPopupmenuItem(menu::add, focusedComp_, currentSelection_, states);
      menu.show(focusedComp_.getComponent(), evt.getX(), evt.getY());
    }
  }

  private ActionStateMap computeStates() {
    final ActionStateMap states = new ActionStateMap();

    actions_.computeState(currentSelection_, states);
    return states;
  }

  @Override
  public void selectionChanged(SelectionHolder<Object> selectionSource, List<Object> selection) {
    if (selectionSource == focusedComp_) {
      reevaluate(selection);
    }
  }

  @Override
  public void focusGained(EntityComponent<?> component) {
    if (entityComponents_.contains(component)) {
      reevaluate((focusedComp_ = component).getSelection());
    }
  }

  private void reevaluate(List<?> newSelection) {
    if (!newSelection.equals(currentSelection_)) {
      currentSelection_ = List.copyOf(newSelection);
      reevaluate();
    }
  }

  private void reevaluate() {
    final ActionStateMap states = computeStates();

    toolbarComponents_.forEach(pair -> states.getState(pair.getRight()).applyTo(pair.getLeft()));
  }

  public JComponent createToolbar() {
    final ToolbarComponentCollector toolbarComponents = new ToolbarComponentCollector();
    final JPanel toolbar;

    actions_.createToolbarComponent(toolbarComponents, () -> focusedComp_, () -> currentSelection_);
    toolbarComponents.addTo(toolbar = new JPanel(new FormLayout(toolbarComponents.getColumnSpec(), "f:p")));
    reevaluate();
    return toolbar;
  }

  class ToolbarComponentCollector {
    private final LinkedList<Pair<JComponent, Action>> components = new LinkedList<>();

    public void add(Pair<JComponent, Action> pair) {
      // avoid subsequent null component pairs
      if (components.isEmpty() || (pair.getLeft() != null) || (components.getLast().getLeft() != null)) {
        components.add(pair);
      }
    }

    public void addTo(JPanel toolbar) {
      while ((!components.isEmpty() && (components.getFirst().getLeft() == null))) {
        components.removeFirst();
      }
      while ((!components.isEmpty() && (components.getLast().getLeft() == null))) {
        components.removeLast();
      }
      IntStream.range(0, components.size()).forEach(i -> addComponent(toolbar, components.get(i), i));
    }

    private void addComponent(JPanel toolbar, Pair<JComponent, Action> pair, int index) {
      final JComponent component = pair.getLeft();

      if (component != null) {
        toolbarComponents_.add(pair);
        toolbar.add(component, new CellConstraints((2 * index) + 1, 1));
      }
    }

    public String getColumnSpec() {
      // "[f:p, 3dlu,f:p]*,0dlu:g"
      return IntStream.range(0, components.size()).mapToObj(i -> "f:p").collect(Collectors.joining(",4dlu,", "", ",0dlu:g"));
    }
  }

  public <E extends EntityComponent<?>> void attachTo(E target, Consumer<E> initializer, Consumer<MouseEvent> onCtxMenuOpen) {
    final JComponent comp = target.getComponent();

    comp.addMouseListener(new ContextMenuMouseAdapter(evt -> triggerContextMenu(evt, onCtxMenuOpen)));
    comp.addMouseListener(new DoubleClickMouseAdapter(evt -> triggerPrimaryAction()));
    initializer.accept(target);
  }

  void triggerContextMenu(MouseEvent evt, Consumer<MouseEvent> onCtxMenuOpen) {
    onCtxMenuOpen.accept(evt);
    showPopupMenu(evt);
  }

}
