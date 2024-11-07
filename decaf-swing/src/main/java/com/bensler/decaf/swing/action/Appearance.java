package com.bensler.decaf.swing.action;

import java.util.Optional;
import java.util.stream.Stream;

import javax.swing.Icon;

/** Encapsulates all information of an action needed to let an user
 * recognize its function. All properties are optional, but at least
 * one must be set. */
public class Appearance {

  private final Optional<Icon> icon_;
  private final Optional<Icon> iconLarge_;
  private final Optional<String> label_;
  private final Optional<String> description_;

  public Appearance(Icon icon, Icon iconLarge, String label, String description) {
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
