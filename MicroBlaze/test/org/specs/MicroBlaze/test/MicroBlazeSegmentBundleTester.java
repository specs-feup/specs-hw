/**
 * Copyright 2020 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package org.specs.MicroBlaze.test;

import java.io.*;

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.binarysegments.detection.SegmentBundle;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeSegmentBundleTester {

    @Test
    public void testDeserializeBundle() {

        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/bundles/cholesky.txt_microblaze32_20200508.bundle");
        fd.deleteOnExit();

        SegmentBundle bund = null;
        try {
            bund = SegmentBundle.serializeFromFile(fd.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(bund.getSummary());
    }

    @Test
    public void testBundleGetPredicate() {

        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/bundles/cholesky.txt_microblaze32_20200508.bundle");
        fd.deleteOnExit();

        SegmentBundle bund = null;
        try {
            bund = SegmentBundle.serializeFromFile(fd.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(bund.getSegments().size());

        var segs = bund.getSegments(data -> data.getSegmentLength() > 10 && data.getContexts().size() > 2);
        System.out.println(segs.size());
    }
}
