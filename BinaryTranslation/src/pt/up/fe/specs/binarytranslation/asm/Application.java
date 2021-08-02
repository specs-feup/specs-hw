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

import com.google.gson.annotations.Expose;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.providers.ResourceProvider;
import pt.up.fe.specs.util.utilities.Replacer;

public abstract class Application {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");

    @Expose
    private final String appName;

    @Expose
    private final String compilationInfo;

    @Expose
    private final ResourceProvider cpuArchitectureName;

    @Expose
    private final ResourceProvider gdb, gdbTmpl, gdbTmplNoninter, objDump, readElf, qemuExe, dtbFile;

    private final ELFProvider elf;
    private final transient File elffile;

    public Application(ELFProvider elf,
            ResourceProvider cpuArchitectureName,
            ResourceProvider gdb,
            ResourceProvider objdump,
            ResourceProvider readelf,
            ResourceProvider gdbtmpl,
            ResourceProvider gdbTmplNoninter,
            ResourceProvider qemuexe,
            ResourceProvider dtbfile) {

        this.elf = elf;
        this.elffile = BinaryTranslationUtils.getFile(elf);
        this.appName = elffile.getName();
        this.cpuArchitectureName = cpuArchitectureName;
        this.gdb = gdb;
        this.objDump = objdump;
        this.readElf = readelf;
        this.gdbTmpl = gdbtmpl;
        this.gdbTmplNoninter = gdbTmplNoninter;
        this.qemuExe = qemuexe;
        this.dtbFile = dtbfile;
        this.compilationInfo = BinaryTranslationUtils.getCompilationInfo(elffile, readelf.getResource());
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

    public ResourceProvider getCpuArchitectureName() {
        return cpuArchitectureName;
    }

    public ResourceProvider getGdb() {
        return gdb;
    }

    public ResourceProvider getObjdump() {
        return objDump;
    }

    public ResourceProvider getReadelf() {
        return readElf;
    }

    public ResourceProvider getGdbtmpl() {
        return gdbTmpl;
    }

    public ResourceProvider getGdbtmplNonInteractive() {
        return gdbTmplNoninter;
    }

    public ResourceProvider getQemuexe() {
        return qemuExe;
    }

    public ResourceProvider getDtbfile() {
        return dtbFile;
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
        return Application.getGDBScript(this, this.getGdbtmpl());
    }

    /*
     * 
     */
    public File getGDBScriptNonInteractive() {
        return Application.getGDBScript(this, this.getGdbtmplNonInteractive());
    }

    /*
     * 
     */
    private static File getGDBScript(Application app, ResourceProvider gdbtmpl) {

        var elfpath = app.getElffile().getAbsolutePath();
        var qemuexe = app.getQemuexe().getResource();
        if (IS_WINDOWS) {
            qemuexe += ".exe";
            elfpath = elfpath.replace("\\", "/");
        }

        var gdbScript = new Replacer(gdbtmpl);
        gdbScript.replace("<ELFNAME>", elfpath);
        gdbScript.replace("<QEMUBIN>", qemuexe);

        if (app.getDtbfile() != null) {
            var fd = BinaryTranslationUtils.getFile(app.getDtbfile().getResource());
            var dtbpath = fd.getAbsolutePath();
            if (IS_WINDOWS)
                dtbpath = dtbpath.replace("\\", "/");
            gdbScript.replace("<DTBFILE>", dtbpath);
        }

        if (IS_WINDOWS)
            gdbScript.replace("<KILL>", "");
        else
            gdbScript.replace("<KILL>", "kill");

        var fd = new File("tmpscript.gdb");
        SpecsIo.write(fd, gdbScript.toString());
        return fd;
    }
}
