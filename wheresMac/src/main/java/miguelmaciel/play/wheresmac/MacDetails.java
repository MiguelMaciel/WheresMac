package miguelmaciel.play.wheresmac;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class MacDetails extends Activity {
    String numRef;
    int numFiletes = 0;
    TextView texto;
    String codHTML = "";
    String down;
    String wait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mac_details);
        texto = (TextView) findViewById(R.id.txtHTML);
        Intent intent = getIntent();
        numRef = intent.getStringExtra(MainActivity.EXTRA_MESSAGE).trim();

        down = this.getString(R.string.txtDownload);
        wait = this.getString(R.string.txtWait);

        //Start assync task
        try {
            GetMethodEx test = new GetMethodEx();
            test.getInternetData();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public class GetMethodEx {
        public void getInternetData() throws Exception {
            //Show process dialog while get all data from web
            final ProgressDialog dialog = ProgressDialog.show(MacDetails.this,
                    wait, down, true);
            dialog.show();

            try {
                //URL to the site, must be https on volley
                String website = "https://www.mcdonalds.pt/restaurantes/restaurante?rid="
                        + numRef;

                RequestQueue queue = Volley.newRequestQueue(MacDetails.this);
                StringRequest req = new StringRequest(Request.Method.GET,
                        website, new Listener<String>() {
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
                                if (line.contains("<div class=\"filete-single\"></div>")) {
                                    numFiletes = numFiletes + 1;
                                }

                                if (line.contains("<div class=\"textos txt-regular\">")) {
                                    apoioLer = 1;
                                }
                                if (apoioLer == 1) {
                                    if (line.contains("</span>")
                                            && !line.contains("<span></span>")) {
                                        line = line + "<br>";
                                    }
                                    if (line.contains("</li>")) {
                                        line = line + "<br>";
                                    }
                                    codHTML = codHTML + line;
                                }
                                if (line.contains("<div class=\"box ")) {
                                    apoioLer = 0;
                                }
                            }
                            reader.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            dialog.dismiss();
                        }

                        // postexecute
                        try {
                            texto.setText(Html.fromHtml(codHTML));
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });

                //Start task
                queue.add(req);

            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
}