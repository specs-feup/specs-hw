package org.specs.MicroBlaze.provider;

import java.io.File;
import java.util.function.Function;

import pt.up.fe.specs.util.SpecsIo;

public enum MicroBlazeTiny implements MicroBlazeELFProvider {

    tiny("org/specs/MicroBlaze/asm/tiny.txt", null, null);

    private String elfName;
    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private MicroBlazeTiny(String fullPath, Number kernelStart, Number kernelStop) {
        this.fullPath = fullPath;
        this.elfName = name() + ".elf";
        this.kernelStart = kernelStart;
        this.kernelStop = kernelStop;
    }

    @Override
    public String getResource() {
        return this.fullPath;
    }

    @Override
    public String read() {
        var fd = SpecsIo.resourceCopy(this.getResource());
        return SpecsIo.read(fd);
    }

    @Override
    public File write(File folder, boolean overwrite, Function<String, String> nameMapper) {
        File fd = SpecsIo.resourceCopy(this.getResource());
        fd.deleteOnExit();
        return fd;
    }

    @Override
    public Number getKernelStart() {
        return kernelStart;
    }

    @Override
    public Number getKernelStop() {
        return kernelStop;
    }

    @Override
    public String getELFName() {
        return this.elfName;
    }
}
