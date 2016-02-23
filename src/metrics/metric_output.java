/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metrics;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import mulan.data.MultiLabelInstances;

/**
 *
 * @author oscglezm
 */
public class metric_output extends javax.swing.JFrame {

    /**
     * Creates new form metric_output
     */
    ArrayList<String> metric_list,metric_list1,metric_list2;
    ArrayList<MultiLabelInstances> list_datasets;
    
    MultiLabelInstances dataset, dataset_train, dataset_test;
    int posx,posy;
    
    String[] info;
    boolean es_de_tipo_meka;
     
    // multilabel datasets
    public metric_output(ArrayList<String> metric_list ,ArrayList<MultiLabelInstances> list_datasets, ArrayList<String> dataset_names , ArrayList<Boolean> es_de_tipo_meka )
    {
      this.metric_list = metric_list;
      this.list_datasets = list_datasets;
      
         
        this.setUndecorated(true);
        
        initComponents();
         
        this.setBounds(10, 10, 800, 600);
        
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        boolean flag=true;
        String encabezado = " Relation names"+"\t"+"\t" +"\t"+"\t";
        
        String value,temp;
        
        atributo[] imbalanced_data, label_frenquency;
        
        int count_meka=0;
        
        for(String metric : metric_list)
        {
            value= metric+ util.get_tabs_multi_datasets(metric);
            System.out.println(value);
            
            for( String current_name : dataset_names)
                if(flag){ encabezado+= current_name+"\t";}
            
            count_meka=0;
            for(MultiLabelInstances current : list_datasets)
            {
                label_frenquency = util.Get_Frequency_x_label(current);
                label_frenquency = util.Ordenar_freq_x_attr(label_frenquency);// ordena de mayor a menor
             
                imbalanced_data = util.Get_data_imbalanced_x_label_inter_class(current,label_frenquency);
                
                temp = util.get_value_metric(metric, current, es_de_tipo_meka.get(count_meka));
                
                if(temp.equals("-1.0")){
                    temp= util.get_value_metric_imbalanced(metric, current, imbalanced_data);
                }
                
                if(temp.contains("NaN")){
                    temp = " ---";
                }
                
                value+=temp +"\t";
                count_meka++;
            }   
           
            if(flag)
            {
                flag =false;
                jTextArea1.append(encabezado+"\n");
            }
            
            jTextArea1.append(value+"\n");
          
          
        }
        
        
    }
    
    
    //para pestaña TRAIN/TEST CV
    public metric_output(ArrayList<String> metric_list_comun, ArrayList<String> metric_list_train, ArrayList<String> metric_list_test , ArrayList<MultiLabelInstances> list_datasets_train, ArrayList<MultiLabelInstances> list_datasets_test, int posx, int posy, boolean es_de_tipo_meka)
    {
      
        this.setUndecorated(true);
        initComponents();
         this.es_de_tipo_meka = es_de_tipo_meka;
               
        if( metric_list_comun.size()+metric_list_train.size()+metric_list_test.size()>20)
        {
         this.setBounds(10, 30, 700, 600);
        }
        else{
         if(posy > 180 && posx<700) this.setBounds(posx, posy-180, 600, 200);
         else if (posy > 180 && posx>700 ) this.setBounds(posx-500, posy, 600, 200);
         else this.setBounds(posx,posy+540,600,200);
        }
        
        
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
               
        boolean flag=true;
        String encabezado = "Commun Metrics for train/test set:"+"\t"+"\t"+"\t";
        
        String temp,temp1,value="";
        
        ArrayList<Double> lista_valores;
        double current_double;
        String value_truncate;
         
        this.metric_list = metric_list_comun;
        this.metric_list1 = metric_list_train;
        this.metric_list2 = metric_list_test;
                
        MultiLabelInstances current;
        
        for(String metric : metric_list)
        {
            value= metric+ util.get_tabs_multi_datasets(metric);
            
            lista_valores= new ArrayList();
            current_double=-1.0;
            //for( int i=0; i<list_datasets_train.size();i++)
                if(flag){ encabezado+= "Fold"+"\t";}
            
            if(flag)
            {
                flag =false;
                jTextArea1.append(encabezado+"\n");
            }
            
            for(int n=0; n<list_datasets_train.size();n++)
            {
                 temp= util.get_value_metric(metric, list_datasets_train.get(n), es_de_tipo_meka); //can  be dataset_test too
                            
                 if(temp.equals( "-1.0" )) temp = util.get_alternative_metric(metric, list_datasets_train.get(n), list_datasets_test.get(n));
           
                 current_double = Double.parseDouble(temp);
                 
                 if(Double.isNaN(current_double) || current_double==-1.0)break;
                 
                 lista_valores.add(current_double);
                  
            }
            
             if(Double.isNaN(current_double)) jTextArea1.append(value+"NaN"+"\n");   
             
             else if (current_double==-1.0) jTextArea1.append(value+current_double+"\n");   
    
             else{             
                 value_truncate = Double.toString(util.Get_media(lista_valores));
                 jTextArea1.append(value+util.Truncate_values_aprox_zero(value_truncate, 5)+"\n");                
                 }
            
        }
         
       //metrics train
        jTextArea1.append("\n");
        jTextArea1.append("\n");
             
        encabezado = "Metrics for train set:"+"\t"+"\t" +"\t"+"\t";
        
        atributo[] imbalanced_data,imbalanced_data1, label_frequency,label_frequency1;
        flag =true;
        

        
        for(String metric : metric_list1)
        {
            value= metric+ util.get_tabs_multi_datasets(metric);
            
            lista_valores= new ArrayList();
            current_double=-1.0;
            
            //for( int i=0; i<list_datasets_train.size();i++)
                if(flag){ encabezado+= "Train fold"+"\t";}
            
            if(flag)
            {
                flag =false;
                jTextArea1.append(encabezado+"\n");
            }
            
            for(int n=0; n<list_datasets_train.size();n++)
            {
                current = list_datasets_train.get(n);
                
                label_frequency = util.Get_Frequency_x_label(current);
                label_frequency = util.Ordenar_freq_x_attr(label_frequency);// ordena de mayor a menor
             
                imbalanced_data = util.Get_data_imbalanced_x_label_inter_class(current,label_frequency);
                
                
               // imbalanced_data =  util.Get_data_imbalanced_x_label(list_datasets_train.get(n));
                             
                 temp= util.get_value_metric(metric, list_datasets_train.get(n), es_de_tipo_meka); 
                               
                if(temp.equals( "-1.0" )) temp = util.get_value_metric_imbalanced(metric, list_datasets_train.get(n), imbalanced_data);
                if(temp.equals( "-1.0" )) temp ="NaN";
                                  
                current_double = Double.parseDouble(temp);
                 
                if(Double.isNaN(current_double) || current_double==-1.0)break;
                 
                lista_valores.add(current_double);
                                  
            }
            
              if(Double.isNaN(current_double)) jTextArea1.append(value+"NaN"+"\n");   
             
             else if (current_double==-1.0) jTextArea1.append(value+current_double+"\n");   
    
             else{             
                 value_truncate = Double.toString(util.Get_media(lista_valores));
                 jTextArea1.append(value+ util.Truncate_values_aprox_zero(value_truncate, 5)+"\n");                  
                 }     
        }
        
         //metrics test
               
        jTextArea1.append("\n");
        jTextArea1.append("\n");
        
         encabezado = "Metrics for test set:"+"\t"+"\t" +"\t"+"\t";
              
         flag =true;
        
        for(String metric : metric_list2)
        {
            lista_valores= new ArrayList();
            current_double=-1.0;
                          
            value= metric+ util.get_tabs_multi_datasets(metric);
            
          //  for( int i=0; i<list_datasets_train.size();i++)
                if(flag){ encabezado+= "Test fold"+"\t";}
            
            if(flag)
            {
                flag =false;
                jTextArea1.append(encabezado+"\n");
            }
            
            for(int n=0; n<list_datasets_test.size();n++)
            {              
                current = list_datasets_test.get(n);
                
                label_frequency1 = util.Get_Frequency_x_label(current);
                label_frequency1 = util.Ordenar_freq_x_attr(label_frequency1);// ordena de mayor a menor
             
                imbalanced_data1 = util.Get_data_imbalanced_x_label_inter_class(current,label_frequency1);
                
             //   imbalanced_data1 = util.Get_data_imbalanced_x_label(list_datasets_test.get(n));
                               
                 temp1= util.get_value_metric(metric, list_datasets_test.get(n), es_de_tipo_meka); 
                 
                 if(temp1.equals( "-1.0" )) temp1 = util.get_alternative_metric(metric, list_datasets_train.get(n), list_datasets_test.get(n));
                 if(temp1.equals( "-1.0" )) temp1 = util.get_value_metric_imbalanced(metric, list_datasets_test.get(n), imbalanced_data1);
                 
                current_double = Double.parseDouble(temp1);
                 
                if(Double.isNaN(current_double) || current_double==-1.0)break;
                 
                lista_valores.add(current_double);
                                  
            }
            
              if(Double.isNaN(current_double)) jTextArea1.append(value+"NaN"+"\n");   
             
             else if (current_double==-1.0) jTextArea1.append(value+current_double+"\n");   
    
             else{             
                 value_truncate = Double.toString(util.Get_media(lista_valores));
                 
                 jTextArea1.append(value+util.Truncate_values_aprox_zero(value_truncate, 5)+"\n");                
                 }  
        }
        
        
         
         
         
    }
    
    
    // IR per labelset (cuando se toca una fila de la tabla)
    public metric_output(MultiLabelInstances dataset , int posx, int posy,String[] args, ArrayList<String> labelnames, atributo[] label_frequency, boolean es_de_tipo_meka)
    {
        this.dataset = dataset;
        this.posx = posx;
        this.posy = posy;
        info = args;        
        this.es_de_tipo_meka = es_de_tipo_meka;
        
        this.setUndecorated(true);
        initComponents();
         
        print_information(labelnames,label_frequency);
        
        if(labelnames.size()>10)
        {
        this.setBounds(10, 30, 700, 600);
        }
        else{
         if(posy > 180 && posx<700) this.setBounds(posx, posy-180, 600, 200);
         else if (posy > 180 && posx>700 ) this.setBounds(posx-500, posy, 600, 200);
         else this.setBounds(posx,posy+490,600,200);
        }
    }
    
    
    public metric_output(MultiLabelInstances dataset , int posx, int posy, ArrayList<String> labelnames, atributo[] label_frequency, boolean es_de_tipo_meka)
    {
        this.dataset = dataset;
        this.posx = posx;
        this.posy = posy;
        this.es_de_tipo_meka = es_de_tipo_meka;
        
        
        this.setUndecorated(true);
        initComponents();
         
        print_information1(labelnames,label_frequency);
        
        if(labelnames.size()>10)
        {
        this.setBounds(10, 30, 700, 600);
        }
        else{
         if(posy > 180 && posx<700) this.setBounds(posx, posy-180, 600, 200);
         else if (posy > 180 && posx>700 ) this.setBounds(posx-500, posy, 600, 200);
         else this.setBounds(posx,posy+490,600,200);
        }
    }
    
    
    // Tab "Dataset"
    public metric_output(ArrayList<String> metric_list, MultiLabelInstances dataset , int posx, int posy, boolean es_de_tipo_meka) {
                
        this.posx = posx;
        this.posy = posy;
        this.es_de_tipo_meka = es_de_tipo_meka;
        this.metric_list = metric_list;

        
        this.setUndecorated(true);
        initComponents();
         
        this.dataset = dataset;
        
       atributo[] label_frenquency = util.Get_Frequency_x_label(dataset);
       label_frenquency = util.Ordenar_freq_x_attr(label_frenquency);// ordena de mayor a menor
                
        atributo[] imbalanced_data = util.Get_data_imbalanced_x_label_inter_class(dataset,label_frenquency);
        
        String value;
        for(String metric : metric_list)
        {
           value= util.get_value_metric(metric, dataset, es_de_tipo_meka);
           if(value.equals("-1.0")){
               value = util.get_value_metric_imbalanced(metric, dataset, imbalanced_data);
           } 	
           
            System.out.println(value);
           if(value.contains("NaN")){
               jTextArea1.append(metric + util.get_tabs_multi_datasets(metric) + " ---" + "\n");
           }else{
               jTextArea1.append(metric + util.get_tabs_multi_datasets(metric) + value + "\n");
           }           
        }
         
        if(metric_list.size()>20)
        {
        this.setBounds(10, 30, 700, 600);
        }
        else
        { 
         if(posy > 180 && posx<700) {
             this.setBounds(posx, posy-180, 600, 200);
         }
         else if (posy > 180 && posx>700 ) {
             this.setBounds(posx-500, posy, 600, 200);
         }
         else {
             this.setBounds(posx,posy+490,600,200);
         }
        }
    }
    
