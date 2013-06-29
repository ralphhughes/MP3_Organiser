/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ralph.mp3organiser.file;

import com.ralph.mp3organiser.objects.Track;
import com.ralph.utils.HashUtils;
import java.io.File;
import java.text.ParseException;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

/**
 *
 * @author Ralph
 */
public class AudioMetadataReader {
    public Track loadMetadataFromFile(File inputFile) {
        Track myTrack = new Track(inputFile.getPath());
        try {
            AudioFile f = AudioFileIO.read(inputFile);
            
            Tag tag = f.getTag();
            if (tag != null) {
                myTrack.setArtist(tag.getFirst(FieldKey.ARTIST));
                myTrack.setTitle(tag.getFirst(FieldKey.TITLE));
                myTrack.setAlbum(tag.getFirst(FieldKey.ALBUM));
                try {
                    myTrack.setTrackNum(Integer.parseInt(tag.getFirst(FieldKey.TRACK)));
                } catch (NumberFormatException nfe) {
                    myTrack.setTrackNum(null);
                }
            }

            AudioHeader ah = f.getAudioHeader();
            if (ah != null) {
                myTrack.setFormat(ah.getFormat());
                myTrack.setLength(ah.getTrackLength());
                myTrack.setQuality(ah.getSampleRateAsNumber());
            }

            myTrack.setHash(HashUtils.fileMD5(inputFile));
            return myTrack;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public void writeMetadataToFile(File outputFile, Track myTrack) {
        try {
            AudioFile f = AudioFileIO.read(outputFile);
            Tag tag = f.getTag();
            tag.setField(FieldKey.ARTIST, myTrack.getArtist());
            tag.setField(FieldKey.TITLE, myTrack.getTitle());
            tag.setField(FieldKey.ALBUM, myTrack.getAlbum());
            tag.setField(FieldKey.TRACK, myTrack.getTrackNum().toString());
            AudioFileIO.write(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Common filename formats:
    // Album\# - Title.ext
    // Artist - Album\# - Title.ext
    // Artist - Title.ext
    // # - Title.ext

    public String getArtistFromFilename(File file) {
        String fullPath = file.getPath();
        // Get just the filename portion
        int startCutPos = fullPath.lastIndexOf(File.separator);
        int endCutPos = fullPath.lastIndexOf(".");
        String shortName = fullPath.substring(startCutPos + 1, endCutPos);
        String parts[] = shortName.split("-");
        if (parts.length==2) {
            return parts[0].trim();
        } else {
            return null;
        }
    }
    public String getTitleFromFilename(File file) {
        String fullPath = file.getPath();
        // Get just the filename portion
        int startCutPos = fullPath.lastIndexOf(File.separator);
        int endCutPos = fullPath.lastIndexOf(".");
        String shortName = fullPath.substring(startCutPos + 1, endCutPos);
        String parts[] = shortName.split("-");
        if (parts.length==2) {
            return parts[1].trim();
        } else {
            return null;
        }
    }
}
