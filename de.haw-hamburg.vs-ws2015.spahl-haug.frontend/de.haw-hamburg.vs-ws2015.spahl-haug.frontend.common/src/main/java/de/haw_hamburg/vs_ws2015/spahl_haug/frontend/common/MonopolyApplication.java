package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common;

import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;

import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows.WindowManager;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.ServiceRepository;

public final class MonopolyApplication implements IApplication {
	private WindowManager windowManager;
	@Override
	public void start(final IApplicationLifecycle lifecycle) {
		final WindowManager manager = new WindowManager(
				lifecycle,
				new ServiceRepository(){
					@Override
					public String getService(final String name) throws Exception {
						if (name.equals("gamesldt")) {
							return "http://192.168.99.100:4568/games";
						} else if(name.equals("boardsldt")) {
							return "http://192.168.99.100:4569/boards";
						}else{
							return "http://192.168.99.100:4570/dice";
						}
					}
				});
		manager.startWindowing();

	}
}
