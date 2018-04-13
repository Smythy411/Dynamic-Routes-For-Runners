package hello;

import java.util.ArrayList;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 * Class used for API Endpoint testing
 */
@RestController
public class OSMEdgeController {

	@RequestMapping("/OSMEdges")
	public ArrayList<OSMEdge> edges() {
		
		DBManager db = new DBManager("GraphTesting");
		ArrayList<OSMNode> nodes = db.getNodes();
		ArrayList<OSMEdge> edges = db.getEdges(nodes);
		db.close();
		return edges;
	}//end edges()
	
	@RequestMapping("/OSMEdgesByWayID")
	public ArrayList<OSMEdge> edgesByWayID(@RequestParam(value="wayid", defaultValue="4258427") long wayID) {
		DBManager db = new DBManager("GraphTesting");
		ArrayList<OSMEdge> edges = db.getEdgesbyWayId(wayID);
		db.close();
		
		return edges;
	}//end edgeByWayID()
}//End OSMEdgeController()
