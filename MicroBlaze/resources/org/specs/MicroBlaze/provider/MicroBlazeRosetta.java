package org.specs.MicroBlaze.provider;

public enum MicroBlazeRosetta implements MicroBlazeZippedELFProvider {

    rendering3d("_Z12rendering_swP11Triangle_3DPA256_h", "3d-rendering"),
    facedetection("_Z14face_detect_swPA320_hPiS1_S1_S1_S1_", "face-detection");

    private String functionName;
    private String elfName;

    private MicroBlazeRosetta(String functionName, String elfname) {
        this.functionName = functionName;
        this.elfName = elfname + ".elf";
    }

    @Override
    public String getELFName() {
        return this.elfName;
    }

    @Override
    public String getFunctionName() {
        return this.functionName;
    }
}