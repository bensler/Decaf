package com.bensler.decaf.swing.table;

import com.bensler.decaf.util.prefs.PrefKey;
import com.bensler.decaf.util.prefs.PrefPersister;
import com.bensler.decaf.util.prefs.Prefs;

public class TablePrefPersister implements PrefPersister {

  private final PrefKey prefKey_;
  private final TableComponent<?> table_;

  public TablePrefPersister(PrefKey prefKey, TableComponent<?> table) {
     prefKey_ = prefKey;
     table_ = table;
  }

  @Override
  public void apply(Prefs prefs) {
    prefs.get(prefKey_).ifPresent(table_::applySortPrefs);
  }

  @Override
  public void store(Prefs prefs) {
    prefs.put(prefKey_, String.valueOf(table_.getSortPrefs()));
  }

}
