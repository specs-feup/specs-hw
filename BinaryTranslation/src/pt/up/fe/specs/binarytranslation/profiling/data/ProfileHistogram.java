package pt.up.fe.specs.binarytranslation.profiling.data;

import java.util.HashMap;

import com.google.gson.annotations.Expose;

public class ProfileHistogram implements InstructionProfileResult {

    @Expose
    private HashMap<String, Integer> histogram;

    public ProfileHistogram() {
        this.histogram = new HashMap<String, Integer>();
    }

    public HashMap<String, Integer> getHistogram() {
        return histogram;
    }
}
