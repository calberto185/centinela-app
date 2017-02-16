package com.jassgroup.centinela.clases;

import java.io.Serializable;


/**
 * Created by Bernett on 23/08/2016.
 */
public class ListaNegraVehiculos implements Serializable {

    public String apellidos;
    public String nombres;
    public String numdoc;
    public String placa;
    public String tipovehiculo;
    public String color;
    public String fecha;

    public ListaNegraVehiculos(){}

    public ListaNegraVehiculos(String apellidos, String nombres, String numdoc, String placa, String tipo, String color, String fecha){
        this.apellidos = apellidos.toUpperCase();
        this.nombres = nombres.toUpperCase();
        this.numdoc = numdoc;
        this.placa = placa.toUpperCase();
        this.tipovehiculo = tipo.toUpperCase();
        this.color = color.toUpperCase();
        this.fecha = fecha;
    }


}
