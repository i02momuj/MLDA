/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package renders;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


public class IRRender extends DefaultTableCellRenderer
{
    int IRPosition, IRPosition_2=-1;
    
    public IRRender(int posicion_IR){ 
        this.IRPosition = posicion_IR;
    }
    
    public IRRender(int posicion_IR, int posicion_IR_2){ 
        this.IRPosition = posicion_IR; 
        this.IRPosition_2 =posicion_IR_2;
    }
    
    
    public Component getTableCellRendererComponent(JTable table,
      Object value,
      boolean isSelected,
      boolean hasFocus,
      int row,
      int column )
    {
        super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);

        double ir_current;
        if(table.getValueAt(row, IRPosition).toString().contains("-")){
            ir_current = -1;
        }
        else{
            ir_current = Double.parseDouble(table.getValueAt(row, IRPosition).toString());
        }
        
        double ir_current2=-1;
       
        if(IRPosition_2!=-1){
            ir_current2 = Double.parseDouble(table.getValueAt(row, IRPosition_2).toString()); 
        }

       
        if((ir_current>1.5 && column==IRPosition )){ 
            this.setForeground(Color.red);
        }
        else if(IRPosition_2!=-1)
        { 
            if(ir_current2>1.5 && column==IRPosition_2 ){ 
                this.setForeground(Color.red);
            }
            else { 
                this.setForeground(Color.black);
            }
        }
       
       else { 
            this.setForeground(Color.black);
        }
       
       this.setOpaque(true);
       
       return this;
    }
    
}
