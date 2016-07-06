/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import utils.Util;
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
import static utils.Util.getMetricValue;
import static utils.Util.getImbalancedMetricValue;
import mulan.data.MultiLabelInstances;


public class Exporter {
    private File file;
    private JTable table;
    private String tableName;

    public Exporter(File mi_file, JTable tabla, String Nombre_tabla) {
        this.file = mi_file;
        this.table = tabla;
        this.tableName = Nombre_tabla;
    }
        
    public Exporter(File mi_file, String Nombre_tabla) {
        this.file = mi_file;
        this.tableName = Nombre_tabla;
        table=null;
    }
        
  
    public Exporter(File mi_file)
    {
        this.file = mi_file;
    }
    
 
    public boolean export(ArrayList<String> metric_list, ArrayList<MultiLabelInstances> list_datasets, 
            ArrayList<String> dataset_names, boolean isMeka)
    {
        try
        {
            FileOutputStream temp1 = new FileOutputStream(file);
            
            DataOutputStream out = new DataOutputStream(temp1);
            
            WritableWorkbook wb = Workbook.createWorkbook(out);
            
            WritableSheet ws =wb.createSheet("Multiple Datasets", 0);
            
            WritableCellFormat wcf2 = new WritableCellFormat();
            wcf2.setAlignment(Alignment.CENTRE);
            wcf2.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.DOTTED,
                    jxl.format.Colour.GRAY_80);

            String temp;
            int rowIndex=0;
            
            ImbalancedFeature[] imbalanced_data, label_frenquency;
            
            //print columns
            ws.setColumnView(0, 60);
            ws.addCell(new Label(0, rowIndex, "Metrics", wcf2));  
            
            for(int i=0;i<dataset_names.size();i++)
            {
                ws.setColumnView(i+1, 15);
                ws.addCell(new Label(i+1, rowIndex, dataset_names.get(i),wcf2));  
            }
            
            rowIndex++;
        
            for(String metric : metric_list)
            {
                MultiLabelInstances current ;
            
                ws.addCell(new Label(0, rowIndex,metric,wcf2));

                for(int i=0; i< list_datasets.size();i++)
                {
                    current = list_datasets.get(i);

                    label_frenquency = Util.getFrequencyByLabel(current);
                    label_frenquency = Util.orderImbalancedDataByFrequency(label_frenquency);

                    imbalanced_data = Util.getImbalancedByIRInterClass(current,label_frenquency);

                    temp = getMetricValue(metric, current, isMeka);
                    if(temp.equals("-1.0")) {
                        temp= getImbalancedMetricValue(metric, current, imbalanced_data);
                    }

                    ws.addCell(new Label(i+1, rowIndex, temp,wcf2));  
                }
                rowIndex++;

            }   
            
            wb.write();
            wb.close();
            
            return true;
            
        }
        catch(IOException | WriteException e) {
            return false;
        }
      
    }
     
      
        
    public boolean export(ArrayList<String> metric_list, MultiLabelInstances dataset, boolean es_de_tipo_meka)
    {
        try
        {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
            
            WritableWorkbook wb = Workbook.createWorkbook(out);
            
            WritableSheet ws =wb.createSheet(tableName, 0);

            WritableCellFormat wcf2 = new WritableCellFormat();
            wcf2.setAlignment(Alignment.CENTRE);
            wcf2.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.DOTTED,
                    jxl.format.Colour.GRAY_80);
            
            
            //columns header
            ws.setColumnView(0, 60);
            ws.addCell(new Label(0, 0, String.valueOf("Relation name"),wcf2));

            ws.setColumnView(1, 15);
            ws.addCell(new Label(1, 0, String.valueOf(tableName),wcf2));

            ImbalancedFeature[] label_frenquency = Util.getFrequencyByLabel(dataset);
            label_frenquency = Util.orderImbalancedDataByFrequency(label_frenquency);

            ImbalancedFeature[] imbalanced_data = Util.getImbalancedByIRInterClass(dataset,label_frenquency);
         
            String temp;
            int rowIndex=1;
            for(String metric : metric_list)
            {
                temp = getMetricValue(metric, dataset, es_de_tipo_meka);
                if(temp.equals("-1.0")) {
                    temp = getImbalancedMetricValue(metric, dataset, imbalanced_data);
                }

                ws.addCell(new Label(0, rowIndex, metric,wcf2));
                ws.addCell(new Label(1, rowIndex, temp,wcf2));

                rowIndex++;
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
    
    public boolean export()
    {
        try
        {
            FileOutputStream temp = new FileOutputStream(file);
            
            DataOutputStream out = new DataOutputStream(temp);
            
            WritableWorkbook wb = Workbook.createWorkbook(out);
            
            WritableSheet ws =wb.createSheet(tableName, 0);

            WritableCellFormat wcf2 = new WritableCellFormat();
            wcf2.setAlignment(Alignment.CENTRE);
            wcf2.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.DOTTED,
                    jxl.format.Colour.GRAY_80);

            for(int i=0;i<table.getColumnCount();i++)
            {
                
                if(i==0){
                    ws.setColumnView(i, 40);
                }
                else{
                    ws.setColumnView(i, 15);
                }
                
                //Add column names
                Object object = table.getColumnName(i);
                ws.addCell(new Label(i, 0, String.valueOf(object),wcf2));
            }
            
            for(int i=0; i<table.getColumnCount(); i++)
                for(int j =0 ; j<table.getRowCount(); j++)
                {
                    Object object = table.getValueAt(j, i);
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
    
    
    public boolean export(JTable columns)
    {
        try
        {
            FileOutputStream temp = new FileOutputStream(file);
            
            DataOutputStream out = new DataOutputStream(temp);
            
            WritableWorkbook wb = Workbook.createWorkbook(out);
            
            WritableSheet ws =wb.createSheet(tableName, 0);
            
            WritableCellFormat wcf2 = new WritableCellFormat();
            wcf2.setAlignment(Alignment.CENTRE);
            wcf2.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.DOTTED,
                    jxl.format.Colour.GRAY_80);

            Object object;
                    
            for(int i=0;i<=table.getColumnCount();i++)
            {
                if(i==0){
                    ws.setColumnView(i, 40);
                }
                else {
                    ws.setColumnView(i, 15);
                }
                
                //Add column names
                if(i==0) {
                    ws.addCell(new Label(i, 0, "Labels",wcf2));
                }
                else
                { 
                    object = table.getColumnName(i-1);
                    ws.addCell(new Label(i, 0, String.valueOf(object),wcf2));
                }
            }
            
            for(int i=0; i<table.getColumnCount(); i++)
                for(int j =0 ; j<=table.getRowCount(); j++)
                {
                    if(j==0) {
                        object = columns.getValueAt(i, j);
                    }
                    else {
                        object = table.getValueAt(i, j-1);
                    }
                    ws.addCell(new Label(j, i+1, String.valueOf(object),wcf2));
                }
            
            wb.write();
            wb.close();
            return true;
            
        }
        catch(IOException | WriteException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
}
