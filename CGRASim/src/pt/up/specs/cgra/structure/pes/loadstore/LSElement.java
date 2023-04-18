package pt.up.specs.cgra.structure.pes.loadstore;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pt.up.specs.cgra.control.PEControlSetting;
import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.structure.pes.AProcessingElement;
import pt.up.specs.cgra.structure.pes.ProcessingElement;
import pt.up.specs.cgra.structure.pes.ProcessingElementPort;
import pt.up.specs.cgra.structure.pes.ProcessingElementPort.PEPortDirection;
import pt.up.specs.cgra.structure.pes.alu.ALUControlSetting;
import pt.up.specs.cgra.structure.pes.loadstore.LSControlSetting;


public class LSElement extends AProcessingElement {

	public LSElement() {
		super();
		this.ports = Arrays.asList(
				new ProcessingElementPort(this, PEPortDirection.output, null));

		this.mem_id = this.getMesh().getCGRA().assignLS(this);
		this.cload = 0;
		this.cstore = 0;

	}	

	/*interface LSOperation {
		PEData apply(LSElement a, int b);
	}

	private static final Map<LSControlSetting, LSOperation> LSOperations;
	static {
		Map<LSControlSetting, LSOperation> amap = new HashMap<LSControlSetting, LSOperation>();
		amap.put(LSControlSetting.LOAD, (a, b) -> a.load(b));
		amap.put(LSControlSetting.STORE, (a, b) -> a.store(b));

		LSOperations = Collections.unmodifiableMap(amap);
	}*/

	
	private final int mem_id;//id para o genericmemory
	private int cload, cstore; //contador para ler dados continuamente, guardar dados continuamente

	@Override
	protected PEData _execute() {
		switch (this.getControl().getName())
		{
		case "LOAD":
		case "STORE":
		case "NULL":
		default:
			return null;
		}
	}

	//le os dados na posicao idx do banco deste LS
	private PEData load(int idx) {
		return this.getRegisterFile().set(0, this.getMesh().getCGRA().read_liveins(this, idx));
	}

	//le os dados a partir da posicao 0 ate nao der mais
	private PEData load_next() {

		load(cload);
		cload++;
		return this.getRegisterFile().get(0);

	}

	//reset ao contador
	public boolean reset_counter()
	{
		cload = 0;
		cstore = 0;
		if (cload == 0 && cstore == 0) return true;
		else return false;
	}

	private PEData store(int idx) {

		if (this.getMesh().getCGRA().store_liveins(this, idx, this.getRegisterFile().get(0))) 
			return this.getRegisterFile().get(0);
		else return null;
	}

	private PEData store_next() {

		store(cstore);
		cstore++;
		return this.getRegisterFile().get(0);

	}

	public boolean setControl(LSControlSetting ctrl) {

		this.ctrl = ctrl;
		return true;
	}

	public boolean setControl(int ctrl) {

		PEControlSetting ectrl = null;

		for(var e: LSControlSetting.values()) {
			ectrl = e;
			if(ctrl == ectrl.getValue()) {
				return this.setControl((LSControlSetting) ectrl);
			}
		}
		return false;

	}

	public int getMem_id() {
		return mem_id;
	}

	@Override
	public LSControlSetting getControl() {
		return (LSControlSetting) this.ctrl;
	}


}
