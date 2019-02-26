package com.example.owner.passwordgenerator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SeekBar lengthSeekBar = findViewById(R.id.passwordLength_sb);
        final TextView lengthTextView = findViewById(R.id.passwordLengthValue_tv);


        final Button generate_button = findViewById(R.id.generatePassword_button);
        final TextView password = findViewById(R.id.generated_tv);
        final TextView passphrase = findViewById(R.id.passphrase_et);
        generate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( ! passphrase.getText().toString().equals("") )
                {
                    generatePassword( );
                } else {
                    password.setText("");
                    password.setHint(getString(R.string.enterPassphrase));
                }
            }
        });


        lengthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                lengthTextView.setText( Integer.toString(progressChangedValue + 10));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void generatePassword( ) {
        final TextView lengthTextView = findViewById(R.id.passwordLengthValue_tv);
        final TextView password = findViewById(R.id.generated_tv);
        final TextView passphrase = findViewById(R.id.passphrase_et);
        final CheckBox letters = findViewById(R.id.letters_cb);
        final CheckBox punctuation = findViewById(R.id.punctuation_cb);
        final CheckBox special = findViewById(R.id.special_cb);
        final CheckBox numbers = findViewById(R.id.number_cb);

        String newPassword = new String();
        String characters = new String();

        if ( letters.isChecked() )
            characters += "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if (punctuation.isChecked() )
            characters += ".?,'!\":;";
        if ( special.isChecked() )
            characters += "/~+<>=\\@#$%^&*-";
        if ( numbers.isChecked() )
            characters += "0123456789";

        Log.d("PC", "Testing " + characters);



        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(passphrase.getText().toString().getBytes());
        byte[] digest = md.digest();
        String myHash = android.util.Base64.encodeToString(digest, android.util.Base64.DEFAULT);

        Integer charactersLength = characters.length();
        Integer hashLength = myHash.length();
        Integer passwordLength = Integer.parseInt(lengthTextView.getText().toString());
        for (int i = 0; i < passwordLength; i++ )
        {
            int j = i % hashLength;
            int k = ( i + 1 ) % hashLength;
            int l = ( i + 3 ) % hashLength;
            char newChar = characters.charAt(((myHash.charAt(j) + ( i + 1 ) ) * (myHash.charAt(k) + ( i + 1 )) *
                           ( myHash.charAt(l) + ( i + 1 ) ) * ( i + 1 ) +
                           passwordLength) % charactersLength);

            newPassword += newChar;
        }
        Log.d("PC", "" + newPassword);
        password.setText(newPassword);
    }
}
