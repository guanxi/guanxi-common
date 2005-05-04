/* CVS Header
   $Id$
   $Log$
   Revision 1.1  2005/05/04 13:30:59  alistairskye
   SOAP functionality that was previously in org.Guanxi.SAMUEL.Utils.SUtils

*/

package org.Guanxi.Common;

import org.w3c.dom.Document;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import java.io.OutputStream;
import java.io.IOException;

/**
 * <font size=5><b></b></font>
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 */
public class SOAPUtils {
  static private SOAPUtils _instance = null;
  private MessageFactory messageFactory = null;

  static public SOAPUtils getInstance() throws GuanxiException {
    if (_instance == null) {
      synchronized(SOAPUtils.class) {
        if (_instance == null) {
          _instance = new SOAPUtils();
        }
      }
    }

    return _instance;
  }

  private SOAPUtils() throws GuanxiException {
    try {
      messageFactory = MessageFactory.newInstance();
    }
    catch(SOAPException se) {
      throw new GuanxiException(se);
    }
  }

  public SOAPMessage wrapResponseInSOAP(Document response) {
    SOAPMessage soapResponse = null;

    try {
      soapResponse = messageFactory.createMessage();
      SOAPBody soapBody = soapResponse.getSOAPPart().getEnvelope().getBody();
      soapBody.addDocument(response);
    }
    catch(SOAPException se) {
      return null;
    }

    return soapResponse;
  }

  public boolean sendSOAPResponse(OutputStream responseStream, SOAPMessage message) {
    try {
      message.writeTo(responseStream);
      return true;
    }
    catch(SOAPException se) {
      return false;
    }
    catch(IOException ioe) {
      return false;
    }
  }
}
