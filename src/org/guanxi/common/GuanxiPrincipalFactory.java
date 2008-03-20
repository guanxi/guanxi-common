/* CVS Header
   $
   $
*/

package org.guanxi.common;

import javax.servlet.http.HttpServletRequest;

/**
 * Factory for creating profile specific GuanxiPrincipal objects
 */
public interface GuanxiPrincipalFactory {
  /**
   * Create a new GuanxiPrincipal
   * @param request Standard HttpServletRequest
   * @return GuanxiPrincipal configured for a particular profile
   */
  public GuanxiPrincipal createNewGuanxiPrincipal(HttpServletRequest request);
}
