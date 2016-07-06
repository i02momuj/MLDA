package conversion;


import java.io.*;
import java.util.*;

public class MekaToMulan {

    String relationName = new String();
    int labels = 0;
    int c = 0;
    Vector<Attribute> attributes = new Vector<Attribute>();
	
    public void convert (String MekaFileName, String MulanFileName)
    {
        FileReader frIn = null;
	BufferedReader br = null;
	FileWriter outARFF = null;
	PrintWriter pwARFF = null;
	FileWriter outXML = null;
	PrintWriter pwXML = null;
		
	try{
            frIn = new FileReader(MekaFileName + ".arff");
            br = new BufferedReader(frIn);
			
            outARFF = new FileWriter(MulanFileName + ".arff");
            pwARFF = new PrintWriter(outARFF);
            outXML = new FileWriter(MulanFileName + ".xml");
            pwXML = new PrintWriter(outXML);
			
            String line = new String();
            String [] words;
			
            while((line=br.readLine()) != null)
            {
                words = line.split(" ");
		
		if(words[0].toLowerCase().equals("@relation"))
		{
                    words = line.split("-C");
                    relationName = MulanFileName;

                    words = words[1].split("[^0-9\\-]");
		
                    c = Integer.parseInt(words[1]);
                    labels = Math.abs(c);
					
                    pwARFF.println("@relation " + relationName);
                    pwARFF.println();
		}
		else if(words[0].toLowerCase().equals("@attribute"))
                {
                    Attribute atr = new Attribute();
					
                    atr.name = getAttributeName(line);
                    atr.type = getAttributeType(line);
					
                    attributes.addElement(atr);
					
                    pwARFF.println(line);
		}
		else if(words[0].toLowerCase().equals("@data"))
		{
                    pwARFF.println();
                    pwARFF.println(line);
					
                    while((line=br.readLine()) != null)
                        pwARFF.println(line);
                }
            }
			
            /*
             * XML file creation
             */
			
            pwXML.println("<?xml version=\"1.0\" ?>");
            pwXML.println("<labels xmlns=\"http://mulan.sourceforge.net/labels\">");
			
            Attribute a = new Attribute();
			
            if(c > 0)
            {
                for(int i=0; i<labels; i++)
                {
                    a = attributes.get(i);
                    /* scape character for replaceAll is \\
                     *      So, replace reemplazamos \' by '
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
        }
        catch(Exception e1)
        {
            e1.printStackTrace();
        }
        finally
        {
            try{
                if(outARFF != null){    
                    outARFF.close();
                }
                if(outXML != null) {
                    outXML.close();
                }
                if(frIn != null) {
                    frIn.close();
                }
            }
            catch(Exception e2){
                e2.printStackTrace();
            }
        }	
    }
	
	
    public String getAttributeName(String s)
    {
        //Remove '@attribute' -> 10 characters
        s = s.substring(10);

        //No spaces
	s = s.trim();
		
	int init = 0;
	int end = s.length();
	boolean quotes = false;
		
	for(int i=0; i<s.length(); i++)
	{
            if(s.charAt(i) == '\\')
            {
                i++;
            }
            else if((s.charAt(i) == '\'') && (quotes == false))
            {
                //If first quote
		//init of attribute name is here
		init = i;
		quotes = true;
            }
            else if((s.charAt(i) == '\'') && (quotes == true))
            {
                //If second quote
                //end of attribute name is here
		end = i;
		break; //Salimos del for
            }
	}
        
        if(quotes == true)
	{	
            s = s.substring(init+1, end); 
	}
        else{
            s = s.split(" ")[0];
        }
	
        return s;
    }
	
	
    public String getAttributeType(String s)
    {
        String [] words;
		
	words = s.split(" ");
		
	return words[words.length - 1];	
    }
}
