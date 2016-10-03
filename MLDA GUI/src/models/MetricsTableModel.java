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
 * 
 * @author Jose Maria Moyano Murillo
 */
public class MetricsTableModel extends AbstractTableModel{
   
    public Object rowData[][];
 
    String columnNames[] = {"Metric","Value", ""};

 
    public MetricsTableModel(Object rowData[][])
    {
        this.rowData = rowData; 
    }
 
    public MetricsTableModel(Object rowData[][], String type)
    {
        this.rowData = rowData;

        if(type.equals("multi")){
            columnNames = new String[2];
            columnNames[0] = "Metric";
            columnNames[1] = "";
        }
    }
 
    public MetricsTableModel(Object rowData[][], String type, int ncol)
    {
        this.rowData = rowData;

        if(type.equals("multi")){
            columnNames = new String[ncol];
            columnNames[0] = "Metric";
            columnNames[1] = "";

            if(ncol > 2){
                for(int i=2; i<ncol; i++){
                    columnNames[i] = "";
                }
            }
        }
    }


    @Override
    public int getColumnCount() {
      return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
      return columnNames[column];
    }

    @Override
    public int getRowCount() {
      return rowData.length;
    }

    @Override
    public Object getValueAt(int row, int column) {
      return rowData[row][column];
    }

    @Override
    public Class getColumnClass(int column) {
      return (getValueAt(0, column).getClass());
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
      rowData[row][column] = value;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      return (column != 0);
    }  
    
}
