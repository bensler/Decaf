package com.bensler.decaf.util.prefs;

import java.util.Arrays;
import java.util.List;

public class PrefPersisterImpl extends BulkPrefPersister {

  private final Prefs prefs_;

  public PrefPersisterImpl(Prefs prefs, PrefPersister... persisters) {
    this(prefs, Arrays.asList(persisters));
  }

  public PrefPersisterImpl(Prefs prefs, List<PrefPersister> persisters) {
    super(persisters);
    prefs_ = prefs;
    apply();
  }

  public void apply() {
    apply(prefs_);
  }

  public void store() {
    store(prefs_);
  }

}
