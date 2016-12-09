package com.example.bianca.managebooks;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bianca.managebooks.contentprovider.BookContentProvider;
import com.example.bianca.managebooks.database.BookTable;
import com.example.bianca.managebooks.pickers.YearPicker;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

public class BookDetailsActivity extends AppCompatActivity {
    private Uri bookUri;
    private EditText titleText;
    private EditText authorText;
    private static TextView yearText;
    private EditText priceText;
    private Long id;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titleText = (EditText) findViewById(R.id.titleDetail);
        authorText = (EditText) findViewById(R.id.authorDetail);
        yearText = (TextView) findViewById(R.id.yearDetail);
        priceText = (EditText) findViewById(R.id.priceDetail);
        Intent i = getIntent();

        Bundle extras = getIntent().getExtras();

        // check from the saved Instance
        bookUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState
                .getParcelable(BookContentProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        if (extras != null) {
            bookUri = extras
                    .getParcelable(BookContentProvider.CONTENT_ITEM_TYPE);

            fillData(bookUri);
        }
        getChart();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(titleText.getText().toString()) || TextUtils.isEmpty(authorText.getText().toString())
                        || TextUtils.isEmpty(yearText.getText().toString()) || TextUtils.isEmpty(priceText.getText().toString())) {
                    Toast.makeText(view.getContext(), "No field can be empty", Toast.LENGTH_SHORT).show();
                } else {
                    BookDetailsActivity.this.saveState();
                    Toast.makeText(view.getContext(), "The data of the book was modified", Toast.LENGTH_SHORT).show();

                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    private void fillData(Uri uri) {
        String[] projection = {BookTable.COLUMN_ID, BookTable.COLUMN_TITLE,
                BookTable.COLUMN_AUTHOR, BookTable.COLUMN_PUBLICATION_YEAR, BookTable.COLUMN_PRICE };
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            id = cursor.getLong(cursor.getColumnIndexOrThrow(BookTable.COLUMN_ID));
            titleText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(BookTable.COLUMN_TITLE)));
            authorText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(BookTable.COLUMN_AUTHOR)));
            yearText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(BookTable.COLUMN_PUBLICATION_YEAR)));
            priceText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(BookTable.COLUMN_PRICE)));
            // always close the cursor
            cursor.close();
        }
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

        // Update book
        getContentResolver().update(bookUri, values, null, null);
    }

    public void showYearDialog(View v) {
        YearPicker yearFragment = new YearPicker();
        yearFragment.setActivityType("details");
        yearFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static void setYear(int year) {
        yearText.setText(String.valueOf(year));
    }

    public void getChart() {
        XYSeries series = new XYSeries("");
        String[] projection = {BookTable.COLUMN_TITLE, BookTable.COLUMN_PRICE };
        Cursor cursor = getContentResolver().query(BookContentProvider.CONTENT_URI, projection, null, null,
                null);
        List<String> text = new ArrayList<>();
        cursor.moveToFirst();
        for(int i = 0; i < cursor.getCount();i++) {
            text.add(cursor.getString(0));
            series.add(i, cursor.getInt(1));
            cursor.moveToNext();
        }
        cursor.close();

        // Now we create the renderer
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(2);
        renderer.setColor(Color.BLUE);
        // Include low and max value
        renderer.setDisplayBoundingPoints(true);
        // we add point markers
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setPointStrokeWidth(3);

        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);
        mRenderer.setXLabelsColor(Color.BLACK);
        mRenderer.setLabelsTextSize(30);
        mRenderer.clearXTextLabels();

        int index = 0;
        for(String str : text) {
            mRenderer.addXTextLabel(index, str);
            mRenderer.setXLabels(0);
            index++;
        }
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
        // Disable Pan on two axis
        mRenderer.setPanEnabled(false, false);
        mRenderer.setYAxisMax(35);
        mRenderer.setYAxisMin(0);
        mRenderer.setShowGrid(true); // we show the grid
        mRenderer.setBarSpacing(2);
        mRenderer.setChartTitle("Book prices");
        mRenderer.setChartTitleTextSize(50);
        mRenderer.setLegendTextSize(50);

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);

        GraphicalView chartView = ChartFactory.getBarChartView(this, dataset, mRenderer, BarChart.Type.DEFAULT);

        LinearLayout chartLyt = (LinearLayout) findViewById(R.id.chartView);
        chartLyt.addView(chartView, 0);
        }

}
