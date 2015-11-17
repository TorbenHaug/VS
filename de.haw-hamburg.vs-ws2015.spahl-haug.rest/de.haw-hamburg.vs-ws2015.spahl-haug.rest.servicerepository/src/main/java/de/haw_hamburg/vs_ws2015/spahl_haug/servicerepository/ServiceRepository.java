package de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class ServiceRepository {

	public String getService(final String name) throws Exception{
		SSLUtil.turnOffSslChecking();
		final RestTemplate template = new RestTemplate();
		final ResponseEntity<NamedServicesDTO> services = template.getForEntity("https://vs-docker.informatik.haw-hamburg.de/ports/8053/services/of/name/" + name, NamedServicesDTO.class);
		if(!services.hasBody() && (services.getBody().getServices().size() == 0)){
			throw new Exception("Unable to get Services for " + name);
		}else{
			final ResponseEntity<RestServiceDTO> service = template.getForEntity(
					"https://vs-docker.informatik.haw-hamburg.de/ports/8053" +
							services.getBody().getServices().get(services.getBody().getServices().size()-1), RestServiceDTO.class);
			if (!service.hasBody()){
				throw new Exception("Unable to get Services for " + name);
			}
			else{
				return service.getBody().getUri();
			}
		}
	}
}
