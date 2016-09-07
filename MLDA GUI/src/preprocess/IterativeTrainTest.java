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
 *    IterativeTrainTest.java
 *    This java class is based on the mulan.data.IterativeStratification.java 
 *    class provided in the mulan java framework for multi-label learning
 *    Tsoumakas, G., Katakis, I., Vlahavas, I. (2010) "Mining Multi-label Data", 
 *    Data Mining and Knowledge Discovery Handbook, O. Maimon, L. Rokach (Ed.),
 *    Springer, 2nd edition, 2010.
 */

package preprocess;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instance;
import weka.core.Instances;
import mulan.data.InvalidDataFormatException;
import mulan.data.IterativeStratification;
import mulan.data.MultiLabelInstances;

/** Class to carry out an stratified iterativeTrainTest partition of multi-label
 * dataset.
 * 
 * This java class is based on the mulan.data.IterativeStratification.java class
 * provided in the Mulan java framework for multi-label learning Tsoumakas, G.,
 * Katakis, I., Vlahavas, I. (2010) "Mining Multi-label Data", Data Mining and
 * Knowledge Discovery Handbook, O. Maimon, L. Rokach (Ed.), Springer, 2nd
 * edition, 2010. Our contribution is the adaptation of method split to generate
 * train-test partition.
 * 
 * @author F.J. Gonzalez
 * @author Eva Gigaja
 * @version 20150925
 * @see Sechidis, K.; Tsoumakas, G. & Vlahavas, I. Gunopulos, D.; Hofmann, T.;
 *      Malerba, D. & Vazirgiannis, M. (Eds.) On the Stratification of
 *      Multi-label Data Machine Learning and Knowledge Discovery in Databases,
 *      Springer Berlin Heidelberg, 2011, 6913, 145-158 */
