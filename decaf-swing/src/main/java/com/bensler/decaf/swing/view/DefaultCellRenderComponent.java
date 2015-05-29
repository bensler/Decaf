package com.bensler.decaf.swing.view;

import java.awt.Color;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;

import com.bensler.decaf.swing.table.TableComponent;

public class DefaultCellRenderComponent extends RendererLabel implements ListRenderComponent, TableRenderComponent {

  public DefaultCellRenderComponent() {
    super();
  }

  @Override
  public void prepareForTable(JTable table, boolean selected, int row, int column, boolean focused) {
    Border border = BORDER_NO_FOCUS;

    focused = table.hasFocus();
    if (selected) {
      setForeground(table.getSelectionForeground());
      setBackground(focused ? table.getSelectionBackground() : ((TableComponent<?>)table).getBackgroundSelectionColorUnfocused());
    } else {
      setForeground(table.getForeground());
      setBackground(table.getBackground());
    }
    setFont(table.getFont());
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
          setForeground(col);
        }
        col = UIManager.getColor("Table.focusCellBackground");
        if (col != null) {
          setBackground(col);
        }
      }
    }
    setBorder(border);
  }

  @Override
  public void prepareForList(JList<?> list, boolean selected, int index, boolean focused) {
    Border border = BORDER_NO_FOCUS;

    setComponentOrientation(list.getComponentOrientation());
    if (selected) {
      setBackground(list.getSelectionBackground());
      setForeground(list.getSelectionForeground());
    } else {
      setBackground(list.getBackground());
      setForeground(list.getForeground());
    }
    setEnabled(list.isEnabled());
    setFont(list.getFont());
    if (focused) {
      if (selected) {
        border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
      }
      if (border == null) {
        border = UIManager.getBorder("List.focusCellHighlightBorder");
      }
    }
    setBorder(border);
  }

}
