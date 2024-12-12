package com.bensler.decaf.swing.awt;

import java.awt.Component;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.LinkedHashMap;

import javax.swing.Icon;


/** Icon implementing its paint() method by delegating it to a number
 * of other icons. If these icons have transparent regions some
 * overlay effect appears.
 */
public class OverlayIcon extends Object implements Icon {

  private final LinkedHashMap<Icon, Alignment2D> iconAlignmentMap_;
  private final Icon baseIcon_;

  private  int width_;
  private  int height_;

  public OverlayIcon(Icon baseIcon, Overlay... overlays) {
    iconAlignmentMap_ = new LinkedHashMap<>();
    baseIcon_ = baseIcon;
    clear();
    Arrays.stream(overlays).forEach(this::addOverlay);
  }

  public void addOverlay(Overlay overlay) {
    if (iconAlignmentMap_.containsKey(overlay.icon_)) {
      iconAlignmentMap_.remove(overlay.icon_);
    }
    iconAlignmentMap_.put(overlay.icon_, overlay.alignment_);
    width_ = -1;
    height_ = -1;
  }

  public void clear() {
    iconAlignmentMap_.clear();
    iconAlignmentMap_.put(baseIcon_, Alignment2D.C);
  }

  @Override
  public void paintIcon(Component c, Graphics g, int x, int y) {
    iconAlignmentMap_.entrySet().forEach(entry -> {
      final Icon icon = entry.getKey();
      final Alignment2D alignment = entry.getValue();

      icon.paintIcon(
        c, g,
        (x + alignment.alignHoriz(getIconWidth(), icon.getIconWidth())),
        (y + alignment.alignVert(getIconHeight(), icon.getIconHeight()))
      );
    });
  }

  @Override
  public int getIconWidth() {
    if (width_ < 0) {
      width_ = iconAlignmentMap_.keySet().stream().mapToInt(Icon::getIconWidth).max().orElse(0);
    }
    return width_;
  }

  @Override
  public int getIconHeight() {
    if (height_ < 0) {
      height_ = iconAlignmentMap_.keySet().stream().mapToInt(Icon::getIconHeight).max().orElse(0);
    }
    return height_;
  }

  private enum Alignment {

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

  public static class Overlay {

    final Icon icon_;
    final Alignment2D alignment_;

    public Overlay(Icon icon, Alignment2D alignment) {
      icon_ = icon;
      alignment_ = alignment;
    }

  }

  public enum Alignment2D {

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
