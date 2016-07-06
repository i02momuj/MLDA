/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author osc
 */
public class ImbalancedFeature {
    
    private String name;
    private int frequency;
    private double ir;
    private double variance;
    double ir_inter_class;
    
    public ImbalancedFeature(String name)
    {
        this.name = name;
        frequency =-1;
        ir=-1;
        variance=-1;
        ir_inter_class=-1;
    }
    
    public ImbalancedFeature(String name, int frequency)
    {
        this.name = name;
        this.frequency = frequency;
        ir=-1;
        variance=-1;
        ir_inter_class=-1;
    }
    
    public ImbalancedFeature(String name, double ir, double variance)
    {
        this.name = name;
        frequency =-1;
        this.ir = ir;
        this.variance = variance;
        ir_inter_class=-1;
    }
    
    public ImbalancedFeature(String name, int frequency, double ir)
    {
        this.name = name;
        this.frequency=frequency ;
        this.ir = ir;
         variance=-1;
         ir_inter_class=-1;
    }
    
    public ImbalancedFeature(String name, int frequency, double ir, double variance, double ir_inter_class)
    {
        this.name = name;
        this.frequency=frequency ;
        this.ir = ir;
        this.variance=variance;
         this.ir_inter_class= ir_inter_class;
    }
    
    public String get_name() { return name; }
    
    public int get_frequency(){return frequency;}
    
    public double get_ir(){return ir;}
    
    public double get_variance(){return variance;}
    
    public double get_ir_inter_class(){return ir_inter_class;}
    
}
