package com.jassgroup.centinela.clases;

import java.io.Serializable;


/**
 * Created by Bernett on 29/08/2016.
 */
public class AsistenciaVehiculos implements Serializable{

    public String apenom;
    public String color;
    public String fecha;
    public String hora;
    public String numdoc;
    public String placa;
    public String tipo;
    public String tipovehiculo;

    public AsistenciaVehiculos(){}

    public AsistenciaVehiculos(String apenom, String color, String fecha, String hora, String numdoc, String placa, String tipo, String tipovehiculo){
        this.apenom = apenom;
        this.color = color;
        this.fecha = fecha;
        this.hora = hora;
        this.numdoc = numdoc;
        this.placa = placa;
        this.tipo = tipo;
        this.tipovehiculo = tipovehiculo;
    }


}
