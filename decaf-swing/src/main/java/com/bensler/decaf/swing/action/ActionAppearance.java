package com.bensler.decaf.swing.action;

import java.awt.Font;
import java.util.Optional;
import java.util.stream.Stream;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/** Encapsulates all information of an action needed to let an user
 * recognize its function. All properties are optional, but at least
 * one must be set. */
public class ActionAppearance {

  private final Optional<Icon> icon_;
  private final Optional<Icon> iconLarge_;
  private final Optional<String> label_;
  private final Optional<String> description_;
  private final Optional<KeyStroke> accelerator_;

  public ActionAppearance(Icon icon, Icon iconLarge, String label, String description, KeyStroke accelerator) {
    this(icon, iconLarge, label, description, Optional.of(accelerator));
  }

  public ActionAppearance(Icon icon, Icon iconLarge, String label, String description) {
    this(icon, iconLarge, label, description, Optional.empty());
  }

  private ActionAppearance(Icon icon, Icon iconLarge, String label, String description, Optional<KeyStroke> accelerator) {
    Stream.of(
      icon_ = Optional.ofNullable(icon),
      iconLarge_ = Optional.ofNullable(iconLarge),
      label_ = Optional.ofNullable(label),
      description_ = Optional.ofNullable(description)
    ).filter(Optional::isPresent)
    .findFirst()
    .orElseThrow(() -> new IllegalArgumentException("At least one of the AppearanceConstructor parameters must be non-null"));
    accelerator_ = accelerator;
  }

  public Optional<Icon> getIcon() {
    return icon_;
  }

  public Icon getIconLarge() {
    return iconLarge_.orElseGet(icon_::get);
  }

  public String getLabel() {
    return label_.orElseGet(() -> description_.orElse(null));
  }

  public String getDescription() {
    return description_.orElseGet(() -> label_.orElse(null));
  }

  public JMenu createMenu() {
    final JMenu menu = new JMenu(getLabel());

    icon_.ifPresent(menu::setIcon);
    return menu;
  }

  public JMenuItem createMenuItem(boolean primary) {
    final JMenuItem menuItem = new JMenuItem(getLabel(), icon_.orElse(null));

    if (primary) {
      menuItem.setFont(menuItem.getFont().deriveFont(Font.BOLD));
    }
    accelerator_.ifPresent(menuItem::setAccelerator);
    return menuItem;
  }

  public JButton createToolbarButton() {
    final JButton button = new JButton(getIconLarge());

    button.setToolTipText(getDescription());
    return button;
  }

}
