/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */
 
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
