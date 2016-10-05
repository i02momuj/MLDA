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

import app.RunApp;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import mulan.data.MultiLabelInstances;
import preprocess.FeatureSelector;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import weka.core.Attribute;
import weka.core.Instances;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;
import static utils.Utils.getMax;
import static utils.Utils.getMin;

/**
 * 
 * @author Jose Maria Moyano Murillo
 */
public class DataIOUtils {
    
    /**
     * Write mulan XML file
     * 
     * @param wr PrintWriter
     * @param labels Label names
     */
    public static void writeXMLFile(PrintWriter wr, String[] labels)
    {
        String line = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    
        wr.write(line);
        wr.write(System.getProperty("line.separator"));
        
        line = "<labels xmlns=\"http://mulan.sourceforge.net/labels\">";
        
        wr.write(line);
        wr.write(System.getProperty("line.separator"));
        
        for(int i=0; i<labels.length;i++)
        {
            line = "<label name=\""+labels[i]+"\"></label>";
            wr.write(line);
            wr.write(System.getProperty("line.separator"));
        }
        
        line = "</labels>";
        wr.write(line);
        wr.write(System.getProperty("line.separator"));
    }
    
    /**
     * Chek if the dataset is in meka format
     * 
     * @param relationName Line of @relation
     * @return True if it is in meka format and false otherwise
     */
    public static boolean isMeka(String relationName)
    {
        String type = "-C";
        
        return relationName.contains(type);
    }
    
    /**
     * Obtain number of label from meka @relation line
     * 
     * @param line @relation line
     * @return Number of labels
     */
    public static int getLabelsFromARFF(String line)
    {
        int labels;

        String [] words = line.split("-C");
        String c = words[1].trim();
        Matcher matcher = Pattern.compile("\\d+").matcher(c);
        matcher.find();
        labels = Integer.valueOf(matcher.group());
        
        if(c.charAt(0) == '-'){
            labels = labels * -1;
        }

        return labels;
    }
    
    /**
     * Obtain label name from @attribute line
     * 
     * @param attributeLine @attribute line
     * @return Label name
     */
    public static String getLabelNameFromLine(String attributeLine)
    {
        String result = null;
        int spaces = 0;
        int initPos = 0, endPos = 0;
        
        if(attributeLine.contains("@attribute")) 
        {
            for(int i=0; i<attributeLine.length(); i++)
            {
                if(attributeLine.charAt(i)==' ' && spaces==0){
                    spaces++;
                    initPos=i;
                }
                else if(attributeLine.charAt(i)==' '){
                    endPos=i; 
                    break;
                }
            }
            
            result = attributeLine.substring(initPos+1, endPos);
        }
        
        return result;
    }
    
    /**
     * Obtain Xml string
     * 
     * @param arffText
     * @return 
     */
    public static String getXMLString(String arffText)
    {
        String result = new String();

        String [] words = arffText.split("-t");
        if(words.length > 1){
            for(int i=0; i<words.length-1; i++){
                if(i == (words.length-2)){
                    result = result + words[i];
                }
                else{
                    result = result + words[i]+"-t";
                }
            }
            result = result.substring(0, result.length()) + ".xml";
        }
        else{
            result = arffText.substring(0,arffText.length()-5)+".xml";
        }
        
        return result;
    }
    
    /**
     * Save mulan xml file
     * 
     * @param wr PrintWriter
     * @param dataset Multi-label dataset
     * @throws IOException 
     */
    public static void saveXMLFile(PrintWriter wr, MultiLabelInstances dataset) 
            throws IOException
    {       
        wr.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        wr.write(System.getProperty("line.separator")); 
        wr.write("<labels xmlns=\"http://mulan.sourceforge.net/labels\">");
        wr.write(System.getProperty("line.separator")); 

        String [] labelNames = dataset.getLabelNames();

        for(int i=0; i<labelNames.length; i++){
            wr.write("<label name=\"" + labelNames[i] + "\"></label>");
            wr.write(System.getProperty("line.separator")); 
        }

        wr.write("</labels>");
        wr.write(System.getProperty("line.separator"));    
    }
    
