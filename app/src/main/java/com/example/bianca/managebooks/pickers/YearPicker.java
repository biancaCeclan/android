package com.example.bianca.managebooks.pickers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.support.v4.app.DialogFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.bianca.managebooks.BookDetailsActivity;
import com.example.bianca.managebooks.NewBookActivity;

import java.util.Calendar;

public class YearPicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private String activityType;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog, this, year, 0, 0);

        ((ViewGroup) dpd.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
        ((ViewGroup) dpd.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("month", "id", "android")).setVisibility(View.GONE);

        return dpd;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (activityType.equals("details")) {
            BookDetailsActivity.setYear(year);
        } else {
            NewBookActivity.setYear(year);
        }
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
}
