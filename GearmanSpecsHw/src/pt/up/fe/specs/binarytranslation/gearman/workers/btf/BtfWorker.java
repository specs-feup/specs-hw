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

import java.util.Map;

import org.gearman.GearmanFunctionCallback;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pt.up.fe.specs.binarytranslation.gearman.SpecsHwWorker;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationFrontEndUtils;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.*;

public class BtfWorker extends SpecsHwWorker {

    @Override
    public byte[] workInternal(String function, byte[] data, GearmanFunctionCallback callback) throws Exception {
        String dataString = new String(data);
        
        Gson gson = new GsonBuilder().create();
        
        // get request data JSON in Map form
        var input = gson.fromJson(dataString, Map.class);
        System.out.println("INPUT OBJECT:\n" + input + "\n");
        var program = "org/specs/MicroBlaze/asm/" + input.get("program_name") + ".txt";
        
        var bundle  = BinaryTranslationFrontEndUtils.doBackend(program,  MicroBlazeElfStream.class, FrequentStaticSequenceDetector.class);
        // save graph bundle
        bundle.toJSON();
        // send output
        var output = new BTFOutput(bundle.getGraphs());
        return output.getJSONBytes();
    }

    @Override
    protected byte[] getErrorOutput(String message) {
        var gson = new GsonBuilder().create();
        return gson.toJson(new BTFErrorOutput(message)).getBytes();   
    }

}
