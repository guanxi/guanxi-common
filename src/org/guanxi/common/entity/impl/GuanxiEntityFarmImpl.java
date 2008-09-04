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

package org.guanxi.common.entity.impl;

import org.guanxi.common.entity.EntityFarm;
import org.guanxi.common.entity.EntityManager;

import java.util.Map;

/**
 * Guanxi implementation of an EntityFarm
 *
 * @author alistair
 */
public class GuanxiEntityFarmImpl implements EntityFarm {
  /** All the metadata managers */
  private Map<String, EntityManager> entityManagers = null;

  /** @see org.guanxi.common.entity.EntityFarm#getEntityManagerForSource(String) */
  public EntityManager getEntityManagerForSource(String metadataSource) {
    for (String source : entityManagers.keySet()) {
      if (source.equals(metadataSource)) {
        return entityManagers.get(source);
      }
    }

    return null;
  }

  /** @see org.guanxi.common.entity.EntityFarm#getEntityManagerForID(String) */
  public EntityManager getEntityManagerForID(String id) {
    for (EntityManager entityManager : entityManagers.values()) {
      if (entityManager.handlesEntity(id)) {
        return entityManager;
      }
    }

    return null;
  }

  /** @see org.guanxi.common.entity.EntityFarm#setEntityManagers(java.util.Map) */
  public void setEntityManagers(Map<String, EntityManager> entityManagers) { this.entityManagers = entityManagers; }
  /** @see org.guanxi.common.entity.EntityFarm#getEntityManagers() */
  public Map getEntityManagers() { return entityManagers; }
}
