package convertir;


import java.io.*;
import java.util.*;

public class MekaToMulan {

	String relationName = new String();
	int labels = 0;
	int c = 0;
	Vector<Atributo> atributos = new Vector<Atributo>();
	
	public void convertir (String nombreFicheroMeka, String nombreFicheroMulan)
	{
		FileReader frEntrada = null;
		BufferedReader br = null;
		FileWriter salidaARFF = null;
		PrintWriter pwARFF = null;
		FileWriter salidaXML = null;
		PrintWriter pwXML = null;
		
		try{
			frEntrada = new FileReader(nombreFicheroMeka + ".arff");
			br = new BufferedReader(frEntrada);
			
			salidaARFF = new FileWriter(nombreFicheroMulan + ".arff");
			pwARFF = new PrintWriter(salidaARFF);
			salidaXML = new FileWriter(nombreFicheroMulan + ".xml");
			pwXML = new PrintWriter(salidaXML);
			
			String linea = new String();
			String [] palabra;
			
			while((linea=br.readLine()) != null)
			{
				palabra = linea.split(" ");
				
				if(palabra[0].toLowerCase().equals("@relation"))
				{
					palabra = linea.split("-C");
					relationName = nombreFicheroMulan;

					palabra = palabra[1].split("[^0-9\\-]");
					
					c = Integer.parseInt(palabra[1]);
					labels = Math.abs(c);
					System.out.println("Labels: " + labels);
					
					pwARFF.println("@relation " + relationName);
					pwARFF.println();
				}
				else if(palabra[0].toLowerCase().equals("@attribute"))
				{
					Atributo atr = new Atributo();
					
					atr.nombre = obtenerNombreAtributo(linea);
					atr.tipo = obtenerTipoAtributo(linea);
					
					atributos.addElement(atr);
					
					pwARFF.println(linea);
				}
				else if(palabra[0].toLowerCase().equals("@data"))
				{
					pwARFF.println();
					pwARFF.println(linea);
					
					while((linea=br.readLine()) != null)
						pwARFF.println(linea);
				}
			}
			
			/*
			 * Creacion del fichero XML
			 */
			
			pwXML.println("<?xml version=\"1.0\" ?>");
			pwXML.println("<labels xmlns=\"http://mulan.sourceforge.net/labels\">");
			
			Atributo a = new Atributo();
			
			if(c > 0)
			{
				for(int i=0; i<labels; i++)
				{
					a = atributos.get(i);
					/* El caracter de escape para replaceAll es \\
					 * 		Asi que reemplazamos \' por '
					 */
					pwXML.println("<label name=\"" + a.nombre.replaceAll("\\\\\'", "\\'") + "\"> </label>");
				}
			}
			else if(c < 0)
			{
				int x = atributos.size();
				for(int i=(x-labels); i<x; i++)
				{
					a = atributos.get(i);
					pwXML.println("<label name=\"" + a.nombre.replaceAll("\\\\\'", "\\'") + "\"> </label>");
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
				if(salidaARFF != null)
					salidaARFF.close();
				if(salidaXML != null)
					salidaXML.close();
				if(frEntrada != null)
					frEntrada.close();
			}
			catch(Exception e2){
				e2.printStackTrace();
			}
		}
		
		System.out.println("End");
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
				//El inicio del nombre del atributo sera aqui
				inicio = i;
				comillas = true;
			}
			else if((s.charAt(i) == '\'') && (comillas == true))
			{
				//Si encontramos una segunda comilla
				//El final del nombre de atributo esta aqui
				fin = i;
				break; //Salimos del for
			}
		}
		
		if(comillas == true)
		{	
			s = s.substring(inicio+1, fin); 
		}
		else
			s = s.split(" ")[0];
		
		return s;
	}
	
	
	public String obtenerTipoAtributo(String s)
	{
		String [] palabra;
		
		palabra = s.split(" ");
		
		return palabra[palabra.length - 1];
	}
}
