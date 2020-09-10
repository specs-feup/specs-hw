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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import pt.up.fe.specs.binarytranslation.binarysegments.detection.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.FrequentTraceSequenceDetector;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.TraceBasicBlockDetector;

public class BTFInput {
    
    private final String program;
    
    private final String analysis;
    
    private final List<Class<?>> detectors;
    
    public BTFInput(byte[] data) {
        // get request data JSON in Map form
        String dataString = new String(data);
        Gson gson = new GsonBuilder().create();
        var inputOptions = gson.fromJson(dataString, Map.class);
        
        this.program = "org/specs/MicroBlaze/asm/" + inputOptions.get("program_name") + ".txt";
        this.analysis = (String) inputOptions.get("analysis_mode");
        this.detectors = new ArrayList<>();
        // this is already an array list for some reason...        
        List<String> segments = (ArrayList) inputOptions.get("binary_segments");
        
        for (var bs : segments) {
            switch(bs) {
                case "Basic Block":
                    if (this.getAnalysis().equals("static"))
                        this.getDetectors().add(StaticBasicBlockDetector.class);
                    else
                        this.getDetectors().add(TraceBasicBlockDetector.class);
                    break;
                case "Frequent Instruction Sequence": 
                    if (this.getAnalysis().equals("static"))
                        this.getDetectors().add(FrequentStaticSequenceDetector.class);
                    else
                        this.getDetectors().add(FrequentTraceSequenceDetector.class);
                    break;
                case "Superblock":
                    // code block
                    break;
                case "Megablock": 
                    // code block
                    break;
                default:
                    break;
            }
        }
    }

    public String getProgram() {
        return program;
    }

    public String getAnalysis() {
        return analysis;
    }

    public List<Class<?>> getDetectors() {
        return detectors;
    }
    
}
