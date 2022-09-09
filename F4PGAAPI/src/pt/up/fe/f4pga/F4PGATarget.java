/**
 * Copyright 2022 SPeCS.
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

package pt.up.fe.f4pga;

/**
 * Represents a target FPGA board/devkit (including board name, part name, etc)
 * 
 * @author nuno
 *
 */
public enum F4PGATarget {

    // Arty100T("Arty100T", "Artix-7", ?),
    Arty100T("Arty35T", "Artix-7", "xc7a35tcsg324-1"),
    Basys3("Basys 3", "Artix-7", "XC7A35T-1CPG236C");

    String devkitName;
    String partName;
    String fpgaFamily;

    private F4PGATarget(String devkitName, String fpgaFamily, String partName) {
        this.devkitName = devkitName;
        this.fpgaFamily = fpgaFamily;
        this.partName = partName;
    }

    public String getDevkitName() {
        return devkitName;
    }

    public String getFpgaFamily() {
        return fpgaFamily;
    }

    public String getPartName() {
        return partName;
    }
}
