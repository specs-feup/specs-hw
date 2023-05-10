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
 
package pt.up.fe.specs.specshw.processes;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TxtDump extends StringProcessRun {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");
    private static final List<String> WARGS = Arrays.asList("cmd", "/c", "type");
    private static final List<String> UARGS = Arrays.asList("cat");

    private static List<String> getArgs(File txtfile) {
        var args = new ArrayList<String>();
        if (IS_WINDOWS)
            args.addAll(WARGS);
        else
            args.addAll(UARGS);
        args.add(txtfile.getAbsolutePath());
        return args;
    }

    public TxtDump(File txtfile) {
        super(TxtDump.getArgs(txtfile));
    }

    @Override
    protected void attachThreads() {
        this.attachStdOut();
    }

    public String getLine() {
        return super.receive();
    }
}
