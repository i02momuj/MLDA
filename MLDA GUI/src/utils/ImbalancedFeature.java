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
 * This class stores metrics for imbalanced features
 * 
 * @author Jose Maria Moyano Murillo
 */
public class ImbalancedFeature {
    
    private String name;
    private int appearances;
    private double IRIntraClass;
    private double variance;
    double IRInterClass;
    
    /**
     * Constructor specifying name and appearances of the attribute
     * 
     * @param name Name of the attribute
     * @param appearances Number of appearances of the attribute
     */
    public ImbalancedFeature(String name, int appearances)
    {
        this.name = name;
        this.appearances = appearances;
        IRIntraClass=-1;
        variance=-1;
        IRInterClass=-1;
    }
    
    /**
     * Constructor specifying name, IR intra-class and variance of the attribute
     * 
     * @param name Name of the attribute
     * @param IRIntraClass IR intra-class
     * @param variance variance
     */
    public ImbalancedFeature(String name, double IRIntraClass, double variance)
    {
        this.name = name;
        appearances =-1;
        this.IRIntraClass = IRIntraClass;
        this.variance = variance;
        IRInterClass=-1;
    }
    
    /**
     * Constructor specifying name, number of appearances and IR intra-class
     * of the attribute
     * 
     * @param name Name of the attribute
     * @param appearances Number of appearances
     * @param IRIntraClass IR intra-class
     */
    public ImbalancedFeature(String name, int appearances, double IRIntraClass)
    {
        this.name = name;
        this.appearances=appearances ;
        this.IRIntraClass = IRIntraClass;
        variance=-1;
        IRInterClass=-1;
    }
    
    /**
     * Constructor specifying name, number of appearances, IR intra-class,
     * variance and IR inter-class of the attribute
     * 
     * @param name Name of the attribute
     * @param appearances Number of appearances
     * @param IRIntraClass IR intra-class
     * @param variance Variance
     * @param IRInterClass IR inter-class
     */
    public ImbalancedFeature(String name, int appearances, double IRIntraClass, 
            double variance, double IRInterClass)
    {
        this.name = name;
        this.appearances=appearances ;
        this.IRIntraClass = IRIntraClass;
        this.variance=variance;
        this.IRInterClass= IRInterClass;
    }
    
    /**
     * Get name
     * 
     * @return Name
     */
    public String getName() { 
        return name; 
    }
    
    /**
     * Get appearances
     * 
     * @return Number of appearances
     */
    public int getAppearances(){
        return appearances;
    }
    
    /**
     * Get IR intra class
     * 
     * @return IR intra class
     */
    public double getIRIntraClass(){
        return IRIntraClass;
    }
    
    /**
     * Get variance
     * 
     * @return Variance
     */
    public double getVariance(){
        return variance;
    }
    
    /**
     * Get IR inter class
     * 
     * @return IR inter class
     */
    public double getIRInterClass(){
        return IRInterClass;
    }
    
}
