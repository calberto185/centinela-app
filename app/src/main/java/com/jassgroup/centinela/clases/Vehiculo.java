package com.jassgroup.centinela.clases;

import java.io.Serializable;

/**
 * Created by Bernett on 23/08/2016.
 */
public class Vehiculo implements Serializable {

            public String placa;
            public String tipovehiculo;
            public String color;
            public String numdoc;

    public Vehiculo(){

    }

    public Vehiculo(String placa, String tipovehiculo, String color, String numdoc){
        this.placa = placa.toUpperCase();
        this.tipovehiculo = tipovehiculo.toUpperCase();
        this.color = color.toUpperCase();
        this.numdoc = numdoc;
    }

}
