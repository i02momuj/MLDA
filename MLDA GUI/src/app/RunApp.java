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

package app;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import conversion.MekaToMulan;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
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
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import models.AbstractTableModel1;
import models.AbstractTableModel2;
import utils.Exporter;
import utils.HeapSort;
import charts.HeatMap;
import utils.LabelsPairValue;
import renders.BaseRender;
import renders.IRRender;
import renders.DefaultRender;
import models.MetricsTableModel;
import utils.ImbalancedFeature;
import utils.ContainerIRInterClass;
import utils.EmergentOutput;
import utils.AttributesPair;
import utils.Utils;
import mldc.attributes.AvgGainRatio;
import mldc.base.MLDataMetric;
import mldc.labelsDistribution.Cardinality;
import mldc.labelsDistribution.Density;
import mldc.labelsRelation.Bound;
import mldc.labelsRelation.Diversity;
import mldc.size.DistinctLabelsets;
import mldc.size.Labels;
import mldc.size.LxIxF;
import mldc.size.RatioInstancesToAttributes;

import mulan.data.InvalidDataFormatException;
import mulan.data.IterativeStratification;
import mulan.data.LabelSet;
import mulan.data.LabelsMetaDataImpl;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;
import mulan.examples.CrossValidationExperiment;
import mulan.transformations.BinaryRelevanceTransformation;
import mulan.transformations.IncludeLabelsTransformation;
import mulan.transformations.LabelPowersetTransformation;
import mulan.transformations.RemoveAllLabels;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import preprocess.FeatureSelector;
import preprocess.RandomTrainTest;
import preprocess.IterativeTrainTest;
import preprocess.LabelPowersetTrainTest;
import utils.AttributePairsUtils;
import utils.ChartUtils;
import utils.DataIOUtils;
import utils.DataInfoUtils;
import utils.MetricUtils;
import utils.ResultsIOUtils;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.Randomize;
import weka.filters.unsupervised.instance.RemoveRange;



/**
 * @author Jose Maria Moyano Murillo
 * 
 * RunApp class is the main class for the GUI
 */
public class RunApp extends javax.swing.JFrame {

    /*
     * Progress bar
     */
    public static JProgressBar progressBar;
    public static JFrame progressFrame;
    
    /*
     * Dataset
     */
    String datasetName = new String();
    MultiLabelInstances dataset;
    String xmlFilename = null, xmlPath = "";
    ArrayList<MultiLabelInstances> listDatasets;
    String datasetCurrentName;
    
    /*
     * Preprocessing
     */
    MultiLabelInstances trainDataset, testDataset;
    MultiLabelInstances preprocessedDataset;
    ArrayList<Instances> transformedDatasets = new ArrayList<>();
    ArrayList<MultiLabelInstances> trainDatasets, testDatasets;
    
    /*
     * Charts
     */
    ChartPanel labelFrequencyChart,labelsetsFrequencyChart, labelsHistogramChart;
    ChartPanel labelsBoxDiagram, attributesBoxDiagram2;
    ChartPanel IRLabelsetsChart, IRInterClassChart, IRIntraClassChart;
    mxGraphComponent graphComponent = null;
   
    /*
     * Buttons
     */
    JButton buttonAll, buttonNone, buttonInvert, buttonCalculate, buttonSave, buttonClear;
    JButton exportChiPhiTable, exportCoocurrenceTable, exportHeatmapTable, exportCoocurrenceGraph, exportHeatmapGraph;
    
    /*
     * Tables
     */
    JTable jTablePrincipal, jTableMulti;
    JTable jTableChiPhi, jTableCoocurrences, jTableHeatmap;
    JTable fixedTableChiPhi, fixedTableCoocurrences, fixedTableHeatmap;
    
    /*
     * Table models
     */
    TableModel tmBox, tmLabelFrequency, tmLabelsetFrequency, tmIR;
    TableModel tmLabelsHistogram, tmAttributes, tmCoocurrences, tmHeatmap;
    TableModel tmIRLabelset, tmIRInterClass, tmIRIntraClass;    
    DefaultListModel list = new DefaultListModel();
    
    /*
     * Imbalanced
     */
    ImbalancedFeature[] labelsetsSorted = null;
    ImbalancedFeature[] labelsetsIRSorted = null;
    ImbalancedFeature[] labelsFreqSorted;
    double[] labelsetsByIR = null;
    ImbalancedFeature[] imbalancedLabels;
    int [] idByNLabelsInterClass = null;      
    double[] idByIRInterClass = null;  
    ContainerIRInterClass irTimes = null;
    double[] IRInterClass = null;
    double[] IRIntraClass = null;
    ImbalancedFeature[] labelAppearances = null;
    double[] labelsetsFrequency = null;
    
    /*
     * Dependences
     */
    JLabel chiLabel, phiLabel; 
    double[][] chiPhiCoefficients;
    double [][] coocurrenceCoefficients;
    double [][] heatmapCoefficients;
    Object[][] data; 
    Object[] column;
    HeatMap heatMap = null;
    boolean firstTimeChi = true; 
    
    /*
     * Metrics
     */
    Hashtable<String, String> tableMetrics = new Hashtable<>();  
    Hashtable<String, Hashtable<String, String>> tableMetricsMulti = new Hashtable<>();
    
    /*
     * MVML
     */
    Hashtable<String, Integer[]> views = new Hashtable<>();
    Hashtable<String, String> viewsIntervals = new Hashtable<>();
    boolean mv = false;
    
