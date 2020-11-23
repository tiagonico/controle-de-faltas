package com.example.pet_pc.controledefaltas;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    ImageButton floatButton;
    private List<Disciplina> listaDisciplinas = new ArrayList<>();
    ListView lista;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // DESCOMENTE A LINHA ABAIXO PARA DEIXAR FULLSCREEN

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Calendar cal = GregorianCalendar.getInstance();
        int dia = cal.get(Calendar.DAY_OF_WEEK);
        if(dia > Calendar.SUNDAY && dia < Calendar.SATURDAY){
            cal.set(Calendar.HOUR_OF_DAY, 20);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.SECOND,0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
        }


      //  hideNavigationBar();

        realm = Realm.getDefaultInstance();


        floatButton = findViewById(R.id.imageButton);

        floatButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Adicionar.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();

        lista = (ListView) findViewById(R.id.lvDisciplina);
        listaDisciplinas = realm.where(Disciplina.class).findAll();

        TextView textView = findViewById(R.id.textView);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(),"fonts/KGMakesYouStronger.ttf");
        textView.setTypeface(myCustomFont);

        DisciplinaAdapter adapter = new DisciplinaAdapter(this, listaDisciplinas);
        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, Detalhes.class);

                intent.putExtra("nome",listaDisciplinas.get(position).getNome());

                startActivity(intent);
            }
        });

    }

    private List<Disciplina> adicionarDisciplinas() {

        List<Disciplina> disciplinas = new ArrayList<Disciplina>();

        Disciplina d = new Disciplina("Sinais e Sistemas",10);
        disciplinas.add(d);
        d = new Disciplina("Instalações Elétricas",20);
        disciplinas.add(d);

        return disciplinas;
    }


}
