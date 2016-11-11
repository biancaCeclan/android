package com.example.bianca.managebooks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bianca.managebooks.model.Book;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    public static List<Book> books;
    public static BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Context context = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bookIntent = new Intent(context, NewBookActivity.class);
                startActivity(bookIntent);
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        createBooks();
        bookAdapter = new BookAdapter(this, books);
        listView.setAdapter(bookAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = books.get(position);

                Intent detailIntent = new Intent(context, BookDetailsActivity.class);
                detailIntent.putExtra("title", book.getTitle());
                detailIntent.putExtra("author", book.getAuthorName());
                detailIntent.putExtra("year", String.valueOf(book.getPublicationYear()));
                // put the position in order to be able to modify the car when the save button is clicked
                detailIntent.putExtra("position", String.valueOf(position));

                startActivity(detailIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createBooks() {
        books = new ArrayList<>();
        for(int i=0;i<10;i++) {
            Book book = new Book("Title"+i, "Author"+i, 1990+i);
            books.add(book);
        }
    }
}
