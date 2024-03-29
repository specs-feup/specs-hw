/**
 * Copyright 2022 SPeCS.
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

package pt.up.fe.f4pga;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.utilities.Replacer;

public class F4FPGATests {

    //////////////////////////////////////////////////////////
    @Test
    public void testF4PGAFlow() {

        // TODO: build dynamically or use a class to represent all HDLs
        var hdls = new ArrayList<String>();
        hdls.add("\"counter1.v\"");
        hdls.add("\"counter2.v\"");

        var flow1 = new F4PGAFlow("counter", "./output", hdls, F4PGATarget.Basys3);

        // should output the json and bash files
        flow1.setupFlow();

        // TODO:
        // var output = flow1.run();
    }

    //////////////////////////////////////////////////////////
    @Test
    public void testJSONRead() {
        var tmpl = F4PGAResource.F4PGAJSON_TEMPLATE;
        var tmplAsString = tmpl.read();
        System.out.println(tmplAsString);
    }

    @Test
    public void testJSONWriteToDisk() {
        var tmpl = F4PGAResource.F4PGAJSON_TEMPLATE;
        var tmplOnDisk = tmpl.write();
        tmplOnDisk.deleteOnExit();
    }

    @Test
    public void testJSONReplaceToDisk() {

        var tmpl = F4PGAResource.F4PGAJSON_TEMPLATE;
        var tmplAsString = tmpl.read();

        // list of HDL files
        var hdls = new ArrayList<String>();
        hdls.add("\"counter1.v\"");
        hdls.add("\"counter2.v\"");

        var repl = new Replacer(tmplAsString);
        repl.replace("<SOURCE_LIST>", hdls);
        repl.replace("<PART_NUMBER>", F4PGATarget.Arty100T.getPartName());
        // repl.replace("<PART_NUMBER>", F4PGATarget.Basys3.getPartName());

        var tmplChanged = new File("./jsonChanged.json");
        SpecsIo.write(tmplChanged, repl.toString());
        tmplChanged.deleteOnExit();
    }

    public void testGetBuildCommand() {

        String DEFAULT_F4PGA_INSTALL_DIR = "~/opt/f4pga";
        List<String> aux = F4PGA.getBuildCommands(DEFAULT_F4PGA_INSTALL_DIR);

    }

    /*
     * 
     * make script
     *  var topscript = new File("./topscript.sh");
     *  topscript.add ????
     *      export 
        SpecsIo.write(tmplChanged, repl.toString());
     * 
     *  var args = new ArrayList<String>();            
        args.add("bash");
        args.add("topscript.sh");
        var unzipResult = SpecsSystem.runProcess(args, true, false);
     */
}
