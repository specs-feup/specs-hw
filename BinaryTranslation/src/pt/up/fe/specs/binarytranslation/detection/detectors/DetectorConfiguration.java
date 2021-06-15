package pt.up.fe.specs.binarytranslation.detection.detectors;

public class DetectorConfiguration {

    protected Number startAddr = 0x00000000L, stopAddr = 0xFFFFFFFFL;
    protected int minsize = 2;
    protected int maxsize = 10; // segment size that will be looked for
    protected int maxBlocks; // max forward jumping or backward jumping links in superblocks and megablocks
    protected float superblockBranchThreshold;
    protected Number prematureStopAddr = -1;
    protected Number skipToAddr = -1;

    private DetectorConfiguration() {

    }

    public Number getStartAddr() {
        return startAddr;
    }

    public Number getStopAddr() {
        return stopAddr;
    }

    public int getMinsize() {
        return minsize;
    }

    public int getMaxsize() {
        return maxsize;
    }

    public int getMaxBlocks() {
        return maxBlocks;
    }

    public float getSuperblockBranchThreshold() {
        return superblockBranchThreshold;
    }

    public Number getPrematureStopAddr() {
        return prematureStopAddr;
    }

    public Number getSkipToAddr() {
        return skipToAddr;
    }

    public String configString() {
        return "maxwindow" + this.maxsize;
    }

    /*
     * Builder class
     */
    public static class DetectorConfigurationBuilder extends DetectorConfiguration {

        public DetectorConfigurationBuilder() {

        }

        public DetectorConfigurationBuilder withStartAddr(Number startAddr) {
            this.startAddr = startAddr;
            return this;
        }

        public DetectorConfigurationBuilder withStopAddr(Number stopAddr) {
            this.stopAddr = stopAddr;
            return this;
        }

        public DetectorConfigurationBuilder withMinWindow(int minsize) {
            this.minsize = minsize;
            return this;
        }

        public DetectorConfigurationBuilder withMaxWindow(int maxsize) {
            this.maxsize = maxsize;
            return this;
        }

        public DetectorConfigurationBuilder withMaxBlocks(int maxBlocks) {
            this.maxBlocks = maxBlocks;
            return this;
        }

        public DetectorConfigurationBuilder withBranchThreshold(float superblockBranchThreshold) {
            this.superblockBranchThreshold = superblockBranchThreshold;
            return this;
        }

        public DetectorConfigurationBuilder withPrematureStopAddr(Number addr) {
            this.prematureStopAddr = addr;
            return this;
        }

        public DetectorConfigurationBuilder withSkipToAddr(Number addr) {
            this.skipToAddr = addr;
            return this;
        }

        public DetectorConfiguration build() {
            DetectorConfiguration config = new DetectorConfiguration();
            config.startAddr = this.startAddr;
            config.stopAddr = this.stopAddr;
            config.minsize = this.minsize;
            config.maxsize = this.maxsize;
            config.maxBlocks = this.maxBlocks;
            config.superblockBranchThreshold = this.superblockBranchThreshold;
            config.prematureStopAddr = this.prematureStopAddr;
            config.skipToAddr = this.skipToAddr;
            return config;
        }

        public static DetectorConfiguration defaultConfig() {
            return new DetectorConfigurationBuilder().build();
        }
    }
}
