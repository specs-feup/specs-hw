package pt.up.fe.specs.binarytranslation.binarysegments.detection;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public class TraceBasicBlockDetector extends ABasicBlockDetector {

    public TraceBasicBlockDetector(InstructionStream istream) {
        super(istream);
    }

    private enum DetectState {
        IDLING,
        WAITING,
        RECORDING,
        CLEARING
    }

    @Override
    public List<BinarySegment> detectSegments() {

        DetectState state = DetectState.IDLING;
        int delay = 0;
        Instruction branchref = null;
        List<Instruction> candidate = null;
        Instruction inst = null;
        while ((inst = istream.nextInstruction()) != null) {

            switch (state) {

            // wait for a backwards branch
            case IDLING:
                if (inst.isBackwardsJump() && inst.isConditionalJump() && inst.isRelativeJump()) {
                    branchref = inst;
                    delay = inst.getDelay();
                    candidate = new ArrayList<Instruction>();
                    if (delay > 0)
                        state = DetectState.WAITING;
                    else
                        state = DetectState.RECORDING;
                }
                break;

            // allow for the delay slot to clear
            case WAITING:
                delay--;
                if (delay == 0)
                    state = DetectState.RECORDING;
                break;

            // record basic block
            case RECORDING:
                candidate.add(inst);
                if (inst == branchref) {
                    delay = inst.getDelay();
                }
                if(delay > 0) {
                    delay--;
                    if(delay == 0)
                        state = 
                }
                    
                break;
            }

            /*
            // starting building the candidate after the
            // backwards jump, but allow for the delay slot to clear
            if (buildingCandidate == true && delay == 0) {
            
                candidate.add(inst);
                if (inst == branchref) {
            
                }
            
                // buildingCandidate = false;
            }
            
            // if backwards branch, start constructing the candidate on the next instruction
            else if (inst.isBackwardsJump() && inst.isConditionalJump() && inst.isRelativeJump()) {
                branchref = inst;
                delay = inst.getDelay();
                candidate = new ArrayList<Instruction>();
                buildingCandidate = true;
            }
            */
        }

        return null;
    }

    @Override
    public float getCoverage(int segmentSize) {
        // TODO Auto-generated method stub
        return 0;
    }
}
