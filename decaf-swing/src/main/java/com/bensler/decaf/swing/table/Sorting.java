package com.bensler.decaf.swing.table;

import java.util.Map;

/** Represents a sort direction: {@link #ASCENDING}, {@link #DESCENDING}.
 */
public enum Sorting {

  ASCENDING("ascending", 1),
  DESCENDING("descending", -1);

  public final static Map<Sorting, Sorting> OPPOSITES = Map.of(
    ASCENDING,  DESCENDING,
    DESCENDING, ASCENDING
  );

  private final         String    id_;
  private final         int       factor_;

  Sorting(String key, int factor) {
    id_ = key;
    factor_ = factor;
  }

   public String getId() {
    return id_;
  }

  public int getFactor() {
    return factor_;
  }

  public Sorting getOpposite() {
    return OPPOSITES.get(this);
  }

}