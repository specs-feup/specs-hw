package pt.up.specs.cgra.dfg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.up.specs.cgra.structure.context.Context;

public class GenericDFG implements DataflowGraph, Iterable<Node> {
	
	private Map<Node, List<Node>> dfg;
	private Context context;
	
	public void addNewNode(Math_Op op, int id) {
	    dfg.putIfAbsent(new Node(op, id), new ArrayList<>());
	}
	

	public void addNode(Node x) {
	    dfg.putIfAbsent(x, new ArrayList<>());
	}
	
	public void addEdge(Node x, Node y)
	{
		dfg.get(x).add(y);
	}

	public void removeNode(Node x) {
		dfg.remove(x);
	}
	
	public void removeEdge(Node x, Node y) {
		dfg.get(x).remove(y);
	}
	
	public List<Node> getEdges(Node x)
	{
		return dfg.get(x);
		
	}
	
	@Override
	public Map<Node, List<Node>> getDFG() {
		return dfg;
	}
	
	

	@Override
	public Context makeContext() {
		return context;
	}

	@Override
	public Iterator<Node> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
