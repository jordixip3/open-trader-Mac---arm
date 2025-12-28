package com.opentrader.ui;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class NewsWindow extends Stage {
    
    public NewsWindow() {
        setTitle("Global Financial News - FxStreet");
        
        BorderPane root = new BorderPane();
        
        WebView webView = new WebView();
        // FxStreet provides free widgets/news. Loading their main page or a filtered feed.
        // For a desktop app, loading the mobile view or specific widget URL is often cleaner.
        // Using a generic financial news source for this example.
        webView.getEngine().load("https://www.fxstreet.com/news");
        
        root.setCenter(webView);
        
        Scene scene = new Scene(root, 400, 600);
        setScene(scene);
    }
}
