package game;

import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import java.util.Map;

public class Enemy extends Game{

    private ImageView imageView;
    private boolean shootAbility;
    private int ID;

    private Image enemySprite = new Image("file:resources/pixel_ship_red_small_2.png");

    public Enemy(ImageView imageView, boolean shootAbility) {
        this.imageView = imageView;
        this.shootAbility = shootAbility;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public boolean isShootAbility() {
        return shootAbility;
    }

    public void setShootAbility(boolean shootAbility) {
        this.shootAbility = shootAbility;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void enemyMovement(ImageView enemy) {
        for(int i=0; i< 100; i++) {
            TranslateTransition transition = new TranslateTransition();
            transition.setDuration(Duration.seconds(1.5));
            transition.setToX(enemy.getTranslateX() + 60);
            transition.setCycleCount(Timeline.INDEFINITE);
            transition.setAutoReverse(true);
            transition.setNode(enemy);
            transition.play();
        }
    }

    public Map createEnemy() {
        int position = 1;
        int position2 = 23;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 11; j++) {
                ImageView enemyImage = new ImageView();
                Enemy enemy = new Enemy(enemyImage, false);
                enemyImage.setImage(enemySprite);
                enemy.setShootAbility(false);
                enemyImage.setRotate(180);
                enemyImage.setFitHeight(30);
                enemyImage.setFitWidth(30);
                enemyImage.setTranslateX(5 + j * 40);
                enemyImage.setTranslateY(50 + i * 50);
                enemy.setID(position);
                enemies.put(enemy.getID(), enemy);
                position++;
                enemyMovement(enemyImage);
            }
        }
        for (int k = 0; k < 1; k++) {
            for (int l = 0; l < 11; l++) {
                ImageView enemyImage = new ImageView();
                Enemy enemy2 = new Enemy(enemyImage, true);
                enemyImage.setImage(enemySprite);
                enemy2.setShootAbility(true);
                enemyImage.setRotate(180);
                enemyImage.setFitHeight(30);
                enemyImage.setFitWidth(30);
                enemyImage.setTranslateX(5 + l * 40);
                enemyImage.setTranslateY(150);
                enemy2.setID(position2);
                enemies.put(enemy2.getID(), enemy2);
                position2++;
                enemyMovement(enemyImage);
            }
        }
        return enemies;
    }
}