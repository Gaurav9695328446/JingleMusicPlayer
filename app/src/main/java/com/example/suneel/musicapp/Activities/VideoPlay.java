package com.example.suneel.musicapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suneel.musicapp.Utils.HttpHandler;
import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.Adapters.VideoAdapter;
import com.example.suneel.musicapp.models.Youtubemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by suneel on 16/5/18.
 */

public class VideoPlay extends AppCompatActivity {
    private String playlistid, playlistname;
    private RecyclerView recyclerView;
    private Button back;
    private TextView text;
    private ProgressDialog pDialog;
    private VideoAdapter videoAdapter;
    private TextView textView;
    private ArrayList<Youtubemodel> videoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoplay);
        playlistid = getIntent().getStringExtra("playlistid");
        playlistname = getIntent().getStringExtra("playlistname");
        recyclerView = (RecyclerView) findViewById(R.id.videoplaylist);
        textView = (TextView) findViewById(R.id.hello);
        back = (Button) findViewById(R.id.back);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VideoPlay.this, Youtube.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        textView
                .setText(playlistname);
        new GetVideoList(playlistid).execute();


    }

    public void videoSong(ArrayList<Youtubemodel> videoList, String videoid) {
        Intent intent = new Intent(VideoPlay.this, YoutubePlay.class);
        intent.putExtra("videoList", videoList);
        intent.putExtra("videoid", videoid);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }

    private class GetVideoList extends AsyncTask<Void, Void, Void> {
        String playlistid;

        public GetVideoList(String playlistId) {
            this.playlistid = playlistId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(VideoPlay.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("https://www.googleapis.com/youtube/v3/playlistItems?part=snippet%2CcontentDetails&maxResults=50&playlistId="+playlistid+"&key=AIzaSyBkN6a80sJzYHq9UzkHfMkee0IS64lt45s");

            Log.e("message", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray items = jsonObj.getJSONArray("items");

                    // looping through All Contacts
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject c = items.getJSONObject(i);

                        String id = c.getString("id");
                        String publishedat = c.getJSONObject("snippet").getString("title");
                        String image = c.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("default").getString("url");
                        String videoid = c.getJSONObject("snippet").getJSONObject("resourceId").getString("videoId");
                        // tmp hash map for single contact
                        // adding contact to contact list
                        videoList.add(new Youtubemodel(image, id, publishedat, videoid));
                    }
                } catch (final JSONException e) {
                    Log.e("message", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e("message", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
           /* *
             * Updating parsed JSON data into ListView
             **/

            videoAdapter = new VideoAdapter(VideoPlay.this, videoList);
            recyclerView.setAdapter(videoAdapter);

        }


    }
}
