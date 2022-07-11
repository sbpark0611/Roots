package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaylistActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    TextView playlistNameText;
    RankingAdapter rankingAdapter;
    Button playlistStartButton;
    Button playlistAddButton;
    String playlistId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        Intent getIntent = getIntent();
        playlistId = getIntent.getStringExtra("playlist_id");

        playlistNameText = findViewById(R.id.playlist_name_text);

        recyclerView = findViewById(R.id.playlist_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        rankingAdapter = new RankingAdapter(getApplicationContext());

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.address))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RetrofitService service1 = retrofit.create(RetrofitService.class);

        Call<PlaylistData> call = service1.getPlaylistData(playlistId);

        call.enqueue(new Callback<PlaylistData>(){
            @Override
            public void onResponse(Call<PlaylistData> call, Response<PlaylistData> response){
                if(response.isSuccessful()){
                    PlaylistData result = response.body();
                    Log.d("MY TAG", "onResponse: 성공, 결과\n"+result);

                    playlistNameText.setText(result.getName());

                    for(int i = 0; i < result.getPlaylist().size(); i++){
                        rankingAdapter.setArrayData(result.getPlaylist().get(i));
                    }
                    recyclerView.setAdapter(rankingAdapter);
                }
                else{
                    Log.d("MY TAG", "onResponse: 실패 "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<PlaylistData> call, Throwable t){
                Log.d("MY TAG", "onFailure: "+t.getMessage());
            }
        });

        playlistStartButton = (Button) findViewById(R.id.playlist_start_button);
        playlistStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new GsonBuilder().setLenient().create();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(getResources().getString(R.string.address))
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                RetrofitService service1 = retrofit.create(RetrofitService.class);
                Call<PlaylistData> call = service1.getPlaylistData(playlistId);
                call.enqueue(new Callback<PlaylistData>(){
                    @Override
                    public void onResponse(Call<PlaylistData> call, Response<PlaylistData> response){
                        if(response.isSuccessful()){
                            PlaylistData result = response.body();

                            Intent intent = new Intent(getApplicationContext(), PlayingMusic.class);
                            intent.putExtra("playlist_id", playlistId);
                            intent.putExtra("index", "0");
                            intent.putExtra("repeatMode", "repeat_all");
                            intent.putExtra("musicName", result.getPlaylist().get(0));
                            startActivity(intent);
                        }
                        else{
                            Log.d("MY TAG", "onResponse: 실패 " + String.valueOf(response.code()));
                        }
                    }
                    @Override
                    public void onFailure(Call<PlaylistData> call, Throwable t){
                        Log.d("MY TAG", "onFailure: "+t.getMessage());
                    }
                });

            }
        });

        playlistAddButton = (Button) findViewById(R.id.playlist_add_button);
        playlistAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectMusicActivity.class);
                intent.putExtra("playlist_id", playlistId);
                startActivity(intent);
            }
        });
    }
}