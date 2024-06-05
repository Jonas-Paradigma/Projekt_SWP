package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.mygdx.game.Screens.TitleScreen;

public class ScreenGame extends Game {
	public static boolean enableSound = true;
	public Music menuMusic;
	public Music backgroundMusic;

	@Override
	public void create() {
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/menu.mp3"));
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/background.mp3"));
		if (enableSound) {
			menuMusic.setLooping(true);
			menuMusic.play();
		}
		setScreen(new TitleScreen(this));
	}

	public void playMenuMusic() {
		if (enableSound && !menuMusic.isPlaying()) {
			menuMusic.play();
		}
	}

	public void stopMenuMusic() {
		if (menuMusic.isPlaying()) {
			menuMusic.stop();
		}
	}

	public void playBackgroundMusic() {
		if (enableSound && !backgroundMusic.isPlaying()) {
			backgroundMusic.setLooping(true);

		}
	}

	public void stopBackgroundMusic() {
		if (backgroundMusic.isPlaying()) {
			backgroundMusic.stop();
		}
	}

	public void toggleSound(boolean enabled) {
		enableSound = enabled;
		if (enableSound) {
			playMenuMusic();
		} else {
			menuMusic.pause();
			backgroundMusic.pause();
		}
	}

	public boolean isSoundEnabled() {
		return enableSound;
	}
}