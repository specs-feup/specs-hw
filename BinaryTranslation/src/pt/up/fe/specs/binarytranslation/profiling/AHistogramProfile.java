/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */
 
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
        boolean profileOn = (this.startAddr.longValue() == -1) ? true : false;

        Instruction inst = null;
        while ((inst = istream.nextInstruction()) != null) {

            // NOTE: this allows for intermittent profiling of a given region
            // start profiling (only profile if start address is hit or if startaddr == -1)
            if (!profileOn && (inst.getAddress().equals(this.startAddr.longValue())))
                profileOn = true;

            // end profiling
            else if (profileOn && (inst.getAddress().equals(this.stopAddr.longValue())))
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
