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

import pt.up.fe.specs.binarytranslation.asm.Application;

public class ObjDump extends StringProcessRun {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");

    /*
     * 
     */
    private static List<String> getArgs(Application app) {
        var args = new ArrayList<String>();
        var elfname = app.getElffile().getAbsolutePath();
        var objdumpexe = app.get(Application.OBJDUMP);
        if (IS_WINDOWS)
            objdumpexe += ".exe";
        args.add(objdumpexe);
        args.add("-d");
        args.add(elfname);
        return args;
    }

    public ObjDump(Application app) {
        super(ObjDump.getArgs(app));
    }

    @Override
    protected void attachThreads() {
        this.attachStdOut();
    }

    public String getLine() {
        return super.receive();
    }
}
