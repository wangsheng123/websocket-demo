package com.ws.demowebsocket.domain;

import java.io.Serializable;

public class WiselyMessage implements Serializable{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
