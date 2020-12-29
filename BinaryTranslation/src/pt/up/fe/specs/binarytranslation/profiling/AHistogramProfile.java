package pt.up.fe.specs.binarytranslation.profiling;

import java.util.function.Function;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.profiling.data.InstructionProfileResult;
import pt.up.fe.specs.binarytranslation.profiling.data.ProfileHistogram;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public abstract class AHistogramProfile extends AInstructionStreamProfiler {

    private Function<Instruction, String> getKey;

    public AHistogramProfile(Function<Instruction, String> getKey) {
        super();
        this.getKey = getKey;
    }

    @Override
    public InstructionProfileResult profile(InstructionStream istream) {
        ProfileHistogram histogram = new ProfileHistogram(
                istream.getApp().getAppName(), this.getClass().getSimpleName());
        var hist = histogram.getHistogram();

        this.profileTime = System.nanoTime();
        boolean profileOn = (this.startAddr.intValue() == -1) ? true : false;

        Instruction inst = null;
        while ((inst = istream.nextInstruction()) != null) {

            // NOTE: this allows for intermittent profiling of a given region
            // start profiling (only profile if start address is hit or if startaddr == -1)
            if (!profileOn && (inst.getAddress().equals(this.startAddr)))
                profileOn = true;

            // end profiling
            else if (profileOn && (inst.getAddress().equals(this.stopAddr)))
                profileOn = false;

            if (profileOn) {
                var key = this.getKey.apply(inst);
                if (hist.containsKey(key)) {
                    var count = hist.get(key);
                    hist.put(key, count + 1);

                } else {
                    hist.put(key, 1);
                }
            }
        }
        this.profileTime = System.nanoTime() - this.profileTime.longValue();
        histogram.setProfileTime(profileTime); // ugly, but works for now
        return histogram;
    }
}
