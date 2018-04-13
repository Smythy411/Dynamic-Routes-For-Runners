package hello;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jgrapht.Graph;
import org.jgrapht.io.CSVExporter;
import org.jgrapht.io.CSVFormat;
import org.jgrapht.io.ComponentNameProvider;
import org.jgrapht.io.StringComponentNameProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/*
 * Controller for handling GET requests from the client
 */
@RestController
public class GraphController {
	
	OSMNode sourceNode = new OSMNode();
	
	//Endpoint for GETting optimal route using Approach 3
	//The @RequestParam notation maps the fields passed from the get request
	@RequestMapping("/GetBestGraph")
	public Route getBestGraph(@RequestParam(value="source", defaultValue="500") int sourceN, 
			@RequestParam(value="reqDistance", defaultValue="5.0") double reqDistance,
			@RequestParam(value="sourceLat", defaultValue="53.279") double sourceLat,
			@RequestParam(value="sourceLon", defaultValue="-6.346") double sourceLon,
			@RequestParam(value="x1", defaultValue="53.2745") String x1,
			@RequestParam(value="y1", defaultValue="-6.3553") String y1,
			@RequestParam(value="x2", defaultValue="53.2897") String x2,
			@RequestParam(value="y2", defaultValue="-6.3302") String y2,
			@RequestParam(value="includeResidential", defaultValue="true") boolean includeResidential) {
		
		System.out.println("New Route Request of length " + reqDistance + "!");
		System.out.println("Boundary: " + x1 + " / " + y1 + " / " + x2 + " / " + y2);
		
		GraphConstructor gc = new GraphConstructor(reqDistance);
		DBManager db = new DBManager("drfr-dublin");
		
		Graph<OSMNode, OSMEdge> graph = createFullGraph(gc, db, includeResidential, sourceLat, sourceLon, x1, y1, x2, y2);
		
		db.close();
		
	    OSMEdge[] edgeSet = graph.edgeSet().toArray(new OSMEdge[graph.edgeSet().size()]);
		
	    System.out.println("Attempting Optimal Approach 3...");
		Route DJKRoute = gc.constructDJKRoute(graph, edgeSet, sourceNode, reqDistance);
		
		if (DJKRoute != null) {
			System.out.println("Sending Route to Client " + DJKRoute.getRoute().size());
		} else {
			System.out.println("Unable to generate route. Returning null");
		}//end if else
		return DJKRoute;
	}//End getGraph
	
	//Endpoint for GETting quick route using Approach 3
	//The @RequestParam notation maps the fields passed from the get request
	@RequestMapping("/GetQuickGraph")
	public Route getQuickGraph(@RequestParam(value="source", defaultValue="500") int sourceN, 
			@RequestParam(value="reqDistance", defaultValue="5.0") double reqDistance,
			@RequestParam(value="sourceLat", defaultValue="53.279") double sourceLat,
			@RequestParam(value="sourceLon", defaultValue="-6.346") double sourceLon,
			@RequestParam(value="x1", defaultValue="53.2745") String x1,
			@RequestParam(value="y1", defaultValue="-6.3553") String y1,
			@RequestParam(value="x2", defaultValue="53.2897") String x2,
			@RequestParam(value="y2", defaultValue="-6.3302") String y2,
			@RequestParam(value="includeResidential", defaultValue="true") boolean includeResidential) {
		
		System.out.println("New Route Request of length " + reqDistance + "!");
		System.out.println("Boundary: " + x1 + " / " + y1 + " / " + x2 + " / " + y2);
		
		GraphConstructor gc = new GraphConstructor(reqDistance);
		DBManager db = new DBManager("drfr-dublin");
		
		Graph<OSMNode, OSMEdge> graph = createFullGraph(gc, db, includeResidential, sourceLat, sourceLon, x1, y1, x2, y2);
		
		db.close();
		
	    OSMEdge[] edgeSet = graph.edgeSet().toArray(new OSMEdge[graph.edgeSet().size()]);
		
	    System.out.println("Attempting Quick Approach 3...");
		Route DJKRoute = gc.constructQuickDJKRoute(graph, edgeSet, sourceNode, reqDistance);
		if (DJKRoute != null) {
			System.out.println("Sending Route to Client " + DJKRoute.getRoute().size());
		} else {
			System.out.println("Unable to generate route. Returning null");
		}//end if else
		return DJKRoute;
	}//End getGraph
	
