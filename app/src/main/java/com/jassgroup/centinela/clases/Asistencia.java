package com.jassgroup.centinela.clases;

import java.io.Serializable;

/**
 * Created by kenwhiston on 03/08/2016.
 */
public class Asistencia implements Serializable {
    public String area;
    public String autorizado;
    public String fecharegistro;
    public String horaregistro;
    public String motivo;
    public String numdoc;
    public String tipo;

    public Asistencia(){

    }

    public Asistencia(String area,
                   String autorizado,
                   String fecharegistro,
                   String horaregistro,
                   String motivo,
                   String numdoc,
                   String tipo){
        this.area = area.toUpperCase();
        this.autorizado = autorizado.toUpperCase();
        this.fecharegistro = fecharegistro;
        this.horaregistro = horaregistro;
        this.motivo = motivo.toUpperCase();
        this.numdoc = numdoc.toUpperCase();
        this.tipo = tipo.toUpperCase();

    }

}
