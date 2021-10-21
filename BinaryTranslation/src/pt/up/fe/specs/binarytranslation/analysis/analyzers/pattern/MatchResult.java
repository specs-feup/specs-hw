/**
 *  Copyright 2021 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package pt.up.fe.specs.binarytranslation.analysis.analyzers.pattern;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.GraphMapping;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;

public class MatchResult {
    private boolean match = false;
    private List<String> strictRegisters = new ArrayList<>();
    private List<String> strictImms = new ArrayList<>();
    private List<GraphMapping<BtfVertex, DefaultEdge>> matchedGraphs = new ArrayList<>();

    public boolean isMatch() {
        return match;
    }

    public void setMatch(boolean match) {
        this.match = match;
    }

    public List<String> getStrictRegisters() {
        return strictRegisters;
    }

    public void addStrictRegister(String strictRegister) {
        this.strictRegisters.add(strictRegister);
    }

    public List<String> getStrictImms() {
        return strictImms;
    }

    public void addStrictImm(String strictImm) {
        this.strictImms.add(strictImm);
    }

    public String getRegisterSet() {
        return String.join("|", strictRegisters);
    }

    public List<GraphMapping<BtfVertex, DefaultEdge>> getMatchedGraphs() {
        return matchedGraphs;
    }

    public void addMatchedGraphs(GraphMapping<BtfVertex, DefaultEdge> graph) {
        this.matchedGraphs.add(graph);
    }
}