/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metrics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Double.NaN;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import mulan.data.InvalidDataFormatException;
import mulan.data.LabelSet;
import mulan.data.LabelsPair;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;
import mulan.data.UnconditionalChiSquareIdentifier;
import mulan.data.generation.DataSetBuilder;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import weka.core.Attribute;
import weka.core.Instances;

/**
 *
 * @author osc
 */
public class util {
    
    
    public static String[] Get_labels_from_line(ArrayList<String> label_lines)
    {
        String[] labels = new String[label_lines.size()];
        
        for(int i=0;i<label_lines.size(); i++)
            labels[i]= Get_label_from_line(label_lines.get(i));
        
        return labels;
    }
    
    private static String Get_label_from_line(String line)
    {
        String label="";
        boolean flag_space=false;
        char current;
        for(int i=0;i<line.length();i++)
        {
            current =line.charAt(i);
            
            if(current==' '&& !flag_space) {flag_space=true;continue;}
            if(current==' '&& flag_space) {break;}
            
            if(flag_space){label+=line.charAt(i); continue;}
        }
        return label;
    }
    
    public static void Write_into_xml_file( PrintWriter wr , String[] labels )
    {
        //<?xml version="1.0" encoding="utf-8"?>
        String imprimir = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    
        wr.write(imprimir);//escribe en el fichero
        wr.write(System.getProperty("line.separator"));// escribe el salto de linea
        
        //<labels xmlns="http://mulan.sourceforge.net/labels">
        imprimir = "<labels xmlns=\"http://mulan.sourceforge.net/labels\">";
        
        wr.write(imprimir);//escribe en el fichero
        wr.write(System.getProperty("line.separator"));// escribe el salto de linea
        
        for(int i=0; i<labels.length;i++)
        {
            //<label name="label-current"></label>
            imprimir = "<label name=\""+labels[i]+"\"></label>";
            wr.write(imprimir);//escribe en el fichero
            wr.write(System.getProperty("line.separator"));// escribe el salto de linea
        }
        
        imprimir = "</labels>";
        wr.write(imprimir);//escribe en el fichero
        wr.write(System.getProperty("line.separator"));// escribe el salto de linea
        
        
    }
    
    public static boolean Es_de_tipo_MEKA(String cadena)
    {
        String tipo="-C";
        
        if(cadena.indexOf(tipo)!=-1) return true;
        return false;
    }
    
    public static String transforma_cadena_tipo_A(String cadena, int cant_labels)
    {
        String result="",temp="";
        int comas=0;
        for(int i=0;i<cadena.length();i++)
        {
            if(comas<cant_labels)
            {
                if(cadena.charAt(i)==',') comas++;
                
                temp+=cadena.charAt(i);
            }
            else{result+= cadena.charAt(i);}
        }
        return result+=","+temp.substring(0, temp.length()-1);
    
    }
    
    public static String Get_tipo_formato(String cadena)
    {
        if(cadena.charAt(0)=='{') return "tipo_B";
        return "tipo_A";
    }
    
  
    public static String Extract_relation_name(String cadena)
    {
        String relation_name =" ";
        String simbol = "'";
        boolean flag =false;
        
        for(int i=1; i<cadena.length(); i++)
        {
            if(cadena.substring(i-1, i).equals(simbol)){ flag=true; continue;}
            
            if(flag)
            {
                if(cadena.substring(i-1, i).equals(":")) break;
                relation_name+=cadena.substring(i-1, i);
            }
            
        }
        
        return relation_name;
    }
    
    public static int Extract_labels_from_arff(String cadena)
    {
        int labels;
        String current, value="";
        
        boolean flag=false;
        for(int i=2; i< cadena.length(); i++)
        {
            current= cadena.substring(i-2,i);
                    
            if(current.equals("-C")){ flag = true; continue;}
            
            if(flag && es_numero(cadena.charAt(i))) value+=cadena.charAt(i);
            
            if(flag && !es_numero(cadena.charAt(i))) break;
        }
        
         labels =Integer.parseInt(value);
        
        return labels;
    }
    
    public static String Extract_label_name_from_String(String cadena)
    {
        String result=null;
        int espacios=0;
        int pos_inicio=0,pos_final=0;
        
        if(cadena.indexOf("@attribute")!= -1) 
        {
            for(int i=0; i<cadena.length(); i++)
            {
                if(cadena.charAt(i)==' ' && espacios==0){espacios++;pos_inicio=i;}
                else if(cadena.charAt(i)==' '){pos_final=i; break;}
                
            }
            
            result = cadena.substring(pos_inicio+1, pos_final);
        }
        
        return result;
    }
    
    	 
	 public static boolean es_numero(char valor)
	 {
             String numero_aceptado="0123456789";
	
            if(!Esta_valor(numero_aceptado, valor)) return false; 

            return true;
	 }
    
    	 public static boolean Esta_valor(String conjunto, char valor)
	 {
		 for (int i=0; i<conjunto.length(); i++)
			 if(conjunto.charAt(i)== valor) return true;
		 
         	 return false;
	 }
    
    public static String Truncate_values_aprox_zero (String value, int digits)
    {
        String simbol ="";
        if(this_value_Has_E(value))
        {
            simbol=value.substring(value.length()-3, value.length());
        }
        double valor = Double.parseDouble(value);
        return Truncate_value(valor, digits);//+simbol;
    }
    private static boolean this_value_Has_E(String value)
    {               
        if(value.charAt(value.length()-3) =='E' ||value.charAt(value.length()-2) =='E')
           return true;
        
        return false;
    }
    
    
    public static String[] Labelcommb_information(Statistics stat1, int index)
    {
        String[] selected= new String[2];
        
        HashMap<LabelSet,Integer> labelcombination = stat1.labelCombCount();
        Set<LabelSet> keysets = labelcombination.keySet();
        
        int count =0;
        for(LabelSet current : keysets)
        {
            
            if(count == index)
            {
               int value= labelcombination.get(current);
               String labelname = current.toString();
               
               selected[0]=  labelname;
               selected[1] = Integer.toString(value);
               break;
            }
            count++;
        }
        return selected;
        
    }
    
     //ENCONTRAR TODAS LAS ETIQUETAS QUE TENGAN EL MISMO IR inter class
    public static ArrayList<String> Get_labelnames_x_IR_inter_class(double ir, int cant_etiquetas_encontradas,atributo[] label_imbalanced)
    {
         ArrayList<String> labelnames= new ArrayList();
         atributo current;
         int encontrados=0;
         String truncate_current;
         double current_ir;
         
         for(int i=0; i<label_imbalanced.length; i++)
         {
             current = label_imbalanced[i];
             if(encontrados == cant_etiquetas_encontradas) break;
             
             truncate_current = Double.toString(current.get_ir_inter_class());
             truncate_current = Truncate_values_aprox_zero(truncate_current, 5);
             
             current_ir = Double.parseDouble(truncate_current);
             
             if(current_ir == ir)
             {
                 encontrados++;
                 labelnames.add(current.get_name());
             }
         }
         return labelnames;
    }
    
    
    //ENCONTRAR TODAS LAS ETIQUETAS QUE TENGAN EL MISMO IR intra class
    public static ArrayList<String> Get_labelnames_x_IR_intra_class(int ir, int cant_etiquetas_encontradas,atributo[] label_imbalanced)
    {
         ArrayList<String> labelnames= new ArrayList();
         atributo current;
         int encontrados=0;
         for(int i=0; i<label_imbalanced.length; i++)
         {
             current = label_imbalanced[i];
             if(encontrados == cant_etiquetas_encontradas) break;
             
             if(current.get_ir() == ir)
             {
                 encontrados++;
                 labelnames.add(current.get_name());
             }
         }
         return labelnames;
    }
    
    //ENCONTRAR TODAS LAS ETIQUETAS QUE TENGAN EL MISMO IR intra class
        public static ArrayList<String> Get_labelnames_x_IR_intra_class(double ir, int cant_etiquetas_encontradas,atributo[] label_imbalanced)
    {
         ArrayList<String> labelnames= new ArrayList();
         atributo current;
         int encontrados=0;
         String truncate;
         double current_truncate;
         
         for(int i=0; i<label_imbalanced.length; i++)
         {
             current = label_imbalanced[i];
             if(encontrados == cant_etiquetas_encontradas) break;
             
             truncate = Double.toString(current.get_ir());
             truncate = util.Truncate_values_aprox_zero(truncate, 5);
             current_truncate = Double.parseDouble(truncate);
             
             if(current_truncate == ir)
             {
                 encontrados++;
                 labelnames.add(current.get_name());
             }
         }
         return labelnames;
    }
    
    public static ArrayList<String> Get_labelnames_x_labelcombination( MultiLabelInstances dataset, String labelcombination)
    {
        ArrayList<String> labelnames_x_labelcombination = new ArrayList();
        
        for(int i=0; i<labelcombination.length();i++)
        {
            if(labelcombination.charAt(i)=='1')
            {
                labelnames_x_labelcombination.add(Get_label_x_indice(dataset, i).name());
            }
        }
        return labelnames_x_labelcombination;
    }
    
    public static Attribute Get_label_x_indice( MultiLabelInstances dataset,int id)
    {
        int[] label_indices = dataset.getLabelIndices();
        
        Attribute result = dataset.getDataSet().instance(1).attribute(label_indices[id]);
        return result;
    }
    
     public static void update_values_bar_chart(atributo[] label_x_frequency, int cant_instancias, CategoryPlot cp )
    {
            DefaultCategoryDataset my_data = new DefaultCategoryDataset();
      
            double prob;
            
            label_x_frequency = util.Ordenar_freq_x_attr(label_x_frequency);
            
            double sum = 0.0;
            for(int i=0; i<label_x_frequency.length;i++)
            {
                prob= label_x_frequency[i].get_frequency()*1.0/cant_instancias;
                sum += prob;
               // System.out.println(" TESTING "+label_x_frequency[i].get_name()+" "+label_x_frequency[i].get_frequency() +" "+ prob);
                                
                    //type === bar
                    my_data.setValue(prob, label_x_frequency[i].get_name()," ");               
            }
          
            cp.setDataset(my_data);
            
            // add a labelled marker for the bid start price...
            sum = sum/label_x_frequency.length;
            Marker start = new ValueMarker(sum);
            start.setPaint(Color.red);
            start.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
            start.setLabel("                        Mean: "+util.Truncate_value(sum, 3));
            cp.addRangeMarker(start);
            
            //return cp;
              
            
    }
     
     
   
    
 
  public static void update_values_xydataset(ChartPanel xyplot1, double[] arreglo_ordenado) {

   XYPlot xyplot = xyplot1.getChart().getXYPlot();
    
   double min = arreglo_ordenado[0];
    System.out.println("el menor es " +min);
   double max = arreglo_ordenado[arreglo_ordenado.length-1];
   
   double mediana = util.get_mediana(arreglo_ordenado);
   
   double q1 = util.get_q1(arreglo_ordenado);
   double q3 = util.get_q3(arreglo_ordenado);

   double ir = util.get_RI_q1_q3(q1, q3);
   
   double linf = util.Limite_inf(q1, ir);
   double lsup = util.Limite_sup(q3, ir);
   
   XYSeries serie_linf=null;
   XYSeries serie_lsup=null;

   // l_inf vertical
       serie_linf = new XYSeries("8");
       serie_linf.add(linf, 0.4);
       serie_linf.add(linf, 0.6);
       
       XYTextAnnotation annotation = new XYTextAnnotation("Li", linf, 0.35);
       annotation.setFont(new Font("SansSerif", Font.PLAIN, 11));
       xyplot.addAnnotation(annotation);
   
    //min-linf horizontal
    XYSeries serie15 = new XYSeries("15");
    serie15.add(min, 0.5);
    serie15.add(linf, 0.5);
    
    //max-lsup horizontal
    XYSeries serie16 = new XYSeries("16");
    serie16.add(max, 0.5);
    serie16.add(lsup, 0.5);
    
   
   //min vertical
    XYSeries serie1 = new XYSeries("0");
    serie1.add(min, 0.45);
    serie1.add(min, 0.55);
    
     annotation = new XYTextAnnotation("Min", min, 0.40);
     annotation.setFont(new Font("SansSerif", Font.PLAIN, 11));
     xyplot.addAnnotation(annotation);
     
  
    //min-q1 horizontal
    XYSeries serie2 = new XYSeries("1");
    serie2.add(min, 0.5);
    serie2.add(q1, 0.5);
  
    //q1 vertical  
    XYSeries serie3 = new XYSeries("2");
    serie3.add(q1, 0.1);
    serie3.add(q1, 0.9);
    
    annotation = new XYTextAnnotation("Q1", q1, 0.08);
    annotation.setFont(new Font("SansSerif", Font.PLAIN, 11));
    xyplot.addAnnotation(annotation);
    
    // mediana 
    XYSeries serie_mediana = new XYSeries("11");
    serie_mediana.add(mediana, 0.1);
    serie_mediana.add(mediana, 0.9);
    
    annotation = new XYTextAnnotation("Median", mediana, 0.04);
    annotation.setFont(new Font("SansSerif", Font.PLAIN, 11));
    xyplot.addAnnotation(annotation);
 
    //q1-q3 horizontal sup
    XYSeries serie4 = new XYSeries("3");
    serie4.add(q1, 0.9);
    serie4.add(q3, 0.9);
 
    //q1-q3 horizontal inf
    XYSeries serie5 = new XYSeries("4");
    serie5.add(q1, 0.1);
    serie5.add(q3, 0.1);
 
    //q3 vertical
    XYSeries serie6 = new XYSeries("5");
    serie6.add(q3, 0.1);
    serie6.add(q3, 0.9);
    
    annotation = new XYTextAnnotation("Q3", q3, 0.08);
    annotation.setFont(new Font("SansSerif", Font.PLAIN, 11));
    xyplot.addAnnotation(annotation);
    
    //q3-max horizontal
    XYSeries serie7 = new XYSeries("6");
    serie7.add(q3, 0.5);
    serie7.add(max, 0.5);
    
    //max vertical
    XYSeries serie8 = new XYSeries("7");
    serie8.add(max, 0.45);
    serie8.add(max, 0.55);
    
    annotation = new XYTextAnnotation("Max", max, 0.4);
    annotation.setFont(new Font("SansSerif", Font.PLAIN, 11));
    xyplot.addAnnotation(annotation);
    

        serie_lsup = new XYSeries("9");
        serie_lsup.add(lsup, 0.4);
        serie_lsup.add(lsup, 0.6);
        
       annotation = new XYTextAnnotation("Ls", lsup, 0.35);
       annotation.setFont(new Font("SansSerif", Font.PLAIN, 11));
       xyplot.addAnnotation(annotation);

       

 
 XYSeriesCollection xyseriescollection = new XYSeriesCollection();

 //xyseriescollection.addSeries(serie_point);
 
 xyseriescollection.addSeries(serie1);
 xyseriescollection.addSeries(serie2);
 xyseriescollection.addSeries(serie3);
 xyseriescollection.addSeries(serie4);
 xyseriescollection.addSeries(serie5);
 xyseriescollection.addSeries(serie6);
 xyseriescollection.addSeries(serie7);
 xyseriescollection.addSeries(serie8);
 xyseriescollection.addSeries(serie15);
 xyseriescollection.addSeries(serie16);
 xyseriescollection.addSeries(serie_mediana);
 
 xyseriescollection.addSeries(serie_linf);
 xyplot.getRenderer().setSeriesPaint(9, Color.black);
 xyseriescollection.addSeries(serie_lsup); 
 xyplot.getRenderer().setSeriesPaint(10, Color.black); 
 
 
 xyplot.getRenderer().setSeriesPaint(0, Color.black);
 xyplot.getRenderer().setSeriesPaint(1, Color.black);
 xyplot.getRenderer().setSeriesPaint(2, Color.black);
 xyplot.getRenderer().setSeriesPaint(3, Color.black);
 xyplot.getRenderer().setSeriesPaint(4, Color.black);
 xyplot.getRenderer().setSeriesPaint(5, Color.black);
 xyplot.getRenderer().setSeriesPaint(6, Color.black);
 xyplot.getRenderer().setSeriesPaint(7, Color.black);
 xyplot.getRenderer().setSeriesPaint(8, Color.black);
 xyplot.getRenderer().setSeriesPaint(9, Color.black);
 xyplot.getRenderer().setSeriesPaint(10, Color.black);
 xyplot.getRenderer().setSeriesPaint(11, Color.black);
 xyplot.getRenderer().setSeriesPaint(12, Color.black);
 xyplot.getRenderer().setSeriesPaint(13, Color.black);
 
  //agregar el dataset
 xyplot.setDataset(xyseriescollection);
 
 
 
 
 
 
    // add a second dataset and renderer... 
     XYSeriesCollection anotherserie = new XYSeriesCollection();
         
     XYSeries serie_point = new XYSeries("21");
     
     double[] valor_y = {0.47,0.49,0.51,0.53};
     
     for(int i=0, j=0; i<arreglo_ordenado.length; i++ , j++)
     {
         if(j%4==0) j=0;
         serie_point.add(arreglo_ordenado[i],valor_y[j] );
     }
         
    anotherserie.addSeries(serie_point);
       
     
     XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer(false, true); 
     renderer1.setSeriesPaint(0, Color.lightGray);
    // arguments of new XYLineAndShapeRenderer are to activate or deactivate the display of points or line. Set first argument to true if you want to draw lines between the points for e.g.
    xyplot.setDataset(1, anotherserie);
    xyplot.setRenderer(1, renderer1);
 
 
 }
  
