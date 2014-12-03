


/**
 * Created by alexbiju on 11/8/14.
 */

import java.io.IOException;



public class RenderServer {
    

    public static void main(String[] args) throws IOException {
        RenderController rThread = new RenderController();
        Thread mediaThread = new Thread(rThread);
        mediaThread.start();
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        while(rThread.isRunning()){
        	try {
				mediaThread.join(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

    }
}