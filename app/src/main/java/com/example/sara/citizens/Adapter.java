package com.example.sara.citizens;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

public class Adapter extends BaseAdapter {

    Context context;
    List<User> listUsers;

    public Adapter(Context context, List<User> listUsers) {
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
        view = inflater.inflate(R.layout.activity_list_users_elements,null);

        ImageView imageView= view.findViewById(R.id.elemImage);
        TextView textView1 = view.findViewById(R.id.elemName);
        TextView textView2 = view.findViewById(R.id.elemGender);
        TextView textView3 = view.findViewById(R.id.elemRegistered);
        ImageButton imageButton = view.findViewById(R.id.elemLocation);

        textView1.setText(listUsers.get(i).getName());
        textView2.setText(listUsers.get(i).getGender());
        textView3.setText(listUsers.get(i).getFecha());
        new DownloadImageFromInternet((ImageView) view.findViewById(R.id.elemImage)).execute(listUsers.get(i).getPicture());

        final String location = listUsers.get(i).getLocation();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a Uri from an intent string. Use the result to create an Intent.
                String uri = String.format(Locale.ENGLISH, "geo:0,0?q=%s",location);
                Log.d("LOCATION", location);
                Uri gmmIntentUri = Uri.parse(uri);



                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");

                // Attempt to start an activity that can handle the Intent
                context.startActivity(mapIntent);
            }

        });


        return view;
    }

    static class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
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
