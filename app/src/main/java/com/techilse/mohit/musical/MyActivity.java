package com.techilse.mohit.musical;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.techilse.mohit.musical.dao.MediaAccess;
import com.techilse.mohit.musical.entity.Album;
import com.techilse.mohit.musical.entity.CurrentAppState;
import com.techilse.mohit.musical.entity.Song;
import com.techilse.mohit.musical.service.PlayerServices;
import com.techilse.mohit.musical.ui.AlbumAdapter;
import com.techilse.mohit.musical.ui.SongAdapter;
import com.techilse.mohit.musical.util.AlbumArtCache;
import com.techilse.mohit.musical.util.CalcUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MyActivity extends Activity {
    MediaPlayer mp = new MediaPlayer();
    Handler handler;

    private AlbumArtCache albumArtCache;
    CurrentAppState currentAppState;
    PlayerServices playerServices;
    List<Song> playlist;

    List<Album> albums = null;

    SeekBar volumeSeekBar;

    ListView listView;
    View songListViewFooter;

    TextView playingSongDetailTV;

    Button shuffleButton;
    TextView numberOfTracksTV;
    Button repeatButton;

    TextView elapsedTimeTV;
    SeekBar songProgressSeekBar;
    TextView totalTimeTV;

    Button rewindButton;
    Button playPauseButton;
    Button forwardButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        handler = new Handler();
        mp.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //Initialisation of all the views
        volumeSeekBar = (SeekBar) findViewById(R.id.volumeSeekBar);

        listView = (ListView) findViewById(R.id.listView);
        songListViewFooter = View.inflate(MyActivity.this, R.layout.song_listview_footer, null);

        shuffleButton = (Button) findViewById(R.id.shuffleButton);
        playingSongDetailTV = (TextView) findViewById(R.id.playingSongDetailTV);
        repeatButton = (Button) findViewById(R.id.repeatButton);

        elapsedTimeTV = (TextView) findViewById(R.id.elapsedTimeTV);
        songProgressSeekBar = (SeekBar) findViewById(R.id.songProgressSeekBar);
        totalTimeTV = (TextView) findViewById(R.id.totalTimeTV);

        rewindButton = (Button) findViewById(R.id.rewindButton);
        playPauseButton = (Button) findViewById(R.id.playPauseButton);
        forwardButton = (Button) findViewById(R.id.forwardButton);
        new SongPopulator().run();

        //Setting up App Defaults
        if (currentAppState == null) {
            playlist = new ArrayList<Song>();
            currentAppState = new CurrentAppState();
            playlist = new MediaAccess(MyActivity.this).getAllSongs(albums);
            currentAppState.setRepeat(0);
            currentAppState.setShuffle(false);
            currentAppState.setVolumeLevel((float) 0.5);
            currentAppState.setPositionOfSong(0);
            currentAppState.setPlayingSongInPlaylist(0);
            currentAppState.setPlaylist(playlist);
            playerServices = new PlayerServices(currentAppState, mp);

            currentAppState.setQueuedSongList(playerServices.prepareQueuedSongList());
        } else {
            //TODO
            //Getting serialized data from SDCARD
        }


        if (albumArtCache == null) {
            albumArtCache = new AlbumArtCache(MyActivity.this);
        }

        volumeSeekBar.setProgress((int) (currentAppState.getVolumeLevel() * 100));
        volumeSeekBar.setOnSeekBarChangeListener(volumeSeekBarChangeListener);
        mp.setVolume(currentAppState.getVolumeLevel(), currentAppState.getVolumeLevel());

        shuffleButton.setOnClickListener(onClickListener);
        repeatButton.setOnClickListener(onClickListener);

        songProgressSeekBar.setOnSeekBarChangeListener(songProgressSeekBarChangeListener);

        rewindButton.setOnClickListener(onClickListener);
        playPauseButton.setOnClickListener(onClickListener);
        forwardButton.setOnClickListener(onClickListener);

        rewindButton.setOnLongClickListener(onLongClickListener);
        playPauseButton.setOnLongClickListener(onLongClickListener);
        forwardButton.setOnLongClickListener(onLongClickListener);


        Collections.sort(albums, Album.ALBUM_NAME_SORTER);
        setAlbumsInListView(albums);


        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playerServices.getNextTrack();
                updateViewAndPlay();
            }
        });

    }


    //For handling click on the ListView
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (adapterView.getItemAtPosition(i) instanceof Album) {
                Album songsOfAlbum = (Album) adapterView.getItemAtPosition(i);
                setSongsInListView(songsOfAlbum);
            } else if (adapterView.getItemAtPosition(i) instanceof Song) {
                Song selectedSong = (Song) adapterView.getItemAtPosition(i);
                Toast.makeText(MyActivity.this, "Have Faith," + selectedSong.getTitle() + " will play :)", Toast.LENGTH_SHORT).show();
                //TODO
            } else {
                listView.removeFooterView(view);
                setAlbumsInListView(albums);
            }
        }
    };

    //For handling long clicks on the rewind, forward & play button
    View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            int buttonId = view.getId();
            if (buttonId == R.id.rewindButton) {
                Toast.makeText(MyActivity.this, "Previous Track", Toast.LENGTH_SHORT).show();
                playerServices.getPreviousTrack();
                updateViewAndPlay();
            } else if (buttonId == R.id.forwardButton) {
                playerServices.getNextTrack();
                updateViewAndPlay();
                Toast.makeText(MyActivity.this, "Next Track", Toast.LENGTH_SHORT).show();
            } else if (buttonId == R.id.playPauseButton) {
                //TODO
                // Playlist update on long click
            }
            return true;
        }
    };

    //For handling clicks on all the buttons
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int buttonId = view.getId();
            if (buttonId == R.id.shuffleButton) {
                if (shuffleButton.getText().toString().equals(getResources().getString(R.string.shuffle_on_text))) {
                    shuffleButton.setBackgroundResource(R.drawable.shuffle_off);
                    shuffleButton.setText(R.string.shuffle_off_text);
                    currentAppState.setShuffle(false);
                } else if (shuffleButton.getText().toString().equals(getResources().getString(R.string.shuffle_off_text))) {
                    shuffleButton.setBackgroundResource(R.drawable.shuffle_on);
                    shuffleButton.setText(R.string.shuffle_on_text);
                    currentAppState.setShuffle(true);
                }
                currentAppState.setQueuedSongList(playerServices.prepareQueuedSongList());
            } else if (buttonId == R.id.repeatButton) {
                if (repeatButton.getText().toString().equals(getResources().getString(R.string.repeat_all_text))) {
                    repeatButton.setBackgroundResource(R.drawable.repeat_one);
                    repeatButton.setText(R.string.repeat_one_text);
                    currentAppState.setRepeat(1);
                } else if (repeatButton.getText().toString().equals(getResources().getString(R.string.repeat_one_text))) {
                    repeatButton.setBackgroundResource(R.drawable.repeat_off);
                    repeatButton.setText(R.string.repeat_off_text);
                    currentAppState.setRepeat(0);
                } else if (repeatButton.getText().toString().equals(getResources().getString(R.string.repeat_off_text))) {
                    repeatButton.setBackgroundResource(R.drawable.repeat_all);
                    repeatButton.setText(R.string.repeat_all_text);
                    currentAppState.setRepeat(2);
                }
                currentAppState.setQueuedSongList(playerServices.prepareQueuedSongList());
            } else if (buttonId == R.id.playPauseButton) {
                if (playPauseButton.getText().toString().equals(getResources().getString(R.string.play_text))) {
                    playPauseButton.setBackgroundResource(R.drawable.pause);
                    playPauseButton.setText(R.string.pause_text);
                    updateViewAndPlay();
                    handler.postDelayed(songProgressBarUpdate2, 100);

                    //songProgressBarUpdate.execute(null);
                } else if (playPauseButton.getText().toString().equals(getResources().getString(R.string.pause_text))) {
                    playPauseButton.setBackgroundResource(R.drawable.play);
                    playPauseButton.setText(R.string.play_text);
                    playerServices.pauseSong();

                }
                //TODO
            } else if (buttonId == R.id.rewindButton) {
                playerServices.seekBackward(3);
            } else if (buttonId == R.id.forwardButton) {
                playerServices.seekForward(3);
            }
        }
    };

    //Volume Control
    SeekBar.OnSeekBarChangeListener volumeSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            mp.setVolume((float) (i * 0.01), (float) (i * 0.01));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            currentAppState.setVolumeLevel((float) (seekBar.getProgress() * 0.01));
            playerServices.setVolume();
        }
    };


    //Auto Update and Seeking by the seek bar.
    SeekBar.OnSeekBarChangeListener songProgressSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mp.seekTo(CalcUtil.getCurrentTimeFromPercentage(seekBar.getProgress(), mp.getDuration()));
        }
    };


    private void setAlbumsInListView(List<Album> albums) {
        if (albums != null) {
            final ListAdapter adapter = new AlbumAdapter(this, albums, albumArtCache);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(onItemClickListener);
        }
    }

    private void setSongsInListView(Album songsOfAlbum) {
        //playlist.clear();
        final ListAdapter adapter = new SongAdapter(MyActivity.this, songsOfAlbum, albumArtCache);
        //playlist.addAll(songsOfAlbum.getSongs());
        listView.addFooterView(songListViewFooter);
        listView.setAdapter(adapter);

        //TODO
        //Fix playlist change for view change
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SongPopulator implements Runnable {

        @Override
        public void run() {
            MediaAccess mediaAccess = new MediaAccess(getApplicationContext());
            albums = mediaAccess.getAllAlbums(new ArrayList<Song>());
        }
    }

    Runnable songProgressBarUpdate2 = new Runnable() {
        @Override
        public void run() {
            songProgressSeekBar.setProgress(CalcUtil.getPercentage(mp.getCurrentPosition(), mp.getDuration()));
            elapsedTimeTV.setText(CalcUtil.getFormattedTime(mp.getCurrentPosition()));
            handler.postDelayed(this, 100);
        }
    };

    //Trial of AsyncTask
    /*
    AsyncTask songProgressBarUpdate = new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] objects) {
            //  MediaAccess mediaAccess = new MediaAccess(getApplicationContext());
            // albums = mediaAccess.getAllAlbums(new ArrayList<Song>());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            listView.deferNotifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }
    };
    */

    private void updateViewAndPlay() {
        mp = playerServices.playSong();
        totalTimeTV.setText(CalcUtil.getFormattedTime(mp.getDuration()));
        playingSongDetailTV.setText(currentAppState.getPlaylist().get(currentAppState.getPlayingSongInPlaylist()).getSongDetail());

        playingSongDetailTV.setSelected(true);
        playingSongDetailTV.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        playingSongDetailTV.setSingleLine(true);
    }
}
