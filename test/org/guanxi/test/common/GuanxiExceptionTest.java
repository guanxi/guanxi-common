/**
 * 
 */
package org.guanxi.test.common;

import static org.junit.Assert.assertEquals;

import org.guanxi.common.GuanxiException;
import org.guanxi.test.TestUtils;
import org.junit.Test;

/**
 * This will test the Guanxi Exception class from Guanxi Common.
 * 
 * @author matthew
 *
 */
public class GuanxiExceptionTest {
    
    /**
     * This tests that the empty constructor creates an 
     * exception object with no associated message, exception
     * or error code.
     */
    @Test
    public void testEmptyConstructor() {
        GuanxiException exception;
        
        exception = new GuanxiException();
        
        assertEquals("GuanxiException empty constructor creates exception with causing exception", null, exception.getCause());
        assertEquals("GuanxiException empty constructor creates exception with message", null, exception.getMessage());
        assertEquals("GuanxiException empty constructor creates exception with error code", 0, exception.getErrorCode());
    }
    
    /**
     * This tests that the causing exception constructor
     * creates an exception object where the cause exception
     * is the same exception that was passed to the constructor.
     * This also confirms that no error code is set and that any
     * message in the causing error is reflected in the message
     * provided by the GuanxiException.
     */
    @Test
    public void testExceptionConstructor() {
        GuanxiException exception;
        String causeMessage;
        
        for ( Exception cause : new Exception[]{
            new Exception("Cause"),
            new Exception(),
            new Exception(new RuntimeException()),
            new Exception(new Throwable()),
            new NullPointerException(),
            new RuntimeException(),
        }) {
            exception = new GuanxiException(cause);
            
            // the message of the exception is the class of the exception that contains the message followed by the message if it exists
            causeMessage = cause.getClass().getName();
            if ( cause.getMessage() != null ) {
                causeMessage += ": " + cause.getMessage();
            }
            
            assertEquals("GuanxiException exception constructor alters causing exception", cause, exception.getCause());
            assertEquals("GuanxiException exception constructor alters causing exception message", causeMessage, exception.getMessage());
            
            assertEquals("GuanxiException exception constructor creates exception with error code", 0, exception.getErrorCode());
        }
        
        for (int i = 0;i < 100;i++) {
            Exception cause;
            
            cause = new Exception(TestUtils.randomString(100));
            exception = new GuanxiException(cause);

            // the message of the exception is the class of the exception that contains the message followed by the message if it exists
            causeMessage = cause.getClass().getName();
            if ( cause.getMessage() != null ) {
                causeMessage += ": " + cause.getMessage();
            }
            
            assertEquals("GuanxiException exception constructor alters causing exception", cause, exception.getCause());
            assertEquals("GuanxiException exception constructor alters causing exception message", causeMessage, exception.getMessage());
            
            assertEquals("GuanxiException exception constructor creates exception with error code", 0, exception.getErrorCode());
        }
    }
    
    /**
     * This tests that the message passed to the string constructor
     * is used as the message. This also tests that no cause exception
     * and no error code is set.
     */
    @Test
    public void testStringConstructor() {
        GuanxiException exception;
        String message;
        
        for (int i = 0;i < 100;i++) {
            message = TestUtils.randomString(100);
            exception = new GuanxiException(message);
            
            assertEquals("GuanxiException string constructor alters the message", message, exception.getMessage());
            
            assertEquals("GuanxiException string constructor creates exception with causing exception", null, exception.getCause());
            assertEquals("GuanxiException string constructor creates exception with error code", 0, exception.getErrorCode());
        }
    }
    
    /**
     * This tests that setting the error code is honoured.
     */
    @Test
    public void testErrorCode() {
        GuanxiException exception;
        int errorCode;
        
        for (int i = 0;i < 100;i++) {
            errorCode = TestUtils.random.nextInt();
            exception = new GuanxiException();

            assertEquals("GuanxiException empty constructor alters the error code", 0, exception.getErrorCode());
            
            exception.setErrorCode(errorCode);
            assertEquals("GuanxiException setErrorCode(int) does not honour the error code", errorCode, exception.getErrorCode());
        }
    }
}
