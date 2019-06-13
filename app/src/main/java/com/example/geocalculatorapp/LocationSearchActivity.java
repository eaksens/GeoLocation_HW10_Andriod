package com.example.geocalculatorapp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.joda.time.DateTime;
import org.parceler.Parcels;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationSearchActivity extends AppCompatActivity {

    int origLocationField_AUTOCOMPLETE_REQUEST_CODE = 1;
    int dstLocationField_AUTOCOMPLETE_REQUEST_CODE = 2;
    private static final String TAG = "LocationSearchActivity";
    //@BindView(R.id.locationInfo) EditText locationInfoLabel;
    @BindView(R.id.origLocationField) TextView origLocationField;
    @BindView(R.id.dstLocationField) TextView dstLocationField;
    //@BindView(R.id.calculationDate) TextView calDateLabel;
    @BindView(R.id.datePickerField) TextView calDateView;


    private DateTime calDate;
    private LocationLookup location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

/*        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        location = new LocationLookup();

    }

    //Once click on original Location and destination location to search API google
    @OnClick(R.id.origLocationField)
    public void origLocationPressed(){
        // Requested details: PlaceId, name, and address
        //place picker -google code that enable to select the location
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        Intent intent =
                new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(this);
        startActivityForResult(intent, origLocationField_AUTOCOMPLETE_REQUEST_CODE);
    }

    @OnClick(R.id.dstLocationField)
    public void dstLocationPressed(){
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        Intent intent =
                new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(this);
        startActivityForResult(intent, dstLocationField_AUTOCOMPLETE_REQUEST_CODE);
    }

    @OnClick(R.id.datePickerField)
    public void datePickerPressed()
    {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @OnClick(R.id.fab)
    public void FABPressed() {
        Intent result = new Intent();
        Parcelable parcel = Parcels.wrap(location);
        result.putExtra("SEARCH_RESULTS", parcel);
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == origLocationField_AUTOCOMPLETE_REQUEST_CODE || requestCode == dstLocationField_AUTOCOMPLETE_REQUEST_CODE)  {
            TextView target = requestCode == origLocationField_AUTOCOMPLETE_REQUEST_CODE ? origLocationField : dstLocationField;
            //if operation is successful
            if (resultCode == RESULT_OK)
            {
                Place pl = Autocomplete.getPlaceFromIntent(data);
                target.setText(pl.getName());
                LatLng position = pl.getLatLng();
                if(target == origLocationField && position != null) {
                    location.origLat = pl.getLatLng().latitude;
                    location.origLng = pl.getLatLng().longitude;
                } else if (position != null){
                    location.destLat = pl.getLatLng().latitude;
                    location.destLng = pl.getLatLng().longitude;
                }
                Log.i(TAG, "onActivityResult: " + pl.getName() + "/" + pl.getAddress());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Log.d(TAG, "onActivityResult: error");
            }
            //if the operation is failed or the user backed out
            else if (requestCode == RESULT_CANCELED){
                System.out.println("Cancelled by the user");
            }
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    public static class DatePickerFragment extends DialogFragment
            implements android.app.DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            DateTime now = DateTime.now();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = now.dayOfMonth().get();;

            // Create a new instance of DatePickerDialog and return it
            return new android.app.DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            DateTime d = new DateTime(year, month + 1, day, 0, 0);
            LocationSearchActivity activity = (LocationSearchActivity) getActivity();
            activity.calDateView.setText(formatted(d));
        }

        private String formatted(DateTime d) {
            return d.monthOfYear().getAsShortText(Locale.getDefault()) + " " +
                    d.getDayOfMonth() + ", " + d.getYear();
        }
    }
}
