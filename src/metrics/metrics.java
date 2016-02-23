/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metrics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import mulan.data.LabelSet;
import mulan.data.LabelsPair;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;
import mulan.data.UnconditionalChiSquareIdentifier;
import mulan.dimensionalityReduction.BinaryRelevanceAttributeEvaluator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.core.Attribute;
import weka.core.AttributeStats;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

/**
 *
 * @author oscglezm
 */
public class metrics 
{
        public static double Diversity(MultiLabelInstances dataset, Statistics stat)
        {
            double bound = Bound(dataset);
            double labelsets = DistincLabelset(stat);
            
            return labelsets/bound;
            
        }
    
        public static double Label_Density(MultiLabelInstances dataset, Statistics stat1)
        {
            double label_cardinality = LabelCardinality(dataset, stat1);
            return label_cardinality/dataset.getNumLabels();
        }
        
        public static double ratio_unseen_clases_over_number_DC_test(MultiLabelInstances dataset_train,MultiLabelInstances dataset_test, Statistics stat_test)
        {
            int unseenInTrain = metrics.UnseenInTrain(dataset_train,dataset_test);
            double DL_test = DistincLabelset(stat_test);
            double ratio_unseen_clases_over_number_DC_test = unseenInTrain*1.0/DL_test;
            
            return ratio_unseen_clases_over_number_DC_test;
        }
    
    
        public static double ratio_distinc_clases_found_in_train_and_test(Statistics stat_train,Statistics stat_test, MultiLabelInstances dataset)
        {
            double value = number_distinc_clases_found_in_train_and_test(stat_train, stat_test);
            return value/Maxim_label_combination(dataset)*1.0;
        }
        
        public static double ratio_DL_over_bount( MultiLabelInstances dataset,Statistics stat1)
        {
            double bound = Bound(dataset);
            double DL =  DistincLabelset(stat1);
            
            return DL/bound;
        }
    
    
        public static int Maxim_label_combination(MultiLabelInstances dataset)
        {
            return (int)Math.pow(2, dataset.getNumLabels());
        }
    
        public static double ratio_test_instances_to_train_instances(MultiLabelInstances dataset_train,MultiLabelInstances dataset_test)
        {
            return dataset_test.getNumInstances()*1.0/dataset_train.getNumInstances();
        }
        
        public static double number_distinc_clases_found_in_train_and_test(Statistics stat_train,Statistics stat_test)
        {
            
            int count=0;
            
            HashMap<LabelSet,Integer> labelcombination_train = stat_train.labelCombCount();
            Set<LabelSet> keysets_train = labelcombination_train.keySet();	
            
            HashMap<LabelSet,Integer> labelcombination_test = stat_test.labelCombCount();
            Set<LabelSet> keysets_test = labelcombination_test.keySet();
		
            String labelset_train, labelset_test;
            for(LabelSet current : keysets_train)
                {
                    labelset_train=current.toString();
                    
                    for( LabelSet actual : keysets_test)
                    {
                        labelset_test = actual.toString();
                        
                        if(labelset_train.equals(labelset_test))
                        {
                            count++;
                            break;
                        }
                        
                    }
                }
            
            
            return count;
        }
        
        public static double Distinct_classes(Statistics stat1)
        {
            double result = stat1.labelCombCount().size();
            System.out.println("distinct classes "+result);
            return result;
        }
    
        public static double Ratio_DC_to_total_label_combination(Statistics stat1, MultiLabelInstances dataset)
        {
            double DL = DistincLabelset(stat1);
             int max_combination = (int)Math.pow(2, dataset.getNumLabels());
             double ratio_combination = DL*1.0/max_combination;
                
            
            return ratio_combination;
        }
    
    
        public static double Proportion_nominal_attr(MultiLabelInstances dataset)
        {
            Instances i1 = dataset.getDataSet();
            double atributos = (i1.numAttributes()-dataset.getNumLabels());
            double proportions_attr_nominal = metrics.count_attributes_nominal(dataset)*1.0/atributos;
            
            return proportions_attr_nominal;
        }
    
    
         public static double Proportion_binary_attr(MultiLabelInstances dataset)
        {
            Instances i1 = dataset.getDataSet();
            int count_attr_binary = metrics.count_attributes_binary(dataset);
            double atributos = (i1.numAttributes()-dataset.getNumLabels());
            double proportions_attr_binary = count_attr_binary*1.0/atributos;
          
            return  proportions_attr_binary;
        }
    
