package game;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.List;

public class Player extends Game {

    final static double playerSpeed = 300;
    final static double minX = 30;
    final static double maxX = 440;
    final static DoubleProperty playerVelocity = new SimpleDoubleProperty();
    final static LongProperty lastUpdateTime = new SimpleLongProperty();

    private Image playerSprite = new Image("file:resources/pixel_ship_blue.png");
    private Image heartSprite = new Image("file:resources/heart_80x80.png");

    public ImageView createPlayer() {
        ImageView playerImage = new ImageView();
        playerImage.setImage(playerSprite);
        playerImage.setFitHeight(30);
        playerImage.setFitWidth(30);
        playerImage.setTranslateX(240);
        playerImage.setTranslateY(500 - 39);
        return playerImage;
    }

    public List createLifes() {
        for (int i = 0; i < 3; i++) {
            ImageView heart = new ImageView();
            heart.setImage(heartSprite);
            heart.setFitHeight(15);
            heart.setFitWidth(15);
            heart.relocate(490 - i * 20, 10);
            lifes.add(heart);
        }
        return lifes;
    }

    public Text createRemainingLifesText() {
        lifesAmount.relocate(390, 10);
        lifesAmount.setText("LIFES: ");
        lifesAmount.setFont(Font.font("Noto Mono", FontWeight.BOLD, 16));
        lifesAmount.setFill(Color.WHEAT);
        return lifesAmount;
    }

}