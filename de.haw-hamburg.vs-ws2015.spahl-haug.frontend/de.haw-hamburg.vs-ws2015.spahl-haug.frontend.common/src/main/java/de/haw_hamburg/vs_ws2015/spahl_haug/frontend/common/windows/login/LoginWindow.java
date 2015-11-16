package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows.login;

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

import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows.AbstractWindow;

public class LoginWindow extends AbstractWindow{

	private final IButtonBluePrint buttonBp;
	private final IButton button;
	private final IInputFieldBluePrint<String> userNameBp;
	private final IInputField userName;

	public LoginWindow(final ILoginActions loginActions){
		super(false,new Dimension(230, 120));
		final String textFieldCC = "growx, w 0::";
		getFrame().setTitle("Monopoly - Login");
		getFrame().setLayout(new MigLayoutDescriptor("wrap", "[grow, 0::]", "[]"));

		userNameBp = BPF.inputFieldString();
		userName = getFrame().add(userNameBp, textFieldCC);
		//userName.setPreferredSize(new Dimension(180, 50));

		buttonBp = BPF.button().setText("Login");
		button = getFrame().add(buttonBp);
		button.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				loginActions.onLogin(userName.getText());
			}
		});

		getFrame().setVisible(true);
	}
}
