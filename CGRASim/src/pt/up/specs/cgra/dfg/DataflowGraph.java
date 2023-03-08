package pt.up.specs.cgra.dfg;

import java.util.List;
import java.util.Map;

import pt.up.specs.cgra.structure.context.Context;

public interface DataflowGraph {
	
	public Map<Node, List<Node>> getDFG();
	
	public Context makeContext();
	
	public void addNewNode(Math_Op op, int id);
	
	public void addNode(Node x);
	
	public void addEdge(Node x, Node y);
	
	public void removeNode(Node x);
	
	public void removeEdge(Node x, Node y);
	
	public List<Node> getEdges(Node x);


	
	
}
