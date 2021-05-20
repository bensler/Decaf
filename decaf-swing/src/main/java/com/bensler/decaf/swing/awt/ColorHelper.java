package com.bensler.decaf.swing.awt;

import java.awt.Color;

public final class ColorHelper {

  private ColorHelper() {}

  public static Color mix(Color c1, Color c2) {
    return mix(c1, 1, c2, 1);
  }
  
  public static Color mix(Color c1, int weight1, Color c2, int weight2) {
    final int weightSum = weight1 + weight2;
    
    return new Color(
      ((c1.getRed() * weight1) + (c2.getRed() * weight2)) / weightSum,
      ((c1.getGreen() * weight1) + (c2.getGreen() * weight2)) / weightSum,
      ((c1.getBlue() * weight1) + (c2.getBlue() * weight2)) / weightSum,
      ((c1.getAlpha() * weight1) + (c2.getAlpha() * weight2)) / weightSum
    );
  }

}
