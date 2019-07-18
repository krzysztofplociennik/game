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
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

public class Game extends Application {

    private AnimationTimer timer;
    private Pane root;

    private Node player;
    private int numberOfLifes = 3;
    private List<Node> playerBullets = new ArrayList<>();
    private List<Node> enemies = new ArrayList<>();
    private List<Node> enemiesBullets = new ArrayList<>();
    private boolean playable = true;

    private Image background = new Image("file:resources/background-black.png");
    private Image playerImage = new Image("file:resources/pixel_ship_blue.png");
    private Image enemyImage = new Image("file:resources/pixel_ship_red_small_2.png");
    private Image enemyBulletImage = new Image("file:resources/pixel_laser_red.png");
    private Image playerBulletImage = new Image("file:resources/pixel_laser_blue.png");
    BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
    BackgroundImage backgroundImage = new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
    Background background2 = new Background(backgroundImage);

    private Parent createContent() {
        root = new Pane();
        root.setPrefSize(500, 500);
        root.setBackground(new Background(backgroundImage));

        player = initPlayer();
        enemies = initEnemies();

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
        player.setImage(playerImage);
        player.setFitHeight(30);
        player.setFitWidth(30);
        player.setTranslateX(240);
        player.setTranslateY(500 - 39);
        root.getChildren().add(player);

        return player;
    }

    private List initEnemies() {
        for(int i=0; i<3; i++) {
            for(int j=0; j<11; j++) {
                ImageView enemy = new ImageView();
                enemy.setImage(enemyImage);
                enemy.setRotate(180);
                enemy.setFitHeight(30);
                enemy.setFitWidth(30);
                enemy.setTranslateX(40 + j*40);
                enemy.setTranslateY(50 + i*50);
                enemies.add(enemy);
                root.getChildren().add(enemy);
            }
        }
        return enemies;
    }
    
    private void enemyFire() {
        for (Node enemyInstance: enemies) {
                Circle checker = new Circle(2);
                root.getChildren().add(checker);
                checker.relocate(enemyInstance.getTranslateX() + 10, enemyInstance.getTranslateY() + 40);
                checker.setVisible(false);
                TranslateTransition transition = new TranslateTransition();
                transition.setDuration(Duration.seconds(0.5));
                transition.setToY(enemyInstance.getTranslateY() + 15);
                transition.setNode(checker);
                transition.play();
                if (checker.getBoundsInParent().intersects(enemyInstance.getBoundsInParent())) {
                    //checker.setTranslateX(0);
                    //checker.relocate(0,0);
                    //checker.setTranslateY(0);
                    //System.out.println("YES");
                } else if(Math.random() * 4444 < 4) {
                    System.out.println("NO");
                    ImageView enemyBullet = new ImageView();
                    enemyBullet.setImage(enemyBulletImage);
                    enemyBullet.setFitHeight(15);
                    enemyBullet.setFitWidth(15);
                    root.getChildren().add(enemyBullet);
                    enemiesBullets.add(enemyBullet);
                    enemyBullet.relocate(enemyInstance.getTranslateX() + 7.5, enemyInstance.getTranslateY() + 20);
                    TranslateTransition transition2 = new TranslateTransition();
                    transition2.setDuration(Duration.seconds(2.5));
                    transition2.setToY(enemyInstance.getTranslateY() + 400);
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
                System.out.println("A hit! " + numberOfLifes);
            }
        }
        for(Node playerBulletInstance: playerBullets) {
            for(Node enemyInstance: enemies) {
                if(playerBulletInstance.getBoundsInParent().intersects((enemyInstance.getBoundsInParent()))) {
                    enemyInstance.setTranslateX(-800);
                    enemyInstance.setTranslateY(1000);
                    enemyInstance.setVisible(false);
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
                        playerBullet.setImage(playerBulletImage);
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