package game;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.nio.file.Paths;

public class Audio {

    private static Media backMusic = new Media(new File("resources/backMusic.wav").toURI().toString());
    private static MediaPlayer music = new MediaPlayer(backMusic);
    private static AudioClip shot = new AudioClip(Paths.get("resources/playerShot.wav").toUri().toString());
    private static AudioClip playerDeath = new AudioClip(Paths.get("resources/playerDeath.wav").toUri().toString());
    private static AudioClip enemyDeath = new AudioClip(Paths.get("resources/enemyDeath.wav").toUri().toString());
    private static AudioClip win = new AudioClip(Paths.get("resources/win.wav").toUri().toString());

    public static void playBackgroundMusic() {
        music.setVolume(0.03);
        music.setCycleCount(MediaPlayer.INDEFINITE);
        music.play();
    }

    public static void stopBackgroundMusic() {
        music.stop();
        music.dispose();
    }

    public static void playPlayerShotSound() {
        shot.setVolume(0.2);
        shot.play();
    }

    public static void playPlayerDeathSound() {
        playerDeath.setVolume(0.03);
        playerDeath.play();
    }

    public static void playEnemyDeathSound() {
        enemyDeath.setVolume(0.04);
        enemyDeath.play();
    }

    public static void playWinSound() {
        win.setVolume(0.07);
        win.play();
    }
}
