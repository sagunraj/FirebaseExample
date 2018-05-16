package np.com.sagunraj.firebaseexample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
Button signupBtn, loginBtn;
FirebaseAuth mAuth;
EditText email, password;
ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signupBtn = findViewById(R.id.signupBtn);
        loginBtn = findViewById(R.id.loginBtn);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Logging you in!");
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Successfully logged in.", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Login Failed.", Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }
}
