package pt.up.specs.cgra.dfg;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Node implements Iterable<Node> {

	Math_Op op;
	int id;

	private Set<Node> leaves;

	  public Node(Math_Op op, int id) {
		  this.op = op;
		  this.id = id;
		  leaves = new HashSet<Node>();
	  }

	  public boolean addLeaf(Node x) {
		  return leaves.add(x);
	  }

	  public boolean deleteLeaf(Node x) {
		  return leaves.remove(x);
	  }

	  public Iterator<Node> iterator() {
	    return leaves.iterator();
	  }
	}
