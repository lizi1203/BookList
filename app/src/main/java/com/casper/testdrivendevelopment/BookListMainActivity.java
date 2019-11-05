package com.casper.testdrivendevelopment;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BookListMainActivity extends AppCompatActivity {
    private static final int  MENU_ITEM_NEW =1;
    private static final int MENU_ITEM_UPDATE = 2;
    private static final int MENU_ITEM_DELETE = 3;
    private static final int MENU_ITEM_ABOUT = 4;
    private static final int REQUEST_CODE_NEW = 901;
    private static final int REQUEST_CODE_UPDATE = 902;


    private ArrayList<Book>listBooks=new ArrayList<Book>();
    private BooksArrayAdapter bookAdapter;
    private FileDataSource fileDataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);
        fileDataSource=new FileDataSource(this);
        listBooks=fileDataSource.load();
        if (listBooks.size()==0) {
            InitData();
        }
        bookAdapter=new BookListMainActivity.BooksArrayAdapter(this,R.layout.list_item_book,listBooks);
        BookFragmentAdapter myPageAdapter = new BookFragmentAdapter(getSupportFragmentManager());

        ArrayList<Fragment> datas = new ArrayList<Fragment>();
        datas.add(new BookListFragment(bookAdapter));
        datas.add(new WebViewFragment());
        datas.add(new MapViewFragment());

        myPageAdapter.setData(datas);

        ArrayList<String> titles = new ArrayList<String>();
        titles.add("图书");
        titles.add("新闻");
        titles.add("卖家");
        myPageAdapter.setTitles(titles);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(myPageAdapter);
        tabLayout.setupWithViewPager(viewPager,true);




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fileDataSource.save();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v==findViewById(R.id.list_view_books)){
            int itemPosition=((AdapterView.AdapterContextMenuInfo)menuInfo).position;
            menu.setHeaderTitle(listBooks.get(itemPosition).getTitle());
            menu.add(0,MENU_ITEM_NEW,0,"新建");
            menu.add(0,MENU_ITEM_UPDATE,0,"修改");
            menu.add(0,MENU_ITEM_DELETE,0,"删除");
            menu.add(0,MENU_ITEM_ABOUT,0,"关于");
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case REQUEST_CODE_NEW:
                if(resultCode==RESULT_OK){
                    int position=data.getIntExtra("edit_position",0);
                    String bookTitle=data.getStringExtra("book_title");
                    listBooks.add(position, new Book(bookTitle,R.drawable.book_new));

                    bookAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "新建成功", Toast.LENGTH_LONG).show();
                }
                break;
            case REQUEST_CODE_UPDATE:
                if(resultCode==RESULT_OK){
                    int position=data.getIntExtra("edit_position",0);
                    String bookTitle=data.getStringExtra("book_title");
                    Book book=listBooks.get(position);
                    book.setTitle(bookTitle);
                    bookAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "修改成功", Toast.LENGTH_LONG).show();
                }

        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId ()){            //获取Id
            case MENU_ITEM_NEW: {
                AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                Intent intent=new Intent(BookListMainActivity.this,EditBookActivity.class);
                intent.putExtra("edit_position",menuInfo.position);
                startActivityForResult(intent,REQUEST_CODE_NEW);
                break;
            }
            case MENU_ITEM_UPDATE:{
                AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                Book book=listBooks.get(menuInfo.position);
                Intent intent =new Intent (BookListMainActivity.this,EditBookActivity.class);
                intent.putExtra("edit_position",menuInfo.position);
                intent.putExtra("book_title",book.getTitle());
                startActivityForResult(intent,REQUEST_CODE_UPDATE);
                break;
            }
            case MENU_ITEM_DELETE: {
                final AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                final int itemPosition=menuInfo.position;
                new android.app.AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("询问")
                        .setMessage("你确定要删除这条吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            listBooks.remove(itemPosition);
                                bookAdapter.notifyDataSetChanged();
                                Toast.makeText(BookListMainActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create().show();
                break;
            }
            case MENU_ITEM_ABOUT: {
                Toast.makeText(this, "版权所有 by LXu", Toast.LENGTH_LONG).show();
                break;
            }
        }
        return super.onContextItemSelected(item);
    }




    private void InitData() {
            listBooks.add(new Book("空",  R.drawable.book_1));
    }

    public List<Book> getListBooks() {
        return listBooks;
    }

    class BooksArrayAdapter extends ArrayAdapter<Book> {
        private int resourceId;

        public BooksArrayAdapter( Context context, int resource, List<Book> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position,  View convertView, ViewGroup parent) {
            Book book=getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            ImageView img = (ImageView) view.findViewById(R.id.image_view_book_cover);
            TextView title = (TextView) view.findViewById(R.id.text_view_book_title);

            img.setImageResource(book.getCoverResourceId());
            title.setText(book.getTitle());
            return view;
        }
    }
}
