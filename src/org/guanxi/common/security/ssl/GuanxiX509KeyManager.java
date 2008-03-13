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

package org.guanxi.common.security.ssl;

import javax.net.ssl.X509KeyManager;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.net.Socket;

public class GuanxiX509KeyManager implements X509KeyManager {
  X509KeyManager baseKM = null;
  String alias = null;

  public GuanxiX509KeyManager(X509KeyManager keyManager, String alias) {
    baseKM = keyManager;
    this.alias = alias;
  }

  public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
    return alias;
  }

  public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
    return baseKM.chooseServerAlias(keyType, issuers, socket);
  }

  public X509Certificate[] getCertificateChain(String alias) {
    return baseKM.getCertificateChain(alias);
  }

  public String[] getClientAliases(String keyType, Principal[] issuers) {
    return baseKM.getClientAliases(keyType, issuers);
  }

  public PrivateKey getPrivateKey(String alias) {
    return baseKM.getPrivateKey(alias);
  }

  public String[] getServerAliases(String keyType, Principal[] issuers) {
    return baseKM.getServerAliases(keyType, issuers);
  }
}
