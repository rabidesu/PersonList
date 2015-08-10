package com.example.nem.recycle.app;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.LruCache;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Адаптер для списка RecycleView.
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.PersonViewHolder>{

    List<Person> persons;
    private LruCache<String, Bitmap> memoryCache;
    public RecycleAdapter(List<Person> persons) {
        this.persons = persons;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        final int cacheSize = (int) Runtime.getRuntime().maxMemory() / 1024 / 8;
        memoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        setViewText(holder.personName, "", persons.get(position).getName());
        setViewText(holder.personAge, "Age: ", persons.get(position).getAge() != 0 ? String.valueOf(persons.get(position).getAge()) : "");
        setViewText(holder.personId, "Id: ", persons.get(position).getId());
        setViewText(holder.personEmail, "E-mail: ", persons.get(position).getEmail());
        setViewText(holder.personPhone, "Phone: ", persons.get(position).getPhone());
        setViewText(holder.personAddress, "Address: ", persons.get(position).getAddress());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 'Time: 'HH:mm:ss", Locale.ENGLISH);

        setViewText(holder.personRegistered, "Registered: ", persons.get(position).getRegistered().getTime() == 0 ?
                "" : dateFormat.format(persons.get(position).getRegistered()));

        /*
        * Устанавливается стандартное изображение, если изображение пользователя не указано.
        * Если указано - вызывается метод загрузки изображения.
         */
        if (persons.get(position).getPicture().equals("")) holder.personPhoto.setImageResource(R.drawable.placeholder);
        else new ImageLoader(holder.personPhoto).execute(persons.get(position).getPicture());

        if (persons.get(position).isActive()){
            holder.personActive.setText("Active");
        }else{
            holder.personActive.setText("No Active");
        }

        switch (persons.get(position).getGender()){
            case "male": holder.personGender.setImageResource(R.drawable.male_img);break;
            case "female": holder.personGender.setImageResource(R.drawable.female_img);break;
            default: holder.personGender.setImageResource(R.drawable.unknown_gender_img);
        }
    }

    // Устанавливает текст для большинства TextView. Если значение не указано - выводится Unknown.
    private void setViewText(TextView view, String value, String data){
        if (!data.equals("")) view.setText(value + data);
        else view.setText(value + "Unknown");
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView personName;
        TextView personAge;
        TextView personActive;
        TextView personId;
        TextView personEmail;
        TextView personPhone;
        TextView personAddress;
        TextView personRegistered;
        ImageView personPhoto;
        ImageView personGender;
        Toolbar toolbar;

        public PersonViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.cardView);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personAge = (TextView)itemView.findViewById(R.id.person_age);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
            personActive = (TextView)itemView.findViewById(R.id.person_active);
            personId = (TextView)itemView.findViewById(R.id.person_id);
            personEmail = (TextView)itemView.findViewById(R.id.person_email);
            personPhone = (TextView)itemView.findViewById(R.id.person_phone);
            personAddress = (TextView)itemView.findViewById(R.id.person_address);
            personRegistered = (TextView)itemView.findViewById(R.id.person_registered);
            personGender = (ImageView)itemView.findViewById(R.id.person_gender);
            toolbar = (Toolbar) itemView.findViewById(R.id.card_toolbar);
            toolbar.inflateMenu(R.menu.list_context_menu);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    persons.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    return true;
                }
            });

        }

    }

    /*
    * В отдельном потоке загружает изображение из кэша или по URL и устанавливает в ImageView.
     */
    private class ImageLoader extends AsyncTask<String, Void, Bitmap> {

        ImageView imageView;

        public ImageLoader (ImageView view){
            imageView = view;
        }
        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap bitmap = getImageFromCache(params[0]);

            if (bitmap == null) {
                try {
                    URL imageURL = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) imageURL.openConnection();
                    connection.getHeaderFields();
                    URL currentURL = new URL(connection.getURL().toString());
                    connection = (HttpURLConnection) currentURL.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    connection.disconnect();
                    addImageToCache(params[0], bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }

    }

    private void addImageToCache(String key, Bitmap bitmap){
        if (getImageFromCache(key) == null){
            memoryCache.put(key, bitmap);
        }
    }

    private Bitmap getImageFromCache(String key){
        return memoryCache.get(key);
    }

}
