/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metrics_API.size;

import metrics_API.MLDataMetric;
import mulan.data.MultiLabelInstances;

/**
 *
 * @author Jose
 */
public class Attributes extends MLDataMetric{

    public Attributes(MultiLabelInstances mlData) {
        super("Attributes", mlData);        
    }
    
    protected double calculate(){
        return mlData.getFeatureIndices().length;
    }
    
}
