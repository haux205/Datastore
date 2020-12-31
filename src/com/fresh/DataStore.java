package com.fresh;

import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataStore {
    private static final String LOCK="1";
    private static final String UNLOCK="0";
    private static String defpath="/Users/yuvaraj/Desktop/jav/";//default folder path for all data stores (path format= /Users/username/Desktop/jav/)
    private static String path="";

    void setup() throws IOException {
        //Setup the initial directory
        System.out.println("Done1");
        File dir= new File(defpath);
        System.out.println(dir.mkdir());
    }

    void createFile() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the file name");
        String name= sc.nextLine();
        File fileobj= new File("/Users/yuvaraj/Desktop/jav/"+name);
        if(!fileobj.createNewFile())
        {
            System.out.println("File already exists.");
            return;
        }
        else{
            path=path.concat(defpath).concat(name);
            RandomAccessFile ra= new RandomAccessFile(path,"rw");
            ra.seek(0);
            ra.write("0\n".getBytes());
            System.out.println("File created succesfully. File path set automatically");
        }
    }

    void writeData(String key, JSONObject value) throws IOException {
        if(value.toString().getBytes().length>16000)
        {
            System.out.println("Unable to write data. Size of JSON value >16KB");
            return;
        }
        String val="";
        BufferedReader reader= new BufferedReader(new FileReader("/Users/yuvaraj/Desktop/jav/store2.txt"));
        String line = reader.readLine();
        if(line.equals("0")){
            changeAccessStatus(LOCK);
        }
        else {
            System.out.println("File already in access by another process. Please try again later.");
            return;
        }
        if (key.length() > 32) {
                System.out.println("Enter a key value <32 chars! Unable to perform write!");
                changeAccessStatus(UNLOCK);
                return;
            }
        line=reader.readLine();
        while (line != null)
        {
            if(line.substring(0,line.indexOf(":")).equals(key))
            {
                System.out.println("Value already exists");
                changeAccessStatus(UNLOCK);
                return;
            }
            else{
                line=reader.readLine();
            }
        }

        System.out.println("*&"+key);
            val=val+key+":"+value.toString()+"\n";
            try {
                Path filepath = Path.of("/Users/yuvaraj/Desktop/jav/store2.txt");
                Files.writeString(filepath,val, StandardOpenOption.APPEND);
            }
            catch (IOException e){System.out.println("Failed");}
            changeAccessStatus(UNLOCK);
    }

    void readData(String val) throws IOException {
        BufferedReader reader= new BufferedReader(new FileReader("/Users/yuvaraj/Desktop/jav/store2.txt"));
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
        if(line != null){
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
   private void changeAccessStatus(String val) throws IOException {
        RandomAccessFile ra=new RandomAccessFile("/Users/yuvaraj/Desktop/jav/store1.txt","rw");
        ra.seek(0);
        ra.write(val.getBytes());
        ra.close();
    }

    void selectStore(){
        System.out.println("Enter the file name to select (with .txt extension)");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        path=defpath+name;
        System.out.println("store name set"+path);
    }



}
