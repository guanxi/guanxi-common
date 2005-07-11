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
//: The Initial Developer of the Original Code is Alistair Young alistair@smo.uhi.ac.uk.
//: Portions created by SMO WWW Development Group are Copyright (C) 2005 SMO WWW Development Group.
//: All Rights Reserved.
//:
/* CVS Header
   $Id$
   $Log$
   Revision 1.2  2005/07/11 10:52:24  alistairskye
   Package restructure

   Revision 1.1  2005/05/04 13:30:59  alistairskye
   SOAP functionality that was previously in org.Guanxi.SAMUEL.Utils.SUtils

*/

package org.guanxi.common;

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
