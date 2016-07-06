/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package renders;

import java.awt.Color;
import java.awt.Component;
import java.awt.print.PrinterException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import utils.Util;


public class BaseRender extends DefaultTableCellRenderer
{
    String tableType; 
    double criticalValue;
   
    public BaseRender(String tableType ,double criticalValue)
    {
        this.tableType = tableType;
        this.criticalValue = criticalValue;
    }
    
    
    public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column )
    {
        super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
      
        double current_value=-1.0,tmp=-1.0;
        String tmp2, tmp3;
        
        if(value !=null && column!=0 && value.toString().trim().length()!=0 && !value.toString().equals("---") )
        {           
            if(value.toString().contains("E")){
                value = value.toString().split("E")[0] + "E" + value.toString().split("E")[1];
                table.setValueAt(value, row, column);
            }
           
            if(Util.isNumber(value.toString()))
            {
                current_value = Double.parseDouble(value.toString());
            }
        }
      
        this.setHorizontalAlignment(SwingConstants.CENTER);
       
        if(tableType.equals("chi_fi"))
        {
            if(column == row){ 
                this.setBackground(Color.gray); 
                this.setForeground(Color.white);
            }

            else if (column > row)
            {
                if(table.getValueAt(column, row).toString().equals("---")) {
                    tmp=-1.0;
                }
                else{
                    tmp2 = table.getValueAt(row, column).toString();
                    if(tmp2.contains("E")){
                        tmp3 = tmp2.split("E")[0] + "E" + tmp2.split("E")[1];
                        tmp= Double.parseDouble(tmp3);
                        table.setValueAt(tmp3, row, column);
                    }
                    else{
                        tmp= Double.parseDouble(tmp2);
                    }
                }

                if(tmp > criticalValue){
                    this.setBackground(Color.lightGray);
                    this.setForeground(Color.red);
                }
                else{  
                    this.setBackground(Color.lightGray); 
                    this.setForeground(Color.black);
                }
            }

            else if(column <= row)
            {
                if(value!=null&& value.toString().trim().length()!=0 && !value.toString().equals("---"))
                {
                    tmp2 = table.getValueAt(row, column).toString();
                    if(tmp2.contains("E")){
                        tmp3 = tmp2.split("E")[0] + "E" + tmp2.split("E")[1];
                        tmp= Double.parseDouble(tmp3);
                        table.setValueAt(tmp3, row, column);
                    }
                    else{
                        tmp= Double.parseDouble(tmp2);
                    }
                    
                    if(tmp > criticalValue){
                        this.setBackground(Color.white); 
                        this.setForeground(Color.red);
                    }
                    else {
                        this.setBackground(Color.white); 
                        this.setForeground(Color.black);
                    }
                }
                else {
                    this.setBackground(Color.white); this.setForeground(Color.black);
                }
            }
        }
      
        else if(tableType.equals("chi_fi_fixed"))
        {
            if (column==0) { 
                this.setHorizontalAlignment(SwingConstants.LEFT);
                this.setBackground(Color.darkGray); 
                this.setForeground(Color.white);
            }
        }
      
        else if(tableType.equals("heatmap"))
        {
            if(column == row){ 
                this.setBackground(Color.lightGray); 
                this.setForeground(Color.black);
            }
            else if (column <= row)
            {
                if(current_value > criticalValue){
                    this.setBackground(Color.red); 
                    this.setForeground(Color.white);
                }
                else{
                    this.setBackground(Color.white); 
                    this.setForeground(Color.black);
                }
            }
            else{
                this.setBackground(Color.white);
                this.setForeground(Color.black);
            }
        }
      
        else if(tableType.equals("estandar"))
        {
            if(column == row){ 
                this.setBackground(Color.gray); 
                this.setForeground(Color.white);
            }

            else if (column <= row)
            {
                if(current_value > criticalValue){
                    this.setBackground(Color.red); 
                    this.setForeground(Color.white);
                }
                else{
                    this.setBackground(Color.white); 
                    this.setForeground(Color.black);
                }
            }
            else{
                this.setBackground(Color.gray);
                this.setForeground(Color.black);
            }
        }
          
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setOpaque(true);
      
        return this;
    }
}