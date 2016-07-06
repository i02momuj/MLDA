/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metrics;

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
import static metrics.util.Cambia_pto_x_coma;
import static metrics.util.get_value_metric;
import static metrics.util.get_value_metric_imbalanced;
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
            
            atributo[] imbalanced_data, label_frenquency;
            
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
     
      
      // CV STRATIFIED AND RAMDON TRAIN/TEST 
      public boolean exporta(ArrayList<MultiLabelInstances> list_dataset_train ,ArrayList<MultiLabelInstances>  list_dataset_test,String dataset_current_name, boolean es_de_tipo_meka)
      {
      try
        {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(mi_file));
            
            WritableWorkbook wb = Workbook.createWorkbook(out);
            
            WritableSheet ws =wb.createSheet(dataset_current_name, 0);

            //Estilo centrado y con bordes
            WritableCellFormat wcf2 = new WritableCellFormat();
            wcf2.setAlignment(Alignment.CENTRE);
            wcf2.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.DOTTED,
                    jxl.format.Colour.GRAY_80);
            
            
          //encabezado de las columnas
          ws.setColumnView(0, 60);
          ws.addCell(new Label(0, 0, String.valueOf("Commun Metrics for"),wcf2));
          
          ws.setColumnView(1, 15);
          ws.addCell(new Label(1, 0, String.valueOf("Train/Test set"),wcf2));
        
          int ind_fila=1;
          
          ArrayList<Double> lista_valores;
          double current_double;
          String value_truncate,temp;
          
         for(String metrica : metric_list_comun)
        {
           lista_valores= new ArrayList();
           current_double=-1.0;
           
                      
           for(int n=0; n<list_dataset_train.size();n++)
            {
                 temp= util.get_value_metric(metrica, list_dataset_train.get(n), es_de_tipo_meka); //can  be dataset_test too
           
                 if(temp.equals( "-1.0" )) temp = util.get_alternative_metric(metrica, list_dataset_train.get(n), list_dataset_test.get(n));
           
                 current_double = Double.parseDouble(temp);
                 
                 if(Double.isNaN(current_double) || current_double==-1.0)break;
                 
                 lista_valores.add(current_double);
                  
            }
             if(Double.isNaN(current_double)) 
             {
                 ws.addCell(new Label(0, ind_fila, metrica,wcf2));
                 ws.addCell(new Label(1, ind_fila, "NaN",wcf2));
             }
             
             else if (current_double==-1.0)
             {
                 ws.addCell(new Label(0, ind_fila, metrica,wcf2));
                 ws.addCell(new Label(1, ind_fila, "-1.0",wcf2));
             }
    
             else
             {             
                 value_truncate = util.Truncate_value(util.Get_media(lista_valores), 5);
                 
                 ws.addCell(new Label(0, ind_fila, metrica,wcf2));
                 ws.addCell(new Label(1, ind_fila, value_truncate,wcf2));
             }
         ind_fila++;
        }
        
          
          
        ind_fila++;
        
        //encabezado de las columnas
          
          ws.addCell(new Label(0, ind_fila, String.valueOf("Metrics for"),wcf2));
          ws.addCell(new Label(1, ind_fila, String.valueOf("Train set"),wcf2));
          ws.addCell(new Label(2, ind_fila, String.valueOf("Test set"),wcf2));
          
          ind_fila++;
          
                  
        String value_test, value_train,value_truncate_test;
        double current_double_train,current_double_test;
              
        atributo[] imbalanced_data_train, label_frenquency_train;
        atributo[] imbalanced_data_test, label_frenquency_test;
        
        ArrayList<Double> lista_valores_test;
        MultiLabelInstances current_train, current_test;
        
        
        for(String metrica : metric_list_test)
        {
            lista_valores= new ArrayList();
            lista_valores_test=new ArrayList();
            
            current_double_train=-1.0;    
            current_double_test=-1.0;    
            
           for(int n=0; n<list_dataset_train.size();n++)
            {
                current_train = list_dataset_train.get(n);
                current_test  = list_dataset_test.get(n);
                
                //TRAIN!!
                label_frenquency_train = util.Get_Frequency_x_label(current_train);
                label_frenquency_train = util.Ordenar_freq_x_attr(label_frenquency_train);// ordena de mayor a menor
             
                imbalanced_data_train = util.Get_data_imbalanced_x_label_inter_class(current_train,label_frenquency_train);    
                
                //TEST!!
                label_frenquency_test = util.Get_Frequency_x_label(current_test);
                label_frenquency_test = util.Ordenar_freq_x_attr(label_frenquency_test);// ordena de mayor a menor
             
                imbalanced_data_test = util.Get_data_imbalanced_x_label_inter_class(current_test,label_frenquency_test);   
                
                
                /*
                imbalanced_data_train =  util.Get_data_imbalanced_x_label(list_dataset_train.get(n));
                imbalanced_data_test =   util.Get_data_imbalanced_x_label(list_dataset_test.get(n));
                */   
                
                 value_train= util.get_value_metric(metrica, list_dataset_train.get(n), es_de_tipo_meka); 
                 value_test= util.get_value_metric(metrica, list_dataset_test.get(n), es_de_tipo_meka); 
                 
                if(value_test.equals( "-1.0" )) value_test = util.get_alternative_metric(metrica, list_dataset_train.get(n), list_dataset_test.get(n));
                 
                if(value_train.equals( "-1.0" )) value_train = util.get_value_metric_imbalanced(metrica, list_dataset_train.get(n), imbalanced_data_train);
                if(value_test.equals( "-1.0" )) value_test = util.get_value_metric_imbalanced(metrica, list_dataset_test.get(n), imbalanced_data_test);
                
                current_double_train = Double.parseDouble(value_train);
                current_double_test = Double.parseDouble(value_test);
                
                if(!(Double.isNaN(current_double_train)))lista_valores.add(current_double_train);
                if(!(Double.isNaN(current_double_test)))lista_valores_test.add(current_double_test); 
                
                  
            }
           
             if(lista_valores.isEmpty())//train 
             {
                 if(lista_valores_test.isEmpty())
                 {
                    ws.addCell(new Label(0, ind_fila, metrica,wcf2));
                    ws.addCell(new Label(1, ind_fila, "NaN",wcf2));
                    ws.addCell(new Label(2, ind_fila, "NaN",wcf2));
                 }
                 else 
                 {
                    value_truncate_test = util.Truncate_value(util.Get_media(lista_valores_test), 5);

                    ws.addCell(new Label(0, ind_fila, metrica,wcf2));
                    ws.addCell(new Label(1, ind_fila, "NaN",wcf2));
                    ws.addCell(new Label(2, ind_fila, value_truncate_test,wcf2));                    
                 }
                 
             }
                
             else
             { 
                 if(lista_valores_test.isEmpty())
                 {
                    value_truncate = util.Truncate_value(util.Get_media(lista_valores), 5);
                    
                    ws.addCell(new Label(0, ind_fila, metrica,wcf2));
                    ws.addCell(new Label(1, ind_fila, value_truncate,wcf2));
                    ws.addCell(new Label(2, ind_fila, "NaN",wcf2));                     
                 }
                  else
                  {
                    
                    if(metrica.equals("Number of unseen classes") || metrica.equals("Ratio of unseen classes"))
                     {
                        value_truncate_test = util.Truncate_value(util.Get_media(lista_valores_test), 5);
                    
                        ws.addCell(new Label(0, ind_fila, metrica,wcf2));
                        ws.addCell(new Label(1, ind_fila, "NaN",wcf2));
                        ws.addCell(new Label(2, ind_fila, value_truncate_test,wcf2));  
                     }
                      
                    else
                    {
                        value_truncate = util.Truncate_value(util.Get_media(lista_valores), 5);
                        value_truncate_test = util.Truncate_value(util.Get_media(lista_valores_test), 5);

                        ws.addCell(new Label(0, ind_fila, metrica,wcf2));
                        ws.addCell(new Label(1, ind_fila, value_truncate,wcf2));
                        ws.addCell(new Label(2, ind_fila, value_truncate_test,wcf2));                                              
                    }
                  }
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
        
        
      //HOLDOUT STRATIFIED AND RAMDON TRAIN/TEST
     public boolean exporta(MultiLabelInstances dataset_train , MultiLabelInstances dataset_test,String dataset_current_name, boolean es_de_tipo_meka)
     {
       try
        {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(mi_file));
            
            WritableWorkbook wb = Workbook.createWorkbook(out);
            
            WritableSheet ws =wb.createSheet(dataset_current_name, 0);
            

            //Estilo centrado y con bordes
            WritableCellFormat wcf2 = new WritableCellFormat();
            wcf2.setAlignment(Alignment.CENTRE);
            wcf2.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.DOTTED,
                    jxl.format.Colour.GRAY_80);
            
            
          //encabezado de las columnas
          ws.setColumnView(0, 60);
          ws.addCell(new Label(0, 0, String.valueOf("Commun Metrics for"),wcf2));
          
          ws.setColumnView(1, 15);
          ws.addCell(new Label(1, 0, String.valueOf("Train/Test set"),wcf2));
        
          atributo[] label_frenquency = util.Get_Frequency_x_label(dataset_train);
          label_frenquency = util.Ordenar_freq_x_attr(label_frenquency);// ordena de mayor a menor
                    
          atributo[] imbalanced_data_train =  util.Get_data_imbalanced_x_label_inter_class(dataset_train,label_frenquency);
         
         String temp;
         int ind_fila=1;
         
        for(String metric : metric_list_comun)
        {
            temp = get_value_metric(metric, dataset_train, es_de_tipo_meka);
            if(temp.equals("-1.0")) temp = util.get_alternative_metric(metric, dataset_train,dataset_test);
            
            ws.addCell(new Label(0, ind_fila, metric,wcf2));
            ws.addCell(new Label(1, ind_fila, temp,wcf2));
                  
            ind_fila++;
        }
        
        ind_fila++;
        
        //encabezado de las columnas
          
          ws.addCell(new Label(0, ind_fila, String.valueOf("Metrics for"),wcf2));
          ws.addCell(new Label(1, ind_fila, String.valueOf("Train set"),wcf2));
          ws.addCell(new Label(2, ind_fila, String.valueOf("Test set"),wcf2));
          
          ind_fila++;
          
          String value_test, value_train;
        //atributo[] imbalanced_data_train =  util.Get_data_imbalanced_x_label(dataset_train); 
          
          
          label_frenquency = util.Get_Frequency_x_label(dataset_test);
          label_frenquency = util.Ordenar_freq_x_attr(label_frenquency);// ordena de mayor a menor
          
        atributo[] imbalanced_data_test =  util.Get_data_imbalanced_x_label_inter_class(dataset_test,label_frenquency);
          
        for(String metrica : metric_list_test)
        {
           value_train = util.get_value_metric(metrica, dataset_train, es_de_tipo_meka);
           if(value_train.equals("-1.0")) value_train = util.get_value_metric_imbalanced(metrica, dataset_train, imbalanced_data_train); 	
           
           value_test= util.get_value_metric(metrica, dataset_test, es_de_tipo_meka);
           if(value_test.equals("-1.0")) value_test = util.get_value_metric_imbalanced(metrica, dataset_test, imbalanced_data_test); 	
          
           if (value_test.equals("-1.0"))  value_test= util.get_alternative_metric(metrica,dataset_train, dataset_test);
                     
           if(value_train.equals("-1.0"))
           {
            ws.addCell(new Label(0, ind_fila, metrica,wcf2));
            ws.addCell(new Label(1, ind_fila, "NaN",wcf2));
            ws.addCell(new Label(2, ind_fila, value_test,wcf2));
           }
           //wr.write(metrica+";"+ "NaN"+";"+value_test);
                  
           else
           {
             ws.addCell(new Label(0, ind_fila, metrica,wcf2));
             ws.addCell(new Label(1, ind_fila, value_train,wcf2));
             ws.addCell(new Label(2, ind_fila, value_test,wcf2));
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
        
          atributo[] label_frenquency = util.Get_Frequency_x_label(dataset);
          label_frenquency = util.Ordenar_freq_x_attr(label_frenquency);// ordena de mayor a menor
          
          atributo[] imbalanced_data = util.Get_data_imbalanced_x_label_inter_class(dataset,label_frenquency);
         
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
