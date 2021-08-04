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

import org.suikasoft.jOptions.DataStore.ADataClass;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;

import com.google.gson.annotations.Expose;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.providers.ResourceProvider;
import pt.up.fe.specs.util.utilities.Replacer;

public abstract class Application extends ADataClass<Application> {

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
            ResourceProvider.class);
    public static final DataKey<ResourceProvider> QEMU_ARGS_TEMPLATE = KeyFactory.object("QEMU_ARGS_TEMPLATE",
            ResourceProvider.class);

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");

    @Expose
    private final String appName;

    @Expose
    private final String compilationInfo;

    private final ELFProvider elf;
    private final transient File elffile;

    public Application(ELFProvider elf) {
        this.elf = elf;
        this.elffile = elf.getFile();
        this.appName = elffile.getName();
        this.compilationInfo = BinaryTranslationUtils.getCompilationInfo(elffile, get(READELF));
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

    /*
     * 
     */
    public File getGDBScriptInteractive() {
        return Application.getGDBScript(this, get(GDBTMPLNONINTER));
    }

    /*
     * 
     */
    public File getGDBScriptNonInteractive() {
        return Application.getGDBScript(this, get(GDBTMPLINTER));
    }

    /*
     * 
     */
    private static File getGDBScript(Application app, ResourceProvider gdbtmpl) {

        var elfpath = app.getElffile().getAbsolutePath();
        var gdbScript = new Replacer(gdbtmpl);
        gdbScript.replace("<ELFNAME>", elfpath);

        if (IS_WINDOWS)
            gdbScript.replace("<KILL>", "");
        else
            gdbScript.replace("<KILL>", "kill");

        var fd = new File("tmpscript.gdb");
        SpecsIo.write(fd, gdbScript.toString());
        return fd;
    }
}
