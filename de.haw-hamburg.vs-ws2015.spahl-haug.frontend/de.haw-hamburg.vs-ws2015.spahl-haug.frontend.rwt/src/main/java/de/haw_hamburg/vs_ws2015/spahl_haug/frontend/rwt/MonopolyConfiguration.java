package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.rwt;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.Application.OperationMode;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.eclipse.rap.rwt.client.WebClient;

public final class MonopolyConfiguration implements ApplicationConfiguration {

	@Override
	public void configure(final Application application) {
		application.setOperationMode(OperationMode.SWT_COMPATIBILITY);
		final Map<String, String> properties = new HashMap<String, String>();
		properties.put(WebClient.PAGE_TITLE, "Monopoly");
		application.addEntryPoint("/monopoly", MonopolyStarter.class, properties);
	}
}
