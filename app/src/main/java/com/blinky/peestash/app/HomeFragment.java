package com.blinky.peestash.app;

/**
 * Created by nelly on 15/04/2015.
 */
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.*;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public HomeFragment(){}

    Button btnNextProfil;
    String id_user = "";
    private TextView Pseudo, Email, Adresse, CP, Nom, Prenom, Ville, Pays, Mobile,
            Fixe, Siteweb, Genre, Dispo, Facebook, Age;
    int nbreponse;
    List<String> nom, prenom, adresse, ville, pays, cp, email, pseudo, telportable, telfixe, soundcloud, dispo, siteweb, imgUrl, genre_musical, age;
    ImageView img;
    ProgressDialog progress;
    private WebView wv;
    int i=0;
    String html;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        btnNextProfil = (Button) rootView.findViewById(R.id.btnNextProfil);

        Bundle var = getActivity().getIntent().getExtras();
        id_user = var.getString("id_user");

        Pseudo = (TextView) rootView.findViewById(R.id.Pseudo);
        Nom = (TextView) rootView.findViewById(R.id.Nom);
        Prenom = (TextView) rootView.findViewById(R.id.Prenom);
        Age = (TextView) rootView.findViewById(R.id.Age);
        Adresse = (TextView) rootView.findViewById(R.id.Adresse);
        CP = (TextView) rootView.findViewById(R.id.CP);
        Ville = (TextView) rootView.findViewById(R.id.Ville);
        Pays = (TextView) rootView.findViewById(R.id.Pays);
        Genre = (TextView) rootView.findViewById(R.id.Genre);
        Dispo = (TextView) rootView.findViewById(R.id.Dispo);
        Facebook = (TextView) rootView.findViewById(R.id.Facebook);
        Siteweb = (TextView) rootView.findViewById(R.id.Siteweb);
        Fixe = (TextView) rootView.findViewById(R.id.Fixe);
        Mobile = (TextView) rootView.findViewById(R.id.Mobile);
        Email = (TextView) rootView.findViewById(R.id.Email);
        img = (ImageView) rootView.findViewById(R.id.imageView);
        wv = (WebView) rootView.findViewById(R.id.webView);

        btnNextProfil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
             /*   String msg = "nb" + nbreponse;
                System.out.println("nb" + nbreponse);*/

                i++;
                Pseudo.setText(pseudo.get(i).toString());
                Email.setText(email.get(i).toString());
                Nom.setText(nom.get(i).toString());
                Prenom.setText(prenom.get(i).toString());
                Adresse.setText(adresse.get(i).toString());
                CP.setText(cp.get(i).toString());
                Ville.setText(ville.get(i).toString());
                Pays.setText(pays.get(i).toString());
                Genre.setText(genre_musical.get(i).toString());
                Siteweb.setText(siteweb.get(i).toString());
                Fixe.setText(telfixe.get(i).toString());
                Mobile.setText(telportable.get(i).toString());

                if (i == (nbreponse - 1)) {
                    i = 0;
                }

            }
        });

        new Thread(new Runnable() {
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        new ReadProfilTask().execute();
                    }
                });
            }
        }).start();

        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {

                        final int SWIPE_MIN_DISTANCE = 120;
                        final int SWIPE_MAX_OFF_PATH = 250;
                        final int SWIPE_THRESHOLD_VELOCITY = 200;
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                                return false;
                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                                Log.i("right to left", "Right to Left");
                                i++;
                                Pseudo.setText(pseudo.get(i).toString());
                                Email.setText(email.get(i).toString());
                                Nom.setText(nom.get(i).toString());
                                Prenom.setText(nom.get(i).toString());
                                Adresse.setText(adresse.get(i).toString());
                                CP.setText(cp.get(i).toString());
                                Ville.setText(ville.get(i).toString());
                                Pays.setText(pays.get(i).toString());
                                Genre.setText(genre_musical.get(0).toString().replace(String.valueOf("["), "").replace(String.valueOf("]"), ""));
                                Siteweb.setText(siteweb.get(i).toString());
                                Fixe.setText(telfixe.get(i).toString());
                                Mobile.setText(telportable.get(i).toString());
                                Dispo.setText(dispo.get(0).toString().replaceAll(String.valueOf("["), "").replace(String.valueOf("]"), ""));


                                if (i == (nbreponse - 1)) {
                                    i = -1;
                                }

                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.i("left to rigth", "Left to Right");

                                if(i==0||i<0)
                                {
                                    i=nbreponse;
                                }
                                    i--;
                                    Pseudo.setText(pseudo.get(i).toString());
                                    Email.setText(email.get(i).toString());
                                    Nom.setText(nom.get(i).toString());
                                    Prenom.setText(nom.get(i).toString());
                                    Adresse.setText(adresse.get(i).toString());
                                    CP.setText(cp.get(i).toString());
                                    Ville.setText(ville.get(i).toString());
                                    Pays.setText(pays.get(i).toString());
                                    Genre.setText(genre_musical.get(0).toString().replace(String.valueOf("["), "").replace(String.valueOf("]"), ""));
                                    Siteweb.setText(siteweb.get(i).toString());
                                    Fixe.setText(telfixe.get(i).toString());
                                    Mobile.setText(telportable.get(i).toString());
                                    Dispo.setText(dispo.get(0).toString().replaceAll(String.valueOf("["), "").replace(String.valueOf("]"), ""));



                            }
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View rootView, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });

        return rootView;
    }


    private class ReadProfilTask extends AsyncTask<Void, Void, InputStream> {
        int i;
        String result = null;
        String tag = "read_AllProfil";
        String type = "artiste";
        InputStream is = null;
        List<NameValuePair> nameValuePairs;

        protected InputStream doInBackground(Void... params) {
            //setting nameValuePairs
            nameValuePairs = new ArrayList<NameValuePair>(1);
            //adding string variables into the NameValuePairs
            nameValuePairs.add(new BasicNameValuePair("tag", tag));
            nameValuePairs.add(new BasicNameValuePair("type", type));
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
            progress.setMessage("Chargement de vos informations de profil...");
            progress.show();
        }

        protected void onPostExecute(InputStream is) {

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder total = new StringBuilder();
                String json = reader.readLine();
                JSONTokener tokener = new JSONTokener(json);
                JSONArray finalResult = new JSONArray(tokener);
                Bitmap imgurl;
                int i=0;
                // Access by key : value
                nbreponse = finalResult.length();
                String nb = String.valueOf(nbreponse);
                System.out.println("nb reponse "+ nb);

                email = new ArrayList<String>(nbreponse);
                pseudo = new ArrayList<String>(nbreponse);
                nom = new ArrayList<String>(nbreponse);
                prenom = new ArrayList<String>(nbreponse);
                adresse = new ArrayList<String>(nbreponse);
                cp = new ArrayList<String>(nbreponse);
                soundcloud = new ArrayList<String>(nbreponse);
                ville = new ArrayList<String>(nbreponse);
                pays = new ArrayList<String>(nbreponse);
                telportable = new ArrayList<String>(nbreponse);
                telfixe = new ArrayList<String>(nbreponse);
                dispo = new ArrayList<String>(nbreponse);
                siteweb = new ArrayList<String>(nbreponse);
                imgUrl = new ArrayList<String>(nbreponse);
                genre_musical = new ArrayList<String>(nbreponse);
                age = new ArrayList<String>(nbreponse);

                for (i = 0; i < finalResult.length(); i++) {

                    JSONObject element = finalResult.getJSONObject(i);
                    email.add(element.getString("email"));
                    nom.add(element.getString("nom"));
                    pseudo.add(element.getString("pseudo"));
                    soundcloud.add(element.getString("soundcloud"));
                    adresse.add(element.getString("adresse"));
                    cp.add(element.getString("code_postal"));

                    prenom.add(element.getString("prenom"));
                    ville.add(element.getString("ville"));
                    pays.add(element.getString("pays"));
                    telportable.add(element.getString("tel_portable"));
                    telfixe.add(element.getString("tel_fixe"));
                    dispo.add(element.getString("disponibilites"));

                    siteweb.add(element.getString("siteweb"));
                    imgUrl.add(element.getString("image_url"));
                    genre_musical.add(element.getString("genre_musical"));
                    age.add(element.getString("age"));

                }

                    /*  if(imgUrl[i].length() != 0) {
                        InputStream in = new java.net.URL(imgUrl[i]).openStream();
                        imgurl = BitmapFactory.decodeStream(in);
                        img.setImageBitmap(imgurl);
                    }*/


                    Prenom.setText(prenom.get(0).toString());

                    Age.setText(age.get(0).toString());
                    Genre.setText(genre_musical.get(0).toString().replace(String.valueOf("["), "").replace(String.valueOf("]"), ""));
                    Pseudo.setText(pseudo.get(0).toString());
                    Email.setText(email.get(0).toString());
                    Nom.setText(nom.get(0).toString());
                    Adresse.setText(adresse.get(0).toString());
                    CP.setText(cp.get(0).toString());
                    Ville.setText(ville.get(0).toString());
                    Pays.setText(pays.get(0).toString());
                    Dispo.setText(dispo.get(0).toString().replaceAll(String.valueOf("["), "").replace(String.valueOf("]"), ""));
                    Mobile.setText(telportable.get(0).toString());
                    Fixe.setText(telfixe.get(0).toString());

                    Siteweb.setText(siteweb.get(0).toString());


             /*   if (soundcloud[i].length() != 0) {
                    html = "<iframe width=\"100%\" height=\"400\" scrolling=\"yes\" frameborder=\"no\" src=\"https://w.soundcloud.com/player/?url=https%3A//api.soundcloud.com/playlists/" + soundcloud[i] + "&amp;auto_play=false&amp;hide_related=false&amp;show_comments=false&amp;show_user=false&amp;show_reposts=false&amp;show_artwork=false&amp;buying=false\"></iframe>";
                    wv.getSettings().setJavaScriptEnabled(true);
                    wv.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
                } else {
                    html = "Vous n'avez pas renseigne l\'ID de votre playlist Soundcloud";
                    wv.getSettings().setJavaScriptEnabled(true);
                    wv.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");

                }*/

                is.close();

            } catch (Exception e) {
                Log.i("tagconvertstr", "" + e.toString());
            }
            if (progress.isShowing()) {
                progress.dismiss();
            }

        }
    }

}