	//*LEGACY* Endpoint for getting Approach 2 route
	@RequestMapping("/GetSimpleGraph")
	public Route getSimpleGraph(@RequestParam(value="source", defaultValue="500") int sourceN, 
			@RequestParam(value="reqDistance", defaultValue="5.0") double reqDistance,
			@RequestParam(value="sourceLat", defaultValue="53.279") double sourceLat,
			@RequestParam(value="sourceLon", defaultValue="-6.346") double sourceLon,
			@RequestParam(value="x1", defaultValue="53.2745") String x1,
			@RequestParam(value="y1", defaultValue="-6.3553") String y1,
			@RequestParam(value="x2", defaultValue="53.2897") String x2,
			@RequestParam(value="y2", defaultValue="-6.3302") String y2,
			@RequestParam(value="includeResidential", defaultValue="true") boolean includeResidential){
		
		System.out.println("New Route Request of length " + reqDistance + "!");
		System.out.println("Boundary: " + x1 + " / " + y1 + " / " + x2 + " / " + y2);
		
		GraphConstructor gc = new GraphConstructor(reqDistance);
		DBManager db = new DBManager("drfr-dublin");
		
		Graph<OSMNode, OSMEdge> graph = createFullGraph(gc, db, includeResidential, sourceLat, sourceLon, x1, y1, x2, y2);
		
		db.close();
		
	    OSMEdge[] edgeSet = graph.edgeSet().toArray(new OSMEdge[graph.edgeSet().size()]);
	    OSMNode source = edgeSet[sourceN].getSourceNode();
		
	    System.out.println("Attempting Approach 2...");
		//ArrayList<OSMEdge> subGraph = gc.constructSubGraph(false, 1, source, target, new ArrayList<OSMEdge>());
    	ArrayList<OSMEdge> subGraph =  gc.constructDFSandDJKRoute(1000, sourceNode, graph);
		//System.out.println(subGraph.size() + " : " + subGraph);
		
    	Route route = new Route();
    	route.addToRoute(subGraph);
		
		System.out.println("Sending Route to Client");
		return route;
	}//End getGraph
	
	//Exports the route as an adjacency list to a file on  the server
	public static void exportGraph(Graph g) throws FileNotFoundException, UnsupportedEncodingException {
		ComponentNameProvider<OSMNode> nodeName = new StringComponentNameProvider();
		ComponentNameProvider<OSMEdge> edgeName = new StringComponentNameProvider();
		
		CSVExporter cE = new CSVExporter(nodeName, CSVFormat.ADJACENCY_LIST, ',');
		cE.setEdgeIDProvider(edgeName);
		
		PrintWriter writer = new PrintWriter("/home/eoin/resources/graph.csv", "UTF-8");
		cE.exportGraph(g, writer);
	}//End exportGraph
	
