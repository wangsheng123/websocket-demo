package com.ws.demowebsocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RedirectController {
    @RequestMapping("/")
    public String redirectWs() {
        return "ws";
    }

    @RequestMapping("/Blob_ArrayBuffer")
    public String redirectBA() {
        return "BlobAndArrayBuffer";
    }

    @RequestMapping("/ws_Blob")
    public String redirectWsBl() {
        return "ws_Blob";
    }

}
