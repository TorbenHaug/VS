package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.api.widgets.ITextControl;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextFieldBluePrint;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.widgets.blueprint.BPF;

public class LoginWindow {

	final IFrameBluePrint frameBp;
	final IFrame frame;
	final IButtonBluePrint buttonBp;
	final IButton button;
	final IInputFieldBluePrint<String> userNameBp;
	final IInputField userName;

	public LoginWindow(){
		frameBp = BPF.frame();
		frameBp.setSize(new Dimension(1024, 768)).setTitle("Monopoly - Login");
		frameBp.setCloseable(false);

		frame = Toolkit.createRootFrame(frameBp);
		frame.setLayout(new MigLayoutDescriptor("[]", "[]"));

		userNameBp = BPF.inputFieldString();
		userName = frame.add(userNameBp);

		buttonBp = BPF.button().setText("Login");
		button = frame.add(buttonBp);
		button.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				System.out.println(userName.getText());
			}
		});

		frame.setVisible(true);
	}
}
