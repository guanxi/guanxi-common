/* CVS Header
   $
   $
*/

package org.guanxi.common.metadata;

import org.apache.xmlbeans.XmlException;
import org.guanxi.xal.metadata.*;

import javax.servlet.ServletContext;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * This is responsible for managing the loaded SP Metadata.
 * The following operations must be supported:
 *
 * <ul>
 *  <li>Adding new SP Metadata</li>
 *  <li>Retrieving SP Metadata by entityID</li>
 *  <li>Unloading loaded SP Metadata</li>
 * </ul>
 *
 * The SP Metadata must be associated with the method of loading
 * the data. This is required because for periodic loading of
 * SP Metadata the previous data must be unloaded to successfully
 * remove SP entries that are no longer present in the current
 * Metadata.
 *
 * A current potential bug is that if the metadata for an SP is loaded
 * twice then removal of either set will remove the SP metadata for both.
 *
 * @author alistair
 */
public class SPMetadataManager extends MetadataManager<SPMetadata> {
  /** This is the key used to get the IdP out of the ServletContext. */
  private static final String key = SPMetadataManager.class.getName();

  /**
   * This will return the SPManager for this application.
   * If there is no current SPManager then this will create one.
   *
   * @param context  This is the ServletContext which holds the SPManager
   * @return         This will return the SPManager held by the ServletContext
   */
  public static SPMetadataManager getManager(ServletContext context) {
    SPMetadataManager manager;

    if ((manager = (SPMetadataManager)context.getAttribute(key)) != null) {
      return manager;
    }

    manager = new SPMetadataManager();
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
    SPDescriptorsDocument document = SPDescriptorsDocument.Factory.parse(in);
    SPDescriptors base = document.getSPDescriptors();
    Map<String, Set<SPTemplateMetadata>> spMetadataBySource;
    spMetadataBySource = new TreeMap<String, Set<SPTemplateMetadata>>();

    // This loads all of the metadata and associates it with the source
    for (SPDescriptor spMetadata : base.getSPDescriptorArray()) {
      Set<SPTemplateMetadata> sourceMetadata = spMetadataBySource.get(spMetadata.getSource());
      if (sourceMetadata == null) {
        sourceMetadata = new TreeSet<SPTemplateMetadata>(new Metadata.MetadataComparator<SPTemplateMetadata>());
        spMetadataBySource.put(spMetadata.getSource(), sourceMetadata);
      }

      sourceMetadata.add(new SPTemplateMetadata(spMetadata));
    }

    // This then overwrites the existing metadata that has been loaded for that source
    for (String source : spMetadataBySource.keySet()) {
      setMetadata(source, spMetadataBySource.get(source).toArray(new SPTemplateMetadata[spMetadataBySource.size()]));
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
    SPDescriptorsDocument document = SPDescriptorsDocument.Factory.newInstance();
    SPDescriptors base = document.addNewSPDescriptors();

    for (String source : entityIDBySource.keySet()) {
      for (String entityID : entityIDBySource.get(source)) {
        SPMetadata metadata = metadataByEntityID.get(entityID);

        SPDescriptor instance = base.addNewSPDescriptor();
        instance.setSource(source);
        instance.setEntityID(entityID);
        instance.setAssertionConsumerServiceURL(metadata.getAssertionConsumerServiceURL());
      }
    }

    document.save(out);
  }
}
