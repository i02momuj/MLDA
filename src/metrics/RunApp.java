package metrics;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
 

import javax.swing.*;


import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import static metrics.util.Get_labelset_x_values;

import mulan.data.InvalidDataFormatException;
import mulan.data.IterativeStratification;
import mulan.data.LabelPowersetStratification;
import mulan.data.LabelSet;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;
import mulan.data.Stratification;
import mulan.examples.CrossValidationExperiment;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.tc33.jheatchart.HeatChart;
import weka.core.Attribute;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemovePercentage;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author osc
 */
public class RunApp extends javax.swing.JFrame {

    
    /**
     * Creates new form RunApp
     */
       
      private Task task;
    
      
     class Task extends SwingWorker<Void, Void> {
         
         progress_bar new_progress;
         int velocidad;
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            Random random = new Random();
            int progress = 0;
            //Initialize progress property.
            setProgress(0);
            
            while (progress < 100) {
                //Sleep for up to one second.
                try {
                    if(velocidad ==2) Thread.sleep(1000);
                    else Thread.sleep(500);
                } catch (InterruptedException ignore) {}
                //Make random progress.
                progress += velocidad;
                setProgress(Math.min(progress, 100));
            }
            return null;
        }

        public Task(progress_bar new_progress, int velocidad)
        {
            this.new_progress = new_progress;
            this.velocidad = velocidad;
        }
        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            setCursor(null); //turn off the wait cursor
          //  taskOutput.append("Done!\n");
            new_progress.setVisible(false);
        }
    }
       
     String dataset_name1="";
    
     static int count_test=1;
     ArrayList<String> test_list = new ArrayList();
     
    //JPanel container;
    MultiLabelInstances dataset,dataset_train, dataset_test;
    String filename_database_xml=null,filename_database_xml_path="";
    String filename_database_arff_test;
             
    ChartPanel cp,cp1,cp2,cp3,cp11,cp22,cp_box,cp_ir_x_label_intra_class,cp_ir_x_label_inter_class,cp_per_labelset, cp_ir_x_label_inter_class_only, cp_ir_x_label_intra_class_only;
    
    double radio;
    int num_atributos;
    int numero_etiquetas;
    int num_instancias;
    int velocidad_multiple=5; //pestaña multiple dataset
   
    //PERTENECE A LA PESTAÑA CHOOSE DATASETS
    JButton button_all_2,button_none_2,button_invert_2, button_calculate_2,button_save2;
    
    JButton button_all_1,button_none_1,button_invert_1, button_calculate_1,button_save;
    JButton button_all_3,button_none_3,button_invert_3, button_calculate_3,button_save3;
    JButton export1,export2,export3,export4,export5,export6,export7;
    
    final JTable jTable5,jTable6,jTable7, jTable1, jTable8,jTable9;
    JTable jTable10,jTable11,jTable12,fixedTable,fixedTable1,fixedTable2;
    
    TableModel tm_BR,tm_BR1,tm_LP,tm_LP1,tm_IR,tm_coefficient,tm_labelxExamples, tm_coocurrences, tm_heapmap_values, tm_attr, tm_jgraph, tm_heapmap_graph, tm_ir_per_label_intra_class,tm_ir_per_label_inter_class,tm_ir_per_labelset, tm_ir_per_label_inter_class_only, tm_ir_per_label_intra_class_only;
    
    DefaultListModel lista = new DefaultListModel();
    ArrayList<MultiLabelInstances> list_dataset, list_dataset_train, list_dataset_test;
    
    ArrayList<Boolean> lista_son_meka;
    ArrayList<String> Dataset_names;
    
    atributo[] labelsets_sorted=null,labelsets_sorted_IR=null;
    atributo[] label_x_frequency;
    double[] labelset_per_ir=null;
    
    Statistics stat1;
  
    atributo[] label_imbalanced;
    JLabel chi,fi;
    String dataset_current_name;
    
    mxGraphComponent graphComponent=null ;
    ArrayList<pares_atributos> lista_pares=null;
    HeatChart jheat_chart= null;
    int[] label_indices_seleccionados=null;
    int[] id_x_nums_label=null, id_x_nums_label_inter_class = null;
    double[] id_x_IR=null;         
    double[] id_x_IR_inter_class=null;  
    container_id_ir_inter_class ir_veces=null;
     double[] IR_inter_class = null;
     double[] IR_intra_class = null;
    atributo[] label_frenquency = null;
    double[] labelset_frequency = null;
    
    //variables chi and fi
    double[][] chi_fi_coefficient;
     Object[][] data; 
     Object[] column;
     
     //variables TRAIN-TEST
     boolean choose_test=false, holdout_ramdon=false, holdout_stratified=false, cv_ramdon=false, cv_stratified =false;
    JButton button_show_traintest,button_save_train;
    
    //VARIABLES BOX DIAGRAM
    JRadioButton jRadioButton8;
    
    boolean first_time_chi=true; 
    boolean es_de_tipo_meka = false;
    
    
    
    public RunApp() 
    {
        
       
        
        jTable5 = new JTable();
        jTable6 = new JTable();
        jTable7 = new JTable();
        jTable1 = new JTable();
        jTable8 = new JTable();
        jTable9 = new JTable();
        jTable10= new JTable();
        fixedTable = new JTable();
        fixedTable1 = new JTable();
        fixedTable2 = new JTable();
        jTable11= new JTable();
        jTable12= new JTable();
        
        this.setTitle("Multilabel Dataset GUI (MUDA-GUI)");
        this.setMinimumSize(new Dimension(780,500));       
        this.setBounds(300,0, 780, 500);
        
        
        
        initComponents();        

        Inicializa_config(); //add jradionbutton
     
        dataset_current_name="";          
        
       // System.out.println("valor de fortaleza "+util.get_valor_fortaleza(2, 15, 3, 6));      
        
       start_config_multiples_datasets();
       
       // System.out.println("punto inicial del jheat graph " + jLabel20.getBounds().x+" , "+ jLabel20.getBounds().y);

       // BOX DIAGRAM INICIALIZACION
       jRadioButton8 = new JRadioButton();
       
       jRadioButton8.setVisible(false);
       radioExamplesPerLabel.setVisible(true);
       radioExamplesPerLabelset.setVisible(true);
       jPanel15.setVisible(false);
       labelIR1.setVisible(false); // comentario de IR>1.5
       labelIR2.setVisible(false); // comentario de IR>1.5
       labelChiThreshold.setVisible(false); // comentario de valores dependientes chi- coefficient
       labelChiThreshold.setVisible(true);
       
       buttonGroup5.add(jRadioButton8);
       buttonGroup5.add(radioExamplesPerLabel);
       buttonGroup5.add(radioExamplesPerLabelset);
       
       
       
    }
    
    private void Inicializa_jtable_fi_chi()
    {
                /*
       
      //JTABLE CHI & FI COEFFICIENT

      */

        
    fixedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable10.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    fixedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    jTable10.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
    fixedTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable11.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    fixedTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    jTable11.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    fixedTable2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jTable12.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    fixedTable2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    jTable12.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    /*
    JScrollPane scroll = new JScrollPane(jTable10);
    JViewport viewport = new JViewport();
    viewport.setView(fixedTable);
    viewport.setPreferredSize(fixedTable.getPreferredSize());
    scroll.setRowHeaderView(viewport);
    
    scroll.setBounds(20, 20, 760, 350);
    
    scroll.setCorner(JScrollPane.UPPER_LEFT_CORNER, fixedTable
        .getTableHeader());
    
    jTable10.setBorder(BorderFactory.createLineBorder(Color.black));
    jPanel24.add(scroll, BorderLayout.CENTER);  
   */
   
//      chi = new JLabel("Chi");
//      chi.setBounds(10,120, 20,20);
//      chi.setBackground(Color.white);
//      chi.setForeground(Color.black);
//      chi.setOpaque(true);
//           
//      panelChiFi.add(chi);
//    
//      fi = new JLabel("Fi");
//      fi.setBounds(300,0, 20,20);
//      fi.setBackground(Color.gray);
//      fi.setForeground(Color.white);
//      fi.setOpaque(true);
//      
//      panelChiFi.add(fi);
    
    }
    
    
