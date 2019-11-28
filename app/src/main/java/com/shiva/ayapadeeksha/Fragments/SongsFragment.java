package com.shiva.ayapadeeksha.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shiva.ayapadeeksha.Activities.PlayMusicActivity;
import com.shiva.ayapadeeksha.POJOClass.AudiosModel;
import com.shiva.ayapadeeksha.R;
import com.shiva.ayapadeeksha.Utils.AppConstants;
import com.shiva.ayapadeeksha.Utils.AppUrls;
import com.shiva.ayapadeeksha.Utils.AppUtils;
import com.shiva.ayapadeeksha.Utils.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SongsFragment extends Fragment {
    private ViewGroup root;
    private ProgressBar progressBar;
    private RecyclerView rvAudios;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = (ViewGroup) inflater.inflate(R.layout.fragment_songs, container, false);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppConstants.FRAGMENT_NAME = "Songs";
        getActivity().setTitle("Songs");
        init();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvAudios.setLayoutManager(linearLayoutManager);

        getAudiosList();
    }

    private void init() {
        rvAudios = (RecyclerView) root.findViewById(R.id.rvAudios);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
    }

    private void getAudiosList() {
        final ArrayList<AudiosModel> audiosModelArrayList = new ArrayList<>();
        if (AppUtils.isOnline(getActivity())) {
            progressBar.setVisibility(View.VISIBLE);
            AppUtils.getSongs(AppUrls.SONGS_LIST_URL, getActivity(), progressBar, new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject response = new JSONObject(result);
                        if (!response.getBoolean("error")) {
                            JSONArray jsonArray = response.getJSONArray("Audios List");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                AudiosModel audiosModel = new AudiosModel();

                                audiosModel.setId(jsonObject.getString("id"));
                                audiosModel.setTitle(jsonObject.getString("title"));
                                audiosModel.setType(jsonObject.getString("type"));
                                audiosModel.setAudio(jsonObject.getString("audio"));
                                audiosModel.setDate(jsonObject.getString("date"));
                                audiosModel.setStatus(jsonObject.getString("status"));

                                audiosModelArrayList.add(audiosModel);
                                AudiosAdapter audiosAdapter = new AudiosAdapter(getActivity(), audiosModelArrayList);
                                rvAudios.setAdapter(audiosAdapter);
                                audiosAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Empty", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else AppUtils.showSnackBar(getActivity());
    }


    public class AudiosAdapter extends RecyclerView.Adapter<AudiosAdapter.MyViewHolder> {
        private Activity activity;
        List<AudiosModel> audiosModelList;
        private String strSongURL, strSongName;

        public AudiosAdapter(Activity activity, List<AudiosModel> audiosModelList) {
            this.activity = activity;
            this.audiosModelList = audiosModelList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.list_item_songs, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final AudiosModel audiosModel = audiosModelList.get(position);

            holder.tvId.setText(audiosModel.getId());
            holder.tvName.setText(audiosModel.getTitle());
            holder.tvLink.setText(AppUrls.AUDIO_URL + audiosModel.getAudio());
        }

        @Override
        public int getItemCount() {
            return audiosModelList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvId, tvName, tvLink;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvId = (TextView) itemView.findViewById(R.id.tvId);
                tvName = (TextView) itemView.findViewById(R.id.tvName);
                tvLink = (TextView) itemView.findViewById(R.id.tvLink);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        strSongURL = tvLink.getText().toString();
                        strSongName = tvName.getText().toString();
                        Intent intent=new Intent(activity,PlayMusicActivity.class);
                        intent.putExtra("SONG_URL", strSongURL);
                        intent.putExtra("SONG_NAME", strSongName);
                        activity.startActivity(intent);
                    }
                });

            }
        }
    }

}
