package com.bensler.decaf.swing.selection;

import java.util.List;


/**
 */
public interface EntitySelectionListener<E> {

  public void selectionChanged(SelectionHolder<E> source, List<E> selection);

  public static <E> EntitySelectionListener<E> getNopInstance() {
    return (source, selection) -> {};
  }

  public static class Multiplexer<E> extends Object implements EntitySelectionListener<E> {

    private final EntitySelectionListener<E> delegate1_;
    private final EntitySelectionListener<E> delegate2_;

    public Multiplexer(EntitySelectionListener<E> delegate1, EntitySelectionListener<E> delegate2) {
      delegate1_ = delegate1;
      delegate2_ = delegate2;
    }

    @Override
    public void selectionChanged(SelectionHolder<E> source, List<E> selection) {
      delegate1_.selectionChanged(source, selection);
      delegate2_.selectionChanged(source, selection);
    }

  }

}