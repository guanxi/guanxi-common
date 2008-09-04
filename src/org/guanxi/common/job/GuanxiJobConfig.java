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

/**
 * Represents core configuration information passed to jobs. All jobs get this information.
 * Those that require specific settings should override SimpleGuanxiJobConfig
 */
public interface GuanxiJobConfig {
  /** The key in a job's data map where the job's config is stored */
  public static final String JOB_KEY_JOB_CONFIG = "JOB_KEY_JOB_CONFIG";
  
  public void setKey(String key);
  public String getKey();
  public void setCronLine(String cronLine);
  public String getCronLine();
  public void setJobClass(String jobClass);
  public String getJobClass();

  public void setPrivateData(Object privateData);
  public Object getPrivateData();

  public void setStartImmediately(boolean startImmediately);
  public boolean isStartImmediately();
}
