package com.example.rssrodalies;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class MainActivity extends ListActivity {

/*    Relación posición en array - url/nombre

    [0] = R1 http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r1_ca_ES.xml
    [1] = R2 Nord http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r2_nord_ca_ES.xml
    [2] = R2 Sud http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r2_sud_ca_ES.xml
    [3] = R3 http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r3_ca_ES.xml
    [4] = R4 http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r4_ca_ES.xml
    [5] = R7 http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r7_ca_ES.xml
    [6] = R8 http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r8_ca_ES.xml
    [7] = R11 http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r11_ca_ES.xml
    [8] = R12 http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r12_ca_ES.xml
    [9] = R13 http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r13_ca_ES.xml
    [10] = R14 http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r14_ca_ES.xml
    [11] = R15 http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r15_ca_ES.xml
    [12] = R16 http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r16_ca_ES.xml */

    /* Array con las URL de las lineas */
    private String[] lineasURL = new String[]{
        "http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r1_ca_ES.xml",
        "http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r2_nord_ca_ES.xml",
        "http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r2_sud_ca_ES.xml",
        "http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r3_ca_ES.xml",
        "http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r4_ca_ES.xml",
        "http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r7_ca_ES.xml",
        "http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r8_ca_ES.xml",
        "http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r11_ca_ES.xml",
        "http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r12_ca_ES.xml",
        "http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r13_ca_ES.xml",
        "http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r14_ca_ES.xml",
        "http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r15_ca_ES.xml",
        "http://www.gencat.cat/rodalies/incidencies_rodalies_rss_r16_ca_ES.xml"
    };

    /* Array con los nombres de las lineas */
    private String[] nombreLineas = new String[]{
            "R1", "R2 Nord", "R2 Sud", "R3", "R4", "R7", "R8", "R11", "R12", "R13", "R14", "R15", "R16"
    };

    /* Lista que contiene los objetos "Linea" creados */
    private List<Linea> listaDeLineas = new ArrayList<Linea>();

    private LineasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getListView().setAdapter(adapter);

        obtenerTweets();

       crearObjetosLinea();
    }

    private void obtenerTweets() {

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("sucgKFbEBFcK7CQ2owEHXw")
                .setOAuthConsumerSecret("G2hiP52HjmlTa2VkcDfMRFeXok1EUdHYqvFpYSFc")
                .setOAuthAccessToken("58214096-4zwXE7v5PaSCgiDtkqFbl1vhJYpv2ZBUqMmCACKGc")
                .setOAuthAccessTokenSecret("EZCucXVtARGyvdipOEO4LhEVDTLD3y3upR4TUSh0zMdxR");
        TwitterFactory tf = new TwitterFactory(cb.build());
        final Twitter twitter = tf.getInstance();

        final Query query = new Query("rodalia2"); //Establecer nombre de usuario (sin @) o hashtag
        query.count(15); //Numero maximo de tuits maximo

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    QueryResult result = twitter.search(query);
                    for (Status status : result.getTweets()) {
                        Log.i("TWEETS", "@" + status.getUser().getScreenName() + ": " + status.getText());
                    }
                } catch (TwitterException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /* Esta funcion crea un objecto "Linea", le asigna el nombre y la url.
    * El estado inicialmente es un string vacio "", a continuación se hace una llamada
    * al AsyncTask que descargará el estado y se le asignará al objeto */
    private void crearObjetosLinea() {
        listaDeLineas = new ArrayList<Linea>();
        for(int i=0; i < nombreLineas.length; i++)
        {
            Linea L = new Linea(nombreLineas[i], "", lineasURL[i]);
            new LineasAsyncTask(L).execute();
        }
        adapter = new LineasAdapter(this, android.R.layout.simple_list_item_1, listaDeLineas);
        this.getListView().setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_actualizar: /* Si se selecciona la opcion de menú "Actualizar", se actualiza la lista */
                crearObjetosLinea();
                obtenerTweets();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class LineasAsyncTask extends AsyncTask<String, Void, String> {
        private Linea linea;

        public LineasAsyncTask(Linea l) {
            this.linea = l;
        }

        /* Se llama a la clase RssHandler pasando como parametro el string URL.
        * Está clase devolverá el estado de la linea como un string.
        * Si la descarga se ha realizado con exito (!null), se asigna el estado
        * y se añade el objeto "Linea" a la lista de lineas */
        @Override
        protected String doInBackground(String... params) {
            RssHandler rh = new RssHandler();
            return rh.getLatestArticles(this.linea.getUrl());
        }
        @Override
        protected void onPostExecute(String response)
        {
            if(response != null)
            {
                this.linea.setEstado(response);
            }
            listaDeLineas.add(this.linea);
            adapter.notifyDataSetChanged();
        }
    }

}
