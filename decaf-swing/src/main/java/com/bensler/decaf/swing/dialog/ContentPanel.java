package com.bensler.decaf.swing.dialog;

import javax.swing.JComponent;

public interface ContentPanel<IN, OUT> {

  DialogAppearance getAppearance();

  JComponent getComponent(Context ctx);

  void setInData(IN inData);

  OUT getData();

  interface Context {

    void setValid(boolean valid);

  }

}
