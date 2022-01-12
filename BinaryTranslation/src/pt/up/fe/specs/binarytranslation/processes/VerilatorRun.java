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

package pt.up.fe.specs.binarytranslation.processes;

import java.util.ArrayList;
import java.util.List;

public class VerilatorRun extends StringProcessRun {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");

    private boolean started = false;

    /*
     * TODO: this class should receive a VerilatorCompiledDesign as an argument
     * to initialize its own argument list
     */
    private static List<String> getArgs(String directory, String testbenchName) {

        var args = new ArrayList<String>();
        var compileddesignexe =  testbenchName + "_tb";
        if (IS_WINDOWS)
            compileddesignexe += ".exe"; // TODO: required??
        
        args.add("cd" + directory);
        args.add("&&");
        args.add("./obj_dir/" + compileddesignexe);
        return args;
    }

    public VerilatorRun(String directory, String testbenchName) {
        super(VerilatorRun.getArgs(directory, testbenchName));
    }

    @Override
    public Process start() {
        this.started = true;
        return super.start();
    }

    public boolean simulate() {

        if (this.started == false)
            this.start();

        String rline, result = null;
        while ((rline = this.receive()) != null) {
            result = rline;
        }

        if (result != null) {
            System.out.println("\t" + result); // TODO: sanitize
            return result.equals("PASSED") ? true : false;
        } else
            return false;
    }
}
