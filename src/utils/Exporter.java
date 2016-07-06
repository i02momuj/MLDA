/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JTable;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import static utils.util.Cambia_pto_x_coma;
import static utils.util.get_value_metric;
import static utils.util.get_value_metric_imbalanced;
import mulan.data.MultiLabelInstances;

/**
 *
 * @author oscglezm
 */
public class Exporter {
    private File mi_file;
    private JTable tabla;
    private String Nombre_tabla;
    private ArrayList<String> metric_list_comun,metric_list_train,metric_list_test;

    public Exporter(File mi_file, JTable tabla, String Nombre_tabla) {
        this.mi_file = mi_file;
        this.tabla = tabla;
        this.Nombre_tabla = Nombre_tabla;
    }
    
    
        public Exporter(File mi_file, String Nombre_tabla) {
        this.mi_file = mi_file;
        this.Nombre_tabla = Nombre_tabla;
        tabla=null;
    }
        
        public Exporter(File mi_file,ArrayList<String> metric_list_comun,ArrayList<String> metric_list_train,ArrayList<String> metric_list_test) {
        this.mi_file = mi_file;
        tabla=null;
        this.metric_list_comun = metric_list_comun;
        this.metric_list_train = metric_list_train;
        this.metric_list_test = metric_list_test;
    }
        
