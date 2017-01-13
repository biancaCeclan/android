package com.example.bianca.managebooks;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.bianca.managebooks.contentprovider.BookContentProvider;
import com.example.bianca.managebooks.database.BookTable;
import com.example.bianca.managebooks.firebaseutil.FirebaseUtil;
import com.example.bianca.managebooks.model.Book;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    public static BookAdapter adapter;

    public static FirebaseUtil firebaseUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseUtil = new FirebaseUtil();

        setContentView(R.layout.activity_main);

        final Context context = this;

        if (firebaseUtil.getmFirebaseUser() == null) {
            // Not logged in ==> launch the Log In activity
            loadLogInView();
        } else {
            firebaseUtil.setmUserId(firebaseUtil.getmFirebaseUser().getUid());

            listView = (ListView) findViewById(R.id.listView);
            adapter = new BookAdapter(this);
            firebaseUtil.addObserver(adapter);

            listView.setAdapter(adapter);

            fillData();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Book book = adapter.getBookList().get(position);

                    Intent detailIntent = new Intent(context, BookDetailsActivity.class);
                    detailIntent.putExtra("title", book.getTitle());
                    detailIntent.putExtra("author", book.getAuthorName());
                    detailIntent.putExtra("year", String.valueOf(book.getPublicationYear()));
                    detailIntent.putExtra("price", String.valueOf(book.getPrice()));
                    detailIntent.putExtra("uuid", book.getUuid());

                    detailIntent.putExtra("titles", (ArrayList<String>)adapter.getTitles());
                    detailIntent.putExtra("prices", (ArrayList<Integer>)adapter.getPrices());

                    startActivity(detailIntent);
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final Book book = adapter.getBookList().get(position);
                    final String uuid = book.getUuid();
                    new AlertDialog.Builder(MainActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Delete action")
                            .setMessage("Are you sure you want to delete this book?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    firebaseUtil.remove(uuid, book);
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                    return true;
                }
            });
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bookIntent = new Intent(context, NewBookActivity.class);
                startActivity(bookIntent);
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

        if (id == R.id.action_logout) {
            firebaseUtil.getmFirebaseAuth().signOut();
            loadLogInView();
        }

        return super.onOptionsItemSelected(item);
    }

    private void fillData() {
        firebaseUtil.fillData();
    }

    // for firebase
    private void loadLogInView() {
        Intent intent = new Intent(this, LogInActivity.class);
        // this activity will become the start of a new task on this history stack
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // this flag will cause any existing task that would be associated with the activity to be
        // cleared before the activity is started
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
