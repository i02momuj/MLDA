/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metrics;

/**
 *
 * @author oscglezm
 */
public class container_id_ir_inter_class {
    
    private int[] id_x_cant_veces;
    private double[] id_x_ir_inter_class;
    
    public container_id_ir_inter_class(int[] id, double[] id_ir)
    {
        id_x_cant_veces = id;
        id_x_ir_inter_class = id_ir;
    }
    
    public void ordena_IR_Mayor_a_Menor()
    {
        int temp_veces;
        double temp_ir;
        
        for(int i=0;i<id_x_ir_inter_class.length-1; i++)
            for(int j=i+1; j<id_x_ir_inter_class.length;j++)
            {
                if( id_x_ir_inter_class[i] < id_x_ir_inter_class[j])
                {
                    //swap IR
                    temp_ir = id_x_ir_inter_class[i];
                    id_x_ir_inter_class[i]=id_x_ir_inter_class[j];
                    id_x_ir_inter_class[j]=temp_ir;
                    
                    //swap CANT_VECES
                    temp_veces = id_x_cant_veces[i];
                    id_x_cant_veces[i]= id_x_cant_veces[j];
                    id_x_cant_veces[j]=temp_veces;
                }
            }
    }
    
    public void ordena_IR_Menor_a_Mayor()
    {
        int temp_veces;
        double temp_ir;
        
        for(int i=0;i<id_x_ir_inter_class.length-1; i++)
            for(int j=i+1; j<id_x_ir_inter_class.length;j++)
            {
                if( id_x_ir_inter_class[i] > id_x_ir_inter_class[j])
                {
                    //swap IR
                    temp_ir = id_x_ir_inter_class[i];
                    id_x_ir_inter_class[i]=id_x_ir_inter_class[j];
                    id_x_ir_inter_class[j]=temp_ir;
                    
                    //swap CANT_VECES
                    temp_veces = id_x_cant_veces[i];
                    id_x_cant_veces[i]= id_x_cant_veces[j];
                    id_x_cant_veces[j]=temp_veces;
                }
            }
    }
    
    public double[] Get_Id_x_IR(){return id_x_ir_inter_class;}
    
    public int[] Get_Id_x_Cant_veces(){return id_x_cant_veces;}
    
}
