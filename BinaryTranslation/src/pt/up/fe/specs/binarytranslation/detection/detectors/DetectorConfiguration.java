package pt.up.fe.specs.binarytranslation.detection.detectors;

public class DetectorConfiguration {

    // privar final List< ??? > prohibited types; ?

    /*
     * min and max size of windows 
     */
    private int minsize;
    private int maxsize;
    // private List<InstructionType>

    private DetectorConfiguration() {

    }

    public int getMaxsize() {
        return maxsize;
    }

    public int getMinsize() {
        return minsize;
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

        public DetectorConfiguration build() {
            DetectorConfiguration config = new DetectorConfiguration();
            config.minsize = this.minsize;
            config.maxsize = this.maxsize;
            return config;
        }

        public static DetectorConfiguration defaultConfig() {
            return new DetectorConfigurationBuilder().build();
        }
    }
}
