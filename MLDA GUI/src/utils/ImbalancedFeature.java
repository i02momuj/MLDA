package utils;

/**
 * 
 * @author Jose Maria Moyano Murillo
 */
public class ImbalancedFeature {
    
    private String name;
    private int appearances;
    private double IRIntraClass;
    private double variance;
    double IRInterClass;
    
    
    public ImbalancedFeature(String name, int appearances)
    {
        this.name = name;
        this.appearances = appearances;
        IRIntraClass=-1;
        variance=-1;
        IRInterClass=-1;
    }
    
    public ImbalancedFeature(String name, double IRIntraClass, double variance)
    {
        this.name = name;
        appearances =-1;
        this.IRIntraClass = IRIntraClass;
        this.variance = variance;
        IRInterClass=-1;
    }
    
    public ImbalancedFeature(String name, int appearances, double IRIntraClass)
    {
        this.name = name;
        this.appearances=appearances ;
        this.IRIntraClass = IRIntraClass;
        variance=-1;
        IRInterClass=-1;
    }
    
    public ImbalancedFeature(String name, int appearances, double IRIntraClass, 
            double variance, double IRInterClass)
    {
        this.name = name;
        this.appearances=appearances ;
        this.IRIntraClass = IRIntraClass;
        this.variance=variance;
        this.IRInterClass= IRInterClass;
    }
    
    public String getName() { 
        return name; 
    }
    
    public int getAppearances(){
        return appearances;
    }
    
    public double getIRIntraClass(){
        return IRIntraClass;
    }
    
    public double getVariance(){
        return variance;
    }
    
    public double getIRInterClass(){
        return IRInterClass;
    }
    
}
