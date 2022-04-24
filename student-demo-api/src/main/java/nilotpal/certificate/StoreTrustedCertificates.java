package nilotpal.certificate;

import nilotpal.config.PropertyConfig;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.net.ssl.SSLSocket;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.stream.Collectors;

public class StoreTrustedCertificates {
    private final PropertyConfig propertyConfig = PropertyConfig.getInstance();
    private final Provider<SSLSocket> socketProvider;
    private final Logger log = LoggerFactory.getLogger(StoreTrustedCertificates.class);

    @Inject
    public StoreTrustedCertificates(Provider<SSLSocket> socketProvider) throws CertificateException, IOException {
        this.socketProvider = socketProvider;
        List<X509Certificate> trustedCertificates = downloadCertificatesFromUrl(propertyConfig.get("download.cert.url"));
        KeyStore keyStore = loadKeyStore();
        writeCertificatesToStore(trustedCertificates, keyStore, propertyConfig.get("socket.keystore"), propertyConfig.get("socket.keystorePassword"));
    }

    private List<X509Certificate> downloadCertificatesFromUrl(String url) throws CertificateException, IOException {
        log.info("Trying to download certificates from  {}", url);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X509", new BouncyCastleProvider());
        InputStream inputStream = new URL(url).openStream();
        return certificateFactory.generateCertificates(inputStream).stream()
                .map(certificate -> (X509Certificate) certificate)
                .collect(Collectors.toList());

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

    private void writeCertificatesToStore(List<X509Certificate> trustedCertificates, KeyStore keyStore, String trustStorePath, String trustStorePassword) {
        log.info("Trying to write the downloaded certificates to the truststore");
        for (X509Certificate certificate : trustedCertificates) {
            try {
                keyStore.setCertificateEntry(certificate.getIssuerDN().getName(), certificate);
            } catch (KeyStoreException e) {
                log.error("Exception occurred during certificates load to truststore");
            }
        }
        try (FileOutputStream outputStream = new FileOutputStream(trustStorePath)) {
            keyStore.store(outputStream, trustStorePassword.toCharArray());
            log.info("Successfully written {} certificates to truststore", trustedCertificates.size());
        } catch (CertificateException | KeyStoreException | NoSuchAlgorithmException | IOException e) {
            log.error("Exception occurred while writing certificates to truststore");
        }

    }

}
