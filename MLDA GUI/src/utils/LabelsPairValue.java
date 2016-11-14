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

/**
 * Stores pair of labels
 * 
 * @author Jose Maria Moyano Murillo
 */
public class LabelsPairValue implements Comparable<LabelsPairValue>{
    
    public int label1;
    
    public int label2;
    
    public double value;
    
    /**
     * Constructor without parameters
     */
    public LabelsPairValue(){
        label1 = -1;
        label2 = -1;
        value = -1;
    }
    
    /**
     * Constructor with parameters
     * 
     * @param label1 Label 1 index
     * @param label2 Label 2 index
     * @param value Value
     */
    public LabelsPairValue(int label1, int label2, double value){
        this.label1 = label1;
        this.label2 = label2;
        this.value = value;
    }

    @Override
    public int compareTo(LabelsPairValue t) {
        if(this.value > t.value){
            return 1;
        }
        else if(this.value < t.value){
            return -1;
        }
        else{
            return 0;
        }
    }
    
    @Override
    public String toString(){
        return("[" + label1 + ", " + label2 + "] - " + value);
    }
}
