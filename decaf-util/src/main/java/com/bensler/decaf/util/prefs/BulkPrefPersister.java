package com.bensler.decaf.util.prefs;

import java.util.Arrays;
import java.util.List;

public class BulkPrefPersister implements PrefPersister {

  private final Prefs prefs_;
  private final List<PrefPersister> persisters_;

  public BulkPrefPersister(Prefs prefs, PrefPersister... persisters) {
    this(prefs, Arrays.asList(persisters));
  }

  public BulkPrefPersister(Prefs prefs, List<PrefPersister> persisters) {
    prefs_ = prefs;
    persisters_ = List.copyOf(persisters);
    apply();
  }

  public void apply() {
    apply(prefs_);
  }

  public void store() {
    store(prefs_);
  }

  @Override
  public void apply(Prefs prefs) {
    persisters_.forEach(persister -> persister.apply(prefs_));
  }

  @Override
  public void store(Prefs prefs) {
    persisters_.forEach(persister -> persister.store(prefs_));
  }

}
