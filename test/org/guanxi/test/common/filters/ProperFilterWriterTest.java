/**
 * 
 */
package org.guanxi.test.common.filters;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.guanxi.common.filters.ProperFilterWriterSubclass;
import org.guanxi.test.TestUtils;
import org.junit.Test;

/**
 * This tests the ProperFilterWriter class.
 * 
 * @author matthew
 *
 */
public class ProperFilterWriterTest {
    /**
     * This tests that the filter writer correctly writes to the underlying
     * stream.
     * @throws IOException 
     */
    @Test
    public void testStringWrites() throws IOException {
        StringWriter out;
        StringBuffer buffer;
        ProperFilterWriterSubclass writer;
        String current, result;
        int offset, length;
        
        out = new StringWriter();
        writer = new ProperFilterWriterSubclass(out);
        buffer = out.getBuffer();
        
        for ( int i = 0;i < 100;i++ ) {
            current = TestUtils.randomString(100);
            offset = Math.abs(TestUtils.random.nextInt()) % (current.length() - 1);
            length = Math.abs(TestUtils.random.nextInt()) % (current.length() - offset) + 1;
            // the (length - offset) + 1 is done so that the range of the length is between one character and the end of the string
            // consider the following:
            // offset = 0
            // RandomInt % 100 + 1 = RandomInt % 100 (0-99) + 1 (1-100)
            // offset = 99
            // RandomInt % (100 - 99) + 1 = RandomInt % 1 (0) + 1 (1)
            
            writer.write(current, offset, length);
            result = buffer.toString();
            
            assertEquals("Written string does not match expected", current.substring(offset, offset + length), result);
            buffer.delete(0, buffer.length());
        }
    }

    /**
     * This tests that the filter writer correctly writes to the underlying
     * stream.
     * @throws IOException 
     */
    @Test
    public void testCharBufferWrites() throws IOException {
        StringWriter out;
        StringBuffer buffer;
        ProperFilterWriterSubclass writer;
        String current, result;
        int offset, length;
        
        out = new StringWriter();
        writer = new ProperFilterWriterSubclass(out);
        buffer = out.getBuffer();
        
        for ( int i = 0;i < 100;i++ ) {
            current = TestUtils.randomString(100);
            offset = Math.abs(TestUtils.random.nextInt()) % (current.length() - 1);
            length = Math.abs(TestUtils.random.nextInt()) % (current.length() - offset) + 1;
            // the (length - offset) + 1 is done so that the range of the length is between one character and the end of the string
            // consider the following:
            // offset = 0
            // RandomInt % 100 + 1 = RandomInt % 100 (0-99) + 1 (1-100)
            // offset = 99
            // RandomInt % (100 - 99) + 1 = RandomInt % 1 (0) + 1 (1)
            
            writer.write(current.toCharArray(), offset, length);
            result = buffer.toString();
            
            assertEquals("Written string does not match expected", current.substring(offset, offset + length), result);
            buffer.delete(0, buffer.length());
        }
    }
    
    /**
     * This confirms that a string write with a null string throws a NullPointerException
     * @throws IOException 
     */
    @Test (expected = NullPointerException.class)
    public void testStringNullPointerException() throws IOException {
        ProperFilterWriterSubclass writer;
        
        writer = new ProperFilterWriterSubclass(new StringWriter());
        writer.write((String)null, 0, 100);
    }
    
    /**
     * This confirms that a char buffer write with a null string throws a NullPointerException
     * @throws IOException 
     */
    @Test (expected = NullPointerException.class)
    public void testCharBufferNullPointerException() throws IOException {
        ProperFilterWriterSubclass writer;
        
        writer = new ProperFilterWriterSubclass(new StringWriter());
        writer.write((char[])null, 0, 100);
    }
    
    /**
     * This confirms that a string write with a negative length produces
     * an IndexOutOfBoundsException
     * 
     * @throws IOException 
     */
    @Test (expected = IndexOutOfBoundsException.class)
    public void testStringIndexOutOfBoundsException1() throws IOException {
        ProperFilterWriterSubclass writer;
        
        writer = new ProperFilterWriterSubclass(new StringWriter());
        writer.write("hello", 0, -1);
    }
    
    /**
     * This confirms that a string write with a negative offset produces
     * an IndexOutOfBoundsException
     * 
     * @throws IOException 
     */
    @Test (expected = IndexOutOfBoundsException.class)
    public void testStringIndexOutOfBoundsException2() throws IOException {
        ProperFilterWriterSubclass writer;
        
        writer = new ProperFilterWriterSubclass(new StringWriter());
        writer.write("hello", -1, 0);
    }
    
    /**
     * This confirms that a string write where the offset plus length
     * is greater than the length of the string produces an
     * IndexOutOfBoundsException.
     * 
     * @throws IOException 
     */
    @Test (expected = IndexOutOfBoundsException.class)
    public void testStringIndexOutOfBoundsException3() throws IOException {
        ProperFilterWriterSubclass writer;
        String string;
        
        string = "hello";
        writer = new ProperFilterWriterSubclass(new StringWriter());
        writer.write(string, 0, string.length() + 1);
    }
    
    /**
     * This confirms that a char buffer write with a negative length produces
     * an IndexOutOfBoundsException
     * 
     * @throws IOException 
     */
    @Test (expected = IndexOutOfBoundsException.class)
    public void testCharBufferIndexOutOfBoundsException1() throws IOException {
        ProperFilterWriterSubclass writer;
        
        writer = new ProperFilterWriterSubclass(new StringWriter());
        writer.write("hello".toCharArray(), 0, -1);
    }
    
    /**
     * This confirms that a char buffer write with a negative offset produces
     * an IndexOutOfBoundsException
     * 
     * @throws IOException 
     */
    @Test (expected = IndexOutOfBoundsException.class)
    public void testCharBufferIndexOutOfBoundsException2() throws IOException {
        ProperFilterWriterSubclass writer;
        
        writer = new ProperFilterWriterSubclass(new StringWriter());
        writer.write("hello".toCharArray(), -1, 0);
    }
    
    /**
     * This confirms that a char buffer write where the offset plus length
     * is greater than the length of the char buffer produces an
     * IndexOutOfBoundsException.
     * 
     * @throws IOException 
     */
    @Test (expected = IndexOutOfBoundsException.class)
    public void testCharBufferIndexOutOfBoundsException3() throws IOException {
        ProperFilterWriterSubclass writer;
        String string;
        
        string = "hello";
        writer = new ProperFilterWriterSubclass(new StringWriter());
        writer.write(string.toCharArray(), 0, string.length() + 1);
    }
}
