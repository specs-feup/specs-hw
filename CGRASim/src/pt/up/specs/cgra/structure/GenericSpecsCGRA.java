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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.specs.cgra.CGRAUtils;
import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.dataypes.PEInteger;
import pt.up.specs.cgra.microcontroler.InstructionDecoder;
import pt.up.specs.cgra.structure.context.Context;
import pt.up.specs.cgra.structure.interconnect.Interconnect;
import pt.up.specs.cgra.structure.interconnect.NearestNeighbour;
import pt.up.specs.cgra.structure.interconnect.ToroidalNNInterconnect;
import pt.up.specs.cgra.structure.memory.GenericMemory;
import pt.up.specs.cgra.structure.mesh.Mesh;
import pt.up.specs.cgra.structure.pes.NullPE;
import pt.up.specs.cgra.structure.pes.ProcessingElement;

public class GenericSpecsCGRA implements SpecsCGRA {

    // TODO: extend this class with CGRAs with global memories
    // and memory ports

    // TODO: list of contexts is required to switch connections
    // the switch operation can model the latency upon call

    /*
     * 
     */
    private static final Map<Class<? extends Interconnect>, Constructor<?>> INTCCONSTRUCTORS;
    static {

        var amap = new HashMap<Class<? extends Interconnect>, Constructor<?>>();
        try {
            amap.put(NearestNeighbour.class, NearestNeighbour.class.getConstructor(SpecsCGRA.class));
            amap.put(ToroidalNNInterconnect.class, ToroidalNNInterconnect.class.getConstructor(SpecsCGRA.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        INTCCONSTRUCTORS = Collections.unmodifiableMap(amap);
    }

    // string name?
    private final int localmemsize;
    protected final GenericMemory localmem;
    protected final GenericMemory liveins, liveouts;
    protected final Mesh mesh;
    protected final Interconnect interconnect;
    protected ArrayList<Context> context_memory;
    protected final InstructionDecoder instdec;

    protected int execute_count;
    protected int cycle_count;
    protected boolean isExecuting;
    protected boolean execute_terminated;

    private void debug(String str) {
        CGRAUtils.debug(GenericSpecsCGRA.class.getSimpleName(), str);
    }

    private GenericSpecsCGRA(List<List<ProcessingElement>> mesh,
            Class<? extends Interconnect> intclass,
            int localmemsize) {

        this.debug("Constructor started...");
        this.mesh = new Mesh(mesh, this);

        // in theory, should never fail
        try {
            this.interconnect = (Interconnect) INTCCONSTRUCTORS.get(intclass).newInstance(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.liveins = new GenericMemory(8);
        this.liveouts = new GenericMemory(8);
        this.localmemsize = localmemsize;
        this.instdec = new InstructionDecoder(this);
        if (this.localmemsize > 0)
            this.localmem = new GenericMemory(this.localmemsize);
        else
            this.localmem = null;

        this.debug("Constructor finished!");
    }

    /*
     * Only for use by builder child class
     */
    protected GenericSpecsCGRA() {
        this.liveins = null;
        this.liveouts = null;
        this.localmem = null;
        this.localmemsize = 0;
        this.mesh = null;
        this.interconnect = null;
        this.instdec = new InstructionDecoder(this);
        this.execute_count = 0;
        this.cycle_count = 0;
        this.isExecuting = false;
        this.execute_terminated = false;
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public Interconnect getInterconnect() {
        return this.interconnect;
    }

    @Override
    public GenericMemory getMemory() {
        return this.localmem;
    }

    @Override
    public PEData writeMemory(PEInteger waddr, PEData data) {
        this.localmem.write(waddr, data);
        return data;
    }

    @Override
    public PEData readMemory(PEInteger raddr) {
        return this.localmem.read(raddr);
    }

    @Override
    public boolean isExecuting() {
        return isExecuting;
    }

    @Override
    public void setExecuting(boolean isExecuting) {
        this.isExecuting = isExecuting;
    }

    public void resetExecuteCount() {
        this.execute_count = 0;
    }

    public boolean isExecute_terminated() {
        return execute_terminated;
    }

    public void setExecute_terminated(boolean execute_terminated) {
        this.execute_terminated = execute_terminated;
    }

    /*@Override
    public ProcessingElement setPE(int x, int y, ProcessingElement pe) {
        return this.mesh.setProcessingElement(x, y, pe);
    }*/

    @Override
    public InstructionDecoder getInstructionDecoder() {
        return instdec;
    }

    public int getLiveinsSize() {
        return this.liveins.getSize();
    }

    public int getLiveoutsSize() {
        return this.liveouts.getSize();
    }

    /*
     * executes a single simulation step (clock cycle)
     
    @Override
    public boolean execute() {
    
        // propagate data from interconnect settings
        this.interconnect.propagate();
    
        // execute compute
        this.mesh.execute();
    
        // update view
    
        return true; // eventually use this return to indicate stalling or something
    }*/

    @Override
    public boolean execute(String instruction) {
        return this.instdec.decode(instruction);
    }

    @Override
    public int execute() {

        this.setExecuting(true);

        do {
            // propagate data from interconnect settings
            if (!this.interconnect.propagate()) {
                this.setExecuting(false);
                this.setExecute_terminated(true);

                return execute_count;
            }

            // execute compute
            if (!this.mesh.execute()) {
                this.setExecuting(false);
                this.setExecute_terminated(true);

                return execute_count;
            }

            this.execute_count++;
            this.cycle_count++;

        } while (this.isExecuting && !this.execute_terminated);

        this.setExecuting(false);
        this.setExecute_terminated(true);

        var tmp = this.execute_count;
        this.resetExecuteCount();
        this.setExecute_terminated(false);

        return tmp;
    }

    @Override
    public boolean step() {

        this.setExecuting(true);

        // execute compute
        if (!this.mesh.execute()) {
            this.setExecuting(false);
            return false;
        }

        // propagate data from interconnect settings
        if (!this.interconnect.propagate()) {
            this.setExecuting(false);
            return false;
        }

        this.execute_count++;

        this.setExecuting(false);
        return true;
    }

    @Override
    public boolean pause() {
        this.setExecuting(false);
        return true;
    }

    @Override
    public Context getContext(int i) {
        var x = this.context_memory.get(i);
        if (x != null)
            this.debug("Context nr. " + i + " retrieved successfuly");
        else
            this.debug("Context fetch failed...");
        return x;
    }

    @Override
    public boolean setContext(Context c) {
        if (c != null && c.getConnections().size() > 0) {
            if (this.context_memory.add(c)) {
                this.debug("Context added");
                return true;
            }
        }
        this.debug("Context add failed...");
        return false;
    }

    @Override
    public int getExecuteCount() {
        return this.execute_count;
    }

    @Override
    public boolean applyContext(Integer i) {
        if (this.context_memory.get(i) != null) {
            if (this.interconnect.applyContext(this.context_memory.get(i))) {
                this.debug("\"Context set succesfully");
                return true;
            }
        }

        this.debug("Setting context failed");
        return false;
    }

    @Override
    public boolean reset() {
        this.debug("Resetting CGRA");
        this.mesh.reset();
        this.interconnect.clear();
        this.debug("Reset complete");
        return true;
    }

    /*
     * 
     */
    public void setLiveIn(int idx, PEData data) {
        this.liveins.write(idx, data);
    }

    /*
     * 
     */
    public PEData getLiveOut(int idx) {
        return this.liveouts.read(idx);
    }

    // TODO: use a graphical representation later
    @Override
    public void visualize() {
        // TODO:visualize register files etc
        var sbld = new StringBuilder();
        System.out.println(sbld.append(this.mesh.visualize()).toString());
    }

    /****************************************************************************************
     * Builder class for @GenericSpecsCGRA
     */
    public static class Builder extends GenericSpecsCGRA {

        private int meshX, meshY;
        private int memsize;
        private final List<List<ProcessingElement>> mesh;
        private Class<? extends Interconnect> intclass = NearestNeighbour.class;
        // default interconnect

        private void debug(String str) {
            CGRAUtils.debug(GenericSpecsCGRA.Builder.class.getSimpleName(), str);
        }

        /*
         * mesh size is mandatory before any "with..." calls
         */
        public Builder(int x, int y) {
            this.debug("Starting CGRA Builder, with (x, y) = (" + x + ", " + y + ")");
            this.meshX = x;
            this.meshY = y;
            this.memsize = 0;
            this.mesh = new ArrayList<List<ProcessingElement>>(x);
            for (int i = 0; i < x; i++) {
                this.mesh.add(new ArrayList<ProcessingElement>(y));
                for (int j = 0; j < y; j++) {
                    this.mesh.get(i).add(new NullPE());
                }
            }
        }

        /*
         * "pe" must be copied for each grid position by deep copy
         */
        public Builder withHomogeneousPE(ProcessingElement pe) {
            this.debug("Setting PEs to homogeneous, with type " + pe.getClass().getSimpleName());
            for (int i = 0; i < this.meshX; i++)
                for (int j = 0; j < this.meshY; j++)
                    this.mesh.get(i).set(j, pe.copy());
            return this;
        }

        public Builder withProcessingElement(ProcessingElement pe, int x, int y) {
            this.debug("Started build PE of type "
                    + pe.toString() + " at (x, y) = (" + x + ", " + y + ")");
            this.mesh.get(x).set(y, pe);
            return this;
        }

        public Builder withMemory(int memsize) {
            this.debug("Setting localmem to size " + memsize);
            this.memsize = memsize;
            return this;
        }

        public Builder withNearestNeighbourInterconnect() {
            this.debug("Setting mesh to NearestNeighbour");
            this.intclass = NearestNeighbour.class;
            return this;
        }

        public Builder withToroidalNNInterconnect() {
            this.debug("Setting mesh to ToroidalNearestNeighbour");
            this.intclass = ToroidalNNInterconnect.class;
            return this;
        }

        public GenericSpecsCGRA build() {
            this.debug("Building CGRA...");
            return new GenericSpecsCGRA(this.mesh, this.intclass, memsize);
        }
    }
}