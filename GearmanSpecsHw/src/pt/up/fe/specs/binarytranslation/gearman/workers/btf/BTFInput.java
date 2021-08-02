/**
 * Copyright 2020 SPeCS.
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

package pt.up.fe.specs.binarytranslation.gearman.workers.btf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentTraceSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;

/**
 * 
 * Class that stores information about a request from the Binary Translation Tool Web Application.
 * 
 * @author marantesss
 *
 */
public class BTFInput {

    /**
     * Path to chosen program
     */
    private final ELFProvider program;

    /**
     * Analysis mode
     * 
     * Can be either 'static' or 'dynamic'
     */
    private final String analysis;

    /**
     * List of chosen detectors
     * 
     * @see SegmentDetector
     */
    private final List<Class<?>> detectors;

    /**
     * 
     * @param name
     * @return
     */
    private ELFProvider getELF(String name) {

        for (var elf : MicroBlazeLivermoreELFN10.values()) {
            if (elf.asTxtDump().equals(name)) {
                return elf;
            }
        }
        return null;
    }

    /**
     * Constructor
     * 
     * @param data
     *            Incoming byte JSON String from BTF Web App
     */
    public BTFInput(byte[] data) {

        // get request data JSON in Map form
        String dataString = new String(data);
        Gson gson = new GsonBuilder().create();
        var inputOptions = gson.fromJson(dataString, Map.class);

        var progname = "org/specs/MicroBlaze/asm/N10/"
                + inputOptions.get("program_name") + ".txt";

        this.program = getELF(progname);
        this.analysis = (String) inputOptions.get("analysis_mode");
        this.detectors = new ArrayList<>();

        // this is already an array list for some reason...
        List<String> segments = (ArrayList) inputOptions.get("binary_segments");

        for (var bs : segments) {
            switch (bs) {
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

    /**
     * Getter method for program
     * 
     * @return program
     */
    public ELFProvider getProgram() {
        return program;
    }

    /**
     * Getter method for analysis
     * 
     * @return analysis
     */
    public String getAnalysis() {
        return analysis;
    }

    /**
     * Getter method for detectors
     * 
     * @return detectors
     */
    public List<Class<?>> getDetectors() {
        return detectors;
    }

}
