package game;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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
    private List<Node> enemies = new ArrayList<>();
    private List<Node> bullets = new ArrayList<>();

    private Image background = new Image("file:resources/background-black.png");
    private Image playerShip = new Image("file:resources/pixel_ship_red.png");
    private Image enemyShip = new Image("file:resources/pixel_ship_blue_small.png");
    BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
    BackgroundImage backgroundImage = new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
    Background background2 = new Background(backgroundImage);

    private Parent createContent() {
        root = new Pane();
        root.setPrefSize(500, 500);
        root.setBackground(new Background(backgroundImage));

        player = initPlayer();
        enemies = initEnemies();

        root.getChildren().add(player);

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
        Rectangle player = new Rectangle(25, 25, Color.BLUE);
        player.setTranslateX(240);
        player.setTranslateY(500 - 39);

        return player;
    }

    private List initEnemies() {
        for(int i=0; i<4; i++) {
            for(int j=0; j<11; j++) {
                Rectangle enemy = new Rectangle(15, 15, Color.RED);
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
            if((Math.random() * 4449 + 1)<4) {
                Circle bullet = new Circle(2, Color.DARKGRAY);
                bullets.add(bullet);
                root.getChildren().add(bullet);
                bullet.relocate(enemyInstance.getTranslateX()+5, enemyInstance.getTranslateY()+10);
                TranslateTransition transition = new TranslateTransition();
                transition.setDuration(Duration.seconds(3));
                transition.setToY(enemyInstance.getTranslateY() + 400);
                transition.setNode(bullet);
                transition.play();
            }
        }
    }

    private void collisionState() {
        for (Node bulletInstance: bullets) {
            if(bulletInstance.getBoundsInParent().intersects(player.getBoundsInParent())) {
                bulletInstance.setVisible(false);
                numberOfLifes = numberOfLifes - 1;
                System.out.println("Game Over!");
            }
            if(bulletInstance.getTranslateY()>300) {
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
            player.setVisible(false);
            //timer.stop();
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        root = new Pane();

        primaryStage.setTitle("Space Invaders");
        primaryStage.setScene(new Scene(createContent()));

        primaryStage.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    if(!(player.getTranslateX() < 40)) {
                        player.setTranslateX(player.getTranslateX() - 14); }
                    break;
                case RIGHT:
                    if(!(player.getTranslateX() > 440)) {
                    player.setTranslateX(player.getTranslateX() + 14); }
                    break;
                case SPACE:
                    Circle bullet = new Circle(2, Color.DARKSLATEGREY);
                    bullet.relocate(player.getTranslateX()+10, player.getTranslateY());
                    TranslateTransition transition = new TranslateTransition();
                    transition.setDuration(Duration.seconds(1.5));
                    transition.setToY(-500);
                    transition.setNode(bullet);
                    transition.play();
                    bullets.add(bullet);
                    root.getChildren().add(bullet);
            }
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}