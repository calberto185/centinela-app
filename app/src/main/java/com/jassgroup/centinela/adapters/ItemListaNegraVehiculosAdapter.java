package com.jassgroup.centinela.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jassgroup.centinela.R;
import com.jassgroup.centinela.clases.ListaNegra;
import com.jassgroup.centinela.clases.ListaNegraVehiculos;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.List;

public class ItemListaNegraVehiculosAdapter extends UltimateViewAdapter {

    private List<ListaNegraVehiculos> sampleData = null;
    private Context contexto = null;
    //public  static String idusuario;


    public ItemListaNegraVehiculosAdapter(List<ListaNegraVehiculos> sampleData, Context contexto){
        this.setSampleData(sampleData);
        this.contexto = contexto;
        //this.idusuario=idusuario;
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parentViewGroup) {
        View rowView;

        rowView = LayoutInflater.from(parentViewGroup.getContext())
                .inflate(R.layout.ly_item_vehiculo_listanegra, parentViewGroup, false);
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
            final ViewHolder vh = (ViewHolder) viewHolder;
            final ListaNegraVehiculos rowData = getSampleData().get(position);
            //cargas items propios
            vh.apellidos.setText(rowData.apellidos.toUpperCase()+" "+rowData.nombres.toUpperCase());
            //vh.nombres.setText(rowData.nombres);
            //vh.numdoc.setText(rowData.numdoc);
            vh.placa.setText(rowData.placa);
            //vh.tipo.setText(rowData.tipovehiculo.toUpperCase());
            if (rowData.tipovehiculo.equals("M")){
                vh.imagen.setImageResource(R.drawable.moto);
            }else {
                vh.imagen.setImageResource(R.drawable.car);
            }
            //vh.color.setText(rowData.color);
            vh.fecha.setText(rowData.fecha);

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

    public void insert(ListaNegraVehiculos miSolicitud, int position) {
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

    public List<ListaNegraVehiculos> getSampleData() {
        return sampleData;
    }

    public void setSampleData(List<ListaNegraVehiculos> sampleData) {
        this.sampleData = sampleData;
    }

    /**********parte ViewHolder************/
    public static class ViewHolder extends UltimateRecyclerviewViewHolder implements View.OnClickListener {


        private TextView apellidos;
        //private TextView nombres;
        //private TextView numdoc;
        private TextView placa;
        //private TextView color;
        //private TextView tipo;
        private ImageView imagen;
        private TextView fecha;



        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            apellidos = (TextView) itemView.findViewById(
                    R.id.conductor);
            placa = (TextView) itemView.findViewById(
                    R.id.placa);
            /*color = (TextView) itemView.findViewById(
                    R.id.color);*/
            /*tipo = (TextView) itemView.findViewById(
                    R.id.tipo);*/
            imagen = (ImageView) itemView.findViewById(
                    R.id.iv_carro);
            fecha = (TextView) itemView.findViewById(
                    R.id.fecha);
        }



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
