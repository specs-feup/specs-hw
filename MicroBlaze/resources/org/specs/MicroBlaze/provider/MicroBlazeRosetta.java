package org.specs.MicroBlaze.provider;

public enum MicroBlazeRosetta implements MicroBlazeELFProvider {

    rendering3d(0x14c4, 0x1528),
    facedetection(0x1990, 0x1a00);

    private String elfName;
    private Number kernelStart;
    private Number kernelStop;

    private MicroBlazeRosetta(Number kernelStart, Number kernelStop) {
        this.elfName = name() + ".elf";
        this.kernelStart = kernelStart;
        this.kernelStop = kernelStop;
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
