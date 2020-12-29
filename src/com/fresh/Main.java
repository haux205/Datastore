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
        while(true) {
            ds=new DataStore();
            System.out.println("File based data store menu");
            System.out.println("Select operation from the menu \n" +
                            "1.Create File \n" +
                            "2.Write data \n" +
                            "3.Read data \n" +
                            "4.Delete data \n" +
                            "5.Exit");
            Scanner sc=new Scanner(System.in);
            menu=0;
            menu=sc.nextInt();

            switch(menu){
                case 1: ds.createFile();break;

                case 2: getInput();break;

                case 3:
                    System.out.println("Enter the key value to be searched");
                    ds.readData("ts");
                    break;

                case 4: ds.deleteData("key123");break;

                case 5: System.exit(0);

            }
        }
    }

    private static void getInput() throws JSONException, IOException {
        String key;
        Scanner s= new Scanner(System.in);
        key=s.nextLine();
        JSONObject json= new JSONObject();
        json.put("name","sonoo");
        json.put("age",27);
        json.put("salary",600000);
        ds.writeData(key,json);
    }

}
