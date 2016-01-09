package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows;

import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.widgets.IFrame;

public interface IMyFrame extends IFrame{
	IUiThreadAccess getUIThread();
}