         public static double Ratio_instances_to_attr(MultiLabelInstances dataset)
        {
            int num_instancias = dataset.getNumInstances();
            Instances i1 = dataset.getDataSet();
            double atributos = (i1.numAttributes()-dataset.getNumLabels());
        
             double ratio_train_attr =( num_instancias*1.0)/atributos;
            return  ratio_train_attr;
        }
    
     public static double PMax(Statistics stat1,MultiLabelInstances dataset)
        {
            int num_instancias = dataset.getNumInstances();
            
            double pmax = (util.Label_value_most_frequency(stat1.labelCombCount()))/(num_instancias*1.0);
            
            return  pmax;
        }
    
     
     public static double imbalanced_ratio_LP(MultiLabelInstances dataset)
     {
          Statistics stat1 = new Statistics();
          stat1.calculateStats(dataset);
     
          HashMap<LabelSet,Integer> result = stat1.labelCombCount();
          
          double mayor = Mayor_labelset(result);
          double menor= menor_labelset(result);
          
          return mayor/menor;
     }
     
     
     public static double get_media(double[] elementos)
     {
         double value=0.0;
         
         for(int i=0; i<elementos.length ;i++)
         {
             value+=elementos[i];
         }
         return value/elementos.length;
     }
     
         
    public static int get_Mayor(Set<LabelSet> keysets ,HashMap<LabelSet,Integer> result)
    {
        int mayor=0;
        
        for(LabelSet current : keysets)
            if(mayor<result.get(current))mayor=result.get(current);
        
        return mayor;
    }
   
    
     public static double get_mean_ir_per_labelset(MultiLabelInstances dataset)
     {
         Statistics stat1 = new Statistics();
         stat1.calculateStats(dataset);
                  
        //CALCULA LA FRECUENCIA DE LOS LABEL-COMBINATION
        HashMap<LabelSet,Integer> result = stat1.labelCombCount();
        
        Set<LabelSet> keysets = result.keySet();
        
        double[] ir_values = new double[keysets.size()];
        double IR_labelset,mean;
         
         int mayor = get_Mayor(keysets, result);
         int value,i=0;
         
         for(LabelSet current : keysets)
         {
             value=  result.get(current); //es la cantidad de veces que aparece el labelset en el dataset
             IR_labelset = mayor /(value*1.0);
             ir_values[i]=IR_labelset;
             i++;
         }
         
         mean = get_media(ir_values);
         return mean;
     }
     
     public static double Variance_imbalanced_ratio_LP(MultiLabelInstances dataset)
     {
          Statistics stat1 = new Statistics();
          stat1.calculateStats(dataset);
     
          HashMap<LabelSet,Integer> result = stat1.labelCombCount();
          
          double value=0;
          double media = dataset.getNumInstances()/result.values().size();
                  
          
            for(int current : result.values())            
            {
                value+=Math.pow((current-media), 2);
            }
          return value/result.values().size();
     }
     
      public static double Mayor_labelset(HashMap<LabelSet,Integer> result)
        {
            double mayor=0;
            
            for(int current : result.values())            
                if(current >mayor) mayor=current;
            
            return mayor;
        }
      
     public static double menor_labelset(HashMap<LabelSet,Integer> result)
        {
            double menor=10000;
            
            for(int current : result.values())            
                if(current <menor) menor=current;
            
            return menor;
        }
      
     
      public static double PUniq(Statistics stat1, MultiLabelInstances dataset)
        {
            int num_instancias = dataset.getNumInstances();
            
            HashMap<LabelSet,Integer> result = stat1.labelCombCount();
                    
            double labelsets_uniq=0;
                        
            for(int current : result.values())            
                if(current == 1) labelsets_uniq++;
                   
            //mal
            double puniq = labelsets_uniq/num_instancias;
            
            return  puniq;
        }
    
    
     public static double labelsxinstancesxfeatures(MultiLabelInstances dataset) 
     {
         double result = (dataset.getNumInstances()*1.0)*(dataset.getNumLabels() * dataset.getFeatureIndices().length);
         return result;
     }
      
