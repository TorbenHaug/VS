package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows;

import java.net.URL;

import org.jowidgets.api.image.IImage;
import org.jowidgets.api.image.ImageFactory;
import org.jowidgets.api.layout.NullLayout;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IIcon;
import org.jowidgets.api.widgets.ITextLabel;
import org.jowidgets.tools.widgets.base.CompositeControl;
import org.jowidgets.tools.widgets.blueprint.BPF;

public class PlayerInfo extends CompositeControl{

	String playerId;
	private final IIcon imageIcon;
	private final ITextLabel idLabel;
	private int pos = 0;
	private final String color;
	private final ITextLabel moneyLabel;

	public PlayerInfo(final IContainer parent, final String playerId, final String color) {
		super(parent);
		this.playerId = playerId;
		this.color = color;
		final IComposite composite = getComposite();
		composite.setLayout(NullLayout.get());
		setSize(200, 20);
		final URL imageUrl = PlayerPosition.class.getClassLoader().getResource(color + ".png");
		final IImage image = ImageFactory.createImage(imageUrl);
		imageIcon = composite.add(BPF.icon(image));
		imageIcon.setSize(20, 20);
		imageIcon.setPosition(0, 0);
		idLabel = composite.add(BPF.textLabel(playerId));
		idLabel.setPosition(25,0);
		idLabel.setSize(55,20);
		moneyLabel = composite.add(BPF.textLabel("1000"));
		moneyLabel.setPosition(90,0);
		moneyLabel.setSize(55,20);
	}

	public int getPos() {
		return pos;
	}

	public void setPos(final int pos) {
		this.pos = pos;
	}

	public String getColor() {
		return color;
	}

	public void updateMoney(final int amount){
		moneyLabel.setText(String.valueOf(amount));
	}


}
