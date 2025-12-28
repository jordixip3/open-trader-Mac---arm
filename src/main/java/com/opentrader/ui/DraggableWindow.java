package com.opentrader.ui;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class DraggableWindow extends BorderPane {

    private double xOffset = 0;
    private double yOffset = 0;
    private final Pane parentContainer;
    private boolean isMaximized = false;
    private double prevX, prevY, prevWidth, prevHeight;

    public DraggableWindow(String title, Node content, Pane parent) {
        this.parentContainer = parent;
        
        // Style
        setStyle("-fx-background-color: #2d2d2d; -fx-border-color: #444; -fx-border-width: 1; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 0);");
        setPrefSize(500, 400); // Default size (approx 1/4 of 1000x800)
        
        // Title Bar
        HBox titleBar = new HBox();
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setStyle("-fx-background-color: #3c3c3c; -fx-padding: 5;");
        titleBar.setSpacing(10);
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        
        // Buttons
        Button popOutBtn = createCaptionButton("^", e -> popOut()); // Project/Detach
        Button minBtn = createCaptionButton("_", e -> minimize());
        Button maxBtn = createCaptionButton("[]", e -> toggleMaximize());
        Button closeBtn = createCaptionButton("X", e -> close());
        closeBtn.setStyle("-fx-background-color: #cc0000; -fx-text-fill: white; -fx-font-size: 10px; -fx-min-width: 25px;");
        
        titleBar.getChildren().addAll(titleLabel, popOutBtn, minBtn, maxBtn, closeBtn);
        setTop(titleBar);
        setCenter(content);
        
        // Resize Handle (Bottom Right)
        Label resizeHandle = new Label("◢");
        resizeHandle.setStyle("-fx-text-fill: #666; -fx-font-size: 12px; -fx-cursor: se-resize;");
        resizeHandle.setAlignment(Pos.BOTTOM_RIGHT);
        resizeHandle.setPadding(new javafx.geometry.Insets(0, 2, 0, 0));
        
        BorderPane bottomBar = new BorderPane();
        bottomBar.setRight(resizeHandle);
        bottomBar.setStyle("-fx-background-color: transparent;");
        setBottom(bottomBar);

        // Dragging Logic
        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX() - getLayoutX();
            yOffset = event.getSceneY() - getLayoutY();
            toFront(); // Move window to top
        });
        
        titleBar.setOnMouseDragged(event -> {
            if (!isMaximized) {
                setLayoutX(event.getSceneX() - xOffset);
                setLayoutY(event.getSceneY() - yOffset);
            }
        });
        
        // Resize Logic
        resizeHandle.setOnMousePressed(event -> {
            prevX = event.getSceneX();
            prevY = event.getSceneY();
            toFront();
            event.consume();
        });
        
        resizeHandle.setOnMouseDragged(event -> {
            if (!isMaximized) {
                double deltaX = event.getSceneX() - prevX;
                double deltaY = event.getSceneY() - prevY;
                
                double newWidth = getPrefWidth() + deltaX;
                double newHeight = getPrefHeight() + deltaY;
                
                if (newWidth > 200) setPrefWidth(newWidth);
                if (newHeight > 150) setPrefHeight(newHeight);
                
                prevX = event.getSceneX();
                prevY = event.getSceneY();
            }
            event.consume();
        });
    }

    private Button createCaptionButton(String text, EventHandler<MouseEvent> action) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #aaa; -fx-font-size: 10px; -fx-min-width: 25px; -fx-border-color: #555;");
        btn.setOnMouseClicked(action);
        return btn;
    }

    private void minimize() {
        setVisible(false);
    }

    private void toggleMaximize() {
        if (isMaximized) {
            // Restore
            prefWidthProperty().unbind();
            prefHeightProperty().unbind();
            setLayoutX(prevX);
            setLayoutY(prevY);
            setPrefSize(prevWidth, prevHeight);
            isMaximized = false;
            toFront();
        } else {
            // Maximize with Binding
            prevX = getLayoutX();
            prevY = getLayoutY();
            prevWidth = getPrefWidth();
            prevHeight = getPrefHeight();
            
            setLayoutX(0);
            setLayoutY(0);
            // Bind to parent size so it resizes WITH the main window
            prefWidthProperty().bind(parentContainer.widthProperty());
            prefHeightProperty().bind(parentContainer.heightProperty());
            isMaximized = true;
            toFront();
        }
    }
    
    private void popOut() {
        // Detach content and put in a new Stage (TV/Projector safe)
        Node content = getCenter();
        setCenter(null); // Remove from MDI window
        
        javafx.stage.Stage stage = new javafx.stage.Stage();
        stage.setTitle("Projector View");
        javafx.scene.Scene scene = new javafx.scene.Scene(new javafx.scene.layout.StackPane(content), 800, 600);
        stage.setScene(scene);
        stage.show();
        
        // Close the MDI wrapper
        close();
    }

    private void close() {
        parentContainer.getChildren().remove(this);
    }
}
