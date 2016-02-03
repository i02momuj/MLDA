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
public class AbstractTableModel1 extends AbstractTableModel{
    
    Object[][] data;
    Object[] column;
            
    public AbstractTableModel1(Object[][] data, Object[] column)
    {
        this.data = data;
        this.column = column;
    }
    
    public int getColumnCount() {
        return 1;
      }

      public int getRowCount() {
        return data.length;
      }

      public String getColumnName(int col) {
        return (String) column[col];
      }

      public Object getValueAt(int row, int col) {
        return data[row][col];
      }
    
}
