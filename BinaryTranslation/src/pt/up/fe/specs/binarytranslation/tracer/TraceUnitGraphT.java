package pt.up.fe.specs.binarytranslation.tracer;

import org.jgrapht.GraphType;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.util.SupplierUtil;

public class TraceUnitGraphT extends AbstractBaseGraph<TraceUnit, DefaultEdge> {

    /**
     * 
     */
    private static final long serialVersionUID = -7408990248590065377L;
    private static GraphType GTYPE = GraphTypeBuilder
            .<TraceUnit, DefaultEdge> directed()
            .allowingMultipleEdges(true)
            .allowingSelfLoops(true)
            .edgeClass(DefaultEdge.class).buildType();

    public TraceUnitGraphT() {
        super(null, SupplierUtil.createSupplier(DefaultEdge.class), GTYPE);
    }

    /*
     * Insert a new unit 
     */
    public void insert(TraceUnit newNode) {

        this.addVertex(newNode);
        var allUnits = this.vertexSet();

        for (var unit : allUnits) {
            // insert newNode as child of candidate parent, if parent jumps to or precedes newNode
            if (unit.jumpsTo(newNode) || unit.precedes(newNode) || newNode.includesTarget(unit)) {
                this.addEdge(unit, newNode);
            }

            // backwards jumps?
            if ((newNode != unit) && (newNode.jumpsTo(unit) || unit.includesTarget(newNode))) {
                this.addEdge(newNode, unit);
            }
        }
    }
}
