package pt.up.fe.specs.binarytranslation.detection.detectors;

public class DetectorConfiguration {

    // privar final List< ??? > prohibited types; ?

    /*
     * min and max size of windows 
     */
    @Deprecated
    private int minsize;

    private int maxsize; // segment size that will be looked for
    private int maxBlocks; // max forward jumping or backward jumping links in superblocks and megablocks
    private float superblockBranchThreshold;
    // private List<InstructionType>

    private DetectorConfiguration() {

    }

    public int getMaxsize() {
        return maxsize;
    }

    public int getMinsize() {
        return minsize;
    }

    public int getMaxBlocks() {
        return maxBlocks;
    }

    public float getSuperblockBranchThreshold() {
        return superblockBranchThreshold;
    }

    /*
     * Builder class
     */
    public static class DetectorConfigurationBuilder {

        /*
         * min and max size of windows 
         */
        private int minsize = 2;
        private int maxsize = 3;
        private int maxBlocks = 2;
        private float superblockBranchThreshold;

        public DetectorConfigurationBuilder() {

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

        public DetectorConfiguration build() {
            DetectorConfiguration config = new DetectorConfiguration();
            config.minsize = this.minsize;
            config.maxsize = this.maxsize;
            config.maxBlocks = this.maxBlocks;
            config.superblockBranchThreshold = this.superblockBranchThreshold;
            return config;
        }

        public static DetectorConfiguration defaultConfig() {
            return new DetectorConfigurationBuilder().build();
        }
    }
}
