/**
 * OBVTests.java  0.1  10 July 2017 9:26:25 PM
 *
 * Copyright © 2017-2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.volume.MultiVoteOBVTest;
import org.ikankechil.iota.indicators.volume.OBVDITest;
import org.ikankechil.iota.indicators.volume.OBVTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  OBVTest.class,
  OBVDITest.class,
  MultiVoteOBVTest.class,
})

/**
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class OBVTests {
  // holder for above annotations
}
