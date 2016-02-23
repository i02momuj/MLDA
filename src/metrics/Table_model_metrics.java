/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metrics;

/**
 *
 * @author oscglezm
 */
import javax.swing.table.AbstractTableModel;

public class Table_model_metrics extends AbstractTableModel{
    
    
 public Object rowData[][];
 
String columnNames[] = {"Metric","Value", ""};

 
 public Table_model_metrics(Object rowData[][])
 {
     this.rowData = rowData;
 }
 
 public Table_model_metrics(Object rowData[][], String type)
 {
     this.rowData = rowData;
     
     if(type.equals("train-test")){
         columnNames = new String[4];
         columnNames[0] = "Metric";
         columnNames[1] = "Train";
         columnNames[2] = "Test";
         columnNames[3] = "";
     }
 }


  public int getColumnCount() {
    return columnNames.length;
  }

  public String getColumnName(int column) {
    return columnNames[column];
  }

  public int getRowCount() {
    return rowData.length;
  }

  public Object getValueAt(int row, int column) {
    return rowData[row][column];
  }

  public Class getColumnClass(int column) {
    return (getValueAt(0, column).getClass());
  }

  public void setValueAt(Object value, int row, int column) {
    rowData[row][column] = value;
  }

  public boolean isCellEditable(int row, int column) {
    return (column != 0);
  }
  
  
    
}
