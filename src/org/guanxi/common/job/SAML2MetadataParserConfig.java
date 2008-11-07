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

import java.io.File;

/**
 * Represents a configuration object that is passed to a
 * SAML2 metadata parserJob via its JobDataMap.
 *
 * @author alistair
 */
public class SAML2MetadataParserConfig extends SimpleGuanxiJobConfig {
  /** Where the get the SAML2 metadata */
  private String metadataURL = null;
  /** The value of User-Agent to set */
  private String who = null;
  /** Whether to start the job straight away */
  private boolean startImmediately;
  /** The directory in which to store an offline version of the metadata */
  private String cacheDir = null;
  /** The name of the offline metadata file in cacheDir */
  private String cacheFile = null;
  /** Full path and name of the metadata cache file */
  private String metadataCacheFile = null;
  /** The location of the federation PEM file. This is used to verify
   *  the fingerprint of the metadata signing certificate. */
  private String pemLocation = null;
  /** Whether the metadata is signed */
  private boolean signed;

  /**
   * Initialisation
   */
  public void init() {
    super.init();

    metadataCacheFile = sanitisePath(cacheDir);
    if (!metadataCacheFile.endsWith(File.separator)) {
      metadataCacheFile += File.separator;
    }

    File file = new File(metadataCacheFile);
    if (!file.exists()) {
      file.mkdirs();
    }

    metadataCacheFile += cacheFile;
  }

  public void setMetadataURL(String metadataURL) { this.metadataURL = metadataURL; }
  public String getMetadataURL() { return metadataURL; }

  public void setWho(String who) { this.who = who; }
  public String getWho() { return who; }

  public void setStartImmediately(boolean startImmediately) { this.startImmediately = startImmediately; }
  public boolean isStartImmediately() { return startImmediately; }

  public String getCacheDir() { return cacheDir; }
  public void setCacheDir(String cacheDir) { this.cacheDir = cacheDir; }

  public String getCacheFile() { return cacheFile; }
  public void setCacheFile(String cacheFile) { this.cacheFile = cacheFile; }

  public String getMetadataCacheFile() { return metadataCacheFile; }

  public void setPemLocation(String pemLocation) { this.pemLocation = pemLocation; }
  public String getPemLocation() { return pemLocation; }

  public boolean getSigned() { return signed; }
  public void setSigned(boolean signed) { this.signed = signed; }
}