public class IterativeTrainTest
  {
  /** Seed for reproduction of results */
  private int seed = 1;

  /** Sets the seed for reproduction of results.
   *
   * @param aSeed
   *          Sed for reproduction of results. */
  public void setSeed(int aSeed)
    {
    seed = aSeed;
    }

  /** Returns a array with two multi-label stratified datasets corresponding to
   * the train and test datasets respectively.
   *
   * @param data
   *          A multi-label dataset.
   * @param percentage
   *          Percentage of train dataset.
   * @return MultiLabelInstances
   * @throws java.lang.Exception */
  public MultiLabelInstances[] split(MultiLabelInstances data, double percentage)
    {
    int folds = 2;
    MultiLabelInstances[] segments = new MultiLabelInstances[folds];
    double[] splitRatio = new double[folds];
    splitRatio[0] = percentage / 100;
    splitRatio[1] = 1.0 - splitRatio[0];
    Instances[] singleSegments = foldsCreation(data.getDataSet(), new Random(seed), splitRatio, data.getNumLabels(), data.getLabelIndices(), data.getNumInstances());
    for (int i = 0; i < folds; i++)
      {
      try
        {
        segments[i] = new MultiLabelInstances(singleSegments[i], data.getLabelsMetaData());
        }
      catch (InvalidDataFormatException ex)
        {
        Logger.getLogger(IterativeStratification.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    return segments;
    }

  private Instances[] foldsCreation(Instances workingSet, Random random, double[] splitRatio, int numLabels, int[] labelIndices, int totalNumberOfInstances)
    {
    int numFolds = splitRatio.length;
    // The instances on the final folds
    Instances[] instancesOnSplits = new Instances[numFolds];
    // Initialize the folds
    for (int fold = 0; fold < numFolds; fold++)
      {
      instancesOnSplits[fold] = new Instances(workingSet, 0);
      }
    // *************************************
    // First Part of the Algorithm LINES 1-9
    // *************************************
    // LINE 7 in the Algorithm
    // The vector with the frequencies in the data set (frequency: the number of
    // examples per label)
    int[] frequenciesOnDataset = new int[numLabels];
    // Calculating the number of examples per label in the initial data set
    frequenciesOnDataset = calculatingTheFrequencies(workingSet, numLabels, labelIndices);
    // LINE 2-3 and 8-9 in the Algorithm
    // I define the desiredFolds that I want by calculating them using the
    // array of the splitRatio and in the last column the desired number of
    // instances in each fold
    double[][] desiredSplit = new double[numFolds][numLabels + 1];
    // In the beginning is the desiredSplit and I reduce the values of the
    // frequencies (first numLabels columns) and of the instances (last column)
    // every time I put an instance in the splits.
    desiredSplit = calculatingTheDesiredSplits(frequenciesOnDataset, splitRatio, numLabels, totalNumberOfInstances);
    // *************************************
    // Second Part of the Algorithm LINES 10-34
    // *************************************
    // LINE 11-14 in the Algorithm
    // A vector to keep the rarest label. I keep both the index [0] and the
    // value [1], when I say value I mean the number of examples for the rarest
    // label.
    int[] smallestFreqLabel = new int[2];
    // Function which returns these characteristics of the rarest label
    smallestFreqLabel = takingTheSmallestIndexAndNumberInVector(frequenciesOnDataset, totalNumberOfInstances);
    // This variable gives me the fold in which I will insert an instance
    int splitToBeInserted;
    // The instances that are filtered for a particular label (there are 1
    // for a particular label)
    Instances filteredInstancesForLabel;
    Instance filteredInstance;
    boolean[] trueLabels = new boolean[numLabels];
    for (int lab = 0; lab < numLabels; lab++)
      {
      // By calling the function I take the instances that are annotated
      // with the label with index smallestFreqLabel[0]
      // and I also take the workingSet with the remaining instances.
      // I use a temporal variable temp for making the code more efficient
      Instances[] temp = new Instances[2];
      temp = takeTheInstancesOfTheLabel(workingSet, numLabels, labelIndices, smallestFreqLabel);
      // The instances that I will split at this point
      // LINE 13 in the Algorithm
      filteredInstancesForLabel = temp[0];
      // The remaining instances
      workingSet = temp[1];
      // This variable is used to tell me the suitable folds in which an
      // instance can be inserted.
      // The first element contains the total number of the proper Folds and the
      // rest are the indexes of these folds
      int[] possibleSplits;
      // I share the filtered instances into the splits.
      // The first priority is the splits with the highest desired frequency.
      // The second priority is the split with the highest desired number of
      // instances.
      // If two splits are equivalent for the above two rules I decide randomly
      // in which fold the instance will be inserted
      for (int instancesOfTheLab = 0; instancesOfTheLab < filteredInstancesForLabel.numInstances(); instancesOfTheLab++)
        {
        filteredInstance = filteredInstancesForLabel.instance(instancesOfTheLab);
        trueLabels = getTrueLabels(filteredInstance, numLabels, labelIndices);
        // LINES 20-27 in the Algorithm
        // I call that function to return the possible folds with the above
        // priorities.
        // possibleSplits[0] contains the total number of possible folds and the
        // rest elements
        // are the indexes of the possible folds.
        possibleSplits = findThePossibleSpit(desiredSplit, smallestFreqLabel[0], numFolds);
        // I decide in which fold to enter the instance. If there are more that
        // one possible folds
        // I break the ties randomly
        if (possibleSplits[0] != 1)
          {
          splitToBeInserted = possibleSplits[random.nextInt(possibleSplits[0]) + 1];
          }
        else
          {
          splitToBeInserted = possibleSplits[1];
          }
        // LINE 28 in the Algorithm
        // Enter the instance to the proper fold
        instancesOnSplits[splitToBeInserted].add(filteredInstance);
        // LINE 30-32 in the Algorithm
        // Update the statistics of this fold
        desiredSplit[splitToBeInserted] = updateDesiredSplitStatistics(desiredSplit[splitToBeInserted], trueLabels);
        }
      // I updating the values for the next iteration
      frequenciesOnDataset = calculatingTheFrequencies(workingSet, numLabels, labelIndices);
      smallestFreqLabel = takingTheSmallestIndexAndNumberInVector(frequenciesOnDataset, totalNumberOfInstances);
      }
    // Special case when I have a number of examples that are not annotated with
    // any label (i.e. mediamill data set)
    // These examples are distributed so as to balance the desired number of
    // examples at each fold
    Instance noAnnotatedInstances;
    int[] possibleSplitsNoAnnotated = new int[numFolds];
    while (workingSet.numInstances() != 0)
      {
      possibleSplitsNoAnnotated = returnPossibleSplitsForNotAnnotated(desiredSplit);
      noAnnotatedInstances = workingSet.instance(0);
      if (possibleSplitsNoAnnotated[0] != 1)
        {
        splitToBeInserted = possibleSplitsNoAnnotated[random.nextInt(possibleSplitsNoAnnotated[0]) + 1];
        }
      else
        {
        splitToBeInserted = possibleSplitsNoAnnotated[1];
        }
      // Entering the instance to the proper fold
      instancesOnSplits[splitToBeInserted].add(noAnnotatedInstances);
      // Updating the instances
      desiredSplit[splitToBeInserted][desiredSplit[splitToBeInserted].length - 1] = desiredSplit[splitToBeInserted][desiredSplit[splitToBeInserted].length - 1] - 1;
      // Deleting the instance from the working set
      workingSet.delete(0);
      }
    return instancesOnSplits;
    }

  /** Returns the number of examples per label in each fold.
   *
   * @param dataSet
   *          A dataset.
   * @param numLabels
   *          Number of labels.
   * @param labelIndices
   *          Array with label indices.
   * @return int[] */
  /* Function that returns the number of examples per label in each fold */
  private int[] calculatingTheFrequencies(Instances dataSet, int numLabels, int[] labelIndices)
    {
    int[] vectorSumOfLabels = new int[numLabels];
    int numInstances = dataSet.numInstances();
    boolean[] trueLabels = new boolean[numLabels];
    for (int instanceIndex = 0; instanceIndex < numInstances; instanceIndex++)
      {
      Instance instance = dataSet.instance(instanceIndex);
      trueLabels = getTrueLabels(instance, numLabels, labelIndices);
      for (int lab = 0; lab < numLabels; lab++)
        {
        if (trueLabels[lab] == true)
          {
          vectorSumOfLabels[lab] += 1;
          }
        else
          {
          vectorSumOfLabels[lab] += 0;
          }
        }
      }
    return vectorSumOfLabels;
    }

  /** Returns the desired number of examples per label in each fold and in the
   * last column the total desired number of examples in each fold.
   *
   * @param frequenciesOnDataset
   * @param splitRatio
   * @param numLabels
   * @param totalNumberOfInstances
   * @return double[][] */
  /* Function that returns the desired number of examples per label in each fold
   * and in the last column the total desired number of examples in each fold. */
  private double[][] calculatingTheDesiredSplits(int[] frequenciesOnDataset, double[] splitRatio, int numLabels, int totalNumberOfInstances)
    {
    double[][] desiredSplit = new double[splitRatio.length][numLabels + 1];
    for (int fold = 0; fold < splitRatio.length; fold++)
      {
      for (int lab = 0; lab < numLabels; lab++)
        {
        desiredSplit[fold][lab] = splitRatio[fold] * frequenciesOnDataset[lab];
        }
      desiredSplit[fold][numLabels] = splitRatio[fold] * totalNumberOfInstances;
      }
    return desiredSplit;
    }

  /** Returns the rarest label and the number of examples that are annotated with
   * that label.
   *
   * @param vectorSumOfLabels
   * @param totalNumberOfInstances
   * @return int[] */
  /* Function that returns the rarest label and the number of examples that are
   * annotated with that label */
  private int[] takingTheSmallestIndexAndNumberInVector(int[] vectorSumOfLabels, int totalNumberOfInstances)
    {
    int smallestIndex = 0;
    int smallestValue = totalNumberOfInstances;
    int[] returnedTable = new int[2];
    for (int index = 0; index < vectorSumOfLabels.length; index++)
      {
      if (vectorSumOfLabels[index] < smallestValue && vectorSumOfLabels[index] != 0)
        {
        smallestIndex = index;
        smallestValue = vectorSumOfLabels[index];
        }
      }
    returnedTable[0] = smallestIndex;
    returnedTable[1] = smallestValue;
    return returnedTable;
    }

  /** Returns two sets of instances. The instances that are annotated with the
   * label desiredLabel[0] and also returns the rest on the instances.
   *
   * @param workingSet
   * @param numLabels
   * @param labelIndices
   * @param desiredLabel
   * @return Instances[] */
  /* This function returns two sets of instances. The instances that are
   * annotated with the label desiredLabel[0] and also returns the rest on the
   * instances */
  private Instances[] takeTheInstancesOfTheLabel(Instances workingSet, int numLabels, int[] labelIndices, int[] desiredLabel)
    {
    // In the returnedInstance in the [0] index is the filtered instances for
    // the desired label
    // while on the [1] index is the remaining workingSet returned
    Instances[] returnedInstances = new Instances[2];
    Instances filteredInstancesOfLabel = new Instances(workingSet, 0);
    int numInstances = workingSet.numInstances();
    boolean[] trueLabels = new boolean[numLabels];
    int[] removedIndexes = new int[desiredLabel[1]];
    int count = 0;
    // Firstly I filter the instances that are annotated with the label
    // desiredLabel[0] and I keep the indexes of the filtered instances
    for (int instanceIndex = 0; instanceIndex < numInstances; instanceIndex++)
      {
      Instance instance = workingSet.instance(instanceIndex);
      trueLabels = getTrueLabels(instance, numLabels, labelIndices);
      if (trueLabels[desiredLabel[0]] == true)
        {
        filteredInstancesOfLabel.add(instance);
        removedIndexes[count] = instanceIndex;
        count++;
        }
      }
    // Using the indexes of the filtered instances i remove them from the
    // working set. CAUTION: I count in inverse order to make the removal in
    // the proper way
    for (int k = count - 1; k >= 0; k--)
      {
      workingSet.delete(removedIndexes[k]);
      }
    returnedInstances[0] = filteredInstancesOfLabel;
    returnedInstances[1] = workingSet;
    return returnedInstances;
    }

  /** Takes fold statistics and the index of the desired label (desired in the
   * sense the label that we will apply the stratification sampling at this
   * point) and it decides which are the folds that this instance can be
   * inserted. The first priority is the fold with the smallest number of labels
   * in the desired label. The second priority is the fold with the less number
   * of instances.
   *
   * @param desiredSplit
   * @param lab
   * @param numFolds
   * @return int[] */
  /* This function takes fold statistics and the index of the desired label
   * (desired in the sense the label that we will apply the stratification
   * sampling at this point) and it decides which are the folds that this
   * instance can be inserted. The first priority is the fold with the smallest
   * number of labels in the desired label. The second priority is the fold with
   * the less number of instances. */
  private int[] findThePossibleSpit(double[][] desiredSplit, int lab, int numFolds)
    {
    int[] possibleSplits = new int[numFolds + 1];
    // Firstly Check which fold has the highest nonnegative value on the label
    // lab
    int maxIndex = 0;
    double maxValue = -1;
    for (int fold = 0; fold < numFolds; fold++)
      {
      if (desiredSplit[fold][lab] > maxValue)
        {
        maxIndex = fold;
        maxValue = desiredSplit[fold][lab];
        }
      }
    // Now I will check the case that two folds have the same number of
    // maximum desired frequency
    for (int fold = 0; fold < numFolds; fold++)
      {
      if (desiredSplit[fold][lab] == maxValue)
        {
        // I will take the split with the maximum number of desired examples
        if (desiredSplit[fold][desiredSplit[0].length - 1] > desiredSplit[maxIndex][desiredSplit[0].length - 1])
          {
          maxIndex = fold;
          }
        }
      }
    int count = 0;
    // Check if there are also other folds with the same maximum desired
    // frequency and the desired number of examples
    for (int fold = 0; fold < numFolds; fold++)
      {
      if (desiredSplit[fold][lab] == maxValue)
        {
        // I will take as min the fold with the smallest number of instances
        if (desiredSplit[fold][desiredSplit[0].length - 1] == desiredSplit[maxIndex][desiredSplit[0].length - 1])
          {
          count++;
          possibleSplits[count] = fold;
          maxIndex = fold;
          }
        }
      }
    possibleSplits[0] = count; // In the first place of this array I put the
                               // total number of possible Folds
    return possibleSplits;
    }

  /** Updates the desired splits every time that an instance is inserted into a
   * fold.
   *
   * @param desiredSplit
   * @param trueLabels
   * @return double[] */
  /* Function that updates the desired splits every time that an instance is
   * inserted into a fold */
  private double[] updateDesiredSplitStatistics(double[] desiredSplit, boolean[] trueLabels)
    {
    double[] returnedArray = new double[desiredSplit.length];
    for (int lab = 0; lab < desiredSplit.length - 1; lab++)
      {
      if (trueLabels[lab] == true)
        {
        returnedArray[lab] = desiredSplit[lab] - 1;
        }
      else
        {
        returnedArray[lab] = desiredSplit[lab];
        }
      }
    // Also add in the last column another instance
    returnedArray[desiredSplit.length - 1] = desiredSplit[desiredSplit.length - 1] - 1;
    return returnedArray;
    }

  /** Returns the possible folds for the examples that are not annotated with any
   * label. In this special case the only criterion is the total number of
   * examples in each fold.
   *
   * @param desiredSplit
   * @return int[] */
  /* Function that returns the possible folds for the examples that are not
   * annotated with any label. In this special case the only criterion is the
   * total number of examples in each fold */
  private int[] returnPossibleSplitsForNotAnnotated(double[][] desiredSplit)
    {
    int numFolds = desiredSplit.length;
    int minIndex = 0;
    int[] possibleSplits = new int[numFolds + 1];
    for (int fold = 0; fold < numFolds; fold++)
      {
      if (desiredSplit[fold][desiredSplit[0].length - 1] > desiredSplit[minIndex][desiredSplit[0].length - 1])
        {
        minIndex = fold;
        }
      }
    int count = 0;
    // Check if there are also other folds with the same min number and the
    // smallest number of instances
    for (int fold = 0; fold < numFolds; fold++)
      {
      if (desiredSplit[fold][desiredSplit[0].length - 1] == desiredSplit[minIndex][desiredSplit[0].length - 1])
        {
        count++;
        possibleSplits[count] = fold;
        minIndex = fold;
        }
      }
    possibleSplits[0] = count;
    return possibleSplits;
    }

  /** Returns the relevant labels of one instance.
   *
   * @param instance
   * @param numLabels
   * @param labelIndices
   * @return boolean[] */
  private boolean[] getTrueLabels(Instance instance, int numLabels, int[] labelIndices)
    {
    boolean[] trueLabels = new boolean[numLabels];
    for (int counter = 0; counter < numLabels; counter++)
      {
      int classIdx = labelIndices[counter];
      String classValue = instance.attribute(classIdx).value((int) instance.value(classIdx));
      trueLabels[counter] = classValue.equals("1");
      }
    return trueLabels;
    }
  }
