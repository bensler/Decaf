package com.bensler.decaf.swing.dialog;

import javax.swing.JComponent;
import javax.swing.JDialog;

public interface ContentPanel<IN, OUT> {

  DialogAppearance getAppearance();

  JComponent getComponent(Context ctx);

  void setInData(IN inData);

  OUT getData();

  interface Context {

    JDialog getDialog();

    void setValid(boolean valid);

  }

}
