package game;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import java.util.*;

public class Game extends Application {

    Pane root;
    Text scoreText = new Text();

    private Node player;
    private int numberOfLifes = 3;
    private List<Node> playerBullets = new ArrayList<>();
    private boolean playable = true;
    private int points = 0;
    List<ImageView> lifes = new ArrayList<>();

    Map<Integer, Enemy> enemies = new HashMap<>();
    private List<Node> enemiesBullets = new ArrayList<>();
    private int numberOfEnemies = 33;

    private Image background = new Image("file:resources/background-black.png");
    private Image enemyBulletSprite = new Image("file:resources/pixel_laser_red.png");
    private Image playerBulletSprite = new Image("file:resources/pixel_laser_blue.png");
    private BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
    private BackgroundImage backgroundImage = new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);

    private Parent createContent() {
        root = new Pane();
        root.setPrefSize(500, 500);
        root.setBackground(new Background(backgroundImage));

        Player playerObject = new Player();
        Enemy enemyObject = new Enemy(null, false);

        player = playerObject.createPlayer();
        lifes = playerObject.createLifes();
        enemies = enemyObject.createEnemy();

        root.getChildren().addAll(player, scoreText);
        for (Node node : lifes) {
            root.getChildren().add(node);
        }
        for (Map.Entry<Integer, Enemy> entry : enemies.entrySet()) {
            root.getChildren().add(entry.getValue().getImageView());
        }

        Audio.playBackgroundMusic();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();

        return root;
    }

    public void updateScore() {
        scoreText.relocate(5, 5);
        scoreText.setText("SCORE: " + points);
        scoreText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 18));
        scoreText.setFill(Color.WHITE);
    }

    private void enemyFire() {
        for (Map.Entry<Integer, Enemy> entry : enemies.entrySet()) {
            if (entry.getValue().isShootAbility() && Math.random() * 4444 < 4) {
                ImageView enemyBullet = new ImageView();
                enemyBullet.setImage(enemyBulletSprite);
                enemyBullet.setFitHeight(15);
                enemyBullet.setFitWidth(15);
                root.getChildren().add(enemyBullet);
                enemiesBullets.add(enemyBullet);
                enemyBullet.relocate(entry.getValue().getImageView().getTranslateX() + 7.5, entry.getValue().getImageView().getTranslateY() + 20);
                TranslateTransition transition2 = new TranslateTransition();
                transition2.setDuration(Duration.seconds(3.5));
                transition2.setToY(entry.getValue().getImageView().getTranslateY() + 600);
                transition2.setNode(enemyBullet);
                transition2.play();
                transition2.setAutoReverse(true);
            }
        }
    }

    private void collisionState() {
        for (Node enemyBulletInstance : enemiesBullets) {
            if (enemyBulletInstance.getBoundsInParent().intersects(player.getBoundsInParent())) {
                enemyBulletInstance.setVisible(false);
                enemyBulletInstance.relocate(0, 0);
                numberOfLifes--;
                root.getChildren().remove(lifes.get(numberOfLifes));
                Audio.playPlayerDeathSound();
            }
        }
        for (Node playerBulletInstance : playerBullets) {
            for (Map.Entry<Integer, Enemy> entry : enemies.entrySet()) {
                if (playerBulletInstance.getBoundsInParent().intersects((entry.getValue().getImageView().getBoundsInParent()))) {
                    entry.getValue().getImageView().setTranslateX(-800);
                    entry.getValue().getImageView().setTranslateY(1000);
                    entry.getValue().getImageView().setVisible(false);
                    root.getChildren().remove(entry);
                    if (!(entry.getKey() - 11 < 1)) {
                        enemies.get(entry.getKey() - 11).setShootAbility(true);
                    }
                    playerBulletInstance.setTranslateX(-800);
                    playerBulletInstance.setTranslateY(1000);
                    playerBulletInstance.setVisible(false);
                    numberOfEnemies--;
                    points+=147;
                    Audio.playEnemyDeathSound();
                }
            }
        }
    }

    private void checkState() {
        if (numberOfLifes == 0) {
            playable = false;
            try { Audio.stopBackgroundMusic();
            }catch (NullPointerException e) {}
            Text loseMessage = new Text(10, 10, "YOU LOSE!");
            loseMessage.relocate(170, 240);
            loseMessage.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
            loseMessage.setFill(Color.RED);
            root.getChildren().add(loseMessage);
            player.relocate(-1000, -1000);
            root.getChildren().remove(player);
        }
        if (numberOfEnemies==0) {
            playable = false;
            try {
                Audio.stopBackgroundMusic();
                Audio.playWinSound();
            }catch (NullPointerException e) {}
            Text winMessage = new Text(10, 10, "YOU WIN!");
            winMessage.relocate(170, 240);
            winMessage.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
            winMessage.setFill(Color.GREEN);
            root.getChildren().add(winMessage);
        }

    }private void onUpdate() {
        updateScore();
        enemyFire();
        collisionState();
        checkState();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        root = new Pane();

        primaryStage.setTitle("Space Invaders");
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.setResizable(false);

        final AnimationTimer playerAnimation = new AnimationTimer() {
            @Override
            public void handle(long timestamp) {
                if (Player.lastUpdateTime.get() > 0) {
                    final double elapsedSeconds = (timestamp - Player.lastUpdateTime.get()) / 1_000_000_000.0 ;
                    final double deltaX = elapsedSeconds * Player.playerVelocity.get();
                    final double oldX = player.getTranslateX();
                    final double newX = Math.max(Player.minX, Math.min(Player.maxX, oldX + deltaX));
                    player.setTranslateX(newX);
                }
                Player.lastUpdateTime.set(timestamp);
            }
        };
        playerAnimation.start();

        primaryStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode()==KeyCode.RIGHT && playable) {
                    Player.playerVelocity.set(Player.playerSpeed);
                }
                if (event.getCode() == KeyCode.LEFT && playable) {
                    Player.playerVelocity.set(-Player.playerSpeed);
                }
                if (event.getCode() == KeyCode.SPACE && playable) {
                    Audio.playPlayerShotSound();
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
                if (event.getCode() == KeyCode.ESCAPE) {
                    Platform.exit();
                }
            }
        });
        primaryStage.getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT) {
                    Player.playerVelocity.set(0);
                }
            }
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}