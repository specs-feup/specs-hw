package pt.up.fe.specs.binarytranslation.loopdetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.binarytranslation.InstructionProperties;
import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.FrequentSequence;
import pt.up.fe.specs.binarytranslation.interfaces.StaticStream;

public class FrequentStaticSequenceDetector implements SegmentDetector {

    private List<BinarySegment> sequences;
    private List<Instruction> insts;
    private StaticStream elfstream;

    // easy lookup of insts by converting addr to index
    private Map<Integer, Integer> addrtoindex;

    /*
     * Since list needs revisiting, absorb all instructions in
     * the static dump into StaticBasicBlockDetector class instance
     */
    public FrequentStaticSequenceDetector(StaticStream istream) {
        this.elfstream = istream;
        this.sequences = new ArrayList<BinarySegment>();
        this.insts = new ArrayList<Instruction>();
        this.addrtoindex = new HashMap<Integer, Integer>();

        int i = 0;
        Instruction inst;
        while ((inst = elfstream.nextInstruction()) != null) {
            insts.add(inst);
            addrtoindex.put(inst.getAddress().intValue(), i++);
        }
    }

    /*
     * Constructs a map with <instruction addr, sequence hash>
     * Only returns hashes for sequences which happens more than once
     * Returns a map with <sequence hash, List<instruction start addr(s)>>
     */
    private Map<Integer, List<Integer>> getHashCodes(int sequenceSize) {

        // TODO the hash must take into account operands??

        /*
         *  Generate a hash code list of all sequences of size "sequenceSize"
         */
        Map<Integer, Integer> hashes = new LinkedHashMap<Integer, Integer>();
        int i = 0, j = 0;
        while ((i + sequenceSize) < insts.size()) {

            String hashstring = "";
            for (j = 0; j < sequenceSize; j++) {

                Instruction inst = insts.get(i + j);

                // do not form frequent sequences containing jumps // TODO and stream instructions
                if (inst.isJump()) {
                    i += inst.getDelay(); // skip over
                    break; // stop sequence
                }

                // do not form frequent sequences memory accesses
                else if (inst.isMemory()) {
                    break; // stop sequence
                }

                hashstring += Integer.toHexString(inst.getProperties().getOpCode());
                // TODO this unique id (the opcode) will not be unique for arm, since the
                // specific instruction is resolved later with fields that arent being
                // interpreted yet; how to solve?
                // TODO replace getOpCode with getUniqueID()
            }

            // if sequence was complete
            if (j == sequenceSize) {
                hashes.put(insts.get(i).getAddress().intValue(), hashstring.hashCode());
            }

            i++;

            // TODO this detection method doesnt work either, since the flow of
            // register values within the instruciton has to be the same even if
            // the sequence of instructions is the same

            // TODO need new type of instruction class, where operands are abstracted
            // away from their register representations
        }

        /*
         * Make histogram of occurrences
         */
        Map<Integer, Integer> hashcountMap = new HashMap<Integer, Integer>();
        for (Integer hash : hashes.values()) {
            int count = hashcountMap.containsKey(hash) ? hashcountMap.get(hash) : 0;
            hashcountMap.put(hash, count + 1);
        }

        /*
         * Reduce list to sequences which happen more than once 
         */
        Iterator<Integer> it = hashes.keySet().iterator();
        while (it.hasNext()) {
            var hash = hashes.get(it.next());
            if (hashcountMap.get(hash) <= 1)
                it.remove();
        }

        /*
         * Construct map of hashes and lists of addrs where the sequence starts
         */
        Map<Integer, List<Integer>> inv = new HashMap<Integer, List<Integer>>();
        for (Entry<Integer, Integer> entry : hashes.entrySet()) {
            if (inv.containsKey(entry.getValue())) {
                var existinglist = inv.get(entry.getValue());
                existinglist.add(entry.getKey());
                inv.put(entry.getValue(), existinglist);
            } else {
                List<Integer> nl = new ArrayList<Integer>();
                nl.add(entry.getKey());
                inv.put(entry.getValue(), nl);
            }
        }

        return inv;
    }

    @Override
    public List<BinarySegment> detectSegments() {

        // TODO pass window size?
        // TODO pass forbidden operations list?

        // get all stream; redo this another way later

        /*
         * Construct sequences between given sizes
         */
        for (int size = 2; size < 10; size++) {

            Map<Integer, List<Integer>> hashes = getHashCodes(size);

            for (Entry<Integer, List<Integer>> entry : hashes.entrySet()) {
                var firstaddr = entry.getValue().get(0);
                var idx = addrtoindex.get(firstaddr);

                List<InstructionProperties> props = new ArrayList<InstructionProperties>();
                for (Instruction inst : insts.subList(idx, idx + size)) {
                    props.add(inst.getProperties());
                }

                sequences.add(new FrequentSequence(props, entry.getValue()));
            }

            /*
            List<Integer> addrs = getHashCodes(size);
            
            for (int i = 0; i < insts.size(); i++) {
                if (addrs.contains(insts.get(i).getAddress().intValue())) {
                    sequences.add(new FrequentSequence(insts.subList(i, i + size)));
                }
            }*/
        }

        /*
        int decreasecount = 0;
        for (Instruction inst : insts) {
            if (hashes.containsKey(inst.getAddress().intValue())) {
                System.out.print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
                decreasecount = size;
            }
            inst.printInstruction();
            decreasecount--;
            if (decreasecount == 0)
                System.out.print("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
        }
        */
        return sequences;
    }

    @Override
    public void close() throws Exception {
        elfstream.close();
    }
}
