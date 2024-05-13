package com.example.cityquest.model;

public class Filter {
    private String name;
    private int iconResId;

    public Filter(String name, int iconResId) {
        this.name = name;
        this.iconResId = iconResId;
    }

    public String getName() { return name; }
    public int getIconResId() { return iconResId; }
}

