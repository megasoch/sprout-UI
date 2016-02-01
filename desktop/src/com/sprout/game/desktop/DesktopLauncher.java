package com.sprout.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sprout.game.SproutGame;

import java.io.IOException;

public class DesktopLauncher {
	public static void main (String[] arg) throws IOException {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 650;
		config.height = 600;
		config.fullscreen = false;
		config.resizable = false;
		config.title = "Sprout-Client";
		new LwjglApplication(new SproutGame(), config);
	}
}
