/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;



/* The java code below generates a XY line chart using JFreeChart java API */
import java.io.*;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.axis.*;
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.ChartUtilities; 
public class XYlineChart {  
      public static void main(String[] args){
         try {
                /* Define some XY Data series for the chart */
                XYSeries team1_xy_data = new XYSeries("Team 1");
                team1_xy_data.add(1990, 45);
                team1_xy_data.add(1991, 16);
                team1_xy_data.add(1992, 80);
                team1_xy_data.add(1993, 1);
                team1_xy_data.add(1994, 6);
                
                XYSeries team2_xy_data = new XYSeries("Team 2");
                team2_xy_data.add(1990, 2);
                team2_xy_data.add(1991, 10);
                team2_xy_data.add(1992, 60);
                team2_xy_data.add(1993, 60);
                team2_xy_data.add(1994, 18);
                
                XYSeries team3_xy_data = new XYSeries("Team 3");
                team3_xy_data.add(1990, 15);
                team3_xy_data.add(1991, 5);
                team3_xy_data.add(1992, 14);
                team3_xy_data.add(1993, 18);
                team3_xy_data.add(1994, 25);
                
                /* Add all XYSeries to XYSeriesCollection */
                //XYSeriesCollection implements XYDataset
                XYSeriesCollection my_data_series= new XYSeriesCollection();
                // add series using addSeries method
                my_data_series.addSeries(team1_xy_data);
                my_data_series.addSeries(team2_xy_data);
                my_data_series.addSeries(team3_xy_data);
                
                
                //Use createXYLineChart to create the chart
                JFreeChart XYLineChart=ChartFactory.createXYLineChart("Team - Number of Wins","Year","Win Count",my_data_series,PlotOrientation.VERTICAL,true,true,false);
          
                /* Step -3 : Write line chart to a file */               
                 int width=640; /* Width of the image */
                 int height=480; /* Height of the image */                
                 File XYlineChart=new File("xy_line_Chart_example.png");              
                 ChartUtilities.saveChartAsPNG(XYlineChart,XYLineChart,width,height); 
         }
         catch (Exception i)
         {
             System.out.println(i);
         }
     }
 }

