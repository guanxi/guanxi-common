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
   Revision 1.6  2007/01/25 08:58:21  alistairskye
   Added support for chaining map files

   Revision 1.5  2007/01/16 09:26:19  alistairskye
   Updated to use XMLBeans

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

import org.guanxi.common.security.SecUtils;
import org.guanxi.xal.idp.AttributeMapDocument;
import org.guanxi.xal.idp.Map;
import org.apache.xmlbeans.XmlException;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Vector;

/**
 * <p>AttributeMap</p>
 * Provides mapping functionality for attributes.
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 * @author Aggie Booth bmb6agb@ds.leeds.ac.uk
 */
public class AttributeMap {
  /** Our map files */
  private Vector mapFiles = null;
  /** The new name of the attribute passed to map() */
  private String mappedName = null;
  /** The new value of the attribute passed to map() */
  private String mappedValue = null;

  /**
   * Default constructor
   *
   * @param mapXMLFile Full path and name of the map file to use
   * @throws GuanxiException if an error occurred parsing the map file
   */
  public AttributeMap(String mapXMLFile) throws GuanxiException {
    mapFiles = new Vector();
    loadMaps(mapXMLFile);
  }

  /**
   * Maps attributes and values. The method searches all <map> elements in the map file unless
   * mapName has been specified (is not null), in which case the method will only use that
   * particular map entry.
   * Once the mapping has been done, the application should use the helper methods to retrieve
   * the mapped attribute name and value:
   * getMappedName
   * getMappedValue
   *
   * The process of mapping is thus:
   *   Attribute is renamed if there is a mappedName entry
   *   Attribute value is changed to what is in the mappedValue entry if there is one
   *   Attribute value is further changed by any rule defined in the mappedRule entry if there is one
   *
   * @param mapName If not null, specifies the particular Map to use. This should be the content of
   * an 'name' attribute on a Map element. If this is null, all Map elements are searched until a
   * matching Map is found.
   * @param attrName The name of the attribute to map
   * @param attrValue The value to give the mapped attribute
   * @return true if the attribute was mapped otherwise false
   */
  public boolean map(String mapName, String attrName, String attrValue) {
    Pattern pattern = null;
    Matcher matcher = null;

    for (int mapFilesCount = 0; mapFilesCount < mapFiles.size(); mapFilesCount++) {
      AttributeMapDocument.AttributeMap attributeMap = (AttributeMapDocument.AttributeMap)mapFiles.get(mapFilesCount);

      for (int count=0; count < attributeMap.getMapArray().length; count++) {
        Map map = attributeMap.getMapArray(count);

        // Should we restrict the map to a certain mapping name?
        if ((mapName == null) || (mapName.equals(map.getName()))) {
          // Have we got the correct attribute to map?
          if (attrName.equals(map.getAttrName())) {
            pattern = Pattern.compile(map.getAttrValue());
            matcher = pattern.matcher(attrValue);

            // Match the value of the attribute before mapping
            if (matcher.find()) {
              // Rename the attribute...
              if (map.getMappedName() != null)
                mappedName = map.getMappedName();
              else
                mappedName = attrName;

              // Attribute value is what it says in the map...
              if (map.getMappedValue() != null)
                mappedValue = map.getMappedValue();
              // ...or just use the original attribute value
              else
                mappedValue = attrValue;

              // ...and transform the value if required
              if (map.getMappedRule() != null) {

                // Encrypt the attribute value
                if (map.getMappedRule().equals("encrypt"))
                  mappedValue = SecUtils.getInstance().encrypt(mappedValue);

                /* Append the domain to the attribute value by signalling to the
                 * attributor that it needs to add the domain.
                 */
                if (map.getMappedRule().equals("append_domain"))
                    mappedValue = mappedValue + "@";
              }

              return true;
            }
          }
        }
      } // for (int count=0; count < attributeMap.getMapArray().length; count++)
    } // for (int mapFilesCount = 0; mapFilesCount < mapFiles.size(); mapFilesCount++)

    return false;
  }

  /**
   * Retrieves the new name of the attribute passed to the map method
   *
   * @return the new name of the attribute passed to the map method
   * or null if map has not been called or no mappings were found.
   */
  public String getMappedName() {
    return mappedName;
  }

  /**
   * Retrieves the new value of the attribute passed to the map method
   *
   * @return the new value of the attribute passed to the map method
   * or null if map has not been called or no mappings were found.
   */
  public String getMappedValue() {
    return mappedValue;
  }

  /**
   * Loads up the chain of map files to use. The chain will always have at least one in it.
   *
   * @param mapXMLFile The full path and name of the root map file
   * @throws GuanxiException if an error occurs
   */
  private void loadMaps(String mapXMLFile) throws GuanxiException {
    try {
      // Load up the root map file
      AttributeMapDocument attrMapDoc = AttributeMapDocument.Factory.parse(new File(mapXMLFile));
      mapFiles.add(attrMapDoc.getAttributeMap());

      // Do we have any other map files to include?
      if (attrMapDoc.getAttributeMap().getIncludeArray() != null) {
        for (int c=0; c < attrMapDoc.getAttributeMap().getIncludeArray().length; c++) {
          // Load up any further included map files
          loadMaps(attrMapDoc.getAttributeMap().getIncludeArray(c).getMapFile());
        }
      }
    }
    catch(XmlException xe) {
      throw new GuanxiException(xe);
    }
    catch(IOException ioe) {
      throw new GuanxiException(ioe);
    }
  }
}
