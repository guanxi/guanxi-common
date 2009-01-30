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

import org.guanxi.common.entity.EntityManager;
import org.guanxi.common.metadata.Metadata;
import org.guanxi.common.GuanxiException;
import org.guanxi.common.trust.TrustEngine;

import java.util.HashMap;

/**
 * Guanxi implementation of the MetadataManager interface.
 * This class works with XMLBeans defined SAML2 metadata objects
 *
 * @author alistair
 */
public class GuanxiEntityManagerImpl implements EntityManager {
  /** The class to use for handling metadata entities */
  private String entityHandlerClass = null;
  /** The list of entities this manger looks after */
  private HashMap<String, Metadata> metadataHandlers = null;
  /** The trust engine implementation */
  private TrustEngine trustEngine = null;

  /** @see org.guanxi.common.entity.EntityManager#createNewEntityHandler()  */
  public Metadata createNewEntityHandler() throws GuanxiException {
    try {
      return (Metadata)Class.forName(entityHandlerClass).newInstance();
    }
    catch(Exception e) {
      throw new GuanxiException(e);
    }
  }

  public void init() {
    metadataHandlers = new HashMap<String, Metadata>();
  }

  /** @see org.guanxi.common.entity.EntityManager#addMetadata(org.guanxi.common.metadata.Metadata) */
  public void addMetadata(Metadata metadata) {
    metadataHandlers.put(metadata.getEntityID(), metadata);
  }

  /** @see org.guanxi.common.entity.EntityManager#getMetadata(String) */
  public Metadata getMetadata(String entityID) {
    return metadataHandlers.get(entityID);
  }

  /** @see org.guanxi.common.entity.EntityManager#removeAllMetadata() */
  public void removeAllMetadata() {
    metadataHandlers.clear();
  }

  /** @see org.guanxi.common.entity.EntityManager#handlesEntity(String)  */
  public boolean handlesEntity(String entityID) {
    return metadataHandlers.containsKey(entityID);
  }

  /** @see org.guanxi.common.entity.EntityManager#setEntityHandlerClass(String)   */
  public void setEntityHandlerClass(String entityHandlerClass) {
    this.entityHandlerClass = entityHandlerClass;
  }

  /** @see org.guanxi.common.entity.EntityManager#setTrustEngine(org.guanxi.common.trust.TrustEngine) */
  public void setTrustEngine(TrustEngine trustEngine) {
    this.trustEngine = trustEngine;
  }

  /** @see org.guanxi.common.entity.EntityManager#getTrustEngine() */
  public TrustEngine getTrustEngine() {
    return trustEngine;
  }

  /** @see org.guanxi.common.entity.EntityManager#getEntityIDs() */
  public String[] getEntityIDs() {
    String[] entityIDs = new String[metadataHandlers.keySet().size()];
    return metadataHandlers.keySet().toArray(entityIDs);
  }

  /** @see org.guanxi.common.entity.EntityManager#removeMetadata(String) */
  public void removeMetadata(String entityID) {
    metadataHandlers.remove(entityID);
  }
}
