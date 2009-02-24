/**
 * 
 */
package org.guanxi.test.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.TreeMap;

import org.guanxi.common.GuanxiPrincipal;
import org.guanxi.test.TestUtils;
import org.guanxi.xal.idp.Creds;
import org.junit.Test;

/**
 * @author matthew
 *
 */
public class GuanxiPrincipalTest {
    /**
     * This tests that the freshly created GuanxiPrincipal object
     * does not have any associated data.
     */
    @Test
    public void testEmptyGuanxiPrincipal() {
        GuanxiPrincipal principal;
        String randomKey;
        
        principal = new GuanxiPrincipal();
        
        assertEquals("GuanxiPrincipal created with name", null, principal.getName());
        assertEquals("GuanxiPrincipal created with uniqueId", null, principal.getUniqueId());
        assertEquals("GuanxiPrincipal created with non empty private profile data", 0, principal.getPrivateProfileData().size());
        
        for ( int i = 0;i < 100;i++ ) {
            randomKey = TestUtils.randomString(Math.abs(TestUtils.random.nextInt()) % 100); 
            // random length string to try and increase the chance it will find something
            // its still a very long shot
            // if this does find anything it should be added as an explicit test
            
            assertTrue("GuanxiPrincipal created with credentials for " + randomKey, ! principal.hasCredsFor(randomKey));
            assertTrue("GuanxiPrincipal created with issuer for " + randomKey, ! principal.hasIssuerFor(randomKey));

            assertEquals("GuanxiPrincipal created with issuer for " + randomKey, null, principal.getIssuerFor(randomKey));
            assertEquals("GuanxiPrincipal created with private profile data entry for " + randomKey, null, principal.getPrivateProfileDataEntry(randomKey));
            assertEquals("GuanxiPrincipal created with signing creds for " + randomKey, null, principal.getSigningCredsFor(randomKey));
        }
    }
    
    /**
     * This tests the setting and retrieval of the name.
     */
    @Test
    public void testName() {
        GuanxiPrincipal principal;
        String randomName;
        
        principal = new GuanxiPrincipal();
        
        assertEquals("GuanxiPrincipal created with name", null, principal.getName());
        
        for ( int i = 0;i < 100;i++ ) {
            randomName = TestUtils.randomString(100);
            
            principal.setName(randomName);
            assertEquals("GuanxiPrincipal name not honoured", randomName, principal.getName());
        }
    }
    
    /**
     * This tests the setting and retrieval of the unique id.
     */
    @Test
    public void testUniqueId() {
        GuanxiPrincipal principal;
        String randomId;
        
        principal = new GuanxiPrincipal();
        
        assertEquals("GuanxiPrincipal created with unique id", null, principal.getUniqueId());
        
        for ( int i = 0;i < 100;i++ ) {
            randomId = TestUtils.randomString(100);
            
            principal.setUniqueId(randomId);
            assertEquals("GuanxiPrincipal unique id not honoured", randomId, principal.getUniqueId());
        }
    }
    
    /**
     * This tests the setting and retrieval of the issuer by relying party
     */
    @Test
    public void testIssuer() {
        GuanxiPrincipal principal;
        String issuer, relyingParty;
        
        principal = new GuanxiPrincipal();
        
        for ( int i = 0;i < 100;i++ ) {
            relyingParty = TestUtils.randomString(100);
            
            for ( int j = 0;j < 10;j++ ) {
                issuer = TestUtils.randomString(10);
                
                principal.addIssuer(relyingParty, issuer);
                assertTrue("GuanxiPrincipal not honouring issuer setting", principal.hasIssuerFor(relyingParty));
                assertEquals("GuanxiPrincipal not honouring issuer setting", issuer, principal.getIssuerFor(relyingParty));
            }
        }
    }
    
    /**
     * This tests the setting and retrieval of the private profile data
     */
    @Test
    public void testPrivateProfileData() {
        GuanxiPrincipal principal;
        String key, value;
        Map<String, Object> privateProfileData;
        
        principal = new GuanxiPrincipal();
        privateProfileData = new TreeMap<String, Object>(); // this is a copy of the data that has been assigned
        
        for ( int i = 0;i < 100;i++ ) {
            key = TestUtils.randomString(100);
            
            for ( int j = 0;j < 10;j++ ) {
                value = TestUtils.randomString(10);
                
                principal.addPrivateProfileDataEntry(key, value);
                privateProfileData.put(key, value);
                
                assertEquals("GuanxiPrincipal not honouring private profile data setting", value, principal.getPrivateProfileDataEntry(key));
                TestUtils.assertMapEquals("GuanxiPrincipal private profile data is not up to date", privateProfileData, principal.getPrivateProfileData());
            }
        }
    }
    
    /**
     * This tests the setting and retrieval of the signing creds by relying party
     */
    @Test
    public void testSigningCreds() {
        GuanxiPrincipal principal;
        String relyingParty;
        Creds signingCreds;
        
        principal = new GuanxiPrincipal();
        
        for ( int i = 0;i < 100;i++ ) {
            relyingParty = TestUtils.randomString(100);
            
            signingCreds = Creds.Factory.newInstance();
            
            principal.addSigningCreds(relyingParty, signingCreds);
            assertTrue("GuanxiPrincipal not honouring creds setting", principal.hasCredsFor(relyingParty));
            assertEquals("GuanxiPrincipal not honouring creds setting", signingCreds, principal.getSigningCredsFor(relyingParty));
        }
    }
}
