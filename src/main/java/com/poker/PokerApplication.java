package com.poker;

import com.poker.ui.MainScene;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PokerApplication extends Application {

    private ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        springContext = SpringApplication.run(PokerApplication.class);
    }

    @Override
    public void start(Stage primaryStage) {
        new MainScene(primaryStage);
    }

    @Override
    public void stop() {
        springContext.close();
    }
}