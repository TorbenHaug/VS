package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common;

import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;

import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows.WindowManager;

public final class MonopolyApplication implements IApplication {
	private WindowManager windowManager;
	@Override
	public void start(final IApplicationLifecycle lifecycle) {
		windowManager = new WindowManager();
		windowManager.start();

	}
}
