package cn.ac.nya.nsgdx.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import cn.ac.nya.nsgdx.NyaEgg;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "NSGDX v0.01 | based on libGDX, NSASM";
		config.width = 854;
		config.height = 480;
		new LwjglApplication(new NyaEgg(), config);
	}
}
