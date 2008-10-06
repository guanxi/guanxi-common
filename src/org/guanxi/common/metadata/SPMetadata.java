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
 * This provides an interface to access all of the different attributes of the
 * SP Metadata that are required by Guanxi. Using this means that different
 * formats of metadata can be supported much more easily.
 *
 * @author alistair
 */
public interface SPMetadata extends Metadata {
  /**
   * This gets the Assertion Consumer Service URL. Where possible this should get the
   * ACSURL that has the urn:oasis:names:tc:SAML:1.0:profiles:browser-post
   * binding.
   *
   * @return This returns the Assertion Consumer Service URL
   */
  public String getAssertionConsumerServiceURL();
}
