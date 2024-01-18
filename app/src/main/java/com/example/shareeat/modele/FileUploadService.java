package com.example.shareeat.modele;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileUploadService {
    @Multipart
    @POST("/uploadPP.php") // Le chemin doit être ajusté en fonction de votre structure de serveur Alwaysdata
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part image);

    @Multipart
    @POST("/uploadRecipeImage.php") // Changer le chemin en fonction de votre serveur
    Call<ResponseBody> uploadRecipeImage(@Part MultipartBody.Part image);
}