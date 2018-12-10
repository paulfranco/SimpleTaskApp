package co.paulfran.paulfranco.simpletaskapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    // Initialize TextView
    private TextView signup;

    // Initialize EditText
    private EditText email;
    private EditText pass;

    // Initilize Button
    private Button btnLogin;

    // Firebase Auth
    private FirebaseAuth mAuth;

    // Initialize ProgressDialog
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get instance of FireBaseAuth
        mAuth = FirebaseAuth.getInstance();

        // check is user is already registered or logged in
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }

        // Complete initialization of ProgressDialog
        mDialog = new ProgressDialog(this);

        // complete initialization of the TextView
        signup = findViewById(R.id.signup_txt);

        // complete initialization of EditText
        email = findViewById(R.id.username_txt);
        pass = findViewById(R.id.password_txt);

        // complete initialization of Button
        btnLogin = findViewById(R.id.login_btn);

        // set onClickListener on the button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // complete firebase login functionality

                String mEmail = email.getText().toString().trim();
                String mPass = pass.getText().toString().trim();

                // Check if email field is empty
                if (TextUtils.isEmpty(mEmail)) {
                    email.setError("Required Field");
                    return;
                }
                // Check if pass field is empty
                if (TextUtils.isEmpty(mPass)) {
                    pass.setError("Required Field");
                    return;
                }

                // Set message on the dialog
                mDialog.setMessage("Processing ...");
                mDialog.show();

                mAuth.signInWithEmailAndPassword(mEmail, mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            mDialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failure to sign in", Toast.LENGTH_LONG).show();
                            mDialog.dismiss();
                        }
                    }
                });
            }
        });

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
