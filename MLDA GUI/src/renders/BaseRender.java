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

package renders;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import utils.Utils;

/**
 * 
 * @author Jose Maria Moyano Murillo
 */
public class BaseRender extends DefaultTableCellRenderer
{
   String tableType; 
   double criticalValue;
   
   
    public BaseRender(String tableType ,double criticalValue)
    {
       this.tableType = tableType;
       this.criticalValue = criticalValue;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column )
    {
        super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
      
        double currentValue=-1.0, tmp;
        String tmp2, tmp3;

        if(value !=null && column!=0 && value.toString().trim().length()!=0 && !value.toString().equals("---") )
        {           
            if(value.toString().contains("E")){
                value = value.toString().split("E")[0] + "E" + value.toString().split("E")[1];
                table.setValueAt(value, row, column);
            }
           
            if(Utils.isNumber(value.toString()))
            {
                currentValue = Double.parseDouble(value.toString());
            }
        }
      
        this.setHorizontalAlignment(SwingConstants.CENTER);
       
        switch (tableType) {
            case "chi_fi":
                if(column == row){
                    this.setBackground(Color.gray);
                    this.setForeground(Color.white);
                }               
                else if (column > row)
                {
                    if(table.getValueAt(column, row).toString().equals("---")) {
                        tmp = -1.0;
                    }
                    else{
                        tmp2 = table.getValueAt(row, column).toString();
                        if(tmp2.contains("E")){
                            tmp3 = tmp2.split("E")[0] + "E" + tmp2.split("E")[1];
                            tmp= Double.parseDouble(tmp3);
                            table.setValueAt(tmp3, row, column);
                        }
                        else{
                            tmp = Double.parseDouble(tmp2);
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
                        this.setBackground(Color.white);
                        this.setForeground(Color.black);
                    }
                }  
                break;
            case "chi_fi_fixed":
                if (column==0) {
                    this.setHorizontalAlignment(SwingConstants.LEFT);
                    this.setBackground(Color.darkGray);
                    this.setForeground(Color.white);
                }  
                break;
            case "heatmap":
                if(column == row){
                    this.setBackground(Color.lightGray);
                    this.setForeground(Color.black);
                }
                else if (column <= row)
                {
                    if(currentValue > criticalValue){
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
                break;
            case "estandar":
                if(column == row){
                    this.setBackground(Color.gray);
                    this.setForeground(Color.white);
                }
                else if (column <= row)
                {
                    if(currentValue > criticalValue){
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
                break;
            default:
                break;
        }

        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setOpaque(true);

        return this;
    }
}