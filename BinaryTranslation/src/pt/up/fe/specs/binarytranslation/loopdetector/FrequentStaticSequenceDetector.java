package pt.up.fe.specs.binarytranslation.loopdetector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.interfaces.StaticStream;

public class FrequentStaticSequenceDetector implements SegmentDetector {

    private List<BinarySegment> sequences;
    private List<Instruction> insts;
    private StaticStream elfstream;
    private Map<String, Integer> countMap;

    /*
     * Since list needs revisiting, absorb all instructions in
     * the static dump into StaticBasicBlockDetector class instance
     */
    public FrequentStaticSequenceDetector(StaticStream istream) {
        this.elfstream = istream;
        this.sequences = new ArrayList<BinarySegment>();
        this.insts = new ArrayList<Instruction>();
        this.countMap = new HashMap<String, Integer>();
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
    private Map<Integer, Integer> getHashCodes(int sequenceSize) {

        // generate a hashcode list of all sequences of size "sequenceSize"
        Map<Integer, Integer> hashcodes = new LinkedHashMap<Integer, Integer>();
        int i = 0;
        while((i + sequenceSize) < insts.size()) {
            String hashstring = "";      
            for(int j = 0; j < sequenceSize && (i + j) < insts.size(); j++) {
                hashstring += insts.get(i + j).getName();      
            }       
            hashcodes.put(insts.get(i).getAddress().intValue(), hashstring.hashCode());
            i++;
        }
        System.out.print(hashcodes);
        System.out.print("\n" + hashcodes.size());        
        return hashcodes;
    }
    
    private void countHashFrequency(Map<Integer, Integer> hashcodes) {     
       Map<Integer, Integer> hashcountMap = new HashMap<Integer, Integer>();     
       for(Integer hash : hashcodes.values()) {
            int count = hashcountMap.containsKey(hash) ? hashcountMap.get(hash) : 0;
            hashcountMap.put(hash, count + 1);
        }
       
       System.out.print(hashcountMap);
    }

    @Override
    public List<BinarySegment> detectSegments() {

        // TODO pass window size?
        // TODO pass forbidden operations list?
        
        // get all stream; redo this another way later
        Instruction inst;
        while ((inst = elfstream.nextInstruction()) != null) {
            insts.add(inst);
        }
        
        countHashFrequency(getHashCodes(3));
        return null;
    }

    @Override
    public void close() throws Exception {
        elfstream.close();
    }
}
