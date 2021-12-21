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

public class VerilatorCompile extends StringProcessRun {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");

    // TODO: this class should return an object of type VerilatorCompiledDesign, or something
    // and then we pass that class to VerilatorRun

    /*
     * Arguments to call Verilator on a particular
     * testbench and then call "make" on the resulting cpp code
     */
    private static List<String> getArgs(String directory, String testbenchName) {

        var args = new ArrayList<String>();
        var verilatorexe = "verilator";
        var filepath_partial = "./" + directory + "/" + testbenchName;

        if (IS_WINDOWS)
            verilatorexe += ".exe";

        args.add(verilatorexe);
        args.add("-cc");
        args.add(filepath_partial + "_tb.sv");
        args.add("./hdl/" + testbenchName + ".sv"); // TODO: whats this argument for?
        args.add("-exe");
        args.add(filepath_partial + "_tb.cpp");
        args.add("&&");
        args.add("make");
        args.add("-C");
        args.add("obj_dir");
        args.add("-f");
        args.add("V" + testbenchName + "_tb.mk");
        args.add("V" + testbenchName + "_tb");
        return args;
    }

    // TODO: remove the need for directory specification
    public VerilatorCompile(String directory, String testbenchName) {
        super(VerilatorCompile.getArgs(directory, testbenchName));
    }
}
