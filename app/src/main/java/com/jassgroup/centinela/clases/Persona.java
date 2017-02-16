package com.jassgroup.centinela.clases;

import java.io.Serializable;

/**
 * Created by kenwhiston on 29/07/2016.
 */
public class Persona implements Serializable {
    public String apematerno;
    public String apepaterno;
    public String correo;
    public String fecharegistro;
    public String horaregistro;
    public String imagen;
    public String nombres;
    public String numdoc;
    public String sexo;
    public String telefono;
    public String tipodoc;
    public String estado;

    public Persona(){

    }

    public Persona(String apematerno,
            String apepaterno,
            String correo,
            String fecharegistro,
            String horaregistro,
            String imagen,
            String nombres,
            String numdoc,
            String sexo,
            String telefono,
            String tipodoc,
            String estado){
        this.apematerno = apematerno.toUpperCase();
        this.apepaterno = apepaterno.toUpperCase();
        this.correo = correo;
        this.fecharegistro = fecharegistro;
        this.horaregistro = horaregistro;
        this.imagen = imagen;
        this.nombres = nombres.toUpperCase();
        this.numdoc = numdoc;
        this.sexo = sexo.toUpperCase();
        this.telefono = telefono;
        this.tipodoc = tipodoc;
        this.estado = estado;
    }

}
