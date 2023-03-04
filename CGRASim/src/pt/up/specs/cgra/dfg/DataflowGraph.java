package pt.up.specs.cgra.dfg;

import java.util.List;
import java.util.Map;

import pt.up.specs.cgra.structure.context.Context;

public interface DataflowGraph {
	
	public Map<Node, List<Node>> getDFG();
	
	public Context makeContext();
}
