package pt.up.fe.specs.binarytranslation.tracer;

import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jgrapht.GraphType;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.util.SupplierUtil;

public class StreamUnitGraph extends AbstractBaseGraph<StreamUnit, StreamUnitEdge> {

    /**
     * NOTE: remember that vertex equality is checked by the override hashCode and equals functions in each @StreamUnit
     * child
     */
    private static final long serialVersionUID = -7408990248590065377L;
    private static GraphType GTYPE = GraphTypeBuilder
            .<StreamUnit, StreamUnitEdge> directed()
            .allowingMultipleEdges(true)
            .weighted(true)
            .allowingSelfLoops(true)
            .edgeClass(StreamUnitEdge.class).buildType();

    public StreamUnitGraph() {
        super(null, SupplierUtil.createSupplier(StreamUnitEdge.class), GTYPE);
    }

    // TODO: add toDotty and toPNG to output interface class??
    /*
     * 
     */
    public String toDotty() {

        var exporter = new DOTExporter<StreamUnit, StreamUnitEdge>();
        exporter.setVertexAttributeProvider(v -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            map.put("label", DefaultAttribute.createAttribute(v.toString().replace("\n", "\\l")));
            map.put("shape", DefaultAttribute.createAttribute("box"));
            return map;
        });

        exporter.setEdgeAttributeProvider(e -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            map.put("label", DefaultAttribute.createAttribute(e.toString()));
            return map;
        });

        var writer = new StringWriter();
        exporter.exportGraph(this, writer);
        return writer.toString();
    }

    /*
     * 
     */
    private void weightUpdate(StreamUnit newNode) {

        this.addVertex(newNode);
        var allUnits = this.vertexSet();

        // if edge already exists, increment weigh
        for (var unit : allUnits) {

            // a -> b
            if (this.containsEdge(newNode, unit)) {
                var e1 = this.getEdge(newNode, unit);
                this.setEdgeWeight(e1, e1.getWeight() + 1);
            }

            // b -> a
            if (this.containsEdge(unit, newNode)) {
                var e1 = this.getEdge(unit, newNode);
                this.setEdgeWeight(e1, e1.getWeight() + 1);
            }
        }
    }

    /*
     * 
     */
    private void realInsert(StreamUnit newNode) {

        this.addVertex(newNode);
        var allUnits = this.vertexSet();

        for (var unit : allUnits) {

            // insert newNode as child of candidate parent, if parent jumps to or precedes newNode
            if (unit.jumpsTo(newNode) || unit.precedes(newNode) || newNode.includesTarget(unit)) {
                var n = this.addEdge(unit, newNode);
                this.setEdgeWeight(n, 1);
            }

            // backwards jumps?
            if ((newNode != unit) && (newNode.jumpsTo(unit) || unit.includesTarget(newNode))) {
                var n = this.addEdge(newNode, unit);
                this.setEdgeWeight(n, 1);
            }
        }
    }

    /*
     * Insert a new unit 
     */
    public void insert(StreamUnit newNode) {

        var allUnits = this.vertexSet();

        /**
         * first check if equal @StreamUnit exists if so, do not insert, and instead only check edge weights
         */
        if (allUnits.contains(newNode)) {
            weightUpdate(newNode);
        }

        else {
            realInsert(newNode);
        }
    }
}
