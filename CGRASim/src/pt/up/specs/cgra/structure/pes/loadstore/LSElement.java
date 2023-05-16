package pt.up.specs.cgra.structure.pes.loadstore;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.specs.cgra.control.PEControl;
import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.dataypes.PEDataNull;
import pt.up.specs.cgra.dataypes.PEInteger;
import pt.up.specs.cgra.structure.pes.binary.BinaryProcessingElement;

public class LSElement extends BinaryProcessingElement implements PEControl<LSControlSetting> {

    private void debug(String str) {
        SpecsLogs.debug(LSElement.class.getSimpleName() + ": " + str);
    }

    /*
     * At the next "_execute" step, this operation will be executed
     */
    private LSControlSetting ctrl;
    private int cload, cstore; // contador para ler dados continuamente, guardar dados continuamente
    private int offset;

    public LSElement(int x) {
        super(1, 1);
        this.cload = 0;
        this.cstore = 0;
        this.offset = x;
        this.ctrl = LSControlSetting.NULL;
    }

    public LSElement() {
        this(0);
    }

    @Override
    public LSControlSetting getOperation() {
        return this.ctrl;
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

    private PEData load() {
        // get parent memory and load
        this.cload++;
        var addr = new PEInteger(this.getOperand(0).getValue().intValue() + this.getOffset());
        var data = this.getMesh().getCGRA().readMemory(addr);
        if (data != null) {
            this.debug(this.getAt() + "LOADed value" + data + "from addr " + addr);

        } else {
            data = new PEDataNull();
            this.debug(this.getAt() + "LOADed NULL from addr " + addr);
        }
        return data;
    }

    private PEData store() {
        // get parent memory and store
        this.cstore++;
        var addr = new PEInteger(this.getOperand(0).getValue().intValue() + this.getOffset());
        var data = this.getMesh().getCGRA().writeMemory(addr, this.getOperand(1));
        if (data != null) {
            this.debug(this.getAt() + "executed STORE succesfully");

        } else {
            data = new PEDataNull();
            System.out.printf("LS at %d %d STOREd NULL value \n", this.getX(), this.getY());
        }
        return data;
    }

    @Override
    protected PEData _execute() {
        if (ctrl == LSControlSetting.LOAD)
            return this.load();

        else if (ctrl == LSControlSetting.STORE)
            return this.store();

        // explode
        else
            throw new RuntimeException("LSElement: invalid control setting");
    }

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

    @Override
    public boolean setOperation(LSControlSetting ctrl) {
        this.ctrl = ctrl;
        this.debug("set control on " + this.getAt()
                + " for instruction " + this.getOperation().getName());
        return true;
    }

    /*
    public boolean setControl(int ctrl) {
    
        PEControlSetting ectrl = null;
    
        for (var e : LSControlSetting.values()) {
            ectrl = e;
            if (ctrl == ectrl.getValue()) {
                return this.setControl((LSControlSetting) ectrl);
            }
        }
        return false;
    
    }*/

    @Override
    protected LSElement getThis() {
        return this;
    }

    @Override
    public String toString() {
        return "LSU";
    }
}
