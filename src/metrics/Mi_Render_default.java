/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metrics;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author oscglezm
 */
public class Mi_Render_default extends DefaultTableCellRenderer
{

    public Mi_Render_default(){}
    
    
     public Component getTableCellRendererComponent(JTable table,
      Object value,
      boolean isSelected,
      boolean hasFocus,
      int row,
      int column )
   {
       super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
      
       this.setForeground(Color.black);
       
       this.setOpaque(true);
       
       return this;
   }
    
}
