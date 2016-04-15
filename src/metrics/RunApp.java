package metrics;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import convertir.MekaToMulan;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
 

import javax.swing.*;


import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import static metrics.util.Get_labelset_x_values;

import mulan.data.InvalidDataFormatException;
import mulan.data.IterativeStratification;
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
import preprocess.FeatureSelector;
import preprocess.RandomTrainTest;
import preprocess.IterativeTrainTest;
import preprocess.LabelPowersetTrainTest;
import weka.core.Attribute;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.Randomize;
import weka.filters.unsupervised.instance.RemovePercentage;
import weka.filters.unsupervised.instance.RemoveRange;


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
       
      //private Task task;
    
      /*
     class Task extends SwingWorker<Void, Void> {
         
         //SEE THAT TO CREATE A GOOD PROGRESS BAR
         //http://felinfo.blogspot.com.es/2010/06/crear-una-barra-de-progreso-en-java.html
         
         
         progress_bar new_progress;
         int velocidad;

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

        
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            setCursor(null); //turn off the wait cursor
          //  taskOutput.append("Done!\n");
            new_progress.setVisible(false);
        }
    }*/
    
    public static JProgressBar progressBar;
    public static JFrame progressFrame;
    
    
     String dataset_name1="";
    
     static int count_test=1;
     ArrayList<String> test_list = new ArrayList();
     
    //JPanel container;
    MultiLabelInstances dataset,dataset_train, dataset_test;
    MultiLabelInstances preprocessedDataset;
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
    
    JButton button_all_1,button_none_1,button_invert_1, button_calculate_1,button_save, button_clear;
    JButton button_all_3,button_none_3,button_invert_3, button_calculate_3,button_save3;
    JButton export1,export2,export3,export4,export5,export6,export7;
    
    final JTable jTable5,jTable6,jTable7, jTable8,jTable9;
    final JTable jTable1, jTableMulti;
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
    double [][] coocurrence_coefficients;
    double [][] heatmap_coefficients;
     Object[][] data; 
     Object[] column;
     
     //variables TRAIN-TEST
     /*boolean  holdout_random=false, holdout_iterative_stratified=false, 
             cv_ramdon=false, cv_iterative_stratified =false,
             holdout_LP_stratified=false, cv_LP_stratified=false,
             feature_selection_br=false, feature_selection_random=false;*/
    //JButton button_calculate2_train,button_save_train;
    
    //VARIABLES BOX DIAGRAM
    JRadioButton jRadioButton8;
    
    boolean first_time_chi=true; 
    boolean es_de_tipo_meka = false;
    
    
    Hashtable<String, String> tableMetrics = new Hashtable<String, String>();
    Hashtable<String, String> tableMetrics_common = new Hashtable<String, String>();
    Hashtable<String, String> tableMetrics_train = new Hashtable<String, String>();
    Hashtable<String, String> tableMetrics_test = new Hashtable<String, String>();
    
    Hashtable<String, Hashtable<String, String>> tableMetricsMulti = new Hashtable<String, Hashtable<String, String>>();
    
    HeatMap heatMap = null; 
    
    
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
        jTableMulti = new JTable();
        
        this.setTitle("Multi-Label Dataset Analyzer (MLDA)");
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
       //jPanel15.setVisible(false);
       radioExamplesPerLabel.setVisible(false);
       radioExamplesPerLabelset.setVisible(false);
       //labelIR1.setVisible(false); // comentario de IR>1.5
       //labelIR2.setVisible(false); // comentario de IR>1.5
       jLabelChiFi_text.setVisible(false); // comentario de valores dependientes chi- coefficient
       
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
   
      chi = new JLabel("Chi coefficients", SwingConstants.CENTER);
      chi.setBounds(25,420, 120,20);
      chi.setBackground(Color.white);
      chi.setForeground(Color.black);
      chi.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
      chi.setOpaque(true);
      chi.setToolTipText("White cells corresponds to chi coefficients");
           
      panelChiPhi.add(chi);
    
      fi = new JLabel("Phi coefficients", SwingConstants.CENTER);
      fi.setBounds(165,420, 120, 20);
      fi.setBackground(Color.gray);
      fi.setForeground(Color.white);
      fi.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
      fi.setOpaque(true);
      fi.setToolTipText("Gray cells corresponds to phi coefficients");
      
      panelChiPhi.add(fi);
      
      
      progressBar = new JProgressBar(0, 100);
      progressBar.setValue(0);  
      progressFrame = new JFrame();
      progressFrame.setBounds(this.getBounds().x + this.getBounds().width/2 - 100, this.getBounds().y + this.getBounds().height/2 - 15, 200, 30);
      progressFrame.setResizable(false);
      progressFrame.setUndecorated(true);
      progressFrame.add(progressBar);      
      
    
    }
    
    
    
