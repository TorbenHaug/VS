package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows;

import org.jowidgets.api.layout.NullLayout;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputFieldBluePrint;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.tools.controller.WindowAdapter;
import org.jowidgets.tools.widgets.base.Frame;
import org.jowidgets.tools.widgets.blueprint.BPF;

public class LoginWindow extends Frame implements IMyFrame{
	private final IButtonBluePrint buttonBp;
	private final IButton button;
	private final IInputFieldBluePrint<String> userNameBp;
	private final IInputField<String> userName;
	private final IUiThreadAccess uiThreadAccess;

	public LoginWindow(final ILoginActions loginActions) {
		super("Login");
		uiThreadAccess = Toolkit.getUiThreadAccess();
		setLayout(NullLayout.get());
		setSize(300, 125);
		addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosed() {
				try {
					loginActions.closeWindow();
				} catch (final RepositoryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		userNameBp = BPF.inputFieldString();
		userName = add(userNameBp);
		userName.setSize(260, 30);
		userName.setPosition(20, 45);

		buttonBp = BPF.button().setText("Login");
		button = add(buttonBp);
		button.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				try {
					loginActions.onLogin(userName.getText());
				} catch (final RepositoryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		button.setSize(110, 30);
		button.setPosition(20, 85);
		setVisible(true);
	}

	@Override
	public IUiThreadAccess getUIThread() {
		return uiThreadAccess;
	}
}
