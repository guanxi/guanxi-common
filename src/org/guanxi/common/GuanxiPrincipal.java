//: "The contents of this file are subject to the Mozilla Public License
//: Version 1.1 (the "License"); you may not use this file except in
//: compliance with the License. You may obtain a copy of the License at
//: http://www.mozilla.org/MPL/
//:
//: Software distributed under the License is distributed on an "AS IS"
//: basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
//: License for the specific language governing rights and limitations
//: under the License.
//:
//: The Original Code is Guanxi (http://www.guanxi.uhi.ac.uk).
//:
//: The Initial Developer of the Original Code is Alistair Young alistair@codebrane.com
//: All Rights Reserved.
//:

package org.guanxi.common;

import org.guanxi.xal.idp.Creds;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * <font size=5><b>Represents a user within a particular profile session</b></font>
 *
 * @author Alistair Young alistair@codebrane.com
 */
public class GuanxiPrincipal implements Principal {
  /** The username from the authentication event */
  private String name = null;
  /** A unique ID that identifies this principal */
  private String uniqueId = null;
  /** Private storage area for profile specific information */
  private Map<String, Object> privateProfileData = null;
  /** The credentials to use when signing SAML on behalf of this principal */
  private Map<String, Creds> signingCreds = null;
  /** The issuer to use when communicating with a service provider on behalf of this principal */
  private Map<String, String> issuers = null;

  public GuanxiPrincipal() {
    privateProfileData = new HashMap<String, Object>();
    signingCreds = new HashMap<String, Creds>();
    issuers = new HashMap<String, String>();
  }

  /**
   * Checks whether the principal has creds for signing SAML for the particular relying party
   *
   * @param relyingParty The relying party ID
   * @return true if the principal has creds for signing saml for the relying party, otherwise false
   */
  public boolean hasCredsFor(String relyingParty) {
    return signingCreds.containsKey(relyingParty);
  }

  /**
   * Stores a set of signing creds for use with the specific relying party
   *
   * @param relyingParty The relying party ID
   * @param creds The signing creds to use for this relying party
   */
  public void addSigningCreds(String relyingParty, Creds creds) {
    signingCreds.put(relyingParty, creds);
  }

  /**
   * Retrieves the signing creds for the specific relying party
   *
   * @param relyingParty The relying party ID
   * @return The signing creds to use for this relying party
   */
  public Creds getSigningCredsFor(String relyingParty) {
    return signingCreds.get(relyingParty);
  }

  /**
   * Checks whether the principal has an issuer for the particular relying party
   *
   * @param relyingParty The relying party ID
   * @return true if the principal has an issuer for relying party, otherwise false
   */
  public boolean hasIssuerFor(String relyingParty) {
    return issuers.containsKey(relyingParty);
  }

  /**
   * Stores an issuer for use with the specific relying party
   *
   * @param relyingParty The relying party ID
   * @param issuer The issuer to use for this relying party
   */
  public void addIssuer(String relyingParty, String issuer) {
    issuers.put(relyingParty, issuer);
  }

  /**
   * Retrieves the issuer for the specific relying party
   *
   * @param relyingParty The relying party ID
   * @return The issuer to use for this relying party
   */
  public String getIssuerFor(String relyingParty) {
    return issuers.get(relyingParty);
  }

  public void setName(String inName) {
    name = inName;
  }

  public String getName() {
    return name;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public String getUniqueId() {
    return uniqueId;
  }

  public void addPrivateProfileDataEntry(String key, Object value) {
    privateProfileData.put(key, value);
  }

  public Object getPrivateProfileDataEntry(String key) {
    return privateProfileData.get(key);
  }

  public Map<String, Object> getPrivateProfileData() {
    return privateProfileData;
  }
}
