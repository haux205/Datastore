package com.fresh;

import netscape.javascript.JSObject;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Scanner;


/*Freshworks file based data store*/
public class Main {
static int menu=0;
static DataStore ds;
    public static void main(String[] args) throws IOException, JSONException {
        ds=new DataStore();
        ds.setup();
        while(true) {
            System.out.println("File based data store menu");
            System.out.println("Select operation from the menu \n" +
                            "1.Create File \n" +
                            "2.Write data \n" +
                            "3.Read data \n" +
                            "4.Delete data \n" +
                            "5.Choose data store file \n"+
                            "6.Exit");
            System.out.println("Make sure data store is created");

            Scanner sc=new Scanner(System.in);
            menu=0;
            menu=sc.nextInt();

            switch(menu){
                case 1: ds.createFile();break;

                case 2: getInput();break;

                case 3:
                    System.out.println("Enter the key value to be searched");
                    ds.readData("fin");
                    break;

                case 4: ds.deleteData("te");break;

                case 5: ds.selectStore();break;

                case 6: System.exit(0);

            }
        }
    }

    private static void getInput() throws JSONException, IOException {
        String key;
        long time = System.currentTimeMillis()/1000;
        long t=0;
        System.out.println("Enter the key value");
        Scanner s= new Scanner(System.in);
        key=s.nextLine();
        System.out.println("Enter time-to-live value (in seconds)(Enter '0' if not needed");
        t=s.nextLong();
        if(t==0){
            time=0;
        }
        else{
            time=time+t;
        }
        JSONObject json= new JSONObject();
        json.put("name","sonoo");
        json.put("age",27);
        json.put("salary",600000);
        ds.writeData(key,json,Long.toString(time));
    }

}
