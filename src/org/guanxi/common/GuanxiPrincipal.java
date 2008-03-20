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
  /** The ID of the relying party with which this principal is associated */
  private String relyingPartyID = null;
  /** Private storage area for profile specific information */
  private Map<String, Object> privateProfileData = null;

  public GuanxiPrincipal() {
    privateProfileData = new HashMap<String, Object>();
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

  public Map getPrivateProfileData() {
    return privateProfileData;
  }

  public String getRelyingPartyID() {
    return relyingPartyID;
  }

  public void setRelyingPartyID(String relyingPartyID) {
    this.relyingPartyID = relyingPartyID;
  }
}
