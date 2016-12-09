package com.example.bianca.managebooks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bianca.managebooks.model.Book;

import java.util.List;

public class BookAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Book> bookList;

    public BookAdapter(Context context, List<Book> bookList) {
        this.bookList = bookList;
        this.context = context;
        layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View contextView, ViewGroup parent) {
        // Get the view for the row item
        View rowView = layoutInflater.inflate(R.layout.item, parent, false);

        TextView makeTextView = (TextView) rowView.findViewById(R.id.title);

        Book book = (Book) getItem(position);
        makeTextView.setText(book.getTitle());

        return rowView;
    }

    @Override
    public void notifyDataSetChanged() {
        // used when the data is changed in the details view
        super.notifyDataSetChanged();
    }
}
