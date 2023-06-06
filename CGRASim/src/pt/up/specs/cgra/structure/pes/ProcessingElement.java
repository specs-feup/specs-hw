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

import pt.up.specs.cgra.control.PEControlSetting;
import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.structure.mesh.Mesh;
import pt.up.specs.cgra.structure.pes.alu.ALUControlSetting;

public interface ProcessingElement {
	
    public List<ProcessingElementPort> getPorts();

    default public String statusString() {
        return "No status";
    }

    default public boolean setMesh(Mesh mesh) {
        return false;
    }

    default public Mesh getMesh() {
        return null;
    }

    default public List<PEData> getRegisterFile() {
        return null;
    }

    default public void setRegisterFile(List<PEData> x) {

    }

	public void clearRegisterFile();
    
    default public boolean setX(int x) { // sera que devia ter mesmo?
        return false;
    }

    default public boolean setY(int y) { // idem aspas
        return false;
    }

    default public int getX() {
        return -1;
    }

    default public int getY() {
        return -1;
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
   
    default public boolean isReady() { // is ready == ! is executing?
        return false;
    }

    default public boolean isExecuting() {
        return false;
    }

    default public int getExecuteCount() { // data token approach
        return 0;
    }

	default public PEData getOperand(int idx) {
		return null;
	}

    default public PEData execute() {
        return null;
    }

    default public String printStatus() {
		return null;
    }

    default public ProcessingElement copy() {
        return null;
    }

    default public boolean setControl(int i) {
    	return false;
    }
	
	default public int getnConnections() {
		return 0;
	}
	
	default public void setnConnections(int nConnections) {
	}
	
	public void setnConnections();
	
	default public boolean setReady() {
		return false;
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

    // public getImplementation ??

    // TODO: processingElement builder pattern?
    // peBuilder.withDataType(...).withMemory(memSize).modelLantency(2)... etc?
    // peBuilder.withClass(AdderElement.class).with... ???
}
