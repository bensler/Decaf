package com.bensler.decaf.util.prefs;

import static java.util.Objects.requireNonNull;

import com.bensler.decaf.util.Named;
import com.bensler.decaf.util.tree.Hierarchical;

public final class PrefKey implements Hierarchical<PrefKey>, Named {

  public static final PrefKey ROOT = new PrefKey();

  private final PrefKey parent_;

  private final String name_;

  private PrefKey() {
    name_ = "";
    parent_ = null;
  }

  public PrefKey(PrefKey parent, String name) {
    requireNonNull(name);
    if (name.isEmpty()) {
      throw new IllegalArgumentException("given name must not be empty");
    }
    name_ = name;
    parent_ = requireNonNull(parent);
  }

  @Override
  public PrefKey getParent() {
    return parent_;
  }

  @Override
  public String getName() {
    return name_;
  }

  @Override
  public int hashCode() {
    return name_.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return
      (obj instanceof PrefKey otherKey)
      && (name_.equals(otherKey.name_))
      && (
        ((parent_ == null) && (otherKey.parent_ == null))
        || (
          ((parent_ != null) && (otherKey.parent_ != null))
          && ((parent_.hashCode() == otherKey.parent_.hashCode()) && parent_.equals(otherKey.parent_))
         )
      );
  }

}
