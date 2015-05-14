package com.bensler.decaf.swing.view;


public abstract class StringGetter<E> extends Object implements PropertyGetter<E, String> {

  protected final static    StringBuffer      buffer_               = new StringBuffer(200);

  private   final           EntityComparator  comparator_;

  public StringGetter() {
    comparator_ = new EntityComparator(new CollatorComparator());
  }

  @Override
  public int compare(E v1, E v2) {
    return comparator_.compare(this, v1, v2);
  }

  public final static class Constant<E> extends StringGetter<E> {

    private   final           String constant_;

    public Constant(String constant) {
      super();
      constant_ = constant;
    }

    @Override
    public String getProperty(E viewable) {
      return constant_;
    }

  }

}
