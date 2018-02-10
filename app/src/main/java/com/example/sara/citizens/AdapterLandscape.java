package com.example.sara.citizens;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;


public class AdapterLandscape extends BaseAdapter {
    Context context;
    List<User> listUsers;

    public AdapterLandscape(Context context, List<User> listUsers) {
        this.context = context;
        this.listUsers = listUsers;
    }

    @Override
    public int getCount() {
        return listUsers.size();
    }

    @Override
    public Object getItem(int i) {
        return listUsers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.activity_list_users_elements_landscape,null);

        ImageView imageView= view.findViewById(R.id.elemImage);
        TextView textView1 = view.findViewById(R.id.elemName);
        TextView textView2 = view.findViewById(R.id.elemGender);
        TextView textView3 = view.findViewById(R.id.elemRegistered);
        TextView textView4 = view.findViewById(R.id.elemUser);
        TextView textView5 = view.findViewById(R.id.elemPassword);


        textView1.setText(listUsers.get(i).getName());
        textView2.setText(listUsers.get(i).getGender());
        textView3.setText(listUsers.get(i).getFecha());
        textView4.setText("  "+listUsers.get(i).getUsername());
        textView5.setText("  "+listUsers.get(i).getPassword());
        new Adapter.DownloadImageFromInternet((ImageView) view.findViewById(R.id.elemImage)).execute(listUsers.get(i).getPicture());

        return view;
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

}
