package com.bensler.decaf.swing.dialog;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;

import com.bensler.decaf.util.prefs.PrefPersisterImpl;

public interface ContentPanel<IN, OUT> {

  DialogAppearance getAppearance();

  JComponent getComponent();

  void setContext(Context ctx);

  void setInData(IN inData);

  OUT getData();

  class ValidationContext {

    private final List<String> errorMessages_;

    public ValidationContext() {
      errorMessages_ = new ArrayList<>();
    }

    public void addErrorMsg(String errorMsg) {
      errorMessages_.add(errorMsg);
    }

    public String popFirstErrorMsg() {
      return errorMessages_.remove(0);
    }

    public boolean isValid() {
      return errorMessages_.isEmpty();
    }

  }

  interface Context {

    JDialog getDialog();

    void setComponentToFocus(JComponent comp);

    void setPrefs(PrefPersisterImpl prefs);

    void setValid(ValidationContext validationCtx);

    void setOkButtonText(String okButtonText);

    void setCancelButtonText(String cancelButtonText);

  }

}
