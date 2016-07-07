package utils;

import static utils.util.hasMoreNDigits;

/**
 *
 * @author Jose Maria Moyano Murillo
 */
public class MetricUtils {
    
    public static String truncateValue (String value, int digits)
    {
        double valor = Double.parseDouble(value);
        return truncateValue(valor, digits);
    }
    
    
    public static String truncateValue(double value, int digits)
    {
        String number = Double.toString(value);
        int countDigits =0;
        String result = "";
        boolean flag =false;
        
        if(!hasMoreNDigits(value, digits)) {
            return Double.toString(value);
        }
        
        for(int i=0; i<number.length();i++)
        {
            if(flag && countDigits!=digits){
                countDigits++;
            }
                
            if(number.charAt(i)=='.') {
                flag=true; 
                continue;
            }
            
            if(countDigits == digits) 
            {
                result=number.substring(0,i);
                break;
            }
        }
        
        return result;
    }
    
}
