package com.bensler.decaf.swing.table;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

/** Represents a sort direction: {@link #ASCENDING}, {@link #DESCENDING} or {@link #NONE} .
 */
public enum Sorting {

  ASCENDING("ascending", true, 1),
  DESCENDING("descending", false, -1),
  NONE("none", false, 0);

  public final static Map<Sorting, Sorting> OPPOSITES = ImmutableMap.of(
    ASCENDING,  DESCENDING,
    DESCENDING, ASCENDING,
    NONE,       NONE
  );

  private final         String    id_;
  private final         boolean   ascending_;
  private final         int       factor_;

  Sorting(String key, boolean ascending, int factor) {
    id_ = key;
    ascending_ = ascending;
    factor_ = factor;
  }

   public String getId() {
    return id_;
  }

  public int getFactor() {
    return factor_;
  }

  public boolean isAscending() {
    return ascending_;
  }

  public Sorting getOpposite() {
    return OPPOSITES.get(this);
  }

}