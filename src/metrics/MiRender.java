/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metrics;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author oscglezm
 */
public class MiRender extends DefaultTableCellRenderer
{
   String tipo_tabla; 
   double critical_value;
   
   public MiRender(String tipo_tabla ,double critical_value)
   {
       this.tipo_tabla = tipo_tabla;
       this.critical_value = critical_value;
   }
    
   public Component getTableCellRendererComponent(JTable table,
      Object value,
      boolean isSelected,
      boolean hasFocus,
      int row,
      int column )
   {
      super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
      
      double current_value=-1.0,tmp=-1.0;
      
      //if(value!=null) System.out.println("tipo de tabla: "+ tipo_tabla+" ,valor: "+ table.getValueAt(row, column).toString()+" ,fila: "+row + " ,columna: "+column  );
       
       if(value !=null && column!=0 && value.toString().trim().length()!=0 && !value.toString().equals("----------") )
       {
          if(util.is_number(value.toString()))
              {
                  //System.out.println("entro el error es "+value.toString());
                  current_value = Double.parseDouble(value.toString());
              }
       }
      
      if(tipo_tabla.equals("chi_fi"))
      {
     // if (column==0) { this.setBackground(Color.black); this.setForeground(Color.white);}
      if(column == row){ this.setBackground(Color.black); this.setForeground(Color.white);}
      
      else if (column > row)
      {
          if(table.getValueAt(column, row).toString().equals("----------")) tmp=-1.0;
          else{ tmp = Double.parseDouble(table.getValueAt(column, row).toString());}
          
          if(tmp > critical_value){this.setBackground(Color.gray); this.setForeground(Color.red);}
          else{  this.setBackground(Color.gray); this.setForeground(Color.white);}
          
          System.out.println("phi "+table.getValueAt(row, column));
          System.out.println("chi "+table.getValueAt(column, row));
      }
      
      else if(column <= row)
      {
          if(value!=null&& value.toString().trim().length()!=0 && !value.toString().equals("----------"))
                {
                  tmp= Double.parseDouble(table.getValueAt(row, column).toString());
                  if(tmp > critical_value){this.setBackground(Color.white); this.setForeground(Color.red);}
                  else {this.setBackground(Color.white); this.setForeground(Color.black);}
                }
          else {this.setBackground(Color.white); this.setForeground(Color.black);}
      }
      
      }
      
      else if(tipo_tabla.equals("chi_fi_fixed"))
      {
          if (column==0) { this.setBackground(Color.black); this.setForeground(Color.white);}
      }
      
      else if(tipo_tabla.equals("estandar"))
      {
          if(column == row){ this.setBackground(Color.black); this.setForeground(Color.white);}

          else if (column <= row)
          {
              if(current_value > critical_value){this.setBackground(Color.red); this.setForeground(Color.white);}
              else{this.setBackground(Color.gray); this.setForeground(Color.white);}
          }
          else{
              this.setBackground(Color.white); this.setForeground(Color.black);
              }
      }
          
      this.setBorder(BorderFactory.createLineBorder(Color.black));
      this.setOpaque(true);
      
      return this;
   }
}