package co.paulfran.paulfranco.simpletaskapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Initialize TextView
    private TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // complete initialization of the TextView
        signup = findViewById(R.id.signup_txt);

        // set onclickListener
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // redirect user to the registration activity
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });
    }
}
