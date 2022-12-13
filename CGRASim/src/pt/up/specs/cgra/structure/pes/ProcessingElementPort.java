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
 
package pt.up.specs.cgra.structure.pes;

import pt.up.specs.cgra.dataypes.PEData;

public class ProcessingElementPort {

    public enum PEPortDirection {
        input,
        output
    }

    // private String portID;
    private PEPortDirection dir;
    private ProcessingElement PE;
    private PEData payload;

    public ProcessingElementPort(ProcessingElement myParent, PEPortDirection dir, PEData payload) {
        this.PE = myParent;
        this.dir = dir;
        this.payload = payload;
    }

    public ProcessingElement getPE() {
        return PE;
    }

    public PEData getPayload() {
        return payload;
    }

    public PEData setPayload(PEData payload) {
        this.payload = payload;
        return this.payload;
    }

    public PEPortDirection getDir() {
        return dir;
    }
}
