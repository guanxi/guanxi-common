/* CVS Header
   $Id$
   $Log$
   Revision 1.2  2005/08/12 09:03:14  alistairskye
   Added SAML attribute gathering

   Revision 1.1  2005/08/11 14:18:52  alistairskye
   SAML attribute parser and container

*/

package org.guanxi.common;

import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.guanxi.samuel.utils.ParseErrorHandler;
import org.guanxi.samuel.utils.Resolver;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * <font size=5><b></b></font>
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 */
public class Bag {
  /** The Guard session that this bag of attributes is for */
  private String sessionID = null;
  private String assertionID = null;
  private String issueInstant = null;
  private String issuer = null;
  private String majorVersion = null;
  private String minorVersion = null;
  private StringBuffer value = new StringBuffer();
  Hashtable attributes = null;

  public Bag(InputSource in) throws GuanxiException {
    SAXParserFactory factory = SAXParserFactory.newInstance();
    factory.setNamespaceAware(true);
    factory.setValidating(true);

    attributes = new Hashtable();

    try {
      String[] schemas = {"soap-envelope.xsd",
                          "oasis-sstc-saml-schema-assertion-1.1.xsd",
                          "oasis-sstc-saml-schema-protocol-1.1.xsd",
                          "saml-schema-protocol-2.0.xsd",
                          "saml-schema-assertion-2.0.xsd"};
      SAXParser parser = factory.newSAXParser();
      XMLReader xmlReader = parser.getXMLReader();
      xmlReader.setFeature("http://apache.org/xml/features/validation/schema", true);
      xmlReader.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", schemas);
      xmlReader.setEntityResolver(new Resolver());
      xmlReader.setErrorHandler(new ParseErrorHandler());
      xmlReader.setContentHandler(new BagContentHandler());
      xmlReader.parse(in);
    }
    catch(Exception e) {
      throw new GuanxiException(e);
    }
  }

  public String getSessionID() { return sessionID; }
  public String getAssertionID() { return assertionID; }
  public String getIssueInstant() { return issueInstant; }
  public String getIssuer() { return issuer; }
  public String getMajorVersion() { return majorVersion; }
  public String getMinorVersion() { return minorVersion; }

  public String getAttributeValue(String name) {
    return (String)attributes.get(name);
  }

  public Enumeration getAttributeNames() {
    return attributes.keys();
  }

  class BagContentHandler extends DefaultHandler {
    String name = null;
    boolean inAssertion, inGuanxiGuardSessionID, inAttribute, inAttributeValue;

    public void startElement(String uri, String localName, String qName, Attributes attrs) {
      if (localName.equals("GuanxiGuardSessionID")) {
        inGuanxiGuardSessionID = true;
      }

      if (localName.equals("Assertion")) {
        assertionID = getAttribute(attrs, "AssertionID");
        issueInstant = getAttribute(attrs, "IssueInstant");
        issuer = getAttribute(attrs, "Issuer");
        majorVersion = getAttribute(attrs, "MajorVersion");
        minorVersion = getAttribute(attrs, "MinorVersion");

        inAssertion = true;
      }

      if (localName.equals("Attribute")) {
        name = getAttribute(attrs, "AttributeName");

        inAttribute = true;
      }

      if (localName.equals("AttributeValue")) {
        inAttributeValue = true;
      }
    }

    public void endElement(String uri, String localName, String qName) {
      if (inGuanxiGuardSessionID) {
        sessionID = getValue();
        inGuanxiGuardSessionID = false;
      }

      if (localName.equals("Attribute")) {
        inAttribute = false;
      }

      if (localName.equals("AttributeValue")) {
        addSAMLAttribute(name, getValue());

        inAttributeValue = false;
      }

      value = new StringBuffer();
    }

    public void characters(char[] chars, int start, int length) {
      /**
       * We're currently processing an element. All character data from now until
       * the next endElement() call will be the data for this  element.
       */
      value.append(chars, start, length);
    }

    public String getValue() {
      return value.toString().trim();
    }

    String getAttribute(Attributes attrs, String name) {
      for (int count=0; count < attrs.getLength(); count++) {
        if (attrs.getLocalName(count).equals(name)) {
          return attrs.getValue(count);
        }
      }

      return "";
    }

    void addSAMLAttribute(String name, String value) {
      if (attributes.containsKey(name)) {
        String buffer = (String)attributes.get(name);
        buffer += ";" + value;
        attributes.remove(name);
        attributes.put(name, buffer);
      }
      else
        attributes.put(name, value);
    }
  }
}
