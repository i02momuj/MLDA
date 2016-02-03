/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metrics;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 *
 * @author oscglezm
 */
public class jframe_temp extends javax.swing.JFrame {

    double[][] pares_freq;
    String[] labelname;
    int posx;
    int posy;
    
    JTable fixedTable;

    public jframe_temp(double[][] pares_freq , String[] labelname, int posx, int posy) 
    {
        
        this.pares_freq = pares_freq;
        this.labelname = labelname;
        this.posx = posx;
        this.posy = posy;
        
        fixedTable = new JTable();
        
        fixedTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTable1FocusLost(evt);
            }
        });
        
        this.setUndecorated(true);
        initComponents();
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
       
        /*
        TableModel tm_jtable = jtable_frequency_pair(jTable1, pares_freq, labelname);
        
        jTable1.setModel(tm_jtable);
        
        JScrollPane scroll = new JScrollPane(jTable1);
        
        
        //jPanel1.removeAll();
        jPanel1.add(scroll, BorderLayout.CENTER, 0);
        */
        int ancho,alto;
        
        if(labelname.length+1<4)ancho= (4)*75;
        else if(labelname.length+1>10)ancho= (10)*75;
         else  ancho= (labelname.length+1)*75;

        if(labelname.length+1 <5) alto = (5)*20;
        else if(labelname.length+1 >9) alto = (9)*20;
        else alto = (labelname.length+1)*20;
        
        
        fixedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
         jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
         fixedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        jtable_coefficient_values(jTable1,fixedTable,jPanel1,ancho,alto);
        
        double critical_value = 6.635; //CAMBIAR
        
        jTable1.setDefaultRenderer(Object.class, new MiRender("estandar",critical_value));
        fixedTable.setDefaultRenderer(Object.class, new MiRender("chi_fi_fixed",critical_value));
        
             
         if(posy > 180 && posx<700) this.setBounds(posx, posy-180, ancho+20, alto+30);
         else if (posy > 180 && posx>700 ) this.setBounds(posx-500, posy, ancho, alto);
         else this.setBounds(posx,posy+520,ancho+20,alto+20);
        
         /* 
        scroll.setBounds(10,20,this.getBounds().width-20,this.getBounds().height-30);
        
        */
        
    }
    
    
    private void jtable_coefficient_values(JTable temp,JTable fixedTable_temp, JPanel jpanel_temp,int ancho, int alto)
    {
        double[][] pair_label_values = pares_freq;
        
        Object [][] data = new Object[pair_label_values.length][pair_label_values.length+1];
        Object [] column = new Object[data.length+1];
        
        System.out.println("longitud "+data.length);
         
          for(int num_fila=0;  num_fila< pair_label_values.length;num_fila++)
        {            
              data[num_fila]  = util.Get_values_x_fila(num_fila, pair_label_values,labelname[num_fila]);
        }
       
           for(int i = 0; i< column.length;i++)//se le agrega 1 pq realmente se emieza en 1 y no en 0.
         {
             if(i==0) column[i]="Labels"; //table_model1.addColumn("Labels");
             else column[i]=(labelname[i-1]);
         } 
          
        AbstractTableModel1 fixedModel = new AbstractTableModel1(data, column);
        AbstractTableModel2 model = new AbstractTableModel2(data, column);   
        
       

       fixedTable_temp.setModel(fixedModel);
       temp.setModel(model);
       
       JScrollPane scroll = new JScrollPane(temp);
    JViewport viewport = new JViewport();
    viewport.setView(fixedTable_temp);
    viewport.setPreferredSize(fixedTable_temp.getPreferredSize());
    scroll.setRowHeaderView(viewport);
    
    scroll.setBounds(10, 20, ancho, alto);
    
    scroll.setCorner(JScrollPane.UPPER_LEFT_CORNER, fixedTable_temp
        .getTableHeader());
        
    temp.setBorder(BorderFactory.createLineBorder(Color.black));
    
    
    jpanel_temp.remove(0); //una curiosa manera de resolver el problema
    jpanel_temp.add(scroll, BorderLayout.CENTER, 0);
    
    }
    
    private TableModel jtable_frequency_pair(JTable jtable ,double[][] pares_freq , String[] labelname )
    {
        DefaultTableModel table_model1= new DefaultTableModel();

        table_model1.addColumn("Label:");
        
        for(int i=0; i<labelname.length;i++)
             table_model1.addColumn(labelname[i]);
        
      
        
        Object[] fila = new Object[labelname.length+1];
        
        
        
        String truncate_value;
       // System.out.println("TEST JTABLE_FREQUENCY");
        //RECORRE LAS ETIQUETAS
        
        for(int i=0; i<pares_freq.length;i++)
        {
            fila[0]=labelname[i];

            for( int j =0; j<pares_freq.length; j++)
            {
                truncate_value = Double.toString(pares_freq[i][j]);
                truncate_value = util.Truncate_values_aprox_zero(truncate_value, 5);
                
                fila[j+1]= truncate_value;
            }
           table_model1.addRow(fila); 
        }
                     
        
        jtable.setModel(table_model1);
        
        //RESIZE COLUMNS! jtable
            TableColumnModel tcm = jtable.getColumnModel();
            
        for(int i=0; i<=labelname.length; i++)
        {
            tcm.getColumn(i).setPreferredWidth(80);
        }
            
        return jtable.getModel();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Values Selected"));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTable1FocusLost(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTable1FocusLost
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_jTable1FocusLost

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
