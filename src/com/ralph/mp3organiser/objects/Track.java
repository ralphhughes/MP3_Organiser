package com.ralph.mp3organiser.objects;
/**
 *
 * @author Ralph
 */
public class Track {
    private String fullPath; //PK
    private String title;
    private String artist;
    private String album;
    private String format;
    private String hash;
    private Integer length;
    private Integer quality;
    private Integer trackNum;

    public Track(String fullPath) {
        this.fullPath = fullPath;
    }
    
    // Getters and setters
    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public Integer getTrackNum() {
        return trackNum;
    }

    public void setTrackNum(Integer trackNum) {
        this.trackNum = trackNum;
    }
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
