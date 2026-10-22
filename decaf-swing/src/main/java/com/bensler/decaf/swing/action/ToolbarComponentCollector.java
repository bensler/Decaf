package com.bensler.decaf.swing.action;

import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.bensler.decaf.util.Pair;
import com.jgoodies.forms.layout.CellConstraints;

class ToolbarComponentCollector {

  private final LinkedList<Pair<JComponent, Action>> components;
  private final BiConsumer<JComponent, Action> componentCollector_;

  ToolbarComponentCollector(BiConsumer<JComponent, Action> componentCollector) {
    components = new LinkedList<>();
    componentCollector_ = componentCollector;
  }

  public void add(Pair<JComponent, Action> pair) {
    // avoid subsequent null component pairs
    if (components.isEmpty() || (pair.getLeft() != null) || (components.getLast().getLeft() != null)) {
      components.add(pair);
    }
  }

  public JPanel addToToolbar(JPanel toolbar) {
    while ((!components.isEmpty() && (components.getFirst().getLeft() == null))) {
      components.removeFirst();
    }
    while ((!components.isEmpty() && (components.getLast().getLeft() == null))) {
      components.removeLast();
    }
    IntStream.range(0, components.size()).forEach(i -> addComponent(toolbar, components.get(i), i));
    return toolbar;
  }

  private void addComponent(JPanel toolbar, Pair<JComponent, Action> pair, int index) {
    final JComponent component = pair.getLeft();

    if (component != null) {
      componentCollector_.accept(component, pair.getRight());
      toolbar.add(component, new CellConstraints((2 * index) + 1, 1));
    }
  }

  public String getColumnSpec() {
    // "[f:p, 3dlu,f:p]*,0dlu:g"
    return IntStream.range(0, components.size()).mapToObj(_ -> "f:p").collect(Collectors.joining(",4dlu,", "", ",0dlu:g"));
  }
}