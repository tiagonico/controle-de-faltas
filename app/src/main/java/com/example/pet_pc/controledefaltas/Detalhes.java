package com.example.pet_pc.controledefaltas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.realm.Realm;

public class Detalhes extends AppCompatActivity {

    private Realm realm;
    ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);


        realm = Realm.getDefaultInstance();

        Intent intent = getIntent();
        final String parametro = (String) intent.getSerializableExtra("nome");

        ListView lista = (ListView) findViewById(R.id.lvAulas);
        Disciplina disciplina = realm.where(Disciplina.class).equalTo("nome",parametro).findFirst();



        AulaAdapter adapter = new AulaAdapter(this, disciplina.getAulas());
        lista.setAdapter(adapter);

        TextView nome = (TextView) findViewById(R.id.txtNome);
        nome.setText(parametro);

        Typeface myCustomFont = Typeface.createFromAsset(getAssets(),"fonts/KGMakesYouStronger.ttf");
        nome.setTypeface(myCustomFont);

        //Add back button
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btnVoltar = findViewById(R.id.btnVoltar);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Detalhes.this, MainActivity.class);
                startActivity(intent);
            }
        });
        Button btn = findViewById(R.id.btnDelete);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                AlertDialog.Builder alerta = new AlertDialog.Builder(Detalhes.this);
                alerta.setTitle("Alerta");
                alerta
                        .setIcon(R.mipmap.ic_warning)
                        .setMessage("Deseja mesmo apagar essa disciplina?")
                        .setCancelable(false)
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Detalhes.this, MainActivity.class);
                                realm.beginTransaction();
                                Disciplina d = realm.where(Disciplina.class).equalTo("nome",parametro).findFirst();
                                d.deleteFromRealm();
                                realm.commitTransaction();
                                startActivity(intent);
                            }
                        });
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();



            }
        });
    }



}

