/* CVS Header
   $
   $
*/

package org.guanxi.common.metadata;

/**
 * This provides an interface to access all of the different attributes of the
 * IdP Metadata that are required by Guanxi. Using this means that different
 * formats of metadata can be supported much more easily.
 *
 * @author alistair
 */
public interface SPMetadata extends Metadata {
  /**
   * This will return the entityID of the SP
   *
   * @return The string representation of the SP entityID.
   */
  public String getEntityID();

  /**
   * This gets the Assertion Consumer Service URL. Where possible this should get the
   * ACSURL that has the urn:oasis:names:tc:SAML:1.0:profiles:browser-post
   * binding.
   *
   * @return This returns the Assertion Consumer Service URL
   */
  public String getAssertionConsumerServiceURL();
}
