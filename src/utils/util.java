package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import mldc.attributes.*;
import mldc.base.MLDataMetric;
import mldc.imbalance.*;
import mldc.labelsDistribution.*;
import mldc.labelsRelation.*;
import mldc.size.*;

import mulan.data.InvalidDataFormatException;
import mulan.data.LabelsPair;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;
import mulan.data.UnconditionalChiSquareIdentifier;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;


/**
 * 
 * @author Jose Maria Moyano Murillo
 */
public class util {          
    
    public  static int maxKey (HashMap<Integer,Integer> hm)
    {
        Set<Integer> keys= hm.keySet();

        int max = 0;

        for(int current : keys)
        {
            if(max<current) {
                max = current;
            }
        }

        return max;
    }
    
    
    public static boolean existsValue (double[] visited , double current)
    {
        for(int i=0; i<visited.length;i++)
        {
            if(visited[i]==current){
                return true;
            }
        }
        
        return false;
    }

     
    public static double getQ1(double[] orderedArray)
    {
        int q = orderedArray.length/4;
       
        if(orderedArray.length %4 ==0){
            return orderedArray[q-1];
        }
        else{
            return orderedArray[q];
        }
    }

    
    public static double getQ3(double[] orderedArray)
    {
        int q = 3*(orderedArray.length/4);
        
        if(orderedArray.length %4 ==0){
            return orderedArray[q-1];
        }
        else{
            return orderedArray[q];
        }
    }
    
     
    public static double getMedian(double[] sortedArray)
    {
        int mean = sortedArray.length/2;
           
        if( sortedArray.length %2 !=0 ) {
            return sortedArray[mean];
        }
        else
        {
            double value1 = sortedArray[mean-1];
            double value2 = sortedArray[mean];
            return (value1+value2)/2;
        }
    }
        

    public static ImbalancedFeature getMax(ArrayList<ImbalancedFeature> list)
    {
        ImbalancedFeature max = list.get(0);
               
        for(ImbalancedFeature current : list)
        {
            if(current.getAppearances()>max.getAppearances()) {
                max = current;
            }
        }
            
        return max;       
    }
     

    public static ImbalancedFeature getMin( ArrayList<ImbalancedFeature> list)
    {
        ImbalancedFeature min = list.get(0);
               
        for(ImbalancedFeature current : list)
        {
            if(current.getAppearances()<min.getAppearances()) {
                min = current;
            }
        }
            
        return min;       
    }
    

    public static Object[] getValuesByRow(int rowNumber, double[][] coefficients, String rowName )
    {
        Object[] row = new Object[coefficients.length+1];
        String truncate;
        
        for(int i=0; i<row.length;i++)
        {       
            if(i==0)
            {              
                row[i]= rowName;
                continue;
            }
          
            if(coefficients[i-1][rowNumber]==-1.0)
            {
                row[i]= " ";
                continue;
            }
            
            if(i-1 != rowNumber)
            {
                truncate = Double.toString(coefficients[i-1][rowNumber]);
                row[i] = MetricUtils.truncateValue(truncate, 4);
            }                    
            else if (i-1==rowNumber) {
                row[i]= "---"; 
            }
        }
        
        return row;
    }
    

    public static int getMax(int [] v)
    {        
        int max = Integer.MIN_VALUE;
        
        for(int i=0; i<v.length; i++){
            if(v[i] > max){
                max = v[i];
            }
        }
        
        return max;
    }
    
    
    public static int getMin(int [] v)
    {        
        int min = Integer.MAX_VALUE;
        
        for(int i=0; i<v.length; i++){
            if(v[i] < min){
                min = v[i];
            }
        }
        
        return min;
    }
    
    
    public static double[][] initializeNegativeValuesMatrix(int length)
    {
        double[][] data = new double[length][length];
         
        for(int i=0;i<length;i++){
            for(int j=0;j<length;j++){
            data[i][j]=-1.0; 
           }     
        }
            
        return data;
    }
    

    //---------------------------------------------------------------------------------------------------
    
    
    public static boolean Esta_dataset (ArrayList<MultiLabelInstances> list_dataset, String dataset_name)
    {
        for(MultiLabelInstances current : list_dataset)
              if(current.getDataSet().relationName().equals(dataset_name)) return true;
        
        return false;
    }
    
    public static double[] get_ir_values_intra_class(ImbalancedFeature[] label_imbalanced)
    {
        double[] result = new double[label_imbalanced.length];
        
        for(int i=0; i<label_imbalanced.length; i++)
            result[i]=label_imbalanced[i].getIRIntraClass();
        
        return result;    
    
    }
    
