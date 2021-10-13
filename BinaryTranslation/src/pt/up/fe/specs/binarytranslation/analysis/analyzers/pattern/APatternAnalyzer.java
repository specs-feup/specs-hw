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

import java.util.Comparator;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.alg.isomorphism.VF2GraphIsomorphismInspector;
import org.jgrapht.alg.isomorphism.VF2SubgraphIsomorphismInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.ABasicBlockAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.GraphUtils;
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

    protected boolean matchGraphToTemplate(SimpleDirectedGraph<BtfVertex, DefaultEdge> graph,
            SimpleDirectedGraph<BtfVertex, DefaultEdge> template) {
        return matchGraphToTemplate(graph, template, false);
    }

    protected boolean matchGraphToTemplate(SimpleDirectedGraph<BtfVertex, DefaultEdge> graph,
            SimpleDirectedGraph<BtfVertex, DefaultEdge> template, boolean strictRegister) {

        Comparator<BtfVertex> comparator = strictRegister ? new StrictVertexComparator()
                : new RelaxedVertexComparator();
        //var iso = new VF2GraphIsomorphismInspector<BtfVertex, DefaultEdge>(graph, template, comparator, new EdgeComparator());
        var iso = new VF2SubgraphIsomorphismInspector<BtfVertex, DefaultEdge>(graph, template, comparator, new EdgeComparator());
        return iso.isomorphismExists();
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
