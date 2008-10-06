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

package org.guanxi.common.entity;

import java.util.Map;

/**
 * Metadata manager farm
 *
 * @author alistair
 */
public interface EntityFarm {
  /**
   * Sets all the entity managers to use with the various sources of metadata. This is designed
   * to be injected from Spring.
   *
   * @param metadataManagers HashMap of sources to managers mappings
   */
  public void setEntityManagers(Map<String, EntityManager> metadataManagers);

  /**
   * Gets the list of entity mangers in use
   *
   * @return all the metadata managers that are being used
   */
  public Map getEntityManagers();

  /**
   * Gets the entity manager that handles the particular source of metadata
   *
   * @param entitySource the source of the metadata, e.g. a URL
   * @return metadata manager for that source or null if none exists
   */
  public EntityManager getEntityManagerForSource(String entitySource);

  /**
   * Gets the entity manager that knows about the particular entity
   *
   * @param id entityID
   * @return metadata manager for that entity or null if none exists
   */
  public EntityManager getEntityManagerForID(String id);
}