        public Exporter(File mi_file)
        {
            this.mi_file = mi_file;
        }
    
 
      public boolean exporta(ArrayList<String> metric_list, ArrayList<MultiLabelInstances> list_datasets, ArrayList<String> dataset_names, boolean es_de_tipo_meka)
      {
           try
        {
            FileOutputStream temp1 = new FileOutputStream(mi_file);
            
            DataOutputStream out = new DataOutputStream(temp1);
            
            WritableWorkbook wb = Workbook.createWorkbook(out);
            
            WritableSheet ws =wb.createSheet("Multiple Datasets", 0);
            

            //Estilo centrado y con bordes
            WritableCellFormat wcf2 = new WritableCellFormat();
            wcf2.setAlignment(Alignment.CENTRE);
            wcf2.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.DOTTED,
                    jxl.format.Colour.GRAY_80);

            String temp;
            int ind_fila=0;
            
            ImbalancedFeature[] imbalanced_data;
            ImbalancedFeature[] label_frenquency;
            
            //pintando las columnas
            ws.setColumnView(0, 60);
            ws.addCell(new Label(0, ind_fila, "Metrics",wcf2));  
            
            for(int i=0;i<dataset_names.size();i++)
            {
               ws.setColumnView(i+1, 15);
               ws.addCell(new Label(i+1, ind_fila, dataset_names.get(i),wcf2));  
            }
            
            ind_fila++;
        
        for(String metric : metric_list)
        {
            MultiLabelInstances current ;
            
            ws.addCell(new Label(0, ind_fila,metric,wcf2));
            
            for(int i=0; i< list_datasets.size();i++)
            {
                current = list_datasets.get(i);
                
                label_frenquency = util.Get_Frequency_x_label(current);
                label_frenquency = util.Ordenar_freq_x_attr(label_frenquency);// ordena de mayor a menor
             
                imbalanced_data = util.Get_data_imbalanced_x_label_inter_class(current,label_frenquency);
                //imbalanced_data =  util.Get_data_imbalanced_x_label(current); 
                                          
                temp = get_value_metric(metric, current, es_de_tipo_meka);
                if(temp.equals("-1.0")) temp= get_value_metric_imbalanced(metric, current, imbalanced_data);
                
                ws.addCell(new Label(i+1, ind_fila, temp,wcf2));  
            }
            ind_fila++;
            
        }   
            
            wb.write();
            wb.close();
            return true;
            
        }
        catch(IOException | WriteException e)
        {
            return false;
        }
      
      }
     
      
       
    public boolean exporta(ArrayList<String> metric_list, MultiLabelInstances dataset, boolean es_de_tipo_meka)
    {
        try
        {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(mi_file));
            
            WritableWorkbook wb = Workbook.createWorkbook(out);
            
            WritableSheet ws =wb.createSheet(Nombre_tabla, 0);
            

            //Estilo centrado y con bordes
            WritableCellFormat wcf2 = new WritableCellFormat();
            wcf2.setAlignment(Alignment.CENTRE);
            wcf2.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.DOTTED,
                    jxl.format.Colour.GRAY_80);
            
            
          //encabezado de las columnas
          ws.setColumnView(0, 60);
          ws.addCell(new Label(0, 0, String.valueOf("Relation name"),wcf2));
          
          ws.setColumnView(1, 15);
          ws.addCell(new Label(1, 0, String.valueOf(Nombre_tabla),wcf2));
        
          ImbalancedFeature[] label_frenquency = util.Get_Frequency_x_label(dataset);
          label_frenquency = util.Ordenar_freq_x_attr(label_frenquency);// ordena de mayor a menor
          
          ImbalancedFeature[] imbalanced_data = util.Get_data_imbalanced_x_label_inter_class(dataset,label_frenquency);
         
         String temp;
         int ind_fila=1;
        for(String metric : metric_list)
        {
            temp = get_value_metric(metric, dataset, es_de_tipo_meka);
            if(temp.equals("-1.0")) temp = get_value_metric_imbalanced(metric, dataset, imbalanced_data);
            
            ws.addCell(new Label(0, ind_fila, metric,wcf2));
            ws.addCell(new Label(1, ind_fila, temp,wcf2));
                  
            ind_fila++;
        }

            wb.write();
            wb.close();
            return true;
            
        }
        catch(IOException | WriteException e)
        {
            return false;
        }     
    
    }
    
    public boolean exporta()
    {
        try
        {
            FileOutputStream temp = new FileOutputStream(mi_file);
            
            DataOutputStream out = new DataOutputStream(temp);
            
            WritableWorkbook wb = Workbook.createWorkbook(out);
            
            WritableSheet ws =wb.createSheet(Nombre_tabla, 0);
            

            //Estilo centrado y con bordes
            WritableCellFormat wcf2 = new WritableCellFormat();
            wcf2.setAlignment(Alignment.CENTRE);
            wcf2.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.DOTTED,
                    jxl.format.Colour.GRAY_80);

            
            for(int i=0;i<tabla.getColumnCount();i++)
            {
                
                if(i==0)ws.setColumnView(i, 40); //establecer ancho de las columnas
                else ws.setColumnView(i, 15);
                
                //agregar los nombres de las columnas
                Object object = tabla.getColumnName(i);
                ws.addCell(new Label(i, 0, String.valueOf(object),wcf2));
            }
            
            for(int i=0; i<tabla.getColumnCount(); i++)
                for(int j =0 ; j<tabla.getRowCount(); j++)
                {
                    Object object = tabla.getValueAt(j, i);
                    ws.addCell(new Label(i, j+1, String.valueOf(object),wcf2));
                }
            
            wb.write();
            wb.close();
            return true;
            
        }
        catch(IOException | WriteException e)
        {
            return false;
        }
    }
    
    
       public boolean exporta(JTable columns)
    {
        try
        {
            FileOutputStream temp = new FileOutputStream(mi_file);
            
            DataOutputStream out = new DataOutputStream(temp);
            
            WritableWorkbook wb = Workbook.createWorkbook(out);
            
            WritableSheet ws =wb.createSheet(Nombre_tabla, 0);
            

            //Estilo centrado y con bordes
            WritableCellFormat wcf2 = new WritableCellFormat();
            wcf2.setAlignment(Alignment.CENTRE);
            wcf2.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.DOTTED,
                    jxl.format.Colour.GRAY_80);

            Object object;
                    
            for(int i=0;i<=tabla.getColumnCount();i++)
            {
                
                if(i==0)ws.setColumnView(i, 40); //establecer ancho de las columnas
                else ws.setColumnView(i, 15);
                
                //agregar los nombres de las columnas
                if(i==0) ws.addCell(new Label(i, 0, "Labels",wcf2));
                else
                { 
                    object = tabla.getColumnName(i-1);
                    ws.addCell(new Label(i, 0, String.valueOf(object),wcf2));
                }
            }
            
            for(int i=0; i<tabla.getColumnCount(); i++)
                for(int j =0 ; j<=tabla.getRowCount(); j++)
                {
                    if(j==0) object = columns.getValueAt(i, j);
                    else object = tabla.getValueAt(i, j-1);
                    ws.addCell(new Label(j, i+1, String.valueOf(object),wcf2));
                }
            
            wb.write();
            wb.close();
            return true;
            
        }
        catch(IOException | WriteException e)
        {
            System.out.println("este es el mensaje "+e.getMessage());
            return false;
        }
    }
    
}
