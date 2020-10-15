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

import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.util.providers.ResourceProvider;

public abstract class Application {

    @Expose
    private final String appName;

    @Expose
    private final String compilationInfo;

    @Expose
    private final ResourceProvider cpuArchitectureName;

    private final File elffile;
    private final ResourceProvider gdb, gdbTmpl, objDump, readElf, qemuExe, dtbFile;

    public Application(File elffile, ResourceProvider cpuArchitectureName, ResourceProvider gdb,
            ResourceProvider objdump, ResourceProvider readelf, ResourceProvider gdbtmpl, ResourceProvider qemuexe,
            ResourceProvider dtbfile) {

        this.elffile = elffile;
        this.appName = elffile.getName();
        this.cpuArchitectureName = cpuArchitectureName;
        this.gdb = gdb;
        this.objDump = objdump;
        this.readElf = readelf;
        this.gdbTmpl = gdbtmpl;
        this.qemuExe = qemuexe;
        this.dtbFile = dtbfile;
        this.compilationInfo = BinaryTranslationUtils.getCompilationInfo(elffile.getPath(), readelf.getResource());
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
     *     @Override
    public int getInstructionWidth() {
        return 4; // return in bytes
    }
     */

}
