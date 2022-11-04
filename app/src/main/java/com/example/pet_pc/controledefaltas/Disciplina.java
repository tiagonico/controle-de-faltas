package com.example.pet_pc.controledefaltas;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by PET-PC on 01/08/2018.
 */

public class Disciplina extends RealmObject{


    private int id;
    private String nome;
    private RealmList<Integer> numAulas;
    private int numFaltas;
    private int cargaHoraria;
    private Date dataInicio;
    private RealmList<Aula> aulas;

    public Disciplina(){

    }

    public Disciplina(String nome, int numFaltas) {
        this.nome = nome;
        this.numFaltas = numFaltas;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public RealmList<Aula> getAulas() {
        return aulas;
    }

    public void setAulas(RealmList<Aula> aulas) {
        this.aulas = aulas;
    }

    public RealmList<Integer> getNumAulas() {
        return numAulas;
    }

    public void setNumAulas(RealmList<Integer> numAulas) {
        this.numAulas = numAulas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNumFaltas() {
        return numFaltas;
    }

    public void setNumFaltas(int numFaltas) {
        this.numFaltas = numFaltas;
    }


/*    public boolean calculaAtraso(){

        RealmList realmList =

    }*/

}
