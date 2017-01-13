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
import com.example.bianca.managebooks.model.Book;
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
import java.util.Map;

public class BookDetailsActivity extends AppCompatActivity {
    private EditText titleText;
    private EditText authorText;
    private static TextView yearText;
    private EditText priceText;
    private Long id;
    private List<String> titles;
    private List<Integer> prices;
    private String uuid;

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

        String title = i.getExtras().getString("title");
        String author = i.getExtras().getString("author");
        String year = i.getExtras().getString("year");
        String price = i.getExtras().getString("price");
        uuid = i.getExtras().getString("uuid");
        titles = i.getExtras().getStringArrayList("titles");
        prices = i.getExtras().getIntegerArrayList("prices");

        titleText.setText(title);
        authorText.setText(author);
        yearText.setText(year);
        priceText.setText(price);

        getChart();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(titleText.getText().toString()) || TextUtils.isEmpty(authorText.getText().toString())
                        || TextUtils.isEmpty(yearText.getText().toString()) || TextUtils.isEmpty(priceText.getText().toString())) {
                    Toast.makeText(view.getContext(), "No field can be empty", Toast.LENGTH_SHORT).show();
                } else {
                    final Book book = new Book();
                    book.setTitle(titleText.getText().toString());
                    book.setAuthorName(authorText.getText().toString());
                    book.setPublicationYear(Integer.valueOf(yearText.getText().toString()));
                    book.setPrice(Integer.valueOf(priceText.getText().toString()));
                    book.setUuid(uuid);

                    Map<String,Object> bookMap = book.bookToMap();
                    MainActivity.firebaseUtil.update(uuid, bookMap);

                    Toast.makeText(view.getContext(), "The book was modified", Toast.LENGTH_SHORT).show();

                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
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

        List<String> text = new ArrayList<>();
        for(int i = 0; i < titles.size();i++) {
            text.add(titles.get(i));
            series.add(i, prices.get(i));
        }

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
