/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metrics_API;

import mulan.data.MultiLabelInstances;

/**
 *
 * @author Jose Maria Moyano Murillo
 */
public class MLDataMetric implements Comparable{
    
    /*
        Attributes
    */
    
    /* Metric name */
    protected String name;
    
    /* Metric value */
    protected double value;
    
    /* Dataset */
    protected MultiLabelInstances mlData;
    
    
    
    /*
        Constructor
    */
    
    public MLDataMetric(String name, MultiLabelInstances mlData){
        this.name = name;
        this.value = Double.NaN;
        this.mlData = mlData;
    }
    
    
    
    /*
        Public methods
    */
    
    public String getName(){
        return name;
    }
    
    public double getValue(){
        return value;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setValue(double value){
        this.value = value;
    }
    
    
    public String toString(){
        String s = new String();
        
        s += name + ": " + value;
        
        return s;
    } 
    
    protected double calculate(){
        return -1;
    }
       
    
    @Override
    public int compareTo(Object other) {
        return this.name.compareTo(((MLDataMetric)other).getName());
    }
    
}
