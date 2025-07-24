package com.example.cns_rsa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {

    private EditText inputP, inputQ, inputE, inputMessage;
    private TextView outputText;
    private BigInteger p, q, n, phi, e, d, message, cipher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing UI components
        inputP = findViewById(R.id.inputP);
        inputQ = findViewById(R.id.inputQ);
        inputE = findViewById(R.id.inputE);
        inputMessage = findViewById(R.id.inputMessage);
        outputText = findViewById(R.id.outputText);

        Button btnGenerateKeys = findViewById(R.id.btnGenerateKeys);
        Button btnEncrypt = findViewById(R.id.btnEncrypt);
        Button btnDecrypt = findViewById(R.id.btnDecrypt);
        Button btnShare = findViewById(R.id.btnShare);  // Share button


        // Generate Keys Button
        btnGenerateKeys.setOnClickListener(v -> {
            generateKeys();
        });

        // Encrypt Button
        btnEncrypt.setOnClickListener(v -> {
            encryptMessage();
        });

        // Decrypt Button
        btnDecrypt.setOnClickListener(v -> {
            decryptMessage();
        });

        // Share Button
        btnShare.setOnClickListener(v -> {
            shareKeys();  // Share keys function
        });
    }

    private void generateKeys() {
        try {
            // Input prime numbers and public exponent e
            p = new BigInteger(inputP.getText().toString());
            q = new BigInteger(inputQ.getText().toString());
            e = new BigInteger(inputE.getText().toString());

            n = p.multiply(q);
            phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
            d = e.modInverse(phi);

            // Display Keys
            String publicKey = "Public Key: 🔑 (" + e + ", " + n + ")";
            String privateKey = "Private Key: 🛡️ (" + d + ", " + n + ")";
            outputText.setText(publicKey + "\n" + privateKey);

        } catch (Exception e) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    private void encryptMessage() {
        try {
            message = new BigInteger(inputMessage.getText().toString());
            cipher = message.modPow(e, n);
            String encryptedMessage = "Encrypted Text: 🔐 " + cipher;
            outputText.append("\n" + encryptedMessage);
        } catch (Exception e) {
            Toast.makeText(this, "Invalid message", Toast.LENGTH_SHORT).show();
        }
    }

    private void decryptMessage() {
        try {
            BigInteger decryptedMessage = cipher.modPow(d, n);
            String decrypted = "Decrypted Message: 🔓 " + decryptedMessage;
            outputText.append("\n" + decrypted);
        } catch (Exception e) {
            Toast.makeText(this, "No encrypted text found", Toast.LENGTH_SHORT).show();
        }
    }

    // Share keys function
    private void shareKeys() {
        String publicKey = "Public Key: 🔑 (" + e + ", " + n + ")";
        String privateKey = "Private Key: 🛡️ (" + d + ", " + n + ")";

        // Create share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBody = publicKey + "\n" + privateKey;
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "RSA Keys");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
}
