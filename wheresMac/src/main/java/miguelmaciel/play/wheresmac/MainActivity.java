package miguelmaciel.play.wheresmac;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemSelectedListener {
    public final static String EXTRA_MESSAGE = "com.android.wheresmac.MESSAGE";
    String localidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerLocalidades);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.arrayLocalidades,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos,
                               long id) {
        // An item was selected. You can retrieve the selected item using
        localidade = parent.getItemAtPosition(pos).toString();
        if (isOnline() == true) {
            if (!localidade.equals(this.getString(R.string.txtSelectLocal))) {
                try {
                    Intent intent = new Intent(this, MacExistentes.class);
                    intent.putExtra(EXTRA_MESSAGE, localidade);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this,
                            this.getString(R.string.txtNeedInternet),
                            Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(MainActivity.this,
                    this.getString(R.string.txtNeedInternet),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    //Check if mobile are connected to network
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return (ni != null && ni.isConnected());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals(this.getString(R.string.txtAbout))) {
            Intent intent = new Intent(this, Information.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
