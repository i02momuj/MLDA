package renders;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * 
 * @author Jose Maria Moyano Murillo
 */
public class IRRender extends DefaultTableCellRenderer
{
    int posIR, porIR2=-1;
    
    public IRRender(int posIR){ 
        this.posIR = posIR;
    }
    
    
    public IRRender(int posIR, int posIR2){ 
        this.posIR = posIR; 
        this.porIR2 = posIR2;
    }
    
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column)
    {
        super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
       
        double currentIR;
        if(table.getValueAt(row, posIR).toString().contains("-")){
            currentIR = -1;
        }
        else{
            currentIR = Double.parseDouble(table.getValueAt(row, posIR).toString());
        }

        double currentIR2 = -1;

        if(porIR2!=-1){
           currentIR2 = Double.parseDouble(table.getValueAt(row, porIR2).toString()); 
        }

       if((currentIR>1.5 && column==posIR )){ 
           this.setForeground(Color.red);
       }
       else if(porIR2!=-1)
       { 
           if(currentIR2>1.5 && column==porIR2 ){ 
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