    /**
     * Save datasets
     * 
     * @param datasets List of datasets
     * @param path Path to store
     * @param dataName Dataset name
     * @param type Type
     * @throws IOException 
     */
    public static void saveDatasets(ArrayList<MultiLabelInstances> datasets, 
            String path, String dataName, String type) throws IOException
    {
        BufferedWriter  bwCurrent;
        PrintWriter wr;        
              
        int index = 1;
        String currentPath;

        for(MultiLabelInstances currentData : datasets)
        {
            currentPath = path + "/" + dataName + type + index + ".arff";

            bwCurrent = new BufferedWriter(new FileWriter(currentPath));
            wr = new PrintWriter(bwCurrent);

            saveDataset(wr,currentData);

            wr.close();
            bwCurrent.close();

            index++;
        }
        
    }
    
    /**
     * Save multi-view multi-label datasets
     * 
     * @param datasets List of datasets
     * @param path Path to store
     * @param dataName Dataset name
     * @param type Type
     * @throws IOException 
     */
    public static void saveMVDatasets(ArrayList<MultiLabelInstances> datasets, 
            String path, String dataName, String type) throws IOException
    {
        BufferedWriter  bwCurrent;
        PrintWriter wr;        

        int index = 1;
        String currentPath;

        for(MultiLabelInstances currentData : datasets)
        {
            currentPath = path + "/"+ dataName + type + index + ".arff";

            bwCurrent = new BufferedWriter(new FileWriter(currentPath));
            wr = new PrintWriter(bwCurrent);

            saveDataset(wr, currentData, dataName);

            wr.close();
            bwCurrent.close();

            index++;
        }
    }
    
    /**
     * Save meka datasets
     * 
     * @param datasets List of datasets
     * @param path Path to store
     * @param dataName Dataset name
     * @param type Type
     * @param relationName Name of the relation
     * @throws IOException 
     */
    public static void saveMekaDatasets(ArrayList<MultiLabelInstances> datasets, 
            String path, String dataName, String type, String relationName) 
            throws IOException
    {
        BufferedWriter  bwCurrent;
        PrintWriter wr;        

        int index = 1;
        String currentPath;

        for(MultiLabelInstances currentData : datasets)
        {
            currentPath = path + "/" + dataName + "-" + type + index + ".arff";

            bwCurrent = new BufferedWriter(new FileWriter(currentPath));
            wr = new PrintWriter(bwCurrent);

            saveMekaDataset(wr, currentData);

            wr.close();
            bwCurrent.close();

            index++;
        }
    }
    
    /**
     * Save meka datasets with no views
     * 
     * @param datasets List of datasets
     * @param path Path to store
     * @param dataName Dataset name
     * @param type Type
     * @param relationName Name of the relation
     * @throws IOException 
     */
    public static void saveMekaDatasetsNoViews(ArrayList<MultiLabelInstances> 
            datasets, String path, String dataName, String type, String 
                    relationName) throws IOException
    {
        BufferedWriter  bwCurrent;
        PrintWriter wr;        

        int index = 1;
        String currentPath;

        for(MultiLabelInstances currentData : datasets)
        {
            currentPath = path + "/" + dataName + "-" + type + index + ".arff";

            bwCurrent = new BufferedWriter(new FileWriter(currentPath));
            wr = new PrintWriter(bwCurrent);

            saveMekaDataset(wr, currentData, dataName);

            wr.close();
            bwCurrent.close();

            index++;
        }
    }
    
    /**
     * Save meka datasets
     * 
     * @param datasets List of datasets
     * @param path path to store
     * @param dataName Dataset name
     * @param type Type
     * @throws IOException 
     */
    public static void saveMekaDataset(ArrayList<MultiLabelInstances> datasets, 
            String path, String dataName, String type) throws IOException
    {
        saveMekaDatasets(datasets, path, dataName, type, dataName);        
    }
    
    /**
     * Save meka datasets with no views
     * 
     * @param datasets List of datasets
     * @param path Path to store
     * @param dataName Dataset name
     * @param type Type
     * @throws IOException 
     */
    public static void saveMekaDatasetsNoViews(ArrayList<MultiLabelInstances> 
            datasets, String path, String dataName, String type) 
            throws IOException
    {
        DataIOUtils.saveMekaDatasetsNoViews(datasets, path, dataName, type, dataName);        
    }
    
