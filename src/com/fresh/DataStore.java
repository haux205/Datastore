package com.fresh;

import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static final String LOCK="1";
    private final static String UNLOCK="0";
    void createFile() throws IOException {
        File fileobj= new File("/Users/yuvaraj/Desktop/jav/store1.txt");
        fileobj.createNewFile();
    }

    void writeData(String key, JSONObject value) throws IOException {
        String val="";
        BufferedReader reader= new BufferedReader(new FileReader("/Users/yuvaraj/Desktop/jav/store1.txt"));
        if(reader.readLine().equals("0")){
            changeAccessStatus(LOCK);
        }
        else {
            System.out.println("File already in access by another process. Please try again later.");
            return;
        }
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
            changeAccessStatus(UNLOCK);
    }

    void readData(String val) throws IOException {
        BufferedReader reader= new BufferedReader(new FileReader("/Users/yuvaraj/Desktop/jav/store1.txt"));
        String line= reader.readLine();
        int len=val.length();
        if(line.equals("0")){
            changeAccessStatus(LOCK);
            line=reader.readLine();
        }
        else{
            System.out.println("File already in access by another process. Please try again later.");
            return;
        }
        while(line != null) //Check whether you need this variable
        {
            if(val.equals(line.substring(0,line.indexOf(":")))){
                System.out.println("Value found!!");
                System.out.println(line.substring(len+1,line.length()));
                return;
            }
            else {line=reader.readLine();}
        }
        System.out.println("The value is not found");
        reader.close();
        changeAccessStatus(UNLOCK);
    }

    void deleteData(String val) throws IOException {
        List<String> data= new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new FileReader("/Users/yuvaraj/Desktop/jav/store1.txt"));
        String line = reader.readLine();
        int i;
        if(line.equals("0")){
            changeAccessStatus(UNLOCK);
            data.add(line);
            line=reader.readLine();
        }
        else {
            System.out.println("File already in access by another process. Please try again later.");
            return;
        }
        while(line != null){
            System.out.println("********");
            if(val.equals(line.substring(0,line.indexOf(":")))){
                System.out.println(line+"/"+""+line.substring(0,line.indexOf(":"))+"***");
                line=reader.readLine();
            }
            else {
                System.out.println(line+"/"+""+line.substring(0,line.indexOf(":")));
                data.add(line);
                line=reader.readLine();
            }
        }
        Path path = Path.of("/Users/yuvaraj/Desktop/jav/store1.txt");
        Files.writeString(path,"", StandardOpenOption.TRUNCATE_EXISTING);
        for(i=0;i<data.size();i++){
            try {
                Files.writeString(path,data.get(i)+"\n", StandardOpenOption.APPEND);
            }
            catch (IOException e){System.out.println("Failed");}
        }
        reader.close();
        changeAccessStatus(UNLOCK);
    }
    void changeAccessStatus(String val) throws IOException {
        RandomAccessFile ra=new RandomAccessFile("/Users/yuvaraj/Desktop/jav/store1.txt","rw");
        ra.seek(0);
        ra.write(val.getBytes());
        ra.close();
    }
}
