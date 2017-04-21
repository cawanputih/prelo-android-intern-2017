package purba.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Purba on 4/12/2017.
 */

public class WishAdapter extends RecyclerView.Adapter<WishAdapter.RecyclerViewHolder> {

    ArrayList<Wish> wl ;
    Context context;

    public WishAdapter(Context c, ArrayList<Wish> wishlist){
        wl = wishlist;
        context = c;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        // di tutorial ini kita hanya menggunakan data String untuk tiap item
        public TextView tvname;
        public TextView tvprice;
        public ImageView ivdp;
        public View v;

        public RecyclerViewHolder(View v) {
            super(v);
            this.v = v;
            tvname = (TextView) v.findViewById(R.id.idname);
            tvprice= (TextView) v.findViewById(R.id.idprice);
            ivdp = (ImageView) v.findViewById(R.id.idimage);
        }
    }

    @Override
    public int getItemCount() {
        return wl.size();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        RecyclerViewHolder vh = new RecyclerViewHolder(v);

        return vh;
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        final Wish wish = wl.get(position);
        holder.tvname.setText(wish.getName());
        holder.tvprice.setText("Rp "+wish.getPrice());
        Glide.with(context).load(wish.getSrcimage()).into(holder.ivdp);


        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idbarang = wish.getid();
                Intent i = new Intent(context, CheckoutActivity.class);
                i.putExtra("key_idbarang",idbarang);
                context.startActivity(i);
            }
        });

    }


}
