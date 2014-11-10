package com.MediaServer;

/**
 * Created by alexbiju on 11/8/14.
 */
import static spark.Spark.*;
import static com.MediaServer.JsonUtil.*;
public class MediaController {
    public MediaController(MediaService mediaService){

        //searches the media library
        get("/search/:term",(req,res)->{
            String searchTerm = req.params(":term");
            return searchTerm;
        },json());

        //returns top results? don't think i am doing this
        get("/top/",(req,res)->{
            return "hello";
        },json());

        //send a file name/id and then respond with the url that is the file
        get("/get/:fileName",(req,res)->{
            return req.params(":fileName");
        },json());
    }
}
