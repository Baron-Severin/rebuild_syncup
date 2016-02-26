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
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.joda.time.DateTime;
import org.w3c.dom.Text;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    static TextView current_textview;
    static String start_vs_end;
    static int next_saved_entry_row = 1;

    // entry_holder retains current value of filled in fields for redraws
    static HashMap entry_holder = new HashMap();

    // Each index in current_availabilities is one saved availability block for the currently selected event
    static List<HashMap> current_availabilities = new List<HashMap>() {
        @Override
        public void add(int location, HashMap object) {

        }

        @Override
        public boolean add(HashMap object) {
            return false;
        }

        @Override
        public boolean addAll(int location, Collection<? extends HashMap> collection) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends HashMap> collection) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public boolean contains(Object object) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return false;
        }

        @Override
        public HashMap get(int location) {
            return null;
        }

        @Override
        public int indexOf(Object object) {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @NonNull
        @Override
        public Iterator<HashMap> iterator() {
            return null;
        }

        @Override
        public int lastIndexOf(Object object) {
            return 0;
        }

        @Override
        public ListIterator<HashMap> listIterator() {
            return null;
        }

        @NonNull
        @Override
        public ListIterator<HashMap> listIterator(int location) {
            return null;
        }

        @Override
        public HashMap remove(int location) {
            return null;
        }

        @Override
        public boolean remove(Object object) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            return false;
        }

        @Override
        public HashMap set(int location, HashMap object) {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }

        @NonNull
        @Override
        public List<HashMap> subList(int start, int end) {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(T[] array) {
            return null;
        }
    };

    // define fragment that creates TimePicker dialog
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
            String Minute = String.valueOf(minute);
            if (Hour == "0") {
                Hour = "00";
            }
            if (Minute == "0") {
                Minute = "00";
            }

            if (hour == 0 && minute == 0) {
                current_textview.setText("Midnight");
                display_time = "Midnight";


            } else if (hour < 12) {
                current_textview.setText(Hour + ":" + Minute + " AM");
                display_time = Hour + ":" + Minute + " AM";
            } else if (hour == 12 && minute == 0) {
                current_textview.setText("Noon");
                display_time = "Noon";
            } else {
                current_textview.setText(Hour + ":" + Minute + " PM");
                display_time = Hour + ":" + Minute + " PM";
            }

            if (start_vs_end == "start") {
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

            current_textview.setText(day_of_week + ", " + getMonth(month) + " " + day);

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

    public void save_entry(View view) {

        if (entry_holder.get("day_int") != null && entry_holder.get("end_display") != null &&
                entry_holder.get("start_display") != null) {
            build_availability_button(entry_holder.get("day_of_week"), entry_holder.get("month"), entry_holder.get("day_int"), entry_holder.get("start_display"), entry_holder.get("end_display"));

            // store and delete current entries
            push_entries_to_storage(entry_holder);

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
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(row_spec, column_spec);

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


        my_button.setBackgroundColor((getResources().getColor(R.color.orange_button)));
        my_button.setText("Delete");
        my_button.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        my_button.setTextColor(getResources().getColor(R.color.black));
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

    public void push_entries_to_storage (HashMap entry_holder) {

    }

    // Random logging code
    public void random_logging_code (View view) {
        System.out.println(entry_holder);
        System.out.println(entry_holder.get("year"));
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

