package com.bensler.decaf.swing.dialog;

import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class WindowSizePersister {

  public WindowSizePersister() {
    // TODO Auto-generated constructor stub
  }

  public void listenTo(Window window, String name) {
    window.addComponentListener(new WindowListenerContext(name));
  }

  public void listenTo(Window window) {
    listenTo(window, window.getClass().getSimpleName());
  }

  static class WindowListenerContext implements ComponentListener {

    private final String name_;

    public WindowListenerContext(String name) {
      name_ = name;
    }

    @Override
    public void componentResized(ComponentEvent e) {
      System.out.println(name_ + " " + e.getComponent().getBounds());
    }

    @Override
    public void componentMoved(ComponentEvent e) {
      System.out.println(name_ + " " + e.getComponent().getBounds());
    }

    @Override
    public void componentShown(ComponentEvent e) { }

    @Override
    public void componentHidden(ComponentEvent e) { }
  }

}
