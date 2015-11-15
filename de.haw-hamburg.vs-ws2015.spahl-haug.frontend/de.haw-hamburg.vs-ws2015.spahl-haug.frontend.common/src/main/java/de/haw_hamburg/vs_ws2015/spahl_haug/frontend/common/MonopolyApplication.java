package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class MonopolyApplication implements IApplication {

	@Override
	public void start(final IApplicationLifecycle lifecycle) {
		new LoginWindow();
		//
		//		//Create a frame BluePrint with help of the BluePrintFactory (BPF)
		//		final IFrameBluePrint frameBp = BPF.frame();
		//		frameBp.setSize(new Dimension(1024, 768)).setTitle("Monopoly");
		//		frameBp.setCloseable(false);
		//
		//		//Create a frame with help of the Toolkit and BluePrint.
		//		//This convenience method finishes the ApplicationLifecycle when
		//		//the root frame will be closed.
		//		final IFrame frame = Toolkit.createRootFrame(frameBp, lifecycle);
		//
		//		//Use a simple MigLayout with one column and one row
		//		frame.setLayout(new MigLayoutDescriptor("[]", "[]"));
		//
		//		//Create a button BluePrint with help of the BluePrintFactory (BPF)
		//		final IButtonBluePrint buttonBp = BPF.button().setText("Hello World");
		//
		//		//Add the button defined by the BluePrint to the frame
		//		final IButton button = frame.add(buttonBp);
		//
		//		//Add an ActionListener to the button
		//		button.addActionListener(new IActionListener() {
		//			@Override
		//			public void actionPerformed() {
		//				System.out.println("Hello World");
		//			}
		//		});
		//
		//		//set the root frame visible
		//		frame.setVisible(true);
	}
}
