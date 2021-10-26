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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.templates.GraphTemplateType;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegmentType;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class ExpressionIncrementMatcher extends APatternAnalyzer {
    private List<String> bbIDs = new ArrayList<>();
    private List<String> memIDs = new ArrayList<>();
    private List<String> memTypes = new ArrayList<>();
    private List<String> streaming = new ArrayList<>();
    private List<String> inductionVars = new ArrayList<>();
    private List<String> increments = new ArrayList<>();
    private List<String> baseAddrs = new ArrayList<>();

    public ExpressionIncrementMatcher(ATraceInstructionStream stream, ZippedELFProvider elf, int window, BinarySegmentType type) {
        super(stream, elf, window, type);
    }

    @Override
    protected APatternReport matchTemplates(List<BinarySegment> segs) {
        var memAn = new MemoryAccessTypesAnalyzer(stream, elf, window, getSegmentType());
        var memRep = (MemoryPatternReport) memAn.matchWithPrecalculatedBlocks(segs);

        var incAn = new LoopIncrementPatternAnalyzer(stream, elf, window, getSegmentType());
        var incRep = (LoopIncrementReport) incAn.matchWithPrecalculatedBlocks(segs);

        var name = elf.getELFName();
        memRep.setName(name);
        incRep.setName(name);

        matchReports(memRep, incRep);
        var rep = new ExpressionIncrementMatchReport(bbIDs, memIDs, memTypes, streaming, inductionVars, increments, baseAddrs);
        rep.setName(memRep.getName());
        
        return rep;
    }

    private void matchReports(MemoryPatternReport memRep, LoopIncrementReport incRep) {
        for (int i = 0; i < incRep.getBasicBlockIDs().size(); i++) {
            var bbid = incRep.getBasicBlockIDs().get(i);
            var regs = incRep.getRegisters().get(i);
            
            for (var j = 0; j < memRep.getBasicBlockIDs().size(); j++) {
                
                if (memRep.getBasicBlockIDs().get(j).equals(bbid)) {
                    bbIDs.add(bbid); 
                    var exprGraph = memRep.getGraphs().get(j);
                    var memID = memRep.getMemIDs().get(j);
                    memIDs.add(memID);
                    
                    // Mem Expr type
                    var memType = memRep.getTypes().get(j);
                    memTypes.add(memType.toString());
                    
                    // Streaming?
                    boolean streaming = checkStreaming(memType);
                    this.streaming.add(streaming ? "yes" : "no");
                    
                    // Induction var
                    var indVarReg = matchRegistersToExpression(exprGraph, regs);
                    inductionVars.add(indVarReg);

                    // Increment
                    int inc = getIncrement(exprGraph, indVarReg);
                    increments.add("" + inc);
                    
                    // BaseAddr
                    var bases = getBases(exprGraph, indVarReg);
                    baseAddrs.add(String.join("|", bases));
                }
            }
        }
    }

    private List<String> getBases(Graph<BtfVertex, DefaultEdge> exprGraph, String indVarReg) {
        // TODO Auto-generated method stub
        return new ArrayList<>();
    }

    private int getIncrement(Graph<BtfVertex, DefaultEdge> exprGraph, String indVarReg) {
        // TODO Auto-generated method stub
        return 0;
    }

    private boolean checkStreaming(GraphTemplateType memType) {
        return true;
    }

    private String matchRegistersToExpression(Graph<BtfVertex, DefaultEdge> exprGraph, String regs) {
        var regList = regs.split("\\|");
        var matchList = new ArrayList<String>();
        
        for (var reg : regList) {
            var found = false;
            
            for (var v : exprGraph.vertexSet()) {

                if (v.getType() == BtfVertexType.REGISTER) {
                    if (v.getLabel().equals(reg)) {
                        found = true;

                    }
                }
            }
            if (found)
                matchList.add(reg);
        }
        return String.join("|", matchList);
    }
}
