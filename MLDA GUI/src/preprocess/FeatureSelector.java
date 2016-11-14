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

package preprocess;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.ChiSquaredAttributeEval;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import mulan.data.MultiLabelInstances;
import mulan.dimensionalityReduction.BinaryRelevanceAttributeEvaluator;
import mulan.dimensionalityReduction.Ranker;

/**
 * This class includes some feature selectors for multi-label datasets
 * 
 * @author Jose Maria Moyano Murillo
 */
public class FeatureSelector {
    
    private MultiLabelInstances dataset;
    
    private int nFeatures;
    
    /**
     * Constructor specifying the dataset and the number a features to select
     * 
     * @param dataset Multi-label dataset
     * @param nFeatures Number of features to select
     */
    public FeatureSelector(MultiLabelInstances dataset, int nFeatures){
        this.dataset = dataset;
        this.nFeatures = nFeatures;
    }
    
    /**
     * Select features from dataset by BR method
     * 
     * @param combination Combination type
     * @param normalization Normalization type
     * @param output Output type
     * @return Feature-selected dataset
     */
    public MultiLabelInstances select(String combination, String normalization, String output){
        
        MultiLabelInstances modifiedDataset = null;
        
        if( (!combination.equals("max")) && (!combination.equals("min")) && (!combination.equals("avg")) 
                && (!normalization.equals("dl")) && (!normalization.equals("dm")) && (!normalization.equals("none"))
                && (!output.equals("eval")) && (!output.equals("rank")) ){
            return null;
        }
        
        try {
            ASEvaluation ase = new ChiSquaredAttributeEval();
            BinaryRelevanceAttributeEvaluator ae = new BinaryRelevanceAttributeEvaluator(ase, dataset, combination, normalization, output);
            
            Ranker r = new Ranker();
            int[] result = r.search(ae, dataset);
            
            int[] toKeep = new int[nFeatures + dataset.getNumLabels()];
            System.arraycopy(result, 0, toKeep, 0, nFeatures);
            int[] labelIndices = dataset.getLabelIndices();
            System.arraycopy(labelIndices, 0, toKeep, nFeatures, dataset.getNumLabels());
            
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
    
    /**
     * Randomly feature selection
     * 
     * @return Preprocessed dataset
     */
    public MultiLabelInstances randomSelect(){
        
        MultiLabelInstances modifiedDataset = null;

        try {

            int [] attIndices = dataset.getFeatureIndices();
            int r, swap;
            Random rand = new Random();
            
            for(int i=0; i<attIndices.length; i++){
                r = rand.nextInt(attIndices.length);
                swap = attIndices[r];
                attIndices[r] = attIndices[i];
                attIndices[i] = swap;
            }
            
            
            int[] toKeep = new int[nFeatures + dataset.getNumLabels()];
            
            System.arraycopy(attIndices, 0, toKeep, 0, nFeatures);

            int[] labelIndices = dataset.getLabelIndices();
            System.arraycopy(labelIndices, 0, toKeep, nFeatures, dataset.getNumLabels());
            
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
    
    /**
     * Generates new dataset with selected attributes
     * 
     * @param indicesToKeep Indices of the attributes to keep in the new dataset
     * @return New multi-label dataset
     */
    public MultiLabelInstances keepAttributes(int [] indicesToKeep){
        
        MultiLabelInstances modifiedDataset = null;

        try {
            int[] toKeep = new int[nFeatures + dataset.getNumLabels()];
            System.arraycopy(indicesToKeep, 0, toKeep, 0, indicesToKeep.length);
            int[] labelIndices = dataset.getLabelIndices();
            System.arraycopy(labelIndices, 0, toKeep, indicesToKeep.length, dataset.getNumLabels());
            
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

}
