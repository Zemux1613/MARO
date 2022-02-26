package Objects;

import java.awt.image.BufferedImage;

import Rendering.IOUtils;

public class MapItemCoin extends MapItem {
	
	private int value;
	
	private BufferedImage sprite;

	public MapItemCoin() {
		super("Coin", false, true, 64*5, 64*3, 64, 64, "Coin.png");
		value = 10;
		sprite = IOUtils.getBufferedImage("Coin_bag.png");
	}
	
	public int getValue() {
		return value;
	}
	
	public BufferedImage getSprite() {
		return sprite;
	}
	
}
