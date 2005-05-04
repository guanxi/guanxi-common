/* CVS Header
   $Id$
   $Log$
   Revision 1.1  2005/05/04 13:29:58  alistairskye
   Guanxi::* will standardisde exceptions on GuanxiException

*/

package org.Guanxi.Common;

/**
 * <font size=5><b></b></font>
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 */
public class GuanxiException extends Exception {
  private int errorCode;

  public GuanxiException() {
  }

  public GuanxiException(Exception e) {
  }

  public GuanxiException (String inMessage) {
    super(inMessage);
  }

  public void setErrorCode(int inErrorCode) {
    errorCode = inErrorCode;
  }

  public int getErrorCode() {
    return errorCode;
  }
}
