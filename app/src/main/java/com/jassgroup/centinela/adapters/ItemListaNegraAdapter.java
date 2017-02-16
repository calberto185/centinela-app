package com.jassgroup.centinela.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jassgroup.centinela.clases.ListaNegra;
import com.jassgroup.centinela.clases.Persona;
import com.jassgroup.centinela.R;
import com.jassgroup.centinela.clases.Persona;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.List;

/**
 * Created by kenwhiston on 13/08/2016.
 */
public class ItemListaNegraAdapter extends UltimateViewAdapter {

    private List<ListaNegra> sampleData = null;
    private Context contexto = null;
    //public  static String idusuario;


    public ItemListaNegraAdapter(List<ListaNegra> sampleData, Context contexto){
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
            final ListaNegra rowData = getSampleData().get(position);
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
            vh.nombre.setText(rowData.apellidos+", "+rowData.nombres);
            vh.dni.setText(rowData.numdoc);
            vh.telefono.setText(rowData.telefono);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        // mark  the view as selected:
        //viewHolder.itemView.setSelected(mSelectedRows.contains(i));
    }

    public void insert(ListaNegra miSolicitud, int position) {
        insertInternal(getSampleData(), miSolicitud, position);
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

    public List<ListaNegra> getSampleData() {
        return sampleData;
    }

    public void setSampleData(List<ListaNegra> sampleData) {
        this.sampleData = sampleData;
    }

    /**********parte ViewHolder************/
    public static class ViewHolder extends UltimateRecyclerviewViewHolder implements View.OnClickListener {

        private TextView dni;
        private TextView nombre;
        private TextView apematerno;
        private TextView apepaterno;
        private TextView correo;
        private ImageView imagen;
        private TextView telefono;

        /*private final ImageView imagen;
        private final TextView alias;
        private final TextView dni;
        private final TextView numunidades;
        private final TextView fecha;
        private final TextView grupoSangre;
        private final ImageView imageCompartir;
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

        /*private void registrarDonacion(Solicitud miSolicitud, final Context miContexto){
            //obtener valores de la base de datos
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mDatabase;
            mDatabase = database.getReference("donaciones");

            // (1) get today's date
            Date today = Calendar.getInstance().getTime();

            // (2) create a date "formatter" (the date format we want)
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            String fecha = formatoFecha.format(today);

            //asignamos donacion
            Donacion miDonacion = new Donacion(GestorDatabase.getInstance(miContexto).obtenerValorUsuario(ScriptDatabaseUsuario.Column.NICK),
                    GestorDatabase.getInstance(miContexto).obtenerValorUsuario(ScriptDatabaseUsuario.Column.CORREO),
                    GestorDatabase.getInstance(miContexto).obtenerValorUsuario(ScriptDatabaseUsuario.Column.GRUPOSANGRE),
                    fecha,
                    uid,
                    GestorDatabase.getInstance(miContexto).obtenerValorUsuario(ScriptDatabaseUsuario.Column.TELEFONO));

            final MaterialDialog miProgress = new MaterialDialog.Builder(miContexto)
                    .title("Registro de Donación")
                    .content("Porfavor estamos registrando su donación, espere.")
                    .progress(true, 0)
                    .show();

            mDatabase.child(miSolicitud.idsol).child(uid).setValue(miDonacion).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    miProgress.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(miContexto,
                                "Donación Registrada Correctamente...!",
                                Toast.LENGTH_LONG).show();
                    }else{
                        miProgress.dismiss();
                        Toast.makeText(miContexto,
                                "Error intente en otro momento...!",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }*/

        @Override
        public void onClick(View v) {

            // Creating Bundle object
            /*Bundle b = new Bundle();
            // Storing data into bundle
            final Persona miListanegra = (Persona) v.getTag();
            final Context miContexto = v.getContext();
            b.putString("numdoc", miListanegra.numdoc);
            Intent intent = new Intent(miContexto, null);
            intent.putExtras(b);
            v.getContext().startActivity(intent);*/



            /*new MaterialDialog.Builder(v.getContext())
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            // TODO
                            dialog.dismiss();
                            registrarDonacion(miSolicitud,miContexto);
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            // TODO
                            dialog.dismiss();
                        }
                    })
                    .title(this.getResources().getString(R.string.txt_dconfirmacion))
                    .content(miSolicitud.motivo)
                    .positiveText(this.getResources().getString(R.string.txt_dpositivo))
                    .negativeText(this.getResources().getString(R.string.txt_dnegativo))
                    .show();*/

            //v.setSelected(true);
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
