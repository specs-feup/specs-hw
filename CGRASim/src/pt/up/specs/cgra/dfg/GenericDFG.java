package pt.up.specs.cgra.dfg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.up.specs.cgra.structure.context.Context;

public class GenericDFG implements DataflowGraph, Iterable {
	
	private Map<Node, List<Node>> dfg;
	private Context context;
	
	void addNode(Math_Op op, int id) {
	    dfg.putIfAbsent(new Node(op, id), new ArrayList<>());
	}
	
	void addEdge(Math_Op op1, int id1, Math_Op op2, int id2)
	{
		dfg.get(op1).add(new Node(op2, id2));
		
	}

	void removeNode(Math_Op op, int id) {
		dfg.remove(op, id);
	    
	}
	
	void removeEdge(Math_Op op1, int id1, Math_Op op2, int id2)
	{
		dfg.get(op1).remove(new Node(op2, id2));
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
