package com.bensler.decaf.util.cmp;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;


public final class CollatorComparator extends Object implements Comparator<String> {

  public  static final CollatorComparator  INSTANCE   = new CollatorComparator();

  private        final Comparator<Object>  collator_;

  public CollatorComparator() {
    collator_ = Collator.getInstance();
  }

  public CollatorComparator(Locale locale) {
    collator_ = Collator.getInstance(locale);
  }

  @Override
  public int compare(String p1, String p2) {
    return collator_.compare(p1, p2);
  }

}