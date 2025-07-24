package com.example.otp_rsa;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {

    private EditText phoneInput, otpInput;
    private Button sendOtpBtn, verifyOtpBtn, viewProcessBtn;
    private TextView resultText, encryptedTextView, decryptedTextView;

    private String generatedOtp;
    private BigInteger encryptedOtp;

    private RSAUtils rsa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rsa = new RSAUtils();

        phoneInput = findViewById(R.id.editPhone);
        otpInput = findViewById(R.id.editOtp);
        sendOtpBtn = findViewById(R.id.btnSendOtp);
        verifyOtpBtn = findViewById(R.id.btnVerifyOtp);
        viewProcessBtn = findViewById(R.id.btnViewProcess);
        resultText = findViewById(R.id.resultView);

        encryptedTextView = findViewById(R.id.encryptedTextView);
        decryptedTextView = findViewById(R.id.decryptedTextView);

        sendOtpBtn.setOnClickListener(v -> {
            generatedOtp = String.valueOf(new Random().nextInt(9000) + 1000); // 4-digit OTP
            encryptedOtp = rsa.encrypt(generatedOtp);
            Toast.makeText(this, "OTP Sent: " + generatedOtp, Toast.LENGTH_SHORT).show(); // For testing only
            // Clear previous outputs
            resultText.setText("");
            encryptedTextView.setText("Encrypted OTP: ");
            decryptedTextView.setText("Decrypted OTP: ");
        });

        verifyOtpBtn.setOnClickListener(v -> {
            String userInputOtp = otpInput.getText().toString().trim();
            String decryptedOtp = rsa.decrypt(encryptedOtp);
            if (userInputOtp.equals(decryptedOtp)) {
                resultText.setText("✅ OTP Verified Successfully!");
            } else {
                resultText.setText("❌ Invalid OTP");
            }
        });

        viewProcessBtn.setOnClickListener(v -> {
            if (generatedOtp == null) {
                Toast.makeText(this, "Please send OTP first!", Toast.LENGTH_SHORT).show();
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("🔑 Public Key (e, n): (")
                    .append(rsa.getE()).append(", ").append(rsa.getN()).append(")\n\n");
            sb.append("🛡️ Private Key (d, n): (")
                    .append(rsa.getD()).append(", ").append(rsa.getN()).append(")");
            resultText.setText(sb.toString());

            // Show encrypted and decrypted OTP separately
            encryptedTextView.setText("Encrypted OTP:\n" + encryptedOtp.toString());
            decryptedTextView.setText("Decrypted OTP:\n" + rsa.decrypt(encryptedOtp));
        });
    }
}
