package com.bensler.decaf.swing.action;

import java.util.LinkedList;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

class MenuItemCollector {

  private final LinkedList<Optional<JMenuItem>> items_;
          final BiConsumer<JComponent, Action> componentCollector_;

  MenuItemCollector() {
    this((_, _) -> {});
  }

  MenuItemCollector(BiConsumer<JComponent, Action> componentCollector) {
    items_ = new LinkedList<>();
    componentCollector_ = componentCollector;
  }

  void add(Action action, Optional<JMenuItem> menuItem) {
    menuItem.ifPresent(lMenuItem -> componentCollector_.accept(lMenuItem, action));
    add(menuItem);
  }

  void add(Optional<JMenuItem> menuItem) {
      // avoid subsequent null component pairs
    if (
      menuItem.isPresent()
      || (!items_.isEmpty() && !items_.getLast().isEmpty())
    ) {
      items_.add(menuItem);
    }
  }

  JMenuBar populateMenubar() {
    final JMenuBar menuBar = new JMenuBar();

    populateMenu(menuBar::add, () -> {});
    return menuBar;
  }

  JPopupMenu populatePopupMenu() {
    final JPopupMenu popupMenu = new JPopupMenu();

    populateMenu(popupMenu::add, popupMenu::addSeparator);
    return popupMenu;
  }

  void populateMenu(JMenu menu) {
    populateMenu(menu::add, menu::addSeparator);
  }

  private void populateMenu(Consumer<JMenuItem> menuAdder, Runnable separatorAdder) {
    while (!items_.isEmpty() && items_.getLast().isEmpty()) {
      items_.removeLast();
    }
    items_.forEach(item -> {
      if (item.isPresent()) {
        menuAdder.accept(item.get());
      } else {
        separatorAdder.run();
      }
    });
  }

  boolean isEmpty() {
    while (items_.getLast().isEmpty()) {
      items_.removeLast();
    }
    return items_.isEmpty();
  }

}