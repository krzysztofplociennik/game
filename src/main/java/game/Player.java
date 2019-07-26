package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player {

    ImageView playerImage = new ImageView();
    private Image playerSprite = new Image("file:resources/pixel_ship_blue.png");

    public ImageView createPlayer() {
        playerImage.setImage(playerSprite);
        playerImage.setFitHeight(30);
        playerImage.setFitWidth(30);
        playerImage.setTranslateX(240);
        playerImage.setTranslateY(500 - 39);

        return playerImage;
    }
}