    /**
     * Save dataset
     * 
     * @param wr PrintWriter
     * @param dataset Dataset
     */
    public static void saveDataset(PrintWriter wr, MultiLabelInstances dataset)
    {
        saveDataset(wr, dataset, dataset.getDataSet().relationName());   
    }
    
    /**
     * Save meka dataset
     * 
     * @param wr PrintWriter
     * @param dataset Dataset
     */
    public static void saveMekaDataset(PrintWriter wr, MultiLabelInstances 
            dataset)
    {
        saveMekaDataset(wr, dataset, dataset.getDataSet().relationName());   
    }
    
    /**
     * Save dataset
     * 
     * @param wr PrintWriter
     * @param dataset Dataset
     * @param relationName Name of the relation
     */
    public static void saveDataset(PrintWriter wr, MultiLabelInstances dataset, 
            String relationName)
    {
        //relationName = relationName.replaceAll(" ", "_");
        if(relationName.contains("-")){
            wr.write("@relation " + "\'" + relationName + "\'");
        }
        else if(relationName.contains(":")){
            wr.write("@relation " + "\'" + relationName + "\'");
        }
        else{
            wr.write("@relation " + relationName);
        }

        wr.write(System.getProperty("line.separator"));  

        Instances instances = dataset.getDataSet();
       
        Attribute att;
        for (int i=0; i< instances.numAttributes();i++)
        {
            att = instances.attribute(i);
            wr.write(att.toString());
            wr.write(System.getProperty("line.separator")); 
        }   

        String current;
        
        wr.write("@data");
        wr.write(System.getProperty("line.separator"));  
        for(int i=0; i<dataset.getNumInstances();i++)
        {
            current = dataset.getDataSet().get(i).toString();
            wr.write(current);
            wr.write(System.getProperty("line.separator"));  
        }
    }
    
    /**
     * Save multi-label multi-view dataset
     * 
     * @param wr PrintWriter
     * @param dataset Dataset
     * @param relationName Name of the relation
     * @param views String with views intervals
     */
    public static void saveDatasetMV(PrintWriter wr, MultiLabelInstances 
            dataset, String relationName, String views)
    {
        //relationName = relationName.replaceAll(" ", "_");
        
        wr.write("@relation " + "\'" + relationName + " " + views + "\'");
        wr.write(System.getProperty("line.separator"));  
        
        Instances instancias = dataset.getDataSet();
       
        Attribute att;
        for (int i=0; i< instancias.numAttributes();i++)
        {
            att = instancias.attribute(i);
            wr.write(att.toString());
            wr.write(System.getProperty("line.separator")); 
        }   

        String current ;
        
        wr.write("@data");
        wr.write(System.getProperty("line.separator"));  
        for(int i=0; i<dataset.getNumInstances();i++)
        {
            current = dataset.getDataSet().get(i).toString();
            wr.write(current);
            wr.write(System.getProperty("line.separator"));  
        } 
    }
    
