package com.bensler.decaf.swing.view;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;

import com.bensler.decaf.swing.table.TableComponent;

public class DefaultCellRenderComponent implements RenderComponent {

  private final RendererLabel component_;

  public DefaultCellRenderComponent() {
    super();
    component_ = new RendererLabel();
  }

  @Override
  public JLabel getComponent() {
    return component_;
  }

  @Override
  public void prepareForTable(JTable table, boolean selected, int row, int column, boolean focused) {
    Border border = RendererLabel.BORDER_NO_FOCUS;

    if (selected) {
      component_.setForeground(table.getSelectionForeground());
      component_.setBackground(focused ? table.getSelectionBackground() : ((TableComponent<?>)table).getBackgroundSelectionColorUnfocused());
    } else {
      component_.setForeground(table.getForeground());
      component_.setBackground(table.getBackground());
    }
    component_.setFont(table.getFont());
    if (focused) {
      if (selected) {
        border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
      }
      if (border == null) {
        border = UIManager.getBorder("Table.focusCellHighlightBorder");
      }
      if (!selected && table.isCellEditable(row, column)) {
        Color col = UIManager.getColor("Table.focusCellForeground");
        if (col != null) {
          component_.setForeground(col);
        }
        col = UIManager.getColor("Table.focusCellBackground");
        if (col != null) {
          component_.setBackground(col);
        }
      }
    }
    component_.setBorder(border);
  }

  @Override
  public void prepareForList(JList<?> list, boolean selected, int index, boolean focused) {
    Border border = RendererLabel.BORDER_NO_FOCUS;

    component_.setComponentOrientation(list.getComponentOrientation());
    if (selected) {
      component_.setBackground(list.getSelectionBackground());
      component_.setForeground(list.getSelectionForeground());
    } else {
      component_.setBackground(list.getBackground());
      component_.setForeground(list.getForeground());
    }
    component_.setEnabled(list.isEnabled());
    component_.setFont(list.getFont());
    if (focused) {
      if (selected) {
        border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
      }
      if (border == null) {
        border = UIManager.getBorder("List.focusCellHighlightBorder");
      }
    }
    component_.setBorder(border);
  }

}
