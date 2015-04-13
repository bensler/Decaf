package com.bensler.decaf.swing.view;

import java.awt.Color;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import com.bensler.decaf.swing.Viewable;

/**
 */
public interface EntityComponent {

  public JComponent getComponent();
  
  public JScrollPane getScrollPane();
  
  public List<? extends Viewable> getSelection();
  
  public Viewable getSingleSelection();

  public void addSelectionListener(EntitySelectionListener listener);
  public void removeSelectionListener(EntitySelectionListener listener);

  public boolean contains(Viewable entity);
  
  public void select(Collection<? extends Viewable> entities);
  
  public void select(Viewable entity);
  
  public void clearSelection();
  
  public void setBackground(Color c);
  public void setToolTipText(String hint);
  public boolean isEnabled();

  public interface FocusListener {
    
    public void focusGained(EntityComponent component);
    
  }
  
}
