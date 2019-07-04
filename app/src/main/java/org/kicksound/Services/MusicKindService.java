package org.kicksound.Services;

import org.kicksound.Models.MusicKind;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface MusicKindService {

    @GET("musicKinds")
    Call<List<MusicKind>> getMusicKinds(@Header("Authorization") String authorization);
}
