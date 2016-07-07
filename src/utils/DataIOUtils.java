package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import mulan.data.MultiLabelInstances;
import static utils.util.getMax;
import static utils.util.getMin;
import weka.core.Attribute;
import weka.core.Instances;

/**
 * 
 * @author Jose Maria Moyano Murillo
 */
public class DataIOUtils {
    
    public static void writeXMLFile(PrintWriter wr , String[] labels)
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
    
    
    public static boolean isMeka(String relationName)
    {
        String type = "-C";
        
        return relationName.contains(type);
    }
    
    
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
    
        
    public static String getXMLString( String arff_text)
    {
        String result = new String();

        String [] words = arff_text.split("-t");
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
            result = arff_text.substring(0,arff_text.length()-5)+".xml";
        }
        
        return result;
    }
    
    
    public static void saveXMLFile(PrintWriter wr, MultiLabelInstances dataset) throws IOException
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
    
    
    public static void saveDataset(ArrayList<MultiLabelInstances> dataset, 
            String path, String dataName, String type) throws IOException
    {
        BufferedWriter  bw_current;
        PrintWriter wr;        
              
        int index=1;
        String currentPath;

        for(MultiLabelInstances dataset_current : dataset)
        {
            currentPath = path + "/"+dataName+type+index+".arff";

            bw_current = new BufferedWriter(new FileWriter(currentPath));
            wr = new PrintWriter(bw_current);

            saveDataset(wr,dataset_current);

            wr.close();
            bw_current.close();

            index++;
        }
        
    }
    
    
    public static void saveMVDataset(ArrayList<MultiLabelInstances> dataset, 
            String path, String dataName, String type) throws IOException
    {
        BufferedWriter  bw_current;
        PrintWriter wr;        

        int index = 1;
        String currentPath;

        for(MultiLabelInstances dataset_current : dataset)
        {
            currentPath = path + "/"+dataName+type+index+".arff";

            bw_current = new BufferedWriter(new FileWriter(currentPath));
            wr = new PrintWriter(bw_current);

            saveDataset(wr,dataset_current, dataName);

            wr.close();
            bw_current.close();

            index++;
        }
    }
    
    
    public static void saveMekaDataset(ArrayList<MultiLabelInstances> dataset, 
            String path, String dataName, String type, String relationName) throws IOException
    {
        BufferedWriter  bw_current;
        PrintWriter wr;        

        int index = 1;
        String currentPath;

        for(MultiLabelInstances dataset_current : dataset)
        {
            currentPath = path + "/"+dataName+"-"+type+index+".arff";

            bw_current = new BufferedWriter(new FileWriter(currentPath));
            wr = new PrintWriter(bw_current);

            saveMekaDataset(wr,dataset_current);

            wr.close();
            bw_current.close();

            index++;
        }
    }
    
    
    public static void saveMekaDatasetNoViews(ArrayList<MultiLabelInstances> dataset, 
            String path, String dataset_name, String type, String relationName) throws IOException
    {
        BufferedWriter  bw_current;
        PrintWriter wr;        

        int index = 1;
        String currentPath;

        for(MultiLabelInstances dataset_current : dataset)
        {
            currentPath = path + "/"+dataset_name+"-"+type+index+".arff";

            bw_current = new BufferedWriter(new FileWriter(currentPath));
            wr = new PrintWriter(bw_current);

            saveMekaDataset(wr,dataset_current, dataset_name);

            wr.close();
            bw_current.close();

            index++;
        }
    }
    
    
    public static void saveMekaDataset(ArrayList<MultiLabelInstances> dataset, 
            String path, String dataName, String type) throws IOException
    {
        saveMekaDataset(dataset, path, dataName, type, dataName);        
    }
    
    
    public static void saveMekaDatasetNoViews(ArrayList<MultiLabelInstances> dataset, 
            String path, String dataName, String type) throws IOException
    {
        saveMekaDatasetNoViews(dataset, path, dataName, type, dataName);        
    }
    
    
    public static void saveDataset(PrintWriter wr, MultiLabelInstances dataset)
    {
        saveDataset(wr, dataset, dataset.getDataSet().relationName());   
    }
    
    public static void saveMekaDataset(PrintWriter wr, MultiLabelInstances dataset)
    {
        saveMekaDataset(wr, dataset, dataset.getDataSet().relationName());   
    }
    
    
    public static void saveDataset(PrintWriter wr, MultiLabelInstances dataset, String relationName)
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
    
    
    public static void saveDatasetMV(PrintWriter wr, MultiLabelInstances dataset, String relationName, String views)
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
    
    
    public static void saveMekaDataset(PrintWriter wr, MultiLabelInstances dataset, 
            String relationName)
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
}