     public static double Bound(MultiLabelInstances dataset)
        {
            int numero_etiquetas = dataset.getNumLabels();
            double bound = Math.pow(2, numero_etiquetas);
            
            return  bound;
        }
     
    
        public static double Cardinality(Statistics stat1)
        {
            return  stat1.cardinality();
        }
    
        public static double Density(Statistics stat1)
        {
            return  stat1.density();
        }
        
       public static double ProportionDistincLabelset(Statistics stat1, MultiLabelInstances dataset)
        {
            double num_instances=dataset.getNumInstances();
            double DL = stat1.labelCombCount().size();
            return DL/num_instances;
        }
    
        public static double DistincLabelset(Statistics stat1)
        {
            return stat1.labelCombCount().size();
        }
    
         
       public static double MeanOfLabelEntropies(MultiLabelInstances my_dataaset)
       {
           double value;
           
           Instances instances = my_dataaset.getDataSet();
           double res = 0.0;
           int countNominal = 0;
           int[] labels = my_dataaset.getLabelIndices();
           
           for (int i = 0; i < labels.length; i++) 
           {
                AttributeStats stats = instances.attributeStats(labels[i]);
           
                if (stats.nominalCounts != null) 
                {
                    countNominal++;
                    res += entropy(stats.nominalCounts);
                   
                }
            }
            value = res / countNominal;
           
           return value;
       }
    
       public static double[] Min_MaxOfLabelEntropies(MultiLabelInstances my_dataset)
       {
           double[] value = new double[2];
           
           Instances instances = my_dataset.getDataSet();
           int[] labels = my_dataset.getLabelIndices();            
           double[] vals = new double[labels.length];
            
            for (int i = 0; i < labels.length; i++) {
                AttributeStats stats = instances.attributeStats(labels[i]);
                
                if (stats.nominalCounts != null) {
                    //stats.nominalCounts devuelve un arreglo de 2 posiciones,
                    // donde en la primera se guarda la cantidad de veces que no aparece, en la 2da todo lo contrario.(las que aparecen en las instancias)
                    //util.Recorre_Arreglo(stats.nominalCounts);
                    vals[i] = entropy(stats.nominalCounts); //guarda la entropia de estos valores.
                }
            }
            //util.Recorre_Arreglo(vals);            
            Arrays.sort(vals);
                       
            value[0] = vals[0];
            value[1] = vals[labels.length-1];
                       
           return value;
       }
    
       public static double AvgExamplesPerClass(MultiLabelInstances my_dataset) throws Exception
       {
           double value;
           
           String[]trainClasses =getDistinctClasses(my_dataset); // devuelve la salida de labelcomb en 0 y 1.
           //util.Recorre_Arreglo(trainClasses);
           
           int numExamples = my_dataset.getNumInstances();
           value = (double)numExamples/trainClasses.length;
       
           return value;
       }
       
    
       public static double RatioClassesWithExamplesLessHalfAttributes(MultiLabelInstances my_dataset,Integer[] combCounts)
       {
           double value;
           
            int numAtts = my_dataset.getFeatureAttributes().size();
            
            //System.out.println(numAtts);
            
            int num = numAtts / 2;
            int res = countUpTo(num,combCounts);
            value = (res*1.0) / combCounts.length;
           
           return value;
       }
    
       public static double RatioClassesWithUpTo_n_Examples(Integer[] combCounts,int n)
        {             
            int value = countUpTo(n,combCounts);
            double ratioClassesWithUpTo_n_Examples = (value*1.0) / combCounts.length;
            
            return ratioClassesWithUpTo_n_Examples;            
        }
        
