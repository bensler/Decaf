package com.bensler.decaf.swing.dialog;

import java.awt.Rectangle;
import java.awt.Window;
import java.util.Optional;

import com.bensler.decaf.util.prefs.PrefKey;
import com.bensler.decaf.util.prefs.PrefPersister;
import com.bensler.decaf.util.prefs.PrefsStorage;

public class WindowPrefsPersister implements PrefPersister {

  private final Window window_;
  private final PrefKey prefKeyX_;
  private final PrefKey prefKeyY_;
  private final PrefKey prefKeyW_;
  private final PrefKey prefKeyH_;

  public WindowPrefsPersister(PrefKey baseKey, Window window) {
    window_ = window;
    prefKeyX_ = new PrefKey(baseKey, "x");
    prefKeyY_ = new PrefKey(baseKey, "y");
    prefKeyW_ = new PrefKey(baseKey, "w");
    prefKeyH_ = new PrefKey(baseKey, "h");
  }

  @Override
  public void apply(PrefsStorage prefs) {
    final Optional<Integer> x = prefs.get(prefKeyX_).flatMap(PrefsStorage::tryParseInt);
    final Optional<Integer> y = prefs.get(prefKeyY_).flatMap(PrefsStorage::tryParseInt);
    final Optional<Integer> w = prefs.get(prefKeyW_).flatMap(PrefsStorage::tryParseInt);
    final Optional<Integer> h = prefs.get(prefKeyH_).flatMap(PrefsStorage::tryParseInt);

    if (x.flatMap(none -> y).flatMap(none -> w).flatMap(none -> h).isPresent()) {
      window_.setBounds(new Rectangle(x.get(), y.get(), w.get(), h.get()));
    };
  }

  @Override
  public void store(PrefsStorage prefs) {
    final Rectangle bounds = window_.getBounds();

    prefs.put(prefKeyX_, String.valueOf(bounds.x));
    prefs.put(prefKeyY_, String.valueOf(bounds.y));
    prefs.put(prefKeyW_, String.valueOf(bounds.width));
    prefs.put(prefKeyH_, String.valueOf(bounds.height));
  }

}
