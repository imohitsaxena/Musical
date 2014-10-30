package com.techilse.mohit.musical.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mohit on 28-10-2014.
 */
public class CurrentAppState implements Serializable{
    private List<Song> playlist;
    private float volumeLevel;
    private boolean shuffle;
    private int repeat;
    private long positionOfSong;
    private int playingSongInPlaylist;

    private List<Integer> queuedSongList;
    private List<Integer> completedSongList;

    public List<Song> getPlaylist() {
        if (playlist != null) {
            return playlist;
        } else {
            return new ArrayList<Song>();
        }
    }

    public void setPlaylist(List<Song> playlist) {
        this.playlist = playlist;
    }

    public float getVolumeLevel() {
        return volumeLevel;
    }

    public void setVolumeLevel(float volumeLevel) {
        this.volumeLevel = volumeLevel;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public long getPositionOfSong() {
        return positionOfSong;
    }

    public void setPositionOfSong(long positionOfSong) {
        this.positionOfSong = positionOfSong;
    }

    public int getPlayingSongInPlaylist() {
        return playingSongInPlaylist;
    }

    public void setPlayingSongInPlaylist(int playingSongInPlaylist) {
        this.playingSongInPlaylist = playingSongInPlaylist;
    }

    public List<Integer> getQueuedSongList() {
        if (queuedSongList != null) {
            return queuedSongList;
        } else {
            return new LinkedList<Integer>();
        }
    }

    public void setQueuedSongList(List<Integer> queuedSongList) {
        this.queuedSongList = queuedSongList;
    }

    public List<Integer> getCompletedSongList() {
        if (completedSongList != null) {
            return completedSongList;
        } else {
            return new LinkedList<Integer>();
        }
    }

    public void setCompletedSongList(List<Integer> completedSongList) {
        this.completedSongList = completedSongList;
    }
}