        public static int countUpTo(int num,Integer[] combCounts )
        {
           int res=0;
            for (int i=0; i<combCounts.length; i++){
                if(combCounts[i]<=num){
                    res++;
                }
                else{
                    break;
                    }
                }
            
            return res;
        }
    
    
        public static Integer[] get_combCounts(Statistics my_stat)
        {
            Collection<Integer> counts = my_stat.labelCombCount().values();
            Integer[] combCounts = new Integer[counts.size()];
            counts.toArray(combCounts);
            Arrays.sort(combCounts);
            
            /*util.Recorre_Arreglo(combCounts);
            System.out.println(combCounts.length);
            */
            return combCounts;
        }
        

        
        public static double[] UncondDepPairsNum_UncondDepPairsRatio_AvgOfDepChiScores(MultiLabelInstances my_dataset)
        {
            double[] value = new double[3];
            
            try {
                
                UnconditionalChiSquareIdentifier depid = new UnconditionalChiSquareIdentifier();
                LabelsPair[] pairs = depid.calculateDependence(my_dataset);
                
                //TESTING
                /*System.out.println("Chi square test para cada par de etiqueta");
                for(LabelsPair current : pairs)
                {
                    System.out.println(current.toString());
                }
                System.out.println("cantidad de pares "+ pairs.length);
                */
                
                int total = pairs.length;
                int dep=0;
                double sum= 0;
                double score=0;
                
                for (int i=0;i<total;i++){
                    score = pairs[i].getScore();
                        if(score > 6.635){
                        dep++;
                        sum+=score;
                    }
                    else{
                        break;
                    }
                }
               double  ratioOfDepPairs = (double)dep / total;
               double avgOfChiScores = sum / dep;
               double numDepPairs = dep;
             
               value[0]= numDepPairs;
               value[1]= ratioOfDepPairs;
               value[2]= avgOfChiScores;
               
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            
            return value;
        }
    
        public static double KurtosisOfTrainCardinality(Statistics my_stat,int[] labelsForInstance)
        {
            double value;
            
            try {
                double avg = my_stat.cardinality();
                double sum2 = 0;
                double sum4 = 0;
               
                for (int i=0; i<labelsForInstance.length;i++) {
                    double v = labelsForInstance[i] - avg;
                    sum2 += Math.pow(v, 2);
                    sum4 += Math.pow(v, 4);
                }
                int num = labelsForInstance.length;
                double kurtosis = (num*sum4/Math.pow(sum2,2))-3;
                double  sampleKurtosis = (kurtosis*(num+1) + 6) * (num-1)/((num-2)*(num-3));
                value = sampleKurtosis;
            } 
            catch (Exception e) {
                value = 0;
                e.printStackTrace();
            }
                 
            return value;
        }
    
        public static double SkewnessOfTrainCardinality( Statistics my_stat,int[] labelsForInstance)
        {
            double value;
            
            try {
                double avg = my_stat.cardinality();
                double sum = 0;
              
                for (int i=0; i<labelsForInstance.length;i++) {
                    double v = labelsForInstance[i] - avg;
                    sum += Math.pow(v, 3);
                }
                int num = labelsForInstance.length;
                double var = sum /(num - 1);
                double cardStdev  = Math.sqrt(var);
                
                double skewness = num * sum / ((num - 1)*(num-2) * Math.pow(cardStdev, 3));
                value = skewness;
            }
            catch (Exception e) {
                value = 0;
                e.printStackTrace();
            }
                       
            return value;
        }
    
        public static int[] LabelsForInstance( MultiLabelInstances my_dataset) // return the labels count that exit of each instance
        {
            Instances numInstances = my_dataset.getDataSet();
            
            int[] labelsForInstance = new int [numInstances.size()];
            int numLabels = my_dataset.getNumLabels();
            int[]labelIndices = my_dataset.getLabelIndices();
            
            int count_label=0;
                    
            for (int i = 0; i < numInstances.size(); i++)
            {                
            for (int j = 0; j < numLabels; j++) 
            {
                if (numInstances.instance(i).stringValue(labelIndices[j]).equals("1"))
                {
                    count_label++;
                } 
            }
            labelsForInstance[i]=count_label;
            count_label=0;
           }
            
           return labelsForInstance;
        }
    
        public static double StDevOfTrainCardinality (Statistics my_stat, int[] labelsForInstance)
        {
            double value;
            
            try {
                double avg = my_stat.cardinality();
                double sum = 0;
                
                for (int i=0; i<labelsForInstance.length;i++)
                {
                    double v = labelsForInstance[i] - avg;
                    sum += Math.pow(v, 2);
                }
                double var = sum / (labelsForInstance.length - 1);
                double cardStdev  = Math.sqrt(var);                
                value = cardStdev;
            } 
            catch (Exception e) {
                value = 0;
                e.printStackTrace();
            }
            
            return value;
        }
        
       public static double AbsDifferenceOfCardinality_train_test(MultiLabelInstances dataset_train,MultiLabelInstances dataset_test , Statistics stat_train, Statistics stat_test)
       {
            //Label Cardinality [13] of the training
                double labelCardinalityTrain = metrics.LabelCardinality(dataset_train,stat_train);
                
            //Label Cardinality [13] of the test set
                double labelCardinalityTest = metrics.LabelCardinality(dataset_test,stat_test);
                
                return AbsDifferenceOfCardinality(labelCardinalityTrain, labelCardinalityTest);
                
       }
    
        private static double AbsDifferenceOfCardinality(double cardinality_train,double cardinality_test)
        {
            double value;
            
            try {
                double diff = cardinality_test - cardinality_train;
                value = Math.abs(diff);
            } catch (Exception e) {
                value = 0;
                e.printStackTrace();
            }
            
            return value;
        }
    
        public static double LabelCardinality(MultiLabelInstances dataset_i , Statistics my_stats )
        {
            double value;
            
             try 
             {
                my_stats.calculateStats(dataset_i);
                value = my_stats.cardinality();
             }
             catch (Exception e) {
                value = 0;
                e.printStackTrace();
            }
            
            return value;
        }
    
        public static String[] getDistinctClasses(MultiLabelInstances dataset_i) throws Exception
        {
       
        mulan.data.Statistics st =  new mulan.data.Statistics();
        st.calculateStats(dataset_i);
        
        Set<LabelSet> sets = st.labelSets();
        String[] classes = new String[sets.size()];
        
        Iterator<LabelSet> labelSetIterator = sets.iterator();
        for (int i=0;i<sets.size();i++) {
            LabelSet set = labelSetIterator.next();
            classes[i] = set.toBitString();
        }
        return classes;
    }
     
     public static int UnseenInTrain(MultiLabelInstances train, MultiLabelInstances test)
     {
         int unseen =0;
         int value =0;
         
         try {
                String[]trainClasses =getDistinctClasses(train);
                String[]testClasses =getDistinctClasses(test);
                
                int count = 0;
                Collection tr = Arrays.asList(trainClasses);
                Collection ts = Arrays.asList(testClasses);
                for (Iterator iterator = ts.iterator(); iterator.hasNext();) 
                {
                    String val =  (String)iterator.next();
                    if(!tr.contains(val)) count++;
                }
                unseen = count;
                value = unseen;
            } 
         catch (Exception e) 
            {
                value = 0;
                e.printStackTrace();
            }
         return value;
     }
    
     
     public static double Mean_Variance_imbalance(atributo[] imbalanced_data)
     {
         double value=0;
         
         for(int i=0; i<imbalanced_data.length ; i++)
              value+=imbalanced_data[i].get_variance();
                  
         value=value/imbalanced_data.length;
         
         return value;
            
     }
     
          public static double Mean_Standard_Desviation_imbalance_intra_class(atributo[] imbalanced_data)
     {
         double value=0;
         double variance;
         for(int i=0; i<imbalanced_data.length ; i++)
         {
             variance= imbalanced_data[i].get_variance();
              value+= Math.sqrt(variance);
         }        
         value=value/imbalanced_data.length;
         
         return value;
            
     }
          

     
     public static double Mean_IR_BR_intra_class(atributo[] imbalanced_data)
     {
         double value=0;
         
         for(int i=0; i<imbalanced_data.length ; i++)
              value+=imbalanced_data[i].get_ir();
                  
         value=value/imbalanced_data.length;
         

         return value;
     }
     
          public static double Mean_IR_BR_inter_class(atributo[] imbalanced_data)
          {
         double value=0;
         
         for(int i=0; i<imbalanced_data.length ; i++)
              value+=imbalanced_data[i].get_ir_inter_class();
                  
         value=value/imbalanced_data.length;
         
         return value;
          }
     
          public static double CVIR_inter_class (atributo[] imbalanced_data)
            {
           
              double value=0,temp;
              double media = Mean_IR_BR_inter_class(imbalanced_data);
         
                System.out.println("imbalanced_data.length: " + imbalanced_data.length);
              for(int i=0; i<imbalanced_data.length ; i++)
              {
                  temp = imbalanced_data[i].get_ir_inter_class() - media;
                  temp = Math.pow(temp, 2);
                  
                 value+= temp;
              }
                               
              value = value/(imbalanced_data.length-1);
              value = Math.sqrt(value);
              value = value/media;
              
              return value;
          }
          
          
     
     public static double AverageGainRatio(MultiLabelInstances train, boolean es_de_tipo_meka)
     {
         double value;
         
         try {
                
                double res = 0.0, v= 0.0;
                ASEvaluation ase = new InfoGainAttributeEval();
                
                BinaryRelevanceAttributeEvaluator eval = new BinaryRelevanceAttributeEvaluator(ase, train, "avg", "none", "eval");
                
                int[] ints = train.getFeatureIndices();
                
                util.Recorre_Arreglo(ints);
                
               int count;
               
               
               int tam = ints.length;
               
               if(es_de_tipo_meka)
               {
                   tam= tam - train.getNumLabels();
                   
                   for(int i=0; i< train.getNumLabels();i++)
                   {
                       v = eval.evaluateAttribute(i);
                       res += v;
                   }
               }
               
                for (int i=0;i<tam;i++) 
                {
                    count=i+1;
                    System.out.println(" atributo "+ count+" ,indice dentro del elemento"+ ints[i] + " , indice "+i);
                    
                    v = eval.evaluateAttribute(ints[i]);
                    System.out.println("valor "+ v);
                    res += v;
                }
                
                value = res / ints.length;
            }
         
            catch (Exception e) {
                value = Double.NaN;
                e.printStackTrace();
            }
         
         return value;
     }
    
     public static double ProportionWithOutliers(MultiLabelInstances train)
     {
         double value;
         
          Instances instances = train.getDataSet();
            int num = instances.size();
            double alpha = 0.05;
            int numToTrimAtSide = (int) (num * alpha / 2);
            int countNumeric = 0;
            int countWithOutliers = 0;
            int[] ints = train.getFeatureIndices();
            
            for (int i : ints) {
                Attribute att = instances.attribute(i);
                if (att.isNumeric()) {
                    countNumeric++;
                    double variance = instances.variance(att);
                    double[] values = instances.attributeToDoubleArray(i);
            
                    Arrays.sort(values);
                    double[] trimmed = new double[num - (numToTrimAtSide * 2)];
                    
                    for (int j = 0; j < trimmed.length; j++) {
                        trimmed[j] = values[j + numToTrimAtSide];
                    }
                    
                    double varianceTrimmed = Utils.variance(trimmed);
                    double ratio = varianceTrimmed / variance;
                    if (ratio < 0.7) {
                        countWithOutliers++;
                    }
                }
            }
            if (countNumeric > 0) {
                value = (double) countWithOutliers / countNumeric;
            } else {
                //no numeric attributes in dataset
                value = Double.NaN;
            }
            
            return value;
     }
    
     public static double  AverageAbsoluteCorrelation(MultiLabelInstances train )
     {
         double value;
         
         Instances instances = train.getDataSet();
            int num = instances.size();
            double res = 0.0;
            int count = 0;
            
            int[] featureIndices = train.getFeatureIndices();
            for (int ind1 : featureIndices) {
                if (instances.attribute(ind1).isNumeric()) {
                    
                    for (int ind2 = ind1 + 1; ind2 < featureIndices.length; ind2++) {
                        if (instances.attribute(ind2).isNumeric()) {
                            count++;
                            double[] attVals1 = instances.attributeToDoubleArray(ind1);
                            double[] attVals2 = instances.attributeToDoubleArray(ind2);
                            
                            res += Utils.correlation(attVals1, attVals2, num);
                        }
                    }
                }
            }
            if (count > 0) {
                value = res / count;
            } else {
                //no numeric attributes in dataset
                value = Double.NaN;
            }
            
            return value;
     }
    
     public static double lnFunc(double num) {

        // Constant hard coded for efficiency reasons
        if (num < 1e-6) {
            return 0;
        } else {
            return num * Math.log(num);
        }
    }
    
      public static double entropy(int[] array)
      {
         double returnValue = 0, sum = 0;
          
         for (int i = 0; i < array.length; i++)
          {
            returnValue -= lnFunc(array[i]);
            sum += array[i];//sumatoria
          }
            
         if (Utils.eq(sum, 0))
         {
            return 0;
         }
         else
         {
            return (returnValue + lnFunc(sum)) / (sum * Math.log(array.length));
         }
         
      }
      
      public static double entropy(double[] array)
      {
         double returnValue = 0, sum = 0;
          
         for (int i = 0; i < array.length; i++)
          {
            returnValue -= lnFunc(array[i]);
            sum += array[i];//sumatoria
          }
            
         if (Utils.eq(sum, 0))
         {
            return 0;
         }
         else
         {
            return (returnValue + lnFunc(sum)) / (sum * Math.log(array.length));
         }
         
      }
      
    public static int count_numeric_attr(MultiLabelInstances dataset)  
    {
      Instances instances = dataset.getDataSet();
      int[] featureIndices = dataset.getFeatureIndices(); // devuelve los indices de los atributos
      int count =0;
      
      for( int index : featureIndices )
          if(instances.attribute(index).isNumeric()) count++;
      
      return count;    
    }
      
    public static double MeanOfEntropies_numeric_attr(MultiLabelInstances dataset)  
    {
        double value=0;
        int cant_attr_numeric=0;
        
        Instances instances = dataset.getDataSet();
               
        int[] featureIndices = dataset.getFeatureIndices(); // devuelve los indices de los atributos
        double[] values_attr;
        
        for(int i : featureIndices)
        {
            if(instances.attribute(i).isNumeric())
            {
                values_attr = instances.attributeToDoubleArray(i);
                value+=entropy(values_attr);
                cant_attr_numeric++;
            }
        }
        return value/cant_attr_numeric;
      
           
    }
      
    
      //calcula la media de la entropia de los atributos nominales
    public static double MeanOfEntropies_nominal_attr(MultiLabelInstances train )
    {
        double value;
        
        Instances instances = train.getDataSet();
      
            double res = 0.0;
            int countNominal = 0;
            int[] featureIndices = train.getFeatureIndices(); // devuelve los indices de los atributos
            
                        
            for (int ind : featureIndices) 
            {
                AttributeStats stats = instances.attributeStats(ind); //calcula ciertos valores estadisticos para el atributo actual
                
                //System.out.println("la media del atributo "+ instances.attribute(ind).name() +" es "+ stats.numericStats.mean);
                //System.out.println("primer valor "+instances.instance(0).value(0));
                
                                
                if (stats.nominalCounts != null) {
                    
                    util.Recorre_Arreglo(stats.nominalCounts);
                    System.out.println("cantidad "+ stats.nominalCounts.length);
                    
                    countNominal++;
                    
                    res += entropy(stats.nominalCounts);
                }
            }
            value = res / countNominal;
        
        return value;
    }
    
    public static double MeanKurtosis(MultiLabelInstances train)
    {
        double value;
        
        Instances instances = train.getDataSet();
            int num = instances.size();
            double res = 0.0;
            double avg;
            double var2;
            double var4;
            double val;
            int countNumeric = 0;
            int[] ints = train.getFeatureIndices();
            for (int i : ints) {
                Attribute att = instances.attribute(i);
                if (att.isNumeric()) {
                    countNumeric++;
                    avg = instances.meanOrMode(att);
                    var2 = 0;
                    var4 = 0;
                    for (Instance instance : instances) {
                        val = instance.value(att);
                        var4 += Math.pow(val - avg, 4);
                        var2 += Math.pow(val - avg, 2);
                    }
                    double kurtosis = (num*var4/Math.pow(var2,2))-3;
                    double  sampleKurtosis = (kurtosis*(num+1) + 6) * (num-1)/((num-2)*(num-3));
                    res += sampleKurtosis;
                }
            }
            if (countNumeric > 0) {
                value = res / countNumeric;
            } else {
                //no numeric attributes in dataset
                value = Double.NaN;
            }
            return value;
    }
    
    public static double MeanSkewness(MultiLabelInstances train)
    {
        double value;
        
        Instances instances = train.getDataSet();
            int num = instances.size();
            double res = 0.0;
            double stdev;
            double avg;
            double var;
            double val;
            int countNumeric = 0;
            int[] ints = train.getFeatureIndices();
            for (int i : ints) {
                Attribute att = instances.attribute(i);
                if (att.isNumeric()) {
                    countNumeric++;
                    avg = instances.meanOrMode(att);
                    var = 0;
                    for (Instance instance : instances) {
                        val = instance.value(att);
                        var += Math.pow(val - avg, 3);
                    }
                    double variance = instances.variance(att);
                    stdev = Math.sqrt(variance);
                    double skewness = num*var / ((num - 1) *(num-2)* Math.pow(stdev, 3));
                    res += skewness;
                }
            }
            if (countNumeric > 0) {
                value = res / countNumeric;
            } else {
                //no numeric attributes in dataset
                value = Double.NaN;
            }
            return value;
    }

    
    public static double Standard_Desviation_x_labelset (MultiLabelInstances dataset ,Statistics stat1)
    {
        double value;
        
        HashMap<LabelSet,Integer> result = stat1.labelCombCount();
        
        int cant_labelset=result.values().size();
        double media=0;
        
                
        for(int current : result.values())
             media+=current;
      
        media= media/cant_labelset;
             
        double varianza=0;
        double temp;
               
       for(int current : result.values())
       {
              temp = current-media;
              varianza+= Math.pow(temp, 2);
              //System.out.println("calculo ,"+temp+" , "+Math.pow(temp, 2));
       }
        varianza = varianza/cant_labelset;
        
       
        
        value = Math.sqrt(varianza);
       
        
        return value;
    }
    
    
    
    public static double MeanOfStDev(MultiLabelInstances train)
    {
        double value;
        
        Instances instances = train.getDataSet();
            double res = 0.0;
            double dev;
            int countNumeric = 0;
            int[] ints = train.getFeatureIndices();
            for (int i : ints) {
                Attribute att = instances.attribute(i);
                if (att.isNumeric()) {
                    countNumeric++;
                    double variance = instances.variance(att);
                    dev = Math.sqrt(variance);
                    res += dev;
                }
            }
            if (countNumeric > 0) {
                value = res / countNumeric;
            } else {
                //no numeric attributes in dataset
                value = Double.NaN;
            }
            return value;
    }
    
    
    public static double MeanOfMeans(MultiLabelInstances train )
    {
            double value;
            
            Instances instances = train.getDataSet();
            double res = 0.0;
            int countNumeric = 0;
            Set<Attribute> attributeSet = train.getFeatureAttributes();
            for (Attribute att : attributeSet) {
                if (att.isNumeric()) {
                    countNumeric++;
                    res += instances.meanOrMode(att);
                }
            }
            value = res / countNumeric;
            
            return value;
    }
    
    public static double Default_Accuracy(MultiLabelInstances train)
    {
        double value;
        
        try {
                int total = train.getNumInstances();
                mulan.data.Statistics st =  new mulan.data.Statistics();
                st.calculateStats(train);
                
                //get labelcombination
                HashMap<LabelSet, Integer> map = st.labelCombCount();
                                
                Collection<Integer> values = map.values(); 
                SortedSet<Integer> sorted = new TreeSet<Integer>(values);
                double res = sorted.last();
                
                System.out.println("res " +res);
                
                value = res / total;
            } catch (Exception e) {
                value = 0;
                e.printStackTrace();
            }
        
        return value;
    }
    
    public static int count_attributes_nominal(MultiLabelInstances dataset1)
    {
        Instances dataset_i = dataset1.getDataSet();
        int length = dataset_i.numAttributes()-dataset1.getNumLabels();
        int count=0;
        
        for(int i=0;i<length;i++)
            if(dataset_i.attribute(i).isNominal())count++;
      
        return count;
    }
    

    
    public static int count_attributes_binary(MultiLabelInstances dataset1)
    {        
        Instances dataset_i = dataset1.getDataSet();
        int length = dataset_i.numAttributes()-dataset1.getNumLabels();
        int count=0;
        
        for(int i=0;i<length;i++)
            if(dataset_i.attribute(i).numValues()==2)count++;
      
        return count;
    }
    
    
     
   
    

    /**
     * @param args the command line arguments
     */
    
    
    
    
}
