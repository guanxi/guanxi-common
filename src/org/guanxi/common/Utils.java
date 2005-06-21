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
   Revision 1.4  2005/06/21 09:14:40  alistairskye
   Added decodeBase64()

   Revision 1.3  2005/05/04 13:33:55  alistairskye
   base64() moved here from org.Guanxi.SAMUEL.Utils.Utils

   Revision 1.2  2005/04/15 10:02:41  alistairskye
   License updated

*/

package org.Guanxi.Common;

import org.w3c.dom.Document;
import org.apache.xml.security.utils.Base64;
import org.apache.xml.security.exceptions.Base64DecodingException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import java.util.Hashtable;
import java.util.Enumeration;
import java.rmi.server.UID;
import java.io.StringWriter;

/**
 * <font size=5><b></b></font>
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 */
public class Utils
{
  /**
   * Returns the parameters and their values from an HTTP request
   *
   * @param request HTTP request object
   * @return Hashtable of parameters and their values
   */
  public static Hashtable getRequestParameters(HttpServletRequest request) {
    Hashtable params = new Hashtable();
    Enumeration e = request.getParameterNames();
    String name,value;

    while (e.hasMoreElements()) {
      name = (String)e.nextElement();
      value = request.getParameter(name);
      params.put(name, value);
    }

    return params;
  }

  public static String getUniqueID() {
    UID uid = new UID();
    return uid.toString();
  }

  public static String base64(Document inDocToEncode) {
    try {
      DOMSource domSource = new DOMSource(inDocToEncode);
      StringWriter writer = new StringWriter();
      StreamResult result = new StreamResult(writer);
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer transformer = tf.newTransformer();
      transformer.transform(domSource, result);
      return Base64.encode(writer.toString().getBytes());
    }
    catch(TransformerException te) {
      return null;
    }
  }

  public static byte[] decodeBase64(String b64Data) {
    try {
      return Base64.decode(b64Data);
    }
    catch(Base64DecodingException bde) {
      return null;
    }
  }
}
