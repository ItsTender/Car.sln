package com.tawfeeq.carsln;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.protobuf.StringValue;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedFragment extends Fragment {

    FireBaseServices fbs;
    TextView tvMan, tvPrice, tvPower, tvYear, tvUsers, tvKilometre, tvTransmission, tvSeller, tvEngine, tvLocation, tvTest, tvColor;
    ImageView ivCar, ivSeller, ivSaved, ivBack, ivDelete;
    boolean sell_lend;
    String Email,Man, Mod, Photo,Transmission,Engine,ID,Color,Location,NextTest,SecondPhoto,ThirdPhoto;
    Integer Price,Power,Year,Users,Kilometre;
    String pfp;
    Boolean isFound;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailedFragment newInstance(String param1, String param2) {
        DetailedFragment fragment = new DetailedFragment();
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
        View view = inflater.inflate(R.layout.fragment_detailed, container, false);

        Bundle bundle =this.getArguments();


        ID=bundle.getString("ID");
        sell_lend=bundle.getBoolean("SellorLend");
        Email=bundle.getString("Email");
        Man=bundle.getString("Man");
        Mod=bundle.getString("Mod");
        Price=bundle.getInt("Price");
        Photo=bundle.getString("Photo");
        SecondPhoto=bundle.getString("Second");
        ThirdPhoto=bundle.getString("Third");
        Power =bundle.getInt("HP");
        Engine =bundle.getString("Engine");
        Year =bundle.getInt("Year");
        Users =bundle.getInt("Users");
        Kilometre=bundle.getInt("Kilo");
        Transmission=bundle.getString("Transmission");
        Color=bundle.getString("Color");
        Location=bundle.getString("Area");
        NextTest=bundle.getString("Test");


        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        fbs = FireBaseServices.getInstance();
        ivCar=getView().findViewById(R.id.DetailedCar);
        tvMan=getView().findViewById(R.id.DetailedMan);
        tvPrice =getView().findViewById(R.id.DetailedPrice);
        tvPower =getView().findViewById(R.id.DetailedHP);
        tvYear = getView().findViewById(R.id.DetailedYear);
        tvKilometre =getView().findViewById(R.id.DetailedKilometre);
        tvUsers= getView().findViewById(R.id.DetailedUsers);
        tvTransmission =getView().findViewById(R.id.DetailedGear);
        tvLocation = getView().findViewById(R.id.DetailedLocationArea);
        tvTest = getView().findViewById(R.id.DetailedTestUntil);
        tvColor = getView().findViewById(R.id.DetailedColor);
        tvSeller = getView().findViewById(R.id.DetailedUserMail);
        ivSeller = getView().findViewById(R.id.imageViewSeller);
        tvEngine = getView().findViewById(R.id.DetailedEngine);
        ivSaved = getView().findViewById(R.id.imageView4); //the Saved Icon......
        ivBack =getView().findViewById(R.id.DetailedGoBack); // Goes Back To Where ever the User Was.
        ivDelete =getView().findViewById(R.id.DetailedDeleteListing);


        String str = Email;
        int n = str.indexOf("@");
        String user = str.substring(0,n);

        // Get User Profile Photo.....

        if(fbs.getUser()!=null && Email.equals(fbs.getAuth().getCurrentUser().getEmail())){

            tvSeller.setText(fbs.getUser().getUsername());

            pfp = fbs.getUser().getUserPhoto();
            if (pfp == null || pfp.isEmpty()) {
                Picasso.get().load(R.drawable.generic_icon).into(ivSeller);
            } else {
                Picasso.get().load(pfp).into(ivSeller);
            }

            ivDelete.setVisibility(View.VISIBLE);

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "Hold The Remove Listing Button to Delete This Car Listing", Toast.LENGTH_LONG).show();
                }
            });

            ivDelete.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    ProgressDialog progressDialog= new ProgressDialog(getActivity());
                    progressDialog.setTitle("Deleting...");
                    progressDialog.setMessage("Deleting Your Car Listing, Please Wait!");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setIcon(R.drawable.slnround);
                    progressDialog.show();

                    fbs.getStore().collection("MarketPlace").document(ID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(), "Successfully Deleted Your Car Listing", Toast.LENGTH_SHORT).show();
                            BottomNavigationView bnv = getNavigationBar();

                            if (bnv.getSelectedItemId() == R.id.market) {
                                GoToFragmentCars();
                            }
                            else if (bnv.getSelectedItemId() == R.id.searchcar){
                                GoToFragmentSearch();
                            }
                            else if (bnv.getSelectedItemId() == R.id.savedcars) {
                                GoToFragmentSaved();
                            }
                            else if (bnv.getSelectedItemId() == R.id.profile){
                                GoToProfile();
                            }
                            progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Couldn't Deleted Your Car Listing, Try Again Later", Toast.LENGTH_SHORT).show();
                            BottomNavigationView bnv = getNavigationBar();

                            if (bnv.getSelectedItemId() == R.id.market) {
                                GoToFragmentCars();
                            }
                            else if (bnv.getSelectedItemId() == R.id.searchcar){
                                GoToFragmentSearch();
                            }
                            else if (bnv.getSelectedItemId() == R.id.savedcars) {
                                GoToFragmentSaved();
                            }
                            else if (bnv.getSelectedItemId() == R.id.profile){
                                GoToProfile();
                            }
                            progressDialog.dismiss();
                        }
                    });

                    return true;
                }
            });

        }
        else {

            ivDelete.setVisibility(View.INVISIBLE);
            fbs.getStore().collection("Users").document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    tvSeller.setText(documentSnapshot.getString("username"));

                    pfp = documentSnapshot.getString("userPhoto");

                    if (pfp == null || pfp.isEmpty()) {
                        Picasso.get().load(R.drawable.generic_icon).into(ivSeller);
                    } else {
                        Picasso.get().load(pfp).into(ivSeller);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Couldn't Retrieve User Profile Info", Toast.LENGTH_SHORT).show();
                    Picasso.get().load(R.drawable.generic_icon).into(ivSeller);
                }
            });
        }

        // Get Profile Photo Ends


        if(!Photo.equals("")) {
            ivCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Fragment gtn = new DetailedPhotosFragment();
                    Bundle bundle = new Bundle();


                    bundle.putString("ID", ID);
                    bundle.putBoolean("SellorLend", sell_lend);
                    bundle.putString("Email", Email);
                    bundle.putString("Man", Man);
                    bundle.putString("Mod", Mod);
                    bundle.putInt("HP", Power);
                    bundle.putInt("Price", Price);
                    bundle.putString("Photo", Photo);
                    bundle.putString("Second", SecondPhoto);
                    bundle.putString("Third", ThirdPhoto);
                    bundle.putString("Engine", Engine);
                    bundle.putString("Transmission", Transmission);
                    bundle.putInt("Year", Year);
                    bundle.putInt("Kilo", Kilometre);
                    bundle.putInt("Users", Users);
                    bundle.putString("Color", Color);
                    bundle.putString("Area", Location);
                    bundle.putString("Test", NextTest);


                    gtn.setArguments(bundle);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.FrameLayoutMain, gtn);
                    ft.commit();
                }
            });
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomNavigationView bnv = getNavigationBar();

                if (bnv.getSelectedItemId() == R.id.market) {
                    GoToFragmentCars();
                }
                else if (bnv.getSelectedItemId() == R.id.searchcar){
                    GoToFragmentSearch();
                }
                else if (bnv.getSelectedItemId() == R.id.savedcars) {
                    GoToFragmentSaved();
                }
                else if (bnv.getSelectedItemId() == R.id.profile){
                    GoToProfile();
                }
            }
        });

        tvSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str = fbs.getAuth().getCurrentUser().getEmail();

                if(str.equals(Email)){
                    // Always will be like that!
                    GoToProfile();
                    setNavigationBarProfile();
                }
                else {
                    // might make it a popup with (UserPhoto, Username, Phone-"Call,Whatsapp").......
                    Fragment gtn = new SellerPageFragment();
                    Bundle bundle = new Bundle();


                    bundle.putString("Email", Email);
                    bundle.putString("CarName", Year.toString() + " " + Man + " " + Mod);


                    gtn.setArguments(bundle);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.FrameLayoutMain, gtn);
                    ft.commit();
                }
            }
        });

        ivSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str = fbs.getAuth().getCurrentUser().getEmail();

                if(str.equals(Email)){

                    GoToProfile();
                    setNavigationBarProfile();
                }
                else {

                    Fragment gtn = new SellerPageFragment();
                    Bundle bundle = new Bundle();


                    bundle.putString("Email", Email);
                    bundle.putString("CarName", Year.toString() + " " + Man + " " + Mod);


                    gtn.setArguments(bundle);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.FrameLayoutMain, gtn);
                    ft.commit();
                }
            }
        });


        // Bind the Saved Icon *and* Save/Remove the Car

        String str1 = fbs.getAuth().getCurrentUser().getEmail();
        int n1 = str1.indexOf("@");
        String user1 = str1.substring(0,n1);

        ArrayList<String> Saved = fbs.getUser().getSavedCars();

        if(Saved.contains(ID)) {
            ivSaved.setImageResource(R.drawable.saved_removebg_preview__1_);
            isFound = true;
        }
        else{
            ivSaved.setImageResource(R.drawable.saved_removebg_preview);
            isFound = false;
        }

        ivSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFound) Saved.remove(ID);
                if(!isFound) Saved.add(ID);

                fbs.getStore().collection("Users").document(user1).update("savedCars", Saved).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if(isFound)
                        {
                            ivSaved.setImageResource(R.drawable.saved_removebg_preview);
                            fbs.getUser().setSavedCars(Saved);
                            isFound = false;
                        }
                        else{
                            ivSaved.setImageResource(R.drawable.saved_removebg_preview__1_);
                            fbs.getUser().setSavedCars(Saved);
                            isFound = true;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Couldn't Add/Remove The Car, Try Again Later", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        // Functions Ends......


        tvMan.setText(Man + " " + Mod);

        String prc= Price.toString();
        if(sell_lend) {
            tvPrice.setText(prc + "₪");
        }
        else{
            tvPrice.setText(prc + "₪" + " Monthly");
        }

        String power=Power.toString(); tvPower.setText(power);
        String year=Year.toString(); tvYear.setText(year);
        String Kilo =Kilometre.toString(); tvKilometre.setText(Kilo+" km");
        tvTransmission.setText(Transmission);
        tvLocation.setText(Location);
        tvTest.setText(NextTest);
        tvColor.setText(Color);

        if(Users==1){
            String Owners= Users.toString(); tvUsers.setText(Owners + " Owner");
        }
        else {
            String Owners= Users.toString(); tvUsers.setText(Owners + " Owners");
        }

        tvEngine.setText(Engine);


        if ( Photo == null || Photo.isEmpty())
        {
            Picasso.get().load(R.drawable.photo_iv).into(ivCar);

        }
        else {
            Picasso.get().load(Photo).into(ivCar);

        }

    }

    private void GoToFragmentCars() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new AllCarsFragment());
        ft.commit();
    }

    private void GoToFragmentSearch() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new SearchFragment());
        ft.commit();
    }

    private void GoToFragmentAdd() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new AddCarFragment());
        ft.commit();
    }

    private void GoToFragmentSaved() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new SavedCarsFragment());
        ft.commit();
    }

    private void GoToProfile() {
        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new ProfileFragment());
        ft.commit();
    }


    private void setNavigationBarProfile() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.profile);
    }

    private BottomNavigationView getNavigationBar(){
        return ((MainActivity) getActivity()).getBottomNavigationView();
    }



}