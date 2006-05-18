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
   Revision 1.3  2006/05/18 09:24:23  alistairskye
   Now stores original request

   Revision 1.2  2005/08/12 12:48:31  alistairskye
   Added license

   Revision 1.1  2005/08/11 14:17:47  alistairskye
   Class for holding session specific information

*/

package org.guanxi.common;

import javax.servlet.ServletRequest;

/**
 * <font size=5><b></b></font>
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 */
public class Pod {
  private String requestURL = null;
  private String sessionID = null;
  private Bag attributes = null;
  private ServletRequest request = null;

  public void setRequestURL(String requestURL) {
    this.requestURL = requestURL;
  }

  public void setSessionID(String sessionID) {
    this.sessionID = sessionID;
  }

  public void setAttributes(Bag attributes) {
    this.attributes = attributes;
  }

  public void setRequest(ServletRequest request) {
    this.request = request;
  }

  public String getRequestURL() { return requestURL; }
  public String getSessionID() { return sessionID; }
  public Bag getAttributes() { return attributes; }
  public ServletRequest getRequest() { return request; }
}
