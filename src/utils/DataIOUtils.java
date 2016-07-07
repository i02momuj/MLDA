package utils;

import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Jose Maria Moyano Murillo
 */
public class DataIOUtils {
    
    public static void writeXMLFile(PrintWriter wr , String[] labels)
    {
        String line = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    
        wr.write(line);
        wr.write(System.getProperty("line.separator"));
        
        line = "<labels xmlns=\"http://mulan.sourceforge.net/labels\">";
        
        wr.write(line);
        wr.write(System.getProperty("line.separator"));
        
        for(int i=0; i<labels.length;i++)
        {
            line = "<label name=\""+labels[i]+"\"></label>";
            wr.write(line);
            wr.write(System.getProperty("line.separator"));
        }
        
        line = "</labels>";
        wr.write(line);
        wr.write(System.getProperty("line.separator"));
    }
    
    
    public static boolean isMeka(String relationName)
    {
        String type = "-C";
        
        return relationName.contains(type);
    }
    
    
    public static int getLabelsFromARFF(String line)
    {
        int labels;

        String [] words = line.split("-C");
        String c = words[1].trim();
        Matcher matcher = Pattern.compile("\\d+").matcher(c);
        matcher.find();
        labels = Integer.valueOf(matcher.group());
        
        if(c.charAt(0) == '-'){
            labels = labels * -1;
        }

        return labels;
    }
    
    
    public static String getLabelNameFromLine(String attributeLine)
    {
        String result = null;
        int spaces = 0;
        int initPos = 0, endPos = 0;
        
        if(attributeLine.contains("@attribute")) 
        {
            for(int i=0; i<attributeLine.length(); i++)
            {
                if(attributeLine.charAt(i)==' ' && spaces==0){
                    spaces++;
                    initPos=i;
                }
                else if(attributeLine.charAt(i)==' '){
                    endPos=i; 
                    break;
                }
            }
            
            result = attributeLine.substring(initPos+1, endPos);
        }
        
        return result;
    }
    
        
        
}
