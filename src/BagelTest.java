import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import bagel.AbstractGame;
import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.MouseButtons;
import bagel.Window;
import bagel.util.Point;


public class BagelTest extends AbstractGame {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "Gareeb Spotify";
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");
    private final Image WHITE_LINE = new Image("res/double-white-road-lines.jpg");
    private final Image SPOTIFY_LOGO = new Image("res/BlueSpotify.png");
    private final Image PLAY_BUTTON = new Image("res/PlayButton.png");
    private final Image PAUSE_BUTTON = new Image("res/PauseButton.png");
    private final Image NEXT_BUTTON = new Image("res/NextButton.png");
    private final Image BORDERLINE = new Image("res/BorderlinePic.png");
    private final Font spotifyFont = new Font("res/conformable.otf", 150);
    private static final Point SPOTIFY_TEXT_POINT = new Point(125, 85);
    private static final String SPOTIFY_NAME = "Mewtify";
    private DrawOptions SIZE = new DrawOptions();
    private DrawOptions NEXT_ROTATION = new DrawOptions();
    private DrawOptions SPOTIFY_TEXT = new DrawOptions();
    private boolean isPlay = false;
    private boolean playOnce = false;
    private Clip clip;

    int flag = 0;

    public BagelTest(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        try {
            File file = new File("res/Tame Impala - Borderline (Single Version).wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        BagelTest game = new BagelTest();
        game.run();
    }

    private void playMusic(int flag) {
        if (flag == 1){
            clip.start();
        }
        if (flag == 2) {
            clip.stop();
        }
    }
    /**
     * Performs a state update. This simple example shows an image that can be controlled with the arrow keys, and
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        WHITE_LINE.draw(145, 650);
        WHITE_LINE.draw(441, 650);
        WHITE_LINE.draw(737, 650);
        WHITE_LINE.draw(900, 650);

        BORDERLINE.draw(500, 370);

        SIZE.setScale(.6, .6);

        SPOTIFY_LOGO.draw(60, 60, SIZE);

        NEXT_BUTTON.draw(600, 700);
        NEXT_ROTATION.setRotation(Math.PI);
        NEXT_BUTTON.draw(410, 700, NEXT_ROTATION);

        SPOTIFY_TEXT.setBlendColour(0,0.3, .9);
        spotifyFont.drawString(SPOTIFY_NAME, SPOTIFY_TEXT_POINT.x, SPOTIFY_TEXT_POINT.y, SPOTIFY_TEXT);

        if ((input.wasPressed(MouseButtons.LEFT) || input.wasPressed(Keys.SPACE))&& !isPlay){
            isPlay = true;
            playOnce = true;
        }
        else if ((input.wasPressed(MouseButtons.LEFT) || input.wasPressed(Keys.SPACE)) && isPlay) {
            isPlay = false;
            flag = 2;
            playMusic(flag);
        }

        if (isPlay){
            PAUSE_BUTTON.draw(510, 702);
            flag = 1;
            if (playOnce){
                playMusic(flag);
            }

        }
        else{
            PLAY_BUTTON.draw(510, 702);
        }
    }
}
