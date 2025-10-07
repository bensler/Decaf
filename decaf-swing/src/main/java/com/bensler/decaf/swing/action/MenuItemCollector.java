package com.bensler.decaf.swing.action;

import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class MenuItemCollector {

  private final LinkedList<Optional<JMenuItem>> items_;

  public MenuItemCollector() {
    items_ = new LinkedList<>();
  }

  public void add(Optional<JMenuItem> menuItem) {
    // avoid subsequent null component pairs
    if (
      menuItem.isPresent()
      || (!items_.isEmpty() && !items_.getLast().isEmpty())
    ) {
      items_.add(menuItem);
    }
  }

  public void populateMenu(JPopupMenu menu) {
    populateMenu(menu::add, menu::addSeparator);
  }

  public void populateMenu(JMenu menu) {
    populateMenu(menu::add, menu::addSeparator);
  }

  public void populateMenu(Consumer<JMenuItem> menuAdder, Runnable separatorAdder) {
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

  public boolean isEmpty() {
    while (items_.getLast().isEmpty()) {
      items_.removeLast();
    }
    return items_.isEmpty();
  }

}