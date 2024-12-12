package com.bensler.decaf.swing.dialog;

import javax.swing.Icon;

/** Encapsulates all information appearing in the banner panel in the
 * upper section of an {@link OkCancelDialog}. */
public class DialogAppearance {

  private final Icon icon_;
  private final String windowTitle_;
  private final String title_;

  public DialogAppearance(Icon icon, String windowTitle, String title) {
    icon_ = icon;
    windowTitle_= windowTitle;
    title_ = title;
  }

  public Icon getIcon() {
    return icon_;
  }

  public String getWindowTitle() {
    return windowTitle_;
  }

  public String getTitle() {
    return title_;
  }

}
