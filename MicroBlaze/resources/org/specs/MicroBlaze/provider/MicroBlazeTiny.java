package org.specs.MicroBlaze.provider;

public enum MicroBlazeTiny implements MicroBlazeELFProvider {

    tiny("org/specs/MicroBlaze/asm/tiny.txt", null, null);

    // private String elfname;
    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private MicroBlazeTiny(String fullPath, Number kernelStart, Number kernelStop) {
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
