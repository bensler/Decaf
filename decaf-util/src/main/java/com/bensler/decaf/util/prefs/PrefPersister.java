package com.bensler.decaf.util.prefs;

public interface PrefPersister {

  public void apply(Prefs prefs);

  public void store(Prefs prefs);

}
