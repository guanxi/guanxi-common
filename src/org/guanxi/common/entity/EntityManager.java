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

import org.guanxi.common.GuanxiException;
import org.guanxi.common.metadata.Metadata;
import org.guanxi.common.trust.TrustEngine;

/**
 * This is responsible for managing Entity metadata and the
 * trust based on that metadata.
 * 
 * @author matthew alistair
 */
public interface EntityManager {
  /**
   * Sets the class to use for entity handlers. Tnis is normally injected.
   *
   * @param entityHandlerClass fully qualified class name to use for entity handlers
   */
  public void setEntityHandlerClass(String entityHandlerClass);

  /**
   * Creates an empty entity handler class, ready for use.
   * This will be an instance of the class specifed via
   * @see EntityManager#setEntityHandlerClass(String)
   *
   * @return Metadata instance for use as an entity handler
   * @throws GuanxiException if an error occurs
   */
  public Metadata createNewEntityHandler() throws GuanxiException;

  /**
	 * This adds metadata to the list of metadata loaded for this
	 * particular source. This will overwrite existing metadata for
   * the entity.
	 * 
	 * @param metadata This is the metadata that has been loaded from the source.
	 */
	public void addMetadata(Metadata metadata);

	/**
	 * This returns the metadata associated with a particular entityID.
	 * If the entityID is not associated with metadata then null is returned.
	 * 
	 * @param entityID   This is the entityID for the IdP whos metadata is being requested.
	 * @return           This will return the IdPMetadata for the IdP or null if there is no metadata for the IdP.
	 */
	public Metadata getMetadata(String entityID);

	/**
	 * This will remove all metadata that has been loaded for a particular
	 * source.
	 */
	public void removeAllMetadata();

  /**
   * Determines whether a particular MetadataManager knows about the particular entity
   *
   * @param entityID The entityID to look for
   * @return true if the manager knows about the entity otherwise false
   */
  public boolean handlesEntity(String entityID);

  /**
   * Sets the trust implementation for this entity manager
   *
   * @param trustEngine the entity manager will use this engine to manage
   * trust for its entities.
   */
  public void setTrustEngine(TrustEngine trustEngine);

  /**
   * Retrieve the trust engine in use.
   *
   * @return the trust engine this entity manager is using.
   */
  public TrustEngine getTrustEngine();

  /**
   * Returns all the entity IDs handled by this manager
   *
   * @return array of entity IDs
   */
  public String[] getEntityIDs();

  /**
   * Removes the metadata associated with the specified entity ID
   *
   * @param entityID the entity who's metadata is to be removed
   */
  public void removeMetadata(String entityID);
}
