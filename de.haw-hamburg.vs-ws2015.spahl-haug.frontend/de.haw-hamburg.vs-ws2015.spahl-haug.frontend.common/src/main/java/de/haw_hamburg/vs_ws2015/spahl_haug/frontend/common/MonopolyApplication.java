package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common;

import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;

import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows.WindowManager;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.IServiceRepository;

public final class MonopolyApplication implements IApplication {
	private WindowManager windowManager;
	@Override
	public void start(final IApplicationLifecycle lifecycle) {
		windowManager = new WindowManager(lifecycle, new IServiceRepository() {

			@Override
			public String getService(final String name) throws Exception {
				return "http://127.0.0.1:4568/games";
			}
		});
		windowManager.start();

	}
}
