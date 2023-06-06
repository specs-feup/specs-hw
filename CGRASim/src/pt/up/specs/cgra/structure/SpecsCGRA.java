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
import pt.up.specs.cgra.instructions_decoder.InstructionDecoder;
import pt.up.specs.cgra.structure.context.Context;
import pt.up.specs.cgra.structure.interconnect.Interconnect;
import pt.up.specs.cgra.structure.memory.GenericMemory;
import pt.up.specs.cgra.structure.mesh.Mesh;
import pt.up.specs.cgra.structure.pes.ProcessingElement;

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
     * Get local CGRA memory
     */
    public GenericMemory getMemory();

    /*
     * write data to localmemory addr
     */
    public PEData writeMemory(Integer waddr, PEData data);

    /*
     * read data from localmemory addr
     */
    public PEData readMemory(Integer raddr);

    /*
     * Executes a single simulation tick (can be considered a clock cycle)
     */
    public int execute();
    
    public boolean step();
    
    public boolean pause();

    public boolean reset();
    
    /*
     * Generate a visual representation of the current CGRA status
     * (Use JFreeChart or similar?)
     */
    public void visualize();

    public Context getContext(int i);

    /*
     * switch between one of X (max) available contexts
     */

    public boolean setContext(Context c);
    
    public int getExecuteCount();

    public boolean applyContext(Integer id);

    public boolean isExecuting();

    public void setExecuting(boolean isExecuting);
    
	public void resetExecuteCount();
	
	public boolean isExecute_terminated();
	
	public void setExecute_terminated(boolean execute_terminated);

    public ProcessingElement setPE(int x, int y, ProcessingElement pe);
    
	public InstructionDecoder getInstdec();

	public int getLiveinsSize();
	
	public int getLiveoutsSize();
	
	public int getLocalmemSize();


    /*public int assignLS(LSElement x);
    
    public PEData read_liveins(LSElement x, int i);
    
    public PEData read_liveins_specific(int x, int y);
    
    public boolean store_liveins(LSElement x, int i, PEData d);*/

    // some kind of method that recieves an emitter class
    // which lowers this functional spec into chisel3 or hdl
    // along with whatever other artefecats are necessary
    // public IMplementation(?) toImplementation(Implementer hdlImplementer);
}