/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Facebook;

import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author YUYUETONG_S2137573
 */

public class Post {
    private User author;
    private String content;

    public Post(User user, String content) {
        this.author=user;
        this.content = content;
    }

    public String getContent() {
        return content;
    }
    
    public void editContent(String content) {
        this.content = content;
    }

    //To check whether the content is inappropriate or not
    public boolean isInappropriate(){
        if (containsOffensiveLanguage(this.content)) {
            return true;
        } else {
            return false;
        }
    }
    
    //To check if the content contains offensive words or not
    private boolean containsOffensiveLanguage(String content) {
        List<String> offensiveWords = new ArrayList<>();
        try {
            Scanner inputStream = new Scanner(new FileInputStream("C:\\s2137573\\DSAssignment\\src\\main\\java\\Facebook\\ListOfProhibitedWords.txt"));
            //read all words from the prohibited words list
            while (inputStream.hasNextLine()){
                offensiveWords.add(inputStream.next());
                //store those words into ArrayList offensiveWords one by one
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("File was not found"); 
        }

        for (String word : offensiveWords) {
            //Extract each word from the ArrayList to see if they are included in the content of the post
            //If yes, then the content contains offensive words, return true
            if (content.toLowerCase().contains(word)) {
                return true;
            }
        }
        return false;
    }
    
}
