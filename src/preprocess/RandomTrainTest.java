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

package preprocess;

import java.util.Random;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemovePercentage;
import mulan.data.MultiLabelInstances;

/** Class to split a multi-label dataset into two multi-label random datasets
 * corresponding to the train and test datasets respectively.
 * 
 * @author F.J. Gonzalez
 * @author Eva Gigaja
 * @version 20150925 */
public class RandomTrainTest
  {
  /** Seed for reproduction of train-test results. */
  private int seed = 1;

  /** Sets the seed for reproduction of train-test results.
   *
   * @param aSeed
   *          Seed for reproduction of train-test results. */
  public void setSeed(int aSeed)
    {
    seed = aSeed;
    }

  /** Returns a array with two multi-label random datasets corresponding to the
   * train and test datasets respectively.
   *
   * @param mlDataSet
   *          A multi-label dataset.
   * @param percentage
   *          Percentage of train dataset.
   * @return MultiLabelInstances
   * @throws java.lang.Exception */
  public MultiLabelInstances[] split(MultiLabelInstances mlDataSet, double percentage) throws Exception
    {
        System.out.println("percentage: " + percentage);
    Instances trainDataSet = null, testDataSet = null;
    // splits the data set into train and test
    Instances dataSet = mlDataSet.getDataSet();
    dataSet.randomize(new Random(seed));
    RemovePercentage rmvp = new RemovePercentage();
    rmvp.setInvertSelection(true);
    rmvp.setPercentage(percentage);
    rmvp.setInputFormat(dataSet);
    trainDataSet = Filter.useFilter(dataSet, rmvp);
    rmvp = new RemovePercentage();
    rmvp.setPercentage(percentage);
    rmvp.setInputFormat(dataSet);
    testDataSet = Filter.useFilter(dataSet, rmvp);
    MultiLabelInstances Partition[] = new MultiLabelInstances[2];
    Partition[0] = new MultiLabelInstances(trainDataSet, mlDataSet.getLabelsMetaData());
    Partition[1] = new MultiLabelInstances(testDataSet, mlDataSet.getLabelsMetaData());
    return Partition;
    }
  }
