package com.example.bianca.managebooks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bianca.managebooks.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class BookAdapter extends BaseAdapter implements Observer {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Book> bookList;

    public BookAdapter(Context context){
        this.bookList = new ArrayList<>();
        this.context = context;
        layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void add(Book book) {
        bookList.add(book);
    }

    public void update(Book book) {
        for(Book b : bookList) {
            if(b.getUuid().equals(book.getUuid())) {
                b.setTitle(book.getTitle());
                b.setAuthorName(book.getAuthorName());
                b.setPublicationYear(book.getPublicationYear());
                b.setPrice(book.getPrice());
                return;
            }
        }
    }

    public void remove(Book book) {
        bookList.remove(book);
    }

    @Override
    public void update(Observable o, Object arg) {
        notifyDataSetChanged();
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

        TextView makeTextView = (TextView) rowView.findViewById(R.id.titleItem);

        Book book = (Book) getItem(position);
        makeTextView.setText(book.getTitle());

        return rowView;
    }

    @Override
    public void notifyDataSetChanged() {
        // used when the data is changed in the details view
        super.notifyDataSetChanged();
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public List<String> getTitles() {
        List<String> titles = new ArrayList<>();
        for(Book b : bookList) {
            titles.add(b.getTitle());
        }
        return titles;
    }

    public List<Integer> getPrices() {
        List<Integer> prices= new ArrayList<>();
        for(Book b : bookList) {
            prices.add(b.getPrice());
        }
        return prices;
    }
}
