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

import javax.servlet.ServletContext;

import org.apache.xmlbeans.XmlException;
import org.guanxi.xal.metadata.IdPDescriptor;
import org.guanxi.xal.metadata.IdPDescriptors;
import org.guanxi.xal.metadata.IdPDescriptorsDocument;

/**
 * This is responsible for managing the loaded IdP Metadata.
 * The following operations must be supported:
 * 
 * <ul>
 *  <li>Adding new IdP Metadata</li>
 *  <li>Retrieving IdP Metadata by entityID</li>
 *  <li>Unloading loaded IdP Metadata</li>
 * </ul>
 * 
 * The IdP Metadata must be associated with the method of loading
 * the data. This is required because for periodic loading of
 * IdP Metadata the previous data must be unloaded to successfully
 * remove IdP entries that are no longer present in the current
 * Metadata.
 * 
 * A current potential bug is that if the metadata for an IdP is loaded
 * twice then removal of either set will remove the IdP metadata for both.
 * 
 * @author matthew
 */
public class IdPMetadataManager extends MetadataManager<IdPMetadata> {
  /**
   * This is the key used to get the IdP out of the ServletContext.
   */
  private static final String key = IdPMetadataManager.class.getName();
  
  /**
   * This will return the IdPManager for this application.
   * If there is no current IdPManager then this will create one.
   * 
   * @param context  This is the ServletContext which holds the IdPManager
   * @return         This will return the IdPManager held by the ServletContext
   */
  public static IdPMetadataManager getManager(ServletContext context) {
    IdPMetadataManager manager;
    
    if ( (manager = (IdPMetadataManager)context.getAttribute(key)) != null ) {
      return manager;
    }
    
    manager = new IdPMetadataManager();
    context.setAttribute(key, manager);
    
    return manager;
  }

  /**
   * This will initialise the metadata manager
   * with data loaded from the input stream
   * provided. Any data source specified in the
   * input stream will completely replace any
   * existing metadata loaded from that source.
   * 
   * @param in            This is the input stream that will provide the data to load.
   * @throws IOException  If there is a problem reading from the stream.
   * @throws XmlException If there is a problem parsing the content of the stream.
   */
  @Override
  public void read(InputStream in) throws IOException, XmlException {
    IdPDescriptorsDocument document;
    IdPDescriptors base;
    Map<String, Set<IdPTemplateMetadata>> idpMetadataBySource;
    
    document = IdPDescriptorsDocument.Factory.parse(in);
    base = document.getIdPDescriptors();
    idpMetadataBySource = new TreeMap<String, Set<IdPTemplateMetadata>>();
    
    // This loads all of the metadata and associates it with the source
    for ( IdPDescriptor idpMetadata : base.getIdPDescriptorArray() ) {
      Set<IdPTemplateMetadata> sourceMetadata;
      
      sourceMetadata = idpMetadataBySource.get(idpMetadata.getSource());
      if ( sourceMetadata == null ) {
        sourceMetadata = new TreeSet<IdPTemplateMetadata>(new Metadata.MetadataComparator<IdPTemplateMetadata>());
        idpMetadataBySource.put(idpMetadata.getSource(), sourceMetadata);
      }
      
      sourceMetadata.add(new IdPTemplateMetadata(idpMetadata));
    }
    
    // This then overwrites the existing metadata that has been loaded for that source
    for ( String source : idpMetadataBySource.keySet() ) {
      setMetadata(source, idpMetadataBySource.get(source).toArray(new IdPTemplateMetadata[idpMetadataBySource.size()]));
    }
  }

  /**
   * This will write all loaded metadata to the
   * output stream provided.
   * 
   * @param out           This is the output stream that will receive the XML representation of the metadata.
   * @throws IOException  If there is a problem writing to the stream.
   */
  @Override
  public void write(OutputStream out) throws IOException {
    IdPDescriptorsDocument document;
    IdPDescriptors base;
    IdPDescriptor instance;
    IdPMetadata metadata;
    
    document = IdPDescriptorsDocument.Factory.newInstance();
    base = document.addNewIdPDescriptors();
    
    for ( String source : entityIDBySource.keySet() ) {
      for ( String entityID : entityIDBySource.get(source) ) {
        metadata = metadataByEntityID.get(entityID);
        
        instance = base.addNewIdPDescriptor();
        instance.setSource(source);
        instance.setEntityID(entityID);
        instance.setAttributeAuthorityURL(metadata.getAttributeAuthorityURL());
        instance.setSigningCertificate(metadata.getX509Certificate());
      }
    }
    
    document.save(out);
  }
}
