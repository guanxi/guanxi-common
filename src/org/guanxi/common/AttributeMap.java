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
   Revision 1.9  2008/01/18 15:48:50  alistairskye
   Updated to support mapping a single attribute multiple times

   Revision 1.8  2007/05/24 11:12:56  alistairskye
   Updated to allow paths relative to WEB-INF to be used for ARPs

   Revision 1.7  2007/01/25 11:16:34  alistairskye
   Now based on provderId groupings of mapping rules

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
import org.guanxi.xal.idp.MapProvider;
import org.apache.xmlbeans.XmlException;

import javax.servlet.ServletContext;
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
  /** Our provider groupings and mapping rules */
  private Vector maps = null;
  private Vector providers = null;
  /** Servlet context for resolving relative paths */
  private ServletContext context = null;
  /** The new names of the attribute passed to map() */
  private Vector mappedNames = null;
  /** The new values of the attribute passed to map() */
  private Vector mappedValues = null;

  /**
   * Default constructor
   *
   * @param mapXMLFile Full path and name of the map file to use
   * @param context ServletContext
   * @throws GuanxiException if an error occurred parsing the map file
   */
  public AttributeMap(String mapXMLFile, ServletContext context) throws GuanxiException {
    this.context = context;
    maps = new Vector();
    providers = new Vector();
    mappedNames = new Vector();
    mappedValues = new Vector();
    loadMaps(mapXMLFile);
  }

  /**
   * Maps attributes and values.
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
   * @param spProviderId This should be the content of a 'providerId' attribute on a map element.
   * If no maps are found that match this value then no attributes will be mapped. If any mapping
   * rules are service provider agnostic, they should have a "providerId" set to "*" on their
   * provider element.
   * @param attrName The name of the attribute to map
   * @param attrValue The value to give the mapped attribute
   * @return true if the attribute was mapped otherwise false
   */
  public boolean map(String spProviderId, String attrName, String attrValue) {
    int index = -1;
    boolean mapped = false;
    Pattern pattern = null;
    Matcher matcher = null;

    mappedNames.clear();
    mappedValues.clear();

    // Look for provider groups that are either specific to this providerId or wildcard
    for (int providersCount = 0; providersCount < providers.size(); providersCount++) {
      MapProvider provider = (MapProvider)providers.get(providersCount);

      if ((provider.getProviderId().equals(spProviderId)) || provider.getProviderId().equals("*")) {
        // Load up the mapping references for this provider
        for (int mapRefsCount = 0; mapRefsCount < provider.getMapRefArray().length; mapRefsCount++) {
          // Look for the map in the maps cache
          for (int mapsCount = 0; mapsCount < maps.size(); mapsCount++) {
            Map map = (Map)maps.get(mapsCount);
            if (map.getName().equals(provider.getMapRefArray(mapRefsCount).getName())) {
              // Have we got the correct attribute to map?
              if (attrName.equals(map.getAttrName())) {
                pattern = Pattern.compile(map.getAttrValue());
                matcher = pattern.matcher(attrValue);

                // Match the value of the attribute before mapping
                if (matcher.find()) {
                  index++;
                  mapped = true;
                  
                  // Rename the attribute...
                  if (map.getMappedName() != null)
                    mappedNames.add(map.getMappedName());
                  else
                    mappedNames.add(attrName);

                  // Attribute value is what it says in the map...
                  if (map.getMappedValue() != null)
                    mappedValues.add(map.getMappedValue());
                  // ...or just use the original attribute value
                  else
                    mappedValues.add(attrValue);

                  // ...and transform the value if required
                  if (map.getMappedRule() != null) {
                    // Encrypt the attribute value
                    if (map.getMappedRule().equals("encrypt"))
                      mappedValues.set(index, SecUtils.getInstance().encrypt((String)mappedValues.get(index)));

                    /* Append the domain to the attribute value by signalling to the
                     * attributor that it needs to add the domain.
                     */
                    if (map.getMappedRule().equals("append_domain"))
                      mappedValues.set(index, mappedValues.get(index) + "@");
                  }

                  //return true;
                } // if (matcher.find()) {
              } // if (attrName.equals(map.getAttrName()))
            } // if (map.getName().equals(provider.getMapRefArray(mapRefsCount)))
          } // for (int mapsCount = 0; mapsCount < maps.size(); mapsCount++)
        } // for (int mapRefsCount = 0; mapRefsCount < provider.getMapRefArray().length; mapRefsCount++)
      } // if ((provider.getProviderId().equals(spProviderId)) || provider.getProviderId().equals("*"))
    } // for (int providersCount = 0; providersCount < providers.size(); providersCount++)

    // No mappings found for the attribute
    return mapped;
  }

  /**
   * Retrieves the new name of the attribute passed to the map method
   *
   * @return the new name of the attribute passed to the map method
   * or null if map has not been called or no mappings were found.
   */
  public String[] getMappedNames() {
    return (String[])mappedNames.toArray(new String[mappedNames.size()]);
  }

  /**
   * Retrieves the new value of the attribute passed to the map method
   *
   * @return the new value of the attribute passed to the map method
   * or null if map has not been called or no mappings were found.
   */
  public String[] getMappedValues() {
    return (String[])mappedValues.toArray(new String[mappedValues.size()]);
  }

  /**
   * Loads up the chain of map files to use. The chain will always have at least one in it.
   *
   * @param mapXMLFile The full path and name of the root map file
   * @throws GuanxiException if an error occurs
   */
  private void loadMaps(String mapXMLFile) throws GuanxiException {
    // Sort out the path to the ARP file
    String mapFile = null;
    if ((mapXMLFile.startsWith("WEB-INF")) ||
        (mapXMLFile.startsWith("/WEB-INF"))) {
      mapFile = context.getRealPath(mapXMLFile);
    }
    else
      mapFile = mapXMLFile;

    try {
      // Load up the root map file
      AttributeMapDocument attrMapDoc = AttributeMapDocument.Factory.parse(new File(mapFile));

      // Cache all the maps...
      for (int c = 0; c < attrMapDoc.getAttributeMap().getMapArray().length; c++ ) {
        maps.add(attrMapDoc.getAttributeMap().getMapArray(c));
      }

      // ...and providers
      for (int c = 0; c < attrMapDoc.getAttributeMap().getProviderArray().length; c++ ) {
        providers.add(attrMapDoc.getAttributeMap().getProviderArray(c));
      }

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
