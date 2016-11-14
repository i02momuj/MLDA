/*
 * This file is part of the MLDA.
 *
 * (c)  Jose Maria Moyano Murillo
 *      Eva Lucrecia Gibaja Galindo
 *      Sebastian Ventura Soto <sventura@uco.es>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package models;

import javax.swing.table.AbstractTableModel;

/**
 * Abstract Table Model 1
 * 
 * @author Jose Maria Moyano Murillo
 */
public class AbstractTableModel1 extends AbstractTableModel{
    
    Object[][] data;
    Object[] column;
            
    /**
     * Constructor specifying data and columns types
     * 
     * @param data data
     * @param column column types
     */
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
