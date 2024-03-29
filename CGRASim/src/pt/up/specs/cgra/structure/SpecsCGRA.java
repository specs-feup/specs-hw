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

import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.dataypes.PEInteger;
import pt.up.specs.cgra.microcontroler.InstructionDecoder;
import pt.up.specs.cgra.structure.context.Context;
import pt.up.specs.cgra.structure.interconnect.Interconnect;
import pt.up.specs.cgra.structure.memory.GenericMemory;
import pt.up.specs.cgra.structure.mesh.Mesh;
import pt.up.specs.cgra.structure.pes.ProcessingElement;

public interface SpecsCGRA {

    /*
     * get entire Mesh of PEs
     */
    public Mesh getMesh();

    /*
     * get specific PE
     */
    public default ProcessingElement getPE(int x, int y) {
        return this.getMesh().getProcessingElement(x, y);
    }

    /*
     * Interconnect
     */
    public Interconnect getInterconnect();

    /*
     * Get local CGRA memory
     */
    public GenericMemory getMemory();

    /*
     * write data to memory adddr
     */
    public PEData writeMemory(PEInteger waddr, PEData data);

    /*
     * read data from memory addr
     */
    public PEData readMemory(PEInteger raddr);

    /*
     * Executes a single simulation tick (can be considered a clock cycle)
     */
    public int execute(); // TODO: return of this should go back
    // to "boolean".. it indicates if execute was sucessful or not

    /*
     * invokes the built-in decoder
     */
    public boolean execute(String instruction);

    public boolean pause();

    public Context getContext(int i);

    public boolean setContext(Context c);

    public boolean applyContext(Integer id);

    /*
     * switch between one of X (max) available contexts
     */
    // public boolean switchContext();

    /*
     * Generate a visual representation of the current CGRA status
     * (Use JFreeChart or similar?)
     */
    public void visualize();

    public int getExecuteCount();

    public boolean isExecuting();

    public void setExecuting(boolean isExecuting);

    public boolean step();

    public boolean reset();

    // public ProcessingElement setPE(int x, int y, ProcessingElement pe);

    public InstructionDecoder getInstructionDecoder();

    // some kind of method that recieves an emitter class
    // which lowers this functional spec into chisel3 or hdl
    // along with whatever other artefecats are necessary
    // public IMplementation(?) toImplementation(Implementer hdlImplementer);
}