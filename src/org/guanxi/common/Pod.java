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
   Revision 1.8  2007/01/17 17:36:32  alistairskye
   Added ServletContext storing

   Revision 1.7  2007/01/04 13:30:51  alistairskye
   Changed get/setAttributes() to get/setBag().
   Updated javadoc.

   Revision 1.6  2006/11/23 14:31:22  alistairskye
   Added support for request scheme and hostname to fix HTTPS bug when Guard comms are using HTTPS

   Revision 1.5  2006/11/17 14:55:23  alistairskye
   Updated setRequestParameters() to fix bug when adding extra request parameters for non spring applications

   Revision 1.4  2006/05/18 13:30:51  alistairskye
   Removed original request.
   Now only stores parameters from original request

   Revision 1.3  2006/05/18 09:24:23  alistairskye
   Now stores original request

   Revision 1.2  2005/08/12 12:48:31  alistairskye
   Added license

   Revision 1.1  2005/08/11 14:17:47  alistairskye
   Class for holding session specific information

*/

package org.guanxi.common;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

/**
 * The Pod contains information about the original request, the Guard session ID and the
 * attributes obtained from an IdP via the Engine, which are contained in a Bag object
 * within the Pod.
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 */
public class Pod {
  /** The ServletContext where the Pod lives */
  private ServletContext context = null;
  /** Original request is either HTTP or HTTPS */
  private String requestScheme = null;
  /** The host name of the original request */
  private String hostName = null;
  /** The URL of the original request */
  private String requestURL = null;
  /** The Guard session ID associated with this Pod */
  private String sessionID = null;
  /** The collection of SAML attributes */
  private Bag attributes = null;
  /** The parameters from the original request */
  private HashMap requestParameters = null;

  public void setContext(ServletContext context) {
    this.context = context;
  }

  public void setRequestScheme(String requestScheme) {
    this.requestScheme = requestScheme;
  }

  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  public void setRequestURL(String requestURL) {
    this.requestURL = requestURL;
  }

  public void setSessionID(String sessionID) {
    this.sessionID = sessionID;
  }

  public void setBag(Bag attributes) {
    this.attributes = attributes;
  }

  public void setRequestParameters(Map requestParameters) {
    if (this.requestParameters == null)
      this.requestParameters = new HashMap();
    this.requestParameters.putAll(requestParameters);
  }

  public ServletContext getContext() { return context; }
  public String getRequestScheme() { return requestScheme; }
  public String getHostName() { return hostName; }
  public String getRequestURL() { return requestURL; }
  public String getSessionID() { return sessionID; }
  public Bag getBag() { return attributes; }
  public HashMap getRequestParameters() { return requestParameters; }
}
