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

package pt.up.specs.cgra.structure;

import pt.up.specs.cgra.structure.interconnect.Interconnect;
import pt.up.specs.cgra.structure.mesh.Mesh;

public interface SpecsCGRA {

    /*
     * 
     */
    public Mesh getMesh();

    /*
     * Interconnect
     */
    public Interconnect getInterconnect();

    /*
     * Executes a single simulation tick (can be considered a clock cycle)
     */
    public boolean execute();

    /*
     * switch between one of X (max) available contexts
     */
    // public boolean switchContext();

    /*
     * Generate a visual representation of the current CGRA status
     * (Use JFreeChart or similar?)
     */
    public void visualize();

    // some kind of method that recieves an emitter class
    // which lowers this functional spec into chisel3 or hdl
    // along with whatever other artefecats are necessary
    // public IMplementation(?) toImplementation(Implementer hdlImplementer);
}
