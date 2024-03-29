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
 
package pt.up.fe.specs.binarytranslation.analysis.analyzers.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.ocurrence.BasicBlockOccurrenceTracker;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.InstructionType;
import pt.up.fe.specs.binarytranslation.instruction.register.RegisterDump;

public class InductionVariablesDetector {

    private List<Instruction> basicBlock;

    public InductionVariablesDetector(List<Instruction> bb) {
        this.basicBlock = bb;
    }
    
    public InductionVariablesDetector(BinarySegment bb) {
        this.basicBlock = bb.getInstructions();
    }


    public HashMap<String, Long> registerDiff(RegisterDump r1, RegisterDump r2) {
        var diff = new HashMap<String, Long>();
//        for (var k : r1.getRegisterMap().keySet()) {
//            long i1 = r1.getRegisterMap().get(k);
//            long i2 = r2.getRegisterMap().get(k);
//            long id = i2 - i1;
//            diff.put(k, id);
//        }
        return diff;
    }

//    public void printDifferences() {
//        var keys = getTracker().getOccurrences().get(0).getRegisters().getRegisterMap().keySet();
//        var diffs = new ArrayList<HashMap<String, Long>>();
//        for (int i = 0; i < getTracker().getOccurrences().size() - 1; i++) {
//            var b1 = getTracker().getOccurrences().get(i).getRegisters();
//            var b2 = getTracker().getOccurrences().get(i + 1).getRegisters();
//            var bd = registerDiff(b1, b2);
//            diffs.add(bd);
//        }
//
//        var sb = new StringBuilder();
//        for (var k : keys) {
//            boolean isZero = true;
//            for (var diff : diffs) {
//                if (diff.get(k) != 0)
//                    isZero = false;
//            }
//            if (!isZero) {
//                sb.append(k).append(": ");
//                for (var diff : diffs) {
//                    sb.append(diff.get(k)).append("| ");
//                }
//                sb.append("\n");
//            }
//
//        }
//        System.out.println(sb.toString());
//    }

//    public void printInstDifferences() {
//        var rows = getTracker().getBasicBlock().getInstructions().size();
//        var cols = getTracker().getOccurrences().size();
//        String[][] regs = new String[rows][cols];
//        for (int col = 0; col < getTracker().getOccurrences().size(); col++) {
//            var bb = getTracker().getOccurrences().get(col);
//            for (int row = 0; row < bb.getInsts().size(); row++) {
//                var inst = bb.getInsts().get(row);
//                String regInfo = getRelevantRegs(inst);
//                regs[row][col] = regInfo;
//            }
//        }
//
//        var sb = new StringBuilder();
//        for (int i = 0; i < regs.length; i++) {
//            for (int j = 0; j < regs[i].length; j++) {
//                sb.append(regs[i][j]).append("| ");
//            }
//            sb.append("\n");
//        }
//        System.out.println(sb.toString());
//    }

    private String getRelevantRegs(Instruction inst) {
        StringBuilder sb = new StringBuilder();
        for (var op : inst.getData().getOperands()) {
            if (op.isRegister() && op.isRead()) {
                String regName = AnalysisUtils.getRegisterName(op);
                long val = 0;//inst.getRegisters().getValue(regName);
                sb.append(regName).append("{").append(val).append("} ");
            }
        }
        return sb.toString();
    }

    public HashMap<String, Integer> detectVariables(ArrayList<Graph<BtfVertex, DefaultEdge>> graphs,
            boolean verbose) {
        var res = new HashMap<String, Integer>();
        var regs = AnalysisUtils.findAllRegistersOfSeq(basicBlock);
        String[][] fullRes = new String[regs.size()][4];

        for (int i = 0; i < regs.size(); i++) {
            String reg = regs.get(i);
            var regIncrements = findIncrements();

            boolean c1 = conditionPartOfAddress(reg, graphs);
            boolean c2 = conditionHasIncrement(reg, regIncrements);
            boolean c3 = conditionIsInComparison(reg);
            if (c1 && c2 && c3)
                res.put(reg, regIncrements.get(reg));

            if (verbose) {
                fullRes[i][0] = reg;
                fullRes[i][1] = Boolean.toString(c1);
                fullRes[i][2] = Boolean.toString(c2);
                fullRes[i][3] = Boolean.toString(c3);
            }
        }
        if (verbose)
            printFullResult(fullRes);
        return res;
    }

    private void printFullResult(String[][] fullRes) {
        for (int i = 0; i < fullRes.length; i++) {
            var sb = new StringBuilder(fullRes[i][0] + ": ");
            sb.append(fullRes[i][1] + ", ");
            sb.append(fullRes[i][2] + ", ");
            sb.append(fullRes[i][3]);
            System.out.println(sb.toString());
        }
    }

    private boolean conditionPartOfAddress(String reg, ArrayList<Graph<BtfVertex, DefaultEdge>> graphs) {
        for (var graph : graphs) {
            for (var vertex : graph.vertexSet()) {
                if (vertex.getType() == BtfVertexType.REGISTER) {
                    if (vertex.getLabel().equals(reg))
                        return true;
                }
            }
        }
        return false;
    }

    private boolean conditionHasIncrement(String reg, HashMap<String, Integer> map) {
        return map.containsKey(reg);
    }

    private HashMap<String, Integer> findIncrements() {
        var map = new HashMap<String, Integer>();

        for (var inst : basicBlock) {
            if (inst.isAdd() || inst.isSub() || inst.isMul()) {
                if (inst.getData().getOperands().size() == 3) {

                    var op0 = inst.getData().getOperands().get(0);
                    var op1 = inst.getData().getOperands().get(1);
                    var op3 = inst.getData().getOperands().get(2);

                    if (op0.isRegister() && op1.isRegister() && op3.isImmediate()) {
                        var reg0 = AnalysisUtils.getRegisterName(op0);
                        var reg1 = AnalysisUtils.getRegisterName(op1);
                        if (reg0.equals(reg1)) {
                            int inc = Integer.decode(op3.getRepresentation());
                            map.put(reg0, inc);
                        }
                    }
                }
            }
        }
        return map;
    }

    private boolean conditionIsInComparison(String reg) {
        for (var inst : basicBlock) {
            if (inst.getData().getProperties().getGenericTypes().contains(InstructionType.G_CMP)) {
                for (var op : inst.getData().getOperands()) {
                    if (op.isRegister()) {
                        var currReg = AnalysisUtils.getRegisterName(op);
                        if (currReg.equals(reg))
                            return true;
                    }
                }
            }
        }
        return false;
    }
}
