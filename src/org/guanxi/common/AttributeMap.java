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
   Revision 1.4  2006/08/30 12:27:45  alistairskye
   Updated to provide mapping to support eduPersonPrincipalName

   Revision 1.3  2006/04/05 12:27:53  alistairskye
   Updated to allow passthrough of mapped attribute's original value if no value rules are specified.

   Revision 1.2  2005/10/20 16:09:23  alistairskye
   Added support for mapping attributes to rules

   Revision 1.1  2005/09/19 12:09:28  alistairskye
   Class for mapping attributes from one form/value to another

*/

package org.guanxi.common;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.guanxi.samuel.utils.ParserPool;
import org.guanxi.samuel.utils.XUtils;
import org.guanxi.samuel.exception.ParserPoolException;
import org.guanxi.common.definitions.Guanxi;
import org.guanxi.common.security.SecUtils;
import java.io.File;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * <font size=5><b></b></font>
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 * @author Aggie Booth bmb6agb@ds.leeds.ac.uk
 */
public class AttributeMap {
  private static final String MAP_ATTR_ID = "id";
  private static final String MAP_ATTR_NAME = "attrName";
  private static final String MAP_ATTR_VALUE = "attrValue";
  private static final String MAP_ATTR_MAPPED_NAME = "mappedName";
  private static final String MAP_ATTR_MAPPED_VALUE = "mappedValue";
  private static final String MAP_ATTR_MAPPED_RULE = "mappedRule";

  ParserPool parser = null;
  XUtils xUtils = null;
  Document map = null;
  NodeList mapNodes = null;
  String mappedName, mappedValue;

  public AttributeMap(String mapXMLFile) throws GuanxiException {
    // Get a parser
    try {
      parser = ParserPool.getInstance();
    }
    catch(ParserPoolException ppe) {
      throw new GuanxiException(ppe);
    }

    xUtils = XUtils.getInstance();

    // Load up the config file
    try {
      map = parser.parse(new File(mapXMLFile));
    }
    catch(ParserPoolException ppe) {
      throw new GuanxiException(ppe);
    }

    // Get an iterator for the <server> nodes in the config file
    mapNodes = map.getElementsByTagNameNS(Guanxi.NS_IDP_NAME_IDENTIFIER, "map");
  }

  public boolean map(String id, String attrName, String attrValue) {
    Node mapNode = null;
    NamedNodeMap mapAttrs = null;
    Pattern pattern = null;
    Matcher matcher = null;

    for (int count=0; count < mapNodes.getLength(); count++) {
      mapNode = mapNodes.item(count);
      mapAttrs = mapNode.getAttributes();

      // Should we restrict the map to a certain mapping id?
      if ((id == null) || (id.equals(mapAttrs.getNamedItem(MAP_ATTR_ID).getNodeValue()))) {
        // Have we got the correct attribute to map?
        if (attrName.equals(mapAttrs.getNamedItem(MAP_ATTR_NAME).getNodeValue())) {
          pattern = Pattern.compile(mapAttrs.getNamedItem(MAP_ATTR_VALUE).getNodeValue());
          matcher = pattern.matcher(attrValue);

          // Match the value of the attribute before mapping
          if (matcher.find()) {
            // Rename the attribute...
            mappedName = mapAttrs.getNamedItem(MAP_ATTR_MAPPED_NAME).getNodeValue();
            // ...and transform the value
            if (mapAttrs.getNamedItem(MAP_ATTR_MAPPED_RULE) != null) {
                
              if (mapAttrs.getNamedItem(MAP_ATTR_MAPPED_RULE).getNodeValue().equals("encrypt"))
                mappedValue = SecUtils.getInstance().encrypt(attrValue);
              
              if (mapAttrs.getNamedItem(MAP_ATTR_MAPPED_RULE).getNodeValue().equals("append_domain"))
                  // signal to the attributor that it needs to add the domain
                  mappedValue = attrValue + "@";
            }
            else {
              if (mapAttrs.getNamedItem(MAP_ATTR_MAPPED_VALUE) != null)
                mappedValue = mapAttrs.getNamedItem(MAP_ATTR_MAPPED_VALUE).getNodeValue();
              else
                mappedValue = attrValue;
            }

            return true;
          }
        }
      }
    }

    return false;
  }

  public String getMappedName() {
    return mappedName;
  }

  public String getMappedValue() {
    return mappedValue;
  }
}
