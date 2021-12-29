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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import pt.up.fe.specs.binarytranslation.NullResource;
import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.specshw.SpecsHwUtils;
import pt.up.fe.specs.util.utilities.Replacer;

/**
 * Starts a QEMU process, so that the BTF can have a direct handle to it, in order to KILL IT PROPERLY!
 * 
 * @author nuno
 *
 */
public class QEMU implements AutoCloseable {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");
    private static Set<Integer> portsInUse = new HashSet<Integer>();

    private Integer port = 0;
    private Process proc;
    private final List<String> args;

    public QEMU(Application app) {
        // find lowest available port starting from 1234
        var tryport = Integer.valueOf(1234);
        while (portsInUse.contains(tryport)) {
            tryport = Integer.valueOf(tryport.intValue() + 1);
        }
        portsInUse.add(tryport);

        this.port = tryport;
        this.args = QEMU.getArgsList(app, this.port);
    }

    public List<String> getLaunchArguments() {
        var newArgs = new ArrayList<String>();
        newArgs.add("bash");
        newArgs.add("-l");
        newArgs.add("-c");
        newArgs.add(args.stream().collect(Collectors.joining(" ")));
        return newArgs;
    }

    public void start() {
        this.proc = SpecsHwUtils.newProcess(
                new ProcessBuilder(this.getLaunchArguments()));
    }

    public int getPort() {
        return port.intValue();
    }

    /*
     * 
     */
    @Override
    public void close() {
        try {
            portsInUse.remove(this.port);
            if (!this.proc.waitFor(200, TimeUnit.MILLISECONDS))
                proc.destroy();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * 
     */
    private static List<String> getArgsList(Application app, int port) {

        // template as string
        var qemutmpl = app.get(Application.QEMU_ARGS_TEMPLATE).read();

        var elfpath = app.getElffile().getAbsolutePath();
        var qemuexe = app.get(Application.QEMUEXE);
        if (IS_WINDOWS) {
            qemuexe += ".exe";
            elfpath = elfpath.replace("\\", "/");
        }

        var qemuArgs = new Replacer(qemutmpl);
        qemuArgs.replace("<ELFNAME>", elfpath);
        qemuArgs.replace("<QEMUBIN>", qemuexe);
        qemuArgs.replace("<PORT>", port);

        var dtbResourceProvider = app.get(Application.BAREMETAL_DTB);
        if (dtbResourceProvider != NullResource.nullResource) {
            var dtbfileOnDisk = dtbResourceProvider.write();
            dtbfileOnDisk.deleteOnExit();
            var dtbpath = dtbfileOnDisk.getAbsolutePath();
            if (IS_WINDOWS)
                dtbpath = dtbpath.replace("\\", "/");
            qemuArgs.replace("<DTBFILE>", dtbpath);
        }

        if (IS_WINDOWS)
            qemuArgs.replace("<KILL>", "");
        else
            qemuArgs.replace("<KILL>", "kill");

        return Arrays.asList(qemuArgs.toString().split(" "));
    }

    public boolean isAlive() {
        return this.proc.isAlive();
    }
}
