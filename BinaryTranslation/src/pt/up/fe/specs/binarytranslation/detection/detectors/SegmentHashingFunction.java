package pt.up.fe.specs.binarytranslation.detection.detectors;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

/**
 * A functional interface to create a hash value based on a sequence of instructions
 * 
 * @author nuno
 *
 */
public interface SegmentHashingFunction {
    Integer apply(List<Instruction> candidate);
}
