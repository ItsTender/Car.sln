package com.tawfeeq.carsln;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.CarsHolder> {

// 1- Planet Adapter

    private Context context;
    private  CarsAdapter.OnItemClickListener CarsListener;
    private ArrayList<Cars> cars;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(CarsAdapter.OnItemClickListener Listener) {
        this.CarsListener = Listener;
    }
    public CarsAdapter(Context context, ArrayList<Cars> cars) {
        this.context = context;
        this.cars = cars;

        this.CarsListener =new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Fragment gtn = new InfoFragment();
                Bundle bundle= new Bundle();

                int hp =cars.get(position).getBHP();
                int price =cars.get(position).getPrice();


                bundle.putString("Car", cars.get(position).getManufacturer()+ " " +cars.get(position).getModel());
                bundle.putInt("HP", hp);
                bundle.putInt("Price",price);
                bundle.putString("Photo",cars.get(position).getPhoto() );

                gtn.setArguments(bundle);
                FragmentTransaction ft= ((MainActivity)context).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayoutMain, gtn);
                ft.commit();
            }
        };
    }

    public CarsAdapter() {
    }

    @NonNull
    @Override
    public CarsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cars_test,parent,false);

        return new CarsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarsAdapter.CarsHolder holder, int position) {

        Cars car= cars.get(position);
        holder.SetDetails(car);

        holder.itemView.setOnClickListener(v -> {
            if (CarsListener != null) {
                CarsListener.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cars.size();
    }


    // 2- Cars Holder

    class CarsHolder extends RecyclerView.ViewHolder {

        private TextView txtMan,txtMod,txtHP,txtPrice;
        private ImageView ivCar; // shows the car photo from the firestore string url, if the photo url is "" then show the stock image (R.drawable.carplain.jpg)
        public CarsHolder(@NonNull View itemView) {
            super(itemView);

            txtMan= itemView.findViewById(R.id.tvtxtMan);
            txtMod= itemView.findViewById(R.id.tvtxtMod);
            txtHP= itemView.findViewById(R.id.tvtxtHP);
            txtPrice= itemView.findViewById(R.id.tvtxtPrice);
            ivCar= itemView.findViewById(R.id.CarRes);

        }

        void SetDetails (Cars car){

            txtMan.setText(car.getManufacturer());
            txtMod.setText(car.getModel());
            txtHP.setText("Horse Power: " + car.getBHP());
            txtPrice.setText("Price: " + car.getPrice()+"$");

            if (car.getPhoto() == null || car.getPhoto().isEmpty())
            {
                Picasso.get().load(R.drawable.carplain).into(ivCar);

            }
            else {
                Picasso.get().load(car.getPhoto()).into(ivCar);

            }

        }


    }


}
