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
   Revision 1.4  2007/02/09 10:59:57  alistairskye
   Added support for storing ServletContext for logout functionality

   Revision 1.3  2005/07/11 10:52:24  alistairskye
   Package restructure

   Revision 1.2  2005/04/15 10:00:56  alistairskye
   License updated

*/

package org.guanxi.common;

import javax.servlet.ServletContext;
import java.security.Principal;

/**
 * <font size=5><b></b></font>
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 */
public class GuanxiPrincipal implements Principal {
  private String name = null;
  private String id = null;
  private String providerID = null;
  private Object userData = null;
  private ServletContext context = null;

  public GuanxiPrincipal() {}

  public void setName(String inName) {
    name = inName;
  }

  public String getName() {
    return name;
  }

  public void setID(String inID) {
    id = inID;
  }

  public String getID() {
    return id;
  }

  public void setProviderID(String inProviderID) {
    providerID = inProviderID;
  }

  public String getProviderID() {
    return providerID;
  }

  public void setUserData(String inUserData) {
    userData = inUserData;
  }

  public Object getUserData() {
    return userData;
  }

  public void setServletContext(ServletContext context) {
    this.context = context;
  }

  public ServletContext getServletContext() {
    return context;
  }
}
