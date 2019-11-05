package com.casper.testdrivendevelopment;

import android.content.Context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by jszx on 2019/10/22.
 */

public class DataSourceFile {
    private Context context;

    public DataSourceFile(Context context) {
        this.context = context;
    }

  public ArrayList<Book> getBooks(){
        return books;
    }

    private ArrayList<Book> books=new ArrayList<>();

    public void save(){
        try {
            ObjectOutputStream outputStream=new ObjectOutputStream
                    (context.openFileOutput("serializable.txt",Context.MODE_PRIVATE));
            outputStream.writeObject(books);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Book> load(){
        try {
            ObjectInputStream inputStream=new ObjectInputStream
                    (context.openFileInput("Serializable.txt"));
            books=(ArrayList<Book>)inputStream.readObject();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }
}
