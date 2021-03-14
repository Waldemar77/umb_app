package com.example.umbtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Configuracion del splashScreen
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(R.style.Theme_UMBTest);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*if(!Python.isStarted())
            Python.start(new AndroidPlatform( context: this));

        Python py = Python.getInstance();
        Python py_obj = py.getModule( s: "py_script.py");*/
    }
    //Método para mostrar y ocultar menú de items
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow, menu);
        return true;
    }

    // Método para funcionalidades de los items
    public boolean onOptionsItemSelected(MenuItem item){
        //Almacenamos el id de cada item
        int id = item.getItemId();

        // si se detecta acción el cada item, se hara una accion
        // para el item1, se ejecutará el navegador del android para ir a un sitio web
        if (id == R.id.item1){
            // creamos una variable local de texto para almacenar el sitio web de la u
            String urlUmb = "https://umbvirtual.edu.co/";

            // mediante la clase Uri vamos a llamar la url de la u
            Uri _link = Uri.parse(urlUmb);

            // creamos el objeto de tipo Intent para cambiar de activity y ejecutar el navegador
            Intent i = new Intent(Intent.ACTION_VIEW,_link);
            startActivity(i);
        } else if(id == R.id.item2){
            // si el item2 (''buscar otras paginas...'') escucha un evento, nos vamos a otra vista
            // para buscar la página
            Intent i = new Intent(this, SecondActivity.class);
            startActivity(i);
        } else if (id == R.id.item3){
            Intent i = new Intent(this, ThirdActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


}