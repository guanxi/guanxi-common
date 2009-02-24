/**
 * 
 */
package org.guanxi.test.common;

import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.TreeSet;

import org.guanxi.common.Errors;
import org.guanxi.test.TestUtils;
import org.junit.Test;

/**
 * This will test the errors class from Guanxi Common.
 * 
 * @author matthew
 *
 */
public class ErrorTest {
    /**
     * This checks that the Errors class correctly returns values
     * for the errors defined within it.
     */
    @Test
    public void knownErrorTest() {
        for ( String current : new String[]{
            Errors.ENGINE_WAYF_LOCATION_EXCEPTION,
            Errors.ENGINE_WAYF_LOCATION_NO_GUARD_ID,
            Errors.ENGINE_WAYF_LOCATION_GUARD_FAILED_VERIFICATION,
            Errors.ENGINE_CURRENTLY_INITIALISING,
        }) {
            assertTrue("Errors.isError does not return true for error text " + current, Errors.isError(current));
        }
          
        for ( String current : new String[]{
                Errors.GUARD_CERT_PROBE_FAILED,
                Errors.MISSING_PARAM,
        }) {
            assertTrue("Errors.isError does not return false for non error text " + current, ! Errors.isError(current));
        }
    }
    
    /**
     * This checks that the Errors class correctly returns false
     * for randomly generated strings.
     */
    @Test
    public void randomErrorTest() {
        Set<String> realStrings;
        String current;
        
        realStrings = new TreeSet<String>();
        realStrings.add(Errors.ENGINE_CURRENTLY_INITIALISING);
        realStrings.add(Errors.ENGINE_WAYF_LOCATION_EXCEPTION);
        realStrings.add(Errors.ENGINE_WAYF_LOCATION_GUARD_FAILED_VERIFICATION);
        realStrings.add(Errors.ENGINE_WAYF_LOCATION_NO_GUARD_ID);
        realStrings.add(Errors.GUARD_CERT_PROBE_FAILED);
        realStrings.add(Errors.MISSING_PARAM);
        
        for ( int i = 0;i < 100;i++ ) {
            do {
                current = TestUtils.randomString((Math.abs(TestUtils.random.nextInt()) % 100) + 1);
            } while ( realStrings.contains(current) );
            
            assertTrue("Errors.isError does not return false for the random text " + current, ! Errors.isError(current));
        }
    }
    
    /**
     * This checks that the Errors class correctly returns values
     * for the errors defined within it. This test creates an instance
     * of the Errors class to test that it returns the same values.
     */
    @Test
    public void instanciatedKnownErrorTest() {
        Errors instance;
        
        instance = new Errors();
        
        for ( String current : new String[]{
            Errors.ENGINE_WAYF_LOCATION_EXCEPTION,
            Errors.ENGINE_WAYF_LOCATION_NO_GUARD_ID,
            Errors.ENGINE_WAYF_LOCATION_GUARD_FAILED_VERIFICATION,
            Errors.ENGINE_CURRENTLY_INITIALISING,
        }) {
            assertTrue("Errors.isError does not return true for error text " + current, instance.isError(current));
        }
          
        for ( String current : new String[]{
                Errors.GUARD_CERT_PROBE_FAILED,
                Errors.MISSING_PARAM,
        }) {
            assertTrue("Errors.isError does not return false for non error text " + current, ! instance.isError(current));
        }
    }
    
    /**
     * This checks that the Errors class correctly returns false
     * for randomly generated strings. This test creates an instance
     * of the Errors class to test that it returns the same values.
     */
    @Test
    public void instanciatedRandomErrorTest() {
        Errors instance;
        Set<String> realStrings;
        String current;
        
        realStrings = new TreeSet<String>();
        realStrings.add(Errors.ENGINE_CURRENTLY_INITIALISING);
        realStrings.add(Errors.ENGINE_WAYF_LOCATION_EXCEPTION);
        realStrings.add(Errors.ENGINE_WAYF_LOCATION_GUARD_FAILED_VERIFICATION);
        realStrings.add(Errors.ENGINE_WAYF_LOCATION_NO_GUARD_ID);
        realStrings.add(Errors.GUARD_CERT_PROBE_FAILED);
        realStrings.add(Errors.MISSING_PARAM);
        
        instance = new Errors();
        
        for ( int i = 0;i < 100;i++ ) {
            do {
                current = TestUtils.randomString((Math.abs(TestUtils.random.nextInt()) % 100) + 1);
            } while ( realStrings.contains(current) );
            
            assertTrue("Errors.isError does not return false for the random text " + current, ! instance.isError(current));
        }
    }
}
