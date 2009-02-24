/**
 * 
 */
package org.guanxi.test.common;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.guanxi.common.Bag;
import org.guanxi.common.Pod;
import org.guanxi.test.TestUtils;
import org.junit.Test;

/**
 * This tests the Pod, which holds details about the original request from the user.
 * 
 * @author matthew
 *
 */
public class PodTest {
    
    /**
     * This confirms that a newly created pod has no value which
     * is not null.
     */
    @Test
    public void testInitialValues() {
        Pod pod;
        
        pod = new Pod();
        assertEquals("New Pod contains Bag", null, pod.getBag());
        assertEquals("New Pod contains ServletContext", null, pod.getContext());
        assertEquals("New Pod contains Host Name", null, pod.getHostName());
        assertEquals("New Pod contains Request Parameters", null, pod.getRequestParameters());
        assertEquals("New Pod contains Request Scheme", null, pod.getRequestScheme());
        assertEquals("New Pod contains Request URL", null, pod.getRequestURL());
        assertEquals("New Pod contains Session ID", null, pod.getSessionID());
    }
    
    /**
     * This confirms that the Pod stores the values passed to it.
     * N.B. The request map is cloned. Validation of that must check
     * the keys and values.
     */
    @Test
    public void testInitializedValues() {
        Pod pod;
        Bag bag;
        ServletContext servletContext;
        String hostname, requestScheme, requestURL, sessionID;
        Map<String, String> requestParameters;
        
        pod = new Pod();
        
        // initialise
        bag               = new Bag();
        servletContext    = new ServletContextImpl();
        hostname          = TestUtils.randomString(100);
        requestScheme     = TestUtils.randomString(100);
        requestURL        = TestUtils.randomString(100);
        sessionID         = TestUtils.randomString(100);
        requestParameters = new TreeMap<String, String>();
        for (int i = 0;i < 100;i++) {
            requestParameters.put(TestUtils.randomString(100), TestUtils.randomString(100));
        }
        
        // set
        pod.setBag(bag);
        pod.setContext(servletContext);
        pod.setHostName(hostname);
        pod.setRequestParameters(requestParameters);
        pod.setRequestScheme(requestScheme);
        pod.setRequestURL(requestURL);
        pod.setSessionID(sessionID);
        
        // test
        assertEquals("Bag not set", bag, pod.getBag());
        assertEquals("ServletContext not set", servletContext, pod.getContext());
        assertEquals("Host Name not set", hostname, pod.getHostName());
        assertEquals("Request Scheme not set", requestScheme, pod.getRequestScheme());
        assertEquals("Request URL not set", requestURL, pod.getRequestURL());
        assertEquals("Session ID not set", sessionID, pod.getSessionID());
        
        TestUtils.assertMapEquals("Request Parameters from Pod differ to those that were set", pod.getRequestParameters(), requestParameters);
    }
    
    /**
     * This tests the fact that the request parameters are not cleared 
     * between invocations of setRequestParameters. This will test the
     * addition of new keys and the overwriting of old keys.
     */
    @Test
    public void testCumulativeRequestParameters() {
        Pod pod;
        Map<String, String> initialAdd, noOverwriteAdd, overwriteAdd, expected;
        
        initialAdd = new TreeMap<String, String>();
        noOverwriteAdd = new TreeMap<String, String>();
        overwriteAdd = new TreeMap<String, String>();
        
        for (int i = 0;i < 100;i++) {
            String key;
            
            do {
                key = TestUtils.randomString(100);
            }
            while (initialAdd.containsKey(key));
            
            initialAdd.put(key, TestUtils.randomString(100));
        }

        for (int i = 0;i < 100;i++) {
            String key;
            
            do {
                key = TestUtils.randomString(100);
            }
            while (initialAdd.containsKey(key));
            
            noOverwriteAdd.put(key, TestUtils.randomString(100));
        }
        
        for (String key : initialAdd.keySet()) {
            overwriteAdd.put(key, TestUtils.randomString(100));
        }
        
        pod = new Pod();
        expected = new TreeMap<String, String>();
        
        for (Map<String, String> current : new Map[]{ initialAdd, noOverwriteAdd, overwriteAdd } ) {
            pod.setRequestParameters(current);
            expected.putAll(current);
            TestUtils.assertMapEquals("Request Parameters from Pod differ to those that were set", expected, pod.getRequestParameters());
        }
    }
    
    /**
     * This is an empty ServletContext Implementation used to set the
     * ServletContext for the Pod.
     * 
     * @author matthew
     *
     */
    private static class ServletContextImpl implements ServletContext {

        public Object getAttribute(String name) {
            return null;
        }

        public Enumeration getAttributeNames() {
            return null;
        }

        public ServletContext getContext(String uripath) {
            return null;
        }

        public String getInitParameter(String name) {
            return null;
        }

        public Enumeration getInitParameterNames() {
            return null;
        }

        public int getMajorVersion() {
            return 0;
        }

        public String getMimeType(String file) {
            return null;
        }

        public int getMinorVersion() {
            return 0;
        }

        public RequestDispatcher getNamedDispatcher(String name) {
            return null;
        }

        public String getRealPath(String path) {
            return null;
        }

        public RequestDispatcher getRequestDispatcher(String path) {
            return null;
        }

        public URL getResource(String path) throws MalformedURLException {
            return null;
        }

        public InputStream getResourceAsStream(String path) {
            return null;
        }

        public Set getResourcePaths(String path) {
            return null;
        }

        public String getServerInfo() {
            return null;
        }

        public Servlet getServlet(String name) throws ServletException {
            return null;
        }

        public String getServletContextName() {
            return null;
        }

        public Enumeration getServletNames() {
            return null;
        }

        public Enumeration getServlets() {
            return null;
        }

        public void log(String msg) {
            
        }

        public void log(Exception exception, String msg) {
            
        }

        public void log(String message, Throwable throwable) {
            
        }

        public void removeAttribute(String name) {
            
        }

        public void setAttribute(String name, Object object) {
            
        }
    }
}
