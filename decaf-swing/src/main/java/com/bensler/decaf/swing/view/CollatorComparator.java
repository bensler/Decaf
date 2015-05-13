package com.bensler.decaf.swing.view;

import java.text.Collator;
import java.util.Comparator;


public final class CollatorComparator extends Object implements Comparator<String> {

  private                 Comparator<Object>  collator_;

  public int compare(String p1, String p2) {
    if (collator_ == null) {
      collator_ = Collator.getInstance();
    }
    return collator_.compare(p1, p2);
  }

}