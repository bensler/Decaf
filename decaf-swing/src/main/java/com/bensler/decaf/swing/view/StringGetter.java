package com.bensler.decaf.swing.view;

import com.bensler.decaf.swing.Viewable;

public abstract class StringGetter extends Object implements PropertyGetter {

  protected final static    StringBuffer      buffer_               = new StringBuffer(200);

  private   final           EntityComparator  comparator_;

  public StringGetter() {
    comparator_ = new EntityComparator(new CollatorComparator());
  }

  public boolean isSortable() {
    return true;
  }

  public int compare(Viewable v1, Viewable v2) {
    return comparator_.compare(this, v1, v2);
  }

  public final static class Constant extends StringGetter {

    private   final           String constant_;

    public Constant(String constant) {
      super();
      constant_ = constant;
    }

    public String getProperty(Viewable viewable) {
      return constant_;
    }

  }

}
