package com.bensler.decaf.swing.awt;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;
import java.util.function.Consumer;

public class SimpleMouseAdapter extends MouseAdapter {

  private final static Consumer<MouseEvent> NOOP_CONSUMER = _ -> {};

  public static SimpleMouseAdapter pressed(Consumer<MouseEvent> listener) {
    return new SimpleMouseAdapter(listener, null, null);
  }

  public static SimpleMouseAdapter released(Consumer<MouseEvent> listener) {
    return new SimpleMouseAdapter(null, listener, null);
  }

  public static SimpleMouseAdapter clicked(Consumer<MouseEvent> listener) {
    return new SimpleMouseAdapter(null, null, listener);
  }

  public static SimpleMouseAdapter doubleClicked(Consumer<MouseEvent> listener) {
    return new SimpleMouseAdapter(null, null, evt -> {
      if (
        (evt.getButton() == MouseEvent.BUTTON1)
        && (evt.getClickCount() == 2)
      ) {
        listener.accept(evt);
      }
    });
  }

  private final Consumer<MouseEvent> pressedConsumer_;
  private final Consumer<MouseEvent> releasedConsumer_;
  private final Consumer<MouseEvent> clickedConsumer_;

  public SimpleMouseAdapter(
    Consumer<MouseEvent> pressedConsumer,
    Consumer<MouseEvent> releasedConsumer,
    Consumer<MouseEvent> clickedConsumer
  ) {
    pressedConsumer_ = Optional.ofNullable(pressedConsumer).orElse(NOOP_CONSUMER);
    releasedConsumer_ = Optional.ofNullable(releasedConsumer).orElse(NOOP_CONSUMER);
    clickedConsumer_ = Optional.ofNullable(clickedConsumer).orElse(NOOP_CONSUMER);
  }

  @Override
  public void mouseClicked(MouseEvent evt) {
    clickedConsumer_.accept(evt);
  }

  @Override
  public void mousePressed(MouseEvent evt) {
    pressedConsumer_.accept(evt);
  }

  @Override
  public void mouseReleased(MouseEvent evt) {
    releasedConsumer_.accept(evt);
  }

}
