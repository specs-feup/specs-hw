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

package pt.up.specs.cgra.structure.interconnect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.dataypes.PEInteger;
import pt.up.specs.cgra.structure.SpecsCGRA;
import pt.up.specs.cgra.structure.context.Context;
import pt.up.specs.cgra.structure.pes.ProcessingElementPort;

/*
 * mapa de ports - map<PEport, list<PEports>>
 * fonte - destino(s) (lista)
 */

public abstract class AInterconnect implements Interconnect {

	// TODO: each interconnect class should only hold a set of permitted connection rules
	// the AInterconnect should implement all methods?

	// protected final RULES = ??? some kind of map of possible connections?

	private final SpecsCGRA myparent;
	private Map<ProcessingElementPort, List<ProcessingElementPort>> connections;

	public AInterconnect(SpecsCGRA myparent) {
		this.myparent = myparent;
		this.connections = new HashMap<ProcessingElementPort, List<ProcessingElementPort>>();
		//this.myparent.setContext(new Context(this.connections));
	}

	/**
	 * For every connection, copy the output payload into the input ports of each @ProcessingElement
	 */
	
	//TODO: Exceptions
	@Override
	public boolean propagate() {
		
		System.out.println("\n========== propagation step ==========");


		for (var drive : this.connections.keySet()) //para cada x, isto e, cada registo de saida de PE
		{
			List<PEData> reg = drive.getPE().getRegisterFile(); //copy the data from
			drive.setPayload(reg.get(0)); //regfile to output port, only 1st PEData for now

			var drivenList = this.connections.get(drive);

			for (var drivenPort : drivenList)
				drivenPort.setPayload(drive.getPayload().copy()); // copy the data element from output port to input ports of next PE
		
		drive.setPayload(new PEInteger(0));
		} 
		
		System.out.println("propagated succesfully");

		return true;
	}

	
	//TODO: VERIFICAR N. DE CONECCOES > N DE PORTS
	@Override
	public boolean setConnection(ProcessingElementPort from, ProcessingElementPort to) {

		if (!connectionValid(from, to))//ISTO SO VERIFICA DISTANCIA
		{
			System.out.printf("connection invalid between %d, %d and %d, %d \n", 
					from.getPE().getX(), from.getPE().getY(), to.getPE().getX(), to.getPE().getY());

			return false;
		}

		// get list for this driving port, i.e., "from"
		if (this.connections.containsKey(from)) {
			var drivenList = this.connections.get(from);
			if (!drivenList.contains(to))
				drivenList.add(to);
			
			from.getPE().setnConnections();
			
			System.out.println("new port to existing output port list");

		} else {
			var newList = new ArrayList<ProcessingElementPort>();
			newList.add(to);
			this.connections.put(from, newList);
			System.out.println("new port to new list element");

		}
		System.out.printf("Connection set between PE %d, %d and %d, %d \n", 
				from.getPE().getX(), from.getPE().getY(), to.getPE().getX(), to.getPE().getY());

		return true;
	}

	@Override
	public boolean applyContext(Context ctx) {
		var connections = ctx.getConnections();
		for (var driver : connections.keySet()) {
			for (var sink : connections.get(driver))
				if (!this.setConnection(driver, sink))
					return false;
			/*
			 * TODO: create exception classes to handle these errors
			 */
		}
		return true;
	}

	@Override
	public Context getContext() {
		return new Context(this.connections);
	}

	@Override
	public SpecsCGRA getCGRA() {
		return this.myparent;
	}

	@Override
	public ProcessingElementPort findDriver(ProcessingElementPort to) {

		ProcessingElementPort driver = null;
		for (var drive : this.connections.keySet()) {
			var drivenList = this.connections.get(drive);
			if (drivenList.contains(to)) {
				driver = drive;
				break;
			}
		}
		return driver;

	}
	
	public void clear() {
		this.connections.clear();
	}
	
	
}

