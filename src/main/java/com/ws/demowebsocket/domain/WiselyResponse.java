package com.ws.demowebsocket.domain;

import java.io.Serializable;

public class WiselyResponse implements Serializable{
    private String responseMessage;
    public WiselyResponse(String responseMessage){
        this.responseMessage = responseMessage;
    }
    public String getResponseMessage(){
        return responseMessage;
    }
}
