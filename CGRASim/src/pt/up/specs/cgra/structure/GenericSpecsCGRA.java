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

import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.instructions_decoder.InstructionDecoder;
import pt.up.specs.cgra.structure.context.Context;
import pt.up.specs.cgra.structure.interconnect.Interconnect;
import pt.up.specs.cgra.structure.interconnect.NearestNeighbour;
import pt.up.specs.cgra.structure.interconnect.ToroidalNNInterconnect;
import pt.up.specs.cgra.structure.memory.GenericMemory;
import pt.up.specs.cgra.structure.mesh.Mesh;
import pt.up.specs.cgra.structure.pes.EmptyPE;
import pt.up.specs.cgra.structure.pes.ProcessingElement;
import pt.up.specs.cgra.structure.pes.loadstore.LSElement;

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
	protected final List<GenericMemory> liveins, liveouts;
	protected final Mesh mesh;
	protected final Interconnect interconnect;
	protected ArrayList<Context> context_memory;
	protected final InstructionDecoder instdec;
	protected int execute_count;
	protected int cycle_count;
	protected boolean isExecuting;
	protected boolean execute_terminated;


	private GenericSpecsCGRA(List<List<ProcessingElement>> mesh,
			Class<? extends Interconnect> intclass,
			int localmemsize) {
		this.mesh = new Mesh(mesh, this);

		// in theory, should never fail
		try {
			this.interconnect = (Interconnect) INTCCONSTRUCTORS.get(intclass).newInstance(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		this.liveins = new ArrayList<GenericMemory>();
		this.liveouts = new ArrayList<GenericMemory>();
		this.localmemsize = localmemsize;
		this.instdec = new InstructionDecoder(this);
		if (this.localmemsize > 0)
			this.localmem = new GenericMemory(this.localmemsize);
		else
			this.localmem = null;
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
	public int execute() {

		int tmp;

		this.setExecuting(true);

		do {
			// propagate data from interconnect settings
			if (!this.interconnect.propagate()) 
			{
				this.setExecuting(false);
				this.setExecute_terminated(true);

				return execute_count;
			}

			// execute compute
			if (!this.mesh.execute()) 
			{
				this.setExecuting(false);
				this.setExecute_terminated(true);

				return execute_count;
			}


			this.execute_count++;
			this.cycle_count++;

		} while (this.isExecuting && !this.execute_terminated);
		//} while (CONDICAO);


		this.setExecuting(false);
		this.setExecute_terminated(true);

		tmp = this.execute_count;
		this.resetExecuteCount();
		this.setExecute_terminated(false);

		return tmp;
	}

	@Override
	public boolean step() {

		this.setExecuting(true);

		// propagate data from interconnect settings
		if (!this.interconnect.propagate()) 
		{
			this.setExecuting(false);
			return false;
		}

		// execute compute
		if (!this.mesh.execute()) 
		{
			this.setExecuting(false);
			return false;
		}

		this.execute_count++;

		this.setExecuting(false);
		return true;
	}

	public boolean pause()
	{
		this.setExecuting(false);
		return true;
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

		/*
		 * mesh size is mandatory before any "with..." calls
		 */
		public Builder(int x, int y) {
			this.meshX = x;
			this.meshY = y;
			this.memsize = 0;
			this.mesh = new ArrayList<List<ProcessingElement>>(x);
			for (int i = 0; i < x; i++) {
				this.mesh.add(new ArrayList<ProcessingElement>(y));
				for (int j = 0; j < y; j++) {
					this.mesh.get(i).add(new EmptyPE());
				}
			}
		}

		/*
		 * "pe" must be copied for each grid position by deep copy
		 */
		public Builder withHomogeneousPE(ProcessingElement pe) {
			for (int i = 0; i < this.meshX; i++)
				for (int j = 0; j < this.meshY; j++)
					this.mesh.get(i).set(j, pe.copy());
			return this;
		}

		public Builder withProcessingElement(ProcessingElement pe, int x, int y) {
			this.mesh.get(x).set(y, pe);
			return this;
		}

		public Builder withMemory(int memsize) {
			this.memsize = memsize;
			return this;
		}

		public Builder withNearestNeighbourInterconnect() {
			this.intclass = NearestNeighbour.class;
			return this;
		}

		public Builder withToroidalNNInterconnect() {
			this.intclass = ToroidalNNInterconnect.class;
			return this;
		}

		public GenericSpecsCGRA build() {
			return new GenericSpecsCGRA(this.mesh, this.intclass, memsize);
		}
	}

	@Override
	public Context getContext(int i) {
		return this.context_memory.get(i);
	}

	@Override
	public boolean setContext(Context c) {

		if(c != null && c.getConnections().size() > 0) {
			return this.context_memory.add(c);
		}

		else return false;

	}

	@Override
	public int getExecuteCount() {
		return this.execute_count;
	}

	public boolean applyContext(Integer i) {
		if(this.context_memory.get(i) != null) {
			return this.interconnect.applyContext(this.context_memory.get(i));
		}

		else return false;

	}

	public boolean isExecuting() {
		return isExecuting;
	}

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

	@Override
	public boolean reset() {
		this.mesh.reset();
		this.interconnect.clear();
		return true;
	}

	public ProcessingElement setPE(int x, int y, ProcessingElement pe) {
		return this.mesh.setProcessingElement(x, y, pe);
	}

	//cria um banco de memoria exclusivo para um load store
	public int assignLS(LSElement x)
	{
		if (x.getMem_id() == 0)
		{
			this.liveins.add(new GenericMemory(8));
			return this.liveins.size();
		}
		return 0;
	}

	//le um objeto no banco de um LS "x" na posicao i
	public PEData read_liveins(LSElement x, int i)
	{
		if (i <= this.liveins.get(x.getMem_id()).getSize()) return this.liveins.get(x.getMem_id()).read(i);
		else return null;
	}

	//le um objeto no banco x na posicao i
	public PEData read_liveins_specific(int x, int y)
	{
		return this.liveins.get(x).read(y);
	}

	//guarda um objeto "d" no banco de um LS "x" na posicao "i"
	public boolean store_liveins(LSElement x, int i, PEData d)
	{
		GenericMemory a = this.liveins.get(x.getMem_id());
		if (a != null) {
			if (!a.write(i, d)) return false;
			if (this.liveins.set(x.getMem_id(), a) != null) return true;
			else return false;
		}
		return false;

	}
}