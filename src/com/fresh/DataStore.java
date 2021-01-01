package com.fresh;

import org.json.JSONException;
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
    private static String finalpath="";
    private static final long FILESIZE=1000000000;

    void setup() throws IOException {
        //Setup the initial directory
        File dir= new File(defpath);
        dir.mkdirs();
    }

    void createFile() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the file name");
        String name= sc.nextLine();
        File fileobj= new File(defpath+name);
        if(!fileobj.createNewFile())
        {
            System.out.println("File already exists.");
            return;
        }
        else{
            finalpath="";
            finalpath=finalpath.concat(defpath).concat(name);
            RandomAccessFile ra= new RandomAccessFile(finalpath,"rw");
            ra.seek(0);
            ra.write("0\n".getBytes());
            System.out.println("File created succesfully. File path set automatically");
        }
    }

    void writeData(String key, JSONObject value, String time) throws IOException {
        if(value.toString().getBytes().length>16000)
        {
            System.out.println("Unable to write data. Size of JSON value >16KB");
            return;
        }
        String val="";
        BufferedReader reader= new BufferedReader(new FileReader(finalpath));
        String line = reader.readLine();
        if(line.equals("0")){
            changeAccessStatus(LOCK);
        }
        else {
            System.out.println("File already in access by another process. Please try again later.");
            reader.close();
            return;
        }
        if (key.length() > 32) {
                System.out.println("Enter a key value <32 chars! Unable to perform write!");
                changeAccessStatus(UNLOCK);
                reader.close();
                return;
            }
        if(Files.size(Path.of(finalpath))>FILESIZE){
            System.out.println("File Size greater than 1GB. Unable to perform write.");
            changeAccessStatus(UNLOCK);
            return;
        }
        line=reader.readLine();
        while (line != null)
        {
            if(line.substring(0,line.indexOf(":")).equals(key))
            {
                System.out.println("Value already exists");
                reader.close();
                changeAccessStatus(UNLOCK);
                return;
            }
            else{
                line=reader.readLine();
            }
        }

            val=val+key+":"+value.toString()+"*/*"+time+"\n";
            try {
                Path filepath = Path.of(finalpath);
                Files.writeString(filepath,val, StandardOpenOption.APPEND);
            }
            catch (IOException e){System.out.println("Failed");}
            reader.close();
            changeAccessStatus(UNLOCK);
    }

    JSONObject readData(String val) throws IOException, JSONException {
        BufferedReader reader= new BufferedReader(new FileReader(finalpath));
        String line= reader.readLine();
        int len=val.length();
        if(line.equals("0")){
            changeAccessStatus(LOCK);
            line=reader.readLine();
        }
        else{
            System.out.println("File already in access by another process. Please try again later.");
            return null;
        }
        while(line != null) //Check whether you need this variable
        {
            if(val.equals(line.substring(0,line.indexOf(":")))){
                String time=line.substring(line.indexOf("*/*")+3,line.length());
                if(time.equals("0")){
                    System.out.println("Value found");
                    System.out.println(line.substring(len+1,line.indexOf("*/*")));
                    changeAccessStatus(UNLOCK);
                    JSONObject js=new JSONObject(line.substring(len+1,line.indexOf("*/*")));
                    return js;
                }
                else if((System.currentTimeMillis()/1000)>Long.parseLong(time))
                {
                    System.out.println("Value time limit over!");
                    changeAccessStatus(UNLOCK);
                    deleteData(val);
                    reader.close();
                    return null;
                }
            }
            else {line=reader.readLine();}
        }
        System.out.println("The value is not found");
        reader.close();
        changeAccessStatus(UNLOCK);
        return null;
    }

    void deleteData(String val) throws IOException {
        List<String> data= new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new FileReader(finalpath));
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
            if(val.equals(line.substring(0,line.indexOf(":")))){
                line=reader.readLine();
            }
            else {
                data.add(line);
                line=reader.readLine();
            }
        }
        Path path = Path.of(finalpath);
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
        RandomAccessFile ra=new RandomAccessFile(finalpath,"rw");
        ra.seek(0);
        ra.write(val.getBytes());
        ra.close();
    }

    void selectStore(){
        System.out.println("Enter the file name to select (with .txt extension)");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        finalpath="";
        finalpath=defpath+name;
        System.out.println("store name set"+finalpath);
    }



}
