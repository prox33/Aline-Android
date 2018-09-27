package coolgroup.com.aline.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.rengwuxian.materialedittext.MaterialEditText;

import coolgroup.com.aline.Controller;
import coolgroup.com.aline.R;
import coolgroup.com.aline.model.User;
import dmax.dialog.SpotsDialog;

public class Authenticate extends AppCompatActivity {


    // Declare Button and Intro View
    private Button btnSignIn, btnRegister;
    private RelativeLayout authLayout;

    // Declare a users database
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);

        users = Controller.getInstance().serverCommunicator.getmFirebaseDatabase().getReference("Users");

        // Initialize Views
        btnRegister = findViewById(R.id.btnRegister);
        btnSignIn = findViewById(R.id.btnSignIn);
        authLayout = findViewById(R.id.authLayout);

        // Override button onClick methods to show Register Dialog
        btnRegister.setOnClickListener(v -> showRegisterDialog());

        // Override button onClick methods to show Login Dialog
        btnSignIn.setOnClickListener(v -> showLogInDialog());
    }

    // Create Login Dialog which is of type AlertDialog
    private void showLogInDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        // Set the Title and Message for the dialog
        dialog.setTitle("SIGN IN");
        dialog.setMessage("Please use email to sign in");

        // Instantiates a Login layout XML file into the corresponding Authenticate view object
        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_login, null);

        // Create custom TextView for email and password input
        final MaterialEditText edtEmail = login_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = login_layout.findViewById(R.id.edtPassword);

        dialog.setView(login_layout);

        // Set Sign In Button
        dialog.setPositiveButton("SIGN IN", (dialog1, which) -> {

            dialog1.dismiss();

            // Disable Sign In button after it starts processing
            btnSignIn.setEnabled(false);

            // Validate email and password
            if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                Snackbar.make(authLayout, "Please enter email address", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                Snackbar.make(authLayout, "Please enter password", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            // Android AlertDialog with moving spots progress indicator packed as android library.
            android.app.AlertDialog waitingDialog = new SpotsDialog(Authenticate.this);
            waitingDialog.show();

            // Try to Login with correctly validated email and password
            // auth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString()) TODO: Delete me!
            Controller.getInstance().serverCommunicator.logInUserEmail(edtEmail.getText().toString(), edtPassword.getText().toString())
                    .addOnSuccessListener(authResult -> {

                        // Remove the loading dialog
                        waitingDialog.dismiss();

                        // If email and password is authenticated open the welcome layout
                        startActivity(new Intent(Authenticate.this, Chat.class));

                        // And close the Login layout
                        finish();
                    })
                    .addOnFailureListener(e -> {

                        // Remove the loading dialog
                        waitingDialog.dismiss();

                        // Incorrect email or password
                        Snackbar.make(authLayout, "Failed " + e.getMessage(), Snackbar.LENGTH_LONG)
                                .show();

                        // Make SignIn button active again
                        btnSignIn.setEnabled(true);

                    });
        });

        // Set the Cancel button
        dialog.setNegativeButton("CANCEL", (dialog12, which) -> dialog12.dismiss());

        // Show the Login dialog when ShowLogInDialog method is called
        dialog.show();
    }

    // Create Register Dialog which is of type AlertDialog
    private void showRegisterDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        // Set the Title and Message for the dialog
        dialog.setTitle("REGISTER");
        dialog.setMessage("Please use email to register");

        // Instantiates a Register layout XML file into the corresponding Authenticate view object
        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register, null);

        // Create custom TextView for email, name, phone and password
        final MaterialEditText edtName, edtEmail, edtPhone, edtPassword;
        edtName = register_layout.findViewById(R.id.edtName);
        edtEmail = register_layout.findViewById(R.id.edtEmail);
        edtPhone = register_layout.findViewById(R.id.edtPhone);
        edtPassword = register_layout.findViewById(R.id.edtPassword);

        dialog.setView(register_layout);

        // Set Register button
        dialog.setPositiveButton("REGISTER", (dialog12, which) -> {

            dialog12.dismiss();

            // Validate email, password, name and phone
            if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                Snackbar.make(authLayout, "Please enter email address", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                Snackbar.make(authLayout, "Please enter phone number", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            if (TextUtils.isEmpty(edtName.getText().toString())) {
                Snackbar.make(authLayout, "Please enter name", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                Snackbar.make(authLayout, "Please enter password", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            // If password is shorter than six characters
            if (edtPassword.getText().toString().length() < 6) {
                Snackbar.make(authLayout, "Password too short!", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            // Create a new user with the input details
            Controller.getInstance().serverCommunicator.signUpUser(edtEmail.getText().toString(), edtPassword.getText().toString(), "", "")
                    .addOnSuccessListener(authResult -> {
                        // Save user to database
                        // Create a new user object TODO: Move this logic out of here
                        User user = new User();

                        // Set email, name and password for the user
                        user.setEmail(edtEmail.getText().toString());
                        user.setName(edtName.getText().toString());
                        user.setPhone(edtPhone.getText().toString());

                        // Use UID as the unique key
                        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user)
                                .addOnSuccessListener(aVoid -> {

                                    // If email and password is authenticated open the welcome layout
                                    startActivity(new Intent(Authenticate.this, Chat.class));

                                    //WELCOME TO ALINE
                                    Snackbar.make(authLayout, "Welcome to ALINE!", Snackbar.LENGTH_SHORT)
                                            .show();

                                    // And close the Login layout
                                    finish();

                                })
                                .addOnFailureListener(e -> Snackbar.make(authLayout, "Failed" + e.getMessage(), Snackbar.LENGTH_LONG)
                                        .show());

                    })
                    .addOnFailureListener(e -> Snackbar.make(authLayout, "Failed" + e.getMessage(), Snackbar.LENGTH_LONG)
                            .show());
        });
        // Set the Cancel button
        dialog.setNegativeButton("CANCEL", (dialog1, which) -> dialog1.dismiss());

        dialog.show();

    }
}

