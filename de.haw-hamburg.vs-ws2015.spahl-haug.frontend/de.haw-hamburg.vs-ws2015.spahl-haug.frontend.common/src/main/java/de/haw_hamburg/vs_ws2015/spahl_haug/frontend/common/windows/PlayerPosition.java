package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows;

import java.net.URL;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.image.IImage;
import org.jowidgets.api.image.ImageFactory;
import org.jowidgets.api.layout.NullLayout;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IIcon;
import org.jowidgets.api.widgets.ITextLabel;
import org.jowidgets.tools.widgets.base.CompositeControl;
import org.jowidgets.tools.widgets.blueprint.BPF;

public class PlayerPosition extends CompositeControl{

	private final IIcon imageIconBlue;
	private final IIcon imageIconGreen;
	private final IIcon imageIconRed;
	private final IIcon imageIconYellow;
	private final IIcon imageIconPurple;
	private final IIcon imageIconBlack;
	private boolean rotated = false;
	private final ITextLabel ownerLabel;

	public PlayerPosition(final IContainer parent) {
		super(parent);
		final IComposite composite = getComposite();
		composite.setLayout(NullLayout.get());
		setSize(40, 60);

		ownerLabel = composite.add(BPF.textLabel("0"));
		ownerLabel.setPosition(-10,0);
		ownerLabel.setSize(55,20);

		final URL blue = PlayerPosition.class.getClassLoader().getResource("blue.png");
		final IImage imageBlue = ImageFactory.createImage(blue);
		imageIconBlue = composite.add(BPF.icon(imageBlue));
		imageIconBlue.setSize(20, 20);
		imageIconBlue.setPosition(0, 0);
		imageIconBlue.setVisible(false);

		final URL green = PlayerPosition.class.getClassLoader().getResource("green.png");
		final IImage imageGreen = ImageFactory.createImage(green);
		imageIconGreen = composite.add(BPF.icon(imageGreen));
		imageIconGreen.setSize(20, 20);
		imageIconGreen.setPosition(0, 20);
		imageIconGreen.setVisible(false);

		final URL red = PlayerPosition.class.getClassLoader().getResource("red.png");
		final IImage imageRed = ImageFactory.createImage(red);
		imageIconRed = composite.add(BPF.icon(imageRed));
		imageIconRed.setSize(20, 20);
		imageIconRed.setPosition(20, 0);
		imageIconRed.setVisible(false);

		final URL yellow = PlayerPosition.class.getClassLoader().getResource("yellow.png");
		final IImage imageYellow = ImageFactory.createImage(yellow);
		imageIconYellow = composite.add(BPF.icon(imageYellow));
		imageIconYellow.setSize(20, 20);
		imageIconYellow.setPosition(20, 20);
		imageIconYellow.setVisible(false);

		final URL purple = PlayerPosition.class.getClassLoader().getResource("purple.png");
		final IImage imagePurple = ImageFactory.createImage(purple);
		imageIconPurple = composite.add(BPF.icon(imagePurple));
		imageIconPurple.setSize(20, 20);
		imageIconPurple.setPosition(0, 40);
		imageIconPurple.setVisible(false);

		final URL black = PlayerPosition.class.getClassLoader().getResource("black.png");
		final IImage imageBlack = ImageFactory.createImage(black);
		imageIconBlack = composite.add(BPF.icon(imageBlack));
		imageIconBlack.setSize(20, 20);
		imageIconBlack.setPosition(20, 40);
		imageIconBlack.setVisible(false);

	}

	public void setGreenVisible(final boolean b){
		imageIconGreen.setVisible(b);
	}
	public void setRedVisible(final boolean b){
		imageIconRed.setVisible(b);
	}

	public void setYellowVisible(final boolean b){
		imageIconYellow.setVisible(b);
	}
	public void setPurpleVisible(final boolean b){
		imageIconPurple.setVisible(b);
	}
	public void setBlackVisible(final boolean b){
		imageIconBlack.setVisible(b);
	}
	public void setBlueVisible(final boolean b){
		imageIconBlue.setVisible(b);
	}
	public void rotate(){
		if(rotated){
			setSize(40,60);
			imageIconPurple.setPosition(0, 40);
			imageIconBlack.setPosition(20, 40);
			rotated = false;
		}else{
			setSize(60,40);
			imageIconPurple.setPosition(40, 0);
			imageIconBlack.setPosition(40, 20);
			rotated = true;
		}

	}
}
