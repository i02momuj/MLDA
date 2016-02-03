/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;


import javax.swing.JFrame;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class HelloWorld extends JFrame
{


	private static final long serialVersionUID = -2707712944901661771L;

	public HelloWorld()
	{
		super("Hello, World!");

		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try
		{
			Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 100,
					20,"ROUNDED");
			Object v2 = graph.insertVertex(parent, null, "World!", 240, 150,
					80, 30);
                   
                        Object v3 = graph.insertVertex(parent, null, "TEST", 300, 100,
					100, 20,"ROUNDED;strokeColor=red;fillColor=green");
                        
                         Object v4 = graph.insertVertex(parent, null, "TEST", 100, 300,
					100, 20,"ROUNDED;strokeColor=red;fillColor=green");
                   
			graph.insertEdge(null, null, "", v1, v2,"startArrow=none;endArrow=none;strokeWidth=4");
                        graph.insertEdge(parent, null, "", v1, v3,"startArrow=none;endArrow=none;strokeWidth=2");
                        graph.insertEdge(parent, null, "", v3, v2,"startArrow=none;endArrow=none;strokeWidth=6");
                        graph.insertEdge(parent, null, "", v3, v4,"startArrow=none;endArrow=none;strokeWidth=1");
                        graph.insertEdge(parent, null, "", v4, v2,"startArrow=none;endArrow=none;strokeWidth=3");
                        graph.insertEdge(parent, null, "", v4, v1,"startArrow=none;endArrow=none;strokeWidth=5");
		}
		finally
		{
			graph.getModel().endUpdate();
		}

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		getContentPane().add(graphComponent);
	}

	public static void main(String[] args)
	{
		HelloWorld frame = new HelloWorld();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 320);
		frame.setVisible(true);
	}

}
