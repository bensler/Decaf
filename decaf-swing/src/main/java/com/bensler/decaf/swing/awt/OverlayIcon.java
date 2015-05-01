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

  private   final         LinkedHashMap<Icon, Alignment2D>   iconAlignmentMap_;
  private   final         Icon                             baseIcon_;

  private                 int             width_;
  private                 int             height_;

  public OverlayIcon(Icon baseIcon) {
    iconAlignmentMap_ = new LinkedHashMap<Icon, Alignment2D>();
    addIcon(baseIcon_ = baseIcon, Alignment2D.C);
  }

  public void addIcon(Icon icon, Alignment2D alignment) {
    if (iconAlignmentMap_.containsKey(icon)) {
      iconAlignmentMap_.remove(icon);
    }
    iconAlignmentMap_.put(icon, alignment);
    width_ = -1;
    height_ = -1;
  }

  public void clear() {
    iconAlignmentMap_.clear();
    addIcon(baseIcon_, Alignment2D.C);
  }

  @Override
  public void paintIcon(Component c, Graphics g, int x, int y) {
    for (Icon icon : iconAlignmentMap_.keySet()) {
      final Alignment2D alignment = iconAlignmentMap_.get(icon);

      icon.paintIcon(
        c, g,
        (x + alignment.alignHoriz(getIconWidth(), icon.getIconWidth())),
        (y + alignment.alignVert(getIconHeight(), icon.getIconHeight()))
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

  private static enum Alignment {

    LO(0),
    C (1),
    HI(2);

    private final int x_;

    private Alignment(int x) {
      x_ = x;
    }

    public int align(int whole, int part) {
      return Math.round(x_ * ((whole - part) / 2.0f));
    }

  }

  public static enum Alignment2D {

    N (Alignment.C,  Alignment.LO),
    NE(Alignment.HI, Alignment.LO),
    E (Alignment.HI, Alignment.C),
    SE(Alignment.HI, Alignment.HI),
    S (Alignment.C,  Alignment.HI),
    SW(Alignment.LO, Alignment.HI),
    W (Alignment.LO, Alignment.C),
    NW(Alignment.LO, Alignment.LO),

    C (Alignment.C,  Alignment.C);

    private final Alignment horiz_;
    private final Alignment vert_;

    private Alignment2D(Alignment horiz, Alignment vert) {
      horiz_ = horiz;
      vert_ = vert;
    }

    public int alignHoriz(int wholeWidth, int partWidth) {
      return horiz_.align(wholeWidth, partWidth);
    }

    public int alignVert(int wholeHeight, int partHeight) {
      return vert_.align(wholeHeight, partHeight);
    }

  }

}
