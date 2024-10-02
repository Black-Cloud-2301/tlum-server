package org.tlum.notification.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Stock {
    String name;
    String icon;
    float price;
    boolean increased;

    public Stock(String name, String icon, float price) {
        this.name = name;
        this.icon = icon;
        this.price = price;
    }
}