package np.com.sagunraj.firebaseexample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    EditText rollno, name;
    Button saveBtn, storageBtn;
    ListView lv;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    List<DataModule> dataList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        storageBtn = findViewById(R.id.btnStorage);
        rollno = findViewById(R.id.rollno);
        name = findViewById(R.id.name);
        saveBtn = findViewById(R.id.saveBtn);
        lv = findViewById(R.id.lv);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user");
        progressDialog = new ProgressDialog(DashboardActivity.this);
        progressDialog.setMessage("Saving your data...");

        storageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inte = new Intent(DashboardActivity.this, StorageActivity.class);
                startActivity(inte);
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                DataModule dm = new DataModule();
                dm.setRollno(Integer.parseInt(rollno.getText().toString()));
                dm.setName(name.getText().toString());
                databaseReference.push().setValue(dm).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(DashboardActivity.this, "Your data have been saved successfully.", Toast.LENGTH_LONG).show();
                        onResume();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(DashboardActivity.this, "Your data couldn't be saved.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataList.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()
                     ) {
                    DataModule dm = new DataModule();
                    dm.setRollno(Integer.parseInt(ds.child("rollno").getValue().toString()));
                    dm.setName(ds.child("name").getValue().toString());
                    dataList.add(dm);
                }

                lv.setAdapter(new MyAdapter(DashboardActivity.this, dataList));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
