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

package conversion;

import java.io.*;
import java.util.*;

/**
 * This class allows to convert Meka datasets into Mulan datasets
 * 
 * @author Jose Maria Moyano Murillo
 */
public class MekaToMulan {

    String relationName = new String();
    int labels = 0;
    int c = 0;
    Vector<Attribute> attributes = new Vector<>();
	
    /**
     * Convert a meka dataset into a mulan dataset
     * 
     * @param mekaFileName Name of meka dataset file
     * @param mulanFileName Name of new mulan dataset file
     */
    public void convert (String mekaFileName, String mulanFileName)
    {
        FileReader frIn = null;
	BufferedReader br;
	FileWriter outArff = null;
	PrintWriter pwARFF;
	FileWriter outXML = null;
	PrintWriter pwXML;
        
        try{
            frIn = new FileReader(mekaFileName + ".arff");
            br = new BufferedReader(frIn);
			
            outArff = new FileWriter(mulanFileName + ".arff");
            pwARFF = new PrintWriter(outArff);
            outXML = new FileWriter(mulanFileName + ".xml");
            pwXML = new PrintWriter(outXML);
			
            String line;
            String [] words;
            
            while((line=br.readLine()) != null)
            {
                words = line.split(" ");
				
                switch (words[0].toLowerCase()) {
                    case "@relation":
                        words = line.split("-C");
                        relationName = mulanFileName;
                        words = words[1].split("[^0-9\\-]");
                        c = Integer.parseInt(words[1]);
                        labels = Math.abs(c);
                        pwARFF.println("@relation " + relationName);
                        pwARFF.println();
                        break;
                    case "@attribute":
                        Attribute atr = new Attribute();
                        atr.name = getAttributeName(line);
                        atr.type = getAttributeType(line);
                        attributes.addElement(atr);
                        pwARFF.println(line);
                        break;
                    case "@data":
                        pwARFF.println();
                        pwARFF.println(line);
                        while((line=br.readLine()) != null) {
                            pwARFF.println(line);
                        }   break;
                    default:
                        break;
                }
            }
			
            /*
             * Generating XML file
             */
            
            pwXML.println("<?xml version=\"1.0\" ?>");
            pwXML.println("<labels xmlns=\"http://mulan.sourceforge.net/labels\">");
			
            Attribute a;
			
            if(c > 0)
            {
                for(int i=0; i<labels; i++)
                {
                    a = attributes.get(i);
                    /* scape caracter is \\
                     * 		replace \' by '
                     */
                    pwXML.println("<label name=\"" + a.name.replaceAll("\\\\\'", "\\'") + "\"> </label>");
		}
            }
            else if(c < 0)
            {
                int x = attributes.size();
                for(int i=(x-labels); i<x; i++)
		{
                    a = attributes.get(i);
                    pwXML.println("<label name=\"" + a.name.replaceAll("\\\\\'", "\\'") + "\"> </label>");
		}
            }
			
            pwXML.println("</labels>");
	} catch(IOException | NumberFormatException e1) { 
            e1.printStackTrace();
	} finally {
            try{
                if(outArff != null) {
                    outArff.close();
                }
		if(outXML != null) {
                    outXML.close();
                }
		if(frIn != null) {
                    frIn.close();
                }
            } catch(Exception e2){
                e2.printStackTrace();
            }
	}
    }
	
    /**
     * Get attribute name from an .arff @attribute line
     * 
     * @param s Attribute line
     * @return Attribute name
     */
    public String getAttributeName(String s)
    {
        //Remove '@attribute' -> 10 characters
        s = s.substring(10);
        s = s.trim();
		
	int init = 0;
	int end = s.length();
        boolean quotes = false;
        
        for(int i=0; i<s.length(); i++)
	{
            if(s.charAt(i) == '\\')
            {
                //If scape symbol, go to the next character
		i++;
            }
            else if((s.charAt(i) == '\'') && (quotes == false))
            {
                //If first quote: starting attribute name
                init = i;
		quotes = true;
            }
            else if((s.charAt(i) == '\'') && (quotes == true))
            {
                //If second quote: end attribute name
                end = i;
		break; //Salimos del for
            }
	}
		
	if(quotes == true)
	{	
            s = s.substring(init+1, end); 
	}
        else {
            s = s.split(" ")[0];
        }
	
        return s;
    }
	
    /**
     * Get attribute type from an attribute line
     * 
     * @param s attribute line
     * @return Attribute type
     */
    public String getAttributeType(String s)
    {
        String [] palabra;
		
        palabra = s.split(" ");
		
	return palabra[palabra.length - 1];
    }
}
