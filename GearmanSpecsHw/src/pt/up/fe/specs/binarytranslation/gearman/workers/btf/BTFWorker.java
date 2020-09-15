/**
 *  Copyright 2020 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package pt.up.fe.specs.binarytranslation.gearman.workers.btf;

import org.gearman.GearmanFunctionCallback;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;

import pt.up.fe.specs.binarytranslation.gearman.SpecsHwWorker;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationFrontEndUtils;

public class BTFWorker extends SpecsHwWorker {

    @Override
    public byte[] workInternal(String function, byte[] data, GearmanFunctionCallback callback) throws Exception {
        var input = new BTFInput(data);
        var output = new BTFOutput();

        // perform analysis with all detectors
        for(var detector : input.getDetectors()) {
            var bundle  = BinaryTranslationFrontEndUtils.doBackend(input.getProgram(),  MicroBlazeElfStream.class, detector);
            bundle.toJSON();
            // save graph bundle
            output.addGraphs(bundle.getGraphs());
            // set total instructions (this only needs to be done once though, figure out a better way to do this later)
            output.setTotalInstructions(bundle.getIstream().getNumInstructions());
        }
        // sort graphs based on address
        output.sortAddresses();
        // send output
        return output.getJSONBytes();
    }

    @Override
    protected byte[] getErrorOutput(String message) {
        return (new BTFErrorOutput(message)).getJSONBytes();   
    }

}
