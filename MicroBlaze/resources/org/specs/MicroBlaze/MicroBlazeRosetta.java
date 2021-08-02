package org.specs.MicroBlaze;

public enum MicroBlazeRosetta implements MicroBlazeELFProvider {

    rendering3d("org/specs/MicroBlaze/asm/Rosetta/3d-rendering.elf", 0x14c4, 0x1528),
    facedetection("org/specs/MicroBlaze/asm/Rosetta/face-detection.elf", 0x1990, 0x1a00);

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
    public Number getKernelStart() {
        return this.kernelStart;
    }

    @Override
    public Number getKernelStop() {
        return this.kernelStop;
    }
}
