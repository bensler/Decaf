package com.bensler.decaf.swing.view;

import java.text.Collator;
import java.util.Comparator;


public final class CollatorComparator extends Object implements Comparator<String> {

  private final Comparator<Object>  collator_;

  public CollatorComparator() {
    collator_ = Collator.getInstance();
  }

  public int compare(String p1, String p2) {
    return collator_.compare(p1, p2);
  }

}