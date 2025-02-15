package com.bensler.decaf.swing.table;

import com.bensler.decaf.util.prefs.PrefKey;
import com.bensler.decaf.util.prefs.PrefPersister;
import com.bensler.decaf.util.prefs.Prefs;

public class TablePrefPersister implements PrefPersister {

  private static final String SORT_KEY = "sort";
  private static final String SIZE_KEY = "sizes";

  private final PrefKey prefKeySort_;
  private final PrefKey prefKeySizes_;
  private final TableComponent<?> table_;

  public TablePrefPersister(PrefKey prefKey, TableComponent<?> table) {
     prefKeySort_ = new PrefKey(prefKey, SORT_KEY);
     prefKeySizes_ = new PrefKey(prefKey, SIZE_KEY);
     table_ = table;
  }

  @Override
  public void apply(Prefs prefs) {
    prefs.get(prefKeySizes_).ifPresent(table_::applyColumnWidthPrefs);
    prefs.get(prefKeySort_).ifPresent(table_::applySortPrefs);
  }

  @Override
  public void store(Prefs prefs) {
    prefs.put(prefKeySizes_, String.valueOf(table_.getColumnWidthPrefs()));
    prefs.put(prefKeySort_, String.valueOf(table_.getSortPrefs()));
  }

}
