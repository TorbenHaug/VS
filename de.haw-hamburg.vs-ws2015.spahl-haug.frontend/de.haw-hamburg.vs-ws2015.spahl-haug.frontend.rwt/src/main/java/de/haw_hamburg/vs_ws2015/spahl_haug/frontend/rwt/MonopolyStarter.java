package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.rwt;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.spi.impl.rwt.RwtEntryPoint;

import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.MonopolyApplication;

public final class MonopolyStarter extends RwtEntryPoint {

	//URL: http://127.0.0.1:8080/helloWorldRwt/HelloWorld
	public MonopolyStarter() {
		super(new Runnable() {
			@Override
			public void run() {
				Toolkit.getApplicationRunner().run(new MonopolyApplication());
			}
		});
	}

}
