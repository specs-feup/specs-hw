package pt.up.fe.specs.binarytranslation.loopdetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.FrequentSequence;
import pt.up.fe.specs.binarytranslation.interfaces.StaticStream;

public class FrequentStaticSequenceDetector implements SegmentDetector {

    private List<BinarySegment> sequences;
    private List<Instruction> insts;
    private StaticStream elfstream;

    /*
     * Since list needs revisiting, absorb all instructions in
     * the static dump into StaticBasicBlockDetector class instance
     */
    public FrequentStaticSequenceDetector(StaticStream istream) {
        this.elfstream = istream;
        this.sequences = new ArrayList<BinarySegment>();
        this.insts = new ArrayList<Instruction>();

        Instruction inst;
        while ((inst = elfstream.nextInstruction()) != null) {
            insts.add(inst);
        }
    }

    /*
    private void countStaticFrequency() {
        Instruction inst = null;
        while ((inst = elfstream.nextInstruction()) != null) {
    
            // do not form frequent sequences containing jumps
            if (inst.isJump())
                continue;
    
            var name = inst.getName();
            int count = countMap.containsKey(name) ? countMap.get(name) : 0;
            countMap.put(name, count + 1);
            insts.add(inst);
        }
    
        // sort map
        countMap = countMap.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(e -> e.getKey(),
                        e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));
    }
    */

    /*
     * Constructs a map with <instruction addr, sequence hash>
     * Only returns hashes for sequences which happens more than once
     * Returns a list of start instructions
     */
    private List<Integer> getHashCodes(int sequenceSize) {

        /*
         *  Generate a hash code list of all sequences of size "sequenceSize"
         */
        Map<Integer, Integer> hashes = new LinkedHashMap<Integer, Integer>();
        int i = 0, j = 0;
        while ((i + sequenceSize) < insts.size()) {
            String hashstring = "";

            // do not form frequent sequences containing jumps or memory accesses
            for (j = 0; j < sequenceSize && !insts.get(i + j).isJump()
                    && !insts.get(i + j).isMemory(); j++) {
                hashstring += insts.get(i + j).getName();
            }
            if (j == sequenceSize)
                hashes.put(insts.get(i).getAddress().intValue(), hashstring.hashCode());

            i++;
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

        // System.out.print(hashes + "\n");
        // System.out.print(hashes.size() + "\n");

        // System.out.print(hashcountMap + "\n");
        // System.out.print(hashcountMap.size() + "\n");

        return new ArrayList<>(hashes.keySet());
    }

    @Override
    public List<BinarySegment> detectSegments() {

        // TODO pass window size?
        // TODO pass forbidden operations list?

        // get all stream; redo this another way later

        /*
         * Construct sequences between given sizes
         */
        for (int size = 2; size < 4; size++) {
            List<Integer> addrs = getHashCodes(size);
            for (int i = 0; i < insts.size(); i++) {
                if (addrs.contains(insts.get(i).getAddress().intValue())) {
                    sequences.add(new FrequentSequence(insts.subList(i, i + size)));
                }
            }
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
