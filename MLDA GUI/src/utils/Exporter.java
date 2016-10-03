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

package utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JTable;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * 
 * @author Jose Maria Moyano Murillo
 */
public class Exporter {
    private File file;
    private JTable table;
    private String tableName;

    public Exporter(File file, JTable table, String tableName) {
        this.file = file;
        this.table = table;
        this.tableName = tableName;
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
                
                //Add label names
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
            {
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
            }
            
            wb.write();
            wb.close();
            return true;
        } catch(IOException | WriteException e) {
            e.printStackTrace();
            return false;
        }
    }

}
