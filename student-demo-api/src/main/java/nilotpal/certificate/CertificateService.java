package nilotpal.certificate;

import nilotpal.config.PropertyConfig;
import nilotpal.entity.CertificateInformation;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.naming.ldap.Rdn;
import javax.net.ssl.*;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * The class which will fetch certificates from urls and display information
 */
public class CertificateService {
    private final PropertyConfig propertyConfig;
    //    private final Provider<SSLSocket> sslSocketProvider;
    private final HostnameVerifier hostnameVerifier;

    private final StoreTrustedCertificates storeTrustedCertificates;
    private final Logger log = LoggerFactory.getLogger(CertificateService.class);

    /**
     * @param storeTrustedCertificates Injects {@link StoreTrustedCertificates}
     */
    @Inject
    public CertificateService(StoreTrustedCertificates storeTrustedCertificates) {
        this.storeTrustedCertificates = storeTrustedCertificates;
        this.propertyConfig = PropertyConfig.getInstance();
        this.hostnameVerifier = new DefaultHostnameVerifier();
    }

    /**
     * Get the certificate information from the provided url
     *
     * @param url The url from which to fetch certificates
     * @return {@link CertificateInformation CertificateInformation} object
     */
    public CertificateInformation getCertificateInformation(URI url) {
        List<Certificate> certificates = getCertificates(url);
        if(null != certificates && !certificates.isEmpty()) {
            return buildCertificateInformation((X509Certificate) certificates.get(0));
        }
        return null;
    }

    /**
     * This will fetch all the available certificates from the provided url
     *
     * @param url The url from which the certificates need to be extracted
     * @return A list of {@link Certificate}
     */
    public List<Certificate> getCertificates(URI url) {
        final int port = url.getPort() == -1 ? 443 : url.getPort();
        final int timeout = Integer.parseInt(propertyConfig.get("socket.timeout"));
//        try (SSLSocket socket = sslSocketProvider.get()) {
        try (SSLSocket socket = (SSLSocket) getCustomSocketFactory().createSocket()) {
            if(null == socket) {
                log.info("SSLSocket is null");
                return null;
            } else {
                socket.connect(new InetSocketAddress(url.getHost(), port), timeout);
                socket.startHandshake();
                SSLSession session = socket.getSession();
                if(hostnameVerifier.verify(url.getHost(), session)) {
                    return Arrays.asList(session.getPeerCertificates());
                } else {
                    log.info("Unable to verify the hostname from url");
                    return null;
                }
            }

        } catch (SocketTimeoutException | ConnectException | KeyStoreException | NoSuchAlgorithmException |
                 UnrecoverableKeyException | KeyManagementException | CertificateException e) {
            log.error("Unable to connect to host : {} , port : {}", url.getHost(), port);
            log.error(e.getMessage());
            return null;
        } catch (IOException e) {
            log.error("Unable to get connection info for host : {} , port : {}", url.getHost(), port);
            log.error(e.getMessage());
            return null;
        }
    }
    /**
     * Get the keystore and loads it to the SocketFactory
     *
     * @return {@link SSLSocketFactory}
     * @throws KeyStoreException This exception is thrown if a key in the keystore cannot be recovered.
     * @throws UnrecoverableKeyException This exception is thrown if a key in the keystore cannot be recovered.
     * @throws KeyManagementException This is the general key management exception for all operations dealing with key management.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws CertificateException This exception indicates one of a variety of certificate problems
     * @throws NoSuchAlgorithmException This exception is thrown when a particular cryptographic algorithm is requested but is not available in the environment.
     */
    private SSLSocketFactory getCustomSocketFactory() throws KeyStoreException, UnrecoverableKeyException, KeyManagementException, IOException, CertificateException, NoSuchAlgorithmException {
        log.info("Using getCustomSocketFactory()");
        String keyStoreFile = propertyConfig.get("socket.keystore");
        String keyStorePassword = propertyConfig.get("socket.keystorePassword");
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(Files.newInputStream(Paths.get(keyStoreFile)), keyStorePassword.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX");
        kmf.init(ks, keyStorePassword.toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
        tmf.init(ks);
        SSLContext sc = SSLContext.getInstance("TLSv1.2");
        TrustManager[] trustManagers = tmf.getTrustManagers();
        sc.init(kmf.getKeyManagers(), trustManagers, null);
        return sc.getSocketFactory();

    }

    /**
     * @param certificate The {@link X509Certificate}
     * @return {@link CertificateInformation}
     */
    private CertificateInformation buildCertificateInformation(X509Certificate certificate) {
        CertificateInformation certificateInformation = new CertificateInformation();
        Map<String, String> issuer = getIssuerDN(certificate.getIssuerDN());
        Map<String, String> subject = getSubjectDN(certificate.getSubjectDN());
        certificateInformation.setIssuer(issuer.get("O"));
        certificateInformation.setSubject(subject.get("CN"));
        certificateInformation.setValidFrom(certificate.getNotBefore().toString());
        certificateInformation.setValidTo(certificate.getNotAfter().toString());
        certificateInformation.setVersion(String.valueOf(certificate.getVersion()));
        certificateInformation.setAlgorithm(certificate.getSigAlgName());
        certificateInformation.setOrganization(subject.get("O"));
        certificateInformation.setDomainName(subject.get("CN"));
        certificateInformation.setLocation(
                new CertificateInformation.Location(subject.get("C"), subject.get("ST"), subject.get("L")));
        return certificateInformation;
    }

    /**
     * @param subjectDN The {@link Principal} for subject
     * @return A Map of extracted subject names
     */
    private Map<String, String> getSubjectDN(Principal subjectDN) {
        Map<String, String> rdns = new HashMap<>();
        String[] subject = subjectDN.getName().split(",");
        String[] deepElements = null;
        for (String eachElement : subject) {
            deepElements = eachElement.split("=");
            if(deepElements.length == 2) {
                rdns.put(deepElements[0].trim(), deepElements[1]);
            }
        }
        return rdns;
    }

    /**
     * @param issuerDN The {@link  Principal} for issuer
     * @return A Map of extracted issuer names
     */
    private Map<String, String> getIssuerDN(Principal issuerDN) {
        Map<String, String> rdns = new HashMap<>();
        String[] issuer = issuerDN.getName().split(",");
        String[] deepElements = null;
        for (String eachElement : issuer) {
            deepElements = eachElement.split("=");
            if(deepElements.length == 2) {
                rdns.put(deepElements[0].trim(), deepElements[1]);
            }
        }
        return rdns;
    }

}
