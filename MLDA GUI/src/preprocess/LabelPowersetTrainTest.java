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

/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 *    LabelPowersetTrainTest.java
 *    This java class is based on the mulan.data.LabelPowersetStratification.java 
 *    class provided in the mulan java framework for multi-label learning
 *    Tsoumakas, G., Katakis, I., Vlahavas, I. (2010) "Mining Multi-label Data", 
 *    Data Mining and Knowledge Discovery Handbook, O. Maimon, L. Rokach (Ed.),
 *    Springer, 2nd edition, 2010.
 */
package preprocess;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import mulan.data.MultiLabelInstances;
import mulan.transformations.LabelPowersetTransformation;
import weka.core.Instances;
import weka.core.TechnicalInformation;
import weka.core.TechnicalInformationHandler;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;


public class LabelPowersetTrainTest{

    private int seed = 1;

    /**
     * Default constructor
     */
    public LabelPowersetTrainTest() {
        seed = 1;
    }
    
    /**
     * Constructor setting the random seed
     * 
     * @param aSeed the seed for random generation
     */    
    public LabelPowersetTrainTest(int aSeed) {
        seed = aSeed;
    }

    
    public MultiLabelInstances[] split(MultiLabelInstances data, double percentage) {
        try {
            int folds = 100;
            int testFolds = (int)Math.round(percentage/100.0);
            
            MultiLabelInstances[] segments = new MultiLabelInstances[folds];
            LabelPowersetTransformation transformation = new LabelPowersetTransformation();
            Instances transformed;

            // transform to single-label
            transformed = transformation.transformInstances(data);
            
            // add id 
            Add add = new Add();
            add.setAttributeIndex("first");
            add.setAttributeName("instanceID");
            add.setInputFormat(transformed);
            transformed = Filter.useFilter(transformed, add);
            for (int i=0; i<transformed.numInstances(); i++) {
                transformed.instance(i).setValue(0, i);
            }            
            transformed.setClassIndex(transformed.numAttributes()-1);
            
            // stratify
            transformed.randomize(new Random(seed));
            transformed.stratify(folds);
            
            for (int i = 0; i < folds; i++) {
                //System.out.println("Fold " + (i + 1) + "/" + folds);
                Instances temp = transformed.testCV(folds, i);
                Instances test = new Instances(data.getDataSet(), 0);
                for (int j=0; j<temp.numInstances(); j++) {
                    test.add(data.getDataSet().instance((int) temp.instance(j).value(0)));
                }                
                segments[i] = new MultiLabelInstances(test, data.getLabelsMetaData());
            }
            
            MultiLabelInstances [] trainTest = new MultiLabelInstances[2];
            int [] v = new int[100];
            for(int i=0; i<100; i++){
                v[i] = i;
            }
            Random rand = new Random();
            int r, swap;
            for(int i=0; i<100; i++){
                r = rand.nextInt(100);
                swap = v[r];
                v[r] = v[i];
                v[i] = swap;
            }
            
            Instances train = new Instances(segments[0].getDataSet(), 0);
            Instances test = new Instances(segments[0].getDataSet(), 0);
            
            for(int i=0; i<testFolds; i++){
                test.addAll(segments[v[i]].getDataSet());
            }
            for(int i=testFolds; i<100; i++){
                train.addAll(segments[v[i]].getDataSet());
            }
            
            trainTest[0] = new MultiLabelInstances(train, data.getLabelsMetaData());
            trainTest[1] = new MultiLabelInstances(test, data.getLabelsMetaData());
            
            return trainTest;
            
        } catch (Exception ex) {
            Logger.getLogger(LabelPowersetTrainTest.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    
    public MultiLabelInstances[] stratify(MultiLabelInstances data, int folds) {
        try {
            MultiLabelInstances[] segments = new MultiLabelInstances[folds];
            LabelPowersetTransformation transformation = new LabelPowersetTransformation();
            Instances transformed;

            // transform to single-label
            transformed = transformation.transformInstances(data);
            
            // add id 
            Add add = new Add();
            add.setAttributeIndex("first");
            add.setAttributeName("instanceID");
            add.setInputFormat(transformed);
            transformed = Filter.useFilter(transformed, add);
            for (int i=0; i<transformed.numInstances(); i++) {
                transformed.instance(i).setValue(0, i);
            }            
            transformed.setClassIndex(transformed.numAttributes()-1);
            
            // stratify
            transformed.randomize(new Random(seed));
            transformed.stratify(folds);
            
            for (int i = 0; i < folds; i++) {
                //System.out.println("Fold " + (i + 1) + "/" + folds);
                Instances temp = transformed.testCV(folds, i);
                Instances test = new Instances(data.getDataSet(), 0);
                for (int j=0; j<temp.numInstances(); j++) {
                    test.add(data.getDataSet().instance((int) temp.instance(j).value(0)));
                }                
                segments[i] = new MultiLabelInstances(test, data.getLabelsMetaData());
            }
            return segments;
        } catch (Exception ex) {
            Logger.getLogger(LabelPowersetTrainTest.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