    /**
     * Save meka dataset
     * 
     * @param wr PrintWriter
     * @param dataset Dataset
     * @param relationName Name of the relation
     */
    public static void saveMekaDataset(PrintWriter wr, MultiLabelInstances 
            dataset, String relationName)
    {
        int maxAttIndex;
        int minAttIndex;
        
        String c;
        c = "-C ";
        
        int [] attIndex = dataset.getFeatureIndices();
        
        maxAttIndex = getMax(attIndex);
        minAttIndex = getMin(attIndex);
        
        int [] labelIndices = dataset.getLabelIndices();
        
        boolean areLabelMaxIndices = true;
        boolean areLabelMinIndices = false;
        
        for(int i=0; i<labelIndices.length && areLabelMaxIndices; i++){
            if(labelIndices[i] < maxAttIndex){
                areLabelMaxIndices = false;
            }
        }
        
        if(!areLabelMaxIndices){
            areLabelMinIndices = true;
            for(int i=0; i<labelIndices.length && areLabelMinIndices; i++){
                if(labelIndices[i] > minAttIndex){
                    areLabelMinIndices = false;
                }
            }
        }
        
        if((!areLabelMaxIndices) && (!areLabelMinIndices)){
            JOptionPane.showMessageDialog(null, "Cannot save as meka.", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if(areLabelMaxIndices){
            c = c + "-" + labelIndices.length;
        }
        else{
            c = c + labelIndices.length;
        }
        
        if(relationName.contains("-V:")){
            wr.write("@relation " + "\'" + relationName.split("-V:")[0] + ": " + c + " -V:" + relationName.split("-V:")[1] +  "\'");
        }
        else{
            wr.write("@relation " + "\'" + relationName + ": " + c + "\'");
        }
        
        wr.write(System.getProperty("line.separator"));  

        Instances instances = dataset.getDataSet();
       
        Attribute att;
        for (int i=0; i< instances.numAttributes();i++)
        {
            att = instances.attribute(i);
            wr.write(att.toString());
            wr.write(System.getProperty("line.separator")); 
        }   

        String current;
        
        wr.write("@data");
        wr.write(System.getProperty("line.separator"));  
        for(int i=0; i<dataset.getNumInstances();i++)
        {
            current = dataset.getDataSet().get(i).toString();
            wr.write(current);
            wr.write(System.getProperty("line.separator"));  
        }
    }
    
    /**
     * Save multi-view multi-label meka dataset
     * 
     * @param wr PrintWriter
     * @param dataset Dataset
     * @param relationName Name of the relation
     * @param views String with views intervals
     */
    public static void saveMVMekaDataset(PrintWriter wr, MultiLabelInstances dataset, 
            String relationName, String views)
    {
        int maxAttIndex;
        int minAttIndex;
        
        String c;
        c = "-C ";
        
        int [] attIndex = dataset.getFeatureIndices();
        
        maxAttIndex = getMax(attIndex);
        minAttIndex = getMin(attIndex);
        
        int [] labelIndices = dataset.getLabelIndices();
        
        boolean areLabelMaxIndices = true;
        boolean areLabelMinIndices = false;
        
        for(int i=0; i<labelIndices.length && areLabelMaxIndices; i++){
            if(labelIndices[i] < maxAttIndex){
                areLabelMaxIndices = false;
            }
        }
        
        if(!areLabelMaxIndices){
            areLabelMinIndices = true;
            for(int i=0; i<labelIndices.length && areLabelMinIndices; i++){
                if(labelIndices[i] > minAttIndex){
                    areLabelMinIndices = false;
                }
            }
        }
        
        if((!areLabelMaxIndices) && (!areLabelMinIndices)){
            JOptionPane.showMessageDialog(null, "Cannot save as meka.", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if(areLabelMaxIndices){
            c = c + "-" + labelIndices.length;
        }
        else{
            c = c + labelIndices.length;
        }
        
        
        wr.write("@relation " + "\'" + relationName + ": " + c + " " + views + "\'");
        wr.write(System.getProperty("line.separator"));  

        Instances instances = dataset.getDataSet();
       
        Attribute att;
        for (int i=0; i< instances.numAttributes();i++)
        {
            att = instances.attribute(i);
            wr.write(att.toString());
            wr.write(System.getProperty("line.separator")); 
        }   

        String current;
        
        wr.write("@data");
        wr.write(System.getProperty("line.separator"));  
        for(int i=0; i<dataset.getNumInstances();i++)
        {
            current = dataset.getDataSet().get(i).toString();
            wr.write(current);
            wr.write(System.getProperty("line.separator"));  
        }
    }
    
    /**
     * Get filename from a path
     * 
     * @param path Path
     * @return Filename
     */
    public static String getFileName(String path)
    {           
        for(int i=path.length()-1;i>0;i--)
        {
            if (path.charAt(i)=='\\')break;
        }
        return path;
    }
    
    /**
     * Get filepath
     * @param path Path
     * @return Filepath
     */
    public static String getFilePath(String path)
    {           
        String [] words;
        words = path.split("/");

        path = words[words.length-1];
        String dir = new String();
        if(words.length > 1){
            for(int i=0; i<words.length-1; i++){
                dir += words[i] + "/";
            }
        }
      
        path = dir+path;
        
        return path;
    }
    
}
