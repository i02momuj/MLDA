/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package renders;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author oscglezm
 */
public class DefaultRender extends DefaultTableCellRenderer
{

    public DefaultRender(){}
    
    
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
