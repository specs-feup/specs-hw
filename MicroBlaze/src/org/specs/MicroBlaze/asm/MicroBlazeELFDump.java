package org.specs.MicroBlaze.asm;

import java.io.File;

import org.specs.MicroBlaze.stream.MicroBlazeStaticProvider;

import pt.up.fe.specs.binarytranslation.asm.ELFDump;

public class MicroBlazeELFDump extends ELFDump {

    public MicroBlazeELFDump(File elfname) {
        super(new MicroBlazeStaticProvider(elfname));
    }
}
