package pt.up.fe.specs.binarytranslation.profiling.data;

import java.util.function.Function;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.profiling.AInstructionStreamProfiler;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public abstract class AHistogramProfile extends AInstructionStreamProfiler {

    private Function<Instruction, String> getKey;

    public AHistogramProfile(Function<Instruction, String> getKey) {
        super();
        this.getKey = getKey;
    }

    @Override
    public InstructionProfileResult profile(InstructionStream istream) {
        ProfileHistogram histogram = new ProfileHistogram();
        var hist = histogram.getHistogram();

        Instruction inst = null;
        while ((inst = istream.nextInstruction()) != null) {

            var key = this.getKey.apply(inst);
            if (hist.containsKey(key)) {
                var count = hist.get(key);
                hist.put(key, count + 1);

            } else {
                hist.put(key, 1);
            }
        }

        return histogram;
    }
}
