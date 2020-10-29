package pt.up.fe.specs.binarytranslation.detection.detectors;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class SegmentHashingUtils {

    // TODO: use the .and() to combine hashing!

    public static Integer hashWithOperandPositions(List<Instruction> candidate) {

        var bld = new StringBuilder();
        /* for (Instruction inst : candidate) {
        
            // make part 1 of hash string
            bld.append("_").append(Integer.toHexString(inst.getProperties().getOpCode()));
            // TODO this unique id (the opcode) will not be unique for arm, since the
            // specific instruction is resolved later with fields that arent being
            // interpreted yet; how to solve?
            // TODO replace getOpCode with getUniqueID() (as a string)
        
            // make part 2 of hash string
            for (Operand op : inst.getData().getOperands()) {
                bld.append("_").append(regremap.get(op.getRepresentation()));
                // at this point, imms have either been (or not) all (or partially) symbolified
            }
        }*/

        // return hashed sequence
        return bld.toString().hashCode();
    }
}
