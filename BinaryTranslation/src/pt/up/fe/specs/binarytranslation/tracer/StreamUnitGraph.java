/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */
 
package pt.up.fe.specs.binarytranslation.tracer;

import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.GraphType;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.util.SupplierUtil;

public class StreamUnitGraph extends AbstractBaseGraph<StreamUnit, StreamUnitEdge> {

    // resource
    // https://paulosuzart.github.io/blog/2020/08/08/graphs-with-jgrapht/

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

    /*
     * last inserted unit;
     * when inserting a new unit for a TraceInstructionStream
     * I only need to insert edges to the last one
     */
    private StreamUnit tailUnit;

    /*
     * create for interactive insertion
     */
    public StreamUnitGraph() {
        super(null, SupplierUtil.createSupplier(StreamUnitEdge.class), GTYPE);
    }

    /*
     * create from known list
     */
    public StreamUnitGraph(List<StreamUnit> units) {
        this();
        for (var unit : units)
            this.insert(unit);
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
     * Insert a new unit 
    */
    public void insert(StreamUnit newNode) {

        // first
        if (this.tailUnit == null) {
            this.addVertex(newNode);
        }

        // check if edge already exists
        else {
            var edge = this.getEdge(this.tailUnit, newNode);
            if (edge != null) {
                this.setEdgeWeight(edge, edge.getWeight() + 1);
            }

            // add vertex
            else {
                this.addVertex(newNode);
                var n = this.addEdge(this.tailUnit, newNode);
                this.setEdgeWeight(n, 1);
            }
        }

        // new tail
        this.tailUnit = newNode;
    }
}
