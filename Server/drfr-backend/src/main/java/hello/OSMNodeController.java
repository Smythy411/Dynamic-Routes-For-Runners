package hello;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/*
 * Class used for API Endpoint testing
 */

@RestController
public class OSMNodeController {

	@RequestMapping("/OSMNode")
	public OSMNode node(@RequestParam(value="nodeId", defaultValue="243881410") long nodeID) {
		
		DBManager db = new DBManager("GraphTesting");
		OSMNode node = db.getNodeByNodeId(nodeID);
		db.close();
		return node;
	}//end node()
	
	@RequestMapping("/OSMNodes")
	public ArrayList<OSMNode> nodes(@RequestParam(value="nodeId", defaultValue="243881410") long nodeID) {
		
		DBManager db = new DBManager("GraphTesting");
		ArrayList<OSMNode> nodes = db.getNodes();
		db.close();
		return nodes; 
	}//end nodes()
}//End OSMNodeController()
