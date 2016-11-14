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
 * This class implements a pair of attributes
 * 
 * @author Jose Maria Moyano Murillo
 */
public class AttributesPair 
{
    private String attributeName1, attributeName2;
    private int attribute1Index, attribute2Index, attributeAppearances1, attributeAppearances2;
    
    int appearances;
    
    /**
     * Constructor specifying the name of both attributes and the number of 
     * appearances of the pair
     * 
     * @param attr1 Name of attribute 1
     * @param attr2 Name of attribute 2
     * @param appearances Number of appearances of the pair
     */
    public AttributesPair(String attr1, String attr2, int appearances)
    {
        this.attributeName1 = attr1;
        this.attributeName2 = attr2;
        this.appearances = appearances;       
    }
    
    /**
     * Constructor specifying the names, indices and number of appearances of 
     * both attributes, and also the number of appearances of the pair
     * 
     * @param attr1 Name of attribute 1
     * @param attr2 Name of attribute 2
     * @param appearances Number of appearances of the pair
     * @param att1Index Index of attribute 1
     * @param att2Index Index of attribute 2
     * @param att1Appearances Number of appearances of attribute 1
     * @param att2Appearances Number of appearances of attribute 2
     */
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
               
    /**
     * Get the attribute 1 index
     * 
     * @return Index of attribute1
     */
    public int getAttribute1Index(){
        return attribute1Index;
    }
    
    /**
     * Get the attribute 2 index 
     * 
     * @return Index of attribute2
     */
    public int getAttribute2Index(){
        return attribute2Index;
    }
    
    /**
     * Get the name of attribute 1
     * 
     * @return Name of attribute1
     */
    public String getAttributeName1(){ 
        return attributeName1;
    }
    
    /**
     * Get the name of attribute 2
     * 
     * @return Name of attribute2
     */
    public String getAttributeName2(){ 
        return attributeName2;
    }
    
    /**
     * Get the number of appearances of the pair
     * 
     * @return Appearances
     */
    public double getAppearances(){ 
        return appearances;
    }
    
    /**
     * Get the number of appearances of attribute 1
     * 
     * @return Attribute1 appearances
     */
    public int getAttribute1Appearances(){ 
        return attributeAppearances1;
    }
    
    /**
     * Get the number of appearances of attribute 2
     * 
     * @return Attribute2 appearances
     */
    public int getAttribute2Appearances(){ 
        return attributeAppearances2;
    }
    
}
