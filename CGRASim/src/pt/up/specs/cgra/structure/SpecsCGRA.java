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
import pt.up.specs.cgra.structure.context.Context;
import pt.up.specs.cgra.structure.interconnect.Interconnect;
import pt.up.specs.cgra.structure.memory.GenericMemory;
import pt.up.specs.cgra.structure.mesh.Mesh;
import pt.up.specs.cgra.structure.pes.ProcessingElement;
import pt.up.specs.cgra.structure.pes.loadstore.LSElement;

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
    public int execute();
    
	public boolean pause();
    
    public Context getContext(int i);
    
    /*
     * switch between one of X (max) available contexts
     */
    // public boolean switchContext();

    /*
     * Generate a visual representation of the current CGRA status
     * (Use JFreeChart or similar?)
     */
    public void visualize();

	public boolean setContext(Context c);

	public boolean applyContext(Integer id);
	
	public int getExecuteCount();
	
	public boolean isExecuting();

	public void setExecuting(boolean isExecuting);

	public boolean step();

	public boolean reset();
	
	public ProcessingElement setPE(int x, int y, ProcessingElement pe);
	
	public int assignLS(LSElement x);
	
	public PEData read_liveins(LSElement x, int i);



    // some kind of method that recieves an emitter class
    // which lowers this functional spec into chisel3 or hdl
    // along with whatever other artefecats are necessary
    // public IMplementation(?) toImplementation(Implementer hdlImplementer);
}