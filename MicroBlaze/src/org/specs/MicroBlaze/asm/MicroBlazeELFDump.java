package org.specs.MicroBlaze.asm;

import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;
import org.specs.MicroBlaze.stream.MicroBlazeStaticProducer;

import pt.up.fe.specs.binarytranslation.asm.ELFDump;

/**
 * Map holding an in memory dump of a MicroBlaze ELF; unique addresses are assumed
 * 
 * @author nuno
 *
 */
public class MicroBlazeELFDump extends ELFDump {

    public MicroBlazeELFDump(MicroBlazeApplication app) {
        super(new MicroBlazeStaticProducer(app));
    }

    public MicroBlazeELFDump(MicroBlazeELFProvider elfprovider) {
        this(elfprovider.toApplication());
    }
}
