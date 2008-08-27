/**
 * 
 */
package org.guanxi.common.metadata;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;

import javax.security.auth.x500.X500Principal;

import org.guanxi.xal.w3.xmldsig.KeyInfoType;
import org.guanxi.xal.w3.xmldsig.X509DataType;

/**
 * This class is responsible for handling and verifying certificates.
 * 
 * @author matthew
 *
 */
public class CertificateHandler {
  /** 
   * We'll use this to generate X509s on the fly from metadata 
   */
  private static CertificateFactory certificateFactory;
  
  public static void init() throws CertificateException {
    certificateFactory = CertificateFactory.getInstance("x.509");
  }
  
  /**
   * This searches through the certificates in the KeyInfoType passed in, looking for
   * one that matches the provided signing certificate.
   * 
   * @param keyInfo                 This is the list of certificates which must be verified.
   * @param signingCertificateData  This is the signing certificate that the metadata states.
   * @return                        A boolean indicating if the certificate is trusted.
   * @throws CertificateException   If there is a problem creating any of the certificates.
   */
  public static boolean verifySigningCertificate(KeyInfoType keyInfo, byte[] signingCertificateData) throws CertificateException {
    
    X509Certificate certificate, signingCertificate;

    // remember that ByteArrayInputStreams do not need to be closed
    signingCertificate = (X509Certificate)certificateFactory.generateCertificate(new ByteArrayInputStream(signingCertificateData));
    
    for ( X509DataType x509CertificateArray : keyInfo.getX509DataArray() ) {
      for ( byte[] x509RawData : x509CertificateArray.getX509CertificateArray() ) {
        // remember that ByteArrayInputStreams do not need to be closed
        certificate = (X509Certificate)certificateFactory.generateCertificate(new ByteArrayInputStream(x509RawData));
        
        if ( certificate.equals(signingCertificate) ) {
          return true;
        }
      }
    }
    
    return false;
  }
  
  /**
   * This searches through the certificates in the KeyInfoType passed in, looking
   * for one that matches the DN passed in. When one is found it must also be trusted.
   * 
   * @param keyInfo
   * @param signingCertificateDN
   * @return
   * @throws CertificateException
   */
  public static boolean verifySigningCertificate(KeyInfoType keyInfo, String signingCertificateDN) throws CertificateException {
    X509Certificate[] certificateArray;
    X509Certificate rootCertificate;
    
    for ( X509DataType x509CertificateDataArray : keyInfo.getX509DataArray() ) {
      certificateArray = createCertificateArray(x509CertificateDataArray.getX509CertificateArray());
      
      for ( X509Certificate currentCertificate : certificateArray ) {
        if ( currentCertificate.getSubjectX500Principal().getName().equals(signingCertificateDN) ) {
          rootCertificate = findRootCertificate(certificateArray, currentCertificate);
          
          
        }
      }
    }
    
    return false;
  }
  
  /**
   * This creates an array of certificate objects from the byte array provided.
   * 
   * @param rawData
   * @return
   */
  private static X509Certificate[] createCertificateArray(byte[][] rawData) throws CertificateException {
    Collection<X509Certificate> result;
    
    result = new ArrayList<X509Certificate>();
    for ( byte[] currentRawData : rawData ) {
      result.add((X509Certificate)certificateFactory.generateCertificate(new ByteArrayInputStream(currentRawData)));
    }
    
    return result.toArray(new X509Certificate[result.size()]);
  }
  
  /**
   * This will return the certificate that is the issuer of the current certificate.
   * A certificate is the issuer of another certificate if the DN of the issuer
   * matches the issuer of the child. This will return null if this certificate cannot
   * be found.
   * 
   * @param certificateArray
   * @param current
   * @return
   */
  private static X509Certificate findIssuingCertificate(X509Certificate[] certificateArray, X509Certificate current) {
    X500Principal currentIssuer;
    
    currentIssuer = current.getIssuerX500Principal();
    
    for ( X509Certificate certificate : certificateArray ) {
      // don't check the certificate against itself, dont want a loop
      if ( certificate != current && currentIssuer.equals(certificate.getSubjectX500Principal()) ) {
        return certificate;
      }
    }
    
    return null;
  }
  
  /**
   * This will return the certificate which issues (directly or indirectly) the start certificate
   * but is not issued by any other certificate in the array.
   * 
   * @param certificateArray
   * @param start
   * @return
   */
  private static X509Certificate findRootCertificate(X509Certificate[] certificateArray, X509Certificate start) {
    X509Certificate current, next;
    
    next = start;
    do {
      current = next;
      next = findIssuingCertificate(certificateArray, current);
    }
    while ( next != null );
    
    return current;
  }
  
  /**
   * This will attempt to verify the root certificate. If the root certificate fails verification 
   * then an exception will be thrown.
   * 
   * @param rootCertificate
   * @throws CertificateNotYetValidException
   * @throws CertificateExpiredException
   */
  private static void verifyRootCertificate(X509Certificate rootCertificate) throws CertificateNotYetValidException, CertificateExpiredException {
    rootCertificate.checkValidity();
  }
}
