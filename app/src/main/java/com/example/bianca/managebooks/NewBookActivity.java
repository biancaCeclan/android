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

import com.example.bianca.managebooks.model.Book;

public class NewBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final EditText titleText = (EditText) findViewById(R.id.titleNew);
        final EditText authorText = (EditText) findViewById(R.id.authorNew);
        final EditText yearText = (EditText) findViewById(R.id.yearNew);

        Intent i = getIntent();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book book = new Book();
                book.setTitle(titleText.getText().toString());
                book.setAuthorName(authorText.getText().toString());
                book.setPublicationYear(Integer.valueOf(yearText.getText().toString()));
                MainActivity.books.add(book);
                MainActivity.bookAdapter.notifyDataSetChanged();

                // send email using an email client
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"anad_95@yahoo.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Car insertion");
                String emailBody = "The book : \n " + book.toString() + "\n  was inserted.";
                emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

                try {
                    startActivity(Intent.createChooser(emailIntent, "Email client:"));
                    finish();
                }catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(view.getContext(), "No email client is installed.", Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(view.getContext(), "A new book was inserted", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
