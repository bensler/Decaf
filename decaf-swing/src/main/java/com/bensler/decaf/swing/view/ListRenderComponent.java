package com.bensler.decaf.swing.view;

import javax.swing.JList;

public interface ListRenderComponent extends RenderComponent {

  void prepareForList(JList<?> list, boolean selected, int index, boolean hasFocus);

}
