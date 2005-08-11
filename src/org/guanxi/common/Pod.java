/* CVS Header
   $Id$
   $Log$
   Revision 1.1  2005/08/11 14:17:47  alistairskye
   Class for holding session specific information

*/

package org.guanxi.common;

/**
 * <font size=5><b></b></font>
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 */
public class Pod {
  private String requestURL = null;
  private String sessionID = null;
  private Bag attributes = null;

  public void setRequestURL(String requestURL) {
    this.requestURL = requestURL;
  }

  public void setSessionID(String sessionID) {
    this.sessionID = sessionID;
  }

  public void setAttributes(Bag attributes) {
    this.attributes = attributes;
  }

  public String getRequestURL() { return requestURL; }
  public String getSessionID() { return sessionID; }
  public Bag getAttributes() { return attributes; }
}
