package com.bensler.decaf.swing.dialog;

import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Optional;

import com.bensler.decaf.util.prefs.PrefKey;
import com.bensler.decaf.util.prefs.Prefs;

public class WindowPrefsPersister {

  private final Window window_;
  private final Prefs prefs_;
  private final PrefKey prefKeyX_;
  private final PrefKey prefKeyY_;
  private final PrefKey prefKeyW_;
  private final PrefKey prefKeyH_;

  public WindowPrefsPersister(Prefs prefs, PrefKey baseKey, Window window) {
    window_ = window;
    prefs_ = prefs;
    prefKeyX_ = new PrefKey(baseKey, "x");
    prefKeyY_ = new PrefKey(baseKey, "y");
    prefKeyW_ = new PrefKey(baseKey, "w");
    prefKeyH_ = new PrefKey(baseKey, "h");
    window.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        save();
      }
    });
    final Optional<Integer> x = prefs.get(prefKeyX_).flatMap(Prefs::tryParseInt);
    final Optional<Integer> y = prefs.get(prefKeyY_).flatMap(Prefs::tryParseInt);
    final Optional<Integer> w = prefs.get(prefKeyW_).flatMap(Prefs::tryParseInt);
    final Optional<Integer> h = prefs.get(prefKeyH_).flatMap(Prefs::tryParseInt);

    if (x.flatMap(none -> y).flatMap(none -> w).flatMap(none -> h).isPresent()) {
      window.setBounds(new Rectangle(x.get(), y.get(), w.get(), h.get()));
    };
  }

  public void save() {
    final Rectangle bounds = window_.getBounds();

    prefs_.put(prefKeyX_, String.valueOf(bounds.x));
    prefs_.put(prefKeyY_, String.valueOf(bounds.y));
    prefs_.put(prefKeyW_, String.valueOf(bounds.width));
    prefs_.put(prefKeyH_, String.valueOf(bounds.height));
  }

}
