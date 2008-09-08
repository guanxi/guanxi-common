/**
 * 
 */
package org.guanxi.common.metadata;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

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
   * Storing the IdPMetadataManager as a singleton makes it much easier to access.
   */
  private static IdPMetadataManager singleton;
  /**
   * This is used to convert the binary data into certificates in order to add
   * them to the in-memory truststore.
   */
  private static CertificateFactory certFactory;
  /**
   * This is the cached truststore which is used
   * to hold the complete set of trusted server
   * certificates.
   */
  private KeyStore truststore;
  
  /**
   * This will return the IdPManager for this application.
   * If there is no current IdPManager then this will create one.
   * 
   * @return         This will return the IdPManager singleton
   */
  public static IdPMetadataManager getManager() {
    if ( singleton != null ) {
      return singleton;
    }

    singleton = new IdPMetadataManager();
    
    return singleton;
  }
  
  /**
   * This returns the truststore object that contains all of the AA URL certificates
   * found in the metadata as well as the certificates that have been explicitly added
   * to the truststore file.
   * 
   * @param truststoreFile
   * @param truststorePassword
   * @return
   * @throws KeyStoreException 
   * @throws IOException 
   * @throws CertificateException 
   * @throws NoSuchAlgorithmException 
   */
  public KeyStore loadTrustStore(String truststoreFile, String truststorePassword) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
    if ( truststore == null || isDirty() ) {
      truststore = generateTrustStore(truststoreFile, truststorePassword);
      setDirty(false);
    }
    return truststore;
  }
  
  /**
   * This loads the truststore from the file and then adds all of the certificates stored
   * in the loaded metadata. This allows the on disk truststore to be used to initialise
   * the data and then the loaded metadata can provide additional information.
   * 
   * @param truststoreFile
   * @param truststorePassword
   * @return
   * @throws KeyStoreException 
   * @throws IOException 
   * @throws CertificateException 
   * @throws NoSuchAlgorithmException 
   */
  private KeyStore generateTrustStore(String truststoreFile, String truststorePassword) throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException {
    InputStream in;
    KeyStore    truststore;
    byte[]      certificate;
    
    if ( certFactory == null ) {
      certFactory = CertificateFactory.getInstance("x.509");
    }
    
    // load the truststore
    truststore = KeyStore.getInstance("jks");
    in         = new FileInputStream(truststoreFile);
    try {
      truststore.load(in, truststorePassword.toCharArray());
    }
    finally {
      in.close();
    }
    
    for ( IdPMetadata current : getMetadata() ) {
      certificate = current.getAACertificate();
      
      if ( certificate != null ) {
        if ( truststore.containsAlias(current.getEntityID()) ) {
          truststore.deleteEntry(current.getEntityID());
        }
        truststore.setCertificateEntry(current.getEntityID(), certFactory.generateCertificate(new ByteArrayInputStream(certificate)));
      }
    }
    
    return truststore;
  }
}
