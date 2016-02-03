/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metrics;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author oscglezm
 */
public class AbstractTableModel2 extends AbstractTableModel {
    
    Object[][] data;
    Object[] column;
            
    public AbstractTableModel2(Object[][] data, Object[] column)
    {
        this.data = data;
        this.column = column;
    }
    
    
     public int getColumnCount() {
        return column.length - 1;
      }

      public int getRowCount() {
        return data.length;
      }

      public String getColumnName(int col) {
        return (String) column[col + 1];
      }

      public Object getValueAt(int row, int col) {
        return data[row][col + 1];
      }

      public void setValueAt(double obj, int row, int col) {
        data[row][col + 1] = obj;
      }

      public boolean CellEditable(int row, int col) {
        return true;
      }
}
