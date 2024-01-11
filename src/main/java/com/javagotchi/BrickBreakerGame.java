package com.javagotchi;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BrickBreakerGame extends Application {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;
    private static final int PADDLE_WIDTH = 60;
    private static final int PADDLE_HEIGHT = 10;
    private static final int BALL_RADIUS = 5;

    private Pane root;
    private Rectangle paddle;
    private Circle ball;
    private double ballSpeedX = -1.5;
    private double ballSpeedY = -1.5;
    private boolean gameOver = false;
    Timeline timeline;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Brick Breaker Game");
        root = new Pane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        createPaddle();
        createBall();
        createBricks();

        scene.setOnKeyPressed(e -> {
            if(!gameOver) {
                if (e.getCode() == KeyCode.LEFT) {
                    movePaddleLeft();
                } else if (e.getCode() == KeyCode.RIGHT) {
                    movePaddleRight();
                }
            }
        });

        timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> {
            if(!gameOver) {
                moveBall();
                checkCollision();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        primaryStage.setScene(scene);
        primaryStage.show();

        checkGameOver(primaryStage);
    }

    private void createPaddle() {
        paddle = new Rectangle(WIDTH / 2 - PADDLE_WIDTH / 2, HEIGHT - PADDLE_HEIGHT - 40, PADDLE_WIDTH, PADDLE_HEIGHT);
        paddle.setFill(Color.BLUE);
        root.getChildren().add(paddle);
    }

    private void createBall() {
        ball = new Circle(WIDTH / 2, HEIGHT / 2, BALL_RADIUS, Color.RED);
        root.getChildren().add(ball);
    }

    private void createBricks() {
        // Create bricks here and add them to the root pane
    }

    private void movePaddleLeft() {
        if (paddle.getX() > 0) {
            paddle.setX(paddle.getX() - 20);
        }
    }

    private void movePaddleRight() {
        if (paddle.getX() < WIDTH - PADDLE_WIDTH) {
            paddle.setX(paddle.getX() + 20);
        }
    }

    private void moveBall() {
        ball.setCenterX(ball.getCenterX() + ballSpeedX);
        ball.setCenterY(ball.getCenterY() + ballSpeedY);

        if (ball.getCenterX() < 0 || ball.getCenterX() > WIDTH) {
            ballSpeedX *= -1;
        }

        if (ball.getCenterY() < 0) {
            ballSpeedY *= -1;
        }
        if (ball.getCenterY() >= HEIGHT){
            ballSpeedY = 0;
            ballSpeedX = 0;
            System.out.println("Game Over");
            gameOver = true;

        }
    }

    private void checkCollision() {
        if (ball.getBoundsInParent().intersects(paddle.getBoundsInParent())) {
            ballSpeedY *= -1;
        }
        // Add collision detection with bricks here
    }
    private void checkGameOver(Stage primaryStage) {
        Timeline gameOverTimeline = new Timeline(new KeyFrame(Duration.millis(1), event -> {
            if (gameOver) {
                displayGameOver(primaryStage);
            }
        }));
        gameOverTimeline.setCycleCount(Timeline.INDEFINITE);
        gameOverTimeline.play();
    }
    private void displayGameOver(Stage primaryStage) {
        Text gameOverText = new Text("Game Over");
        gameOverText.setFont(new Font(60));
        gameOverText.setFill(Color.RED);
        gameOverText.setX((WIDTH - gameOverText.getBoundsInLocal().getWidth()) / 2);
        gameOverText.setY(HEIGHT / 2 - 100);

        Text pressEnterToQuit = new Text("Press Escape to Quit");
        pressEnterToQuit.setFont(new Font(25));
        pressEnterToQuit.setFill(Color.BLUE);
        pressEnterToQuit.setX(WIDTH / 2 - 110);
        pressEnterToQuit.setY(HEIGHT /2 + 40);

        Text pressSpaceToTryAgain = new Text("Press Space to try again");
        pressSpaceToTryAgain.setFont(new Font(25));
        pressSpaceToTryAgain.setFill(Color.BLUE);
        pressSpaceToTryAgain.setX(WIDTH / 2 - 120);
        pressSpaceToTryAgain.setY(HEIGHT /2 + 70);

        root.getScene().setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ESCAPE){
                primaryStage.close();
            }
            else if (keyEvent.getCode() == KeyCode.SPACE){
                restartGame();
                timeline.stop();
                primaryStage.close();
                start(new Stage());

            }
        });
        root.getChildren().addAll(gameOverText, pressEnterToQuit, pressSpaceToTryAgain);
    }
    private void restartGame() {
        gameOver = false;
        ballSpeedX = -1.5;
        ballSpeedY = -1.5;
        root.getChildren().clear();
        createPaddle();
        createBall();
        createBricks();
    }
}
