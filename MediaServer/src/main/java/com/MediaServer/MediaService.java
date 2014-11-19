package com.MediaServer;
import java.util.*;
import java.io.File;
/**
 * Created by alexbiju on 11/8/14.
 */
public class MediaService {
    List<String> musicLibrary = new ArrayList<String>();
    public MediaService(String filePath){
        File[] musicFiles = new File(filePath).listFiles();
        for (File file : musicFiles) {
            if (file.isFile()) {
                musicLibrary.add(file.getName());
                System.out.println(file.getName());
            }
        }
    }

    public List<String> search(String searchTerm){
        List<String> searchResults = new ArrayList<String>();
        for(String music : musicLibrary){
            if (music.toLowerCase().contains(searchTerm.toLowerCase())){
                searchResults.add(music);
            }
        }
        return searchResults;
    }
}
