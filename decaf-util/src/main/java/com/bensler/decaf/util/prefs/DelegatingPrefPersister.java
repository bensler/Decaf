package com.bensler.decaf.util.prefs;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JSplitPane;

public class DelegatingPrefPersister implements PrefPersister {

  public static DelegatingPrefPersister createSplitPanePrefPersister(PrefKey prefKey, JSplitPane splitpane) {
    return new DelegatingPrefPersister(prefKey,
     () -> Optional.of(String.valueOf(splitpane.getDividerLocation())),
     value -> Prefs.tryParseInt(value).ifPresent(splitpane::setDividerLocation)
    );
  }

  private final Supplier<Optional<String>> prefValueProvider_;
  private final Consumer<String> prefValueApplier_;
  protected final PrefKey prefKey_;

  public DelegatingPrefPersister(PrefKey prefKey, Supplier<Optional<String>> prefValueProvider, Consumer<String> prefValueApplier) {
    prefValueProvider_ = requireNonNull(prefValueProvider);
    prefValueApplier_ = requireNonNull(prefValueApplier);
    prefKey_ = requireNonNull(prefKey);
  }

  public void apply(String prefValue) {
    prefValueApplier_.accept(prefValue);
  }

  public Optional<String> store() {
    return prefValueProvider_.get();
  }

  @Override
  public void apply(Prefs prefs) {
    prefs.get(prefKey_).ifPresent(this::apply);
  }

  @Override
  public void store(Prefs prefs) {
    store().ifPresent(value -> prefs.put(prefKey_, value));
  }

}

