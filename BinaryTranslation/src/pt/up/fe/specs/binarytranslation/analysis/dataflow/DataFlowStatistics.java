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

package pt.up.fe.specs.binarytranslation.analysis.dataflow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex;
import pt.up.fe.specs.binarytranslation.analysis.memory.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexType;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class DataFlowStatistics {
    private Graph<AddressVertex, DefaultEdge> graph;
    private Graph<AddressVertex, DefaultEdge> path;
    private List<Instruction> insts;
    private List<String> sources;
    private List<String> sinks;
    private double ilp;
    private int pathSize = 0;

    public DataFlowStatistics(Graph<AddressVertex, DefaultEdge> graph, Graph<AddressVertex, DefaultEdge> path,
            List<Instruction> insts, List<String> sources, List<String> sinks) {
        this.graph = graph;
        this.path = path;
        this.insts = insts;
        this.sources = sources;
        this.sinks = sinks;
        
        for (var v : path.vertexSet()) {
            if (v.getType() == AddressVertexType.OPERATION || v.getType() == AddressVertexType.MEMORY)
                this.pathSize++;
        }
        
        double cnt = 0;
        for (var v : graph.vertexSet()) {
            if (v.getType() == AddressVertexType.OPERATION || v.getType() == AddressVertexType.MEMORY)
                cnt++;
        }
        this.ilp = cnt / (double) pathSize;
        
        for (var v : path.vertexSet())
            v.setColor("red");
    }


    @Override
    public String toString() {
        var label = new StringBuilder()
                .append("Critical path size: ")
                .append(pathSize)
                .append("\\l")
                .append("Number of inst.: ")
                .append(insts.size())
                .append("\\l")
                .append("Sources: ")
                .append(sources.toString())
                .append("\\l")
                .append("Sinks: ")
                .append(sinks.toString())
                .append("\\l")
                .append("Op counts:")
                .append("\\l");
        
        var cnt = getOpCount();
        for (var key : cnt.keySet()) {
            if (!key.equals("Load") && !key.equals("Store"))
            label.append(key + " : " + cnt.get(key) + "\\l");
        }
        if (cnt.containsKey("Load"))
            label.append("Load" + " : " + cnt.get("Load") + "\\l");
        if (cnt.containsKey("Store"))
            label.append("Store" + " : " + cnt.get("Store") + "\\l");
        
        var dfg = GraphUtils.graphToDot(graph);
        dfg = dfg.replace("}", "");
        dfg += "nstat[label=\"" + label.toString() + "\",shape=rect,labeljust=l,nojustify=true]\n}";

        return GraphUtils.generateGraphURL(dfg);
    }
    
    private Map<String, Integer> getOpCount() {
        var map = new HashMap<String, Integer>();
        for (var v : graph.vertexSet()) {
            if (v.getType() == AddressVertexType.OPERATION || v.getType() == AddressVertexType.MEMORY) {
                var op = labelToFullName(v.getLabel());
                if (map.containsKey(op))
                    map.put(op, map.get(op) + 1);
                else
                    map.put(op, 1);
            }
        }
        return map;
    }

    private String labelToFullName(String label) {
        switch (label) {
        case "+":
            return "Add";
        case "-":
            return "Sub";
        case "*":
            return "Mult";
        case "/":
            return "Div";
        case "==":
            return "Cmp";
        default:
            return label;
        }
    }

    public Graph<AddressVertex, DefaultEdge> getGraph() {
        return graph;
    }

    public Graph<AddressVertex, DefaultEdge> getPath() {
        return path;
    }

    public List<Instruction> getInsts() {
        return insts;
    }
    
    public List<String> getSources() {
        return sources;
    }

    public List<String> getSinks() {
        return sinks;
    }


    public int getPathSize() {
        return pathSize;
    }

    public double getILP() {
        return ilp;
    }
}