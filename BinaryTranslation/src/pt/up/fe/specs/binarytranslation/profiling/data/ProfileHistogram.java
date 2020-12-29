package pt.up.fe.specs.binarytranslation.profiling.data;

import java.util.HashMap;

import com.google.gson.annotations.Expose;

public class ProfileHistogram implements InstructionProfileResult {

    @Expose
    private String applicationName = "noName";

    @Expose
    private Number profileTime = 0;

    @Expose
    private String histogramTypeName = "noTypeName";

    @Expose
    private HashMap<String, Integer> histogram;

    public ProfileHistogram(String applicationName, String histogramTypeName) {
        this.applicationName = applicationName;
        this.histogramTypeName = histogramTypeName;
        this.histogram = new HashMap<String, Integer>();
    }

    /*
     * quick naming hack
     */
    @Override
    public String getOutputFolderName() {
        return this.applicationName + "_" + this.histogramTypeName + "_" + this.hashCode();
    }

    public ProfileHistogram() {
        this.histogram = new HashMap<String, Integer>();
    }

    public HashMap<String, Integer> getHistogram() {
        return histogram;
    }

    public void setProfileTime(Number profileTime) {
        this.profileTime = profileTime;
    }
}
