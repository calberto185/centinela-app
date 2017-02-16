package com.jassgroup.centinela.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jassgroup.centinela.R;
import com.jassgroup.centinela.clases.Persona;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kenwhiston on 09/08/2016.
 */
public class ItemPersonaAdapter extends UltimateViewAdapter {

    private List<Persona> sampleData = null;
    private Context contexto = null;
    //public  static String idusuario;


    public ItemPersonaAdapter(List<Persona> sampleData, Context contexto){
        this.setSampleData(sampleData);
        this.contexto = contexto;
        //this.idusuario=idusuario;
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parentViewGroup) {
        View rowView;

        rowView = LayoutInflater.from(parentViewGroup.getContext())
                .inflate(R.layout.ly_item_persona, parentViewGroup, false);
        ViewHolder dvh = new ViewHolder(rowView);
        return dvh;
    }

    @Override
    public int getAdapterItemCount() {
        return getSampleData().size();
    }

    @Override
    public long generateHeaderId(int i) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder newHeaderHolder(View view) {
        return new UltimateRecyclerviewViewHolder<>(view);
    }

    @Override
    public RecyclerView.ViewHolder newFooterHolder(View view) {
        // return new itemCommonBinder(view, false);
        return new UltimateRecyclerviewViewHolder<>(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            /*SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat formatoanno = new SimpleDateFormat("yyyy");
            SimpleDateFormat formatofecha = new SimpleDateFormat("MMM dd");
            SimpleDateFormat formatohora = new SimpleDateFormat("hh:mm a");*/
            final ViewHolder vh = (ViewHolder) viewHolder;
            final Persona rowData = getSampleData().get(position);
            /*vh.numInscritos.setText(String.valueOf(rowData.getNumInscritos())+" Inscritos");
            vh.titulo.setText(rowData.getNombre());
            ///convertimos fecha
            Date fecAuxini = null, fecAuxfin = null;
            try {
                fecAuxini = formato.parse(rowData.getFechaInicio()+" "+rowData.getHoraInicio());
                fecAuxfin = formato.parse(rowData.getFechaFin()+" "+rowData.getHoraFin());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String anno = formatoanno.format(fecAuxini);
            String mesdiaini = formatofecha.format(fecAuxini);
            String mesdiafin = formatofecha.format(fecAuxfin);
            String horaini = formatohora.format(fecAuxini);
            String horafin = formatohora.format(fecAuxfin);

            vh.fecha.setText(mesdiaini.toUpperCase()+" - "+mesdiafin.toUpperCase()+" "+anno+", de "+horaini+" a "+horafin);

            //fin de convertir fecha

            vh.lugar.setText(rowData.getDireccion());
            vh.creadorPor.setText(rowData.getGestor().toUpperCase());

            Picasso.with(contexto)
                    .load(rowData.getImagen())
                    .error(R.drawable.ic_cloud_off_grey)
                    .into(vh.imagen, new Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {
                            vh.progressBar.setVisibility(View.GONE);
                            vh.imagenError.setVisibility(View.GONE);
                            vh.imagen.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {
                            vh.progressBar.setVisibility(View.GONE);
                            vh.imagenError.setVisibility(View.VISIBLE);
                            vh.imagen.setVisibility(View.GONE);
                        }
                    });*/

            //cargas items propios
            vh.nombre.setText(rowData.apepaterno+" "+rowData.apematerno+", "+rowData.nombres);
            vh.dni.setText(rowData.numdoc);
            vh.telefono.setText(rowData.telefono);

            vh.itemView.setTag(rowData);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }

    public void insert(Persona miPersona, int position) {
        insertInternal(getSampleData(), miPersona, position);
    }

    public void remove(int position) {
        removeInternal(getSampleData(), position);
    }

    public void clear() {
        clearInternal(getSampleData());
    }

    public void swapPositions(int from, int to) {
        swapPositions(getSampleData(), from, to);
    }

    public List<Persona> getSampleData() {
        return sampleData;
    }

    public void setSampleData(List<Persona> sampleData) {
        this.sampleData = sampleData;
    }

    /**********parte ViewHolder************/
    public static class ViewHolder extends UltimateRecyclerviewViewHolder implements View.OnClickListener {

        private final ImageView imagen;
        private final TextView nombre;
        private final TextView dni;
        private final TextView telefono;
        /*private final ImageView imageCompartir;
        private final ImageView imageGuardar;
        private final ImageView imagenError;
        private final ProgressBar progressBar;*/


        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            nombre = (TextView) itemView.findViewById(
                    R.id.nombre);
            dni = (TextView) itemView.findViewById(
                    R.id.numdocumento);
            telefono = (TextView) itemView.findViewById(
                    R.id.telefono);
            imagen = (ImageView) itemView.findViewById(
                    R.id.iv_avatar);
            //imagen.setOnClickListener(this);
            /*fecha = (TextView) itemView.findViewById(R.id.txt_fecha);
            lugar = (TextView) itemView.findViewById(R.id.txt_direccion);
            creadorPor = (TextView) itemView.findViewById(R.id.txt_gestor);
            imageCompartir = (ImageView) itemView.findViewById(R.id.image_compartir);
            imageCompartir.setOnClickListener(this);

            imageGuardar = (ImageView) itemView.findViewById(R.id.image_guardar);
            imageGuardar.setOnClickListener(this);

            imagenError = (ImageView) itemView.findViewById(R.id.error_img_evento);
            //imagenError.setOnClickListener(this);

            progressBar = (ProgressBar) itemView.findViewById(R.id.loadingEvento);*/

        }

        @Override
        public void onClick(View v) {
            /*//   CEvento miNoticia = ((CEvento)v.getTag());
            // int ide= miNoticia.getIdevento();

            if(v.getId()==R.id.image_compartir){
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Este es mi texto a enviar.");
                sendIntent.setType("text/plain");
                v.getContext().startActivity(Intent.createChooser(sendIntent, "Compartir con..."));

            }else if(v.getId()==R.id.image_checkin){
                / *String a= idusuario;
                CEvento miEvento = ((CEvento)v.getTag(v.getId()));
                Inscribirce(miEvento.getIdevento(), Integer.parseInt(a), v);* /

            }else if(v.getId()==R.id.image_guardar)  {
                Toast.makeText(v.getContext(), "Se agrego noticia a favoritos...!", Toast.LENGTH_SHORT).show();
            }else{

                 /*Toast.makeText(v.getContext(), "OnItemClick :D -" +
                  ((CNoticia) v.getTag()).getTitulo(), Toast.LENGTH_SHORT).show();* /


                //CEventoDetalle mieve=ObtenrEvento(miNoticia.getIdevento());
                Intent intent = new Intent(v.getContext(), actDetalleEvento.class);

                //intent.putExtra("CNoticia", (java.io.Serializable) v.getTag());

                intent.putExtra("parametro", (Serializable) v.getTag());
                v.getContext().startActivity(intent);
            }*/
        }
    }
}
