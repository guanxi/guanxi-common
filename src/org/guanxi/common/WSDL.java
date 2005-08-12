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
   Revision 1.3  2005/08/12 12:48:31  alistairskye
   Added license

   Revision 1.2  2005/08/11 11:34:19  alistairskye
   Constructor now throws GuanxiException

   Revision 1.1  2005/08/11 11:31:20  alistairskye
   Moved WSDL to org.guanxi.common

   Revision 1.1  2005/08/10 14:20:54  alistairskye
   WSDL parser

*/

package org.guanxi.common;

import org.guanxi.samuel.utils.ParseErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.EntityResolver;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

/**
 * <font size=5><b></b></font>
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 */
public class WSDL {
  private String namespace = null;
  private String portName = null;
  private String serviceName = null;

  public WSDL(URL wsdlURL, int timeout) throws GuanxiException {
    SAXParserFactory factory = SAXParserFactory.newInstance();
    factory.setNamespaceAware(true);
    factory.setValidating(true);

    try {
      SAXParser parser = factory.newSAXParser();
      XMLReader xmlReader = parser.getXMLReader();
      xmlReader.setFeature("http://apache.org/xml/features/validation/schema", true);
      xmlReader.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", "wsdl.xsd");
      xmlReader.setEntityResolver(new WSDLResolver());
      xmlReader.setErrorHandler(new ParseErrorHandler());
      xmlReader.setContentHandler(new WSDLContentHandler());

      HttpClient client = new HttpClient();
      HttpMethod get = new GetMethod(wsdlURL.toString());
      get.getParams().setSoTimeout(timeout);
      client.executeMethod(get);
      xmlReader.parse(new InputSource(get.getResponseBodyAsStream()));
      get.releaseConnection();
    }
    catch(Exception e) {
      throw new GuanxiException(e);
    }
  }

  public String getNamespace() { return namespace; }
  public String getPortName() { return portName; }
  public String getServiceName() { return serviceName; }

  class WSDLContentHandler extends DefaultHandler {
    public void startElement(String uri, String localName, String qName, Attributes attrs) {
      if (localName.equals("definitions")) {
        namespace = getAttribute(attrs, "targetNamespace");
      }

      if (localName.equals("port")) {
        portName = getAttribute(attrs, "name");
      }

      if (localName.equals("service")) {
        serviceName = getAttribute(attrs, "name");
      }
    }

    public void endElement(String uri, String localName, String qName) {
    }

    String getAttribute(Attributes attrs, String name) {
      for (int count=0; count < attrs.getLength(); count++) {
        if (attrs.getLocalName(count).equals(name)) {
          return attrs.getValue(count);
        }
      }

      return "";
    }
  }

  class WSDLResolver implements EntityResolver {
    public InputSource resolveEntity(String publicID, String systemID) throws IOException {
      if (systemID != null) {
        InputStream inputStream = null;

        if (systemID.endsWith("wsdl.xsd")) {
          inputStream = org.guanxi.samuel.utils.Resolver.class.getResourceAsStream("/xsd/wsdl/wsdl.xsd");
        }
        else
          return null;

        StringBuffer buffer = new StringBuffer(1024);
        int b;
        byte[] data;
        while ((b = inputStream.read()) != -1) {
          buffer.append((char)b);
        }
        data = buffer.toString().getBytes();
        inputStream.close();

        return new InputSource(new ByteArrayInputStream(data));
      }

      return null;
    }
  }
}