    public static double[] get_ir_values_inter_class(ImbalancedFeature[] label_freq)//se le pasa el arreglo ordenado de mayor a menor
    {
        
        ImbalancedFeature[] label_freq_sorted = MetricUtils.sortByFrequency(label_freq);
        
        double[] ir_inter_class = new double[label_freq.length];
        
        int mayor= label_freq_sorted[0].getAppearances();
        double value;
        
        double media=0;
        
        for(int i=0;i<label_freq_sorted.length; i++)
        {
            if(label_freq_sorted[i].getAppearances() <= 0){
                value = Double.NaN;
            }
            else{
                value = mayor/(label_freq_sorted[i].getAppearances()*1.0);
            }
            
            ir_inter_class[i] = value;
            //System.out.println(" Atributo "+i+" , "+value);
            
            media+= value;
        }
        media = media/label_freq_sorted.length;
                
       //System.out.println("Media es "+ media);
        
        return ir_inter_class;
    }
    
    private static int get_frequency_x_label_name(ImbalancedFeature[] lista_attr_freq ,String label_name)
    {
        for(int i=0 ; i<lista_attr_freq.length; i++)
        {
            if(lista_attr_freq[i].getName().equals(label_name)) return lista_attr_freq[i].getAppearances();
        }
        return -1;//es que no aparece el nombre de la etiqueta
    }
    
    
    private static ArrayList<AttributesPair> conforma_pares_atributos(int [] pares_etiquetas,int[] label_indices,MultiLabelInstances dataset )
    {
      ArrayList<AttributesPair> mi_lista= new ArrayList<>();
      Instances Instancias = dataset.getDataSet();
      
      String[] name_attr =DataInfoUtils.getLabelNames(label_indices, Instancias); //NOMBRES DE LAS ETIQUETAS
      AttributesPair actual;
      
      ImbalancedFeature[] lista_attr_freq = MetricUtils.getImbalancedDataByAppearances(dataset);
      int cant_i, cant_j;
      
      int index_pares_etiq=0; //indice del arreglo pares_etiquetas
      int value;
                
      for(int i = 0; i<label_indices.length; i++)
      {
          for(int j=i+1 ; j<label_indices.length ; j++)
          {
            value = pares_etiquetas[index_pares_etiq];
            
            index_pares_etiq++; 
              
            if(value ==0) continue; 

            cant_i = get_frequency_x_label_name(lista_attr_freq, name_attr[i]);
            cant_j = get_frequency_x_label_name(lista_attr_freq, name_attr[j]);
            
            actual = new AttributesPair(name_attr[i], name_attr[j],value,i,j,cant_i,cant_j);
            mi_lista.add(actual);
                      
          }
      }
     return mi_lista;
    }
    
    public static int get_indice_pares_etiquetas(int ind_1, int ind_2, int labels)
    {
        int count = ind_1;
        int result=0;
        
        for(int i=1; count>0 ;i++ , count--)
        {
            result += labels-i;
        }
        int distancia = ind_2 - ind_1;
        
        return result+= distancia-1;
    }
    
    //ACTUALIZA LAS FRECUENCIAS DE LAS TUPLAS DE ETIQUETAS QUE APAREZCAN RELEVANTES EN LA N-ESIMA POSICION
    public static int[] Actualiza_Pares_etiquetas(int[] pares_etiquetas, int[] labels_value)
    {
        int index;
        
        for(int i=0; i<labels_value.length;i++)
        {
            if(labels_value[i]== 0) continue;
            
            for(int j=i+1; j<labels_value.length; j++)
            {
               if(labels_value[i] == labels_value[j])
               {
                   index = get_indice_pares_etiquetas(i, j, labels_value.length);
                   pares_etiquetas[index]=pares_etiquetas[index]+1;
               }
            }
        }
        
        return pares_etiquetas;
    }
    
