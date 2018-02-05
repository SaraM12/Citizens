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

/**
 * Created by sara on 4/2/18.
 */

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


        /*int j = i;
        final Bitmap[] bm = {null};
        URL url = null;
        try {
            url = new URL(listUsers.get(i).getPicture());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        final URL finalUrl = url;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    bm[0] = BitmapFactory.decodeStream(finalUrl.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/


        /*AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bm = null;
                try {
                    URL aURL = new URL(listUsers.get(j).getPicture());
                    URLConnection conn = aURL.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    bm = BitmapFactory.decodeStream(bis);
                    bis.close();
                    is.close();


                }catch (IOException e) {
                    Log.e("ERROR", "Error getting bitmap", e);
                }


            }
        });*/


        new DownloadImageFromInternet((ImageView) view.findViewById(R.id.elemImage))
                .execute(listUsers.get(i).getPicture());


        //imageView.setImageBitmap(bm[0]);

        //Log.d("BITMAP", bm[0].toString());

        //imageView.setImageBitmap(bm);
        //imageView.setImageResource(Integer.parseInt(listUsers.get(i).getPicture()));
        textView1.setText(listUsers.get(i).getName());
        textView2.setText(listUsers.get(i).getGender());
        textView3.setText(listUsers.get(i).getFecha());
        //textView2.setText(listUsers.get(i).getFecha()+ " "+ listUsers.get(i).getLocation());

        return view;
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            //Toast.makeText(context, "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
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
