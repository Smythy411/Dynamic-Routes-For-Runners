package hello;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.GraphPath;

public class Route {
	
	private ArrayList<OSMEdge> edges;
	private double weight;
	
	public Route() {
		this.edges = new ArrayList<OSMEdge>();
	}//end Route Constructor	
	
	public void setWeight(double w) {
		this.weight = w;
	}//end setWeight()
	
	public double getWeight() {
		return this.weight;
	}//end getWeight()
	
	public ArrayList<OSMEdge> getRoute() {
		return this.edges;
	}//end getRoute()
	
	public void addWeightlessToRoute(OSMEdge edge) {
		this.edges.add(edge);
	}//end addWeightlessToRoute()
	
	public void addToRoute(GraphPath<OSMNode, OSMEdge> path) {
		List<OSMEdge> list = path.getEdgeList();
		
		double weightToAdd = path.getWeight();
		this.weight += weightToAdd;
		
		this.edges.addAll(list);
	}//end addToRoute(GraphPath)
	
	public void addToRoute(ArrayList<OSMEdge> path) {
		double weightToAdd = 0.0;
		for (int i = 0; i < path.size(); i++) {
			weightToAdd += path.get(i).getDistance();
		}//end for
		this.weight += weightToAdd;
		
		this.edges.addAll(path);
	}//end addToRoute(ArrayList)
	
	public void addToRoute(List<OSMEdge> path) {
		double weightToAdd = 0.0;
		for (int i = 0; i < path.size(); i++) {
			weightToAdd += path.get(i).getDistance();
		}//end for
		this.weight += weightToAdd;
		
		this.edges.addAll(path);
	}//end addToRoute(List)
	
	public void removeFromRoute(ArrayList<OSMEdge> edgesToRemove) {
		double weightToRemove = 0.0;
		for (int i = 0; i < edgesToRemove.size(); i++) {
			weightToRemove += edgesToRemove.get(i).getDistance();
		}
		this.weight = this.weight - weightToRemove;
		this.edges.removeAll(edgesToRemove);
	}//end removeFromRoute()
}//end Route()
