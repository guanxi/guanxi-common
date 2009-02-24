/**
 * 
 */
package org.guanxi.test.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.guanxi.common.Bag;
import org.guanxi.test.TestUtils;
import org.junit.Test;

/**
 * This will test the bag class from Guanxi Common.
 * 
 * @author matthew
 *
 */
public class BagTest {
    
    /**
     * This confirms that the bag is created without any attributes contained
     * in the hashmap.
     */
    @Test
    public void testInitiallyEmpty() {
        Bag bag;
        
        bag = new Bag();
        
        assertTrue("New Bag contains attributes", ! bag.hasAttributes());
        assertTrue("New Bag contains attributes", ! bag.getAttributeNames().hasMoreElements());
        assertEquals("New Bag contains SAML Response", null, bag.getSamlResponse());
    }
    
    /**
     * This is the reverse of the testInitiallyEmpty test.
     * This confirms that the two attribute content tests then
     * have the reverse value when the bag contains a single attribute.
     */
    @Test
    public void testWhenFilled() {
        Bag bag;
        
        bag = new Bag();
        bag.addAttribute("attribute", "value");
        
        assertTrue("Bag with attribute has no attributes", bag.hasAttributes());
        assertTrue("Bag with attribute has no attributes", bag.getAttributeNames().hasMoreElements());
    }
    
    /**
     * This tests the storage of the SAMLResponse
     */
    @Test
    public void testSAMLResponseStorage() {
        Bag bag;
        String current;
        
        bag = new Bag();
        for (int i = 0;i < 100;i++) {
            current = TestUtils.randomString(100);
            bag.setSamlResponse(current);
            
            assertEquals("Bag failed to store SAMLResponse", current, bag.getSamlResponse());
        }
    }
    
    /**
     * This tests that the bag correctly stores attributes that
     * do not already exist in the bag.
     */
    @Test
    public void testSingleAttributeStorage() {
        Bag bag;
        String key, value;
        
        bag = new Bag();
        for (int i = 0;i < 100;i++) {
            
            do {
                key = TestUtils.randomString(100);
            } while (bag.getAttributeValue(key) != null);
            value = TestUtils.randomString(100);
            
            bag.addAttribute(key, value);
            assertEquals("Bag failed to store Attribute", value, bag.getAttributeValue(key));
            assertTrue("Bag has not stored key", enumerationToCollection(bag.getAttributeNames()).contains(key));
        }
    }
    
    /**
     * This tests the ability of the bag to combine attributes and separate them using
     * the delimiter.
     */
    @Test
    public void testMultiAttributeStorage() {
        Bag bag;
        List<String> keys;
        String key, expectedValue, value;
        
        bag = new Bag();
        
        // this adds 100 random keys and values to the bag
        for (int i = 0;i < 100;i++) {
            do {
                key = TestUtils.randomString(100);
            } while (bag.getAttributeValue(key) != null);
            value = TestUtils.randomString(100);
            
            bag.addAttribute(key, value);
        }
        keys = enumerationToCollection(bag.getAttributeNames());
        
        // this now uses those random keys and adds to the value
        for (int i = 0;i < 1000;i++) {
            key = keys.get(Math.abs(TestUtils.random.nextInt()) % keys.size());
            
            value = TestUtils.randomString(100);
            expectedValue = bag.getAttributeValue(key) + Bag.ATTR_VALUE_DELIM + value;
            
            bag.addAttribute(key, value);
            assertEquals("Bag failed to store multi value Attribute", expectedValue, bag.getAttributeValue(key));
        }
    }
    
    /**
     * This transforms an enumeration into a collection.
     * Collections can be iterated over, also they have
     * contains(O) methods.
     * 
     * @param <T>
     * @param enumeration
     * @return
     */
    private static <T> List<T> enumerationToCollection(Enumeration<T> enumeration) {
        List<T> result;
        
        result = new ArrayList<T>();
        while (enumeration.hasMoreElements()) {
            result.add(enumeration.nextElement());
        }
        
        return result;
    }
}
