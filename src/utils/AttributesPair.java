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
    private String attr1,attr2;
    private int ind_att1,ind_att2,cant_att1,cant_att2;
    
   
    
    int cant_veces;
    
    public AttributesPair(String attr1, String attr2,int cant_veces)
    {
        this.attr1 = attr1;
        this.attr2 = attr2;
        this.cant_veces = cant_veces;
        
    }
    
        public AttributesPair(String attr1, String attr2,int cant_veces, int ind_att1, int ind_att2, int cant_att1, int cant_att2)
    {
        this.attr1 = attr1;
        this.attr2 = attr2;
        this.cant_veces = cant_veces;
        this.ind_att1 =ind_att1;
        this.ind_att2 = ind_att2;
        this.cant_att1 =cant_att1;
        this.cant_att2 = cant_att2;
        
    }
        
        
    
    public int get_ind_att1(){return ind_att1;}
    
    public int get_ind_att2(){return ind_att2;}
    
    public String get_name_attr1(){ return attr1;}
    
    public String get_name_attr2(){ return attr2;}
    
    public double get_cant_veces(){ return cant_veces;}
    
    public int get_cant_att1(){ return cant_att1;}
    
    public int get_cant_att2(){ return cant_att2;}
}
