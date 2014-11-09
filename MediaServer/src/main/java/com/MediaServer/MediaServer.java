package com.MediaServer;

/**
 * Created by alexbiju on 11/8/14.
 */

public class MediaServer {
    public static void main(String[] args) {
        new MediaController(new MediaService());
    }
}
