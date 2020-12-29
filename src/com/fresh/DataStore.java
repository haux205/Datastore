package com.fresh;

import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;


public class DataStore {
    void createFile() throws IOException {
        File fileobj= new File("/Users/yuvaraj/Desktop/jav/store1.txt");
        fileobj.createNewFile();
    }

    void writeData(String key, JSONObject value) throws IOException {
        Scanner s=new Scanner(System.in);
        String val="";
            if (key.length() > 32) {
                System.out.println("Enter a key value <32 chars! Unable to perform write!");
                return;
            }
            val=val+key+":"+value.toString()+"\n";
            try {
                Path path = Path.of("/Users/yuvaraj/Desktop/jav/store1.txt");
                Files.writeString(path,val, StandardOpenOption.APPEND);
                
            }
            catch (IOException e){System.out.println("Failed");}


    }

    void readData(String val) throws IOException {
        BufferedReader reader= new BufferedReader(new FileReader("/Users/yuvaraj/Desktop/jav/store1.txt"));
        String line= reader.readLine();
        int len=val.length();
        while(line!=null)
        {
            System.out.println(line+"/"+line.substring(0,len));
            if(val.equals(line.substring(0,len))){
                System.out.println("Value found!!");
                System.out.println(line.substring(len+1,line.length()));
                return;
            }
            else {line=reader.readLine();}
        }
        System.out.println("The value is not found");
        reader.close();
    }

    void deleteData(String val) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("/Users/yuvaraj/Desktop/jav/store1.txt"));
        String line = reader.readLine();
        int len=val.length();
        while(line!=null){
            if(val.equals(line.substring(0,len))){

            }
        }


    }


}
