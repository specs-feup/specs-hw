package org.specs.Arm.asm;

import org.specs.Arm.ArmApplication;
import org.specs.Arm.provider.ArmELFProvider;
import org.specs.Arm.stream.ArmStaticProducer;

import pt.up.fe.specs.binarytranslation.asm.ELFDump;

/**
 * Map holding an in memory dump of a ARM ELF; unique addresses are assumed
 * 
 * @author nuno
 *
 */
public class ArmELFDump extends ELFDump {

    public ArmELFDump(ArmApplication app) {
        super(new ArmStaticProducer(app));
    }

    public ArmELFDump(ArmELFProvider elfprovider) {
        this(elfprovider.toApplication());
    }
}
