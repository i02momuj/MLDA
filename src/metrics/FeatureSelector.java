package metrics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;



import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.ChiSquaredAttributeEval;
import weka.core.Instances;
//import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import mulan.data.MultiLabelInstances;
import mulan.dimensionalityReduction.BinaryRelevanceAttributeEvaluator;
import mulan.dimensionalityReduction.Ranker;

public class FeatureSelector {

	/**
	 * @param args
	 * @throws Exception 
	 */
    
    private MultiLabelInstances dataset;
    
    private int nFeatures;
    
    public FeatureSelector(MultiLabelInstances dataset, int nFeatures){
        this.dataset = dataset;
        this.nFeatures = nFeatures;
    }
    
    public MultiLabelInstances select(){
        
        MultiLabelInstances modifiedDataset = null;
        
        try {
            ASEvaluation ase = new ChiSquaredAttributeEval();
            BinaryRelevanceAttributeEvaluator ae = new BinaryRelevanceAttributeEvaluator(ase, dataset, "max", "dl", "eval");
            
            Ranker r = new Ranker();
            int[] result = r.search(ae, dataset);
            
            int[] toKeep = new int[nFeatures + dataset.getNumLabels()];
            System.arraycopy(result, 0, toKeep, 0, nFeatures);
            int[] labelIndices = dataset.getLabelIndices();
            for (int i = 0; i < dataset.getNumLabels(); i++) {
                toKeep[nFeatures + i] = labelIndices[i];
            }
            
            Remove filterRemove = new Remove();
            filterRemove.setAttributeIndicesArray(toKeep);
            filterRemove.setInvertSelection(true);
            filterRemove.setInputFormat(dataset.getDataSet());

            modifiedDataset = new MultiLabelInstances( Filter.useFilter(dataset.getDataSet(), filterRemove), dataset.getLabelsMetaData());

        } catch (Exception ex) {
            Logger.getLogger(FeatureSelector.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return modifiedDataset;
    }
    
    
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
				// String path = Utils.getOption("path", args);
			    // String filestem = Utils.getOption("filestem", args);
			    // int nKeep = new Integer(Utils.getOption("nKeep", args)).intValue();
		
                            String path = new String("data\\emotions\\");
			    String filestem = new String("emotions"); 
			    int nKeep = new Integer("10").intValue();
			    
			     MultiLabelInstances mlData = new MultiLabelInstances(path + filestem + ".arff", path + filestem + ".xml");
				 System.out.println("Number of attributes after: " + mlData.getFeatureAttributes().size());
			     System.out.println("Number of instances after: " + mlData.getNumInstances());
			     System.out.println("Number of classes after: " + mlData.getNumLabels());
			     			        
			     //ASEvaluation ase = new GainRatioAttributeEval();
			     ASEvaluation ase = new ChiSquaredAttributeEval();
			     
			     //MultiClassTransformation mt = new Copy();
			     //MultiClassAttributeEvaluator ae = new MultiClassAttributeEvaluator(ase, mt, mlData);
			     //LabelPowersetAttributeEvaluator ae = new LabelPowersetAttributeEvaluator(ase, mlData);
			     
			     BinaryRelevanceAttributeEvaluator ae = new BinaryRelevanceAttributeEvaluator(ase, mlData, "max", "dl", "eval");
			     //metodo de combinacion max, min, avg (mayor, menor, o media de las puntuaciones)
			     //normalizacion: dl (divide by length), dm (divide by maximum), none
			     //salida: eval (puntuacion), rank (ranking) 
			     

			     Ranker r = new Ranker();
			     int[] result = r.search(ae, mlData);
			     System.out.println(Arrays.toString(result));
			  
			     int[] toKeep = new int[nKeep + mlData.getNumLabels()];
			     System.arraycopy(result, 0, toKeep, 0, nKeep);
			     int[] labelIndices = mlData.getLabelIndices();
			     for (int i = 0; i < mlData.getNumLabels(); i++) {
			           toKeep[nKeep + i] = labelIndices[i];
			     }			     
		     
			     Remove filterRemove = new Remove();
			     filterRemove.setAttributeIndicesArray(toKeep);
			     filterRemove.setInvertSelection(true);
			     filterRemove.setInputFormat(mlData.getDataSet());
				
			    Instances filtered;
				filtered = Filter.useFilter(mlData.getDataSet(), filterRemove);
				MultiLabelInstances mlFiltered = new MultiLabelInstances(filtered, mlData.getLabelsMetaData());
			      		
				//Copiamos el xml para que tenga el mismo nombre del nuevo arff
			    System.out.println("Constructing XML file... ");
			    copyFile(new File(path + filestem + ".xml"), new File(path + filestem+nKeep+".xml"));
			        
				//FORMA 1 DE ESCRIBIR EL DATASET
				//ArffSaver saver = new ArffSaver();
				//saver.setInstances(mlFiltered.getDataSet());
		        //saver.setFile(new File(path + filestem+nKeep+".arff"));
		        //saver.writeBatch();
				
				//FORMA 2 DE ESCRIBIR EL DATASET
				BufferedWriter writer = new BufferedWriter(new FileWriter(path + filestem+nKeep+".arff"));
		    	writer.write(mlFiltered.getDataSet().toString());
				writer.close();
				
		        	        
		        //Comprobacion:
				MultiLabelInstances mlDataSet = new MultiLabelInstances(path+filestem+nKeep+".arff", path+filestem+".xml");
				
				System.out.println("Number of attributes before: " + mlDataSet.getFeatureAttributes().size());
		        System.out.println("Number of instances before: " + mlDataSet.getNumInstances());
		        System.out.println("Number of classes before: " + mlDataSet.getNumLabels());
				
	}
	
	
	public static void copyFile(File s, File t)
    {
        try{
              FileChannel in = (new FileInputStream(s)).getChannel();
              FileChannel out = (new FileOutputStream(t)).getChannel();
              in.transferTo(0, s.length(), out);
              in.close();
              out.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

}
