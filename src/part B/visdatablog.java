

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.assignment.DataShapeAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.ControlAdapter;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.ToolTipControl;
import prefuse.controls.ZoomControl;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.ShapeRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.ui.JFastLabel;
import prefuse.util.ui.UILib;
import prefuse.visual.EdgeItem;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;
import prefuse.controls.NeighborHighlightControl;
import java.util.*;

/**
 * 
 * Builds a visualization on the dataset on blog affiliations.
 *
 */

public class visdatablog extends Display

{
	
	public static final String GRAPH = "graph";
    public static final String NODES = "graph.nodes";
    public static final String EDGES = "graph.edges";
   
    
 public visdatablog(Graph clusters, int[][] weights, int[][]weightsS, int numb_nodes) 
 {
    	
	 	
        super(new Visualization());
        
        int numb_total = 0;
        
        
        
        ShapeRenderer nodeR = new ShapeRenderer(20);
        
        
        initDataGroups(clusters);
        
        DefaultRendererFactory drf = new DefaultRendererFactory();
        drf.setDefaultRenderer(nodeR);
        m_vis.setRendererFactory(drf);
        
       
        ColorAction nStroke = new ColorAction(NODES, VisualItem.STROKECOLOR);
        nStroke.setDefaultColor(ColorLib.rgba(255,0,0,150));
        
        ColorAction nEdges = new ColorAction(EDGES, VisualItem.STROKECOLOR);
        nEdges.setDefaultColor(ColorLib.gray(100));
        
        int[] palette = new int[] {
            ColorLib.rgba(255,0,0,150),
            ColorLib.rgba(0,0,255,150),
        };
       
        ColorAction aFill = new DataColorAction(NODES, "value",
                Constants.NOMINAL, VisualItem.FILLCOLOR, palette);
        
       
    //    ColorAction aFill = new ColorAction(NODES, VisualItem.FILLCOLOR, (ColorLib.rgba(255, 0, 0, 150)));
        aFill.add(VisualItem.FIXED, ColorLib.rgb(204,255,0));  // assign color when hover over it
        aFill.add(VisualItem.HIGHLIGHT, ColorLib.rgb(204,255,0)); // assign color to the connected nodes.
        
        
        // bundle the color actions
        ActionList colors = new ActionList();
        colors.add(nStroke);
        colors.add(nEdges);
        colors.add(aFill);
        colors.add(new ColorAction(NODES, VisualItem.TEXTCOLOR, ColorLib.rgb(0,0,0)));


        
        // now create the main layout routine
        ActionList layout = new ActionList(Activity.INFINITY);
        layout.add(colors);
        layout.add(new MyForceDirectedLayout1(GRAPH, weights, false));
        layout.add(new RepaintAction());
        m_vis.putAction("layout", layout);
        

        setSize(800,600);
        pan(500,300);
        setHighQuality(true);
        addControlListener(new myDragControl());
        addControlListener(new ZoomControl());
        addControlListener(new PanControl());
        addControlListener(new NeighborHighlightControl());
        

        
        m_vis.run("layout");
        
        
 }
 
 /**
  * Initialize the given graph to a Visual Graph.
  * @param g
  */
 
 private void initDataGroups(Graph g) {
    
     VisualGraph vg = m_vis.addGraph(GRAPH, g);
     m_vis.setInteractive(g.EDGES, null, false);
     m_vis.setValue(g.NODES, null, VisualItem.SHAPE,
             new Integer(Constants.SHAPE_ELLIPSE));
   
 }
    
	public static void main(Graph clusters, int[][] weights, int[][]weightsS, int numb_nodes) 
	{
		
		
		JComponent comp = demo1(clusters, weights, weightsS, numb_nodes);
        JFrame frame = new JFrame("A s s i g n m e n t - 1 b");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setContentPane(comp);
        frame.pack();
        frame.setVisible(true);
        
    
	}
    public static JComponent demo1(Graph clusters, int[][] weights, int[][]weightsS, int numb_nodes) {
        
        
        final visdatablog init = new visdatablog(clusters,weights,weightsS,numb_nodes);
        

		
        final JFastLabel title = new JFastLabel("                 ");
        title.setPreferredSize(new Dimension(350, 40));
        title.setVerticalAlignment(SwingConstants.BOTTOM);
        title.setBorder(BorderFactory.createEmptyBorder(3,0,0,0));
        title.setFont(FontLib.getFont("Tahoma", Font.PLAIN, 20));
        
        
        
        init.addControlListener(new ControlAdapter() {
            public void itemEntered(VisualItem item, MouseEvent e) {
                title.setText(item.getString("label"));
            }
            public void itemExited(VisualItem item, MouseEvent e) {
                title.setText(null);
            }
        });
        
       Box box = UILib.getBox(new Component[]{title}, true, 10, 3, 0);
       
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(init, BorderLayout.CENTER);
        panel.add(box, BorderLayout.SOUTH);
        UILib.setColor(panel, Color.BLACK, Color.GRAY);
        return panel;
        
        
    }
    
    /**
	 * Changed the DragControl to highlight and zoom items when mouse is hovered over them.
	 */
class myDragControl extends DragControl{
	
	private VisualItem activeItem;
    protected String action;
    protected Point2D down = new Point2D.Double();
    protected Point2D temp = new Point2D.Double();
    protected boolean dragged, wasFixed, resetItem;
    private boolean fixOnMouseOver = true;
    protected boolean repaint = true;
	
	public myDragControl() {
		
		super();
		
	}
	
	public void itemEntered(VisualItem item, MouseEvent e) {
		Display d = (Display)e.getSource();
        d.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        activeItem = item;
        if ( fixOnMouseOver ) {
            wasFixed = item.isFixed();
            resetItem = true;
            item.setFixed(true);
            item.getTable().addTableListener(this);

        }
        item.setHighlighted(true);
        item.setSize(2.0d);
	}
	
	public void itemExited(VisualItem item, MouseEvent e) {
        if ( activeItem == item ) {
            activeItem = null;
            item.getTable().removeTableListener(this);
            if ( resetItem ) item.setFixed(wasFixed);

        }
        Display d = (Display)e.getSource();
        d.setCursor(Cursor.getDefaultCursor());
        item.setHighlighted(false);
        item.setSize(1.0d);
    } //
	
}
}

/**
 * 
 * Extends the ForceDirectedLayout to change the spring coefficient of the edges.
 * Also,implemented a method to bring the nodes with same color nearer and different colors far away.
 *
 */
class MyForceDirectedLayout1 extends ForceDirectedLayout
{
	public MyForceDirectedLayout1(String graph, int[][]weights, boolean enforceBounds) {
		super(graph,enforceBounds);
	}
	
    /**
     * Loads the simulator with all relevant force items and springs.
     * @param fsim the force simulator driving this layout
     */
   
	
	protected float getSpringCoefficient(EdgeItem e) {
		
		return 55E-9f;
		
    }
}