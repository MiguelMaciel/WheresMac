package miguelmaciel.play.wheresmac;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Information extends Activity {
    String mail;
    String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        mail = this.getString(R.string.txtEmail);
        subject = this.getString(R.string.txtSubject);
    }

    public void contactClick(View view) {
        //When the user click on the contact button open the mail provider options
        try {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setType("plain/text");
            sendIntent.setClassName("com.google.android.gm",
                    "com.google.android.gm.ComposeActivityGmail");
            sendIntent.putExtra(Intent.EXTRA_EMAIL,
                    new String[]{mail});
            sendIntent.putExtra(Intent.EXTRA_SUBJECT,
                    subject);
            startActivity(sendIntent);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(Information.this, R.string.check_your_mail_provider_, Toast.LENGTH_LONG).show();
        }
    }
}
