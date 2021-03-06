package co.paulfran.paulfranco.simpletaskapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

import co.paulfran.paulfranco.simpletaskapp.model.Data;

public class HomeActivity extends AppCompatActivity {

    // Initialize the toolbar
    private Toolbar toolbar;

    private FloatingActionButton fabBtn;

    // Firebase
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    // RecyclerView
    private RecyclerView recyclerView;

    // Update fields
    private EditText titleUpdate;
    private EditText noteUpdate;
    private Button btnDelete;
    private Button btnUpdate;

    // Global Variables
    private String title;
    private String note;
    private String post_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Bind the toolbar
        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Task App");

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();
        String uId = mUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("TaskNote").child(uId);
        mDatabase.keepSynced(true);

        // RecyclerView
        recyclerView = findViewById(R.id.recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        // Bind FAB
        fabBtn = findViewById(R.id.fab_btn);

        // Click Listener on Fab that inflates the custom alert dialog
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mDialog = new AlertDialog.Builder(HomeActivity.this);
                LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
                View myView = inflater.inflate(R.layout.custominputfield, null);
                mDialog.setView(myView);
                final AlertDialog dialog = mDialog.create();

                final EditText title = myView.findViewById(R.id.edt_title);
                final EditText note = myView.findViewById(R.id.edt_note);
                Button btnSave = myView.findViewById(R.id.btn_save);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String mTitle = title.getText().toString().trim();
                        String mNote = note.getText().toString().trim();

                        // Check if fields are empty
                        if (TextUtils.isEmpty(mTitle)) {
                            title.setError("Required Field...");
                            return;
                        }

                        if (TextUtils.isEmpty(mNote)) {
                            note.setError("Required Fied...");
                            return;
                        }

                        // Generate id and date for the database
                        String id = mDatabase.push().getKey();
                        String date = DateFormat.getDateInstance().format(new Date());

                        // Must match the sequence in the model
                        Data data = new Data(mTitle, mNote, date, id);
                        mDatabase.child(id).setValue(data);

                        Toast.makeText(getApplicationContext(), "Note Added", Toast.LENGTH_LONG).show();

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(
                Data.class,
                R.layout.item_data,
                MyViewHolder.class,
                mDatabase) {

            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, final Data model, final int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setNote(model.getNote());
                viewHolder.setDate(model.getDate());

                viewHolder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        post_key = getRef(position).getKey();
                        title = model.getTitle();
                        note = model.getNote();

                        updateData();

                    }
                });

            }
        };

        recyclerView.setAdapter(adapter);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        View myView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myView = itemView;
        }

        public void setTitle(String title) {
            TextView mTitle = myView.findViewById(R.id.title);
            mTitle.setText(title);
        }

        public void setNote(String note) {
            TextView mNote = myView.findViewById(R.id.note);
            mNote.setText(note);
        }

        public void setDate(String date) {
            TextView mDate = myView.findViewById(R.id.date);
            mDate.setText(date);
        }
    }
    public void updateData(){

        // Set up the layout
        AlertDialog.Builder myDialog = new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);

        View myView = inflater.inflate(R.layout.update_input_field, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();

        titleUpdate = myView.findViewById(R.id.edt_title_update);
        noteUpdate = myView.findViewById(R.id.edt_note_update);

        titleUpdate.setText(title);
        titleUpdate.setSelection(title.length());

        noteUpdate.setText(note);
        noteUpdate.setSelection(note.length());

        btnUpdate = myView.findViewById(R.id.btn_update);
        btnDelete = myView.findViewById(R.id.btn_delete);


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title = titleUpdate.getText().toString().trim();
                note = noteUpdate.getText().toString().trim();

                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(title, note, mDate, post_key);

                mDatabase.child(post_key).setValue(data);

                dialog.dismiss();

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mDatabase.child(post_key).removeValue();

                dialog.dismiss();
            }
        });
        dialog.show();


    }

    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
