/**
 *  Copyright 2021 SPeCS.
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

package pt.up.fe.specs.binarytranslation.hardware.analysis.timing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pt.up.fe.specs.util.SpecsLogs;

public class TimingAnalysisRun {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");

    public static void start(String directory, String moduleName) throws IOException {

        StringBuilder processCommands = new StringBuilder();
        
        if(TimingAnalysisRun.IS_WINDOWS)
            processCommands.append("bash -c \"");
        
        processCommands.append("cd " + directory + "/hw/;");
        processCommands.append("yosys -p 'synth_ice40 -json ./syn/" + moduleName + "_syn_report.json -blif ./syn/" + moduleName + ".blif ' ./hdl/" + moduleName + ".sv;");
        processCommands.append("yosys -o ./syn/" + moduleName + "_syn.sv ./syn/" + moduleName +".blif;");
        processCommands.append("nextpnr-ice40 --hx1k --json ./syn/" + moduleName + "_syn_report.json --report ./syn/" + moduleName + "_asc_report.json --asc ./syn/" + moduleName + ".asc;");
        processCommands.append("icetime -d hx1k ./syn/" + moduleName + ".asc | grep -E -o '([0-9.]+ [npm]*s)' > ./syn/timing_analysis.txt;");
        if(TimingAnalysisRun.IS_WINDOWS)
            processCommands.append("\"");

        ProcessBuilder processBuilder = new ProcessBuilder();
        
        Process process = processBuilder.command("cmd.exe", "/c", processCommands.toString()).start();
        /*
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        
        String line;
        String returnLine = "";
        
        while((line = reader.readLine()) != null){
            returnLine = line;
        }
        
        System.out.println(returnLine);
        */
        
        
    }
    
}
