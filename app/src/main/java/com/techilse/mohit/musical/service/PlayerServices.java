package com.techilse.mohit.musical.service;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.techilse.mohit.musical.entity.CurrentAppState;
import com.techilse.mohit.musical.entity.Song;
import com.techilse.mohit.musical.exception.AppException;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Mohit on 11-10-2014.
 */
public class PlayerServices {

    private CurrentAppState currentAppState;
    private MediaPlayer mp = null;
    private boolean paused = false;

    public PlayerServices(CurrentAppState currentAppState, MediaPlayer mp) {
        this.currentAppState = currentAppState;
        this.mp = mp;
    }


    //Setting up media player for all required services
    private MediaPlayer getMediaPlayer(Song song) throws AppException {
        String uriPath = null;
        if (song != null) {
            uriPath = song.getPathToFile();
        }
        if (mp == null) {
            Log.d("Player Service", "Media Player was null :(");
            mp = new MediaPlayer();
        } else {
            mp.reset();
        }
        try {
            mp.setDataSource(uriPath);
            mp.prepare();
        } catch (IOException e) {
            throw new AppException(song.getTitle() + " not found.", e);
        }
        return mp;
    }

    //For playing a song
    public MediaPlayer playSong() {

        if (!paused) {
            Song song = currentAppState.getPlaylist().get(currentAppState.getPlayingSongInPlaylist());
            try {
                mp = getMediaPlayer(song);
            } catch (AppException e) {
                Log.d("AppException in PlayerService", e.getMessage());
            }
        }
        paused = false;
        mp.start();
        return mp;
    }

    //For pausing a playing song
    public void pauseSong() {
        if (mp != null && mp.isPlaying()) {
            mp.pause();
            paused = true;
        }
    }

    //For stopping a playing / paused song
    public CurrentAppState stopPlayer() {
        if (mp != null) {
            mp.stop();
            mp.release();
        }
        mp = null;
        return currentAppState;
    }

    //Rewind song with given number of seconds or with seek bar position
    public void seekBackward(int bySeconds) {
        int currentPosition;
        if (mp != null) {
            currentPosition = mp.getCurrentPosition();
            mp.seekTo(currentPosition - bySeconds * 1000);
        }
    }

    //Forward song with given number of seconds or with seek bar position
    public void seekForward(int bySeconds) {
        int currentPosition;
        if (mp != null) {
            currentPosition = mp.getCurrentPosition();
            mp.seekTo(currentPosition + bySeconds * 1000);
        }
    }

    //Gives the index of last song completed from queued song list [pointing to playlist]
    public int getPreviousTrack() {
        int lastIndex = currentAppState.getCompletedSongList().size() - 1;
        int indexOfLastSong = -1;
        if (lastIndex >= 0) {
            indexOfLastSong = currentAppState.getCompletedSongList().get(lastIndex);
            currentAppState.getCompletedSongList().remove(lastIndex);
            currentAppState.getQueuedSongList().add(0, indexOfLastSong);
            currentAppState.setPlayingSongInPlaylist(indexOfLastSong);
        }
        return indexOfLastSong;
    }

    //Gives the index of next song in the queued song list [pointing to playlist]
    public int getNextTrack() {
        int indexOfNextSong = 0;
        if (currentAppState.getQueuedSongList().size() > 1) {
            List<Integer> completedSongs = currentAppState.getCompletedSongList();
            completedSongs.add(currentAppState.getQueuedSongList().get(0));
            currentAppState.setCompletedSongList(completedSongs);
            indexOfNextSong = currentAppState.getQueuedSongList().get(1);
            currentAppState.getQueuedSongList().remove(0);
            currentAppState.setPlayingSongInPlaylist(indexOfNextSong);
        }
        return indexOfNextSong;
    }

    //Will prepare the queued playlist of indices of songs in playlist for sequencing to play.
    public List<Integer> prepareQueuedSongList() {
        boolean shuffleStatus = currentAppState.isShuffle();
        int repeatType = currentAppState.getRepeat();
        List<Integer> queuedSongList = currentAppState.getQueuedSongList();
        List<Integer> completedSongList = currentAppState.getCompletedSongList();
        List<Song> playlist = currentAppState.getPlaylist();

        int indexOfCurrentlyPlaySong = currentAppState.getPlayingSongInPlaylist();

        if (playlist.size() > 0) {
            if (queuedSongList != null && queuedSongList.size() + completedSongList.size() != playlist.size()) {
                completedSongList.clear();
                for (int i = 0; i < playlist.size(); i++) {
                    queuedSongList.add(i);
                }
            }

            if (!shuffleStatus) {                           //Shuffle is off

                indexOfCurrentlyPlaySong = currentAppState.getPlayingSongInPlaylist();
                Collections.sort(queuedSongList);
                Iterator<Integer> queuedSongListIterator = queuedSongList.listIterator();
                int index;
                while (queuedSongListIterator.hasNext()) {
                    index = queuedSongListIterator.next();
                    if (index < indexOfCurrentlyPlaySong) {
                        completedSongList.add(index);
                        queuedSongListIterator.remove();
                    }
                }

                if (repeatType == 0) {                      // Repeat is off
                    if (queuedSongList.size() < 1) {
                        pauseSong();
                    }
                } else if (repeatType == 1) {               // Repeat current song
                    queuedSongList.clear();
                    completedSongList.clear();
                    queuedSongList.add(indexOfCurrentlyPlaySong);
                } else {                                    // Repeat all songs
                    if (queuedSongList.size() < 1) {
                        completedSongList.clear();
                        queuedSongList.clear();
                        for (int i = 0; i < playlist.size(); i++) {
                            queuedSongList.add(i);
                        }
                    }
                }

            } else {                                        //Shuffle is on

                indexOfCurrentlyPlaySong = currentAppState.getQueuedSongList().get(0);
                Collections.shuffle(queuedSongList);

                if (repeatType == 0) {                      // Repeat is off
                    if (queuedSongList.size() < 1) {
                        pauseSong();
                    }
                } else if (repeatType == 1) {               // Repeat current song
                    queuedSongList.clear();
                    completedSongList.clear();
                    queuedSongList.add(indexOfCurrentlyPlaySong);
                } else {                                    // Repeat all songs
                    if (queuedSongList.size() < 1) {
                        completedSongList.clear();
                        queuedSongList.clear();
                        for (int i = 0; i < playlist.size(); i++) {
                            queuedSongList.add(i);
                        }
                        Collections.shuffle(queuedSongList);
                    }
                }
            }
        }
        return queuedSongList;
    }

    public void setVolume() {
        float volumeLevel = currentAppState.getVolumeLevel();
        mp.setVolume(volumeLevel, volumeLevel);
    }

    public void setSleepTimer(int forSeconds, final float initialVolume) {
        float finalVolume = 0;
        final float volumeDecreaseFactor = (float) (initialVolume - finalVolume) / forSeconds;

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            float currentVolume = initialVolume;

            @Override
            public void run() {
                if (currentVolume >= 0) {
                    mp.setVolume(currentVolume, currentVolume);
                    currentVolume = currentVolume - volumeDecreaseFactor;
                } else {
                    timer.cancel();
                }
            }
        }, 0, 1000);
    }

}
