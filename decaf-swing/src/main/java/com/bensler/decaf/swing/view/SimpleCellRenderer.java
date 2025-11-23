package com.bensler.decaf.swing.view;

import java.util.Optional;
import java.util.function.BiFunction;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class SimpleCellRenderer<E, P> extends Object implements CellRenderer<E, P, JLabel> {

  private static final int TEXT_ICON_GAP = 5;

  protected final BiFunction<E, P, String> textProducer_;
  protected final BiFunction<E, P, Icon> iconProducer_;
  protected final int alignment_;

  public SimpleCellRenderer(BiFunction<E, P, String> textProducer, BiFunction<E, P, Icon> iconProducer) {
    this(textProducer, iconProducer, SwingConstants.LEFT);
  }

  public SimpleCellRenderer(BiFunction<E, P, String> textProducer, BiFunction<E, P, Icon> iconProducer, int alignment) {
    textProducer_ = Optional.ofNullable(textProducer).orElse((_, property) -> (property != null) ? property.toString() : " ");
    iconProducer_ = Optional.ofNullable(iconProducer).orElse((_, _) -> null);
    alignment_ = alignment;
  }

  protected String getText(E entity, P property) {
    return textProducer_.apply(entity, property);
  }

  @Override
  public JLabel render(E entity, P property, JLabel label) {
    final Icon icon = getIcon(entity, property);

    label.setText(getText(entity, property));
    label.setIcon(icon);
    label.setIconTextGap((icon != null) ? TEXT_ICON_GAP : 0);
    label.setHorizontalAlignment(alignment_);
    return label;
  }

  protected final Icon getIcon(E entity, P property) {
    return iconProducer_.apply(entity, property);
  }

}
