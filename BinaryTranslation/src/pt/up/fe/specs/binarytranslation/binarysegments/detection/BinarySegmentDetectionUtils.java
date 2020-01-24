package pt.up.fe.specs.binarytranslation.binarysegments.detection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.Operand;
import pt.up.fe.specs.binarytranslation.instruction.OperandType;

public class BinarySegmentDetectionUtils {

    /*
     * 
     */
    protected static HashedSequence hashSequence(List<Instruction> candidate) {

        // make register replacement map (for hash building)
        Map<String, String> regremap = BinarySegmentDetectionUtils.makeRegReplaceMap(candidate);

        String hashstring = "";
        for (Instruction inst : candidate) {

            // make part 1 of hash string
            hashstring += "_" + Integer.toHexString(inst.getProperties().getOpCode());
            // TODO this unique id (the opcode) will not be unique for arm, since the
            // specific instruction is resolved later with fields that arent being
            // interpreted yet; how to solve?
            // TODO replace getOpCode with getUniqueID() (as a string)

            // make part 2 of hash string
            for (Operand op : inst.getData().getOperands()) {
                hashstring += "_" + regremap.get(op.getRepresentation());
                // at this point, imms have either been (or not) all (or partially) symbolified
            }
        }

        // return hashed sequence
        Integer hashCode = hashstring.hashCode();
        return new HashedSequence(hashCode, candidate, regremap);
    }

    /*
     * Builds operand value replacement map for a given sequence (assumed valid)
     */
    protected static Map<String, String> makeRegReplaceMap(List<Instruction> ilist) {

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
                    regremap.put(op.getRepresentation(), op.getPossibleSymbolicRepresentation(String.valueOf(c)));
                }
            }

            // TODO implement imm remapping strategies here??
        }

        return regremap;
    }
}
