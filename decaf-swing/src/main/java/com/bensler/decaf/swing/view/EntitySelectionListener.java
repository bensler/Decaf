package com.bensler.decaf.swing.view;

import java.util.List;


/**
 */
public interface EntitySelectionListener<E> {

  public void selectionChanged(EntityComponent<?> source, List<? extends  E> selection);

  public static final Nop<Object> NOP = new Nop<>();

  public static class Nop<E> extends Object implements EntitySelectionListener<E> {

    @Override
    public void selectionChanged(EntityComponent<?> source, List<? extends E> selection) {}

  }

  public static class Multiplexer<E> extends Object implements EntitySelectionListener<E> {

    private final EntitySelectionListener<? super E> delegate1_;
    private final EntitySelectionListener<? super E> delegate2_;

    public Multiplexer(EntitySelectionListener<? super E> delegate1, EntitySelectionListener<? super E> delegate2) {
      delegate1_ = delegate1;
      delegate2_ = delegate2;
    }

    @Override
    public void selectionChanged(EntityComponent<?> source, List<? extends E> selection) {
      delegate1_.selectionChanged(source, selection);
      delegate2_.selectionChanged(source, selection);
    }

  }

}