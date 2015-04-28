package com.blinky.peestash.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
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


public class EditEtablissementProfilActivity extends Activity {

    String id_user = "", type="";
    private EditText editNom, editAdresse, editCP, editVille, editPays, editMobile,
            editFixe, editSiteweb, editMdp, editFacebook, editConfirmMdp, editEmail, editConfirmEmail;
    TextView affichageEmail;
    private Button btnSave;
    private String nom ="", email ="", confirmEmail="", ville ="", adresse ="", cp ="", pays ="",
            telportable ="", telfixe ="", siteweb ="", imgUrl ="", password ="", confirmMdp="", facebook="";
    String msg="";
    ProgressDialog progress;
    ImageView img;
    Verify test = new Verify();
/*test*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_etablissement_profil);
        int i;

        Bundle var = this.getIntent().getExtras();
        id_user = var.getString("id_user");
        editEmail = (EditText) findViewById(R.id.editEmail);
        affichageEmail = (TextView) findViewById(R.id.affichageEmail);
        editConfirmEmail = (EditText) findViewById(R.id.editConfirmEmail);
        editAdresse = (EditText) findViewById(R.id.editAdresse);
        editCP = (EditText) findViewById(R.id.editCP);
        editNom = (EditText) findViewById(R.id.editNom);
        editVille = (EditText) findViewById(R.id.editVille);
        editPays = (EditText) findViewById(R.id.editPays);
        editMobile = (EditText) findViewById(R.id.editMobile);
        editFixe = (EditText) findViewById(R.id.editFixe);
        editSiteweb = (EditText) findViewById(R.id.editSiteweb);
        img = (ImageView) findViewById(R.id.img);
        editFacebook = (EditText) findViewById(R.id.editFB);
        editMdp = (EditText) findViewById(R.id.editMdp);
        editConfirmMdp = (EditText) findViewById(R.id.editConfirmMdp);

        btnSave = (Button) findViewById(R.id.btnSave);

        int id=Integer.parseInt(id_user);

        new ReadProfilTask().execute(id);
        // On met un Listener sur le bouton Artist
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //update datas in database
                InputStream is = null;
                String tag = "update_EtablissementProfil";
                nom = "" + editNom.getText().toString();
                email = "" + editEmail.getText().toString();
                confirmEmail = "" + editConfirmEmail.getText().toString();
                confirmMdp = "" + editConfirmMdp.getText().toString();
                adresse = "" + editAdresse.getText().toString();
                cp = "" + editCP.getText().toString();
                ville = "" + editVille.getText().toString();
                pays = "" + editPays.getText().toString();
                telfixe = "" + editFixe.getText().toString();
                telportable = "" + editMobile.getText().toString();
                siteweb = "" + editSiteweb.getText().toString();
                facebook = "" + editFacebook.getText().toString();
                password = "" + editMdp.getText().toString();

                //setting nameValuePairs
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                //adding string variables into the NameValuePairs
                nameValuePairs.add(new BasicNameValuePair("tag", tag));
                nameValuePairs.add(new BasicNameValuePair("id", id_user));
                nameValuePairs.add(new BasicNameValuePair("nom", nom));
                nameValuePairs.add(new BasicNameValuePair("adresse", adresse));
                nameValuePairs.add(new BasicNameValuePair("code_postal", cp));
                nameValuePairs.add(new BasicNameValuePair("ville", ville));
                nameValuePairs.add(new BasicNameValuePair("pays", pays));
                nameValuePairs.add(new BasicNameValuePair("tel_fixe", telfixe));
                nameValuePairs.add(new BasicNameValuePair("tel_portable", telportable));
                nameValuePairs.add(new BasicNameValuePair("siteweb", siteweb));
                nameValuePairs.add(new BasicNameValuePair("facebook", facebook));
                nameValuePairs.add(new BasicNameValuePair("password", password));

                String emailvalid="ok", passwordvalid = "ok", msg= "";

                if(email!="") {
                    if(test.checkEmailWriting(email)) {

                        if (test.checkEmail(email, confirmEmail)) {
                            emailvalid="ok";
                            nameValuePairs.add(new BasicNameValuePair("email", email));
                        }
                        else {
                            emailvalid="no";
                            msg = "Veuillez écrire correctement l'email et la confirmation d'e-mail";
                        }
                    } else
                    {
                        emailvalid ="no";
                        msg = "Veuillez écrire un email correct.";
                    }

                }else {
                    email = "" + affichageEmail.getText().toString();
                    nameValuePairs.add(new BasicNameValuePair("email", email));
                }
                if(password!="") {

                    if (test.checkMdpWriting(password)) {
                        if (test.checkMdp(password, confirmMdp)) {
                            passwordvalid = "ok";
                            nameValuePairs.add(new BasicNameValuePair("password", password));
                        } else {
                            passwordvalid = "no";
                            msg = msg + "Veuillez écrire votre password et votre confirmation de password correctement\n";
                        }

                    } else {
                        passwordvalid = "no";
                        msg = msg + "Votre mot de passe doit contenir au minimum 3 caractères.\n";
                    }
                }else {
                    password = "" + editMdp.getText().toString();
                    nameValuePairs.add(new BasicNameValuePair("password", password));
                }

                if(emailvalid == "ok" && passwordvalid == "ok") {
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

                        //displaying a toast message if the data is entered in the database
                        msg = "Données modifiées en BDD établissement";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    } catch (ClientProtocolException e) {
                        Log.e("ClientProtocole", "Log_tag");
                        msg = "Erreur client protocole";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Log.e("Log_tag", "IOException");
                        e.printStackTrace();
                        msg = "Erreur IOException";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    msg = "";
                }
            }
        });
        Button btn = (Button) findViewById(R.id.btnImage);

        View.OnClickListener listnr = new View.OnClickListener() {
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        type="etablissement";
                        Intent i = new Intent(EditEtablissementProfilActivity.this, UploadActivity.class);
                        i.putExtra("id_user", id_user);
                        i.putExtra("type", type);
                        startActivity(i);

                    }
                }).start();
            }

        };

        btn.setOnClickListener(listnr);

    }
    private class ReadProfilTask extends AsyncTask<Integer, Void, InputStream>
    {
        @Override
        protected InputStream doInBackground(Integer... params) {

            //tag récupération des informations de profil établissement
            String tag = "read_EtablissementProfil";
            InputStream is = null;

            //setting nameValuePairs
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            //adding string variables into the NameValuePairs
            nameValuePairs.add(new BasicNameValuePair("tag", tag));
            nameValuePairs.add(new BasicNameValuePair("id_user", id_user));
            //Toast.makeText(getApplicationContext(), id_user, Toast.LENGTH_LONG).show();
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
                msg = "Erreur client protocole";

            } catch (IOException e) {
                Log.e("Log_tag", "IOException");
                e.printStackTrace();
                msg = "Erreur IOException";
            }
            return is;
        }
        protected void onProgressUpdate(Void params) {
        }

        protected void onPreExecute() {
            progress = new ProgressDialog(EditEtablissementProfilActivity.this);
            progress.setMessage("Chargement de vos informations de profil...");
            progress.show();
        }


        protected void onPostExecute(InputStream is) {
            int i;
            String result = null;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder total = new StringBuilder();
                String json = reader.readLine();
                JSONTokener tokener = new JSONTokener(json);
                JSONArray finalResult = new JSONArray(tokener);
                Bitmap imgurl;

                // Access by key : value
                for (i = 0; i < finalResult.length(); i++) {
                    JSONObject element = finalResult.getJSONObject(0);

                    email = element.getString("email");
                    adresse = element.getString("adresse");
                    cp = element.getString("code_postal");
                    nom = element.getString("nom");
                    ville = element.getString("ville");
                    pays = element.getString("pays");
                    telportable = element.getString("tel_portable");
                    telfixe = element.getString("tel_fixe");
                    siteweb = element.getString("siteweb");
                    imgUrl = element.getString("image_url");
                    facebook = element.getString("facebook");

                    editNom.setText(nom);
                    affichageEmail.setText(email);
                    editAdresse.setText(adresse);
                    editCP.setText(cp);
                    editVille.setText(ville);
                    editPays.setText(pays);
                    editMobile.setText(telportable);
                    editFixe.setText(telfixe);
                    editSiteweb.setText(siteweb);

                    editFacebook.setText(facebook);
                    editMdp.setText(password);

                    if(imgUrl.length()!=0)
                    {
                        InputStream in = new java.net.URL(imgUrl).openStream();
                        imgurl = BitmapFactory.decodeStream(in);
                        img.setImageBitmap(imgurl);
                    }
                }
                is.close();

                result = total.toString();

                if (result.equals(null) || result.equals("[]")) {
                    msg = "Erreur de lecture";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                Log.i("tagconvertstr", "" + e.toString());
            }
            if (progress.isShowing()) {
                progress.dismiss();
            }
        }

    }
}