    //DEVUELVE EN LA n-ESIMA INSTANCIA EL CONJUNTO DE VALORES DE ETIQUETAS
    public static int[] get_current_value_labels(Instances Instancias, int posicion, int[] labels_indice)
    {
        int[] value_labels = new int[labels_indice.length];
        int value;
        
        for(int i=0; i<value_labels.length; i++)
        {
           value = (int)Instancias.instance(posicion).value(labels_indice[i]);
           value_labels[i] = value;
        }
        return value_labels;
    }
    
    
    public static double[][] Get_pares_seleccionados (String[] labelname, ArrayList<AttributesPair> pares_seleccionados, int num_instances)
    {
        //LANZAR LA VENTANA EMERGENTE CON LOS PARE SELECCIONADOS
       
       double[][] pares_freq= initializeNegativeValuesMatrix(labelname.length);
       
       AttributesPair current=null;
       
       for(int i=0; i<labelname.length;i++)
       {
           for(int j=0;j<labelname.length;j++)
           {
               if(i==j || i>j ) continue;
               
               current = Search_and_get(labelname[i],labelname[j],pares_seleccionados);
               
               if(current !=null) 
               {
                 pares_freq[i][j]=current.getAppearances()/(num_instances*1.0);
               }
               //current =null;
           }
           
       }
       
       return pares_freq;
    }
    
    
    public static AttributesPair Search_and_get(String par1, String par2,ArrayList<AttributesPair> lista )
    {
      for(AttributesPair current : lista)
      {
          if(par1.equals(current.getAttributeName1()) && par2.equals(current.getAttributeName2())) return current;
          else if (par2.equals(current.getAttributeName1()) && par1.equals(current.getAttributeName2())) return current;
      }
      return null;
    }
    
    public static int get_valor_max_entre_pares_atrr (ArrayList<AttributesPair> lista)
    {
        int mayor=0;
        
        for(AttributesPair current : lista)
            if(mayor<current.getAppearances()) mayor=(int)current.getAppearances();
        
        return mayor;        
    }
    
    public static int get_velocidad(MultiLabelInstances dataset)
    {            
        int cant_label = dataset.getNumLabels();
        int instancias = dataset.getNumInstances();
        
        if(cant_label<50 && instancias<2500) return 25;
        if(cant_label<200 && instancias<2500) return 10;
        
        
        return 2;
    }
    
    public static int get_valor_min_entre_pares_atrr (ArrayList<AttributesPair> lista)
    {
        int min=(int)lista.get(0).getAppearances();
        
        for(AttributesPair current : lista)
            if(min >current.getAppearances()) min=(int)current.getAppearances();
        
        return min;        
    }
    
    public static String[] pasa_valores_al_arreglo(ArrayList<String> lista)
    {
        String[] result= new String[lista.size()];
        
        for( int i=0; i<result.length ;i++)
        {
            result[i]=lista.get(i);
        }
            
        return result;
    }
    
    public static int get_valor_fortaleza (int minimo, int maximo, int n, double edge_value)
    {
        double intervalo = (maximo-minimo)/(n*1.0);
        
        int valor_fort=0;
        
        for(double i=minimo; i<maximo ;i=i+intervalo)
        {
            if(edge_value < i) break;
            valor_fort++;
        }
        return valor_fort;
    }
    /*
    public static int[] coordenadas_jgraphx(int x, int y, int count)
    {
        int[] posicion=new int[2];
        
        
    }
    */
    
    public static int get_valor_fortaleza (int minimo, int maximo, int n, int edge_value)
    {
        int intervalo = (maximo-minimo)/n;
        
        int valor_fort=0;
        
        for(int i=minimo; i<maximo ;i=i+intervalo+1)
        {
            if(edge_value < i) break;
            valor_fort++;
        }
        return valor_fort;
    }
    
    
    private static AttributesPair Devuelve_el_par(String att1, String att2, ArrayList<AttributesPair> lista)
    {
        for( AttributesPair current : lista)
        {
            if(current.getAttributeName1().equals(att1) && current.getAttributeName2().equals(att2))return current;
            if(current.getAttributeName1().equals(att2) && current.getAttributeName2().equals(att1))return current;
        }
        return null;
    }
    
    
    public static ArrayList<AttributesPair> Encuentra_pares_attr_seleccionados (ArrayList<AttributesPair> pares_label , ArrayList<String> labels)
    {
       ArrayList<AttributesPair> result = new ArrayList();
       
       AttributesPair current;
       
       for(int i=0; i<labels.size()-1; i++)
       {
           for(int j=i+1; j<labels.size(); j++)
           {
               current = Devuelve_el_par(labels.get(i), labels.get(j), pares_label);
               if(current!=null){
                   result.add(current);
               }
           }
       }    
       
        return result;
    }
    
