package org.kicksound.Services;

import org.kicksound.Models.Music;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface PlaylistService {
    @GET("Playlists/{id}/musicWithArtist")
    Call<List<Music>> getMusicFromPlaylist(
            @Header("Authorization") String authorization,
            @Path("id") String accountId
    );
}
