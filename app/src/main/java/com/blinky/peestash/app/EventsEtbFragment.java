package com.blinky.peestash.app;

/**
 * Created by Muriel on 18/05/2015.
 */
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.blinky.peestash.app.R;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.blinky.peestash.app.ProfilFragment.getCircularBitmapWithBorder;

public class EventsEtbFragment extends Fragment {

    public EventsEtbFragment(){}

    private String id_user = "";
    private Button BtnAddEvent;
    private TextView Demande_inscription, Nb_events, Titre, Ville, Pays, Genre, Date_debut, Date_fin, Heure_Debut, Heure_fin, Statut;
    int nbreponse;
    private List<String> titre, date_debut, ville, pays, genre_musical, statut_recrutement, imgUrl;
    private ImageView img;
    Bitmap imgurl;
    ProgressDialog progress;
    private LinearLayout listeEvents;
    private int layoutHeight, i=0;
    private LinearLayout.LayoutParams listeEventsParams;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_etb_events, container, false);


        arrayList = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);


        list = (ListView) rootView.findViewById(android.R.id.list);

        BtnAddEvent = (Button) rootView.findViewById(R.id.btnAddEvent);
        Demande_inscription = (TextView) rootView.findViewById(R.id.DemandeInscription);
        Nb_events = (TextView) rootView.findViewById(R.id.nbEvents);

        Bundle var = getActivity().getIntent().getExtras();
        id_user = var.getString("id_user");

         BtnAddEvent.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {

                 Intent intent = new Intent(getActivity(), AddEventActivity.class);
                 intent.putExtra("id_user", id_user);
                 startActivity(intent);

             }
         });

        new Thread(new Runnable() {
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        new ShowAllEventsTask().execute();
                    }
                });
            }
        }).start();



        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    private class ShowAllEventsTask extends AsyncTask<Void, Void, InputStream> {

        String result = null;
        String tag = "read_AllEvents";
        InputStream is = null;
        List<NameValuePair> nameValuePairs;

        protected InputStream doInBackground(Void... params) {
            //setting nameValuePairs
            nameValuePairs = new ArrayList<NameValuePair>(1);
            //adding string variables into the NameValuePairs
            nameValuePairs.add(new BasicNameValuePair("tag", tag));
            nameValuePairs.add(new BasicNameValuePair("id_user", id_user));

            //setting the connection to the database
            try {
                //Setting up the default http client
                HttpClient httpClient = new DefaultHttpClient();

                //setting up the http post method
                HttpPost httpPost = new HttpPost("http://peestash.peestash.fr/index.php");
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                //getting the response
                HttpResponse response = httpClient.execute(httpPost);

                //setting up the entity
                HttpEntity entity = response.getEntity();

                //setting up the content inside the input stream reader
                is = entity.getContent();

            } catch (ClientProtocolException e) {

                Log.e("ClientProtocole", "Log_tag");
                String msg = "Erreur client protocole";

            } catch (IOException e) {
                Log.e("Log_tag", "IOException");
                e.printStackTrace();
                String msg = "Erreur IOException";
            }
            return is;
        }

        protected void onProgressUpdate(Void params) {

        }

        protected void onPreExecute() {
            progress = new ProgressDialog(getActivity());
            progress.setMessage("Chargement de la liste des évènements...");
            progress.show();
        }

        protected void onPostExecute(InputStream is) {

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String json = reader.readLine();
                JSONTokener tokener = new JSONTokener(json);
                JSONArray finalResult = new JSONArray(tokener);

                int i=0;
                // Access by key : value
                nbreponse = finalResult.length();
                String nb = String.valueOf(nbreponse);
                System.out.println("nb reponse "+ nb);

                titre = new ArrayList<String>(nbreponse);
                ville = new ArrayList<String>(nbreponse);
                pays = new ArrayList<String>(nbreponse);
                date_debut = new ArrayList<String>(nbreponse);
                imgUrl = new ArrayList<String>(nbreponse);
                genre_musical = new ArrayList<String>(nbreponse);
                statut_recrutement = new ArrayList<String>(nbreponse);

                for (i = 0; i < finalResult.length(); i++) {

                    JSONObject element = finalResult.getJSONObject(i);

                    titre.add(element.getString("titre"));
                    date_debut.add(element.getString("date_debut"));
                    ville.add(element.getString("ville"));
                    pays.add(element.getString("pays"));
                    imgUrl.add(element.getString("img_url"));
                    genre_musical.add(element.getString("genre_musical"));
                    statut_recrutement.add(element.getString("statut_recrutement"));

                }


                for(i=0;i<finalResult.length();i++) {
                    afficheAllEventsResume(i);
                }
                list.setAdapter(adapter);

                is.close();

            } catch (Exception e) {
                Log.i("tagconvertstr", "" + e.toString());
            }
            if (progress.isShowing()) {
                progress.dismiss();
            }

        }
    }
    protected void afficheAllEventsResume(int i)
    {
        arrayList.add(titre.get(i).toString().toUpperCase()+"\n"+"Date : "+date_debut.get(i).toString()
                +"\n"+ville.get(i).toString().toUpperCase()+", "+pays.get(i).toString().toUpperCase()
                +"\n"+"Genre : "+genre_musical.get(i).toString().replace(String.valueOf("["), "").replace(String.valueOf("]"), "")
                        +"\n"+"Statut du recrutement : "+statut_recrutement.get(i).toString());



      /*  Ville.setText(ville.get(i).toString());
        Pays.setText(pays.get(i).toString());
        Statut.setText(statut_recrutement.get(i).toString());

        Genre.setText(genre_musical.get(i).toString().replace(String.valueOf("["), "").replace(String.valueOf("]"), ""));

        if(imgUrl.get(i).toString().length() != 0) {
            InputStream in = null;
            try {
                in = new java.net.URL(imgUrl.get(i).toString()).openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgurl = BitmapFactory.decodeStream(in);

            img.setImageBitmap(getCircularBitmapWithBorder(imgurl, 6, Color.rgb(255, 255, 255)));

        }else
        {
            img.setImageDrawable(getResources().getDrawable(R.drawable.ic_default_event));

        }*/

    }

}