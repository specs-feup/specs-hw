package pt.up.specs.cgra.structure.pes.loadstore;

import pt.up.specs.cgra.control.PEControlSetting;
import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.dataypes.PEInteger;
import pt.up.specs.cgra.structure.pes.alu.ALUControlSetting;
import pt.up.specs.cgra.structure.pes.binary.BinaryProcessingElement;

public class LSElement extends BinaryProcessingElement {

	private int cload, cstore; // contador para ler dados continuamente, guardar dados continuamente
	private int offset;

	public LSElement(int x) {
		super(1, 1);

		this.cload = 0;
		this.cstore = 0;
		this.offset = x;

	}

	public LSElement() {
		this(0);

	}

	@Override
	protected PEData _execute() {

		if (!(this.getOperand(0) instanceof PEInteger)) {
			throw new RuntimeException("LSElement: non integer addr passed as argument");
		}

		var ctrl = (LSControlSetting) this.ctrl;
		var addr = new PEInteger(this.getOperand(0).getValue().intValue() + offset);
		var data = this.getOperand(1);
		var cgra = this.getMesh().getCGRA();

		// get parent memory and load
		if (ctrl == LSControlSetting.LOAD) {
			this.cload++;
			var a = cgra.readMemory(addr); // "data", second argument, is not used here
			if (a != null)
			{
				System.out.printf("LS at %d %d LOADed succesfully value %d from address %d (%d + offset %d) \n", 
						this.getX(), this.getY(), a.getValue().intValue(), addr.getValue().intValue(), 
						this.getOperand(0).getValue().intValue(), this.offset);
				return a;
			}

			else if (a == null)
			{
				System.out.printf("LS at %d %d LOADed NULL value from address %d (%d + offset %d) \n", 
						this.getX(), this.getY(), addr.getValue().intValue(),
						this.getOperand(0).getValue().intValue(), this.offset);
				
				return new PEInteger(0);
			}

		}

		// get parent memory and store
		else if (ctrl == LSControlSetting.STORE) {
			this.cstore++;
			var a = cgra.writeMemory(addr, data);
			if (a != null)
			{
				System.out.printf("LS at %d %d executed STORE succesfully \n", this.getX(), this.getY());
				return a;
			}
			System.out.printf("LS at %d %d STOREd NULL value \n", this.getX(), this.getY());
			return a;
		}

		// explode
		else
			throw new RuntimeException("LSElement: invalid control setting");
		return null;
	}

	/*
    // le os dados na posicao idx do banco deste LS
    private PEData load(int idx) {
        return this.getRegisterFile().set(0, this.getMesh().getCGRA().read_liveins(this, idx));
    }

    // le os dados a partir da posicao 0 ate nao der mais
    private PEData load_next() {

        load(cload);
        cload++;
        return this.getRegisterFile().get(0);

    }*/

	// reset ao contador
	public boolean reset_counter() {
		cload = 0;
		cstore = 0;
		if (cload == 0 && cstore == 0)
			return true;
		else
			return false;
	}
	/*
    private PEData store(int idx) {

        if (this.getMesh().getCGRA().store_liveins(this, idx, this.getRegisterFile().get(0)))
            return this.getRegisterFile().get(0);
        else
            return null;
    }

    private PEData store_next() {

        store(cstore);
        cstore++;
        return this.getRegisterFile().get(0);

    }*/

	public boolean setControl(LSControlSetting ctrl) {

		this.ctrl = ctrl;

		System.out.printf("set control on PE %d %d for instruction: %s \n", this.getX(), this.getY(), this.getControl().getName());

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

	/*public int getMem_id() {
        return mem_id;
    }*/

	@Override
	public LSControlSetting getControl() {
		return (LSControlSetting) this.ctrl;
	}

	@Override
	public String toString() {
		return "LSU";
	}

	public int getCload() {
		return cload;
	}

	public void setCload(int cload) {
		this.cload = cload;
	}

	public int getCstore() {
		return cstore;
	}

	public void setCstore(int cstore) {
		this.cstore = cstore;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

}
