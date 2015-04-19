package com.bensler.decaf.swing.awt;

import java.awt.Component;
import java.awt.Graphics;
import java.util.LinkedHashMap;

import javax.swing.Icon;


/** Icon implementing its paint() method by delegating it to a number
 * of other icons. If these icons have transparent regions some
 * overlay effect appears.
 */
public class OverlayIcon extends Object implements Icon {

  private   final         LinkedHashMap<Icon, Alignment>   iconAlignmentMap_;

  private                 int             width_;

  private                 int             height_;

  public OverlayIcon() {
    iconAlignmentMap_ = new LinkedHashMap<Icon, Alignment>();
    width_ = -1;
    height_ = -1;
  }

  public void addIcon(Icon icon) {
    addIcon(icon, Alignment.C);
  }

  public void addIcon(Icon icon, Alignment alignment) {
    if (iconAlignmentMap_.containsKey(icon)) {
      iconAlignmentMap_.remove(icon);
    }
    iconAlignmentMap_.put(icon, alignment);
  }

  public void clear() {
    iconAlignmentMap_.clear();
  }

  @Override
  public void paintIcon(Component c, Graphics g, int x, int y) {
    for (Icon icon : iconAlignmentMap_.keySet()) {
      final Alignment alignment = iconAlignmentMap_.get(icon);

      icon.paintIcon(
        c, g,
        alignment.alignHoriz(x, getIconWidth(), icon.getIconWidth()),
        alignment.alignVert(y, getIconHeight(), icon.getIconHeight())
      );
    }
  }

  @Override
  public int getIconWidth() {
    if (width_ < 0) {
      width_ = 0;
      for (Icon icon : iconAlignmentMap_.keySet()) {
        width_ = Math.max(width_, icon.getIconWidth());
      }
    }
    return width_;
  }

  @Override
  public int getIconHeight() {
    if (height_ < 0) {
      height_ = 0;
      for (Icon icon : iconAlignmentMap_.keySet()) {
        height_ = Math.max(height_, icon.getIconHeight());
      }
    }
    return height_;
  }

  private static enum Orientation {

    LO, C, HI;

    public int align(
      int whole, int part
    ) {
      final int diff  = Math.max(0, (whole - part));

      if (this == C) {
        return diff / 2;
      } else {
        if (this == LO) {
          return 0;
        } else {
          // (this == HI) {
          return diff;
        }
      }
    }

  }

  public static enum Alignment {

    N (Orientation.C,  Orientation.LO),
    NE(Orientation.HI, Orientation.LO),
    E (Orientation.HI, Orientation.C),
    SE(Orientation.HI, Orientation.HI),
    S (Orientation.C,  Orientation.HI),
    SW(Orientation.LO, Orientation.HI),
    W (Orientation.LO, Orientation.C),
    NW(Orientation.LO, Orientation.LO),

    C (Orientation.C,  Orientation.C);

    private   final         Orientation   horiz_;

    private   final         Orientation   vert_;

    private Alignment(Orientation horiz, Orientation vert) {
      horiz_ = horiz;
      vert_ = vert;
    }

    public int alignHoriz(
      int base, int wholeWidth, int partWidth
    ) {
      return (base + horiz_.align(wholeWidth, partWidth));
    }

    public int alignVert(
      int base, int wholeHeight, int partHeight
    ) {
      return (base + vert_.align(wholeHeight, partHeight));
    }

  }

}
