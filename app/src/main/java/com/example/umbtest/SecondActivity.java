package com.example.umbtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }


    // Método para ejecutar el botón de la segunda actividad para buscar la url escrita por el usuario

    public void onClick(View view){

        EditText pagWeb = findViewById(R.id.txt_input_url);

        String urlLink = pagWeb.getText().toString();

        // mediante la clase Uri vamos a llamar la url que escribimos
        Uri _urlPag = Uri.parse(urlLink);

        // creamos el objeto de tipo Intent para cambiar de activity y ejecutar el navegador
        Intent i = new Intent(Intent.ACTION_VIEW, _urlPag);
        startActivity(i);
    }
}