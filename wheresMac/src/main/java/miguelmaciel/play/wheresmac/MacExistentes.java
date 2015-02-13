package miguelmaciel.play.wheresmac;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class MacExistentes extends Activity {
    String localidade;
    ArrayList<String> listOfRegistosResultado = null;
    ArrayList<String> listOfResults = new ArrayList<String>();
    ArrayList<String> numReferencia = new ArrayList<String>();
    ListView list;
    SpecialAdapter adapter;
    String down;
    String wait;
    OnItemClickListener onItemClick_List = new OnItemClickListener() {
        public void onItemClick(@SuppressWarnings("rawtypes") AdapterView arg0,
                                View view, int position, long index) {
            try {
                Intent intent = new Intent(MacExistentes.this, MacDetails.class);
                intent.putExtra(MainActivity.EXTRA_MESSAGE,
                        numReferencia.get(position).toString());
                startActivity(intent);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    };
    private List<macExiste> lstLocais = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mac_existentes);

        Intent intent = getIntent();
        localidade = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(onItemClick_List);
        lstLocais = new ArrayList<macExiste>();
        adapter = new SpecialAdapter(this, lstLocais);

        down = this.getString(R.string.txtDownload);
        wait = this.getString(R.string.txtWait);

        //Start assync task
        try {
            GetMethodEx test = new GetMethodEx();
            listOfRegistosResultado = test.getInternetData();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public class GetMethodEx {
        public ArrayList<String> getInternetData() throws Exception {
            //Show process dialog while get all data from web
            final ProgressDialog dialog = ProgressDialog.show(
                    MacExistentes.this, wait, down, true);
            dialog.show();
            try {
                //Replace spaces with %20 to create the correct link
                localidade = localidade.replace(" ", "%20");
                String website = "https://www.mcdonalds.pt/restaurantes/pesquisa/?termo="
                        + localidade + "&view=list";

                RequestQueue queue = Volley.newRequestQueue(MacExistentes.this);
                StringRequest req = new StringRequest(Request.Method.GET,
                        website, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        BufferedReader reader = null;
                        StringReader sreader = null;

                        sreader = new StringReader(response);
                        reader = new BufferedReader(sreader);

                        String line = "";
                        Integer apoioLer = 0;
                        // Atribui a pagina html a uma stringbuilder
                        try {
                            while ((line = reader.readLine()) != null) {
                                if (apoioLer == 2) {
                                    line = line
                                            .replace(
                                                    "<span class=\"txt-regular\">",
                                                    "");
                                    line = line.replace("</span>", "");
                                    listOfResults.add(line.trim());
                                    apoioLer = 0;
                                } else if (apoioLer == 1) {
                                    line = line.replace("<strong>", "");
                                    line = line
                                            .replace("</strong>", "");
                                    listOfResults.add(line.trim());
                                    apoioLer = 2;
                                } else if (line.trim().equals(
                                        "<span class=\"texto\">")) {
                                    apoioLer = 1;
                                } else if (line
                                        .trim()
                                        .contains(
                                                "<a href=\"/restaurantes/restaurante?rid=")) {
                                    String start = "<a href=\"/restaurantes/restaurante?rid=";
                                    String end = "\">";
                                    line = line.substring(line
                                            .indexOf(start)
                                            + start.length());
                                    line = line.substring(0,
                                            line.indexOf(end));
                                    numReferencia.add(line.trim());
                                }
                            }
                            reader.close();

                            // postexecute: fill the UI with data
                            for (Integer i = 0; i < listOfResults
                                    .size(); i++) {
                                lstLocais.add(new macExiste(Html
                                        .fromHtml(listOfResults.get(i))
                                        .toString(), Html.fromHtml(
                                        listOfResults.get(i + 1))
                                        .toString()));
                                i = i + 1;
                            }
                            list.setAdapter(adapter);

                            //Close process dialog
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }

                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            dialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });

                queue.add(req);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return listOfResults;
        }
    }
}