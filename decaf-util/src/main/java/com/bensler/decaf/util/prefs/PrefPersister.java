package com.bensler.decaf.util.prefs;

public interface PrefPersister {

  public void apply(PrefsStorage prefs);

  public void store(PrefsStorage prefs);

}
