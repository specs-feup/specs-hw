package org.specs.MicroBlaze;

import org.specs.BinaryTranslation.ELFProvider;

public enum MicroBlazeRosetta implements ELFProvider {

    rendering3d("org/specs/MicroBlaze/asm/Rosetta/3d-rendering.elf", 0x0, 0x1818),
    facedetection("org/specs/MicroBlaze/asm/Rosetta/face-detection.elf", null, null);

    // private String elfname;
    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private MicroBlazeRosetta(String fullPath, Number kernelStart, Number kernelStop) {
        // this.elfname = name();
        this.fullPath = fullPath;
        this.kernelStart = kernelStart;
        this.kernelStop = kernelStop;
    }

    @Override
    public String getResource() {
        return this.fullPath;
    }

    @Override
    public String asTxtDump() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Number getKernelStart() {
        return this.kernelStart;
    }

    @Override
    public Number getKernelStop() {
        return this.kernelStop;
    }

    @Override
    public String asTraceTxtDump() {
        return this.fullPath.replace(".elf", "_trace.txt");
    }

}
