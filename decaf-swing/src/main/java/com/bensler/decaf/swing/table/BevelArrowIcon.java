package com.bensler.decaf.swing.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.UIManager;


/**
 */
public class BevelArrowIcon implements Icon {

  private   final         Color                 edge1_;
  private   final         Color                 edge2_;
  private   final         Color                 fill_;
  private   final         int                   size_;
  private   final         Sorting               sorting_;

  BevelArrowIcon(
    int size, Sorting sorting, boolean isPressedView
  ) {
    if (isPressedView) {
      edge1_ = UIManager.getColor("controlDkShadow");
      edge2_ = UIManager.getColor("controlLtHighlight");
      fill_ = UIManager.getColor("controlShadow");
    } else {
      edge1_ = UIManager.getColor("controlShadow");
      edge2_ = UIManager.getColor("controlHighlight");
      fill_ = UIManager.getColor("control");
    }
    size_ = size;
    sorting_= sorting;
  }

  @Override
  public void paintIcon(Component c, Graphics g, int x, int y) {
    if (sorting_ == Sorting.DESCENDING) {
      drawDownArrow(g, x, y);
    } else {
      if (sorting_ == Sorting.ASCENDING) {
        drawUpArrow(g, x, y);
      }
    }
  }

  @Override
  public int getIconWidth() {
    return size_;
  }

  @Override
  public int getIconHeight() {
    return size_;
  }

  private void drawDownArrow(Graphics g, int xo, int yo) {
    int x   = xo + 1;
    int y   = yo + 2;
    int dx  = size_ - 6;

    g.setColor(edge1_);
    g.drawLine(xo, yo, xo + size_ - 1, yo);
    g.drawLine(xo, yo+1, xo + size_ - 3, yo + 1);
    g.setColor(edge2_);
    g.drawLine(xo + size_ - 2, yo + 1, xo + size_ - 1, yo + 1);
    while (y + 1 < yo + size_) {
      g.setColor(edge1_);
      g.drawLine(x, y, x + 1, y);
      g.drawLine(x, y + 1, x + 1, y + 1);
      if (0 < dx) {
        g.setColor(fill_);
        g.drawLine(x + 2, y, x + 1 + dx, y);
        g.drawLine(x + 2, y + 1, x + 1 + dx, y + 1);
      }
      g.setColor(edge2_);
      g.drawLine(x + dx + 2, y, x + dx + 3, y);
      g.drawLine(x + dx + 2, y + 1, x + dx + 3, y + 1);
      x += 1;
      y += 2;
      dx -= 2;
    }
    g.setColor(edge1_);
    g.drawLine(xo + (size_ / 2), yo + size_ - 1, xo + (size_ / 2), yo + size_ - 1);
  }

  private void drawUpArrow(Graphics g, int xo, int yo) {
    int x   = xo + (size_ / 2);
    int y   = yo + 1;
    int dx  = 0;

    g.setColor(edge1_);
    g.drawLine(x, yo, x, yo);
    x--;
    while (y + 3 < yo + size_) {
      g.setColor(edge1_);
      g.drawLine(x, y, x + 1, y);
      g.drawLine(x, y + 1, x + 1, y + 1);
      if (0 < dx) {
        g.setColor(fill_);
        g.drawLine(x + 2, y, x + 1 + dx, y);
        g.drawLine(x + 2, y + 1, x + 1 + dx, y + 1);
      }
      g.setColor(edge2_);
      g.drawLine(x + dx + 2, y, x + dx + 3, y);
      g.drawLine(x + dx + 2, y + 1, x + dx + 3, y + 1);
      x -= 1;
      y += 2;
      dx += 2;
    }
    g.setColor(edge1_);
    g.drawLine(xo, yo + size_ - 3, xo + 1, yo + size_ - 3);
    g.setColor(edge2_);
    g.drawLine(xo + 2, yo + size_ - 2, xo + size_ - 1, yo + size_ - 2);
    g.drawLine(xo, yo + size_ - 1, xo + size_, yo + size_ - 1);
  }

}
