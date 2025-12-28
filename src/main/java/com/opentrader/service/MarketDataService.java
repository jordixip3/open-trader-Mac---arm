package com.opentrader.service;

import java.util.ArrayList;
import java.util.List;

public class MarketDataService {
    
    // Simulate fetching data
    public List<Double> getHistory(String symbol) {
        // Mock data for initial testing
        List<Double> data = new ArrayList<>();
        double price = 100.0;
        for (int i = 0; i < 100; i++) {
            price += (Math.random() - 0.5) * 2.0;
            data.add(price);
        }
        return data;
    }

    public double getCurrentPrice(String symbol) {
        return 100.0 + Math.random() * 10;
    }
}
