/**
 * 
 */
package org.guanxi.common.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * This is responsible for managing the loaded Metadata.
 * The following operations must be supported:
 * 
 * <ul>
 * 	<li>Adding new Metadata</li>
 *  <li>Retrieving Metadata by entityID</li>
 *  <li>Unloading loaded Metadata</li>
 * </ul>
 * 
 * The Metadata must be associated with the method of loading
 * the data. This is required because for periodic loading of
 * Metadata the previous data must be unloaded to successfully
 * remove entries that are no longer present in the current
 * Metadata.
 * 
 * A current potential bug is that if the metadata for an is loaded
 * twice then removal of either set will remove the metadata for both.
 * 
 * This also now supports the dirty flag, which can be used to indicate
 * that the stored metadata has changed. This allows information based
 * on the metadata to be cached and recalculated when required.
 * 
 * @author matthew
 */
public abstract class MetadataManager<M extends Metadata> {
	/**
	 * This is the map of entityID to Metadata, and this is
	 * the only place that the Metadata is stored.
	 */
	private final Map<String, M> metadataByEntityID;
	/**
	 * This is the map of loaded source name and the entityIDs that
	 * have been loaded from that source.
	 */
	private final Map<String, Set<String>> entityIDBySource;
	/**
	 * This is set when metadata is added, removed, or updated.
	 * Anything that uses this is responsible for clearing it once
	 * the appropriate actions have been taken.
	 * 
	 * If there is a problem with multiple tasks which must occur
	 * at different times and which also rely on this for caching
	 * then this will be replaced with an integer that will be
	 * incremented by one each time the metadata is changed. Then
	 * checking the value against the value at caching time will
	 * indicate changes without requiring that the flag is cleared.
	 */
	private boolean dirty;
	
	/**
	 * This creates an empty Metadata Manager.
	 * It should not be possible to create Metadata Managers directly
	 * because this can lead to multiple instances which will not
	 * have their data synchronised.
	 */
	protected MetadataManager() {
		metadataByEntityID = new TreeMap<String, M>();
		entityIDBySource = new TreeMap<String, Set<String>>();
	}
	
	/**
	 * This appends metadata to the list of metadata loaded for this
	 * particular source. Existing metadata loaded for this source is
	 * retained.
	 * 
	 * @param source   This is a unique string that represents the source of the metadata.
	 * @param metadata This is the metadata that has been loaded from the source.
	 */
	public void addMetadata(String source, M... metadata) {
		String entityID;
		Set<String> entityIDList;
		
		if ( ! entityIDBySource.containsKey(source) ) {
			entityIDBySource.put(source, new TreeSet<String>());
		}
		entityIDList = entityIDBySource.get(source);
		
		for ( M currentMetadata : metadata ) {
			entityID = currentMetadata.getEntityID();
			metadataByEntityID.put(entityID, currentMetadata);
			entityIDList.add(entityID);
		}
		setDirty(true);
	}
	
	/**
	 * This returns all metadata held by this manager.
	 * 
	 * @return This will return an unsorted list of all metadata held by this manager.
	 */
	public Collection<M> getMetadata() {
	  return metadataByEntityID.values();
	}
	
	/**
	 * This returns the metadata associated with a particular entityID.
	 * If the entityID is not associated with metadata then null is returned.
	 * 
	 * @param entityID   This is the entityID for the IdP whos metadata is being requested.
	 * @return           This will return the IdPMetadata for the IdP or null if there is no metadata for the IdP.
	 */
	public M getMetadata(String entityID) {
		return metadataByEntityID.get(entityID);
	}
	
	/**
	 * This gets all the metadata that has been loaded from a specific source.
	 * If no metadata has been loaded from that source then the result will be
	 * null.
	 * 
	 * @param source This is the source of the metadata that has been loaded.
	 * @return       The complete collection of metadata from the source.
	 */
	public Collection<M> getMetadataBySource(String source) {
	  Collection<M> result;
	  
	  if ( ! entityIDBySource.containsKey(source) ) {
	    return null;
	  }
	  
	  result = new ArrayList<M>();
	  for ( String entityID : entityIDBySource.get(source) ) {
	    result.add(metadataByEntityID.get(entityID));
	  }
	  
	  return result;
	}
	
	/**
	 * This will remove all metadata that has been loaded for a particular
	 * source. The record of entityIDs associated with that source is also
	 * cleared.
	 * 
	 * @param source This is a unique string that represents the source of the metadata.
	 */
	public void removeMetadata(String source) {
		if ( entityIDBySource.containsKey(source) ) {
			for ( String entityID : entityIDBySource.get(source) ) {
				metadataByEntityID.remove(entityID);
			}
			entityIDBySource.remove(source);
		}
		setDirty(true);
	}
	
	/**
	 * This will remove any existing metadata from the indicated source,
	 * replacing it with the provided metadata.
	 * 
	 * @param source   This is a unique string that represents the source of the metadata.
	 * @param metadata This is the metadata that has been loaded from the source.
	 */
	public void setMetadata(String source, M... metadata) {
		removeMetadata(source);
		addMetadata(source, metadata);
		setDirty(true);
	}
	
	/**
	 * This gets a list of all of the different sources of metadata.
	 * 
	 * @return A set containing the tags of all of the sources of metadata that have been loaded.
	 */
	public Set<String> getSources() {
	  return entityIDBySource.keySet();
	}
	
	/**
	 * This gets the state of the dirty flag. The dirty flag is used to indicate when
	 * caches of data based on the metadata need to be refreshed.
	 * 
	 * @return
	 */
	public boolean isDirty() {
	  return dirty;
	}
	
	/**
	 * This sets the dirty flag. The dirty flag is used to indicate when
	 * caches of data based on the metadata need to be refreshed.
	 * 
	 * @param dirty
	 */
	public void setDirty(boolean dirty) {
	  this.dirty = dirty;
	}
}
