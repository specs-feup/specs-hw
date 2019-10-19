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

    private void formCandidateSequences() {

        // (only the most frequent insts are likely to form sequences which in turn are frequent)
        List<String> freqnames = new ArrayList<String>();
        for (int i = 0; i < countMap.size() / 5; i++) {
            // freqnames.add(countMap.get(i).get)
        }

    }

    @Override
    public List<BinarySegment> detectSegments() {

        // TODO pass window size?
        // TODO pass forbidden operations list?

        // step 1. read entire stream -> count frequency of all instructions
        countStaticFrequency();
        System.out.print(countMap);

        // step 2. form sequences of any size which contain only the most frequent insts

        return null;
    }

    @Override
    public void close() throws Exception {
        elfstream.close();
    }
}