    public static ArrayList<AttributesPair> Get_pares_atributos(MultiLabelInstances dataset)
    {       
        Instances Instancias = dataset.getDataSet();
        
        //CREO EL ARREGLO DONDE SE GUARDA LA CANT VECES QUE SE REPITE UN PAR, CADA POSICION ES UN PAR TAL QUE
        //EL TAMAÑO DEL ARREGLO ES LA CANTIDAD DE COMBINACIONES POSIBLES ENTRE PARES DE ETIQUETAS
       
        int comb_posibles = get_comb_posibles(dataset.getNumLabels());//devuelve las combinaciones posibles entre pares de etiquetas 
        
        int [] pares_etiquetas = new int[comb_posibles]; // se usa para guardar la cantidad de veces que ocurre un par de atributos entre si, 
        int [] current_labels_value;
        int[] label_indices= dataset.getLabelIndices();
                
        for(int i=0; i<Instancias.size(); i++)
        {
            current_labels_value = get_current_value_labels(Instancias, i, label_indices); // obtiene los valores de cada etiqueta
           pares_etiquetas = Actualiza_Pares_etiquetas(pares_etiquetas, current_labels_value); //actualiza los pares de etiquetas
        }            
        
        return conforma_pares_atributos(pares_etiquetas, label_indices, dataset);
    }
    
        public static int get_comb_posibles(int n)
        {
	 int result=0;
         
         for(int i=n-1; i>0; i--)
             result +=i;
         
         return result;
        }
    
    //-----------------------------------------------------------------------------------------------------------
    
  
    public static String Get_file_name_xml1(String path)
    {           
        for(int i=path.length()-1;i>0;i--)
        {
            if (path.charAt(i)=='\\')break;
        }
        return path;
    }
    
    public static String Get_file_name_xml(String path)
    {           
//        for(int i=path.length()-1;i>0;i--)
//        {
//            if(path.charAt(i)=='_')
//                return path.substring(0,i)+".xml";
//            
//            if (path.charAt(i)=='\\')break;
//        }
        
        String [] words;
        words = path.split("/");
       //System.out.println("words1: " + Arrays.toString(words));
        path = words[words.length-1];
        String dir = new String();
        if(words.length > 1){
            for(int i=0; i<words.length-1; i++){
                dir += words[i] + "/";
            }
        }
       //System.out.println("dir1: " + dir);
       //System.out.println("path1: " + path);
        /*words = path.split("-");
        if(words.length > 1){
           //System.out.println("words2: " + Arrays.toString(words));
            path = "";
            for(int i=0; i<words.length-1; i++){
                path += words[i]+"-";
            }
            path = path.substring(0, path.length()-1) + ".xml";
        }
        else{
           //System.out.println("path2: " + path);
            path = path.split("\\.")[0];
           //System.out.println("path3: " + path);
            path+=".xml";
        }
        */
       //System.out.println("path4: " + path);
        path = dir+path;
       //System.out.println("path5: " + path);
        
        return path;
    }
    
    public static boolean hasMoreNDigits(double d, int digits)
    {
    
        String text = Double.toString(Math.abs(d));
        int integerPlaces = text.indexOf('.');
        int decimalPlaces = text.length() - integerPlaces - 1;
        
        if(decimalPlaces<=digits) return false;
        return true;
    }
    
    
        
    
    public static int Label_value_most_frequency(HashMap labels)
    {
       Iterator it = labels.entrySet().iterator();
        int mayor=0;
        int temp;
    
        while (it.hasNext())
        {
           Map.Entry current = (Map.Entry)it.next();
        
            temp=(int)current.getValue();
            
            if(mayor<temp) mayor=temp;
        }
      
        
        return mayor;
    }
    
    
   	public static String Separate_data(String results)
	{
		String resultados="";
		
		 char simbolo = ':';
		 boolean es_numero=false;
		 
		 int pos_inicial=0;
		 int pos_final;
		 char actual;
		 
		 String temp;
		 
		 for(int i=0; i < results.length(); i++)
		 {
			 actual = results.charAt(i);
			 
                        // if(actual==' ')continue;
                         
			 if(actual==simbolo){ es_numero=true; continue;	 }
			 
			 if(es_numero && !is_number(actual) && actual==' ' && is_number(results.charAt(i-1)))
			 {
				 es_numero=false;
				 pos_final=i;
				 temp = results.substring(pos_inicial,pos_final);
				 pos_inicial=pos_final;
				 resultados+="\n"+temp;
			 }
		 }
		 return resultados;
	}
	

	public static boolean is_number(char valor)
	{
		 String numero_aceptado="012,3456789.±";
		 
		 for(int i=0;i<numero_aceptado.length();i++)
		 {
			 if(valor == numero_aceptado.charAt(i))return true;
		 }
		 return false;
	}
        