	//Creating a Full Graph using the passed paramaters from the client
	public Graph<OSMNode, OSMEdge> createFullGraph(GraphConstructor gc, DBManager db, boolean includeResidential, double sourceLat, double sourceLon, String x1, String y1, String x2, String y2) {
		ArrayList<OSMNode> nodeList = db.getNodesByBoundary(x1, y1, x2, y2);
		System.out.println("Nodes within Boundary: " + nodeList.size());
		Map<Long, OSMNode> nodeMap = new HashMap<Long, OSMNode>();
		for (int i = 0; i < nodeList.size(); i++) {
			OSMNode node = nodeList.get(i);
			nodeMap.put(node.getNodeID(), node);
		}//end for
		
		ArrayList<OSMEdge> edges = db.getEdgesByBoundary(nodeMap, x1, y1, x2, y2);
		System.out.println("Edges within Boundary: " + edges.size());
		
		ArrayList<OSMWay> ways = db.getWaysByBoundary(nodeList, edges, x1, y1, x2, y2);
		System.out.println("Ways within Boundary: " + ways.size());
		
		OSMNode closestNode = new OSMNode();
		double closestDistance = 0.5;
		
		ArrayList<OSMEdge> wayEdges = new ArrayList<>();
		System.out.println("Finding Closest Node");
		
		ArrayList<String> highwaysToIgnore = new ArrayList<String>();
		highwaysToIgnore.add("null");
		highwaysToIgnore.add("service");
		highwaysToIgnore.add("primary");
		highwaysToIgnore.add("motorway");
		highwaysToIgnore.add("trunk");
		
		if (includeResidential == false) {
			highwaysToIgnore.add("residential");
		}//end if
		
		//Filters only necessary ways and gets closest not to det as source
		for (int i = 0; i < ways.size(); i++) {
			if (highwaysToIgnore.contains(ways.get(i).getHighway())) {
				//System.out.println("Not using these ways...Ignore");
			} else {
				ArrayList<OSMEdge> edgesToAdd = ways.get(i).getEdges();
				wayEdges.addAll(edgesToAdd);
				
				for (int j = 0; j < edgesToAdd.size(); j++) {
					if (j == 0) {
						OSMNode firstNode = edgesToAdd.get(j).getSourceNode();
						double checkDistance = Haversine.distance(sourceLat, sourceLon, Double.parseDouble(firstNode.getLat()), Double.parseDouble(firstNode.getLon()));
						if (checkDistance < closestDistance) {
							closestDistance = checkDistance;
							sourceNode = firstNode;
						}//end if
					}//end if
					OSMNode checkNode = edgesToAdd.get(j).getTargetNode();
					double checkDistance = Haversine.distance(sourceLat, sourceLon, Double.parseDouble(checkNode.getLat()), Double.parseDouble(checkNode.getLon()));
					if (checkDistance < closestDistance) {
						closestDistance = checkDistance;
						sourceNode = checkNode;
					}//end if
				}//end for
			}//end if else
		}//end for
		System.out.println("Closest Node is " + sourceNode.getLat() + ", " + sourceNode.getLon() + " which is " + closestDistance + " from current Location");
		
		Graph<OSMNode, OSMEdge> wayGraph = gc.createEdgeGraph(wayEdges);
		//Graph<OSMNode, OSMEdge> edgeGraph = gc.createEdgeGraph(edges);
		
		//Exporting Graph
		try {
			exportGraph(wayGraph);
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//End try catch
		
		return wayGraph;
		
	}//End createFullGraph
	
	//Creating a Graph with OSM Nodes grabbed using WayID (*TESTING*)
	public static void graphByWayID(GraphConstructor gt, DBManager db, long wayID) {
		ArrayList<OSMNode> nodes = db.getNodesbyWayId(wayID);
		ArrayList<OSMEdge> edges = new ArrayList<>();
		for (int i = 0; i < nodes.size() -1; i++) {
			System.out.println(nodes.get(i) + "/ " + nodes.get(i + 1) + " / " + (i - 1) + " / " + i);
			OSMEdge tempEdge = new OSMEdge(4258427, nodes.get(i), nodes.get(i + 1));
			edges.add(tempEdge);
			nodes.get(i).addEdge(tempEdge);
			nodes.get(i + 1).addEdge(tempEdge);
		}//End for
		
		Graph<OSMNode, OSMEdge> edgeGraph = gt.createEdgeGraph(edges);
		System.out.println(edgeGraph);
	}//End graphByWayID

}//End GraphController
