package com.jassgroup.centinela.clases;

import java.io.Serializable;

public class ListaNegra implements Serializable {

    public String numdoc;
    public String apellidos;
    public String nombres;
    public String telefono;
    public String imagen;

    public ListaNegra(){

    }

    public ListaNegra(String numdoc, String apellidos, String nombres, String telefono, String imagen){
        this.numdoc = numdoc;
        this.apellidos = apellidos;
        this.nombres = nombres;
        this.telefono = telefono;
        this.imagen = imagen;
    }


}
