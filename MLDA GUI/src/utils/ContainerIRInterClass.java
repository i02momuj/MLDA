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
 * 
 * @author Jose Maria Moyano Murillo
 */
public class ContainerIRInterClass {
    
    private int[] idByFrequency;
    private double[] idByIRInterClass;
    
    public ContainerIRInterClass(int[] id, double[] id_ir)
    {
        idByFrequency = id;
        idByIRInterClass = id_ir;
    }
    
    
    public void sortByIR()
    {
        int tempAppearances;
        double tempIR;
        
        for(int i=0;i<idByIRInterClass.length-1; i++)
        {
            for(int j=i+1; j<idByIRInterClass.length;j++)
            {
                if( idByIRInterClass[i] < idByIRInterClass[j])
                {
                    //swap IR
                    tempIR = idByIRInterClass[i];
                    idByIRInterClass[i]=idByIRInterClass[j];
                    idByIRInterClass[j]=tempIR;
                    
                    //swap appearances
                    tempAppearances = idByFrequency[i];
                    idByFrequency[i]= idByFrequency[j];
                    idByFrequency[j]=tempAppearances;
                }
            }
        }
    }
    
    
    public double[] getIdByIR(){
        return idByIRInterClass;
    }
    
    public int[] getIdByFrequency(){
        return idByFrequency;
    }
    
}
