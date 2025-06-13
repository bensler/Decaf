package com.bensler.decaf.util.prefs;

import java.util.Arrays;
import java.util.List;

public class BulkPrefPersister implements PrefPersister {

  private final List<PrefPersister> persisters_;

  public BulkPrefPersister(PrefPersister... persisters) {
    this(Arrays.asList(persisters));
  }

  public BulkPrefPersister(List<PrefPersister> persisters) {
    persisters_ = List.copyOf(persisters);
  }

  @Override
  public void apply(Prefs prefs) {
    persisters_.forEach(persister -> persister.apply(prefs));
  }

  @Override
  public void store(Prefs prefs) {
    persisters_.forEach(persister -> persister.store(prefs));
  }

}
