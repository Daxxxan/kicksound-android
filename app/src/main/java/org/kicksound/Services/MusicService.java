package org.kicksound.Services;

import org.kicksound.Models.Music;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface MusicService {
    @GET("Music/musicByArtist/{id}")
    Call<List<Music>> getMusicByArtistId(@Header("Authorization") String authorization, @Path("id") String id);
}
