package com.tawfeeq.carsln.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.tawfeeq.carsln.objects.CarID;
import com.tawfeeq.carsln.adapters.CarsAdapter;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.objects.UserProfile;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellerPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellerPageFragment extends Fragment {

    String CarName; // Custom Car Name String.....
    Button btnSMS, btnCall, btnWhatsapp;
    TextView tvSellerName, tvEmail;
    RecyclerView rc;
    FireBaseServices fbs;
    CarsAdapter Adapter;
    ArrayList<CarID> SellerCars;
    ImageView ivSeller, Back;
    String pfp;
    UserProfile usr;
    CarID currentCar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SellerPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellerPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellerPageFragment newInstance(String param1, String param2) {
        SellerPageFragment fragment = new SellerPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_seller_page, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs= FireBaseServices.getInstance();
        tvSellerName = getView().findViewById(R.id.tvSellerUser);
        tvEmail = getView().findViewById(R.id.tvselleremail);
        btnCall = getView().findViewById(R.id.btnCallSeller);
        btnSMS = getView().findViewById(R.id.btnSMSContact);
        btnWhatsapp =getView().findViewById(R.id.btnWhatsapp);
        ivSeller = getView().findViewById(R.id.imageViewSellerPage);

        SellerCars =new ArrayList<CarID>();


        currentCar = fbs.getSelectedCar();
        CarName = currentCar.getYear() + " " + currentCar.getManufacturer() + " " + currentCar.getModel();

        String str = currentCar.getEmail();
        int n = str.indexOf("@");
        String user = str.substring(0,n);

        // Get User Profile.....

        fbs.getStore().collection("Users").document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                usr = documentSnapshot.toObject(UserProfile.class);

                tvSellerName.setText(usr.getUsername());
                tvEmail.setText(str);

                pfp = usr.getUserPhoto();
                if (pfp == null || pfp.isEmpty())
                {
                    ivSeller.setImageResource(R.drawable.slnpfp);
                }
                else {
                    Glide.with(getActivity()).load(pfp).into(ivSeller);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Couldn't Retrieve User Profile Photo", Toast.LENGTH_SHORT).show();
                Picasso.get().load(R.drawable.slnpfp).into(ivSeller);
            }
        });

        // Get Profile Ends


        ivSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!usr.getUserPhoto().equals("")) GoToViewPhoto();
            }
        });

    }

    private void GoToDetailedCar() {

        Fragment gtn= new DetailedFragment();
        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, gtn);
        ft.commit();
    }


    private void GoToViewPhoto() {

        Fragment gtn= new ViewPhotoFragment();
        Bundle bundle = new Bundle();

        bundle.putString("Email", currentCar.getEmail());
        bundle.putString("Username", usr.getUsername());
        bundle.putString("PFP", usr.getUserPhoto());
        bundle.putString("From", "Seller");

        gtn.setArguments(bundle);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.FrameLayoutMain, gtn);
        ft.commit();
    }

}