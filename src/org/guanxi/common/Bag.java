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
   Revision 1.5  2007/01/04 13:38:25  alistairskye
   Rewritten to remove SAX parsing of raw SOAP message from Engine.
   Now stores original SAML Response as a String.

   Revision 1.4  2006/01/23 12:53:50  alistairskye
   Updated to use SAMUEL parser properties

   Revision 1.3  2005/08/12 12:48:31  alistairskye
   Added license

   Revision 1.2  2005/08/12 09:03:14  alistairskye
   Added SAML attribute gathering

   Revision 1.1  2005/08/11 14:18:52  alistairskye
   SAML attribute parser and container

*/

package org.guanxi.common;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * <font size=5><b></b></font>
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 */
public class Bag {
  /** The value used as a delimiter in multi-valued attributes */
  public static final String ATTR_VALUE_DELIM = ";";

  /** String representing the raw SAML Response */
  private String samlResponse = null;
  /** The attributes as Strings */
  private Hashtable attributes = null;

  public Bag() {
    attributes = new Hashtable();
  }

  /**
   * Determines whether this Bag contains any attributes.
   *
   * @return true if this Bag contains attributes otherwise false
   */
  public boolean hasAttributes() {
    return !attributes.isEmpty();
  }

  /**
   * Adds an attribute to the Bag. The method takes care of multiple valued
   * attributes. Attributes with multiple values have their individual values
   * separated by ATTR_VALUE_DELIM.
   * 
   * @param name The name of the attribute to add
   * @param value The attribute's value
   */
  public void addAttribute(String name, String value) {
    if (attributes.containsKey(name)) {
      String buffer = (String)attributes.get(name);
      buffer += ATTR_VALUE_DELIM + value;
      attributes.remove(name);
      attributes.put(name, buffer);
    }
    else
      attributes.put(name, value);
  }

  /**
   * Returns the value of the specified attribute. If the attribute has
   * multiple values, they will be separated by ATTR_VALUE_DELIM.
   *
   * @param name The attribute name
   * @return The values of the attribute, separated by ATTR_VALUE_DELIM if
   * there are multiple values.
   */
  public String getAttributeValue(String name) {
    return (String)attributes.get(name);
  }

  /**
   * Returns all the names of the attributes stored in this Bag.
   *
   * @return Enumeration listing all stored attribute names.
   */
  public Enumeration getAttributeNames() {
    return attributes.keys();
  }

  /**
   * Sets the raw SAML Response. This can be used by applications to parse to
   * an object model.
   *
   * @param samlResponse String containing the raw SAML Response from which the
   * attributes have been extracted and passed to addAttribute.
   */
  public void setSamlResponse(String samlResponse) { this.samlResponse = samlResponse; }

  /**
   * Retrieves the raw SAML Response from which the attributes stored in this bag were
   * extracted.
   *
   * @return String representing the raw SAML Response. 
   */
  public String getSamlResponse() { return samlResponse; }
}
