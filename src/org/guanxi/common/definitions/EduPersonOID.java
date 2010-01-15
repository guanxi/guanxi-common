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
//: The Initial Developer of the Original Code is Alistair Young alistair@codebrane.com
//: All Rights Reserved.
//:

package org.guanxi.common.definitions;

/**
 * EduPerson Object Class Specification (200806)
 *
 * @author alistair
 * @link http://middleware.internet2.edu/eduperson/docs/internet2-mace-dir-eduperson-200806.html
 */
public class EduPersonOID {
  // The prefix used in a SAML2 Attribute
  public static final String ATTRIBUTE_NAME_PREFIX = "urn:oid:";

  // eduPerson definitions
  public static final String OID_EDUPERSON_AFFILIATION = "1.3.6.1.4.1.5923.1.1.1.1";
  public static final String OID_EDUPERSON_NICKNAME = "1.3.6.1.4.1.5923.1.1.1.2";
  public static final String OID_EDUPERSON_ORG_DN = "1.3.6.1.4.1.5923.1.1.1.3";
  public static final String OID_EDUPERSON_ORGUNIT_DN = "1.3.6.1.4.1.5923.1.1.1.4";
  public static final String OID_EDUPERSON_PRIMARY_AFFILIATION = "1.3.6.1.4.1.5923.1.1.1.5";
  public static final String OID_EDUPERSON_PRINCIPAL_NAME = "1.3.6.1.4.1.5923.1.1.1.6";
  public static final String OID_EDUPERSON_ENTITLEMENT = "1.3.6.1.4.1.5923.1.1.1.7";
  public static final String OID_EDUPERSON_PRIMARY_ORGUNIT_DN = "1.3.6.1.4.1.5923.1.1.1.8";
  public static final String OID_EDUPERSON_SCOPED_AFFILIATION = "1.3.6.1.4.1.5923.1.1.1.9";
  public static final String OID_EDUPERSON_TARGETED_ID = "1.3.6.1.4.1.5923.1.1.1.10";
  public static final String OID_EDUPERSON_ASSURANCE = "1.3.6.1.4.1.5923.1.1.1.11";

  // Common attribute definitions
  public static final String OID_AUDIO = "0.9.2342.19200300.100.1.55";
  public static final String OID_CN = "2.5.4.3";
  public static final String OID_DESCRIPTION = "2.5.4.13";
  public static final String OID_DISPLAY_NAME = "2.16.840.1.113730.3.1.241";
  public static final String OID_FACSIMILE_TELEPHONE_NUMBER = "2.5.4.23";
  public static final String OID_GIVEN_NAME = "2.5.4.42";
  public static final String OID_HOME_PHONE = "0.9.2342.19200300.100.1.20";
  public static final String OID_HOME_POSTAL_ADDRESS = "0.9.2342.19200300.100.1.39";
  public static final String OID_INITIALS = "2.5.4.43";
  public static final String OID_JPEG_PHOTO = "0.9.2342.19200300.100.1.60";
  public static final String OID_L = "2.5.4.7";
  public static final String OID_LABELLED_URI = "1.3.6.1.4.1.250.1.57";
  public static final String OID_MAIL = "0.9.2342.19200300.100.1.3";
  public static final String OID_MANAGER = "0.9.2342.19200300.100.1.10";
  public static final String OID_MOBILE = "0.9.2342.19200300.100.1.41";
  public static final String OID_O = "2.5.4.10";
  public static final String OID_OU = "2.5.4.11";
  public static final String OID_PAGER = "0.9.2342.19200300.100.1.42";
  public static final String OID_POSTAL_ADDRESS = "2.5.4.16";
  public static final String OID_POSTAL_CODE = "2.5.4.17";
  public static final String OID_POST_OFFICE_BOX = "2.5.4.18";
  public static final String OID_PREFERRED_LANGUAGE = "2.16.840.1.113730.3.1.39";
  public static final String OID_SEE_ALSO = "2.5.4.34";
  public static final String OID_SN = "2.5.4.4";
  public static final String OID_ST = "2.5.4.8";
  public static final String OID_STREET = "2.5.4.9";
  public static final String OID_TELEPHONE_NUMBER = "2.5.4.20";
  public static final String OID_TITLE = "2.5.4.12";
  public static final String OID_UID = "0.9.2342.19200300.100.1.1";
  public static final String OID_UNIQUE_IDENTIFIER = "0.9.2342.19200300.100.1.44";
  public static final String OID_USER_CERTIFICATE = "2.5.4.36";
  public static final String OID_USER_PASSWORD = "2.5.4.35";
  public static final String OID_SMIME_CERTIFICATE = "2.16.840.1.113730.3.1.40";
  public static final String OID_X500_UNIQUE_IDENTIFIER = "2.5.4.45";
}
