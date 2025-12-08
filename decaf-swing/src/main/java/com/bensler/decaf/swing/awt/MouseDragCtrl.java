package com.bensler.decaf.swing.awt;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;
import java.util.function.BiConsumer;

public class MouseDragCtrl extends MouseAdapter {

  private final BiConsumer<Point, Point> dragListener_;
  private final BiConsumer<Point, Point> draggedListener_;
  private Optional<Point> origin_;

  public MouseDragCtrl(Component target, BiConsumer<Point, Point> dragListener, BiConsumer<Point, Point> draggedListener) {
    dragListener_ = dragListener;
    draggedListener_ = draggedListener;
    origin_ = Optional.empty();
    target.addMouseListener(this);
    target.addMouseMotionListener(this);
  }

  @Override
  public void mousePressed(MouseEvent evt) {
    origin_ = Optional.of(evt.getPoint());
  }

  @Override
  public void mouseReleased(MouseEvent evt) {
    origin_.ifPresent(origin -> draggedListener_.accept(origin, evt.getPoint()));
    origin_ = Optional.empty();
  }

  @Override
  public void mouseDragged(MouseEvent evt) {
    origin_.ifPresent(origin -> dragListener_.accept(origin, evt.getPoint()));
  }

}