package game;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game extends Application {

    private AnimationTimer timer;
    private Pane root;

    private Node player;
    private int numberOfLifes = 3;
    private List<Node> playerBullets = new ArrayList<>();
    private Map<Integer, Enemy> enemies = new HashMap<>();
    private List<Node> enemiesBullets = new ArrayList<>();
    private boolean playable = true;

    private Image background = new Image("file:resources/background-black.png");
    private Image playerSprite = new Image("file:resources/pixel_ship_blue.png");
    private Image enemySprite = new Image("file:resources/pixel_ship_red_small_2.png");
    private Image enemyBulletSprite = new Image("file:resources/pixel_laser_red.png");
    private Image playerBulletSprite = new Image("file:resources/pixel_laser_blue.png");
    BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
    BackgroundImage backgroundImage = new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
    Background background2 = new Background(backgroundImage);

    private Parent createContent() {
        root = new Pane();
        root.setPrefSize(500, 500);
        root.setBackground(new Background(backgroundImage));

        player = initPlayer();
        enemies = initEnemy();

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();

        return root;
    }

    private Node initPlayer() {
        ImageView player = new ImageView();
        player.setImage(playerSprite);
        player.setFitHeight(30);
        player.setFitWidth(30);
        player.setTranslateX(240);
        player.setTranslateY(500 - 39);
        root.getChildren().add(player);

        return player;
    }

    private Map initEnemy() {
        int position = 1;
        int position2 = 23;
        for(int i=0; i<2; i++) {
            for(int j=0; j<11; j++) {
                ImageView enemyImage = new ImageView();
                Enemy enemy = new Enemy(enemyImage, false);
                enemyImage.setImage(enemySprite);
                enemyImage.setRotate(180);
                enemyImage.setFitHeight(30);
                enemyImage.setFitWidth(30);
                enemyImage.setTranslateX(40 + j*40);
                enemyImage.setTranslateY(50 + i*50);
                enemy.setID(position);
                enemies.put(enemy.getID(), enemy);
                root.getChildren().add(enemyImage);
                position++;

            }
        }
        for(int k=0; k<1; k++) {
            for(int l=0; l<11; l++) {
                ImageView enemyImage = new ImageView();
                Enemy enemy2 = new Enemy(enemyImage, true);
                enemyImage.setImage(enemySprite);
                enemyImage.setRotate(180);
                enemyImage.setFitHeight(30);
                enemyImage.setFitWidth(30);
                enemyImage.setTranslateX(40 + l*40);
                enemyImage.setTranslateY(150);
                enemy2.setID(position2);
                enemies.put(enemy2.getID(), enemy2);
                root.getChildren().add(enemyImage);
                position2++;
            }
        }
        return enemies;
    }
    
    private void enemyFire() {
        for (Map.Entry<Integer, Enemy> entry : enemies.entrySet()) {
            if(entry.getValue().isShootAbility())
                if(Math.random() * 3333 < 4) {
                    ImageView enemyBullet = new ImageView();
                    enemyBullet.setImage(enemyBulletSprite);
                    enemyBullet.setFitHeight(15);
                    enemyBullet.setFitWidth(15);
                    root.getChildren().add(enemyBullet);
                    enemiesBullets.add(enemyBullet);
                    enemyBullet.relocate(entry.getValue().getImageView().getTranslateX() + 7.5, entry.getValue().getImageView().getTranslateY() + 20);
                    TranslateTransition transition2 = new TranslateTransition();
                    transition2.setDuration(Duration.seconds(2.5));
                    transition2.setToY(entry.getValue().getImageView().getTranslateY() + 400);
                    transition2.setNode(enemyBullet);
                    transition2.play();
                }
        }
    }

    private void collisionState() {
        for (Node enemyBulletInstance: enemiesBullets) {
            if(enemyBulletInstance.getBoundsInParent().intersects(player.getBoundsInParent())) {
                enemyBulletInstance.setVisible(false);
                enemyBulletInstance.relocate(0,0);
                numberOfLifes--;
            }
        }
        for(Node playerBulletInstance: playerBullets) {
            for(Map.Entry<Integer, Enemy> entry : enemies.entrySet()) {
                if(playerBulletInstance.getBoundsInParent().intersects((entry.getValue().getImageView().getBoundsInParent()))) {
                    entry.getValue().getImageView().setTranslateX(-800);
                    entry.getValue().getImageView().setTranslateY(1000);
                    entry.getValue().getImageView().setVisible(false);
                    if(!(entry.getKey()-11<1)) {enemies.get(entry.getKey()-11).setShootAbility(true);}
                    enemies.remove(entry);
                    playerBulletInstance.setTranslateX(-800);
                    playerBulletInstance.setTranslateY(1000);
                    playerBulletInstance.setVisible(false);
                }
            }
        }
    }

    private void onUpdate() {
        enemyFire();
        collisionState();
        checkState();
    }

    private void checkState() {
        if(numberOfLifes == 0) {
            playable = false;
            timer.stop();
        }
        if(enemies.isEmpty()) {
            System.out.println("You win!");
            playable = false;
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        //Background background = new Background(backgroundImage);

        root = new Pane();

        primaryStage.setTitle("Space Invaders");
        primaryStage.setScene(new Scene(createContent()));

        primaryStage.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    if(!(player.getTranslateX() < 40)&&playable) {
                        player.setTranslateX(player.getTranslateX() - 14); }
                    break;
                case RIGHT:
                    if(!(player.getTranslateX() > 440)&&playable) {
                    player.setTranslateX(player.getTranslateX() + 14); }
                    break;
                case SPACE:
                    if(playable) {
                        ImageView playerBullet = new ImageView();
                        playerBullet.setImage(playerBulletSprite);
                        playerBullet.setFitHeight(15);
                        playerBullet.setFitWidth(15);
                        playerBullet.relocate(player.getTranslateX() + 7.5, player.getTranslateY() - 15);
                        playerBullets.add(playerBullet);
                        root.getChildren().add(playerBullet);
                        TranslateTransition transition = new TranslateTransition();
                        transition.setDuration(Duration.seconds(1.5));
                        transition.setToY(-500);
                        transition.setNode(playerBullet);
                        transition.play();
                    }
            }
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}