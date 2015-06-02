package com.bensler.decaf.swing.view;

import java.util.List;


/**
 */
public interface EntitySelectionListener<E> {

  public void selectionChanged(EntityComponent<?> source, List<E> selection);

  public static class Nop<E> extends Object implements EntitySelectionListener<E> {

    @Override
    public void selectionChanged(EntityComponent<?> source, List<E> selection) {}

  }

  public static class Multiplexer<E> extends Object implements EntitySelectionListener<E> {

    private final EntitySelectionListener<E> delegate1_;
    private final EntitySelectionListener<E> delegate2_;

    public Multiplexer(EntitySelectionListener<E> delegate1, EntitySelectionListener<E> delegate2) {
      delegate1_ = delegate1;
      delegate2_ = delegate2;
    }

    @Override
    public void selectionChanged(EntityComponent<?> source, List<E> selection) {
      delegate1_.selectionChanged(source, selection);
      delegate2_.selectionChanged(source, selection);
    }

  }

}