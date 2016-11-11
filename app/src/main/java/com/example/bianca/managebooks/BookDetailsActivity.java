package com.example.bianca.managebooks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class BookDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText titleText = (EditText) findViewById(R.id.titleDetail);
        final EditText authorText = (EditText) findViewById(R.id.authorDetail);
        final EditText yearText = (EditText) findViewById(R.id.yearDetail);
        Intent i = getIntent();
        String title = i.getExtras().getString("title");
        String authorName = i.getExtras().getString("author");
        String year = i.getExtras().getString("year");
        final Integer position = Integer.valueOf(i.getExtras().getString("position"));

        titleText.setText(title);
        authorText.setText(authorName);
        yearText.setText(year);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.books.get(position).setTitle(titleText.getText().toString());
                MainActivity.books.get(position).setAuthorName(authorText.getText().toString());
                MainActivity.books.get(position).setPublicationYear(Integer.valueOf(yearText.getText().toString()));
                MainActivity.bookAdapter.notifyDataSetChanged();

                Toast.makeText(view.getContext(), "The data of the book was modified", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
