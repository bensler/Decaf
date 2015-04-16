/**
 *
 */
package com.bensler.decaf.swing.view;

import java.util.Comparator;


public final class CollatorComparator extends Object implements Comparator<Object> {

  private                 Comparator<String>  collator_;

  public int compare(Object p1, Object p2) {
    if (collator_ == null) {
      collator_ = Client.getRes().getCollator();
    }
    return collator_.compare((String)p1, (String)p2);
  }

}