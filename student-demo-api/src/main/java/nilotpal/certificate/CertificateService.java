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
import java.util.List;
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

    @Inject
    public CertificateService(StoreTrustedCertificates storeTrustedCertificates) {
        this.storeTrustedCertificates = storeTrustedCertificates;
        this.propertyConfig = PropertyConfig.getInstance();
        this.hostnameVerifier = new DefaultHostnameVerifier();
    }

    /**
     * Initializing Property configuration , SSLSocket , Hostname Verifier
     *
     * @param sslSocketProvider A provider encapsulated SSLSocket
     */
//    @Inject
//    public CertificateService(Provider<SSLSocket> sslSocketProvider) {
//        this.propertyConfig = PropertyConfig.getInstance();
//        this.sslSocketProvider = sslSocketProvider;
//        this.hostnameVerifier = new DefaultHostnameVerifier();
//    }
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
                log.info("Socket handshake completed");
                SSLSession session = socket.getSession();
                log.info("SSLSession retrieved");
                if(hostnameVerifier.verify(url.getHost(), session)) {
                    log.info("Hostname verified");
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

    private CertificateInformation buildCertificateInformation(X509Certificate certificate) {
        CertificateInformation certificateInformation = new CertificateInformation();
//        Map<String, String> rdns = getRdns(certificate.getSubjectX500Principal());
        certificateInformation.setIssuer(certificate.getIssuerDN().getName());
        certificateInformation.setSubject(certificate.getSubjectDN().getName());
        certificateInformation.setValidFrom(certificate.getNotBefore());
        certificateInformation.setValidTo(certificate.getNotAfter());
        certificateInformation.setVersion(String.valueOf(certificate.getVersion()));
        certificateInformation.setOrganization("");
        certificateInformation.setDomainNames(null);
        certificateInformation.setLocation(null);
        return certificateInformation;
    }

//    private Map<String, String> getRdns(X500Principal subjectX500Principal) throws InvalidNameException {
//        Map<String, String> rdns = new HashMap<>();
//        rdns = new LdapName(subjectX500Principal.getName())
//                .getRdns()
//                .stream()
//                .filter(this::filterRequiredRdn)
//                .collect(Collectors.toMap(this::pickFromList),
//    }

    private String pickFromList(Rdn rdn) {
        return Stream.of("O", "CN", "C", "ST", "L")
                .filter(pick -> pick.equals(rdn.getType())).findFirst()
                .orElseThrow(() -> new RuntimeException(rdn.getType() + " is not an rdn used"));
    }

    private boolean filterRequiredRdn(Rdn rdn) {
        return Stream.of("O", "CN", "C", "ST", "L")
                .anyMatch(fromList -> fromList.equals(rdn.getType()));
    }
}
