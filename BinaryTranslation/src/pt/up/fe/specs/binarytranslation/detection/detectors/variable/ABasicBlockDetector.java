package pt.up.fe.specs.binarytranslation.detection.detectors.variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.detection.detectors.ASegmentDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.detectors.HashedSequence;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

/**
 * 
 * @author Nuno
 *
 */
public abstract class ABasicBlockDetector extends ASegmentDetector {

    /*
     * Constructor
     */
    public ABasicBlockDetector(DetectorConfiguration config) {
        super(config);
    }

    /*
     * Basic block can only have one branch back, and zero branches forward
     */
    protected boolean validSequence(List<Instruction> candidate) {

        int numbranches = 0;
        for (Instruction i : candidate) {

            if (i.isJump())
                numbranches++;

            if (i.isForwardsJump())
                return false;
        }

        return (numbranches > 1) ? false : true;
    }

    /*
     * For a window of this.maxsize, check for valid candidate, and return it
     */
    private List<Instruction> checkCandidate(List<Instruction> window, Instruction backbranch) {

        // try to find start of basic block in window, and its index
        int sidx = window.size() - 1;
        Instruction target = null;
        int startAddr = backbranch.getData().getBranchTarget().intValue();

        try {
            while (sidx >= 0 && target == null) {
                var is = window.get(sidx);
                if (is.getAddress().intValue() == startAddr)
                    target = is;
                sidx--;
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        sidx += 1;

        // wasn't there
        if (target == null)
            return null;

        // find index of branch instruction
        int eidx = 0;
        while (window.get(eidx) != backbranch) {
            eidx++;
        }
        eidx += 1;

        // sub-window
        List<Instruction> candidate = window.subList(sidx, eidx + backbranch.getDelay());

        // discard candidate?
        if (!validSequence(candidate))
            return null;

        // valid basic block
        return candidate;
    }

    /*
     * 
     */
    @Override
    public void processStream(InstructionStream istream, Map<String, HashedSequence> hashed,
            Map<Integer, List<Integer>> addrs) {

        List<Instruction> window = new ArrayList<Instruction>();

        // process entire stream
        int insertcount = 0;
        Instruction is = null;
        while ((is = istream.nextInstruction()) != null) {

            window.add(is);
            insertcount++;

            // "is" is a possible backwards branch that could generate a basic block, so, try to hash it
            if (is.isBackwardsJump() && is.isConditionalJump() && is.isRelativeJump()) {

                // absorb as many instructions as there are in the delay slot
                var delay = is.getDelay();
                while (delay-- > 0) {
                    window.add(istream.nextInstruction());
                    insertcount++;
                }

                // try to hash the possible candidate terminated by backwards branch "is"
                List<Instruction> candidate = null;
                if ((candidate = checkCandidate(window, is)) != null) {

                    // create new candidate hash sequence
                    var newseq = super.hashSequence(candidate);

                    // add sequence to occurrence counters
                    // (counting varies between static to trace detection)
                    super.addAddrToList(addrs, newseq);

                    // add sequence to map which is indexed by hashCode + startaddr
                    super.addHashSequenceToList(hashed, newseq);
                }
            }

            // pop one
            while (insertcount > 0 && window.size() > this.getConfig().getMaxsize()) {
                insertcount--;
                window.remove(0);
            }
        }
    }
}
