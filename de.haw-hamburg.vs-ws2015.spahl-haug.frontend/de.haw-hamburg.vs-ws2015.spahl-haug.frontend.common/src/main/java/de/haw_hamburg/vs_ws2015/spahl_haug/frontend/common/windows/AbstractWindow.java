package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.tools.widgets.blueprint.BPF;

public class AbstractWindow implements IDisposable{
	private final IFrameBluePrint frameBp;
	private final IFrame frame;

	public AbstractWindow(final boolean closeable, final Dimension size){
		frameBp = BPF.frame();
		frameBp.setCloseable(closeable);
		frameBp.setSize(size);
		frame = Toolkit.createRootFrame(frameBp);
	}

	protected IFrameBluePrint getFrameBp() {
		return frameBp;
	}

	protected IFrame getFrame() {
		return frame;
	}

	@Override
	public void dispose() {
		frame.dispose();
	}
}
