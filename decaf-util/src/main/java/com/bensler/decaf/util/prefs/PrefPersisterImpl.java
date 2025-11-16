package com.bensler.decaf.util.prefs;

import java.util.Arrays;
import java.util.List;

public class PrefPersisterImpl extends BulkPrefPersister {

  private final PrefsStorage storage_;

  public PrefPersisterImpl(PrefsStorage storage, PrefPersister... persisters) {
    this(storage, Arrays.asList(persisters));
  }

  public PrefPersisterImpl(PrefsStorage storage, List<PrefPersister> persisters) {
    super(persisters);
    storage_ = storage;
    apply();
  }

  public void apply() {
    apply(storage_);
  }

  public void store() {
    store(storage_);
  }

  public PrefsStorage getStorage() {
    return storage_;
  }

}