private void Inicializa_config()
{
  
        
     //radiobutton to group
      buttonGroup1.add(radioRandomHoldout);
      radioRandomHoldout.setToolTipText("Split the dataset into random train and test files");
      textRandomHoldout.setToolTipText("Percentage of train instances");
      
      buttonGroup1.add(radioIterativeStratifiedHoldout);
      radioIterativeStratifiedHoldout.setToolTipText("Split the dataset into train and test files by Iterative stratified method");
      textIterativeStratifiedHoldout.setToolTipText("Percentage of train instances");
      
      buttonGroup1.add(radioLPStratifiedHoldout);
      radioLPStratifiedHoldout.setToolTipText("Split the dataset into train and test files by Label Powerset stratified method");
      textLPStratifiedHoldout.setToolTipText("Percentage of train instances");
      
      buttonGroup1.add(radioRandomCV);
      radioRandomCV.setToolTipText("Generates random cross-validation files for selected number of folds");
      textRandomCV.setToolTipText("Number of folds for cross-validation");
      
      buttonGroup1.add(radioIterativeStratifiedCV);
      radioIterativeStratifiedCV.setToolTipText("Generates Iterative stratified cross-validation files for selected number of folds");
      textIterativeStratifiedCV.setToolTipText("Number of folds for cross-validation");
      
      buttonGroup1.add(radioLPStratifiedCV);
      radioLPStratifiedCV.setToolTipText("Generates Label Powerset stratified cross-validation files for selected number of folds");
      textLPStratifiedCV.setToolTipText("Number of folds for cross-validation");
      
      buttonGroup1.add(radioNoSplit);
      radioNoSplit.setToolTipText("Not generate any partition of the dataset");
      
      buttonGroup2.add(radioBRFS);
      radioBRFS.setToolTipText("Feature selection by Binary Relevance Feature Selection method");
      textBRFS.setToolTipText("Number of features to select");
      
      labelBRFS_Comb.setToolTipText("Combiantion approach mode");
      jComboBox_BRFS_Comb.setToolTipText("<html>Combiantion approach mode: <br>"
                                        + "max: maximum <br>"
                                        + "avg: average <br>"
                                        + "min: minumum </html>");
      
      labelBRFS_Norm.setToolTipText("Normalization mode");
      jComboBox_BRFS_Norm.setToolTipText("<html>Normalization mode: <br>"
                                        + "dl: divide by length <br>"
                                        + "dm: divide by maximum <br>"
                                        + "none: no normalization </html>");
      
      labelBRFS_Out.setToolTipText("Scoring mode");
      jComboBox_BRFS_Out.setToolTipText("<html>Scoring mode: <br>"
                                        + "eval: evaluation score <br>"
                                        + "rank: ranking score </html>");
      
      
      buttonGroup2.add(radioRandomFS);
      radioRandomFS.setToolTipText("Random selection of the features");
      textRandomFS.setToolTipText("Number of features to select");
      
      buttonGroup2.add(radioNoFS);
      radioNoFS.setToolTipText("No feature selection is done");
      
      jButtonStartPreprocess.setToolTipText("Start preprocessing");
      jButtonSaveDatasets.setToolTipText("Save dataset files in a folder");
      jComboBox_SaveFormat.setToolTipText("Select Mulan or Meka format to save datasets");

      buttonGroup3.add(radioNoIS);
      radioNoIS.setToolTipText("No instance selection is done");
      buttonGroup3.add(radioRandomIS);
      radioRandomIS.setToolTipText("Random selection of the instances");
      textRandomIS.setToolTipText("Number of instances to select");
      
      
      radioRandomHoldout.setSelected(true);
      radioNoFS.setSelected(true);
      radioNoIS.setSelected(true);
      
      textRandomHoldout.setEnabled(true);
      
      
     // jTable1.setVisible(true);        
      
      
      buttonShowCoOcurrence.setToolTipText("Show graph with labels selected in table");
      buttonShowMostFrequent.setToolTipText("Show graph with most frequent labels");
      textMostFrequent.setToolTipText("Number of most frequent labels to show");
      buttonShowMostRelated.setToolTipText("Show graph with most related labels");
      textMostRelated.setToolTipText("Number of most related labels to show");
      
      buttonShowHeatMap.setToolTipText("Show heatmap with labels selected in table");
      buttonShowMostFrequentHeatMap.setToolTipText("Show heatmap with most frequent labels");
      textMostFrequentHeatMap.setToolTipText("Number of most frequent labels to show");
      buttonShowMostRelatedHeatMap.setToolTipText("Show heatmap with most related labels");
      textMostRelatedHeatMap.setToolTipText("Number of most related labels to show");
      
      
      
       
       cp3 = create_jchart(panelExamplesPerLabel,"bar","Labels", "Relative frequency",false);
       cp11 =  create_jchart(panelLabelsPerExample,"bar", "# Labels/example","Relative frequency",false);
       //crea el grafico box diagram
       cp_box =generaGrafico(panelBoxDiagram);
       
       cp22 = create_jchart(panelExamplesPerLabelset, "bar","Labelsets","Relative frequency",false);
       
       cp_ir_x_label_intra_class = create_jchart(panelLabelsIRperLabelIntraClass, "line_2_axis", "Label id","IR values",true);
       cp_ir_x_label_inter_class = create_jchart(panelLabelsIRperLabelInterClass, "line_2_axis", "Label id","IR values",true);
       cp_ir_x_label_inter_class_only = create_jchart(panelIRperLabelInterClass, "line", "Label id","IR values",true);
       cp_ir_x_label_intra_class_only = create_jchart(panelIRperLabelIntraClass, "line", "Label id","IR values",true);
               
       cp_per_labelset = create_jchart(panelIRperLabelset, "line", "Labelset id","IR per labelset",false);
      
     create_jtable_metrics_jpanel1(jTable9,panelImbalanceDataMetrics,button_all_3,button_none_3,button_invert_3,button_calculate_3,button_save3,30,50,500,200,"imbalanced"); //imbalanced class
      
      create_jtable_metrics_jpanel1_principal(jTable1,panelDataset,button_all_1,button_none_1,button_invert_1,button_calculate_1,button_save, button_clear, 30,190,780,280,"database"); //tab Database //35,155,500,355
      create_jtable_metrics_jpanel14(jTable8,panelMultipleDatasets,button_all_2,button_none_2,button_invert_2,button_calculate_2,button_save2,290,35,565,400); //
      
      create_jtable_metrics_multi(jTableMulti,jPanelMulti,button_all_1,button_none_1,button_invert_1,button_calculate_1,button_save, 25,15,510,420); //tab Multi
      
      //create_jtable_metrics_jpanel2();
      
      jButtonSaveDatasets.setEnabled(false);
      jComboBox_SaveFormat.setEnabled(false);
      //button_calculate2_train.setEnabled(false);
      //button_save_train.setEnabled(false);
            
      //CONFIG JTABLE FI AND CHI CO-OCURRENCES VALUES
      Inicializa_jtable_fi_chi();
      
      
      
      
      //CONFIG JTABLE CO-OCURRENCES VALUES
      
      jTable11.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      JScrollPane scrollPane = new JScrollPane(jTable11, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
         
      
      scrollPane.setBounds(20, 20, 780, 390);
      jTable11.setBorder(BorderFactory.createLineBorder(Color.black));
      panelCoOcurrenceValues.add(scrollPane, BorderLayout.CENTER);
      
      //CONFIG JTABLE heapmap VALUES
      
      jTable12.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      scrollPane = new JScrollPane(jTable12, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            
      scrollPane.setBounds(20, 20, 780, 390);
      jTable12.setBorder(BorderFactory.createLineBorder(Color.black));
      panelHeatmapValues.add(scrollPane, BorderLayout.CENTER);
      
      
      //INICIALIZAR ELEMENTOS DE LA PESTAÑA TRAIN/TEST
      list_dataset_train = new ArrayList();
      list_dataset_test = new ArrayList();
      
      //BOTON EXPORTAR PESTAÑA DATASET
      
        
      //create_button_export(jTable2,panelDataset,export1,20,285); //375
      export2 = create_button_export_jtable4(tableImbalance,panelImbalanceLeft,export2, 30,405);
     
      
      
      create_button_export(jTable10,fixedTable ,panelChiPhi ,export3,710,415, "ChiPhi"); // chi and fi values
      create_button_export(jTable11,fixedTable1,panelCoOcurrenceValues ,export4,710,415, "Coocurrence");//graph values
      create_button_export(jTable12,fixedTable2,panelHeatmapValues ,export5,710,415, "Heatmap");//heatmap values
      
      create_button_export(panelCoOcurrence ,export6,720,440); //graph- dependences
      //export6.setToolTipText("Save co-ocurrence graph as image");
      create_button_export(panelHeatmapGraph,export7,720,440);
      //export7.setToolTipText("Save heatmap as image");
      
      //create_button_export(jPanel21 ,export6,5,50);
      Border border = BorderFactory.createLineBorder(Color.gray, 1);
   
      
      
      panelHeatmap.setBorder(border); 
      jTable10.setBorder(border);
      jTable11.setBorder(border);
      jTable12.setBorder(border);
      
      //jradio por defecto "PESTAÑA TRAIN TEST"
      textRandomHoldout.setEnabled(true);
        textIterativeStratifiedHoldout.setEnabled(false);
        //buttonChooseSuppliedTest.setEnabled(false);
        textRandomCV.setEnabled(false);
        textIterativeStratifiedCV.setEnabled(false);
        

        
}
    //"TEST" TAB CONFIG
    private void create_jtable_metrics_jpanel2()
    {
     
     //COMMUN METRICS TRAIN/TEST
     JLabel label1 = new JLabel("Common metrics for train/test");
     panelPreprocess.add(label1);
     label1.setBounds(300, 20, 250, 20);
     label1.setFont(new Font("Arial", Font.BOLD, 12));
         
     
     //create_jtable_metric_principal(jTable5, panelTrainTest, util.Get_row_data_commun_data(), 295, 40, 610, 175);
     
     
   
          
     // METRICS TEST
     JLabel label3 = new JLabel("NOT common metrics for train/test set");
     panelPreprocess.add(label3);
     label3.setBounds(300, 220, 250, 20);
     label3.setFont(new Font("Arial", Font.BOLD, 12));
     
     
     //Not Common train_test
     //create_jtable_metric_train_test(jTable7, panelTrainTest, util.Get_row_data_test_data(), 295, 240, 625, 270);
     
     
     //button ALL
      JButton button_all2 = new JButton("All");
      button_all2.setBounds(295, 515, 80, 20);
            
      button_all2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_allActionPerformed2(evt,jTable5,jTable6,jTable7 );
                            }
        });
      panelPreprocess.add(button_all2);
      
      
     //button NONE
      JButton button_none2 = new JButton("None");
      button_none2.setBounds(385, 515, 80, 20);
            
      button_none2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
                button_noneActionPerformed2(evt,jTable5,jTable6,jTable7);
                            }
        });
      panelPreprocess.add(button_none2);
      
      //button INVERT
     JButton button_invert2 = new JButton("Invert");
      button_invert2.setBounds(475, 515, 80, 20);
            
      button_invert2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_invertActionPerformed2(evt,jTable5,jTable6,jTable7);
                            }
        });
      panelPreprocess.add(button_invert2);
      
      
         //button CALCULATE
      //button_calculate2_train = new JButton("Calculate");
      //button_calculate2_train.setBounds(720, 515, 90, 20);
            
      /*button_calculate2_train.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_calculateActionPerformed1_general(evt,jTable5,jTable6,jTable7);
                            }
        });*/
      //panelTrainTest.add(button_calculate2_train);
     
               //button SAVE
    // button_save_train = new JButton("Save");
     // button_save_train.setBounds(820, 515, 80, 20);
            
      /*button_save_train.addActionListener(new java.awt.event.ActionListener() {
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
     */
        
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
    
    if(cant_labels<30)chart.setCellSize(new Dimension(10,10));
    if(cant_labels>30 && cant_labels <60 ) chart.setCellSize(new Dimension(9,6));
    if(cant_labels >60 && cant_labels < 102)chart.setCellSize(new Dimension(4,3));
    if(cant_labels >102 && cant_labels < 250)chart.setCellSize(new Dimension(3,2));
    if(cant_labels >250 && cant_labels < 500)chart.setCellSize(new Dimension(2,1));
    if(cant_labels >500)
    {
        JOptionPane.showMessageDialog(null, "The heatmap can not be represented.", "Warning", JOptionPane.ERROR_MESSAGE);
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
        JOptionPane.showMessageDialog(null, "The heatmap can not be represented.", "Warning", JOptionPane.ERROR_MESSAGE);
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
        
    private void create_button_export(final JTable jtable,final JTable columns, JPanel jpanel, JButton jbutton_export, int posx,int posy, final String table)
    {
      //button export table
      jbutton_export = new JButton("Save");
      jbutton_export.setBounds(posx, posy, 80, 25);
      
      if(table.equals("ChiPhi")){
          jbutton_export.setToolTipText("Save table with Chi and Phi coefficients");
      }
      else if(table.equals("Coocurrence")){
          jbutton_export.setToolTipText("Save table with co-ocurrence values");
      }
      else if(table.equals("Heatmap")){
          jbutton_export.setToolTipText("Save table with heatmap values");
      }
            
      jbutton_export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_export_ActionPerformed(evt,jtable,columns, table);
                            }
        });
      jpanel.add(jbutton_export);
    }    
        
  private void create_button_export(final JPanel jpanel, JButton jbutton_export, int posx,int posy)
    {
      //button export table
      jbutton_export = new JButton("Save");
      jbutton_export.setBounds(posx, posy, 80, 25);
      jbutton_export.setToolTipText("Save graph as image");
            
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
    private void create_jtable_metrics_jpanel1(final JTable jtable ,JPanel jpanel , JButton button_all, JButton button_none, JButton button_invert, JButton button_calculate,JButton button_save, int posx,int posy, int width, int heigh,String info)
    {
        if(info.equals("imbalanced"))
        {
            create_jtable_metric(jtable,jpanel, util.Get_row_data_imbalanced(),posx,posy,width,heigh);
        }  
        
        if(info.equals("database"))
        {
            create_jtable_metric(jtable,jpanel, util.Get_row_data(),posx,posy,width,heigh);
        }  
        
        
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
      button_calculate = new JButton("Show");
      button_calculate.setBounds(posx+420, posy+heigh+5, 80, 20);
            
      button_calculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_calculateActionPerformed(evt,jtable);
                            }
        });
      jpanel.add(button_calculate);
      
      
       //button SAVE
      button_save = new JButton("Save");
      button_save.setBounds(posx+420, posy+heigh+25, 80, 20);
            
      button_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    button_saveActionPerformed_principal(evt,jtable);
                } catch (IOException ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                            }
        });
      jpanel.add(button_save);
      
    }
    
    
    private void create_jtable_metrics_jpanel1_principal(final JTable jtable ,JPanel jpanel , JButton button_all, JButton button_none, JButton button_invert, JButton button_calculate,JButton button_save, JButton button_clear, int posx,int posy, int width, int heigh,String info)
    {
        if(info.equals("imbalanced"))
        create_jtable_metric(jtable,jpanel, util.Get_row_data_imbalanced(),posx,posy,width,heigh);  
        
        if(info.equals("database"))
        create_jtable_metric_principal(jtable,jpanel, util.Get_row_data_principal(),posx,posy,width,heigh);  
        
        
       //button ALL
      button_all = new JButton("All");
      button_all.setBounds(posx, posy+heigh+5, 80, 20);
      button_all.setToolTipText("Select all metrics");
            //PRINCIPAL_ALL
      button_all.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_allActionPerformed_principal(evt,jtable );
                            }
        });
      jpanel.add(button_all);
      
      
     //button NONE
      button_none = new JButton("None");
      button_none.setToolTipText("Deselect all metrics");
      button_none.setBounds(posx+90, posy+heigh+5, 80, 20);
            
      button_none.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_noneActionPerformed_principal(evt,jtable);
                            }
        });
      jpanel.add(button_none);
      
      //button INVERT
      button_invert = new JButton("Invert");
      button_invert.setToolTipText("Invert selection");
      button_invert.setBounds(posx+180, posy+heigh+5, 80, 20);
            
      button_invert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_invertActionPerformed_principal(evt,jtable);
                            }
        });
      jpanel.add(button_invert);
      
      //button CLEAR
      button_clear = new JButton("Clear");
      button_clear.setToolTipText("Clear selection an metric values");
      button_clear.setBounds(posx+270, posy+heigh+5, 80, 20);
            
      button_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_clearActionPerformed_principal(evt,jtable);
                            }
        });
      jpanel.add(button_clear);
      
         //button CALCULATE
      button_calculate = new JButton("Calculate");
      button_calculate.setBounds(posx+590, posy+heigh+5, 95, 25);
      button_calculate.setToolTipText("Calculate selected metrics");
            
      button_calculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(final java.awt.event.ActionEvent evt) {
                
                progressFrame.setVisible(true);
                progressFrame.repaint();
                progressBar.setIndeterminate(false);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // do the long-running work here
                        button_calculateActionPerformed_principal(evt,jtable);
                        // at the end:
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setIndeterminate(false);
                                progressFrame.setVisible(false);
                                progressFrame.repaint();
                            }//run
                        }); //invokeLater
                    }}
                ).start(); //Thread
                
                //button_calculateActionPerformed_principal(evt,jtable);
            } //actionPerformed
        });//ActionListener
      jpanel.add(button_calculate);
      
      
       //button SAVE
      button_save = new JButton("Save");
      button_save.setBounds(posx+695, posy+heigh+5, 80, 25);
      button_save.setToolTipText("Save selected metrics in a file");
            
      button_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    button_saveActionPerformed_principal(evt,jtable);
                } catch (IOException ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                            }
        });
      jpanel.add(button_save);
      
    }
    
    
    
    private void create_jtable_metrics_multi(final JTable jtable ,JPanel jpanel , JButton button_all, JButton button_none, JButton button_invert, JButton button_calculate,JButton button_save, int posx,int posy, int width, int heigh)
    {

      create_jtable_metric_multi(jtable,jpanel, util.Get_row_data_multi(),posx,posy,width,heigh);  
        
       //button ALL
      button_all = new JButton("All");
      button_all.setBounds(posx, posy+heigh+5, 80, 20);
      button_all.setToolTipText("Select all metrics");
            //PRINCIPAL_ALL
      button_all.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_allActionPerformed_multi(evt,jtable );
                            }
        });
      jpanel.add(button_all);
      
      
     //button NONE
      button_none = new JButton("None");
      button_none.setToolTipText("Deselect all metrics");
      button_none.setBounds(posx+90, posy+heigh+5, 80, 20);
            
      button_none.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_noneActionPerformed_multi(evt,jtable);
                            }
        });
      jpanel.add(button_none);
      
      //button INVERT
      button_invert = new JButton("Invert");
      button_invert.setToolTipText("Invert selection");
      button_invert.setBounds(posx+180, posy+heigh+5, 80, 20);
            
      button_invert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_invertActionPerformed_multi(evt,jtable);
                            }
        });
      jpanel.add(button_invert);
      
      
         //button CALCULATE
      button_calculate = new JButton("Calculate");
      button_calculate.setBounds(posx+320, posy+heigh+5, 95, 20);
      button_calculate.setToolTipText("Calculate selected metrics");
            
      button_calculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(final java.awt.event.ActionEvent evt) {
                progressFrame.setVisible(true);
                progressFrame.repaint();
                progressBar.setIndeterminate(false);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // do the long-running work here
                        button_calculateActionPerformed_multi(evt,jtable);
                        // at the end:
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setIndeterminate(false);
                                progressFrame.setVisible(false);
                                progressFrame.repaint();
                                JOptionPane.showMessageDialog(null, "Metrics have been calculated succesfully.", "Successful", JOptionPane.INFORMATION_MESSAGE);
                                
                            }//run
                        }); //invokeLater
                    }}
                ).start(); //Thread
            }
        });
      jpanel.add(button_calculate);
      
      
       //button SAVE
      button_save = new JButton("Save");
      button_save.setBounds(posx+425, posy+heigh+5, 80, 20);
      button_save.setToolTipText("Save selected metrics in a file");
            
      button_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    button_saveActionPerformed_multi(evt,jtable);
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
     /* button_all = new JButton("All");
      button_all.setBounds(posx, posy+405, 80, 20);
            
      button_all.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_allActionPerformed(evt,jtable );
                            }
        });
      jpanel.add(button_all);*/
      
      /*
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
      jpanel.add(button_save);*/
      
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
        panelPreprocess = new javax.swing.JPanel();
        panelSplitting = new javax.swing.JPanel();
        radioRandomHoldout = new javax.swing.JRadioButton();
        labelPercIterativeStratified = new javax.swing.JLabel();
        radioIterativeStratifiedHoldout = new javax.swing.JRadioButton();
        textRandomHoldout = new javax.swing.JTextField();
        radioRandomCV = new javax.swing.JRadioButton();
        radioIterativeStratifiedCV = new javax.swing.JRadioButton();
        textIterativeStratifiedCV = new javax.swing.JTextField();
        labelFoldsRandom = new javax.swing.JLabel();
        textRandomCV = new javax.swing.JTextField();
        labelFoldsIterativeStratified = new javax.swing.JLabel();
        labelPercRandom = new javax.swing.JLabel();
        textIterativeStratifiedHoldout = new javax.swing.JTextField();
        radioLPStratifiedHoldout = new javax.swing.JRadioButton();
        textLPStratifiedHoldout = new javax.swing.JTextField();
        labelPercLPStratified = new javax.swing.JLabel();
        radioLPStratifiedCV = new javax.swing.JRadioButton();
        textLPStratifiedCV = new javax.swing.JTextField();
        labelFoldsLPStratified = new javax.swing.JLabel();
        radioNoSplit = new javax.swing.JRadioButton();
        jButtonStartPreprocess = new javax.swing.JButton();
        jButtonSaveDatasets = new javax.swing.JButton();
        panelFS = new javax.swing.JPanel();
        radioBRFS = new javax.swing.JRadioButton();
        textBRFS = new javax.swing.JTextField();
        labelBRFS = new javax.swing.JLabel();
        labelBRFS_Comb = new javax.swing.JLabel();
        jComboBox_BRFS_Comb = new javax.swing.JComboBox();
        labelBRFS_Norm = new javax.swing.JLabel();
        jComboBox_BRFS_Norm = new javax.swing.JComboBox();
        labelBRFS_Out = new javax.swing.JLabel();
        jComboBox_BRFS_Out = new javax.swing.JComboBox();
        radioRandomFS = new javax.swing.JRadioButton();
        textRandomFS = new javax.swing.JTextField();
        labelRandomFS = new javax.swing.JLabel();
        radioNoFS = new javax.swing.JRadioButton();
        labelBRFS1 = new javax.swing.JLabel();
        jComboBox_SaveFormat = new javax.swing.JComboBox();
        panelIS = new javax.swing.JPanel();
        radioRandomIS = new javax.swing.JRadioButton();
        textRandomIS = new javax.swing.JTextField();
        labelRandomIS = new javax.swing.JLabel();
        radioNoIS = new javax.swing.JRadioButton();
        panelImbalance = new javax.swing.JPanel();
        comboBoxLabelsInformation = new javax.swing.JComboBox();
        panelImbalanceLeft = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableImbalance = new javax.swing.JTable();
        tabsImbalance = new javax.swing.JTabbedPane();
        panelExamplesPerLabel = new javax.swing.JPanel();
        panelExamplesPerLabelset = new javax.swing.JPanel();
        panelLabelsPerExample = new javax.swing.JPanel();
        panelIRperLabelIntraClass = new javax.swing.JPanel();
        panelLabelsIRperLabelIntraClass = new javax.swing.JPanel();
        panelIRperLabelset = new javax.swing.JPanel();
        panelImbalanceDataMetrics = new javax.swing.JPanel();
        panelBoxDiagram = new javax.swing.JPanel();
        panelIRperLabelInterClass = new javax.swing.JPanel();
        panel1 = new java.awt.Panel();
        panelLabelsIRperLabelInterClass = new javax.swing.JPanel();
        radioExamplesPerLabel = new javax.swing.JRadioButton();
        radioExamplesPerLabelset = new javax.swing.JRadioButton();
        jPanel21 = new javax.swing.JPanel();
        tabsDependences = new javax.swing.JTabbedPane();
        panelChiPhi = new javax.swing.JPanel();
        jLabelChiFi_text = new javax.swing.JLabel();
        panelCoOcurrence = new javax.swing.JPanel();
        panelCoOcurrenceRight = new javax.swing.JPanel();
        buttonShowCoOcurrence = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        tableCoOcurrenceLeft = new javax.swing.JTable();
        buttonShowMostFrequent = new javax.swing.JButton();
        textMostFrequent = new javax.swing.JTextField();
        buttonShowMostRelated = new javax.swing.JButton();
        textMostRelated = new javax.swing.JTextField();
        panelCoOcurrenceValues = new javax.swing.JPanel();
        panelHeatmapGraph = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tableHeatmapLeft = new javax.swing.JTable();
        panelHeatmap = new javax.swing.JPanel();
        buttonShowHeatMap = new javax.swing.JButton();
        buttonShowMostFrequentHeatMap = new javax.swing.JButton();
        buttonShowMostRelatedHeatMap = new javax.swing.JButton();
        textMostRelatedHeatMap = new javax.swing.JTextField();
        textMostFrequentHeatMap = new javax.swing.JTextField();
        panelHeatmapValues = new javax.swing.JPanel();
        panelMultipleDatasets = new javax.swing.JPanel();
        panelMultipleDatasetsLeft = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listMultipleDatasetsLeft = new javax.swing.JList();
        buttonAddMultipleDatasets = new javax.swing.JButton();
        buttonRemoveMultipleDatasets = new javax.swing.JButton();
        jPanelMulti = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        TabPrincipal.setDoubleBuffered(true);

        buttonChooseFile.setText("Choose file");
        buttonChooseFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonChooseFileActionPerformed(evt);
            }
        });

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

        panelCurrentDataset.setBorder(javax.swing.BorderFactory.createTitledBorder("Summary"));
        panelCurrentDataset.setPreferredSize(new java.awt.Dimension(845, 134));

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
                        .addComponent(labelLxIxF)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelLxIxFValue))
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
                                .addComponent(labelDensityValue, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(68, Short.MAX_VALUE))
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
                .addGap(24, 24, 24)
                .addGroup(panelDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelCurrentDataset, javax.swing.GroupLayout.PREFERRED_SIZE, 795, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelDatasetLayout.createSequentialGroup()
                        .addComponent(textChooseFile, javax.swing.GroupLayout.PREFERRED_SIZE, 665, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(buttonChooseFile, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        panelDatasetLayout.setVerticalGroup(
            panelDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatasetLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textChooseFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonChooseFile))
                .addGap(7, 7, 7)
                .addComponent(panelCurrentDataset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(329, Short.MAX_VALUE))
        );

        TabPrincipal.addTab("Summary", panelDataset);

        panelSplitting.setBorder(javax.swing.BorderFactory.createTitledBorder("Splitting"));

        radioRandomHoldout.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        radioRandomHoldout.setText("Random holdout");
        radioRandomHoldout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioRandomHoldoutActionPerformed(evt);
            }
        });

        labelPercIterativeStratified.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        labelPercIterativeStratified.setText("%");

        radioIterativeStratifiedHoldout.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        radioIterativeStratifiedHoldout.setText("Iterative stratified holdout ");
        radioIterativeStratifiedHoldout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioIterativeStratifiedHoldoutActionPerformed(evt);
            }
        });

        textRandomHoldout.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
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

        radioRandomCV.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        radioRandomCV.setText("Random CV");
        radioRandomCV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioRandomCVActionPerformed(evt);
            }
        });

        radioIterativeStratifiedCV.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        radioIterativeStratifiedCV.setText("Iterative stratified CV");
        radioIterativeStratifiedCV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioIterativeStratifiedCVActionPerformed(evt);
            }
        });

        textIterativeStratifiedCV.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        textIterativeStratifiedCV.setText("5");
        textIterativeStratifiedCV.setEnabled(false);
        textIterativeStratifiedCV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textIterativeStratifiedCVActionPerformed(evt);
            }
        });
        textIterativeStratifiedCV.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textIterativeStratifiedCVKeyTyped(evt);
            }
        });

        labelFoldsRandom.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        labelFoldsRandom.setText("Folds");

        textRandomCV.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        textRandomCV.setText("5");
        textRandomCV.setEnabled(false);
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

        labelFoldsIterativeStratified.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        labelFoldsIterativeStratified.setText("Folds");

        labelPercRandom.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        labelPercRandom.setText("%");

        textIterativeStratifiedHoldout.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        textIterativeStratifiedHoldout.setText("70");
        textIterativeStratifiedHoldout.setEnabled(false);
        textIterativeStratifiedHoldout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textIterativeStratifiedHoldoutActionPerformed(evt);
            }
        });
        textIterativeStratifiedHoldout.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textIterativeStratifiedHoldoutKeyTyped(evt);
            }
        });

        radioLPStratifiedHoldout.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        radioLPStratifiedHoldout.setText("LabelPowerset stratified holdout ");
        radioLPStratifiedHoldout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioLPStratifiedHoldoutActionPerformed(evt);
            }
        });

        textLPStratifiedHoldout.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        textLPStratifiedHoldout.setText("70");
        textLPStratifiedHoldout.setEnabled(false);
        textLPStratifiedHoldout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textLPStratifiedHoldoutActionPerformed(evt);
            }
        });
        textLPStratifiedHoldout.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textLPStratifiedHoldoutKeyTyped(evt);
            }
        });

        labelPercLPStratified.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        labelPercLPStratified.setText("%");

        radioLPStratifiedCV.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        radioLPStratifiedCV.setText("LabelPowerset stratified CV");
        radioLPStratifiedCV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioLPStratifiedCVActionPerformed(evt);
            }
        });

        textLPStratifiedCV.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        textLPStratifiedCV.setText("5");
        textLPStratifiedCV.setEnabled(false);
        textLPStratifiedCV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textLPStratifiedCVActionPerformed(evt);
            }
        });
        textLPStratifiedCV.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textLPStratifiedCVKeyTyped(evt);
            }
        });

        labelFoldsLPStratified.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        labelFoldsLPStratified.setText("Folds");

        radioNoSplit.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        radioNoSplit.setText("None");
        radioNoSplit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioNoSplitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSplittingLayout = new javax.swing.GroupLayout(panelSplitting);
        panelSplitting.setLayout(panelSplittingLayout);
        panelSplittingLayout.setHorizontalGroup(
            panelSplittingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSplittingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSplittingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSplittingLayout.createSequentialGroup()
                        .addComponent(radioNoSplit)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelSplittingLayout.createSequentialGroup()
                        .addGroup(panelSplittingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(radioRandomHoldout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(radioIterativeStratifiedHoldout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(radioLPStratifiedHoldout, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelSplittingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelSplittingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelSplittingLayout.createSequentialGroup()
                                    .addComponent(textLPStratifiedHoldout, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(labelPercLPStratified))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSplittingLayout.createSequentialGroup()
                                    .addComponent(textIterativeStratifiedHoldout, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(labelPercIterativeStratified)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSplittingLayout.createSequentialGroup()
                                .addComponent(textRandomHoldout, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11)
                                .addComponent(labelPercRandom)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelSplittingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(radioLPStratifiedCV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(radioIterativeStratifiedCV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(radioRandomCV, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelSplittingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelSplittingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelSplittingLayout.createSequentialGroup()
                                    .addComponent(textRandomCV, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(labelFoldsRandom))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSplittingLayout.createSequentialGroup()
                                    .addComponent(textIterativeStratifiedCV, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(labelFoldsIterativeStratified)))
                            .addGroup(panelSplittingLayout.createSequentialGroup()
                                .addComponent(textLPStratifiedCV, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(labelFoldsLPStratified)))
                        .addGap(107, 107, 107))))
        );
        panelSplittingLayout.setVerticalGroup(
            panelSplittingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSplittingLayout.createSequentialGroup()
                .addComponent(radioNoSplit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelSplittingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioRandomHoldout)
                    .addComponent(textRandomHoldout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPercRandom)
                    .addComponent(radioRandomCV)
                    .addComponent(labelFoldsRandom)
                    .addComponent(textRandomCV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelSplittingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioIterativeStratifiedHoldout)
                    .addComponent(textIterativeStratifiedHoldout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPercIterativeStratified, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(radioIterativeStratifiedCV)
                    .addComponent(textIterativeStratifiedCV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelFoldsIterativeStratified))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelSplittingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioLPStratifiedHoldout)
                    .addComponent(radioLPStratifiedCV)
                    .addComponent(textLPStratifiedCV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelFoldsLPStratified)
                    .addComponent(textLPStratifiedHoldout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPercLPStratified, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jButtonStartPreprocess.setText("Start");
        jButtonStartPreprocess.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonStartPreprocessMouseEntered(evt);
            }
        });
        jButtonStartPreprocess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStartPreprocessActionPerformed(evt);
            }
        });

        jButtonSaveDatasets.setText("Save datasets");
        jButtonSaveDatasets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveDatasetsActionPerformed(evt);
            }
        });

        panelFS.setBorder(javax.swing.BorderFactory.createTitledBorder("Feature Selection"));

        radioBRFS.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        radioBRFS.setText("Binary Relevance attribute selection");
        radioBRFS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioBRFSActionPerformed(evt);
            }
        });

        textBRFS.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        textBRFS.setText("100");
        textBRFS.setEnabled(false);
        textBRFS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textBRFSActionPerformed(evt);
            }
        });
        textBRFS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textBRFSKeyTyped(evt);
            }
        });

        labelBRFS.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        labelBRFS.setText("features");

        labelBRFS_Comb.setFont(new java.awt.Font("Ubuntu", 0, 13)); // NOI18N
        labelBRFS_Comb.setText("Comb");
        labelBRFS_Comb.setEnabled(false);

        jComboBox_BRFS_Comb.setFont(new java.awt.Font("Ubuntu", 0, 13)); // NOI18N
        jComboBox_BRFS_Comb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "max", "min", "avg" }));
        jComboBox_BRFS_Comb.setEnabled(false);
        jComboBox_BRFS_Comb.setPreferredSize(new java.awt.Dimension(58, 20));
        jComboBox_BRFS_Comb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_BRFS_CombActionPerformed(evt);
            }
        });

        labelBRFS_Norm.setFont(new java.awt.Font("Ubuntu", 0, 13)); // NOI18N
        labelBRFS_Norm.setText("Norm");
        labelBRFS_Norm.setEnabled(false);

        jComboBox_BRFS_Norm.setFont(new java.awt.Font("Ubuntu", 0, 13)); // NOI18N
        jComboBox_BRFS_Norm.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dm", "dl", "none" }));
        jComboBox_BRFS_Norm.setEnabled(false);
        jComboBox_BRFS_Norm.setPreferredSize(new java.awt.Dimension(63, 20));
        jComboBox_BRFS_Norm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_BRFS_NormActionPerformed(evt);
            }
        });

        labelBRFS_Out.setFont(new java.awt.Font("Ubuntu", 0, 13)); // NOI18N
        labelBRFS_Out.setText("Score");
        labelBRFS_Out.setEnabled(false);

        jComboBox_BRFS_Out.setFont(new java.awt.Font("Ubuntu", 0, 13)); // NOI18N
        jComboBox_BRFS_Out.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "eval", "rank" }));
        jComboBox_BRFS_Out.setEnabled(false);
        jComboBox_BRFS_Out.setPreferredSize(new java.awt.Dimension(59, 20));
        jComboBox_BRFS_Out.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_BRFS_OutActionPerformed(evt);
            }
        });

        radioRandomFS.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        radioRandomFS.setText("Random attribute selection");
        radioRandomFS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioRandomFSActionPerformed(evt);
            }
        });

        textRandomFS.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        textRandomFS.setText("100");
        textRandomFS.setEnabled(false);
        textRandomFS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textRandomFSActionPerformed(evt);
            }
        });
        textRandomFS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textRandomFSKeyTyped(evt);
            }
        });

        labelRandomFS.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        labelRandomFS.setText("features");

        radioNoFS.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        radioNoFS.setText("None");
        radioNoFS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioNoFSActionPerformed(evt);
            }
        });

        labelBRFS1.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        labelBRFS1.setForeground(new java.awt.Color(106, 106, 106));
        labelBRFS1.setText("//");
        labelBRFS1.setToolTipText("");

        javax.swing.GroupLayout panelFSLayout = new javax.swing.GroupLayout(panelFS);
        panelFS.setLayout(panelFSLayout);
        panelFSLayout.setHorizontalGroup(
            panelFSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFSLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radioNoFS)
                    .addGroup(panelFSLayout.createSequentialGroup()
                        .addComponent(radioRandomFS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textRandomFS, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelRandomFS))
                    .addGroup(panelFSLayout.createSequentialGroup()
                        .addComponent(radioBRFS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textBRFS, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelBRFS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelBRFS1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelBRFS_Comb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox_BRFS_Comb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(labelBRFS_Norm)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox_BRFS_Norm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(labelBRFS_Out)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox_BRFS_Out, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelFSLayout.setVerticalGroup(
            panelFSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFSLayout.createSequentialGroup()
                .addComponent(radioNoFS)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelFSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioBRFS)
                    .addComponent(textBRFS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelBRFS)
                    .addComponent(labelBRFS_Comb)
                    .addComponent(jComboBox_BRFS_Comb, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelBRFS_Norm)
                    .addComponent(jComboBox_BRFS_Norm, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelBRFS_Out)
                    .addComponent(jComboBox_BRFS_Out, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelBRFS1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelFSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioRandomFS)
                    .addComponent(textRandomFS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelRandomFS))
                .addContainerGap())
        );

        jComboBox_SaveFormat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mulan .arff", "Meka .arff" }));
        jComboBox_SaveFormat.setEnabled(false);
        jComboBox_SaveFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_SaveFormatActionPerformed(evt);
            }
        });

        panelIS.setBorder(javax.swing.BorderFactory.createTitledBorder("Instance Selection"));

        radioRandomIS.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        radioRandomIS.setText("Random instance selection");
        radioRandomIS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioRandomISActionPerformed(evt);
            }
        });

        textRandomIS.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        textRandomIS.setText("500");
        textRandomIS.setEnabled(false);
        textRandomIS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textRandomISActionPerformed(evt);
            }
        });
        textRandomIS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textRandomISKeyTyped(evt);
            }
        });

        labelRandomIS.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        labelRandomIS.setText("instances");

        radioNoIS.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        radioNoIS.setText("None");
        radioNoIS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioNoISActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelISLayout = new javax.swing.GroupLayout(panelIS);
        panelIS.setLayout(panelISLayout);
        panelISLayout.setHorizontalGroup(
            panelISLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelISLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelISLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radioNoIS)
                    .addGroup(panelISLayout.createSequentialGroup()
                        .addComponent(radioRandomIS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textRandomIS, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelRandomIS)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelISLayout.setVerticalGroup(
            panelISLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelISLayout.createSequentialGroup()
                .addComponent(radioNoIS)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelISLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioRandomIS)
                    .addComponent(textRandomIS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelRandomIS))
                .addContainerGap())
        );

        javax.swing.GroupLayout panelPreprocessLayout = new javax.swing.GroupLayout(panelPreprocess);
        panelPreprocess.setLayout(panelPreprocessLayout);
        panelPreprocessLayout.setHorizontalGroup(
            panelPreprocessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPreprocessLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(panelPreprocessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelPreprocessLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jButtonStartPreprocess, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSaveDatasets)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox_SaveFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panelFS, javax.swing.GroupLayout.PREFERRED_SIZE, 778, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelSplitting, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelIS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        panelPreprocessLayout.setVerticalGroup(
            panelPreprocessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPreprocessLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(panelSplitting, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelFS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelIS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPreprocessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonStartPreprocess)
                    .addComponent(jButtonSaveDatasets)
                    .addComponent(jComboBox_SaveFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(120, Short.MAX_VALUE))
        );

        panelSplitting.getAccessibleContext().setAccessibleName("");

        TabPrincipal.addTab("Preprocess", panelPreprocess);

        comboBoxLabelsInformation.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Label frequency", "Labelset frequency", "#Labels per example", "Box diagram", "IR per label inter class", "#Labels/IR per label inter class", "IR per label intra class", "#Labels/IR per label intra class", "IR per labelset" }));
        comboBoxLabelsInformation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxLabelsInformationActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout panelImbalanceLeftLayout = new javax.swing.GroupLayout(panelImbalanceLeft);
        panelImbalanceLeft.setLayout(panelImbalanceLeftLayout);
        panelImbalanceLeftLayout.setHorizontalGroup(
            panelImbalanceLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelImbalanceLeftLayout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelImbalanceLeftLayout.setVerticalGroup(
            panelImbalanceLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelImbalanceLeftLayout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 32, Short.MAX_VALUE))
        );

        tabsImbalance.setTabPlacement(javax.swing.JTabbedPane.RIGHT);
        tabsImbalance.setEnabled(false);
        tabsImbalance.setFocusable(false);
        tabsImbalance.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabsImbalanceStateChanged(evt);
            }
        });

        javax.swing.GroupLayout panelExamplesPerLabelLayout = new javax.swing.GroupLayout(panelExamplesPerLabel);
        panelExamplesPerLabel.setLayout(panelExamplesPerLabelLayout);
        panelExamplesPerLabelLayout.setHorizontalGroup(
            panelExamplesPerLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 532, Short.MAX_VALUE)
        );
        panelExamplesPerLabelLayout.setVerticalGroup(
            panelExamplesPerLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("", panelExamplesPerLabel);

        javax.swing.GroupLayout panelExamplesPerLabelsetLayout = new javax.swing.GroupLayout(panelExamplesPerLabelset);
        panelExamplesPerLabelset.setLayout(panelExamplesPerLabelsetLayout);
        panelExamplesPerLabelsetLayout.setHorizontalGroup(
            panelExamplesPerLabelsetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 532, Short.MAX_VALUE)
        );
        panelExamplesPerLabelsetLayout.setVerticalGroup(
            panelExamplesPerLabelsetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("", panelExamplesPerLabelset);

        javax.swing.GroupLayout panelLabelsPerExampleLayout = new javax.swing.GroupLayout(panelLabelsPerExample);
        panelLabelsPerExample.setLayout(panelLabelsPerExampleLayout);
        panelLabelsPerExampleLayout.setHorizontalGroup(
            panelLabelsPerExampleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 532, Short.MAX_VALUE)
        );
        panelLabelsPerExampleLayout.setVerticalGroup(
            panelLabelsPerExampleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("", panelLabelsPerExample);

        javax.swing.GroupLayout panelIRperLabelIntraClassLayout = new javax.swing.GroupLayout(panelIRperLabelIntraClass);
        panelIRperLabelIntraClass.setLayout(panelIRperLabelIntraClassLayout);
        panelIRperLabelIntraClassLayout.setHorizontalGroup(
            panelIRperLabelIntraClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 532, Short.MAX_VALUE)
        );
        panelIRperLabelIntraClassLayout.setVerticalGroup(
            panelIRperLabelIntraClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("", panelIRperLabelIntraClass);

        javax.swing.GroupLayout panelLabelsIRperLabelIntraClassLayout = new javax.swing.GroupLayout(panelLabelsIRperLabelIntraClass);
        panelLabelsIRperLabelIntraClass.setLayout(panelLabelsIRperLabelIntraClassLayout);
        panelLabelsIRperLabelIntraClassLayout.setHorizontalGroup(
            panelLabelsIRperLabelIntraClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 532, Short.MAX_VALUE)
        );
        panelLabelsIRperLabelIntraClassLayout.setVerticalGroup(
            panelLabelsIRperLabelIntraClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("", panelLabelsIRperLabelIntraClass);

        javax.swing.GroupLayout panelIRperLabelsetLayout = new javax.swing.GroupLayout(panelIRperLabelset);
        panelIRperLabelset.setLayout(panelIRperLabelsetLayout);
        panelIRperLabelsetLayout.setHorizontalGroup(
            panelIRperLabelsetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 532, Short.MAX_VALUE)
        );
        panelIRperLabelsetLayout.setVerticalGroup(
            panelIRperLabelsetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("", panelIRperLabelset);

        javax.swing.GroupLayout panelImbalanceDataMetricsLayout = new javax.swing.GroupLayout(panelImbalanceDataMetrics);
        panelImbalanceDataMetrics.setLayout(panelImbalanceDataMetricsLayout);
        panelImbalanceDataMetricsLayout.setHorizontalGroup(
            panelImbalanceDataMetricsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 532, Short.MAX_VALUE)
        );
        panelImbalanceDataMetricsLayout.setVerticalGroup(
            panelImbalanceDataMetricsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("", panelImbalanceDataMetrics);

        javax.swing.GroupLayout panelBoxDiagramLayout = new javax.swing.GroupLayout(panelBoxDiagram);
        panelBoxDiagram.setLayout(panelBoxDiagramLayout);
        panelBoxDiagramLayout.setHorizontalGroup(
            panelBoxDiagramLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 532, Short.MAX_VALUE)
        );
        panelBoxDiagramLayout.setVerticalGroup(
            panelBoxDiagramLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("", panelBoxDiagram);

        javax.swing.GroupLayout panelIRperLabelInterClassLayout = new javax.swing.GroupLayout(panelIRperLabelInterClass);
        panelIRperLabelInterClass.setLayout(panelIRperLabelInterClassLayout);
        panelIRperLabelInterClassLayout.setHorizontalGroup(
            panelIRperLabelInterClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 532, Short.MAX_VALUE)
        );
        panelIRperLabelInterClassLayout.setVerticalGroup(
            panelIRperLabelInterClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("", panelIRperLabelInterClass);

        javax.swing.GroupLayout panelLabelsIRperLabelInterClassLayout = new javax.swing.GroupLayout(panelLabelsIRperLabelInterClass);
        panelLabelsIRperLabelInterClass.setLayout(panelLabelsIRperLabelInterClassLayout);
        panelLabelsIRperLabelInterClassLayout.setHorizontalGroup(
            panelLabelsIRperLabelInterClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 532, Short.MAX_VALUE)
        );
        panelLabelsIRperLabelInterClassLayout.setVerticalGroup(
            panelLabelsIRperLabelInterClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelLabelsIRperLabelInterClass, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelLabelsIRperLabelInterClass, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("", panel1);

        radioExamplesPerLabel.setText("# Examples per label");
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

        javax.swing.GroupLayout panelImbalanceLayout = new javax.swing.GroupLayout(panelImbalance);
        panelImbalance.setLayout(panelImbalanceLayout);
        panelImbalanceLayout.setHorizontalGroup(
            panelImbalanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelImbalanceLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelImbalanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelImbalanceLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelImbalanceLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(panelImbalanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelImbalanceLayout.createSequentialGroup()
                                .addComponent(radioExamplesPerLabelset)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(radioExamplesPerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelImbalanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tabsImbalance)
                    .addComponent(comboBoxLabelsInformation, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(46, 46, 46))
        );
        panelImbalanceLayout.setVerticalGroup(
            panelImbalanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelImbalanceLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelImbalanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelImbalanceLayout.createSequentialGroup()
                        .addComponent(comboBoxLabelsInformation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tabsImbalance))
                    .addGroup(panelImbalanceLayout.createSequentialGroup()
                        .addComponent(panelImbalanceLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(radioExamplesPerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(radioExamplesPerLabelset, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        panelImbalanceLeft.getAccessibleContext().setAccessibleName("pepe");

        TabPrincipal.addTab("Labels information", panelImbalance);

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

        jLabelChiFi_text.setText("When the Chi coefficient is > 6.635 the labels are dependent at 99% confidence (marked in red)");

        javax.swing.GroupLayout panelChiPhiLayout = new javax.swing.GroupLayout(panelChiPhi);
        panelChiPhi.setLayout(panelChiPhiLayout);
        panelChiPhiLayout.setHorizontalGroup(
            panelChiPhiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChiPhiLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabelChiFi_text)
                .addContainerGap(153, Short.MAX_VALUE))
        );
        panelChiPhiLayout.setVerticalGroup(
            panelChiPhiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelChiPhiLayout.createSequentialGroup()
                .addContainerGap(429, Short.MAX_VALUE)
                .addComponent(jLabelChiFi_text)
                .addGap(27, 27, 27))
        );

        tabsDependences.addTab("Chi & Phi coefficient", panelChiPhi);

        panelCoOcurrence.setName("jpanel25"); // NOI18N
        panelCoOcurrence.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                panelCoOcurrenceMouseReleased(evt);
            }
        });

        panelCoOcurrenceRight.setName("jpanel10"); // NOI18N

        javax.swing.GroupLayout panelCoOcurrenceRightLayout = new javax.swing.GroupLayout(panelCoOcurrenceRight);
        panelCoOcurrenceRight.setLayout(panelCoOcurrenceRightLayout);
        panelCoOcurrenceRightLayout.setHorizontalGroup(
            panelCoOcurrenceRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 540, Short.MAX_VALUE)
        );
        panelCoOcurrenceRightLayout.setVerticalGroup(
            panelCoOcurrenceRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 415, Short.MAX_VALUE)
        );

        buttonShowCoOcurrence.setText("Show selected");
        buttonShowCoOcurrence.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowCoOcurrenceActionPerformed(evt);
            }
        });

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

        buttonShowMostFrequent.setText("Show most frequent");
        buttonShowMostFrequent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowMostFrequentActionPerformed(evt);
            }
        });

        textMostFrequent.setText("10");
        textMostFrequent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textMostFrequentActionPerformed(evt);
            }
        });
        textMostFrequent.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textMostFrequentKeyTyped(evt);
            }
        });

        buttonShowMostRelated.setText("Show most related");
        buttonShowMostRelated.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowMostRelatedActionPerformed(evt);
            }
        });

        textMostRelated.setText("10");
        textMostRelated.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textMostRelatedActionPerformed(evt);
            }
        });
        textMostRelated.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textMostRelatedKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout panelCoOcurrenceLayout = new javax.swing.GroupLayout(panelCoOcurrence);
        panelCoOcurrence.setLayout(panelCoOcurrenceLayout);
        panelCoOcurrenceLayout.setHorizontalGroup(
            panelCoOcurrenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCoOcurrenceLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCoOcurrenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCoOcurrenceLayout.createSequentialGroup()
                        .addComponent(buttonShowMostRelated, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textMostRelated, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelCoOcurrenceLayout.createSequentialGroup()
                        .addGroup(panelCoOcurrenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(buttonShowCoOcurrence, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonShowMostFrequent, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textMostFrequent, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(panelCoOcurrenceRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelCoOcurrenceLayout.setVerticalGroup(
            panelCoOcurrenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCoOcurrenceLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCoOcurrenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCoOcurrenceLayout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(buttonShowCoOcurrence, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelCoOcurrenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonShowMostFrequent)
                            .addComponent(textMostFrequent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelCoOcurrenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonShowMostRelated)
                            .addComponent(textMostRelated, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(panelCoOcurrenceRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );

        tabsDependences.addTab("Co-occurrence graph", panelCoOcurrence);

        javax.swing.GroupLayout panelCoOcurrenceValuesLayout = new javax.swing.GroupLayout(panelCoOcurrenceValues);
        panelCoOcurrenceValues.setLayout(panelCoOcurrenceValuesLayout);
        panelCoOcurrenceValuesLayout.setHorizontalGroup(
            panelCoOcurrenceValuesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 828, Short.MAX_VALUE)
        );
        panelCoOcurrenceValuesLayout.setVerticalGroup(
            panelCoOcurrenceValuesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 474, Short.MAX_VALUE)
        );

        tabsDependences.addTab("Co-occurrence values", panelCoOcurrenceValues);

        panelHeatmapGraph.setName("jpanel26"); // NOI18N

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

        panelHeatmap.setName("jpanel10"); // NOI18N

        javax.swing.GroupLayout panelHeatmapLayout = new javax.swing.GroupLayout(panelHeatmap);
        panelHeatmap.setLayout(panelHeatmapLayout);
        panelHeatmapLayout.setHorizontalGroup(
            panelHeatmapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 532, Short.MAX_VALUE)
        );
        panelHeatmapLayout.setVerticalGroup(
            panelHeatmapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 415, Short.MAX_VALUE)
        );

        buttonShowHeatMap.setText("Show selected");
        buttonShowHeatMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowHeatMapActionPerformed(evt);
            }
        });

        buttonShowMostFrequentHeatMap.setText("Show most frequent");
        buttonShowMostFrequentHeatMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowMostFrequentHeatMapActionPerformed(evt);
            }
        });

        buttonShowMostRelatedHeatMap.setText("Show most related");
        buttonShowMostRelatedHeatMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowMostRelatedHeatMapActionPerformed(evt);
            }
        });

        textMostRelatedHeatMap.setText("10");
        textMostRelatedHeatMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textMostRelatedHeatMapActionPerformed(evt);
            }
        });
        textMostRelatedHeatMap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textMostRelatedHeatMapKeyTyped(evt);
            }
        });

        textMostFrequentHeatMap.setText("10");
        textMostFrequentHeatMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textMostFrequentHeatMapActionPerformed(evt);
            }
        });
        textMostFrequentHeatMap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textMostFrequentHeatMapKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout panelHeatmapGraphLayout = new javax.swing.GroupLayout(panelHeatmapGraph);
        panelHeatmapGraph.setLayout(panelHeatmapGraphLayout);
        panelHeatmapGraphLayout.setHorizontalGroup(
            panelHeatmapGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeatmapGraphLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelHeatmapGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelHeatmapGraphLayout.createSequentialGroup()
                        .addComponent(buttonShowMostRelatedHeatMap, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textMostRelatedHeatMap, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelHeatmapGraphLayout.createSequentialGroup()
                        .addGroup(panelHeatmapGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(buttonShowHeatMap, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonShowMostFrequentHeatMap, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textMostFrequentHeatMap, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(panelHeatmap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        panelHeatmapGraphLayout.setVerticalGroup(
            panelHeatmapGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeatmapGraphLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelHeatmapGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelHeatmap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelHeatmapGraphLayout.createSequentialGroup()
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(buttonShowHeatMap, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelHeatmapGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonShowMostFrequentHeatMap)
                            .addComponent(textMostFrequentHeatMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelHeatmapGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonShowMostRelatedHeatMap)
                            .addComponent(textMostRelatedHeatMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        tabsDependences.addTab(" Heatmap graph", panelHeatmapGraph);

        javax.swing.GroupLayout panelHeatmapValuesLayout = new javax.swing.GroupLayout(panelHeatmapValues);
        panelHeatmapValues.setLayout(panelHeatmapValuesLayout);
        panelHeatmapValuesLayout.setHorizontalGroup(
            panelHeatmapValuesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 828, Short.MAX_VALUE)
        );
        panelHeatmapValuesLayout.setVerticalGroup(
            panelHeatmapValuesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 474, Short.MAX_VALUE)
        );

        tabsDependences.addTab("Heatmap values", panelHeatmapValues);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabsDependences))
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelMultipleDatasetsLeftLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonAddMultipleDatasets, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonRemoveMultipleDatasets, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelMultipleDatasetsLeftLayout.setVerticalGroup(
            panelMultipleDatasetsLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMultipleDatasetsLeftLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelMultipleDatasetsLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonAddMultipleDatasets, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonRemoveMultipleDatasets, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanelMultiLayout = new javax.swing.GroupLayout(jPanelMulti);
        jPanelMulti.setLayout(jPanelMultiLayout);
        jPanelMultiLayout.setHorizontalGroup(
            jPanelMultiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 554, Short.MAX_VALUE)
        );
        jPanelMultiLayout.setVerticalGroup(
            jPanelMultiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelMultipleDatasetsLayout = new javax.swing.GroupLayout(panelMultipleDatasets);
        panelMultipleDatasets.setLayout(panelMultipleDatasetsLayout);
        panelMultipleDatasetsLayout.setHorizontalGroup(
            panelMultipleDatasetsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMultipleDatasetsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelMultipleDatasetsLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelMulti, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelMultipleDatasetsLayout.setVerticalGroup(
            panelMultipleDatasetsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMultipleDatasetsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMultipleDatasetsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelMulti, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelMultipleDatasetsLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        TabPrincipal.addTab("Multiple datasets", panelMultipleDatasets);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TabPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 850, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        
        if(current < 0){
            JOptionPane.showMessageDialog(null, "Select a dataset to remove.", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
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
        final JFileChooser jfile1 = new JFileChooser();
        FileNameExtensionFilter fname = new FileNameExtensionFilter(".arff", "arff");
        jfile1.setFileFilter(fname);

        final int returnVal = jfile1.showOpenDialog(this);
        
        progressBar.setIndeterminate(true);
        progressFrame.setVisible(true);
        progressFrame.repaint();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // do the long-running work here
                loadMultiDataset(returnVal, jfile1);
                // at the end:
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setIndeterminate(false);
                        progressFrame.setVisible(false);
                        progressFrame.repaint();
                    }//run
                }); //invokeLater
            }
        }
        ).start();  
        
    }//GEN-LAST:event_buttonAddMultipleDatasetsActionPerformed

    private int loadMultiDataset(int returnVal, JFileChooser jfile1){
        
        if (returnVal == JFileChooser.OPEN_DIALOG)
        {

            File f1 = jfile1.getSelectedFile();

            String dataset_name = f1.getName();
            dataset_name = dataset_name.substring(0,dataset_name.length()-5);
            
            if(Dataset_names.contains(dataset_name))
            {
                JOptionPane.showMessageDialog(null, "The dataset is duplicated.", "alert", JOptionPane.ERROR_MESSAGE);
                return -1;
            }

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
                    //System.out.println("label_count: " + label_count);
                    //label_names_found = new String[label_count];                    
                    
                    
                    //---------------
                    
                    if(label_count > 0){
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
                    }
                    else{
                        label_count = Math.abs(label_count);
                        label_names_found = new String[label_count];
                        
                        String [] sCadenas = new String[label_count];
                        
                        while(!(sCadena = bf.readLine()).contains("@data")){
                            if(!sCadena.trim().equals("")){
                                for(int s=0; s<label_count-1; s++){
                                    sCadenas[s] = sCadenas[s+1];
                                }
                                sCadenas[label_count-1] = sCadena;
                            }
                        }
                        
                        for(int i=0; i<label_count; i++){
                            System.out.println("sCadenas[" + i + "]: -" + sCadenas[i] + "-");
                            label_name = util.Extract_label_name_from_String(sCadenas[i]);

                            if(label_name!= null)
                            {
                                label_names_found[label_found]=label_name;
                                label_found++;

                            }
                        }
                    }
                    
                    //---------------
                    
                    
                    

                    /*while(label_found < label_count)
                    {
                        sCadena = bf.readLine();
                        label_name = util.Extract_label_name_from_String(sCadena);

                        if(label_name!= null)
                        {
                            label_names_found[label_found]=label_name;
                            label_found++;

                        }

                    }*/
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

                if(es_meka){
                    File f2 = new File(filename_database_xml);
                    System.out.println("f2: " + f2.getAbsolutePath());
                    f2.delete();
                }
                
                /*if(util.Esta_dataset(list_dataset, current.getDataSet().relationName()))
                {
                    System.out.println("Dataset name: " + dataset_name);
                    JOptionPane.showMessageDialog(null, "The dataset is duplicated.", "alert", JOptionPane.ERROR_MESSAGE);
                    return;
                }*/

                list_dataset.add(current);
                Dataset_names.add(dataset_name);
                lista.addElement(dataset_name );
                
                System.out.println("current: " + current + " ; dataset_name: " + dataset_name);

            }
            catch (InvalidDataFormatException ex) {
                Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return 1;
    }
    
    private void tabsDependencesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabsDependencesStateChanged

        //System.out.println(jTabbedPane4.getSelectedIndex());
        if(tabsDependences.getSelectedIndex() == 0) {jLabelChiFi_text.setVisible(true);}
        else {jLabelChiFi_text.setVisible(false);}

    }//GEN-LAST:event_tabsDependencesStateChanged

    private void tabsDependencesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabsDependencesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabsDependencesMouseClicked

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

    private void buttonShowCoOcurrenceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowCoOcurrenceActionPerformed

        if(lista_pares== null) 
        {
            JOptionPane.showMessageDialog(null, "You must load a dataset.", "alert", JOptionPane.ERROR_MESSAGE); 
            return;
        }

        ArrayList<String> seleccionados= new  ArrayList();
        int[] selecteds = tableCoOcurrenceLeft.getSelectedRows();
        

        if(selecteds.length<= 1) {
            JOptionPane.showMessageDialog(null, "You must choose two or more labels.", "alert", JOptionPane.ERROR_MESSAGE); 
            return;
        }

        for(int i=0;i<selecteds.length; i++)
        {
            seleccionados.add((tableCoOcurrenceLeft.getValueAt(selecteds[i], 0).toString()));
        }
        
        System.out.println("seleccionados: " + seleccionados.toString());
        
        

        ArrayList<pares_atributos> pares_seleccionados =  util.Encuentra_pares_attr_seleccionados(lista_pares, seleccionados);

        String[] labelname=util.pasa_valores_al_arreglo(seleccionados);//solo cambia el tipo de estructura de datos.

        graphComponent  =  Create_jgraphx(panelCoOcurrenceRight,pares_seleccionados,labelname,graphComponent);

        //LANZAR LA VENTANA EMERGENTE CON LOS PARE SELECCIONADOS

        double[][] pares_freq= util.Get_pares_seleccionados(labelname, pares_seleccionados, num_instancias);

        int posx = this.getBounds().x;
        int posy = this.getBounds().y;

        //jframe_temp mo = new jframe_temp(pares_freq, labelname,posx,posy);
        //mo.setVisible(true);
        //util.Recorre_Arreglo(labelname);
        // util.Recorre_Arreglo_2_dimensiones(pares_freq);

    }//GEN-LAST:event_buttonShowCoOcurrenceActionPerformed

    private void tableCoOcurrenceLeftMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCoOcurrenceLeftMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tableCoOcurrenceLeftMouseClicked

    private void radioExamplesPerLabelsetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioExamplesPerLabelsetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioExamplesPerLabelsetActionPerformed

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

    private void textRandomFSKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textRandomFSKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_textRandomFSKeyTyped

    private void textRandomFSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textRandomFSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textRandomFSActionPerformed

    private void radioRandomFSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioRandomFSActionPerformed
        // TODO add your handling code here:
        
        textBRFS.setEnabled(false);
        labelBRFS_Comb.setEnabled(false);
        jComboBox_BRFS_Comb.setEnabled(false);
        labelBRFS_Norm.setEnabled(false);
        jComboBox_BRFS_Norm.setEnabled(false);
        labelBRFS_Out.setEnabled(false);
        jComboBox_BRFS_Out.setEnabled(false);
        textRandomFS.setEnabled(true);

        jButtonSaveDatasets.setEnabled(false);
        jComboBox_SaveFormat.setEnabled(false);
    }//GEN-LAST:event_radioRandomFSActionPerformed

    private void jComboBox_BRFS_OutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_BRFS_OutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox_BRFS_OutActionPerformed

    private void jComboBox_BRFS_NormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_BRFS_NormActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox_BRFS_NormActionPerformed

    private void jComboBox_BRFS_CombActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_BRFS_CombActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox_BRFS_CombActionPerformed

    private void textBRFSKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textBRFSKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_textBRFSKeyTyped

    private void textBRFSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textBRFSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textBRFSActionPerformed

    private void radioBRFSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioBRFSActionPerformed
        // TODO add your handling code here:
        
        textBRFS.setEnabled(true);
        labelBRFS_Comb.setEnabled(true);
        jComboBox_BRFS_Comb.setEnabled(true);
        labelBRFS_Norm.setEnabled(true);
        jComboBox_BRFS_Norm.setEnabled(true);
        labelBRFS_Out.setEnabled(true);
        jComboBox_BRFS_Out.setEnabled(true);
        textRandomFS.setEnabled(false);

        jButtonSaveDatasets.setEnabled(false);
        jComboBox_SaveFormat.setEnabled(false);
        //button_calculate2_train.setEnabled(false);
        //button_save_train.setEnabled(false);
    }//GEN-LAST:event_radioBRFSActionPerformed

    private void jButtonSaveDatasetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveDatasetsActionPerformed
        // TODO add your handling code here:

        /*
            If only FS is selected, save FS dataset
            If any splitting method is selected, save the splitted datasets (those are FS too if it has been selected)
        */
        
        String format = jComboBox_SaveFormat.getSelectedItem().toString();
        
        if(dataset == null){
            JOptionPane.showMessageDialog(null, "You must load a dataset.", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if((list_dataset_train.isEmpty() && list_dataset_test.isEmpty()) && (radioRandomCV.isSelected() || radioIterativeStratifiedCV.isSelected() || radioLPStratifiedCV.isSelected())){
            JOptionPane.showMessageDialog(null, "You must click on Start before.", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if((dataset_train ==null && dataset_test==null) && (radioIterativeStratifiedHoldout.isSelected()|| radioRandomHoldout.isSelected() || radioLPStratifiedHoldout.isSelected())){
            JOptionPane.showMessageDialog(null, "You must click on Start before.", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if((preprocessedDataset == null) && (radioBRFS.isSelected() || radioRandomFS.isSelected())){
            JOptionPane.showMessageDialog(null, "You must click on Start before.", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }

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
                //Check if none were selected -> Dataset conversion
                if(radioNoFS.isSelected() && radioNoIS.isSelected() && radioNoSplit.isSelected())
                {
                    BufferedWriter bw_train = null;
                    try {

                        String name_dataset= dataset_name1.substring(0,dataset_name1.length()-5);
                        
                        if(format.toLowerCase().contains("meka")){
                            String dataPath = file.getAbsolutePath()+"/"+name_dataset+"-MekaConverted.arff";

                            bw_train = new BufferedWriter(new FileWriter(dataPath));
                            PrintWriter wr_train = new PrintWriter(bw_train);

                            //System.out.println("longitud del train es "+dataset_train.getNumInstances());
                            util.Save_dataset_Meka_in_the_file(wr_train, dataset, name_dataset+"_MekaConverted");

                            wr_train.close();
                            bw_train.close();
                        }
                        else{
                            //Paths trainPath = new Paths.get(file.getAbsolutePath() + "/" + name_dataset + "_train.arff");
                            //Paths testPath = new Paths.get(file.getAbsolutePath() + "/" + name_dataset + "_train.arff");
                            //Paths xmlPath = new Paths.get(file.getAbsolutePath() + "/" + name_dataset + "_train.arff");

                            String dataPath = file.getAbsolutePath()+"/"+name_dataset+"-MulanConverted.arff";
                            path_xml = file.getAbsolutePath()+"/"+name_dataset+"-MulanConverted.xml";

                            bw_train = new BufferedWriter(new FileWriter(dataPath));
                            PrintWriter wr_train = new PrintWriter(bw_train);

                            //System.out.println("longitud del train es "+dataset_train.getNumInstances());
                            util.Save_dataset_in_the_file(wr_train, dataset, name_dataset+"_MulanConverted");

                            wr_train.close();
                            bw_train.close();

                            BufferedWriter bw_xml = new BufferedWriter(new FileWriter(path_xml));
                            PrintWriter wr_xml = new PrintWriter(bw_xml);

                            util.Save_xml_in_the_file(wr_xml, dataset);

                            wr_xml.close();
                            bw_xml.close();
                        }


                        JOptionPane.showMessageDialog(null, "All files have been saved.", "Successful", JOptionPane.INFORMATION_MESSAGE);

                    } catch (IOException ex) {
                        Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                String preprocessedType = new String();
                    
                if(radioRandomIS.isSelected()){
                    preprocessedType += "-randomIS";
                }
                if(radioBRFS.isSelected()){
                    preprocessedType += "-BR_FS";
                }
                else if(radioRandomFS.isSelected()){
                    preprocessedType += "-randomFS";
                }
                
                //check if only FS and/or IS is selected
                if((radioBRFS.isSelected() || radioRandomFS.isSelected() || radioRandomIS.isSelected()) && radioNoSplit.isSelected())//Feature and/or instance selection
                {
                    
                    BufferedWriter bw_train = null;
                    try {

                        String name_dataset= dataset_name1.substring(0,dataset_name1.length()-5);
                        
                        if(format.toLowerCase().contains("meka")){
                            String dataPath = file.getAbsolutePath()+"/"+name_dataset+ preprocessedType + ".arff";

                            bw_train = new BufferedWriter(new FileWriter(dataPath));
                            PrintWriter wr_train = new PrintWriter(bw_train);

                            //System.out.println("longitud del train es "+dataset_train.getNumInstances());
                            util.Save_dataset_Meka_in_the_file(wr_train, preprocessedDataset, name_dataset + preprocessedType);

                            wr_train.close();
                            bw_train.close();
                        }
                        else{
                            //Paths trainPath = new Paths.get(file.getAbsolutePath() + "/" + name_dataset + "_train.arff");
                            //Paths testPath = new Paths.get(file.getAbsolutePath() + "/" + name_dataset + "_train.arff");
                            //Paths xmlPath = new Paths.get(file.getAbsolutePath() + "/" + name_dataset + "_train.arff");

                            String dataPath = file.getAbsolutePath()+"/"+name_dataset+ preprocessedType + ".arff";
                            path_xml = file.getAbsolutePath()+"/"+name_dataset+ preprocessedType +".xml";

                            bw_train = new BufferedWriter(new FileWriter(dataPath));
                            PrintWriter wr_train = new PrintWriter(bw_train);

                            //System.out.println("longitud del train es "+dataset_train.getNumInstances());
                            util.Save_dataset_in_the_file(wr_train, preprocessedDataset, name_dataset+ preprocessedType);

                            wr_train.close();
                            bw_train.close();

                            BufferedWriter bw_xml = new BufferedWriter(new FileWriter(path_xml));
                            PrintWriter wr_xml = new PrintWriter(bw_xml);

                            util.Save_xml_in_the_file(wr_xml,preprocessedDataset);

                            wr_xml.close();
                            bw_xml.close();
                        }


                        JOptionPane.showMessageDialog(null, "All files have been saved.", "Successful", JOptionPane.INFORMATION_MESSAGE);

                    } catch (IOException ex) {
                        Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                
                if(radioIterativeStratifiedHoldout.isSelected()|| radioRandomHoldout.isSelected() || radioLPStratifiedHoldout.isSelected()) //holdout
                {
                    BufferedWriter bw_train = null;
                    try {

                        String name_dataset= dataset_name1.substring(0,dataset_name1.length()-5);

                        //Paths trainPath = new Paths.get(file.getAbsolutePath() + "/" + name_dataset + "_train.arff");
                        //Paths testPath = new Paths.get(file.getAbsolutePath() + "/" + name_dataset + "_train.arff");
                        //Paths xmlPath = new Paths.get(file.getAbsolutePath() + "/" + name_dataset + "_train.arff");

                        if(radioNoFS.isSelected() && radioNoIS.isSelected()){
                            path_train = file.getAbsolutePath()+"/"+name_dataset+"-train.arff";
                            path_test = file.getAbsolutePath()+"/"+name_dataset+"-test.arff";
                            path_xml = file.getAbsolutePath()+"/"+name_dataset+".xml"; 
                        }
                        else{
                            path_train = file.getAbsolutePath()+"/"+name_dataset+ preprocessedType + "-train.arff";
                            path_test = file.getAbsolutePath()+"/"+name_dataset+ preprocessedType + "-test.arff";
                            path_xml = file.getAbsolutePath()+"/"+name_dataset+ preprocessedType + ".xml";
                        }
                        
                        if(format.toLowerCase().contains("meka")){
                            bw_train = new BufferedWriter(new FileWriter(path_train));
                            PrintWriter wr_train = new PrintWriter(bw_train);

                            //System.out.println("longitud del train es "+dataset_train.getNumInstances());
                            util.Save_dataset_Meka_in_the_file(wr_train, dataset_train);

                            wr_train.close();
                            bw_train.close();

                            BufferedWriter bw_test = new BufferedWriter(new FileWriter(path_test));
                            PrintWriter wr_test = new PrintWriter(bw_test);

                            // System.out.println("longitud del test es "+dataset_test.getNumInstances());
                            util.Save_dataset_Meka_in_the_file(wr_test, dataset_test);

                            wr_test.close();
                            bw_test.close();
                        }
                        else{
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

                            util.Save_xml_in_the_file(wr_xml, dataset_train);

                            wr_xml.close();
                            bw_xml.close();
                        }

                        JOptionPane.showMessageDialog(null, "All files have been saved.", "Successful", JOptionPane.INFORMATION_MESSAGE);

                    } catch (IOException ex) {
                        Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                else if(radioIterativeStratifiedCV.isSelected()|| radioRandomCV.isSelected() || radioLPStratifiedCV.isSelected())//CROSS VALIDATION
                {
                    try{

                        if(format.toLowerCase().contains("meka")){
                           if(radioNoFS.isSelected() && radioNoIS.isSelected()){
                                util.Save_dataset_Meka_in_the_file(list_dataset_train,file.getAbsolutePath(), dataset_name1.substring(0,dataset_name1.length()-5), "-train");
                                util.Save_dataset_Meka_in_the_file(list_dataset_test,file.getAbsolutePath(), dataset_name1.substring(0,dataset_name1.length()-5), "-test");  
                            }
                            else{
                                util.Save_dataset_Meka_in_the_file(list_dataset_train,file.getAbsolutePath(), dataset_name1.substring(0,dataset_name1.length()-5),  preprocessedType + "-train");
                                util.Save_dataset_Meka_in_the_file(list_dataset_test,file.getAbsolutePath(), dataset_name1.substring(0,dataset_name1.length()-5),  preprocessedType + "-test");  
                            }
                        }
                        else{
                            if(radioNoFS.isSelected() && radioNoIS.isSelected()){
                                util.Save_dataset_in_the_file(list_dataset_train,file.getAbsolutePath(), dataset_name1.substring(0,dataset_name1.length()-5), "-train");
                                util.Save_dataset_in_the_file(list_dataset_test,file.getAbsolutePath(), dataset_name1.substring(0,dataset_name1.length()-5), "-test");  
                                path_xml = file.getAbsolutePath()+"/"+dataset_name1.substring(0,dataset_name1.length()-5)+".xml";
                            }
                            else{
                                util.Save_dataset_in_the_file(list_dataset_train,file.getAbsolutePath(), dataset_name1.substring(0,dataset_name1.length()-5),  preprocessedType + "-train");
                                util.Save_dataset_in_the_file(list_dataset_test,file.getAbsolutePath(), dataset_name1.substring(0,dataset_name1.length()-5),  preprocessedType + "-test");  
                                path_xml = file.getAbsolutePath()+"/"+dataset_name1.substring(0,dataset_name1.length()-5)+ preprocessedType + ".xml";
                            }
        

                            BufferedWriter bw_xml = new BufferedWriter(new FileWriter(path_xml));
                            PrintWriter wr_xml = new PrintWriter(bw_xml);

                            System.out.println("filename_database_xml_path: " + filename_database_xml_path);
                            util.Save_xml_in_the_file(wr_xml,list_dataset_train.get(0));

                            wr_xml.close();
                            bw_xml.close();
                        }

                        JOptionPane.showMessageDialog(null, "All files have been saved.", "Successful", JOptionPane.INFORMATION_MESSAGE);

                    }
                    catch(Exception e1){
                        e1.printStackTrace();
                    }

                }
                
                Toolkit.getDefaultToolkit().beep();
            }

        }
    }//GEN-LAST:event_jButtonSaveDatasetsActionPerformed

    // COMIENZA A CALCULAR LAS METRICAS SEGUN LA OPCION DEL train/TEST (BOTON "start")
    private void jButtonStartPreprocessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStartPreprocessActionPerformed

        
        progressBar.setIndeterminate(true);
        progressFrame.setVisible(true);
        progressFrame.repaint();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // do the long-running work here
                final int returnCode = preprocess();
                // at the end:
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setIndeterminate(false);
                        progressFrame.setVisible(false);
                        progressFrame.repaint();
                        
                        if(returnCode == 1 && (!(radioNoFS.isSelected() && radioNoSplit.isSelected() && radioNoIS.isSelected()))){
                            JOptionPane.showMessageDialog(null, "Datasets have been generated succesfully.", "Successful", JOptionPane.INFORMATION_MESSAGE);
                        }

                        Toolkit.getDefaultToolkit().beep();
                    }//run
                }); //invokeLater
            }
        }
        ).start();
        
        
    }//GEN-LAST:event_jButtonStartPreprocessActionPerformed

    private int preprocess(){
        
        list_dataset_train = new ArrayList();
        list_dataset_test = new ArrayList();

        Instances train=null,test=null;
        MultiLabelInstances train_ml=null,test_ml=null;

        if(dataset == null){
            JOptionPane.showMessageDialog(null, "You must load a dataset.", "alert", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        
        MultiLabelInstances preprocessDataset = dataset;

        
        //First check that any option is selected
        if(radioNoSplit.isSelected() && radioNoFS.isSelected() && radioNoIS.isSelected()){
            //JOptionPane.showMessageDialog(null, "Select at least one option.", "alert", JOptionPane.ERROR_MESSAGE);
            //return;
        }
        
        if(! radioNoIS.isSelected()){
            //Do Instance Selection
            if(radioRandomIS.isSelected()){
                int nInstances = Integer.parseInt(textRandomIS.getText());

                if(nInstances < 1){
                    JOptionPane.showMessageDialog(null, "The number of instances must be a positive natural number.", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }
                else if(nInstances > dataset.getNumInstances()){
                    JOptionPane.showMessageDialog(null, "The number of instances to select must be less than the original.", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }
                
                
                Instances dataIS = null;
                try {
                    Randomize randomize = new Randomize();                
                    dataIS = dataset.getDataSet();
                    
                    System.out.println("dataIS.get(0): " + dataIS.get(0));
                    
                    randomize.setInputFormat(dataIS);
                    dataIS = Filter.useFilter(dataIS, randomize);
                    randomize.batchFinished();
                    
                    System.out.println("dataIS.get(0): " + dataIS.get(0));
                    
                    RemoveRange removeRange = new RemoveRange();
                    removeRange.setInputFormat(dataIS);
                    System.out.println((nInstances+1) + "-last");
                    removeRange.setInstancesIndices((nInstances+1) + "-last");
                    
                    dataIS = Filter.useFilter(dataIS, removeRange);
                    removeRange.batchFinished();
                    System.out.println("dataIS.numInstances(): " + dataIS.numInstances());
                    
                    preprocessDataset = dataset.reintegrateModifiedDataSet(dataIS);
                } catch (Exception ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                
   
                if(preprocessDataset == null)
                {
                    JOptionPane.showMessageDialog(null, "Error when selecting instances.", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }

                preprocessedDataset = preprocessDataset;
                
                System.out.println("preprocessedDataset.getNumInstances(): " + preprocessedDataset.getNumInstances());

            }
        }
        
        if(! radioNoFS.isSelected()){
            //FS_BR
            if(radioBRFS.isSelected()){
                int nFeatures = Integer.parseInt(textBRFS.getText());
                if(nFeatures < 1){
                    JOptionPane.showMessageDialog(null, "The number of features must be a positive natural number.", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }
                else if(nFeatures > dataset.getFeatureIndices().length){
                    JOptionPane.showMessageDialog(null, "The number of features to select must be less than the original.", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }

                String combination = jComboBox_BRFS_Comb.getSelectedItem().toString();
                String normalization = jComboBox_BRFS_Norm.getSelectedItem().toString();
                String output = jComboBox_BRFS_Out.getSelectedItem().toString();

                FeatureSelector fs;
                if(radioNoIS.isSelected()){
                   fs = new FeatureSelector(dataset, nFeatures); 
                }
                else{
                    //If IS have been done
                    fs = new FeatureSelector(preprocessDataset, nFeatures);
                }
                
                preprocessedDataset = fs.select(combination, normalization, output);

                if(preprocessedDataset == null)
                {
                    JOptionPane.showMessageDialog(null, "Error when selecting features.", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }
                
                preprocessDataset = preprocessedDataset;

                /*holdout_random =false;
                cv_ramdon =false;
                holdout_iterative_stratified =false;
                cv_iterative_stratified =false;
                holdout_LP_stratified =false;
                cv_LP_stratified =false;
                feature_selection_br = true;
                feature_selection_random = false;*/
            }
            else if(radioRandomFS.isSelected()){
                int nFeatures = Integer.parseInt(textRandomFS.getText());

                if(nFeatures < 1){
                    JOptionPane.showMessageDialog(null, "The number of features must be a positive natural number.", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }
                else if(nFeatures > dataset.getFeatureIndices().length){
                    JOptionPane.showMessageDialog(null, "The number of features to select must be less than the original.", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }
                
                FeatureSelector fs;

                if(radioNoIS.isSelected()){
                   fs = new FeatureSelector(dataset, nFeatures);
                }
                else{
                    //If IS have been done
                    fs = new FeatureSelector(preprocessDataset, nFeatures);
                }
                                
                preprocessedDataset = fs.randomSelect();
                
                if(preprocessedDataset == null)
                {
                    JOptionPane.showMessageDialog(null, "Error when selecting features.", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }
                
                preprocessDataset = preprocessedDataset;

                /*holdout_random =false;
                cv_ramdon =false;
                holdout_iterative_stratified =false;
                cv_iterative_stratified =false;
                holdout_LP_stratified =false;
                cv_LP_stratified =false;
                feature_selection_br = false;
                feature_selection_random = true;*/
            }
        }

        
        if(!radioNoSplit.isSelected()){
            
            //Random Holdout
            if(radioRandomHoldout.isSelected()){
                String split = textRandomHoldout.getText();
                double percent_split = Double.parseDouble(split);
                if((percent_split <= 0) || (percent_split >= 100)){
                    JOptionPane.showMessageDialog(null, "The percentage must be a number in the range (0, 100).", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }

                try{

                    RandomTrainTest pre = new RandomTrainTest();
                    
                    MultiLabelInstances [] partitions = pre.split(preprocessDataset, percent_split);

                    //dataset_train = new MultiLabelInstances(trainDataSet, dataset.getLabelsMetaData());
                    //dataset_test = new MultiLabelInstances(testDataSet, dataset.getLabelsMetaData());

                    dataset_train = partitions[0];
                    dataset_test = partitions[1];

                    /*holdout_random =true;
                    cv_ramdon =false;
                    holdout_iterative_stratified =false;
                    cv_iterative_stratified =false;
                    holdout_LP_stratified =false;
                    cv_LP_stratified =false;
                    feature_selection_br = false;
                    feature_selection_random = false;*/
                    //calculte metric selected

                    //button_calculateActionPerformed1(evt, jTable5, jTable6, jTable7);// HOLDOUT percentage
                }

                catch (InvalidDataFormatException ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //Random CV
            else if(radioRandomCV.isSelected()){
                String split = textRandomCV.getText();

                if(split.equals(""))
                {
                    JOptionPane.showMessageDialog(null, "You must enter a number of folds.", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }

                int nFolds = 0;

                try{
                    nFolds = Integer.parseInt(split);
                }
                catch(Exception e){
                    JOptionPane.showMessageDialog(null, "Introduce a correct number of folds.", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }

                if(nFolds < 2)
                {
                    JOptionPane.showMessageDialog(null, "The number of folds must be greater or equal to 2.", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }
                else if(nFolds > preprocessDataset.getNumInstances()){
                    JOptionPane.showMessageDialog(null, "The number of folds can not be greater than the number of instances.", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }

                try{
                    MultiLabelInstances temp = preprocessDataset.clone();
                    Instances dataset_temp = temp.getDataSet();

                    int seed = (int)(Math.random()*100)+100;
                    Random rand = new Random(seed);

                    dataset_temp.randomize(rand);

                    Instances [] foldsCV = new Instances[nFolds];
                    for(int i=0; i<nFolds; i++){
                        foldsCV[i] = new Instances(preprocessDataset.getDataSet(), 1);
                    }

                    for(int i=0; i<dataset_temp.numInstances(); i++){
                        foldsCV[i%nFolds].add(dataset_temp.get(i));
                    }

                    train = new Instances(preprocessDataset.getDataSet(), 0);
                    test = new Instances(preprocessDataset.getDataSet(), 0);
                    for(int i=0; i<nFolds; i++){
                        train.clear();
                        test.clear();
                        for(int j=0; j<nFolds; j++){
                            if(i != j){
                                train.addAll(foldsCV[j]);
                            }
                        }
                        test.addAll(foldsCV[i]);

                        System.out.println("train: " + train.numInstances());
                        System.out.println("test: " + test.numInstances());

                        list_dataset_train.add(new MultiLabelInstances(train, preprocessDataset.getLabelsMetaData()));
                        list_dataset_test.add(new MultiLabelInstances(test, preprocessDataset.getLabelsMetaData()));
                    }

                    /*holdout_random =false;
                    cv_ramdon =true;
                    holdout_iterative_stratified =false;
                    cv_iterative_stratified =false;
                    holdout_LP_stratified =false;
                    cv_LP_stratified =false;
                    feature_selection_br = false;
                    feature_selection_random = false;*/
                }

                catch (Exception ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //Iterative stratified holdout
            else if(radioIterativeStratifiedHoldout.isSelected()){
                String split = textIterativeStratifiedHoldout.getText();
                double percent_split = Double.parseDouble(split);
                if((percent_split <= 0) || (percent_split >= 100)){
                    JOptionPane.showMessageDialog(null, "The percentage must be a number in the range (0, 100).", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }

                try{
                    IterativeTrainTest pre = new IterativeTrainTest();
                    MultiLabelInstances [] partitions = pre.split(preprocessDataset, percent_split);

                    dataset_train = partitions[0];
                    dataset_test = partitions[1];

                    /*holdout_random =false;
                    cv_ramdon =false;
                    holdout_iterative_stratified =true;
                    cv_iterative_stratified =false;
                    holdout_LP_stratified =false;
                    cv_LP_stratified =false;
                    feature_selection_br = false;
                    feature_selection_random = false;*/
                    //calculte metric selected

                    //button_calculateActionPerformed1(evt, jTable5, jTable6, jTable7);// HOLDOUT percentage
                }
                catch (Exception ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //Iterative stratified CV
            else if(radioIterativeStratifiedCV.isSelected()){
                String split = textIterativeStratifiedCV.getText();

                if(split.equals(""))
                {
                    JOptionPane.showMessageDialog(null, "You must enter a number of folds.", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }

                int nFolds = 0;

                try{
                    nFolds = Integer.parseInt(split);
                }
                catch(Exception e){
                    JOptionPane.showMessageDialog(null, "Introduce a correct number of folds.", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }

                if(nFolds < 2)
                {
                    JOptionPane.showMessageDialog(null, "The number of folds must be greater or equal to 2.", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }
                else if(nFolds > preprocessDataset.getNumInstances()){
                    JOptionPane.showMessageDialog(null, "The number of folds can not be greater than the number of instances.", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }

                IterativeStratification strat = new IterativeStratification();
                MultiLabelInstances folds [] = strat.stratify(preprocessDataset, nFolds);

                for(int i=0; i<nFolds; i++){
                    try {

                        int trainSize = 0, testSize = 0;
                        for(int j=0; j<nFolds; j++){
                            if(i != j){
                                trainSize += folds[j].getNumInstances();
                            }
                        }
                        testSize += folds[i].getNumInstances();

                        train = new Instances(preprocessDataset.getDataSet(), trainSize);
                        test = new Instances(preprocessDataset.getDataSet(), testSize);
                        for(int j=0; j<nFolds; j++){
                            if(i != j){
                                train.addAll(folds[j].getDataSet());
                            }
                        }
                        test.addAll(folds[i].getDataSet());

                        list_dataset_train.add(new MultiLabelInstances(train, preprocessDataset.getLabelsMetaData()));
                        list_dataset_test.add(new MultiLabelInstances(test, preprocessDataset.getLabelsMetaData()));
                    } catch (InvalidDataFormatException ex) {
                        Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
            //LP stratified holdout
            else if(radioLPStratifiedHoldout.isSelected()){
                String split = textLPStratifiedHoldout.getText();
                double percent_split = Double.parseDouble(split);
                if((percent_split <= 0) || (percent_split >= 100)){
                    JOptionPane.showMessageDialog(null, "The percentage must be a number in the range (0, 100).", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }

                try{
                    IterativeTrainTest pre = new IterativeTrainTest();
                    MultiLabelInstances [] partitions = pre.split(preprocessDataset, percent_split);

                    dataset_train = partitions[0];
                    dataset_test = partitions[1];

                    /*holdout_random =false;
                    cv_ramdon =false;
                    holdout_iterative_stratified =false;
                    cv_iterative_stratified =false;
                    holdout_LP_stratified =true;
                    cv_LP_stratified =false;
                    feature_selection_br = false;
                    feature_selection_random = false;*/
                    //calculte metric selected

                    //button_calculateActionPerformed1(evt, jTable5, jTable6, jTable7);// HOLDOUT percentage
                }
                catch (Exception ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //LP stratified CV
            else if(radioLPStratifiedCV.isSelected()){
                String split = textLPStratifiedCV.getText();

                if(split.equals(""))
                {
                    JOptionPane.showMessageDialog(null, "You must enter a number of folds.", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }

                int nFolds = 0;

                try{
                    nFolds = Integer.parseInt(split);
                }
                catch(Exception e){
                    JOptionPane.showMessageDialog(null, "Introduce a correct number of folds.", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }

                if(nFolds < 2)
                {
                    JOptionPane.showMessageDialog(null, "The number of folds must be greater or equal to 2.", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }
                else if(nFolds > preprocessDataset.getNumInstances()){
                    JOptionPane.showMessageDialog(null, "The number of folds can not be greater than the number of instances.", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }

                LabelPowersetTrainTest strat = new LabelPowersetTrainTest();
                MultiLabelInstances folds [] = strat.stratify(preprocessDataset, nFolds);

                for(int i=0; i<nFolds; i++){
                    try {
                        train = new Instances(preprocessDataset.getDataSet(), 0);
                        test = new Instances(preprocessDataset.getDataSet(), 0);

                        for(int j=0; j<nFolds; j++){
                            if(i != j){
                                train.addAll(folds[j].getDataSet());
                            }
                        }
                        test.addAll(folds[i].getDataSet());

                        list_dataset_train.add(new MultiLabelInstances(train, preprocessDataset.getLabelsMetaData()));
                        list_dataset_test.add(new MultiLabelInstances(test, preprocessDataset.getLabelsMetaData()));
                    } catch (InvalidDataFormatException ex) {
                        Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                /*holdout_random =false;
                cv_ramdon =false;
                holdout_iterative_stratified =false;
                cv_iterative_stratified =false;
                holdout_LP_stratified =false;
                cv_LP_stratified =true;
                feature_selection_br = false;
                feature_selection_random = false;*/
            }

        }

            jButtonSaveDatasets.setEnabled(true);
            jComboBox_SaveFormat.setEnabled(true);
            //button_calculate2_train.setEnabled(true);
            //button_save_train.setEnabled(false);

            /*
            if(!(radioNoFS.isSelected() && radioNoSplit.isSelected())){
                JOptionPane.showMessageDialog(null, "Datasets have been generated succesfully.", "Successful", JOptionPane.INFORMATION_MESSAGE);
            }
            
            Toolkit.getDefaultToolkit().beep();
            */
            
        return 1;
    }
    
    private void textLPStratifiedCVKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textLPStratifiedCVKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_textLPStratifiedCVKeyTyped

    private void textLPStratifiedCVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textLPStratifiedCVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textLPStratifiedCVActionPerformed

    private void radioLPStratifiedCVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioLPStratifiedCVActionPerformed
        // TODO add your handling code here:
        textRandomHoldout.setEnabled(false);
        textIterativeStratifiedHoldout.setEnabled(false);
        textLPStratifiedHoldout.setEnabled(false);
        //buttonChooseSuppliedTest.setEnabled(false);
        textRandomCV.setEnabled(false);
        textIterativeStratifiedCV.setEnabled(false);
        textLPStratifiedCV.setEnabled(true);

        jButtonSaveDatasets.setEnabled(false);
        jComboBox_SaveFormat.setEnabled(false);
        //button_calculate2_train.setEnabled(false);
        //button_save_train.setEnabled(false);
    }//GEN-LAST:event_radioLPStratifiedCVActionPerformed

    private void textLPStratifiedHoldoutKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textLPStratifiedHoldoutKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_textLPStratifiedHoldoutKeyTyped

    private void textLPStratifiedHoldoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textLPStratifiedHoldoutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textLPStratifiedHoldoutActionPerformed

    private void radioLPStratifiedHoldoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioLPStratifiedHoldoutActionPerformed
        // TODO add your handling code here:
        textRandomHoldout.setEnabled(false);
        textIterativeStratifiedHoldout.setEnabled(false);
        textLPStratifiedHoldout.setEnabled(true);
        //buttonChooseSuppliedTest.setEnabled(false);
        textRandomCV.setEnabled(false);
        textIterativeStratifiedCV.setEnabled(false);
        textLPStratifiedCV.setEnabled(false);

        jButtonSaveDatasets.setEnabled(false);
        jComboBox_SaveFormat.setEnabled(false);
        //button_calculate2_train.setEnabled(false);
        //button_save_train.setEnabled(false);
    }//GEN-LAST:event_radioLPStratifiedHoldoutActionPerformed

    //FILTRA LOS NUMEROS SOLAMENTE
    private void textIterativeStratifiedHoldoutKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textIterativeStratifiedHoldoutKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if(!Character.isDigit(c))
        {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_textIterativeStratifiedHoldoutKeyTyped

    private void textIterativeStratifiedHoldoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textIterativeStratifiedHoldoutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textIterativeStratifiedHoldoutActionPerformed

    private void textRandomCVKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textRandomCVKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_textRandomCVKeyTyped

    private void textRandomCVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textRandomCVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textRandomCVActionPerformed

    private void textIterativeStratifiedCVKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textIterativeStratifiedCVKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_textIterativeStratifiedCVKeyTyped

    private void textIterativeStratifiedCVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textIterativeStratifiedCVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textIterativeStratifiedCVActionPerformed

    private void radioIterativeStratifiedCVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioIterativeStratifiedCVActionPerformed
        // TODO add your handling code here:

        textRandomHoldout.setEnabled(false);
        textIterativeStratifiedHoldout.setEnabled(false);
        textLPStratifiedHoldout.setEnabled(false);
        //buttonChooseSuppliedTest.setEnabled(false);
        textRandomCV.setEnabled(false);
        textIterativeStratifiedCV.setEnabled(true);
        textLPStratifiedCV.setEnabled(false);

        jButtonSaveDatasets.setEnabled(false);
        jComboBox_SaveFormat.setEnabled(false);
        //button_calculate2_train.setEnabled(false);
        //button_save_train.setEnabled(false);
    }//GEN-LAST:event_radioIterativeStratifiedCVActionPerformed

    private void radioRandomCVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioRandomCVActionPerformed
        // TODO add your handling code here:
        textRandomHoldout.setEnabled(false);
        textIterativeStratifiedHoldout.setEnabled(false);
        textLPStratifiedHoldout.setEnabled(false);
        //buttonChooseSuppliedTest.setEnabled(false);
        textRandomCV.setEnabled(true);
        textIterativeStratifiedCV.setEnabled(false);
        textLPStratifiedCV.setEnabled(false);

        jButtonSaveDatasets.setEnabled(false);
        jComboBox_SaveFormat.setEnabled(false);
        //button_calculate2_train.setEnabled(false);
        //button_save_train.setEnabled(false);
    }//GEN-LAST:event_radioRandomCVActionPerformed

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
    private void radioIterativeStratifiedHoldoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioIterativeStratifiedHoldoutActionPerformed
        // TODO add your handling code here:
        textIterativeStratifiedHoldout.setEnabled(true);
        textRandomHoldout.setEnabled(false);
        //buttonChooseSuppliedTest.setEnabled(false);
        textRandomCV.setEnabled(false);
        textIterativeStratifiedCV.setEnabled(false);
        textLPStratifiedHoldout.setEnabled(false);
        textLPStratifiedCV.setEnabled(false);

        jButtonSaveDatasets.setEnabled(false);
        jComboBox_SaveFormat.setEnabled(false);
        //button_calculate2_train.setEnabled(false);
        //button_save_train.setEnabled(false);
    }//GEN-LAST:event_radioIterativeStratifiedHoldoutActionPerformed

  //OPTION "Ramdon  split"  
    private void radioRandomHoldoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioRandomHoldoutActionPerformed
        // TODO add your handling code here:
        textRandomHoldout.setEnabled(true);
        textIterativeStratifiedHoldout.setEnabled(false);
        // buttonChooseSuppliedTest.setEnabled(false);
        textRandomCV.setEnabled(false);
        textIterativeStratifiedCV.setEnabled(false);
        textLPStratifiedHoldout.setEnabled(false);
        textLPStratifiedCV.setEnabled(false);

        jButtonSaveDatasets.setEnabled(false);
        jComboBox_SaveFormat.setEnabled(false);
        //button_calculate2_train.setEnabled(false);
        //button_save_train.setEnabled(false);
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
        
        final JFileChooser jfile1 = new JFileChooser();
        FileNameExtensionFilter fname = new FileNameExtensionFilter(".arff", "arff");
        jfile1.setFileFilter(fname);

        final boolean deleteXML = false;
        
        final int returnVal = jfile1.showOpenDialog(this);
        
        progressBar.setIndeterminate(true);
        progressFrame.setVisible(true);
        progressFrame.repaint();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // do the long-running work here
                loadDataset(returnVal, jfile1, deleteXML);
                // at the end:
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setIndeterminate(false);
                        progressFrame.setVisible(false);
                        progressFrame.repaint();
                    }//run
                }); //invokeLater
            }
        }
        ).start();        

    }//GEN-LAST:event_buttonChooseFileActionPerformed

    private int loadDataset(int returnVal, JFileChooser jfile1, boolean deleteXML){
        
        if (returnVal == JFileChooser.OPEN_DIALOG)
        {
            File f1 = jfile1.getSelectedFile();
            dataset_name1 = f1.getName();
            dataset_current_name = dataset_name1.substring(0,dataset_name1.length()-5);

            String filename_database_arff = f1.getAbsolutePath();

            filename_database_xml_path=  filename_database_arff.substring(0,filename_database_arff.length()-5)+".xml";
            filename_database_xml = util.Get_file_name_xml1(filename_database_xml_path);

            File  file_temp = new File(filename_database_xml_path);

            
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
                    deleteXML = true;
                    
                    System.out.println("el dataset es de tipo MEKA");
                    es_de_tipo_meka = true;

                    int label_count = util.Extract_labels_from_arff(sCadena);
                    
                    if(label_count > 0){
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
                    }
                    else{
                        label_count = Math.abs(label_count);
                        label_names_found = new String[label_count];
                        
                        String [] sCadenas = new String[label_count];
                        
                        while(!(sCadena = bf.readLine()).contains("@data")){
                            if(!sCadena.trim().equals("")){
                                for(int s=0; s<label_count-1; s++){
                                    sCadenas[s] = sCadenas[s+1];
                                }
                                sCadenas[label_count-1] = sCadena;
                            }
                        }
                        
                        for(int i=0; i<label_count; i++){
                            System.out.println("sCadenas[" + i + "]: -" + sCadenas[i] + "-");
                            label_name = util.Extract_label_name_from_String(sCadenas[i]);

                            if(label_name!= null)
                            {
                                label_names_found[label_found]=label_name;
                                label_found++;

                            }
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
                return -1;
            } catch (IOException ex) {
                Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
            }

            //-------------------------------------------------------------------------------------------------------

            //-------------------------------------------------------------------------------------------------------

            if(!file_temp.exists()) //SI NO EXISTE EL XML
            {
                filename_database_xml_path = util.Get_xml_string(filename_database_arff);
                filename_database_xml = util.Get_file_name_xml(filename_database_xml_path);
            }

            System.out.println("Choosefilename_database_xml_path: " + filename_database_xml_path);

            try {
                File f = new File(filename_database_xml);
                if(f.exists() && !f.isDirectory()) { 
                    MultiLabelInstances dataset_temp = new MultiLabelInstances(filename_database_arff, filename_database_xml);
                }
                else{
                    JOptionPane.showMessageDialog(null, "File could not be loaded.", "alert", JOptionPane.ERROR_MESSAGE); 
                    return -1;
                }

                //System.out.println("prueba xml "+filename_database_xml);
                //velocidad =util.get_velocidad(dataset_temp);
            } catch (InvalidDataFormatException ex) {
                Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                progressBar.setVisible(false);
                progressFrame.setVisible(false);
                progressFrame.repaint();
                return -1;
            }
            
            

            //--------------------------------------------------------------
            

            //--------------------------------------------------------------

            initializeTableMetrics();
            initializeTableMetricsCommon();
            initializeTableMetricsTrainTest();
            clearTable_metrics_principal();
            clearTable_metrics_traintest();
            
            File f = new File(filename_database_xml);
            if(f.exists() && !f.isDirectory()) { 
                Load_dataset(filename_database_arff, filename_database_xml);
            }
            else{
                Load_dataset(filename_database_arff, null);
            }

            if(deleteXML){
                    File f2 = new File(filename_database_xml);
                    System.out.println("f2: " + f2.getAbsolutePath());
                    f2.delete();
                }

            textChooseFile.setText(filename_database_arff);

        }
        
        return 1;
    }
    
    private void tableHeatmapLeftMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableHeatmapLeftMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tableHeatmapLeftMouseClicked

    private void buttonShowMostFrequentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowMostFrequentActionPerformed

        int n = Integer.parseInt(textMostFrequent.getText());
        
        if(n > dataset.getNumLabels()){
            JOptionPane.showMessageDialog(null, "The number of labels to show must be less than the number of labels in the dataset.", "alert", JOptionPane.ERROR_MESSAGE); 
            return;
        }
        else if(n < 2){
            JOptionPane.showMessageDialog(null, "Select at least 2 labels.", "alert", JOptionPane.ERROR_MESSAGE); 
            return;
        }

        if(lista_pares== null) 
        {
            JOptionPane.showMessageDialog(null, "You must load a dataset.", "alert", JOptionPane.ERROR_MESSAGE); 
            return;
        }
        
        
        tableCoOcurrenceLeft.setRowSelectionInterval(0, n-1);
        
        ArrayList<String> seleccionados= new  ArrayList();
        
        String current = new String();
        for(int i=0;i<n; i++)
        {
            current = (tableCoOcurrenceLeft.getValueAt(i, 0).toString());
            if(current != null){
                seleccionados.add(current);
            }
            else break;
        }
        

        ArrayList<pares_atributos> pares_seleccionados =  util.Encuentra_pares_attr_seleccionados(lista_pares, seleccionados);

        String[] labelname=util.pasa_valores_al_arreglo(seleccionados);//solo cambia el tipo de estructura de datos.

        graphComponent  =  Create_jgraphx(panelCoOcurrenceRight,pares_seleccionados,labelname,graphComponent);

        //LANZAR LA VENTANA EMERGENTE CON LOS PARE SELECCIONADOS

        double[][] pares_freq= util.Get_pares_seleccionados(labelname, pares_seleccionados, num_instancias);

        int posx = this.getBounds().x;
        int posy = this.getBounds().y;

        //jframe_temp mo = new jframe_temp(pares_freq, labelname,posx,posy);
        //mo.setVisible(true);
        //util.Recorre_Arreglo(labelname);
        // util.Recorre_Arreglo_2_dimensiones(pares_freq);

    }//GEN-LAST:event_buttonShowMostFrequentActionPerformed

    
    private int[] getFrequencySortedLabels(int n){
        
        int nLabels = dataset.getNumLabels();
        
        int [] freqSorted = new int[n];
        
        for(int i=0; i<n; i++){
            freqSorted[i] = -1;
        }
        
        String labelName;
        for(int i=0; i<n; i++){
            labelName = tableCoOcurrenceLeft.getValueAt(i, 0).toString();
            freqSorted[i] = getLabelIndex(labelName);
        }
        
        //System.out.println("freqSorted: " + Arrays.toString(freqSorted));
                
        return freqSorted;
    }
    
    private int[] getFrequencySortedLabels(){

        return getFrequencySortedLabels(dataset.getNumLabels());
    }
    
    
    private int getLabelIndex(String name){
     
        for(int i=0; i<jTable11.getColumnCount(); i++){
            if(jTable11.getColumnName(i).equals(name)){
                return(i);
            }
        }
        
        return(-1);
    }
    
    private void textMostFrequentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textMostFrequentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textMostFrequentActionPerformed

    private void textMostFrequentKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textMostFrequentKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_textMostFrequentKeyTyped

    private void textMostRelatedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textMostRelatedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textMostRelatedActionPerformed

    private void textMostRelatedKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textMostRelatedKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_textMostRelatedKeyTyped

    private void buttonShowMostRelatedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowMostRelatedActionPerformed

        int n = Integer.parseInt(textMostRelated.getText());
        
        if(n > dataset.getNumLabels()){
            JOptionPane.showMessageDialog(null, "The number of labels to show must be less than the number of labels in the dataset.", "alert", JOptionPane.ERROR_MESSAGE); 
            return;
        }
        else if(n < 2){
            JOptionPane.showMessageDialog(null, "Select at least 2 labels.", "alert", JOptionPane.ERROR_MESSAGE); 
            return;
        }
        
        if(lista_pares== null) 
        {
            JOptionPane.showMessageDialog(null, "You must load a dataset.", "alert", JOptionPane.ERROR_MESSAGE); 
            return;
        }

        ArrayList<String> seleccionados= new  ArrayList();
        
        seleccionados = selectTopCoocurrenceLabels(n, true);      

        ArrayList<pares_atributos> pares_seleccionados =  util.Encuentra_pares_attr_seleccionados(lista_pares, seleccionados);

        String[] labelname=util.pasa_valores_al_arreglo(seleccionados);//solo cambia el tipo de estructura de datos.

        graphComponent  =  Create_jgraphx(panelCoOcurrenceRight,pares_seleccionados,labelname,graphComponent);

        //LANZAR LA VENTANA EMERGENTE CON LOS PARE SELECCIONADOS

        double[][] pares_freq= util.Get_pares_seleccionados(labelname, pares_seleccionados, num_instancias);

        int posx = this.getBounds().x;
        int posy = this.getBounds().y;

        //jframe_temp mo = new jframe_temp(pares_freq, labelname,posx,posy);
        //mo.setVisible(true);
    }//GEN-LAST:event_buttonShowMostRelatedActionPerformed

    private void radioNoSplitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioNoSplitActionPerformed
        // TODO add your handling code here:
        textRandomHoldout.setEnabled(false);
        textIterativeStratifiedHoldout.setEnabled(false);
        textLPStratifiedHoldout.setEnabled(false);
        //buttonChooseSuppliedTest.setEnabled(false);
        textRandomCV.setEnabled(false);
        textIterativeStratifiedCV.setEnabled(false);
        textLPStratifiedCV.setEnabled(false);

        jButtonSaveDatasets.setEnabled(false);
        jComboBox_SaveFormat.setEnabled(false);
    }//GEN-LAST:event_radioNoSplitActionPerformed

    private void radioNoFSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioNoFSActionPerformed
        // TODO add your handling code here:
        textBRFS.setEnabled(false);
        labelBRFS_Comb.setEnabled(false);
        jComboBox_BRFS_Comb.setEnabled(false);
        labelBRFS_Norm.setEnabled(false);
        jComboBox_BRFS_Norm.setEnabled(false);
        labelBRFS_Out.setEnabled(false);
        jComboBox_BRFS_Out.setEnabled(false);
        textRandomFS.setEnabled(false);

        jButtonSaveDatasets.setEnabled(false);
        jComboBox_SaveFormat.setEnabled(false);
    }//GEN-LAST:event_radioNoFSActionPerformed

    private void jComboBox_SaveFormatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_SaveFormatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox_SaveFormatActionPerformed

    private void buttonShowHeatMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowHeatMapActionPerformed

        showHeatMap();
    }//GEN-LAST:event_buttonShowHeatMapActionPerformed

    private void textMostRelatedHeatMapKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textMostRelatedHeatMapKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_textMostRelatedHeatMapKeyTyped

    private void textMostRelatedHeatMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textMostRelatedHeatMapActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textMostRelatedHeatMapActionPerformed

    private void buttonShowMostRelatedHeatMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowMostRelatedHeatMapActionPerformed
        // TODO add your handling code here:
        int n = Integer.parseInt(textMostRelatedHeatMap.getText());
        showMostRelatedHeatMap(n);
    }//GEN-LAST:event_buttonShowMostRelatedHeatMapActionPerformed

    private void textMostFrequentHeatMapKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textMostFrequentHeatMapKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_textMostFrequentHeatMapKeyTyped

    private void textMostFrequentHeatMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textMostFrequentHeatMapActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textMostFrequentHeatMapActionPerformed

    private void buttonShowMostFrequentHeatMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowMostFrequentHeatMapActionPerformed
        // TODO add your handling code here:

        int n = Integer.parseInt(textMostFrequentHeatMap.getText());
        showMostFrequentsHeatMap(n);
    }//GEN-LAST:event_buttonShowMostFrequentHeatMapActionPerformed

    private void jButtonStartPreprocessMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonStartPreprocessMouseEntered

    }//GEN-LAST:event_jButtonStartPreprocessMouseEntered

    private void tableImbalanceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableImbalanceMouseClicked
        // evento cuando se da un click en el jtable de la pestaña class imbalance

        for(int i=0; i<tabsImbalance.getTabCount(); i++){
            System.out.println("i: " + i + " - " + tabsImbalance.getTitleAt(i));
        }

        //#Examples per labelset
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

    private void comboBoxLabelsInformationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxLabelsInformationActionPerformed
        // TODO add your handling code here:
        
        /*
        comboBoxLabelsInformation:
            0) Label frequency
            1) Labelset frequency
            2) #Labels per example
            3) Box diagram
            4) IR per label inter class
            5) #Labels/IR per label inter class
            6) IR per label intra class
            7) #Labels/IR per label intra class
            8) IR per labelset
            9) -Metrics-
        
        tabbedPane:
            0) Label frequency
            1) Labelset frequency
            2) #Labels per example
            3) #Labels/IR per label intra class
            4) IR per label intra class
            5) IR per labelset
            6) -Metrics-
            7) Box diagram
            8) IR per label inter class
            9) #Labels/IR per label inter class
        */
        
        if(comboBoxLabelsInformation.getSelectedIndex() == 0){
                tabsImbalance.setSelectedIndex(0);
        }
        else if(comboBoxLabelsInformation.getSelectedIndex() == 1){
                tabsImbalance.setSelectedIndex(1);
        }
        else if(comboBoxLabelsInformation.getSelectedIndex() == 2){
                tabsImbalance.setSelectedIndex(2);
        }
        else if(comboBoxLabelsInformation.getSelectedIndex() == 3){
                tabsImbalance.setSelectedIndex(7);
        }
        else if(comboBoxLabelsInformation.getSelectedIndex() == 4){
                tabsImbalance.setSelectedIndex(8);
        }
        else if(comboBoxLabelsInformation.getSelectedIndex() == 5){
                tabsImbalance.setSelectedIndex(9);
        }
        else if(comboBoxLabelsInformation.getSelectedIndex() == 6){
                tabsImbalance.setSelectedIndex(4);
        }
        else if(comboBoxLabelsInformation.getSelectedIndex() == 7){
                tabsImbalance.setSelectedIndex(3);
        }
        else if(comboBoxLabelsInformation.getSelectedIndex() == 8){
                tabsImbalance.setSelectedIndex(5);
        }
        else if(comboBoxLabelsInformation.getSelectedIndex() == 9){
                tabsImbalance.setSelectedIndex(6);
        }
    }//GEN-LAST:event_comboBoxLabelsInformationActionPerformed

    private void tabsImbalanceStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabsImbalanceStateChanged
        // jtabbpane de la pestaña class imbalance
        if(tm_BR1 !=null && tm_LP1!=null){

            System.out.println(" id "+ tabsImbalance.getSelectedIndex());

            if(tabsImbalance.getSelectedIndex()==1)
            {
                tableImbalance.setModel(tm_LP1);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Labelset frequency"));

                //jPanel15.setVisible(false);
                radioExamplesPerLabel.setVisible(false);
                radioExamplesPerLabelset.setVisible(false);

                //labelIR1.setVisible(false);
                //labelIR2.setVisible(false);
            }

            else if (tabsImbalance.getSelectedIndex()==5)
            {
                tableImbalance.setModel(tm_ir_per_labelset);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Labelset id per IR"));

                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_IR(1));
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();

                //jPanel15.setVisible(false);
                radioExamplesPerLabel.setVisible(false);
                radioExamplesPerLabelset.setVisible(false);

                //labelIR1.setVisible(true);
                //labelIR2.setVisible(true);
            }
            else if (tabsImbalance.getSelectedIndex()==0)
            {
                tableImbalance.setModel(tm_BR1);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Label frequency"));

                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_default());
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();

                //jPanel15.setVisible(false);
                radioExamplesPerLabel.setVisible(false);
                radioExamplesPerLabelset.setVisible(false);

                //labelIR1.setVisible(false);
                //labelIR2.setVisible(false);

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
                //jPanel15.setVisible(false);//es el panel del los botones del diagram box
                radioExamplesPerLabel.setVisible(false);
                radioExamplesPerLabelset.setVisible(false);

                //labelIR1.setVisible(true);
                //labelIR2.setVisible(true);
            }
            else if (tabsImbalance.getSelectedIndex()==3)
            {
                tableImbalance.setModel(tm_ir_per_label_intra_class);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Number of labels intra class per IR"));

                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_IR(2));
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();

                //jPanel15.setVisible(false);
                radioExamplesPerLabel.setVisible(false);
                radioExamplesPerLabelset.setVisible(false);

                //labelIR1.setVisible(true);
                //labelIR2.setVisible(true);

            }
            else if (tabsImbalance.getSelectedIndex()==2)
            {
                tableImbalance.setModel(tm_labelxExamples);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Labels per example"));

                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_default());
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();

                //jPanel15.setVisible(false);
                radioExamplesPerLabel.setVisible(false);
                radioExamplesPerLabelset.setVisible(false);

                //labelIR1.setVisible(false);
                //labelIR2.setVisible(false);
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

                //jPanel15.setVisible(false);
                radioExamplesPerLabel.setVisible(false);
                radioExamplesPerLabelset.setVisible(false);

                //labelIR1.setVisible(true);
                //labelIR2.setVisible(true);

            }

            else if (tabsImbalance.getSelectedIndex()==9)
            {
                tableImbalance.setModel(tm_ir_per_label_inter_class);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Number of labels inter class per IR"));

                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_IR(2));
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();

                //jPanel15.setVisible(false);
                radioExamplesPerLabel.setVisible(false);
                radioExamplesPerLabelset.setVisible(false);

                //labelIR1.setVisible(true);
                //labelIR2.setVisible(true);

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

                //jPanel15.setVisible(false);
                radioExamplesPerLabel.setVisible(false);
                radioExamplesPerLabelset.setVisible(false);

                //labelIR1.setVisible(true);
                //labelIR2.setVisible(true);

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
                //jPanel15.setVisible(true);
                radioExamplesPerLabel.setVisible(true);
                radioExamplesPerLabelset.setVisible(true);

                //labelIR1.setVisible(false);
                //labelIR2.setVisible(false);

            }

            else
            {
                tableImbalance.setModel(tm_labelxExamples);
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Labels per example"));

                tableImbalance.setDefaultRenderer(Object.class, new Mi_Render_default());
                panelImbalanceLeft.repaint();
                panelImbalanceLeft.validate();

                //jPanel15.setVisible(false);
                radioExamplesPerLabel.setVisible(false);
                radioExamplesPerLabelset.setVisible(false);

                //labelIR1.setVisible(false);
                //labelIR2.setVisible(false);

            }

            tableImbalance.repaint();
            tableImbalance.validate();
            //  System.out.println("SE HA PRESIONADO el "+jTabbedPane2.getSelectedIndex() );
        }
    }//GEN-LAST:event_tabsImbalanceStateChanged

    private void radioRandomISActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioRandomISActionPerformed
        // TODO add your handling code here:
        textRandomIS.setEnabled(true);

        jButtonSaveDatasets.setEnabled(false);
        jComboBox_SaveFormat.setEnabled(false);
    }//GEN-LAST:event_radioRandomISActionPerformed

    private void textRandomISActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textRandomISActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textRandomISActionPerformed

    private void textRandomISKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textRandomISKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_textRandomISKeyTyped

    private void radioNoISActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioNoISActionPerformed
        // TODO add your handling code here:
        textRandomIS.setEnabled(false);

        jButtonSaveDatasets.setEnabled(false);
        jComboBox_SaveFormat.setEnabled(false);
    }//GEN-LAST:event_radioNoISActionPerformed

    private void showHeatMap(){
        if(lista_pares== null) 
        {
            JOptionPane.showMessageDialog(null, "You must load a dataset.", "alert", JOptionPane.ERROR_MESSAGE); 
            return;
        }

        ArrayList<String> seleccionados= new  ArrayList();
        Vector<Integer> selectedIndex = new Vector<Integer>();

        int[] selecteds=tableHeatmapLeft.getSelectedRows();

        if(selecteds.length<= 1) {
            JOptionPane.showMessageDialog(null, "You must choose two or more labels.", "alert", JOptionPane.ERROR_MESSAGE); 
            return;
        }
        

        
        for(int i=0;i<selecteds.length; i++)
        {
            seleccionados.add((tableHeatmapLeft.getValueAt(selecteds[i], 0).toString()));
            selectedIndex.add(getLabelIndex((tableHeatmapLeft.getValueAt(selecteds[i], 0).toString())));
           // selectedIndex.add(selecteds[i]);
            //System.out.println(dataset.getLabelNames()[i]);
            //selectedIndex.add(getLabelIndex(tableHeatmapLeft.getValueAt(selecteds[i], 0).toString()));
        }
          
        Collections.sort(selectedIndex);
        System.out.println("Show Heatmap selected: " + Arrays.toString(selectedIndex.toArray()));
        
        double [][] newCoeffs = new double[selectedIndex.size()][selectedIndex.size()];

        
        for(int i=0; i<selectedIndex.size(); i++){
            for(int j=0; j<selectedIndex.size(); j++){
                newCoeffs[i][j] = heatmap_coefficients[selectedIndex.get(i)][selectedIndex.get(j)];
            }
        }
        
        heatMap = Create_heatmap_graph(panelHeatmap, newCoeffs, null, heatMap);
    }
    
    private void showMostFrequentsHeatMap(int n){
        if(lista_pares== null) 
        {
            JOptionPane.showMessageDialog(null, "You must load a dataset.", "alert", JOptionPane.ERROR_MESSAGE); 
            return;
        }
        
         if(n <= 1) {
            JOptionPane.showMessageDialog(null, "You must choose two or more labels.", "alert", JOptionPane.ERROR_MESSAGE); 
            return;
        }
        else if (n > dataset.getNumLabels()){
            JOptionPane.showMessageDialog(null, "The number of labels to show must be less than the number of labels in the dataset.", "alert", JOptionPane.ERROR_MESSAGE); 
            return;
        }
        
        tableHeatmapLeft.setRowSelectionInterval(0, n-1);
        
        ArrayList<String> seleccionados= new  ArrayList();
        
        String current = new String();
        for(int i=0;i<n; i++)
        {
            current = (tableHeatmapLeft.getValueAt(i, 0).toString());
            if(current != null){
                seleccionados.add(current);
            }
            else break;
        }

        int [] selectedIndex = new int[n];
        
        for(int i=0; i<n; i++){
            for(int j=0; j<tableHeatmapLeft.getRowCount(); j++){
                if(tableHeatmapLeft.getValueAt(j, 0).equals(seleccionados.get(i))){
                    selectedIndex[i] = getLabelIndex(tableHeatmapLeft.getValueAt(j, 0).toString());
                }
            }
        }
        
        Arrays.sort(selectedIndex);
        
        System.out.println("HEatmap selectedindex: " + Arrays.toString(selectedIndex));
          
        double [][] newCoeffs = new double[n][n];

        
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                newCoeffs[i][j] = heatmap_coefficients[selectedIndex[i]][selectedIndex[j]];
            }
        }
        
        heatMap = Create_heatmap_graph(panelHeatmap, newCoeffs, null, heatMap);
    }
    
    private void showMostRelatedHeatMap(int n){
        selectTopHeatmapLabels(n ,true);
        
        if(lista_pares== null) 
        {
            JOptionPane.showMessageDialog(null, "You must load a dataset.", "alert", JOptionPane.ERROR_MESSAGE); 
            return;
        }
        
        if(n <= 1) {
            JOptionPane.showMessageDialog(null, "You must choose two or more labels.", "alert", JOptionPane.ERROR_MESSAGE); 
            return;
        }
        else if (n > dataset.getNumLabels()){
            JOptionPane.showMessageDialog(null, "The number of labels to show must be less than the number of labels in the dataset.", "alert", JOptionPane.ERROR_MESSAGE); 
            return;
        }

        Vector<Integer> selectedIndex = new Vector<Integer>();
        //tableHeatmapLeft.setRowSelectionInterval(0, n-1);
        int[] selecteds = getTopRelatedHeatmap(n);
        Arrays.sort(selecteds);
        //tableHeatmapLeft.setRowSelectionInterval(selecteds[0], selecteds[n-1]);
        System.out.println("selecteds: " + Arrays.toString(selecteds));

        
        for(int i=0;i<selecteds.length; i++)
        {
            selectedIndex.add(selecteds[i]);
        }
          
        double [][] newCoeffs = new double[selectedIndex.size()][selectedIndex.size()];

        
        for(int i=0; i<selectedIndex.size(); i++){
            for(int j=0; j<selectedIndex.size(); j++){
                newCoeffs[i][j] = heatmap_coefficients[selectedIndex.get(i)][selectedIndex.get(j)];
            }
        }
        
        heatMap = Create_heatmap_graph(panelHeatmap, newCoeffs, null, heatMap);
    }
    
    private void initializeTableMetrics(){
        ArrayList<String> metricsList = util.Get_all_metrics();
        
        tableMetrics.clear();
        
        for(int i=0; i<metricsList.size(); i++){
            tableMetrics.put(metricsList.get(i), "-");
        }
    }
    
    private void initializeTableMetricsMulti(String dataName){
        ArrayList<String> metricsList = util.Get_metrics_multi();
        
        tableMetricsMulti.get(dataName).clear();
        
        for(int i=0; i<metricsList.size(); i++){
            tableMetricsMulti.get(dataName).put(metricsList.get(i), "-");
        }
    }
    
    private void initializeTableMetricsCommon(){
        ArrayList<String> metricsList = util.Get_common_metrics();
        
        tableMetrics_common.clear();
        
        for(int i=0; i<metricsList.size(); i++){
            tableMetrics_common.put(metricsList.get(i), "-");
        }
    }
    
    private void initializeTableMetricsTrainTest(){
        ArrayList<String> metricsList = util.Get_test_metrics();
        
        tableMetrics_train.clear();
        tableMetrics_test.clear();
        
        for(int i=0; i<metricsList.size(); i++){
            tableMetrics_train.put(metricsList.get(i), "-");
            tableMetrics_test.put(metricsList.get(i), "-");
        }
    }
    
       private void save_as_ActionPerformed1(java.awt.event.ActionEvent evt) throws AWTException, IOException
    {
        BufferedImage image = new Robot().createScreenCapture(new Rectangle(panelHeatmap.getLocationOnScreen().x, panelHeatmap.getLocationOnScreen().y, panelHeatmap.getWidth(), panelHeatmap.getHeight()));

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
                
            JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
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
                
                JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
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
        //jpanel.setPreferredSize(new Dimension(584, 399));//POR FIN!!
        jpanel.setPreferredSize(new Dimension(550, 425));
        jpanel.add(graphComponent,BorderLayout.CENTER);
        
        jpanel.validate();
        jpanel.repaint();
        
   
        return graphComponent;
        
 
    }
    
    
    private HeatMap Create_heatmap_graph(JPanel jpanel, double [][] coefficients, ArrayList<pares_atributos> mi_lista, HeatMap old_heatmap)
    {
        Color [] colors = new Color[256];
        
        for(int i=0; i<colors.length; i++){
            colors[i] = new Color(i, i, i);
        }
        
        
        HeatMap heatMap = null;
        
        double [][] newCoefs = coefficients.clone();
        
        for(int i=0; i<newCoefs.length; i++){
            for(int j=0; j<newCoefs.length; j++){
                if(newCoefs[i][j] < 0){
                    newCoefs[i][j] = 0;
                }
            }
        }
        
        if((mi_lista != null) && (mi_lista.size() > 0)){
            HashSet<Integer> selected = new HashSet<Integer>();
            
            for(int i=0; i<mi_lista.size(); i++){
                selected.add(mi_lista.get(i).get_ind_att1());
                selected.add(mi_lista.get(i).get_ind_att2());
            }
            
            newCoefs = new double[selected.size()][selected.size()];
            
            List sortedSelected = new ArrayList(selected);
            Collections.sort(sortedSelected);
            
            for(int i=0; i<sortedSelected.size(); i++){
                for(int j=0; j<sortedSelected.size(); j++){
                    newCoefs[i][j] = coefficients[(int)sortedSelected.get(i)][(int)sortedSelected.get(j)];
                }
            }
            
            System.out.println("NEWCOEFS");
            for(int i=0; i<sortedSelected.size(); i++){
                System.out.println(Arrays.toString(newCoefs[i]));
            }
        }
        
        heatMap = new HeatMap(newCoefs, false, colors);
        
        if(old_heatmap != null){
            jpanel.remove(old_heatmap);
        }
            
        jpanel.setLayout(new BorderLayout());
        //jpanel.setPreferredSize(new Dimension(584, 399));//POR FIN!!
        jpanel.setPreferredSize(new Dimension(550, 425));
        jpanel.add(heatMap,BorderLayout.CENTER);

        jpanel.validate();
        jpanel.repaint();

        return heatMap;
    }
    
    
    
    // UNA VEZ CARGADO EL DATASET, COMIENZA A MOSTRAR LAS PRIMERAS METRICAS SELECCIONADAS EN LA PESTAÑA 
    private void Load_dataset(String filename_database_arff, String filename_database_xml )
    {
        //System.out.println(filename_database_arff);
 
        try {        
             export2.setVisible(true); //boton salvar de la tabla de la izquierda en class imbalance
            
             
             if(tabsDependences.getSelectedIndex()==0)jLabelChiFi_text.setVisible(true);
             else jLabelChiFi_text.setVisible(false);
            
             
             //para el box diagram
             if(tabsImbalance.getSelectedIndex()==7){
                 //jPanel15.setVisible(true);
                radioExamplesPerLabel.setVisible(true);
                radioExamplesPerLabelset.setVisible(true);
             }
             else {
                 //jPanel15.setVisible(false);
                radioExamplesPerLabel.setVisible(false);
                radioExamplesPerLabelset.setVisible(false);
             }
            
             if(tabsImbalance.getSelectedIndex()==3
                     || tabsImbalance.getSelectedIndex()==4
                     || tabsImbalance.getSelectedIndex()==5
                     || tabsImbalance.getSelectedIndex()==6) 
             {
                 //labelIR1.setVisible(true);
                 //labelIR2.setVisible(true);
             }
             else 
             {
                 //labelIR1.setVisible(false);
                 //labelIR2.setVisible(false);
             }
             
             
            //restableciendo valores
            dataset_train= null;
            dataset_test= null;
            filename_database_arff_test=null;//es el encargado de cargar un test
            
             //new Instances
              
            if(filename_database_xml == null){
                MekaToMulan m = new MekaToMulan();
                System.out.println(filename_database_arff);
                m.convertir(filename_database_arff, filename_database_arff+"_mulan");
            
                dataset = new MultiLabelInstances(filename_database_arff+"_mulan.arff", filename_database_arff+"_mulan.xml");
                
                /*File f = new File(filename_database_arff+"_mulan.arff");
                f.delete();*/
                File f2 = new File(filename_database_arff+"_mulan.xml");
                System.out.println("f2: " + f2.getAbsolutePath());
                f2.delete();
            }
            else{
                dataset = new MultiLabelInstances(filename_database_arff, filename_database_xml);
            }
            
              
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
             //jtable_frequency(jTable2,dataset);
                   
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
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Labelset frequency"));
                
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
                panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Label frequency"));
                
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
            
            
            panelChiPhi.repaint();
            panelChiPhi.validate();
           
            
             //tm_coocurrences   
              lista_pares = util.Get_pares_atributos(dataset);   
             
             //tm_coocurrences = jtable_coefficient_values(dataset, lista_pares,"coocurrence");
            // jTable11.setModel(tm_coocurrences);
              
             jtable_coefficient_values(dataset, lista_pares,"coocurrence");
             jTable11.setDefaultRenderer(Object.class, new MiRender("estandar",Double.MAX_VALUE));
             fixedTable1.setDefaultRenderer(Object.class, new MiRender("chi_fi_fixed",Double.MAX_VALUE));
           
             panelCoOcurrenceValues.repaint();
             panelCoOcurrenceValues.validate();

             //tm heapmap values
             //tm_heapmap_values = jtable_coefficient_values(dataset, lista_pares, "heapmap");
             //jTable12.setModel(tm_heapmap_values);
             
             jtable_coefficient_values(dataset, lista_pares,"heapmap");
             jTable12.setDefaultRenderer(Object.class, new MiRender("estandar",Double.MAX_VALUE));
             fixedTable2.setDefaultRenderer(Object.class, new MiRender("chi_fi_fixed",Double.MAX_VALUE));
             
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
        if(primeros_seleccionados> dataset.getNumLabels()) {
            primeros_seleccionados = dataset.getNumLabels();
        }
         
        String current=null; 
               
        for(int i=0;i<primeros_seleccionados; i++)
        {
            current = (tableCoOcurrenceLeft.getValueAt(i, 0).toString());
            if(current != null){
                seleccionados.add(current);
            }
            else break;
        }
        
        seleccionados = selectTopCoocurrenceLabels(10, true);

       ArrayList<pares_atributos> pares_seleccionados = util.Encuentra_pares_attr_seleccionados(lista_pares, seleccionados);
        
       String[] labelname1=util.pasa_valores_al_arreglo(seleccionados);
       

      graphComponent  =  Create_jgraphx(panelCoOcurrenceRight,pares_seleccionados,labelname1,graphComponent);

            
            // System.out.println("dimensiones del panel jpanel25 "+jPanel25.getBounds().width+ " , "+ jPanel25.getBounds().height);
        //    graphComponent  =  Create_jgraphx(jPanel10,lista_pares,labelname,graphComponent);// balanced
            //create_button_export(jPanel25 ,export6,350,370);


            //System.out.println("dimensiones son "+ jLabel20.getBounds().width+" ancho ,"+ jLabel20.getBounds().height+" alto ");
            
            label_indices_seleccionados = dataset.getLabelIndices();
            //jheat_chart= create_heapmap(labelHeatmapGraph, dataset); // balanced
            //create_heatmap(panelHeatmap);
            heatMap = Create_heatmap_graph(panelHeatmap, getHeatMapCoefficients(), null, heatMap);
            
            
             //util.get_chi_fi_coefficient(dataset);
        
             // jpanel8 box diagram
              cp_box.getChart().getXYPlot().clearAnnotations();
               
              DefaultXYDataset xyseriescollection = new DefaultXYDataset();
              DefaultXYDataset xyseriescollection1 = new DefaultXYDataset();
              
              cp_box.getChart().getXYPlot().setDataset(xyseriescollection);             
              cp_box.getChart().getXYPlot().setDataset(1, xyseriescollection1);
            
              //TRAIN TEST
              jButtonSaveDatasets.setEnabled(false);
              jComboBox_SaveFormat.setEnabled(false);
              //button_calculate2_train.setEnabled(false);
              //button_save_train.setEnabled(false);
             
            }
        catch (InvalidDataFormatException ex) {
            Logger.getLogger(CrossValidationExperiment.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Error: " + ex);
            Logger.getLogger(CrossValidationExperiment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<String> selectTopCoocurrenceLabels(int n, boolean selectInTable){
            
            LabelsPairValue p = new LabelsPairValue();
            
            ArrayList<String> pares = new ArrayList<String>();
            
            ArrayList<LabelsPairValue> pairs = new ArrayList<LabelsPairValue>();
            for(int i=0; i<coocurrence_coefficients.length; i++){
                for(int j=0; j<coocurrence_coefficients.length; j++){
                    if(coocurrence_coefficients[i][j] >= 0){
                        pairs.add(new LabelsPairValue(i, j, coocurrence_coefficients[i][j]));
                    }
                }
            }
            Collections.sort(pairs, Collections.reverseOrder());
            
            System.out.println("COCURRENCE-> " + Arrays.toString(pairs.toArray()));

            int numLabels = n;
            int currentSelectedLabels = 0;

            Vector<Integer> selectedLabels = new Vector<Integer>();

            do{
                if(!selectedLabels.contains(pairs.get(0).label1)){
                    selectedLabels.add(pairs.get(0).label1);
                    System.out.println("Add " + pairs.get(0).label1);
                    currentSelectedLabels++;
                }
                
                if(currentSelectedLabels < numLabels){
                    if(!selectedLabels.contains(pairs.get(0).label2)){
                        selectedLabels.add(pairs.get(0).label2);
                        System.out.println("Add " + pairs.get(0).label2);
                        currentSelectedLabels++;
                    }
                }
                
                pairs.remove(pairs.get(0));
            }while((pairs.size() > 0) && (currentSelectedLabels < numLabels));

            String s = new String();
            
            for(int i=0; i<selectedLabels.size(); i++){
                s = jTable11.getColumnName(selectedLabels.get(i));

                if(s != null){
                    pares.add(s);
                }
            }
            
            if(selectInTable){
                tableCoOcurrenceLeft.clearSelection();
                
                String labelName;
                for(int i=0; i<selectedLabels.size(); i++){
                    //Get label name
                    labelName = dataset.getLabelNames()[selectedLabels.get(i)];
                    for(int r=0; r<tableCoOcurrenceLeft.getRowCount(); r++){
                        if(tableCoOcurrenceLeft.getValueAt(r, 0).equals(labelName)){
                            tableCoOcurrenceLeft.addRowSelectionInterval(r, r);
                        }
                    }
                    //tableCoOcurrenceLeft.addRowSelectionInterval(selectedLabels.get(i), selectedLabels.get(i));
                }
            }

             return pares;
        }
    
    
    public ArrayList<String> selectTopHeatmapLabels(int n, boolean selectInTable){
            
            LabelsPairValue p = new LabelsPairValue();
            
            ArrayList<String> pares = new ArrayList<String>();
            
            ArrayList<LabelsPairValue> pairs = new ArrayList<LabelsPairValue>();
            for(int i=0; i<heatmap_coefficients.length; i++){
                for(int j=0; j<heatmap_coefficients.length; j++){
                    if(heatmap_coefficients[i][j] >= 0){
                        pairs.add(new LabelsPairValue(i, j, heatmap_coefficients[i][j]));
                    }
                }
            }
            Collections.sort(pairs, Collections.reverseOrder());
            
            System.out.println("HEATMAP-> " + Arrays.toString(pairs.toArray()));

            int numLabels = n;
            int currentSelectedLabels = 0;

            Vector<Integer> selectedLabels = new Vector<Integer>();

            do{
                if(!selectedLabels.contains(pairs.get(0).label1)){
                    selectedLabels.add(pairs.get(0).label1);
                    System.out.println("Add " + pairs.get(0).label1);
                    currentSelectedLabels++;
                }
                
                if(currentSelectedLabels < numLabels){
                    if(!selectedLabels.contains(pairs.get(0).label2)){
                        selectedLabels.add(pairs.get(0).label2);
                        System.out.println("Add " + pairs.get(0).label2);
                        currentSelectedLabels++;
                    }
                }
                
                pairs.remove(pairs.get(0));
            }while((pairs.size() > 0) && (currentSelectedLabels < numLabels));

            String s = new String();
            
            for(int i=0; i<selectedLabels.size(); i++){
                s = jTable12.getColumnName(selectedLabels.get(i));

                if(s != null){
                    pares.add(s);
                }
            }
            
            if(selectInTable){
                tableHeatmapLeft.clearSelection();
                
                String labelName;
                for(int i=0; i<selectedLabels.size(); i++){
                    //Get label name
                    labelName = dataset.getLabelNames()[selectedLabels.get(i)];
                    for(int r=0; r<tableHeatmapLeft.getRowCount(); r++){
                        if(tableHeatmapLeft.getValueAt(r, 0).equals(labelName)){
                            tableHeatmapLeft.addRowSelectionInterval(r, r);
                        }
                    }
                    //tableCoOcurrenceLeft.addRowSelectionInterval(selectedLabels.get(i), selectedLabels.get(i));
                }
            }

             return pares;
        }
    
    private void Print_main_metric_dataset(MultiLabelInstances dataset, Statistics stat1 )
    {
            Instances i1= dataset.getDataSet();

                     
            //Relation
            if(dataset_current_name.length() > 30){
                labelRelationValue.setText(dataset_current_name.substring(0, 28) + "...");
            }
            else{
                labelRelationValue.setText(dataset_current_name);
            }
            
            
            //Instances
            String num_instancias = util.get_value_metric("Instances", dataset, es_de_tipo_meka);
            labelInstancesValue.setText(util.getValueFormatted("Instances", num_instancias));
            
            //Attributes
            String num_atributos = util.get_value_metric("Attributes", dataset, es_de_tipo_meka);
            labelAttributesValue.setText(util.getValueFormatted("Attributes", num_atributos));
            
            //Labels
            labelLabelsValue.setText(util.getValueFormatted("Labels", util.get_value_metric("Labels", dataset, es_de_tipo_meka)));
            
            //Density
            String density = util.get_value_metric("Density", dataset, es_de_tipo_meka);
            labelDensityValue.setText(util.getValueFormatted("Density", density));
                      
            //Cardinality
            String cardinality = util.get_value_metric("Cardinality", dataset, es_de_tipo_meka);
            labelCardinalityValue.setText(util.getValueFormatted("Cardinality", cardinality));
            
            //Diversity      
            String diversity = util.get_value_metric("Diversity", dataset, es_de_tipo_meka);
            labelDiversityValue.setText(util.getValueFormatted("Diversity", diversity));
            
            //Bound
            String bound = util.get_value_metric("Bound", dataset, es_de_tipo_meka);
            labelBoundValue.setText(util.getValueFormatted("Bound", bound));
            
            //Distinct labelset     
            String distinct_labelset = util.get_value_metric("Distinct Labelset", dataset, es_de_tipo_meka);
            labelDistinctValue.setText(util.getValueFormatted("Distinct Labelset", distinct_labelset));
            
            //LxIxF
            String LIF = util.get_value_metric("Labels x instances x features", dataset, es_de_tipo_meka);
            labelLxIxFValue.setText(util.getValueFormatted("Labels x instances x features", LIF));
    }
  
    
    private void button_calculateActionPerformed1_general(java.awt.event.ActionEvent evt, JTable jtable, JTable jtable1, JTable jtable2)
    {

    }
    
    
    // CALCULA LAS METRICAS SELECCIONADAS EN LA PESTAÑA TEST
    private void button_calculateActionPerformed1(java.awt.event.ActionEvent evt, JTable jtable, JTable jtable1, JTable jtable2)
    {
       ArrayList<String> metric_list_commun = Get_metrics_selected_principal(jtable);
       ArrayList<String> metric_list_train = Get_metrics_selected_test(jtable2);
       ArrayList<String> metric_list_test = Get_metrics_selected_test(jtable2);
       
       if(dataset == null) {
           JOptionPane.showMessageDialog(null, "You must load a dataset.", "alert", JOptionPane.ERROR_MESSAGE); 
            return; 
       }

       boolean flag=true;
        
       if(!list_dataset_train.isEmpty()){
           flag=false;
       }
       
       
       if((dataset_train ==null || dataset_test == null) && flag ){
            JOptionPane.showMessageDialog(null, "You must click on Start before.", "alert", JOptionPane.ERROR_MESSAGE); 
            return;
       }
       
       
       if((dataset_train ==null || dataset_test == null))
       {
            JOptionPane.showMessageDialog(null, "You must click on Start before.", "alert", JOptionPane.ERROR_MESSAGE); 
            return;
       }
         
       
       String value = new String();
       //Common metrics
        for(String metric : metric_list_commun)
        {
            //If metric value exists, don't calculate
           if((tableMetrics_common.get(metric) == null) || (tableMetrics_common.get(metric).equals("-"))){
               value= util.get_value_metric(metric, dataset_train, es_de_tipo_meka); //can  be dataset_test too
                      
                if(value.equals("-1.000") || value.equals("-1,000")){
                    value = util.get_alternative_metric(metric, dataset_train, dataset_test);
                }

                tableMetrics_common.put(metric, value.replace(",", "."));
           }           
        }
        
        //Train metrics
        atributo[] label_frenquency = util.Get_Frequency_x_label(dataset_train);
        label_frenquency = util.Ordenar_freq_x_attr(label_frenquency);// ordena de mayor a menor
             
        atributo[] imbalanced_data =  util.Get_data_imbalanced_x_label_inter_class(dataset_train,label_frenquency);
        
        for(String metric : metric_list_train)
        {
            //If metric value exists, don't calculate
            if((tableMetrics_train.get(metric) == null) || (tableMetrics_train.get(metric).equals("-"))){
                value= util.get_value_metric(metric, dataset_train, es_de_tipo_meka);
                if(value.equals("-1.000") || value.equals("-1,000")) {
                    value = util.get_value_metric_imbalanced(metric, dataset_train, imbalanced_data);
                }
                if(value.equals("-1.000") || value.equals("-1,000")) {
                    value ="NaN";
                }
                tableMetrics_train.put(metric, value.replace(",", "."));
            }
        }
        
        //Test metrics
        label_frenquency = util.Get_Frequency_x_label(dataset_test);
        label_frenquency = util.Ordenar_freq_x_attr(label_frenquency);// ordena de mayor a menor
        
        imbalanced_data = util.Get_data_imbalanced_x_label_inter_class(dataset_test,label_frenquency);
        
        for(String metric : metric_list_test)
        {
           //If metric value exists, don't calculate
           if((tableMetrics_test.get(metric) == null) || (tableMetrics_test.get(metric).equals("-"))){
               value= util.get_value_metric(metric, dataset_test, es_de_tipo_meka);
                if(value.equals("-1.000") || value.equals("-1,000")) {
                    value = util.get_alternative_metric(metric, dataset_train, dataset_test);
                }
                if(value.equals("-1.000") || value.equals("-1,000")) {
                    value = util.get_value_metric_imbalanced(metric, dataset_test, imbalanced_data);
                }
                if(value.equals("-1.000") || value.equals("-1,000")) {
                    value ="NaN";
                }
                tableMetrics_test.put(metric, value.replace(",", "."));
           }
        }
       
        TableModel model = jtable.getModel();
        
        for(int i=0; i<model.getRowCount(); i++){
            model.setValueAt(tableMetrics_common.get(model.getValueAt(i, 0).toString()), i, 1);
        }
        
        jtable.repaint();
        
        model = jtable2.getModel();
        
        for(int i=0; i<model.getRowCount(); i++){
            model.setValueAt(tableMetrics_train.get(model.getValueAt(i, 0).toString()), i, 1);
            model.setValueAt(tableMetrics_test.get(model.getValueAt(i, 0).toString()), i, 2);
        }
        
        jtable2.repaint();
       
       //metric_output mo = new metric_output(metric_list_commun,metric_list_train,metric_list_test,dataset_train,dataset_test, posx, posy,es_de_tipo_meka);
       //mo.setVisible(true);

       

    } 
    
    
     // CALCULA LAS METRICAS SELECCIONADAS EN LA PESTAÑA TEST PARA CROSS VALIDATION
    private void button_calculateActionPerformed1(java.awt.event.ActionEvent evt, JTable jtable, JTable jtable1, JTable jtable2, ArrayList<MultiLabelInstances> list_datasets_train, ArrayList<MultiLabelInstances> list_datasets_test )
    {       
       ArrayList<String> metric_list_commun = Get_metrics_selected_principal(jtable);
       ArrayList<String> metric_list_train = Get_metrics_selected_test(jtable2);
       ArrayList<String> metric_list_test = Get_metrics_selected_test(jtable2);
       
       if(dataset == null) {
           JOptionPane.showMessageDialog(null, "You must load a dataset.", "alert", JOptionPane.ERROR_MESSAGE); 
           return; 
       }
       
       
       boolean flag=true;
       
       String temp,temp1,value="";
       
       ArrayList<Double> lista_valores;
       double current_double;
       
       MultiLabelInstances current;
        
        for(String metric : metric_list_commun)
        {
            //If metric value exists, don't calculate
           if((tableMetrics_common.get(metric) == null) || (tableMetrics_common.get(metric).equals("-"))){
               value= metric+ util.get_tabs_multi_datasets(metric);

                lista_valores= new ArrayList();
                current_double=-1.0;

                for(int n=0; n<list_datasets_train.size();n++)
                {
                     temp = util.get_value_metric(metric, list_datasets_train.get(n), es_de_tipo_meka); //can  be dataset_test too

                     if(temp.equals( "-1.000" ) || temp.equals( "-1,000" )) {
                         temp = util.get_alternative_metric(metric, list_datasets_train.get(n), list_datasets_test.get(n));
                     }

                     //System.out.println(temp);
                     temp=temp.replace(",", ".");
                     //System.out.println(temp);
                     current_double = Double.parseDouble(temp);

                     if(Double.isNaN(current_double) || current_double==-1.0){
                         break;
                     }

                     lista_valores.add(current_double);

                }    
                tableMetrics_common.put(metric, Double.toString(util.Get_media(lista_valores)).replace(",", "."));     
           }         
        }
        
        
        
        atributo[] imbalanced_data,imbalanced_data1, label_frequency,label_frequency1;
        flag =true;
        

        
        //Train metrics
        for(String metric : metric_list_train)
        {
            //If metric value exists, don't calculate
           if((tableMetrics_train.get(metric) == null) || (tableMetrics_train.get(metric).equals("-"))){
               value= metric+ util.get_tabs_multi_datasets(metric);
            
                lista_valores= new ArrayList();
                current_double=-1.0;

                for(int n=0; n<list_datasets_train.size();n++)
                {
                    current = list_datasets_train.get(n);

                    label_frequency = util.Get_Frequency_x_label(current);
                    label_frequency = util.Ordenar_freq_x_attr(label_frequency);// ordena de mayor a menor

                    imbalanced_data = util.Get_data_imbalanced_x_label_inter_class(current,label_frequency);

                     temp= util.get_value_metric(metric, list_datasets_train.get(n), es_de_tipo_meka); 
                     System.out.println(temp);              

                    if(temp.equals( "-1.0" )){
                        temp = util.get_value_metric_imbalanced(metric, list_datasets_train.get(n), imbalanced_data);
                        System.out.println(temp);
                    }
                    if(temp.equals( "-1.0" )) {
                        temp ="NaN";
                        System.out.println(temp);
                    }

                    temp=temp.replace(",", ".");
                    current_double = Double.parseDouble(temp);

                    if(Double.isNaN(current_double) || current_double==-1.0){
                        break;
                    }

                    lista_valores.add(current_double);

                }         

                tableMetrics_train.put(metric, Double.toString(util.Get_media(lista_valores)).replace(",", "."));
           }
        }
        
        
        //Test metrics
        for(String metric : metric_list_test)
        {
            //If metric value exists, don't calculate
           if((tableMetrics_test.get(metric) == null) || (tableMetrics_test.get(metric).equals("-"))){
               value= metric+ util.get_tabs_multi_datasets(metric);
            
                lista_valores= new ArrayList();
                current_double=-1.0;

                for(int n=0; n<list_datasets_test.size();n++)
                {
                    current = list_datasets_test.get(n);

                    label_frequency = util.Get_Frequency_x_label(current);
                    label_frequency = util.Ordenar_freq_x_attr(label_frequency);// ordena de mayor a menor

                    imbalanced_data = util.Get_data_imbalanced_x_label_inter_class(current,label_frequency);

                     temp= util.get_value_metric(metric, list_datasets_test.get(n), es_de_tipo_meka); 

                    if(temp.equals( "-1.0" )){
                        temp = util.get_value_metric_imbalanced(metric, list_datasets_test.get(n), imbalanced_data);
                    }
                    if(temp.equals( "-1.0" )) {
                        temp ="NaN";
                    }

                    temp=temp.replace(",", ".");
                    current_double = Double.parseDouble(temp);

                    if(Double.isNaN(current_double) || current_double==-1.0){
                        break;
                    }

                    lista_valores.add(current_double);

                }         

                tableMetrics_test.put(metric, Double.toString(util.Get_media(lista_valores)).replace(",", "."));
           }

        }
        
        
        TableModel model = jtable.getModel();
        
        for(int i=0; i<model.getRowCount(); i++){
            model.setValueAt(tableMetrics_common.get(model.getValueAt(i, 0).toString()), i, 1);
        }
        
        jtable.repaint();
        
        model = jtable2.getModel();
        
        for(int i=0; i<model.getRowCount(); i++){
            model.setValueAt(tableMetrics_train.get(model.getValueAt(i, 0).toString()), i, 1);
            model.setValueAt(tableMetrics_test.get(model.getValueAt(i, 0).toString()), i, 2);
        }
        
        jtable2.repaint();
       
    }   
    
    //SALVAR EN LA PESTAÑA TRAIN/TEST DATASET
     private void button_saveActionPerformed1(java.awt.event.ActionEvent evt, JTable jtable, JTable jtable1, JTable jtable2) throws IOException
    {
       ArrayList<String> metric_list_commun = Get_metrics_selected_principal(jtable);
       ArrayList<String> metric_list_train = Get_metrics_selected_test(jtable2);
       ArrayList<String> metric_list_test = Get_metrics_selected_test(jtable2);
      
       String dataset_name11;
      
       if(dataset == null ) {
           JOptionPane.showMessageDialog(null, "You must load a dataset.", "alert", JOptionPane.ERROR_MESSAGE); 
           return; }
       
       if((dataset_train ==null || dataset_test == null )&& list_dataset_train.isEmpty()){
            JOptionPane.showMessageDialog(null, "You must click on Start before.", "alert", JOptionPane.ERROR_MESSAGE); 
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
                
                if(radioRandomCV.isSelected() || radioIterativeStratifiedCV.isSelected())//CV STRATIFIED AND RAMDON
                {
                    //System.out.println("se va a guardar los txt");
                    util.Save_in_the_file_txt(wr,metric_list_commun,metric_list_train,metric_list_test,list_dataset_train,list_dataset_test, es_de_tipo_meka);
                }
               // util.Save_in_the_file(wr, metric_list, dataset, stat1);
                else{
                util.Save_in_the_file(wr, metric_list_commun, metric_list_train, metric_list_test, dataset_train, dataset_test, es_de_tipo_meka);
                }
                
                JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                wr.close();
                bw.close();      
                }
              
              else if(f1.getDescription().equals(".csv"))
               {
                
                  if(radioRandomCV.isSelected() || radioIterativeStratifiedCV.isSelected())//CV STRATIFIED AND RAMDON
                    {
                      try
                        {
                            dataset_name11 = dataset_name1.substring(0,dataset_name1.length()-5);
                            path = file.getAbsolutePath() +".csv";
                            
                            Exporter exp = new Exporter(new File(path), metric_list_commun,metric_list_train,metric_list_test);
                     
                             if(exp.exporta(list_dataset_train,list_dataset_test,dataset_name11,es_de_tipo_meka))
                                 {
                                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                                 }
                        }

                      catch(Exception e1)
                        {
                            System.out.println("Error: " +  e1.getMessage());
                            JOptionPane.showMessageDialog(null, "File not saved correctly.", "Error", JOptionPane.ERROR_MESSAGE); 
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
                                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                                 }
                        }

                      catch(Exception e1)
                        {
                            System.out.println("este es el mensaje "+e1.getMessage());
                            JOptionPane.showMessageDialog(null, "File not saved correctly.", "Error", JOptionPane.ERROR_MESSAGE); 
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
                    
                    if(radioRandomCV.isSelected() || radioIterativeStratifiedCV.isSelected())//CV STRATIFIED AND RAMDON
                    {
                         util.Save_in_the_file_arff_meka(wr, metric_list_all, list_dataset_train,list_dataset_test,file.getName(),es_de_tipo_meka);
                    }
                    else
                    {
                        util.Save_in_the_file_arff_meka(wr, metric_list_all, dataset_train, dataset_test, dataset_name1, es_de_tipo_meka);
                    }
                                   
                    
                    
                    wr.close();
                    bw.close();      
                    
                   JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                   
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
                    
                    if(radioRandomCV.isSelected() || radioIterativeStratifiedCV.isSelected())//CV STRATIFIED AND RAMDON
                    {
                         util.Save_in_the_file_arff_mulan(wr, metric_list_all, list_dataset_train,list_dataset_test,file.getName(), es_de_tipo_meka);
                    }
                    else
                    {
                        util.Save_in_the_file_arff_mulan(wr, metric_list_all, dataset_train, dataset_test, dataset_name1, es_de_tipo_meka);
                    }
                                   
                    
                    
                    wr.close();
                    bw.close();      
                    
                   JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                   
                }
                
        } 
    }
     
     //BOTON GRAPHIC EN LA PESTAÑA MULTIDATASET
     private void button_graphicActionPerformed_multidatasets(java.awt.event.ActionEvent evt)throws IOException
     {
        //PieChart demo = new PieChart("Comparison", "Which operating system are you using?");
         
           
       if(list_dataset.isEmpty()) {
           JOptionPane.showMessageDialog(null, "You must load a dataset.", "Warning", JOptionPane.ERROR_MESSAGE);
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
           JOptionPane.showMessageDialog(null, "You must load a dataset.", "Warning", JOptionPane.ERROR_MESSAGE);
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
                
           JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE);
           Toolkit.getDefaultToolkit().beep();
                
        } 

        
    }
             
             
             
     //BOTON SALVAR EN LA PESTAÑA DATABASE
     private void button_saveActionPerformed(java.awt.event.ActionEvent evt, JTable jtable) throws IOException
    {
        ArrayList<String> metric_list = Get_metrics_selected(jtable);
        
        
         if(dataset == null) {
           JOptionPane.showMessageDialog(null, "You must load a dataset.", "Warning", JOptionPane.ERROR_MESSAGE);
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
                    
                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                    
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
                         JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                     }
                 }
                 catch(Exception e1)
                 {
                     JOptionPane.showMessageDialog(null, "File not saved correctly.", "Error", JOptionPane.ERROR_MESSAGE); 
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
                    
                  JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE);     
                }
                
           Toolkit.getDefaultToolkit().beep();
        } 

            
        
    }
     
     
      private void button_saveActionPerformed_principal(java.awt.event.ActionEvent evt, JTable jtable) throws IOException
    {
        ArrayList<String> metric_list = Get_metrics_selected_principal(jtable);
        
        
         if(dataset == null) {
           JOptionPane.showMessageDialog(null, "You must load a dataset.", "Warning", JOptionPane.ERROR_MESSAGE);
           return; 
       }
             
        
       // JFILECHOOSER SAVE
         JFileChooser fc= new JFileChooser();
        
         // extension txt
        FileNameExtensionFilter fname = new FileNameExtensionFilter(".txt", "txt");
        FileNameExtensionFilter fname2 = new FileNameExtensionFilter(".csv", "csv");
        FileNameExtensionFilter fname3 = new FileNameExtensionFilter(".arff (Meka)", ".arff (Meka)");
        FileNameExtensionFilter fname4 = new FileNameExtensionFilter(".tex", ".tex");
        
        //eliminar el que tiene por defecto
        fc.removeChoosableFileFilter(fc.getChoosableFileFilters()[0]);

        fc.addChoosableFileFilter(fname);
        fc.addChoosableFileFilter(fname2);
        fc.addChoosableFileFilter(fname3);
        fc.addChoosableFileFilter(fname4);
        
        fc.setFileFilter(fname);

        
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
                
                    util.Save_text_file(wr, metric_list, dataset, label_imbalanced, es_de_tipo_meka, tableMetrics);
                
                    wr.close();
                    bw.close(); 
                    
                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                    
                }
                
                else if(f1.getDescription().equals(".tex"))
                {
                    String path = file.getAbsolutePath() +".tex";
                
                    BufferedWriter bw = new BufferedWriter(new FileWriter(path));
                    PrintWriter wr = new PrintWriter(bw);
                
                    util.Save_tex_file(wr, metric_list, dataset, label_imbalanced, es_de_tipo_meka, tableMetrics);
                
                    wr.close();
                    bw.close(); 
                    
                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                    
                }
                
               else if(f1.getDescription().equals(".csv"))
               {
              
                   String path = file.getAbsolutePath() +".csv";
                
                    BufferedWriter bw = new BufferedWriter(new FileWriter(path));
                    PrintWriter wr = new PrintWriter(bw);
                
                    util.Save_csv_file(wr, metric_list, dataset, label_imbalanced, es_de_tipo_meka, tableMetrics);
                
                    wr.close();
                    bw.close(); 
                    
                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                /*
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
                   */
                
               }
                
               
               else if (f1.getDescription().equals(".arff (Meka)"))
                {
                    String path = file.getAbsolutePath() +".arff";
                
                    BufferedWriter bw = new BufferedWriter(new FileWriter(path));
                    PrintWriter wr = new PrintWriter(bw);
                
                    util.Save_meka_file(wr, metric_list, dataset, label_imbalanced, es_de_tipo_meka, tableMetrics);
                
                    wr.close();
                    bw.close(); 
                    
                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                    
                    /*
                    String path = file.getAbsolutePath() +".arff";
                
                    BufferedWriter bw = new BufferedWriter(new FileWriter(path));
                    PrintWriter wr = new PrintWriter(bw);
                    
                    ArrayList<MultiLabelInstances> list_dataset1 = new ArrayList();
                    list_dataset1.add(dataset);
                                   
                    util.Save_in_the_file_arff_meka(wr, metric_list, list_dataset1,file.getName(), es_de_tipo_meka);
                    
                    wr.close();
                    bw.close();      
                    
                  JOptionPane.showMessageDialog(null, "File Saved", "Successful", JOptionPane.INFORMATION_MESSAGE);   
                    */
                }
                
           Toolkit.getDefaultToolkit().beep();
        } 

            
        
    }
      
      
      
      
      private void button_saveActionPerformed_multi(java.awt.event.ActionEvent evt, JTable jtable) throws IOException
    {
        ArrayList<String> metric_list = Get_metrics_selected_multi(jtable);
        
        
         if(list_dataset == null || list_dataset.isEmpty() || Dataset_names.isEmpty()) {
           JOptionPane.showMessageDialog(null, "You must load a dataset.", "Warning", JOptionPane.ERROR_MESSAGE);
           return; 
       }
             
        
       // JFILECHOOSER SAVE
         JFileChooser fc= new JFileChooser();
        
         // extension txt
        FileNameExtensionFilter fname = new FileNameExtensionFilter(".txt", "txt");
        FileNameExtensionFilter fname2 = new FileNameExtensionFilter(".csv", "csv");
        FileNameExtensionFilter fname3 = new FileNameExtensionFilter(".arff (Meka)", ".arff (Meka)");
        FileNameExtensionFilter fname4 = new FileNameExtensionFilter(".tex", ".tex");
        
        //eliminar el que tiene por defecto
        fc.removeChoosableFileFilter(fc.getChoosableFileFilters()[0]);

        fc.addChoosableFileFilter(fname);
        fc.addChoosableFileFilter(fname2);
        fc.addChoosableFileFilter(fname3);
        fc.addChoosableFileFilter(fname4);
        
        fc.setFileFilter(fname);

        
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
                
                    util.Save_text_file_multi(wr, metric_list, Dataset_names, tableMetricsMulti);
                
                    wr.close();
                    bw.close(); 
                    
                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                    
                }
                
                else if(f1.getDescription().equals(".tex"))
                {
                    String path = file.getAbsolutePath() +".tex";
                
                    BufferedWriter bw = new BufferedWriter(new FileWriter(path));
                    PrintWriter wr = new PrintWriter(bw);
                
                    util.Save_tex_file_multi(wr, metric_list, Dataset_names, tableMetricsMulti);
                
                    wr.close();
                    bw.close(); 
                    
                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                    
                }
                
               else if(f1.getDescription().equals(".csv"))
               {
              
                   String path = file.getAbsolutePath() +".csv";
                
                    BufferedWriter bw = new BufferedWriter(new FileWriter(path));
                    PrintWriter wr = new PrintWriter(bw);
                
                    util.Save_csv_file_multi(wr, metric_list, Dataset_names, tableMetricsMulti);
                
                    wr.close();
                    bw.close(); 
                    
                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                /*
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
                   */
                
               }
                
               
               else if (f1.getDescription().equals(".arff (Meka)"))
                {
                    String path = file.getAbsolutePath() +".arff";
                
                    BufferedWriter bw = new BufferedWriter(new FileWriter(path));
                    PrintWriter wr = new PrintWriter(bw);
                
                    util.Save_meka_file_multi(wr, metric_list, Dataset_names, tableMetricsMulti);
                
                    wr.close();
                    bw.close(); 
                    
                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                    
                    /*
                    String path = file.getAbsolutePath() +".arff";
                
                    BufferedWriter bw = new BufferedWriter(new FileWriter(path));
                    PrintWriter wr = new PrintWriter(bw);
                    
                    ArrayList<MultiLabelInstances> list_dataset1 = new ArrayList();
                    list_dataset1.add(dataset);
                                   
                    util.Save_in_the_file_arff_meka(wr, metric_list, list_dataset1,file.getName(), es_de_tipo_meka);
                    
                    wr.close();
                    bw.close();      
                    
                  JOptionPane.showMessageDialog(null, "File Saved", "Successful", JOptionPane.INFORMATION_MESSAGE);   
                    */
                }
                
           Toolkit.getDefaultToolkit().beep();
        } 

            
        
    }
     
     
     //CALCULA LAS METRICAS ESPECIFICADAS
     private void button_calculateActionPerformed_datasets(java.awt.event.ActionEvent evt, JTable jtable)
    {
       ArrayList<String> metric_list = Get_metrics_selected(jtable);
        
       
       if(list_dataset.isEmpty()) {
           JOptionPane.showMessageDialog(null, "You must load a dataset.", "Warning", JOptionPane.ERROR_MESSAGE);
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
           JOptionPane.showMessageDialog(null, "You must load a dataset.", "Warning", JOptionPane.ERROR_MESSAGE);
           return; 
       }
       
       
       int posx = this.getBounds().x;
       int posy = this.getBounds().y;
       
       
       metric_output mo = new metric_output(metric_list,dataset, posx, posy, es_de_tipo_meka);
       
       mo.setVisible(true);
     
    }   
     

    private void button_calculateActionPerformed_principal(java.awt.event.ActionEvent evt, JTable jtable)
    {
       ArrayList<String> metric_list = Get_metrics_selected_principal(jtable);

       if(dataset == null) {
           JOptionPane.showMessageDialog(null, "You must load a dataset.", "Warning", JOptionPane.ERROR_MESSAGE);
           return; 
       }
       else if(metric_list.size()==0){
           JOptionPane.showMessageDialog(null, "You must select any metric.", "Warning", JOptionPane.ERROR_MESSAGE);
           return; 
       }

       atributo[] label_frenquency = util.Get_Frequency_x_label(dataset);
       label_frenquency = util.Ordenar_freq_x_attr(label_frenquency);// ordena de mayor a menor
                
        atributo[] imbalanced_data = util.Get_data_imbalanced_x_label_inter_class(dataset,label_frenquency);
        
        String value = new String();

        progressBar.setMinimum(0);
        progressBar.setMaximum(metric_list.size()+1);
        progressBar.setValue(0);
        int v = 1;
        for(String metric : metric_list)
        {
            progressBar.setValue(v);
            //If metric value exists, don't calculate
           if((tableMetrics.get(metric) == null) || (tableMetrics.get(metric).equals("-"))){
               value = util.get_value_metric(metric, dataset, es_de_tipo_meka);
                if(value.equals("-1.0") || value.equals("-1,0")){
                    value = util.get_value_metric_imbalanced(metric, dataset, imbalanced_data);
                } 	

                System.out.println(metric + " --- " + value + " --> " + value.replace(",", "."));
                tableMetrics.put(metric, value.replace(",", "."));
                //jTextArea1.append(metric + util.get_tabs_multi_datasets(metric) + value + "\n"); 
           }
           
           v++;
        }
       
        TableModel model = jtable.getModel();
        
        for(int i=0; i<model.getRowCount(); i++){
            model.setValueAt(util.getValueFormatted(model.getValueAt(i, 0).toString(), tableMetrics.get(model.getValueAt(i, 0).toString())), i, 1);
        }

       jtable.repaint();
    }   
    
    
    private void button_calculateActionPerformed_multi(java.awt.event.ActionEvent evt, JTable jtable)
    {
       ArrayList<String> metric_list = Get_metrics_selected_multi(jtable);

       if(list_dataset == null || list_dataset.size() < 1) {
           JOptionPane.showMessageDialog(null, "You must load a dataset.", "Warning", JOptionPane.ERROR_MESSAGE);
           return; 
       }
       else if(metric_list.isEmpty()){
           JOptionPane.showMessageDialog(null, "You must select any metric.", "Warning", JOptionPane.ERROR_MESSAGE);
           return; 
       }

       atributo[] label_frenquency;
                
        atributo[] imbalanced_data;
        
        String value = new String();

        progressBar.setMinimum(0);
        progressBar.setMaximum(metric_list.size() * Dataset_names.size() + 1);
        progressBar.setValue(0);
        int v = 1;
        
        int d = 0;
        for(String dataName : Dataset_names){

            label_frenquency = util.Get_Frequency_x_label(list_dataset.get(d));
            label_frenquency = util.Ordenar_freq_x_attr(label_frenquency);
            imbalanced_data = util.Get_data_imbalanced_x_label_inter_class(list_dataset.get(d), label_frenquency);
            
            
            if(!tableMetricsMulti.contains(dataName)){
                
                tableMetricsMulti.put(dataName, new Hashtable<String, String>());
                initializeTableMetricsMulti(dataName);
            }
            
            for(String metric : metric_list)
            {
                progressBar.setValue(v);
                
                //If metric value exists, don't calculate
               if((tableMetricsMulti.get(dataName).get(metric) == null) || (tableMetricsMulti.get(dataName).get(metric).equals("-"))){
                   value = util.get_value_metric(metric, list_dataset.get(d), es_de_tipo_meka);
                    if(value.equals("-1.0") || value.equals("-1,0")){
                        value = util.get_value_metric_imbalanced(metric, list_dataset.get(d), imbalanced_data);
                    } 	

                    System.out.println(metric + " --- " + value + " --> " + value.replace(",", "."));
                    tableMetricsMulti.get(dataName).put(metric, value.replace(",", "."));
                    //jTextArea1.append(metric + util.get_tabs_multi_datasets(metric) + value + "\n"); 
               } 
               
               v++;
            }
            
            d++;
        }

        for(int i=0; i<Dataset_names.size(); i++){
            System.out.println("DATASET: " + Dataset_names.get(i));
            for(int m=0; m<metric_list.size(); m++){
                System.out.println("\t" + metric_list.get(m) + tableMetricsMulti.get(Dataset_names.get(i)).get(metric_list.get(m)));
            }
        }
        
        //TableModel model = jtable.getModel();
        
        /*
        TableModel model = new Table_model_metrics(util.Get_row_data_multi(Dataset_names.size()), "multi", Dataset_names.size()+2);
        System.out.println("model columns: " + model.getColumnCount());
        
        for(int i=0; i<metric_list.size(); i++){
            //model.setValueAt(util.getValueFormatted(model.getValueAt(i, 0).toString(), tableMetrics.get(model.getValueAt(i, 0).toString())), i, 1);
            for(int j=0; j<Dataset_names.size(); j++){
                model.setValueAt(tableMetricsMulti.get(Dataset_names.get(i)).get(metric_list.get(i)), i, j+2);
            }
        }
        */

       jtable.repaint();
    }   
      
      
      
    private void clearTable_metrics_principal()
    {
       ArrayList<String> metric_list = util.Get_all_metrics();

        String value;
        for(String metric : metric_list)
        {
           tableMetrics.put(metric, "-");
        }
       
        TableModel model = jTable1.getModel();
        
        for(int i=0; i<model.getRowCount(); i++){
            model.setValueAt(tableMetrics.get(model.getValueAt(i, 0).toString()), i, 1);
        }
        
        jTable1.repaint();
    } 
    
    
    private void clearTable_metrics_traintest()
    {
        ArrayList<String> metric_list = util.Get_all_metrics();

        String value;
      
        ArrayList<String> metric_list_common = util.Get_common_metrics();

        for(String metric : metric_list_common)
        {
           tableMetrics_common.put(metric, "-");
        }
       
        TableModel model = jTable5.getModel();
        
        for(int i=0; i<model.getRowCount(); i++){
            model.setValueAt(tableMetrics_common.get(model.getValueAt(i, 0).toString()), i, 1);
        }
        
        jTable5.repaint();
        
        ArrayList<String> metric_list_test = util.Get_test_metrics();

        for(String metric : metric_list_test)
        {
           tableMetrics_test.put(metric, "-");
           tableMetrics_train.put(metric, "-");
        }
       
        model = jTable7.getModel();
        
        for(int i=0; i<model.getRowCount(); i++){
            model.setValueAt(tableMetrics_train.get(model.getValueAt(i, 0).toString()), i, 1);
            model.setValueAt(tableMetrics_test.get(model.getValueAt(i, 0).toString()), i, 2);
        }
        
        jTable7.repaint();
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
     
     private ArrayList<String> Get_metrics_selected_test(JTable jtable)
     {
       ArrayList<String> result= new ArrayList();
       TableModel tmodel = jtable.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
       {
           if((Boolean)tmodel.getValueAt(i, 3))
           {
               String selected =(String)tmodel.getValueAt(i, 0);
               result.add(selected);                      
           }                
       }   
       return result;
     }
     
     private ArrayList<String> Get_metrics_selected_principal(JTable jtable)
     {
       ArrayList<String> result= new ArrayList();
       TableModel tmodel = jtable.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
       {
           if((Boolean)tmodel.getValueAt(i, 2))
           {
               String selected =(String)tmodel.getValueAt(i, 0);
               result.add(selected);                      
           }                
       }   
       return result;
     }
     
     
     private ArrayList<String> Get_metrics_selected_multi(JTable jtable)
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
     
     private void button_invertActionPerformed_principal(java.awt.event.ActionEvent evt,JTable jtable )
    {
       TableModel tmodel = jtable.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
       {
           if((Boolean)tmodel.getValueAt(i, 2)) {
               tmodel.setValueAt(Boolean.FALSE, i, 2);
           }
           else  {
               tmodel.setValueAt(Boolean.TRUE, i, 2);
           }          
       }      
       
       jtable.setModel(tmodel);
       jtable.repaint();
       
    }     
     
     
     private void button_invertActionPerformed_multi(java.awt.event.ActionEvent evt,JTable jtable )
    {
       TableModel tmodel = jtable.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
       {
           if((Boolean)tmodel.getValueAt(i, 1)) {
               tmodel.setValueAt(Boolean.FALSE, i, 1);
           }
           else  {
               tmodel.setValueAt(Boolean.TRUE, i, 1);
           }          
       }      
       
       jtable.setModel(tmodel);
       jtable.repaint();
       
    }     
    
    private void button_invertActionPerformed2(java.awt.event.ActionEvent evt,JTable jtable,JTable jtable1,JTable jtable2 )
    {
       TableModel tmodel = jtable.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
       {
           if((Boolean)tmodel.getValueAt(i, 2)) {
               tmodel.setValueAt(Boolean.FALSE, i, 2);
           }
           else  tmodel.setValueAt(Boolean.TRUE, i, 2);          
       }      
       
       jtable.setModel(tmodel);
       jtable.repaint();
       
       tmodel = jtable1.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
       {
           if((Boolean)tmodel.getValueAt(i, 3)) {
               tmodel.setValueAt(Boolean.FALSE, i, 3);
           }
           else  tmodel.setValueAt(Boolean.TRUE, i, 3);          
       }      
       
       jtable1.setModel(tmodel);
       jtable1.repaint();
       
       
        tmodel = jtable2.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
       {
           if((Boolean)tmodel.getValueAt(i, 3)) {
               tmodel.setValueAt(Boolean.FALSE, i, 3);
           }
           else  tmodel.setValueAt(Boolean.TRUE, i, 3);          
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
    
    private void button_noneActionPerformed_principal(java.awt.event.ActionEvent evt,JTable jtable)
    {

      TableModel tmodel = jtable.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
       {
           tmodel.setValueAt(Boolean.FALSE, i, 2);
       }
             
       jtable.setModel(tmodel);
       jtable.repaint();
       //frame.setVisible(false);
    }      
    
    private void button_noneActionPerformed_multi(java.awt.event.ActionEvent evt,JTable jtable)
    {

      TableModel tmodel = jtable.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
       {
           tmodel.setValueAt(Boolean.FALSE, i, 1);
       }
             
       jtable.setModel(tmodel);
       jtable.repaint();
       //frame.setVisible(false);
    }      
    
        private void button_noneActionPerformed2(java.awt.event.ActionEvent evt,JTable jtable,JTable jtable1,JTable jtable2)
    {
      TableModel tmodel = jtable.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
           tmodel.setValueAt(Boolean.FALSE, i, 2);
       
       jtable.setModel(tmodel);
       jtable.repaint();
       
       tmodel = jtable1.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
           tmodel.setValueAt(Boolean.FALSE, i, 3);
       
       jtable1.setModel(tmodel);
       jtable1.repaint();
       
       
       tmodel = jtable2.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
           tmodel.setValueAt(Boolean.FALSE, i, 3);
       
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
    
    private void button_allActionPerformed_principal(java.awt.event.ActionEvent evt ,JTable jtable)
    {
       TableModel tmodel = jtable.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
           tmodel.setValueAt(Boolean.TRUE, i, 2);
             
       jtable.setModel(tmodel);
       jtable.repaint();
    }
    
    private void button_allActionPerformed_multi(java.awt.event.ActionEvent evt ,JTable jtable)
    {
       TableModel tmodel = jtable.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
           tmodel.setValueAt(Boolean.TRUE, i, 1);
             
       jtable.setModel(tmodel);
       jtable.repaint();
    }
    
    
    private void button_clearActionPerformed_principal(java.awt.event.ActionEvent evt ,JTable jtable)
    {
       TableModel tmodel = jtable.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
       {
           tmodel.setValueAt(Boolean.FALSE, i, 2);
       }
             
       clearTable_metrics_principal();

    }
    

    
    private void button_export_ActionPerformed(java.awt.event.ActionEvent evt ,JTable jtable)
    {
          if(jtable.getRowCount()==0 || dataset == null)
          {
              JOptionPane.showMessageDialog(null, "The table is empty.", "Error", JOptionPane.ERROR_MESSAGE); 
              return;
          }
        
         // JFILECHOOSER SAVE
         JFileChooser fc= new JFileChooser();
        
         // extension 
        //FileNameExtensionFilter fname = new FileNameExtensionFilter(".xls", "xls"); 
        FileNameExtensionFilter fname1 =  new FileNameExtensionFilter(".csv", "csv");
        
        //eliminar el que tiene por defecto
        fc.removeChoosableFileFilter(fc.getChoosableFileFilters()[0]);
        
        //fc.setFileFilter(fname);
        fc.setFileFilter(fname1);
        //fc.addChoosableFileFilter(fname);
        
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
                     
                    BufferedWriter bw = new BufferedWriter(new FileWriter(path));
                    PrintWriter wr = new PrintWriter(bw);
                    
                    util.Save_table_csv(wr, jtable);
                
                    wr.close();
                    bw.close(); 
                    
                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                    
                     //Exporter exp = new Exporter(new File(path), jtable, "prueba");
                     
                     /*if(exp.exporta())
                     {
                         JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                     }*/
                    
                 }
                 catch(Exception e1)
                 {
                     JOptionPane.showMessageDialog(null, "File not saved correctly.", "Error", JOptionPane.ERROR_MESSAGE); 
                 }   
                             
                }
              
              /*else if(f1.getDescription().equals(".xls"))
               {                             
                               
                 try
                 {
                     String path = file.getAbsolutePath() +".xls";
                     Exporter exp = new Exporter(new File(path), jtable, "prueba");
                     
                     if(exp.exporta())
                     {
                         JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                     }
                 }
                 catch(Exception e1)
                 {
                     JOptionPane.showMessageDialog(null, "File not Saved correctly.", "Error", JOptionPane.ERROR_MESSAGE); 
                 }
               
               }*/
                
        } 
        
        
    }
    
     private void button_export_ActionPerformed(java.awt.event.ActionEvent evt ,JTable jtable, JTable columns, String table)
    {
          if(jtable.getRowCount()==0 || dataset == null)
          {
              JOptionPane.showMessageDialog(null, "The table is empty.", "Error", JOptionPane.ERROR_MESSAGE); 
              return;
          }
        
         // JFILECHOOSER SAVE
         JFileChooser fc= new JFileChooser();
        
         // extension 
        //FileNameExtensionFilter fname = new FileNameExtensionFilter(".xls", "xls"); 
        FileNameExtensionFilter fname1 =  new FileNameExtensionFilter(".csv", "csv");
        
        //eliminar el que tiene por defecto
        fc.removeChoosableFileFilter(fc.getChoosableFileFilters()[0]);
        
        //fc.setFileFilter(fname);
        fc.setFileFilter(fname1);
        //fc.addChoosableFileFilter(fname);
        
        int returnVal = fc.showSaveDialog(this);
         
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            File file = fc.getSelectedFile();
            FileFilter f1 = fc.getFileFilter();
                
            //Saving csv chi_phi
            if(f1.getDescription().equals(".csv"))
            {
                BufferedWriter bw = null;
                PrintWriter wr = null;
                
                try
                 {
                    String path = file.getAbsolutePath() +".csv";
                    bw = new BufferedWriter(new FileWriter(path));
                    wr = new PrintWriter(bw);
                    
                    if(table.equals("ChiPhi")){
                        util.Save_chi_phi_csv_file(wr, chi_fi_coefficient, dataset.getLabelNames());
                    }
                    else if(table.equals("Coocurrence")){
                        util.Save_coocurrence_csv_file(wr, coocurrence_coefficients, dataset.getLabelNames());
                    }
                    else if(table.equals("Heatmap")){
                        util.Save_heatmap_csv_file(wr, heatmap_coefficients, dataset.getLabelNames());
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "File not saved correctly.", "Error", JOptionPane.ERROR_MESSAGE); 
                    }
                    
                    
                    wr.close();
                    bw.close();  
                    
                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                 }
                 catch(Exception e1)
                 {
                     System.out.println("otro mensaje "+e1.toString());
                     JOptionPane.showMessageDialog(null, "File not saved correctly.", "Error", JOptionPane.ERROR_MESSAGE); 
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
                         JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                     }
                 }
                 catch(Exception e1)
                 {
                     JOptionPane.showMessageDialog(null, "File not saved correctly.", "Error", JOptionPane.ERROR_MESSAGE); 
                 }
               
               }
                
        } 
        
        
    }
    
    
    
      private void button_allActionPerformed2(java.awt.event.ActionEvent evt ,JTable jtable,JTable jtable1,JTable jtable2 )
    {
       TableModel tmodel = jtable.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
           tmodel.setValueAt(Boolean.TRUE, i, 2);
       
       jtable.setModel(tmodel);
       jtable.repaint();
       
       tmodel = jtable1.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
           tmodel.setValueAt(Boolean.TRUE, i, 3);
       
       jtable1.setModel(tmodel);
       jtable1.repaint();
       
       
       tmodel = jtable2.getModel();
       
       for(int i=0; i<tmodel.getRowCount();i++)
           tmodel.setValueAt(Boolean.TRUE, i, 3);
       
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
         if(tipo_tabla.equals("coocurrence")) {
             pair_label_values =util.get_pair_label_values(dataset, lista_pares);
             coocurrence_coefficients = pair_label_values;
         }
         //si la tabla es heatmap values "heatmap"
         else {
            //pair_label_values =util.get_heatmap_values(dataset, lista_pares);
            pair_label_values = getHeatMapCoefficients();
             heatmap_coefficients = pair_label_values.clone();
             
             /*
             System.out.println("HEATMAP");
             for(int i=0; i<heatmap_coefficients.length; i++){
                 System.out.println(Arrays.toString(heatmap_coefficients[i]));
             }
             */
        }
      
   //-----------------------------------------------------------------------------------
   //-----------------------------------------------------------------------------------
         data = new Object[pair_label_values.length][pair_label_values.length+1];
         column = new Object[data.length+1];
         
         if(tipo_tabla.equals("coocurrence")) {
            for(int num_fila=0;  num_fila< pair_label_values.length;num_fila++)
            {            
                for(int num_col=0; num_col < pair_label_values.length; num_col++){
                    
                    if(num_col == 0){
                        data[num_fila][num_col] = dataset.getLabelNames()[num_fila];
                    }
                    else if(num_fila == num_col-1){
                        data[num_fila][num_col] = "---";
                    }
                    else if(num_col > num_fila){
                        data[num_fila][num_col] = "";
                    }
                    else{
                        //data[num_fila][num_col] = pair_label_values[num_fila][num_col];                        
                        if(pair_label_values[num_col-1][num_fila] <= 0.0){
                            data[num_fila][num_col] = "";
                        }
                        else{
                            data[num_fila][num_col] = (int) pair_label_values[num_col-1][num_fila];
                        }
                    }
                }
                //data[num_fila]  = util.Get_values_x_fila(num_fila, pair_label_values,dataset.getLabelNames()[num_fila]);
            }
         }
         else{
            for(int num_fila=0;  num_fila< pair_label_values.length;num_fila++)
            {            
                //data[num_fila]  = util.Get_values_x_fila(num_fila, pair_label_values,dataset.getLabelNames()[num_fila]);
            
                for(int num_col=0; num_col < pair_label_values.length+1; num_col++){
                    
                    if(num_col == 0){
                        data[num_fila][num_col] = dataset.getLabelNames()[num_fila];
                    }
                    else if(num_fila == num_col-1){
                        data[num_fila][num_col] = "---";
                    }
                    else{
                        //data[num_fila][num_col] = pair_label_values[num_fila][num_col];                        
                        if(pair_label_values[num_col-1][num_fila] <= 0.0){
                            data[num_fila][num_col] = "";
                        }
                        else{
                            NumberFormat formatter = new DecimalFormat("#0.000"); 
                            data[num_fila][num_col] = formatter.format(pair_label_values[num_col-1][num_fila]).replace(",", ".");
                    
                        }
                    }
                }
            
            } 
            
         }
         
       
           for(int i = 0; i< column.length;i++)
         {
             if(i==0) {
                 column[i]="Labels";
             }
             else {
                 column[i]=(dataset.getLabelNames()[i-1]);
             }
         } 
          
        AbstractTableModel1 fixedModel = new AbstractTableModel1(data, column);
        AbstractTableModel2 model = new AbstractTableModel2(data, column);   
        
       
       JTable temp,fixedTable_temp;
       JPanel jpanel_temp;
       
       if(tipo_tabla.equals("coocurrence")){
           temp=jTable11; 
           jpanel_temp=panelCoOcurrenceValues; 
           fixedTable_temp=fixedTable1;
       }
       else {
           temp=jTable12;
           jpanel_temp=panelHeatmapValues; 
           fixedTable_temp=fixedTable2;
       }

       fixedTable_temp.setModel(fixedModel);
       temp.setModel(model);
       
       JScrollPane scroll = new JScrollPane(temp);
    JViewport viewport = new JViewport();
    viewport.setView(fixedTable_temp);
    viewport.setPreferredSize(fixedTable_temp.getPreferredSize());
    scroll.setRowHeaderView(viewport);
    
    scroll.setBounds(20, 20, 780, 390);
    
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
    
    scroll.setBounds(20, 20, 780, 390);
    
    scroll.setCorner(JScrollPane.UPPER_LEFT_CORNER, fixedTable
        .getTableHeader());
    
    jTable10.setBorder(BorderFactory.createLineBorder(Color.black));
    
    if(first_time_chi){panelChiPhi.add(scroll, BorderLayout.CENTER, 0); first_time_chi=false; return; }
        
        
    panelChiPhi.remove(0); //una curiosa manera de resolver el problema
    panelChiPhi.add(scroll, BorderLayout.CENTER, 0);
       
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
        
        //jpanel.add(scrollPane, BorderLayout.CENTER);
        
        /*jpanel.repaint();
        jpanel.validate();
*/
  
    }
     
     
     public void create_jtable_metric_train_test(JTable table,JPanel jpanel , Object rowData[][], int posx, int posy, int width,int height)
     {
            
        TableModel model = new Table_model_metrics(rowData, "train-test");
        
        table.setModel(model);
       
        //table.setPreferredScrollableViewportSize(table.getPreferredSize());
        //JScrollPane scrollPane = new JScrollPane(table);
        //jPanel13.add(scrollPane);
        
       TableColumnModel tcm = table.getColumnModel();
            
       tcm.getColumn(0).setPreferredWidth(350);
       tcm.getColumn(1).setPreferredWidth(70);
       tcm.getColumn(2).setPreferredWidth(70);
       tcm.getColumn(3).setPreferredWidth(30);
       tcm.getColumn(3).setMinWidth(30);
       tcm.getColumn(3).setMaxWidth(30);
        
       JScrollPane scrollPane = new JScrollPane(table);
        
        //table.setBounds(20, 100, 200, 100);
       
        scrollPane.setBounds(posx, posy, width, height);
        
        table.setBorder(BorderFactory.createLineBorder(Color.black));
        
        jpanel.add(scrollPane, BorderLayout.CENTER);
        jpanel.repaint();
        jpanel.validate();

  
    }
     
     
     public void create_jtable_metric_principal(JTable table,JPanel jpanel , Object rowData[][], int posx, int posy, int width,int height)
     {
            
        TableModel model = new Table_model_metrics(rowData);
        
        table.setModel(model);
       
        //table.setPreferredScrollableViewportSize(table.getPreferredSize());
        //JScrollPane scrollPane = new JScrollPane(table);
        //jPanel13.add(scrollPane);
        
       TableColumnModel tcm = table.getColumnModel();
            
       tcm.getColumn(0).setPreferredWidth(420);
       tcm.getColumn(1).setPreferredWidth(70);

       DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
       rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
       tcm.getColumn(1).setCellRenderer(rightRenderer);

       tcm.getColumn(2).setPreferredWidth(50);
       tcm.getColumn(2).setMaxWidth(50);
       tcm.getColumn(2).setMinWidth(50);
        
       JScrollPane scrollPane = new JScrollPane(table);
        
        //table.setBounds(20, 100, 200, 100);
       
        scrollPane.setBounds(posx, posy, width, height);
        
        table.setBorder(BorderFactory.createLineBorder(Color.black));
        
        jpanel.add(scrollPane, BorderLayout.CENTER);
        jpanel.repaint();
        jpanel.validate();

  
    }
     
     
     public void create_jtable_metric_multi(JTable table,JPanel jpanel , Object rowData[][], int posx, int posy, int width,int height)
     {
            
       TableModel model = new Table_model_metrics(rowData, "multi");
        
       table.setModel(model);
        
       TableColumnModel tcm = table.getColumnModel();
            
       tcm.getColumn(0).setPreferredWidth(320);

       tcm.getColumn(1).setPreferredWidth(40);
       tcm.getColumn(1).setMaxWidth(40);
       tcm.getColumn(1).setMinWidth(40);
        
       JScrollPane scrollPane = new JScrollPane(table);
        
        //table.setBounds(20, 100, 200, 100);
       
        scrollPane.setBounds(posx, posy, width, height);
        
        table.setBorder(BorderFactory.createLineBorder(Color.black));
        
        jpanel.add(scrollPane, BorderLayout.CENTER);
        jpanel.repaint();
        jpanel.validate();
    }
     
     
     private double[][] getHeatMapCoefficients(){
         
         atributo [] label_frenquency = util.Get_Frequency_x_label(dataset);;
         double [] label_frenquency_values = util.get_label_frequency(label_frenquency);
         
         double [][] coeffs = new double[dataset.getNumLabels()][dataset.getNumLabels()];
         
         System.out.println("COOCURRENCE");
         for(int i=0; i<dataset.getNumLabels(); i++){
             System.out.println(Arrays.toString(coocurrence_coefficients[i]));
         }
         
        for(int i=0; i<dataset.getNumLabels(); i++){
            for(int j=0; j<dataset.getNumLabels(); j++){
                
                if(label_frenquency_values[j] <= 0){
                    coeffs[i][j] = 0;
                }
                else if (i==j){
                    coeffs[i][j] = label_frenquency_values[i] / dataset.getNumInstances();
                }
                else{
                    if(coocurrence_coefficients[i][j] > 0){
                        coeffs[i][j] = coocurrence_coefficients[i][j] / label_frenquency_values[j];
                        System.out.println("1. coocurrence[" + i + "][" + j + "]: " + coocurrence_coefficients[i][j] + " ; frequency[" + i + "]: " + label_frenquency_values[j]);
                    }
                    else{
                        if(coocurrence_coefficients[j][i] > 0){
                            coeffs[i][j] = coocurrence_coefficients[j][i] / label_frenquency_values[j];
                            System.out.println("2. coocurrence[" + j + "][" + i + "]: " + coocurrence_coefficients[j][i] + " ; frequency[" + i + "]: " + label_frenquency_values[j]);
                        }
                        else{
                           coeffs[i][j] = 0; 
                        }
                    }
                }
                
                
            }
        }
        
        System.out.println("HEATMAP TABLE");
         for(int p=0; p<coeffs.length; p++){
             System.out.println(Arrays.toString(coeffs[p]));
         }
         
         return coeffs;
     }
     

     public int[] getTopRelatedHeatmap(int n){
            
            LabelsPairValue p = new LabelsPairValue();
            
            ArrayList<String> pares = new ArrayList<String>();
            
            ArrayList<LabelsPairValue> pairs = new ArrayList<LabelsPairValue>();
            for(int i=0; i<heatmap_coefficients.length; i++){
                for(int j=0; j<heatmap_coefficients.length; j++){
                    if(heatmap_coefficients[i][j] > 0){
                        pairs.add(new LabelsPairValue(i, j, heatmap_coefficients[i][j]));
                    }
                }
            }
            Collections.sort(pairs, Collections.reverseOrder());
            
            System.out.println(Arrays.toString(pairs.toArray()));

            int numLabels = n;
            int currentSelectedLabels = 0;

            Vector<Integer> selectedLabels = new Vector<Integer>();

            do{
                if(!selectedLabels.contains(pairs.get(0).label1)){
                    selectedLabels.add(pairs.get(0).label1);
                    System.out.println("Add " + pairs.get(0).label1);
                    currentSelectedLabels++;
                }
                
                if(currentSelectedLabels < numLabels){
                    if(!selectedLabels.contains(pairs.get(0).label2)){
                        selectedLabels.add(pairs.get(0).label2);
                        System.out.println("Add " + pairs.get(0).label2);
                        currentSelectedLabels++;
                    }
                }
                
                pairs.remove(pairs.get(0));
            }while((pairs.size() > 0) && (currentSelectedLabels < numLabels));

            
            System.out.println("-----");
            
            int [] labelIndices = new int[n];
            
            String s = new String();

            if(selectedLabels.size() < n){
                //tableHeatmapLeft.setRowSelectionInterval(0, dataset.getNumLabels()-1);
                //int[] selectedsFreq = tableHeatmapLeft.getSelectedRows();
                
                int[] selectedsFreq = new int[dataset.getNumLabels()];
                for(int i=0; i<selectedsFreq.length; i++){
                    selectedsFreq[i] = i;
                }
                
                System.out.println("selectedsFreq: " + Arrays.toString(selectedsFreq));
                
                int i = 0;
                do{
                    if(!selectedLabels.contains((int)selectedsFreq[i])){
                        selectedLabels.add(selectedsFreq[i]);
                    }

                    i++;
                }while(selectedLabels.size() < n);
            }
             
            for(int i=0; i<selectedLabels.size(); i++){
                s = jTable12.getColumnName(selectedLabels.get(i));
                if(s != null){
                    pares.add(s);
                    labelIndices[i] = selectedLabels.get(i);
                    System.out.println("Adding " + labelIndices[i]);
                }
                else{
                    System.out.println("NULL");
                }
            }
            
            return labelIndices;
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
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.JButton buttonRemoveMultipleDatasets;
    private javax.swing.JButton buttonShowCoOcurrence;
    private javax.swing.JButton buttonShowHeatMap;
    private javax.swing.JButton buttonShowMostFrequent;
    private javax.swing.JButton buttonShowMostFrequentHeatMap;
    private javax.swing.JButton buttonShowMostRelated;
    private javax.swing.JButton buttonShowMostRelatedHeatMap;
    private javax.swing.JComboBox comboBoxLabelsInformation;
    private javax.swing.JButton jButtonSaveDatasets;
    private javax.swing.JButton jButtonStartPreprocess;
    private javax.swing.JComboBox jComboBox_BRFS_Comb;
    private javax.swing.JComboBox jComboBox_BRFS_Norm;
    private javax.swing.JComboBox jComboBox_BRFS_Out;
    private javax.swing.JComboBox jComboBox_SaveFormat;
    private javax.swing.JLabel jLabelChiFi_text;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanelMulti;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JLabel labelAttributes;
    private javax.swing.JLabel labelAttributesValue;
    private javax.swing.JLabel labelBRFS;
    private javax.swing.JLabel labelBRFS1;
    private javax.swing.JLabel labelBRFS_Comb;
    private javax.swing.JLabel labelBRFS_Norm;
    private javax.swing.JLabel labelBRFS_Out;
    private javax.swing.JLabel labelBound;
    private javax.swing.JLabel labelBoundValue;
    private javax.swing.JLabel labelCardinality;
    private javax.swing.JLabel labelCardinalityValue;
    private javax.swing.JLabel labelDensity;
    private javax.swing.JLabel labelDensityValue;
    private javax.swing.JLabel labelDistinct;
    private javax.swing.JLabel labelDistinctValue;
    private javax.swing.JLabel labelDiversity;
    private javax.swing.JLabel labelDiversityValue;
    private javax.swing.JLabel labelFoldsIterativeStratified;
    private javax.swing.JLabel labelFoldsLPStratified;
    private javax.swing.JLabel labelFoldsRandom;
    private javax.swing.JLabel labelInstances;
    private javax.swing.JLabel labelInstancesValue;
    private javax.swing.JLabel labelLabels;
    private javax.swing.JLabel labelLabelsValue;
    private javax.swing.JLabel labelLxIxF;
    private javax.swing.JLabel labelLxIxFValue;
    private javax.swing.JLabel labelPercIterativeStratified;
    private javax.swing.JLabel labelPercLPStratified;
    private javax.swing.JLabel labelPercRandom;
    private javax.swing.JLabel labelRandomFS;
    private javax.swing.JLabel labelRandomIS;
    private javax.swing.JLabel labelRelation;
    private javax.swing.JLabel labelRelationValue;
    private javax.swing.JList listMultipleDatasetsLeft;
    private java.awt.Panel panel1;
    private javax.swing.JPanel panelBoxDiagram;
    private javax.swing.JPanel panelChiPhi;
    private javax.swing.JPanel panelCoOcurrence;
    private javax.swing.JPanel panelCoOcurrenceRight;
    private javax.swing.JPanel panelCoOcurrenceValues;
    private javax.swing.JPanel panelCurrentDataset;
    private javax.swing.JPanel panelDataset;
    private javax.swing.JPanel panelExamplesPerLabel;
    private javax.swing.JPanel panelExamplesPerLabelset;
    private javax.swing.JPanel panelFS;
    private javax.swing.JPanel panelHeatmap;
    private javax.swing.JPanel panelHeatmapGraph;
    private javax.swing.JPanel panelHeatmapValues;
    private javax.swing.JPanel panelIRperLabelInterClass;
    private javax.swing.JPanel panelIRperLabelIntraClass;
    private javax.swing.JPanel panelIRperLabelset;
    private javax.swing.JPanel panelIS;
    private javax.swing.JPanel panelImbalance;
    private javax.swing.JPanel panelImbalanceDataMetrics;
    private javax.swing.JPanel panelImbalanceLeft;
    private javax.swing.JPanel panelLabelsIRperLabelInterClass;
    private javax.swing.JPanel panelLabelsIRperLabelIntraClass;
    private javax.swing.JPanel panelLabelsPerExample;
    private javax.swing.JPanel panelMultipleDatasets;
    private javax.swing.JPanel panelMultipleDatasetsLeft;
    private javax.swing.JPanel panelPreprocess;
    private javax.swing.JPanel panelSplitting;
    private javax.swing.JRadioButton radioBRFS;
    private javax.swing.JRadioButton radioExamplesPerLabel;
    private javax.swing.JRadioButton radioExamplesPerLabelset;
    private javax.swing.JRadioButton radioIterativeStratifiedCV;
    private javax.swing.JRadioButton radioIterativeStratifiedHoldout;
    private javax.swing.JRadioButton radioLPStratifiedCV;
    private javax.swing.JRadioButton radioLPStratifiedHoldout;
    private javax.swing.JRadioButton radioNoFS;
    private javax.swing.JRadioButton radioNoIS;
    private javax.swing.JRadioButton radioNoSplit;
    private javax.swing.JRadioButton radioRandomCV;
    private javax.swing.JRadioButton radioRandomFS;
    private javax.swing.JRadioButton radioRandomHoldout;
    private javax.swing.JRadioButton radioRandomIS;
    private javax.swing.JTable tableCoOcurrenceLeft;
    private javax.swing.JTable tableHeatmapLeft;
    private javax.swing.JTable tableImbalance;
    private javax.swing.JTabbedPane tabsDependences;
    private javax.swing.JTabbedPane tabsImbalance;
    private javax.swing.JTextField textBRFS;
    private javax.swing.JTextField textChooseFile;
    private javax.swing.JTextField textIterativeStratifiedCV;
    private javax.swing.JTextField textIterativeStratifiedHoldout;
    private javax.swing.JTextField textLPStratifiedCV;
    private javax.swing.JTextField textLPStratifiedHoldout;
    private javax.swing.JTextField textMostFrequent;
    private javax.swing.JTextField textMostFrequentHeatMap;
    private javax.swing.JTextField textMostRelated;
    private javax.swing.JTextField textMostRelatedHeatMap;
    private javax.swing.JTextField textRandomCV;
    private javax.swing.JTextField textRandomFS;
    private javax.swing.JTextField textRandomHoldout;
    private javax.swing.JTextField textRandomIS;
    // End of variables declaration//GEN-END:variables
}
