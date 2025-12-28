package com.opentrader.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import com.opentrader.ui.ChartPane;
import com.opentrader.ui.DraggableWindow;
import com.opentrader.ui.NewsWindow;
import com.opentrader.service.MarketDataService;

public class MainController {

    @FXML
    private AnchorPane desktopPane;

    private final MarketDataService marketDataService = new MarketDataService();
    private int windowCount = 0;

    @FXML
    private void handleNewChart() {
        String symbol = "EUR/USD";
        ChartPane chartContent = new ChartPane(symbol);
        
        DraggableWindow window = new DraggableWindow("Chart: " + symbol, chartContent, desktopPane);
        
        // Simple tiling/cascading logic
        double width = 500;
        double height = 400;
        
        // Arrange in a grid if possible (2x2)
        if (windowCount == 0) {
            window.setLayoutX(0); window.setLayoutY(0);
        } else if (windowCount == 1) {
            window.setLayoutX(width); window.setLayoutY(0);
        } else if (windowCount == 2) {
             window.setLayoutX(0); window.setLayoutY(height);
        } else {
             window.setLayoutX(width); window.setLayoutY(height);
        }
        
        window.setPrefSize(width, height);
        
        desktopPane.getChildren().add(window);
        windowCount = (windowCount + 1) % 4; // Cycle through 4 positions
    }

    @FXML
    private void handleNewNews() {
        // Here we could also put news in an MDI window instead of a separate stage
        // But kept separate for now unless user asks.
        NewsWindow window = new NewsWindow();
        window.show();
    }

    @FXML
    public void initialize() {
        // Open 4 default charts to demonstrate the "Quad" view
        handleNewChart();
        handleNewChart();
        handleNewChart();
        handleNewChart();
    }
}
