package com.example.pet_pc.controledefaltas;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;

/**
 * Created by PET-PC on 01/08/2018.
 */

public class DisciplinaAdapter extends ArrayAdapter<Disciplina> {

    private final Context context;
    private final List<Disciplina> elementos;
    Realm realm;

    public DisciplinaAdapter(Context context, List<Disciplina> elementos) {
        super(context, R.layout.linha, elementos);
        this.context = context;
        this.elementos = elementos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.linha, parent, false);

        TextView nomeDisciplina = (TextView) rowView.findViewById(R.id.textViewDisciplina);
        TextView numFaltas = (TextView) rowView.findViewById(R.id.textViewFaltas);
        LinearLayout ll = rowView.findViewById(R.id.linearLayoutLinhaDisciplina);

        realm = Realm.getDefaultInstance();

        Disciplina disciplina = elementos.get(position);

        RealmList<Aula> listaAula = disciplina.getAulas();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,23);
        c.set(Calendar.MINUTE,59);

        listaAula.sort("data", Sort.ASCENDING);

        Aula aula = listaAula.first();

        boolean atrasado = false;

        Iterator<Aula> it = listaAula.iterator();


        while(aula.getData().before(c.getTime())){
            Log.d("teste4",aula.getData().toString());
            if(aula.getStatus().equals("-")){
                atrasado=true;
                break;
            }

            aula = it.next();
/*            cal.setTime(aula.getData());
            cal.set(Calendar.HOUR_OF_DAY,1);
            aula.setData(cal.getTime());*/
        }

        if(atrasado){
            ImageView imageView = rowView.findViewById(R.id.imageView);
            imageView.setVisibility(View.VISIBLE);
        }


        int faltasMaxima = disciplina.getCargaHoraria() / 4;
        float proporcao = (float) disciplina.getNumFaltas() / (float) faltasMaxima;

        nomeDisciplina.setText(disciplina.getNome());
        numFaltas.setText("FALTAS: "+disciplina.getNumFaltas()+"/"+faltasMaxima);


        if(proporcao>=0 && proporcao<0.2){
            ll.setBackgroundColor(Color.parseColor("#5500CC00"));
        }else if(proporcao>=0.2 && proporcao<0.4){
            ll.setBackgroundColor(Color.parseColor("#5599FF33"));
        }else if(proporcao>=0.4 && proporcao<0.6){
            ll.setBackgroundColor(Color.parseColor("#55FFFF00"));
        }else if(proporcao>=0.6 && proporcao<0.8){
            ll.setBackgroundColor(Color.parseColor("#55FF9900"));
        }else if(proporcao>=0.8){
            ll.setBackgroundColor(Color.parseColor("#55FF3300"));
        }

        return rowView;
    }
}
