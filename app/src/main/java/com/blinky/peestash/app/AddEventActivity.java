package com.blinky.peestash.app;

import android.app.*;
import android.content.res.Configuration;
import android.location.Address;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.blinky.peestash.app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddEventActivity extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private String id_user="";
    List<String> statuts, adresses;
    ArrayAdapter<String> statutAdapter, adresseAdapter;
    Spinner spinnerStatut, spinnerAdresse;
    String statut, adresse;

    private EditText fromDateEtxt, HeureDebut;
    private EditText toDateEtxt, HeureFin;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;

    private CheckBox rock, pop, metal, jazz, funk, electro, blues, rap, folk, classique;
    private String genremusical = "";

    private ArrayList<String> genrelist = new ArrayList<String>();

    EditText Adresse, Cp, Ville, Pays;
    LinearLayout root;
    LinearLayout.LayoutParams rootParams;
    int layoutHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Bundle var = this.getIntent().getExtras();
        id_user=var.getString("id_user");

        findViewsById();

        Locale locale = new Locale("FR");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, null);

        // Spinner element
        spinnerStatut = (Spinner) findViewById(R.id.spinnerStatut);
        // Spinner Drop down elements
        statuts = new ArrayList<String>();

        // Creating adapter for spinner
        statutAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statuts);

        // Spinner click listener
        spinnerStatut.setOnItemSelectedListener(this);

        statuts.add(String.valueOf("Ouvert"));
        statuts.add(String.valueOf("Fermé"));

        // Drop down layout style - list view with radio button
        statutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerStatut.setAdapter(statutAdapter);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        setDateTimeField();
        setHourTimeField();

        addListenerOnChkWindows();
        // Spinner element
        spinnerAdresse = (Spinner) findViewById(R.id.spinnerStatut);
        // Spinner Drop down elements
        adresses = new ArrayList<String>();

        // Creating adapter for spinner
        adresseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, adresses);


        spinnerAdresse = (Spinner) findViewById(R.id.spinnerAdresse);
        // Spinner click listener
        spinnerAdresse.setOnItemSelectedListener(new SelectAdress());

        adresses.add(String.valueOf("Par défaut celle de mon établissement"));
        adresses.add(String.valueOf("Ajouter une adresse"));

        // Drop down layout style - list view with radio button
        adresseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerAdresse.setAdapter(adresseAdapter);
        // Get the layout id
        root = (LinearLayout) findViewById(R.id.layout_hidden);

        // Lastly, set the height of the layout
        Adresse = (EditText)findViewById(R.id.Adresse);
        Cp = (EditText)findViewById(R.id.Cp);
        Ville = (EditText)findViewById(R.id.Ville);
        Pays = (EditText)findViewById(R.id.Pays);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_event, menu);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        statut = spinnerStatut.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void findViewsById() {

        //récupération des checkbox genres musicaux
        rock = (CheckBox) findViewById(R.id.rock);
        pop = (CheckBox) findViewById(R.id.pop);
        metal = (CheckBox) findViewById(R.id.metal);
        rap = (CheckBox) findViewById(R.id.rap);
        funk = (CheckBox) findViewById(R.id.funk);
        classique = (CheckBox) findViewById(R.id.classique);
        blues = (CheckBox) findViewById(R.id.blues);
        electro = (CheckBox) findViewById(R.id.electro);
        folk = (CheckBox) findViewById(R.id.folk);
        jazz = (CheckBox) findViewById(R.id.jazz);

        fromDateEtxt = (EditText) findViewById(R.id.etxt_fromdate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt = (EditText) findViewById(R.id.etxt_todate);
        toDateEtxt.setInputType(InputType.TYPE_NULL);

        HeureDebut = (EditText) findViewById(R.id.HeureDebut);
        HeureDebut.setInputType(InputType.TYPE_NULL);

        HeureFin = (EditText) findViewById(R.id.HeureFin);
        HeureFin.setInputType(InputType.TYPE_NULL);

    }

    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
    private void setHourTimeField() {

        HeureDebut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                final TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        HeureDebut.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        HeureFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                final TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        HeureFin.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

}
    @Override
    public void onClick(View view) {
        if(view == fromDateEtxt) {
            fromDatePickerDialog.show();
        } else if(view == toDateEtxt) {
            toDatePickerDialog.show();
        }
    }
    public void addListenerOnChkWindows() {

        //listener sur les checkbox des genres musicaux
        rock.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    genremusical = "rock";
                    genrelist.add(genremusical);

                } else {
                    genremusical = "rock";
                    int pos = genrelist.indexOf(genremusical);
                    genrelist.remove(pos);
                }
            }
        });
        pop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    genremusical = "pop";
                    genrelist.add(genremusical);
                } else {
                    genremusical = "pop";
                    int pos = genrelist.indexOf(genremusical);
                    genrelist.remove(pos);
                }

            }
        });
        metal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    genremusical = "metal";
                    genrelist.add(genremusical);
                } else {
                    genremusical = "metal";
                    int pos = genrelist.indexOf(genremusical);
                    genrelist.remove(pos);
                }

            }
        });
        classique.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    genremusical = "classique";
                    genrelist.add(genremusical);
                } else {
                    genremusical = "classique";
                    int pos = genrelist.indexOf(genremusical);
                    genrelist.remove(pos);
                }

            }
        });
        folk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    genremusical = "folk";
                    genrelist.add(genremusical);
                } else {
                    genremusical = "folk";
                    int pos = genrelist.indexOf(genremusical);
                    genrelist.remove(pos);
                }
            }
        });
        funk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    genremusical = "funk";
                    genrelist.add(genremusical);
                } else {
                    genremusical = "funk";
                    int pos = genrelist.indexOf(genremusical);
                    genrelist.remove(pos);
                }
            }
        });
        jazz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    genremusical = "jazz";
                    genrelist.add(genremusical);
                } else {
                    genremusical = "jazz";
                    int pos = genrelist.indexOf(genremusical);
                    genrelist.remove(pos);
                }
            }
        });
        rap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    genremusical = "rap";
                    genrelist.add(genremusical);
                } else {
                    genremusical = "rap";
                    int pos = genrelist.indexOf(genremusical);
                    genrelist.remove(pos);
                }
            }
        });
        blues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    genremusical = "blues";
                    genrelist.add(genremusical);
                } else {
                    genremusical = "blues";
                    int pos = genrelist.indexOf(genremusical);
                    genrelist.remove(pos);
                }
            }
        });
        electro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    genremusical = "electro";
                    genrelist.add(genremusical);
                } else {
                    genremusical = "electro";
                    int pos = genrelist.indexOf(genremusical);
                    genrelist.remove(pos);
                }
            }
        });

    }

    private class SelectAdress implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            adresse = spinnerAdresse.getItemAtPosition(position).toString();
          /*  String msg = adresse;
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();*/

            if(adresse == "Ajouter une adresse") {
                layoutHeight=200;
                rootParams = (LinearLayout.LayoutParams) root.getLayoutParams();
                rootParams.height = layoutHeight;
                root.setLayoutParams(rootParams);
                Adresse.setVisibility(View.VISIBLE);
                Cp.setVisibility(View.VISIBLE);
                Ville.setVisibility(View.VISIBLE);
                Pays.setVisibility(View.VISIBLE);

            }else
            {
                layoutHeight=0;
                rootParams = (LinearLayout.LayoutParams) root.getLayoutParams();
                rootParams.height = layoutHeight;
                root.setLayoutParams(rootParams);
                Adresse.setVisibility(View.INVISIBLE);
                Cp.setVisibility(View.INVISIBLE);
                Ville.setVisibility(View.INVISIBLE);
                Pays.setVisibility(View.INVISIBLE);

            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {


        }

    }
}
