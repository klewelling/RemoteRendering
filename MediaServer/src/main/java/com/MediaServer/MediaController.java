package com.MediaServer;

/**
 * Created by alexbiju on 11/8/14.
 */
import static spark.Spark.*;
import static com.MediaServer.JsonUtil.*;
public class MediaController {
    public MediaController(MediaService mediaService){

        //search the media library
        get("/search/:term",(req,res)->{
            String searchTerm = req.params(":term");
            return searchTerm;
        },json());

        //returns top results
        get("/top/",(req,res)->{
            return "hello";
        },json());


    }
}
