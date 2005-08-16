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
   Revision 1.3  2005/08/16 14:57:51  alistairskye
   Added license

   Revision 1.2  2005/08/16 14:56:57  alistairskye
   Added isError()

   Revision 1.1  2005/08/16 12:36:53  alistairskye
   Errors class

*/

package org.guanxi.common;

/**
 * <font size=5><b></b></font>
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 */
public class Errors {
  public static final String ENGINE_WAYF_LOCATION_EXCEPTION = "ENGINE_WAYF_LOCATION_EXCEPTION";
  public static final String ENGINE_WAYF_LOCATION_NO_GUARD_ID = "ENGINE_WAYF_LOCATION_NO_GUARD_ID";
  public static final String ENGINE_WAYF_LOCATION_GUARD_FAILED_VERIFICATION = "ENGINE_WAYF_LOCATION_GUARD_FAILED_VERIFICATION";
  public static final String ENGINE_CURRENTLY_INITIALISING = "ENGINE_CURRENTLY_INITIALISING";

  public static boolean isError(String text) {
    if ((text.equals(ENGINE_WAYF_LOCATION_EXCEPTION)) ||
        (text.equals(ENGINE_WAYF_LOCATION_NO_GUARD_ID)) ||
        (text.equals(ENGINE_WAYF_LOCATION_GUARD_FAILED_VERIFICATION)) ||
        (text.equals(ENGINE_CURRENTLY_INITIALISING)))
      return true;
    else
      return false;
  }
}
