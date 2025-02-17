package com.bensler.decaf.swing.view;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;

import com.bensler.decaf.swing.table.TableComponent;

public class DefaultCellRenderComponent implements RenderComponent {

  private final RendererLabel compListTable_;

  public DefaultCellRenderComponent() {
    compListTable_ = new RendererLabel();
  }

  @Override
  public RendererLabel prepareForTable(JTable table, boolean selected, int row, int column, boolean focused) {
    Border border = RendererLabel.BORDER_NO_FOCUS;

    if (selected) {
      compListTable_.setForeground(table.getSelectionForeground());
      compListTable_.setBackground(focused ? table.getSelectionBackground() : ((TableComponent<?>)table).getBackgroundSelectionColorUnfocused());
    } else {
      compListTable_.setForeground(table.getForeground());
      compListTable_.setBackground(table.getBackground());
    }
    compListTable_.setFont(table.getFont());
    if (focused) {
      if (selected) {
        border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
      }
      if (border == null) {
        border = UIManager.getBorder("Table.focusCellHighlightBorder");
      }
    }
    compListTable_.setBorder(border);
    return compListTable_;
  }

  @Override
  public RendererLabel prepareForList(JList<?> list, boolean selected, int index, boolean focused) {
    Border border = RendererLabel.BORDER_NO_FOCUS;

    compListTable_.setComponentOrientation(list.getComponentOrientation());
    if (selected) {
      compListTable_.setBackground(list.getSelectionBackground());
      compListTable_.setForeground(list.getSelectionForeground());
    } else {
      compListTable_.setBackground(list.getBackground());
      compListTable_.setForeground(list.getForeground());
    }
    compListTable_.setEnabled(list.isEnabled());
    compListTable_.setFont(list.getFont());
    if (focused) {
      if (selected) {
        border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
      }
      if (border == null) {
        border = UIManager.getBorder("List.focusCellHighlightBorder");
      }
    }
    compListTable_.setBorder(border);
    return compListTable_;
  }

}
