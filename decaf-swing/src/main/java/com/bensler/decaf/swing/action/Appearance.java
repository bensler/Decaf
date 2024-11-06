package com.bensler.decaf.swing.action;

import java.util.Optional;

import javax.swing.Icon;

public class Appearance {

  private final Optional<Icon> icon_;
  private final Optional<Icon> iconLarge_;
  private final Optional<String> label_;
  private final Optional<String> description_;

  public Appearance(Icon icon, Icon iconLarge, String label, String description) {
    icon_ = Optional.ofNullable(icon);
    iconLarge_ = Optional.ofNullable(iconLarge);
    label_ = Optional.ofNullable(label);
    description_ = Optional.ofNullable(description);
  }

  public Optional<Icon> getIcon() {
    return icon_;
  }

  public Optional<Icon> getIconLarge() {
    return iconLarge_;
  }

  public Optional<String> getLabel() {
    return label_;
  }

  public Optional<String> getDescription() {
    return description_;
  }

}
