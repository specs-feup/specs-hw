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