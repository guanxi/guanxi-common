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

package org.guanxi.common.metadata;

/**
 * This is the super interface that allows a single generic
 * class to accommodate both IdP and SP Metadata.
 * 
 * @author matthew alistair
 *
 */
public interface Metadata {
  /**
   * This will return the entityID of the IdP or SP.
   * 
   * @return The string representation of the entityID.
   */
  public String getEntityID();

  /**
   * Store any private data required by an implementation
   *
   * @param privateData Anything you like!
   */
  public void setPrivateData(Object privateData);

  /**
   * Gets hold of an implementation's private data
   *
   * @return Anything you like!
   */
  public Object getPrivateData();
}
