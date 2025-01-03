package com.bensler.decaf.swing.dialog;

import javax.swing.JComponent;
import javax.swing.JDialog;

import com.bensler.decaf.util.prefs.BulkPrefPersister;

public interface ContentPanel<IN, OUT> {

  DialogAppearance getAppearance();

  JComponent getComponent();

  void setContext(Context ctx);

  void setInData(IN inData);

  OUT getData();

  interface Context {

    JDialog getDialog();

    void setPrefs(BulkPrefPersister prefs);

    void setValid(boolean valid);

    void setOkButtonText(String okButtonText);

    void setCancelButtonText(String cancelButtonText);

  }

}
