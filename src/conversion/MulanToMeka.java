package conversion;


import java.io.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class MulanToMeka {
    
    /* Raltion name */
    String relationName = new String();
	
    /* Number of labels */
    int nLabels = 0;
	
    /* Vector with attributes */
    Vector<Attribute> attributes = new Vector<Attribute>();
	
    /* Vector with labels */
    Vector<Attribute> labels = new Vector<Attribute>();
	
    /* Array with label names */
    String [] labelNames;
    
    /* Indicates if instances are in reduced format */
    boolean formatoReducido;
    
    /* Correspondence between old attributes order and new order */
    Hashtable<Integer, String> originalCorrespondence = new Hashtable<Integer, String>();
    Hashtable<String, Integer> newCorrespondence = new Hashtable<String, Integer>();
    
    
    public void convertir(String nombreFicheroMulan, String nombreFicheroMeka)
    {
        FileReader frIn = null;
	BufferedReader br;
	FileWriter outARFF = null;
	PrintWriter pwARFF;
		
	TreeMap<Integer, String> instanceMap = new TreeMap<Integer, String>();
        
        int n=0;
        
	/* 
	 *  Get labels from XML
	 */
	try{
            File fXmlFile = new File(nombreFicheroMulan + ".xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
			
            doc.getDocumentElement().normalize();
			
            NodeList nList = doc.getElementsByTagName("label");
            
            nLabels = nList.getLength();
            labelNames = new String[nLabels];
			
            for (int i = 0; i < nLabels; i++) {
				 
                Node nNode = nList.item(i);
		 
                if (nNode.getNodeType() == Node.ELEMENT_NODE) 
		{		 
                    Element eElement = (Element) nNode;				
                    labelNames[i] = eElement.getAttribute("name");
		}
            }
	} catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
	}
		
	try{
            frIn = new FileReader(nombreFicheroMulan + ".arff");
            br = new BufferedReader(frIn);			
            outARFF = new FileWriter(nombreFicheroMeka + ".arff");
            pwARFF = new PrintWriter(outARFF);
			
            String line;
            String [] words;
            
            while((line=br.readLine()) != null){
				
                words = line.split(" ");
				
                if(words[0].toLowerCase().equals("@relation"))
		{
                    relationName = nombreFicheroMeka;
		
                    pwARFF.println("@relation '" + relationName + ": -C -" + nLabels + "'");
                    pwARFF.println();
		}
		else if(words[0].toLowerCase().equals("@attribute"))
		{
                    Attribute atr = new Attribute();

                    atr.name = getAttributeName(line);
                    atr.type = getAttributeType(line);
                    
                    /* Store original attribute number */
                    originalCorrespondence.put(n, getAttributeName(line));
					
                    if(isLabel(atr.name)){
                        labels.addElement(atr);
                    }
                    else{	
                        attributes.addElement(atr);
                    }
					
                    n++;
		}
		else if(words[0].toLowerCase().equals("@data"))
		{
                    n = 0; 
					
                    Attribute a;
                    
                    /* Print attributes and labels */
                    for(int i=0; i<attributes.size(); i++)
                    {
                        a = attributes.get(i);
						
                        if (a.name.contains(" ")){
                            pwARFF.println("@attribute " + "\'" + a.name + "\'" + " " + a.type);
			}
                        else {
                            pwARFF.println("@attribute " + a.name + " " + a.type);
                        }
						
			/* Store new file attribute numbers */
                        newCorrespondence.put(a.name, n);
                        n++;
                    }
                    
                    for(int i=0; i<nLabels; i++)
                    {
                        a = labels.get(i);
                        
                        if (a.name.contains(" "))
			{
                            //If has spaces
                            pwARFF.println("@attribute " + "\'" + a.name + "\'" + " " + "{0,1}");
                        }
			else
                        {
                            pwARFF.println("@attribute " + a.name + " " + "{0,1}");
                        }
                        
                        /*Store number of attribute in new file*/   
                        newCorrespondence.put(a.name, n);
			n++;
                    }
		
                    pwARFF.println();
                    pwARFF.println(line);
					
                    int newNum;
                    String name;
                    String inst;
					
                    line = br.readLine();
                    while(line.equals("") == true)
                    {
                        line = br.readLine();
                    }
					
                    if(line.charAt(0) == '{')
                    {
                        /*
                         * Reduced format
			 */
                        
                        formatoReducido = true;

			line = line.replace("{", "");
			line = line.replace("}", "");
			words = line.split(",");
							
			inst = "{";
			instanceMap.clear();
			for(int i=0; i<words.length; i++)
			{
                            /* Obtain correspondence between old and new */
                            words[i] = words[i].trim();
                            name = originalCorrespondence.get(Integer.parseInt(words[i].split(" ")[0]));
                            newNum = newCorrespondence.get(name);
                            instanceMap.put(newNum, words[i].split(" ")[1]);
			}
							
			Iterator iterator = instanceMap.keySet().iterator();
			while (iterator.hasNext()) {
                            Object key = iterator.next();
                            inst = inst + key + " " + instanceMap.get(key) + ",";
			}
							
			inst = inst + "}";
			inst = inst.replaceAll(",}", "}");
			pwARFF.println(inst);
							
			while((line=br.readLine()) != null){
                            if(line.equals("") == false)
                            {
                                line = line.replace("{", "");
				line = line.replace("}", "");
				words = line.split(",");
									
				inst = "{";
				instanceMap.clear();
				for(int i=0; i<words.length; i++)
				{
                                    /* Obtain correspondence between old and new */
                                    words[i] = words[i].trim();
                                    name = originalCorrespondence.get(Integer.parseInt(words[i].split(" ")[0]));
                                    newNum = newCorrespondence.get(name);
                                    instanceMap.put(newNum, words[i].split(" ")[1]);
				}
									
				Iterator iterator2 = instanceMap.keySet().iterator();
				while (iterator2.hasNext()) {
                                    Object key = iterator2.next();
                                    inst = inst + key + " " + instanceMap.get(key) + ",";
				}
									
				inst = inst + "}";
				inst = inst.replaceAll(",}", "}");
				pwARFF.println(inst);
                            }
			}
                    }						
                    else
                    {
                        /*
			 * Complete format
			 */
                        
                        formatoReducido = false;
							
			String [] instance = new String[attributes.size() + labels.size()];
                        
                        words = line.split(",");
							
			for(int i=0; i<words.length; i++)
			{
                            /* Get correspondence between old and new numbers */
                            name = originalCorrespondence.get(i);
                            newNum = newCorrespondence.get(name);
                            instance[newNum] = words[i];
			}
							
			inst = "";
			for(int i=0; i<instance.length; i++)
			{
                            inst = inst + instance[i] + ",";
			}
			inst = inst + ";"; //Used to remove the last ","
			inst = inst.replace(",;", "");
			pwARFF.println(inst);
                        
                        while((line=br.readLine()) != null){
                            if(line.equals("") == false)
                            {
                                instance = new String[attributes.size() + labels.size()];

				words = line.split(",");
									
				for(int i=0; i<words.length; i++)
				{
                                    /* Get correspondence between old and new */
                                    name = originalCorrespondence.get(i);
                                    newNum = newCorrespondence.get(name);
                                    instance[newNum] = words[i];
				}
									
				inst = "";
				for(int i=0; i<instance.length; i++)
				{
                                    inst = inst + instance[i] + ",";
				}
				inst = inst + ";";//Used to remove the last ","
                                inst = inst.replace(",;", "");
				pwARFF.println(inst);
                            }
                        }
                    }
                }
            }
        }
	catch(IOException | NumberFormatException e1)
	{
            e1.printStackTrace();
	}
        finally
        {
            try{
                if(outARFF != null){
                    outARFF.close();
                }
                if(frIn != null){
                    frIn.close();
                }
            } catch(Exception e2){
                e2.printStackTrace();
            }
        }
    }
	
    
    public boolean isLabel(String s){
		
        for(int i=0; i<nLabels; i++)
	{
            if(s.equals(labelNames[i].replace("\'", "\\\'")))
            {
                return true;
            }
        }
        
	return false;
    }

    
    public String getAttributeName(String s)
    {
        //Remove '@attribute' -> 10 caracteres
        s = s.substring(10);

        //Dejamos la cadena sin espacios al principio y al final
        s = s.trim();
        
        int init = 0;
	int end = s.length();
	boolean quotes = false;
        
        for(int i=0; i<s.length(); i++)
	{
            if(s.charAt(i) == '\\')
            {
                //If scape symbol, go to next character
                i++;
            }
            else if((s.charAt(i) == '\'') && (quotes == false))
            {
                //First quote
                init = i;
                quotes = true;
            }
            else if((s.charAt(i) == '\'') && (quotes == true))
            {
		//Second quote
                end = i;
		break; //Salimos del for
            }
	}
		
	if(quotes == true)
        {
            s = s.substring(init+1, end);
        } 
	else
        {
            s = s.split(" ")[0];
        }
		
	return s;
    }
	
	
    public String getAttributeType(String s)
    {
        String [] word;
		
	if (s.contains("{"))
	{
            return s.substring(s.indexOf("{"), s.indexOf("}")+1);
	}
	else
        {
            word = s.split(" ");			
            return word[word.length - 1];
	}
    }
}
