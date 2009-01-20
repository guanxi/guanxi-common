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
//: The Initial Developer of the Original Code is Alistair Young alistair@codebrane.com
//: All Rights Reserved.
//:

package org.guanxi.common.trust;

import javax.xml.namespace.NamespaceContext;
import javax.xml.XMLConstants;
import java.util.Iterator;

/**
 * NamespaceContext implementation for use by XPath expressions
 *
 * @author alistair
 */
public class SAMLNamespaceContext implements NamespaceContext {
  public String getNamespaceURI(String prefix) {
    if (prefix == null) throw new NullPointerException("Null prefix");

    if (prefix.equals("ds")) {
      return "http://www.w3.org/2000/09/xmldsig#";
    }

    return XMLConstants.NULL_NS_URI;
  }

  // This method isn't necessary for XPath processing.
  public String getPrefix(String uri) {
    throw new UnsupportedOperationException();
  }

  // This method isn't necessary for XPath processing either.
  public Iterator getPrefixes(String uri) {
    throw new UnsupportedOperationException();
  }
}
