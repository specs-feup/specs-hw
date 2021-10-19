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

package pt.up.fe.specs.binarytranslation.analysis.analyzers.pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.jgrapht.GraphMapping;
import org.jgrapht.alg.isomorphism.IsomorphismInspector;
import org.jgrapht.alg.isomorphism.VF2SubgraphIsomorphismInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.ABasicBlockAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public abstract class APatternAnalyzer extends ABasicBlockAnalyzer {
    public APatternAnalyzer(ATraceInstructionStream stream, ZippedELFProvider elf, int window) {
        super(stream, elf, window);
    }

    public APatternReport analyzeSegment() {
        var segs = getBasicBlockSegments();
        var report = matchTemplates(segs);
        return report;
    }

    protected abstract APatternReport matchTemplates(List<BinarySegment> segs);

    protected MatchResult matchGraphToTemplate(SimpleDirectedGraph<BtfVertex, DefaultEdge> graph,
            SimpleDirectedGraph<BtfVertex, DefaultEdge> template) {
        return matchGraphToTemplate(graph, template, false, false);
    }

    protected MatchResult matchGraphToTemplate(SimpleDirectedGraph<BtfVertex, DefaultEdge> graph,
            SimpleDirectedGraph<BtfVertex, DefaultEdge> template, boolean strictRegister, boolean strictImm) {

        Comparator<BtfVertex> comparator = new RelaxedVertexComparator();
        var iso = new VF2SubgraphIsomorphismInspector<BtfVertex, DefaultEdge>(graph, template, comparator,
                new EdgeComparator());
        var newIso = (IsomorphismInspector<BtfVertex, DefaultEdge>) iso;

        if (!iso.isomorphismExists())
            return new MatchResult();

        var fullMatch = new MatchResult();
        if (strictRegister || strictImm) {
            var iter = newIso.getMappings();

            while (iter.hasNext()) {
                var map = iter.next();
                var same = "";
                if (strictImm) {
                    same = checkMatchImmediates(map, graph);
                    if (same.equals("")) {
                        return new MatchResult();
                    }
                    else
                        fullMatch.addStrictImm(same);
                }
                if (strictRegister) {
                    same = checkMatchRegisters(map, graph);
                    if (same.equals(""))
                        return new MatchResult();
                    else
                        fullMatch.addStrictRegister(same);
                }
            }
        }
        fullMatch.setMatch(true);
        return fullMatch;
    }

    private String checkMatchImmediates(GraphMapping<BtfVertex, DefaultEdge> map,
            SimpleDirectedGraph<BtfVertex, DefaultEdge> graph) {
        var imms = new ArrayList<Long>();

        for (var v : graph.vertexSet()) {
            var match = map.getVertexCorrespondence(v, true);
            if (match != null) {
                if (v.getType() == BtfVertexType.IMMEDIATE) {
                    imms.add(Long.valueOf(v.getLabel()));
                }
            }

        }
        // System.out.println("imm[0] = " + imms.get(0));
        var same = Arrays.stream(imms.toArray()).allMatch(s -> s.equals(imms.get(0)));
        return same ? "" + imms.get(0) : "";
    }

    private String checkMatchRegisters(GraphMapping<BtfVertex, DefaultEdge> map,
            SimpleDirectedGraph<BtfVertex, DefaultEdge> graph) {
        var regs = new ArrayList<String>();

        for (var v : graph.vertexSet()) {
            var match = map.getVertexCorrespondence(v, true);
            if (match != null) {
                if (v.getType() == BtfVertexType.REGISTER) {
                    regs.add(v.getLabel());
                }
            }
        }
        // System.out.println("reg[0] = " + regs.get(0));
        var same = Arrays.stream(regs.toArray()).allMatch(s -> s.equals(regs.get(0)));
        return same ? "" + regs.get(0) : "";
    }

    protected class RelaxedVertexComparator implements Comparator<BtfVertex> {
        @Override
        public int compare(BtfVertex o1, BtfVertex o2) {
            var type1 = o1.getType();
            var type2 = o2.getType();
            var label1 = o1.getLabel();
            var label2 = o2.getLabel();

            if (type1 == BtfVertexType.REGISTER && type2 == BtfVertexType.REGISTER)
                return 0;
            if (type1 == BtfVertexType.OPERATION && type2 == BtfVertexType.OPERATION) {
                if (label1.equals(label2))
                    return 0;
                else if ((label1.equals("+") || label1.equals("-")) && (label2.equals("+") || label2.equals("-")))
                    return 0;
                else
                    return -1;
            }
            if (type1 == BtfVertexType.IMMEDIATE && type2 == BtfVertexType.IMMEDIATE) {
                return 0;
            }
            if (type1 == BtfVertexType.MEMORY && type2 == BtfVertexType.MEMORY)
                return 0;
            return -1;
        }
    }

    protected class StrictVertexComparator implements Comparator<BtfVertex> {
        @Override
        public int compare(BtfVertex o1, BtfVertex o2) {
            var type1 = o1.getType();
            var type2 = o2.getType();
            var label1 = o1.getLabel();
            var label2 = o2.getLabel();

            if (type1 == BtfVertexType.REGISTER && type2 == BtfVertexType.REGISTER) {
                if (label1.equals(label2))
                    return 0;
                else
                    return -1;
            }
            if (type1 == BtfVertexType.OPERATION && type2 == BtfVertexType.OPERATION) {
                if (label1.equals(label2))
                    return 0;
                else if ((label1.equals("+") || label1.equals("-")) && (label2.equals("+") || label2.equals("-")))
                    return 0;
                else
                    return -1;
            }
            if (type1 == BtfVertexType.IMMEDIATE && type2 == BtfVertexType.IMMEDIATE) {
                return 0;
            }
            if (type1 == BtfVertexType.MEMORY && type2 == BtfVertexType.MEMORY)
                return 0;
            return -1;
        }
    }

    protected class EdgeComparator implements Comparator<DefaultEdge> {
        @Override
        public int compare(DefaultEdge e1, DefaultEdge e2) {
            return 0;
        }
    }
}
