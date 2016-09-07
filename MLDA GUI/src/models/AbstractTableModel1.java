package models;

import javax.swing.table.AbstractTableModel;

/**
 * 
 * @author Jose Maria Moyano Murillo
 */
public class AbstractTableModel1 extends AbstractTableModel{
    
    Object[][] data;
    Object[] column;
            
    public AbstractTableModel1(Object[][] data, Object[] column)
    {
        this.data = data;
        this.column = column;
    }
    
    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public String getColumnName(int col) {
        return (String) column[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }
    
}
