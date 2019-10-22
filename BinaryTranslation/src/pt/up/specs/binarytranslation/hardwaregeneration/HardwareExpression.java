package pt.up.specs.binarytranslation.hardwaregeneration;

/**
 * This interface represents an arithmetic expression which can be returned as a String in a specific HDL language (or
 * similar), (e.g., getExpression() outputs "assign rd = ra + rb;")
 * 
 * @author Nuno
 *
 */
public interface HardwareExpression {

    /*
     * Returns a String implementing this expression
     * in the language of this expression object (e.g. verilog) 
     */
    String getExpression();
}
