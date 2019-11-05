package com.casper.testdrivendevelopment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by jszx on 2019/10/15.
 */

public class EditBookActivity extends AppCompatActivity{
    private EditText editText;
    private Button buttonOk,buttonCancel;
    private int editPosition;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);
        editText=findViewById(R.id.edit_text_view);
        buttonOk=findViewById(R.id.button_ok);
        buttonCancel=findViewById(R.id.button_cancel);

        editPosition=getIntent().getIntExtra("edit_position",0);
       final String bookTitle=getIntent().getStringExtra("book_title");
       if(bookTitle!=null)
       {
           editText.setText(bookTitle);
       }

       buttonOk.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              Intent intent =new Intent();
              intent.putExtra("edit_position",editPosition);
              intent.putExtra("book_title",editText.getText().toString().trim());
               Log.d("bookTitle", editText.getText().toString());
              setResult(RESULT_OK,intent);
              EditBookActivity.this.finish();
           }
       });

       buttonCancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               EditBookActivity.this.finish();
           }
       });
    }
}