  public static void update_values_xydataset(ChartPanel xyplot1, int[] arreglo_ordenado) {

   XYPlot xyplot = xyplot1.getChart().getXYPlot();
    
   double min = arreglo_ordenado[0];
    System.out.println("el menor es " +min);
   double max = arreglo_ordenado[arreglo_ordenado.length-1];
   
   double mediana = get_mediana(arreglo_ordenado);
   
   double q1 = get_q1(arreglo_ordenado);
   double q3 = get_q3(arreglo_ordenado);

   double ir = util.get_RI_q1_q3(q1, q3);
   
   double linf = Limite_inf(q1, ir);
   double lsup = Limite_sup(q3, ir);
   
   XYSeries serie_linf=null;
   XYSeries serie_lsup=null;

   // l_inf vertical
       serie_linf = new XYSeries("8");
       serie_linf.add(linf, 0.4);
       serie_linf.add(linf, 0.6);
       
       XYTextAnnotation annotation = new XYTextAnnotation("Li", linf, 0.35);
       annotation.setFont(new Font("SansSerif", Font.PLAIN, 11));
       xyplot.addAnnotation(annotation);
   
    //min-linf horizontal
    XYSeries serie15 = new XYSeries("15");
    serie15.add(min, 0.5);
    serie15.add(linf, 0.5);
    
    //max-lsup horizontal
    XYSeries serie16 = new XYSeries("16");
    serie16.add(max, 0.5);
    serie16.add(lsup, 0.5);
    
   
   //min vertical
    XYSeries serie1 = new XYSeries("0");
    serie1.add(min, 0.45);
    serie1.add(min, 0.55);
    
     annotation = new XYTextAnnotation("Min", min, 0.40);
     annotation.setFont(new Font("SansSerif", Font.PLAIN, 11));
     xyplot.addAnnotation(annotation);
     
  
    //min-q1 horizontal
    XYSeries serie2 = new XYSeries("1");
    serie2.add(min, 0.5);
    serie2.add(q1, 0.5);
  
    //q1 vertical  
    XYSeries serie3 = new XYSeries("2");
    serie3.add(q1, 0.1);
    serie3.add(q1, 0.9);
    
    annotation = new XYTextAnnotation("Q1", q1, 0.08);
    annotation.setFont(new Font("SansSerif", Font.PLAIN, 11));
    xyplot.addAnnotation(annotation);
    
    // mediana 
    XYSeries serie_mediana = new XYSeries("11");
    serie_mediana.add(mediana, 0.1);
    serie_mediana.add(mediana, 0.9);
    
    annotation = new XYTextAnnotation("Median", mediana, 0.04);
    annotation.setFont(new Font("SansSerif", Font.PLAIN, 11));
    xyplot.addAnnotation(annotation);
 
    //q1-q3 horizontal sup
    XYSeries serie4 = new XYSeries("3");
    serie4.add(q1, 0.9);
    serie4.add(q3, 0.9);
 
    //q1-q3 horizontal inf
    XYSeries serie5 = new XYSeries("4");
    serie5.add(q1, 0.1);
    serie5.add(q3, 0.1);
 
    //q3 vertical
    XYSeries serie6 = new XYSeries("5");
    serie6.add(q3, 0.1);
    serie6.add(q3, 0.9);
    
    annotation = new XYTextAnnotation("Q3", q3, 0.08);
    annotation.setFont(new Font("SansSerif", Font.PLAIN, 11));
    xyplot.addAnnotation(annotation);
    
    //q3-max horizontal
    XYSeries serie7 = new XYSeries("6");
    serie7.add(q3, 0.5);
    serie7.add(max, 0.5);
    
    //max vertical
    XYSeries serie8 = new XYSeries("7");
    serie8.add(max, 0.45);
    serie8.add(max, 0.55);
    
    annotation = new XYTextAnnotation("Max", max, 0.4);
    annotation.setFont(new Font("SansSerif", Font.PLAIN, 11));
    xyplot.addAnnotation(annotation);
    

        serie_lsup = new XYSeries("9");
        serie_lsup.add(lsup, 0.4);
        serie_lsup.add(lsup, 0.6);
        
       annotation = new XYTextAnnotation("Ls", lsup, 0.35);
       annotation.setFont(new Font("SansSerif", Font.PLAIN, 11));
       xyplot.addAnnotation(annotation);

       

 
 XYSeriesCollection xyseriescollection = new XYSeriesCollection();

 //xyseriescollection.addSeries(serie_point);
 
 xyseriescollection.addSeries(serie1);
 xyseriescollection.addSeries(serie2);
 xyseriescollection.addSeries(serie3);
 xyseriescollection.addSeries(serie4);
 xyseriescollection.addSeries(serie5);
 xyseriescollection.addSeries(serie6);
 xyseriescollection.addSeries(serie7);
 xyseriescollection.addSeries(serie8);
 xyseriescollection.addSeries(serie15);
 xyseriescollection.addSeries(serie16);
 xyseriescollection.addSeries(serie_mediana);
 
 xyseriescollection.addSeries(serie_linf);
 xyplot.getRenderer().setSeriesPaint(9, Color.black);
 xyseriescollection.addSeries(serie_lsup); 
 xyplot.getRenderer().setSeriesPaint(10, Color.black); 
 
 
 xyplot.getRenderer().setSeriesPaint(0, Color.black);
 xyplot.getRenderer().setSeriesPaint(1, Color.black);
 xyplot.getRenderer().setSeriesPaint(2, Color.black);
 xyplot.getRenderer().setSeriesPaint(3, Color.black);
 xyplot.getRenderer().setSeriesPaint(4, Color.black);
 xyplot.getRenderer().setSeriesPaint(5, Color.black);
 xyplot.getRenderer().setSeriesPaint(6, Color.black);
 xyplot.getRenderer().setSeriesPaint(7, Color.black);
 xyplot.getRenderer().setSeriesPaint(8, Color.black);
 xyplot.getRenderer().setSeriesPaint(9, Color.black);
 xyplot.getRenderer().setSeriesPaint(10, Color.black);
 xyplot.getRenderer().setSeriesPaint(11, Color.black);
 xyplot.getRenderer().setSeriesPaint(12, Color.black);
 xyplot.getRenderer().setSeriesPaint(13, Color.black);
 
  //agregar el dataset
 xyplot.setDataset(xyseriescollection);
 
 
 
 
 
 
    // add a second dataset and renderer... 
     XYSeriesCollection anotherserie = new XYSeriesCollection();
         
     XYSeries serie_point = new XYSeries("21");
     
     double[] valor_y = {0.47,0.49,0.51,0.53};
     
     for(int i=0, j=0; i<arreglo_ordenado.length; i++ , j++)
     {
         if(j%4==0) j=0;
         serie_point.add(arreglo_ordenado[i],valor_y[j] );
     }
         
    anotherserie.addSeries(serie_point);
       
     
     XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer(false, true); 
     renderer1.setSeriesPaint(0, Color.lightGray);
    // arguments of new XYLineAndShapeRenderer are to activate or deactivate the display of points or line. Set first argument to true if you want to draw lines between the points for e.g.
    xyplot.setDataset(1, anotherserie);
    xyplot.setRenderer(1, renderer1);
 
 
 }

   public static void update_values_line_chart(double[] id_x_IR ,int[] id_x_nums_label, CategoryPlot cp)
    {      
       DefaultCategoryDataset my_data = new DefaultCategoryDataset();
       DefaultCategoryDataset my_data1 = new DefaultCategoryDataset();
       
       if(id_x_IR==null) return;
       
           for(int i=0; i<id_x_IR.length ; i++)
           {
                if(id_x_IR[i]==0 && id_x_nums_label[i]==0) continue; //my_data.setValue(0 , "Label-Combination: ",Integer.toString(i));
                else 
                {
                    my_data.setValue(id_x_IR[i] , "IR",Integer.toString(i+1));
                    my_data1.setValue(id_x_nums_label[i] , "# labels",Integer.toString(i+1));                                    
                }
           }  
        if(id_x_IR.length>50) cp.getDomainAxis().setTickLabelsVisible(false);   
        else{cp.getDomainAxis().setTickLabelsVisible(true);   }
        
        cp.setDataset(my_data);      
        
        double sum = get_mean(id_x_nums_label, id_x_IR);
        
        Marker start = new ValueMarker(sum);
        start.setPaint(Color.red);
        start.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
        start.setLabel("                        Mean: "+util.Truncate_value(sum, 3));
        cp.addRangeMarker(start);
        
        
        cp.setDataset(1, my_data1);
    
    }
  
     public static void update_values_line_chart(double[] ir_values, CategoryPlot cp, boolean invertir_reccorrido)
    {      
       DefaultCategoryDataset my_data = new DefaultCategoryDataset();
       
       
       if(ir_values==null) return;
       
       
       if(!invertir_reccorrido){
       
           for(int i=0; i<ir_values.length ; i++)
           {
                if(ir_values[i]==0 ) continue; //my_data.setValue(0 , "Label-Combination: ",Integer.toString(i));
                else 
                {
                    my_data.setValue(ir_values[i] , "IR",Integer.toString(i+1));
                                             
                }
           }
       }
       else
       {
           int temp = ir_values.length-1;
           
            for(int i=temp, count=1; i>=0 ; i--,count++)
           {
                if(ir_values[i]==0 ) continue; //my_data.setValue(0 , "Label-Combination: ",Integer.toString(i));
                else 
                {
                    my_data.setValue(ir_values[i] , "IR",Integer.toString(count));
                                             
                }
           }
           
       }
           
           
           
        if(ir_values.length>50) cp.getDomainAxis().setTickLabelsVisible(false);   
        else{cp.getDomainAxis().setTickLabelsVisible(true);   }
        
        cp.setDataset(my_data);      
        
        double sum = get_mean(ir_values);
        
        Marker start = new ValueMarker(sum);
        start.setPaint(Color.red);
        start.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
        start.setLabel("                        Mean: "+util.Truncate_value(sum, 3));
        cp.addRangeMarker(start);
        
        
      
    
    }
     
       public static double get_mean(double[] values)
       {
           double result=0;
           
           for(int i=0;i<values.length; i++)
           {
               result+=values[i];
           }
           return result/values.length;
       }
     
       public static double get_mean(int[] cant_values, double[] values)
     {
         int sum=0;
         double value=0;
         
         double result;
         
         for(int i=0;i<cant_values.length; i++)
         {
             sum+=cant_values[i];
             value+= values[i]*(cant_values[i]);
         }
         
         result = value/(sum*1.0);
         return result;
     }
     
     
     public static double get_mean(int[] cant_values, int[] values)
     {
         int sum=0;
         int value=0;
         
         for(int i=0;i<cant_values.length; i++)
         {
             sum+=cant_values[i];
             value+= values[i]*(cant_values[i]*1.0);
         }
         return value/(sum*1.0);
     }
     
     public static int get_sum_values_array(int[] values)
     {
         int count=0;
         
         for(int i=0;i<values.length; i++)
             count+=values[i];
         
         return count;
     }
     
     public static double[] Get_array_values_IR(int[] id_x_labels, double[] id_x_ir)
     {
         int size = get_sum_values_array(id_x_labels);
         double[] result = new double[size];
         
         int count,temp=0;
         int j=0;//posicion de result
         
         for(int i=0;i<id_x_labels.length;i++)
         {
             count = id_x_labels[i];
             
             while(temp<count)
             {
                 result[j]=id_x_ir[i];
                 j++;
                 temp++;
             }
             temp=0;//temporal para asignar tantas veces hasta que la cantidad de veces de count.
         }
         
         return result;
     }
  
  
     // VIZUALIZA LAS COMBINACIONES DE ETIQUETAS ORDENADAS DE MENOR A MAYOR, MOSTRANDO SU FRECUENCIA: EJ: # LABEL QUE TIENEN 1, # LABEL QUE TIENEN 2...
     public static void update_values_line_chart(int cant_instancias, CategoryPlot cp,HashMap<Integer,Integer> labelset_x_frequency )
    {      
       DefaultCategoryDataset my_data = new DefaultCategoryDataset();
      
            double prob;            

            int max = Maxim_key(labelset_x_frequency);
                     
           for(int i=0; i<=max+1 ; i++)
           {
               int freq_current=0;
               if(labelset_x_frequency.get(i)!=null) freq_current=labelset_x_frequency.get(i);
               
               prob= freq_current*1.0/cant_instancias;
               
                if(prob==0.0) my_data.setValue(0 , "Label-Combination: ",Integer.toString(i));
                else my_data.setValue(prob , "Label-Combination: ",Integer.toString(i));
               
           }         
        cp.setDataset(my_data);       
        
        if(max>30) cp.getDomainAxis().setTickLabelsVisible(false);   
        else{cp.getDomainAxis().setTickLabelsVisible(true);   }
    
    }
    
    public  static int Maxim_key (HashMap<Integer,Integer> hm)
    {
       Set<Integer> keys= hm.keySet();
       
       int mayor=0;
       
       for(int current : keys)
       {
           if(mayor<current) mayor = current;
       }
       return mayor;
    }
    
    public static double[] get_label_frequency(atributo[] label_freq)
    {
        double[] label_frequency = new double[label_freq.length];
        
        for(int i=0;i<label_freq.length; i++)
            label_frequency[i]=(double)label_freq[i].get_frequency();
        
        return label_frequency;
    }
     
    //DEVUELVE UN HASHMAP CON KEY: CANTIDAD DE ETIQUETAS, VALUE: CANTIDAD DE VECES QUE SE REPITE EL LABELSET EN UN DATASET,
    public static HashMap<Integer,Integer> Get_labelset_x_values( Statistics stat1 )
    {            
        HashMap<LabelSet,Integer> result = stat1.labelCombCount();
        Set<LabelSet> keysets = result.keySet();
        
        HashMap<Integer,Integer> labelset_x_frequency = new HashMap<Integer,Integer>();
        
             
        int old_value;
        
        for(LabelSet current : keysets)
        {
            int value=  result.get(current);
            int key = current.size();
            
            if(labelset_x_frequency.get(key)==null)
            {
                labelset_x_frequency.put(key, value);
            }
            else
            {
                old_value = labelset_x_frequency.get(key);
                labelset_x_frequency.remove(key);
                labelset_x_frequency.put(key, value+old_value);     
            }
            
           
        }    
        return labelset_x_frequency;
    }
    
    public static String Get_xml_string( String arff_text)
    {
        String result="";
        System.out.println("arff_text: " + arff_text);
        
        //for(int i=0; i<arff_text.length();i++)
        //{
            //if(arff_text.charAt(i)=='-') return result+=".xml";
        
        //    result+=arff_text.charAt(i);
        //}     
        
        String [] words = arff_text.split("-");
        if(words.length > 1){
            System.out.println("words: " + Arrays.toString(words));
            for(int i=0; i<words.length-1; i++){
                result = result + words[i]+"-";
            }
            result = result.substring(0, result.length()-1) + ".xml";
            System.out.println("result1: " + result);
        }
        else{
            result = arff_text.substring(0,arff_text.length()-5)+".xml";
            System.out.println("result2: " + result);
        }
        
       
       return result;
    }
     
    
    public static double[][] Get_data_heapmap(MultiLabelInstances dataset)
    {
        int cant_labels = dataset.getNumLabels();
        double[][] data = new double[cant_labels][cant_labels];
        
        int[] label_indices= dataset.getLabelIndices(); //ALMACENA LOS INDICES DE LAS ETIQUETAS
        
        
        
        for(int j=0; j<cant_labels ; j++)
            for(int k=0; k<cant_labels; k++)
            {
                data[j][k]= Get_probability_x_2_labels(dataset, label_indices[j], label_indices[k]);//[j][k], j-fila y k-columna
            }
         
         return data;
    }
    
     public static int[] get_label_indices(String[] labels, MultiLabelInstances dataset)
     {
         int[] label_indices = new int[labels.length];
         
         int current;
         Instances instancias = dataset.getDataSet();
         for(int i=0; i<label_indices.length;i++)
         {
            current = instancias.attribute(labels[i]).index();
            label_indices[i]=current;                    
         }
         return label_indices;
     }
    
     public static double[][] Get_data_heapmap(MultiLabelInstances dataset, int[] labels)
    {
        double[][] data = new double[labels.length][labels.length];
        
        int[] label_indices= dataset.getLabelIndices(); //ALMACENA LOS INDICES DE LAS ETIQUETAS
        
        
        for(int j=0; j<labels.length ; j++)
            for(int k=0; k<labels.length; k++)
            {
                data[j][k]= Get_probability_x_2_labels(dataset, labels[j], labels[k]);//[j][k], j-fila y k-columna
            }
         
         return data;
    }
    
    
    public static double[][] Invertir_Matrix(double[][] data)
    {
        double[][] result = new double[data.length][data.length];
        
        for(int k=data.length-1; k>=0 ; k--)
            for(int j=0; j<data.length;j++)
            {
                result[k][j]=data[j][data.length-1-k];
            }
        
        return result;
    }
    
     public static void Recorre_Arreglo(int[] data)
    {
        System.out.println("RECORRER ARREGLO DATOS");
        
        for(int j=0; j<data.length ; j++)
             System.out.print(" , "+data[j]);
               
        System.out.println();            
    }
     
     public static void Recorre_Arreglo(String[] data)
    {
        //System.out.println("RECORRER ARREGLO DATOS");
        
        for(int j=0; j<data.length ; j++)
             System.out.print(" , "+data[j]);
               
        System.out.println();            
    }
     
     public static void Recorre_Arreglo(atributo[] data)
    {
        //System.out.println("RECORRER ARREGLO DATOS");
        
        for(int j=0; j<data.length ; j++)
             System.out.print(" , "+data[j].get_frequency());
               
        System.out.println();            
    }
         
     public static void Recorre_Arreglo(Integer[] data)
    {
       // System.out.println("RECORRER ARREGLO DATOS");
        
        for(int j=0; j<data.length ; j++)
             System.out.print(" , "+data[j]);
               
        System.out.println();            
    }
     
     
     public static void Recorre_Arreglo(double[] data)
    {
        System.out.println("RECORRER ARREGLO DATOS");
        
        for(int j=0; j<data.length ; j++)
             System.out.print(" , "+data[j]);
               
        System.out.println();            
    }
    
    public static void Recorre_Arreglo_2_dimensiones(double[][] data)
    {
        System.out.println("RECORRER ARREGLO DATOS");
        for(int j=0; j<data.length ; j++)
        {
            for(int k=0; k<data.length; k++)
            {
                System.out.print(" , "+data[j][k]);
            }
            System.out.println();            
        }   
    }
    
     
    
     public static double Get_repetition_number_x_1_label(MultiLabelInstances dataset, int label_k)
    {
      double value=0.0;
        
        Instances Instancias = dataset.getDataSet();
        
        double  esta_label_k;
        
         for(int i=0; i<Instancias.size();i++)
             {
               esta_label_k=Instancias.instance(i).value(label_k);                
               if(esta_label_k==1.0) value++;
             }         
                
        return value;
    }
    
    public static double Get_frequency_x_1_label(MultiLabelInstances dataset, int label_k)
    {
      double value=0.0;
        
        Instances Instancias = dataset.getDataSet();
        
        double  esta_label_k;
        
         for(int i=0; i<Instancias.size();i++)
             {
               esta_label_k=Instancias.instance(i).value(label_k);                
               if(esta_label_k==1.0) value++;
             }         
                
        return value/dataset.getNumInstances();
    }
    
    //DADO DOS ETIQUETAS DEVOLVER LA FREQUENCIA CON QUE OCURREN AMBAS EN EL CONJUNTO DE INSTANCIAS
    public static double Get_probability_x_2_labels(MultiLabelInstances dataset, int label_j, int label_k)
    {
        double value=0.0;
        
        Instances Instancias = dataset.getDataSet();
        
        double esta_label_j , esta_label_k;
        
         for(int i=0; i<Instancias.size();i++)
             {
                esta_label_j=Instancias.instance(i).value(label_j);
                esta_label_k=Instancias.instance(i).value(label_k);
                
                if(esta_label_j ==1.0 && esta_label_k==1.0) value++;
             }         
        
         double freq = value/dataset.getNumInstances();
         
        if(label_j == label_k)  return freq;
        
        return freq / Get_frequency_x_1_label(dataset, label_k);
        
    }
    
     public static double Get_repetitions_x_2_labels(MultiLabelInstances dataset, int label_j, int label_k)
    {
        double value=0.0;
        
        Instances Instancias = dataset.getDataSet();
        
        double esta_label_j , esta_label_k;
        
         for(int i=0; i<Instancias.size();i++)
             {
                esta_label_j=Instancias.instance(i).value(label_j);
                esta_label_k=Instancias.instance(i).value(label_k);
                
                if(esta_label_j ==1.0 && esta_label_k==1.0) value++;
             }         
        
         double freq = value;
         
        if(label_j == label_k)  return freq;
        
        return freq ;
        
    }
    
    
    public static String[] devuelve_etiquetas_seleccionadas (MultiLabelInstances dataset, int[] label_indices)
    {
        String[] result = new String[label_indices.length];
        
        String current;
        for(int i=0;i<label_indices.length;i++)
        {
            current = dataset.getDataSet().attribute(label_indices[i]).name();
            result[i]= current;
        }
        return result;
    }
    
    public static int Devuelve_num_celda(int pos_inicial, int posicion_Actual, int ancho_celda, int cant_celdas)
    {
        int limite = pos_inicial+(cant_celdas*ancho_celda);
        
        if(posicion_Actual<pos_inicial) return -1;
        
        int temp=pos_inicial;
        
        for(int i=0;temp<=limite;i++, temp=temp+ancho_celda)
        {
            if(temp>posicion_Actual) return i;
        }
        
        return -1;
    }
    
        
     public static boolean Esta_el_valor (int[] visitados , int actual)
     {
         for(int i=0; i<visitados.length;i++)
             if(visitados[i]==actual)return true;
         return false;
     }
     
    public static boolean Esta_el_valor (double[] visitados , double actual)
     {
         for(int i=0; i<visitados.length;i++)
             if(visitados[i]==actual)return true;
         return false;
     }
    
    
     public static boolean Esta_Atributo (ArrayList<String> visitados , atributo actual)
     {
         for( String current : visitados)
             if(current.equals(actual.get_name())) return true;
         
         return false;
     }
    
     public static atributo Devuelve_Mayor_IR_intra_class(atributo[] imbalanced_data, ArrayList<String> visitados)
     {
         atributo mayor=null ;
         
         for( atributo current : imbalanced_data )
         {
             if( Esta_Atributo(visitados, current)) continue;
             else
             {
                 if(mayor == null) mayor = current;
                 else
                 {
                     if(mayor.get_ir() <= current.get_ir() && mayor.get_variance() < current.get_variance()) mayor = current;
                 }
             }
         }
         return mayor;
     }
     