private void Inicializa_config()
{
        
     //radiobutton to group
      buttonGroup1.add(radioRandomHoldout);
      buttonGroup1.add(radioSuppliedTest);
      buttonGroup1.add(radioStratifiedHoldout);
      buttonGroup1.add(radioRandomCV);
      buttonGroup1.add(radioStratifiedCV);
      
      radioRandomHoldout.setSelected(true);
      
      textRandomHoldout.setEnabled(true);
      buttonChooseSuppliedTest.setEnabled(false);
      
     // jTable1.setVisible(true);        
       
       cp3 = create_jchart(panelExamplesPerLabel,"bar","# Examples/label", "Relative frequency",false);
       cp11 =  create_jchart(panelLabelsPerExample,"line", "# Labels/example","Relative frequency",false);
       //crea el grafico box diagram
       cp_box =generaGrafico(panelBoxDiagram);
       
       cp22 = create_jchart(panelExamplesPerLabelset, "bar","# Examples/labelset","Relative frequency",false);
       
       cp_ir_x_label_intra_class = create_jchart(panelLabelsIRperLabelIntraClass, "line_2_axis", "Label id","IR values",true);
       cp_ir_x_label_inter_class = create_jchart(panelLabelsIRperLabelInterClass, "line_2_axis", "Label id","IR values",true);
       cp_ir_x_label_inter_class_only = create_jchart(panelIRperLabelInterClass, "line", "Label id","IR values",true);
       cp_ir_x_label_intra_class_only = create_jchart(panelIRperLabelIntraClass, "line", "Label id","IR values",true);
               
       cp_per_labelset = create_jchart(panelIRperLabelset, "line", "Labelset id","IR per labelset",false);
      
      create_jtable_metrics_jpanel1(jTable9,panelImbalanceDataMetrics,button_all_3,button_none_3,button_invert_3,button_calculate_3,button_save3,30,50,500,200,"imbalanced"); //imbalanced class
      
      create_jtable_metrics_jpanel1(jTable1,panelDataset,button_all_1,button_none_1,button_invert_1,button_calculate_1,button_save,30,195,500,270,"database"); //tab Database //35,155,500,355
      create_jtable_metrics_jpanel14(jTable8,panelMultipleDatasets,button_all_2,button_none_2,button_invert_2,button_calculate_2,button_save2,290,35,565,400); //
      
      create_jtable_metrics_jpanel2();
      
      jButton6.setEnabled(false);
      button_show_traintest.setEnabled(false);
      button_save_train.setEnabled(false);
            
      //CONFIG JTABLE FI AND CHI CO-OCURRENCES VALUES
      Inicializa_jtable_fi_chi();
      
      
      
      
      //CONFIG JTABLE CO-OCURRENCES VALUES
      
      jTable11.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      JScrollPane scrollPane = new JScrollPane(jTable11, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            
      scrollPane.setBounds(20, 20, 760, 350);
      jTable11.setBorder(BorderFactory.createLineBorder(Color.black));
      panelCoOcurrenceValues.add(scrollPane, BorderLayout.CENTER);
      
      //CONFIG JTABLE heapmap VALUES
      
      jTable12.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      scrollPane = new JScrollPane(jTable12, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            
      scrollPane.setBounds(20, 20, 760, 350);
      jTable12.setBorder(BorderFactory.createLineBorder(Color.black));
      panelHeatmapValues.add(scrollPane, BorderLayout.CENTER);
      
      
      //INICIALIZAR ELEMENTOS DE LA PESTAÑA TRAIN/TEST
      list_dataset_train = new ArrayList();
      list_dataset_test = new ArrayList();
      
      //BOTON EXPORTAR PESTAÑA DATASET
      
        
      create_button_export(jTable2,panelLabelFrequency,export1,20,285); //375
      export2 = create_button_export_jtable4(tableImbalance,panelImbalanceLeft,export2,80,455);
     
      create_button_export(jTable10,fixedTable ,panelChiFi ,export3, panelChiFi.getWidth()/2 - 40, 420); // chi and fi values //350, 370
      create_button_export(jTable11,fixedTable1,panelCoOcurrenceValues ,export4,350,370);//graph values
      create_button_export(jTable12,fixedTable2,panelHeatmapValues ,export5,350,370);//heatmap values
      
      create_button_export(panelCoOcurrence ,export6,450,405); //graph- dependences
      create_button_export(panelHeatmapGraph,export7,450,370);
      
      //create_button_export(jPanel21 ,export6,5,50);
      Border border = BorderFactory.createLineBorder(Color.gray, 1);
   
      
      
      labelHeatmapGraph.setBorder(border); 
      jTable10.setBorder(border);
      jTable11.setBorder(border);
      jTable12.setBorder(border);
      
      //jradio por defecto "PESTAÑA TRAIN TEST"
      textRandomHoldout.setEnabled(true);
        textStratifiedHoldout.setEnabled(false);
        buttonChooseSuppliedTest.setEnabled(false);
        textRandomCV.setEnabled(false);
        textStratifiedCV.setEnabled(false);
}
    //"TEST" TAB CONFIG
    private void create_jtable_metrics_jpanel2()
    {
     
     //COMMUN METRICS TRAIN/TEST
     JLabel label1 = new JLabel("Common metrics for train/test");
     panelTrainTest.add(label1);
     label1.setBounds(290, 20, 250, 20);
     label1.setFont(new Font("Arial", Font.BOLD, 12));
         
     
     create_jtable_metric(jTable5, panelTrainTest, util.Get_row_data_commun_data(), 285, 40, 565, 175);
     
     
   
          
     // METRICS TEST
     JLabel label3 = new JLabel("NOT common metrics for train/test set");
     panelTrainTest.add(label3);
     label3.setBounds(290, 220, 250, 20);
     label3.setFont(new Font("Arial", Font.BOLD, 12));
     
     
     
     create_jtable_metric(jTable7, panelTrainTest, util.Get_row_data_test_data(), 285, 240,565, 240);
     
     
     //button ALL
      JButton button_all2 = new JButton("All");
      button_all2.setBounds(285, 485, 80, 20);
            
      button_all2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_allActionPerformed2(evt,jTable5,jTable6,jTable7 );
                            }
        });
      panelTrainTest.add(button_all2);
      
      
     //button NONE
      JButton button_none2 = new JButton("None");
      button_none2.setBounds(385, 485, 80, 20);
            
      button_none2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
                button_noneActionPerformed2(evt,jTable5,jTable6,jTable7);
                            }
        });
      panelTrainTest.add(button_none2);
      
      //button INVERT
     JButton button_invert2 = new JButton("Invert");
      button_invert2.setBounds(485, 485, 80, 20);
            
      button_invert2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_invertActionPerformed2(evt,jTable5,jTable6,jTable7);
                            }
        });
      panelTrainTest.add(button_invert2);
      
      
         //button CALCULATE
      button_show_traintest = new JButton("Show");
      button_show_traintest.setBounds(670, 485, 80, 20);
            
      button_show_traintest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_calculateActionPerformed1_general(evt,jTable5,jTable6,jTable7);
                            }
        });
      panelTrainTest.add(button_show_traintest);
     
               //button SAVE
     button_save_train = new JButton("Save");
      button_save_train.setBounds(770, 485, 80, 20);
            
      button_save_train.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    button_saveActionPerformed1(evt,jTable5,jTable6,jTable7);
                    Toolkit.getDefaultToolkit().beep();
                    //button_saveActionPerformed1(evt,jTable5,jTable6,jTable7);
                } catch (IOException ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                            }
        });
      panelTrainTest.add(button_save_train);
     
        
    }

  
    
    private HeatChart create_heapmap(JLabel jlabel, MultiLabelInstances dataset, int[] labels_names)
    {
       double[][] data= util.Get_data_heapmap(dataset, labels_names);
       
     //  util.Recorre_Arreglo_2_dimensiones(data);
       
       // Create our heat chart using our data.
       data = util.Invertir_Matrix(data);
       HeatChart chart = new HeatChart(data);
       
    chart.setAxisColour(Color.black);
    chart.setAxisValuesColour(Color.black);
  
    chart.setShowXAxisValues(false);
    chart.setShowYAxisValues(false);
    chart.setHighValueColour(Color.white);
    chart.setLowValueColour(Color.black);
  
    chart.setChartMargin(0);
    
    
    int cant_labels=labels_names.length;
    
    if(cant_labels<30)chart.setCellSize(new Dimension(15,15));
    if(cant_labels>30 && cant_labels <60 ) chart.setCellSize(new Dimension(9,6));
    if(cant_labels >60 && cant_labels < 102)chart.setCellSize(new Dimension(4,3));
    if(cant_labels >102 && cant_labels < 250)chart.setCellSize(new Dimension(3,2));
    if(cant_labels >250 && cant_labels < 500)chart.setCellSize(new Dimension(2,1));
    if(cant_labels >500)
    {
        JOptionPane.showMessageDialog(null, "Can´t represent the heapmap", "Warning", JOptionPane.ERROR_MESSAGE);
        return null;
    }   
    
    ImageIcon temp = new ImageIcon(chart.getChartImage());
    
    jlabel.setIcon(temp);
    jlabel.setHorizontalAlignment(JLabel.CENTER);
   
    
       // System.out.println(" las dimensiones del chart son "+chart.getCellSize().height +" alto "+ chart.getCellSize().width);
    
        return chart;
    }     
    
    
    private HeatChart create_heapmap(JLabel jlabel, MultiLabelInstances dataset)
    {
       double[][] data= util.Get_data_heapmap(dataset);
       
     //  util.Recorre_Arreglo_2_dimensiones(data);
       
       // Create our heat chart using our data.
       data = util.Invertir_Matrix(data);
       HeatChart chart = new HeatChart(data);
       
    chart.setAxisColour(Color.black);
    chart.setAxisValuesColour(Color.black);
  
    chart.setShowXAxisValues(false);
    chart.setShowYAxisValues(false);
    chart.setHighValueColour(Color.white);
    chart.setLowValueColour(Color.black);
  
    chart.setChartMargin(0);
    
    int cant_labels=dataset.getNumLabels();
    
    if(cant_labels<30)chart.setCellSize(new Dimension(20,20));
    if(cant_labels>30 && cant_labels <60 ) chart.setCellSize(new Dimension(9,6));
    if(cant_labels >60 && cant_labels < 102)chart.setCellSize(new Dimension(4,3));
    if(cant_labels >102 && cant_labels < 250)chart.setCellSize(new Dimension(3,2));
    if(cant_labels >250 && cant_labels < 500)chart.setCellSize(new Dimension(2,1));
    if(cant_labels >500)
    {
        JOptionPane.showMessageDialog(null, "Can´t represent the heapmap", "Warning", JOptionPane.ERROR_MESSAGE);
        return null;
    }   
    
    ImageIcon temp = new ImageIcon(chart.getChartImage());
    
    jlabel.setIcon(temp);
    jlabel.setHorizontalAlignment(JLabel.CENTER);
   
    
    return chart;
    
    }     
    
   
        private void create_button_export(final JTable jtable, JPanel jpanel, JButton jbutton_export, int posx,int posy)
    {
      //button export table
      jbutton_export = new JButton("Save");
      jbutton_export.setBounds(posx, posy, 80, 20);
     
      jbutton_export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_export_ActionPerformed(evt,jtable );
                            }
        });
      jpanel.add(jbutton_export);
    }
        
         private JButton create_button_export_jtable4(final JTable jtable, JPanel jpanel, JButton jbutton_export, int posx,int posy)
    {
      //button export table
      jbutton_export = new JButton("Save");
      jbutton_export.setBounds(posx, posy, 80, 20);
      jbutton_export.setVisible(false);      
      jbutton_export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_export_ActionPerformed(evt,jtable );
                            }
        });
      jpanel.add(jbutton_export);
      return jbutton_export;
    }
        
            private void create_button_export(final JTable jtable,final JTable columns, JPanel jpanel, JButton jbutton_export, int posx,int posy)
    {
      //button export table
      jbutton_export = new JButton("Save");
      jbutton_export.setBounds(posx, posy, 80, 20);
            
      jbutton_export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_export_ActionPerformed(evt,jtable,columns );
                            }
        });
      jpanel.add(jbutton_export);
    }    
        
  private void create_button_export(final JPanel jpanel, JButton jbutton_export, int posx,int posy)
    {
      //button export table
      jbutton_export = new JButton("Save");
      jbutton_export.setBounds(posx, posy, 80, 20);
            
      jbutton_export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
              if(jpanel.getName().equals("jpanel25")) try {  
                  save_as_ActionPerformed(evt);
              } catch (AWTException ex) {
                  Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
              } catch (IOException ex) {
                  Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
              }  
              else try {
                  save_as_ActionPerformed1(evt);
              } catch (AWTException ex) {
                  Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
              } catch (IOException ex) {
                  Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
              }  
                            }
        });
      jpanel.add(jbutton_export);
    }
  
   private void create_button_export(JButton jbutton_export, int posx,int posy)
    {
      //button export table
      jbutton_export = new JButton("Save");
      jbutton_export.setBounds(posx, posy, 80, 20);
            
      jbutton_export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {               
                    save_as_ActionPerformed1(evt);
                } catch (AWTException ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                            }
        });
      panelHeatmapGraph.add(jbutton_export);
    }

  //"Dataset" TAB CONFIG    
    private void create_jtable_metrics_jpanel1(final JTable jtable ,JPanel jpanel , JButton button_all, JButton button_none, JButton button_invert, JButton button_show_dataset,JButton button_save, int posx,int posy, int width, int heigh,String info)
    {
        if(info.equals("imbalanced"))
        create_jtable_metric(jtable,jpanel, util.Get_row_data_imbalanced(),posx,posy,width,heigh);  
        
        if(info.equals("database"))
        create_jtable_metric(jtable,jpanel, util.Get_row_data(),posx,posy,width,heigh);  
        
        
       //button ALL
      button_all = new JButton("All");
      button_all.setBounds(posx, posy+heigh+5, 80, 20);
            
      button_all.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_allActionPerformed(evt,jtable );
                            }
        });
      jpanel.add(button_all);
      
      
     //button NONE
      button_none = new JButton("None");
      button_none.setBounds(posx+100, posy+heigh+5, 80, 20);
            
      button_none.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_noneActionPerformed(evt,jtable);
                            }
        });
      jpanel.add(button_none);
      
      //button INVERT
      button_invert = new JButton("Invert");
      button_invert.setBounds(posx+200, posy+heigh+5, 80, 20);
            
      button_invert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_invertActionPerformed(evt,jtable);
                            }
        });
      jpanel.add(button_invert);
      
         //button CALCULATE
      button_show_dataset = new JButton("Show");
      button_show_dataset.setBounds(posx+420, posy+heigh+5, 80, 20);
            
      button_show_dataset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_calculateActionPerformed(evt,jtable);
                            }
        });
      jpanel.add(button_show_dataset);
      
      
       //button SAVE
      button_save = new JButton("Save");
      button_save.setBounds(posx+420, posy+heigh+25, 80, 20);
            
      button_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    button_saveActionPerformed(evt,jtable);
                } catch (IOException ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                            }
        });
      jpanel.add(button_save);
      
    }


  //"choose datasets" PESTAÑA
    
    private void create_jtable_metrics_jpanel14(final JTable jtable ,JPanel jpanel , JButton button_all, JButton button_none, JButton button_invert, JButton button_calculate,JButton button_save, int posx,int posy, int width, int heigh)
    {
        
        create_jtable_metric(jtable,jpanel, util.Get_row_data_multi_datasets(),posx,posy,width,heigh);  
       //button ALL
      button_all = new JButton("All");
      button_all.setBounds(posx, posy+405, 80, 20);
            
      button_all.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_allActionPerformed(evt,jtable );
                            }
        });
      jpanel.add(button_all);
      
      
     //button NONE
      button_none = new JButton("None");
      button_none.setBounds(posx+100, posy+405, 80, 20);
            
      button_none.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_noneActionPerformed(evt,jtable);
                            }
        });
      jpanel.add(button_none);
      
      //button INVERT
      button_invert = new JButton("Invert");
      button_invert.setBounds(posx+200, posy+405, 80, 20);
            
      button_invert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_invertActionPerformed(evt,jtable);
                            }
        });
      jpanel.add(button_invert);
      
         //button CALCULATE
      button_calculate = new JButton("Show");
      button_calculate.setBounds(posx+480, posy+405, 80, 20);
            
      button_calculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_calculateActionPerformed_datasets(evt,jtable);
                            }
        });
      jpanel.add(button_calculate);
      
      
       //button SAVE
      button_save = new JButton("Save");
      button_save.setBounds(posx+480, posy+425, 80, 20);
            
      button_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    button_saveActionPerformed_multidatasets(evt,jtable);
                } catch (IOException ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                            }
        });
      jpanel.add(button_save);
      
      
             //button Graphics
      button_save = new JButton("# Labels per example");
      button_save.setBounds(posx+390, posy+445,170, 20);
            
      button_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    button_graphicActionPerformed_multidatasets(evt);
                } catch (IOException ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                            }
        });
      jpanel.add(button_save);
      
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        buttonGroup5 = new javax.swing.ButtonGroup();
        TabPrincipal = new javax.swing.JTabbedPane();
        panelDataset = new javax.swing.JPanel();
        buttonChooseFile = new javax.swing.JButton();
        panelLabelFrequency = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        textChooseFile = new javax.swing.JTextField();
        panelCurrentDataset = new javax.swing.JPanel();
        labelRelation = new javax.swing.JLabel();
        labelInstances = new javax.swing.JLabel();
        labelRelationValue = new javax.swing.JLabel();
        labelInstancesValue = new javax.swing.JLabel();
        labelAttributes = new javax.swing.JLabel();
        labelLabels = new javax.swing.JLabel();
        labelAttributesValue = new javax.swing.JLabel();
        labelLabelsValue = new javax.swing.JLabel();
        labelDensity = new javax.swing.JLabel();
        labelCardinality = new javax.swing.JLabel();
        labelDensityValue = new javax.swing.JLabel();
        labelCardinalityValue = new javax.swing.JLabel();
        labelDistinct = new javax.swing.JLabel();
        labelBound = new javax.swing.JLabel();
        labelDistinctValue = new javax.swing.JLabel();
        labelBoundValue = new javax.swing.JLabel();
        labelDiversity = new javax.swing.JLabel();
        labelDiversityValue = new javax.swing.JLabel();
        labelLxIxF = new javax.swing.JLabel();
        labelLxIxFValue = new javax.swing.JLabel();
        panelTrainTest = new javax.swing.JPanel();
        panelTestOption = new javax.swing.JPanel();
        radioRandomHoldout = new javax.swing.JRadioButton();
        radioSuppliedTest = new javax.swing.JRadioButton();
        buttonChooseSuppliedTest = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        labelPercStratified = new javax.swing.JLabel();
        radioStratifiedHoldout = new javax.swing.JRadioButton();
        textRandomHoldout = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        labelHoldout = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        labelCV = new javax.swing.JLabel();
        radioRandomCV = new javax.swing.JRadioButton();
        radioStratifiedCV = new javax.swing.JRadioButton();
        textStratifiedCV = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        labelFoldsRandom = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        textRandomCV = new javax.swing.JTextField();
        labelFoldsStratified = new javax.swing.JLabel();
        labelPercRandom = new javax.swing.JLabel();
        textStratifiedHoldout = new javax.swing.JTextField();
        panelImbalance = new javax.swing.JPanel();
        tabsImbalance = new javax.swing.JTabbedPane();
        panelExamplesPerLabel = new javax.swing.JPanel();
        panelExamplesPerLabelset = new javax.swing.JPanel();
        panelLabelsPerExample = new javax.swing.JPanel();
        panelLabelsIRperLabelIntraClass = new javax.swing.JPanel();
        panelIRperLabelIntraClass = new javax.swing.JPanel();
        panelIRperLabelset = new javax.swing.JPanel();
        panelImbalanceDataMetrics = new javax.swing.JPanel();
        panelBoxDiagram = new javax.swing.JPanel();
        panelIRperLabelInterClass = new javax.swing.JPanel();
        panel1 = new java.awt.Panel();
        panelLabelsIRperLabelInterClass = new javax.swing.JPanel();
        panelImbalanceLeft = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableImbalance = new javax.swing.JTable();
        labelIR1 = new javax.swing.JLabel();
        labelIR2 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        radioExamplesPerLabel = new javax.swing.JRadioButton();
        radioExamplesPerLabelset = new javax.swing.JRadioButton();
        jPanel21 = new javax.swing.JPanel();
        tabsDependences = new javax.swing.JTabbedPane();
        panelChiFi = new javax.swing.JPanel();
        labelChiThreshold = new javax.swing.JLabel();
        labelChiCoefficients = new javax.swing.JLabel();
        labelChiCoefficients1 = new javax.swing.JLabel();
        panelCoOcurrence = new javax.swing.JPanel();
        panelCoOcurrenceRight = new javax.swing.JPanel();
        panelCoOcurrenceLeft = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tableCoOcurrenceLeft = new javax.swing.JTable();
        buttonShowCoOcurrence = new javax.swing.JButton();
        panelCoOcurrenceValues = new javax.swing.JPanel();
        panelHeatmapGraph = new javax.swing.JPanel();
        labelHeatmapGraph = new javax.swing.JLabel();
        panelHeatmapLeft = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tableHeatmapLeft = new javax.swing.JTable();
        buttonShowHeatmapLeft = new javax.swing.JButton();
        panelHeatmapValues = new javax.swing.JPanel();
        panelMultipleDatasets = new javax.swing.JPanel();
        panelMultipleDatasetsLeft = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listMultipleDatasetsLeft = new javax.swing.JList();
        buttonAddMultipleDatasets = new javax.swing.JButton();
        buttonRemoveMultipleDatasets = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        TabPrincipal.setDoubleBuffered(true);

        buttonChooseFile.setText("Choose file");
        buttonChooseFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonChooseFileActionPerformed(evt);
            }
        });

        panelLabelFrequency.setBorder(javax.swing.BorderFactory.createTitledBorder("Label frequency"));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Label", "# Examples", "Frequency"
            }
        ));
        jScrollPane3.setViewportView(jTable2);

        javax.swing.GroupLayout panelLabelFrequencyLayout = new javax.swing.GroupLayout(panelLabelFrequency);
        panelLabelFrequency.setLayout(panelLabelFrequencyLayout);
        panelLabelFrequencyLayout.setHorizontalGroup(
            panelLabelFrequencyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
        );
        panelLabelFrequencyLayout.setVerticalGroup(
            panelLabelFrequencyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLabelFrequencyLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 32, Short.MAX_VALUE))
        );

        textChooseFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textChooseFileActionPerformed(evt);
            }
        });
        textChooseFile.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textChooseFileFocusLost(evt);
            }
        });
        textChooseFile.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textChooseFileKeyPressed(evt);
            }
        });

        panelCurrentDataset.setBorder(javax.swing.BorderFactory.createTitledBorder("Current dataset"));

        labelRelation.setText("Relation:");
        labelRelation.setName(""); // NOI18N

        labelInstances.setText("Instances:");

        labelRelationValue.setName(""); // NOI18N

        labelInstancesValue.setName(""); // NOI18N

        labelAttributes.setText("Attributes:");

        labelLabels.setText("Labels:");

        labelAttributesValue.setName(""); // NOI18N

        labelLabelsValue.setName(""); // NOI18N

        labelDensity.setText("Density:");

        labelCardinality.setText("Cardinality:");
        labelCardinality.setMaximumSize(new java.awt.Dimension(80, 18));

        labelDensityValue.setName(""); // NOI18N

        labelCardinalityValue.setName(""); // NOI18N

        labelDistinct.setText("Distinct labelset:");

        labelBound.setText("Bound:");

        labelDistinctValue.setName(""); // NOI18N

        labelBoundValue.setName(""); // NOI18N

        labelDiversity.setText("Diversity:");

        labelDiversityValue.setName(""); // NOI18N

        labelLxIxF.setText("Labels * instances * features:");

        labelLxIxFValue.setName(""); // NOI18N

        javax.swing.GroupLayout panelCurrentDatasetLayout = new javax.swing.GroupLayout(panelCurrentDataset);
        panelCurrentDataset.setLayout(panelCurrentDatasetLayout);
        panelCurrentDatasetLayout.setHorizontalGroup(
            panelCurrentDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCurrentDatasetLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCurrentDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCurrentDatasetLayout.createSequentialGroup()
                        .addGroup(panelCurrentDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelCurrentDatasetLayout.createSequentialGroup()
                                .addGroup(panelCurrentDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(panelCurrentDatasetLayout.createSequentialGroup()
                                        .addComponent(labelInstances)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(labelInstancesValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(panelCurrentDatasetLayout.createSequentialGroup()
                                        .addComponent(labelCardinality, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(labelCardinalityValue, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelCurrentDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(panelCurrentDatasetLayout.createSequentialGroup()
                                        .addComponent(labelDiversity)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(labelDiversityValue, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelCurrentDatasetLayout.createSequentialGroup()
                                        .addComponent(labelAttributes)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(labelAttributesValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(panelCurrentDatasetLayout.createSequentialGroup()
                                .addComponent(labelRelation)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelRelationValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(11, 11, 11)
                        .addGroup(panelCurrentDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCurrentDatasetLayout.createSequentialGroup()
                                .addComponent(labelLabels)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCurrentDatasetLayout.createSequentialGroup()
                                .addComponent(labelBound)
                                .addGap(5, 5, 5)))
                        .addGroup(panelCurrentDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(labelLabelsValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelBoundValue, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelCurrentDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelCurrentDatasetLayout.createSequentialGroup()
                                .addComponent(labelDistinct)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelDistinctValue, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelCurrentDatasetLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(labelDensity)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelDensityValue, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelCurrentDatasetLayout.createSequentialGroup()
                        .addComponent(labelLxIxF)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelLxIxFValue)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelCurrentDatasetLayout.setVerticalGroup(
            panelCurrentDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCurrentDatasetLayout.createSequentialGroup()
                .addGroup(panelCurrentDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelRelation)
                    .addComponent(labelRelationValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelCurrentDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelInstances)
                    .addComponent(labelInstancesValue)
                    .addComponent(labelAttributes)
                    .addComponent(labelAttributesValue)
                    .addComponent(labelLabels)
                    .addComponent(labelLabelsValue)
                    .addComponent(labelDensity)
                    .addComponent(labelDensityValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelCurrentDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCardinality, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCardinalityValue)
                    .addComponent(labelDiversity)
                    .addComponent(labelDiversityValue)
                    .addComponent(labelBound)
                    .addComponent(labelBoundValue)
                    .addComponent(labelDistinct)
                    .addComponent(labelDistinctValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelCurrentDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelLxIxF)
                    .addComponent(labelLxIxFValue, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout panelDatasetLayout = new javax.swing.GroupLayout(panelDataset);
        panelDataset.setLayout(panelDatasetLayout);
        panelDatasetLayout.setHorizontalGroup(
            panelDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatasetLayout.createSequentialGroup()
                .addGroup(panelDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelDatasetLayout.createSequentialGroup()
                        .addContainerGap(570, Short.MAX_VALUE)
                        .addComponent(panelLabelFrequency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelDatasetLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(panelDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelDatasetLayout.createSequentialGroup()
                                .addComponent(textChooseFile, javax.swing.GroupLayout.PREFERRED_SIZE, 701, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(buttonChooseFile, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE))
                            .addComponent(panelCurrentDataset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(26, 26, 26))
        );
        panelDatasetLayout.setVerticalGroup(
            panelDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatasetLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonChooseFile)
                    .addComponent(textChooseFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelCurrentDataset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelLabelFrequency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        TabPrincipal.addTab("Dataset", panelDataset);

        panelTestOption.setBorder(javax.swing.BorderFactory.createTitledBorder("Test option"));

        radioRandomHoldout.setText("Random  holdout");
        radioRandomHoldout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioRandomHoldoutActionPerformed(evt);
            }
        });

        radioSuppliedTest.setText("Supplied test set");
        radioSuppliedTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioSuppliedTestActionPerformed(evt);
            }
        });

        buttonChooseSuppliedTest.setText("choose");
        buttonChooseSuppliedTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonChooseSuppliedTestActionPerformed(evt);
            }
        });

        jButton3.setText("Start");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        labelPercStratified.setText("%");

        radioStratifiedHoldout.setText("Stratified holdout ");
        radioStratifiedHoldout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioStratifiedHoldoutActionPerformed(evt);
            }
        });

        textRandomHoldout.setText("70");
        textRandomHoldout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textRandomHoldoutActionPerformed(evt);
            }
        });
        textRandomHoldout.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textRandomHoldoutKeyTyped(evt);
            }
        });

        labelHoldout.setText("Holdout");

        labelCV.setText("Cross validation");

        radioRandomCV.setText("Random CV");
        radioRandomCV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioRandomCVActionPerformed(evt);
            }
        });

        radioStratifiedCV.setText("Stratified CV");
        radioStratifiedCV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioStratifiedCVActionPerformed(evt);
            }
        });

        textStratifiedCV.setText("5");
        textStratifiedCV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textStratifiedCVActionPerformed(evt);
            }
        });
        textStratifiedCV.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textStratifiedCVKeyTyped(evt);
            }
        });

        labelFoldsRandom.setText("Folds");

        jButton6.setText("Save datasets");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        textRandomCV.setText("5");
        textRandomCV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textRandomCVActionPerformed(evt);
            }
        });
        textRandomCV.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textRandomCVKeyTyped(evt);
            }
        });

        labelFoldsStratified.setText("Folds");

        labelPercRandom.setText("%");

        textStratifiedHoldout.setText("70");
        textStratifiedHoldout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textStratifiedHoldoutActionPerformed(evt);
            }
        });
        textStratifiedHoldout.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textStratifiedHoldoutKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout panelTestOptionLayout = new javax.swing.GroupLayout(panelTestOption);
        panelTestOption.setLayout(panelTestOptionLayout);
        panelTestOptionLayout.setHorizontalGroup(
            panelTestOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator3)
            .addComponent(jSeparator2)
            .addComponent(jSeparator1)
            .addGroup(panelTestOptionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTestOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTestOptionLayout.createSequentialGroup()
                        .addComponent(radioSuppliedTest)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonChooseSuppliedTest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelTestOptionLayout.createSequentialGroup()
                        .addGroup(panelTestOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelCV)
                            .addGroup(panelTestOptionLayout.createSequentialGroup()
                                .addGroup(panelTestOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(labelHoldout)
                                    .addGroup(panelTestOptionLayout.createSequentialGroup()
                                        .addComponent(radioStratifiedHoldout)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(textStratifiedHoldout, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelTestOptionLayout.createSequentialGroup()
                                        .addComponent(radioRandomHoldout)
                                        .addGap(18, 18, 18)
                                        .addComponent(textRandomHoldout, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelTestOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(labelPercRandom)
                                    .addComponent(labelPercStratified)))
                            .addGroup(panelTestOptionLayout.createSequentialGroup()
                                .addComponent(radioRandomCV)
                                .addGap(25, 25, 25)
                                .addComponent(textRandomCV, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelFoldsRandom))
                            .addGroup(panelTestOptionLayout.createSequentialGroup()
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelTestOptionLayout.createSequentialGroup()
                                .addComponent(radioStratifiedCV)
                                .addGap(18, 18, 18)
                                .addComponent(textStratifiedCV, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelFoldsStratified)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelTestOptionLayout.setVerticalGroup(
            panelTestOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTestOptionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTestOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioSuppliedTest)
                    .addComponent(buttonChooseSuppliedTest))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelHoldout)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelTestOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioRandomHoldout)
                    .addComponent(textRandomHoldout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPercRandom))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelTestOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioStratifiedHoldout)
                    .addComponent(labelPercStratified, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textStratifiedHoldout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelCV)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelTestOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioRandomCV)
                    .addComponent(labelFoldsRandom)
                    .addComponent(textRandomCV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelTestOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioStratifiedCV)
                    .addComponent(textStratifiedCV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelFoldsStratified))
                .addGap(8, 8, 8)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelTestOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelTrainTestLayout = new javax.swing.GroupLayout(panelTrainTest);
        panelTrainTest.setLayout(panelTrainTestLayout);
        panelTrainTestLayout.setHorizontalGroup(
            panelTrainTestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTrainTestLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTestOption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(620, Short.MAX_VALUE))
        );
        panelTrainTestLayout.setVerticalGroup(
            panelTrainTestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTrainTestLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(panelTestOption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(192, Short.MAX_VALUE))
        );

        TabPrincipal.addTab("Train/test dataset", panelTrainTest);

        tabsImbalance.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabsImbalanceStateChanged(evt);
            }
        });

        javax.swing.GroupLayout panelExamplesPerLabelLayout = new javax.swing.GroupLayout(panelExamplesPerLabel);
        panelExamplesPerLabel.setLayout(panelExamplesPerLabelLayout);
        panelExamplesPerLabelLayout.setHorizontalGroup(
            panelExamplesPerLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 548, Short.MAX_VALUE)
        );
        panelExamplesPerLabelLayout.setVerticalGroup(
            panelExamplesPerLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("# Examples per label", panelExamplesPerLabel);

        javax.swing.GroupLayout panelExamplesPerLabelsetLayout = new javax.swing.GroupLayout(panelExamplesPerLabelset);
        panelExamplesPerLabelset.setLayout(panelExamplesPerLabelsetLayout);
        panelExamplesPerLabelsetLayout.setHorizontalGroup(
            panelExamplesPerLabelsetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 548, Short.MAX_VALUE)
        );
        panelExamplesPerLabelsetLayout.setVerticalGroup(
            panelExamplesPerLabelsetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("# Examples per labelset", panelExamplesPerLabelset);

        javax.swing.GroupLayout panelLabelsPerExampleLayout = new javax.swing.GroupLayout(panelLabelsPerExample);
        panelLabelsPerExample.setLayout(panelLabelsPerExampleLayout);
        panelLabelsPerExampleLayout.setHorizontalGroup(
            panelLabelsPerExampleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 548, Short.MAX_VALUE)
        );
        panelLabelsPerExampleLayout.setVerticalGroup(
            panelLabelsPerExampleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("# Labels per example ", panelLabelsPerExample);

        javax.swing.GroupLayout panelLabelsIRperLabelIntraClassLayout = new javax.swing.GroupLayout(panelLabelsIRperLabelIntraClass);
        panelLabelsIRperLabelIntraClass.setLayout(panelLabelsIRperLabelIntraClassLayout);
        panelLabelsIRperLabelIntraClassLayout.setHorizontalGroup(
            panelLabelsIRperLabelIntraClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 548, Short.MAX_VALUE)
        );
        panelLabelsIRperLabelIntraClassLayout.setVerticalGroup(
            panelLabelsIRperLabelIntraClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("#Labels/IR per label intra class", panelLabelsIRperLabelIntraClass);

        javax.swing.GroupLayout panelIRperLabelIntraClassLayout = new javax.swing.GroupLayout(panelIRperLabelIntraClass);
        panelIRperLabelIntraClass.setLayout(panelIRperLabelIntraClassLayout);
        panelIRperLabelIntraClassLayout.setHorizontalGroup(
            panelIRperLabelIntraClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 548, Short.MAX_VALUE)
        );
        panelIRperLabelIntraClassLayout.setVerticalGroup(
            panelIRperLabelIntraClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("IR per label intra class", panelIRperLabelIntraClass);

        javax.swing.GroupLayout panelIRperLabelsetLayout = new javax.swing.GroupLayout(panelIRperLabelset);
        panelIRperLabelset.setLayout(panelIRperLabelsetLayout);
        panelIRperLabelsetLayout.setHorizontalGroup(
            panelIRperLabelsetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 548, Short.MAX_VALUE)
        );
        panelIRperLabelsetLayout.setVerticalGroup(
            panelIRperLabelsetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("IR per labelset", panelIRperLabelset);

        javax.swing.GroupLayout panelImbalanceDataMetricsLayout = new javax.swing.GroupLayout(panelImbalanceDataMetrics);
        panelImbalanceDataMetrics.setLayout(panelImbalanceDataMetricsLayout);
        panelImbalanceDataMetricsLayout.setHorizontalGroup(
            panelImbalanceDataMetricsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 548, Short.MAX_VALUE)
        );
        panelImbalanceDataMetricsLayout.setVerticalGroup(
            panelImbalanceDataMetricsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("Imbalance data metrics ", panelImbalanceDataMetrics);

        javax.swing.GroupLayout panelBoxDiagramLayout = new javax.swing.GroupLayout(panelBoxDiagram);
        panelBoxDiagram.setLayout(panelBoxDiagramLayout);
        panelBoxDiagramLayout.setHorizontalGroup(
            panelBoxDiagramLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 548, Short.MAX_VALUE)
        );
        panelBoxDiagramLayout.setVerticalGroup(
            panelBoxDiagramLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("Box diagram", panelBoxDiagram);

        javax.swing.GroupLayout panelIRperLabelInterClassLayout = new javax.swing.GroupLayout(panelIRperLabelInterClass);
        panelIRperLabelInterClass.setLayout(panelIRperLabelInterClassLayout);
        panelIRperLabelInterClassLayout.setHorizontalGroup(
            panelIRperLabelInterClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 548, Short.MAX_VALUE)
        );
        panelIRperLabelInterClassLayout.setVerticalGroup(
            panelIRperLabelInterClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("IR per label inter class", panelIRperLabelInterClass);

        javax.swing.GroupLayout panelLabelsIRperLabelInterClassLayout = new javax.swing.GroupLayout(panelLabelsIRperLabelInterClass);
        panelLabelsIRperLabelInterClass.setLayout(panelLabelsIRperLabelInterClassLayout);
        panelLabelsIRperLabelInterClassLayout.setHorizontalGroup(
            panelLabelsIRperLabelInterClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 548, Short.MAX_VALUE)
        );
        panelLabelsIRperLabelInterClassLayout.setVerticalGroup(
            panelLabelsIRperLabelInterClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelLabelsIRperLabelInterClass, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelLabelsIRperLabelInterClass, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("#Labels/IR per label inter class", panel1);

        tableImbalance.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableImbalance.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableImbalanceMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tableImbalance);

        labelIR1.setText("When IR> 1.5, the value is imbalanced");

        labelIR2.setText(" and it is marked with a red color.");

        javax.swing.GroupLayout panelImbalanceLeftLayout = new javax.swing.GroupLayout(panelImbalanceLeft);
        panelImbalanceLeft.setLayout(panelImbalanceLeftLayout);
        panelImbalanceLeftLayout.setHorizontalGroup(
            panelImbalanceLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(panelImbalanceLeftLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelImbalanceLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelIR1)
                    .addComponent(labelIR2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelImbalanceLeftLayout.setVerticalGroup(
            panelImbalanceLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelImbalanceLeftLayout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelIR1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelIR2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        radioExamplesPerLabel.setText("# Examples per Label");
        radioExamplesPerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radioExamplesPerLabelMouseClicked(evt);
            }
        });
        radioExamplesPerLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioExamplesPerLabelActionPerformed(evt);
            }
        });

        radioExamplesPerLabelset.setText("# Examples per labelset");
        radioExamplesPerLabelset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radioExamplesPerLabelsetMouseClicked(evt);
            }
        });
        radioExamplesPerLabelset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioExamplesPerLabelsetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(radioExamplesPerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(radioExamplesPerLabelset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(radioExamplesPerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(radioExamplesPerLabelset, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout panelImbalanceLayout = new javax.swing.GroupLayout(panelImbalance);
        panelImbalance.setLayout(panelImbalanceLayout);
        panelImbalanceLayout.setHorizontalGroup(
            panelImbalanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelImbalanceLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelImbalanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelImbalanceLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabsImbalance, javax.swing.GroupLayout.PREFERRED_SIZE, 553, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelImbalanceLayout.setVerticalGroup(
            panelImbalanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelImbalanceLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelImbalanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabsImbalance, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(panelImbalanceLayout.createSequentialGroup()
                        .addComponent(panelImbalanceLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        panelImbalanceLeft.getAccessibleContext().setAccessibleName("pepe");

        TabPrincipal.addTab("Imbalance data", panelImbalance);

        tabsDependences.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabsDependencesMouseClicked(evt);
            }
        });
        tabsDependences.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabsDependencesStateChanged(evt);
            }
        });

        labelChiThreshold.setText("Chi coefficient values are marked in red when the labels are dependent (Chi > 6.635)");

        labelChiCoefficients.setBackground(new java.awt.Color(254, 254, 254));
        labelChiCoefficients.setText("  Chi coefficients  ");
        labelChiCoefficients.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labelChiCoefficients.setOpaque(true);

        labelChiCoefficients1.setBackground(java.awt.Color.lightGray);
        labelChiCoefficients1.setForeground(new java.awt.Color(1, 1, 1));
        labelChiCoefficients1.setText("  Phi coefficients  ");
        labelChiCoefficients1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labelChiCoefficients1.setOpaque(true);

        javax.swing.GroupLayout panelChiFiLayout = new javax.swing.GroupLayout(panelChiFi);
        panelChiFi.setLayout(panelChiFiLayout);
        panelChiFiLayout.setHorizontalGroup(
            panelChiFiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChiFiLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(panelChiFiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelChiThreshold)
                    .addGroup(panelChiFiLayout.createSequentialGroup()
                        .addComponent(labelChiCoefficients)
                        .addGap(18, 18, 18)
                        .addComponent(labelChiCoefficients1)))
                .addContainerGap(239, Short.MAX_VALUE))
        );
        panelChiFiLayout.setVerticalGroup(
            panelChiFiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelChiFiLayout.createSequentialGroup()
                .addContainerGap(418, Short.MAX_VALUE)
                .addGroup(panelChiFiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelChiCoefficients)
                    .addComponent(labelChiCoefficients1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelChiThreshold)
                .addContainerGap())
        );

        tabsDependences.addTab("Chi & Fi coefficient", panelChiFi);

        panelCoOcurrence.setName("jpanel25"); // NOI18N
        panelCoOcurrence.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                panelCoOcurrenceMouseReleased(evt);
            }
        });

        panelCoOcurrenceRight.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panelCoOcurrenceRight.setName("jpanel10"); // NOI18N

        javax.swing.GroupLayout panelCoOcurrenceRightLayout = new javax.swing.GroupLayout(panelCoOcurrenceRight);
        panelCoOcurrenceRight.setLayout(panelCoOcurrenceRightLayout);
        panelCoOcurrenceRightLayout.setHorizontalGroup(
            panelCoOcurrenceRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 603, Short.MAX_VALUE)
        );
        panelCoOcurrenceRightLayout.setVerticalGroup(
            panelCoOcurrenceRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 379, Short.MAX_VALUE)
        );

        panelCoOcurrenceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        tableCoOcurrenceLeft.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableCoOcurrenceLeft.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableCoOcurrenceLeftMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tableCoOcurrenceLeft);

        buttonShowCoOcurrence.setText("Show");
        buttonShowCoOcurrence.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowCoOcurrenceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCoOcurrenceLeftLayout = new javax.swing.GroupLayout(panelCoOcurrenceLeft);
        panelCoOcurrenceLeft.setLayout(panelCoOcurrenceLeftLayout);
        panelCoOcurrenceLeftLayout.setHorizontalGroup(
            panelCoOcurrenceLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCoOcurrenceLeftLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(panelCoOcurrenceLeftLayout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(buttonShowCoOcurrence)
                .addContainerGap(81, Short.MAX_VALUE))
        );
        panelCoOcurrenceLeftLayout.setVerticalGroup(
            panelCoOcurrenceLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCoOcurrenceLeftLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonShowCoOcurrence, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout panelCoOcurrenceLayout = new javax.swing.GroupLayout(panelCoOcurrence);
        panelCoOcurrence.setLayout(panelCoOcurrenceLayout);
        panelCoOcurrenceLayout.setHorizontalGroup(
            panelCoOcurrenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCoOcurrenceLayout.createSequentialGroup()
                .addComponent(panelCoOcurrenceLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelCoOcurrenceRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelCoOcurrenceLayout.setVerticalGroup(
            panelCoOcurrenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCoOcurrenceLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCoOcurrenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelCoOcurrenceLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelCoOcurrenceRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(61, Short.MAX_VALUE))
        );

        tabsDependences.addTab("Co-occurrence graph", panelCoOcurrence);

        javax.swing.GroupLayout panelCoOcurrenceValuesLayout = new javax.swing.GroupLayout(panelCoOcurrenceValues);
        panelCoOcurrenceValues.setLayout(panelCoOcurrenceValuesLayout);
        panelCoOcurrenceValuesLayout.setHorizontalGroup(
            panelCoOcurrenceValuesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 841, Short.MAX_VALUE)
        );
        panelCoOcurrenceValuesLayout.setVerticalGroup(
            panelCoOcurrenceValuesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 494, Short.MAX_VALUE)
        );

        tabsDependences.addTab("Co-occurrence values", panelCoOcurrenceValues);

        panelHeatmapGraph.setName("jpanel26"); // NOI18N

        labelHeatmapGraph.setText(" ");
        labelHeatmapGraph.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                labelHeatmapGraphMouseReleased(evt);
            }
        });

        panelHeatmapLeft.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        tableHeatmapLeft.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableHeatmapLeft.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableHeatmapLeftMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tableHeatmapLeft);

        buttonShowHeatmapLeft.setText("Show");
        buttonShowHeatmapLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowHeatmapLeftActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelHeatmapLeftLayout = new javax.swing.GroupLayout(panelHeatmapLeft);
        panelHeatmapLeft.setLayout(panelHeatmapLeftLayout);
        panelHeatmapLeftLayout.setHorizontalGroup(
            panelHeatmapLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHeatmapLeftLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(panelHeatmapLeftLayout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(buttonShowHeatmapLeft)
                .addContainerGap(82, Short.MAX_VALUE))
        );
        panelHeatmapLeftLayout.setVerticalGroup(
            panelHeatmapLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeatmapLeftLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonShowHeatmapLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout panelHeatmapGraphLayout = new javax.swing.GroupLayout(panelHeatmapGraph);
        panelHeatmapGraph.setLayout(panelHeatmapGraphLayout);
        panelHeatmapGraphLayout.setHorizontalGroup(
            panelHeatmapGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHeatmapGraphLayout.createSequentialGroup()
                .addComponent(panelHeatmapLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labelHeatmapGraph, javax.swing.GroupLayout.PREFERRED_SIZE, 547, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(72, Short.MAX_VALUE))
        );
        panelHeatmapGraphLayout.setVerticalGroup(
            panelHeatmapGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeatmapGraphLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelHeatmapGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelHeatmapLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelHeatmapGraph, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(61, Short.MAX_VALUE))
        );

        tabsDependences.addTab(" Heatmap graph", panelHeatmapGraph);

        javax.swing.GroupLayout panelHeatmapValuesLayout = new javax.swing.GroupLayout(panelHeatmapValues);
        panelHeatmapValues.setLayout(panelHeatmapValuesLayout);
        panelHeatmapValuesLayout.setHorizontalGroup(
            panelHeatmapValuesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 841, Short.MAX_VALUE)
        );
        panelHeatmapValuesLayout.setVerticalGroup(
            panelHeatmapValuesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 494, Short.MAX_VALUE)
        );

        tabsDependences.addTab("Heatmap values", panelHeatmapValues);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabsDependences)
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabsDependences)
        );

        TabPrincipal.addTab("Dependences", jPanel21);

        panelMultipleDatasetsLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Choose datasets"));

        jScrollPane2.setViewportView(listMultipleDatasetsLeft);

        buttonAddMultipleDatasets.setText("Add");
        buttonAddMultipleDatasets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddMultipleDatasetsActionPerformed(evt);
            }
        });

        buttonRemoveMultipleDatasets.setText("Remove");
        buttonRemoveMultipleDatasets.setPreferredSize(new java.awt.Dimension(80, 20));
        buttonRemoveMultipleDatasets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRemoveMultipleDatasetsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMultipleDatasetsLeftLayout = new javax.swing.GroupLayout(panelMultipleDatasetsLeft);
        panelMultipleDatasetsLeft.setLayout(panelMultipleDatasetsLeftLayout);
        panelMultipleDatasetsLeftLayout.setHorizontalGroup(
            panelMultipleDatasetsLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMultipleDatasetsLeftLayout.createSequentialGroup()
                .addGroup(panelMultipleDatasetsLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(panelMultipleDatasetsLeftLayout.createSequentialGroup()
                        .addComponent(buttonAddMultipleDatasets, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                        .addComponent(buttonRemoveMultipleDatasets, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelMultipleDatasetsLeftLayout.setVerticalGroup(
            panelMultipleDatasetsLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMultipleDatasetsLeftLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMultipleDatasetsLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonRemoveMultipleDatasets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonAddMultipleDatasets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(11, 11, 11))
        );

        javax.swing.GroupLayout panelMultipleDatasetsLayout = new javax.swing.GroupLayout(panelMultipleDatasets);
        panelMultipleDatasets.setLayout(panelMultipleDatasetsLayout);
        panelMultipleDatasetsLayout.setHorizontalGroup(
            panelMultipleDatasetsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMultipleDatasetsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelMultipleDatasetsLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(623, Short.MAX_VALUE))
        );
        panelMultipleDatasetsLayout.setVerticalGroup(
            panelMultipleDatasetsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMultipleDatasetsLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(panelMultipleDatasetsLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(159, Short.MAX_VALUE))
        );

        TabPrincipal.addTab("Multiple datasets", panelMultipleDatasets);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TabPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 881, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(TabPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //ELIMINAR DATASET SELECCIONADO
    private void buttonRemoveMultipleDatasetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRemoveMultipleDatasetsActionPerformed
        // TODO add your handling code here:
        int current = listMultipleDatasetsLeft.getSelectedIndex();
        list_dataset.remove(current);
        Dataset_names.remove(current);
        lista_son_meka.remove(current);
        /*
        System.out.println("");
        for(MultiLabelInstances actual : list_dataset)
        System.out.print(", "+actual.getDataSet().relationName());
        System.out.println("");
        */

        lista.remove(current);

    }//GEN-LAST:event_buttonRemoveMultipleDatasetsActionPerformed

    //AGREGAR LOS DATASETS EN LA PESTAÑA MULTIPLES DATASET
    private void buttonAddMultipleDatasetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddMultipleDatasetsActionPerformed
        // TODO add your handling code here:

        // ESCOGER EL DATASET
        JFileChooser jfile1 = new JFileChooser();
        FileNameExtensionFilter fname = new FileNameExtensionFilter(".arff", "arff");
        jfile1.setFileFilter(fname);

        int returnVal = jfile1.showOpenDialog(this);

        if (returnVal == JFileChooser.OPEN_DIALOG)
        {

            File f1 = jfile1.getSelectedFile();

            String dataset_name = f1.getName();
            dataset_name = dataset_name.substring(0,dataset_name.length()-5);

            String filename_database_arff = f1.getAbsolutePath();
            filename_database_xml = util.Get_xml_string(filename_database_arff);
            filename_database_xml = util.Get_file_name_xml(filename_database_xml);
            
            boolean es_meka=false;
            
           String filename_database_xml_path1=  filename_database_arff.substring(0,filename_database_arff.length()-5)+".xml";
            //-------------------------------------------------------------------------------------------------------
            FileReader fr;
            try 
            {
                fr = new FileReader(filename_database_arff);
                BufferedReader bf = new BufferedReader(fr);
                
                 String sCadena = bf.readLine();
                 int label_found=0;
                 String label_name;
                 String[] label_names_found;
                 
                 es_meka = util.Es_de_tipo_MEKA(sCadena);
                 lista_son_meka.add(es_meka);
                 
                 if(es_meka)
                 {
                         System.out.println("el dataset es de tipo MEKA");
                         es_de_tipo_meka = true;
                         
                         int label_count = util.Extract_labels_from_arff(sCadena);
                         label_names_found = new String[label_count];
                         
                         while(label_found < label_count)
                         {
                             sCadena = bf.readLine();
                             label_name = util.Extract_label_name_from_String(sCadena);
                             
                             if(label_name!= null)
                             {
                                 label_names_found[label_found]=label_name;
                                 label_found++;
                                 
                             }                             
                             
                         }
                         //util.Recorre_Arreglo(label_names_found);
                         
                         BufferedWriter bw_xml= new BufferedWriter(new FileWriter(filename_database_xml_path1));
                         PrintWriter wr_xml = new PrintWriter(bw_xml);
 
                         util.Write_into_xml_file(wr_xml, label_names_found); //crea el xml para el caso de MEKA
                         
                         bw_xml.close();
                         wr_xml.close();
 
                         filename_database_xml = util.Get_file_name_xml(filename_database_xml_path1); //carga el xml creado...
                         
                 }
                 
                 else 
                 {
                     System.out.println("el dataset NO es de tipo MEKA");
                     es_de_tipo_meka= false;
                 }
            }
            catch (FileNotFoundException ex) {
                Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //-------------------------------------------------------------------------------------------------------
            
            //-------------------------------------------------------------------------------------------------------
            
            
            

            try {

                MultiLabelInstances current = new MultiLabelInstances(filename_database_arff, filename_database_xml);

                if(util.Esta_dataset(list_dataset, current.getDataSet().relationName()))
                {
                    JOptionPane.showMessageDialog(null, "Dataset duplicated", "alert", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                list_dataset.add(current);
                Dataset_names.add(dataset_name);
                lista.addElement(dataset_name );
                
                

            }
            catch (InvalidDataFormatException ex) {
                Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_buttonAddMultipleDatasetsActionPerformed

    private void tabsDependencesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabsDependencesStateChanged
        
        //System.out.println(jTabbedPane4.getSelectedIndex());
        if(tabsDependences.getSelectedIndex() == 0) {labelChiThreshold.setVisible(true);}
        else {labelChiThreshold.setVisible(false);}
        
    }//GEN-LAST:event_tabsDependencesStateChanged

    private void tabsDependencesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabsDependencesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabsDependencesMouseClicked

    private void tableImbalanceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableImbalanceMouseClicked
        // evento cuando se da un click en el jtable de la pestaña class imbalance
        
        
        
        if(tabsImbalance.getSelectedIndex()==1) // si esta activado el panel de los labelcoumb
        {
            //System.out.println("Se ha tocado");
            int seleccionada = tableImbalance.getSelectedRow();

            if(labelsets_sorted == null) return;

            // String[] labels_info = util.Labelcommb_information(stat1, seleccionada);

            ArrayList<String> label_names= util.Get_labelnames_x_labelcombination(dataset, labelsets_sorted[seleccionada].get_name());

            String[] args = new String[2];

            args[0]= labelsets_sorted[seleccionada].get_name();
            args[1]= Integer.toString(labelsets_sorted[seleccionada].get_frequency());

            int posx = this.getBounds().x;
            int posy = this.getBounds().y;

            //System.out.println(posx+" "+posy);

            metric_output mo = new metric_output(dataset, posx, posy+50,args,label_names,label_x_frequency, es_de_tipo_meka);

            mo.setVisible(true);
        }
        
        else if(tabsImbalance.getSelectedIndex()==7) // si esta activado el panel de los BOX DIAGRAM
        {
            //BUTTON GROUP
            jRadioButton8.setSelected(true);
            
            int seleccionada = tableImbalance.getSelectedRow();
            
            String attr= tableImbalance.getValueAt(seleccionada, 0).toString();
           
            Instances instancias = dataset.getDataSet();
            
            Attribute attr_current = instancias.attribute(attr);
            
           double[] valores_attr= instancias.attributeToDoubleArray(attr_current.index());
                   
          
            HeapSort.sort(valores_attr);

            cp_box.getChart().setTitle(attr_current.name());//cambia el titulo del charpanel
            
            cp_box.getChart().getXYPlot().clearAnnotations();
            
            util.update_values_xydataset(cp_box, HeapSort.get_array_sorted());
            
         
        }
        
           else if(tabsImbalance.getSelectedIndex()==5) // si esta activado el panel del IR per Labelset
        {
             
               //System.out.println("Se ha tocado");
            int seleccionada = tableImbalance.getSelectedRow();

            if(labelsets_sorted_IR == null) return;

            // String[] labels_info = util.Labelcommb_information(stat1, seleccionada);

            ArrayList<String> label_names= util.Get_labelnames_x_labelcombination(dataset, labelsets_sorted_IR[seleccionada].get_name());

            String[] args = new String[2];

            args[0]= labelsets_sorted_IR[seleccionada].get_name();
            args[1]= Integer.toString(labelsets_sorted_IR[seleccionada].get_frequency());

            int posx = this.getBounds().x;
            int posy = this.getBounds().y;

            //System.out.println(posx+" "+posy);

            metric_output mo = new metric_output(dataset, posx, posy+50,args,label_names,label_x_frequency, es_de_tipo_meka);

            mo.setVisible(true);
        }
        
         else if(tabsImbalance.getSelectedIndex()==3) // si esta activado el panel del IR per Label intra class
        {
             
               //System.out.println("Se ha tocado");
            int seleccionada = tableImbalance.getSelectedRow();
            
            if(id_x_IR == null) return;
            
            int cant_labels =(int)tableImbalance.getValueAt(seleccionada, 1);
          //  System.out.println(" hay "+cant_labels +" etiquetas");
            
            double ir = Double.parseDouble(tableImbalance.getValueAt(seleccionada, 2).toString());
          //  System.out.println(" el ir es de "+ir +" ");
            
            ArrayList<String> label_names= util.Get_labelnames_x_IR_intra_class(ir,cant_labels,label_imbalanced);
            
            int posx = this.getBounds().x;
            int posy = this.getBounds().y;

            metric_output mo = new metric_output(dataset, posx, posy+50,label_names,label_x_frequency, es_de_tipo_meka);

            mo.setVisible(true);
            
    
        }
        
         else if(tabsImbalance.getSelectedIndex()==9) // si esta activado el panel del IR per Label inter class
        {
             
               //System.out.println("Se ha tocado");
            int seleccionada = tableImbalance.getSelectedRow();
            
            if(id_x_IR == null) return;
            
            int cant_labels =(int)tableImbalance.getValueAt(seleccionada, 1);
          //  System.out.println(" hay "+cant_labels +" etiquetas");
            
            double ir = Double.parseDouble(tableImbalance.getValueAt(seleccionada, 2).toString());
          //  System.out.println(" el ir es de "+ir +" ");
            
            ArrayList<String> label_names= util.Get_labelnames_x_IR_inter_class(ir,cant_labels,label_imbalanced);
            
            int posx = this.getBounds().x;
            int posy = this.getBounds().y;

            metric_output mo = new metric_output(dataset, posx, posy+50,label_names,label_x_frequency, es_de_tipo_meka);

            mo.setVisible(true);
            
    
        }
        

    }//GEN-LAST:event_tableImbalanceMouseClicked

    private void tabsImbalanceStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabsImbalanceStateChanged
        // jtabbpane de la pestaña class imbalance
        if(tm_BR1 !=null && tm_LP1!=null){
                        
            System.out.println(" id "+ tabsImbalance.getSelectedIndex());
            
            if(tabsImbalance.getSelectedIndex()==1)
            {
                tableImbalance.setModel(tm_LP1);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Frequency per labelset"));
                
                jPanel15.setVisible(false);
                
                labelIR1.setVisible(false);
                labelIR2.setVisible(false);
            }
            
            else if (tabsImbalance.getSelectedIndex()==5)
            {
                tableImbalance.setModel(tm_ir_per_labelset);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Labelset id per IR"));
                
                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_IR(1));
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();

                jPanel15.setVisible(false);
                
                labelIR1.setVisible(true);
                labelIR2.setVisible(true);
            }
            else if (tabsImbalance.getSelectedIndex()==0)
            {
                tableImbalance.setModel(tm_BR1);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Frequency per label"));
                
                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_default());
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();

                jPanel15.setVisible(false);
                
                labelIR1.setVisible(false);
                labelIR2.setVisible(false);

            }
             else if (tabsImbalance.getSelectedIndex()==6) // imbalance data metric
            {
                 tableImbalance.setModel(tm_IR);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Imabalance ratio per label"));
		
                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_IR(1,2));
                
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();
                //jRadioButton6.setVisible(true);
                //jRadioButton7.setVisible(true);
                jPanel15.setVisible(false);//es el panel del los botones del diagram box
                
                labelIR1.setVisible(true);
                labelIR2.setVisible(true);
            }
            else if (tabsImbalance.getSelectedIndex()==3)
            {
                tableImbalance.setModel(tm_ir_per_label_intra_class);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Number of labels intra class per IR"));
                
                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_IR(2));
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();

                jPanel15.setVisible(false);
                
                labelIR1.setVisible(true);
                labelIR2.setVisible(true);

            }
            else if (tabsImbalance.getSelectedIndex()==2)
            {
                tableImbalance.setModel(tm_labelxExamples);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Labels per example"));
                
                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_default());
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();

                jPanel15.setVisible(false);
                
                labelIR1.setVisible(false);
                labelIR2.setVisible(false);
                /*
                jTable4.setModel(tm_ir_per_label_inter_class);
                jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Number of labels inter class per IR"));
                
                jTable4.setDefaultRenderer(Object.class, new Mi_Render_IR(2));
                jPanel17.repaint();
                jPanel17.validate();

                jPanel15.setVisible(false);
                
                jLabel27.setVisible(true);
                jLabel28.setVisible(true);
                 */

            }
            
            else if (tabsImbalance.getSelectedIndex()==8)
            {
               tableImbalance.setModel(tm_ir_per_label_inter_class_only);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Label per IR values inter class"));
                
                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_IR(1));
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();

                jPanel15.setVisible(false);
                
                labelIR1.setVisible(true);
                labelIR2.setVisible(true);
            
                
            }
            
            else if (tabsImbalance.getSelectedIndex()==9)
            {
                tableImbalance.setModel(tm_ir_per_label_inter_class);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Number of labels inter class per IR"));
                
                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_IR(2));
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();
                
                jPanel15.setVisible(false);
                
                labelIR1.setVisible(true);
                labelIR2.setVisible(true);
                
                /*
                jTable4.setModel(tm_ir_per_label_intra_class_only);
                jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Label per IR values intra class"));
                
                jTable4.setDefaultRenderer(Object.class, new Mi_Render_IR(1));
                jPanel17.repaint();
                jPanel17.validate();

                jPanel15.setVisible(false);
                
                jLabel27.setVisible(true);
                jLabel28.setVisible(true);
                */
            }
            
            else if (tabsImbalance.getSelectedIndex()==4)
            {
               // jTable4.setModel(tm_ir_per_labelset);
               // jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Labelset id per IR"));
                
                tableImbalance.setModel(tm_ir_per_label_intra_class_only);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Label per IR values intra class"));
                
                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_IR(1));
                                
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();

                jPanel15.setVisible(false);
                
                labelIR1.setVisible(true);
                labelIR2.setVisible(true);

            }
            
            else if (tabsImbalance.getSelectedIndex()==7)
            {
                tableImbalance.setModel(tm_attr);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Numeric attributes"));
                
                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_default());
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();
                //jRadioButton6.setVisible(true);
                //jRadioButton7.setVisible(true);
                jPanel15.setVisible(true);
                
                labelIR1.setVisible(false);
                labelIR2.setVisible(false);

            }
            
            else
            {
                tableImbalance.setModel(tm_labelxExamples);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Labels per example"));
                
                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_default());
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();

                jPanel15.setVisible(false);
                
                labelIR1.setVisible(false);
                labelIR2.setVisible(false);

            }

            tableImbalance.repaint();
            tableImbalance.validate();
            //  System.out.println("SE HA PRESIONADO el "+jTabbedPane2.getSelectedIndex() );
        }

    }//GEN-LAST:event_tabsImbalanceStateChanged

