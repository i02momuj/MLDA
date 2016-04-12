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
public class Mi_Render_IR extends DefaultTableCellRenderer
{
    int posicion_IR, posicion_IR_2=-1;
    
    public Mi_Render_IR(int posicion_IR){ this.posicion_IR = posicion_IR;}
    
    public Mi_Render_IR(int posicion_IR, int posicion_IR_2){ this.posicion_IR = posicion_IR; this.posicion_IR_2 =posicion_IR_2;}
    
    
    public Component getTableCellRendererComponent(JTable table,
      Object value,
      boolean isSelected,
      boolean hasFocus,
      int row,
      int column )
   {
       super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
       
        System.out.println("row: " + row);
        System.out.println("posicion_IR: " + posicion_IR);
        System.out.println("posicion_IR_2: " + posicion_IR_2);
        System.out.println("table.getValueAt(row, posicion_IR).toString(): " + table.getValueAt(row, posicion_IR).toString());
       
       double ir_current = Double.parseDouble(table.getValueAt(row, posicion_IR).toString());
       double ir_current2=-1;
       
       if(posicion_IR_2!=-1){
          ir_current2 = Double.parseDouble(table.getValueAt(row, posicion_IR_2).toString()); 
       }
        
       
      // System.out.println("valor IR: "+ir_current);
       
       if((ir_current>1.5 && column==posicion_IR )){ this.setForeground(Color.red);}
       else if(posicion_IR_2!=-1)
       { 
           if(ir_current2>1.5 && column==posicion_IR_2 ){ this.setForeground(Color.red);}
           else { this.setForeground(Color.black);}
       }
       
       else { this.setForeground(Color.black);}
       
       this.setOpaque(true);
       
       return this;
   }
    
    

    
    
    
}