        public static boolean is_number(String cadena)
        {
            if(cadena.isEmpty()) return false;
            
            cadena = cadena.toLowerCase().trim();
            
            String alfabeto ="abcdefghijklmnopqrstuvwxyz";
            
            char current;
            for(int i=0;i<cadena.length();i++)
            {
                current = alfabeto.charAt(i);
                
                if( cadena.indexOf(current)!= -1) return false;
            }
            return true;
        }
        
        
        
        public static String getValueFormatted(String name, String value){
            String formattedValue = new String();

            value = value.replace(",", ".");

            if(value.equals("-")){
                return value;
            }

            if(value.equals("NaN")){
                return "---";
            }


            //Scientific notation numbers
            if( (((Math.abs(Double.parseDouble(value)*1000) < 1.0)) && 
                        ((Math.abs(Double.parseDouble(value)*1000) > 0.0))) ||
                    (Math.abs(Double.parseDouble(value)/1000.0) > 10)){
                NumberFormat formatter = new DecimalFormat("0.###E0");
                formattedValue = formatter.format(Double.parseDouble(value));
            }
            //Integer numbers
            else if( (name.toLowerCase().equals("attributes")) 
                    || (name.toLowerCase().equals("bound")) 
                    || (name.toLowerCase().equals("distinct labelsets"))
                    || (name.toLowerCase().equals("instances"))
                    || (name.toLowerCase().equals("labels x instances x features"))
                    || (name.toLowerCase().equals("labels")) 
                    || (name.toLowerCase().equals("number of binary attributes"))
                    || (name.toLowerCase().equals("number of labelsets up to 2 examples"))
                    || (name.toLowerCase().equals("number of labelsets up to 5 examples"))
                    || (name.toLowerCase().equals("number of labelsets up to 10 examples"))
                    || (name.toLowerCase().equals("number of labelsets up to 50 examples"))
                    || (name.toLowerCase().equals("number of nominal attributes"))
                    || (name.toLowerCase().equals("number of numeric attributes"))
                    || (name.toLowerCase().equals("number of unqiue labelsets"))
                    || (name.toLowerCase().equals("number of unconditionally dependent label pairs by chi-square test"))){

                NumberFormat formatter = new DecimalFormat("#0"); 
                formattedValue = formatter.format(Double.parseDouble(value));
            }
            //Decimal numbers
            else{
                NumberFormat formatter = new DecimalFormat("#0.000"); 
                formattedValue = formatter.format(Double.parseDouble(value));
            } 

            formattedValue = formattedValue.replace(",", ".");
            return formattedValue;
        }
        
        
        public static String getValueFormatted(String value, int nDecimals){
            String formattedValue = new String();

            value = value.replace(",", ".");

            if(value.equals("-")){
                return value;
            }

            if(value.equals("NaN")){
                return "---";
            }

            //Scientific notation numbers
            if( (((Math.abs(Double.parseDouble(value)*1000) < 1.0)) && 
                        ((Math.abs(Double.parseDouble(value)*1000) > 0.0))) ||
                    (Math.abs(Double.parseDouble(value)/1000.0) > 10)){
                NumberFormat formatter = new DecimalFormat("0.###E0");
                formattedValue = formatter.format(Double.parseDouble(value));
            }
            //Decimal numbers
            else{
                String f = "#0.";
                for(int i=0; i<nDecimals; i++){
                    f += "0";
                }
                
                NumberFormat formatter = new DecimalFormat(f); 
                formattedValue = formatter.format(Double.parseDouble(value));
            } 

            formattedValue = formattedValue.replace(",", ".");
            return formattedValue;
        }
        
        public static double[][] calculateCoocurrences(MultiLabelInstances mldata){
            
            int nLabels = mldata.getNumLabels();
            Instances data = mldata.getDataSet();
            
            double [][] coocurrenceMatrix = new double[nLabels][nLabels];
            
            int nFeatures = data.numAttributes() - nLabels;
            
            int [] labelIndices = mldata.getLabelIndices();
            
            Instance temp = null;
            for(int k=0; k<data.numInstances(); k++){   
                temp = data.instance(k);
                
                for(int i=0; i<nLabels; i++){
                    for(int j=i+1; j<nLabels; j++){
                        //if(i!=j){
                          if((temp.value(labelIndices[i]) == 1.0) && (temp.value(labelIndices[j]) == 1.0)){
                            coocurrenceMatrix[i][j]++;
                          }  
                        //}
                    }
                }
            }
            
            return coocurrenceMatrix;
        }
        
        
        
        
        
}
