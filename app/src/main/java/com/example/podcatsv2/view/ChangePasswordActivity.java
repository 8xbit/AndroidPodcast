package com.example.podcatsv2.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.podcatsv2.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText oldPasswordField;
    private EditText newPasswordField;
    private EditText confirmPasswordField;
    private Button changePasswordButton;
    private Button backhijode;
    private FirebaseAuth auth;
    private ImageView imgbuttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_password);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();

        // Link UI
        oldPasswordField = findViewById(R.id.old_password);
        newPasswordField = findViewById(R.id.new_password);
        confirmPasswordField = findViewById(R.id.confirm_password);
        changePasswordButton = findViewById(R.id.save_button);
        imgbuttonBack =(ImageView) findViewById(R.id.back_button);

        // Configure the change password button
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleChangePassword();
            }
        });
        imgbuttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void handleChangePassword() {
        String oldPassword = oldPasswordField.getText().toString().trim();
        String newPassword = newPasswordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();

        // Field validation
        if (oldPassword.isEmpty()) {
            showToast(getString(R.string.enter_old_password));
            return;
        }
        if (newPassword.isEmpty()) {
            showToast(getString(R.string.enter_new_password));
            return;
        }
        if (confirmPassword.isEmpty()) {
            showToast(getString(R.string.confirm_new_password));
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            showToast(getString(R.string.password_mismatch));
            return;
        }


        // Get the current user
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            reauthenticateAndChangePassword(user, oldPassword, newPassword);
        } else {
            showToast(getString(R.string.error_user_not_logged_in));
        }
    }

    private void reauthenticateAndChangePassword(FirebaseUser user, String oldPassword, String newPassword) {
        // Re-authenticate the user with the old password
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Update password if re-authentication is successful
                    user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                showToast(getString(R.string.password_changed_successfully));
                                finish(); // Close activity after changing password
                            } else {
                                showToast(getString(R.string.error_password_change_failed));
                            }
                        }
                    });
                } else {
                    showToast(getString(R.string.error_invalid_old_password));
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ChangePasswordActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }

}