    public metric_output(MultiLabelInstances dataset , String label1, String label2, int posx, int posy , boolean es_de_tipo_meka )
    {
        this.posx = posx;
        this.posy = posy;
        this.es_de_tipo_meka = es_de_tipo_meka;
        this.setUndecorated(true);
        initComponents();
         
        this.dataset = dataset;
        
        int pos_label1 = dataset.getDataSet().attribute(label1).index();
        int pos_label2 = dataset.getDataSet().attribute(label2).index();
        
        double repet ,freq;
        
        String tabs ="\t"+"\t" +"\t";
        String tabs2 = "\t"+"\t";
        String tabs1 = "\t";
        String truncate;
        
        if(label1.equals(label2))
        {
            
            repet = util.Get_repetition_number_x_1_label(dataset, pos_label1);
            freq = repet/dataset.getNumInstances();
            truncate =Double.toString(freq);
            
            jTextArea1.append("Labels: "+tabs+label1+" \n");
            jTextArea1.append("Frequency:"+tabs+ util.Truncate_values_aprox_zero(truncate, 5) +"\n");
            jTextArea1.append("# Examples:"+ tabs+ ((int)repet)+"\n");
            
        }
        
        else 
        {
            jTextArea1.append("Labels: "+tabs2+label1+" (A)"+ "  ------  " + label2+" (B)"+ "\n");
            
            truncate =Double.toString(util.Get_probability_x_2_labels(dataset, pos_label1, pos_label2));
            
            jTextArea1.append("Prob(A|B):"+tabs+util.Truncate_values_aprox_zero(truncate, 5)  +" \n");
            
            double repet_A_B = util.Get_repetitions_x_2_labels(dataset, pos_label1, pos_label2);
            double freq_A_B = repet_A_B/dataset.getNumInstances();
            
            jTextArea1.append("Freq(A^B):"+tabs+ util.Truncate_values_aprox_zero(Double.toString(freq_A_B), 5)+" \n");
            jTextArea1.append("# Examples(A^B):"+tabs2+ ((int)repet_A_B)   +" \n");

            jTextArea1.append("----------------------------------------------------------------------------------------------"+" \n");
            repet = util.Get_repetition_number_x_1_label(dataset, pos_label1);
            freq = repet/dataset.getNumInstances();
            
            jTextArea1.append("Label:  "+tabs+label1+" \n");
            
            
            jTextArea1.append("Frequency:"+ tabs+ util.Truncate_values_aprox_zero(Double.toString(freq), 5)  +"\n");
            jTextArea1.append("# Examples:"+tabs+ ((int)repet) +"\n");
            
            repet = util.Get_repetition_number_x_1_label(dataset, pos_label2);
            freq = repet/dataset.getNumInstances();
            
            jTextArea1.append("----------------------------------------------------------------------------------------------"+" \n");
            jTextArea1.append("Label:  "+tabs+label2+" \n");
            
            
            jTextArea1.append("Frequency:"+tabs+ util.Truncate_values_aprox_zero(Double.toString(freq), 5) +"\n");
            jTextArea1.append("# Examples:"+tabs+ ((int)repet) +"\n");
            
        }
        
         
         if(posy > 180 && posx<700) this.setBounds(posx, posy-180, 600, 200);
         else if (posy > 180 && posx>700 ) this.setBounds(posx-500, posy, 600, 200);
         else this.setBounds(posx,posy+490,600,200);
        
    
    }
    
    
    