//FILTRA LOS NUMEROS SOLAMENTE
    private void textRandomHoldoutKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textRandomHoldoutKeyTyped
        // TODO add your handling code here:

        char c = evt.getKeyChar();
        if(!Character.isDigit(c))
        {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_textRandomHoldoutKeyTyped

    private void textRandomHoldoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textRandomHoldoutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textRandomHoldoutActionPerformed

    //CUANDO ESTA ACTIVO "Stratification split" EN LA PESTAÑA TEST
    private void radioStratifiedHoldoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioStratifiedHoldoutActionPerformed
        // TODO add your handling code here:
        textStratifiedHoldout.setEnabled(true);
        textRandomHoldout.setEnabled(false);
        buttonChooseSuppliedTest.setEnabled(false);
        textRandomCV.setEnabled(false);
        textStratifiedCV.setEnabled(false);
        
        jButton6.setEnabled(false);
        button_show_traintest.setEnabled(false);
        button_save_train.setEnabled(false);
        
    }//GEN-LAST:event_radioStratifiedHoldoutActionPerformed

    //FILTRA LOS NUMEROS SOLAMENTE
    private void textStratifiedHoldoutKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textStratifiedHoldoutKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if(!Character.isDigit(c))
        {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_textStratifiedHoldoutKeyTyped

    private void textStratifiedHoldoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textStratifiedHoldoutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textStratifiedHoldoutActionPerformed

    // COMIENZA A CALCULAR LAS METRICAS SEGUN LA OPCION DEL train/TEST (BOTON "start")
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        list_dataset_train = new ArrayList();
        list_dataset_test = new ArrayList();
            
        Instances train=null,test=null;
        MultiLabelInstances train_ml=null,test_ml=null;
        
        
        if(dataset == null){JOptionPane.showMessageDialog(null, "You must load a dataset", "alert", JOptionPane.ERROR_MESSAGE); return;}

        if(radioSuppliedTest.isSelected()) // choose test
        {
            
           if(filename_database_arff_test == null){
                JOptionPane.showMessageDialog(null, "You must load a test Dataset", "AVISO", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try
            {
                dataset_train = dataset.clone();
                dataset_test = new MultiLabelInstances(filename_database_arff_test, filename_database_xml);

                //calculte metric selected
                choose_test=true;
                holdout_ramdon =false;
                holdout_stratified =false;
                cv_ramdon =false;
                cv_stratified =false;
                
                //button_calculateActionPerformed1(evt, jTable5, jTable6, jTable7); //OPTION CHOOSE TEST
            }

            catch (InvalidDataFormatException ex) {
                Logger.getLogger(CrossValidationExperiment.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.INFORMATION_MESSAGE);
                Logger.getLogger(CrossValidationExperiment.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        else if(radioStratifiedHoldout.isSelected())
        {
            // holdout WITH STRATIFICATION

            String split = textStratifiedHoldout.getText();
            int percent_split = Integer.parseInt(split);

            Stratification stratification;

            //small ratio of labelsets over examples
           
            if(radio<=0.1)    stratification =new LabelPowersetStratification(); //el calculo del radio esta en el load_dataset
            else   stratification =new IterativeStratification();

            // int numFolds = size / numTrain;

            MultiLabelInstances [] mldatasets= stratification.stratify(dataset.clone(), 100);

            Instances data_train=new Instances(mldatasets[0].getDataSet());
            Instances data_test=new Instances(mldatasets[mldatasets.length-1].getDataSet());

            for(int i=1;i<mldatasets.length-1;i++)
            {
                if(i<percent_split)data_train.addAll(mldatasets[i].getDataSet());
                else data_test.addAll(mldatasets[i].getDataSet());
            }
            try {

                dataset_train = new MultiLabelInstances(data_train, mldatasets[0].getLabelsMetaData());
                dataset_test = new MultiLabelInstances(data_test, mldatasets[0].getLabelsMetaData());

                choose_test=false;
                holdout_ramdon =false;
                holdout_stratified =true;
                cv_ramdon =false;
                cv_stratified =false;
              
                //calculte metric selected
               // button_calculateActionPerformed1(evt, jTable5, jTable6, jTable7);// HOLDOUT WITH STRATIFICATION

            }
            catch (InvalidDataFormatException ex) {
                Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        else if(radioRandomHoldout.isSelected())
        {
            
                    
            // random holdout
            String split = textRandomHoldout.getText();
            double percent_split = Double.parseDouble(split);

            //int num_instance = dataset.getNumInstances();

            try{

                Instances dataSet = dataset.getDataSet();

                RemovePercentage rmvp = new RemovePercentage();
                rmvp.setPercentage(percent_split);
                rmvp.setInputFormat(dataSet);
                Instances testDataSet = Filter.useFilter(dataSet, rmvp);
                

                rmvp = new RemovePercentage();
                rmvp.setInvertSelection(true);
                rmvp.setPercentage(percent_split);
                rmvp.setInputFormat(dataSet);
                Instances trainDataSet = Filter.useFilter(dataSet, rmvp);

                dataset_train = new MultiLabelInstances(trainDataSet, dataset.getLabelsMetaData());
                dataset_test = new MultiLabelInstances(testDataSet, dataset.getLabelsMetaData());

                choose_test=false;
                holdout_ramdon =true;
                holdout_stratified =false;
                cv_ramdon =false;
                cv_stratified =false;
                //calculte metric selected
              
                //button_calculateActionPerformed1(evt, jTable5, jTable6, jTable7);// HOLDOUT percentage

            }

            catch (InvalidDataFormatException ex) {
                Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        // RAMDON CROSS VALIDATION
          else if(radioRandomCV.isSelected())
        {
            
           
            String split = textRandomCV.getText();
            
            if(split.equals(""))
            {
                JOptionPane.showMessageDialog(null, "You must put a fold number", "alert", JOptionPane.ERROR_MESSAGE);
                return;

            }
            
            int folds = Integer.parseInt(split);
            

            
            //int num_instance = dataset.getNumInstances();

            try{
                 MultiLabelInstances temp = dataset.clone();
                 Instances dataset_temp = temp.getDataSet();
                
                 int seed = (int)(Math.random()*100)+100;
                                                
                 Random rand = new Random(seed);
                                                   
                 dataset_temp.randomize(rand);
                   
                         
                 // aplica cross validation
                  for (int n = 0; n < folds; n++) 
                  {
                        train = dataset_temp.trainCV(folds, n,rand);
                        test = dataset_temp.testCV(folds, n);
                        
                        train_ml = new MultiLabelInstances(train, dataset.getLabelsMetaData());
                        test_ml = new MultiLabelInstances(test, dataset.getLabelsMetaData());
                        
                        list_dataset_train.add(train_ml);
                        list_dataset_test.add(test_ml);
                  }
                
                                    
                //System.out.println("numero generado "+seed);
                
                choose_test=false;
                holdout_ramdon =false;
                holdout_stratified =false;
                cv_ramdon =true;
                cv_stratified =false;
               
                //calculte metric selected
               // button_calculateActionPerformed1(evt, jTable5, jTable6, jTable7,list_dataset_train,list_dataset_test );// RAMDON CROSS VALIDATION

            }

             catch (Exception ex) {
                Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
          // CV STRATIFIED
          else if(radioStratifiedCV.isSelected())
          {
              
            String split = textStratifiedCV.getText();
            
            if(split.equals(""))
            {
                JOptionPane.showMessageDialog(null, "You must put a fold number", "alert", JOptionPane.ERROR_MESSAGE);
                return;

            }
            
            int folds = Integer.parseInt(split);
            
              try
              {
                  Stratification stratification;                             
                  
                  if(radio<=0.1)    stratification =new LabelPowersetStratification();
                  else   stratification =new IterativeStratification();
                  
                  MultiLabelInstances [] mldatasets= stratification.stratify(dataset.clone(), folds);
                  
                  MultiLabelInstances[] current;
                  //train = new Instances(
                  for (int n = 0; n < folds; n++) 
                  {
                      current = util.Get_dataset_train_test(n, mldatasets);
                                                                  
                        list_dataset_train.add(current[0]);
                        list_dataset_test.add(current[1]);
                  }
                   
                  
                choose_test=false;
                holdout_ramdon =false;
                holdout_stratified =false;
                cv_ramdon =false;
                cv_stratified =true;
               // return;
                 //calculte metric selected
                //button_calculateActionPerformed1(evt, jTable5, jTable6, jTable7,list_dataset_train,list_dataset_test );  // CV STRATIFIED
                  
              }  
              catch (Exception ex) {
                Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
            }
              
          }

           jButton6.setEnabled(true);
           button_show_traintest.setEnabled(true);
           button_save_train.setEnabled(false);
           
           JOptionPane.showMessageDialog(null, "Dataset is generated", "Successful", JOptionPane.INFORMATION_MESSAGE);
           Toolkit.getDefaultToolkit().beep();
    }//GEN-LAST:event_jButton3ActionPerformed

    // "choose" ESCOGE EL TEST SET 
    private void buttonChooseSuppliedTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonChooseSuppliedTestActionPerformed
        // TODO add your handling code here:

        // ESCOGER EL DATASET
        JFileChooser jfile1 = new JFileChooser();
        FileNameExtensionFilter fname = new FileNameExtensionFilter(".arff", "arff");
        jfile1.setFileFilter(fname);
        //int returnVal =
        int result= jfile1.showOpenDialog(this);

        if(result == JFileChooser.OPEN_DIALOG)
        {
            File f1 = jfile1.getSelectedFile();
            filename_database_arff_test = f1.getAbsolutePath();
        }
    }//GEN-LAST:event_buttonChooseSuppliedTestActionPerformed

    //OPTION "Supplied test set"
    private void radioSuppliedTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioSuppliedTestActionPerformed
        // TODO add your handling code here:
        textRandomHoldout.setEnabled(false);
        textStratifiedHoldout.setEnabled(false);
        buttonChooseSuppliedTest.setEnabled(true);
        textRandomCV.setEnabled(false);
        textStratifiedCV.setEnabled(false);
        
        jButton6.setEnabled(false);
        button_show_traintest.setEnabled(false);
        button_save_train.setEnabled(false);
    }//GEN-LAST:event_radioSuppliedTestActionPerformed

  //OPTION "Ramdon  split"  
    private void radioRandomHoldoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioRandomHoldoutActionPerformed
        // TODO add your handling code here:
        textRandomHoldout.setEnabled(true);
        textStratifiedHoldout.setEnabled(false);
        buttonChooseSuppliedTest.setEnabled(false);
        textRandomCV.setEnabled(false);
        textStratifiedCV.setEnabled(false);

        jButton6.setEnabled(false);
        button_show_traintest.setEnabled(false);
        button_save_train.setEnabled(false);
    }//GEN-LAST:event_radioRandomHoldoutActionPerformed

    // CAPTURA LA DIRECCION DEL XML EN EL JTEXTFIELD DE LA 1ra pestaña para cargar el dataset.
    private void textChooseFileKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textChooseFileKeyPressed

        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
             //--------------------------------------------------------------
            //INVOCAR EL PROGRESS BAR     
            /*
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
            progress_bar new_progress = new progress_bar();
            task = new Task(new_progress);
            new_progress.setVisible(true);
        
            task.addPropertyChangeListener(new_progress);
            task.execute();
            //--------------------------------------------------------------
            */
            
            
            String filename_database_arff = textChooseFile.getText();

            filename_database_xml = util.Get_xml_string(filename_database_arff);

            filename_database_xml = util.Get_file_name_xml(filename_database_xml);

            Load_dataset(filename_database_arff, filename_database_xml);
        }
    }//GEN-LAST:event_textChooseFileKeyPressed

    private void textChooseFileFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textChooseFileFocusLost

        // CUANDO EL TEXTFIELD PIERDE EL FOCUS
        /*
        String filename_database_arff = jTextField2.getText();

        filename_database_xml = filename_database_arff.substring(0,filename_database_arff.length()-5)+".xml";

        filename_database_xml = util.Get_file_name_xml(filename_database_xml);

        Load_dataset(filename_database_arff, filename_database_xml);

        */
    }//GEN-LAST:event_textChooseFileFocusLost

    private void textChooseFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textChooseFileActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textChooseFileActionPerformed

    // carga el dataset buscandolo por el filechooser
    private void buttonChooseFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonChooseFileActionPerformed
        // TODO add your handling code here:

        // ESCOGER EL DATASET
        JFileChooser jfile1 = new JFileChooser();
        FileNameExtensionFilter fname = new FileNameExtensionFilter(".arff", "arff");
        jfile1.setFileFilter(fname);
        //int returnVal =

        int returnVal = jfile1.showOpenDialog(this);

        if (returnVal == JFileChooser.OPEN_DIALOG)
        {

            
            
            File f1 = jfile1.getSelectedFile();
            dataset_name1 = f1.getName();
            dataset_current_name = dataset_name1.substring(0,dataset_name1.length()-5);

            String filename_database_arff = f1.getAbsolutePath();
            
            
            filename_database_xml_path=  filename_database_arff.substring(0,filename_database_arff.length()-5)+".xml";
            filename_database_xml = util.Get_file_name_xml1(filename_database_xml_path);
            
            File  file_temp = new File(filename_database_xml_path);
            
            int velocidad=5;
            
            //-------------------------------------------------------------------------------------------------------
            FileReader fr;
            try 
            {
                fr = new FileReader(filename_database_arff);
                BufferedReader bf = new BufferedReader(fr);
                
                 String sCadena = bf.readLine();
                 int label_found=0;
                 String label_name;
                 String[] label_names_found;
                 
                 if(util.Es_de_tipo_MEKA(sCadena))
                 {
                         System.out.println("el dataset es de tipo MEKA");
                         es_de_tipo_meka = true;
                         
                         int label_count = util.Extract_labels_from_arff(sCadena);
                         label_names_found = new String[label_count];
                         
                         while(label_found < label_count)
                         {
                             sCadena = bf.readLine();
                             label_name = util.Extract_label_name_from_String(sCadena);
                             
                             if(label_name!= null)
                             {
                                 label_names_found[label_found]=label_name;
                                 label_found++;
                                 
                             }                             
                             
                         }
                         //util.Recorre_Arreglo(label_names_found);
                         
                         BufferedWriter bw_xml= new BufferedWriter(new FileWriter(filename_database_xml_path));
                         PrintWriter wr_xml = new PrintWriter(bw_xml);
 
                         util.Write_into_xml_file(wr_xml, label_names_found); //crea el xml para el caso de MEKA
                         
                         bw_xml.close();
                         wr_xml.close();
 
                         filename_database_xml = util.Get_file_name_xml(filename_database_xml_path); //carga el xml creado...
                         file_temp = new File(filename_database_xml_path);
                 }
                 
                 else 
                 {
                     System.out.println("el dataset NO es de tipo MEKA");
                     es_de_tipo_meka= false;
                     
                 }
            }
            catch (FileNotFoundException ex) {
                Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //-------------------------------------------------------------------------------------------------------
            
            //-------------------------------------------------------------------------------------------------------
            
            
            if(!file_temp.exists()) //SI NO EXISTE EL XML
            {
                 filename_database_xml_path = util.Get_xml_string(filename_database_arff);
                 filename_database_xml = util.Get_file_name_xml(filename_database_xml_path);
            }
            try {
                MultiLabelInstances dataset_temp = new MultiLabelInstances(filename_database_arff, filename_database_xml);
               //System.out.println("prueba xml "+filename_database_xml);
                velocidad =util.get_velocidad(dataset_temp);
            } catch (InvalidDataFormatException ex) {
                Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            
            //--------------------------------------------------------------
            //INVOCAR EL PROGRESS BAR
             
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            progress_bar new_progress = new progress_bar();
            task = new Task(new_progress,velocidad);
            new_progress.setVisible(true);
        
            task.addPropertyChangeListener(new_progress);
            task.execute();
           
            //--------------------------------------------------------------
            
            
            

            Load_dataset(filename_database_arff, filename_database_xml);

            textChooseFile.setText(filename_database_arff);
        }
    }//GEN-LAST:event_buttonChooseFileActionPerformed

    private void textRandomCVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textRandomCVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textRandomCVActionPerformed

    private void textRandomCVKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textRandomCVKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_textRandomCVKeyTyped

    private void textStratifiedCVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textStratifiedCVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textStratifiedCVActionPerformed

    private void textStratifiedCVKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textStratifiedCVKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_textStratifiedCVKeyTyped

    private void radioRandomCVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioRandomCVActionPerformed
        // TODO add your handling code here:
        textRandomHoldout.setEnabled(false);
        textStratifiedHoldout.setEnabled(false);
        buttonChooseSuppliedTest.setEnabled(false);
        textRandomCV.setEnabled(true);
        textStratifiedCV.setEnabled(false);
        
        jButton6.setEnabled(false);
        button_show_traintest.setEnabled(false);
        button_save_train.setEnabled(false);
        
    }//GEN-LAST:event_radioRandomCVActionPerformed

    private void radioStratifiedCVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioStratifiedCVActionPerformed
        // TODO add your handling code here:
        
        textRandomHoldout.setEnabled(false);
        textStratifiedHoldout.setEnabled(false);
        buttonChooseSuppliedTest.setEnabled(false);
        textRandomCV.setEnabled(false);
        textStratifiedCV.setEnabled(true);
        
        jButton6.setEnabled(false);
        button_show_traintest.setEnabled(false);
        button_save_train.setEnabled(false);
    }//GEN-LAST:event_radioStratifiedCVActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        
        if(dataset == null){JOptionPane.showMessageDialog(null, "You must load a dataset", "alert", JOptionPane.ERROR_MESSAGE); return;}
        if((list_dataset_train.isEmpty() && list_dataset_test.isEmpty()) && (radioRandomCV.isSelected()|| radioStratifiedCV.isSelected() )){JOptionPane.showMessageDialog(null, "You must click Start before", "alert", JOptionPane.ERROR_MESSAGE); return;}//cross validation
        if((dataset_train ==null && dataset_test==null) && (radioStratifiedHoldout.isSelected()|| radioRandomHoldout.isSelected() )){JOptionPane.showMessageDialog(null, "You must click Start before", "alert", JOptionPane.ERROR_MESSAGE); return;}// holdout

        
         // JFILECHOOSER SAVE
         JFileChooser fc= new JFileChooser();
        
         fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        String path_train, path_test,path_xml;
        
        int returnVal = fc.showSaveDialog(this);
         
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
               File file = fc.getSelectedFile();
                FileFilter f1 = fc.getFileFilter();
                
                
              if(fc.isDirectorySelectionEnabled())
              {
                  if(radioStratifiedHoldout.isSelected()|| radioRandomHoldout.isSelected()) //holdout
                    {
                      BufferedWriter bw_train = null;
                      try {
                          
                          String name_dataset= dataset_name1.substring(0,dataset_name1.length()-5);
                          
                          path_train = file.getAbsolutePath()+"\\"+name_dataset+"-train.arff";
                          path_test = file.getAbsolutePath()+"\\"+name_dataset+"-test.arff";
                          path_xml = file.getAbsolutePath()+"\\"+name_dataset+".xml";

                          bw_train = new BufferedWriter(new FileWriter(path_train));
                          PrintWriter wr_train = new PrintWriter(bw_train);
                          
                          //System.out.println("longitud del train es "+dataset_train.getNumInstances());
                          util.Save_dataset_in_the_file(wr_train, dataset_train);
                          
                          wr_train.close();
                          bw_train.close();
                          
                          BufferedWriter bw_test = new BufferedWriter(new FileWriter(path_test));
                          PrintWriter wr_test = new PrintWriter(bw_test);

                         // System.out.println("longitud del test es "+dataset_test.getNumInstances());
                          util.Save_dataset_in_the_file(wr_test, dataset_test);
                          
                          wr_test.close();
                          bw_test.close();
                          
                          BufferedWriter bw_xml = new BufferedWriter(new FileWriter(path_xml));
                          PrintWriter wr_xml = new PrintWriter(bw_xml);
                            
                          util.Save_xml_in_the_file(wr_xml,filename_database_xml_path);
                          
                          wr_xml.close();
                          bw_xml.close();
                          
                         JOptionPane.showMessageDialog(null, "All Files are saved", "Successful", JOptionPane.INFORMATION_MESSAGE);
                          
                       } catch (IOException ex) {
                          Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                      } 
                                         
                    }
                  
                  else //CROSS VALIDATION 
                  {
                      try{
                          
                        util.Save_dataset_in_the_file(list_dataset_train,file.getAbsolutePath(), dataset_name1.substring(0,dataset_name1.length()-5), "train");
                        util.Save_dataset_in_the_file(list_dataset_test,file.getAbsolutePath(), dataset_name1.substring(0,dataset_name1.length()-5), "test");
                        
                          path_xml = file.getAbsolutePath()+"\\"+dataset_name1.substring(0,dataset_name1.length()-5)+".xml";
                          
                          BufferedWriter bw_xml = new BufferedWriter(new FileWriter(path_xml));
                          PrintWriter wr_xml = new PrintWriter(bw_xml);
                            
                          util.Save_xml_in_the_file(wr_xml,filename_database_xml_path);
                          
                          wr_xml.close();
                          bw_xml.close();
                          
                          JOptionPane.showMessageDialog(null, "All Files are saved", "Successful", JOptionPane.INFORMATION_MESSAGE);
                          
                        }
                      catch(Exception e1){}
                  
                  }
                  Toolkit.getDefaultToolkit().beep();
              }
              
                
        }
        
        
        
        
        
        
        
        
    }//GEN-LAST:event_jButton6ActionPerformed

    private void panelCoOcurrenceMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelCoOcurrenceMouseReleased
        // TODO add your handling code here:
        
        if(evt.getButton() == MouseEvent.BUTTON3 )
        {
          //  System.out.println("se dio el clic derecho");
            
            jPopupMenu1.removeAll();
               
           JMenuItem salvar = new JMenuItem("Save as...");
          
           salvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    save_as_ActionPerformed(evt);
                } catch (AWTException ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
          
           jPopupMenu1.add(salvar);
            jPopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_panelCoOcurrenceMouseReleased

    private void labelHeatmapGraphMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelHeatmapGraphMouseReleased
        // TODO add your handling code here:
        
       if(evt.getButton() == MouseEvent.BUTTON1)  
       {
            int pos_inicial_x = (labelHeatmapGraph.getBounds().width/2) - jheat_chart.getChartSize().width/2 ;
            int pos_incial_y = (labelHeatmapGraph.getBounds().height/2- jheat_chart.getChartSize().height/2);
           
            int pos_actual_x=evt.getPoint().x;
            int pos_actual_y=evt.getPoint().y;
                        
            int cant_celdas_x = jheat_chart.getChartSize().width/jheat_chart.getCellSize().width;
            int cant_celdas_y = jheat_chart.getChartSize().height/jheat_chart.getCellSize().height;
            
            int ancho_celdas_x = jheat_chart.getCellSize().width;
            int ancho_celdas_y = jheat_chart.getCellSize().height;
            
            int celda_actual_x = util.Devuelve_num_celda(pos_inicial_x, pos_actual_x, ancho_celdas_x, cant_celdas_x);
            int celda_actual_y = util.Devuelve_num_celda(pos_incial_y, pos_actual_y, ancho_celdas_y, cant_celdas_y);
            
            if(celda_actual_x ==-1 || celda_actual_y ==-1)
            {
               // System.out.println("fuera del borde");
                return;
            }
            
            int invierte_eje_y = cant_celdas_y-celda_actual_y+1;
            
            String[] seleccionados =util.devuelve_etiquetas_seleccionadas(dataset, label_indices_seleccionados);
            
                        
           int posx = this.getBounds().x;
           int posy = this.getBounds().y;
           
           metric_output mo = new metric_output(dataset,seleccionados[celda_actual_x-1],seleccionados[invierte_eje_y-1],posx,posy,es_de_tipo_meka);
           mo.setVisible(true);
           
       }
        
        
        
       if(evt.getButton() == MouseEvent.BUTTON3 )
        {
            //System.out.println("se dio el clic derecho");
            
            jPopupMenu1.removeAll();
               
           JMenuItem salvar = new JMenuItem("Save as...");
          
           salvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    save_as_ActionPerformed1(evt);
                } catch (AWTException ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
          
           jPopupMenu1.add(salvar);
            jPopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
        }
        
    }//GEN-LAST:event_labelHeatmapGraphMouseReleased

    private void tableCoOcurrenceLeftMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCoOcurrenceLeftMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tableCoOcurrenceLeftMouseClicked

    private void buttonShowCoOcurrenceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowCoOcurrenceActionPerformed
        // TODO add your handling code here:
        
        if(lista_pares== null) {JOptionPane.showMessageDialog(null, "You must load a dataset", "alert", JOptionPane.ERROR_MESSAGE); return;}
         
        
        ArrayList<String> seleccionados= new  ArrayList();
        int[] selecteds=tableCoOcurrenceLeft.getSelectedRows();
        
        if(selecteds.length<= 1) {JOptionPane.showMessageDialog(null, "You must choose two or more labels", "alert", JOptionPane.ERROR_MESSAGE); return;}
        
        for(int i=0;i<selecteds.length; i++)
        {
            seleccionados.add((tableCoOcurrenceLeft.getValueAt(selecteds[i], 0).toString()));
        }

       ArrayList<pares_atributos> pares_seleccionados=  util.Encuentra_pares_attr_seleccionados(lista_pares, seleccionados);
        
       String[] labelname=util.pasa_valores_al_arreglo(seleccionados);//solo cambia el tipo de estructura de datos.
       
       graphComponent  =  Create_jgraphx(panelCoOcurrenceRight,pares_seleccionados,labelname,graphComponent);
        
       
       //LANZAR LA VENTANA EMERGENTE CON LOS PARE SELECCIONADOS
       
       double[][] pares_freq= util.Get_pares_seleccionados(labelname, pares_seleccionados, num_instancias);
       
       int posx = this.getBounds().x;
       int posy = this.getBounds().y;
       
       jframe_temp mo = new jframe_temp(pares_freq, labelname,posx,posy);
       mo.setVisible(true);
       //util.Recorre_Arreglo(labelname);
      // util.Recorre_Arreglo_2_dimensiones(pares_freq);
       
       
    }//GEN-LAST:event_buttonShowCoOcurrenceActionPerformed

    private void tableHeatmapLeftMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableHeatmapLeftMouseClicked
        // TODO add your handling code here:
        
          
      
        
        
    }//GEN-LAST:event_tableHeatmapLeftMouseClicked

    private void buttonShowHeatmapLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowHeatmapLeftActionPerformed
        // TODO add your handling code here:
        
          if(dataset== null) {JOptionPane.showMessageDialog(null, "You must load a dataset", "alert", JOptionPane.ERROR_MESSAGE); return;}
         
        
        ArrayList<String> seleccionados= new  ArrayList();
        int[] selecteds=tableHeatmapLeft.getSelectedRows();
        
        if(selecteds.length< 1) {JOptionPane.showMessageDialog(null, "You must choose one or more labels", "alert", JOptionPane.ERROR_MESSAGE); return;}
        
        for(int i=0;i<selecteds.length; i++)
        {
            seleccionados.add((tableHeatmapLeft.getValueAt(selecteds[i], 0).toString()));
        }

       //ArrayList<pares_atributos> pares_seleccionados=  util.Encuentra_pares_attr_seleccionados(lista_pares, seleccionados);
        
       String[] labelname=util.pasa_valores_al_arreglo(seleccionados);
       
       int[] label_indices =util.get_label_indices(labelname, dataset);
       
       //graphComponent  =  Create_jgraphx(jPanel10,pares_seleccionados,labelname,graphComponent);
       
       HeapSort1.sort(label_indices);
       //util.Recorre_Arreglo(HeapSort1.get_array_sorted());
       label_indices_seleccionados = label_indices;
       
       jheat_chart= create_heapmap(labelHeatmapGraph,dataset,label_indices);
       
       ArrayList<pares_atributos> pares_seleccionados=  util.Encuentra_pares_attr_seleccionados(lista_pares, seleccionados);
       
       double[][] pares_freq= util.get_heatmap_values(pares_seleccionados, num_instancias,labelname);
       
       int posx = this.getBounds().x;
       int posy = this.getBounds().y;
        
       jframe_temp mo = new jframe_temp(pares_freq, labelname,posx,posy);
       mo.setVisible(true);      
       
       
    }//GEN-LAST:event_buttonShowHeatmapLeftActionPerformed

    private void radioExamplesPerLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_radioExamplesPerLabelMouseClicked
        // TODO add your handling code here:
        tableImbalance.clearSelection();
        if(label_frenquency == null) return;
        
        double [] label_frenquency_values = util.get_label_frequency(label_frenquency);
        
        util.Recorre_Arreglo(label_frenquency_values);
        
        HeapSort.sort(label_frenquency_values);
        
        cp_box.getChart().setTitle("# Examples per Label");//cambia el titulo del charpanel
        cp_box.getChart().getXYPlot().clearAnnotations();
            
        util.update_values_xydataset(cp_box, HeapSort.get_array_sorted());
        
        
    }//GEN-LAST:event_radioExamplesPerLabelMouseClicked

    private void radioExamplesPerLabelsetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_radioExamplesPerLabelsetMouseClicked
        // TODO add your handling code here:
        tableImbalance.clearSelection();
        
        if(labelset_frequency == null) return;
        
        util.Recorre_Arreglo(labelset_frequency);
        
        HeapSort.sort(labelset_frequency);
        
        cp_box.getChart().setTitle("# Examples per Labelset");//cambia el titulo del charpanel
        cp_box.getChart().getXYPlot().clearAnnotations();
            
        util.update_values_xydataset(cp_box, HeapSort.get_array_sorted());
    }//GEN-LAST:event_radioExamplesPerLabelsetMouseClicked

    private void radioExamplesPerLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioExamplesPerLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioExamplesPerLabelActionPerformed

    private void radioExamplesPerLabelsetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioExamplesPerLabelsetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioExamplesPerLabelsetActionPerformed

       private void save_as_ActionPerformed1(java.awt.event.ActionEvent evt) throws AWTException, IOException
    {
        BufferedImage image = new Robot().createScreenCapture(new Rectangle(labelHeatmapGraph.getLocationOnScreen().x, labelHeatmapGraph.getLocationOnScreen().y, labelHeatmapGraph.getWidth(), labelHeatmapGraph.getHeight()));
        
       // JFILECHOOSER SAVE
         JFileChooser fc= new JFileChooser();
        
         FileNameExtensionFilter fname1 = new FileNameExtensionFilter(".png", "png");
        
        //eliminar el que tiene por defecto
        fc.removeChoosableFileFilter(fc.getChoosableFileFilters()[0]);

        fc.setFileFilter(fname1);
        fc.addChoosableFileFilter(fname1);
        
        int returnVal = fc.showSaveDialog(this);
         
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {                               
              
                  File file =new File(fc.getSelectedFile().getAbsolutePath()+".png");
                  
              // TODO add your handling code here:
              //jpanel25
                
                ImageIO.write(image, "png", file);
                
                JOptionPane.showMessageDialog(null, "File Saved", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                Toolkit.getDefaultToolkit().beep();
     
          } 
       
        
        
    }
    
    private void save_as_ActionPerformed(java.awt.event.ActionEvent evt) throws AWTException, IOException
    {
        
        BufferedImage image = new Robot().createScreenCapture(new Rectangle(panelCoOcurrenceRight.getLocationOnScreen().x, panelCoOcurrenceRight.getLocationOnScreen().y, panelCoOcurrenceRight.getWidth(), panelCoOcurrenceRight.getHeight()));
         
       // JFILECHOOSER SAVE
         JFileChooser fc= new JFileChooser();
        
         FileNameExtensionFilter fname1 = new FileNameExtensionFilter(".png", "png");
        
        //eliminar el que tiene por defecto
        fc.removeChoosableFileFilter(fc.getChoosableFileFilters()[0]);

        fc.setFileFilter(fname1);
        fc.addChoosableFileFilter(fname1);
        
        int returnVal = fc.showSaveDialog(this);
         
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {                               
              
                  File file =new File(fc.getSelectedFile().getAbsolutePath()+".png");
                  
              // TODO add your handling code here:
              //jpanel25
               
                ImageIO.write(image, "png", file);
                
                JOptionPane.showMessageDialog(null, "File Saved", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                Toolkit.getDefaultToolkit().beep();
            
              
        }
        
        
    }
    
    private void start_config_multiples_datasets()
    {
      lista_son_meka = new ArrayList();
      list_dataset = new ArrayList();
      Dataset_names = new ArrayList();
      listMultipleDatasetsLeft.setModel(lista);
    }
    
    
    //CREA EL GRAFO EN LA PESTAÑA "vISUALIZE"/"GRAPH CO-OCURRENCE"
    private mxGraphComponent Create_jgraphx(JPanel jpanel , ArrayList<pares_atributos> mi_lista, String[] Label_name,mxGraphComponent graphComponent_viejo )
    {
        
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

         
	graph.getModel().beginUpdate();
        
        graph.setLabelsClipped(true);
        
        Random aleatorio=new Random();
	
        Object[] lista_vertices = new Object[Label_name.length];
        
        atributo current;
        double freq;
        
        int min = 0; //util.get_valor_min_entre_pares_atrr(mi_lista);
        int max = 1; //util.get_valor_max_entre_pares_atrr(mi_lista);
        int cant_intervalos = 10;           
        int fortaleza;
  
        try
        {
            //create vertexs
            for(int i=0;i<Label_name.length;i++)
            {
                current = util.Get_label_x_labelname(Label_name[i],label_x_frequency);
                freq = current.get_frequency()/(dataset.getNumInstances()*1.0);//# de ocurrencia 
                
                //System.out.println("cantidad de veces que se repite "+Label_name[i]+" es "+freq );
                
                fortaleza =  util.get_valor_fortaleza(min, max, cant_intervalos, freq);
                
                if(fortaleza==1)lista_vertices[i]= graph.insertVertex(parent, null,Label_name[i], aleatorio.nextInt(430), aleatorio.nextInt(280), Label_name[i].length()*6,20);      
                else if (fortaleza==2) lista_vertices[i]= graph.insertVertex(parent, null,Label_name[i], aleatorio.nextInt(430), aleatorio.nextInt(280), Label_name[i].length()*5,20,"ROUNDED;strokeWidth=2");      
                else if (fortaleza==3) lista_vertices[i]= graph.insertVertex(parent, null,Label_name[i], aleatorio.nextInt(430), aleatorio.nextInt(280), Label_name[i].length()*5,20,"ROUNDED;strokeWidth=3");      
                else if (fortaleza==4) lista_vertices[i]= graph.insertVertex(parent, null,Label_name[i], aleatorio.nextInt(430), aleatorio.nextInt(280), Label_name[i].length()*5,20,"ROUNDED;strokeWidth=4");      
                else if (fortaleza==5) lista_vertices[i]= graph.insertVertex(parent, null,Label_name[i], aleatorio.nextInt(430), aleatorio.nextInt(280), Label_name[i].length()*5,20,"strokeWidth=5");      
                else if (fortaleza==6) lista_vertices[i]= graph.insertVertex(parent, null,Label_name[i], aleatorio.nextInt(430), aleatorio.nextInt(280), Label_name[i].length()*5,20,"strokeWidth=6");      
                else if (fortaleza==7) lista_vertices[i]= graph.insertVertex(parent, null,Label_name[i], aleatorio.nextInt(430), aleatorio.nextInt(280), Label_name[i].length()*5,20,"strokeWidth=7");      
                else if (fortaleza==8) lista_vertices[i]= graph.insertVertex(parent, null,Label_name[i], aleatorio.nextInt(430), aleatorio.nextInt(280), Label_name[i].length()*5,20,"strokeWidth=8");      
                else if (fortaleza==9) lista_vertices[i]= graph.insertVertex(parent, null,Label_name[i], aleatorio.nextInt(430), aleatorio.nextInt(280), Label_name[i].length()*5,20,"strokeWidth=9");      
                else lista_vertices[i]= graph.insertVertex(parent, null,Label_name[i], aleatorio.nextInt(430), aleatorio.nextInt(280), Label_name[i].length()*5,20,"strokeWidth=10");      
                    
                    
            }
            
            ArrayList<String> lista_del_otro_par;
            //create edges 
            
            if(!mi_lista.isEmpty()){

          
            
            pares_atributos temp;
            
            for(int i=0;i<Label_name.length;i++)
            {
                lista_del_otro_par=util.Get_lista_vertices_del_par(Label_name[i], mi_lista);//devuelve el extremo que forma el par de etiquetas
                
                 
                for(String actual : lista_del_otro_par)
                {
                    int index = util.devuelve_indice(Label_name, actual);
                    
                    temp =util.Search_and_get(Label_name[i], actual, mi_lista);
                    freq = temp.get_cant_veces()/(dataset.getNumInstances()*1.0);
                    
                    fortaleza =  util.get_valor_fortaleza(min, max, cant_intervalos,freq );
                    
                    //System.out.println("pares a buscar "+ Label_name[i]+" "+ actual);
                    //System.out.println("fortaleza ="+fortaleza);
                    
                    if(fortaleza==1) graph.insertEdge(parent, null,"",lista_vertices[i], lista_vertices[index], "startArrow=none;endArrow=none;strokeWidth=1");
                    else if (fortaleza==2) graph.insertEdge(parent, null,"",lista_vertices[i], lista_vertices[index], "startArrow=none;endArrow=none;strokeWidth=2");
                    else if (fortaleza==3) graph.insertEdge(parent, null,"",lista_vertices[i], lista_vertices[index], "startArrow=none;endArrow=none;strokeWidth=3");
                    else if (fortaleza==4) graph.insertEdge(parent, null,"",lista_vertices[i], lista_vertices[index], "startArrow=none;endArrow=none;strokeWidth=4");
                    else if (fortaleza==5) graph.insertEdge(parent, null,"",lista_vertices[i], lista_vertices[index], "startArrow=none;endArrow=none;strokeWidth=5");
                    else if (fortaleza==6) graph.insertEdge(parent, null,"",lista_vertices[i], lista_vertices[index], "startArrow=none;endArrow=none;strokeWidth=6");
                    else if (fortaleza==7) graph.insertEdge(parent, null,"",lista_vertices[i], lista_vertices[index], "startArrow=none;endArrow=none;strokeWidth=7");
                    else if (fortaleza==8) graph.insertEdge(parent, null,"",lista_vertices[i], lista_vertices[index], "startArrow=none;endArrow=none;strokeWidth=8");
                    else if (fortaleza==9) graph.insertEdge(parent, null,"",lista_vertices[i], lista_vertices[index], "startArrow=none;endArrow=none;strokeWidth=9");
                    else graph.insertEdge(parent, null,"",lista_vertices[i], lista_vertices[index], "startArrow=none;endArrow=none;strokeWidth=3");
                }
                
            }           
              }
        }
        finally
        {
            graph.getModel().endUpdate();
        }
        
        if(graphComponent_viejo !=null) jpanel.remove(graphComponent_viejo); // esto es el santo grial!! 
       
        
        graph.setCellsEditable(false);
        graph.setAllowDanglingEdges(false); //deshabilita que las aristas se desprendan
        
	mxGraphComponent graphComponent = new mxGraphComponent(graph);               
        graphComponent.getGraph().getModel().endUpdate();
                
 
         jpanel.setLayout(new BorderLayout());
         jpanel.setPreferredSize(new Dimension(584, 399));//POR FIN!!
         jpanel.add(graphComponent,BorderLayout.CENTER);
        
        jpanel.validate();
        jpanel.repaint();
   
        return graphComponent;
        
 
    }
    
    
    
    // UNA VEZ CARGADO EL DATASET, COMIENZA A MOSTRAR LAS PRIMERAS METRICAS SELECCIONADAS EN LA PESTAÑA 
    private void Load_dataset(String filename_database_arff, String filename_database_xml )
    {
        //System.out.println(filename_database_arff);
        
        
               
        try {            
            
             export2.setVisible(true); //boton salvar de la tabla de la izquierda en class imbalance
            
             
//             if(tabsDependences.getSelectedIndex()==0)labelChiThreshold.setVisible(true);
//             else labelChiThreshold.setVisible(false);
             labelChiThreshold.setVisible(true);
            
             
             //para el box diagram
             if(tabsImbalance.getSelectedIndex()==7)jPanel15.setVisible(true);
             else jPanel15.setVisible(false);
            
             if(tabsImbalance.getSelectedIndex()==3 || tabsImbalance.getSelectedIndex()==4 || tabsImbalance.getSelectedIndex()==5|| tabsImbalance.getSelectedIndex()==6) 
             {
                 labelIR1.setVisible(true);
                 labelIR2.setVisible(true);
             }
             else 
             {
                 labelIR1.setVisible(false);
                 labelIR2.setVisible(false);
             }
             
             
            //restableciendo valores
            dataset_train= null;
            dataset_test= null;
            filename_database_arff_test=null;//es el encargado de cargar un test
            
             //new Instances
              
              dataset = new MultiLabelInstances(filename_database_arff, filename_database_xml);
              
             label_frenquency = util.Get_Frequency_x_label(dataset);
             label_frenquency = util.Ordenar_freq_x_attr(label_frenquency);// ordena de mayor a menor
             
             label_imbalanced = util.Get_data_imbalanced_x_label_inter_class(dataset,label_frenquency);
             
             IR_intra_class = util.get_ir_values_intra_class(label_imbalanced);
             HeapSort.sort(IR_intra_class);
             IR_intra_class = HeapSort.get_array_sorted();
             
             //    }
           
            stat1 = new Statistics();
            stat1.calculateStats(dataset);
                                    
            radio=  metrics.DistincLabelset(stat1) /(double)dataset.getNumInstances();

            num_instancias = dataset.getNumInstances();
            
            //Print_Metrics(dataset);
            Print_main_metric_dataset(dataset, stat1);
             
            // graphic label_x_frequency jchart
             label_x_frequency = util.Get_Frequency_x_label(dataset);
             

             
             //actualiza label distribution bar BR a pestaña class imbalance
             CategoryPlot temp1 = cp3.getChart().getCategoryPlot();
             temp1.clearRangeMarkers();
             util.update_values_bar_chart(label_x_frequency,num_instancias,temp1); //actualiza los valores del dataset
             
             //actualiza label distribution line pestaña class imbalance
              HashMap<Integer,Integer> labels_x_example = Get_labelset_x_values(stat1);
                            
             util.update_values_line_chart(num_instancias,cp11.getChart().getCategoryPlot(),labels_x_example); //actualiza los valores del dataset
             
             
             //print frequency´s table         
             jtable_frequency(jTable2,dataset);
                   
             temp1 = cp22.getChart().getCategoryPlot();
             temp1.clearRangeMarkers();
             //actualiza label distribution bar LP a pestaña class imbalance 
            tm_LP1= jchart_and_jtable_label_combination_freq(tableImbalance,dataset,stat1,temp1);

            tm_IR = jtable_imbalanced(tableImbalance,dataset);  
                                
            tm_BR1= jtable_frequency(tableImbalance,dataset);
            tm_attr = jtable_attributes(tableImbalance, dataset);
            tm_jgraph = jtable_label_graph(tableCoOcurrenceLeft, dataset);
            tm_heapmap_graph = jtable_label_graph(tableCoOcurrenceLeft, dataset);
            tm_labelxExamples = jtable_lablelsxExamples(tableImbalance, labels_x_example);
            
            
            tm_ir_per_label_intra_class = jtable_ir_per_label_intra_class(tableImbalance);   //tiene que ejecutarse despues del jtable_imbalanced para que cargue los datos imbabalanced.
            tm_ir_per_label_inter_class = jtable_ir_per_label_inter_class(tableImbalance);  
            
            tm_ir_per_label_inter_class_only = jtable_ir_per_label_inter_class_only(tableImbalance);
            tm_ir_per_label_intra_class_only = jtable_ir_per_label_intra_class_only(tableImbalance);
            
            temp1=cp_per_labelset.getChart().getCategoryPlot();
            temp1.clearRangeMarkers();
            tm_ir_per_labelset = jchart_and_jtable_label_set_IR(tableImbalance,dataset,stat1,cp_per_labelset.getChart().getCategoryPlot());
            
            
            temp1 = cp_ir_x_label_intra_class.getChart().getCategoryPlot();
            temp1.clearRangeMarkers();
            util.update_values_line_chart(id_x_IR, id_x_nums_label, cp_ir_x_label_intra_class.getChart().getCategoryPlot()); //tiene que ejecutarse despues del jtable_ir_per_label_intra_class para que cargue los datos imbabalanced.
            
            temp1 = cp_ir_x_label_inter_class.getChart().getCategoryPlot();
            temp1.clearRangeMarkers();
            
            util.update_values_line_chart(ir_veces.Get_Id_x_IR(), ir_veces.Get_Id_x_Cant_veces(), cp_ir_x_label_inter_class.getChart().getCategoryPlot()); //tiene que ejecutarse despues del jtable_ir_per_label_intra_class para que cargue los datos imbabalanced.
            
            //ir per label inter class only
            temp1= cp_ir_x_label_inter_class_only.getChart().getCategoryPlot();
            temp1.clearRangeMarkers();
            util.update_values_line_chart(IR_inter_class,temp1,true);
            
            //ir per label intra class only
            temp1= cp_ir_x_label_intra_class_only.getChart().getCategoryPlot();
            temp1.clearRangeMarkers();
            util.update_values_line_chart(IR_intra_class,temp1,true);
            
            
            if(tm_BR1 !=null && tm_LP1!=null)
            {               
                
            if(tabsImbalance.getSelectedIndex()==1)
            {
                tableImbalance.setModel(tm_LP1);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Frequency per labelset"));
                
                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_default());
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();

            }
            else if (tabsImbalance.getSelectedIndex()==5) // ir per labelset
            {
                tableImbalance.setModel(tm_ir_per_labelset);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Labelset id per IR"));
                
                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_IR(1));
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();
                
                /*
                jTable4.setModel(tm_IR);
                
                jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Imabalance ratio per label"));
                jTable4.setDefaultRenderer(Object.class, new Mi_Render_IR(1,2));
                jPanel17.repaint();
                jPanel17.validate();
                */
            }
            
            else if (tabsImbalance.getSelectedIndex()==0)
            {
                tableImbalance.setModel(tm_BR1);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Frequency per label"));
                
                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_default());
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();
            }
            else if (tabsImbalance.getSelectedIndex()==6)//imbalance data metric
            {
                tableImbalance.setModel(tm_IR);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Imabalance ratio per label"));
                
		tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_IR(1,2));
                
               /* jTable4.setModel(tm_attr);
                jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Numeric attributes"));
                
                jTable4.setDefaultRenderer(Object.class, new Mi_Render_default());*/
                
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();
            }
            else if (tabsImbalance.getSelectedIndex()==3)
            {
                tableImbalance.setModel(tm_ir_per_label_intra_class);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Number of labels intra class per IR"));
                
                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_IR(2));
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();
            }
            
            else if (tabsImbalance.getSelectedIndex()==2)
            {
                tableImbalance.setModel(tm_labelxExamples);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Labels per example"));
                
                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_default());
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();
                
               /* jTable4.setModel(tm_ir_per_label_inter_class);
                jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Number of labels inter class per IR"));
                
                jTable4.setDefaultRenderer(Object.class, new Mi_Render_IR(2));
                jPanel17.repaint();
                jPanel17.validate();*/
            }
            
            else if (tabsImbalance.getSelectedIndex()==8)
            {
                tableImbalance.setModel(tm_ir_per_label_inter_class_only);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Label per IR values inter class"));
                
                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_IR(1));
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();
            }
            
            else if (tabsImbalance.getSelectedIndex()==9)
            {
                tableImbalance.setModel(tm_ir_per_label_inter_class);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Number of labels inter class per IR"));
                
                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_IR(2));
                /*
                jTable4.setModel(tm_ir_per_label_intra_class_only);
                jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Label per IR values intra class"));
                
                jTable4.setDefaultRenderer(Object.class, new Mi_Render_IR(1));
                * */
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();
            }
            
            else if (tabsImbalance.getSelectedIndex()==4)
            {
                              
                tableImbalance.setModel(tm_ir_per_label_intra_class_only);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Label per IR values intra class"));
                
                
                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_IR(1));
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();
            }
            
            
            else if (tabsImbalance.getSelectedIndex()==7)
            {
                tableImbalance.setModel(tm_attr);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Numeric attributes"));
                
                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_default());
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();
                
            }
            
            else
            {
                tableImbalance.setModel(tm_labelxExamples);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Labels per example"));
                
                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_default());
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();
            }

            tableImbalance.repaint();
            tableImbalance.validate();
            //  System.out.println("SE HA PRESIONADO el "+jTabbedPane2.getSelectedIndex() );
        }
            
            
            // Tabla de chi y fi cuadrado.
            jtable_chi_fi_coefficient(dataset); //jtable de chi & fi coefficient
            
            double critical_value = 6.635;
            
            //jTable10.setModel(tm_coefficient);
            //cambia los colores de la celda creando una instancia de la clase MiRender
            jTable10.setDefaultRenderer(Object.class, new MiRender("chi_fi",critical_value ));
            fixedTable.setDefaultRenderer(Object.class, new MiRender("chi_fi_fixed", critical_value));
            
            
            panelChiFi.repaint();
            panelChiFi.validate();
           
            
             //tm_coocurrences   
              lista_pares = util.Get_pares_atributos(dataset);   
             
             //tm_coocurrences = jtable_coefficient_values(dataset, lista_pares,"coocurrence");
            // jTable11.setModel(tm_coocurrences);
              
             jtable_coefficient_values(dataset, lista_pares,"coocurrence");
             jTable11.setDefaultRenderer(Object.class, new MiRender("estandar",critical_value));
             fixedTable1.setDefaultRenderer(Object.class, new MiRender("chi_fi_fixed",critical_value));
           
             panelCoOcurrenceValues.repaint();
             panelCoOcurrenceValues.validate();

             //tm heapmap values
             //tm_heapmap_values = jtable_coefficient_values(dataset, lista_pares, "heapmap");
             //jTable12.setModel(tm_heapmap_values);
             
             jtable_coefficient_values(dataset, lista_pares,"heapmap");
             jTable12.setDefaultRenderer(Object.class, new MiRender("estandar",critical_value));
             fixedTable2.setDefaultRenderer(Object.class, new MiRender("chi_fi_fixed",critical_value));
             
             panelHeatmapValues.repaint();
             panelHeatmapValues.validate();
            
             //graph co-ocurrence           
             //String[] labelname= dataset.getLabelNames();
             
            tableCoOcurrenceLeft.setRowSelectionAllowed(true);
            tableCoOcurrenceLeft.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
              
            tableCoOcurrenceLeft.setModel(tm_jgraph); //tabla del grafo coocurrence
            tableHeatmapLeft.setModel(tm_heapmap_graph); //tabla del heapmap graph
            
         //escoger los 6 primeros labels mas frequentes de la tabla
             
         ArrayList<String> seleccionados= new  ArrayList();

         int primeros_seleccionados=10;
        if(primeros_seleccionados> dataset.getNumLabels()) primeros_seleccionados = dataset.getNumLabels();
         
        String current=null; 
               
        for(int i=0;i<primeros_seleccionados; i++)
        {
            current = (tableCoOcurrenceLeft.getValueAt(i, 0).toString());
            if(current != null)seleccionados.add(current);
            else break;
        }

       ArrayList<pares_atributos> pares_seleccionados=  util.Encuentra_pares_attr_seleccionados(lista_pares, seleccionados);
        
       String[] labelname1=util.pasa_valores_al_arreglo(seleccionados);
       
       graphComponent  =  Create_jgraphx(panelCoOcurrenceRight,pares_seleccionados,labelname1,graphComponent);
             
            
            // System.out.println("dimensiones del panel jpanel25 "+jPanel25.getBounds().width+ " , "+ jPanel25.getBounds().height);
        //    graphComponent  =  Create_jgraphx(jPanel10,lista_pares,labelname,graphComponent);// balanced
            //create_button_export(jPanel25 ,export6,350,370);


            //System.out.println("dimensiones son "+ jLabel20.getBounds().width+" ancho ,"+ jLabel20.getBounds().height+" alto ");
            
            label_indices_seleccionados = dataset.getLabelIndices();
            jheat_chart= create_heapmap(labelHeatmapGraph, dataset); // balanced
              
             //util.get_chi_fi_coefficient(dataset);
        
             // jpanel8 box diagram
              cp_box.getChart().getXYPlot().clearAnnotations();
               
              DefaultXYDataset xyseriescollection = new DefaultXYDataset();
              DefaultXYDataset xyseriescollection1 = new DefaultXYDataset();
              
              cp_box.getChart().getXYPlot().setDataset(xyseriescollection);             
              cp_box.getChart().getXYPlot().setDataset(1, xyseriescollection1);
            
              //TRAIN TEST
              jButton6.setEnabled(false);
              button_show_traintest.setEnabled(false);
              button_save_train.setEnabled(false);
             
            }
        catch (InvalidDataFormatException ex) {
            Logger.getLogger(CrossValidationExperiment.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Este es el mensaje "+ ex);
            Logger.getLogger(CrossValidationExperiment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void Print_main_metric_dataset(MultiLabelInstances dataset, Statistics stat1 )
    {
            Instances i1= dataset.getDataSet();

                     
            //relation
            labelRelationValue.setText(dataset_current_name);
            
            //cantidad de instancias
            int num_instancias = i1.size();
            labelInstancesValue.setText(Integer.toString(num_instancias));
            
            //cant de atributos
            int num_atributos= i1.numAttributes();
            
            int numero_etiquetas = dataset.getNumLabels();
            labelAttributesValue.setText(Integer.toString(num_atributos-numero_etiquetas));
            
            //cant de etiquetas
            labelLabelsValue.setText(Integer.toString(numero_etiquetas));
            
            //densidad
            labelDensityValue.setText(util.Truncate_value(stat1.density(), 4));
                      
            //cardinalidad
            labelCardinalityValue.setText(util.Truncate_value(stat1.cardinality(), 4));
            
            // distinc labelset           
            int DL = stat1.labelCombCount().size();
            labelDistinctValue.setText(Integer.toString(DL));
            
            //bound
            int bound = (int)metrics.Bound(dataset);
            labelBoundValue.setText(Integer.toString(bound));
            
            //diversity          
            stat1.calculateStats(dataset);
            double diversity = metrics.Diversity(dataset, stat1);
            labelDiversityValue.setText(util.Truncate_values_aprox_zero(Double.toString(diversity), 4));
            
            int value = (int) metrics.labelsxinstancesxfeatures(dataset);
            labelLxIxFValue.setText(Integer.toString(value));
    }
  
    
    private void button_calculateActionPerformed1_general(java.awt.event.ActionEvent evt, JTable jtable, JTable jtable1, JTable jtable2)
    {
        button_save_train.setEnabled(true);
        
        if(choose_test)
        {
            button_calculateActionPerformed1(evt, jTable5, jTable6, jTable7); //OPTION CHOOSE TEST
            return;
        }
        if(holdout_ramdon)
        {
            button_calculateActionPerformed1(evt, jTable5, jTable6, jTable7);// HOLDOUT RAMDON
            return;
        }
        if(holdout_stratified)
        {
           button_calculateActionPerformed1(evt, jTable5, jTable6, jTable7);// HOLDOUT WITH STRATIFICATION
           return;
        }
        if(cv_ramdon)
        {
            button_calculateActionPerformed1(evt, jTable5, jTable6, jTable7,list_dataset_train,list_dataset_test );// RAMDON CROSS VALIDATION
            return;
        }
        if(cv_stratified)
        {
            button_calculateActionPerformed1(evt, jTable5, jTable6, jTable7,list_dataset_train,list_dataset_test );  // CV STRATIFIED
            return;
        }
        
        // SINO ERROR LANZA EXCEPCION
    
    }
    
    
    // CALCULA LAS METRICAS SELECCIONADAS EN LA PESTAÑA TEST
    private void button_calculateActionPerformed1(java.awt.event.ActionEvent evt, JTable jtable, JTable jtable1, JTable jtable2)
    {
       ArrayList<String> metric_list_commun = Get_metrics_selected(jtable);
       ArrayList<String> metric_list_train = Get_metrics_selected(jtable2);
       ArrayList<String> metric_list_test = Get_metrics_selected(jtable2);
       
       if(dataset == null) {
           JOptionPane.showMessageDialog(null, "You must load a dataset", "alert", JOptionPane.ERROR_MESSAGE); 
            return; }
       
       
       int posx = this.getBounds().x;
       int posy = this.getBounds().y;
       
       // System.out.println(posx+" "+posy);
       boolean flag=true;
        
       if(!list_dataset_train.isEmpty()) flag=false;
       
       
       if((dataset_train ==null || dataset_test == null) && flag ){
            JOptionPane.showMessageDialog(null, "You must to click Start", "alert", JOptionPane.ERROR_MESSAGE); 
            return;}
       
       
          if((dataset_train ==null || dataset_test == null))
               {
                JOptionPane.showMessageDialog(null, "You must to click Start", "alert", JOptionPane.ERROR_MESSAGE); 
                return;
               }
             
                metric_output mo = new metric_output(metric_list_commun,metric_list_train,metric_list_test,dataset_train,dataset_test, posx, posy,es_de_tipo_meka);
                mo.setVisible(true);
            
         
         
    }   
    
    
     // CALCULA LAS METRICAS SELECCIONADAS EN LA PESTAÑA TEST PARA CROSS VALIDATION
    private void button_calculateActionPerformed1(java.awt.event.ActionEvent evt, JTable jtable, JTable jtable1, JTable jtable2, ArrayList<MultiLabelInstances> dataset_train_ml, ArrayList<MultiLabelInstances> dataset_test_ml )
    {
       ArrayList<String> metric_list_commun = Get_metrics_selected(jtable);
       ArrayList<String> metric_list_train = Get_metrics_selected(jtable2);
       ArrayList<String> metric_list_test = Get_metrics_selected(jtable2);
       
       if(dataset == null) {
           JOptionPane.showMessageDialog(null, "You must load a dataset", "alert", JOptionPane.ERROR_MESSAGE); 
            return; }
       
       int posx = this.getBounds().x;
       int posy = this.getBounds().y;
       
       metric_output mo = new metric_output(metric_list_commun,metric_list_train,metric_list_test,dataset_train_ml,dataset_test_ml,posx,posy,es_de_tipo_meka);
       
       mo.setVisible(true);
       
    }   
    
    //SALVAR EN LA PESTAÑA TRAIN/TEST DATASET
     private void button_saveActionPerformed1(java.awt.event.ActionEvent evt, JTable jtable, JTable jtable1, JTable jtable2) throws IOException
    {
       ArrayList<String> metric_list_commun = Get_metrics_selected(jtable);
       ArrayList<String> metric_list_train = Get_metrics_selected(jtable2);
       ArrayList<String> metric_list_test = Get_metrics_selected(jtable2);
      
       String dataset_name11;
      
       if(dataset == null ) {
           JOptionPane.showMessageDialog(null, "You must load a dataset", "alert", JOptionPane.ERROR_MESSAGE); 
           return; }
       
       if((dataset_train ==null || dataset_test == null )&& list_dataset_train.isEmpty()){
            JOptionPane.showMessageDialog(null, "You must to click Start", "alert", JOptionPane.ERROR_MESSAGE); 
            return;}
       
       
       
       // JFILECHOOSER SAVE
         JFileChooser fc= new JFileChooser();
        
         // extension txt
        FileNameExtensionFilter fname = new FileNameExtensionFilter(".txt", "txt");
        FileNameExtensionFilter fname1 = new FileNameExtensionFilter(".csv", "csv");
        FileNameExtensionFilter fname2 = new FileNameExtensionFilter("MEKA-arff", "MEKA-arff");
        FileNameExtensionFilter fname3 = new FileNameExtensionFilter("MULAN-arff", "MULAN-arff");
        
        //eliminar el que tiene por defecto
        fc.removeChoosableFileFilter(fc.getChoosableFileFilters()[0]);
        
        fc.setFileFilter(fname);
        fc.setFileFilter(fname2);
        fc.setFileFilter(fname3);
        fc.setFileFilter(fname1);
        
        fc.addChoosableFileFilter(fname);
        String path;
        
        int returnVal = fc.showSaveDialog(this);
         
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
               File file = fc.getSelectedFile();
                FileFilter f1 = fc.getFileFilter();
                
              if(f1.getDescription().equals(".txt"))
                {
               
                //This is where a real application would save the file.
               // System.out.println("Saving: "  + file.getName()+ " ruta "+file.getAbsolutePath());
                
                // BufferedWriter AND  PrintWriter
                path = file.getAbsolutePath() +".txt";
                
                BufferedWriter bw = new BufferedWriter(new FileWriter(path));
                PrintWriter wr = new PrintWriter(bw);
                
                if(radioRandomCV.isSelected() || radioStratifiedCV.isSelected())//CV STRATIFIED AND RAMDON
                {
                    //System.out.println("se va a guardar los txt");
                    util.Save_in_the_file_txt(wr,metric_list_commun,metric_list_train,metric_list_test,list_dataset_train,list_dataset_test, es_de_tipo_meka);
                }
               // util.Save_in_the_file(wr, metric_list, dataset, stat1);
                else{
                util.Save_in_the_file(wr, metric_list_commun, metric_list_train, metric_list_test, dataset_train, dataset_test, es_de_tipo_meka);
                }
                
                JOptionPane.showMessageDialog(null, "File Saved", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                wr.close();
                bw.close();      
                }
              
              else if(f1.getDescription().equals(".csv"))
               {
                
                  if(radioRandomCV.isSelected() || radioStratifiedCV.isSelected())//CV STRATIFIED AND RAMDON
                    {
                      try
                        {
                            dataset_name11 = dataset_name1.substring(0,dataset_name1.length()-5);
                            path = file.getAbsolutePath() +".csv";
                            
                            Exporter exp = new Exporter(new File(path), metric_list_commun,metric_list_train,metric_list_test);
                     
                             if(exp.exporta(list_dataset_train,list_dataset_test,dataset_name11,es_de_tipo_meka))
                                 {
                                    JOptionPane.showMessageDialog(null, "File Saved", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                                 }
                        }

                      catch(Exception e1)
                        {
                            System.out.println("este es el mensaje"+ e1.getMessage());
                            JOptionPane.showMessageDialog(null, "File NOT Saved correctly", "Error", JOptionPane.ERROR_MESSAGE); 
                        }   
                        
                      //util.Save_in_the_file_csv(wr,metric_list_commun,metric_list_train,metric_list_test,list_dataset_train,list_dataset_test,dataset_current_name);
                    }
                  
                  else //holdout STRATIFIED AND RAMDON
                  {
                      try
                        {
                            dataset_name11 = dataset_name1.substring(0,dataset_name1.length()-5);
                            path = file.getAbsolutePath() +".csv";
                            
                            Exporter exp = new Exporter(new File(path), metric_list_commun,metric_list_train,metric_list_test);
                     
                             if(exp.exporta(dataset_train,dataset_test,dataset_name11,es_de_tipo_meka))
                                 {
                                    JOptionPane.showMessageDialog(null, "File Saved", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                                 }
                        }

                      catch(Exception e1)
                        {
                            System.out.println("este es el mensaje "+e1.getMessage());
                            JOptionPane.showMessageDialog(null, "File NOT Saved correctly", "Error", JOptionPane.ERROR_MESSAGE); 
                        }   
                      //util.Save_in_the_file_csv(wr, metric_list_commun, metric_list_train, metric_list_test, dataset_train, dataset_test,dataset_current_name);
                  }

               }
              
              else if (f1.getDescription().equals("MEKA-arff"))
                {
                    
                    path = file.getAbsolutePath() +".arff";
                
                    BufferedWriter bw = new BufferedWriter(new FileWriter(path));
                    PrintWriter wr = new PrintWriter(bw);
                    
                    //adaptar el metric_list y el list_dataset
                    
                    ArrayList<String> metric_list_all = new ArrayList(metric_list_commun);
                    
                    for(String current : metric_list_test)
                        metric_list_all.add(current);
                    
                    if(radioRandomCV.isSelected() || radioStratifiedCV.isSelected())//CV STRATIFIED AND RAMDON
                    {
                         util.Save_in_the_file_arff_meka(wr, metric_list_all, list_dataset_train,list_dataset_test,file.getName(),es_de_tipo_meka);
                    }
                    else
                    {
                        util.Save_in_the_file_arff_meka(wr, metric_list_all, dataset_train, dataset_test, dataset_name1, es_de_tipo_meka);
                    }
                                   
                    
                    
                    wr.close();
                    bw.close();      
                    
                   JOptionPane.showMessageDialog(null, "File Saved", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                   
                }
              
                else if (f1.getDescription().equals("MULAN-arff"))
                {
                    
                    path = file.getAbsolutePath() +".arff";
                
                    BufferedWriter bw = new BufferedWriter(new FileWriter(path));
                    PrintWriter wr = new PrintWriter(bw);
                    
                    //adaptar el metric_list y el list_dataset
                    
                    ArrayList<String> metric_list_all = new ArrayList(metric_list_commun);
                    
                    for(String current : metric_list_test)
                        metric_list_all.add(current);
                    
                    if(radioRandomCV.isSelected() || radioStratifiedCV.isSelected())//CV STRATIFIED AND RAMDON
                    {
                         util.Save_in_the_file_arff_mulan(wr, metric_list_all, list_dataset_train,list_dataset_test,file.getName(), es_de_tipo_meka);
                    }
                    else
                    {
                        util.Save_in_the_file_arff_mulan(wr, metric_list_all, dataset_train, dataset_test, dataset_name1, es_de_tipo_meka);
                    }
                                   
                    
                    
                    wr.close();
                    bw.close();      
                    
                   JOptionPane.showMessageDialog(null, "File Saved", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                   
                }
                
        } 
    }
     
     //BOTON GRAPHIC EN LA PESTAÑA MULTIDATASET
     private void button_graphicActionPerformed_multidatasets(java.awt.event.ActionEvent evt)throws IOException
     {
        //PieChart demo = new PieChart("Comparison", "Which operating system are you using?");
         
           
       if(list_dataset.isEmpty()) {
           JOptionPane.showMessageDialog(null, "you must load a dataset ", "Warning", JOptionPane.ERROR_MESSAGE);
           return; 
       }
                    
         grapchics demo = new grapchics(list_dataset, Dataset_names);
         demo.setVisible(true);
     }
     
     // BOTON SALVAR EN LA PESTAÑA MULTI DATASET
     private void button_saveActionPerformed_multidatasets(java.awt.event.ActionEvent evt, JTable jtable) throws IOException
    {
        ArrayList<String> metric_list = Get_metrics_selected(jtable);
        
        
       if(list_dataset.isEmpty()) {
           JOptionPane.showMessageDialog(null, "you must load a dataset ", "Warning", JOptionPane.ERROR_MESSAGE);
           return; 
       }
                 
       // JFILECHOOSER SAVE
         JFileChooser fc= new JFileChooser();
        
         // extension txt
        FileNameExtensionFilter fname = new FileNameExtensionFilter(".csv", "csv");
        FileNameExtensionFilter fname1 = new FileNameExtensionFilter(".xls", "xls");
        FileNameExtensionFilter fname2 = new FileNameExtensionFilter("MULAN-arff", "MULAN-arff");
        FileNameExtensionFilter fname3 = new FileNameExtensionFilter("MEKA-arff", "MEKA-arff");
        
        //eliminar el que tiene por defecto
        fc.removeChoosableFileFilter(fc.getChoosableFileFilters()[0]);
        
        fc.addChoosableFileFilter(fname);
        fc.addChoosableFileFilter(fname1);
        fc.addChoosableFileFilter(fname2);          
        fc.addChoosableFileFilter(fname3);   
        
        //fc.setFileFilter(fname);
        
        int returnVal = fc.showSaveDialog(this);
        
               
        //String FilePath = SomeFile.getPath();  
         
        if (returnVal == JFileChooser.APPROVE_OPTION  )
        {
            
                File file = fc.getSelectedFile();
                FileFilter f1 = fc.getFileFilter();
                
                System.out.println("escogio la opcion "+f1.getDescription());
               
                if(f1.getDescription().equals(".csv"))
                {
                      String path = file.getAbsolutePath() +".csv";
                      Exporter exp = new Exporter(new File(path));
                     
                     if(exp.exporta(metric_list,list_dataset,Dataset_names,es_de_tipo_meka))
                     {
                     }
                     
                }
                
                else if(f1.getDescription().equals(".xls"))
                {
                      String path = file.getAbsolutePath() +".xls";
                      Exporter exp = new Exporter(new File(path));
                     
                     if(exp.exporta(metric_list,list_dataset,Dataset_names,es_de_tipo_meka))
                     {
                         
                     }
                     
                }
                
                else if (f1.getDescription().equals("MULAN-arff"))
                {
                    
                    String path = file.getAbsolutePath() +".arff";
                
                    BufferedWriter bw = new BufferedWriter(new FileWriter(path));
                    PrintWriter wr = new PrintWriter(bw);
                                   
                    util.Save_in_the_file_arff(wr, metric_list, list_dataset,file.getName(), es_de_tipo_meka);
                    
                    wr.close();
                    bw.close();      
                    
                
                }
                
                 else if (f1.getDescription().equals("MEKA-arff"))
                {
                    
                    String path = file.getAbsolutePath() +".arff";
                
                    BufferedWriter bw = new BufferedWriter(new FileWriter(path));
                    PrintWriter wr = new PrintWriter(bw);
                                   
                    util.Save_in_the_file_arff_meka(wr, metric_list, list_dataset,file.getName(), es_de_tipo_meka);
                    
                    wr.close();
                    bw.close();      
                    
                
                }
                
           JOptionPane.showMessageDialog(null, "File Saved", "Successful", JOptionPane.INFORMATION_MESSAGE);
           Toolkit.getDefaultToolkit().beep();
                
        } 

        
    }
             
             
             
     //BOTON SALVAR EN LA PESTAÑA DATABASE
     private void button_saveActionPerformed(java.awt.event.ActionEvent evt, JTable jtable) throws IOException
    {
        ArrayList<String> metric_list = Get_metrics_selected(jtable);
        
        
         if(dataset == null) {
           JOptionPane.showMessageDialog(null, "you must load a dataset ", "Warning", JOptionPane.ERROR_MESSAGE);
           return; 
       }
             
        
       // JFILECHOOSER SAVE
         JFileChooser fc= new JFileChooser();
        
         // extension txt
        FileNameExtensionFilter fname = new FileNameExtensionFilter(".txt", "txt");
        FileNameExtensionFilter fname1 = new FileNameExtensionFilter(".csv", "csv");
        FileNameExtensionFilter fname3 = new FileNameExtensionFilter("MEKA-arff", "MEKA-arff");
        
        //eliminar el que tiene por defecto
        fc.removeChoosableFileFilter(fc.getChoosableFileFilters()[0]);
        
        fc.setFileFilter(fname);
        fc.setFileFilter(fname3);
        fc.setFileFilter(fname1);
        
        
        fc.addChoosableFileFilter(fname);
        
        int returnVal = fc.showSaveDialog(this);
         
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
                File file = fc.getSelectedFile();
                FileFilter f1 = fc.getFileFilter();
                
                if(f1.getDescription().equals(".txt"))
                {
                     String path = file.getAbsolutePath() +".txt";
                
                     BufferedWriter bw = new BufferedWriter(new FileWriter(path));
                     PrintWriter wr = new PrintWriter(bw);
                
                     util.Save_in_the_file(wr, metric_list, dataset, label_imbalanced, es_de_tipo_meka);
                
                    wr.close();
                    bw.close(); 
                    
                    JOptionPane.showMessageDialog(null, "File Saved", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                    
                }
                
               else if(f1.getDescription().equals(".csv"))
               {
              
                try
                 {
                     dataset_name1 = dataset_name1.substring(0,dataset_name1.length()-5);
                     String path = file.getAbsolutePath() +".csv";
                      Exporter exp = new Exporter(new File(path), dataset_name1);
                     
                     if(exp.exporta(metric_list,dataset,es_de_tipo_meka))
                     {
                         JOptionPane.showMessageDialog(null, "File Saved", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                     }
                 }
                 catch(Exception e1)
                 {
                     JOptionPane.showMessageDialog(null, "File NOT Saved correctly", "Error", JOptionPane.ERROR_MESSAGE); 
                 }   
                
               }
                
                //COMPROBAR SI TIENE SENTIDO SALVAR UNA SOLA LINEA.
               else if (f1.getDescription().equals("MEKA-arff"))
                {
                    
                    String path = file.getAbsolutePath() +".arff";
                
                    BufferedWriter bw = new BufferedWriter(new FileWriter(path));
                    PrintWriter wr = new PrintWriter(bw);
                    
                    ArrayList<MultiLabelInstances> list_dataset1 = new ArrayList();
                    list_dataset1.add(dataset);
                                   
                    util.Save_in_the_file_arff_meka(wr, metric_list, list_dataset1,file.getName(), es_de_tipo_meka);
                    
                    wr.close();
                    bw.close();      
                    
                  JOptionPane.showMessageDialog(null, "File Saved", "Successful", JOptionPane.INFORMATION_MESSAGE);     
                }
                
           Toolkit.getDefaultToolkit().beep();
        } 

            
        
    }
     
     //CALCULA LAS METRICAS ESPECIFICADAS
     private void button_calculateActionPerformed_datasets(java.awt.event.ActionEvent evt, JTable jtable)
    {
       ArrayList<String> metric_list = Get_metrics_selected(jtable);
        
       
       if(list_dataset.isEmpty()) {
           JOptionPane.showMessageDialog(null, "you must load a dataset ", "Warning", JOptionPane.ERROR_MESSAGE);
           return; 
       }
              
           
       
       metric_output mo = new metric_output(metric_list,list_dataset, Dataset_names, lista_son_meka );
       
       mo.setVisible(true);
     
    }   
     
     
     //CALCULA LAS METRICAS ESPECIFICADAS
     private void button_calculateActionPerformed(java.awt.event.ActionEvent evt, JTable jtable)
    {
       ArrayList<String> metric_list = Get_metrics_selected(jtable);
        
       if(dataset == null) {
           JOptionPane.showMessageDialog(null, "you must load a dataset ", "Warning", JOptionPane.ERROR_MESSAGE);
           return; 
       }
       
       
       int posx = this.getBounds().x;
       int posy = this.getBounds().y;
       
       
       metric_output mo = new metric_output(metric_list,dataset, posx, posy, es_de_tipo_meka);
       
       mo.setVisible(true);
     
    }   
     
     
     
    
     private ArrayList<String> Get_metrics_selected(JTable jtable)
     {
       ArrayList<String> result= new ArrayList();
       TableModel tmodel = jtable.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
       {
           if((Boolean)tmodel.getValueAt(i, 1))
           {
               String selected =(String)tmodel.getValueAt(i, 0);
               result.add(selected);                      
           }                
       }   
       return result;
     }
     
     private void button_invertActionPerformed(java.awt.event.ActionEvent evt,JTable jtable )
    {
       TableModel tmodel = jtable.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
       {
           if((Boolean)tmodel.getValueAt(i, 1)) tmodel.setValueAt(Boolean.FALSE, i, 1);
           else  tmodel.setValueAt(Boolean.TRUE, i, 1);          
       }      
       
       jtable.setModel(tmodel);
       jtable.repaint();
       
    }     
    
          private void button_invertActionPerformed2(java.awt.event.ActionEvent evt,JTable jtable,JTable jtable1,JTable jtable2 )
    {
       TableModel tmodel = jtable.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
       {
           if((Boolean)tmodel.getValueAt(i, 1)) tmodel.setValueAt(Boolean.FALSE, i, 1);
           else  tmodel.setValueAt(Boolean.TRUE, i, 1);          
       }      
       
       jtable.setModel(tmodel);
       jtable.repaint();
       
       tmodel = jtable1.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
       {
           if((Boolean)tmodel.getValueAt(i, 1)) tmodel.setValueAt(Boolean.FALSE, i, 1);
           else  tmodel.setValueAt(Boolean.TRUE, i, 1);          
       }      
       
       jtable1.setModel(tmodel);
       jtable1.repaint();
       
       
        tmodel = jtable2.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
       {
           if((Boolean)tmodel.getValueAt(i, 1)) tmodel.setValueAt(Boolean.FALSE, i, 1);
           else  tmodel.setValueAt(Boolean.TRUE, i, 1);          
       }      
       
       jtable2.setModel(tmodel);
       jtable2.repaint();
    }  
     
    
    private void button_noneActionPerformed(java.awt.event.ActionEvent evt,JTable jtable)
    {
      TableModel tmodel = jtable.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
           tmodel.setValueAt(Boolean.FALSE, i, 1);
             
       jtable.setModel(tmodel);
       jtable.repaint();
    }        
    
        private void button_noneActionPerformed2(java.awt.event.ActionEvent evt,JTable jtable,JTable jtable1,JTable jtable2)
    {
      TableModel tmodel = jtable.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
           tmodel.setValueAt(Boolean.FALSE, i, 1);
       
       jtable.setModel(tmodel);
       jtable.repaint();
       
       tmodel = jtable1.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
           tmodel.setValueAt(Boolean.FALSE, i, 1);
       
       jtable1.setModel(tmodel);
       jtable1.repaint();
       
       
       tmodel = jtable2.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
           tmodel.setValueAt(Boolean.FALSE, i, 1);
       
       jtable2.setModel(tmodel);
       jtable2.repaint();
    }        
            
    private void button_allActionPerformed(java.awt.event.ActionEvent evt ,JTable jtable)
    {
       TableModel tmodel = jtable.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
           tmodel.setValueAt(Boolean.TRUE, i, 1);
             
       jtable.setModel(tmodel);
       jtable.repaint();
    }
    

    
    private void button_export_ActionPerformed(java.awt.event.ActionEvent evt ,JTable jtable)
    {
          if(jtable.getRowCount()==0 || dataset == null)
          {
              JOptionPane.showMessageDialog(null, "the table is empty ", "Error", JOptionPane.ERROR_MESSAGE); 
              return;
          }
        
         // JFILECHOOSER SAVE
         JFileChooser fc= new JFileChooser();
        
         // extension 
        FileNameExtensionFilter fname = new FileNameExtensionFilter(".xls", "xls"); 
        FileNameExtensionFilter fname1 =  new FileNameExtensionFilter(".csv", "csv");
        
        //eliminar el que tiene por defecto
        fc.removeChoosableFileFilter(fc.getChoosableFileFilters()[0]);
        
        fc.setFileFilter(fname);
        fc.setFileFilter(fname1);
        fc.addChoosableFileFilter(fname);
        
        int returnVal = fc.showSaveDialog(this);
         
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
               File file = fc.getSelectedFile();
                FileFilter f1 = fc.getFileFilter();
                
              if(f1.getDescription().equals(".csv"))
                {
               
                try
                 {
                     String path = file.getAbsolutePath() +".csv";
                     Exporter exp = new Exporter(new File(path), jtable, "prueba");
                     
                     if(exp.exporta())
                     {
                         JOptionPane.showMessageDialog(null, "File Saved", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                     }
                 }
                 catch(Exception e1)
                 {
                     JOptionPane.showMessageDialog(null, "File NOT Saved correctly", "Error", JOptionPane.ERROR_MESSAGE); 
                 }   
                             
                }
              
              else if(f1.getDescription().equals(".xls"))
               {                             
                               
                 try
                 {
                     String path = file.getAbsolutePath() +".xls";
                     Exporter exp = new Exporter(new File(path), jtable, "prueba");
                     
                     if(exp.exporta())
                     {
                         JOptionPane.showMessageDialog(null, "File Saved", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                     }
                 }
                 catch(Exception e1)
                 {
                     JOptionPane.showMessageDialog(null, "File NOT Saved correctly", "Error", JOptionPane.ERROR_MESSAGE); 
                 }
               
               }
                
        } 
        
        
    }
    
     private void button_export_ActionPerformed(java.awt.event.ActionEvent evt ,JTable jtable, JTable columns)
    {
          if(jtable.getRowCount()==0 || dataset == null)
          {
              JOptionPane.showMessageDialog(null, "the table is empty ", "Error", JOptionPane.ERROR_MESSAGE); 
              return;
          }
        
         // JFILECHOOSER SAVE
         JFileChooser fc= new JFileChooser();
        
         // extension 
        FileNameExtensionFilter fname = new FileNameExtensionFilter(".xls", "xls"); 
        FileNameExtensionFilter fname1 =  new FileNameExtensionFilter(".csv", "csv");
        
        //eliminar el que tiene por defecto
        fc.removeChoosableFileFilter(fc.getChoosableFileFilters()[0]);
        
        fc.setFileFilter(fname);
        fc.setFileFilter(fname1);
        fc.addChoosableFileFilter(fname);
        
        int returnVal = fc.showSaveDialog(this);
         
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
               File file = fc.getSelectedFile();
                FileFilter f1 = fc.getFileFilter();
                
              if(f1.getDescription().equals(".csv"))
                {
               
                try
                 {
                     String path = file.getAbsolutePath() +".csv";
                     Exporter exp = new Exporter(new File(path), jtable, "prueba");
                     
                     if(exp.exporta(columns))
                     {
                         JOptionPane.showMessageDialog(null, "File Saved", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                     }
                 }
                 catch(Exception e1)
                 {
                     System.out.println("otro mensaje "+e1.toString());
                     JOptionPane.showMessageDialog(null, "File NOT Saved correctly", "Error", JOptionPane.ERROR_MESSAGE); 
                 }   
                             
                }
              
              else if(f1.getDescription().equals(".xls"))
               {                             
                               
                 try
                 {
                     String path = file.getAbsolutePath() +".xls";
                     Exporter exp = new Exporter(new File(path), jtable, "prueba");
                     
                     if(exp.exporta(columns))
                     {
                         JOptionPane.showMessageDialog(null, "File Saved", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                     }
                 }
                 catch(Exception e1)
                 {
                     JOptionPane.showMessageDialog(null, "File NOT Saved correctly", "Error", JOptionPane.ERROR_MESSAGE); 
                 }
               
               }
                
        } 
        
        
    }
    
    
    
      private void button_allActionPerformed2(java.awt.event.ActionEvent evt ,JTable jtable,JTable jtable1,JTable jtable2 )
    {
       TableModel tmodel = jtable.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
           tmodel.setValueAt(Boolean.TRUE, i, 1);
       
       jtable.setModel(tmodel);
       jtable.repaint();
       
       tmodel = jtable1.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
           tmodel.setValueAt(Boolean.TRUE, i, 1);
       
       jtable1.setModel(tmodel);
       jtable1.repaint();
       
       
       tmodel = jtable2.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
           tmodel.setValueAt(Boolean.TRUE, i, 1);
       
       jtable2.setModel(tmodel);
       jtable2.repaint();
       

    }
    
       
  private ChartPanel generaGrafico(JPanel jpanel) 
  {
     XYDataset xydataset  = new DefaultXYDataset();
     JFreeChart jfreechart = ChartFactory.createXYLineChart("Box diagram", "Distribution of numeric values", "", xydataset, PlotOrientation.VERTICAL, false, true, false);
 
         XYPlot xyplot = (XYPlot) jfreechart.getPlot();
         xyplot.setBackgroundPaint(Color.white);
         xyplot.setDomainGridlinePaint(Color.gray);
         xyplot.setRangeGridlinePaint(Color.gray);
         
     //XYLineAndShapeRenderer xylineandshaperenderer =  (XYLineAndShapeRenderer) xyplot.getRenderer();
     //xylineandshaperenderer.setBaseShapesVisible(true);
         
        //ocultar el eje de las y
        xyplot.getRangeAxis().setTickLabelsVisible(false);  
 
        ChartPanel cp1 = new ChartPanel(jfreechart);
        cp1.setSize(new Dimension(450,300));        
        cp1.setBounds(260,100,450,300);
        cp1.setPreferredSize(new Dimension(450,300));
        cp1.repaint();
        
        //ChartFrame f1= new ChartFrame("PROBANDO", chart1);
        jpanel.setBounds(260,100,450,300);
        jpanel.setLayout(new BorderLayout());
        jpanel.add(cp1,BorderLayout.CENTER);
        jpanel.repaint();
        jpanel.validate();
        
        return cp1;       
 }
  
  
    
    
    private ChartPanel create_jchart(JPanel jpanel, String type, String title_x_axis, String title_y_axis, boolean show_x_axis)
    {
        DefaultCategoryDataset my_data = new DefaultCategoryDataset();
        JFreeChart chart1;
            //"Number of labels per Examples"
      //  chart1 = ChartFactory.createLineChart(" ", title_x_axis, "Relative frequency ", my_data, PlotOrientation.VERTICAL, false, true, false);
        
        CategoryPlot plot1;
        
        //hide horizontal axis
        if(type =="bar")
        {
          chart1 = ChartFactory.createBarChart(" ", title_x_axis, title_y_axis, my_data, PlotOrientation.VERTICAL, false, true, false);
        
          plot1 =  chart1.getCategoryPlot();
          plot1.setRangeGridlinePaint(Color.black);

        }
        else if(type =="line_2_axis")
        {
          chart1 = ChartFactory.createLineChart(" ",title_x_axis,title_y_axis , my_data, PlotOrientation.VERTICAL, true, true, false);
          
          plot1 =  chart1.getCategoryPlot();
          plot1.setRangeGridlinePaint(Color.black);
         
          //para mostrar mini rectangulos
          LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer)plot1.getRenderer();
          lineandshaperenderer.setBaseShapesVisible(true);
          
          CategoryAxis domainAxis = plot1.getDomainAxis();
          domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
          ValueAxis axis2 = new NumberAxis("# Labels");
          plot1.setRangeAxis(1, axis2);
          plot1.mapDatasetToRangeAxis(1, 1);
        
          LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
          plot1.setRenderer(1, renderer2);
        }
        
        
        else //type == "line")
        {
          chart1 = ChartFactory.createLineChart(" ",title_x_axis,title_y_axis , my_data, PlotOrientation.VERTICAL, false, true, false);
          
          plot1 =  chart1.getCategoryPlot();
          plot1.setRangeGridlinePaint(Color.black);
         
          //para mostrar mini rectangulos
          LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer)plot1.getRenderer();
          lineandshaperenderer.setBaseShapesVisible(true);
                        
        }
        
        //ocultar el eje de las x
        plot1.getDomainAxis().setTickLabelsVisible(show_x_axis);     
                 
        ChartPanel cp1 = new ChartPanel(chart1);
        cp1.setSize(new Dimension(450,300));        
        cp1.setBounds(260,100,450,300);
        cp1.setPreferredSize(new Dimension(450,300));
        cp1.repaint();
        
        //ChartFrame f1= new ChartFrame("PROBANDO", chart1);
        jpanel.setBounds(260,100,450,300);
        jpanel.setLayout(new BorderLayout());
        jpanel.add(cp1,BorderLayout.CENTER);
        jpanel.repaint();
        jpanel.validate();
        
        return cp1;       
    }
    
    
    public static int get_Mayor(Set<LabelSet> keysets ,HashMap<LabelSet,Integer> result)
    {
        int mayor=0;
        
        for(LabelSet current : keysets)
            if(mayor<result.get(current))mayor=result.get(current);
        
        return mayor;
    }
   
     //JTABLE AND JCHART VISUALIZE-LP
    private TableModel jchart_and_jtable_label_set_IR(JTable jtable, MultiLabelInstances dataset ,Statistics stat1, CategoryPlot cp ) throws Exception
    {
        //grafico
        DefaultTableModel table_model1= new DefaultTableModel()
                          {
            public boolean isCellEditable(int row, int column)
            {
                return false;//This causes all cells to be not editable
            }
          };

        DefaultCategoryDataset my_data = new DefaultCategoryDataset();
               
        table_model1.addColumn("Labelset id");
        table_model1.addColumn("IR values");
        

        //CALCULA LA FRECUENCIA DE LOS LABEL-COMBINATION
        HashMap<LabelSet,Integer> result = stat1.labelCombCount();
        
        Set<LabelSet> keysets = result.keySet();
        
        Object[] fila = new Object[2];
        
        int count=1;
        double IR_labelset;
        int mayor = get_Mayor(keysets, result);
        
        ArrayList<atributo> lista1 = new ArrayList();
        atributo temp;
       // System.out.println("TESTING CODE");
        int value;
        
         for(LabelSet current : keysets)
        {
              value=  result.get(current); //es la cantidad de veces que aparece el labelset en el dataset
              IR_labelset = mayor /(value*1.0);
              String temp1 =util.Truncate_value(IR_labelset, 4);
              IR_labelset = Double.parseDouble(temp1);
              
              //System.out.println(" labelname "+current.toString());
              temp = new atributo(current.toString(), value,IR_labelset);
              lista1.add(temp);
        }      
         
         labelsets_sorted_IR = new atributo[lista1.size()];
         labelset_per_ir = new double[lista1.size()]; //guarda los ir por labelset
         
         String truncate;
         
        while(!lista1.isEmpty())
        {
            temp = util.Devuelve_menor(lista1);
            
            labelsets_sorted_IR[count-1]= temp;
            labelset_per_ir[count-1]=temp.get_ir();
            //String label_name = current.toString();
            
             fila[0]=count;    
             
             truncate = Double.toString(temp.get_ir());
             fila[1]= util.Truncate_values_aprox_zero(truncate,5);
             
             table_model1.addRow(fila);
             
              my_data.setValue(temp.get_ir(), "",Integer.toString(count));
              
              count++;
              lista1.remove(temp);
        }
        
          jtable.setModel(table_model1);
          jtable.setBounds(jtable.getBounds());
          
           //RESIZE COLUMNS! jtable
            TableColumnModel tcm = jtable.getColumnModel();
            
            tcm.getColumn(0).setPreferredWidth(50);
            tcm.getColumn(1).setPreferredWidth(50);
            
            //grafico
          cp.setDataset(my_data);
          
          //get mean
          double sum=0;
          for(int i=0; i<labelsets_sorted_IR.length;i++)
          {
              sum+= labelsets_sorted_IR[i].get_ir();
          }
          sum = sum/labelsets_sorted_IR.length;

         
          // add a labelled marker for the bid start price...
          
            Marker start = new ValueMarker(sum);
            start.setPaint(Color.blue);
            start.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
            start.setLabel("                        Mean: "+util.Truncate_value(sum, 4));
            cp.addRangeMarker(start);
                        
        
          return jtable.getModel();
    }
 
    
    
    //JTABLE AND JCHART VISUALIZE-LP
    private TableModel jchart_and_jtable_label_combination_freq(JTable jtable, MultiLabelInstances dataset ,Statistics stat1, CategoryPlot cp ) throws Exception
    {
        //grafico
        DefaultTableModel table_model1= new DefaultTableModel()
                          {
            public boolean isCellEditable(int row, int column)
            {
                return false;//This causes all cells to be not editable
            }
          };

        DefaultCategoryDataset my_data = new DefaultCategoryDataset();
               
        table_model1.addColumn("Labelset Id");
        table_model1.addColumn("# Examples");
        table_model1.addColumn("Frequency");
                      
        double freq ;
        
        //CALCULA LA FRECUENCIA DE LOS LABEL-COMBINATION
        HashMap<LabelSet,Integer> result = stat1.labelCombCount();
        
        double sum=0.0;
        Set<LabelSet> keysets = result.keySet();
        
        Object[] fila = new Object[3];
        
        int count=1;
        
        ArrayList<atributo> lista1 = new ArrayList();
        atributo temp;
       // System.out.println("TESTING CODE");
        int value;
         for(LabelSet current : keysets)
        {
              value=  result.get(current); //es la cantidad de veces que aparece el labelset en el dataset
            //  System.out.println(" value "+ value);
              temp = new atributo(current.toString(), value);
              lista1.add(temp);
        }      
        
         
         labelsets_sorted = new atributo[lista1.size()];
         labelset_frequency = new double[lista1.size()];
         
        while(!lista1.isEmpty())
        {
            temp = util.Devuelve_mayor(lista1);
            //temp = util.Devuelve_menor(lista1);
            
           
            
            labelsets_sorted[count-1]= temp;
            
            
            //String label_name = current.toString();
            
            value = temp.get_frequency();
            
            labelset_frequency[count-1]= value;
                    
            fila[0]=count;
             
             freq =value*1.0/dataset.getNumInstances();
             
              sum += freq;
             
             String value_freq =Double.toString(freq);
             
             fila[1]= value;
             
             fila[2]= util.Truncate_values_aprox_zero(value_freq,4);
             
             
              
             table_model1.addRow(fila);
             
             
             
             // representar en el grafico
             String id = "ID: "+Integer.toString(count)+" , "+"Labelset: ";
             
              my_data.setValue(freq, id + temp.get_name(),"");
              
              count++;
              lista1.remove(temp);
        }
        
          jtable.setModel(table_model1);
          jtable.setBounds(jtable.getBounds());
          
           //RESIZE COLUMNS! jtable
            TableColumnModel tcm = jtable.getColumnModel();
            
            tcm.getColumn(0).setPreferredWidth(50);
            tcm.getColumn(1).setPreferredWidth(50);
            tcm.getColumn(2).setPreferredWidth(60);
            
            //grafico
          cp.setDataset(my_data);
          
          
          // add a labelled marker for the bid start price...
            sum = sum/keysets.size();
            Marker start = new ValueMarker(sum);
            start.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
            start.setLabel("                        Mean: "+util.Truncate_value(sum, 4));
            start.setPaint(Color.red);
            cp.addRangeMarker(start);
                             
        
          return jtable.getModel();
    }
    
    
    
    
    private void jtable_coefficient_values(MultiLabelInstances dataset ,ArrayList<pares_atributos> lista_pares,String tipo_tabla)
    {
         //Object[] fila ;
         double[][] pair_label_values;
         
        //si la tabla es coocurrence values
         if(tipo_tabla.equals("coocurrence"))         
         pair_label_values =util.get_pair_label_values(dataset, lista_pares);
         
         //si la tabla es heatmap values "heatmap"
         else pair_label_values =util.get_heatmap_values(dataset, lista_pares);
      
   //-----------------------------------------------------------------------------------
   //-----------------------------------------------------------------------------------
         data = new Object[pair_label_values.length][pair_label_values.length+1];
         column = new Object[data.length+1];
         
          for(int num_fila=0;  num_fila< pair_label_values.length;num_fila++)
        {            
              data[num_fila]  = util.Get_values_x_fila(num_fila, pair_label_values,dataset.getLabelNames()[num_fila]);
        }
       
           for(int i = 0; i< column.length;i++)
         {
             if(i==0) column[i]="Labels"; //table_model1.addColumn("Labels");
             else column[i]=(dataset.getLabelNames()[i-1]);
         } 
          
        AbstractTableModel1 fixedModel = new AbstractTableModel1(data, column);
        AbstractTableModel2 model = new AbstractTableModel2(data, column);   
        
       
       JTable temp,fixedTable_temp;
       JPanel jpanel_temp;
       
       if(tipo_tabla.equals("coocurrence")){temp=jTable11; jpanel_temp=panelCoOcurrenceValues; fixedTable_temp=fixedTable1;}
       else {temp=jTable12;jpanel_temp=panelHeatmapValues; fixedTable_temp=fixedTable2;}

       fixedTable_temp.setModel(fixedModel);
       temp.setModel(model);
       
       JScrollPane scroll = new JScrollPane(temp);
    JViewport viewport = new JViewport();
    viewport.setView(fixedTable_temp);
    viewport.setPreferredSize(fixedTable_temp.getPreferredSize());
    scroll.setRowHeaderView(viewport);
    
    scroll.setBounds(20, 20, 760, 350);
    
    scroll.setCorner(JScrollPane.UPPER_LEFT_CORNER, fixedTable_temp
        .getTableHeader());
        
    temp.setBorder(BorderFactory.createLineBorder(Color.black));
    
    
    jpanel_temp.remove(0); //una curiosa manera de resolver el problema
    jpanel_temp.add(scroll, BorderLayout.CENTER, 0);
    
    
    
    }
    
  
    
    //JTABLE DE LOS COEFICIENTES DE CHI Y FI.
    private void jtable_chi_fi_coefficient(MultiLabelInstances dataset )
    {

       chi_fi_coefficient = util.get_chi_fi_coefficient(dataset);
      
       data = new Object[chi_fi_coefficient.length][chi_fi_coefficient.length+1];        
       
       column = new Object[data.length+1];
                          
        for(int num_fila=0;  num_fila< chi_fi_coefficient.length;num_fila++)
        {            
              data[num_fila]  = util.Get_values_x_fila(num_fila, chi_fi_coefficient,dataset.getLabelNames()[num_fila]);
        }
      
      for(int i = 0; i< column.length;i++)//se le agrega 1 pq realmente se emieza en 1 y no en 0.
         {
             if(i==0) column[i]="Labels"; //table_model1.addColumn("Labels");
             else column[i]=(dataset.getLabelNames()[i-1]);
         }    
     
       AbstractTableModel1 fixedModel = new AbstractTableModel1(data, column);
       AbstractTableModel2 model = new AbstractTableModel2(data, column);
       
       fixedTable.setModel(fixedModel);
       jTable10.setModel(model);

      
    JScrollPane scroll = new JScrollPane(jTable10);
    JViewport viewport = new JViewport();
    viewport.setView(fixedTable);
    viewport.setPreferredSize(fixedTable.getPreferredSize());
    scroll.setRowHeaderView(viewport);
    
    scroll.setBounds(20, 15, 825, 390); //Chi
    
    scroll.setCorner(JScrollPane.UPPER_LEFT_CORNER, fixedTable.getTableHeader());
    
    jTable10.setBorder(BorderFactory.createLineBorder(Color.black));
    
    if(first_time_chi){panelChiFi.add(scroll, BorderLayout.CENTER, 0); first_time_chi=false; return; }
        
        
    panelChiFi.remove(0); //una curiosa manera de resolver el problema
    panelChiFi.add(scroll, BorderLayout.CENTER, 0);
       
    }
  

    private TableModel jtable_label_graph(JTable jtable, MultiLabelInstances dataset )
    {
         DefaultTableModel table_model1= new DefaultTableModel()
                           {
            public boolean isCellEditable(int row, int column)
            {
                return false;//This causes all cells to be not editable
            }
          };

               
        table_model1.addColumn("Label");
        table_model1.addColumn("# Examples");
        table_model1.addColumn("Frequency");
        
       //devuelve una lista de atributos que contiene el nombre y su frecuencia

        
        //util.Recorre_Arreglo(label_frenquency);
        
        Object[] fila = new Object[3];
        
        atributo current;
        double freq ;
        String truncate;
        
       // System.out.println("TEST JTABLE_FREQUENCY");
        //RECORRE LAS ETIQUETAS
                     
        for(int i=0;i<dataset.getNumLabels();i++)
        {
            current = label_frenquency[i];
            
            fila[0]=current.get_name();
            freq =current.get_frequency()*1.0/dataset.getNumInstances();
            
            //System.out.println( "( "+current.get_frequency()+ " / "+dataset.getNumInstances()+")"+" = "+freq );
            
            fila[1]= current.get_frequency();
            
            truncate = Double.toString(freq);
            fila[2]= util.Truncate_values_aprox_zero(truncate, 4);
            
            
          //  System.out.println(fila[1]);
            
            table_model1.addRow(fila);
        }
        
        jtable.setModel(table_model1);
        
        //RESIZE COLUMNS! jtable
            TableColumnModel tcm = jtable.getColumnModel();
            
            tcm.getColumn(0).setPreferredWidth(80);
            tcm.getColumn(1).setPreferredWidth(70);
            tcm.getColumn(2).setPreferredWidth(70);
            
        return jtable.getModel();
    
    
    
    }
    
    
    private TableModel jtable_attributes(JTable jtable, MultiLabelInstances dataset )
    {
        DefaultTableModel table_model1= new DefaultTableModel()
                          {
            public boolean isCellEditable(int row, int column)
            {
                return false;//This causes all cells to be not editable
            }
          };
               
        table_model1.addColumn("Attribute");

        Object[] fila = new Object[1];
        
        Instances instancias = dataset.getDataSet();
        
        int num_atributos= instancias.numAttributes();
        int numero_etiquetas = dataset.getNumLabels();
        
        int cant_attr =num_atributos -numero_etiquetas;
         
         Attribute att;
        for (int i=0;i<cant_attr;i++) 
        {
            att = instancias.attribute(i);
            if(att.isNumeric())
            {
                fila[0]= att.name();
                table_model1.addRow(fila);
            }
            
        }
        
        jtable.setModel(table_model1);
        
            
        return jtable.getModel();
        
    }
    
   
       
    private TableModel jtable_ir_per_label_intra_class(JTable jtable)
    {
        DefaultTableModel table_model1= new DefaultTableModel()
                          {
            public boolean isCellEditable(int row, int column)
            {
                return false;//This causes all cells to be not editable
            }
          };
               
        table_model1.addColumn("Label id");
        table_model1.addColumn("# Labels");
        table_model1.addColumn("IR");
        
        Object[] fila = new Object[3];
        double[] visitados = new  double[label_imbalanced.length];
        int cant_veces,id=1;
        String truncate;
        double current;
        
        id_x_nums_label = new int [label_imbalanced.length];
        id_x_IR = new double [label_imbalanced.length];
        
        
        for(int i=0; i< label_imbalanced.length ; i++)
        {
            current= (label_imbalanced[i].get_ir());
            truncate = Double.toString(current);
            
            cant_veces=  util.Devuelve_cant_labels_x_IR(label_imbalanced, visitados,current );

            if(cant_veces ==-1) continue;
            
            fila[0]=id;          
            fila[1]= cant_veces;
            fila[2]= util.Truncate_values_aprox_zero(truncate, 5);
                        
            table_model1.addRow(fila);

            id_x_nums_label[id-1]=cant_veces;
            id_x_IR[id-1]=current;
            visitados[id-1]=current;

            id++;
        }
        
        
        jtable.setModel(table_model1);
        
        //RESIZE COLUMNS! jtable
            TableColumnModel tcm = jtable.getColumnModel();
            
            tcm.getColumn(0).setPreferredWidth(80);
            tcm.getColumn(1).setPreferredWidth(50);
            tcm.getColumn(2).setPreferredWidth(60);
            
        return jtable.getModel();
        
        
    }
    
    
     private TableModel jtable_ir_per_label_intra_class_only(JTable jtable)
    {
        DefaultTableModel table_model1= new DefaultTableModel()
                          {
            public boolean isCellEditable(int row, int column)
            {
                return false;//This causes all cells to be not editable
            }
          };
               
        table_model1.addColumn("Label");
        table_model1.addColumn("IR");
        
        Object[] fila = new Object[2];

        String truncate;
                
            //la lista que tiene los valores IR intra class es el label_imbalanced
              
       
        //representalos en la tabla
        for(int i=0; i<label_imbalanced.length; i++)
        {
                                    
            truncate = Double.toString(label_imbalanced[i].get_ir());
            
            fila[0]= label_imbalanced[i].get_name();          
            fila[1]=util.Truncate_values_aprox_zero(truncate, 5);
            
                        
            table_model1.addRow(fila);
            
        }

        jtable.setModel(table_model1);
        
        //RESIZE COLUMNS! jtable
            TableColumnModel tcm = jtable.getColumnModel();
            
            tcm.getColumn(0).setPreferredWidth(80);
            tcm.getColumn(1).setPreferredWidth(50);
            
            
        return jtable.getModel();
        
    }
    
     private TableModel jtable_ir_per_label_inter_class_only(JTable jtable)
    {
        DefaultTableModel table_model1= new DefaultTableModel()
                          {
            public boolean isCellEditable(int row, int column)
            {
                return false;//This causes all cells to be not editable
            }
          };
               
        table_model1.addColumn("Label");
        table_model1.addColumn("IR");
        
        Object[] fila = new Object[2];

        String truncate;
                
             
         IR_inter_class= util.get_ir_values_inter_class(label_frenquency); //calcula el ir inter class
        
          int temp = IR_inter_class.length-1;
        //representalos en la tabla
        for(int i=temp; i>=0; i--)//recorrido inverso de la lista para pq en este caso la etiqueta mas frecuente es la de menor IR.
        {
                                    
            truncate = Double.toString(IR_inter_class[i]);
            
            fila[0]= label_frenquency[i].get_name();          
            fila[1]=util.Truncate_values_aprox_zero(truncate, 5);
            
                        
            table_model1.addRow(fila);
            
        }

        jtable.setModel(table_model1);
        
        //RESIZE COLUMNS! jtable
            TableColumnModel tcm = jtable.getColumnModel();
            
            tcm.getColumn(0).setPreferredWidth(80);
            tcm.getColumn(1).setPreferredWidth(50);
            
            
        return jtable.getModel();
        
    }
  
    private TableModel jtable_ir_per_label_inter_class(JTable jtable)
    {
        DefaultTableModel table_model1= new DefaultTableModel()
                          {
            public boolean isCellEditable(int row, int column)
            {
                return false;//This causes all cells to be not editable
            }
          };
               
        table_model1.addColumn("Label id");
        table_model1.addColumn("# Labels");
        table_model1.addColumn("IR");
        
        Object[] fila = new Object[3];
        double[] visitados = new  double[label_frenquency.length];
        int cant_veces,id=1;
        String truncate;
        double current;
        
        id_x_nums_label_inter_class = new int [label_frenquency.length];
        id_x_IR_inter_class = new double [label_frenquency.length];
        
        double[] IR_inter_class= util.get_ir_values_inter_class(label_frenquency); //calcula el ir inter class
        
        
        for(int i=0; i< label_frenquency.length ; i++)
        {
            current= IR_inter_class[i];
            
            cant_veces=  util.Devuelve_cant_labels_x_IR(IR_inter_class, visitados,current ); //calcula la cantidad de veces que aparece un mismo ir inter class

            if(cant_veces ==-1) continue;
            
            id_x_nums_label_inter_class[id-1]=cant_veces;
            id_x_IR_inter_class[id-1]=current;
            visitados[id-1]=current;

            id++;
        }
        
        ir_veces = new container_id_ir_inter_class(id_x_nums_label_inter_class, id_x_IR_inter_class);
        ir_veces.ordena_IR_Mayor_a_Menor();
        
        id_x_nums_label_inter_class = ir_veces.Get_Id_x_Cant_veces();
        id_x_IR_inter_class = ir_veces.Get_Id_x_IR();
        
        System.out.println("Recorre tabla inter class");
        util.Recorre_Arreglo(id_x_nums_label_inter_class);
        util.Recorre_Arreglo( id_x_IR_inter_class);
        
        //representalos en la tabla
        for(int i=0; i<id_x_IR_inter_class.length; i++)
        {
            if(id_x_nums_label_inter_class[i] ==0) continue;
                        
            truncate = Double.toString(id_x_IR_inter_class[i]);
            
            fila[0]= i+1;          
            fila[1]= id_x_nums_label_inter_class[i];
            fila[2]= util.Truncate_values_aprox_zero(truncate, 5);
                        
            table_model1.addRow(fila);
            
        }

        jtable.setModel(table_model1);
        
        //RESIZE COLUMNS! jtable
            TableColumnModel tcm = jtable.getColumnModel();
            
            tcm.getColumn(0).setPreferredWidth(80);
            tcm.getColumn(1).setPreferredWidth(50);
            tcm.getColumn(2).setPreferredWidth(60);
            
        return jtable.getModel();
        
    }
    
    //CREA LA TABLA DE datos imbalanceados
    private TableModel jtable_imbalanced(JTable jtable, MultiLabelInstances dataset )
    {
        DefaultTableModel table_model1= new DefaultTableModel()
          {
            public boolean isCellEditable(int row, int column)
            {
                return false;//This causes all cells to be not editable
            }
          };
               
        table_model1.addColumn("Label");
        table_model1.addColumn("IR intra class");
        table_model1.addColumn("IR inter class");
        table_model1.addColumn("STDEV intra class");
       //devuelve una lista de atributos que contiene el nombre y su frecuencia
       // label_imbalanced = util.Get_data_imbalanced_x_label_inter_class(dataset,label_frenquency);
        label_imbalanced = util.Sort_data_imbalance_Mayor_IR_intra_class(label_imbalanced);
        
               
        Object[] fila = new Object[4];
        double std;
        String truncate;
        
        atributo current;
     
                     
        for(int i=0;i<dataset.getNumLabels();i++)
        {
            current = label_imbalanced[i];
            
            fila[0]=current.get_name();          
            
            truncate = Double.toString(current.get_ir());            
            fila[1]= util.Truncate_values_aprox_zero(truncate, 5);
            
            truncate = Double.toString(current.get_ir_inter_class());            
            fila[2]= util.Truncate_values_aprox_zero(truncate, 5);
            
            std = Math.sqrt(current.get_variance());
            fila[3]= util.Truncate_value(std,4);
            
            table_model1.addRow(fila);
        }
        
        jtable.setModel(table_model1);
        
        //RESIZE COLUMNS! jtable
            TableColumnModel tcm = jtable.getColumnModel();
            
            tcm.getColumn(0).setPreferredWidth(100);
            tcm.getColumn(1).setPreferredWidth(20);
            tcm.getColumn(2).setPreferredWidth(80);
            tcm.getColumn(3).setPreferredWidth(40);
            
        return jtable.getModel();
    }
    
    
    private TableModel jtable_lablelsxExamples(JTable jtable , HashMap<Integer,Integer> labels_x_example)
    {
        DefaultTableModel table_model1= new DefaultTableModel()
                          {
            public boolean isCellEditable(int row, int column)
            {
                return false;//This causes all cells to be not editable
            }
          };
               
        table_model1.addColumn("# Labels");
        table_model1.addColumn("# Examples");
        table_model1.addColumn("Frequency");

        Object[] fila = new Object[3];
       
        double freq ;
        int freq_current;
        String truncate;
       // System.out.println("TEST JTABLE_FREQUENCY");
        //RECORRE LAS ETIQUETAS
        
        int max = util.Maxim_key(labels_x_example);
				 
				            
	for(int i=1; i<=max ; i++)
            {
		freq_current=0;
		if(labels_x_example.get(i)!=null) freq_current=labels_x_example.get(i);
               
                freq= freq_current*1.0/dataset.getNumInstances();
                
                
                fila[0]= i;
                fila[1]=freq_current;
                truncate = Double.toString(freq);
                fila[2]=util.Truncate_values_aprox_zero(truncate, 5);
                
                table_model1.addRow(fila);
            }
        
        jtable.setModel(table_model1);
        
        //RESIZE COLUMNS! jtable
            TableColumnModel tcm = jtable.getColumnModel();
            
            tcm.getColumn(0).setPreferredWidth(80);
            tcm.getColumn(1).setPreferredWidth(50);
            tcm.getColumn(2).setPreferredWidth(70);
        
            
        return jtable.getModel();
    
    }
  
    //CREA LA TABLA DE ETIQUETAS X FRECUENCIA
    private TableModel jtable_frequency(JTable jtable, MultiLabelInstances dataset )
    {
        DefaultTableModel table_model1= new DefaultTableModel()
          {
            public boolean isCellEditable(int row, int column)
            {
                return false;//This causes all cells to be not editable
            }
          };

               
        table_model1.addColumn("Label");
        table_model1.addColumn("# Examples");
        table_model1.addColumn("Frequency");
        
        
        Object[] fila = new Object[3];
        
        atributo current;
        double freq ;
        String truncate;
       // System.out.println("TEST JTABLE_FREQUENCY");
        //RECORRE LAS ETIQUETAS
                     
        for(int i=0;i<dataset.getNumLabels();i++)
        {
            current = label_frenquency[i];
            
            fila[0]=current.get_name();
            freq =current.get_frequency()*1.0/dataset.getNumInstances();
            
            //System.out.println( "( "+current.get_frequency()+ " / "+dataset.getNumInstances()+")"+" = "+freq );
            fila[1]= current.get_frequency(); //numero de ejemplos
            
            truncate = Double.toString(freq);
            fila[2]= util.Truncate_values_aprox_zero(truncate, 5);
            
            
            
          //  System.out.println(fila[1]);
            
            table_model1.addRow(fila);
        }
        
        jtable.setModel(table_model1);
        
        //RESIZE COLUMNS! jtable
            TableColumnModel tcm = jtable.getColumnModel();
            
            tcm.getColumn(0).setPreferredWidth(80);
            tcm.getColumn(1).setPreferredWidth(70);
            tcm.getColumn(2).setPreferredWidth(50);
            
        return jtable.getModel();
    }
    
    
     public void create_jtable_metric(JTable table,JPanel jpanel , Object rowData[][], int posx, int posy, int width,int height)
     {
            
        TableModel model = new Table_model_metrics(rowData);
        
        table.setModel(model);
       
        //table.setPreferredScrollableViewportSize(table.getPreferredSize());
        //JScrollPane scrollPane = new JScrollPane(table);
        //jPanel13.add(scrollPane);
        
       TableColumnModel tcm = table.getColumnModel();
            
       tcm.getColumn(0).setPreferredWidth(440);
       tcm.getColumn(1).setPreferredWidth(30);
        
       JScrollPane scrollPane = new JScrollPane(table);
        
        //table.setBounds(20, 100, 200, 100);
       
        scrollPane.setBounds(posx, posy, width, height);
        
        table.setBorder(BorderFactory.createLineBorder(Color.black));
        
        jpanel.add(scrollPane, BorderLayout.CENTER);
        jpanel.repaint();
        jpanel.validate();

  
    }
    
    
     
     
    /**
     * @param args the command line arguments
     */
    
      
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RunApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RunApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RunApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RunApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RunApp().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane TabPrincipal;
    private javax.swing.JButton buttonAddMultipleDatasets;
    public javax.swing.JButton buttonChooseFile;
    private javax.swing.JButton buttonChooseSuppliedTest;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.JButton buttonRemoveMultipleDatasets;
    private javax.swing.JButton buttonShowCoOcurrence;
    private javax.swing.JButton buttonShowHeatmapLeft;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel labelAttributes;
    private javax.swing.JLabel labelAttributesValue;
    private javax.swing.JLabel labelBound;
    private javax.swing.JLabel labelBoundValue;
    private javax.swing.JLabel labelCV;
    private javax.swing.JLabel labelCardinality;
    private javax.swing.JLabel labelCardinalityValue;
    private javax.swing.JLabel labelChiCoefficients;
    private javax.swing.JLabel labelChiCoefficients1;
    private javax.swing.JLabel labelChiThreshold;
    private javax.swing.JLabel labelDensity;
    private javax.swing.JLabel labelDensityValue;
    private javax.swing.JLabel labelDistinct;
    private javax.swing.JLabel labelDistinctValue;
    private javax.swing.JLabel labelDiversity;
    private javax.swing.JLabel labelDiversityValue;
    private javax.swing.JLabel labelFoldsRandom;
    private javax.swing.JLabel labelFoldsStratified;
    private javax.swing.JLabel labelHeatmapGraph;
    private javax.swing.JLabel labelHoldout;
    private javax.swing.JLabel labelIR1;
    private javax.swing.JLabel labelIR2;
    private javax.swing.JLabel labelInstances;
    private javax.swing.JLabel labelInstancesValue;
    private javax.swing.JLabel labelLabels;
    private javax.swing.JLabel labelLabelsValue;
    private javax.swing.JLabel labelLxIxF;
    private javax.swing.JLabel labelLxIxFValue;
    private javax.swing.JLabel labelPercRandom;
    private javax.swing.JLabel labelPercStratified;
    private javax.swing.JLabel labelRelation;
    private javax.swing.JLabel labelRelationValue;
    private javax.swing.JList listMultipleDatasetsLeft;
    private java.awt.Panel panel1;
    private javax.swing.JPanel panelBoxDiagram;
    private javax.swing.JPanel panelChiFi;
    private javax.swing.JPanel panelCoOcurrence;
    private javax.swing.JPanel panelCoOcurrenceLeft;
    private javax.swing.JPanel panelCoOcurrenceRight;
    private javax.swing.JPanel panelCoOcurrenceValues;
    private javax.swing.JPanel panelCurrentDataset;
    private javax.swing.JPanel panelDataset;
    private javax.swing.JPanel panelExamplesPerLabel;
    private javax.swing.JPanel panelExamplesPerLabelset;
    private javax.swing.JPanel panelHeatmapGraph;
    private javax.swing.JPanel panelHeatmapLeft;
    private javax.swing.JPanel panelHeatmapValues;
    private javax.swing.JPanel panelIRperLabelInterClass;
    private javax.swing.JPanel panelIRperLabelIntraClass;
    private javax.swing.JPanel panelIRperLabelset;
    private javax.swing.JPanel panelImbalance;
    private javax.swing.JPanel panelImbalanceDataMetrics;
    private javax.swing.JPanel panelImbalanceLeft;
    private javax.swing.JPanel panelLabelFrequency;
    private javax.swing.JPanel panelLabelsIRperLabelInterClass;
    private javax.swing.JPanel panelLabelsIRperLabelIntraClass;
    private javax.swing.JPanel panelLabelsPerExample;
    private javax.swing.JPanel panelMultipleDatasets;
    private javax.swing.JPanel panelMultipleDatasetsLeft;
    private javax.swing.JPanel panelTestOption;
    private javax.swing.JPanel panelTrainTest;
    private javax.swing.JRadioButton radioExamplesPerLabel;
    private javax.swing.JRadioButton radioExamplesPerLabelset;
    private javax.swing.JRadioButton radioRandomCV;
    private javax.swing.JRadioButton radioRandomHoldout;
    private javax.swing.JRadioButton radioStratifiedCV;
    private javax.swing.JRadioButton radioStratifiedHoldout;
    private javax.swing.JRadioButton radioSuppliedTest;
    private javax.swing.JTable tableCoOcurrenceLeft;
    private javax.swing.JTable tableHeatmapLeft;
    private javax.swing.JTable tableImbalance;
    private javax.swing.JTabbedPane tabsDependences;
    private javax.swing.JTabbedPane tabsImbalance;
    private javax.swing.JTextField textChooseFile;
    private javax.swing.JTextField textRandomCV;
    private javax.swing.JTextField textRandomHoldout;
    private javax.swing.JTextField textStratifiedCV;
    private javax.swing.JTextField textStratifiedHoldout;
    // End of variables declaration//GEN-END:variables
}
