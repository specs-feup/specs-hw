package pt.up.fe.specs.binarytranslation.binarysegments.detection;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.Operand;
import pt.up.fe.specs.binarytranslation.instruction.OperandType;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public abstract class AFrequentSequenceDetector implements SegmentDetector {

    /*
     * An open instruction stream, either from elf dump, or simulator
     */
    private InstructionStream istream;

    /*
     * min and max size of windows 
     */
    private final int minsize = 2;
    private final int maxsize = 10;

    /*
     * Since list needs revisiting, absorb all instructions in
     * the static dump into StaticBasicBlockDetector class instance
     */
    protected AFrequentSequenceDetector(InstructionStream istream) {
        this.istream = istream;
    }

    /*
     * Private Helper class to hold a dumbed down instruction
     * so as to consume less memory. Instructions can be rebuilt
     * by reparsing calling the constructor that originated them,
     * after frequent hashed sequences are determined
     */
    private class SimpleInstruction {
        protected String address;
        protected String instruction;
        protected Method c;

        public SimpleInstruction(Instruction i) {
            this.address = i.getAddress().toString();
            this.instruction = i.getInstruction();
            try {
                this.c = i.getClass().getMethod("newInstance", String.class, String.class);
            } catch (Exception e) {
                throw new RuntimeException(e.getCause());
            }
        }

        public Instruction rebuild() {
            Instruction i = null;
            try {
                i = (Instruction) this.c.invoke(null, this.address, this.instruction);
            } catch (Exception e) {
                throw new RuntimeException(e.getCause());
            }
            return i;
        }
    }

    // no point in starting to build hashes if sequence will fail at some point
    private Boolean validSequence(List<Instruction> insts) {

        // check if this subsequence is at all apt
        for (Instruction inst : insts) {

            // TODO fail with stream instructions

            // do not form sequences with unknown instructions
            // do not form frequent sequences containing jumps
            // do not form frequent sequences memory accesses
            if (inst.isUnknown() || inst.isJump() || inst.isMemory()) {
                return false;
            }
        }

        return true;
    }

    /*
     * Builds operand value replacement map for a given sequence (assumed valid)
     */
    private Map<String, String> makeRegReplaceMap(List<Instruction> ilist) {

        Map<OperandType, Character> counter = new HashMap<OperandType, Character>();
        Map<String, String> regremap = new HashMap<String, String>();
        for (Instruction i : ilist) {

            var operands = i.getData().getOperands();
            for (Operand op : operands) {

                // register must not be special (e.g. stack pointer in ARM)
                if (op.isSpecial())
                    continue;

                if (!regremap.containsKey(op.getRepresentation())) {

                    // get current count
                    char c;

                    // OperandType
                    var typeid = op.getProperties().getMainType();

                    // TODO should be an exception here if operand is symbolic
                    // must be non symbolic REGISTER or IMMEDIATE

                    if (!counter.containsKey(typeid))
                        counter.put(typeid, 'a');
                    else {
                        c = counter.get(typeid).charValue();
                        counter.put(typeid, Character.valueOf(++c));
                    }

                    // remap
                    c = counter.get(typeid).charValue();
                    regremap.put(op.getRepresentation(), String.valueOf(c));
                }
            }

            // TODO implement imm remapping strategies here??
        }

        return regremap;
    }

    /*
     * Makes a copy of each instruction, but turns operands symbolic
     */
    private List<Instruction> makeSymbolic(List<Instruction> ilist, Map<String, String> regremap) {

        // Hard copy
        List<Instruction> symbolic = new ArrayList<Instruction>();
        for (Instruction i : ilist) {
            symbolic.add(i.copy());
        }

        // Symbolify
        Integer addr = 0;
        for (Instruction i : symbolic) {
            i.makeSymbolic(addr, regremap);
            addr += 4;
        }

        return symbolic;
    }

}
