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
//: The Original Code is Guanxi::Common (http://www.guanxi.uhi.ac.uk).
//:
//: The Initial Developer of the Original Code is Alistair Young alistair@smo.uhi.ac.uk.
//: Portions created by SMO WWW Development Group are Copyright (C) 2005 SMO WWW Development Group.
//: All Rights Reserved.
//:
//: Contributor(s):
//: Alistair Young alistair@smo.uhi.ac.uk Lead developer Guanxi::Common
//:
/* CVS Header
   $Id$
   $Log$
   Revision 1.1  2005/04/03 14:23:21  seanskye
   Initial import of code tree into module on SF.

*/

package org.Guanxi.Common;

import javax.servlet.http.HttpServletRequest;
import java.util.Hashtable;
import java.util.Enumeration;
import java.rmi.server.UID;

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
  public static Hashtable getRequestParameters (HttpServletRequest request) {
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
}
