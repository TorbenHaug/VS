package de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository;

import javax.net.ssl.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public final class SSLUtil{

	private static final TrustManager[] UNQUESTIONING_TRUST_MANAGER = new TrustManager[]{
			new X509TrustManager() {
				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers(){
					return null;
				}
				@Override
				public void checkClientTrusted( final X509Certificate[] certs, final String authType ){}
				@Override
				public void checkServerTrusted( final X509Certificate[] certs, final String authType ){}
			}
	};

	public  static void turnOffSslChecking() throws NoSuchAlgorithmException, KeyManagementException {
		// Install the all-trusting trust manager
		final SSLContext sc = SSLContext.getInstance("SSL");
		sc.init( null, UNQUESTIONING_TRUST_MANAGER, null );
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	public static void turnOnSslChecking() throws KeyManagementException, NoSuchAlgorithmException {
		// Return it to the initial state (discovered by reflection, now hardcoded)
		SSLContext.getInstance("SSL").init( null, null, null );
	}

	private SSLUtil(){
		throw new UnsupportedOperationException( "Do not instantiate libraries.");
	}
}
