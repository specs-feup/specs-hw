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
import pt.up.specs.cgra.structure.pes.loadstore.LSControlSetting;


public class LSElement extends AProcessingElement {

	public LSElement() {
		super();

		this.ports = Arrays.asList(
				new ProcessingElementPort(this, PEPortDirection.output, null),
				new ProcessingElementPort(this, PEPortDirection.output, null));

		this.mem_id = this.getMesh().getCGRA().assignLS(this);
	}	

	interface LSOperation {
		PEData operation(LSElement a, int b);
	}

	private static final Map<LSControlSetting, LSOperation> LSOperations;
	static {
		Map<LSControlSetting, LSOperation> amap = new HashMap<LSControlSetting, LSOperation>();
		amap.put(LSControlSetting.LOAD, (a, b) -> a.load(b));
		amap.put(LSControlSetting.STORE, (a, b) -> a.store(b));


		//amap.put(ALUControlSetting.NORM1, (a, b) -> a.mul(a) + b.mul(b));
		//amap.put(ALUControl.MOD, (a, b) -> a.mod(b));
		//amap.put(ALUControl.XOR, (a, b) -> a.xor(b));

		LSOperations = Collections.unmodifiableMap(amap);
	}

	/*
	 * At the next "_execute" step, this operation will be executed
	 */
	private int mem_id;

	@Override
	protected PEData _execute() {
        return LSOperations.get(this.ctrl).apply(this, this.getOperand(1));
		return null;
	}

	private PEData load(int idx) {
		return this.getMesh().getCGRA().read_liveins(this, idx);

	}


	private PEData store(int idx) {
		return null;

	}

	public PEControlSetting setControl(int i) {
		var ctrl_tmp = new LSControlSetting(i);
		this.ctrl = ctrl_tmp;
		return this.ctrl;
	}

	public int getMem_id() {
		return mem_id;
	}

	public void setMem_id(int mem_id) {
		this.mem_id = mem_id;
	}



}
