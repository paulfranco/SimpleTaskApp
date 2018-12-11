package co.paulfran.paulfranco.simpletaskapp;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    // Initialize the toolbar
    private Toolbar toolbar;

    private FloatingActionButton fabBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Bind the toolbar
        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Task App");

        // Bind FAB
        fabBtn = findViewById(R.id.fab_btn);

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
