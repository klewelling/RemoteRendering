package com.MediaServer;

/**
 * Created by alexbiju on 11/8/14.
 */

import java.io.IOException;



public class MediaServer {
    

    public static void main(String[] args) throws IOException {
        MediaController mThread = new MediaController();
        Thread mediaThread = new Thread(mThread);
        mediaThread.start();
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        while(mThread.isRunning()){
        	try {
				mediaThread.join(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

    }
}