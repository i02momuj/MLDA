/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author oscglezm
 */
public class AttributesPair 
{
    private String attr1, attr2;
    private int index1, index2, appearances1, appearances2;    
    int appearances;
    
    public AttributesPair(String attr1, String attr2, int appearances)
    {
        this.attr1 = attr1;
        this.attr2 = attr2;
        this.appearances = appearances;
        
    }
    
    public AttributesPair(String attr1, String attr2, int appearances, 
                int index1, int index2, int appearances1, int appearances2)
    {
        this.attr1 = attr1;
        this.attr2 = attr2;
        this.appearances = appearances;
        this.index1  = index1;
        this.index2 = index2;
        this.appearances1 = appearances1;
        this.appearances2 = appearances2;
        
    }
                
    
    public int getIndex1(){
        return index1;
    }
    
    public int getIndex2(){
        return index2;
    }
    
    public String getAttributeName1() {
        return attr1;
    }
    
    public String getAttributeName2() { 
        return attr2;
    }
    
    public double getAppearances() { 
        return appearances;
    }
    
    public int getAppearances1() { 
        return appearances1;
    }
    
    public int getAppearances2() { 
        return appearances2;
    }
}