     public static atributo Devuelve_Mayor_IR_inter_class(atributo[] imbalanced_data, ArrayList<String> visitados)
     {
         atributo mayor=null ;
         
         for( atributo current : imbalanced_data )
         {
             if( Esta_Atributo(visitados, current)) continue;
             else
             {
                 if(mayor == null) mayor = current;
                 else
                 {
                     if(mayor.get_ir_inter_class()<= current.get_ir_inter_class()&& mayor.get_variance() < current.get_variance()) mayor = current;
                 }
             }
         }
         return mayor;
     }
     
          public static atributo Devuelve_Menor_IR(atributo[] imbalanced_data, ArrayList<String> visitados)
     {
         atributo menor=null ;
         
         for( atributo current : imbalanced_data )
         {
             if( Esta_Atributo(visitados, current)) continue;
             else
             {
                 if(menor == null) menor = current;
                 else
                 {
                     if(menor.get_ir() >= current.get_ir() && menor.get_variance() > current.get_variance()) menor = current;
                 }
             }
         }
         return menor;
     }
     
     
     
     
     
    public static double get_q1(double[] arreglo_ordenado)
     {
         int cuarto = arreglo_ordenado.length/4;
       //  System.out.println("cuarto " + cuarto);
          
          if(arreglo_ordenado.length %4 ==0)return arreglo_ordenado[cuarto-1];
          return arreglo_ordenado[cuarto];
          //return arreglo_ordenado[parte_entera-1];
                 
     }
    
    public static double get_q1(int[] arreglo_ordenado)
     {
         int cuarto = arreglo_ordenado.length/4;
       //  System.out.println("cuarto " + cuarto);
          
          if(arreglo_ordenado.length %4 ==0)return arreglo_ordenado[cuarto-1];
          return arreglo_ordenado[cuarto];
          //return arreglo_ordenado[parte_entera-1];
                 
     }
    
    public static double get_q3(double[] arreglo_ordenado)
     {
         int cuarto = 3*(arreglo_ordenado.length/4);
        // System.out.println("cuarto " + cuarto);
          
          if(arreglo_ordenado.length %4 ==0)return arreglo_ordenado[cuarto-1];
          return arreglo_ordenado[cuarto];
          //return arreglo_ordenado[parte_entera-1];
                 
     }
    
        public static double get_q3(int[] arreglo_ordenado)
     {
         int cuarto = 3*(arreglo_ordenado.length/4);
        // System.out.println("cuarto " + cuarto);
          
          if(arreglo_ordenado.length %4 ==0)return arreglo_ordenado[cuarto-1];
          return arreglo_ordenado[cuarto];
          //return arreglo_ordenado[parte_entera-1];
                 
     }
     
     public static double get_mediana(double[] arreglo_ordenado)
     {
       int mean = arreglo_ordenado.length/2;
           
       if( arreglo_ordenado.length %2 !=0 ) return arreglo_ordenado[mean];
       else
       {
           double value1 = arreglo_ordenado[mean-1];
           double value2 = arreglo_ordenado[mean];
           return (value1+value2)/2;
       }
     }
     
          public static double get_mediana(int[] arreglo_ordenado)
     {
       int mean = arreglo_ordenado.length/2;
           
       if( arreglo_ordenado.length %2 !=0 ) return arreglo_ordenado[mean];
       else
       {
           double value1 = arreglo_ordenado[mean-1];
           double value2 = arreglo_ordenado[mean];
           return (value1+value2)/2;
       }
     }
     
     public static double get_RI_q1_q3(double q1, double q3) { return q3-q1; }
     
     public static double Limite_inf(double q1, double ri) { return q1-(1.5*ri); }
     public static double Limite_sup(double q3, double ri) { return q3+(1.5*ri); }
     
             
             
     public static double[] Quick_sort(double array[])
     {
       quick_srt(array,0,array.length-1);
       return array;
     }
             
     private static void quick_srt(double array[],int low, int n){
      int lo = low;
      int hi = n;
      if (lo >= n) {
          return;
      }
      double mid = array[(lo + hi) / 2];
      while (lo < hi) {
          while (lo<hi && array[lo] < mid) {
              lo++;
          }
          while (lo<hi && array[hi] > mid) {
              hi--;
          }
          if (lo < hi) {
              double T = array[lo];
              array[lo] = array[hi];
              array[hi] = T;
          }
      }
      if (hi < lo) {
          int T = hi;
          hi = lo;
          lo = T;
      }
      quick_srt(array, low, lo);
      quick_srt(array, lo == low ? lo+1 : lo, n);
   }
    
     public static atributo[] Sort_data_imbalance_Mayor_IR_intra_class(atributo[] imbalanced_data)
     {
        atributo[] ordenados = new atributo[imbalanced_data.length];
        
        ArrayList<String> visitados = new ArrayList();
        atributo current;
        
        for(int i=0; i<imbalanced_data.length; i++)
        {
            current= Devuelve_Mayor_IR_intra_class(imbalanced_data,visitados);
            if(current ==null) break;
            
            ordenados[i]=current;
            visitados.add(current.get_name());
            
        }
        
        return ordenados;
                
     }
     
          public static atributo[] Sort_data_imbalance_Mayor_IR_inter_class(atributo[] imbalanced_data)
     {
        atributo[] ordenados = new atributo[imbalanced_data.length];
        
        ArrayList<String> visitados = new ArrayList();
        atributo current;
        
        for(int i=0; i<imbalanced_data.length; i++)
        {
            current= Devuelve_Mayor_IR_inter_class(imbalanced_data,visitados);
            if(current ==null) break;
            
            ordenados[i]=current;
            visitados.add(current.get_name());
            
        }
        
        return ordenados;
                
     }
     
          public static atributo[] Sort_data_imbalance_Menor(atributo[] imbalanced_data)
     {
        atributo[] ordenados = new atributo[imbalanced_data.length];
        
        ArrayList<String> visitados = new ArrayList();
        atributo current;
        
        for(int i=0; i<imbalanced_data.length; i++)
        {
            current= Devuelve_Menor_IR(imbalanced_data, visitados);
            if(current ==null) break;
            
            ordenados[i]=current;
            visitados.add(current.get_name());
            
        }
        
        return ordenados;
                
     }
          
    public static atributo[] Get_data_imbalanced_x_label_inter_class( MultiLabelInstances dataset, atributo[] label_frequency)
    {
        int[] label_indices= dataset.getLabelIndices();
        
        atributo[] labels_imbalanced = new atributo[label_indices.length];
         
        Instances Instancias = dataset.getDataSet();
         
         int cantidad_1=0, cantidad_0=0, mayor_ocurrencia;
         double esta, ir, variance,ir_inter_class;         
         double media = dataset.getNumInstances()/2;
         
         Attribute current; //para obtener el atributo de la primera instancia
         atributo current_label_freq;
        
         
         for(int i=0; i<label_indices.length;i++)//recorre por etiquetas
         {
             current= Instancias.attribute(label_indices[i]); // atributo actual
           
             for(int j=0; j<Instancias.size();j++)//recorre por instancias
             {
                esta=Instancias.instance(j).value(current);
                if(esta ==1.0) cantidad_1++;
                else cantidad_0++;
             }
             
           try { 
                if(cantidad_0 ==0 || cantidad_1 ==0) ir=0;
                else if(cantidad_0>cantidad_1) ir= cantidad_0/(cantidad_1*1.0);
                else ir=cantidad_1/(cantidad_0*1.0);
                }
           
           catch(Exception e1)
           {
               ir=0;            
           }
                    
             variance = (Math.pow((cantidad_0-media), 2) + Math.pow((cantidad_1-media), 2))/2;
             
             current_label_freq = Get_label_x_labelname(current.name(),label_frequency);
             
             mayor_ocurrencia = label_frequency[0].get_frequency();
             
             ir_inter_class = mayor_ocurrencia/(current_label_freq.get_frequency()*1.0);
             
             labels_imbalanced[i]= new atributo(current.name(),current_label_freq.get_frequency(),ir, variance, ir_inter_class);
             
             cantidad_0=0;
             cantidad_1=0;
         }
         
         return labels_imbalanced;
    }
    
    

        public static atributo[] Get_data_imbalanced_x_label( MultiLabelInstances dataset)
    {
        int[] label_indices= dataset.getLabelIndices();
        
        atributo[] labels_imbalanced = new atributo[label_indices.length];
         
        Instances Instancias = dataset.getDataSet();
         
         int cantidad_1=0, cantidad_0=0;
         double esta, ir, variance;         
         double media = dataset.getNumInstances()/2;
         
         Attribute current; //para obtener el atributo de la primera instancia
         
        
         
         for(int i=0; i<label_indices.length;i++)//recorre por etiquetas
         {
             current= Instancias.attribute(label_indices[i]); // atributo actual
           
             for(int j=0; j<Instancias.size();j++)//recorre por instancias
             {
                esta=Instancias.instance(j).value(current);
                if(esta ==1.0) cantidad_1++;
                else cantidad_0++;
             }
             
           try { 
                if(cantidad_0 ==0 || cantidad_1 ==0) ir=0;
                else if(cantidad_0>cantidad_1) ir= cantidad_0/(cantidad_1*1.0);
                else ir=cantidad_1/(cantidad_0*1.0);
                }
           
           catch(Exception e1)
           {
               ir=0;            
           }
                    
             variance = (Math.pow((cantidad_0-media), 2) + Math.pow((cantidad_1-media), 2))/2;
             
           
             
             labels_imbalanced[i]= new atributo(current.name(), ir, variance);
             
             cantidad_0=0;
             cantidad_1=0;
         }
         
         return labels_imbalanced;
    }
    
    public static atributo[] Get_Frequency_x_label( MultiLabelInstances dataset)
    {
        int[] label_indices= dataset.getLabelIndices();
        
        atributo[] frequencia_x_labels = new atributo[label_indices.length];
         
        Instances Instancias = dataset.getDataSet();
         
         int frequency=0;
         double esta;
         Attribute current; //para obtener el atributo de la primera instancia
         
         for(int i=0; i<label_indices.length;i++)
         {
             current= Instancias.attribute(label_indices[i]); // atributo actual
             
             for(int j=0; j<Instancias.size();j++)
             {
                esta=Instancias.instance(j).value(current);
                if(esta ==1.0) frequency++;
             }
             frequencia_x_labels[i]= new atributo(current.name(), frequency);
             frequency=0;
         }
         
         return frequencia_x_labels;
    }
    
    public static atributo[] Ordenar_freq_x_attr (atributo[] label_frenquency) // ordena de mayor a menor
    {
        ArrayList<atributo> lista = new ArrayList();
        
        for(int i=0; i<label_frenquency.length; i++)
            lista.add(label_frenquency[i]);
        
        atributo[] ordenado = new atributo [label_frenquency.length];
        
        for(int i=0 ; i<label_frenquency.length; i++)
        {
            ordenado[i]= Devuelve_mayor(lista);
            lista.remove(ordenado[i]);
        }
        
      return ordenado;
    }
    
     public static atributo Devuelve_mayor( ArrayList<atributo> lista)
    {
        atributo mayor = lista.get(0);
               
        for(atributo current : lista)
          if(current.get_frequency()>mayor.get_frequency()) mayor = current;
            
        return mayor;       
    }
     
     
    
    public static atributo Devuelve_menor( ArrayList<atributo> lista)
    {
        atributo menor = lista.get(0);
               
        for(atributo current : lista)
          if(current.get_frequency()<menor.get_frequency()) menor = current;
            
        return menor;       
    }
    
   
    
    
    public static String get_alternative_metric(String metric, MultiLabelInstances dataset_train,MultiLabelInstances dataset_test )
    {
        
        Statistics stat_train = new Statistics();
        stat_train.calculateStats(dataset_train);
        
        Statistics stat_test = new Statistics();
        stat_test.calculateStats(dataset_test);
        
        double value =-1.0;
      
          try{
            
            
        switch (metric) 
        {
            
            case "Ratio of test instances over training instances":  value = metrics.ratio_test_instances_to_train_instances(dataset_train,dataset_test);
                     break;
                
            case "Number distinct labelset found in train and test sets":  value = metrics.number_distinc_clases_found_in_train_and_test(stat_train, stat_test);
                     break;     
                
            case "Ratio of distinct labelset found in both datasets over number of labelset":  value = metrics.ratio_distinc_clases_found_in_train_and_test(stat_train,stat_test,dataset_train);
                    break; 
                
            case "Number of unseen labelsets":  value = metrics.UnseenInTrain(dataset_train,dataset_test);
                   break; 
            
            case "Ratio of unseen labelsets":  value = metrics.ratio_unseen_clases_over_number_DC_test(dataset_train, dataset_test, stat_test);
                   break; 
            
            case "Absolute difference between label cardinality of train and test sets":  value = metrics.AbsDifferenceOfCardinality_train_test(dataset_train, dataset_test, stat_train, stat_test);
                  break; 
                
                
            default:  value = -1.0;
                    break;    
                
        }
        
        }
        catch (Exception e) {
                e.printStackTrace();
            }
          
        if(Double.isNaN(value) || value == Double.POSITIVE_INFINITY || value == Double.NEGATIVE_INFINITY){
            System.out.println("isNaN");
            return("NaN");
        }
        else{
            NumberFormat formatter = new DecimalFormat("#0.000"); 
            //System.out.println("formatter: " + formatter.format(value) + "  value: " + value);
           return(formatter.format(value)); 
        }
    
    }   
    
    public static int Devuelve_cant_labels_x_IR(atributo[] data_imbalanced, double[] visitados , double current)
    {
        if (Esta_el_valor(visitados,current)) return -1;
        
        int cant_veces=0;
        
        for(int i=0; i<data_imbalanced.length;i++)
        {
            if(current > data_imbalanced[i].get_ir()) return cant_veces;
           if(current == data_imbalanced[i].get_ir()) cant_veces++;
        }
        
        return cant_veces;
    }
    
     public static int Devuelve_cant_labels_x_IR(double[] IR_inter_class, double[] visitados , double current)
    {
        if (Esta_el_valor(visitados,current)) return -1;
        
        int cant_veces=0;
        
        for(int i=0; i<IR_inter_class.length;i++)
        {
          //  if(current > IR_inter_class[i]) return cant_veces;
            if(current == IR_inter_class[i]) cant_veces++;
        }
        
        return cant_veces;
    }
    
    
    
    public static Object[] Get_values_x_fila(int num_fila, double[][] coefficient, String name_row )
    {
        Object[] fila = new Object[coefficient.length+1];
        String truncate;
        
        for(int i=0; i<fila.length;i++)
        {       
          if(i==0)
          {              
               fila[i]= name_row;
               continue;
          }
          
          
          if(coefficient[i-1][num_fila]==-1.0)
          {
              fila[i]= " ";
              continue;
          }
            
            
          if(i-1 != num_fila  )
          {
              truncate = Double.toString(coefficient[i-1][num_fila]);
              fila[i]= util.Truncate_values_aprox_zero(truncate, 4);
          }
                    
          else if (i-1==num_fila) {fila[i]= "---"; }
        }
        
        return fila;
    }
    
    
       
    public static String get_tabs_multi_datasets(String metric)
    {
       String value;
    switch (metric) 
        {     
        
            case "Instances": value ="\t"+"\t" +"\t"+"\t"+"\t";
                    break;                
                
            case "Attributes": value ="\t"+"\t" +"\t"+"\t"+"\t";
                     break;
            
            case "Labels":  value ="\t"+"\t" +"\t"+"\t"+"\t";
                      break;

            case "Label density": value ="\t"+"\t" +"\t"+"\t"+"\t";
                    break;                
               
            case "Distinct Labelset":  value = "\t"+"\t" +"\t"+"\t";
                     break;
            
            case "Proportion of Distinct Labelset":  value = "\t"+"\t" +"\t";
                     break;
                
            case "Density": value ="\t"+"\t" +"\t"+"\t"+"\t";
                     break;
                
            case "Cardinality":  value ="\t"+"\t" +"\t"+"\t"+"\t";
                     break;
                
            case "Bound":  value ="\t"+"\t" +"\t"+"\t"+"\t";
                     break;      
            
            case "Diversity":  value ="\t"+"\t" +"\t"+"\t"+"\t";
                   break;       
                                
            case "Proportion of unique label combination (PUniq)":  value ="\t"+"\t";
                     break;
            
            case "Proportion of maxim label combination (PMax)": value ="\t"+"\t";
                     break;
                
            case "Ratio of number of instances to the number of attributes": value ="\t";
                     break;
                
            case "Number of nominal attributes": value ="\t"+"\t" +"\t";
                     break;      
                
                            
            case "Labels x instances x features": value ="\t"+"\t" +"\t";
                     break;      
                    
                
            case "Number of binary attributes": value ="\t"+"\t" +"\t"+"\t";
                     break;   
                
             case "Proportion of binary attributes": value ="\t"+"\t" +"\t";
                     break;
            
            case "Proportion of nominal attributes": value ="\t"+"\t" +"\t";
                     break;
                
            case "Default accuracy":  value ="\t"+"\t" +"\t"+"\t";
                     break;
                
            case "Mean of mean of numeric attributes":  value ="\t"+"\t" +"\t";
                     break;
                
            case "Mean of standar deviation of numeric attributes":  value ="\t"+"\t";
                     break;      
                
            case "Mean of skewness of numeric attributes": value ="\t"+"\t" +"\t";
                     break;    
                
            case "Mean of kurtosis": value ="\t"+"\t" +"\t"+"\t";
                     break;
            
            case "Mean of entropy of nominal attributes":  value ="\t"+"\t" +"\t";
                     break;
                
           /* case "Mean of entropies (numeric attr)":  value ="\t"+"\t" +"\t"+"\t";
                     break;
            */    
            case "Average absolute correlation between numeric attributes":  value ="\t";
                     break;
                
            case "Proportion of numeric attributes with outliers": value ="\t"+"\t";
                     break;
                
            case "Average gain ratio": value ="\t"+"\t" +"\t"+"\t";
                     break;      
                
            case "Ratio of distinct classes to the total number label combinations": value ="\t"+"\t";
                     break;
            
            case "Standard desviation of the label cardinality": value ="\t"+"\t";
                     break;    
          
            case "Skewness cardinality":  value ="\t"+"\t" +"\t"+"\t";
                     break;
            
            case "CVIR inter class":  value ="\t"+"\t" +"\t"+"\t";
                     break;
            
            case "Kurtosis cardinality": value ="\t"+"\t" +"\t"+"\t";
                     break;
                
            case "Number of unconditionally dependent label pairs by chi-square test":  value ="\t";
                     break;
                
            case "Ratio of unconditionally dependent label pairs by chi-square test":  value ="\t";
                     break;
                
            case "Average of unconditionally dependent label pairs by chi-square test":  value ="\t";
                     break;      
                
            case "Number of labelsets up to 2 examples": value ="\t"+"\t" +"\t";
                     break;
            
            case "Number of labelsets up to 5 examples": value ="\t"+"\t" +"\t";
                     break;
                
            case "Number of labelsets up to 10 examples":  value ="\t"+"\t" +"\t";
                     break;
                
            case "Number of labelsets up to 50 examples":  value ="\t"+"\t" +"\t";
                     break;   
                
            case "Ratio of labelsets with number of examples < half of the attributes":  value ="\t";
                     break;    
                 
             case "Ratio of number of labelsets up to 2 examples":  value ="\t"+"\t";
                     break;
            
            case "Ratio of number of labelsets up to 5 examples": value ="\t"+"\t";
                     break;
                
            case "Ratio of number of labelsets up to 10 examples":  value ="\t"+"\t";
                     break;
                
            case "Ratio of number of labelsets up to 50 examples":  value ="\t"+"\t";
                     break;     
                
            case "Average examples per labelset": value ="\t"+"\t" +"\t";
                     break;
            
            case "Minimal entropy of labels":  value ="\t"+"\t" +"\t"+"\t";
                     break;
                
            case "Maximal entropy of labels": value ="\t"+"\t" +"\t"+"\t";
                     break;
                
            case "Mean entropy of labels":  value ="\t"+"\t" +"\t"+"\t";
                    break;
            
            case "Ratio of test instances over training instances":  value ="\t"+"\t" +"\t";
                    break;
                
            case "Standard desviation of examples per labelset":  value ="\t"+"\t";
                    break;
                
            case "Mean of IR per labelset":  value = "\t"+"\t" +"\t"+"\t";
                    break;                 
 
            case "Mean of standard deviation IR per label intra class":  value = "\t"+"\t";
                    break;
                
            case "Mean of IR per label intra class":  value ="\t"+"\t" +"\t";
                    break;
                
            case "Mean of IR per label inter class":  value ="\t"+"\t" +"\t";
                    break;
                
            case "Variance of examples per labelset":  value ="\t"+"\t" +"\t";
                    break;     
                
            case "Absolute difference between label cardinality of train and test sets":  value ="\t";
                    break;     
            
           case "Number distinct labelset found in train and test sets": value ="\t"+"\t";
                    break;  
           
          
           case "Ratio of distinct labelset found in both datasets over number of labelset": value ="\t";
                    break;  
                      
          case "Number of unseen labelsets": value ="\t"+"\t" +"\t"+"\t";
                    break;  
                              
          case "Ratio of unseen labelsets": value ="\t"+"\t"+"\t" +"\t";
                    break;  

                         
            default:  value ="\t"+"\t" +"\t"+"\t"+"\t";
                     break;    
        
        }
    return value;
    
    }
    
