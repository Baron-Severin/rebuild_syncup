/*

todo: saving availability saves to xml
todo: delete availability buttons

todo: friends list

todo: networking

todo: data visualization

*/

package com.example.severin.rebuildsyncup;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import org.joda.time.DateTime;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // current_textview and start_vs_end are used when saving and setting text from DatePicker and TimePicker dialogs
    static TextView current_textview;
    static String start_vs_end;

    // next_saved_entry_row determines the grid placement of saved availability TextViews
    static int next_saved_entry_row = 1;

    // entry_holder retains current value of filled in fields for redraws
    static HashMap <String, Object> entry_holder = new <String, Object> HashMap();

    // calls to show TimePicker dialog
    public void start_time_picker_dialog(View view) {
        DialogFragment new_fragment = new TimePickerFragment();
        new_fragment.show(getFragmentManager(), "timePicker");

        current_textview = (TextView) view;
        start_vs_end = "start";
    }

    public void end_time_picker_dialog(View view) {
        DialogFragment new_fragment = new TimePickerFragment();
        new_fragment.show(getFragmentManager(), "timePicker");

        current_textview = (TextView) view;
        start_vs_end = "end";
    }

    // defines fragment that creates TimePicker dialog
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {



        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new TimePickerDialog(getActivity(), this, 12, 0, DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hour, int minute) {
            String Hour;
            // rounds minute down to the nearest 5
            String display_time;
            minute = Math.round(minute / 5) * 5;
            if (hour > 12) {
                Hour = String.valueOf(hour - 12);
            } else {
                Hour = String.valueOf(hour);
            }

            // rules for making display text more human readable
            String Minute = String.valueOf(minute);
            if (Hour.equals("0")) {
                Hour = "00";
            }
            if (Minute.equals("0")) {
                Minute = "00";
            }

            if (hour == 0 && minute == 0) {
                current_textview.setText(R.string.midnight);
                display_time = "Midnight";

            } else if (hour < 12) {
                String temporary_text = Hour + ":" + Minute + " AM";
                current_textview.setText(temporary_text);
                display_time = temporary_text;
            } else if (hour == 12 && minute == 0) {
                current_textview.setText(R.string.noon);
                display_time = "Noon";
            } else {
                String temporary_text = Hour + ":" + Minute + " PM";
                current_textview.setText(temporary_text);
                display_time = Hour + ":" + Minute + " PM";
            }

            if (start_vs_end.equals("start")) {
                entry_holder.put("start_hour", hour);
                entry_holder.put("start_minute", minute);
                entry_holder.put("start_display", display_time);
            } else {
                entry_holder.put("end_hour", hour);
                entry_holder.put("end_minute", minute);
                entry_holder.put("end_display", display_time);
            }

        }

    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Set current date as default
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);

        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            // Month is 0-11. Some methods access in 0-11 format, some in 1-12. SMH
            month += 1;

            entry_holder.put("year", year);
            entry_holder.put("month", getMonth(month));
            entry_holder.put("day_int", day);

            DateTime date_time = new DateTime(year, month, day, 0, 0);

            String day_of_week = date_time.dayOfWeek().getAsText();

            entry_holder.put("day_of_week", day_of_week);

            String temporary_string = day_of_week + ", " + getMonth(month) + " " + day;
            current_textview.setText(temporary_string);

        }

        public String getMonth(int month) {
            return new DateFormatSymbols().getMonths()[month-1];
        }

    }

    public void show_date_picker_dialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");

        current_textview = (TextView) view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // fill in editable fields for redraws
        if (entry_holder.isEmpty() == false) {

            if (entry_holder.get("day_of_week") != "null") {
                current_textview = (TextView) findViewById(R.id.day_1);

                current_textview.setText(entry_holder.get("day_of_week") + ", " + entry_holder.get("month") + " " +
                        entry_holder.get("day_int"));
            }

            if (entry_holder.get("start_display") != null) {
                current_textview = (TextView) findViewById(R.id.from_time_1);

                current_textview.setText("" + entry_holder.get("start_display"));
            }

            if (entry_holder.get("end_display") != null) {
                current_textview = (TextView) findViewById(R.id.until_time_1);

                current_textview.setText("" + entry_holder.get("end_display"));
            }

        }

    }

    public void save_entry(View view) {

        if (entry_holder.get("day_int") != null && entry_holder.get("end_display") != null &&
                entry_holder.get("start_display") != null) {
            build_availability_button(entry_holder.get("day_of_week"), entry_holder.get("month"), entry_holder.get("day_int"), entry_holder.get("start_display"), entry_holder.get("end_display"));

            // store and delete current entries
            //push_entries_to_storage(entry_holder);
            // todo: maybe this method should do something?

            System.out.println(entry_holder);
            // todo: add sound (or otherfeedback) to save
        } else {
            //todo: "put some stuff in the TextEdits to save, moron" message
        }
    }

    public void build_availability_button (Object day_of_week, Object month, Object day_int, Object start_display,
                                           Object end_display) {
        GridLayout my_layout = (GridLayout) findViewById(R.id.saved_availability_layout);

        // format textview
        TextView my_textview = new TextView(this);
        Button my_button = new Button(this);

        GridLayout.Spec row_spec = GridLayout.spec(next_saved_entry_row);
        GridLayout.Spec column_spec = GridLayout.spec(0);

        my_textview.setLayoutParams(new ViewGroup.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.MATCH_PARENT));
        GridLayout.LayoutParams params;

        params = new GridLayout.LayoutParams(row_spec, column_spec);
        params.setMargins(0, 5, 0, 5);

        my_textview.setLayoutParams(params);
        my_textview.setText("Available " + day_of_week + " " + month +
                " " + day_int + ", from " + start_display +
                " until " + end_display);

        // add textview
        my_layout.addView(my_textview);
        next_saved_entry_row += 1;

        // format button
        row_spec = GridLayout.spec(next_saved_entry_row);

        params = new GridLayout.LayoutParams(row_spec, column_spec);
        float my_float = 28f;
        params.height = dp_to_px(this.getApplicationContext(), my_float);

        my_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange_button));
        my_button.setText("Delete");
        my_button.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        my_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        my_button.setAllCaps(false);
        my_button.setPadding(0, 0, 0, 0);

        // add button
        my_button.setLayoutParams(params);
        my_layout.addView(my_button);

        next_saved_entry_row += 1;
    }

    public static int dp_to_px (Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return  Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics));
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

}


                    /*
        entry_holder
            day_int
            day_of_week
            month
            year
            start_hour
            start_minute
            start_display
            end_hour
            end_minute
            end_display
         */

