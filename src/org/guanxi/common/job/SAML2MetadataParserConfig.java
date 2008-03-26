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

package org.guanxi.common.job;

public class SAML2MetadataParserConfig extends SimpleGuanxiJobConfig {
  /** Where the get the SAML2 metadata */
  private String metadataURL = null;
  /** The value of User-Agent to set */
  private String who = null;

  public void setMetadataURL(String metadataURL) { this.metadataURL = metadataURL; }
  public String getMetadataURL() { return metadataURL; }

  public void setWho(String who) { this.who = who; }
  public String getWho() { return who; }
}
