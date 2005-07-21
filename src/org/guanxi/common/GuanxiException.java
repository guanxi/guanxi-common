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
   Revision 1.3  2005/07/21 13:02:01  alistairskye
   GuanxiException(Exception) now calls superclass

   Revision 1.2  2005/07/11 10:52:24  alistairskye
   Package restructure

   Revision 1.1  2005/05/04 13:29:58  alistairskye
   Guanxi::* will standardisde exceptions on GuanxiException

*/

package org.guanxi.common;

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
    super(e);
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
