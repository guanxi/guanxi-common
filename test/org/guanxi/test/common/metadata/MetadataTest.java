package org.guanxi.test.common.metadata;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.guanxi.common.definitions.Guanxi;
import org.guanxi.common.job.SAML2MetadataParserConfig;
import org.guanxi.common.job.ShibbolethSAML2MetadataParser;
import org.guanxi.common.trust.TrustUtils;
import org.junit.Test;

import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;

import static junit.framework.Assert.*;

public class MetadataTest extends  ShibbolethSAML2MetadataParser {
  private static final String UK_FED_TEST_METADATA_URL = "http://metadata.ukfederation.org.uk/ukfederation-test.xml";
  private static final String UK_FED_TEST_METADATA_PEM_LOCATION = "http://metadata.ukfederation.org.uk/ukfederation.pem";

  @Test
  public void test() {
    try {
      SAML2MetadataParserConfig parserConfig = new SAML2MetadataParserConfig();
      parserConfig.setJobClass("org.guanxi.idp.job.SAML2MetadataParser");
      parserConfig.setKey("TEST");
      parserConfig.setMetadataURL(UK_FED_TEST_METADATA_URL);
      parserConfig.setPemLocation(UK_FED_TEST_METADATA_PEM_LOCATION);
      parserConfig.setSigned(true);
      parserConfig.setWho("org.guanxi.test.common.metadata.MetadataTest");
      parserConfig.setCacheDir("/tmp");
      parserConfig.setCacheFile("metadatatest.xml");
      parserConfig.init();

      config = parserConfig;
      init();

      Security.addProvider(new BouncyCastleProvider());

      if (!verifyMetadataFingerprint()) {
        fail("Metadata failed key validation");
      }

      if (!verifyMetadataSignature()) {
        fail("Metadata failed signature validation");
      }

      PublicKey metadataKey = getX509FromMetadataSignature().getPublicKey();
      PublicKey fedKey = TrustUtils.pem2x509(UK_FED_TEST_METADATA_PEM_LOCATION).getPublicKey();
      if (!TrustUtils.compareKeys(metadataKey, fedKey)) {
        fail("Metadata failed key validation");
      }

      loadAndCacheEntities();
    }
    catch(Exception e) {
      fail(e.getMessage());
    }
    finally {
      Provider[] providers = Security.getProviders();
      for (int i=0; i < providers.length; i++) {
        if (providers[i].getName().equalsIgnoreCase(Guanxi.BOUNCY_CASTLE_PROVIDER_NAME)) {
          Security.removeProvider(Guanxi.BOUNCY_CASTLE_PROVIDER_NAME);
        }
      }
    }
  }
}
