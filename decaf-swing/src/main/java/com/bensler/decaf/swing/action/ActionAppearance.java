package com.bensler.decaf.swing.action;

import java.awt.Font;
import java.util.Optional;
import java.util.stream.Stream;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/** Encapsulates all information of an action needed to let an user
 * recognize its function. All properties are optional, but at least
 * one must be set. */
public class ActionAppearance {

  private final Optional<Icon> icon_;
  private final Optional<Icon> iconLarge_;
  private final Optional<String> label_;
  private final Optional<String> description_;

  public ActionAppearance(Icon icon, Icon iconLarge, String label, String description) {
    Stream.of(
      icon_ = Optional.ofNullable(icon),
      iconLarge_ = Optional.ofNullable(iconLarge),
      label_ = Optional.ofNullable(label),
      description_ = Optional.ofNullable(description)
    ).filter(Optional::isPresent)
    .findFirst()
    .orElseThrow(() -> new IllegalArgumentException("At least one of the AppearanceConstructor parameters must be non-null"));
  }

  public Optional<Icon> getIcon() {
    return icon_;
  }

  public Icon getIconLarge() {
    return iconLarge_.orElseGet(icon_::get);
  }

  public Optional<String> getLabel() {
    return label_;
  }

  public Optional<String> getDescription() {
    return description_;
  }

  public JMenu createMenu() {
    final JMenu menu = new JMenu(label_.orElse(null));

    icon_.ifPresent(menu::setIcon);
    return menu;
  }

  public JMenuItem createPopupmenuItem(boolean primary) {
    final JMenuItem menuItem = new JMenuItem(label_.orElse(null), icon_.orElse(null));

    if (primary) {
      menuItem.setFont(menuItem.getFont().deriveFont(Font.BOLD));
    }
    return menuItem;
  }

  public JButton createToolbarButton() {
    final JButton button = new JButton(getIconLarge());

    button.setToolTipText(description_.orElseGet(() -> label_.orElse(null)));
    return button;
  }

}
