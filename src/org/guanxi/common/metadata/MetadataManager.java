/**
 * 
 */
package org.guanxi.common.metadata;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.xmlbeans.XmlException;

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
 * @author matthew
 */
public abstract class MetadataManager<M extends Metadata> {
	/**
	 * This is the map of entityID to Metadata, and this is
	 * the only place that the Metadata is stored.
	 */
	protected final Map<String, M> metadataByEntityID;
	/**
	 * This is the map of loaded source name and the entityIDs that
	 * have been loaded from that source.
	 */
	protected final Map<String, Set<String>> entityIDBySource;
	
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
	 * This will write all of the loaded metadata out to the 
	 * output stream provided. It is recommended that the 
	 * special template XML bean is used for this purpose.
	 * 
	 * @param out          This is the output stream that will receive the serialised metadata.
	 * @throws IOException This will be thrown if there is a problem writing the metadata to the stream.
	 */
	public abstract void write(OutputStream out) throws IOException;
	/**
	 * This will read metadata in from the input stream provided.
	 * This will overwrite any metadata that has been loaded from
	 * a source that is also found in the metadata loaded from the
	 * input stream. Existing metadata from a source not found in
	 * the input stream metadata will not be replaced or removed.
	 * 
	 * @param in             This is the input stream which will be used to load the metadata.
	 * @throws IOException   This will be thrown if there is a problem reading the input stream.
	 * @throws XmlException  This will be thrown if there is a problem parsing the XML from the input stream.
	 */
	public abstract void read(InputStream in) throws IOException, XmlException;
	
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
	}
}
