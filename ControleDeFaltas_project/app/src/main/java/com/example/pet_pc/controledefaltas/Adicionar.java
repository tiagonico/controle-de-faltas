package com.example.pet_pc.controledefaltas;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmList;

public class Adicionar extends AppCompatActivity {

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Date data = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar);


        mDisplayDate = findViewById(R.id.textViewData);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Calendar cal = GregorianCalendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                EditText editText = findViewById(R.id.editText);
                if(editText.getText().toString().equals("")){

                    Log.d("piru","aaaaaaaaa" );
                }else{
                    Log.d("piru","bbbbbbbbb" );
                }

                DatePickerDialog dialog = new DatePickerDialog(Adicionar.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal = GregorianCalendar.getInstance();
                cal.set(year,month,dayOfMonth);

                String date = String.format("%02d", dayOfMonth) + "/" + String.format("%02d", month + 1) + "/" + year;

                data = cal.getTime();

                mDisplayDate.setText("Data de início das aulas: " + date);
            }
        };

/*        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/


        final Realm realm = Realm.getDefaultInstance();
        final EditText editText = findViewById(R.id.editText);
        final EditText editTextSegunda = findViewById(R.id.editTextSegunda);
        final EditText editTextTerca = findViewById(R.id.editTextTerca);
        final EditText editTextQuarta = findViewById(R.id.editTextQuarta);
        final EditText editTextQuinta = findViewById(R.id.editTextQuinta);
        final EditText editTextSexta = findViewById(R.id.editTextSexta);
        final Button button = (Button) findViewById(R.id.button);



        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Adicionar.this, MainActivity.class);

                Disciplina disciplina = new Disciplina("a", 0);

                int proxId = 1;
                if (realm.where(Disciplina.class).max("id") != null)
                    proxId = realm.where(Disciplina.class).max("id").intValue() + 1;

                disciplina.setId(proxId);

                if(editText.getText().toString().equals("")){
                    disciplina.setNome("Sem nome");
                }else {
                    disciplina.setNome(editText.getText().toString());
                }
                RealmList<Integer> lista = new RealmList<>();

                lista.add(MyParseInt(editTextSegunda.getText().toString(), 0));
                lista.add(MyParseInt(editTextTerca.getText().toString(), 0));
                lista.add(MyParseInt(editTextQuarta.getText().toString(), 0));
                lista.add(MyParseInt(editTextQuinta.getText().toString(), 0));
                lista.add(MyParseInt(editTextSexta.getText().toString(), 0));

/*                Calendar cal = GregorianCalendar.getInstance();
                cal.setTime(data);
                cal.set(Calendar.HOUR_OF_DAY,1);
                data = cal.getTime();
                Log.d("teste2",""+cal.get(Calendar.HOUR_OF_DAY));*/

                disciplina.setNumAulas(lista);
                disciplina.setNumFaltas(0);
                disciplina.setDataInicio(data);
                disciplina.setAulas(adicionarAulas(disciplina));
                disciplina.setCargaHoraria((lista.get(0)+lista.get(1)+lista.get(2)+lista.get(3)+lista.get(4))*15);

                if(lista.get(0) == 0 && lista.get(1) == 0 && lista.get(2) == 0 && lista.get(3) == 0 && lista.get(4) == 0){

                    AlertDialog.Builder alerta = new AlertDialog.Builder(Adicionar.this);
                    alerta.setTitle("Alerta");
                    alerta
                            .setIcon(R.mipmap.ic_warning)
                            .setMessage("Disciplina sem horas de aula.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    AlertDialog alertDialog = alerta.create();
                    alertDialog.show();
                }else{

                    realm.beginTransaction();
                    realm.copyToRealm(disciplina);
                    realm.commitTransaction();

                    realm.close();

                    startActivity(intent);

                }

            }

        });
    }


    public static int MyParseInt(String texto, int valorPadrao) {
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return valorPadrao;
        }
    }

    public RealmList<Aula> adicionarAulas(Disciplina d) {

        RealmList<Aula> listaAula = new RealmList<>();

        Date dataInicio = d.getDataInicio();

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(dataInicio);

        Aula aula;

        int numSemanas = 19;

        String diaDasemana = "ALOPRADOOOO";

        for (int i = 0; i < numSemanas*7; i++) {


            switch (cal.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.MONDAY:
                case Calendar.TUESDAY:
                case Calendar.WEDNESDAY:
                case Calendar.THURSDAY:
                case Calendar.FRIDAY:
                    if (d.getNumAulas().get(cal.get(Calendar.DAY_OF_WEEK) - 2).intValue() > 0) {
                        aula = new Aula(cal.getTime(), "-");
                        aula.setHorasAula(d.getNumAulas().get(cal.get(Calendar.DAY_OF_WEEK) - 2).intValue());
                        aula.setDisciplina(d);

                        switch (cal.get(Calendar.DAY_OF_WEEK)){
                            case Calendar.MONDAY:
                                diaDasemana = "Seg";
                                break;
                            case Calendar.TUESDAY:
                                diaDasemana = "Ter";
                                break;
                            case Calendar.WEDNESDAY:
                                diaDasemana = "Qua";
                                break;
                            case Calendar.THURSDAY:
                                diaDasemana = "Qui";
                                break;
                            case Calendar.FRIDAY:
                                diaDasemana = "Sex";
                                break;
                        }
                        aula.setDiaDaSemana(diaDasemana);
                        listaAula.add(aula);
                    }
                    break;
                case Calendar.SATURDAY:
                case Calendar.SUNDAY:
                    break;
            }
            cal.add(Calendar.DAY_OF_MONTH, +1);

        }

        return listaAula;
    }

}
