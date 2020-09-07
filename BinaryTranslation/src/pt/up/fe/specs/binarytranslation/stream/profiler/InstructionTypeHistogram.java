package pt.up.fe.specs.binarytranslation.stream.profiler;

import java.util.HashMap;

import pt.up.fe.specs.binarytranslation.instruction.InstructionType;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public class InstructionTypeHistogram extends AInstructionStreamProfiler {

    private HashMap<InstructionType, Integer> histogram;

    public InstructionTypeHistogram(InstructionStream istream) {
        super(istream);
        this.histogram = new HashMap<InstructionType, Integer>();
    }

    @Override
    public void profile() {
        while (this.istream.hasNext()) {
            var inst = this.istream.nextInstruction();
            if (inst == null)
                break;

            var type = inst.getData().getGenericTypes().get(0);
            if (this.histogram.containsKey(type)) {
                var count = this.histogram.get(type);
                this.histogram.put(type, count + 1);
            } else {
                this.histogram.put(type, 1);
            }
        }
    }

    public HashMap<InstructionType, Integer> getHistogram() {
        return this.histogram;
    }
}
