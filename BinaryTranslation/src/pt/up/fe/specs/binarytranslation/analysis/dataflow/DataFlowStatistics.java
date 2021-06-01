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

import java.util.List;

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
    private int pathSize = 0;

    public DataFlowStatistics(Graph<AddressVertex, DefaultEdge> graph, Graph<AddressVertex, DefaultEdge> path,
            List<Instruction> insts, List<String> sources, List<String> sinks) {
        this.setGraph(graph);
        this.setPath(path);
        this.setInsts(insts);
        this.setSources(sources);
        this.setSinks(sinks);
        for (var v : path.vertexSet()) {
            if (v.getType() == AddressVertexType.OPERATION)
                setPathSize(getPathSize() + 1);
        }
    }

    @Override
    public String toString() {
        var sb = new StringBuilder("DataFlow Statistics\n")
                .append("----------------------\n")
                .append(AnalysisUtils.padRight("DFG: ", 20))
                .append(GraphUtils.generateGraphURL(graph))
                .append("\n")
                .append(AnalysisUtils.padRight("Critical path: ", 20))
                .append(GraphUtils.generateGraphURL(path))
                .append("\n")
                .append(AnalysisUtils.padRight("Critical path size: ", 20))
                .append(pathSize)
                .append("\n")
                .append(AnalysisUtils.padRight("Number of inst.: ", 20))
                .append(insts.size())
                .append("\n")
                .append(AnalysisUtils.padRight("Sources: ", 20))
                .append(sources.toString())
                .append("\n")
                .append(AnalysisUtils.padRight("Sinks: ", 20))
                .append(sinks.toString())
                .append("\n");
        return sb.toString();
    }

    public Graph<AddressVertex, DefaultEdge> getGraph() {
        return graph;
    }

    public void setGraph(Graph<AddressVertex, DefaultEdge> graph) {
        this.graph = graph;
    }

    public Graph<AddressVertex, DefaultEdge> getPath() {
        return path;
    }

    public void setPath(Graph<AddressVertex, DefaultEdge> path) {
        this.path = path;
    }

    public List<Instruction> getInsts() {
        return insts;
    }

    public void setInsts(List<Instruction> insts) {
        this.insts = insts;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public List<String> getSinks() {
        return sinks;
    }

    public void setSinks(List<String> sinks) {
        this.sinks = sinks;
    }

    public int getPathSize() {
        return pathSize;
    }

    public void setPathSize(int pathSize) {
        this.pathSize = pathSize;
    }
}