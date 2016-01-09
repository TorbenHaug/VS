package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.restservice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestBody;

import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows.WindowManager;

@Path("/")
public class RestService {
	private final static Map<String, WindowManager> playerServices = new ConcurrentHashMap<String, WindowManager>();

	public static void registerPlayerService(final String name, final WindowManager windowManager){
		playerServices.put(name, windowManager);
	}
	public static void removePlayerService(final String name, final WindowManager windowManager){
		playerServices.remove(name);
	}

	@POST
	@Path("{name}/player/turn")
	public Response turn(@PathParam("name") final String name){
		System.out.println("anounce" + name);
		final WindowManager manager = playerServices.get(name);
		if(manager == null){
			return Response.status(Response.Status.NOT_FOUND).entity("Not Found").build();
		}
		new Thread(){
			@Override
			public void run() {
				manager.anounceTurn();
			}
		}.start();
		return Response.ok().build();
	}

	@POST
	@Path("{name}/player/event")
	public Response event(@PathParam("name") final String name,@RequestBody final String uri){
		final WindowManager manager = playerServices.get(name);
		if(manager == null){
			return Response.status(Response.Status.NOT_FOUND).entity("Not Found").build();
		}
		manager.anounceEvent(uri);
		return Response.ok().build();
	}
}