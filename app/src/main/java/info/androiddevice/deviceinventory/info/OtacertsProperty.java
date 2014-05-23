package info.androiddevice.deviceinventory.info;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static android.util.Base64.encodeToString;

public class OtacertsProperty implements Property {


    @Override
    public String getName() {
        return "otacerts";
    }

    @Override
    public Object getProperty() throws JSONException {
        JSONObject result = new JSONObject();
        try {
            Iterator<Map.Entry<String, X509Certificate>> certs = getTrustedCerts(new File("/system/etc/security/otacerts.zip")).entrySet().iterator();
            while(certs.hasNext()) {
                Map.Entry<String, X509Certificate> cert = certs.next();
                result.put(cert.getKey(), encodeToString(cert.getValue().getEncoded(), Base64.DEFAULT));
            }
        } catch (IOException e) {
            return JSONObject.NULL;
        } catch (GeneralSecurityException e) {
            return JSONObject.NULL;
        }
        return result;
    }

    private static HashMap<String, X509Certificate> getTrustedCerts(File keystore) throws IOException, GeneralSecurityException {
        HashMap<String, X509Certificate> certs = new HashMap<String, X509Certificate>();

        ZipFile zip = new ZipFile(keystore);
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                InputStream is = zip.getInputStream(entry);
                try {
                    certs.put(entry.getName(), (X509Certificate) cf.generateCertificate(is));
                } finally {
                    is.close();
                }
            }
        } finally {
            zip.close();
        }
        return certs;
    }
}
