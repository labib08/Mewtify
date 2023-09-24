import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

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

public class Mewtify extends AbstractGame {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "Mewtify";
    private final static int MAX_SONGS = 6;
    private static final Point SPOTIFY_TEXT_POINT = new Point(125, 85);
    private static final String SPOTIFY_NAME = "Mewtify";
    private static final Point PAUSE_BUTTON_POINT = new Point(510, 702);
    private static final Point NEXT_BUTTON_POINT = new Point(600, 700);
    private static final Point PREV_BUTTON_POINT = new Point(410, 700);
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");
    private final Image WHITE_LINE = new Image("res/double-white-road-lines.jpg");
    private final Image SPOTIFY_LOGO = new Image("res/BlueSpotify.png");
    private final Image PLAY_BUTTON = new Image("res/PlayButton.png");
    private final Image PAUSE_BUTTON = new Image("res/PauseButton.png");
    private final Image NEXT_BUTTON = new Image("res/NextButton.png");
    private final Font spotifyFont = new Font("res/conformable.otf", 150);
    private DrawOptions SIZE = new DrawOptions();
    private DrawOptions NEXT_ROTATION = new DrawOptions();
    private DrawOptions SPOTIFY_TEXT = new DrawOptions();
    private boolean isPlay = false;
    private boolean playOnce = false;
    private Clip clip;
    private Image ARTIST_PHOTO;
    private String[] songs = {"res/Tame Impala - Borderline (Single Version).wav", "res/BlindingLights.wav", "res/Shape_of_you.wav", "res/One_Dance.wav", "res/This-Is-What-You-Came-For.wav", "res/Stay.wav"};
    private String[] artistPhotos = {"res/BorderlinePic.png", "res/The_Weeknd_-_Blinding_Lights.png", "res/Shape_Of_You_(Official_Single_Cover)_by_Ed_Sheeran.png", "res/DrakeOneDance.png", "res/This_Is_What_You_Came_For_cover.png", "res/Stay.png"};
    private String[] artistPhotosCover = {"res/BorderlineRe.png", "res/The_Weeknd_-_Blinding_Lights_Re.png", "res/Shape_Of_You_(Official_Single_Cover)_by_Ed_Sheeran_re.png", "res/DrakeOneDanceRe.png", "res/This_Is_What_You_Came_For_cover_Re.png", "res/StayRe.png"};
    private Point[] artistPoints = new Point[MAX_SONGS];
    private boolean flagCsv = true;
    private int currSong = 0;
    private int currArtist = 0;
    private int currPoint = 0;
    private boolean flagSong = true;
    private int page = 1;
    private double xCoordinate;
    private double yCoordinate;
    int flag = 0;

    public Mewtify(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);

    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        Mewtify game = new Mewtify();
        game.run();
    }

    private void loadSong(int songIndex) {
        try {
            File file = new File(songs[songIndex]);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        flagSong = false;
    }

    private void playMusic(int flag) {
        if (flag == 1){
            clip.start();
        }
        if (flag == 2) {
            clip.stop();
        }
        if (flag == 3) {
            clip.close();
        }
    }


    private void readCSV() {
        try (Scanner file = new Scanner(new FileReader("res/artistiPhotoPoint.csv"))) {
            while (file.hasNextLine()) {
                String[] pointData = file.nextLine().split(",");
                xCoordinate = Double.parseDouble(pointData[0]);
                yCoordinate = Double.parseDouble(pointData[1]);
                artistPoints[currPoint] = new Point(xCoordinate, yCoordinate);
                currPoint++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // A flag variable to make sure the program reads the csv file only once.
        flagCsv = false;

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

        SIZE.setScale(.6, .6);
        SIZE.setRotation(Math.PI);
        SPOTIFY_LOGO.draw(60, 60, SIZE);

        SPOTIFY_TEXT.setBlendColour(0,0.3, 0.9);
        spotifyFont.drawString(SPOTIFY_NAME, SPOTIFY_TEXT_POINT.x, SPOTIFY_TEXT_POINT.y, SPOTIFY_TEXT);

        if (page == 1) {

            if (flagCsv) {
                readCSV();
            }

            for (int i =0; i< MAX_SONGS; i++){
                ARTIST_PHOTO = new Image(artistPhotosCover[i]);
                ARTIST_PHOTO.draw(artistPoints[i].x, artistPoints[i].y);
                if ((input.wasPressed(MouseButtons.LEFT)) && (ARTIST_PHOTO.getBoundingBoxAt(artistPoints[i]).intersects(input.getMousePosition()))) {
                    currSong = i;
                    currArtist = i;
                    page = 2;
                    break;
                }
            }

        }
        if (page == 2) {
            WHITE_LINE.draw(145, 650);
            WHITE_LINE.draw(441, 650);
            WHITE_LINE.draw(737, 650);
            WHITE_LINE.draw(900, 650);

            ARTIST_PHOTO = new Image(artistPhotos[currArtist]);
            ARTIST_PHOTO.draw(500, 370);

            NEXT_BUTTON.draw(NEXT_BUTTON_POINT.x, NEXT_BUTTON_POINT.y);
            NEXT_ROTATION.setRotation(Math.PI);
            NEXT_BUTTON.draw(PREV_BUTTON_POINT.x, PREV_BUTTON_POINT.y, NEXT_ROTATION);

            if (flagSong) {
                loadSong(currSong);
            }

            if (input.wasPressed(MouseButtons.LEFT) && (PAUSE_BUTTON.getBoundingBoxAt(PAUSE_BUTTON_POINT).intersects(input.getMousePosition())) || (input.wasPressed(Keys.SPACE))) {
                if (!isPlay) {
                isPlay = true;
                playOnce = true;
            }
                else if (isPlay) {
                    isPlay = false;
                    flag = 2;
                    playMusic(flag);
                }
            }

            else if (input.wasPressed(MouseButtons.LEFT) && NEXT_BUTTON.getBoundingBoxAt(NEXT_BUTTON_POINT).intersects(input.getMousePosition())) {
                playMusic(3);
                if (currArtist == artistPhotos.length - 1) {
                    currArtist = 0;
                    currSong = 0;
                }
                else{
                    currArtist ++;
                    currSong++;
                }
                flagSong = true;
            }

            else if (input.wasPressed(MouseButtons.LEFT) && NEXT_BUTTON.getBoundingBoxAt(PREV_BUTTON_POINT).intersects(input.getMousePosition())) {
                playMusic(3);
                if (currArtist == 0) {
                    currArtist = artistPhotos.length - 1;
                    currSong = songs.length - 1;
                }
                else{
                    currArtist --;
                    currSong--;
                }
                flagSong = true;
            }

            if (isPlay){
                PAUSE_BUTTON.draw(PAUSE_BUTTON_POINT.x, PAUSE_BUTTON_POINT.y);
                flag = 1;
                if (playOnce){
                    playMusic(flag);
                }

            }
            else {
                PLAY_BUTTON.draw(PAUSE_BUTTON_POINT.x, PAUSE_BUTTON_POINT.y);
            }
        }
        if (input.wasPressed(Keys.Q)) {
            playMusic(3);
            page = 1;
            flagSong = true;
        }
    }
}
