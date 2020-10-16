package pt.up.fe.specs.binarytranslation.detection.detectors;

public class DetectorConfiguration {

    /*
     * min and max size of windows 
     */
    private final int minsize;
    private final int maxsize;

    // privar final List< ??? > prohibited types; ?

    public DetectorConfiguration(int minsize, int maxsize) {
        this.minsize = minsize;
        this.maxsize = maxsize;
    }

    public int getMaxsize() {
        return maxsize;
    }

    public int getMinsize() {
        return minsize;
    }

    public static DetectorConfiguration defaultConfig() {
        return new DetectorConfiguration(4, 20);
    }
}
