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

public class RegistrationActivity extends AppCompatActivity {

    // Initialize Views
    private EditText email;
    private EditText pass;
    private Button btnReg;
    private TextView login_txt;

    // Initialize FirebaseAuth
    private FirebaseAuth mAuth;

    // Initialize a Progress Dialog
    private ProgressDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Set instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Complete initialization of the ProgressDialog
        mDialog = new ProgressDialog(this);

        email = findViewById(R.id.username_reg);
        pass = findViewById(R.id.password_reg);

        btnReg = findViewById(R.id.reg_btn);
        login_txt = findViewById(R.id.login_txt);

        // Click Listener to take user to the log in screen
        login_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        // Click Listener for the registration btn
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the info from the EditText
                String  mEmail = email.getText().toString().trim();
                String  mPass = pass.getText().toString().trim();

                // if the email field is empty
                if (TextUtils.isEmpty(mEmail)) {
                    email.setError("Required Field...");
                    return;
                }

                // if pass field is empty
                if (TextUtils.isEmpty(mPass)) {
                    pass.setError("Required Field ...");
                    return;
                }

                // set and show a message in the dialog
                mDialog.setMessage("Processing ...");
                mDialog.show();

                // Create onCompleteListener
                mAuth.createUserWithEmailAndPassword(mEmail, mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Show Toast
                            Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_LONG).show();
                            // redirect user to home activity
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            // dismiss the dialog
                            mDialog.dismiss();
                        } else {
                            // show Toast
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                            // dismiss the dialog
                            mDialog.dismiss();
                        }
                    }
                });
            }
        });



    }
}
