package np.com.sagunraj.firebaseexample;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    EditText email, password;
    Button signupBtn;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signupBtn = findViewById(R.id.signupBtn);
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Registering you... Please wait...");
        progressDialog.setCancelable(false);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "User registered successfully.", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "User registration failed.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
