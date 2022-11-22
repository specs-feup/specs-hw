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

import java.util.List;

import pt.up.specs.cgra.dataypes.PEControlALU;
import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.structure.mesh.Mesh;

public interface ProcessingElement {

    /*
     * 
     */
    default public boolean setMesh(Mesh mesh) {
        return false;
    }

    /*
     * 
     */
    default public Mesh getMesh() {
        return null;
    }

    /*
     * 
     */
    default public boolean setX(int x) {
        return false;
    }

    /*
     * 
     */
    default public boolean setY(int y) {
        return false;
    }
    
    default public List<PEData> getRegisterFile() {
		return null;
	}

	default public void setRegisterFile(List<PEData> x) {
		
	}

   
    default public int getX() {
        return -1;
    }

   
    default public int getY() {
        return -1;
    }

    /*
     * Copy constructor
     */
    default public ProcessingElement copy() {
        return null;
    }

    
    default public int getLatency() {
        return 1;
    }

    
    default public boolean hasMemory() {
        return true;
    }

    
    default public int getMemorySize() {
        return 1;
    }

    
    default public boolean isReady() {
        return false;
    }

   
    default public boolean isExecuting() {
        return false;
    }

   
    default public int getExecuteCount() {
        return 0;
    }

    /*
     * opIndex:
     * 0: lhs
     * 1: rhs
     */
    // public boolean setOperand(int opIndex, PEData op);

    /*
     * Sets result register for the next operation to execute (this setting is/should be persistent)
     */
/*    default public boolean setResultRegister(int regIndex) {
        return false;
    }
*/
    /*
     * Implements ONE execution tick (can be considered as a clock cycle)
     * this does not guarantee that the return is valid, only when
     * isReady returns true can the result be considered valid
     */
    default public PEData execute() {
        return null;
    }

    /*
     * 
     */
    default public String statusString() {
        return "No status";
    }
    


    /*
     * JUST FOR TESTING (??)
     */
    public List<ProcessingElementPort> getPorts();

	default public PEControlALU getControl() {
			return null;
		}
	
	default public void setControl(PEControlALU control) {
		}


    // public getImplementation ??

    // TODO: processingElement builder pattern?
    // peBuilder.withDataType(...).withMemory(memSize).modelLantency(2)... etc?
    // peBuilder.withClass(AdderElement.class).with... ???
}
