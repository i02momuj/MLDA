
package utils;

/**
 * 
 * @author Jose Maria Moyano Murillo
 */
public class AttributesPair 
{
    private String attributeName1, attributeName2;
    private int attribute1Index, attribute2Index, attributeAppearances1, attributeAppearances2;
    
    int appearances;
    
    public AttributesPair(String attr1, String attr2, int appearances)
    {
        this.attributeName1 = attr1;
        this.attributeName2 = attr2;
        this.appearances = appearances;       
    }
    
    
    public AttributesPair(String attr1, String attr2,int appearances, 
            int att1Index, int att2Index, int att1Appearances, int att2Appearances)
    {
        this.attributeName1 = attr1;
        this.attributeName2 = attr2;
        this.appearances = appearances;
        this.attribute1Index =att1Index;
        this.attribute2Index = att2Index;
        this.attributeAppearances1 =att1Appearances;
        this.attributeAppearances2 = att2Appearances;
        
    }
               
    
    public int getAttribute1Index(){
        return attribute1Index;
    }
    
    public int getAttribute2Index(){
        return attribute2Index;
    }
    
    public String getAttributeName1(){ 
        return attributeName1;
    }
    
    public String getAttributeName2(){ 
        return attributeName2;
    }
    
    public double getAppearances(){ 
        return appearances;
    }
    
    public int getAttribute1Appearances(){ 
        return attributeAppearances1;
    }
    
    public int getAttribute2Appearances(){ 
        return attributeAppearances2;
    }
}
