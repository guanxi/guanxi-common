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
   Revision 1.1  2007/01/24 09:58:46  alistairskye
   eduPerson definitions

*/

package org.guanxi.common.definitions;

/**
 * EduPerson Object Class Specification (200604)
 *
 * @link http://www.nmi-edit.org/eduPerson/internet2-mace-dir-eduperson-200604.html
 */
public class EduPerson {
  /** eduPerson namespace */
  public static final String EDUPERSON_URN = "urn:mace:dir:attribute-def:";

  /**
   * Specifies the person's affiliation within a particular security domain in broad categories
   * such as student, faculty, staff, alum, etc. The values consist of a left and right component
   * separated by an "@" sign. The left component is one of the values from the eduPersonAffiliation
   * controlled vocabulary.This right-hand side syntax of eduPersonScopedAffiliation intentionally
   * matches that used for the right-hand side values for eduPersonPrincipalName since both identify
   * a security domain. Multiple "@" signs are not recommended, but in any case, the first occurrence
   * of the "@" sign starting from the left is to be taken as the delimiter between components.
   * Thus, user identifier is to the left, security domain to the right of the first "@". This parsing
   * rule conforms to the POSIX "greedy" disambiguation method in regluar expression processing.
   * Permissible values:
   * See controlled vocabulary for eduPersonAffiliation.
   * Only these values are allowed to the left of the "@" sign.
   * The values to the right of the "@" sign should indicate a security domain.
   */
  public static final String EDUPERSON_SCOPED_AFFILIATION = EDUPERSON_URN + "eduPersonScopedAffiliation";
  public static final String EDUPERSON_SCOPED_AFFILIATION_DELIMITER = "@";
  public static final String EDUPERSON_SCOPED_AFFILIATION_SCOPE_ATTRIBUTE = "Scope";

  /**
   * Specifies the person's relationship(s) to the institution in broad categories.
   * Permissible values:
   * faculty, student, staff, alum, member, affiliate, employee
   */
  public static final String EDUPERSON_AFFILIATION = EDUPERSON_URN + "eduPersonAffiliation";

  /** URI (either URN or URL) that indicates a set of rights to specific resources */
  public static final String EDUPERSON_ENTITLEMENT = EDUPERSON_URN + "eduPersonEntitlement";
  
  /** Person's nickname, or the informal name by which they are accustomed to be hailed */
  public static final String EDUPERSON_NICKNAME = EDUPERSON_URN + "eduPersonNickname";

  /**
   * The distinguished name (DN) of the directory entry representing the
   * institution with which the person is associated
   */
  public static final String EDUPERSON_ORG_DN = EDUPERSON_URN + "eduPersonOrgDN";

  /**
   * The distinguished name(s) (DN) of the directory entries representing the
   * person's Organizational Unit(s). May be multivalued, as for example,
   * in the case of a faculty member with appointments in multiple departments
   * or a person who is a student in one department and an employee in another
   */
  public static final String EDUPERSON_ORG_UNIT_DN = EDUPERSON_URN + "eduPersonOrgUnitDN";

  /**
   * Specifies the person's PRIMARY relationship to the institution in broad categories.
   * Permissible values:
   * faculty, student, staff, alum, member, affiliate, employee
   */
  public static final String EDUPERSON_PRIMARY_AFFILIATION = EDUPERSON_URN + "eduPersonPrimaryAffiliation";

  /**
   * The distinguished name (DN) of the directory entry representing
   * the person's primary Organizational Unit(s)
   */
  public static final String EDUPERSON_PRIMARY_ORG_UNIT_DN = EDUPERSON_URN + "eduPersonPrimaryOrgUnitDN";

  /**
   * The "NetID" of the person for the purposes of inter-institutional authentication.
   * It should be represented in the form "user@scope" where scope defines a local security domain.
   * Multiple "@" signs are not recommended, but in any case, the first occurrence of the "@" sign
   * starting from the left is to be taken as the delimiter between components.
   * Thus, user identifier is to the left, security domain to the right of the first "@".
   * This parsing rule conforms to the POSIX "greedy" disambiguation method in regular expression
   * processing. When the scope is a registered domain name, the corresponding registrant organization
   * is to be taken as the scope. For example, francis@trinity.edu would imply that the identity behind
   * the ePPN has the "NetID" "francis" at the instituion of higher education that registered itself
   * with the domain name "trinity.edu." If other value styles are used, their semantics will have to
   * be profiled by the parties involved. Each value of scope defines a namespace within which the
   * assigned principal names are unique. Given this rule, no pair of eduPersonPrincipalName values
   * should clash. If they are the same, they refer to the same principal within the same administrative domain.
   */
  public static final String EDUPERSON_PRINCIPAL_NAME = EDUPERSON_URN + "eduPersonPrincipalName";
  public static final String EDUPERSON_TARGETED_ID = EDUPERSON_URN + "eduPersonTargetedID";

  // The rest is a mish-mash from other schemata
  public static final String EDUPERSON_AUDIO = "audio";
  public static final String EDUPERSON_CN = "cn";
  public static final String EDUPERSON_DESCRIPTION = "description";
  public static final String EDUPERSON_DISPLAY_NAME = "displayName";
  public static final String EDUPERSON_FACSIMILE_TELEPHONE_NUMBER = "facsimileTelephoneNumber";
  public static final String EDUPERSON_GIVEN_NAME = "givenName";
  public static final String EDUPERSON_HOME_PHONE = "homePhone";
  public static final String EDUPERSON_HOME_POSTAL_ADDRESS = "homePostalAddress";
  public static final String EDUPERSON_INITIALS = "initials";
  public static final String EDUPERSON_JPEG_PHOTO = "jpegPhoto";
  public static final String EDUPERSON_LOCALITY_NAME = "localityName";
  public static final String EDUPERSON_LABELLED_URI = "labeledURI";
  public static final String EDUPERSON_MAIL = "mail";
  public static final String EDUPERSON_MANAGER = "manager";
  public static final String EDUPERSON_MOBILE = "mobile";
  public static final String EDUPERSON_O = "o";
  public static final String EDUPERSON_OU = "ou";
  public static final String EDUPERSON_PAGER = "pager";
  public static final String EDUPERSON_POSTAL_ADDRESS = "postalAddress";
  public static final String EDUPERSON_POSTAL_CODE = "postalCode";
  public static final String EDUPERSON_POST_OFFICE_BOX = "postOfficeBox";
  public static final String EDUPERSON_PREFERRED_LANGUAGE = "preferredLanguage";
  public static final String EDUPERSON_SEE_ALSO = "seeAlso";
  public static final String EDUPERSON_SN = "sn";
  public static final String EDUPERSON_ST = "st";
  public static final String EDUPERSON_STREET = "street";
  public static final String EDUPERSON_TELEPHONE_NUMBER = "telephoneNumber";
  public static final String EDUPERSON_TITLE = "title";
  public static final String EDUPERSON_UID = "uid";
  public static final String EDUPERSON_UNIQUE_IDENTIFIER = "uniqueIdentifier";
  public static final String EDUPERSON_USER_CERTIFICATE = "userCertificate";
  public static final String EDUPERSON_USER_PASSWORD = "userPassword";
  public static final String EDUPERSON_USER_SMIME_CERTIFICATE = "userSMIMECertificate";
  public static final String EDUPERSON_X500_UNIQUE_IDENTIFIER = "x500uniqueIdentifier";
}
