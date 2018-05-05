package mx.com.dgom.sercco.android.prevent.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mx.com.dgom.sercco.android.prevent.Constantes;
import mx.com.dgom.sercco.android.prevent.Controller;
import mx.com.dgom.sercco.android.prevent.NotUserLogeadoException;
import mx.com.dgom.sercco.android.prevent.act.R;
import mx.com.dgom.sercco.android.prevent.to.DelitoTO;
import mx.com.dgom.util.io.net.NoInternetException;
import mx.com.dgom.util.str.StringsUtils;

/**
 * Created by beto on 10/15/15.
 */
public class TimelineListAdapter extends BaseAdapter {

    private final String TAG = "TimelineListAdapter";

    Context mContext;
    private List<DelitoTO> data = new ArrayList<DelitoTO>();
    private LayoutInflater mLayoutInflater = null;

    public TimelineListAdapter(Context ctx) {
        this.mContext = ctx;
        mLayoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addDelito(DelitoTO delito) {
        data.add(delito);
        this.notifyDataSetChanged();
    }

    public void addDelito(List<DelitoTO> delitos) {
        data.addAll(delitos);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int index) {
        return data.get(index);
    }

    @Override
    public long getItemId(int index) {
        return data.get(index).getId_evento();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        DelitoViewHolder viewHolder;
        if (convertView == null) {

            v = mLayoutInflater.inflate(R.layout.time_line_row_event, null);

            viewHolder = new DelitoViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (DelitoViewHolder) v.getTag();
        }

        final DelitoTO item = data.get(position);

        int idTipoDelito = item.getId_tipo_delito();

        viewHolder.imgLogoDelito.setImageBitmap(Constantes.getIcoBadgeByDelitoMed(idTipoDelito));
        viewHolder.txtNombreDelito.setText(Constantes.getDelitoNombre(idTipoDelito));
        viewHolder.txtFechaDelito.setText(StringsUtils.timeElapsedFormatDDText(item.getFch_delito()));
        viewHolder.txtLikeDelito.setText(item.getNum_likes() + "");
        viewHolder.txtDireccion.setText(item.getTxt_direccion());

        viewHolder.imgLikeButton.setOnClickListener(new ClickListener(item, viewHolder.txtLikeDelito));

        return v;
    }


    /**
     * Clase para controlar el clik del like
     */
    class ClickListener implements View.OnClickListener{

        DelitoTO delito;
        TextView txtLike;

        public ClickListener(DelitoTO delito,TextView txtLike){
            this.delito = delito;
            this.txtLike = txtLike;
        }

        @Override
        public void onClick(View view) {
            addPointAction(delito,txtLike);
        }
    }


    class DelitoViewHolder {

        public ImageView imgLogoDelito;
        public TextView txtNombreDelito;
        public TextView txtFechaDelito;
        public ImageView imgLikeButton;
        public TextView txtLikeDelito;
        public TextView txtDireccion;

        public DelitoViewHolder(View base) {
            imgLogoDelito   = (ImageView) base.findViewById(R.id.img_logo_delito);
            txtNombreDelito = (TextView)  base.findViewById(R.id.txt_nombre_delito);
            txtFechaDelito  = (TextView)  base.findViewById(R.id.txt_fecha_delito);
            imgLikeButton   = (ImageView) base.findViewById(R.id.img_like_button);
            txtLikeDelito   = (TextView)  base.findViewById(R.id.txt_likes_delito);
            txtDireccion    = (TextView)  base.findViewById(R.id.txt_direccion);
        }
    }

    /**
     * Agrega en uno el contador de likes
     * @param delito
     * @param txtLikes
     */
    public void addPointAction(DelitoTO delito, TextView txtLikes) {
        Controller controller = new Controller(mContext);
        try {
            int res = controller.addPoint(delito);
            //1 guardado ok, 0, no se puede por que es mio, -1 error, -2 ya se le dio like
            switch (res) {
                case 1:
                    int count = Integer.parseInt(txtLikes.getText().toString());
                    count++; //Agrega 1 al contador
                    txtLikes.setText((count) + "");
                    delito.setNum_likes(count);
                    Toast.makeText(mContext, R.string.like_ok, Toast.LENGTH_LONG).show();
                    break;
                case 0:
                    Toast.makeText(mContext, R.string.error_like_mio, Toast.LENGTH_LONG).show();
                    break;
                case -1:
                    Toast.makeText(mContext, R.string.error_like_generico, Toast.LENGTH_LONG).show();
                    break;
                case -2:
                    Toast.makeText(mContext, R.string.error_like_ya_like, Toast.LENGTH_LONG).show();
                    break;
            }


        } catch (NotUserLogeadoException e) {
            e.printStackTrace();
            Toast.makeText(mContext, R.string.error_usuario_no_logeado, Toast.LENGTH_LONG).show();
        } catch (NoInternetException e) {
            e.printStackTrace();
            Toast.makeText(mContext, R.string.error_no_internet, Toast.LENGTH_LONG).show();
        }
    }


}
