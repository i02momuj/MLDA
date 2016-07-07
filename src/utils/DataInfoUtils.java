package utils;

import java.util.ArrayList;
import mulan.data.MultiLabelInstances;
import weka.core.Attribute;

/**
 *
 * @author Jose Maria Moyano Murillo
 */
public class DataInfoUtils {
    
    public static ArrayList<String> getLabelNamesByLabelset(MultiLabelInstances dataset, String labelset)
    {
        ArrayList<String> labelNames = new ArrayList();
        
        for(int i=0; i<labelset.length();i++)
        {
            if(labelset.charAt(i)=='1')
            {
                labelNames.add(getLabelByIndex(dataset, i).name());
            }
        }
        
        return labelNames;
    }
    
    
    public static Attribute getLabelByIndex(MultiLabelInstances dataset, int id)
    {
        int[] labelIndices = dataset.getLabelIndices();
        
        Attribute result = dataset.getDataSet().instance(1).attribute(labelIndices[id]);
        
        return result;
    }
}
