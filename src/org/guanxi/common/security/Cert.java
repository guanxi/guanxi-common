/* CVS Header
   $Id$
   $Log$
   Revision 1.1  2005/08/15 11:22:47  alistairskye
   Handy dandy class for loading X509 certificates

*/

package org.guanxi.common.security;

import javax.security.cert.X509Certificate;
import javax.security.cert.CertificateException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * <font size=5><b></b></font>
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 */
public class Cert {
  public static X509Certificate getCertificate(String certFilePath) {
    try {
      return getCertificate(new FileInputStream(new File(certFilePath)));
    }
    catch(FileNotFoundException fnfe) {
      return null;
    }
  }

  public static X509Certificate getCertificate(File certFile) {
    try {
      return getCertificate(new FileInputStream(certFile));
    }
    catch(FileNotFoundException fnfe) {
      return null;
    }
  }

  public static X509Certificate getCertificate(InputStream in) {
    try {
      return X509Certificate.getInstance(in);
    }
    catch(CertificateException se) {
      return null;
    }
  }
}