    //para pestaña TRAIN/TEST holdout
     public metric_output(ArrayList<String> metric_list_comun,ArrayList<String> metric_list_train,ArrayList<String> metric_list_test, MultiLabelInstances dataset_train , MultiLabelInstances dataset_test , int posx, int posy, boolean es_de_tipo_meka) {
                
        this.posx = posx;
        this.posy = posy;
        this.es_de_tipo_meka = es_de_tipo_meka;
        this.metric_list = metric_list_comun;
        this.metric_list1 = metric_list_train;
        this.metric_list2 = metric_list_test;
                
        //this.dataset = dataset;
        this.dataset_train = dataset_train;
        this.dataset_test = dataset_test;
        
        this.setUndecorated(true);
        initComponents();
         
        String value;
        
        //metrics commun
        jTextArea1.append("Commun Metrics for train/test set:"+"\n");
        for(String metric : metric_list)
        {
           value= util.get_value_metric(metric, dataset_train, es_de_tipo_meka); //can  be dataset_test too
           
           
           if(value.equals( "-1.0" )) value = util.get_alternative_metric(metric, dataset_train, dataset_test);
           
           jTextArea1.append(metric+util.get_tabs_multi_datasets(metric)+value+"\n");
        }
        
        //metrics train
        jTextArea1.append("\n");
        String train_title ="Metrics for train set:";        
              
        jTextArea1.append(train_title+"\n");
        
        
        atributo[] label_frenquency = util.Get_Frequency_x_label(dataset_train);
        label_frenquency = util.Ordenar_freq_x_attr(label_frenquency);// ordena de mayor a menor
             
        atributo[] imbalanced_data =  util.Get_data_imbalanced_x_label_inter_class(dataset_train,label_frenquency);
        
        for(String metric : metric_list1)
        {
           value= util.get_value_metric(metric, dataset_train, es_de_tipo_meka);
           if(value.equals("-1.0")) value = util.get_value_metric_imbalanced(metric, dataset_train, imbalanced_data);
           if(value.equals("-1.0")) value ="NaN";
           jTextArea1.append(metric+util.get_tabs_multi_datasets(metric)+value+"\n");
        }
        
        //metrics test
        
        
        jTextArea1.append("\n");
                        
        String temp = "Metrics for"+ " test set:";
        
        jTextArea1.append(temp+"\n");
        
        label_frenquency = util.Get_Frequency_x_label(dataset_test);
        label_frenquency = util.Ordenar_freq_x_attr(label_frenquency);// ordena de mayor a menor
        
        imbalanced_data = util.Get_data_imbalanced_x_label_inter_class(dataset_test,label_frenquency);
        
        for(String metric : metric_list2)
        {
           value= util.get_value_metric(metric, dataset_test, es_de_tipo_meka);
           if(value.equals( "-1.0" )) value = util.get_alternative_metric(metric, dataset_train, dataset_test);
           if(value.equals("-1.0")) value = util.get_value_metric_imbalanced(metric, dataset_test, imbalanced_data);
           jTextArea1.append(metric+util.get_tabs_multi_datasets(metric)+value+"\n");
        }
         
        if( metric_list_comun.size()+metric_list_train.size()+metric_list_test.size()>20)
        {
         this.setBounds(10, 30, 700, 600);
        }
        else{
         if(posy > 180 && posx<700) this.setBounds(posx, posy-180, 600, 200);
         else if (posy > 180 && posx>700 ) this.setBounds(posx-500, posy, 600, 200);
         else this.setBounds(posx,posy+540,600,200);
        }
    }
    
    
    
     
     private void print_information1(ArrayList<String> labelnames, atributo[] label_frequency)
    {
        String tabs ="\t"+"\t" +"\t";
        String tabs1 = "\t";
        this.es_de_tipo_meka = es_de_tipo_meka;
        atributo current;
        double freq;
        int repeat, label_id=1;
        
        String truncate;
        
        for(int i=0;i<labelnames.size(); i++)
        {
              current = util.Get_label_x_labelname(labelnames.get(i), label_frequency);
              
              if (current != null)
              {
                  repeat = current.get_frequency();
                  freq = repeat*1.0/(dataset.getNumInstances());
                  System.out.println("freq "+ freq);
            
                  jTextArea1.append("--------------------------------------------------------------------------------------------------------------------------" +"\n");
                  jTextArea1.append("Label "+ label_id+":  "+tabs+current.get_name()+" \n");
                  
                  
                  jTextArea1.append("Frequency:"+ tabs+ util.Truncate_values_aprox_zero(Double.toString(freq), 5)  +"\n");
                  jTextArea1.append("# Examples:"+tabs+ repeat +"\n");
        
                  label_id++;
              }
        }
    
    }
     
     
    //IR per labelset
    private void print_information(ArrayList<String> labelnames, atributo[] label_frequency)
    {
        String tabs ="\t"+"\t" +"\t";
        String tabs2 = "\t"+"\t";
        String tabs1 = "\t";
        
        int cant_repeats = Integer.parseInt(info[1]);
        double value = cant_repeats*1.0/dataset.getNumInstances();
        
        jTextArea1.append("Labelset: "+tabs+ info[0]+"\n");
        jTextArea1.append("# Labels: "+tabs+labelnames.size()+"\n");
        
        
        jTextArea1.append("Frequency: "+tabs+ util.Truncate_values_aprox_zero(Double.toString(value), 5)  +"\n");
        jTextArea1.append("# Examples: " +tabs+info[1]+"\n");
        
        

        atributo current;
        double freq;
        int repeat, id_label=1;
        
        for(int i=0;i<labelnames.size(); i++)
        {
              current = util.Get_label_x_labelname(labelnames.get(i), label_frequency);
              
              if (current != null)
              {
                  repeat = current.get_frequency();
                  freq = repeat*1.0/dataset.getNumInstances();
            
                  jTextArea1.append("--------------------------------------------------------------------------------------------------------------------------" +"\n");
                  jTextArea1.append("Label "+id_label +":  "+tabs+current.get_name()+" \n");
                  
                  
                  jTextArea1.append("Frequency:"+ tabs+ util.Truncate_values_aprox_zero(Double.toString(freq), 5) +"\n");
                  
                  jTextArea1.append("# Examples: "+tabs+ repeat +"\n");
                  
                //  freq = current.get_frequency()/(dataset.getNumInstances()*1.0);
                //  jTextArea1.append(current.get_name()+ tabs +"" +util.Truncate_value(freq,5)+" | " +current.get_frequency()+"\n");
                 id_label++; 
              }
        }
        
        //jTextArea1.append(info[2]+"\n");
        
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Output values"));

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextArea1FocusLost(evt);
            }
        });
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextArea1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextArea1FocusLost
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_jTextArea1FocusLost

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(metric_output.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(metric_output.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(metric_output.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(metric_output.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
