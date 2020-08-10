package pt.up.fe.specs.binarytranslation.hardware.generation.exception;

public abstract class HardwareGenerationException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -2780532778298069097L;

    public HardwareGenerationException(String error) {
        super(error);
    }
}
