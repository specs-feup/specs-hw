package pt.up.fe.specs.binarytranslation.detection.detectors.fixed;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.detection.detectors.ASegmentDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.BinarySegmentDetectionUtils;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.detectors.HashedSequence;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.detection.segments.MegaBlock;
import pt.up.fe.specs.binarytranslation.detection.segments.SegmentContext;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

/**
 * 
 * @author nuno
 *
 */
public class FixedSizeMegablockDetector extends ASegmentDetector {

    public FixedSizeMegablockDetector(DetectorConfiguration config) {
        super(config);
    }

    /*
     * Check if candidate sequence is valid
     */
    private Boolean validSequence(List<Instruction> insts) {

        // not big enough
        // if (insts.size() < this.getConfig().getMaxBlocks())
        // return false;

        // check if all branches except the last are forward
        for (int i = 0; i < insts.size() - 1; i++) {
            if (insts.get(i).isBackwardsJump())
                return false;
        }

        // check last one
        if (!insts.get(insts.size() - 1).isBackwardsJump())
            return false;

        /*
         * 
            // TODO fail with stream instructions
        
            // do not form sequences with unknown instructions
            // do not form frequent sequences containing jumps
            if (inst.isUnknown() || inst.isJump()) {
                return false;
            }
         */

        return true;
    }

    @Override
    protected BinarySegment makeSegment(List<Instruction> symbolicseq, List<SegmentContext> contexts) {
        return new MegaBlock(symbolicseq, contexts, this.getCurrentStream().getApp());
    }

    @Override
    protected void processStream(InstructionStream istream, Map<String, HashedSequence> hashed,
            Map<Integer, List<Integer>> addrs) {

        // list of backwards branches, can be used to reconstruct block
        List<Instruction> window = new ArrayList<Instruction>();

        // desired number of basic blocks in Megablock (last one must be backwards branch...
        int blockNr = this.getConfig().getMaxBlocks();

        // get first window
        for (int i = 0; i < blockNr && istream.hasNext(); i++) {
            var is = istream.nextInstruction();
            if (is.isJump())
                window.add(is);
        }

        // process entire stream
        do {

            // hash candidate
            if (validSequence(window)) {

                // create new candidate hash sequence
                var newseq = BinarySegmentDetectionUtils.hashSequence(window);

                // add sequence to occurrence counters (counting varies between static to trace detection)
                BinarySegmentDetectionUtils.addAddrToList(addrs, newseq);

                // add sequence to map which is indexed by hashCode + startaddr
                BinarySegmentDetectionUtils.addHashSequenceToList(hashed, newseq);
            }

            // find next branch
            Instruction is = null;
            while (!(is = istream.nextInstruction()).isJump())
                ;

            // add it
            window.remove(0);
            window.add(is);

        } while (istream.hasNext());
    }
}
