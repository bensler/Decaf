package com.bensler.decaf.util.cmp;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * For some reason {@link Collator} implements {@link Comparator} with generic parameter {@link Object} but
 * trying to compare non-{@link String} objects results in a {@link ClassCastException} at runtime. To fix
 * this issue {@link CollatorComparator} wraps a {@link Collator} but implements {@link Comparator} with
 * generic parameter {@link String}.
 */
public final class CollatorComparator extends Object implements Comparator<String> {

  public  static final CollatorComparator  COLLATOR_COMPARATOR   = new CollatorComparator();

  private        final Collator collator_;

  protected CollatorComparator() {
    collator_ = Collator.getInstance();
  }

  public CollatorComparator(Locale locale) {
    collator_ = Collator.getInstance(locale);
  }

  @Override
  public int compare(String p1, String p2) {
    return collator_.compare(p1, p2);
  }

  /**
   * @return the underlaying {@link Collator}.
   */
  public Collator getCollator() {
    return collator_;
  }

}