package com.poker.ui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class MainScene {
    private Stage stage;
    private Scene scene;
    private VBox root;
    private GameView gameView;
    private StackPane contentArea;

    public MainScene(Stage stage) {
        this.stage = stage;
        initializeUI();
    }

    private void initializeUI() {
        root = new VBox(20);
        root.getStyleClass().add("main-scene");
        
        // 创建顶部导航栏
        HBox navBar = createNavBar();
        
        // 创建内容区域
        contentArea = new StackPane();
        contentArea.getStyleClass().add("content-area");
        
        // 默认显示登录界面
        showLoginView();
        
        root.getChildren().addAll(navBar, contentArea);
        VBox.setVgrow(contentArea, Priority.ALWAYS);
        
        scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/static/styles.css").toExternalForm());
        
        stage.setScene(scene);
        stage.setTitle("德州扑克");
        stage.show();
    }

    private HBox createNavBar() {
        HBox navBar = new HBox(20);
        navBar.getStyleClass().add("nav-bar");
        navBar.setPadding(new Insets(10));
        navBar.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("德州扑克");
        title.getStyleClass().add("nav-title");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);

        Button homeBtn = createNavButton("首页");
        Button tablesBtn = createNavButton("游戏桌");
        Button profileBtn = createNavButton("个人中心");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        navBar.getChildren().addAll(title, homeBtn, tablesBtn, spacer, profileBtn);
        return navBar;
    }

    private Button createNavButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("nav-button");
        button.setOnAction(e -> handleNavAction(text));
        return button;
    }

    private void handleNavAction(String action) {
        switch (action) {
            case "首页":
                showLoginView();
                break;
            case "游戏桌":
                showGameLobby();
                break;
            case "个人中心":
                showProfileView();
                break;
        }
    }

    private void showLoginView() {
        VBox loginView = new VBox(20);
        loginView.getStyleClass().add("login-view");
        loginView.setAlignment(Pos.CENTER);
        loginView.setPadding(new Insets(40));

        Label title = new Label("欢迎来到德州扑克");
        title.getStyleClass().add("login-title");

        TextField usernameField = new TextField();
        usernameField.setPromptText("用户名");
        usernameField.getStyleClass().add("login-field");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("密码");
        passwordField.getStyleClass().add("login-field");

        Button loginButton = new Button("登录");
        loginButton.getStyleClass().add("login-button");
        loginButton.setOnAction(e -> handleLogin(usernameField.getText(), passwordField.getText()));

        loginView.getChildren().addAll(title, usernameField, passwordField, loginButton);
        contentArea.getChildren().setAll(loginView);
    }

    private void showGameLobby() {
        VBox lobbyView = new VBox(20);
        lobbyView.getStyleClass().add("lobby-view");
        lobbyView.setPadding(new Insets(20));

        Label title = new Label("游戏大厅");
        title.getStyleClass().add("lobby-title");

        // 创建游戏桌列表
        TableView<Table> tableList = createTableList();
        
        // 创建新游戏桌按钮
        Button createTableBtn = new Button("创建新游戏桌");
        createTableBtn.getStyleClass().add("create-table-button");
        createTableBtn.setOnAction(e -> showCreateTableDialog());

        lobbyView.getChildren().addAll(title, tableList, createTableBtn);
        contentArea.getChildren().setAll(lobbyView);
    }

    private TableView<Table> createTableList() {
        TableView<Table> tableList = new TableView<>();
        tableList.getStyleClass().add("table-list");

        TableColumn<Table, String> nameCol = new TableColumn<>("桌名");
        TableColumn<Table, Integer> playersCol = new TableColumn<>("玩家数");
        TableColumn<Table, Integer> blindsCol = new TableColumn<>("盲注");
        TableColumn<Table, String> statusCol = new TableColumn<>("状态");

        tableList.getColumns().addAll(nameCol, playersCol, blindsCol, statusCol);
        return tableList;
    }

    private void showCreateTableDialog() {
        Dialog<Table> dialog = new Dialog<>();
        dialog.setTitle("创建新游戏桌");
        dialog.setHeaderText("请设置游戏桌参数");

        // 添加表单字段
        TextField nameField = new TextField();
        nameField.setPromptText("桌名");
        
        Spinner<Integer> smallBlindSpinner = new Spinner<>(1, 100, 1);
        Spinner<Integer> bigBlindSpinner = new Spinner<>(2, 200, 2);

        dialog.getDialogPane().setContent(new VBox(10, 
            new Label("桌名:"), nameField,
            new Label("小盲注:"), smallBlindSpinner,
            new Label("大盲注:"), bigBlindSpinner
        ));

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait();
    }

    private void showProfileView() {
        VBox profileView = new VBox(20);
        profileView.getStyleClass().add("profile-view");
        profileView.setPadding(new Insets(20));

        Label title = new Label("个人中心");
        title.getStyleClass().add("profile-title");

        // 添加个人信息卡片
        VBox infoCard = new VBox(10);
        infoCard.getStyleClass().add("info-card");
        
        Label usernameLabel = new Label("用户名: Player1");
        Label chipsLabel = new Label("筹码: 1000");
        Label gamesPlayedLabel = new Label("游戏场次: 10");
        Label winRateLabel = new Label("胜率: 60%");

        infoCard.getChildren().addAll(usernameLabel, chipsLabel, gamesPlayedLabel, winRateLabel);
        profileView.getChildren().addAll(title, infoCard);
        contentArea.getChildren().setAll(profileView);
    }

    private void handleLogin(String username, String password) {
        // TODO: 实现登录逻辑
        showGameLobby();
    }
}