   public static String get_tabs(String metric)
   {
        String value;
        switch (metric) 
        {          
            case "Average absolute correlation between numeric attributes":  
                return("\t"+"\t"+"\t"+"\t"+"\t");
            
            case "Average examples per labelset":  
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
            
            case "Average gain ratio":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");

            case "Average of unconditionally dependent label pairs by chi-square test":
                return("\t");
            
            case "Bound":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+ "\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");   
             
            case "Cardinality":  
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+ "\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
               
            case "CVIR inter class":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
             
            case "Density":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+ "\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
             
            case "Distinct Labelset":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
            
            case "Diversity":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+ "\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
                
            case "Kurtosis cardinality":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
                
            case "Labels x instances x features":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
                
            case "Maximal entropy of labels":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
                
            case "Mean of entropy of nominal attributes":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
                
            case "Mean of standard deviation IR per label intra class":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t");
                
            case "Mean of IR per label intra class":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
            
            case "Mean of IR per label inter class":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
             
            case "Mean of IR per labelset":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
                
             case "Mean of mean of numeric attributes":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
                
            case "Mean of standar deviation of numeric attributes":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
                
            case "Mean of skewness of numeric attributes":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
                
            case "Mean of kurtosis":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
                
            case "Minimal entropy of labels":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
                
            case "Number of binary attributes":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
            
            case "Number of labelsets up to 2 examples":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
            
            case "Number of labelsets up to 5 examples":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
                
            case "Number of labelsets up to 10 examples":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
                
            case "Number of labelsets up to 50 examples":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
                
            case "Number of nominal attributes":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
                
            case "Number of unconditionally dependent label pairs by chi-square test":
                return("\t");
                
            case "Proportion of Distinct Labelset":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
            
            case "Proportion of maxim label combination (PMax)":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
            
            case "Proportion of numeric attributes with outliers":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
                
            case "Proportion of binary attributes":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
            
            case "Proportion of nominal attributes":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
                
            case "Proportion of unique label combination (PUniq)":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
              
            case "Ratio of labelsets with number of examples < half of the attributes":
                return("\t");
                
            case "Ratio of number of instances to the number of attributes":
                return("\t"+"\t"+"\t"+"\t");
               
            case "Ratio of number of labelsets up to 2 examples":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
            
            case "Ratio of number of labelsets up to 5 examples":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
                
            case "Ratio of number of labelsets up to 10 examples":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
                
            case "Ratio of number of labelsets up to 50 examples":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
                
            case "Ratio of unconditionally dependent label pairs by chi-square test":
                return("\t");
                
            case "Skewness cardinality":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
            
            case "Standard desviation of the label cardinality":
                return("\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
                
            case "Standard desviation of examples per labelset":
                return("\t"+"\t" +"\t"+"\t"+"\t"+"\t"+"\t"+"\t");
                
                
                
                
                
                
            case "Label density":  value =  "\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t";
                    break;                
                
            case "Instances":  value = "\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t";
                     break;
            
            case "Label Cardinality":  value = "\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t";
                      break;

            case "Default accuracy":  value ="\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t";
                     break;

           /* case "Mean of entropies (numeric attr)":  value = "\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t";
                     break;                
             */   

            case "Ratio of distinct classes to the total number label combinations":  value = "\t"+"\t"+"\t"+"\t";
                     break;

            case "Mean entropy of labels":  value = "\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t";
                    break;
            
            case "Ratio of test instances over training instances":   value ="\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t";
                    break;
                
            case "Number distinct labelset found in train and test sets":  value ="\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t";
                    break;
                
            case "Ratio of distinct labelset found in both datasets over number of labelset":  value ="\t"+"\t";
                    break;
                
            case "Absolute difference between label cardinality of train and test sets":  value ="\t"+"\t"+"\t";
                    break;                

            case "Number of unseen labelsets":   value ="\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t";
                    break;                
                                  
            case "Ratio of unseen labelsets":   value ="\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t";
                    break; 

            case "Variance of examples per labelset":   value ="\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t";
                    break;

            default:  value ="\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t";
                     break;    
        
        }
    return value;
   
   }
   
   
    public static String Cambia_espacios_por_(String metric)
    {
        String result="";
        for(int i=0; i<metric.length();i++)
        {
            if(metric.charAt(i)==' ') {
                result+="_";
            } 
            else {
                result+=metric.charAt(i);
            }
            
        }        
 
        return result;
    }
    
    public static void Save_xml_in_the_file(PrintWriter wr, String path) throws IOException
    {
       FileReader fr = new FileReader(path);
       BufferedReader bf = new BufferedReader(fr);
       
       String sCadena = "";
       
        while ((sCadena = bf.readLine())!=null) 
       	{
            wr.write(sCadena);
            wr.write(System.getProperty("line.separator")); 
        }
        
    
    }
    
    public static void Save_dataset_in_the_file(ArrayList<MultiLabelInstances> dataset, String path,String dataset_name, String type) throws IOException
    {
      BufferedWriter  bw_current;
      PrintWriter wr;        
              
       // new BufferedWriter(new FileWriter(path));
       // new PrintWriter(bw_train);
      int index=1;
      String current_path;
      
      for(MultiLabelInstances dataset_current : dataset)
      {
          current_path = path + "/"+dataset_name+"-"+type+index+".arff";
          
          bw_current = new BufferedWriter(new FileWriter(current_path));
          wr = new PrintWriter(bw_current);
          
          Save_dataset_in_the_file(wr,dataset_current);
          
          wr.close();
          bw_current.close();
          
          index++;
      }
        
    }
    
    public static void Save_dataset_in_the_file(PrintWriter wr, MultiLabelInstances dataset)
    {
        Save_dataset_in_the_file(wr, dataset, dataset.getDataSet().relationName());   
    }
    
    public static void Save_dataset_in_the_file(PrintWriter wr, MultiLabelInstances dataset, String relationName)
    {
       wr.write("@relation " + relationName);
       wr.write(System.getProperty("line.separator"));  
       //wr.write(System.getProperty("line.separator"));  
        
        //Set<Attribute> attributeSet = dataset.getFeatureAttributes();
        Instances instancias = dataset.getDataSet();
       
        Attribute att;
       for (int i=0; i< instancias.numAttributes();i++)
       {
             att = instancias.attribute(i);
             wr.write(att.toString());
             wr.write(System.getProperty("line.separator")); 
       }   
       
       //wr.write(System.getProperty("line.separator")); 
        
        String current ;
        
        wr.write("@data");
        wr.write(System.getProperty("line.separator"));  
        for(int i=0; i<dataset.getNumInstances();i++)
        {
          current = dataset.getDataSet().get(i).toString();
          wr.write(current);
          wr.write(System.getProperty("line.separator"));  
        }
        
    }
    
    public static void Save_in_the_file_arff_mulan(PrintWriter wr,ArrayList<String> metric_list, MultiLabelInstances dataset_train, MultiLabelInstances dataset_test , String relation_name, boolean es_de_tipo_meka)
     {
       relation_name = "@relation "+"'"+relation_name+"'";
       
       wr.write(relation_name);
       wr.write(System.getProperty("line.separator"));  
       wr.write(System.getProperty("line.separator"));  
       
       //set attributes
        for(String metric : metric_list)
        {
          wr.write("@attribute "+Cambia_espacios_por_(metric)+" numeric");
          wr.write(System.getProperty("line.separator"));  
          
        }
        wr.write(System.getProperty("line.separator"));  
       
        //set data
        wr.write("@data ");
        wr.write(System.getProperty("line.separator"));  
               
        String value,temp="";
        
        atributo[] imbalanced_data_train, imbalanced_data_test, label_frenquency_train, label_frenquency_test;
        
        label_frenquency_train = util.Get_Frequency_x_label(dataset_train);
        label_frenquency_train = util.Ordenar_freq_x_attr(label_frenquency_train);// ordena de mayor a menor             
        imbalanced_data_train = util.Get_data_imbalanced_x_label_inter_class(dataset_train,label_frenquency_train);
        
        label_frenquency_test = util.Get_Frequency_x_label(dataset_test);
        label_frenquency_test = util.Ordenar_freq_x_attr(label_frenquency_test);// ordena de mayor a menor
        imbalanced_data_test = util.Get_data_imbalanced_x_label_inter_class(dataset_test,label_frenquency_test);
        
        
            //TRAIN
             value="";  
             for(String metric : metric_list)
             {            
                 if(metric.equals("Ratio of unseen labelsets") || metric.equals("Number of unseen labelsets"))temp ="NaN";
                     
                 else
                 {    
                   temp = util.get_value_metric(metric, dataset_train, es_de_tipo_meka);
                   if(temp.equals("-1.0"))temp = get_alternative_metric(metric, dataset_train, dataset_test);
                   if(temp.equals("-1.0"))temp = get_value_metric_imbalanced(metric, dataset_train, imbalanced_data_train);
                   if(temp.equals("-1.0")) temp ="NaN";
                 }

                 value+= temp +",";
             }     
            wr.write(value.substring(0,value.length()-1) );
            wr.write(System.getProperty("line.separator"));  

            
             //Test
             value="";  
             for(String metric : metric_list)
             {            
                  temp = util.get_value_metric(metric, dataset_test, es_de_tipo_meka);
                   if(temp.equals("-1.0"))temp = get_alternative_metric(metric, dataset_train, dataset_test);
                   if(temp.equals("-1.0"))temp = get_value_metric_imbalanced(metric, dataset_test, imbalanced_data_test);
                   if(temp.equals("-1.0")) temp ="NaN";
                   
                   value+= temp +",";
             }     
            wr.write(value.substring(0,value.length()-1) );
            wr.write(System.getProperty("line.separator"));  
        
         
     }
     
    
     public static void Save_in_the_file_arff_meka(PrintWriter wr,ArrayList<String> metric_list, MultiLabelInstances dataset_train, MultiLabelInstances dataset_test , String relation_name, boolean es_de_tipo_meka)
     {
       //write the relation name 'tt: -C 0'
       relation_name = "@relation "+"'"+relation_name+": -C 0'";
       
       wr.write(relation_name);
       wr.write(System.getProperty("line.separator"));  
       wr.write(System.getProperty("line.separator"));  
       
       //set attributes
        for(String metric : metric_list)
        {
          wr.write("@attribute "+Cambia_espacios_por_(metric)+" numeric");
          wr.write(System.getProperty("line.separator"));  
          
        }
        wr.write(System.getProperty("line.separator"));  
       
        //set data
        wr.write("@data ");
        wr.write(System.getProperty("line.separator"));  
               
        String value,temp="";
        
        atributo[] imbalanced_data_train, imbalanced_data_test, label_frenquency_train, label_frenquency_test;
        
        label_frenquency_train = util.Get_Frequency_x_label(dataset_train);
        label_frenquency_train = util.Ordenar_freq_x_attr(label_frenquency_train);// ordena de mayor a menor             
        imbalanced_data_train = util.Get_data_imbalanced_x_label_inter_class(dataset_train,label_frenquency_train);
        
        label_frenquency_test = util.Get_Frequency_x_label(dataset_test);
        label_frenquency_test = util.Ordenar_freq_x_attr(label_frenquency_test);// ordena de mayor a menor
        imbalanced_data_test = util.Get_data_imbalanced_x_label_inter_class(dataset_test,label_frenquency_test);
        
        
            //TRAIN
             value="";  
             for(String metric : metric_list)
             {            
                 if(metric.equals("Ratio of unseen labelsets") || metric.equals("Number of unseen labelsets"))temp ="NaN";
                     
                 else
                 {    
                   temp = util.get_value_metric(metric, dataset_train, es_de_tipo_meka);
                   if(temp.equals("-1.0"))temp = get_alternative_metric(metric, dataset_train, dataset_test);
                   if(temp.equals("-1.0"))temp = get_value_metric_imbalanced(metric, dataset_train, imbalanced_data_train);
                   if(temp.equals("-1.0")) temp ="NaN";
                 }

                 value+= temp +",";
             }     
            wr.write(value.substring(0,value.length()-1) );
            wr.write(System.getProperty("line.separator"));  

            
             //Test
             value="";  
             for(String metric : metric_list)
             {            
                  temp = util.get_value_metric(metric, dataset_test, es_de_tipo_meka);
                   if(temp.equals("-1.0"))temp = get_alternative_metric(metric, dataset_train, dataset_test);
                   if(temp.equals("-1.0"))temp = get_value_metric_imbalanced(metric, dataset_test, imbalanced_data_test);
                   if(temp.equals("-1.0")) temp ="NaN";
                   
                   value+= temp +",";
             }     
            wr.write(value.substring(0,value.length()-1) );
            wr.write(System.getProperty("line.separator"));  
        
         
     }
     
     
      public static void Save_in_the_file_arff_mulan(PrintWriter wr,ArrayList<String> metric_list, ArrayList<MultiLabelInstances> dataset_train_list, ArrayList<MultiLabelInstances> dataset_test_list , String relation_name, boolean es_de_tipo_meka)
     {
         relation_name = "@relation "+"'"+relation_name+"'";
       
       wr.write(relation_name);
       wr.write(System.getProperty("line.separator"));  
       wr.write(System.getProperty("line.separator"));  
       
       //set attributes
        for(String metric : metric_list)
        {
          wr.write("@attribute "+Cambia_espacios_por_(metric)+" numeric");
          wr.write(System.getProperty("line.separator"));  
          
        }
        wr.write(System.getProperty("line.separator"));  
       
        //set data
        wr.write("@data ");
        wr.write(System.getProperty("line.separator"));  
               
        String value,temp="";
        
        atributo[] imbalanced_data_train, imbalanced_data_test, label_frenquency_train, label_frenquency_test;
        
        
        MultiLabelInstances current_train, current_test;
        
        for(int i=0; i<dataset_test_list.size();i++)
        {
            current_train = dataset_train_list.get(i);
            current_test = dataset_test_list.get(i);
        
            label_frenquency_train = util.Get_Frequency_x_label(current_train);
            label_frenquency_train = util.Ordenar_freq_x_attr(label_frenquency_train);// ordena de mayor a menor             
            imbalanced_data_train = util.Get_data_imbalanced_x_label_inter_class(current_train,label_frenquency_train);
            
            //TRAIN
             value="";  
             for(String metric : metric_list)
             {            
                 if(metric.equals("Ratio of unseen labelsets") || metric.equals("Number of unseen labelsets"))temp ="NaN";
                     
                 else
                 {    
                   temp = util.get_value_metric(metric, current_train, es_de_tipo_meka);
                   if(temp.equals("-1.0"))temp = get_alternative_metric(metric, current_train, current_test);
                   if(temp.equals("-1.0"))temp = get_value_metric_imbalanced(metric, current_train, imbalanced_data_train);
                   if(temp.equals("-1.0")) temp ="NaN";
                 }

                 value+= temp +",";
             } 
             
            wr.write(value.substring(0,value.length()-1) );
            wr.write(System.getProperty("line.separator"));  

            
            label_frenquency_test = util.Get_Frequency_x_label(current_test);
            label_frenquency_test = util.Ordenar_freq_x_attr(label_frenquency_test);// ordena de mayor a menor
            imbalanced_data_test = util.Get_data_imbalanced_x_label_inter_class(current_test,label_frenquency_test);
            
             //Test
             value="";  
             for(String metric : metric_list)
             {            
                  temp = util.get_value_metric(metric, current_test, es_de_tipo_meka);
                   if(temp.equals("-1.0"))temp = get_alternative_metric(metric, current_train, current_test);
                   if(temp.equals("-1.0"))temp = get_value_metric_imbalanced(metric, current_test, imbalanced_data_test);
                   if(temp.equals("-1.0")) temp ="NaN";
                   
                   value+= temp +",";
             }     
            wr.write(value.substring(0,value.length()-1) );
            wr.write(System.getProperty("line.separator"));  
        
        }
         
     }
    
     
     
      public static void Save_in_the_file_arff_meka(PrintWriter wr,ArrayList<String> metric_list, ArrayList<MultiLabelInstances> dataset_train_list, ArrayList<MultiLabelInstances> dataset_test_list , String relation_name, boolean es_de_tipo_meka)
     {
       //write the relation name 'tt: -C 0'
       relation_name = "@relation "+"'"+relation_name+": -C 0'";
       
       wr.write(relation_name);
       wr.write(System.getProperty("line.separator"));  
       wr.write(System.getProperty("line.separator"));  
       
       //set attributes
        for(String metric : metric_list)
        {
          wr.write("@attribute "+Cambia_espacios_por_(metric)+" numeric");
          wr.write(System.getProperty("line.separator"));  
          
        }
        wr.write(System.getProperty("line.separator"));  
       
        //set data
        wr.write("@data ");
        wr.write(System.getProperty("line.separator"));  
               
        String value,temp="";
        
        atributo[] imbalanced_data_train, imbalanced_data_test, label_frenquency_train, label_frenquency_test;
        
        
        MultiLabelInstances current_train, current_test;
        
        for(int i=0; i<dataset_test_list.size();i++)
        {
            current_train = dataset_train_list.get(i);
            current_test = dataset_test_list.get(i);
        
            label_frenquency_train = util.Get_Frequency_x_label(current_train);
            label_frenquency_train = util.Ordenar_freq_x_attr(label_frenquency_train);// ordena de mayor a menor             
            imbalanced_data_train = util.Get_data_imbalanced_x_label_inter_class(current_train,label_frenquency_train);
            
            //TRAIN
             value="";  
             for(String metric : metric_list)
             {            
                 if(metric.equals("Ratio of unseen labelsets") || metric.equals("Number of unseen labelsets"))temp ="NaN";
                     
                 else
                 {    
                   temp = util.get_value_metric(metric, current_train, es_de_tipo_meka);
                   if(temp.equals("-1.0"))temp = get_alternative_metric(metric, current_train, current_test);
                   if(temp.equals("-1.0"))temp = get_value_metric_imbalanced(metric, current_train, imbalanced_data_train);
                   if(temp.equals("-1.0")) temp ="NaN";
                 }

                 value+= temp +",";
             } 
             
            wr.write(value.substring(0,value.length()-1) );
            wr.write(System.getProperty("line.separator"));  

            
            label_frenquency_test = util.Get_Frequency_x_label(current_test);
            label_frenquency_test = util.Ordenar_freq_x_attr(label_frenquency_test);// ordena de mayor a menor
            imbalanced_data_test = util.Get_data_imbalanced_x_label_inter_class(current_test,label_frenquency_test);
            
             //Test
             value="";  
             for(String metric : metric_list)
             {            
                  temp = util.get_value_metric(metric, current_test, es_de_tipo_meka);
                   if(temp.equals("-1.0"))temp = get_alternative_metric(metric, current_train, current_test);
                   if(temp.equals("-1.0"))temp = get_value_metric_imbalanced(metric, current_test, imbalanced_data_test);
                   if(temp.equals("-1.0")) temp ="NaN";
                   
                   value+= temp +",";
             }     
            wr.write(value.substring(0,value.length()-1) );
            wr.write(System.getProperty("line.separator"));  
        
        }
         
     }
    
    
    public static void Save_in_the_file_arff_meka(PrintWriter wr,ArrayList<String> metric_list, ArrayList<MultiLabelInstances> list_datasets, String relation_name, boolean es_de_tipo_meka)
   {
       //write the relation name 'tt: -C 0'
       relation_name = "@relation "+"'"+relation_name+": -C 0'";
       
       wr.write(relation_name);
       wr.write(System.getProperty("line.separator"));  
       wr.write(System.getProperty("line.separator"));  
       
       //set attributes
        for(String metric : metric_list)
        {
          wr.write("@attribute "+Cambia_espacios_por_(metric)+" numeric");
          wr.write(System.getProperty("line.separator"));  
          
        }
        wr.write(System.getProperty("line.separator"));  
       
        //set data
        wr.write("@data ");
        wr.write(System.getProperty("line.separator"));  
               
        String value,temp="";
        
        atributo[] imbalanced_data, label_frenquency;
        
          for(MultiLabelInstances current : list_datasets)
          {  
             value="";  
             for(String metric : metric_list)
             {
                label_frenquency = util.Get_Frequency_x_label(current);
                label_frenquency = util.Ordenar_freq_x_attr(label_frenquency);// ordena de mayor a menor
             
                imbalanced_data = util.Get_data_imbalanced_x_label_inter_class(current,label_frenquency);
                
               // imbalanced_data = Get_data_imbalanced_x_label(current);
                   temp = util.get_value_metric(metric, current, es_de_tipo_meka);
                   
                   if(temp.equals("-1.0"))temp = get_value_metric_imbalanced(metric, current, imbalanced_data);
                   if(temp.equals("-1.0")) temp ="NaN";
                   
                   value+= temp +",";
             }     
            wr.write(value.substring(0,value.length()-1) );
            wr.write(System.getProperty("line.separator"));  
          }
          
   }
   
