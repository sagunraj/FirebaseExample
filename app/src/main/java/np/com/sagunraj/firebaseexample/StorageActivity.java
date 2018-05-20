package np.com.sagunraj.firebaseexample;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class StorageActivity extends AppCompatActivity {
    private static final int INT_CONST = 5; // random integer chosen as 5
    Uri dataUri;
    Button btnChoose, btnUpload;
    TextView filename;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    int permissionCheck;
    List<String> dataList = new ArrayList<>();
    ListView listView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE); // runtime permission since it is a Dangerous Permission

        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 12); //12 is just a randomly selected number like INT_CONST
        }
        databaseReference = FirebaseDatabase.getInstance().getReference().child("url");
        storageReference = FirebaseStorage.getInstance().getReference().child("files");

        btnChoose = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        filename = findViewById(R.id.filename);
        listView = findViewById(R.id.listView);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //implicit intent to open file explorer
                intent.setType("*/*");  // shows all types of files
                startActivityForResult(intent, INT_CONST); // the second parameter is requestCode that helps to check if the correct file has been received or not later; onActivityResult() function is called after this statement
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dataUri != null){
                    progressDialog = new ProgressDialog(StorageActivity.this);
                    progressDialog.setMessage("Uploading your file...");
                    progressDialog.setCancelable(false);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.show();
                    storageReference.putFile(dataUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String downloadurl = taskSnapshot.getDownloadUrl().toString();
                            databaseReference.push().setValue(downloadurl).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    Toast.makeText(StorageActivity.this, "File upload successful.", Toast.LENGTH_SHORT).show();
                                onResume();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(StorageActivity.this, "File upload failed.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(StorageActivity.this, "Upload failed.", Toast.LENGTH_LONG).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int percentage = (int)(taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount())*100; //calculation of file upload percentage
                            progressDialog.setProgress(percentage);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == INT_CONST && resultCode == RESULT_OK){
            dataUri = data.getData();
            filename.setText(dataUri.toString());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { // called after requestPermissions()
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(), "You're ready to go!", Toast.LENGTH_SHORT).show();
        }
        else{
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 12);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()
                     ) {
                    dataList.add(ds.getValue().toString());
                }
                listView.setAdapter(new StorageAdapter(StorageActivity.this, dataList));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
