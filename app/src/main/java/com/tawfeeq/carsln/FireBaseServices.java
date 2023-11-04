package com.tawfeeq.carsln;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class FireBaseServices {
    private static FireBaseServices instance;
    private FirebaseAuth auth;

    private FirebaseFirestore store;

    private FirebaseStorage storage;

    private Uri selectedImageURL;

    public Uri getSelectedImageURL() {
        return selectedImageURL;
    }

    public void setSelectedImageURL(Uri selectedImageURL) {
        this.selectedImageURL = selectedImageURL;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseFirestore getStore() {
        return store;
    }

    public FirebaseStorage getStorage() {return storage;}

    private FireBaseServices()
    {
        this.auth= FirebaseAuth.getInstance();
        this.store= FirebaseFirestore.getInstance();
        this.storage=FirebaseStorage.getInstance();
    }
    public static FireBaseServices getInstance()
    {
        if(instance==null) instance = new FireBaseServices();

        return instance;
    }
}