    public static void Save_in_the_file_arff(PrintWriter wr,ArrayList<String> metric_list, ArrayList<MultiLabelInstances> list_datasets, String relation_name, boolean es_de_tipo_meka)
   {
       //write the relation name
       wr.write("@relation "+relation_name);
       wr.write(System.getProperty("line.separator"));  
       wr.write(System.getProperty("line.separator"));  
       
       //set attributes
        for(String metric : metric_list)
        {
          wr.write("@attribute "+Cambia_espacios_por_(metric)+" numeric");
          wr.write(System.getProperty("line.separator"));  
          
        }
        wr.write(System.getProperty("line.separator"));  
       
        //set data
        wr.write("@data ");
        wr.write(System.getProperty("line.separator"));  
               
        String value,temp="";
        
        atributo[] imbalanced_data;
        
          for(MultiLabelInstances current : list_datasets)
          {  
             value="";  
             for(String metric : metric_list)
             {
                   imbalanced_data = Get_data_imbalanced_x_label(current);
                   temp = util.get_value_metric(metric, current, es_de_tipo_meka);
                   
                   if(temp.equals("-1.0"))temp = get_value_metric_imbalanced(metric, current, imbalanced_data);
                     value+= temp +",";
             }     
            wr.write(value.substring(0,value.length()-1) );
            wr.write(System.getProperty("line.separator"));  
          }
          
   }
    public static void Save_in_the_file_csv(PrintWriter wr,ArrayList<String> metric_list_comun, ArrayList<String>  metric_list_train, ArrayList<String> metric_list_test,ArrayList<MultiLabelInstances> list_dataset_train ,ArrayList<MultiLabelInstances> list_dataset_test, String dataset_current_name, boolean es_de_tipo_meka)
    {
          
          String dataset_names = "Relation Name;"+dataset_current_name;        
          wr.write(dataset_names);
          wr.write(System.getProperty("line.separator"));  
   
          String temp;
          //jTextArea1.append("Metrics for train/test set:"+"\n");
          
         
          wr.write(System.getProperty("line.separator"));  
          wr.write("Metrics for train/test set");
          wr.write(System.getProperty("line.separator"));  
          
          ArrayList<Double> lista_valores;
          double current_double;
          String value_truncate;
          
         for(String metrica : metric_list_comun)
        {
           lista_valores= new ArrayList();
           current_double=-1.0;
           
                      
           for(int n=0; n<list_dataset_train.size();n++)
            {
                 temp= util.get_value_metric(metrica, list_dataset_train.get(n), es_de_tipo_meka); //can  be dataset_test too
           
                 if(temp.equals( "-1.0" )) temp = util.get_alternative_metric(metrica, list_dataset_train.get(n), list_dataset_test.get(n));
           
                 current_double = Double.parseDouble(temp);
                 
                 if(Double.isNaN(current_double) || current_double==-1.0)break;
                 
                 lista_valores.add(current_double);
                  
            }
            
             if(Double.isNaN(current_double)) 
             {
                 wr.write(metrica+";"+"NaN" );
                 wr.write(System.getProperty("line.separator"));  
             
             }
             
             else if (current_double==-1.0)
             {
                 wr.write(metrica+";"+"-1,0" );
                 wr.write(System.getProperty("line.separator"));  
             }
    
             else
             {             
                 value_truncate = util.Truncate_value(util.Get_media(lista_valores), 5);
                 value_truncate =Cambia_pto_x_coma(value_truncate);
                 wr.write(metrica+";"+value_truncate );
                 wr.write(System.getProperty("line.separator"));
             }
           
           
           
           /*
           value= util.get_value_metric(metrica, dataset_train); //can  be dataset_test too
           
           if(value.equals( "-1.0" )) value = util.get_alternative_metric(metrica, dataset_train, dataset_test);
           
           value = Cambia_pto_x_coma(value);
           
          wr.write(metrica+";"+value);
          wr.write(System.getProperty("line.separator"));   
          * */
        }
        
       
          wr.write(System.getProperty("line.separator")); 
          
          
          wr.write("Metrics for ;train set;test set");
          wr.write(System.getProperty("line.separator")); 
 
       
        String value_test, value_train,value_truncate_test;
        double current_double_train,current_double_test;
              
        atributo[] imbalanced_data_train;
        atributo[] imbalanced_data_test;
        
        ArrayList<Double> lista_valores_test=new ArrayList();
        
        for(String metrica : metric_list_test)
        {
            lista_valores= new ArrayList();
            current_double_train=-1.0;    
            current_double_test=-1.0;    
            
           for(int n=0; n<list_dataset_train.size();n++)
            {
                imbalanced_data_train =  util.Get_data_imbalanced_x_label(list_dataset_train.get(n));
                imbalanced_data_test =   util.Get_data_imbalanced_x_label(list_dataset_test.get(n));
                             
                 value_train= util.get_value_metric(metrica, list_dataset_train.get(n), es_de_tipo_meka); 
                 value_test= util.get_value_metric(metrica, list_dataset_test.get(n), es_de_tipo_meka); 
                 
                if(value_train.equals( "-1.0" )) value_train = util.get_value_metric_imbalanced(metrica, list_dataset_train.get(n), imbalanced_data_train);
                if(value_test.equals( "-1.0" )) value_test = util.get_value_metric_imbalanced(metrica, list_dataset_test.get(n), imbalanced_data_test);
                
                current_double_train = Double.parseDouble(value_train);
                current_double_test = Double.parseDouble(value_test);
                
                if(!(Double.isNaN(current_double_train)))lista_valores.add(current_double_train);
                if(!(Double.isNaN(current_double_test)))lista_valores_test.add(current_double_test); 
                
                  
            }
           
             if(lista_valores.isEmpty())//train 
             {
                 if(lista_valores_test.isEmpty())
                 {
                    wr.write(metrica+";"+"NaN"+";"+"NaN" );
                    wr.write(System.getProperty("line.separator"));  
                 }
                 else 
                 {
                    value_truncate_test = util.Truncate_value(util.Get_media(lista_valores_test), 5);
                    value_truncate_test =Cambia_pto_x_coma(value_truncate_test);
                 
                    wr.write(metrica+";"+"NaN"+";"+value_truncate_test );
                    wr.write(System.getProperty("line.separator"));  
                 }
                 
             }
                
             else
             { 
                 if(lista_valores_test.isEmpty())
                 {
                                          
                    value_truncate = util.Truncate_value(util.Get_media(lista_valores), 5);
                    value_truncate = Cambia_pto_x_coma(value_truncate);
                    
                    wr.write(metrica+";"+value_truncate+";"+"NaN" );
                    wr.write(System.getProperty("line.separator"));  
                 }
                  else
                  {
                    
                    if(metrica.equals("Number of unseen labelsets")|| metrica.equals("Ratio of unseen labelsets"))
                     {
                        value_truncate_test = util.Truncate_value(util.Get_media(lista_valores_test), 5);
                        value_truncate_test =Cambia_pto_x_coma(value_truncate_test);
                                     
                        wr.write(metrica+";"+"NaN"+";"+value_truncate_test );
                        wr.write(System.getProperty("line.separator")); 
                     }
                      
                    else
                    {
                        value_truncate = util.Truncate_value(util.Get_media(lista_valores), 5);
                        value_truncate = Cambia_pto_x_coma(value_truncate);
                    
                        value_truncate_test = util.Truncate_value(util.Get_media(lista_valores_test), 5);
                        value_truncate_test =Cambia_pto_x_coma(value_truncate_test);
                 
                    
                        wr.write(metrica+";"+value_truncate+";"+value_truncate_test );
                        wr.write(System.getProperty("line.separator")); 
                    }
                  }
             }
           }
            /*
           value_train = util.get_value_metric(metrica, dataset_train);
           if(value_train.equals("-1.0")) value_train = util.get_value_metric_imbalanced(metrica, dataset_train, imbalanced_data_train); 	
           
           value_test= util.get_value_metric(metrica, dataset_test);
           if(value_test.equals("-1.0")) value_test = util.get_value_metric_imbalanced(metrica, dataset_test, imbalanced_data_test); 	
          
           if (value_test.equals("-1.0"))  value_test= util.get_alternative_metric(metrica,dataset_train, dataset_test);
           
           
           value_test = Cambia_pto_x_coma(value_test);
           value_train = Cambia_pto_x_coma(value_train);
           
           if(value_train.equals("-1,0"))wr.write(metrica+";"+ "NaN"+";"+value_test);
                  
            else wr.write(metrica+";"+ value_train+";"+value_test);
           wr.write(System.getProperty("line.separator"));
           * */

        
           
       
       
    
        
        
    }
    
   public static void Save_in_the_file_csv(PrintWriter wr,ArrayList<String> metric_list_comun, ArrayList<String>  metric_list_train, ArrayList<String> metric_list_test, MultiLabelInstances dataset_train , MultiLabelInstances dataset_test,String dataset_current_name, boolean es_de_tipo_meka)
   {
        Instances i1= dataset_train.getDataSet();
          
          String dataset_names ="Relation name;"+ dataset_current_name;        
          wr.write(dataset_names);
          wr.write(System.getProperty("line.separator"));  
   
          //cant de atributos
          
          int num_atributos= i1.numAttributes();
          int numero_etiquetas = dataset_train.getNumLabels();
          
          //Attributes
          wr.write("Attributes;"+Integer.toString(num_atributos-numero_etiquetas));
          wr.write(System.getProperty("line.separator"));   
                  
         //cant de etiquetas
          wr.write("Labels;"+Integer.toString(numero_etiquetas));
          wr.write(System.getProperty("line.separator"));   
            
          String value;
          //jTextArea1.append("Metrics for train/test set:"+"\n");
          
         
          wr.write(System.getProperty("line.separator"));  
          wr.write("Metrics for train/test set");
          wr.write(System.getProperty("line.separator"));  
          
        for(String metrica : metric_list_comun)
        {
           value= util.get_value_metric(metrica, dataset_train, es_de_tipo_meka); //can  be dataset_test too
           
           if(value.equals( "-1.0" )) value = util.get_alternative_metric(metrica, dataset_train, dataset_test);
           
           value = Cambia_pto_x_coma(value);
           
          wr.write(metrica+";"+value);
          wr.write(System.getProperty("line.separator"));   
        }
        
       
          wr.write(System.getProperty("line.separator")); 
          
          
          wr.write("Metrics for ;train set;test set");
          wr.write(System.getProperty("line.separator")); 
 
       
        String value_test, value_train;
        atributo[] imbalanced_data_train =  util.Get_data_imbalanced_x_label(dataset_train); 
        atributo[] imbalanced_data_test =  util.Get_data_imbalanced_x_label(dataset_test); 
          
        for(String metrica : metric_list_test)
        {
           value_train = util.get_value_metric(metrica, dataset_train, es_de_tipo_meka);
           if(value_train.equals("-1.0")) value_train = util.get_value_metric_imbalanced(metrica, dataset_train, imbalanced_data_train); 	
           
           value_test= util.get_value_metric(metrica, dataset_test, es_de_tipo_meka);
           if(value_test.equals("-1.0")) value_test = util.get_value_metric_imbalanced(metrica, dataset_test, imbalanced_data_test); 	
          
           if (value_test.equals("-1.0"))  value_test= util.get_alternative_metric(metrica,dataset_train, dataset_test);
           
           
           value_test = Cambia_pto_x_coma(value_test);
           value_train = Cambia_pto_x_coma(value_train);
           
           if(value_train.equals("-1,0"))wr.write(metrica+";"+ "NaN"+";"+value_test);
                  
            else wr.write(metrica+";"+ value_train+";"+value_test);
           wr.write(System.getProperty("line.separator"));
        }
       
        
   }
   
   public static String Cambia_pto_x_coma(String valor)
   {
       String nuevo_valor="";
       for(int i=0;i<valor.length();i++)
       {
           if(valor.charAt(i)=='.')nuevo_valor+=',';
           else nuevo_valor+= valor.charAt(i);
       }
       return nuevo_valor;
   }
   
   
     public static void Save_in_the_file_txt(PrintWriter wr, ArrayList<String> metric_list, ArrayList<String> metric_list1, ArrayList<String> metric_list2 , ArrayList<MultiLabelInstances> list_datasets_train, ArrayList<MultiLabelInstances> list_datasets_test, boolean es_de_tipo_meka)
     {
                  
        boolean flag=true;
        
               
        String encabezado = "Commun Metrics for train/test set:" +"\t"+"\t"+"\t"+"\t" +"\t"+"\t"+"\t"+"\t" +"\t"+"\t"+"\t"+"\t";
        
        String temp,temp1,value="";
        
        /*
         *          wr.write(encabezado );
         wr.write(System.getProperty("line.separator"));  
         */
          wr.write(System.getProperty("line.separator")); 
          wr.write("---------------------------------------");
          wr.write(System.getProperty("line.separator"));  

        ArrayList<Double> lista_valores;
        double current_double;
        String value_truncate;
        
        
         for(String metric : metric_list)
        {
            lista_valores= new ArrayList();
            current_double=-1.0;
            
            value= metric+":"+ util.get_tabs(metric);
            
           // for( int i=0; i<list_datasets_train.size();i++)
                if(flag){ encabezado+= "Fold" ;}
            
            if(flag)
            {
                flag =false;
               wr.write(encabezado );
               wr.write(System.getProperty("line.separator")); 
               wr.write("---------------------------------------");
               wr.write(System.getProperty("line.separator"));  
                
            }
            
            for(int n=0; n<list_datasets_train.size();n++)
            {
                 temp= util.get_value_metric(metric, list_datasets_train.get(n), es_de_tipo_meka); //can  be dataset_test too
           
                 if(temp.equals( "-1.0" )) temp = util.get_alternative_metric(metric, list_datasets_train.get(n), list_datasets_test.get(n));
           
                 current_double = Double.parseDouble(temp);
                 
                 if(Double.isNaN(current_double) || current_double==-1.0)break;
                 
                 lista_valores.add(current_double);
                  
            }
            
             if(Double.isNaN(current_double)) 
             {
                 wr.write(value+"NaN" );
                 wr.write(System.getProperty("line.separator"));  
             
             }
             
             else if (current_double==-1.0)
             {
                 wr.write(value+"-1.0" );
                 wr.write(System.getProperty("line.separator"));  
             }
    
             else
             {             
                 value_truncate = util.Truncate_value(util.Get_media(lista_valores), 5);
                 wr.write(value+value_truncate );
                 wr.write(System.getProperty("line.separator"));
             }
				 
            
        }
         
       //metrics train
        wr.write(System.getProperty("line.separator"));  
        wr.write(System.getProperty("line.separator"));  
        

          wr.write("---------------------------------------");
          wr.write(System.getProperty("line.separator"));   

         
        encabezado = "Metrics for train set:"+"\t"+"\t" +"\t"+"\t"+"\t"+"\t" +"\t"+"\t"+"\t"+"\t" +"\t"+"\t"+"\t"+"\t" +"\t";
        
        atributo[] imbalanced_data,imbalanced_data1,label_frenquency,label_frenquency1;
        MultiLabelInstances current_train, current_test;
        
        flag =true;
        
        for(String metric : metric_list1)
        {
            lista_valores= new ArrayList();
            current_double=-1.0;     
            
            value= metric+":"+ util.get_tabs(metric);
            
           // for( int i=0; i<list_datasets_train.size();i++)
                if(flag){ encabezado+= "Train fold";}
            
            if(flag)
            {
                flag =false;
                wr.write(encabezado );
                wr.write(System.getProperty("line.separator")); 
                wr.write("---------------------------------------");
                wr.write(System.getProperty("line.separator"));  
            }
            
            for(int n=0; n<list_datasets_train.size();n++)
            {
                current_train = list_datasets_train.get(n);
                label_frenquency = util.Get_Frequency_x_label(current_train);
                label_frenquency = util.Ordenar_freq_x_attr(label_frenquency);// ordena de mayor a menor
                
                imbalanced_data = util.Get_data_imbalanced_x_label_inter_class(current_train,label_frenquency);
                
                             
                 temp= util.get_value_metric(metric, list_datasets_train.get(n), es_de_tipo_meka); 
                               
                if(temp.equals( "-1.0" )) temp = util.get_value_metric_imbalanced(metric, list_datasets_train.get(n), imbalanced_data);
                if(temp.equals( "-1.0" )) temp ="NaN";
                
                current_double = Double.parseDouble(temp);
                 
                if(Double.isNaN(current_double) || current_double==-1.0)break;
                 
                lista_valores.add(current_double);
                  
            }
            
             if(Double.isNaN(current_double))
             {
                wr.write(value +"NaN");
                wr.write(System.getProperty("line.separator"));  
             }   
             
             else if (current_double==-1.0)
             {
                wr.write(value +current_double);
                wr.write(System.getProperty("line.separator"));  
                
             }   
    
             else
             {             
                 value_truncate = util.Truncate_value(util.Get_media(lista_valores), 5);
                 wr.write(value +value_truncate);
                 wr.write(System.getProperty("line.separator"));  
             }
                        
        }
        
         //metrics test
         wr.write(System.getProperty("line.separator")); 
         wr.write(System.getProperty("line.separator")); 
         wr.write("---------------------------------------");
         wr.write(System.getProperty("line.separator")); 
         
         encabezado = "Metrics for test set:"+"\t"+"\t" +"\t"+"\t"+"\t"+"\t" +"\t"+"\t"+"\t"+"\t" +"\t"+"\t"+"\t"+"\t" +"\t";
              
         flag =true;
        
        for(String metric : metric_list2)
        {
            if(metric.equals("Number of unseen labelsets"))value= metric+":"+ util.get_tabs(metric);
            
            else value= metric+":"+ util.get_tabs(metric);
            
            lista_valores= new ArrayList();
            current_double=-1.0;
            
           // for( int i=0; i<list_datasets_train.size();i++)
                if(flag){ encabezado+= "Test fold";}
            
            if(flag)
            {
                flag =false;
                
                wr.write(encabezado );
                
                wr.write(System.getProperty("line.separator")); 
                wr.write("---------------------------------------");
                wr.write(System.getProperty("line.separator"));
                  
            }
            
            for(int n=0; n<list_datasets_test.size();n++)
            {                
                current_test = list_datasets_test.get(n);
                
                label_frenquency1 = util.Get_Frequency_x_label(current_test);
                label_frenquency1 = util.Ordenar_freq_x_attr(label_frenquency1);// ordena de mayor a menor
                
                imbalanced_data1 = util.Get_data_imbalanced_x_label_inter_class(current_test,label_frenquency1);
                               
                 temp1= util.get_value_metric(metric, list_datasets_test.get(n), es_de_tipo_meka); 
                 
                 if(temp1.equals( "-1.0" )) temp1 = util.get_alternative_metric(metric, list_datasets_train.get(n), list_datasets_test.get(n));
                 if(temp1.equals( "-1.0" )) temp1 = util.get_value_metric_imbalanced(metric, list_datasets_test.get(n), imbalanced_data1);
                 
                 current_double = Double.parseDouble(temp1);
                 
                if(Double.isNaN(current_double) || current_double==-1.0)break;
                 
                lista_valores.add(current_double);
                  
            }
            
             if(Double.isNaN(current_double))
             {
                wr.write(value +"NaN");
                wr.write(System.getProperty("line.separator"));  
             }   
             
             else if (current_double==-1.0)
             {
                wr.write(value +current_double);
                wr.write(System.getProperty("line.separator"));  
                
             }   
    
             else
             {             
                 value_truncate = util.Truncate_value(util.Get_media(lista_valores), 5);
                 wr.write(value +value_truncate);
                 wr.write(System.getProperty("line.separator"));  
             }
            
        }
         
     
     }
   
   
    public static void Save_in_the_file_csv(PrintWriter wr,ArrayList<String> metric_list, MultiLabelInstances current,String dataset_name, boolean es_de_tipo_meka)
   {
     
        String encabezado = " Relation name" ;
        
        String value,temp;
        
        encabezado+= ";"+dataset_name;
         wr.write(encabezado );
         wr.write(System.getProperty("line.separator"));  
        
         atributo[] imbalanced_data =  util.Get_data_imbalanced_x_label(current); 
         
        for(String metric : metric_list)
        {
            value= metric;
            temp = get_value_metric(metric, current, es_de_tipo_meka);
            if(temp.equals("-1.0")) temp = get_value_metric_imbalanced(metric, current, imbalanced_data);
            
            value+= ";" + Cambia_pto_x_coma(temp);

            wr.write(value );
            wr.write(System.getProperty("line.separator"));  
                  
        }
		
   }
    
