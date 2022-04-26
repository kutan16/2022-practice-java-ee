package nilotpal.certificate;

import nilotpal.config.PropertyConfig;
import org.glassfish.hk2.api.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.*;
import java.security.cert.*;

public class SSLSocketProvider implements Factory<SSLSocket> {
    private PropertyConfig propertyConfig = PropertyConfig.getInstance();
    private Socket socket;
    private final Logger log = LoggerFactory.getLogger(SSLSocketFactory.class);

    @Override
    public SSLSocket provide() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            TrustManager[] trustManagers = loadCustomTrustManagers();
            sslContext.init(null, trustManagers, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            socket = sslSocketFactory.createSocket();
            return configureCustomSSLSocket(socket);
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | KeyStoreException | IOException |
                 KeyManagementException e) {
            log.error("Can't get SSLSocket");
        }
        return null;
    }

    @Override
    public void dispose(SSLSocket instance) {

    }

    private TrustManager[] loadCustomTrustManagers() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, KeyStoreException {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        PKIXBuilderParameters pkixBuilderParameters = getPKIXBuilderParameters();
        trustManagerFactory.init(new CertPathTrustManagerParameters(pkixBuilderParameters));
        return trustManagerFactory.getTrustManagers();
    }

    private PKIXBuilderParameters getPKIXBuilderParameters() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, KeyStoreException {
        KeyStore trustStore = loadKeyStore();
        PKIXRevocationChecker checker = getPKIXRevocationChecker();
        PKIXBuilderParameters builderParameters = new PKIXBuilderParameters(trustStore, new X509CertSelector());
        builderParameters.addCertPathChecker(checker);
        return builderParameters;
    }

    public KeyStore loadKeyStore() {
        try (FileInputStream fileInputStream = new FileInputStream(propertyConfig.get("socket.keystore"))) {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(fileInputStream, propertyConfig.get("socket.keystorePassword").toCharArray());
            return keyStore;
        } catch (IOException | KeyStoreException | CertificateException | NoSuchAlgorithmException e) {
            log.error("Exception occurred during keystore initialization using provided file path and password");
            throw new RuntimeException("Exception occurred during keystore initialization using provided file path and password");
        }
    }

    private PKIXRevocationChecker getPKIXRevocationChecker() throws NoSuchAlgorithmException {
        CertPathBuilder certPathBuilder = CertPathBuilder.getInstance("PKIX");
        return (PKIXRevocationChecker) certPathBuilder.getRevocationChecker();
    }

    private SSLSocket configureCustomSSLSocket(Socket socket) {
        if(socket == null) {
            throw new ClassCastException("could not get SSLSocket");
        } else {
            return (SSLSocket) socket;
        }
    }
}
