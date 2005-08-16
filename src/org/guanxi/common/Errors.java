/* CVS Header
   $Id$
   $Log$
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