    ArrayList<Boolean> areMeka;
    ArrayList<String> datasetNames;
    Statistics stat;
    ArrayList<AttributesPair> pairs = null;    
    boolean isMeka = false;
    ArrayList<String> labelsetStringsByFreq;
    ArrayList<String> labelsetStringByIR;
    
    
    /**
     * Runs the GUI
     */
    public RunApp() 
    {
        //For menus language at charts
        Locale.setDefault(Locale.UK);
        
        jTablePrincipal = new JTable();
        jTableChiPhi= new JTable();
        fixedTableChiPhi = new JTable();
        fixedTableCoocurrences = new JTable();
        fixedTableHeatmap = new JTable();
        jTableCoocurrences= new JTable();
        jTableHeatmap= new JTable();
        jTableMulti = new JTable();
        
        this.setTitle("Multi-Label Dataset Analyzer (MLDA)");

        try {
            this.setIconImage(ImageIO.read(new File("src/images/64.png")));
        }
        catch (IOException exc) {
        }
        
        this.setMinimumSize(new Dimension(780,500));       
        this.setBounds(300,0, 780, 500);

        initComponents();   

        initConfig();
     
        datasetCurrentName = "";          
        
        multipleDatasetsConfig();
    }
    
    
    /**
     * Initializes Chi and Phi tables
     */
    private void initChiPhiJTable()
    {
        fixedTableChiPhi.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTableChiPhi.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        fixedTableChiPhi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTableChiPhi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        fixedTableCoocurrences.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTableCoocurrences.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        fixedTableCoocurrences.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTableCoocurrences.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        fixedTableHeatmap.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTableHeatmap.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        fixedTableHeatmap.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTableHeatmap.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        chiLabel = new JLabel("Chi coefficients", SwingConstants.CENTER);
        chiLabel.setBounds(25,420, 120,20);
        chiLabel.setBackground(Color.white);
        chiLabel.setForeground(Color.black);
        chiLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        chiLabel.setOpaque(true);
        chiLabel.setToolTipText("White cells corresponds to chi coefficients");

        panelChiPhi.add(chiLabel);

        phiLabel = new JLabel("Phi coefficients", SwingConstants.CENTER);
        phiLabel.setBounds(165,420, 120, 20);
        phiLabel.setBackground(Color.lightGray);
        phiLabel.setForeground(Color.black);
        phiLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        phiLabel.setOpaque(true);
        phiLabel.setToolTipText("Light gray cells corresponds to phi coefficients");

        panelChiPhi.add(phiLabel);
        
        jLabelChiFiText.setVisible(false);
    } 
    
    
    /**
     * Initializes the default configuration of the app
     */
    private void initConfig()
    {   
        /*
         * Preprocess
         */
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

        labelBRFSComb.setToolTipText("Combiantion approach mode");
        jComboBoxBRFSComb.setToolTipText("<html>Combiantion approach mode: <br>"
                                          + "max: maximum <br>"
                                          + "avg: average <br>"
                                          + "min: minumum </html>");

        labelBRFSNorm.setToolTipText("Normalization mode");
        jComboBoxBRFSNorm.setToolTipText("<html>Normalization mode: <br>"
                                          + "dl: divide by length <br>"
                                          + "dm: divide by maximum <br>"
                                          + "none: no normalization </html>");

        labelBRFSOut.setToolTipText("Scoring mode");
        jComboBoxBRFSOut.setToolTipText("<html>Scoring mode: <br>"
                                          + "eval: evaluation score <br>"
                                          + "rank: ranking score </html>");


        buttonGroup2.add(radioRandomFS);
        radioRandomFS.setToolTipText("Random selection of the features");
        textRandomFS.setToolTipText("Number of features to select");

        buttonGroup2.add(radioNoFS);
        radioNoFS.setToolTipText("No feature selection is done");

        jButtonStartPreprocess.setToolTipText("Start preprocessing");
        jButtonSaveDatasets.setToolTipText("Save dataset files in a folder");
        jComboBoxSaveFormat.setToolTipText("Select Mulan or Meka format to save datasets");

        buttonGroup3.add(radioNoIS);
        radioNoIS.setToolTipText("No instance selection is done");
        buttonGroup3.add(radioRandomIS);
        radioRandomIS.setToolTipText("Random selection of the instances");
        textRandomIS.setToolTipText("Number of instances to select");


        radioRandomHoldout.setSelected(true);
        radioNoFS.setSelected(true);
        radioNoIS.setSelected(true);

        textRandomHoldout.setEnabled(true);        
        
        textRandomHoldout.setEnabled(true);
        textIterativeStratifiedHoldout.setEnabled(false);
        textRandomCV.setEnabled(false);
        textIterativeStratifiedCV.setEnabled(false);
        
        /*
         * Transformations
         */
        radioBRTrans.setSelected(true);
        buttonGroup4.add(radioBRTrans);
        radioBRTrans.setToolTipText("Generates a binary dataset for each label");
        buttonGroup4.add(radioLPTrans);
        radioLPTrans.setToolTipText("Generates a multi-class dataset where each class is each one of the labelsets in the MLDataset");
        buttonGroup4.add(radioIncludeLabelsTrans);
        radioIncludeLabelsTrans.setToolTipText("<html>Generates a binary dataset, replicating the instance, where each one is augmented <br> with a label name and the class indicates if the label was associated or not</html>");
        radioIncludeLabelsTrans.setToolTipText("<html>Generates a binary dataset where each instance is replicated as many times as the number of labels.<br>"
                                                +"Each new instance is augmented with a label name and the class indicates if the label was associated or not.</html>");
        buttonGroup4.add(radioRemoveLabelsTrans);
        radioRemoveLabelsTrans.setToolTipText("Remove all the labels of the dataset");
        jButtonStartTrans.setToolTipText("Start transformation");
        jButtonSaveDatasetsTrans.setToolTipText("Save dataset files in a folder");
        
        /*
         * Dependences
         */
        buttonShowCoOcurrence.setToolTipText("Show graph with labels selected in table");
        buttonShowMostFrequent.setToolTipText("Show graph with n most frequent labels");
        textMostFrequent.setToolTipText("Number of most frequent labels to show");
        buttonShowMostRelated.setToolTipText("Show graph with n most related labels");
        textMostRelated.setToolTipText("Number of most related labels to show");
        buttonShowMostFrequentURelated.setToolTipText("Show graph with n most frequent union n most related labels");
        textMostFrequentURelated.setToolTipText("Show graph with n most frequent union n most related labels");

        buttonShowHeatMap.setToolTipText("Show heatmap with labels selected in table");
        buttonShowMostFrequentHeatMap.setToolTipText("Show heatmap with n most frequent labels");
        textMostFrequentHeatMap.setToolTipText("Number of most frequent labels to show");
        buttonShowMostRelatedHeatMap.setToolTipText("Show heatmap with n most related labels");
        textMostRelatedHeatMap.setToolTipText("Number of most related labels to show");
        buttonShowMostFrequentURelatedHeatMap.setToolTipText("Show heatmap with n most frequent union n most related labels");
        textMostFrequentURelatedHeatMap.setToolTipText("Show graph with n most frequent union n most related labels");
        
        jTableChiPhi = setChiPhiTableHelp(jTableChiPhi);
        jTableCoocurrences = setCoocurrenceTableHelp(jTableCoocurrences);
        jTableHeatmap = setHeatmapTableHelp(jTableHeatmap);
        
        initChiPhiJTable();
        
        //Config jTable Co-ocurrence values
        jTableCoocurrences.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane scrollPane = new JScrollPane(jTableCoocurrences, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        scrollPane.setBounds(20, 20, 780, 390);
        jTableCoocurrences.setBorder(BorderFactory.createLineBorder(Color.black));
        panelCoOcurrenceValues.add(scrollPane, BorderLayout.CENTER);
      
        //Config jTable heatmap values
        jTableHeatmap.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scrollPane = new JScrollPane(jTableHeatmap, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.setBounds(20, 20, 780, 390);
        jTableHeatmap.setBorder(BorderFactory.createLineBorder(Color.black));
        panelHeatmapValues.add(scrollPane, BorderLayout.CENTER);
      
        createButtonExportDependencesTable(jTableChiPhi,fixedTableChiPhi ,panelChiPhi ,exportChiPhiTable,710,415, "ChiPhi"); // chiLabel and phiLabel values
        createButtonExportDependencesTable(jTableCoocurrences,fixedTableCoocurrences,panelCoOcurrenceValues ,exportCoocurrenceTable,710,415, "Coocurrence");//graph values
        createButtonExportDependencesTable(jTableHeatmap,fixedTableHeatmap,panelHeatmapValues ,exportHeatmapTable,710,415, "Heatmap");//heatmap values

        createButtonExportDependencesChart(panelCoOcurrence ,exportCoocurrenceGraph,720,440);
        createButtonExportDependencesChart(panelHeatmapGraph,exportHeatmapGraph,720,440);
        Border border = BorderFactory.createLineBorder(Color.gray, 1);
        
        panelHeatmap.setBorder(border); 
        jTableChiPhi.setBorder(border);
        jTableCoocurrences.setBorder(border);
        jTableHeatmap.setBorder(border);
        
        /*
         * Charts
         */
        labelFrequencyChart = createJChart(panelExamplesPerLabel,"bar","Frequency", "Labels",false, "Label frequency");
        labelsetsFrequencyChart = createJChart(panelExamplesPerLabelset, "bar","Frequency","Labelsets",false, "Labelset frequency");
        labelsHistogramChart =  createJChart(panelLabelsPerExample,"bar", "Frequency","Number of labels",false, "Labels histogram");
        labelsBoxDiagram =createGraph(panelBoxDiagram);
        attributesBoxDiagram2 =createGraph(panelBoxDiagramAtt);
        
        IRInterClassChart = createJChart(panelIRperLabelInterClass, "bar", "IR inter-class","Labels",false, "IR inter class");
        IRIntraClassChart = createJChart(panelIRperLabelIntraClass, "bar", "IR intra-class","Labels",false, "IR intra class");

        IRLabelsetsChart = createJChart(panelIRperLabelset, "bar", "IR","Labelsets",false, "IR per labelset");
        
        jLabelIR.setVisible(false);
        
        /*
         * Metrics
         */
        jTablePrincipal = setMetricsHelp(jTablePrincipal);
        createMetricsTable(jTablePrincipal,panelSummary,buttonAll,buttonNone,buttonInvert,buttonCalculate,buttonSave, buttonClear, 30,190,780,280); //tab Database //35,155,500,355

        jTableMulti = setMetricsHelp(jTableMulti);
        createMultiMetricsTable(jTableMulti,jPanelMulti,buttonAll,buttonNone,buttonInvert,buttonCalculate,buttonSave, 25,15,510,420); //tab Multi

        jButtonSaveDatasets.setEnabled(false);
        jComboBoxSaveFormat.setEnabled(false);

        /*
         * Progress bar
         */
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);  

        progressFrame = new JFrame();

        progressFrame.setBounds(this.getX() + this.getWidth()/2 - 100, this.getY() + this.getHeight()/2 - 15, 200, 30);
        progressFrame.setResizable(false);
        progressFrame.setUndecorated(true);
        progressFrame.add(progressBar);   
        
        //Default tab
        TabPrincipal.setEnabledAt(1, false);
        TabPrincipal.setEnabledAt(2, false);
        TabPrincipal.setEnabledAt(3, false);
        TabPrincipal.setEnabledAt(4, false);
        TabPrincipal.setEnabledAt(5, false);
        TabPrincipal.setEnabledAt(6, false);
        TabPrincipal.setEnabledAt(7, false);
    }
    
    
    /**
     * Control multiple datasets configuration
     */
    private void multipleDatasetsConfig()
    {
        areMeka = new ArrayList();
        listDatasets = new ArrayList();
        datasetNames = new ArrayList();
        listMultipleDatasetsLeft.setModel(list);
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
        panelSummary = new javax.swing.JPanel();
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
        labelBRFSComb = new javax.swing.JLabel();
        jComboBoxBRFSComb = new javax.swing.JComboBox();
        labelBRFSNorm = new javax.swing.JLabel();
        jComboBoxBRFSNorm = new javax.swing.JComboBox();
        labelBRFSOut = new javax.swing.JLabel();
        jComboBoxBRFSOut = new javax.swing.JComboBox();
        radioRandomFS = new javax.swing.JRadioButton();
        textRandomFS = new javax.swing.JTextField();
        labelRandomFS = new javax.swing.JLabel();
        radioNoFS = new javax.swing.JRadioButton();
        jComboBoxSaveFormat = new javax.swing.JComboBox();
        panelIS = new javax.swing.JPanel();
        radioRandomIS = new javax.swing.JRadioButton();
        textRandomIS = new javax.swing.JTextField();
        labelRandomIS = new javax.swing.JLabel();
        radioNoIS = new javax.swing.JRadioButton();
        panelTransformation = new javax.swing.JPanel();
        jButtonStartTrans = new javax.swing.JButton();
        jButtonSaveDatasetsTrans = new javax.swing.JButton();
        panelTransformationChoose = new javax.swing.JPanel();
        radioLPTrans = new javax.swing.JRadioButton();
        radioBRTrans = new javax.swing.JRadioButton();
        radioIncludeLabelsTrans = new javax.swing.JRadioButton();
        radioRemoveLabelsTrans = new javax.swing.JRadioButton();
        panelLabels = new javax.swing.JPanel();
        comboBoxLabelsInformation = new javax.swing.JComboBox();
        panelImbalanceLeft = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableImbalance = new javax.swing.JTable();
        export2 = new javax.swing.JButton();
        tabsImbalance = new javax.swing.JTabbedPane();
        panelExamplesPerLabel = new javax.swing.JPanel();
        panelExamplesPerLabelset = new javax.swing.JPanel();
        panelLabelsPerExample = new javax.swing.JPanel();
        panelIRperLabelIntraClass = new javax.swing.JPanel();
        panelIRperLabelset = new javax.swing.JPanel();
        panelBoxDiagram = new javax.swing.JPanel();
        panelIRperLabelInterClass = new javax.swing.JPanel();
        jLabelIR = new javax.swing.JLabel();
        panelAttributes = new javax.swing.JPanel();
        comboBoxAttributeInformation = new javax.swing.JComboBox();
        panelAttributeLeft = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tableAttributesLeft = new javax.swing.JTable();
        tabsAttributes = new javax.swing.JTabbedPane();
        panelBoxDiagramAtt = new javax.swing.JPanel();
        panelDependences = new javax.swing.JPanel();
        tabsDependences = new javax.swing.JTabbedPane();
        panelChiPhi = new javax.swing.JPanel();
        jLabelChiFiText = new javax.swing.JLabel();
        panelCoOcurrence = new javax.swing.JPanel();
        panelCoOcurrenceRight = new javax.swing.JPanel();
        buttonShowCoOcurrence = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        tableCoOcurrenceLeft = new javax.swing.JTable();
        buttonShowMostFrequent = new javax.swing.JButton();
        textMostFrequent = new javax.swing.JTextField();
        buttonShowMostRelated = new javax.swing.JButton();
        textMostRelated = new javax.swing.JTextField();
        buttonShowMostFrequentURelated = new javax.swing.JButton();
        textMostFrequentURelated = new javax.swing.JTextField();
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
        buttonShowMostFrequentURelatedHeatMap = new javax.swing.JButton();
        textMostFrequentURelatedHeatMap = new javax.swing.JTextField();
        panelHeatmapValues = new javax.swing.JPanel();
        panelMultipleDatasets = new javax.swing.JPanel();
        panelMultipleDatasetsLeft = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listMultipleDatasetsLeft = new javax.swing.JList();
        buttonAddMultipleDatasets = new javax.swing.JButton();
        buttonRemoveMultipleDatasets = new javax.swing.JButton();
        jPanelMulti = new javax.swing.JPanel();
        panelMVML = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        labelNumViews = new javax.swing.JLabel();
        labelMaxNumAttrView = new javax.swing.JLabel();
        labelMinNumAttrView = new javax.swing.JLabel();
        labelMeanNumAttrView = new javax.swing.JLabel();
        labelNumViewsValue = new javax.swing.JLabel();
        labelMaxNumAttrViewValue = new javax.swing.JLabel();
        labelMinNumAttrViewValue = new javax.swing.JLabel();
        labelMeanNumAttrViewValue = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        buttonSaveViews = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jComboBox_SaveFormat1 = new javax.swing.JComboBox();
        buttonSaveTable = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        TabPrincipal.setDoubleBuffered(true);

        buttonChooseFile.setText("Choose file");
        buttonChooseFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonChooseFileActionPerformed(evt);
            }
        });

        textChooseFile.setEditable(false);
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

        javax.swing.GroupLayout panelSummaryLayout = new javax.swing.GroupLayout(panelSummary);
        panelSummary.setLayout(panelSummaryLayout);
        panelSummaryLayout.setHorizontalGroup(
            panelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSummaryLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(panelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelCurrentDataset, javax.swing.GroupLayout.PREFERRED_SIZE, 795, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelSummaryLayout.createSequentialGroup()
                        .addComponent(textChooseFile, javax.swing.GroupLayout.PREFERRED_SIZE, 665, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(buttonChooseFile, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        panelSummaryLayout.setVerticalGroup(
            panelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSummaryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textChooseFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonChooseFile))
                .addGap(7, 7, 7)
                .addComponent(panelCurrentDataset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(347, Short.MAX_VALUE))
        );

        TabPrincipal.addTab("Summary", panelSummary);

        panelSplitting.setBorder(javax.swing.BorderFactory.createTitledBorder("Splitting"));

        radioRandomHoldout.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        radioRandomHoldout.setText("Random holdout");
        radioRandomHoldout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioRandomHoldoutActionPerformed(evt);
            }
        });

        labelPercIterativeStratified.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        labelPercIterativeStratified.setText("%");

        radioIterativeStratifiedHoldout.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        radioIterativeStratifiedHoldout.setText("Iterative stratified holdout ");
        radioIterativeStratifiedHoldout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioIterativeStratifiedHoldoutActionPerformed(evt);
            }
        });

        textRandomHoldout.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        textRandomHoldout.setText("70");

        radioRandomCV.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        radioRandomCV.setText("Random CV");
        radioRandomCV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioRandomCVActionPerformed(evt);
            }
        });

        radioIterativeStratifiedCV.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        radioIterativeStratifiedCV.setText("Iterative stratified CV");
        radioIterativeStratifiedCV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioIterativeStratifiedCVActionPerformed(evt);
            }
        });

        textIterativeStratifiedCV.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        textIterativeStratifiedCV.setText("5");
        textIterativeStratifiedCV.setEnabled(false);

        labelFoldsRandom.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        labelFoldsRandom.setText("Folds");

        textRandomCV.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        textRandomCV.setText("5");
        textRandomCV.setEnabled(false);

        labelFoldsIterativeStratified.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        labelFoldsIterativeStratified.setText("Folds");

        labelPercRandom.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        labelPercRandom.setText("%");

        textIterativeStratifiedHoldout.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        textIterativeStratifiedHoldout.setText("70");
        textIterativeStratifiedHoldout.setEnabled(false);

        radioLPStratifiedHoldout.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        radioLPStratifiedHoldout.setText("LabelPowerset stratified holdout ");
        radioLPStratifiedHoldout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioLPStratifiedHoldoutActionPerformed(evt);
            }
        });

        textLPStratifiedHoldout.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        textLPStratifiedHoldout.setText("70");
        textLPStratifiedHoldout.setEnabled(false);

        labelPercLPStratified.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        labelPercLPStratified.setText("%");

        radioLPStratifiedCV.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        radioLPStratifiedCV.setText("LabelPowerset stratified CV");
        radioLPStratifiedCV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioLPStratifiedCVActionPerformed(evt);
            }
        });

        textLPStratifiedCV.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        textLPStratifiedCV.setText("5");
        textLPStratifiedCV.setEnabled(false);

        labelFoldsLPStratified.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        labelFoldsLPStratified.setText("Folds");

        radioNoSplit.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
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
                    .addComponent(radioNoSplit)
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
                        .addGap(84, 84, 84)
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
                                .addComponent(labelFoldsLPStratified)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        radioBRFS.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        radioBRFS.setText("Binary Relevance attribute selection");
        radioBRFS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioBRFSActionPerformed(evt);
            }
        });

        textBRFS.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        textBRFS.setText("100");
        textBRFS.setEnabled(false);

        labelBRFS.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        labelBRFS.setText("features");

        labelBRFSComb.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        labelBRFSComb.setText("Comb");
        labelBRFSComb.setEnabled(false);

        jComboBoxBRFSComb.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jComboBoxBRFSComb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "max", "min", "avg" }));
        jComboBoxBRFSComb.setEnabled(false);
        jComboBoxBRFSComb.setPreferredSize(new java.awt.Dimension(58, 20));

        labelBRFSNorm.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        labelBRFSNorm.setText("Norm");
        labelBRFSNorm.setEnabled(false);

        jComboBoxBRFSNorm.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jComboBoxBRFSNorm.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dm", "dl", "none" }));
        jComboBoxBRFSNorm.setEnabled(false);
        jComboBoxBRFSNorm.setPreferredSize(new java.awt.Dimension(63, 20));

        labelBRFSOut.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        labelBRFSOut.setText("Score");
        labelBRFSOut.setEnabled(false);

        jComboBoxBRFSOut.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jComboBoxBRFSOut.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "eval", "rank" }));
        jComboBoxBRFSOut.setEnabled(false);
        jComboBoxBRFSOut.setPreferredSize(new java.awt.Dimension(59, 20));

        radioRandomFS.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        radioRandomFS.setText("Random attribute selection");
        radioRandomFS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioRandomFSActionPerformed(evt);
            }
        });

        textRandomFS.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        textRandomFS.setText("100");
        textRandomFS.setEnabled(false);

        labelRandomFS.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        labelRandomFS.setText("features");

        radioNoFS.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        radioNoFS.setText("None");
        radioNoFS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioNoFSActionPerformed(evt);
            }
        });

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
                        .addGap(44, 44, 44)
                        .addComponent(labelBRFSComb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxBRFSComb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(labelBRFSNorm)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxBRFSNorm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(labelBRFSOut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxBRFSOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(114, Short.MAX_VALUE))
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
                    .addComponent(labelBRFSComb)
                    .addComponent(jComboBoxBRFSComb, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelBRFSNorm)
                    .addComponent(jComboBoxBRFSNorm, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelBRFSOut)
                    .addComponent(jComboBoxBRFSOut, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelFSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioRandomFS)
                    .addComponent(textRandomFS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelRandomFS))
                .addContainerGap())
        );

        jComboBoxSaveFormat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mulan .arff", "Meka .arff" }));
        jComboBoxSaveFormat.setEnabled(false);

        panelIS.setBorder(javax.swing.BorderFactory.createTitledBorder("Instance Selection"));

        radioRandomIS.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        radioRandomIS.setText("Random instance selection");
        radioRandomIS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioRandomISActionPerformed(evt);
            }
        });

        textRandomIS.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        textRandomIS.setText("500");
        textRandomIS.setEnabled(false);

        labelRandomIS.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        labelRandomIS.setText("instances");

        radioNoIS.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
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
                .addContainerGap()
                .addGroup(panelPreprocessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPreprocessLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jButtonStartPreprocess, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSaveDatasets)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxSaveFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelPreprocessLayout.createSequentialGroup()
                        .addGroup(panelPreprocessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelFS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelSplitting, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelIS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        panelPreprocessLayout.setVerticalGroup(
            panelPreprocessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPreprocessLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelIS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelFS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelSplitting, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPreprocessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonStartPreprocess)
                    .addComponent(jButtonSaveDatasets)
                    .addComponent(jComboBoxSaveFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(191, Short.MAX_VALUE))
        );

        panelSplitting.getAccessibleContext().setAccessibleName("");

        TabPrincipal.addTab("Preprocess", panelPreprocess);

        jButtonStartTrans.setText("Transform");
        jButtonStartTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStartTransActionPerformed(evt);
            }
        });

        jButtonSaveDatasetsTrans.setText("Save");
        jButtonSaveDatasetsTrans.setEnabled(false);
        jButtonSaveDatasetsTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveDatasetsTransActionPerformed(evt);
            }
        });

        panelTransformationChoose.setBorder(javax.swing.BorderFactory.createTitledBorder("Transformation methods"));

        radioLPTrans.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        radioLPTrans.setText("Label Powerset transformation");
        radioLPTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioLPTransActionPerformed(evt);
            }
        });

        radioBRTrans.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        radioBRTrans.setText("Binary Relevance transformation");
        radioBRTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioBRTransActionPerformed(evt);
            }
        });

        radioIncludeLabelsTrans.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        radioIncludeLabelsTrans.setText("Include Labels transformation");
        radioIncludeLabelsTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioIncludeLabelsTransActionPerformed(evt);
            }
        });

        radioRemoveLabelsTrans.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        radioRemoveLabelsTrans.setText("Remove All Labels transformation");
        radioRemoveLabelsTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioRemoveLabelsTransActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTransformationChooseLayout = new javax.swing.GroupLayout(panelTransformationChoose);
        panelTransformationChoose.setLayout(panelTransformationChooseLayout);
        panelTransformationChooseLayout.setHorizontalGroup(
            panelTransformationChooseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransformationChooseLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTransformationChooseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radioLPTrans)
                    .addComponent(radioBRTrans)
                    .addComponent(radioIncludeLabelsTrans)
                    .addComponent(radioRemoveLabelsTrans))
                .addContainerGap(608, Short.MAX_VALUE))
        );
        panelTransformationChooseLayout.setVerticalGroup(
            panelTransformationChooseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransformationChooseLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radioBRTrans)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(radioLPTrans)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(radioIncludeLabelsTrans)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(radioRemoveLabelsTrans)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelTransformationLayout = new javax.swing.GroupLayout(panelTransformation);
        panelTransformation.setLayout(panelTransformationLayout);
        panelTransformationLayout.setHorizontalGroup(
            panelTransformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransformationLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTransformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTransformationLayout.createSequentialGroup()
                        .addComponent(panelTransformationChoose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(panelTransformationLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jButtonStartTrans, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSaveDatasetsTrans, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(625, Short.MAX_VALUE))))
        );
        panelTransformationLayout.setVerticalGroup(
            panelTransformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransformationLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTransformationChoose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelTransformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonStartTrans)
                    .addComponent(jButtonSaveDatasetsTrans))
                .addContainerGap(302, Short.MAX_VALUE))
        );

        TabPrincipal.addTab("Transformation", panelTransformation);

        comboBoxLabelsInformation.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Label frequency", "Labelset frequency", "Labels histogram", "Box diagram", "IR inter class", "IR intra class", "IR per labelset" }));
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

        export2.setText("Save");
        export2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                export2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelImbalanceLeftLayout = new javax.swing.GroupLayout(panelImbalanceLeft);
        panelImbalanceLeft.setLayout(panelImbalanceLeftLayout);
        panelImbalanceLeftLayout.setHorizontalGroup(
            panelImbalanceLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelImbalanceLeftLayout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelImbalanceLeftLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(export2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelImbalanceLeftLayout.setVerticalGroup(
            panelImbalanceLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelImbalanceLeftLayout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(export2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            .addGap(0, 540, Short.MAX_VALUE)
        );
        panelExamplesPerLabelLayout.setVerticalGroup(
            panelExamplesPerLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 469, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("", panelExamplesPerLabel);

        javax.swing.GroupLayout panelExamplesPerLabelsetLayout = new javax.swing.GroupLayout(panelExamplesPerLabelset);
        panelExamplesPerLabelset.setLayout(panelExamplesPerLabelsetLayout);
        panelExamplesPerLabelsetLayout.setHorizontalGroup(
            panelExamplesPerLabelsetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 540, Short.MAX_VALUE)
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
            .addGap(0, 540, Short.MAX_VALUE)
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
            .addGap(0, 540, Short.MAX_VALUE)
        );
        panelIRperLabelIntraClassLayout.setVerticalGroup(
            panelIRperLabelIntraClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("", panelIRperLabelIntraClass);

        panelIRperLabelset.setEnabled(false);

        javax.swing.GroupLayout panelIRperLabelsetLayout = new javax.swing.GroupLayout(panelIRperLabelset);
        panelIRperLabelset.setLayout(panelIRperLabelsetLayout);
        panelIRperLabelsetLayout.setHorizontalGroup(
            panelIRperLabelsetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 540, Short.MAX_VALUE)
        );
        panelIRperLabelsetLayout.setVerticalGroup(
            panelIRperLabelsetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("", panelIRperLabelset);

        javax.swing.GroupLayout panelBoxDiagramLayout = new javax.swing.GroupLayout(panelBoxDiagram);
        panelBoxDiagram.setLayout(panelBoxDiagramLayout);
        panelBoxDiagramLayout.setHorizontalGroup(
            panelBoxDiagramLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 540, Short.MAX_VALUE)
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
            .addGap(0, 540, Short.MAX_VALUE)
        );
        panelIRperLabelInterClassLayout.setVerticalGroup(
            panelIRperLabelInterClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );

        tabsImbalance.addTab("", panelIRperLabelInterClass);

        jLabelIR.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabelIR.setText("label IR");
        jLabelIR.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panelLabelsLayout = new javax.swing.GroupLayout(panelLabels);
        panelLabels.setLayout(panelLabelsLayout);
        panelLabelsLayout.setHorizontalGroup(
            panelLabelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLabelsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLabelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLabelsLayout.createSequentialGroup()
                        .addComponent(panelImbalanceLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(panelLabelsLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabelIR)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(panelLabelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboBoxLabelsInformation, javax.swing.GroupLayout.PREFERRED_SIZE, 553, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tabsImbalance, javax.swing.GroupLayout.PREFERRED_SIZE, 561, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38))
        );
        panelLabelsLayout.setVerticalGroup(
            panelLabelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLabelsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLabelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLabelsLayout.createSequentialGroup()
                        .addComponent(comboBoxLabelsInformation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tabsImbalance, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(panelLabelsLayout.createSequentialGroup()
                        .addComponent(panelImbalanceLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelIR)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        panelImbalanceLeft.getAccessibleContext().setAccessibleName("");

        TabPrincipal.addTab("Labels", panelLabels);

        comboBoxAttributeInformation.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Box diagram for numeric attributes" }));
        comboBoxAttributeInformation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxAttributeInformationActionPerformed(evt);
            }
        });

        tableAttributesLeft.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableAttributesLeft.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableAttributesLeftMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tableAttributesLeft);

        javax.swing.GroupLayout panelAttributeLeftLayout = new javax.swing.GroupLayout(panelAttributeLeft);
        panelAttributeLeft.setLayout(panelAttributeLeftLayout);
        panelAttributeLeftLayout.setHorizontalGroup(
            panelAttributeLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAttributeLeftLayout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelAttributeLeftLayout.setVerticalGroup(
            panelAttributeLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAttributeLeftLayout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(86, Short.MAX_VALUE))
        );

        tabsAttributes.setTabPlacement(javax.swing.JTabbedPane.RIGHT);
        tabsAttributes.setEnabled(false);
        tabsAttributes.setFocusable(false);
        tabsAttributes.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabsAttributesStateChanged(evt);
            }
        });

        javax.swing.GroupLayout panelBoxDiagramAttLayout = new javax.swing.GroupLayout(panelBoxDiagramAtt);
        panelBoxDiagramAtt.setLayout(panelBoxDiagramAttLayout);
        panelBoxDiagramAttLayout.setHorizontalGroup(
            panelBoxDiagramAttLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 540, Short.MAX_VALUE)
        );
        panelBoxDiagramAttLayout.setVerticalGroup(
            panelBoxDiagramAttLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 469, Short.MAX_VALUE)
        );

        tabsAttributes.addTab("", panelBoxDiagramAtt);

        javax.swing.GroupLayout panelAttributesLayout = new javax.swing.GroupLayout(panelAttributes);
        panelAttributes.setLayout(panelAttributesLayout);
        panelAttributesLayout.setHorizontalGroup(
            panelAttributesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAttributesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelAttributeLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAttributesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboBoxAttributeInformation, javax.swing.GroupLayout.PREFERRED_SIZE, 553, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tabsAttributes, javax.swing.GroupLayout.PREFERRED_SIZE, 561, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38))
        );
        panelAttributesLayout.setVerticalGroup(
            panelAttributesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAttributesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAttributesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAttributesLayout.createSequentialGroup()
                        .addComponent(comboBoxAttributeInformation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tabsAttributes, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(panelAttributesLayout.createSequentialGroup()
                        .addComponent(panelAttributeLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        TabPrincipal.addTab("Attributes", panelAttributes);

        tabsDependences.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabsDependencesStateChanged(evt);
            }
        });

        jLabelChiFiText.setText("When the Chi coefficient is > 6.635 the labels are dependent at 99% confidence (marked in red)");

        javax.swing.GroupLayout panelChiPhiLayout = new javax.swing.GroupLayout(panelChiPhi);
        panelChiPhi.setLayout(panelChiPhiLayout);
        panelChiPhiLayout.setHorizontalGroup(
            panelChiPhiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChiPhiLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabelChiFiText)
                .addContainerGap(153, Short.MAX_VALUE))
        );
        panelChiPhiLayout.setVerticalGroup(
            panelChiPhiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelChiPhiLayout.createSequentialGroup()
                .addContainerGap(429, Short.MAX_VALUE)
                .addComponent(jLabelChiFiText)
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
        jScrollPane7.setViewportView(tableCoOcurrenceLeft);

        buttonShowMostFrequent.setText("Show most frequent");
        buttonShowMostFrequent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowMostFrequentActionPerformed(evt);
            }
        });

        textMostFrequent.setText("10");

        buttonShowMostRelated.setText("Show most related");
        buttonShowMostRelated.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowMostRelatedActionPerformed(evt);
            }
        });

        textMostRelated.setText("10");

        buttonShowMostFrequentURelated.setText("Show most frequent U most related");
        buttonShowMostFrequentURelated.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowMostFrequentURelatedActionPerformed(evt);
            }
        });

        textMostFrequentURelated.setText("10");

        javax.swing.GroupLayout panelCoOcurrenceLayout = new javax.swing.GroupLayout(panelCoOcurrence);
        panelCoOcurrence.setLayout(panelCoOcurrenceLayout);
        panelCoOcurrenceLayout.setHorizontalGroup(
            panelCoOcurrenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCoOcurrenceLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCoOcurrenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCoOcurrenceLayout.createSequentialGroup()
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
                        .addComponent(panelCoOcurrenceRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelCoOcurrenceLayout.createSequentialGroup()
                        .addComponent(buttonShowMostFrequentURelated)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textMostFrequentURelated, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelCoOcurrenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonShowMostFrequentURelated)
                    .addComponent(textMostFrequentURelated, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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

        textMostFrequentHeatMap.setText("10");

        buttonShowMostFrequentURelatedHeatMap.setText("Show most frequent U most related");
        buttonShowMostFrequentURelatedHeatMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowMostFrequentURelatedHeatMapActionPerformed(evt);
            }
        });

        textMostFrequentURelatedHeatMap.setText("10");

        javax.swing.GroupLayout panelHeatmapGraphLayout = new javax.swing.GroupLayout(panelHeatmapGraph);
        panelHeatmapGraph.setLayout(panelHeatmapGraphLayout);
        panelHeatmapGraphLayout.setHorizontalGroup(
            panelHeatmapGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeatmapGraphLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelHeatmapGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelHeatmapGraphLayout.createSequentialGroup()
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
                        .addComponent(panelHeatmap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelHeatmapGraphLayout.createSequentialGroup()
                        .addComponent(buttonShowMostFrequentURelatedHeatMap)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textMostFrequentURelatedHeatMap, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelHeatmapGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonShowMostFrequentURelatedHeatMap)
                    .addComponent(textMostFrequentURelatedHeatMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        javax.swing.GroupLayout panelDependencesLayout = new javax.swing.GroupLayout(panelDependences);
        panelDependences.setLayout(panelDependencesLayout);
        panelDependencesLayout.setHorizontalGroup(
            panelDependencesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDependencesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabsDependences))
        );
        panelDependencesLayout.setVerticalGroup(
            panelDependencesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabsDependences)
        );

        TabPrincipal.addTab("Dependences", panelDependences);

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

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Multi-View Multi-Label Summary"));

        labelNumViews.setText("Number of views:");
        labelNumViews.setName(""); // NOI18N

        labelMaxNumAttrView.setText("Max number of attributes per view:");
        labelMaxNumAttrView.setName(""); // NOI18N

        labelMinNumAttrView.setText("Min number of attributes per view:");
        labelMinNumAttrView.setName(""); // NOI18N

        labelMeanNumAttrView.setText("Mean number of attributes per view:");
        labelMeanNumAttrView.setName(""); // NOI18N

        labelNumViewsValue.setText("-");
        labelNumViewsValue.setName(""); // NOI18N

        labelMaxNumAttrViewValue.setText("-");
        labelMaxNumAttrViewValue.setName(""); // NOI18N

        labelMinNumAttrViewValue.setText("-");
        labelMinNumAttrViewValue.setName(""); // NOI18N

        labelMeanNumAttrViewValue.setText("-");
        labelMeanNumAttrViewValue.setName(""); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(labelMaxNumAttrView)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labelMaxNumAttrViewValue))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(labelNumViews)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labelNumViewsValue))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(labelMinNumAttrView)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labelMinNumAttrViewValue))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(labelMeanNumAttrView)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labelMeanNumAttrViewValue)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNumViews)
                    .addComponent(labelNumViewsValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelMaxNumAttrView)
                    .addComponent(labelMaxNumAttrViewValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelMinNumAttrView)
                    .addComponent(labelMinNumAttrViewValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelMeanNumAttrView)
                    .addComponent(labelMeanNumAttrViewValue)))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Views"));

        buttonSaveViews.setText("Save views");
        buttonSaveViews.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveViewsActionPerformed(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "#Attributes", "LxIxF", "Ratio Inst/Att", "Avg Gain Ratio"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable2);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Attributes"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.setFocusable(false);
        jTable3.setRowSelectionAllowed(false);
        jScrollPane4.setViewportView(jTable3);

        jComboBox_SaveFormat1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mulan .arff", "Meka .arff" }));

        buttonSaveTable.setText("Save table");
        buttonSaveTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveTableActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(buttonSaveViews)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox_SaveFormat1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonSaveTable))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonSaveViews)
                    .addComponent(jComboBox_SaveFormat1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSaveTable))
                .addContainerGap())
        );

        javax.swing.GroupLayout panelMVMLLayout = new javax.swing.GroupLayout(panelMVML);
        panelMVML.setLayout(panelMVMLLayout);
        panelMVMLLayout.setHorizontalGroup(
            panelMVMLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMVMLLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMVMLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelMVMLLayout.setVerticalGroup(
            panelMVMLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMVMLLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        TabPrincipal.addTab("MVML", panelMVML);

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

    private void buttonRemoveMultipleDatasetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRemoveMultipleDatasetsActionPerformed
        int current = listMultipleDatasetsLeft.getSelectedIndex();

        if(current < 0){
            JOptionPane.showMessageDialog(null, "Select a dataset to remove.", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }

        listDatasets.remove(current);
        String dataName = datasetNames.get(current);
        datasetNames.remove(current);
        areMeka.remove(current);

        tableMetricsMulti.remove(dataName);

        list.remove(current);
    }//GEN-LAST:event_buttonRemoveMultipleDatasetsActionPerformed

    private void buttonAddMultipleDatasetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddMultipleDatasetsActionPerformed
        //Choose dataset
        final JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        FileNameExtensionFilter fname = new FileNameExtensionFilter(".arff", "arff");
        chooser.setFileFilter(fname);

        final int returnVal = chooser.showOpenDialog(this);

        progressBar.setIndeterminate(true);
        progressFrame.setVisible(true);
        progressFrame.repaint();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // do the long-running work here
                loadMultiDataset(returnVal, chooser);
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

    private void tabsDependencesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabsDependencesStateChanged
        if(tabsDependences.getSelectedIndex() == 0) {
            jLabelChiFiText.setVisible(true);
        }
        else {
            jLabelChiFiText.setVisible(false);
        }
    }//GEN-LAST:event_tabsDependencesStateChanged

    private void buttonShowMostRelatedHeatMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowMostRelatedHeatMapActionPerformed
        int n = Integer.parseInt(textMostRelatedHeatMap.getText());
        showMostRelatedHeatMap(n);
    }//GEN-LAST:event_buttonShowMostRelatedHeatMapActionPerformed

    private void buttonShowMostFrequentHeatMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowMostFrequentHeatMapActionPerformed
        int n = Integer.parseInt(textMostFrequentHeatMap.getText());
        showMostFrequentsHeatMap(n);
    }//GEN-LAST:event_buttonShowMostFrequentHeatMapActionPerformed

    private void buttonShowHeatMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowHeatMapActionPerformed
        showHeatMap();
    }//GEN-LAST:event_buttonShowHeatMapActionPerformed

    private void panelCoOcurrenceMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelCoOcurrenceMouseReleased
        if(evt.getButton() == MouseEvent.BUTTON3 )
        {
            jPopupMenu1.removeAll();

            JMenuItem saver = new JMenuItem("Save as...");

            saver.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    try {
                        saveCoocurrenceGraph();
                    } catch (AWTException | IOException ex) {
                        Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            jPopupMenu1.add(saver);
            jPopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_panelCoOcurrenceMouseReleased

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

        if(pairs == null)
        {
            JOptionPane.showMessageDialog(null, "You must load a dataset.", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<String> selectedsList = selectTopCoocurrenceLabels(n, true);

        ArrayList<AttributesPair> selectedPairs = AttributePairsUtils.findSelectedAttributesPair(pairs, selectedsList);

        String[] labelNames = Utils.listToArray(selectedsList);

        graphComponent = createJGraphX(panelCoOcurrenceRight,selectedPairs,labelNames,graphComponent);
    }//GEN-LAST:event_buttonShowMostRelatedActionPerformed

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

        if(pairs== null)
        {
            JOptionPane.showMessageDialog(null, "You must load a dataset.", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }

        tableCoOcurrenceLeft.setRowSelectionInterval(0, n-1);

        ArrayList<String> selectedsList = new ArrayList<>();

        String current;
        for(int i=0;i<n; i++)
        {
            current = (tableCoOcurrenceLeft.getValueAt(i, 0).toString());
            if(current != null){
                selectedsList.add(current);
            }
            else break;
        }

        ArrayList<AttributesPair> selectedPairs = AttributePairsUtils.findSelectedAttributesPair(pairs, selectedsList);

        String[] labelnames = Utils.listToArray(selectedsList);

        graphComponent  =  createJGraphX(panelCoOcurrenceRight, selectedPairs, labelnames, graphComponent);
    }//GEN-LAST:event_buttonShowMostFrequentActionPerformed

    private void buttonShowCoOcurrenceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowCoOcurrenceActionPerformed
        if(pairs== null)
        {
            JOptionPane.showMessageDialog(null, "You must load a dataset.", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<String> selectedsList = new ArrayList();
        int[] selecteds = tableCoOcurrenceLeft.getSelectedRows();

        if(selecteds.length<= 1) {
            JOptionPane.showMessageDialog(null, "You must choose two or more labels.", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for(int i=0;i<selecteds.length; i++)
        {
            selectedsList.add((tableCoOcurrenceLeft.getValueAt(selecteds[i], 0).toString()));
        }

        ArrayList<AttributesPair> selectedPairs =  AttributePairsUtils.findSelectedAttributesPair(pairs, selectedsList);

        String[] labelname = Utils.listToArray(selectedsList);

        graphComponent = createJGraphX(panelCoOcurrenceRight, selectedPairs, labelname, graphComponent);
    }//GEN-LAST:event_buttonShowCoOcurrenceActionPerformed

    private void tabsImbalanceStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabsImbalanceStateChanged

        if(tmLabelFrequency !=null && tmLabelsetFrequency!=null){

            switch (tabsImbalance.getSelectedIndex()) {
                case 0:
                    tableImbalance.setModel(tmLabelFrequency);
                    panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Label frequency"));
                    tableImbalance.setDefaultRenderer(Object.class, new DefaultRender());
                    panelImbalanceLeft.repaint();
                    panelImbalanceLeft.validate();
                    break;
                case 1:
                    tableImbalance.setModel(tmLabelsetFrequency);
                    panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Labelset frequency"));
                    tableImbalance.setDefaultRenderer(Object.class, new DefaultRender());
                    break;
                case 2:
                    tableImbalance.setModel(tmLabelsHistogram);
                    panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Labels histogram"));
                    tableImbalance.setDefaultRenderer(Object.class, new DefaultRender());
                    panelImbalanceLeft.repaint();
                    panelImbalanceLeft.validate();
                    break;
                case 3:
                    tableImbalance.setModel(tmIRIntraClass);
                    panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Imbalance Ratio intra class"));
                    tableImbalance.setDefaultRenderer(Object.class, new IRRender(1));
                    panelImbalanceLeft.repaint();
                    panelImbalanceLeft.validate();
                    break;
                case 4:
                    tableImbalance.setModel(tmIRLabelset);
                    panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Labelsets Imbalance Ratio"));
                    tableImbalance.setDefaultRenderer(Object.class, new IRRender(1));
                    panelImbalanceLeft.repaint();
                    panelImbalanceLeft.validate();
                    break;
                case 5:
                    tableImbalance.setModel(tmBox);
                    panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Box diagrams"));
                    //panelImbalanceLeft.setVisible(false);
                    tableImbalance.setDefaultRenderer(Object.class, new DefaultRender());
                    panelImbalanceLeft.repaint();
                    panelImbalanceLeft.validate();
                    break;
                case 6:
                    tableImbalance.setModel(tmIRInterClass);
                    panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Imbalance Ratio inter class"));
                    tableImbalance.setDefaultRenderer(Object.class, new IRRender(1));
                    panelImbalanceLeft.repaint();
                    panelImbalanceLeft.validate();
                    break;
                default:
                    tableImbalance.setModel(tmLabelsHistogram);
                    panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Labels histogram"));
                    tableImbalance.setDefaultRenderer(Object.class, new DefaultRender());
                    panelImbalanceLeft.repaint();
                    panelImbalanceLeft.validate();
                    break;
            }

            tableImbalance.repaint();
            tableImbalance.validate();
        }
    }//GEN-LAST:event_tabsImbalanceStateChanged

    private void export2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_export2ActionPerformed
        buttonExportActionPerformed(evt, tableImbalance);
    }//GEN-LAST:event_export2ActionPerformed

    private void tableImbalanceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableImbalanceMouseClicked
        //#Examples per labelset
        int selected;
        ArrayList<String> labelNames;
        String[] args;
        int posx;
        int posy;
        EmergentOutput mo;
        
        switch (tabsImbalance.getSelectedIndex()) {
            case 1:
                selected = tableImbalance.getSelectedRow();
                if(labelsetsSorted == null){
                    return;
                }       
                labelNames = DataInfoUtils.getLabelNamesByLabelset(dataset, labelsetsSorted[selected].getName());
                args = new String[2];
                args[0]= labelsetsSorted[selected].getName();
                args[1]= Integer.toString(labelsetsSorted[selected].getAppearances());
                posx = this.getBounds().x;
                posy = this.getBounds().y;
                mo = new EmergentOutput(dataset, posx, posy+50,args,labelNames,labelsFreqSorted, isMeka);
                mo.setVisible(true);
                break;
            case 5:
                selected = tableImbalance.getSelectedRow();
                if(selected == 0){
                    tableImbalance.clearSelection();
                    if(labelAppearances == null) {
                        return;
                    }
                        
                    double [] labelFrequencies = DataInfoUtils.getLabelAppearances(labelAppearances);
                    
                    HeapSort.sort(labelFrequencies);
                        
                    labelsBoxDiagram.getChart().setTitle("# Examples per Label");
                    labelsBoxDiagram.getChart().getXYPlot().clearAnnotations();
                        
                    ChartUtils.updateXYChart(labelsBoxDiagram, HeapSort.getSortedArray());
                }
                else if(selected == 1){
                    tableImbalance.clearSelection();
                        
                    if(labelsetsFrequency == null) {
                        return;
                    }
                        
                    HeapSort.sort(labelsetsFrequency);
                        
                    labelsBoxDiagram.getChart().setTitle("# Examples per Labelset");
                    labelsBoxDiagram.getChart().getXYPlot().clearAnnotations();
                        
                    ChartUtils.updateXYChart(labelsBoxDiagram, HeapSort.getSortedArray());
                }
                break;
            case 4:
                selected = tableImbalance.getSelectedRow();
                if(labelsetsIRSorted == null) {
                    return;
                }
                labelNames= DataInfoUtils.getLabelNamesByLabelset(dataset, labelsetsIRSorted[selected].getName());
                args = new String[2];
                args[0]= labelsetsIRSorted[selected].getName();
                args[1]= Integer.toString(labelsetsIRSorted[selected].getAppearances());
                posx = this.getBounds().x;
                posy = this.getBounds().y;
                mo = new EmergentOutput(dataset, posx, posy+50,args,labelNames,labelsFreqSorted, isMeka);
                mo.setVisible(true);
                break;
            default:
                break;
        }
    }//GEN-LAST:event_tableImbalanceMouseClicked

    private void comboBoxLabelsInformationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxLabelsInformationActionPerformed

        switch (comboBoxLabelsInformation.getSelectedIndex()) {
            case 0:
                tabsImbalance.setSelectedIndex(0);
                export2.setVisible(true);
                export2.repaint();
                jLabelIR.setVisible(false);
                jLabelIR.repaint();
                break;
            case 1:
                tabsImbalance.setSelectedIndex(1);
                export2.setVisible(true);
                export2.repaint();
                jLabelIR.setVisible(false);
                jLabelIR.repaint();
                break;
            case 2:
                tabsImbalance.setSelectedIndex(2);
                export2.setVisible(true);
                export2.repaint();
                jLabelIR.setVisible(false);
                jLabelIR.repaint();
                break;
            case 3:
                tabsImbalance.setSelectedIndex(5);
                export2.setVisible(false);
                export2.repaint();
                jLabelIR.setVisible(false);
                jLabelIR.repaint();
                break;
            case 4:
                tabsImbalance.setSelectedIndex(6);
                export2.setVisible(true);
                export2.repaint();
                jLabelIR.setText("<html>When IR > 1.5, the label is <br> imbalanced and it is marked in red</html>");
                jLabelIR.setVisible(true);
                jLabelIR.repaint();
                break;
            case 5:
                //tabsImbalance.setSelectedIndex(9);
                tabsImbalance.setSelectedIndex(3);
                export2.setVisible(true);
                export2.repaint();
                jLabelIR.setText("<html>When IR > 1.5, the label is <br> imbalanced and it is marked in red</html>");
                jLabelIR.setVisible(true);
                jLabelIR.repaint();
                break;
            case 6:
                //tabsImbalance.setSelectedIndex(3);
                tabsImbalance.setSelectedIndex(4);
                export2.setVisible(true);
                export2.repaint();
                jLabelIR.setText("<html>When IR > 1.5, the labelset is <br> imbalanced and it is marked in red</html>");
                jLabelIR.setVisible(true);
                jLabelIR.repaint();
                break;
            default:
                break;
        }
    }//GEN-LAST:event_comboBoxLabelsInformationActionPerformed

    private void radioRemoveLabelsTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioRemoveLabelsTransActionPerformed
        jButtonSaveDatasetsTrans.setEnabled(false);
    }//GEN-LAST:event_radioRemoveLabelsTransActionPerformed

    private void radioIncludeLabelsTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioIncludeLabelsTransActionPerformed
        jButtonSaveDatasetsTrans.setEnabled(false);
    }//GEN-LAST:event_radioIncludeLabelsTransActionPerformed

    private void radioBRTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioBRTransActionPerformed
        jButtonSaveDatasetsTrans.setEnabled(false);
    }//GEN-LAST:event_radioBRTransActionPerformed

    private void radioLPTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioLPTransActionPerformed
        jButtonSaveDatasetsTrans.setEnabled(false);
    }//GEN-LAST:event_radioLPTransActionPerformed

    private void jButtonSaveDatasetsTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveDatasetsTransActionPerformed
        try{
            if(dataset == null){
                JOptionPane.showMessageDialog(null, "You must load a dataset.", "alert", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if((transformedDatasets == null || transformedDatasets.isEmpty())){
                JOptionPane.showMessageDialog(null, "You must click on Start before.", "alert", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JFileChooser fc= new JFileChooser();

            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int returnVal = fc.showSaveDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                File file = fc.getSelectedFile();

                String dataName = datasetName.substring(0,datasetName.length()-5);

                if(fc.isDirectorySelectionEnabled()){
                    if(radioBRTrans.isSelected()){
                        for(int i=0; i<transformedDatasets.size(); i++){
                            ArffSaver saver = new ArffSaver();
                            saver.setInstances(transformedDatasets.get(i));
                            saver.setFile(new File(file.getAbsolutePath() + "/" + dataName + "_BRTransformed_" + i + ".arff"));
                            saver.writeBatch();
                        }
                    }
                    else if(radioLPTrans.isSelected()){
                        ArffSaver saver = new ArffSaver();
                        saver.setInstances(transformedDatasets.get(0));
                        saver.setFile(new File(file.getAbsolutePath() + "/" + dataName + "_LPTransformed" + ".arff"));
                        saver.writeBatch();
                    }
                    else if(radioRemoveLabelsTrans.isSelected())
                    {
                        ArffSaver saver = new ArffSaver();
                        saver.setInstances(transformedDatasets.get(0));
                        saver.setFile(new File(file.getAbsolutePath() + "/" + dataName + "_RemoveAllLabelsTransformed" + ".arff"));
                        saver.writeBatch();
                    }
                    else if(radioIncludeLabelsTrans.isSelected())
                    {
                        ArffSaver saver = new ArffSaver();
                        saver.setInstances(transformedDatasets.get(0));
                        saver.setFile(new File(file.getAbsolutePath() + "/" + dataName + "_IncludeLabelsTransformed" + ".arff"));
                        saver.writeBatch();
                    }

                    JOptionPane.showMessageDialog(null, "All files have been saved.", "Successful", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        catch(HeadlessException | IOException e){
            JOptionPane.showMessageDialog(null, "An error ocurred while saving the dataset files.", "alert", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, e);
        }
    }//GEN-LAST:event_jButtonSaveDatasetsTransActionPerformed

    private void jButtonStartTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStartTransActionPerformed
        progressBar.setIndeterminate(true);
        progressFrame.setVisible(true);
        progressFrame.repaint();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // do the long-running work here
                final int returnCode = transform();
                // at the end:
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setIndeterminate(false);
                        progressFrame.setVisible(false);
                        progressFrame.repaint();

                        if(returnCode == 1 && (!(radioNoFS.isSelected() && radioNoSplit.isSelected() && radioNoIS.isSelected()))){
                            JOptionPane.showMessageDialog(null, "Dataset has been transformed succesfully.", "Successful", JOptionPane.INFORMATION_MESSAGE);
                        }

                        Toolkit.getDefaultToolkit().beep();
                    }//run
                }); //invokeLater
            }
        }
        ).start();
    }//GEN-LAST:event_jButtonStartTransActionPerformed

    private void radioNoISActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioNoISActionPerformed
        textRandomIS.setEnabled(false);

        jButtonSaveDatasets.setEnabled(false);
        jComboBoxSaveFormat.setEnabled(false);
    }//GEN-LAST:event_radioNoISActionPerformed

    private void radioRandomISActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioRandomISActionPerformed
        textRandomIS.setEnabled(true);

        jButtonSaveDatasets.setEnabled(false);
        jComboBoxSaveFormat.setEnabled(false);
    }//GEN-LAST:event_radioRandomISActionPerformed

    private void radioNoFSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioNoFSActionPerformed
        textBRFS.setEnabled(false);
        labelBRFSComb.setEnabled(false);
        jComboBoxBRFSComb.setEnabled(false);
        labelBRFSNorm.setEnabled(false);
        jComboBoxBRFSNorm.setEnabled(false);
        labelBRFSOut.setEnabled(false);
        jComboBoxBRFSOut.setEnabled(false);
        textRandomFS.setEnabled(false);

        jButtonSaveDatasets.setEnabled(false);
        jComboBoxSaveFormat.setEnabled(false);
    }//GEN-LAST:event_radioNoFSActionPerformed

    private void radioRandomFSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioRandomFSActionPerformed
        textBRFS.setEnabled(false);
        labelBRFSComb.setEnabled(false);
        jComboBoxBRFSComb.setEnabled(false);
        labelBRFSNorm.setEnabled(false);
        jComboBoxBRFSNorm.setEnabled(false);
        labelBRFSOut.setEnabled(false);
        jComboBoxBRFSOut.setEnabled(false);
        textRandomFS.setEnabled(true);

        jButtonSaveDatasets.setEnabled(false);
        jComboBoxSaveFormat.setEnabled(false);
    }//GEN-LAST:event_radioRandomFSActionPerformed

    private void radioBRFSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioBRFSActionPerformed
        textBRFS.setEnabled(true);
        labelBRFSComb.setEnabled(true);
        jComboBoxBRFSComb.setEnabled(true);
        labelBRFSNorm.setEnabled(true);
        jComboBoxBRFSNorm.setEnabled(true);
        labelBRFSOut.setEnabled(true);
        jComboBoxBRFSOut.setEnabled(true);
        textRandomFS.setEnabled(false);

        jButtonSaveDatasets.setEnabled(false);
        jComboBoxSaveFormat.setEnabled(false);
    }//GEN-LAST:event_radioBRFSActionPerformed

    private void jButtonSaveDatasetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveDatasetsActionPerformed
        try{
            /*
            If only FS is selected, save FS dataset
            If any splitting method is selected, save the splitted datasets (those are FS too if it has been selected)
            */

            String format = jComboBoxSaveFormat.getSelectedItem().toString();

            if(dataset == null){
                JOptionPane.showMessageDialog(null, "You must load a dataset.", "alert", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!(radioNoFS.isSelected() && radioNoIS.isSelected() && radioNoSplit.isSelected())){
                if((trainDatasets.isEmpty() && testDatasets.isEmpty()) && (radioRandomCV.isSelected() || radioIterativeStratifiedCV.isSelected() || radioLPStratifiedCV.isSelected())){
                    JOptionPane.showMessageDialog(null, "You must click on Start before.", "alert", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if((trainDataset ==null && testDataset==null) && (radioIterativeStratifiedHoldout.isSelected()|| radioRandomHoldout.isSelected() || radioLPStratifiedHoldout.isSelected())){
                    JOptionPane.showMessageDialog(null, "You must click on Start before.", "alert", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if((preprocessedDataset == null) && (radioBRFS.isSelected() || radioRandomFS.isSelected())){
                    JOptionPane.showMessageDialog(null, "You must click on Start before.", "alert", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            //JFileChooser save
            JFileChooser fc= new JFileChooser();

            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            String trainPath, testPath, xmlPath;

            int returnVal = fc.showSaveDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                File file = fc.getSelectedFile();

                if(fc.isDirectorySelectionEnabled())
                {
                    //Check if none were selected -> Dataset conversion
                    if(radioNoFS.isSelected() && radioNoIS.isSelected() && radioNoSplit.isSelected())
                    {
                        BufferedWriter bwTrain;
                        try {

                            String dataName;
                            dataName = datasetName.substring(0,datasetName.length()-5);

                            if(format.toLowerCase().contains("meka")){
                                String dataPath = file.getAbsolutePath()+"/"+dataName+"-MekaConverted.arff";

                                bwTrain = new BufferedWriter(new FileWriter(dataPath));
                                PrintWriter wrTrain = new PrintWriter(bwTrain);

                                DataIOUtils.saveMekaDataset(wrTrain, dataset, dataset.getDataSet().relationName());

                                wrTrain.close();
                                bwTrain.close();
                            }
                            else{
                                //Paths trainPath = new Paths.get(file.getAbsolutePath() + "/" + name_dataset + "_train.arff");
                                //Paths testPath = new Paths.get(file.getAbsolutePath() + "/" + name_dataset + "_train.arff");
                                //Paths xmlPath = new Paths.get(file.getAbsolutePath() + "/" + name_dataset + "_train.arff");

                                String dataPath = file.getAbsolutePath()+"/"+dataName+"-MulanConverted.arff";
                                xmlPath = file.getAbsolutePath()+"/"+dataName+"-MulanConverted.xml";

                                bwTrain = new BufferedWriter(new FileWriter(dataPath));
                                PrintWriter wrTrain = new PrintWriter(bwTrain);
                                
                                DataIOUtils.saveDataset(wrTrain, dataset, dataset.getDataSet().relationName());

                                wrTrain.close();
                                bwTrain.close();

                                BufferedWriter bwXml = new BufferedWriter(new FileWriter(xmlPath));
                                PrintWriter wrXml = new PrintWriter(bwXml);

                                DataIOUtils.saveXMLFile(wrXml, dataset);

                                wrXml.close();
                                bwXml.close();
                            }

                            JOptionPane.showMessageDialog(null, "All files have been saved.", "Successful", JOptionPane.INFORMATION_MESSAGE);

                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "An error ocurred while saving the dataset files.", "alert", JOptionPane.ERROR_MESSAGE);
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

                        BufferedWriter bwTrain;
                        try {

                            String dataName = datasetName.substring(0,datasetName.length()-5);

                            if(format.toLowerCase().contains("meka")){
                                String dataPath = file.getAbsolutePath()+"/"+dataName+ preprocessedType + ".arff";

                                bwTrain = new BufferedWriter(new FileWriter(dataPath));
                                PrintWriter wrTrain = new PrintWriter(bwTrain);

                                if(radioNoFS.isSelected()){
                                    DataIOUtils.saveMekaDataset(wrTrain, preprocessedDataset, preprocessedDataset.getDataSet().relationName() + preprocessedType);
                                }
                                else{
                                    DataIOUtils.saveMekaDataset(wrTrain, preprocessedDataset, dataName + preprocessedType);
                                }

                                wrTrain.close();
                                bwTrain.close();
                            }
                            else{
                                //Paths trainPath = new Paths.get(file.getAbsolutePath() + "/" + name_dataset + "_train.arff");
                                //Paths testPath = new Paths.get(file.getAbsolutePath() + "/" + name_dataset + "_train.arff");
                                //Paths xmlPath = new Paths.get(file.getAbsolutePath() + "/" + name_dataset + "_train.arff");

                                String dataPath = file.getAbsolutePath()+"/"+dataName+ preprocessedType + ".arff";
                                xmlPath = file.getAbsolutePath()+"/"+dataName+ preprocessedType +".xml";

                                bwTrain = new BufferedWriter(new FileWriter(dataPath));
                                PrintWriter wrTrain = new PrintWriter(bwTrain);
                                
                                if(radioNoFS.isSelected()){
                                    DataIOUtils.saveDataset(wrTrain, preprocessedDataset, preprocessedDataset.getDataSet().relationName() + preprocessedType);
                                }
                                else{
                                    DataIOUtils.saveDataset(wrTrain, preprocessedDataset, dataName+ preprocessedType);
                                }

                                wrTrain.close();
                                bwTrain.close();

                                BufferedWriter bw_xml = new BufferedWriter(new FileWriter(xmlPath));
                                PrintWriter wr_xml = new PrintWriter(bw_xml);

                                DataIOUtils.saveXMLFile(wr_xml,preprocessedDataset);

                                wr_xml.close();
                                bw_xml.close();
                            }

                            JOptionPane.showMessageDialog(null, "All files have been saved.", "Successful", JOptionPane.INFORMATION_MESSAGE);

                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "An error ocurred while saving the dataset files.", "alert", JOptionPane.ERROR_MESSAGE);
                            Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    if(radioIterativeStratifiedHoldout.isSelected()|| radioRandomHoldout.isSelected() || radioLPStratifiedHoldout.isSelected()) //holdout
                    {
                        BufferedWriter bwTrain;
                        try {

                            String dataName = datasetName.substring(0,datasetName.length()-5);

                            //Paths trainPath = new Paths.get(file.getAbsolutePath() + "/" + name_dataset + "_train.arff");
                            //Paths testPath = new Paths.get(file.getAbsolutePath() + "/" + name_dataset + "_train.arff");
                            //Paths xmlPath = new Paths.get(file.getAbsolutePath() + "/" + name_dataset + "_train.arff");

                            if(radioNoFS.isSelected() && radioNoIS.isSelected()){
                                trainPath = file.getAbsolutePath()+"/"+dataName+"-train.arff";
                                testPath = file.getAbsolutePath()+"/"+dataName+"-test.arff";
                                xmlPath = file.getAbsolutePath()+"/"+dataName+".xml";
                            }
                            else{
                                trainPath = file.getAbsolutePath()+"/"+dataName+ preprocessedType + "-train.arff";
                                testPath = file.getAbsolutePath()+"/"+dataName+ preprocessedType + "-test.arff";
                                xmlPath = file.getAbsolutePath()+"/"+dataName+ preprocessedType + ".xml";
                            }

                            if(format.toLowerCase().contains("meka")){
                                bwTrain = new BufferedWriter(new FileWriter(trainPath));
                                PrintWriter wrTrain = new PrintWriter(bwTrain);

                                if(radioNoFS.isSelected()){
                                    DataIOUtils.saveMekaDataset(wrTrain, trainDataset, trainDataset.getDataSet().relationName());
                                }
                                else{
                                    DataIOUtils.saveMekaDataset(wrTrain, trainDataset, dataName);
                                }

                                wrTrain.close();
                                bwTrain.close();

                                BufferedWriter bwTest = new BufferedWriter(new FileWriter(testPath));
                                PrintWriter wrTest = new PrintWriter(bwTest);

                                if(radioNoFS.isSelected()){
                                    DataIOUtils.saveMekaDataset(wrTest, testDataset, testDataset.getDataSet().relationName());
                                }
                                else{
                                    DataIOUtils.saveMekaDataset(wrTest, testDataset, dataName);
                                }
                                
                                wrTest.close();
                                bwTest.close();
                            }
                            else{
                                bwTrain = new BufferedWriter(new FileWriter(trainPath));
                                PrintWriter wrTrain = new PrintWriter(bwTrain);

                                if(radioNoFS.isSelected()){
                                    DataIOUtils.saveDataset(wrTrain, trainDataset, trainDataset.getDataSet().relationName());
                                }
                                else{
                                    DataIOUtils.saveDataset(wrTrain, trainDataset, dataName);
                                }

                                wrTrain.close();
                                bwTrain.close();

                                BufferedWriter bwTest = new BufferedWriter(new FileWriter(testPath));
                                PrintWriter wrTest = new PrintWriter(bwTest);

                                if(radioNoFS.isSelected()){
                                    DataIOUtils.saveDataset(wrTest, testDataset, testDataset.getDataSet().relationName());
                                }
                                else{
                                    DataIOUtils.saveDataset(wrTest, testDataset, dataName);
                                }
                                
                                wrTest.close();
                                bwTest.close();

                                BufferedWriter bwXml = new BufferedWriter(new FileWriter(xmlPath));
                                PrintWriter wrXml = new PrintWriter(bwXml);

                                DataIOUtils.saveXMLFile(wrXml, trainDataset);

                                wrXml.close();
                                bwXml.close();
                            }

                            JOptionPane.showMessageDialog(null, "All files have been saved.", "Successful", JOptionPane.INFORMATION_MESSAGE);

                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "An error ocurred while saving the dataset files.", "alert", JOptionPane.ERROR_MESSAGE);
                            Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                    else if(radioIterativeStratifiedCV.isSelected()|| radioRandomCV.isSelected() || radioLPStratifiedCV.isSelected())//CROSS VALIDATION
                    {
                        try{

                            if(format.toLowerCase().contains("meka")){
                                if(radioNoFS.isSelected() && radioNoIS.isSelected()){
                                    DataIOUtils.saveMekaDataset(trainDatasets,file.getAbsolutePath(), datasetName.substring(0,datasetName.length()-5), "-train");
                                    DataIOUtils.saveMekaDataset(testDatasets,file.getAbsolutePath(), datasetName.substring(0,datasetName.length()-5), "-test");
                                }
                                if(radioNoFS.isSelected()){
                                    DataIOUtils.saveMekaDataset(trainDatasets,file.getAbsolutePath(), datasetName.substring(0,datasetName.length()-5),  preprocessedType + "-train");
                                    DataIOUtils.saveMekaDataset(testDatasets,file.getAbsolutePath(), datasetName.substring(0,datasetName.length()-5),  preprocessedType + "-test");
                                }
                                else{
                                    DataIOUtils.saveMekaDatasetsNoViews(trainDatasets,file.getAbsolutePath(), datasetName.substring(0,datasetName.length()-5),  preprocessedType + "-train");
                                    DataIOUtils.saveMekaDatasetsNoViews(testDatasets,file.getAbsolutePath(), datasetName.substring(0,datasetName.length()-5),  preprocessedType + "-test");
                                }
                            }
                            else{
                                if(radioNoFS.isSelected() && radioNoIS.isSelected()){
                                    DataIOUtils.saveDatasets(trainDatasets,file.getAbsolutePath(), datasetName.substring(0,datasetName.length()-5), "-train");
                                    DataIOUtils.saveDatasets(testDatasets,file.getAbsolutePath(), datasetName.substring(0,datasetName.length()-5), "-test");
                                    xmlPath = file.getAbsolutePath()+"/"+datasetName.substring(0,datasetName.length()-5)+".xml";
                                }
                                else if(radioNoFS.isSelected()){
                                    DataIOUtils.saveDatasets(trainDatasets,file.getAbsolutePath(), datasetName.substring(0,datasetName.length()-5),  preprocessedType + "-train");
                                    DataIOUtils.saveDatasets(testDatasets,file.getAbsolutePath(), datasetName.substring(0,datasetName.length()-5),  preprocessedType + "-test");
                                    xmlPath = file.getAbsolutePath()+"/"+datasetName.substring(0,datasetName.length()-5)+ preprocessedType + ".xml";
                                }
                                else{
                                    DataIOUtils.saveMVDatasets(trainDatasets,file.getAbsolutePath(), datasetName.substring(0,datasetName.length()-5),  preprocessedType + "-train");
                                    DataIOUtils.saveMVDatasets(testDatasets,file.getAbsolutePath(), datasetName.substring(0,datasetName.length()-5),  preprocessedType + "-test");
                                    xmlPath = file.getAbsolutePath()+"/"+datasetName.substring(0,datasetName.length()-5)+ preprocessedType + ".xml";
                                }

                                BufferedWriter bwXml = new BufferedWriter(new FileWriter(xmlPath));
                                PrintWriter wrXml = new PrintWriter(bwXml);

                                DataIOUtils.saveXMLFile(wrXml,trainDatasets.get(0));

                                wrXml.close();
                                bwXml.close();
                            }

                            JOptionPane.showMessageDialog(null, "All files have been saved.", "Successful", JOptionPane.INFORMATION_MESSAGE);

                        }
                        catch(IOException | HeadlessException e1){
                            JOptionPane.showMessageDialog(null, "An error ocurred while saving the dataset files.", "alert", JOptionPane.ERROR_MESSAGE);
                            e1.printStackTrace();
                        }
                    }

                    Toolkit.getDefaultToolkit().beep();
                }
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "An error ocurred while saving the dataset files.", "alert", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonSaveDatasetsActionPerformed

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

    private void radioNoSplitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioNoSplitActionPerformed
        textRandomHoldout.setEnabled(false);
        textIterativeStratifiedHoldout.setEnabled(false);
        textLPStratifiedHoldout.setEnabled(false);
        textRandomCV.setEnabled(false);
        textIterativeStratifiedCV.setEnabled(false);
        textLPStratifiedCV.setEnabled(false);

        jButtonSaveDatasets.setEnabled(false);
        jComboBoxSaveFormat.setEnabled(false);

    }//GEN-LAST:event_radioNoSplitActionPerformed

    private void radioLPStratifiedCVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioLPStratifiedCVActionPerformed
        textRandomHoldout.setEnabled(false);
        textIterativeStratifiedHoldout.setEnabled(false);
        textLPStratifiedHoldout.setEnabled(false);
        textRandomCV.setEnabled(false);
        textIterativeStratifiedCV.setEnabled(false);
        textLPStratifiedCV.setEnabled(true);

        jButtonSaveDatasets.setEnabled(false);
        jComboBoxSaveFormat.setEnabled(false);
    }//GEN-LAST:event_radioLPStratifiedCVActionPerformed

    private void radioLPStratifiedHoldoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioLPStratifiedHoldoutActionPerformed
        textRandomHoldout.setEnabled(false);
        textIterativeStratifiedHoldout.setEnabled(false);
        textLPStratifiedHoldout.setEnabled(true);
        textRandomCV.setEnabled(false);
        textIterativeStratifiedCV.setEnabled(false);
        textLPStratifiedCV.setEnabled(false);

        jButtonSaveDatasets.setEnabled(false);
        jComboBoxSaveFormat.setEnabled(false);
    }//GEN-LAST:event_radioLPStratifiedHoldoutActionPerformed

    private void radioIterativeStratifiedCVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioIterativeStratifiedCVActionPerformed
        textRandomHoldout.setEnabled(false);
        textIterativeStratifiedHoldout.setEnabled(false);
        textLPStratifiedHoldout.setEnabled(false);
        textRandomCV.setEnabled(false);
        textIterativeStratifiedCV.setEnabled(true);
        textLPStratifiedCV.setEnabled(false);

        jButtonSaveDatasets.setEnabled(false);
        jComboBoxSaveFormat.setEnabled(false);
    }//GEN-LAST:event_radioIterativeStratifiedCVActionPerformed

    private void radioRandomCVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioRandomCVActionPerformed
        textRandomHoldout.setEnabled(false);
        textIterativeStratifiedHoldout.setEnabled(false);
        textLPStratifiedHoldout.setEnabled(false);
        textRandomCV.setEnabled(true);
        textIterativeStratifiedCV.setEnabled(false);
        textLPStratifiedCV.setEnabled(false);

        jButtonSaveDatasets.setEnabled(false);
        jComboBoxSaveFormat.setEnabled(false);
    }//GEN-LAST:event_radioRandomCVActionPerformed

    private void radioIterativeStratifiedHoldoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioIterativeStratifiedHoldoutActionPerformed
        textIterativeStratifiedHoldout.setEnabled(true);
        textRandomHoldout.setEnabled(false);
        textRandomCV.setEnabled(false);
        textIterativeStratifiedCV.setEnabled(false);
        textLPStratifiedHoldout.setEnabled(false);
        textLPStratifiedCV.setEnabled(false);

        jButtonSaveDatasets.setEnabled(false);
        jComboBoxSaveFormat.setEnabled(false);
    }//GEN-LAST:event_radioIterativeStratifiedHoldoutActionPerformed

    private void radioRandomHoldoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioRandomHoldoutActionPerformed
        textRandomHoldout.setEnabled(true);
        textIterativeStratifiedHoldout.setEnabled(false);
        textRandomCV.setEnabled(false);
        textIterativeStratifiedCV.setEnabled(false);
        textLPStratifiedHoldout.setEnabled(false);
        textLPStratifiedCV.setEnabled(false);

        jButtonSaveDatasets.setEnabled(false);
        jComboBoxSaveFormat.setEnabled(false);
    }//GEN-LAST:event_radioRandomHoldoutActionPerformed

    private void textChooseFileKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textChooseFileKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            String arffFilename = textChooseFile.getText();
            xmlFilename = DataIOUtils.getXMLString(arffFilename);
            xmlFilename = DataIOUtils.getFilePath(xmlFilename);
            loadDataset(arffFilename, xmlFilename);
        }
    }//GEN-LAST:event_textChooseFileKeyPressed

    private void buttonChooseFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonChooseFileActionPerformed
        final JFileChooser jfile1 = new JFileChooser();
        jfile1.setLocale(Locale.UK);
        jfile1.repaint();
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

    private void comboBoxAttributeInformationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxAttributeInformationActionPerformed
        if(comboBoxAttributeInformation.getSelectedIndex() == 0){
            //Box diagram
            tabsAttributes.setSelectedIndex(0);
        }
    }//GEN-LAST:event_comboBoxAttributeInformationActionPerformed

    private void tableAttributesLeftMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableAttributesLeftMouseClicked
        if(tabsAttributes.getSelectedIndex()==0)
        {
            int selected = tableAttributesLeft.getSelectedRow();

            String attr= tableAttributesLeft.getValueAt(selected, 0).toString();

            Instances instances = dataset.getDataSet();

            Attribute currentAttr = instances.attribute(attr);

            double[] attributeValues= instances.attributeToDoubleArray(currentAttr.index());

            HeapSort.sort(attributeValues);

            attributesBoxDiagram2.getChart().setTitle(currentAttr.name());

            attributesBoxDiagram2.getChart().getXYPlot().clearAnnotations();
            
            ChartUtils.updateXYChart(attributesBoxDiagram2, HeapSort.getSortedArray());
        }
    }//GEN-LAST:event_tableAttributesLeftMouseClicked

    private void tabsAttributesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabsAttributesStateChanged
        //tmAttributes = jtable_attributes(tableAttributesLeft, dataset);
        
        if (tabsAttributes.getSelectedIndex()==0)
        {
//          tableAttributesLeft.setModel(tmAttributes);
            panelAttributeLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Numeric attributes"));

            tableAttributesLeft.setDefaultRenderer(Object.class, new DefaultRender());
            panelAttributeLeft.repaint();
            panelAttributeLeft.validate();
        }
    }//GEN-LAST:event_tabsAttributesStateChanged

    private void buttonShowMostFrequentURelatedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowMostFrequentURelatedActionPerformed
        int n = Integer.parseInt(textMostFrequentURelated.getText());

        if(n > dataset.getNumLabels()){
            JOptionPane.showMessageDialog(null, "The number of labels to show must be less than the number of labels in the dataset.", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if(n < 2){
            JOptionPane.showMessageDialog(null, "Select at least 2 labels.", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(pairs== null)
        {
            JOptionPane.showMessageDialog(null, "You must load a dataset.", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //tableCoOcurrenceLeft.setRowSelectionInterval(0, n-1);
        
        ArrayList<String> selecteds = new ArrayList();

        String current;
        for(int i=0;i<n; i++)
        {
            current = (tableCoOcurrenceLeft.getValueAt(i, 0).toString());
            if(current != null){
                selecteds.add(current);
            }
            else {
                break;
            }
        }
        
        ArrayList<String> topCoocurrenceSelected = selectTopCoocurrenceLabels(n, true);
        for(int i=0; i<n; i++){
            if(! selecteds.contains(topCoocurrenceSelected.get(i))){
                selecteds.add(topCoocurrenceSelected.get(i));
            }
        }
        tableCoOcurrenceLeft.addRowSelectionInterval(0, n-1);

        ArrayList<AttributesPair> selectedPairs =  AttributePairsUtils.findSelectedAttributesPair(pairs, selecteds);

        String[] labelname = Utils.listToArray(selecteds);

        graphComponent = createJGraphX(panelCoOcurrenceRight,selectedPairs,labelname,graphComponent);
    }//GEN-LAST:event_buttonShowMostFrequentURelatedActionPerformed

    private void buttonShowMostFrequentURelatedHeatMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowMostFrequentURelatedHeatMapActionPerformed
        int n = Integer.parseInt(textMostFrequentURelatedHeatMap.getText());
        showMostFrequentURelatedHeatMap(n);
    }//GEN-LAST:event_buttonShowMostFrequentURelatedHeatMapActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        /*for(int i=0; i<((DefaultTableModel)jTable3.getModel()).getRowCount(); i++){
            ((DefaultTableModel)jTable3.getModel()).removeRow(i);
        }*/

        ((DefaultTableModel)jTable3.getModel()).getDataVector().removeAllElements();
        
        int [] selected = jTable2.getSelectedRows();
        
        for(int i=0; i<selected.length; i++){
            Integer [] indices = views.get("View " + (selected[i]+1));
            for(int j=0; j<indices.length; j++){
                ((DefaultTableModel)jTable3.getModel()).addRow(new Object[]{dataset.getDataSet().attribute(indices[j]).name()});
            }
        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void buttonSaveViewsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveViewsActionPerformed
        progressBar.setIndeterminate(true);
        progressFrame.setVisible(true);
        progressFrame.repaint();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // do the long-running work here
                final int returnCode = saveMultiView();
                // at the end:
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setIndeterminate(false);
                        progressFrame.setVisible(false);
                        progressFrame.repaint();

                        if(returnCode == 1){
                            JOptionPane.showMessageDialog(null, "Dataset saved succesfully.", "Successful", JOptionPane.INFORMATION_MESSAGE);
                        }

                        Toolkit.getDefaultToolkit().beep();
                    }//run
                }); //invokeLater
            }
        }
        ).start();
    }//GEN-LAST:event_buttonSaveViewsActionPerformed

    private void buttonSaveTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveTableActionPerformed
        if(jTable2.getRowCount()==0 || dataset == null)
        {
            JOptionPane.showMessageDialog(null, "The table is empty.", "Error", JOptionPane.ERROR_MESSAGE); 
            return;
        }

        JFileChooser fc= new JFileChooser();
        
        //FileNameExtensionFilter fname = new FileNameExtensionFilter(".xls", "xls"); 
        FileNameExtensionFilter fname1 =  new FileNameExtensionFilter(".csv", "csv");
        
        //Remove default
        fc.removeChoosableFileFilter(fc.getChoosableFileFilters()[0]);
        
        fc.setFileFilter(fname1);
        
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
                    
                    ResultsIOUtils.saveMVTableCsv(wr, jTable2, views, dataset);
                                    
                    wr.close();
                    bw.close(); 
                    
                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE);                     
                }
                catch(IOException | HeadlessException e1)
                {
                    JOptionPane.showMessageDialog(null, "File not saved correctly.", "Error", JOptionPane.ERROR_MESSAGE); 
                }                 
            }
        }
    }//GEN-LAST:event_buttonSaveTableActionPerformed
    
    /**
     * Creates button for exporting table of dependences
     * 
     * @param jtable Table of dependences
     * @param columns Columns
     * @param jpanel Panel
     * @param jButtonExport Export button
     * @param posx Position x
     * @param posy Position y
     * @param table Table type
     */
    private void createButtonExportDependencesTable(final JTable jtable, 
            final JTable columns, JPanel jpanel, JButton jButtonExport, 
            int posx, int posy, final String table)
    {
        jButtonExport = new JButton("Save");
        jButtonExport.setBounds(posx, posy, 80, 25);

        switch (table) {
            case "ChiPhi":
                jButtonExport.setToolTipText("Save table with Chi and Phi coefficients");
                break;
            case "Coocurrence":
                jButtonExport.setToolTipText("Save table with co-ocurrence values");
                break;
            case "Heatmap":
                jButtonExport.setToolTipText("Save table with heatmap values");
                break;
            default:
                break;
        }

        jButtonExport.addActionListener(new java.awt.event.ActionListener() {
              @Override
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                  buttonExportActionPerformed(evt,jtable,columns, table);
                              }
          });
        jpanel.add(jButtonExport);
    }    
        
    /**
     * Creates button for exporting dependences chart
     * 
     * @param jpanel Panel of dependences
     * @param jButtonExport Export button
     * @param posx Position x
     * @param posy Position Y
     */
    private void createButtonExportDependencesChart(final JPanel jpanel, 
            JButton jButtonExport, int posx, int posy)
    {
        //button export table
        jButtonExport = new JButton("Save");
        jButtonExport.setBounds(posx, posy, 80, 25);
        jButtonExport.setToolTipText("Save graph as image");

        jButtonExport.addActionListener(new java.awt.event.ActionListener() {
              @Override
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(jpanel.getName().equals("jpanel25")) try {  
                    saveCoocurrenceGraph();
                } catch (AWTException | IOException ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                }  
                else try {
                    saveHeatmapGraph();
                } catch (AWTException | IOException ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                }  
                              }
          });
        jpanel.add(jButtonExport);
    }
    
    /**
     * Creates table of metrics and its buttons
     * 
     * @param jtable Table with metrics
     * @param jpanel Panel
     * @param buttonAll "All" button
     * @param buttonNone "None" button
     * @param buttonInvert "Invert" button
     * @param buttonCalculate "Calculate" button
     * @param buttonSave "Save" button
     * @param buttonClear "Clear" button
     * @param posx Position x
     * @param posy Position Y
     * @param width Width
     * @param height Height
     */
    private void createMetricsTable(final JTable jtable,
            JPanel jpanel, JButton buttonAll, JButton buttonNone, 
            JButton buttonInvert, JButton buttonCalculate, JButton buttonSave, 
            JButton buttonClear, int posx, int posy, int width, int height)
    {
        createJTableMetricsPrincipal(jtable, jpanel, MetricUtils.getRowData(), posx, posy, width, height);        

        //button All
        buttonAll = new JButton("All");
        buttonAll.setBounds(posx, posy+height+5, 80, 20);
        buttonAll.setToolTipText("Select all metrics");
        buttonAll.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAllActionPerformedPrincipal(evt,jtable );
            }
        });
        jpanel.add(buttonAll);
      
        //button None
        buttonNone = new JButton("None");
        buttonNone.setToolTipText("Deselect all metrics");
        buttonNone.setBounds(posx+90, posy+height+5, 80, 20);

        buttonNone.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNoneActionPerformedPrincipal(evt,jtable);
            }
        });
        jpanel.add(buttonNone);

        //button Invert
        buttonInvert = new JButton("Invert");
        buttonInvert.setToolTipText("Invert selection");
        buttonInvert.setBounds(posx+180, posy+height+5, 80, 20);

        buttonInvert.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonInvertActionButtonPerformed(evt,jtable);
            }
        });
        jpanel.add(buttonInvert);

        //button Clear
        buttonClear = new JButton("Clear");
        buttonClear.setToolTipText("Clear selection and metric values");
        buttonClear.setBounds(posx+270, posy+height+5, 80, 20);

        buttonClear.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonClearActionPerformedPrincipal(evt,jtable);
            }
        });
        jpanel.add(buttonClear);

        //button Calculate
        buttonCalculate = new JButton("Calculate");
        buttonCalculate.setBounds(posx+590, posy+height+5, 95, 25);
        buttonCalculate.setToolTipText("Calculate selected metrics");

        buttonCalculate.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent evt) {
                progressBar.setIndeterminate(false);
                progressFrame.setVisible(true);

                progressFrame.repaint();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // do the long-running work here
                        buttonCalculateActionPerformedPrincipal(evt,jtable);
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
            } //actionPerformed
        });//ActionListener
        jpanel.add(buttonCalculate);

        //button Save
        buttonSave = new JButton("Save");
        buttonSave.setBounds(posx+695, posy+height+5, 80, 25);
        buttonSave.setToolTipText("Save selected metrics in a file");

        buttonSave.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    buttonSaveActionPerformedPrincipal(evt,jtable);
                } catch (IOException ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        jpanel.add(buttonSave);
    }
    
    /**
     * Creates multiple datasets table of metrics and its buttons
     * 
     * @param jtable Table with metrics
     * @param jpanel Panel
     * @param buttonAll "All" button
     * @param buttonNone "None" button
     * @param buttonInvert "Invert" button
     * @param buttonCalculate "Calculate" button
     * @param buttonSave "Save" button
     * @param posx Position X
     * @param posy Position Y
     * @param width Width
     * @param height Height
     */
    private void createMultiMetricsTable(final JTable jtable,
            JPanel jpanel, JButton buttonAll, JButton buttonNone, 
            JButton buttonInvert, JButton buttonCalculate, JButton buttonSave, 
            int posx, int posy, int width, int height)
    {

        createJTableMetricsMultipleDatasets(jtable,jpanel, MetricUtils.getRowDataMulti(),posx,posy,width,height);  
        
        //button All
        buttonAll = new JButton("All");
        buttonAll.setBounds(posx, posy+height+5, 80, 20);
        buttonAll.setToolTipText("Select all metrics");
        
        buttonAll.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAllActionPerformedMulti(evt,jtable );
            }
        });
        jpanel.add(buttonAll);
      
        //button None
        buttonNone = new JButton("None");
        buttonNone.setToolTipText("Deselect all metrics");
        buttonNone.setBounds(posx+90, posy+height+5, 80, 20);

        buttonNone.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNoneActionPerformedMulti(evt,jtable);
            }
        });
        jpanel.add(buttonNone);

        //button Invert
        buttonInvert = new JButton("Invert");
        buttonInvert.setToolTipText("Invert selection");
        buttonInvert.setBounds(posx+180, posy+height+5, 80, 20);

        buttonInvert.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                  buttonInvertActionPerformedMulti(evt,jtable);
            }
        });
        jpanel.add(buttonInvert);
      
        //button Calculate
        buttonCalculate = new JButton("Calculate");
        buttonCalculate.setBounds(posx+320, posy+height+5, 95, 20);
        buttonCalculate.setToolTipText("Calculate selected metrics");

        buttonCalculate.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent evt) {
                progressFrame.setVisible(true);
                progressFrame.repaint();
                progressBar.setIndeterminate(false);
                
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // do the long-running work here
                        buttonCalculateActionPerformedMulti(evt,jtable);
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
        jpanel.add(buttonCalculate);

        //button Save
        buttonSave = new JButton("Save");
        buttonSave.setBounds(posx+425, posy+height+5, 80, 20);
        buttonSave.setToolTipText("Save selected metrics in a file");

        buttonSave.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    buttonSaveActionPerformedMulti(evt,jtable);
                } catch (IOException ex) {
                Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        jpanel.add(buttonSave);
    }
    
    /**
     * Save MVML datasets
     * 
     * @return Positive number if successfull and negative otherwise
     */
    private int saveMultiView(){
        if(dataset == null){
            JOptionPane.showMessageDialog(null, "You must load a dataset.", "alert", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        
        int [] selecteds = jTable2.getSelectedRows();
        if(selecteds.length == 0){
            JOptionPane.showMessageDialog(null, "You must select at least one view.", "alert", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        
        MultiLabelInstances mvData;
        
        int attSize = 0;
        for(int n : selecteds){
            attSize += views.get("View " + (n+1)).length;
        }
        int [] attToKeep = new int[attSize];
        Integer [] view_i;
        int j = 0;
        for(int n : selecteds){
            view_i = views.get("View " + (n+1));
            for(int k : view_i){
                attToKeep[j] = k;
                j++;
            }
        }

        FeatureSelector fs = new FeatureSelector(dataset, attSize); 
        mvData = fs.keepAttributes(attToKeep);

        try{
            String format = jComboBox_SaveFormat1.getSelectedItem().toString();

            JFileChooser fc= new JFileChooser();

            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            String xmlPath;

            int returnVal = fc.showSaveDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                File file = fc.getSelectedFile();

                if(fc.isDirectorySelectionEnabled())
                {
                    String preprocessedType = "-views";
                    
                    for(int n : selecteds){
                        preprocessedType += "_" + (n+1);
                    }

                    BufferedWriter bwTrain;
                    try {
                        String dataName = datasetName.substring(0,datasetName.length()-5);
                        
                        int sumNotSelected = 0;
                        Hashtable<String, Integer[]> v = new Hashtable<>();
                        for(int i=0; i<views.size(); i++){
                            if(Utils.contains(selecteds, i)){
                                Integer [] A = views.get("View " + (i+1));
                                for(int a=0; a<A.length; a++){
                                    A[a] -= sumNotSelected;
                                }
                                v.put("View " + (i+1), A);
                            }
                            else{
                                sumNotSelected += views.get("View " + (i+1))[views.get("View " + (i+1)).length - 1] - views.get("View " + (i+1))[0] + 1;
                            }
                        }

                        String viewsString = "-V:";
                        for(int n : selecteds){
                            attSize += v.get("View " + (n+1)).length;
                            viewsString += viewsIntervals.get("View " + (n+1)) + "!";
                            //viewsString += v.get("View " + (n+1))[0] + "-" + v.get("View " + (n+1))[v.get("View " + (n+1)).length-1] + "!";
                        }
                        viewsString += ";";
                        viewsString = viewsString.replace("!;", "");

                        if(format.toLowerCase().contains("meka")){
                            String dataPath = file.getAbsolutePath()+"/"+dataName+ preprocessedType + ".arff";

                            bwTrain = new BufferedWriter(new FileWriter(dataPath));
                            PrintWriter wrTrain = new PrintWriter(bwTrain);                           
                                                      
                            DataIOUtils.saveMVMekaDataset(wrTrain, mvData, dataName, viewsString);

                            wrTrain.close();
                            bwTrain.close();   
                        }
                        else{
                            String dataPath = file.getAbsolutePath()+"/"+dataName+ preprocessedType + ".arff";
                            xmlPath = file.getAbsolutePath()+"/"+dataName+ preprocessedType +".xml";

                            bwTrain = new BufferedWriter(new FileWriter(dataPath));
                            PrintWriter wrTrain = new PrintWriter(bwTrain);

                            DataIOUtils.saveDatasetMV(wrTrain, mvData, dataName, viewsString);

                            wrTrain.close();
                            bwTrain.close();

                            BufferedWriter bwXml = new BufferedWriter(new FileWriter(xmlPath));
                            PrintWriter wrXml = new PrintWriter(bwXml);

                            DataIOUtils.saveXMLFile(wrXml, mvData);

                            wrXml.close();
                            bwXml.close();
                        }

                        JOptionPane.showMessageDialog(null, "All files have been saved.", "Successful", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "An error ocurred while saving the dataset files.", "alert", JOptionPane.ERROR_MESSAGE);
                        Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "An error ocurred while saving the dataset files.", "alert", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        
        return 1;
    }
    
    /**
     * Load multiple datasets
     * 
     * @param returnVal Value returned from openDialog
     * @param chooser Chooser
     * @return Positive number if successfull and negative otherwise
     */
    private int loadMultiDataset(int returnVal, JFileChooser chooser){
        
        if (returnVal == JFileChooser.OPEN_DIALOG)
        {
            File [] files = chooser.getSelectedFiles();
            
            for(File f1 : files){
                String datasetName = f1.getName();
                datasetName = datasetName.substring(0,datasetName.length()-5);

                if(datasetNames.contains(datasetName))
                {
                    JOptionPane.showMessageDialog(null, "The dataset is duplicated.", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }

                String arffFilename = f1.getAbsolutePath();
                xmlFilename = DataIOUtils.getXMLString(arffFilename);
                xmlFilename = DataIOUtils.getFilePath(xmlFilename);

                boolean isMeka = false;

                String xmlPath = arffFilename.substring(0,arffFilename.length()-5)+".xml";

                FileReader fr;
                try
                {
                    fr = new FileReader(arffFilename);
                    BufferedReader bf = new BufferedReader(fr);

                    String sString = bf.readLine();
                    int labelFound = 0;
                    String labelName;
                    String[] labelNamesFound;

                    isMeka = DataIOUtils.isMeka(sString);
                    areMeka.add(isMeka);

                    if(isMeka)
                    {
                        isMeka = true;

                        int labelCount = DataIOUtils.getLabelsFromARFF(sString);                

                        if(labelCount > 0){
                            labelNamesFound = new String[labelCount];

                            while(labelFound < labelCount)
                            {
                                sString = bf.readLine();
                                labelName = DataIOUtils.getLabelNameFromLine(sString);

                                if(labelName!= null)
                                {
                                    labelNamesFound[labelFound]=labelName;
                                    labelFound++;
                                }
                            }
                        }
                        else{
                            labelCount = Math.abs(labelCount);
                            labelNamesFound = new String[labelCount];

                            String [] sStrings = new String[labelCount];

                            while(!(sString = bf.readLine()).contains("@data")){
                                if(!sString.trim().equals("")){
                                    for(int s=0; s<labelCount-1; s++){
                                        sStrings[s] = sStrings[s+1];
                                    }
                                    sStrings[labelCount-1] = sString;
                                }
                            }

                            for(int i=0; i<labelCount; i++){
                                labelName = DataIOUtils.getLabelNameFromLine(sStrings[i]);

                                if(labelName!= null)
                                {
                                    labelNamesFound[labelFound]=labelName;
                                    labelFound++;
                                }
                            }
                        }

                        BufferedWriter bwXml= new BufferedWriter(new FileWriter(xmlPath));
                        PrintWriter wrXml = new PrintWriter(bwXml);

                        DataIOUtils.writeXMLFile(wrXml, labelNamesFound);

                        bwXml.close();
                        wrXml.close();

                        xmlFilename = DataIOUtils.getFilePath(xmlPath);
                    }
                    else
                    {
                        isMeka= false;
                    }
                }
                catch (FileNotFoundException ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    MultiLabelInstances current = new MultiLabelInstances(arffFilename, xmlFilename);

                    if(isMeka){
                        File f2 = new File(xmlFilename);
                        f2.delete();
                    }

                    listDatasets.add(current);
                    datasetNames.add(datasetName);
                    list.addElement(datasetName );
                }
                catch (InvalidDataFormatException ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }            
        
        return 1;
    }
    
    /**
     * Preprocess dataset
     * 
     * @return Positive number if successfull and negative otherwise
     */
    private int preprocess(){
        trainDatasets = new ArrayList();
        testDatasets = new ArrayList();

        Instances train, test;

        if(dataset == null){
            JOptionPane.showMessageDialog(null, "You must load a dataset.", "alert", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        
        MultiLabelInstances preprocessDataset = dataset.clone();
        
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
                
                Instances dataIS;
                try {
                    Randomize randomize = new Randomize();                
                    dataIS = dataset.getDataSet();

                    randomize.setInputFormat(dataIS);
                    dataIS = Filter.useFilter(dataIS, randomize);
                    randomize.batchFinished();

                    RemoveRange removeRange = new RemoveRange();
                    removeRange.setInputFormat(dataIS);
                    removeRange.setInstancesIndices((nInstances+1) + "-last");
                    
                    dataIS = Filter.useFilter(dataIS, removeRange);
                    removeRange.batchFinished();
                    
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

                String combination = jComboBoxBRFSComb.getSelectedItem().toString();
                String normalization = jComboBoxBRFSNorm.getSelectedItem().toString();
                String output = jComboBoxBRFSOut.getSelectedItem().toString();

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
            }
        }

        
        if(!radioNoSplit.isSelected()){
            //Random Holdout
            if(radioRandomHoldout.isSelected()){
                String split = textRandomHoldout.getText();
                double percentage = Double.parseDouble(split);
                if((percentage <= 0) || (percentage >= 100)){
                    JOptionPane.showMessageDialog(null, "The percentage must be a number in the range (0, 100).", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }

                try{
                    RandomTrainTest pre = new RandomTrainTest();
                    MultiLabelInstances [] partitions = pre.split(preprocessDataset, percentage);
                    trainDataset = partitions[0];
                    testDataset = partitions[1];
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
                    JOptionPane.showMessageDialog(null, "You must enter the number of folds.", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }

                int nFolds;

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
                    Instances dataTemp = temp.getDataSet();

                    int seed = (int)(Math.random()*100)+100;
                    Random rand = new Random(seed);

                    dataTemp.randomize(rand);

                    Instances [] foldsCV = new Instances[nFolds];
                    for(int i=0; i<nFolds; i++){
                        foldsCV[i] = new Instances(preprocessDataset.getDataSet(), 1);
                    }

                    for(int i=0; i<dataTemp.numInstances(); i++){
                        foldsCV[i%nFolds].add(dataTemp.get(i));
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
                        trainDatasets.add(new MultiLabelInstances(train, preprocessDataset.getLabelsMetaData()));
                        testDatasets.add(new MultiLabelInstances(test, preprocessDataset.getLabelsMetaData()));
                    }
                }

                catch (Exception ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //Iterative stratified holdout
            else if(radioIterativeStratifiedHoldout.isSelected()){
                String split = textIterativeStratifiedHoldout.getText();
                double percentage = Double.parseDouble(split);
                if((percentage <= 0) || (percentage >= 100)){
                    JOptionPane.showMessageDialog(null, "The percentage must be a number in the range (0, 100).", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }

                try{
                    IterativeTrainTest pre = new IterativeTrainTest();
                    MultiLabelInstances [] partitions = pre.split(preprocessDataset, percentage);

                    trainDataset = partitions[0];
                    testDataset = partitions[1];
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
                    JOptionPane.showMessageDialog(null, "You must enter the number of folds.", "alert", JOptionPane.ERROR_MESSAGE);
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

                        trainDatasets.add(new MultiLabelInstances(train, preprocessDataset.getLabelsMetaData()));
                        testDatasets.add(new MultiLabelInstances(test, preprocessDataset.getLabelsMetaData()));
                    } catch (InvalidDataFormatException ex) {
                        Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
            //LP stratified holdout
            else if(radioLPStratifiedHoldout.isSelected()){
                String split = textLPStratifiedHoldout.getText();
                double percentage = Double.parseDouble(split);
                if((percentage <= 0) || (percentage >= 100)){
                    JOptionPane.showMessageDialog(null, "The percentage must be a number in the range (0, 100).", "alert", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }

                try{
                    IterativeTrainTest pre = new IterativeTrainTest();
                    MultiLabelInstances [] partitions = pre.split(preprocessDataset, percentage);

                    trainDataset = partitions[0];
                    testDataset = partitions[1];
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
                    JOptionPane.showMessageDialog(null, "You must enter the number of folds.", "alert", JOptionPane.ERROR_MESSAGE);
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

                        trainDatasets.add(new MultiLabelInstances(train, preprocessDataset.getLabelsMetaData()));
                        testDatasets.add(new MultiLabelInstances(test, preprocessDataset.getLabelsMetaData()));
                    } catch (InvalidDataFormatException ex) {
                        Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        jButtonSaveDatasets.setEnabled(true);
        jComboBoxSaveFormat.setEnabled(true);
            
        return 1;
    }
    
    /**
     * Load dataset in principal tab
     * 
     * @param returnVal Positive number if successfull and negative otherwise
     * @param fileChooser Chooser
     * @param deleteXML Boolean indicating if the generated xml must be removed
     * @return 
     */
    private int loadDataset(int returnVal, JFileChooser fileChooser, 
            boolean deleteXML){
        if (returnVal == JFileChooser.OPEN_DIALOG)
        {
            File f1 = fileChooser.getSelectedFile();
            datasetName = f1.getName();
            datasetCurrentName = datasetName.substring(0,datasetName.length()-5);

            String arffFilename = f1.getAbsolutePath();

            xmlPath=  arffFilename.substring(0, arffFilename.length()-5)+".xml";
            xmlFilename = DataIOUtils.getFileName(xmlPath);

            File fileTmp = new File(xmlPath);

            FileReader fr;
            try
            {
                views.clear(); 
                ((DefaultTableModel)jTable2.getModel()).getDataVector().removeAllElements();
                ((DefaultTableModel)jTable3.getModel()).getDataVector().removeAllElements();
                
                fr = new FileReader(arffFilename);
                BufferedReader bf = new BufferedReader(fr);

                String sString = bf.readLine();
                
                if(sString.contains("-V:")){
                    mv = true;
                    
                    TabPrincipal.setEnabledAt(7, true);
                    String s2 = sString.split("'")[1];
                    s2 = s2.split("-V:")[1];
                    String [] intervals = s2.split("!");
                    Vector<Vector<Integer>> newIntervals = new Vector<>();
                    int [] intervalsSize = new int[intervals.length];
                    int max = Integer.MIN_VALUE;
                    int min = Integer.MAX_VALUE;
                    double mean = 0;
                    
                    for(int i=0; i<intervals.length; i++){
                        newIntervals.add(new Vector<Integer>());
                        String [] aux2;
                        
                        viewsIntervals.put("View " + (i+1), intervals[i]);
                        
                        if(intervals[i].contains(",")){
                            aux2 = intervals[i].split(",");
                            for(int j=0; j<aux2.length; j++){
                                if(aux2[j].contains("-")){
                                    int a = Integer.parseInt(aux2[j].split("-")[0]);
                                    int b = Integer.parseInt(aux2[j].split("-")[1]);
 
                                    for(int k=a ; k<=b; k++){
                                        newIntervals.get(i).add(k);
                                    }
                                }
                                else{
                                    newIntervals.get(i).add(Integer.parseInt(aux2[j]));
                                }
                                
                            }
                        }
                        else{
                            if(intervals[i].contains("-")){
                                int a = Integer.parseInt(intervals[i].split("-")[0]);
                                int b = Integer.parseInt(intervals[i].split("-")[1]);
 
                                for(int k=a ; k<=b; k++){
                                    newIntervals.get(i).add(k);
                                }
                            }
                            else{
                                newIntervals.get(i).add(Integer.parseInt(intervals[i]));
                            }
                        }
                    }

                    for(int i=0; i<newIntervals.size(); i++){
                        
                        Integer [] indices = new Integer[newIntervals.get(i).size()];
                        for(int j=0; j<newIntervals.get(i).size(); j++){
                            indices[j] = newIntervals.get(i).get(j);
                        }
                        
                        System.out.println(Arrays.toString(indices));
                        
                        views.put("View " + (i+1), indices);
                        
                        if(newIntervals.get(i).size() > max){
                            max = newIntervals.get(i).size();
                        }
                        if(newIntervals.get(i).size() < min){
                            min = newIntervals.get(i).size();
                        }
                        mean += newIntervals.get(i).size();
                    }
                                        
                    mean /= intervalsSize.length;
                    labelNumViewsValue.setText(Integer.toString(intervalsSize.length));
                    labelMaxNumAttrViewValue.setText(Integer.toString(max));
                    labelMinNumAttrViewValue.setText(Integer.toString(min));
                    labelMeanNumAttrViewValue.setText(Double.toString(mean));
                }
                else{
                    TabPrincipal.setEnabledAt(7, false);
                    mv = false;
                }
                
                int labelFound = 0;
                String labelName;
                String[] labelNamesFound;

                if(DataIOUtils.isMeka(sString))
                {
                    deleteXML = true;
                    isMeka = true;

                    int labelCount = DataIOUtils.getLabelsFromARFF(sString);
                    
                    if(labelCount > 0){
                        labelNamesFound = new String[labelCount];

                        while(labelFound < labelCount)
                        {
                            sString = bf.readLine();
                            labelName = DataIOUtils.getLabelNameFromLine(sString);

                            if(labelName!= null)
                            {
                                labelNamesFound[labelFound]=labelName;
                                labelFound++;
                            }
                        }
                    }
                    else{
                        labelCount = Math.abs(labelCount);
                        labelNamesFound = new String[labelCount];
                        
                        String [] sStrings = new String[labelCount];
                        
                        while(!(sString = bf.readLine()).contains("@data")){
                            if(!sString.trim().equals("")){
                                for(int s=0; s<labelCount-1; s++){
                                    sStrings[s] = sStrings[s+1];
                                }
                                sStrings[labelCount-1] = sString;
                            }
                        }
                        
                        for(int i=0; i<labelCount; i++){
                            labelName = DataIOUtils.getLabelNameFromLine(sStrings[i]);

                            if(labelName!= null)
                            {
                                labelNamesFound[labelFound]=labelName;
                                labelFound++;
                            }
                        }
                    }

                    BufferedWriter bwXml= new BufferedWriter(new FileWriter(xmlPath));
                    PrintWriter wrXml = new PrintWriter(bwXml);

                    DataIOUtils.writeXMLFile(wrXml, labelNamesFound);

                    bwXml.close();
                    wrXml.close();

                    xmlFilename = DataIOUtils.getFilePath(xmlPath);
                    fileTmp = new File(xmlPath);
                }
                else
                {
                    isMeka= false;
                }
            }
            catch (FileNotFoundException ex) {
                Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
            } catch (IOException ex) {
                Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
            }

            
            if(!fileTmp.exists())
            {
                xmlPath = DataIOUtils.getXMLString(arffFilename);
                xmlFilename = DataIOUtils.getFilePath(xmlPath);
            }

            //Enable tabs
            TabPrincipal.setEnabledAt(1, true);
            TabPrincipal.setEnabledAt(2, true);
            TabPrincipal.setEnabledAt(3, true);
            TabPrincipal.setEnabledAt(4, true);
            TabPrincipal.setEnabledAt(5, true);
            TabPrincipal.setEnabledAt(6, true);
            
            try {
                File f = new File(xmlFilename);
                if(f.exists() && !f.isDirectory()) { 
                    //MultiLabelInstances dataset_temp = new MultiLabelInstances(filename_database_arff, xmlFilename);
                }
                else{
                    JOptionPane.showMessageDialog(null, "File could not be loaded.", "alert", JOptionPane.ERROR_MESSAGE); 
                    return -1;
                }
            } catch (Exception ex) {
                Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                progressBar.setVisible(false);
                progressFrame.setVisible(false);
                progressFrame.repaint();
                return -1;
            }

            initTableMetrics();
            clearTableMetricsPrincipal();
            
            File f = new File(xmlFilename);
            if(f.exists() && !f.isDirectory()) { 
                loadDataset(arffFilename, xmlFilename);
            }
            else{
                loadDataset(arffFilename, null);
            }

            if(deleteXML){
                File f2 = new File(xmlFilename);
                f2.delete();
            }

            textChooseFile.setText(arffFilename);
        }
        
        if(mv){   
            
            if(((DefaultTableModel)jTable2.getModel()).getRowCount() > 0){
                ((DefaultTableModel)jTable2.getModel()).getDataVector().removeAllElements();
            }
            
            for(int i=0; i<views.size(); i++){
                MultiLabelInstances view = dataset.clone();
                
                 try {
                    Instances inst = view.getDataSet();
                    
                    int [] attributes = Utils.toPrimitive(views.get("View " + (i+1)));
                    
                    int[] toKeep = new int[attributes.length + dataset.getNumLabels()];
                    System.arraycopy(attributes, 0, toKeep, 0, attributes.length);
                    int[] labelIndices = dataset.getLabelIndices();
                    System.arraycopy(labelIndices, 0, toKeep, attributes.length, dataset.getNumLabels());
                    
                    Remove filterRemove = new Remove();
                    filterRemove.setAttributeIndicesArray(toKeep);
                    filterRemove.setInvertSelection(true);
                    filterRemove.setInputFormat(inst);

                    MultiLabelInstances modifiedDataset = new MultiLabelInstances( Filter.useFilter(view.getDataSet(), filterRemove), dataset.getLabelsMetaData());
                    
                    LxIxF lif = new LxIxF();
                    lif.calculate(modifiedDataset);
                    RatioInstancesToAttributes ratioInstAtt = new RatioInstancesToAttributes();
                    ratioInstAtt.calculate(modifiedDataset);
                    AvgGainRatio avgGainRatio = new AvgGainRatio();
                    avgGainRatio.calculate(modifiedDataset);
                    
                    ((DefaultTableModel)jTable2.getModel()).addRow(new Object[]{"View " + (i+1), attributes.length, 
                        getMetricValueFormatted(lif), 
                        getMetricValueFormatted(ratioInstAtt), 
                        getMetricValueFormatted(avgGainRatio)});
                } catch (Exception ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return 1;
    }
    
    /**
     * Set help for MVML table
     * @param jtable Table
     * @return Table with help
     */
    public JTable setMVTableHelp(JTable jtable){
        jtable = new JTable(jtable.getModel()){
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                        
                    switch (column) {
                        case 0:
                            jc.setToolTipText("View name");
                            break;
                        case 1:
                            jc.setToolTipText("Number of the attributes of the view");
                            break;
                        case 2:
                            jc.setToolTipText("Labels x Instances x Features");
                            break;
                        case 3:
                            jc.setToolTipText("Ratio of number of instances to the number of attributes");
                            break;
                        case 4:
                            jc.setToolTipText("Average gain ratio");
                            break;
                        default:
                            break;
                    }
                }
                return c;
            }
        };
        
        return jtable;
    }

    /**
     * Get label index given a label name
     * @param name Name of the label
     * @return Label index
     */
    private int getLabelIndex(String name){
        for(int i=0; i<jTableCoocurrences.getColumnCount(); i++){
            if(jTableCoocurrences.getColumnName(i).equals(name)){
                return(i);
            }
        }
        
        return(-1);
    }
    
    /**
     * Transform multi-label dataset into one or more multi-class or binary datasets
     * @return Positive number if successfull and negative otherwise
     */
    private int transform(){
        
        if(dataset == null){
            JOptionPane.showMessageDialog(null, "You must load a dataset.", "alert", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        
        transformedDatasets.clear();
        if(radioBRTrans.isSelected()){
            BinaryRelevanceTransformation BRTrans = new BinaryRelevanceTransformation(dataset);
            
            for(int i=0; i<dataset.getNumLabels(); i++){
                try {
                    LabelsMetaDataImpl newLMD = (LabelsMetaDataImpl) dataset.getLabelsMetaData().clone();
                    for(int j=0; j<dataset.getNumLabels(); j++){
                        if(i!=j){
                            newLMD.removeLabelNode(dataset.getLabelNames()[j]);
                        }
                    }
                    Instances inst = BRTrans.transformInstances(i);
                    inst.renameAttribute(inst.classIndex(), dataset.getLabelNames()[i]);
                    transformedDatasets.add(inst);
                } catch (Exception ex) {
                    Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                    return -1;
                }
            }
        }
        else if(radioLPTrans.isSelected()){
            try{
                LabelPowersetTransformation LPTrans = new LabelPowersetTransformation();
                Instances inst = LPTrans.transformInstances(dataset);
                transformedDatasets.add(inst);
            } catch(Exception ex){
                Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
            }
        }
        else if(radioRemoveLabelsTrans.isSelected()){
            try{
                Instances inst = RemoveAllLabels.transformInstances(dataset);
                transformedDatasets.add(inst);
            } catch(Exception ex){
                Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
            }
        }
        else if(radioIncludeLabelsTrans.isSelected()){
            try{
                IncludeLabelsTransformation includeTrans = new IncludeLabelsTransformation();
                Instances inst = includeTrans.transformInstances(dataset);
                transformedDatasets.add(inst);
            } catch(Exception ex){
                Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
            }
        }

        jButtonSaveDatasetsTrans.setEnabled(true);
        
        return 1;
    }
    
    /**
     * Show heatmap with selected labels
     */
    private void showHeatMap(){
        if(pairs == null) 
        {
            JOptionPane.showMessageDialog(null, "You must load a dataset.", "alert", JOptionPane.ERROR_MESSAGE); 
            return;
        }

        Vector<Integer> selectedIndex = new Vector<>();

        int[] selecteds = tableHeatmapLeft.getSelectedRows();
        
        if(selecteds.length <= 1) {
            JOptionPane.showMessageDialog(null, "You must choose two or more labels.", "alert", JOptionPane.ERROR_MESSAGE); 
            return;
        }
        
        for(int i=0;i<selecteds.length; i++)
        {
            selectedIndex.add(getLabelIndex((tableHeatmapLeft.getValueAt(selecteds[i], 0).toString())));
        }
          
        Collections.sort(selectedIndex);

        double [][] newCoeffs = new double[selectedIndex.size()][selectedIndex.size()];
        
        for(int i=0; i<selectedIndex.size(); i++){
            for(int j=0; j<selectedIndex.size(); j++){
                newCoeffs[i][j] = heatmapCoefficients[selectedIndex.get(i)][selectedIndex.get(j)];
            }
        }
        
        heatMap = createHeatmapGraph(panelHeatmap, newCoeffs, null, heatMap);
    }
    
    /**
     * Show heatmap with n most frequent labels
     * 
     * @param n Number of labels to show
     */
    private void showMostFrequentsHeatMap(int n){
        if(pairs== null) 
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
        showHeatMap();
    }    
    
    /**
     * Show heatmap with n most related labels
     * 
     * @param n Number of labels to show
     */
    private void showMostRelatedHeatMap(int n){
        selectTopHeatmapLabels(n ,true);
        
        if(pairs== null) 
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

        Vector<Integer> selectedIndex = new Vector<>();
        int[] selecteds = getTopRelatedHeatmap(n);
        Arrays.sort(selecteds);
        
        for(int i=0;i<selecteds.length; i++)
        {
            selectedIndex.add(selecteds[i]);
        }
          
        double [][] newCoeffs = new double[selectedIndex.size()][selectedIndex.size()];

        
        for(int i=0; i<selectedIndex.size(); i++){
            for(int j=0; j<selectedIndex.size(); j++){
                newCoeffs[i][j] = heatmapCoefficients[selectedIndex.get(i)][selectedIndex.get(j)];
            }
        }
        
        heatMap = createHeatmapGraph(panelHeatmap, newCoeffs, null, heatMap);
    }
    
    /**
     * Show heatmap with n most frequent labels U n most frequent labels
     * 
     * @param n Number of labels to select for each set
     */
    private void showMostFrequentURelatedHeatMap(int n){
        selectTopHeatmapLabels(n ,true);
        
        if(pairs== null) 
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

        int[] selecteds = getTopRelatedHeatmap(n);
        Arrays.sort(selecteds);
        tableHeatmapLeft.addRowSelectionInterval(0, n-1);
        showHeatMap();
    }
    
    /**
     * Initializes table of metrics
     */
    private void initTableMetrics(){
        ArrayList<String> metricsList = MetricUtils.getAllMetrics();
        
        tableMetrics.clear();
        
        for(int i=0; i<metricsList.size(); i++){
            if(metricsList.get(i).charAt(0) != '<'){
                tableMetrics.put(metricsList.get(i), "-");
            }
            else{
                tableMetrics.put(metricsList.get(i), "");
            }
        }
    }
    
    /**
     * Initializes table of metrics of multiple datasets tab
     * @param dataName Dataset name to add
     */
    private void initTableMetricsMulti(String dataName){
        ArrayList<String> metricsList = MetricUtils.getMetricsMulti();
        
        tableMetricsMulti.get(dataName).clear();
        
        for(int i=0; i<metricsList.size(); i++){
            tableMetricsMulti.get(dataName).put(metricsList.get(i), "-");
        }
    }
    
    /**
     * Save heatmap graph as image
     * 
     * @throws AWTException
     * @throws IOException 
     */
    private void saveHeatmapGraph() throws AWTException, IOException
    {
        BufferedImage image = new Robot().createScreenCapture(new Rectangle(panelHeatmap.getLocationOnScreen().x+31, panelHeatmap.getLocationOnScreen().y+31, panelHeatmap.getWidth()-61, panelHeatmap.getHeight()-61));
        JFileChooser fc= new JFileChooser();
        
        FileNameExtensionFilter fname1 = new FileNameExtensionFilter(".png", "png");

        fc.removeChoosableFileFilter(fc.getChoosableFileFilters()[0]);

        fc.setFileFilter(fname1);
        fc.addChoosableFileFilter(fname1);
        
        int returnVal = fc.showSaveDialog(this);
         
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {                               
            File file =new File(fc.getSelectedFile().getAbsolutePath()+".png");
            ImageIO.write(image, "png", file);
                
            JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
            Toolkit.getDefaultToolkit().beep();
        } 
    }
    
    /**
     * Save co-ocurrence graph as image
     * 
     * @throws AWTException
     * @throws IOException 
     */
    private void saveCoocurrenceGraph() throws AWTException, IOException
    {
        BufferedImage image = new Robot().createScreenCapture(new Rectangle(panelCoOcurrenceRight.getLocationOnScreen().x, panelCoOcurrenceRight.getLocationOnScreen().y, panelCoOcurrenceRight.getWidth(), panelCoOcurrenceRight.getHeight()));

        JFileChooser fc= new JFileChooser();
        
        FileNameExtensionFilter fname1 = new FileNameExtensionFilter(".png", "png");

        fc.removeChoosableFileFilter(fc.getChoosableFileFilters()[0]);

        fc.setFileFilter(fname1);
        fc.addChoosableFileFilter(fname1);
        
        int returnVal = fc.showSaveDialog(this);
         
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {                               
            File file =new File(fc.getSelectedFile().getAbsolutePath()+".png");

            ImageIO.write(image, "png", file);
                
            JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
            Toolkit.getDefaultToolkit().beep();     
        } 
    }
    
    /**
     * Create JGraphX chart
     * 
     * @param jpanel Panel
     * @param list List
     * @param labelNames Names of labels
     * @param oldGraph Old graph
     * @return 
     */
    private mxGraphComponent createJGraphX(JPanel jpanel, 
            ArrayList<AttributesPair> list, String[] labelNames, 
            mxGraphComponent oldGraph)
    {
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        
	graph.getModel().beginUpdate();
        
        graph.setLabelsClipped(true);
        
        Random rand = new Random();
	
        Object[] corners = new Object[labelNames.length];
        
        ImbalancedFeature current;
        double freq;
        
        int min = 0;
        int max = 1;
        int numIntervals = 10;           
        int strength;
  
        try
        {
            //create vertices
            for(int i=0;i<labelNames.length;i++)
            {
                current = DataInfoUtils.getLabelByLabelname(labelNames[i],labelsFreqSorted);
                freq = current.getAppearances()/(dataset.getNumInstances()*1.0);

                strength =  ChartUtils.getBorderStrength(min, max, numIntervals, freq);
                
                switch (strength) {
                    case 1:
                        corners[i] = graph.insertVertex(parent, null,labelNames[i], rand.nextInt(430), rand.nextInt(280), labelNames[i].length()*6,20);
                        break;
                    case 2:
                        corners[i] = graph.insertVertex(parent, null,labelNames[i], rand.nextInt(430), rand.nextInt(280), labelNames[i].length()*5,20,"ROUNDED;strokeWidth=2");
                        break;
                    case 3:
                        corners[i] = graph.insertVertex(parent, null,labelNames[i], rand.nextInt(430), rand.nextInt(280), labelNames[i].length()*5,20,"ROUNDED;strokeWidth=3");
                        break;       
                    case 4:
                        corners[i] = graph.insertVertex(parent, null,labelNames[i], rand.nextInt(430), rand.nextInt(280), labelNames[i].length()*5,20,"ROUNDED;strokeWidth=4");
                        break;
                    case 5:
                        corners[i] = graph.insertVertex(parent, null,labelNames[i], rand.nextInt(430), rand.nextInt(280), labelNames[i].length()*5,20,"strokeWidth=5");
                        break;
                    case 6:
                        corners[i] = graph.insertVertex(parent, null,labelNames[i], rand.nextInt(430), rand.nextInt(280), labelNames[i].length()*5,20,"strokeWidth=6");
                        break;
                    case 7:
                        corners[i] = graph.insertVertex(parent, null,labelNames[i], rand.nextInt(430), rand.nextInt(280), labelNames[i].length()*5,20,"strokeWidth=7");
                        break;
                    case 8:
                        corners[i] = graph.insertVertex(parent, null,labelNames[i], rand.nextInt(430), rand.nextInt(280), labelNames[i].length()*5,20,"strokeWidth=8");
                        break;
                    case 9:
                        corners[i] = graph.insertVertex(parent, null,labelNames[i], rand.nextInt(430), rand.nextInt(280), labelNames[i].length()*5,20,"strokeWidth=9");
                        break;
                    default:
                        corners[i] = graph.insertVertex(parent, null,labelNames[i], rand.nextInt(430), rand.nextInt(280), labelNames[i].length()*5,20,"strokeWidth=10");
                        break;
                }
            }
            
            ArrayList<String> otherList;
            
            //create edges             
            if(!list.isEmpty()){
                AttributesPair temp;

                for(int i=0;i<labelNames.length;i++)
                {
                    otherList = ChartUtils.getVertices(labelNames[i], list);

                    for(String actual : otherList)
                    {
                        int index = DataInfoUtils.getLabelIndex(labelNames, actual);

                        temp = AttributePairsUtils.searchAndGet(labelNames[i], actual, list);
                        freq = temp.getAppearances()/(dataset.getNumInstances()*1.0);

                        strength =  ChartUtils.getBorderStrength(min, max, numIntervals,freq );

                        switch (strength) {
                            case 1:
                                graph.insertEdge(parent, null,"",corners[i], corners[index], "startArrow=none;endArrow=none;strokeWidth=1");
                                break;
                            case 2:
                                graph.insertEdge(parent, null,"",corners[i], corners[index], "startArrow=none;endArrow=none;strokeWidth=2");
                                break;
                            case 3:
                                graph.insertEdge(parent, null,"",corners[i], corners[index], "startArrow=none;endArrow=none;strokeWidth=3");
                                break;
                            case 4:
                                graph.insertEdge(parent, null,"",corners[i], corners[index], "startArrow=none;endArrow=none;strokeWidth=4");
                                break;
                            case 5:
                                graph.insertEdge(parent, null,"",corners[i], corners[index], "startArrow=none;endArrow=none;strokeWidth=5");
                                break;
                            case 6:
                                graph.insertEdge(parent, null,"",corners[i], corners[index], "startArrow=none;endArrow=none;strokeWidth=6");
                                break;
                            case 7:
                                graph.insertEdge(parent, null,"",corners[i], corners[index], "startArrow=none;endArrow=none;strokeWidth=7");
                                break;
                            case 8:
                                graph.insertEdge(parent, null,"",corners[i], corners[index], "startArrow=none;endArrow=none;strokeWidth=8");
                                break;
                            case 9:
                                graph.insertEdge(parent, null,"",corners[i], corners[index], "startArrow=none;endArrow=none;strokeWidth=9");
                                break;
                            default:
                                graph.insertEdge(parent, null,"",corners[i], corners[index], "startArrow=none;endArrow=none;strokeWidth=3");
                                break;
                        }
                    }
                }           
            }
        }
        finally
        {
            graph.getModel().endUpdate();
        }
        
        if(oldGraph !=null) {
            jpanel.remove(oldGraph);
        }
        
        graph.setCellsEditable(false);
        graph.setAllowDanglingEdges(false);

	mxGraphComponent graphComponent = new mxGraphComponent(graph);               
        graphComponent.getGraph().getModel().endUpdate();
                
        jpanel.setLayout(new BorderLayout());
        jpanel.setPreferredSize(new Dimension(550, 425));
        jpanel.add(graphComponent,BorderLayout.CENTER);
        
        jpanel.validate();
        jpanel.repaint();
        
        return graphComponent;
    }
    
    /**
     * Create heatmap graph
     * 
     * @param jpanel Panel
     * @param coefficients Coefficients
     * @param list List
     * @param oldHeatmap Old heatmap
     * @return Heatmap
     */
    private HeatMap createHeatmapGraph(JPanel jpanel, double [][] coefficients, 
            ArrayList<AttributesPair> list, HeatMap oldHeatmap)
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
        
        if((list != null) && (list.size() > 0)){
            HashSet<Integer> selected = new HashSet<>();
            
            for(int i=0; i<list.size(); i++){
                selected.add(list.get(i).getAttribute1Index());
                selected.add(list.get(i).getAttribute2Index());
            }
            
            newCoefs = new double[selected.size()][selected.size()];
            
            List sortedSelected = new ArrayList(selected);
            Collections.sort(sortedSelected);
            
            for(int i=0; i<sortedSelected.size(); i++){
                for(int j=0; j<sortedSelected.size(); j++){
                    newCoefs[i][j] = coefficients[(int)sortedSelected.get(i)][(int)sortedSelected.get(j)];
                }
            }
        }
        
        heatMap = new HeatMap(newCoefs, false, colors);
        
        if(oldHeatmap != null){
            jpanel.remove(oldHeatmap);
        }
            
        jpanel.setLayout(new BorderLayout());
        jpanel.setPreferredSize(new Dimension(550, 425));
        jpanel.add(heatMap,BorderLayout.CENTER);

        jpanel.validate();
        jpanel.repaint();

        return heatMap;
    }
    
    /**
     * Load dataset
     * 
     * @param arffFilename Arff filename
     * @param xmlFilename Xml filename
     */
    private void loadDataset(String arffFilename, String xmlFilename)
    {
        try {        
            export2.setVisible(true);
                         
            if(tabsDependences.getSelectedIndex()==0){
                jLabelChiFiText.setVisible(true);
            }
            else{
                jLabelChiFiText.setVisible(false);
            }
             
            trainDataset= null;
            testDataset= null;
            
            //new Instances
              
            if(xmlFilename == null){
                MekaToMulan m = new MekaToMulan();
                m.convert(arffFilename, arffFilename+"_mulan");
                dataset = new MultiLabelInstances(arffFilename+"_mulan.arff", arffFilename+"_mulan.xml");
                File f2 = new File(arffFilename+"_mulan.xml");
                f2.delete();
            }
            else{
                dataset = new MultiLabelInstances(arffFilename, xmlFilename);
            }

            labelAppearances = MetricUtils.getImbalancedDataByAppearances(dataset);
            labelAppearances = MetricUtils.sortByFrequency(labelAppearances);
             
            imbalancedLabels = MetricUtils.getImbalancedDataByIRInterClass(dataset,labelAppearances);
             
            IRIntraClass = MetricUtils.getIRIntraClassValues(imbalancedLabels);
            HeapSort.sort(IRIntraClass);
            IRIntraClass = HeapSort.getSortedArray();

            stat = new Statistics();
            stat.calculateStats(dataset);

            printMainMetrics(dataset);

            labelsFreqSorted = MetricUtils.getImbalancedDataByAppearances(dataset);

            CategoryPlot temp1 = labelFrequencyChart.getChart().getCategoryPlot();
            temp1.clearRangeMarkers();
            ChartUtils.updateValuesBarChart(labelsFreqSorted,dataset.getNumInstances(),temp1);

            HashMap<Integer,Integer> labelsPerExample = DataInfoUtils.getLabelsetByValues(stat);
                            
            ChartUtils.updateLineChart(dataset.getNumInstances(),labelsHistogramChart.getChart().getCategoryPlot(),labelsPerExample);
                   
            temp1 = labelsetsFrequencyChart.getChart().getCategoryPlot();
            temp1.clearRangeMarkers();
            tmLabelsetFrequency= labelsetsFrequencyTableModel(tableImbalance,dataset,stat,temp1);

            tmIR = imbalancedTableModel(tableImbalance,dataset);  
                                
            tmLabelFrequency= frequencyTableModel(tableImbalance,dataset);
            tmAttributes = attributesTableModel(tableImbalance, dataset);
            tmBox = boxplotTableModel(tableImbalance);
            tmCoocurrences = labelFrequencyTableModel(tableCoOcurrenceLeft, dataset);
            tmHeatmap = labelFrequencyTableModel(tableCoOcurrenceLeft, dataset);
            tmLabelsHistogram = labelsPerExampleTableModel(tableImbalance, labelsPerExample);

            tmIRInterClass = irInterClassTableModel(tableImbalance);
            tmIRIntraClass = irIntraClassTableModel(tableImbalance);
            
            temp1=IRLabelsetsChart.getChart().getCategoryPlot();
            temp1.clearRangeMarkers();
            tmIRLabelset = irLabelsetsTableModel(tableImbalance,stat,IRLabelsetsChart.getChart().getCategoryPlot());
            
            temp1= IRInterClassChart.getChart().getCategoryPlot();
            temp1.clearRangeMarkers();
            ChartUtils.updateIRBarChart(labelsFreqSorted, IRInterClass, temp1);
            
            temp1= IRIntraClassChart.getChart().getCategoryPlot();
            temp1.clearRangeMarkers();
            ChartUtils.updateIRBarChart(labelsFreqSorted, IRIntraClass, temp1);
            
            if(tmLabelFrequency !=null && tmLabelsetFrequency!=null)
            {               
                switch (tabsImbalance.getSelectedIndex()) {
                    case 0:
                        tableImbalance.setModel(tmLabelFrequency);
                        panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Label frequency"));
                        tableImbalance.setDefaultRenderer(Object.class, new DefaultRender());
                        panelImbalanceLeft.repaint();
                        panelImbalanceLeft.validate();
                        break;
                    case 1:
                        tableImbalance.setModel(tmLabelsetFrequency);
                        panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Labelset frequency"));
                        tableImbalance.setDefaultRenderer(Object.class, new DefaultRender());
                        panelImbalanceLeft.repaint();
                        panelImbalanceLeft.validate();
                        break;
                    case 2:
                        tableImbalance.setModel(tmLabelsHistogram);
                        panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Labels histogram"));
                        tableImbalance.setDefaultRenderer(Object.class, new DefaultRender());
                        panelImbalanceLeft.repaint();
                        panelImbalanceLeft.validate();
                        break;
                    case 3:
                        tableImbalance.setModel(tmIRIntraClass);
                        panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Imbalance Ratio intra class"));
                        tableImbalance.setDefaultRenderer(Object.class, new IRRender(1));
                        panelImbalanceLeft.repaint();
                        panelImbalanceLeft.validate();
                        break;
                    case 4:
                        tableImbalance.setModel(tmIRLabelset);
                        panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Labelsets Imbalance Ratio"));
                        tableImbalance.setDefaultRenderer(Object.class, new IRRender(1));
                        panelImbalanceLeft.repaint();
                        panelImbalanceLeft.validate();
                        break;
                    case 5:
                        tableImbalance.setModel(tmBox);
                        panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Box diagrams"));
                        //tableImbalance.setDefaultRenderer(Object.class, new DefaultRender());
                        panelImbalanceLeft.repaint();
                        panelImbalanceLeft.validate();
                        break;                    
                    case 6:
                        tableImbalance.setModel(tmIRInterClass);
                        panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Imbalance ratio inter class"));
                        tableImbalance.setDefaultRenderer(Object.class, new IRRender(1));
                        panelImbalanceLeft.repaint();
                        panelImbalanceLeft.validate();
                        break;
                    default:
                        tableImbalance.setModel(tmLabelsHistogram);
                        panelImbalanceLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Labels histogram"));
                        tableImbalance.setDefaultRenderer(Object.class, new DefaultRender());
                        panelImbalanceLeft.repaint();
                        panelImbalanceLeft.validate();
                        break;
                }
                tableImbalance.repaint();
                tableImbalance.validate();
                
                if (tabsAttributes.getSelectedIndex()==0)
                {
                    tableAttributesLeft.setModel(tmAttributes);
                    panelAttributeLeft.setBorder(javax.swing.BorderFactory.createTitledBorder("Numeric attributes"));

                    tableAttributesLeft.setDefaultRenderer(Object.class, new DefaultRender());
                    panelAttributeLeft.repaint();
                    panelAttributeLeft.validate();
                }
            }
            
            fillTableChiPhiCoefficients(dataset);
            
            //Critical value for 99% confidence
            double critical_value = 6.635;

            jTableChiPhi.setDefaultRenderer(Object.class, new BaseRender("chi_fi",critical_value ));
            fixedTableChiPhi.setDefaultRenderer(Object.class, new BaseRender("chi_fi_fixed", critical_value));
            
            panelChiPhi.repaint();
            panelChiPhi.validate();

            //tm_coocurrences   
            pairs = AttributePairsUtils.getAttributePairs(dataset);   
             
            fillTableCoefficients(dataset, "coocurrence");
            jTableCoocurrences.setDefaultRenderer(Object.class, new BaseRender("estandar",Double.MAX_VALUE));
            fixedTableCoocurrences.setDefaultRenderer(Object.class, new BaseRender("chi_fi_fixed",Double.MAX_VALUE));
           
            panelCoOcurrenceValues.repaint();
            panelCoOcurrenceValues.validate();

            fillTableCoefficients(dataset,"heapmap");
            jTableHeatmap.setDefaultRenderer(Object.class, new BaseRender("heatmap",Double.MAX_VALUE));
            fixedTableHeatmap.setDefaultRenderer(Object.class, new BaseRender("chi_fi_fixed",Double.MAX_VALUE));
             
            panelHeatmapValues.repaint();
            panelHeatmapValues.validate();

            tableCoOcurrenceLeft.setRowSelectionAllowed(true);
            tableCoOcurrenceLeft.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
              
            tableCoOcurrenceLeft.setModel(tmCoocurrences);
            tableHeatmapLeft.setModel(tmHeatmap);

            ArrayList<String> seleccionados= new  ArrayList();

            /*
            int firstSelected=10;
            if(firstSelected> dataset.getNumLabels()) {
                firstSelected = dataset.getNumLabels();
            }
         
            String current; 

            for(int i=0;i<firstSelected; i++)
            {
                current = (tableCoOcurrenceLeft.getValueAt(i, 0).toString());
                if(current != null){
                    seleccionados.add(current);
                }
                else break;
            }
            */
        
            seleccionados = selectTopCoocurrenceLabels(10, true);

            ArrayList<AttributesPair> selectedPairs = AttributePairsUtils.findSelectedAttributesPair(pairs, seleccionados);
        
            String[] labelname1 = Utils.listToArray(seleccionados);
       
            graphComponent = createJGraphX(panelCoOcurrenceRight,selectedPairs,labelname1,graphComponent);

            heatMap = createHeatmapGraph(panelHeatmap, getHeatMapCoefficients(), null, heatMap);
        
            // jpanel8 box diagram
            labelsBoxDiagram.getChart().getXYPlot().clearAnnotations();
            labelsBoxDiagram.getChart().setTitle("");
            attributesBoxDiagram2.getChart().getXYPlot().clearAnnotations();
            attributesBoxDiagram2.getChart().setTitle("");
               
            DefaultXYDataset xyseriescollection = new DefaultXYDataset();
            DefaultXYDataset xyseriescollection1 = new DefaultXYDataset();
              
            labelsBoxDiagram.getChart().getXYPlot().setDataset(xyseriescollection);             
            labelsBoxDiagram.getChart().getXYPlot().setDataset(1, xyseriescollection1);
            
            attributesBoxDiagram2.getChart().getXYPlot().setDataset(xyseriescollection);             
            attributesBoxDiagram2.getChart().getXYPlot().setDataset(1, xyseriescollection1);
            
            jButtonSaveDatasets.setEnabled(false);
            jComboBoxSaveFormat.setEnabled(false);
            
        } catch (InvalidDataFormatException ex) {
            Logger.getLogger(CrossValidationExperiment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.INFORMATION_MESSAGE);
            Logger.getLogger(CrossValidationExperiment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Select n top co-ocurrence labels
     * 
     * @param n Number of labels to select
     * @param selectInTable Indicates if the rows are selected in the table
     * @return List with the names of the selected labels
     */
    public ArrayList<String> selectTopCoocurrenceLabels(int n, 
            boolean selectInTable){

        ArrayList<String> pairsString = new ArrayList<>();
            
        ArrayList<LabelsPairValue> pairs = new ArrayList<>();
        for(int i=0; i<coocurrenceCoefficients.length; i++){
            for(int j=0; j<coocurrenceCoefficients.length; j++){
                if(coocurrenceCoefficients[i][j] >= 0){
                    pairs.add(new LabelsPairValue(i, j, coocurrenceCoefficients[i][j]));
                }
            }
        }
        Collections.sort(pairs, Collections.reverseOrder());

        int numLabels = n;
        int currentSelectedLabels = 0;

        Vector<Integer> selectedLabels = new Vector<>();

        do{
            if(!selectedLabels.contains(pairs.get(0).label1)){
                selectedLabels.add(pairs.get(0).label1);
                currentSelectedLabels++;
            }
                
            if(currentSelectedLabels < numLabels){
                if(!selectedLabels.contains(pairs.get(0).label2)){
                    selectedLabels.add(pairs.get(0).label2);
                    currentSelectedLabels++;
                }
            }
                
            pairs.remove(pairs.get(0));
        }while((pairs.size() > 0) && (currentSelectedLabels < numLabels));

        String s = new String();
            
        for(int i=0; i<selectedLabels.size(); i++){
            s = jTableCoocurrences.getColumnName(selectedLabels.get(i));

            if(s != null){
                pairsString.add(s);
            }
        }
        
        if(selectInTable){
            tableCoOcurrenceLeft.clearSelection();
                
            String labelName;
            for(int i=0; i<selectedLabels.size(); i++){
                labelName = dataset.getLabelNames()[selectedLabels.get(i)];
                for(int r=0; r<tableCoOcurrenceLeft.getRowCount(); r++){
                    if(tableCoOcurrenceLeft.getValueAt(r, 0).equals(labelName)){
                        tableCoOcurrenceLeft.addRowSelectionInterval(r, r);
                    }
                }   
            }
        }
        return pairsString;
    }
     
    /**
     * Select n top heatmap labels
     * 
     * @param n Number of labels to select
     * @param selectInTable Indicates if the rows are selected in the table
     * @return List with the names of the selected labels
     */
    public ArrayList<String> selectTopHeatmapLabels(int n, 
            boolean selectInTable){
            
        ArrayList<String> pairsString = new ArrayList<>();
            
        ArrayList<LabelsPairValue> pairs = new ArrayList<>();
        for(int i=0; i<heatmapCoefficients.length; i++){
            for(int j=0; j<heatmapCoefficients.length; j++){
                if(heatmapCoefficients[i][j] >= 0){
                    pairs.add(new LabelsPairValue(i, j, heatmapCoefficients[i][j]));
                }
            }
        }
        Collections.sort(pairs, Collections.reverseOrder());
        
        int numLabels = n;
        int currentSelectedLabels = 0;

        Vector<Integer> selectedLabels = new Vector<>();

        do{
            if(!selectedLabels.contains(pairs.get(0).label1)){
                selectedLabels.add(pairs.get(0).label1);
                currentSelectedLabels++;
            }
                
            if(currentSelectedLabels < numLabels){
                if(!selectedLabels.contains(pairs.get(0).label2)){
                    selectedLabels.add(pairs.get(0).label2);
                        currentSelectedLabels++;
                }
            }
                
            pairs.remove(pairs.get(0));
        }while((pairs.size() > 0) && (currentSelectedLabels < numLabels));

        String s;
            
        for(int i=0; i<selectedLabels.size(); i++){
            s = jTableHeatmap.getColumnName(selectedLabels.get(i));

            if(s != null){
                pairsString.add(s);
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
            }
        }
        
        return pairsString;
    }
    
    /**
     * Print the maint metrics for a dataset
     * 
     * @param dataset Multi-label dataset to calculate the metrics
     */
    private void printMainMetrics(MultiLabelInstances dataset)
    {
        //Relation
        if(datasetCurrentName.length() > 30){
            labelRelationValue.setText(datasetCurrentName.substring(0, 28) + "...");
        }
        else{
            labelRelationValue.setText(datasetCurrentName);
        }

        //Metrics
        mldc.size.Instances instances = new mldc.size.Instances();
        instances.calculate(dataset);
        mldc.size.Attributes attributes = new mldc.size.Attributes();
        attributes.calculate(dataset);
        Labels labels = new Labels();
        labels.calculate(dataset);
        Density density = new Density();
        density.calculate(dataset);
        Cardinality cardinality = new Cardinality();
        cardinality.calculate(dataset);
        Diversity diversity = new Diversity();
        diversity.calculate(dataset);
        Bound bound = new Bound();
        bound.calculate(dataset);
        DistinctLabelsets distinct = new DistinctLabelsets();
        distinct.calculate(dataset);
        LxIxF lif = new LxIxF();
        lif.calculate(dataset);

        labelInstancesValue.setText(getMetricValueFormatted(instances));
        labelAttributesValue.setText(getMetricValueFormatted(attributes));
        labelLabelsValue.setText(getMetricValueFormatted(labels));    
        labelDensityValue.setText(getMetricValueFormatted(density));
        labelCardinalityValue.setText(getMetricValueFormatted(cardinality));
        labelDiversityValue.setText(getMetricValueFormatted(diversity));
        labelBoundValue.setText(getMetricValueFormatted(bound));
        labelDistinctValue.setText(getMetricValueFormatted(distinct));
        labelLxIxFValue.setText(getMetricValueFormatted(lif));
    }
    
    /**
     * Get the metric value formatted for the GUI
     * 
     * @param metric Metric to format
     * @return String with the formatted metric value
     */
    public String getMetricValueFormatted(MLDataMetric metric){
        String value;
        NumberFormat formatter;
        String name = metric.getName();
        
        double numericValue = metric.getValue();
        
        //Scientific notation
        if( ((Math.abs(numericValue*1000) < 1.0) && 
                (Math.abs(numericValue*1000) > 0.0)) 
             || (Math.abs(numericValue/1000) > 10))
        {
            formatter = new DecimalFormat("0.###E0");
            value = formatter.format(numericValue);
        }
        //Integer values
        else if((name.toLowerCase().equals("attributes"))
                    || (name.toLowerCase().equals("bound"))
                    || (name.toLowerCase().equals("distinct labelsets"))
                    || (name.toLowerCase().equals("instances"))
                    || (name.toLowerCase().equals("LIF"))
                    || (name.toLowerCase().equals("labels"))
                    || (name.toLowerCase().equals("number of binary attributes"))
                    || (name.toLowerCase().equals("number of labelsets up to 2 examples"))
                    || (name.toLowerCase().equals("number of labelsets up to 5 examples"))
                    || (name.toLowerCase().equals("number of labelsets up to 10 examples"))
                    || (name.toLowerCase().equals("number of labelsets up to 50 examples"))
                    || (name.toLowerCase().equals("number of nominal attributes"))
                    || (name.toLowerCase().equals("number of unique labelsets"))
                    || (name.toLowerCase().equals("number of unconditionally dependent label pairs by chi-square test")))
        {
            formatter = new DecimalFormat("#0");
            value = formatter.format(numericValue);
        }
        //Decimal values
        else{
            formatter = new DecimalFormat("#0.000");
            value = formatter.format(numericValue);
        }
        
        return(value.replace(",", "."));
    }
  
    /**
     * Action for Save button from principal tab
     * @param evt Event
     * @param jtable Table
     * @throws IOException 
     */
    private void buttonSaveActionPerformedPrincipal(java.awt.event.ActionEvent 
            evt, JTable jtable) throws IOException
    {
        ArrayList<String> metricsList = getMetricsSelectedPrincipal(jtable);
                
        if(dataset == null) {
            JOptionPane.showMessageDialog(null, "You must load a dataset.", "Warning", JOptionPane.ERROR_MESSAGE);
            return; 
        }
        
        JFileChooser fc= new JFileChooser();
        
        // extension txt
        FileNameExtensionFilter fname = new FileNameExtensionFilter(".txt", "txt");
        FileNameExtensionFilter fname2 = new FileNameExtensionFilter(".csv", "csv");
        FileNameExtensionFilter fname3 = new FileNameExtensionFilter(".arff", ".arff");
        FileNameExtensionFilter fname4 = new FileNameExtensionFilter(".tex", ".tex");
        
        //Remove default
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
                
            String path;
            BufferedWriter bw;
            PrintWriter wr;
            
            switch (f1.getDescription()) {
                case ".txt":
                    path = file.getAbsolutePath() +".txt";
                    bw = new BufferedWriter(new FileWriter(path));
                    wr = new PrintWriter(bw);
                    ResultsIOUtils.saveMetricsTxt(wr, metricsList, dataset, tableMetrics);
                    wr.close();
                    bw.close();
                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case ".tex":
                    path = file.getAbsolutePath() +".tex";
                    bw = new BufferedWriter(new FileWriter(path));
                    wr = new PrintWriter(bw);
                    ResultsIOUtils.saveMetricsTex(wr, metricsList, tableMetrics);
                    wr.close();
                    bw.close();
                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case ".csv":
                    path = file.getAbsolutePath() +".csv";
                    bw = new BufferedWriter(new FileWriter(path));
                    wr = new PrintWriter(bw);
                    ResultsIOUtils.saveMetricsCsv(wr, metricsList, dataset, tableMetrics);
                    wr.close();
                    bw.close();
                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case ".arff":
                    path = file.getAbsolutePath() +".arff";
                    bw = new BufferedWriter(new FileWriter(path));
                    wr = new PrintWriter(bw);
                    ResultsIOUtils.saveMetricsArff(wr, metricsList, dataset, tableMetrics);
                    wr.close();
                    bw.close();
                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE);
                    break;
                default:
                    break; 
            }
                
            Toolkit.getDefaultToolkit().beep();
        }
    }
      
    /**
     * Action for Save button from multiple datasets tab
     * 
     * @param evt Event
     * @param jtable Table
     * @throws IOException 
     */
    private void buttonSaveActionPerformedMulti(java.awt.event.ActionEvent evt, 
            JTable jtable) throws IOException
    {
        ArrayList<String> metricsList = getSelectedMetricsMulti(jtable);
        
        if(listDatasets == null || listDatasets.isEmpty() || datasetNames.isEmpty()) {
           JOptionPane.showMessageDialog(null, "You must load a dataset.", "Warning", JOptionPane.ERROR_MESSAGE);
           return; 
        }
             
        //JFileChooser save
        JFileChooser fc= new JFileChooser();
        
        FileNameExtensionFilter fname = new FileNameExtensionFilter(".txt", "txt");
        FileNameExtensionFilter fname2 = new FileNameExtensionFilter(".csv", "csv");
        FileNameExtensionFilter fname3 = new FileNameExtensionFilter(".arff", ".arff");
        FileNameExtensionFilter fname4 = new FileNameExtensionFilter(".tex", ".tex");
        
        //Remove default
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
            
            String path;
            BufferedWriter bw;
            PrintWriter wr;
            
            switch (f1.getDescription()) {
                case ".txt":
                    path = file.getAbsolutePath() +".txt";
                    bw = new BufferedWriter(new FileWriter(path));
                    wr = new PrintWriter(bw);
                    ResultsIOUtils.saveMultiMetricsTxt(wr, metricsList, datasetNames, tableMetricsMulti);
                    wr.close();
                    bw.close();
                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case ".tex":
                    path = file.getAbsolutePath() +".tex";
                    bw = new BufferedWriter(new FileWriter(path));
                    wr = new PrintWriter(bw);
                    ResultsIOUtils.saveMultiMetricsTex(wr, metricsList, datasetNames, tableMetricsMulti);
                    wr.close();
                    bw.close();
                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case ".csv":
                    path = file.getAbsolutePath() +".csv";
                    bw = new BufferedWriter(new FileWriter(path));
                    wr = new PrintWriter(bw);
                    ResultsIOUtils.saveMultiMetricsCsv(wr, metricsList, datasetNames, tableMetricsMulti);
                    wr.close();
                    bw.close();
                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case ".arff":
                    path = file.getAbsolutePath() +".arff";
                    bw = new BufferedWriter(new FileWriter(path));
                    wr = new PrintWriter(bw);
                    ResultsIOUtils.saveMultiMetricsArff(wr, metricsList, datasetNames, tableMetricsMulti);
                    wr.close();
                    bw.close();
                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE);
                    break;
                default:
                    break; 
            }
                
            Toolkit.getDefaultToolkit().beep();
        } 
    }
     
    /**
     * Action for Calculate button from principal tab
     * 
     * @param evt Event
     * @param jtable Table
     */
    private void buttonCalculateActionPerformedPrincipal(
            java.awt.event.ActionEvent evt, JTable jtable)
    {
        ArrayList<String> metricsList = getMetricsSelectedPrincipal(jtable);

        if(dataset == null) {
            JOptionPane.showMessageDialog(null, "You must load a dataset.", "Warning", JOptionPane.ERROR_MESSAGE);
            return; 
        }
        else if(metricsList.isEmpty()){
            JOptionPane.showMessageDialog(null, "You must select any metric.", "Warning", JOptionPane.ERROR_MESSAGE);
            return; 
        }

        //ImbalancedFeature[] label_frenquency = MetricUtils.getImbalancedDataByAppearances(dataset);
        //label_frenquency = MetricUtils.sortByFrequency(label_frenquency);// ordena de mayor a menor

        String value;

        progressBar.setMinimum(0);
        progressBar.setMaximum(metricsList.size()+1);
        progressBar.setValue(0);
        int v = 1;
        for(String metric : metricsList)
        {
            progressBar.setValue(v);
            //If metric value exists, don't calculate
           if((tableMetrics.get(metric) == null) || (tableMetrics.get(metric).equals("-"))){
               value = MetricUtils.getMetricValue(metric, dataset);
                tableMetrics.put(metric, value.replace(",", "."));
           }
           
           v++;
        }
       
        TableModel model = jtable.getModel();
        
        for(int i=0; i<model.getRowCount(); i++){
            model.setValueAt(MetricUtils.getValueFormatted(model.getValueAt(i, 0).toString(), tableMetrics.get(model.getValueAt(i, 0).toString())), i, 1);
        }

        jtable.repaint();
    }   

    /**
     * Action for Calculate button from multiple datasets tab
     * 
     * @param evt Event
     * @param jtable Table
     */
    private void buttonCalculateActionPerformedMulti(java.awt.event.ActionEvent 
            evt, JTable jtable)
    {
        ArrayList<String> metricsList = getSelectedMetricsMulti(jtable);

        if(listDatasets == null || listDatasets.size() < 1) {
            JOptionPane.showMessageDialog(null, "You must load a dataset.", "Warning", JOptionPane.ERROR_MESSAGE);
            return; 
        }
        else if(metricsList.isEmpty()){
            JOptionPane.showMessageDialog(null, "You must select any metric.", "Warning", JOptionPane.ERROR_MESSAGE);
            return; 
        }

        ImbalancedFeature[] labelsFrequency;            
        String value;

        progressBar.setMinimum(0);
        progressBar.setMaximum(metricsList.size() * datasetNames.size() + 1);
        progressBar.setValue(0);
        int v = 1;
        
        int d = 0;
        for(String dataName : datasetNames){
            labelsFrequency = MetricUtils.getImbalancedDataByAppearances(listDatasets.get(d));
            labelsFrequency = MetricUtils.sortByFrequency(labelsFrequency);

            if(!tableMetricsMulti.contains(dataName)){
                tableMetricsMulti.put(dataName, new Hashtable<String, String>());
                initTableMetricsMulti(dataName);
            }
            
            for(String metric : metricsList)
            {
                progressBar.setValue(v);
                
                //If metric value exists, don't calculate
               if((tableMetricsMulti.get(dataName).get(metric) == null) || (tableMetricsMulti.get(dataName).get(metric).equals("-"))){
                   value = MetricUtils.getMetricValue(metric, listDatasets.get(d));
                    tableMetricsMulti.get(dataName).put(metric, value.replace(",", "."));
               } 
               
               v++;
            }
            
            d++;
        }

        jtable.repaint();
    }   

    /**
     * Clear table of metrics from principal tab
     */
    private void clearTableMetricsPrincipal()
    {
        ArrayList<String> metricsList = MetricUtils.getAllMetrics();

        for(String metric : metricsList)
        {
            if(metric.charAt(0) != '<'){
              tableMetrics.put(metric, "-");  
            }
            else{
                tableMetrics.put(metric, "");
            }
        }
       
        TableModel model = jTablePrincipal.getModel();
        
        for(int i=0; i<model.getRowCount(); i++){
            if(metricsList.get(i).charAt(0) != '<'){
               model.setValueAt(tableMetrics.get(model.getValueAt(i, 0).toString()), i, 1); 
            }
        }
        
        jTablePrincipal.repaint();
    } 
     
    /**
     * Get selected metrics in table from principal tab
     * 
     * @param jtable Table
     * @return List with the selected metrics
     */
    private ArrayList<String> getMetricsSelectedPrincipal(JTable jtable)
    {
        ArrayList<String> result = new ArrayList();
        TableModel tmodel = jtable.getModel();

        for(int i=0; i<tmodel.getRowCount();i++)
        {
            if((Boolean)tmodel.getValueAt(i, 2))
            {
                String selected = (String)tmodel.getValueAt(i, 0);
                result.add(selected);                      
            }                
        }   
        return result;
    }

    /**
     * Get selected metrics in table from multiple datasets tab
     * 
     * @param jtable Table
     * @return List with the selected metrics
     */
    private ArrayList<String> getSelectedMetricsMulti(JTable jtable)
    {
        ArrayList<String> result = new ArrayList();
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

    /**
     * Action for Invert button from principal tab
     * 
     * @param evt Event
     * @param jtable Table
     */
    private void buttonInvertActionButtonPerformed(java.awt.event.ActionEvent 
            evt, JTable jtable )
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

    /**
     * Action for Invert button from multiple datasets tab
     * 
     * @param evt Event
     * @param jtable Table
     */
    private void buttonInvertActionPerformedMulti(java.awt.event.ActionEvent 
            evt, JTable jtable )
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
    
    /**
     * Action for None button from principal tab
     * 
     * @param evt Event
     * @param jtable Table
     */
    private void buttonNoneActionPerformedPrincipal(java.awt.event.ActionEvent 
            evt,JTable jtable)
    {
        TableModel tmodel = jtable.getModel();
       
        for(int i=0; i<tmodel.getRowCount();i++)
        {
            tmodel.setValueAt(Boolean.FALSE, i, 2);
        }

        jtable.setModel(tmodel);
        jtable.repaint();
    }      
    
    /**
     * Action for None button from multiple datasets tab
     * 
     * @param evt Event
     * @param jtable Table
     */
    private void buttonNoneActionPerformedMulti(java.awt.event.ActionEvent evt, 
            JTable jtable)
    {
        TableModel tmodel = jtable.getModel();
       
        for(int i=0; i<tmodel.getRowCount();i++)
        {
            tmodel.setValueAt(Boolean.FALSE, i, 1);
        }

        jtable.setModel(tmodel);
        jtable.repaint();
    }      
    
    /**
     * Action for All button from principal tab
     * 
     * @param evt Event
     * @param jtable Table
     */
    private void buttonAllActionPerformedPrincipal(java.awt.event.ActionEvent 
            evt, JTable jtable)
    {
        TableModel tmodel = jtable.getModel();

        for(int i=0; i<tmodel.getRowCount();i++)
        {
            tmodel.setValueAt(Boolean.TRUE, i, 2);
        }

        jtable.setModel(tmodel);
        jtable.repaint();
    }
    
    /**
     * Action for All button from multiple datasets tab
     * 
     * @param evt Event
     * @param jtable Table
     */
    private void buttonAllActionPerformedMulti(java.awt.event.ActionEvent evt,
            JTable jtable)
    {
        TableModel tmodel = jtable.getModel();

        for(int i=0; i<tmodel.getRowCount();i++)
        {
            tmodel.setValueAt(Boolean.TRUE, i, 1);
        }

        jtable.setModel(tmodel);
        jtable.repaint();
    }
    
    /**
     * Action for Clear button from principal tab
     * 
     * @param evt Event
     * @param jtable Table
     */
    private void buttonClearActionPerformedPrincipal(java.awt.event.ActionEvent 
            evt, JTable jtable)
    {
        TableModel tmodel = jtable.getModel();

        for(int i=0; i<tmodel.getRowCount();i++)
        {
            tmodel.setValueAt(Boolean.FALSE, i, 2);
        }

        clearTableMetricsPrincipal();
    }

    /**
     * Action for Export button
     * 
     * @param evt Event
     * @param jtable Table
     */
    private void buttonExportActionPerformed(java.awt.event.ActionEvent evt, 
            JTable jtable)
    {
        if(jtable.getRowCount()==0 || dataset == null)
        {
            JOptionPane.showMessageDialog(null, "The table is empty.", "Error", JOptionPane.ERROR_MESSAGE); 
            return;
        }

        JFileChooser fc = new JFileChooser();
        
        //FileNameExtensionFilter fname = new FileNameExtensionFilter(".xls", "xls"); 
        FileNameExtensionFilter fname1 =  new FileNameExtensionFilter(".csv", "csv");
        
        //Remove default
        fc.removeChoosableFileFilter(fc.getChoosableFileFilters()[0]);
        
        fc.setFileFilter(fname1);
        
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
                    
                    switch (comboBoxLabelsInformation.getSelectedIndex()) {
                        case 1:
                            ResultsIOUtils.saveTableLabelsetsFrequencyCsv(wr, jtable, labelsetStringsByFreq);
                            break;
                        case 6:
                            ResultsIOUtils.saveTableLabelsetsIRCsv(wr, jtable, labelsetStringByIR);
                            break;
                        default:
                            ResultsIOUtils.saveTableCsv(wr, jtable);
                            break;
                    }
                
                    wr.close();
                    bw.close(); 
                    
                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE);                     
                }
                catch(IOException | HeadlessException e1)
                {
                    JOptionPane.showMessageDialog(null, "File not saved correctly.", "Error", JOptionPane.ERROR_MESSAGE); 
                }   
                             
            }
        }
    }
    
    /**
     * Action for Export button
     * 
     * @param evt Event
     * @param jtable Table
     * @param columns Columns
     * @param table Table type
     */
    private void buttonExportActionPerformed(java.awt.event.ActionEvent evt, 
            JTable jtable, JTable columns, String table)
    {
        if(jtable.getRowCount()==0 || dataset == null)
        {
            JOptionPane.showMessageDialog(null, "The table is empty.", "Error", JOptionPane.ERROR_MESSAGE); 
            return;
        }
        
        JFileChooser fc= new JFileChooser();

        //FileNameExtensionFilter fname = new FileNameExtensionFilter(".xls", "xls"); 
        FileNameExtensionFilter fname1 =  new FileNameExtensionFilter(".csv", "csv");
        
        //Remove default
        fc.removeChoosableFileFilter(fc.getChoosableFileFilters()[0]);

        fc.setFileFilter(fname1);
        
        int returnVal = fc.showSaveDialog(this);
         
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            File file = fc.getSelectedFile();
            FileFilter f1 = fc.getFileFilter();
                
            //Saving csv chi_phi
            if(f1.getDescription().equals(".csv"))
            {
                BufferedWriter bw;
                PrintWriter wr;
                
                try
                {
                    String path = file.getAbsolutePath() +".csv";
                    bw = new BufferedWriter(new FileWriter(path));
                    wr = new PrintWriter(bw);
                    
                    switch (table) {
                        case "ChiPhi":
                            ResultsIOUtils.saveChiPhiTableCsv(wr, chiPhiCoefficients, dataset.getLabelNames());
                            break;
                        case "Coocurrence":
                            ResultsIOUtils.saveCoocurrenceTableCsv(wr, coocurrenceCoefficients, dataset.getLabelNames());
                            break;
                        case "Heatmap":
                            ResultsIOUtils.saveHeatmapTableCsv(wr, heatmapCoefficients, dataset.getLabelNames());
                            break;
                        default: 
                            JOptionPane.showMessageDialog(null, "File not saved correctly.", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                    }
                    
                    wr.close();
                    bw.close();  
                    
                    JOptionPane.showMessageDialog(null, "File saved.", "Successful", JOptionPane.INFORMATION_MESSAGE); 
                }
                catch(IOException | HeadlessException e1)
                {
                    JOptionPane.showMessageDialog(null, "File not saved correctly.", "Error", JOptionPane.ERROR_MESSAGE); 
                }         
            }              
            else if(f1.getDescription().equals(".xls"))
            {                                        
                try
                {
                    String path = file.getAbsolutePath() +".xls";
                    Exporter exp = new Exporter(new File(path), jtable, "prueba");
                     
                    if(exp.export(columns))
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

    /**
     * Create graph
     * 
     * @param jpanel Panel
     * @return ChartPanel with the generated graph
     */
    private ChartPanel createGraph(JPanel jpanel) 
    {
        XYDataset xydataset  = new DefaultXYDataset();
        JFreeChart chart = ChartFactory.createXYLineChart("Box diagram", "Values", "", xydataset, PlotOrientation.VERTICAL, false, true, false);
 
        XYPlot xyplot = (XYPlot) chart.getPlot();
        xyplot.setBackgroundPaint(Color.white);
        xyplot.setDomainGridlinePaint(Color.gray);
        xyplot.setRangeGridlinePaint(Color.gray);
         
        //Hide Y axis
        xyplot.getRangeAxis().setTickLabelsVisible(false);  
 
        ChartPanel cp1 = new ChartPanel(chart);
        cp1.setSize(new Dimension(450,300));        
        cp1.setBounds(260,100,450,300);
        cp1.setPreferredSize(new Dimension(450,300));
        cp1.repaint();
        
        jpanel.setBounds(260,100,450,300);
        jpanel.setLayout(new BorderLayout());
        jpanel.add(cp1,BorderLayout.CENTER);
        jpanel.repaint();
        jpanel.validate();
        
        return cp1;       
    }

    /**
     * Create Chart
     * 
     * @param jpanel Panel
     * @param type Chart type
     * @param titleX Title for X axis
     * @param titleY Title for Y axis
     * @param showXAxis Indicates if shows X axis
     * @param chartTitle Chart title
     * @return ChartPanel with the generated chart
     */
    private ChartPanel createJChart(JPanel jpanel, String type, 
            String titleX, String titleY, boolean showXAxis, String chartTitle)
    {
        DefaultCategoryDataset myData = new DefaultCategoryDataset();
        JFreeChart chart1;

        CategoryPlot plot1;
        
        LineAndShapeRenderer lineandshaperenderer;
        
        //hide horizontal axis
        switch (type) {
            case "bar":
                chart1 = ChartFactory.createBarChart(chartTitle, titleY, titleX, myData, PlotOrientation.VERTICAL, false, true, false);
                plot1 =  chart1.getCategoryPlot();
                //Custom tooltips
                BarRenderer renderer = (BarRenderer) plot1.getRenderer();
                switch (chartTitle.toLowerCase()) {
                    case "label frequency":
                        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator(
                                "{0} = {2}", NumberFormat.getInstance()));
                        break;
                    case "labelset frequency":
                        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator(
                                "{0} = {2}", NumberFormat.getInstance()));
                        break;
                    case "labels histogram":
                        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator(
                                "Instances with {1} labels = {2}", NumberFormat.getInstance()));
                        break;
                    case "ir inter class":
                        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator(
                                "{0} = {2}", NumberFormat.getInstance()));
                        break;
                    case "ir intra class":
                        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator(
                                "{0} = {2}", NumberFormat.getInstance()));
                        break;
                    case "ir per labelset":
                        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator(
                                "ID: {0} = {2}", NumberFormat.getInstance()));
                        break;
                    default:
                        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
                        break;
                }
                plot1.setBackgroundAlpha(0);
                plot1.setRangeGridlinePaint(Color.black);
                break;
            case "line_2_axis":
                chart1 = ChartFactory.createLineChart(" ",titleX,titleY , myData, PlotOrientation.VERTICAL, false, true, false);
                plot1 =  chart1.getCategoryPlot();
                plot1.setRangeGridlinePaint(Color.black);
                
                //show little rectangles
                lineandshaperenderer = (LineAndShapeRenderer)plot1.getRenderer();
                lineandshaperenderer.setBaseShapesVisible(true);
                CategoryAxis domainAxis = plot1.getDomainAxis();
                domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
                
                ValueAxis axis2 = new NumberAxis("# Labels");
                plot1.setRangeAxis(1, axis2);
                plot1.mapDatasetToRangeAxis(1, 1);
                LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
                plot1.setRenderer(1, renderer2);
                break;
            default:
                //type == "line")
                chart1 = ChartFactory.createLineChart(" ",titleX,titleY , myData, PlotOrientation.VERTICAL, false, true, false);
                plot1 =  chart1.getCategoryPlot();
                plot1.setRangeGridlinePaint(Color.black);
                
                //show little rectangles
                lineandshaperenderer = (LineAndShapeRenderer)plot1.getRenderer();
                lineandshaperenderer.setBaseShapesVisible(true);
                break;    
        }
        
        //Hide X axis
        plot1.getDomainAxis().setTickLabelsVisible(showXAxis);     
                 
        ChartPanel cp1 = new ChartPanel(chart1);
        cp1.setSize(new Dimension(450,300));        
        cp1.setBounds(260,100,450,300);
        cp1.setPreferredSize(new Dimension(450,300));
        cp1.repaint();
        
        jpanel.setBounds(260,100,450,300);
        jpanel.setLayout(new BorderLayout());
        jpanel.add(cp1,BorderLayout.CENTER);
        jpanel.repaint();
        jpanel.validate();
        
        return cp1;       
    }
    
    /**
     * Get the maximum value
     * 
     * @param keysets Set of keys
     * @param result Hashmap for searching through
     * @return Maximum value of the hashmap values
     */
    public static int getMax(Set<LabelSet> keysets, 
            HashMap<LabelSet,Integer> result)
    {
        int max=0;
        
        for(LabelSet current : keysets){
            if(max < result.get(current)){
                max = result.get(current);
            }
        }
        
        return max;
    }
   
    /**
     * Set label IR
     * 
     * @param jtable Table
     * @param stat Statistics
     * @param cp CategoryPlot
     * @return Generated TableModel
     * @throws Exception 
     */
    private TableModel irLabelsetsTableModel(JTable jtable, Statistics stat, 
            CategoryPlot cp ) throws Exception
    {
        DefaultTableModel tableModel = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                //This causes all cells to be not editable
                return false;
            }
        };

        DefaultCategoryDataset myData = new DefaultCategoryDataset();
               
        tableModel.addColumn("Labelset id");
        tableModel.addColumn("IR values");

        //Labelsets frequency
        HashMap<LabelSet,Integer> labelsetsFrequency = stat.labelCombCount();
        labelsetStringByIR = new ArrayList<>(labelsetsFrequency.size());
        
        Set<LabelSet> keysets = labelsetsFrequency.keySet();
        
        Object[] row = new Object[2];
        
        int count = 1;
        double IR_labelset;
        int max = getMax(keysets, labelsetsFrequency);
        
        ArrayList<ImbalancedFeature> listImbalanced = new ArrayList();
        ImbalancedFeature temp;

        int value;
        
        for(LabelSet current : keysets)
        {
            value = labelsetsFrequency.get(current);
            IR_labelset = max /(value*1.0);
            String temp1 = MetricUtils.truncateValue(IR_labelset, 4);
            IR_labelset = Double.parseDouble(temp1);
            
            temp = new ImbalancedFeature(current.toString(), value, IR_labelset);
            listImbalanced.add(temp);
        }      
         
        labelsetsIRSorted = new ImbalancedFeature[listImbalanced.size()];
        labelsetsByIR = new double[listImbalanced.size()]; //stores IR per labelset
         
        String truncate;
         
        while(!listImbalanced.isEmpty())
        {
            temp = Utils.getMin(listImbalanced);
            
            labelsetsIRSorted[count-1]= temp;
            labelsetsByIR[count-1]=temp.getIRIntraClass();

            row[0] = count;    
             
            truncate = Double.toString(temp.getIRIntraClass());
            row[1] = MetricUtils.getValueFormatted(truncate, 3);
             
            tableModel.addRow(row);

            myData.setValue(temp.getIRIntraClass(), Integer.toString(count),"");
            labelsetStringByIR.add(temp.getName());
            
            count++;
            listImbalanced.remove(temp);
        }
        
        jtable.setModel(tableModel);
        jtable.setBounds(jtable.getBounds());
          
        //Resize columns
        TableColumnModel tcm = jtable.getColumnModel();
            
        tcm.getColumn(0).setPreferredWidth(50);
        tcm.getColumn(1).setPreferredWidth(50);
            
        //graph
        cp.setDataset(myData);
          
        //get mean
        double sum=0;
        for(int i=0; i<labelsetsIRSorted.length;i++)
        {
            sum+= labelsetsIRSorted[i].getIRIntraClass();
        }
        sum = sum/labelsetsIRSorted.length;
          
        Marker start = new ValueMarker(sum);
        start.setPaint(Color.blue);
        start.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
        start.setLabel("                        Mean: "+MetricUtils.truncateValue(sum, 4));
        cp.addRangeMarker(start);
                        
        return jtable.getModel();
    }

    /**
     * Set frequency of labelsets
     * 
     * @param jtable Table
     * @param dataset Dataset
     * @param stat Statistics
     * @param cp Plot
     * @return Generated TableModel
     * @throws Exception 
     */
    private TableModel labelsetsFrequencyTableModel(JTable jtable, 
            MultiLabelInstances dataset, Statistics stat, CategoryPlot cp) 
            throws Exception
    {
        DefaultTableModel tableModel = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                //This causes all cells to be not editable
                return false;
            }
        };

        DefaultCategoryDataset myData = new DefaultCategoryDataset();
               
        tableModel.addColumn("Labelset Id");
        tableModel.addColumn("# Examples");
        tableModel.addColumn("Frequency");
                      
        double freq;
        
        //Labelsets frequency
        HashMap<LabelSet,Integer> result = stat.labelCombCount();
        labelsetStringsByFreq = new ArrayList<>(result.size());
        
        double sum = 0.0;
        Set<LabelSet> keysets = result.keySet();
        
        Object[] row = new Object[3];
        
        int count = 1;
        
        ArrayList<ImbalancedFeature> listImbalanced = new ArrayList();
        ImbalancedFeature temp;

        int value;
        for(LabelSet current : keysets)
        {
            value = result.get(current);
            temp = new ImbalancedFeature(current.toString(), value);
            listImbalanced.add(temp);
        }      
        
        labelsetsSorted = new ImbalancedFeature[listImbalanced.size()];
        labelsetsFrequency = new double[listImbalanced.size()];
         
        while(!listImbalanced.isEmpty())
        {
            temp = Utils.getMax(listImbalanced);
            labelsetsSorted[count-1]= temp;
            value = temp.getAppearances();            
            labelsetsFrequency[count-1]= value;                    
            row[0]=count;             
            freq =value*1.0/dataset.getNumInstances();             
            sum += freq;  
            
            String valueFreq = Double.toString(freq);             
            row[1]= value;             

            row[2] = MetricUtils.getValueFormatted(valueFreq, 4);
            tableModel.addRow(row);

            String id = "ID: " + Integer.toString(count);

            myData.setValue(freq, id,"");
            labelsetStringsByFreq.add(temp.getName());
             
            count++;
            listImbalanced.remove(temp);
        }
        
        jtable.setModel(tableModel);
        jtable.setBounds(jtable.getBounds());
          
        TableColumnModel tcm = jtable.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(50);
        tcm.getColumn(1).setPreferredWidth(50);
        tcm.getColumn(2).setPreferredWidth(60);

        //graph
        cp.setDataset(myData);

        sum = sum/keysets.size();
        Marker start = new ValueMarker(sum);
        start.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
        start.setLabel("                        Mean: "+MetricUtils.truncateValue(sum, 4));
        start.setPaint(Color.red);
        cp.addRangeMarker(start);

        return jtable.getModel();
    }
    
    /**
     * Fill the table with coefficient values
     * 
     * @param dataset
     * @param pairs
     * @param type 
     */
    private void fillTableCoefficients(MultiLabelInstances dataset, String type)
    {
        double[][] pairLabelValues;
         
        //coocurrence values table
        if(type.equals("coocurrence")) {
            pairLabelValues = ChartUtils.getCoocurrences(dataset);
            coocurrenceCoefficients = pairLabelValues;
        }
        //heatmap values table
        else {
            pairLabelValues = getHeatMapCoefficients();
            heatmapCoefficients = pairLabelValues.clone();
        }

        /**     **/
        
        data = new Object[pairLabelValues.length][pairLabelValues.length+1];
        column = new Object[data.length+1];
         
        if(type.equals("coocurrence")) {
            for(int i=0;  i<pairLabelValues.length;i++)
            {            
                for(int j=0; j<pairLabelValues.length; j++){
                    
                    if(j == 0){
                        data[i][j] = dataset.getLabelNames()[i];
                    }
                    else if(i == (j-1)){
                        data[i][j] = "";
                    }
                    else if(j > i){
                        data[i][j] = "";
                    }
                    else{             
                        if(pairLabelValues[j-1][i] <= 0.0){
                            data[i][j] = "";
                        }
                        else{
                            data[i][j] = (int) pairLabelValues[j-1][i];
                        }
                    }
                }
            }
        }
        else{
            for(int i=0;  i<pairLabelValues.length;i++)
            {
                for(int j=0; j<pairLabelValues.length+1; j++){
                    
                    if(j == 0){
                        data[i][j] = dataset.getLabelNames()[i];
                    }
                    else{
                        if(pairLabelValues[j-1][i] <= 0.0){
                            data[i][j] = "";
                        }
                        else{
                            NumberFormat formatter = new DecimalFormat("#0.000"); 
                            data[i][j] = formatter.format(pairLabelValues[j-1][i]).replace(",", ".");
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
        
        JTable temp, fixedTable_temp;
        JPanel jpanel_temp;
       
        if(type.equals("coocurrence")){
            temp=jTableCoocurrences; 
            jpanel_temp=panelCoOcurrenceValues; 
            fixedTable_temp=fixedTableCoocurrences;
        }
        else {
            temp=jTableHeatmap;
            jpanel_temp=panelHeatmapValues; 
            fixedTable_temp=fixedTableHeatmap;
        }

        fixedTable_temp.setModel(fixedModel);
        temp.setModel(model);
       
        JScrollPane scroll = new JScrollPane(temp);
        JViewport viewport = new JViewport();
        viewport.setView(fixedTable_temp);
        viewport.setPreferredSize(fixedTable_temp.getPreferredSize());
        scroll.setRowHeaderView(viewport);
    
        scroll.setBounds(20, 20, 780, 390);

        scroll.setCorner(JScrollPane.UPPER_LEFT_CORNER, fixedTable_temp.getTableHeader());

        temp.setBorder(BorderFactory.createLineBorder(Color.black));
    
        jpanel_temp.remove(0);
        jpanel_temp.add(scroll, BorderLayout.CENTER, 0);
    }
    
    /**
     * Fill the table with Chi and Phi coefficients
     * 
     * @param dataset Multi-label dataset
     */
    private void fillTableChiPhiCoefficients(MultiLabelInstances dataset)
    {
        chiPhiCoefficients = ChartUtils.getChiPhiCoefficients(dataset);
        data = new Object[chiPhiCoefficients.length][chiPhiCoefficients.length+1];        
        column = new Object[data.length+1];
                          
        for(int i=0; i< chiPhiCoefficients.length; i++)
        {            
            data[i] = Utils.getValuesByRow(i, chiPhiCoefficients,dataset.getLabelNames()[i]);
        }
        
        for(int i=0; i<column.length; i++)
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
       
        fixedTableChiPhi.setModel(fixedModel);
        jTableChiPhi.setModel(model);
        
        JScrollPane scroll = new JScrollPane(jTableChiPhi);
        JViewport viewport = new JViewport();
        viewport.setView(fixedTableChiPhi);
        viewport.setPreferredSize(fixedTableChiPhi.getPreferredSize());
        scroll.setRowHeaderView(viewport);

        scroll.setBounds(20, 20, 780, 390);

        scroll.setCorner(JScrollPane.UPPER_LEFT_CORNER, fixedTableChiPhi.getTableHeader());

        jTableChiPhi.setBorder(BorderFactory.createLineBorder(Color.black));

        if(firstTimeChi){
            panelChiPhi.add(scroll, BorderLayout.CENTER, 0); firstTimeChi=false; 
            return; 
        }
        
        panelChiPhi.remove(0);
        panelChiPhi.add(scroll, BorderLayout.CENTER, 0); 
    }
  
    /**
     * Generate TableModel for label frequency
     * 
     * @param jtable Table
     * @param dataset Multi-label dataset
     * @return Generated TableModel
     */
    private TableModel labelFrequencyTableModel(JTable jtable, 
            MultiLabelInstances dataset)
    {
        DefaultTableModel tableModel = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                //This causes all cells to be not editable
                return false;
            }
        };

        tableModel.addColumn("Label");
        tableModel.addColumn("# Examples");
        tableModel.addColumn("Frequency");

        Object[] row = new Object[3];
        
        ImbalancedFeature current;
        double freq;
        String truncate;

        for(int i=0;i<dataset.getNumLabels();i++)
        {
            current = labelAppearances[i];
            
            row[0]=current.getName();
            freq =current.getAppearances()*1.0/dataset.getNumInstances();

            row[1]= current.getAppearances();
            
            truncate = Double.toString(freq);
            row[2]= MetricUtils.truncateValue(truncate, 4);

            tableModel.addRow(row);
        }
        
        jtable.setModel(tableModel);

        TableColumnModel tcm = jtable.getColumnModel();
            
        tcm.getColumn(0).setPreferredWidth(80);
        tcm.getColumn(1).setPreferredWidth(70);
        tcm.getColumn(2).setPreferredWidth(70);
            
        return jtable.getModel();
    }
    
    /**
     * Generates TableModel for attributes
     * 
     * @param jtable Table
     * @param dataset Multi-label dataset
     * @return Generated TableModel
     */
    private TableModel attributesTableModel(JTable jtable, 
            MultiLabelInstances dataset)
    {
        DefaultTableModel tableModel = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                //This causes all cells to be not editable
                return false;
            }
        };
               
        tableModel.addColumn("Attribute");

        Object[] row = new Object[1];
        
        Instances instances = dataset.getDataSet();
        
        int numLabels = dataset.getNumLabels();
        
        int numAttributes = instances.numAttributes() -numLabels;
         
        Attribute att;
        for (int i=0;i<numAttributes;i++) 
        {
            att = instances.attribute(i);
            if(att.isNumeric())
            {
                row[0]= att.name();
                tableModel.addRow(row);
            }
        }
        
        jtable.setModel(tableModel);

        return jtable.getModel();
    }
    
    /**
     * Generates TableModel for Box Diagrams
     * 
     * @param jtable Table
     * @param dataset Multi-label dataset
     * @return Generated TableModel
     */
    private TableModel boxplotTableModel(JTable jtable)
    {
        DefaultTableModel tableModel = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                //This causes all cells to be not editable
                return false;
            }
        };
               
        tableModel.addColumn("Diagram");

        Object[] row = new Object[1];
        
        row[0] = "#Examples per label";
        tableModel.addRow(row);
        row[0] = "#Examples per labelset";
        tableModel.addRow(row);       
        
        jtable.setModel(tableModel);

        return jtable.getModel();
    }
    
    /**
     * Generates TableModel for IR intra class
     * 
     * @param jtable
     * @return 
     */
    private TableModel irIntraClassTableModel(JTable jtable)
    {
        DefaultTableModel tableModel = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                //This causes all cells to be not editable
                return false;
            }
        };
               
        tableModel.addColumn("Label");
        tableModel.addColumn("IR");
        
        Object[] row = new Object[2];

        String truncate;

        for(int i=0; i<imbalancedLabels.length; i++)
        {                       
            truncate = Double.toString(imbalancedLabels[i].getIRIntraClass());
            
            row[0] = imbalancedLabels[i].getName();          
            row[1] = MetricUtils.getValueFormatted(truncate, 3);
      
            tableModel.addRow(row);
        }

        jtable.setModel(tableModel);

        TableColumnModel tcm = jtable.getColumnModel();
            
        tcm.getColumn(0).setPreferredWidth(80);
        tcm.getColumn(1).setPreferredWidth(50);
            
        return jtable.getModel();
    }
    
    /**
     * Generates TableModel for IR inter class
     * 
     * @param jtable
     * @return 
     */
    private TableModel irInterClassTableModel(JTable jtable)
    {
        DefaultTableModel tableModel= new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                //This causes all cells to be not editable
                return false;
            }
        };
               
        tableModel.addColumn("Label");
        tableModel.addColumn("IR");
        
        Object[] row = new Object[2];

        String truncate;
 
        IRInterClass = MetricUtils.getIRInterClassValues(labelAppearances);
        
        int temp = IRInterClass.length-1;
        for(int i=temp; i>=0; i--)
        {                  
            truncate = Double.toString(IRInterClass[i]);
            
            row[0]= labelAppearances[i].getName();          
            row[1] = MetricUtils.getValueFormatted(truncate, 3);
                        
            tableModel.addRow(row);
        }

        jtable.setModel(tableModel);
        
        TableColumnModel tcm = jtable.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(80);
        tcm.getColumn(1).setPreferredWidth(50);
             
        return jtable.getModel();
    }
  
    /**
     * Generates TableModel for imbalanced data
     * 
     * @param jtable Table
     * @param dataset Multi-label dataset
     * @return Generated TableModel
     */
    private TableModel imbalancedTableModel(JTable jtable, 
            MultiLabelInstances dataset)
    {
        DefaultTableModel tableModel = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                //This causes all cells to be not editable
                return false;
            }
        };
               
        tableModel.addColumn("Label");
        tableModel.addColumn("IR intra class");
        tableModel.addColumn("IR inter class");
        tableModel.addColumn("STDEV intra class");
        imbalancedLabels = MetricUtils.sortImbalancedDataByIRIntraClass(imbalancedLabels);

        Object[] row = new Object[4];
        double std;
        String truncate;
        
        ImbalancedFeature current;
     
        for(int i=0;i<dataset.getNumLabels();i++)
        {
            current = imbalancedLabels[i];
            
            row[0]=current.getName();          
            
            truncate = Double.toString(current.getIRIntraClass());            
            row[1]= MetricUtils.truncateValue(truncate, 5);
            
            truncate = Double.toString(current.getIRInterClass());            
            row[2]= MetricUtils.truncateValue(truncate, 5);
            
            std = Math.sqrt(current.getVariance());
            row[3]= MetricUtils.truncateValue(std,4);
            
            tableModel.addRow(row);
        }
        
        jtable.setModel(tableModel);
        
        TableColumnModel tcm = jtable.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(100);
        tcm.getColumn(1).setPreferredWidth(20);
        tcm.getColumn(2).setPreferredWidth(80);
        tcm.getColumn(3).setPreferredWidth(40);
            
        return jtable.getModel();
    }

    /**
     * Generates TableModel for labels per example values
     * 
     * @param jtable Table
     * @param labelsPerExample Number of labels per example
     * @return Generated TableModel
     */
    private TableModel labelsPerExampleTableModel(JTable jtable, 
            HashMap<Integer,Integer> labelsPerExample)
    {
        DefaultTableModel tableModel = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                //This causes all cells to be not editable
                return false;
            }
        };
               
        tableModel.addColumn("# Labels");
        tableModel.addColumn("# Examples");
        tableModel.addColumn("Frequency");

        Object[] row = new Object[3];
       
        double freq ;
        int freqCurrent;
        String truncate;

        int max = Utils.maxKey(labelsPerExample);
				 			            
	for(int i=0; i<=max ; i++)
        {
            freqCurrent = 0;
            if(labelsPerExample.get(i)!=null){
                freqCurrent=labelsPerExample.get(i);
            }
               
            freq= freqCurrent*1.0/dataset.getNumInstances();
            
            row[0]= i;
            row[1]=freqCurrent;
            truncate = Double.toString(freq);
            row[2] = MetricUtils.getValueFormatted(truncate, 4);
            
            tableModel.addRow(row);
        }
        
        jtable.setModel(tableModel);
        
        TableColumnModel tcm = jtable.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(80);
        tcm.getColumn(1).setPreferredWidth(50);
        tcm.getColumn(2).setPreferredWidth(70);
            
        return jtable.getModel();
    }

    /**
     * Generates TableModel for frequency values
     * 
     * @param jtable Table
     * @param dataset Multi-label dataset
     * @return Generated TableModel
     */
    private TableModel frequencyTableModel(JTable jtable, 
            MultiLabelInstances dataset)
    {
        DefaultTableModel tableModel = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                //This causes all cells to be not editable
                return false;
            }
        };
      
        tableModel.addColumn("Label");
        tableModel.addColumn("# Examples");
        tableModel.addColumn("Frequency");
        
        Object[] row = new Object[3];
        
        ImbalancedFeature current;
        double freq;
        String truncate;

        for(int i=0;i<dataset.getNumLabels();i++)
        {
            current = labelAppearances[i];
            
            row[0]=current.getName();
            freq =current.getAppearances()*1.0/dataset.getNumInstances();
            
            row[1]= current.getAppearances();
            
            truncate = Double.toString(freq);
            row[2] = MetricUtils.getValueFormatted(truncate, 4);

            tableModel.addRow(row);
        }
        
        jtable.setModel(tableModel);

        TableColumnModel tcm = jtable.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(80);
        tcm.getColumn(1).setPreferredWidth(70);
        tcm.getColumn(2).setPreferredWidth(50);
            
        return jtable.getModel();
    }
     
    /**
     * Create table of metrics for principal tab
     * 
     * @param table Table of metrics
     * @param jpanel Panel
     * @param rowData Cell values
     * @param posx Pos X
     * @param posy Pos Y
     * @param width Width
     * @param height Height
     */
    public void createJTableMetricsPrincipal(JTable table, JPanel jpanel, 
            Object rowData[][], int posx, int posy, int width, int height)
    {       
        TableModel model = new MetricsTableModel(rowData);
        
        table.setModel(model);

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
       
        scrollPane.setBounds(posx, posy, width, height);
        
        table.setBorder(BorderFactory.createLineBorder(Color.black));
        
        jpanel.add(scrollPane, BorderLayout.CENTER);
        jpanel.repaint();
        jpanel.validate();
    }
     
    /**
     * Set help for metrics table
     * 
     * @param jtable Table
     * @return Table with help
     */
    public JTable setMetricsHelp(JTable jtable){
        jtable = new JTable(jtable.getModel()){
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    jc.setToolTipText(MetricUtils.getMetricTooltip(getValueAt(row, 0).toString()));
                }
                return c;
            }
        };
        
        return jtable;
    }
    
    /**
     * Set help for Chi and Phi 
     * 
     * @param jtable Table
     * @return Table with help
     */
    public JTable setChiPhiTableHelp(JTable jtable){
        jtable = new JTable(jtable.getModel()){
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    if(row > column){
                        jc.setToolTipText("Chi(" + getColumnName(row) + ", " + getColumnName(column) + ")");
                    }
                    else if(column > row){
                        jc.setToolTipText("Phi(" + getColumnName(row) + ", " + getColumnName(column) + ")");
                    }
                }
                return c;
            }
        };
        
        return jtable;
    }
    
    /**
     * Set help for Co-ocurrence table
     * 
     * @param jtable Table
     * @return Table with help
     */
    public JTable setCoocurrenceTableHelp(JTable jtable){
        jtable = new JTable(jtable.getModel()){
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    if(row > column){
                        jc.setToolTipText("Coocurrence(" + getColumnName(row) + ", " + getColumnName(column) + ")");
                    }
                    else{
                        jc.setToolTipText(null);
                    }
                }
                return c;
            }
        };
        
        return jtable;
    }
    
    /**
     * Set help for heatmap table
     * 
     * @param jtable Table
     * @return Table with help
     */
    public JTable setHeatmapTableHelp(JTable jtable){
        jtable = new JTable(jtable.getModel()){
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                        
                    if(column == row){
                        jc.setToolTipText("P(" + getColumnName(row) + ")");
                    }
                    else{
                        jc.setToolTipText("P(" + getColumnName(row) + " | " + getColumnName(column) + ")");
                    }
                }
                return c;
            }
        };
        
        return jtable;
    }
    
    /**
     * Create metrics table for multiple datasets tab
     * 
     * @param table Table of metrics
     * @param jpanel Panel
     * @param rowData Cell values
     * @param posx Position X
     * @param posy Position Y
     * @param width Width
     * @param height Height
     */
    public void createJTableMetricsMultipleDatasets(JTable table, JPanel jpanel, 
            Object rowData[][], int posx, int posy, int width, int height)
    {
        TableModel model = new MetricsTableModel(rowData, "multi");
        
        table.setModel(model);

        TableColumnModel tcm = table.getColumnModel();

        tcm.getColumn(0).setPreferredWidth(320);

        tcm.getColumn(1).setPreferredWidth(40);
        tcm.getColumn(1).setMaxWidth(40);
        tcm.getColumn(1).setMinWidth(40);

        JScrollPane scrollPane = new JScrollPane(table);
        
        scrollPane.setBounds(posx, posy, width, height);
        
        table.setBorder(BorderFactory.createLineBorder(Color.black));
        
        jpanel.add(scrollPane, BorderLayout.CENTER);
        jpanel.repaint();
        jpanel.validate();
    }

    /**
     * Get heatmap coefficients
     * 
     * @return Matrix with heatmap coefficients
     */
    private double[][] getHeatMapCoefficients(){
         
        ImbalancedFeature [] labelsByFrequency = MetricUtils.getImbalancedDataByAppearances(dataset);;
        double [] frequencies = DataInfoUtils.getLabelAppearances(labelsByFrequency);
         
        double [][] coeffs = new double[dataset.getNumLabels()][dataset.getNumLabels()];
         
        for(int i=0; i<dataset.getNumLabels(); i++){
            for(int j=0; j<dataset.getNumLabels(); j++){
                
                if(frequencies[j] <= 0){
                    coeffs[i][j] = 0;
                }
                else if (i==j){
                    coeffs[i][j] = frequencies[i] / dataset.getNumInstances();
                }
                else{
                    if(coocurrenceCoefficients[i][j] > 0){
                        coeffs[i][j] = coocurrenceCoefficients[i][j] / frequencies[j];
                    }
                    else{
                        if(coocurrenceCoefficients[j][i] > 0){
                            coeffs[i][j] = coocurrenceCoefficients[j][i] / frequencies[j];
                        }
                        else{
                           coeffs[i][j] = 0; 
                        }
                    }
                }
            }
        }

        return coeffs;
    }

    /**
     * Get top n related labels for heatmap
     * 
     * @param n Number of labels to select
     * @return Selected label indices
     */
    public int[] getTopRelatedHeatmap(int n){   
        
        ArrayList<LabelsPairValue> pairs = new ArrayList<>();
        for(int i=0; i<heatmapCoefficients.length; i++){
            for(int j=0; j<heatmapCoefficients.length; j++){
                if(heatmapCoefficients[i][j] > 0){
                    pairs.add(new LabelsPairValue(i, j, heatmapCoefficients[i][j]));
                }
            }
        }
        Collections.sort(pairs, Collections.reverseOrder());
        
        int numLabels = n;
        int currentSelectedLabels = 0;

        Vector<Integer> selectedLabels = new Vector<>();

        do{
            if(!selectedLabels.contains(pairs.get(0).label1)){
                selectedLabels.add(pairs.get(0).label1);
                currentSelectedLabels++;
            }
                
            if(currentSelectedLabels < numLabels){
                if(!selectedLabels.contains(pairs.get(0).label2)){
                    selectedLabels.add(pairs.get(0).label2);
                    currentSelectedLabels++;
                }
            }
                
            pairs.remove(pairs.get(0));
        }while((pairs.size() > 0) && (currentSelectedLabels < numLabels));

        int [] labelIndices = new int[n];
            
        String s;

        if(selectedLabels.size() < n){
            int[] selectedsFreq = new int[dataset.getNumLabels()];
            for(int i=0; i<selectedsFreq.length; i++){
                selectedsFreq[i] = i;
            }
 
            int i = 0;
            do{
                if(!selectedLabels.contains((int)selectedsFreq[i])){
                    selectedLabels.add(selectedsFreq[i]);
                }

                i++;
            }while(selectedLabels.size() < n);
        }
             
        for(int i=0; i<selectedLabels.size(); i++){
            s = jTableHeatmap.getColumnName(selectedLabels.get(i));
            if(s != null){
                labelIndices[i] = selectedLabels.get(i);
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
    private javax.swing.JButton buttonSaveTable;
    private javax.swing.JButton buttonSaveViews;
    private javax.swing.JButton buttonShowCoOcurrence;
    private javax.swing.JButton buttonShowHeatMap;
    private javax.swing.JButton buttonShowMostFrequent;
    private javax.swing.JButton buttonShowMostFrequentHeatMap;
    private javax.swing.JButton buttonShowMostFrequentURelated;
    private javax.swing.JButton buttonShowMostFrequentURelatedHeatMap;
    private javax.swing.JButton buttonShowMostRelated;
    private javax.swing.JButton buttonShowMostRelatedHeatMap;
    private javax.swing.JComboBox comboBoxAttributeInformation;
    private javax.swing.JComboBox comboBoxLabelsInformation;
    private javax.swing.JButton export2;
    private javax.swing.JButton jButtonSaveDatasets;
    private javax.swing.JButton jButtonSaveDatasetsTrans;
    private javax.swing.JButton jButtonStartPreprocess;
    private javax.swing.JButton jButtonStartTrans;
    private javax.swing.JComboBox jComboBoxBRFSComb;
    private javax.swing.JComboBox jComboBoxBRFSNorm;
    private javax.swing.JComboBox jComboBoxBRFSOut;
    private javax.swing.JComboBox jComboBoxSaveFormat;
    private javax.swing.JComboBox jComboBox_SaveFormat1;
    private javax.swing.JLabel jLabelChiFiText;
    private javax.swing.JLabel jLabelIR;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelMulti;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JLabel labelAttributes;
    private javax.swing.JLabel labelAttributesValue;
    private javax.swing.JLabel labelBRFS;
    private javax.swing.JLabel labelBRFSComb;
    private javax.swing.JLabel labelBRFSNorm;
    private javax.swing.JLabel labelBRFSOut;
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
    private javax.swing.JLabel labelMaxNumAttrView;
    private javax.swing.JLabel labelMaxNumAttrViewValue;
    private javax.swing.JLabel labelMeanNumAttrView;
    private javax.swing.JLabel labelMeanNumAttrViewValue;
    private javax.swing.JLabel labelMinNumAttrView;
    private javax.swing.JLabel labelMinNumAttrViewValue;
    private javax.swing.JLabel labelNumViews;
    private javax.swing.JLabel labelNumViewsValue;
    private javax.swing.JLabel labelPercIterativeStratified;
    private javax.swing.JLabel labelPercLPStratified;
    private javax.swing.JLabel labelPercRandom;
    private javax.swing.JLabel labelRandomFS;
    private javax.swing.JLabel labelRandomIS;
    private javax.swing.JLabel labelRelation;
    private javax.swing.JLabel labelRelationValue;
    private javax.swing.JList listMultipleDatasetsLeft;
    private javax.swing.JPanel panelAttributeLeft;
    private javax.swing.JPanel panelAttributes;
    private javax.swing.JPanel panelBoxDiagram;
    private javax.swing.JPanel panelBoxDiagramAtt;
    private javax.swing.JPanel panelChiPhi;
    private javax.swing.JPanel panelCoOcurrence;
    private javax.swing.JPanel panelCoOcurrenceRight;
    private javax.swing.JPanel panelCoOcurrenceValues;
    private javax.swing.JPanel panelCurrentDataset;
    private javax.swing.JPanel panelDependences;
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
    private javax.swing.JPanel panelImbalanceLeft;
    private javax.swing.JPanel panelLabels;
    private javax.swing.JPanel panelLabelsPerExample;
    private javax.swing.JPanel panelMVML;
    private javax.swing.JPanel panelMultipleDatasets;
    private javax.swing.JPanel panelMultipleDatasetsLeft;
    private javax.swing.JPanel panelPreprocess;
    private javax.swing.JPanel panelSplitting;
    private javax.swing.JPanel panelSummary;
    private javax.swing.JPanel panelTransformation;
    private javax.swing.JPanel panelTransformationChoose;
    private javax.swing.JRadioButton radioBRFS;
    private javax.swing.JRadioButton radioBRTrans;
    private javax.swing.JRadioButton radioIncludeLabelsTrans;
    private javax.swing.JRadioButton radioIterativeStratifiedCV;
    private javax.swing.JRadioButton radioIterativeStratifiedHoldout;
    private javax.swing.JRadioButton radioLPStratifiedCV;
    private javax.swing.JRadioButton radioLPStratifiedHoldout;
    private javax.swing.JRadioButton radioLPTrans;
    private javax.swing.JRadioButton radioNoFS;
    private javax.swing.JRadioButton radioNoIS;
    private javax.swing.JRadioButton radioNoSplit;
    private javax.swing.JRadioButton radioRandomCV;
    private javax.swing.JRadioButton radioRandomFS;
    private javax.swing.JRadioButton radioRandomHoldout;
    private javax.swing.JRadioButton radioRandomIS;
    private javax.swing.JRadioButton radioRemoveLabelsTrans;
    private javax.swing.JTable tableAttributesLeft;
    private javax.swing.JTable tableCoOcurrenceLeft;
    private javax.swing.JTable tableHeatmapLeft;
    private javax.swing.JTable tableImbalance;
    private javax.swing.JTabbedPane tabsAttributes;
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
    private javax.swing.JTextField textMostFrequentURelated;
    private javax.swing.JTextField textMostFrequentURelatedHeatMap;
    private javax.swing.JTextField textMostRelated;
    private javax.swing.JTextField textMostRelatedHeatMap;
    private javax.swing.JTextField textRandomCV;
    private javax.swing.JTextField textRandomFS;
    private javax.swing.JTextField textRandomHoldout;
    private javax.swing.JTextField textRandomIS;
    // End of variables declaration//GEN-END:variables
}
