package conversion;


import java.io.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class MulanToMeka {

	/* Nombre de la relacion */
	String relationName = new String();
	
	/* Numero de etiquetas */
	int nLabels = 0;
	
	/* Vector con los atributos del dataset */
	Vector<Attribute> atributos = new Vector<Attribute>();
	
	/* Vector con las etiquetas del dataset */
	Vector<Attribute> labels = new Vector<Attribute>();
	
	/* Array que almacena unicamente los nombres de las distintas etiquetas */
	String [] nombreLabels;
	
	/* Valor booleano que indica si las instancias se encuentran o no en el formato reducido de arff */
	boolean formatoReducido;
	
	/* Correspondencias entre el orden de los atributos del fichero original y del nuevo */
	Hashtable<Integer, String> correspOriginal = new Hashtable<Integer, String>();
	Hashtable<String, Integer> correspNueva = new Hashtable<String, Integer>();
	
	
	public void convertir(String nombreFicheroMulan, String nombreFicheroMeka)
	{
		/* Declaracion de los ficheros */
		FileReader frEntrada = null;
		BufferedReader br = null;
		FileWriter salidaARFF = null;
		PrintWriter pwARFF = null;
		
		TreeMap<Integer, String> instanciaMap = new TreeMap<Integer, String>();
		
		int n=0;

		
		/* 
		 * 	Obtener las clases del fichero xml 
		 */
		try{
			File fXmlFile = new File(nombreFicheroMulan + ".xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("label");
			
			nLabels = nList.getLength();
			nombreLabels = new String[nLabels];
			
			for (int i = 0; i < nLabels; i++) {
				 
				Node nNode = nList.item(i);
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) 
				{		 
					Element eElement = (Element) nNode;				
					nombreLabels[i] = eElement.getAttribute("name");
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		try{
			frEntrada = new FileReader(nombreFicheroMulan + ".arff");
			br = new BufferedReader(frEntrada);			
			salidaARFF = new FileWriter(nombreFicheroMeka + ".arff");
			pwARFF = new PrintWriter(salidaARFF);
			
			String linea = new String();
			String [] palabra;
			
			while((linea=br.readLine()) != null){
				
				palabra = linea.split(" ");
				
				if(palabra[0].toLowerCase().equals("@relation"))
				{
					relationName = nombreFicheroMeka;

					System.out.println("Labels: " + nLabels);
					
					pwARFF.println("@relation '" + relationName + ": -C -" + nLabels + "'");
					pwARFF.println();
				}
				else if(palabra[0].toLowerCase().equals("@attribute"))
				{
					Attribute atr = new Attribute();

					atr.name = obtenerNombreAtributo(linea);
					atr.type = obtenerTipoAtributo(linea);
					
					/* Almacenamos el numero de atributo en el fichero original */
					correspOriginal.put(n, obtenerNombreAtributo(linea));
					
					if(isLabel(atr.name)){
						labels.addElement(atr);
					}
					else{	
						atributos.addElement(atr);
					}
					
					n++;
				}
				else if(palabra[0].toLowerCase().equals("@data"))
				{
					n = 0; 
					
					Attribute a = new Attribute();
					/* Imprimimos atributos y labels */
					for(int i=0; i<atributos.size(); i++)
					{
						a = atributos.get(i);
						
						if (a.name.indexOf(" ") != -1){
							pwARFF.println("@attribute " + "\'" + a.name + "\'" + " " + a.type);
						}
						else
							pwARFF.println("@attribute " + a.name + " " + a.type);
						
						/*Almacenamos el numero de atributo en el nuevo fichero */
						correspNueva.put(a.name, n);
						n++;
					}
					for(int i=0; i<nLabels; i++)
					{
						a = labels.get(i);
						
						if (a.name.indexOf(" ") != -1)
						{
							//Si tiene espacios el name
							pwARFF.println("@attribute " + "\'" + a.name + "\'" + " " + "{0,1}");
						}
						else
							pwARFF.println("@attribute " + a.name + " " + "{0,1}");
						
						/*Almacenamos el numero de atributo en el nuevo fichero */
						correspNueva.put(a.name, n);
						n++;
					}
					
					pwARFF.println();
					pwARFF.println(linea);
					
					int nuevoNum;
					String name;
					String inst = new String();
					
					linea = br.readLine();
					while(linea.equals("") == true)
					{
						linea = br.readLine();
					}
					
					if(linea.charAt(0) == '{')
					{
						/*
						 * Las instancias del fichero arff se encuentra en formato reducido
						 * 		Se indican el numero de instancia y su valor
						 */
						formatoReducido = true;

						linea = linea.replace("{", "");
						linea = linea.replace("}", "");
						palabra = linea.split(",");
							
						inst = "{";
						instanciaMap.clear();
						for(int i=0; i<palabra.length; i++)
						{
							/* Obtenemos la correspondencia entre el numero original y el nuevo */
							palabra[i] = palabra[i].trim();
							name = correspOriginal.get(Integer.parseInt(palabra[i].split(" ")[0]));
							nuevoNum = correspNueva.get(name);
							instanciaMap.put(nuevoNum, palabra[i].split(" ")[1]);
						}
							
						Iterator iterator = instanciaMap.keySet().iterator();
						while (iterator.hasNext()) {
							Object key = iterator.next();
							inst = inst + key + " " + instanciaMap.get(key) + ",";
						}
							
						inst = inst + "}";
						inst = inst.replaceAll(",}", "}");
						pwARFF.println(inst);
							
						while((linea=br.readLine()) != null){
							if(linea.equals("") == false)
							{
								linea = linea.replace("{", "");
								linea = linea.replace("}", "");
								palabra = linea.split(",");
									
								inst = "{";
								instanciaMap.clear();
								for(int i=0; i<palabra.length; i++)
								{
									/* Obtenemos la correspondencia entre el numero original y el nuevo */
									palabra[i] = palabra[i].trim();
									name = correspOriginal.get(Integer.parseInt(palabra[i].split(" ")[0]));
									nuevoNum = correspNueva.get(name);
									instanciaMap.put(nuevoNum, palabra[i].split(" ")[1]);
								}
									
								Iterator iterator2 = instanciaMap.keySet().iterator();
								while (iterator2.hasNext()) {
									Object key = iterator2.next();
									inst = inst + key + " " + instanciaMap.get(key) + ",";
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
						 * Las instancias del fichero arff se encuentra en formato completo
						 * 		Se indica el valor de cada uno de los atributos, en orden
						 */
						formatoReducido = false;
							
						String [] instancia = new String[atributos.size() + labels.size()];

						palabra = linea.split(",");
							
						for(int i=0; i<palabra.length; i++)
						{
							/* Obtenemos la correspondencia entre el numero original y el nuevo */
							name = correspOriginal.get(i);
							nuevoNum = correspNueva.get(name);
							instancia[nuevoNum] = palabra[i];
						}
							
						inst = "";
						for(int i=0; i<instancia.length; i++)
						{
								inst = inst + instancia[i] + ",";
						}
						inst = inst + ";";//Lo usaremos simplemente para elminiar la ultima ","
						inst = inst.replace(",;", "");
						pwARFF.println(inst);
							
						while((linea=br.readLine()) != null){
							if(linea.equals("") == false)
							{
								instancia = new String[atributos.size() + labels.size()];

								palabra = linea.split(",");
									
								for(int i=0; i<palabra.length; i++)
								{
									/* Obtenemos la correspondencia entre el numero original y el nuevo */
									name = correspOriginal.get(i);
									nuevoNum = correspNueva.get(name);
									instancia[nuevoNum] = palabra[i];
								}
									
								inst = "";
								for(int i=0; i<instancia.length; i++)
								{
									inst = inst + instancia[i] + ",";
								}
								inst = inst + ";";//Lo usaremos simplemente para elminiar la ultima ","
								inst = inst.replace(",;", "");
								pwARFF.println(inst);
							}
						}
					}							
				}
			}
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
		finally
		{
			try{
				if(salidaARFF != null)
					salidaARFF.close();
				if(frEntrada != null)
					frEntrada.close();
			}
			catch(Exception e2){
				e2.printStackTrace();
			}
		}
		
		System.out.println("End");
		
	}
	
	public boolean isLabel(String s){
		
		for(int i=0; i<nLabels; i++)
		{
			if(s.equals(nombreLabels[i].replace("\'", "\\\'")))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public String obtenerNombreAtributo(String s)
	{
		//Quitamos primero '@attribute' -> Son 10 caracteres
		s = s.substring(10);
		
		//Dejamos la cadena sin espacios al principio y al final
		s = s.trim();
		
		int inicio = 0;
		int fin = s.length();
		boolean comillas = false;
		
		for(int i=0; i<s.length(); i++)
		{
			if(s.charAt(i) == '\\')
			{
				//Si es el simbolo de escape, saltamos el siguiente caracter
				i++;
			}
			else if((s.charAt(i) == '\'') && (comillas == false))
			{
				//Si encontramos una primera comilla
				//El inicio del name del atributo sera aqui
				inicio = i;
				comillas = true;
			}
			else if((s.charAt(i) == '\'') && (comillas == true))
			{
				//Si encontramos una segunda comilla
				//El final del name de atributo esta aqui
				fin = i;
				break; //Salimos del for
			}
		}
		
		if(comillas == true)
			s = s.substring(inicio+1, fin); 
		else
			s = s.split(" ")[0];
		
		return s;
	}
	
	
	public String obtenerTipoAtributo(String s)
	{
		String [] palabra;
		
		if (s.indexOf("{") != -1)
		{
			return s.substring(s.indexOf("{"), s.indexOf("}")+1);
		}
		else
		{
			palabra = s.split(" ");			
			return palabra[palabra.length - 1];
		}
	}
}