   public static void Save_in_the_file_csv(PrintWriter wr,ArrayList<String> metric_list, ArrayList<MultiLabelInstances> list_datasets, ArrayList<String> dataset_names, boolean es_de_tipo_meka)
   {
        boolean flag=true;
        String encabezado = " Metrics" ;
        
        String value, temp;
        
        atributo[] imbalanced_data;
        
        for(String metric : metric_list)
        {
            value= metric;
            MultiLabelInstances current ;
            
            
            for(int i=0; i< list_datasets.size();i++)
            {
                current = list_datasets.get(i);
                imbalanced_data =  util.Get_data_imbalanced_x_label(current); 
            
                if(flag){ encabezado+= ";"+dataset_names.get(i);}
                
                temp = get_value_metric(metric, current,es_de_tipo_meka);
                if(temp.equals("-1.0")) temp= get_value_metric_imbalanced(metric, current, imbalanced_data);
                
                value+= ";" + Cambia_pto_x_coma(temp);
                
            }
            if(flag)
            {
               flag =false;
               wr.write(encabezado );
               wr.write(System.getProperty("line.separator"));  
               // jTextArea1.append(encabezado+"\n");
            }
          
          wr.write(value );
          wr.write(System.getProperty("line.separator"));  
          //jTextArea1.append(value+"\n");
          
          
        }
		
   }
   
   public static atributo Get_label_x_labelname(String labelname , atributo[] lista)
   {
       for(int i=0;i<lista.length; i++)
       {
           if(labelname.equals(lista[i].get_name())) return lista[i];
       }
       return null;
   }
   
