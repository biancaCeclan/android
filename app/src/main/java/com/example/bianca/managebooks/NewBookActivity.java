package com.example.bianca.managebooks;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bianca.managebooks.contentprovider.BookContentProvider;
import com.example.bianca.managebooks.database.BookTable;
import com.example.bianca.managebooks.model.Book;
import com.example.bianca.managebooks.pickers.YearPicker;

public class NewBookActivity extends AppCompatActivity {
    private Uri bookUri;
    private EditText titleText;
    private EditText authorText;
    private static TextView yearText;
    private EditText priceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        titleText = (EditText) findViewById(R.id.titleNew);
        authorText = (EditText) findViewById(R.id.authorNew);
        yearText = (TextView) findViewById(R.id.yearNew);
        priceText = (EditText) findViewById(R.id.priceNew);

        Intent i = getIntent();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(titleText.getText().toString()) || TextUtils.isEmpty(authorText.getText().toString())
                        || TextUtils.isEmpty(yearText.getText().toString()) || TextUtils.isEmpty(priceText.getText().toString())) {
                    Toast.makeText(view.getContext(), "No field can be empty", Toast.LENGTH_SHORT).show();
                } else {
                    NewBookActivity.this.saveState();
                    Toast.makeText(view.getContext(), "A new book was inserted", Toast.LENGTH_SHORT).show();

                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(BookContentProvider.CONTENT_ITEM_TYPE, bookUri);
    }

    public void saveState() {
        String title = titleText.getText().toString();
        String author = authorText.getText().toString();
        Integer year = Integer.valueOf(yearText.getText().toString());
        Integer price = Integer.valueOf(priceText.getText().toString());

        // only save if either title or author name
        // is available
        if (title.length() == 0 && author.length() == 0) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(BookTable.COLUMN_TITLE, title);
        values.put(BookTable.COLUMN_AUTHOR, author);
        values.put(BookTable.COLUMN_PUBLICATION_YEAR, year);
        values.put(BookTable.COLUMN_PRICE, price);

        // New book
        bookUri = getContentResolver().insert(
                BookContentProvider.CONTENT_URI, values);

        // send email using an email client
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"bianca_ceclan@yahoo.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Book insertion");
        String emailBody = "The book : \n " + new Book(title, author, year, price).toString() + "\n  was inserted.";
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

        try {
            startActivity(Intent.createChooser(emailIntent, "Email client:"));
            finish();
        }catch (android.content.ActivityNotFoundException ex) {
        }
    }

    public void showYearDialog(View v) {
        YearPicker yearFragment = new YearPicker();
        yearFragment.setActivityType("newBook");
        yearFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static  void setYear(int year) {
        yearText.setText(String.valueOf(year));
    }

}
