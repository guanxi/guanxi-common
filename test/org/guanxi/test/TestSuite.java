/**
 * 
 */
package org.guanxi.test;

import org.guanxi.test.common.BagTest;
import org.guanxi.test.common.ErrorTest;
import org.guanxi.test.common.GuanxiExceptionTest;
import org.guanxi.test.common.GuanxiPrincipalTest;
import org.guanxi.test.common.PodTest;
import org.guanxi.test.common.filters.GenericEncoderTest;
import org.guanxi.test.common.filters.ProperFilterWriterTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author matthew
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( { BagTest.class, 
	PodTest.class, 
	GuanxiExceptionTest.class, 
	GuanxiPrincipalTest.class, 
	ErrorTest.class, 
	ProperFilterWriterTest.class,
	GenericEncoderTest.class } )
public class TestSuite {

}
