package pt.up.specs.cgra.structure.pes.loadstore;

import pt.up.specs.cgra.control.PEControlSetting;

public enum LSControlSetting implements PEControlSetting {

	PASSNULL(0b0000), // name e value para o construtor
	LOAD(0b0001),
	STORE(0b0010);

	private String name;
    private int value;

    private LSControlSetting(int value) {
        this.value = value;
        this.name = name();
    }
    
    private LSControlSetting() {
        this.value = 0;
        this.name = name();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int getValue() {
        return this.value;
    }
}
