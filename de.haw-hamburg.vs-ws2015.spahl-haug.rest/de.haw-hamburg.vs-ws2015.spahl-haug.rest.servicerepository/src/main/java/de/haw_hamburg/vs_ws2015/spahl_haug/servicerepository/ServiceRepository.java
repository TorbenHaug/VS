package de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class ServiceRepository implements IServiceRepository {

	/* (non-Javadoc)
	 * @see de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.IServiceRepository#getService(java.lang.String)
	 */
	@Override
	public String getService(final String name) throws Exception{
		SSLUtil.turnOffSslChecking();
		final RestTemplate template = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		final String base64Creds = "YWJxMzI5OkRLR1JIZDIwMTUy";
		headers.add("Authorization", "Basic " + base64Creds);
		final HttpEntity<String> request = new HttpEntity<String>(headers);

		final ResponseEntity<NamedServicesDTO> services = template.exchange("https://vs-docker.informatik.haw-hamburg.de/ports/8053/services/of/name/" + name, HttpMethod.GET, request, NamedServicesDTO.class);
		if(!services.hasBody() && (services.getBody().getServices().size() == 0)){
			throw new Exception("Unable to get Services for " + name);
		}else{
			final ResponseEntity<RestServiceDTO> service = template.exchange("https://vs-docker.informatik.haw-hamburg.de/ports/8053" +
					services.getBody().getServices().get(services.getBody().getServices().size()-1), HttpMethod.GET, request, RestServiceDTO.class);
			//					template.getForEntity(
			//					"https://vs-docker.informatik.haw-hamburg.de/ports/8053" +
			//							services.getBody().getServices().get(services.getBody().getServices().size()-1), RestServiceDTO.class);
			if (!service.hasBody()){
				throw new Exception("Unable to get Services for " + name);
			}
			else{
				return service.getBody().getUri();
			}
		}
	}
}
