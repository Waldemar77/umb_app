package com.example.umbtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.io.Console;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ThirdActivity extends AppCompatActivity {
    // conexion con firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // variable que almacena el id de cada registro
    // ademas se crear variables que almacenaran resultados de consultas para obtener el id
    private int id_agenda;
    private String cadena1;     //id_agenda2
    private String[] lista1;    //id_agenda3
    private String[] lista2;    //id_agenda4

    // Vamos a almacenar dentro de tres variables los datos a mostrar en cada text view
    // con la información de un solo documento a la vez
    String asuntoAgendaCons = "";
    String fechaAgendaCons = "";
    String detalleAgendaCons = "";
    int id_agenda_consul = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        // Instancia del botón para agregar el método onClick
        Button btn_guardarAgenda = findViewById(R.id.btn_agregar_agenda);

        //System.out.println("####### este es el valor de id_agenda iniciando: " + id_agenda);

        // vamos a averiguar el ultimo ID ingresado consultando el registro más reciente
        // primero pasamos el tiempo que queremos comparar en la busqueda dentro de Firebase
        long hora = 0000000001;
        Timestamp timestamp = new Timestamp(hora);
        Date date = new Date(timestamp.getTime());

        // con esta consulta podemos acceder al último doc de la colección, y así obtener el id_agenda del ultimo registro
        db.collection("agenda")
                .whereGreaterThanOrEqualTo("timestamp", date).orderBy("timestamp", Query.Direction.DESCENDING).limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                cadena1 = String.valueOf(document.getData());
                                lista1 = cadena1.split(",");
                                Log.d("agenda", document.getId() + " => " + document.getData());
                                //System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX" + id_agenda2);
                                lista2 = lista1[2].split("=");
                                id_agenda = Integer.parseInt(lista2[1]) + 1;
                                //System.out.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMM" + id_agenda);
                            }
                        } else {
                            Log.w("agenda", "Error getting documents.", task.getException());
                        }
                    }
                });

        // programamos el método onClink para poder registrar los datos
        btn_guardarAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Crear variables para cargar registros
                EditText asunto = findViewById(R.id.editTextTitleAct);
                EditText fecha = findViewById(R.id.editTextDate);
                EditText actividades = findViewById(R.id.etxt_detalleEvent);

                //System.out.println("######################### este es el valor de id_agenda: " + id_agenda);

                //Craeción del objeto de tipo HashMap que contendrá la información de las variables
                //para realizar el registro en FireBase
                Map<String, Object> registroAgenda = new HashMap<>();
                registroAgenda.put("id_agenda", id_agenda);
                registroAgenda.put("asunto", asunto.getText().toString());
                registroAgenda.put("fecha", fecha.getText().toString());
                registroAgenda.put("actividades", actividades.getText().toString());
                registroAgenda.put("timestamp", FieldValue.serverTimestamp());

                // Agregar Registro con el método add para generar un id único, automático desde
                // Firebase
                db.collection("agenda").add(registroAgenda).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //creamos un mensaje tipo toast para informar que el registro fue enviado
                        Toast msmEnviado = Toast.makeText(getApplicationContext(), "Registro generado con éxito.", Toast.LENGTH_SHORT);
                        msmEnviado.show();
                        Log.d("agenda", "DocumentSnapshot successfully written!");

                        // intentamos aumentar el valor del id automáticamente
                        //DocumentReference docAgenda = db.collection("agenda").document();
                        //docAgenda.update("id_agenda", FieldValue.increment(1));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //creamos un mensaje tipo toast para informar que el registro fue enviado
                        Toast msmEnviado = Toast.makeText(getApplicationContext(), "Registro fallido, por favor revisar.", Toast.LENGTH_SHORT);
                        msmEnviado.show();
                        Log.w("agenda", "Error adding document", e);
                    }
                });

                // Volvemos a validar el ultimo registro en la base de datos para actualizar la variable id_agenda
                db.collection("agenda")
                        .whereGreaterThanOrEqualTo("timestamp", date).orderBy("timestamp", Query.Direction.DESCENDING).limit(1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        cadena1 = String.valueOf(document.getData());
                                        lista1 = cadena1.split(",");
                                        Log.d("agenda", document.getId() + " => " + document.getData());
                                        //System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX" + id_agenda2);
                                        lista2 = lista1[2].split("=");
                                        id_agenda = Integer.parseInt(lista2[1]) + 1;
                                        //System.out.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMM" + id_agenda);
                                    }
                                } else {
                                    Log.w("agenda", "Error getting documents.", task.getException());
                                }
                            }
                        });

                // borramos los datos ingresados de los edit text
                asunto.setText("");
                fecha.setText("");
                actividades.setText("");
            }
        });

        // Instancia para el botón consultar, el cual traerá el ultimo registro guardado
        Button btn_consultar = findViewById(R.id.btn_consultarAgenda);
        btn_consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Consultamos el último registro ingresado
                db.collection("agenda")
                        .whereGreaterThanOrEqualTo("timestamp", date).orderBy("timestamp", Query.Direction.DESCENDING).limit(1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // almacenamos el asunto de la agenda
                                        cadena1 = String.valueOf(document.getData());
                                        lista1 = cadena1.split(",");
                                        Log.d("agenda", document.getId() + " => " + document.getData());
                                        lista2 = lista1[1].split("=");
                                        asuntoAgendaCons = lista2[1];
                                        TextView txv_asunto = findViewById(R.id.txv_titleAgendaCons);
                                        txv_asunto.setText(asuntoAgendaCons);

                                        // almacenamos la fecha del evento
                                        cadena1 = String.valueOf(document.getData());
                                        lista1 = cadena1.split(",");
                                        Log.d("agenda", document.getId() + " => " + document.getData());
                                        lista2 = lista1[0].split("=");
                                        fechaAgendaCons = lista2[1];
                                        TextView txv_fecha = findViewById(R.id.txv_fechaAgendaCons);
                                        txv_fecha.setText(fechaAgendaCons);

                                        // almacenamos el detalle del asunto de la agenda
                                        cadena1 = String.valueOf(document.getData());
                                        lista1 = cadena1.split(",");
                                        Log.d("agenda", document.getId() + " => " + document.getData());
                                        lista2 = lista1[3].split("=");
                                        detalleAgendaCons = lista2[1];
                                        TextView txv_detalle = findViewById(R.id.txv_detalleAgendaCons);
                                        txv_detalle.setText(detalleAgendaCons);

                                        // almacenamos el id del asunto de la agenda
                                        cadena1 = String.valueOf(document.getData());
                                        lista1 = cadena1.split(",");
                                        Log.d("agenda", document.getId() + " => " + document.getData());
                                        lista2 = lista1[2].split("=");
                                        id_agenda_consul = Integer.parseInt(lista2[1]);
                                        TextView txv_id = findViewById(R.id.txv_Id_agenda);
                                        txv_id.setText(String.valueOf(id_agenda_consul));
                                    }
                                } else {
                                    Log.w("agenda", "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        });


        // Instancia para el botón anterior, el cual traerá la
        // información del id anterior a la agenda mostrada en pantalla
        Button btn_anterior = findViewById(R.id.btn_atras);
        btn_anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Consultaremos primero el id vigente menos 1, es el mismo del boton cosultar
                db.collection("agenda")
                        .whereEqualTo("id_agenda", id_agenda_consul-1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // almacenamos el asunto de la agenda
                                        // y lo mostramos en el text view correspondiente
                                        cadena1 = String.valueOf(document.getData());
                                        lista1 = cadena1.split(",");
                                        Log.d("agenda", document.getId() + " => " + document.getData());
                                        lista2 = lista1[1].split("=");
                                        asuntoAgendaCons = lista2[1];
                                        TextView txv_asunto1 = findViewById(R.id.txv_titleAgendaCons);
                                        txv_asunto1.setText(asuntoAgendaCons);

                                        // almacenamos la fecha del evento
                                        // y lo mostramos en el text view correspondiente
                                        cadena1 = String.valueOf(document.getData());
                                        lista1 = cadena1.split(",");
                                        Log.d("agenda", document.getId() + " => " + document.getData());
                                        lista2 = lista1[0].split("=");
                                        fechaAgendaCons = lista2[1];
                                        TextView txv_fecha1 = findViewById(R.id.txv_fechaAgendaCons);
                                        txv_fecha1.setText(fechaAgendaCons);

                                        // almacenamos el detalle del asunto de la agenda
                                        // y lo mostramos en el text view correspondiente
                                        cadena1 = String.valueOf(document.getData());
                                        lista1 = cadena1.split(",");
                                        Log.d("agenda", document.getId() + " => " + document.getData());
                                        lista2 = lista1[3].split("=");
                                        detalleAgendaCons = lista2[1];
                                        TextView txv_detalle1 = findViewById(R.id.txv_detalleAgendaCons);
                                        txv_detalle1.setText(detalleAgendaCons);

                                        // almacenamos el id del asunto de la agenda
                                        // y lo mostramos en el text view correspondiente
                                        cadena1 = String.valueOf(document.getData());
                                        lista1 = cadena1.split(",");
                                        Log.d("agenda", document.getId() + " => " + document.getData());
                                        lista2 = lista1[2].split("=");
                                        id_agenda_consul = Integer.parseInt(lista2[1]);
                                        TextView txv_id1 = findViewById(R.id.txv_Id_agenda);
                                        txv_id1.setText(String.valueOf(id_agenda_consul));
                                    }
                                }else {
                                    Log.w("agenda", "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        });

        // Instancia para el botón siguiente, el cual traerá los datos
        // de la agenda con el id + 1 de la base de datos
        Button btn_siguiente = findViewById(R.id.btn_siguiente);
        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Consultaremos primero el id vigente más 1, es el mismo del boton cosultar
                db.collection("agenda")
                        .whereEqualTo("id_agenda", id_agenda_consul+1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // almacenamos el asunto de la agenda
                                        // y lo mostramos en el text view correspondiente
                                        cadena1 = String.valueOf(document.getData());
                                        lista1 = cadena1.split(",");
                                        Log.d("agenda", document.getId() + " => " + document.getData());
                                        lista2 = lista1[1].split("=");
                                        asuntoAgendaCons = lista2[1];
                                        TextView txv_asunto2 = findViewById(R.id.txv_titleAgendaCons);
                                        txv_asunto2.setText(asuntoAgendaCons);

                                        // almacenamos la fecha del evento
                                        // y lo mostramos en el text view correspondiente
                                        cadena1 = String.valueOf(document.getData());
                                        lista1 = cadena1.split(",");
                                        Log.d("agenda", document.getId() + " => " + document.getData());
                                        lista2 = lista1[0].split("=");
                                        fechaAgendaCons = lista2[1];
                                        TextView txv_fecha2 = findViewById(R.id.txv_fechaAgendaCons);
                                        txv_fecha2.setText(fechaAgendaCons);

                                        // almacenamos el detalle del asunto de la agenda
                                        // y lo mostramos en el text view correspondiente
                                        cadena1 = String.valueOf(document.getData());
                                        lista1 = cadena1.split(",");
                                        Log.d("agenda", document.getId() + " => " + document.getData());
                                        lista2 = lista1[3].split("=");
                                        detalleAgendaCons = lista2[1];
                                        TextView txv_detalle2 = findViewById(R.id.txv_detalleAgendaCons);
                                        txv_detalle2.setText(detalleAgendaCons);

                                        // almacenamos el id del asunto de la agenda
                                        // y lo mostramos en el text view correspondiente
                                        cadena1 = String.valueOf(document.getData());
                                        lista1 = cadena1.split(",");
                                        Log.d("agenda", document.getId() + " => " + document.getData());
                                        lista2 = lista1[2].split("=");
                                        id_agenda_consul = Integer.parseInt(lista2[1]);
                                        TextView txv_id2 = findViewById(R.id.txv_Id_agenda);
                                        txv_id2.setText(String.valueOf(id_agenda_consul));
                                    }
                                }else {
                                    Log.w("agenda", "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        });


    }


}