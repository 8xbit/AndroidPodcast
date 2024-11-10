package com.example.podcatsapp.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.podcatsapp.R;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText oldPasswordField;
    private EditText newPasswordField;
    private EditText confirmPasswordField;
    private Button changePasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_password);

        // Enlazamos los elementos de la interfaz
        oldPasswordField = findViewById(R.id.old_password);
        newPasswordField = findViewById(R.id.new_password);
        confirmPasswordField = findViewById(R.id.confirm_password);
        changePasswordButton = findViewById(R.id.save_button);

        // Configuración del botón para cambiar la contraseña
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleChangePassword();
            }
        });
    }

    private void handleChangePassword() {
        String oldPassword = oldPasswordField.getText().toString().trim();
        String newPassword = newPasswordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();

        // Validación de los campos
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

        // Aquí puedes añadir la lógica para cambiar la contraseña (enviar al servidor, etc.)
        // Si la operación es exitosa:
        showToast(getString(R.string.password_changed_successfully));
        finish(); // Cierra la actividad después de cambiar la contraseña
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
