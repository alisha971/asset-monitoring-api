package com.example.asset_monitoring.domain;

public enum AlertSeverity {
    NORMAL(0),
    WARNING(1),
    CRITICAL(2);

    private final int weight;

    AlertSeverity(int weight){this.weight = weight;}
    public int weight(){return weight;}

    public boolean isAbove(AlertSeverity other){
        return this.weight > other.weight;
    }
}
