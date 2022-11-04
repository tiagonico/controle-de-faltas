package com.example.pet_pc.controledefaltas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import io.realm.Realm;

public class AulaAdapter extends ArrayAdapter<Aula> {

    private final Context context;
    private final List<Aula> elementos;
    private Realm realm = Realm.getDefaultInstance();

    public AulaAdapter(Context context, List<Aula> elementos) {
        super(context, R.layout.linha_aula, elementos);
        this.context = context;
        this.elementos = elementos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.linha_aula, parent, false);

        TextView data = (TextView) rowView.findViewById(R.id.textViewData);

        Button btnPresenca = rowView.findViewById(R.id.btnPresenca);
        Button btnFalta = rowView.findViewById(R.id.btnFalta);
        Button btnReset = rowView.findViewById(R.id.btnReset);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,23);
        c.set(Calendar.MINUTE,59);


        if(!c.getTime().after(elementos.get(position).getData())){
            btnFalta.setVisibility(View.INVISIBLE);
            btnPresenca.setVisibility(View.INVISIBLE);
        }


        final LinearLayout ll = rowView.findViewById(R.id.linearLayoutLinhaAula);

        btnPresenca.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if(elementos.get(position).getStatus().equals("F")){
                    AlertDialog.Builder alerta = new AlertDialog.Builder(context);
                    alerta.setTitle("Alerta");
                    alerta
                            .setIcon(R.mipmap.ic_warning)
                            .setMessage("Deseja mesmo modificar?")
                            .setCancelable(false)
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ll.setBackgroundColor(Color.parseColor("#9900ff00"));
                                    realm.beginTransaction();
                                    Aula aula = elementos.get(position);
                                    Disciplina disciplina = elementos.get(position).getDisciplina();
                                    aula.setStatus("P");
                                    disciplina.setNumFaltas(disciplina.getNumFaltas() - aula.getHorasAula());
                                    realm.commitTransaction();
                                }
                            });
                        AlertDialog alertDialog = alerta.create();
                        alertDialog.show();


                }
                else{
                    ll.setBackgroundColor(Color.parseColor("#9900ff00"));
                    realm.beginTransaction();
                    Aula aula = elementos.get(position);
                    aula.setStatus("P");
                    realm.commitTransaction();
                }

            }
        });

        btnFalta.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(elementos.get(position).getStatus().equals("P")){
                    AlertDialog.Builder alerta = new AlertDialog.Builder(context);
                    alerta.setTitle("Alerta");
                    alerta
                            .setIcon(R.mipmap.ic_warning)
                            .setMessage("Deseja mesmo modificar?")
                            .setCancelable(false)
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ll.setBackgroundColor(Color.parseColor("#99ff0000"));
                                    realm.beginTransaction();Aula aula = elementos.get(position);
                                    aula.getDisciplina().setNumFaltas( aula.getDisciplina().getNumFaltas() + aula.getHorasAula());
                                    aula.setStatus("F");
                                    realm.commitTransaction();
                                }
                            });
                    AlertDialog alertDialog = alerta.create();
                    alertDialog.show();

                }
                else if(elementos.get(position).getStatus().equals("-")){
                    ll.setBackgroundColor(Color.parseColor("#99ff0000"));
                    realm.beginTransaction();
                    Aula aula = elementos.get(position);
                    aula.getDisciplina().setNumFaltas( aula.getDisciplina().getNumFaltas() + aula.getHorasAula());
                    aula.setStatus("F");
                    realm.commitTransaction();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if(!elementos.get(position).getStatus().equals("-")){
                    AlertDialog.Builder alerta = new AlertDialog.Builder(context);
                    alerta.setTitle("Alerta");
                    alerta
                            .setIcon(R.mipmap.ic_warning)
                            .setMessage("Deseja mesmo modificar?")
                            .setCancelable(false)
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ll.setBackgroundColor(Color.parseColor("#99ffff00"));
                                    realm.beginTransaction();
                                    Aula aula = elementos.get(position);
                                    Disciplina disciplina = elementos.get(position).getDisciplina();
                                    if(elementos.get(position).getStatus().equals("F")){
                                        disciplina.setNumFaltas(disciplina.getNumFaltas() - aula.getHorasAula());
                                    }
                                    aula.setStatus("-");
                                    realm.commitTransaction();
                                }
                            });
                    AlertDialog alertDialog = alerta.create();
                    alertDialog.show();


                }

            }
        });

        if(elementos.get(position).getStatus().toString().equals("P")){
            ll.setBackgroundColor(Color.parseColor("#9900ff00"));
        }
        else if(elementos.get(position).getStatus().toString().equals("F")){
            ll.setBackgroundColor(Color.parseColor("#99ff0000"));
        }
        else{
            ll.setBackgroundColor(Color.parseColor("#99ffff00"));
        }

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(elementos.get(position).getData());
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        data.setText("Dia: "+String.format("%02d",day)+"/"+String.format("%02d",month+1)+"/"+year+" ("+elementos.get(position).getDiaDaSemana()+")");


        return rowView;
    }
}