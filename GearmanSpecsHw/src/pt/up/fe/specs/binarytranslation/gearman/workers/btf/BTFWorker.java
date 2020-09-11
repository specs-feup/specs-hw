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
import pt.up.fe.specs.binarytranslation.binarysegments.detection.*;

public class BTFWorker extends SpecsHwWorker {

    @Override
    public byte[] workInternal(String function, byte[] data, GearmanFunctionCallback callback) throws Exception {
        var input = new BTFInput(data);
        var output = new BTFOutput();

        // perform analysis with all detectors
        for(var detector : input.getDetectors()) {
            var bundle  = BinaryTranslationFrontEndUtils.doBackend(input.getProgram(),  MicroBlazeElfStream.class, detector);
            output.addGraphs(bundle.getGraphs());
        }
        // sort graphs based on address
        output.sortAddresses();
        // TODO save graph bundle
        //bundle.toJSON();
        // send output
        return output.getJSONBytes();
    }

    @Override
    protected byte[] getErrorOutput(String message) {
        return (new BTFErrorOutput(message)).getJSONBytes();   
    }

}
