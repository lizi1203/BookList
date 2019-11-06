package com.casper.testdrivendevelopment;

import android.content.Context;

import com.casper.testdrivendevelopment.data.mode.Book;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileDataSource {
    Context context;

    FileDataSource(Context context) {
        this.context = context;
    }

    public ArrayList<Book> getBooks() {//将数据暴露
        return books;
    }

    private ArrayList<Book> books = new ArrayList<Book>();

    public void save()
    {

        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
            context.openFileOutput("Serializable.txt", context.MODE_PRIVATE));
            outputStream.writeObject(books);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } ;

    }
        public ArrayList<Book> load ()
        {
            ObjectInputStream inputStream = null;
            try {
                inputStream = new ObjectInputStream(context.openFileInput("Serializable.txt"));
                books = (ArrayList<Book>) inputStream.readObject();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return books;

        }
    }



