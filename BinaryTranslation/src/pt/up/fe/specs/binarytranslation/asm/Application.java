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

package pt.up.fe.specs.binarytranslation.asm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.suikasoft.jOptions.DataStore.ADataClass;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import com.google.gson.annotations.Expose;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.providers.ResourceProvider;

public abstract class Application extends ADataClass<Application> {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");

    public static final DataKey<String> CPUNAME = KeyFactory.string("CPUNAME");
    public static final DataKey<String> QEMUEXE = KeyFactory.string("QEMUEXE");
    public static final DataKey<String> GCC = KeyFactory.string("GCC");
    public static final DataKey<String> GDB = KeyFactory.string("GDB");
    public static final DataKey<String> READELF = KeyFactory.string("READELF");
    public static final DataKey<String> OBJDUMP = KeyFactory.string("OBJDUMP");
    public static final DataKey<ResourceProvider> GDBTMPLINTER = KeyFactory.object("GDBTMPLINTER",
            ResourceProvider.class);
    public static final DataKey<ResourceProvider> GDBTMPLNONINTER = KeyFactory.object("GDBTMPLNONINTER",
            ResourceProvider.class);
    public static final DataKey<ResourceProvider> BAREMETAL_DTB = KeyFactory.object("BAREMETAL_DTB",
            ResourceProvider.class).setDefault(null);
    public static final DataKey<ResourceProvider> QEMU_ARGS_TEMPLATE = KeyFactory.object("QEMU_ARGS_TEMPLATE",
            ResourceProvider.class);

    @Expose
    private final String appName;

    @Expose
    protected String compilationInfo;

    private final ELFProvider elf;
    private final transient File elffile;
    private final long kernelStart, kernelStop;

    public Application(ELFProvider elf, DataStore data) {
        this.elf = elf;
        this.elffile = elf.getFile();
        this.appName = elffile.getName();
        this.getDataStore().addAll(data);
        this.compilationInfo = Application.getCompilationInfo(this, elf);
        var startStop = Application.getKernelBounds(this, elf);

        /*
         * Quick fix for when working from a txt dump...
         */
        if (!startStop.isEmpty()) {
            this.kernelStart = startStop.get(0);
            this.kernelStop = startStop.get(1);
        } else {
            this.kernelStart = 0x0;
            this.kernelStop = 0x0;
        }
    }

    /*
     * 
     */
    private static final String SYMBOLPATTERN = "([0-9a-f]+)[ a-zA-Z]*\\.text\\s([0-9a-f]+)\\s";

    /*.
     * get kernel bounds based on kernel name in enum, using "objdump -t <elfname> | grep <kernelname>"
     */
    protected static List<Long> getKernelBounds(Application app, ELFProvider elf) {

        // call objdump
        var arguments = new ArrayList<String>();
        var objdump = app.get(Application.OBJDUMP);
        var elfpath = app.getElffile().getAbsolutePath();

        if (IS_WINDOWS)
            objdump = objdump + ".exe";

        arguments.add(objdump);
        arguments.add("-t");
        arguments.add(elfpath);
        arguments.add("|");

        var nameNoExt = elf.getFunctionName(); // SpecsIo.removeExtension(elf.getELFName());
        if (IS_WINDOWS)
            arguments.add("findstr " + nameNoExt);
        else
            arguments.add("grep -w " + nameNoExt);

        var output = SpecsSystem.runProcess(arguments, true, false);

        var pat = Pattern.compile(SYMBOLPATTERN + nameNoExt);
        var mat = pat.matcher(output.toString());

        var startStop = new ArrayList<Long>();
        if (mat.find()) {
            var startStopList = SpecsStrings.getRegex(mat.group(0), pat);
            startStop.add(Long.parseUnsignedLong(startStopList.get(0), 16));
            startStop.add(startStop.get(0) + Long.parseUnsignedLong(startStopList.get(1), 16) - 4);
        }

        return startStop;
    }

    /*.
     * Output compilation flags for a given elf, using a given variant of a GNU based "readelf"
     */
    protected static String getCompilationInfo(Application app, ELFProvider elf) {

        // call readelf
        var arguments = new ArrayList<String>();
        var readelf = app.get(Application.READELF);
        var elfpath = app.getElffile().getAbsolutePath();
        if (IS_WINDOWS)
            readelf = readelf + ".exe";

        arguments.add(readelf);
        arguments.add("-wi");
        arguments.add(elfpath);
        arguments.add("|");

        if (IS_WINDOWS)
            arguments.add("findstr /irc:DW_AT_producer");
        else
            arguments.add("grep -i compilation -A6");

        var output = SpecsSystem.runProcess(arguments, true, false);
        var pat = Pattern.compile("GNU.*");
        var mat = pat.matcher(output.toString());
        if (mat.find())
            return mat.group(0);
        else
            return "Could not retrieve build information!";
    }

    public String getAppName() {
        return appName;
    }

    public ELFProvider getELFProvider() {
        return elf;
    }

    public File getElffile() {
        return elffile;
    }

    public String getCompilationInfo() {
        return compilationInfo;
    }

    public long getKernelStart() {
        return kernelStart;
    }

    public long getKernelStop() {
        return kernelStop;
    }

    /*
     * TODO: implement this based on reading the elf file once, the storing the inst width
     * 
     * WARNING, this only works for ISAs with constant width!
     * maybe implement inst width as an InstructionProperty?
     * 
     */
    public int getInstructionWidth() {
        return 4; // return in bytes
    }
}
