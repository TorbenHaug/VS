package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ch.qos.logback.core.net.ssl.SSL;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.RegisterServiceDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.ResponseRegisterServiceDTO;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

	private int getServerPort() {
		final Properties prop = new Properties();
		final String propFileName = "application.properties";

		final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

		if (inputStream != null) {
			try {
				prop.load(inputStream);
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

		}
		return Integer.valueOf(prop.getProperty("server.port"));
	}

	@Override
	public void onApplicationEvent(final ContextRefreshedEvent arg0) {
		final RegisterServiceDTO dto = new RegisterServiceDTO();
		dto.setName("spahl_haug_games");
		dto.setDescription("GamesService von Louisa Spahl und Torben Haug");
		dto.setService("games");
		try {
			SSLUtil.turnOffSslChecking();
			final String uri = "http://" + getLocalHostLANAddress().getHostAddress() + ":"+ getServerPort() + "/games";
			Main.setOwnURI(uri);
			dto.setUri(uri);
		} catch (final UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final RestTemplate restTemplate = new RestTemplate();
		final String base64Creds = "YWJxMzI5OkRLR1JIZDIwMTUy";
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		final HttpEntity<RegisterServiceDTO> request = new HttpEntity<RegisterServiceDTO>(dto,headers);
		final ResponseEntity<ResponseRegisterServiceDTO> registerServiceDTO = restTemplate.postForEntity("https://vs-docker.informatik.haw-hamburg.de/ports/8053/services", request, ResponseRegisterServiceDTO.class);
		Main.setServiceID(registerServiceDTO.getBody().get_uri());
	}
	/**
	 * Returns an <code>InetAddress</code> object encapsulating what is most likely the machine's LAN IP address.
	 * <p/>
	 * This method is intended for use as a replacement of JDK method <code>InetAddress.getLocalHost</code>, because
	 * that method is ambiguous on Linux systems. Linux systems enumerate the loopback network interface the same
	 * way as regular LAN network interfaces, but the JDK <code>InetAddress.getLocalHost</code> method does not
	 * specify the algorithm used to select the address returned under such circumstances, and will often return the
	 * loopback address, which is not valid for network communication. Details
	 * <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4665037">here</a>.
	 * <p/>
	 * This method will scan all IP addresses on all network interfaces on the host machine to determine the IP address
	 * most likely to be the machine's LAN address. If the machine has multiple IP addresses, this method will prefer
	 * a site-local IP address (e.g. 192.168.x.x or 10.10.x.x, usually IPv4) if the machine has one (and will return the
	 * first site-local address if the machine has more than one), but if the machine does not hold a site-local
	 * address, this method will return simply the first non-loopback address found (IPv4 or IPv6).
	 * <p/>
	 * If this method cannot find a non-loopback address using this selection algorithm, it will fall back to
	 * calling and returning the result of JDK method <code>InetAddress.getLocalHost</code>.
	 * <p/>
	 *
	 * @throws UnknownHostException If the LAN address of the machine cannot be found.
	 */
	private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
		try {
			InetAddress candidateAddress = null;
			// Iterate all NICs (network interface cards)...
			for (final Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
				final NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
				// Iterate all IP addresses assigned to each card...
				for (final Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
					final InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
					if (!inetAddr.isLoopbackAddress()) {
						if (inetAddr.getHostAddress().startsWith("141")) {
							// Found non-loopback site-local address. Return it immediately...
							return inetAddr;
						}
						else if ((candidateAddress == null) && (inetAddr instanceof Inet4Address)) {
							// Found non-loopback address, but not necessarily site-local.
							// Store it as a candidate to be returned if site-local address is not subsequently found...
							candidateAddress = inetAddr;
							// Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
							// only the first. For subsequent iterations, candidate will be non-null.
						}
					}
				}
			}
			if (candidateAddress != null) {
				// We did not find a site-local address, but we found some other non-loopback address.
				// Server might have a non-site-local address assigned to its NIC (or it might be running
				// IPv6 which deprecates the "site-local" concept).
				// Return this non-loopback candidate address...
				return candidateAddress;
			}
			// At this point, we did not find a non-loopback address.
			// Fall back to returning whatever InetAddress.getLocalHost() returns...
			final InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
			if (jdkSuppliedAddress == null) {
				throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
			}
			return jdkSuppliedAddress;
		}
		catch (final Exception e) {
			final UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
			unknownHostException.initCause(e);
			throw unknownHostException;
		}
	}
}