   //PERTENECE AL TRAIN/TEST DATASET
   public static void Save_in_the_file(PrintWriter wr,ArrayList<String> metric_list_comun, ArrayList<String>  metric_list_train, ArrayList<String> metric_list_test, MultiLabelInstances dataset_train , MultiLabelInstances dataset_test,boolean es_de_tipo_meka )
   {
       Instances i1= dataset_train.getDataSet();
                 
          wr.write("Relation Name:"+ "\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+i1.relationName());
          wr.write(System.getProperty("line.separator"));  
   
          //cant de atributos
          int num_atributos= i1.numAttributes();
          int numero_etiquetas = dataset_train.getNumLabels();
          
          //Attributes
          wr.write("Attributes:"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+ "\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+Integer.toString(num_atributos-numero_etiquetas));
          wr.write(System.getProperty("line.separator"));   
                  
         //cant de etiquetas
          wr.write("Labels:"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+Integer.toString(numero_etiquetas));
          wr.write(System.getProperty("line.separator"));   
            
          String value;
          //jTextArea1.append("Metrics for train/test set:"+"\n");
          
          wr.write(System.getProperty("line.separator")); 
          wr.write("---------------------------------------");
          wr.write(System.getProperty("line.separator"));  
          wr.write("Commun Metrics for train/test set:");
          wr.write(System.getProperty("line.separator")); 
          wr.write("---------------------------------------");
          wr.write(System.getProperty("line.separator"));   
          
          atributo[] imbalanced_data_train, imbalanced_data_test, label_frenquency_train,label_frenquency_test;
          
          label_frenquency_train = util.Get_Frequency_x_label(dataset_train);
          label_frenquency_train = util.Ordenar_freq_x_attr(label_frenquency_train);// ordena de mayor a menor
          imbalanced_data_train = util.Get_data_imbalanced_x_label_inter_class(dataset_train,label_frenquency_train);
                    
          label_frenquency_test = util.Get_Frequency_x_label(dataset_test);
          label_frenquency_test = util.Ordenar_freq_x_attr(label_frenquency_test);// ordena de mayor a menor
          imbalanced_data_test = util.Get_data_imbalanced_x_label_inter_class(dataset_test,label_frenquency_test);
          
        for(String metrica : metric_list_comun)
        {
           if(metrica.equals("Labels")) continue;
             
           value= util.get_value_metric(metrica, dataset_train, es_de_tipo_meka); //can  be dataset_test too
           if(value.equals( "-1.0" )) value = util.get_alternative_metric(metrica, dataset_train, dataset_test);
           
          wr.write(metrica+":"+ get_tabs(metrica)+value);
          wr.write(System.getProperty("line.separator"));   
        }
        
          wr.write(System.getProperty("line.separator")); 
          wr.write("---------------------------------------");
          wr.write(System.getProperty("line.separator"));   
          wr.write("Metrics for train set:");
          wr.write(System.getProperty("line.separator")); 
          wr.write("---------------------------------------");
          wr.write(System.getProperty("line.separator"));   
        

          
        for(String metrica : metric_list_train)
        {
           value= util.get_value_metric(metrica, dataset_train, es_de_tipo_meka);
           if(value.equals("-1.0")) value = util.get_value_metric_imbalanced(metrica, dataset_train, imbalanced_data_train);
           if(value.equals("-1.0")) value = "NaN";
           
          wr.write(metrica+":"+ get_tabs(metrica)+value);
          wr.write(System.getProperty("line.separator"));
        }
         
          wr.write(System.getProperty("line.separator")); 
          wr.write("---------------------------------------");
          wr.write(System.getProperty("line.separator"));   
          wr.write("Metrics for test set:");
          wr.write(System.getProperty("line.separator")); 
          wr.write("---------------------------------------");
          wr.write(System.getProperty("line.separator"));  
          
                                   
        for(String metrica : metric_list_test)
        {
           value= util.get_value_metric(metrica, dataset_test,es_de_tipo_meka);
           if(value.equals("-1.0")) value = util.get_alternative_metric(metrica, dataset_train, dataset_test);
           if(value.equals("-1.0")) value = util.get_value_metric_imbalanced(metrica, dataset_test, imbalanced_data_test);
         
          wr.write(metrica+":"+ get_tabs(metrica)+value);
           
          wr.write(System.getProperty("line.separator"));
        }
          
    
   }
   
   // PERTENEDE A LA PESTAÑA "DATASET"
   public static void Save_in_the_file(PrintWriter wr,ArrayList<String> metric_list, MultiLabelInstances dataset, atributo[] imbalanced_data, boolean  es_de_tipo_meka)
     {
         
         Instances i1= dataset.getDataSet();
                 
          wr.write("Relation Name:"+ "\t"+i1.relationName());
          wr.write(System.getProperty("line.separator"));  
    
          //cant de atributos
          int num_atributos= i1.numAttributes();
          int numero_etiquetas = dataset.getNumLabels();
          int num_instances = dataset.getNumInstances();
          
          //Attributes
          wr.write("Attributes:"+"\t"+"\t"+Integer.toString(num_atributos-numero_etiquetas));
          wr.write(System.getProperty("line.separator"));   
                  
         //cant de etiquetas
          wr.write("Labels:"+"\t"+"\t"+"\t"+Integer.toString(numero_etiquetas));
          wr.write(System.getProperty("line.separator"));   
          
          wr.write("Instances:"+"\t"+"\t"+Integer.toString(num_instances));
          wr.write(System.getProperty("line.separator"));   
          
          wr.write("--------------------------------------------------------------------------------");
          wr.write(System.getProperty("line.separator"));   
            
        //atributo[] imbalanced_data =  util.Get_data_imbalanced_x_label(dataset);
        
        for(String metrica : metric_list)
        {
            String value = get_value_metric(metrica, dataset, es_de_tipo_meka);
            if(value.equals("-1.000")){
                value = get_value_metric_imbalanced(metrica, dataset, imbalanced_data);
            } 	
            
            wr.write(metrica + ":" + get_tabs(metrica) + value);
            wr.write(System.getProperty("line.separator"));  
        }
 
     }
   
   
   public static void Save_text_file(PrintWriter wr,ArrayList<String> metric_list, MultiLabelInstances dataset, atributo[] imbalanced_data, boolean  es_de_tipo_meka, Hashtable<String, String> tableMetrics)
     {
         
         Instances i1= dataset.getDataSet();
                 
          wr.write("Relation Name:"+ "\t"+i1.relationName());
          wr.write(System.getProperty("line.separator"));  
    
          //cant de atributos
          int num_atributos= i1.numAttributes();
          int numero_etiquetas = dataset.getNumLabels();
          int num_instances = dataset.getNumInstances();
          
          //Attributes
          wr.write("Attributes:"+"\t"+"\t"+Integer.toString(num_atributos-numero_etiquetas));
          wr.write(System.getProperty("line.separator"));   
                  
         //cant de etiquetas
          wr.write("Labels:"+"\t"+"\t"+"\t"+Integer.toString(numero_etiquetas));
          wr.write(System.getProperty("line.separator"));   
          
          wr.write("Instances:"+"\t"+"\t"+Integer.toString(num_instances));
          wr.write(System.getProperty("line.separator"));   
          
          wr.write("--------------------------------------------------------------------------------");
          wr.write(System.getProperty("line.separator"));   
            
        //atributo[] imbalanced_data =  util.Get_data_imbalanced_x_label(dataset);
        
        String maxString = "Average of unconditionally dependent label pairs by chi-square test";
        double maxLength = maxString.length();
        String value = new String();
          
        for(String metric : metric_list)
        {
            //wr.write(metric + ":" + get_tabs(metric) + tableMetrics.get(metric).replace(",", "."));
            wr.write(metric + ": ");
            for(int i=0; i<(maxLength-metric.length()); i++){
                wr.write(" ");
            }
            
            //System.out.println("parseDouble: " + Double.parseDouble(tableMetrics.get(metric)));
            if((Math.abs(Double.parseDouble(tableMetrics.get(metric))*1000) < 0) ||
                    (Math.abs(Double.parseDouble(tableMetrics.get(metric))/1000.0) > 10)){
                NumberFormat formatter = new DecimalFormat("0.###E0");
                value = formatter.format(Double.parseDouble(tableMetrics.get(metric)));
                wr.write(value.replace(",", "."));
            }
            else{
                wr.write(tableMetrics.get(metric));
            }
            
            
            wr.write(System.getProperty("line.separator"));  
        }
 
     }
   
   public static void Save_csv_file(PrintWriter wr,ArrayList<String> metric_list, MultiLabelInstances dataset, atributo[] imbalanced_data, boolean  es_de_tipo_meka, Hashtable<String, String> tableMetrics)
     {
         
         Instances i1= dataset.getDataSet();
                 
          wr.write("Relation Name"+ ";" + i1.relationName());
          wr.write(System.getProperty("line.separator"));  
    
          //cant de atributos
          int num_atributos= i1.numAttributes();
          int numero_etiquetas = dataset.getNumLabels();
          int num_instances = dataset.getNumInstances();
          
          //Attributes
          wr.write("Attributes"+ ";" +Integer.toString(num_atributos-numero_etiquetas));
          wr.write(System.getProperty("line.separator"));   
                  
         //cant de etiquetas
          wr.write("Labels"+ ";" +Integer.toString(numero_etiquetas));
          wr.write(System.getProperty("line.separator"));   
          
          wr.write("Instances"+ ";" +Integer.toString(num_instances));
          wr.write(System.getProperty("line.separator"));   
          
          wr.write("");
          wr.write(System.getProperty("line.separator"));   
            
        //atributo[] imbalanced_data =  util.Get_data_imbalanced_x_label(dataset);
        
          String value = new String();
        for(String metric : metric_list)
        {
            if((Math.abs(Double.parseDouble(tableMetrics.get(metric))*1000) < 0) ||
                    (Math.abs(Double.parseDouble(tableMetrics.get(metric))/1000.0) > 10)){
                NumberFormat formatter = new DecimalFormat("0.###E0");
                value = formatter.format(Double.parseDouble(tableMetrics.get(metric)));
                wr.write(metric + ";" + value.replace(",", "."));
            }
            else{
                wr.write(metric + ";" + tableMetrics.get(metric));
            }
            wr.write(System.getProperty("line.separator"));  
        }
 
     }
   
    public static void Save_meka_file(PrintWriter wr,ArrayList<String> metric_list, MultiLabelInstances dataset, atributo[] imbalanced_data, boolean  es_de_tipo_meka, Hashtable<String, String> tableMetrics)
     {
         
         Instances i1= dataset.getDataSet();
                 
          wr.write("@relation" + " \'" + i1.relationName() + ": -C 0\'");
          wr.write(System.getProperty("line.separator"));  
          
          wr.write(System.getProperty("line.separator")); 
    
          //cant de atributos
          int num_atributos= i1.numAttributes();
          int numero_etiquetas = dataset.getNumLabels();
          int num_instances = dataset.getNumInstances();
          
          //Attributes
          wr.write("@attribute Attributes numeric");
          wr.write(System.getProperty("line.separator"));  
          wr.write("@attribute Labels numeric");
          wr.write(System.getProperty("line.separator"));  
          wr.write("@attribute Instances numeric");
          wr.write(System.getProperty("line.separator"));  
          
          /*
          wr.write("@attribute:"+"\t"+"\t"+Integer.toString(num_atributos-numero_etiquetas));
          wr.write(System.getProperty("line.separator"));   
                  
         //cant de etiquetas
          wr.write("Labels:"+"\t"+"\t"+"\t"+Integer.toString(numero_etiquetas));
          wr.write(System.getProperty("line.separator"));   
          
          wr.write("Instances:"+"\t"+"\t"+Integer.toString(num_instances));
          wr.write(System.getProperty("line.separator"));   
          
          wr.write("--------------------------------------------------------------------------------");
          wr.write(System.getProperty("line.separator"));   
          */
            
        //atributo[] imbalanced_data =  util.Get_data_imbalanced_x_label(dataset);
        
        for(String metric : metric_list)
        {
            wr.write("@attribute " + metric.replace(" ", "_") + " numeric");
            //wr.write(metric + ":" + get_tabs(metric) + tableMetrics.get(metric).replace(",", "."));
            wr.write(System.getProperty("line.separator"));  
        }
        
        wr.write(System.getProperty("line.separator")); 
        
        wr.write("@data");
        wr.write(System.getProperty("line.separator")); 
        
        String line = new String();
        line += Integer.toString(num_atributos-numero_etiquetas) + ", ";
        line += Integer.toString(numero_etiquetas) + ", ";
        line += Integer.toString(num_instances) + ", ";
        
        for(String metric : metric_list)
        {            
            if((Math.abs(Double.parseDouble(tableMetrics.get(metric))*1000) < 0) ||
                    (Math.abs(Double.parseDouble(tableMetrics.get(metric))/1000.0) > 10)){
                NumberFormat formatter = new DecimalFormat("0.###E0");
                line += formatter.format(Double.parseDouble(tableMetrics.get(metric)));
            }
            else{
                line += tableMetrics.get(metric);
            }
            line += ", ";
        }
        //Delete last ", "
        line = line.substring(0, line.length()-2);
        wr.write(line);
        wr.write(System.getProperty("line.separator")); 
 
     }
    
    public static String get_value_metric_imbalanced(String metric,MultiLabelInstances dataset,atributo[] imbalanced_data)
    {
        /*
        Statistics stat1 = new Statistics();
        stat1.calculateStats(dataset);        
        
        Integer[] combCounts= metrics.get_combCounts(stat1);
        */
        double value=-1;
        
        try{           
        switch (metric) 
            {
              
            case "Mean of IR per label intra class":  value =  metrics.Mean_IR_BR_intra_class(imbalanced_data);
                    break;     
            
            case "Mean of IR per label inter class":  value =  metrics.Mean_IR_BR_inter_class(imbalanced_data);
                    break;  
                
            case "Mean of IR per labelset":  value =  metrics.get_mean_ir_per_labelset(dataset);
                    break;  
                                                        
            case "Mean of standard deviation IR per label intra class":  value =  metrics.Mean_Standard_Desviation_imbalance_intra_class(imbalanced_data);
                    break;         
         
            //case "IR per labelset":  value =  metrics.imbalanced_ratio_LP(dataset);
               //     break;         
            
            case "Variance of examples per labelset":  value =  metrics.Variance_imbalanced_ratio_LP(dataset);
                    break;       
                
            case "CVIR inter class":  value =  metrics.CVIR_inter_class(imbalanced_data);
                   break;            
                
            
            default:  value = -1.0;
                     break;   
            }
        }
          catch (Exception e) {
                e.printStackTrace();
            }
        
        if(Double.isNaN(value) || value == Double.POSITIVE_INFINITY || value == Double.NEGATIVE_INFINITY){
            System.out.println("isNaN");
            return("NaN");
        }
        else{
            NumberFormat formatter = new DecimalFormat("#0.000"); 
            //System.out.println("formatter: " + formatter.format(value) + "  value: " + value);
           return(formatter.format(value)); 
        }
        //return Truncate_values_aprox_zero(Double.toString(value),4);
    }
    
    
    public static MultiLabelInstances[] Get_dataset_train_test(int n, MultiLabelInstances[] lista) throws InvalidDataFormatException
    {
        MultiLabelInstances[] result = new MultiLabelInstances[2];
        
        Instances data_train, data_test;
        boolean flag=false;
        
        if(n!=0) data_train = new Instances(lista[0].getDataSet()); //inicialiar el objeto data_train sin que n no sea el primer dataset.
        else{
            data_train= new Instances(lista[1].getDataSet());
            data_test = new Instances(lista[0].getDataSet());//entonces n=0
            flag=true;
        }
        
        for(int i =1; i<lista.length; i++)
        {
            if(flag) 
            {
                flag=false;
                continue;                
            }
            if(i==n) {continue;}
            else
            {
                data_train.addAll(lista[i].getDataSet());
            }
        }
        data_test = new Instances(lista[n].getDataSet());
        
        MultiLabelInstances train = new MultiLabelInstances(data_train,lista[0].getLabelsMetaData());
        MultiLabelInstances test = new MultiLabelInstances(data_test,lista[0].getLabelsMetaData());
        
        result[0]=train;
        result[1]= test;
        
        return result;
    }
    
    public static String get_value_metric(String metric, MultiLabelInstances dataset , boolean es_de_tipo_meka)
    {
        Statistics stat1 = new Statistics();
        stat1.calculateStats(dataset);
        
        
        double value =-1.0;
        Integer[] combCounts= metrics.get_combCounts(stat1);
        
        System.out.println("se recorre el combcounts");
        util.Recorre_Arreglo(combCounts);
        
        try{
            
            
        switch (metric) 
        {
            
            case "Labels x instances x features":  value =  metrics.labelsxinstancesxfeatures(dataset);
                    break;     
                 
            
            case "Instances":  value =  dataset.getNumInstances();
                    break;     

            case "Attributes":  value =  dataset.getDataSet().numAttributes()-dataset.getNumLabels();
                    break;    
                
            case "Labels":  value =  dataset.getNumLabels();
                    break;    
                
            case "Label density":  value =  metrics.Label_Density(dataset, stat1);
                    break;                
             
            case "Label Cardinality":  value = metrics.LabelCardinality(dataset, stat1);
                      break;
                
                
            case "Distinct Labelset":  value = metrics.DistincLabelset(stat1);
                     break;
            
            case "Proportion of Distinct Labelset":  value = metrics.ProportionDistincLabelset(stat1,dataset);
                     break;
                
            case "Density":  value = metrics.Density(stat1);
                     break;
                
            case "Cardinality":  value = metrics.Cardinality(stat1);
                     break;
                
            case "Bound":  value = metrics.Bound(dataset);
                     break;      
            
            case "Diversity":  value = metrics.Diversity(dataset, stat1);
                   break;       
                                
            case "Proportion of unique label combination (PUniq)":  value = metrics.PUniq(stat1, dataset);
                     break;
            
            case "Proportion of maxim label combination (PMax)":  value = metrics.PMax(stat1, dataset);
                     break;
                
            case "Ratio of number of instances to the number of attributes":  value = metrics.Ratio_instances_to_attr(dataset);
                     break;
                
            case "Number of binary attributes":  value = metrics.count_attributes_binary(dataset);
                     break;   
                
             case "Proportion of binary attributes":  value = metrics.Proportion_binary_attr(dataset);
                     break;
            
            case "Proportion of nominal attributes":  value = metrics.Proportion_nominal_attr(dataset);
                     break;
                
            case "Number of nominal attributes":  value = metrics.count_attributes_nominal(dataset);
                     break;    
                
            case "Default accuracy":  value = metrics.Default_Accuracy(dataset);
                     break;
                
            case "Mean of mean of numeric attributes":  value = metrics.MeanOfMeans(dataset);
                     break;
                
            case "Mean of standar deviation of numeric attributes":  value = metrics.MeanOfStDev(dataset);
                     break;      
                
            case "Mean of skewness of numeric attributes":  value = metrics.MeanSkewness(dataset);
                     break;    
                
            case "Mean of kurtosis":  value = metrics.MeanKurtosis(dataset);
                     break;
            
            case "Mean of entropy of nominal attributes":  value = metrics.MeanOfEntropies_nominal_attr(dataset);
                     break;
            
            /*case "Mean of entropies (numeric attr)":  value = metrics.MeanOfEntropies_numeric_attr(dataset);
                     break;
              */  
            case "Average absolute correlation between numeric attributes":  value = metrics.AverageAbsoluteCorrelation(dataset);
                     break;
                
            case "Proportion of numeric attributes with outliers":  value = metrics.ProportionWithOutliers(dataset);
                     break;
                
            case "Average gain ratio":  value = metrics.AverageGainRatio(dataset, es_de_tipo_meka);
                     break;      
                
            case "Ratio of distinct classes to the total number label combinations":  value = metrics.Ratio_DC_to_total_label_combination(stat1, dataset);
                     break;
            
            case "Standard desviation of the label cardinality":  value = metrics.StDevOfTrainCardinality(stat1, metrics.LabelsForInstance(dataset));
                     break;    
            
            case "Skewness cardinality":  value = metrics.SkewnessOfTrainCardinality(stat1, metrics.LabelsForInstance(dataset));
                     break;
 
            
            case "Kurtosis cardinality":  value = metrics.KurtosisOfTrainCardinality(stat1,  metrics.LabelsForInstance(dataset));
                     break;
                
            case "Number of unconditionally dependent label pairs by chi-square test":  value = metrics.UncondDepPairsNum_UncondDepPairsRatio_AvgOfDepChiScores(dataset)[0];
                     break;
                
            case "Ratio of unconditionally dependent label pairs by chi-square test":  value = metrics.UncondDepPairsNum_UncondDepPairsRatio_AvgOfDepChiScores(dataset)[1];
                     break;
                
            case "Average of unconditionally dependent label pairs by chi-square test":  value = metrics.UncondDepPairsNum_UncondDepPairsRatio_AvgOfDepChiScores(dataset)[2];
                     break;      
                
            case "Number of labelsets up to 2 examples":  value = metrics.countUpTo(2,combCounts);
                     break;
            
            case "Number of labelsets up to 5 examples":  value = metrics.countUpTo(5,combCounts);
                     break;
                
            case "Number of labelsets up to 10 examples":  value = metrics.countUpTo(10,combCounts);
                     break;
                
            case "Number of labelsets up to 50 examples":  value = metrics.countUpTo(50,combCounts);
                     break;   
                
            case "Ratio of labelsets with number of examples < half of the attributes":  value = metrics.RatioClassesWithExamplesLessHalfAttributes(dataset,combCounts);
                     break;    
                 
             case "Ratio of number of labelsets up to 2 examples":  value = metrics.RatioClassesWithUpTo_n_Examples(combCounts,2);
                     break;
            
            case "Ratio of number of labelsets up to 5 examples":  value = metrics.RatioClassesWithUpTo_n_Examples(combCounts,5);
                     break;
                
            case "Ratio of number of labelsets up to 10 examples":  value = metrics.RatioClassesWithUpTo_n_Examples(combCounts,10);
                     break;
                
            case "Ratio of number of labelsets up to 50 examples":  value =  metrics.RatioClassesWithUpTo_n_Examples(combCounts,50);
                     break;     
                
            case "Ratio of Distinct Labelset over Bound": value= metrics.ratio_DL_over_bount(dataset, stat1);
                    break;
                
            case "Average examples per labelset":  value = metrics.AvgExamplesPerClass(dataset);
                     break;
            
            case "Minimal entropy of labels":  value = metrics.Min_MaxOfLabelEntropies(dataset)[0];
                     break;
                
            case "Maximal entropy of labels":  value = metrics.Min_MaxOfLabelEntropies(dataset)[1];
                     break;
                
            case "Mean entropy of labels":  value =  metrics.MeanOfLabelEntropies(dataset);
                    break;
            
            case "Standard desviation of examples per labelset":  value =  metrics.Standard_Desviation_x_labelset(dataset, stat1);
                    break;                
              
                
            default:  value = -1.0;
                     break;    
        
        }
        
        }
        catch (Exception e) {
                e.printStackTrace();
            }

        //System.out.println("formatter: " + formatter.format(Double.parseDouble(val.toString().replace(",", "."))));
        
        if(Double.isNaN(value) || value == Double.POSITIVE_INFINITY || value == Double.NEGATIVE_INFINITY){
            System.out.println("isNaN");
            return("NaN");
        }
        else{
            NumberFormat formatter = new DecimalFormat("#0.000"); 
            //System.out.println("formatter: " + formatter.format(value) + "  value: " + value);
           return(formatter.format(value)); 
        }
        
        
        //return Truncate_values_aprox_zero(Double.toString(value),4);
    }
    
    public static double Get_media(ArrayList<Double> lista)
    {
        double value=0.0;
        for(double current : lista)
            value+=current;
        
        return value/lista.size();
    }
    
    
    
    public static ArrayList<String> Get_test_metrics()
     {
       ArrayList<String> result= new ArrayList();
       
        result.add("Average absolute correlation between numeric attributes");
        result.add("Average examples per labelset");
        result.add("Average gain ratio");
        result.add("Average of unconditionally dependent label pairs by chi-square test");
        result.add("Cardinality");
        result.add("CVIR inter class");
        result.add("Density");
        result.add("Distinct Labelset");
        result.add("Diversity");
        result.add("Instances");
        result.add("Kurtosis cardinality");
        result.add("Labels x instances x features");
        result.add("Maximal entropy of labels");
        result.add("Mean of entropy of nominal attributes");
        result.add("Mean entropy of labels");
        result.add("Mean of kurtosis");
        result.add("Mean of mean of numeric attributes");
        result.add("Mean of skewness of numeric attributes");
        result.add("Mean of standar deviation of numeric attributes");
        result.add("Mean of standard deviation IR per label intra class");
        result.add("Mean of IR per label intra class");
        result.add("Mean of IR per label inter class");
        result.add("Mean of IR per labelset");
        result.add("Minimal entropy of labels");
        result.add("Number of labelsets up to 2 examples");
        result.add("Number of labelsets up to 5 examples");
        result.add("Number of labelsets up to 10 examples");
        result.add("Number of labelsets up to 50 examples");
        result.add("Number of unconditionally dependent label pairs by chi-square test");
        result.add("Number of unseen labelsets");
        result.add("Proportion of numeric attributes with outliers");
        result.add("Ratio of labelsets with number of examples < half of the attributes");
        result.add("Ratio of number of labelsets up to 2 examples");
        result.add("Ratio of number of labelsets up to 5 examples");
        result.add("Ratio of number of labelsets up to 10 examples");
        result.add("Ratio of number of labelsets up to 50 examples");
        result.add("Ratio of number of instances to the number of attributes");
        result.add("Ratio of unseen labelsets");
        result.add("Ratio of unconditionally dependent label pairs by chi-square test");
        result.add("Skewness cardinality");
        result.add("Standard desviation of the label cardinality");
        result.add("Standard desviation of examples per labelset");

       return result;
     }
    
    
    public static Object[][] Get_row_data_test_data ()
    {
        ArrayList metrics = Get_test_metrics();
        
        Object rowData[][] = new Object[metrics.size()][4];
        
        for(int i=0; i<metrics.size(); i++){
            rowData[i][0] = metrics.get(i);
            rowData[i][1] = "-";
            rowData[i][2] = "-";
            rowData[i][3]= Boolean.FALSE;
        }
        
        return rowData;
    }
    
    
     public static Object[][] Get_row_data_train_data ()
    {
             Object rowData[][] = { 
                 
                 { "Average examples per labelset", "-", Boolean.FALSE },
                 { "Average of unconditionally dependent label pairs by chi-square test", "-", Boolean.FALSE },
                 { "Cardinality", "-", Boolean.FALSE },
                 { "CVIR inter class", "-", Boolean.FALSE }, 
                 { "Density", "-", Boolean.FALSE },
                 //{ "Default accuracy", Boolean.FALSE }, HACE LO MISMO QUE EL PMAX 
                 { "Distinct Labelset", "-", Boolean.FALSE }, 
                 { "Diversity", "-", Boolean.FALSE }, 
                 { "Instances", "-", Boolean.FALSE},
                   
                 
                 { "Kurtosis cardinality", "-", Boolean.FALSE },//MEDIA OF ALL NUMERIC ATTR    
                 { "Labels x instances x features", "-", Boolean.FALSE }, 
                 { "Mean of standard deviation IR per label intra class", "-", Boolean.FALSE }, 
                 
                 { "Mean of IR per label intra class", "-", Boolean.FALSE }, 
                 { "Mean of IR per label inter class", "-", Boolean.FALSE }, 
                 { "Mean of IR per labelset", "-", Boolean.FALSE }, 
                  
                 { "Number of unconditionally dependent label pairs by chi-square test", "-", Boolean.FALSE },                  
                 { "Number of labelsets up to 2 examples", "-", Boolean.FALSE },
                 { "Number of labelsets up to 5 examples", "-", Boolean.FALSE },  
                 { "Number of labelsets up to 10 examples", "-", Boolean.FALSE }, 
                 { "Number of labelsets up to 50 examples", "-", Boolean.FALSE },                 
                 { "Ratio of labelsets with number of examples < half of the attributes", "-", Boolean.FALSE },   
               // { "Ratio of distinct classes to the total number label combinations", Boolean.FALSE },                                                   
                 
                  { "Ratio of number of labelsets up to 2 examples", "-", Boolean.FALSE },
                  { "Ratio of number of labelsets up to 5 examples", "-", Boolean.FALSE },
                  { "Ratio of number of labelsets up to 10 examples", "-", Boolean.FALSE },
                  { "Ratio of number of labelsets up to 50 examples", "-", Boolean.FALSE },
           
                  { "Ratio of number of instances to the number of attributes", "-", Boolean.FALSE }, 
                  { "Ratio of unconditionally dependent label pairs by chi-square test", "-", Boolean.FALSE }, 
                  { "Skewness cardinality", "-", Boolean.FALSE },   
                
                  { "Standard desviation of the label cardinality", "-", Boolean.FALSE },
                  { "Standard desviation of examples per labelset", "-", Boolean.FALSE },        
                 // { "Variance of examples per labelset", Boolean.FALSE },  
              };
             
             return rowData;
    }
    
     public static ArrayList<String> Get_common_metrics()
     {
       ArrayList<String> result= new ArrayList();
       
       result.add("Absolute difference between label cardinality of train and test sets");
       result.add("Labels");
       result.add("Number distinct labelset found in train and test sets");
       result.add("Number of binary attributes");
       result.add("Number of nominal attributes");
       result.add("Proportion of binary attributes");
       result.add("Proportion of nominal attributes");
       result.add("Ratio of test instances over training instances");
       result.add("Ratio of distinct labelset found in both datasets over number of labelset");

       return result;
     }
    
     public static Object[][] Get_row_data_commun_data ()
    {
        ArrayList metrics = Get_common_metrics();
        
        Object rowData[][] = new Object[metrics.size()][3];
        
        for(int i=0; i<metrics.size(); i++){
            rowData[i][0] = metrics.get(i);
            rowData[i][1] = "-";
            rowData[i][2]= Boolean.FALSE;
        }
        
        return rowData;
    }
   
     public static Object[][] Get_row_data_imbalanced ()
    {
         Object rowData[][] = { 
          
           { "CVIR inter class", "-", Boolean.FALSE }, 
           
           { "Kurtosis cardinality", "-", Boolean.FALSE },      
           { "Mean of standard deviation IR per label intra class", "-", Boolean.FALSE }, 
           
           { "Mean of IR per label intra class", "-", Boolean.FALSE },      
           { "Mean of IR per label inter class", "-", Boolean.FALSE }, 
           { "Mean of IR per labelset", "-", Boolean.FALSE }, 
           
           { "Mean of skewness of numeric attributes", "-", Boolean.FALSE },
           { "Mean of kurtosis", "-", Boolean.FALSE },
           { "Proportion of numeric attributes with outliers", "-", Boolean.FALSE }, 
           { "Proportion of maxim label combination (PMax)", "-", Boolean.FALSE},
           { "Proportion of unique label combination (PUniq)", "-", Boolean.FALSE},
           { "Skewness cardinality", "-", Boolean.FALSE },
           { "Standard desviation of examples per labelset", "-", Boolean.FALSE }, 
         //  { "Variance of examples per labelset", Boolean.FALSE }, 
                 
                 
             };
             
             return rowData;
    }
     
        
    public static Object[][] Get_row_data ()
    {
             Object rowData[][] = { 
                 { "Average absolute correlation between numeric attributes", Boolean.FALSE },
                 { "Average examples per labelset", Boolean.FALSE },
                 { "Average gain ratio", Boolean.FALSE }, 
                 { "Average of unconditionally dependent label pairs by chi-square test", Boolean.FALSE },
                 { "Bound", Boolean.FALSE},
                 { "Cardinality", Boolean.FALSE },
                 { "CVIR inter class", Boolean.FALSE }, 
                 
                 
                 //{ "Default accuracy", Boolean.FALSE }, HACE LO MISMO QUE EL PMAX 
                 { "Density", Boolean.FALSE }, 
                 { "Distinct Labelset", Boolean.FALSE}, 
                 { "Diversity", Boolean.FALSE },
                 { "Instances", Boolean.FALSE },
                 
                 { "Kurtosis cardinality", Boolean.FALSE },
                 
                 { "Labels x instances x features", Boolean.FALSE }, 
                 
                 { "Maximal entropy of labels", Boolean.FALSE },
                 { "Mean of entropy of nominal attributes", Boolean.FALSE },
		 { "Mean of standard deviation IR per label intra class", Boolean.FALSE }, 
                 
                 { "Mean of IR per label intra class", Boolean.FALSE },     
                 { "Mean of IR per label inter class", Boolean.FALSE },  
                 { "Mean of IR per labelset", Boolean.FALSE }, 
                 
                 { "Mean of mean of numeric attributes", Boolean.FALSE },
                 { "Mean of standar deviation of numeric attributes", Boolean.FALSE },
                 { "Mean of skewness of numeric attributes", Boolean.FALSE },
                 { "Mean of kurtosis", Boolean.FALSE },
                 //{ "Mean of entropies (numeric attr)", Boolean.FALSE },
                 { "Minimal entropy of labels", Boolean.FALSE },
                 
                 { "Number of binary attributes", Boolean.FALSE },
                 { "Number of labelsets up to 2 examples", Boolean.FALSE },
                 { "Number of labelsets up to 5 examples", Boolean.FALSE },               
                 { "Number of labelsets up to 10 examples", Boolean.FALSE },
                 { "Number of labelsets up to 50 examples", Boolean.FALSE },
                 
                 { "Number of nominal attributes", Boolean.FALSE },
                 
                 { "Number of unconditionally dependent label pairs by chi-square test", Boolean.FALSE },
                 
                 { "Proportion of Distinct Labelset", Boolean.FALSE },                  
                 { "Proportion of maxim label combination (PMax)", Boolean.FALSE},
                 { "Proportion of numeric attributes with outliers", Boolean.FALSE },
                 { "Proportion of binary attributes", Boolean.FALSE },
                 { "Proportion of nominal attributes", Boolean.FALSE },                 
                 { "Proportion of unique label combination (PUniq)", Boolean.FALSE},
                     
                 { "Ratio of labelsets with number of examples < half of the attributes", Boolean.FALSE },
                 //{ "Ratio of distinct classes to the total number label combinations", Boolean.FALSE }, es el DIVERSTY
                 { "Ratio of number of instances to the number of attributes", Boolean.FALSE },                                                                  
                 { "Ratio of number of labelsets up to 2 examples", Boolean.FALSE }, 
                 { "Ratio of number of labelsets up to 5 examples", Boolean.FALSE },
                 { "Ratio of number of labelsets up to 10 examples", Boolean.FALSE },
                 { "Ratio of number of labelsets up to 50 examples", Boolean.FALSE },                 
                 { "Ratio of unconditionally dependent label pairs by chi-square test", Boolean.FALSE },
                 { "Skewness cardinality", Boolean.FALSE },
                 { "Standard desviation of the label cardinality", Boolean.FALSE },
                 { "Standard desviation of examples per labelset", Boolean.FALSE },
                // { "Variance of examples per labelset", Boolean.FALSE }, 
                 
             };
             
             return rowData;
    }
    
    
    public static Object[][] Get_row_data_principal()
    {
        ArrayList metrics = Get_all_metrics();
        
        Object rowData[][] = new Object[metrics.size()][3];
        
        for(int i=0; i<metrics.size(); i++){
            rowData[i][0] = metrics.get(i);
            rowData[i][1] = "-";
            rowData[i][2]= Boolean.FALSE;
        }
        
        return rowData;
    }
    
    
    public static ArrayList<String> Get_all_metrics()
     {
       ArrayList<String> result= new ArrayList();

       result.add("Average absolute correlation between numeric attributes");
       result.add("Average examples per labelset");
       result.add("Average gain ratio");
       result.add("Average of unconditionally dependent label pairs by chi-square test");
       result.add("Bound");
       result.add("Cardinality");
       result.add("CVIR inter class");
       
       result.add("Density");
       result.add("Distinct Labelset");
       result.add("Diversity");
       //result.add("Instances");
       
       result.add("Kurtosis cardinality");
       
       result.add("Labels x instances x features");
       
       result.add("Maximal entropy of labels");
       result.add("Mean of entropy of nominal attributes");
       result.add("Mean of standard deviation IR per label intra class");
       
       result.add("Mean of IR per label intra class");
       result.add("Mean of IR per label inter class");
       result.add("Mean of IR per labelset");
       
       result.add("Mean of mean of numeric attributes");
       result.add("Mean of standar deviation of numeric attributes");
       result.add("Mean of skewness of numeric attributes");
       result.add("Mean of kurtosis");
       
       result.add("Minimal entropy of labels");
       
       result.add("Number of binary attributes");
       result.add("Number of labelsets up to 2 examples");
       result.add("Number of labelsets up to 5 examples");
       result.add("Number of labelsets up to 10 examples");
       result.add("Number of labelsets up to 50 examples");
       
       result.add("Number of nominal attributes");
       
       result.add("Number of unconditionally dependent label pairs by chi-square test");
       
       result.add("Proportion of Distinct Labelset");
       result.add("Proportion of maxim label combination (PMax)");
       result.add("Proportion of numeric attributes with outliers");
       result.add("Proportion of binary attributes");
       result.add("Proportion of nominal attributes");
       result.add("Proportion of unique label combination (PUniq)");
       
       result.add("Ratio of labelsets with number of examples < half of the attributes");
       
       result.add("Ratio of number of instances to the number of attributes");
       result.add("Ratio of number of labelsets up to 2 examples");
       result.add("Ratio of number of labelsets up to 5 examples");
       result.add("Ratio of number of labelsets up to 10 examples");
       result.add("Ratio of number of labelsets up to 50 examples");
       result.add("Ratio of unconditionally dependent label pairs by chi-square test");
       result.add("Skewness cardinality");
       result.add("Standard desviation of the label cardinality");
       result.add("Standard desviation of examples per labelset");

       return result;
     }
    

    
     public static Object[][] Get_row_data_multi_datasets ()
    {
             Object rowData[][] = { 
                 
                 { "Attributes", "-", Boolean.FALSE},  
                 { "Average absolute correlation between numeric attributes", "-", Boolean.FALSE },
                 { "Average examples per labelset", "-", Boolean.FALSE },
                 { "Average gain ratio", "-", Boolean.FALSE },                  
                 { "Average of unconditionally dependent label pairs by chi-square test", "-", Boolean.FALSE },        
                 
                 { "Bound", "-", "-", Boolean.FALSE}, 
                 { "Cardinality", "-", Boolean.FALSE}, 
                 { "CVIR inter class", "-", Boolean.FALSE },
                 
                 //{ "Default accuracy", Boolean.FALSE }, HACE LO MISMO QUE EL PMAX 
                 { "Density", "-", Boolean.FALSE}, 
                 { "Distinct Labelset", "-", Boolean.FALSE}, 
                 { "Diversity", "-", Boolean.FALSE },
                 { "Instances", "-", Boolean.FALSE}, 
                           
                 { "Kurtosis cardinality","-",  Boolean.FALSE },
                 { "Labels", "-", Boolean.FALSE}, 
                 
                 { "Labels x instances x features", "-", Boolean.FALSE },
                 
                 { "Maximal entropy of labels", "-", Boolean.FALSE },
                 { "Mean entropy of labels", "-", Boolean.FALSE },         
                 
                 { "Mean of entropy of nominal attributes", "-", Boolean.FALSE },
                
                 { "Mean of IR per label intra class", "-", Boolean.FALSE }, 
                 { "Mean of IR per label inter class", "-", Boolean.FALSE },    
                 { "Mean of IR per labelset", "-", Boolean.FALSE }, 
                 
                 { "Mean of kurtosis", "-", Boolean.FALSE },
                 { "Mean of mean of numeric attributes", "-", Boolean.FALSE },
                 { "Mean of standar deviation of numeric attributes", "-", Boolean.FALSE },
                 { "Mean of skewness of numeric attributes", "-", Boolean.FALSE },
                 { "Mean of standard deviation IR per label intra class", "-", Boolean.FALSE }, 
                 
                 { "Minimal entropy of labels", "-", Boolean.FALSE },
                 { "Number of binary attributes", "-", Boolean.FALSE },
                 { "Number of labelsets up to 2 examples", "-", Boolean.FALSE },
                 { "Number of labelsets up to 5 examples", "-", Boolean.FALSE },               
                 { "Number of labelsets up to 10 examples", "-", Boolean.FALSE },
                 { "Number of labelsets up to 50 examples", "-", Boolean.FALSE },
                 { "Number of nominal attributes", "-", Boolean.FALSE },
                 
                 { "Number of unconditionally dependent label pairs by chi-square test", "-", Boolean.FALSE },
                  
                 { "Proportion of Distinct Labelset", "-", Boolean.FALSE }, 
                 { "Proportion of unique label combination (PUniq)", "-", Boolean.FALSE},
                 { "Proportion of maxim label combination (PMax)", "-", Boolean.FALSE},
                 { "Proportion of numeric attributes with outliers", "-", Boolean.FALSE },
                 { "Proportion of binary attributes", "-", Boolean.FALSE },
                 { "Proportion of nominal attributes", "-", Boolean.FALSE },
                
                 //{ "Mean of entropies (numeric attr)", Boolean.FALSE },
                 { "Ratio of labelsets with number of examples < half of the attributes", "-", Boolean.FALSE },
                // { "Ratio of distinct classes to the total number label combinations", Boolean.FALSE }, es el DIVERSTY
                 { "Ratio of number of labelsets up to 2 examples", "-", Boolean.FALSE }, 
                 { "Ratio of number of labelsets up to 5 examples", "-", Boolean.FALSE },
                 { "Ratio of number of labelsets up to 10 examples", "-", Boolean.FALSE },
                 { "Ratio of number of labelsets up to 50 examples", "-", Boolean.FALSE },
                 { "Ratio of number of instances to the number of attributes", "-", Boolean.FALSE }, 
                 { "Ratio of unconditionally dependent label pairs by chi-square test", "-", Boolean.FALSE },
                 { "Skewness cardinality", "-", Boolean.FALSE },
                 { "Standard desviation of the label cardinality", "-", Boolean.FALSE },
                 { "Standard desviation of examples per labelset", "-", Boolean.FALSE },
               //  { "Variance of examples per labelset", Boolean.FALSE }, 
                 
             };
             
             return rowData;
    }
     
     
     public static double[][] get_heatmap_values (MultiLabelInstances dataset, ArrayList<pares_atributos> lista_pares)
     {
          int[] label_indices= dataset.getLabelIndices();
         
         //double[][] get_pair_label = new double[label_indices.length][label_indices.length];
          double[][] get_pair_label = inicializa_arreglo_val_neg(label_indices.length);
          
         int i,j;
         int cant_instancias = dataset.getNumInstances();
         double probabilidad_att1_att2,probabilidad_att2,probabilidad_att1;//, probabilidad_att1;
         
         for( pares_atributos current : lista_pares)
         {
             i= current.get_ind_att1();
             j= current.get_ind_att2();
             
             probabilidad_att1_att2 = current.get_cant_veces()/(double)cant_instancias;
             probabilidad_att1 = current.get_cant_att1()/(double)cant_instancias;
             probabilidad_att2 = current.get_cant_att2()/(double)cant_instancias;
             
             get_pair_label[i][j]= probabilidad_att1_att2/probabilidad_att2;                               
             get_pair_label[j][i]= probabilidad_att1_att2/probabilidad_att1;
         }
         
         return get_pair_label;
     }
     
     public static double[][] get_heatmap_values (ArrayList<pares_atributos> lista_pares, int cant_instancias, String[] labelname)
     {
          double[][] get_pair_label = inicializa_arreglo_val_neg(labelname.length);
          
       
         //int cant_instancias = dataset.getNumInstances();
         double probabilidad_att1_att2,probabilidad_att2,probabilidad_att1;//, probabilidad_att1;
         
         pares_atributos current;
         
         for(int i=0;i<labelname.length;i++)
       {
           for(int j=0;j<labelname.length;j++)
           {
               if(i==j || i>j ) continue;
               
               current = Search_and_get(labelname[i],labelname[j],lista_pares);
               
               if(current !=null) 
               {
                   
                probabilidad_att1_att2 = current.get_cant_veces()/(double)cant_instancias;
                probabilidad_att1 = current.get_cant_att1()/(double)cant_instancias;
                probabilidad_att2 = current.get_cant_att2()/(double)cant_instancias;
             
                get_pair_label[i][j]= probabilidad_att1_att2/probabilidad_att2;                               
                get_pair_label[j][i]= probabilidad_att1_att2/probabilidad_att1;
                
               }
               //current =null;
           }
           
       }
         
         
         /*
         for( pares_atributos current : lista_pares)
         {
             i= current.get_ind_att1();
             j= current.get_ind_att2();
             
             probabilidad_att1_att2 = current.get_cant_veces()/(double)cant_instancias;
             probabilidad_att1 = current.get_cant_att1()/(double)cant_instancias;
             probabilidad_att2 = current.get_cant_att2()/(double)cant_instancias;
             
             get_pair_label[i][j]= probabilidad_att1_att2/probabilidad_att2;                               
             get_pair_label[j][i]= probabilidad_att1_att2/probabilidad_att1;
         }
         */
         return get_pair_label;
     }
     
     public static double[][] inicializa_arreglo_val_neg(int length)
     {
         double[][] data = new double[length][length];
         
         for(int i=0;i<length;i++)
             for(int j=0;j<length;j++)
                 data[i][j]=-1.0;
         return data;
     }
     
     public static double[][] get_pair_label_values (MultiLabelInstances dataset, ArrayList<pares_atributos> lista_pares)
     {
         int[] label_indices= dataset.getLabelIndices();
         
         //double[][] get_pair_label = new double[label_indices.length][label_indices.length];
         double[][] get_pair_label = inicializa_arreglo_val_neg(label_indices.length);
                 
         int i,j;
         
         for( pares_atributos current : lista_pares)
         {
             i= current.get_ind_att1();
             j= current.get_ind_att2();
             
             get_pair_label[i][j]=current.get_cant_veces()/dataset.getNumInstances();
            // get_pair_label[j][i]=-1.0;
         }
         
         return get_pair_label;
     
     }
    
     public static double[][] get_chi_fi_coefficient (MultiLabelInstances dataset)
     {
        double[][] chi_fi_coefficient = new double[dataset.getNumLabels()][dataset.getNumLabels()];
        double fi,chi;
        
           try {
                
                UnconditionalChiSquareIdentifier depid = new UnconditionalChiSquareIdentifier();
                LabelsPair[] pairs = depid.calculateDependence(dataset);
                
                for(int i=0; i<pairs.length;i++)
                {
                    chi = pairs[i].getScore();
                    fi = Math.sqrt((chi/dataset.getNumLabels()));
                    
                   // System.out.println("chi:"+pairs[i].getPair()[0]+" , "+ pairs[i].getPair()[1]+"= "+ chi);
                   // System.out.println("fi:"+pairs[i].getPair()[1]+" , "+ pairs[i].getPair()[0]+"= "+ fi);
                    
                    chi_fi_coefficient[pairs[i].getPair()[0]][pairs[i].getPair()[1]] = chi;
                    chi_fi_coefficient[pairs[i].getPair()[1]][pairs[i].getPair()[0]] = fi;
                    
                }
                
                }
           catch (Exception e) {
                e.printStackTrace();
            }
           
           
           
           return chi_fi_coefficient;
            
     }
    
    public static ArrayList<String> Get_lista_vertices_del_par(String labelname, ArrayList<pares_atributos> mi_lista)
    {
        ArrayList<String> result = new ArrayList<>();    
        
        for(pares_atributos actual : mi_lista)
        {
            if(actual.get_name_attr1()==labelname) 
            {
                result.add(actual.get_name_attr2());
     
            }
           else if(actual.get_name_attr2()==labelname)
           {
               result.add(actual.get_name_attr1());
     
           }
            
        }        
        
        return result;     
        
    }
    
    public static int devuelve_indice (String[] label_list, String labelname)
    {
        for(int i=0; i<label_list.length;i++)
            if(labelname==label_list[i])return i;
        return -1;
    }
    
    private static String[] get_name_label( int[] label_indices, Instances Instancias )
    {
        String[] name_label = new String[label_indices.length];
                
        for(int i=0;i<label_indices.length;i++)
        {
            name_label[i]=Instancias.attribute(label_indices[i]).name();
        }
        return name_label;
    }
    //---------------------------------------------------------------------------------------------------
    
    
    public static boolean Esta_dataset (ArrayList<MultiLabelInstances> list_dataset, String dataset_name)
    {
        for(MultiLabelInstances current : list_dataset)
              if(current.getDataSet().relationName().equals(dataset_name)) return true;
        
        return false;
    }
    
    public static double[] get_ir_values_intra_class(atributo[] label_imbalanced)
    {
        double[] result = new double[label_imbalanced.length];
        
        for(int i=0; i<label_imbalanced.length; i++)
            result[i]=label_imbalanced[i].get_ir();
        
        return result;    
    
    }
    
    public static double[] get_ir_values_inter_class(atributo[] label_freq)//se le pasa el arreglo ordenado de mayor a menor
    {
        
        atributo[] label_freq_sorted = Ordenar_freq_x_attr(label_freq);
        
        double[] ir_inter_class = new double[label_freq.length];
        
        int mayor= label_freq_sorted[0].get_frequency();
        double value;
        
        double media=0;
        
        for(int i=0;i<label_freq_sorted.length; i++)
        {
            value = mayor/(label_freq_sorted[i].get_frequency()*1.0);
            ir_inter_class[i] = value;
            //System.out.println(" Atributo "+i+" , "+value);
            
            media+= value;
        }
        media = media/label_freq_sorted.length;
                
        System.out.println("Media es "+ media);
        
        return ir_inter_class;
    }
    
    private static int get_frequency_x_label_name(atributo[] lista_attr_freq ,String label_name)
    {
        for(int i=0 ; i<lista_attr_freq.length; i++)
        {
            if(lista_attr_freq[i].get_name().equals(label_name)) return lista_attr_freq[i].get_frequency();
        }
        return -1;//es que no aparece el nombre de la etiqueta
    }
    
    
    private static ArrayList<pares_atributos> conforma_pares_atributos(int [] pares_etiquetas,int[] label_indices,MultiLabelInstances dataset )
    {
      ArrayList<pares_atributos> mi_lista= new ArrayList<>();
      Instances Instancias = dataset.getDataSet();
      
      String[] name_attr =get_name_label(label_indices, Instancias); //NOMBRES DE LAS ETIQUETAS
      pares_atributos actual;
      
      atributo[] lista_attr_freq = Get_Frequency_x_label(dataset);
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
            
            actual = new pares_atributos(name_attr[i], name_attr[j],value,i,j,cant_i,cant_j);
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
    
    
    public static double[][] Get_pares_seleccionados (String[] labelname, ArrayList<pares_atributos> pares_seleccionados, int num_instances)
    {
        //LANZAR LA VENTANA EMERGENTE CON LOS PARE SELECCIONADOS
       
       double[][] pares_freq= inicializa_arreglo_val_neg(labelname.length);
       
       pares_atributos current=null;
       
       for(int i=0; i<labelname.length;i++)
       {
           for(int j=0;j<labelname.length;j++)
           {
               if(i==j || i>j ) continue;
               
               current = Search_and_get(labelname[i],labelname[j],pares_seleccionados);
               
               if(current !=null) 
               {
                 pares_freq[i][j]=current.get_cant_veces()/(num_instances*1.0);
               }
               //current =null;
           }
           
       }
       
       return pares_freq;
    }
    
    
    public static pares_atributos Search_and_get(String par1, String par2,ArrayList<pares_atributos> lista )
    {
      for(pares_atributos current : lista)
      {
          if(par1.equals(current.get_name_attr1()) && par2.equals(current.get_name_attr2())) return current;
          else if (par2.equals(current.get_name_attr1()) && par1.equals(current.get_name_attr2())) return current;
      }
      return null;
    }
    
    public static int get_valor_max_entre_pares_atrr (ArrayList<pares_atributos> lista)
    {
        int mayor=0;
        
        for(pares_atributos current : lista)
            if(mayor<current.get_cant_veces()) mayor=(int)current.get_cant_veces();
        
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
    
    public static int get_valor_min_entre_pares_atrr (ArrayList<pares_atributos> lista)
    {
        int min=(int)lista.get(0).get_cant_veces();
        
        for(pares_atributos current : lista)
            if(min >current.get_cant_veces()) min=(int)current.get_cant_veces();
        
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
    
    
    private static pares_atributos Devuelve_el_par(String att1, String att2, ArrayList<pares_atributos> lista)
    {
        for( pares_atributos current : lista)
        {
            if(current.get_name_attr1().equals(att1) && current.get_name_attr2().equals(att2))return current;
            if(current.get_name_attr1().equals(att2) && current.get_name_attr2().equals(att1))return current;
        }
        return null;
    }
    
    
    public static ArrayList<pares_atributos> Encuentra_pares_attr_seleccionados (ArrayList<pares_atributos> pares_label , ArrayList<String> labels)
    {
       ArrayList<pares_atributos> result = new ArrayList();
       
       pares_atributos current;
       
       for(int i=0; i<labels.size()-1; i++)
       {
           for(int j=i+1; j<labels.size(); j++)
           {
               current = Devuelve_el_par(labels.get(i), labels.get(j), pares_label);
               if(current!=null) result.add(current);
           }
       }    
       
        return result;
    }
    
    public static ArrayList<pares_atributos> Get_pares_atributos(MultiLabelInstances dataset)
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
        System.out.println("words1: " + Arrays.toString(words));
        path = words[words.length-1];
        String dir = new String();
        if(words.length > 1){
            for(int i=0; i<words.length-1; i++){
                dir += words[i] + "/";
            }
        }
        System.out.println("dir1: " + dir);
        System.out.println("path1: " + path);
        /*words = path.split("-");
        if(words.length > 1){
            System.out.println("words2: " + Arrays.toString(words));
            path = "";
            for(int i=0; i<words.length-1; i++){
                path += words[i]+"-";
            }
            path = path.substring(0, path.length()-1) + ".xml";
        }
        else{
            System.out.println("path2: " + path);
            path = path.split("\\.")[0];
            System.out.println("path3: " + path);
            path+=".xml";
        }
        */
        System.out.println("path4: " + path);
        path = dir+path;
        System.out.println("path5: " + path);
        
        return path;
    }
    
    public static boolean Has_more_n_digits(double d, int digits)
    {
    
        String text = Double.toString(Math.abs(d));
        int integerPlaces = text.indexOf('.');
        int decimalPlaces = text.length() - integerPlaces - 1;
        
        if(decimalPlaces<=digits) return false;
        return true;
    }
    
    public static String Truncate_value(double value, int digits)
    {
       
        String number = Double.toString(value);
        int count_dig =0;
        String result="";
        boolean flag =false;
        
        if(!Has_more_n_digits(value, digits)) return Double.toString(value);
        
        for(int i=0; i<number.length();i++)
        {
            if(flag && count_dig!=digits)count_dig++;
                
            if(number.charAt(i)=='.')
            {flag=true; continue;}
            
            if(count_dig == digits) 
            {
                result=number.substring(0,i);
                break;
            }
        }
        return result;
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
        

        
}
