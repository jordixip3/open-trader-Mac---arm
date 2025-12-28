package com.opentrader.ui;

import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;

public class ChartPane extends BorderPane {
    
    private final String symbol;

    public ChartPane(String symbol) {
        // Normalize symbol for TradingView (e.g., "EUR/USD" -> "FX:EURUSD")
        this.symbol = symbol.replace("/", "").toUpperCase();
        initUI();
    }

    private void initUI() {
        WebView webView = new WebView();
        // Enable JavaScript
        webView.getEngine().setJavaScriptEnabled(true);
        
        // Load the chart
        webView.getEngine().loadContent(getChartHtml(symbol));
        
        setCenter(webView);
    }

    private String getChartHtml(String symbolRaw) {
        String tvSymbol = "FX:" + symbolRaw;
        
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head>" +
               "<style>body, html { margin: 0; padding: 0; height: 100%; overflow: hidden; }</style>" +
               "</head>" +
               "<body>" +
               "<!-- TradingView Widget BEGIN -->" +
               "<div class=\"tradingview-widget-container\" style=\"height:100%;width:100%\">" +
               "  <div id=\"tradingview_widget\"></div>" +
               "  <script type=\"text/javascript\" src=\"https://s3.tradingview.com/tv.js\"></script>" +
               "  <script type=\"text/javascript\">" +
               "  new TradingView.widget(" +
               "  {" +
               "  \"autosize\": true," +
               "  \"symbol\": \"" + tvSymbol + "\"," +
               "  \"interval\": \"D\"," +
               "  \"timezone\": \"Etc/UTC\"," +
               "  \"theme\": \"dark\"," +
               "  \"style\": \"1\"," +
               "  \"locale\": \"en\"," +
               "  \"enable_publishing\": false," +
               "  \"withdateranges\": true," +
               "  \"hide_side_toolbar\": false," +
               "  \"allow_symbol_change\": true," +
               "  \"details\": true," +
               "  \"studies\": [" +
               "    \"MACD@tv-basicstudies\"," +
               "    \"RSI@tv-basicstudies\"," +
               "    \"StochasticRSI@tv-basicstudies\"" +
               "  ]," +
               "  \"container_id\": \"tradingview_widget\"" +
               "  }" +
               "  );" +
               "  </script>" +
               "</div>" +
               "<!-- TradingView Widget END -->" +
               "</body>" +
               "</html>";
    }
}
