/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ralph.mp3organiser;

import com.ralph.mp3organiser.file.AudioMetadataReader;
import com.ralph.mp3organiser.database.TracksDatabase;
import com.ralph.mp3organiser.objects.Track;
import com.ralph.utils.FileFinder;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Ralph
 */
public class Main {
/*
 * Idea of this app is to read my entire music collection file by file
 * cataloging the filenames, paths and metadata into a sql lite database
 *
 * There will then be a few different phases to organising things
 * -Phase 1: Anything without metadata needs its metadata populating from folder/filenames
 * -Phase 2: Anything still without metadata needs marking to be ignored, save list for manual check later
 * -Phase 3: Check for duplicates, delete duplicates or mark to be ignored? Use the higher quality one?
 * -Phase 4: Move the tagged files into the desired folder structure
 */
    public static void main(String[] args) {
        //new Main().readFilesToDB();
        new Main().test();
    }
    public void test() {
        TracksDatabase db = new TracksDatabase();
        //db.previewTable();
        //db.musicStatistics();
        db.showNulls();
        db.showDuplicates();
        db.closeConnection();
    }
    public void readFilesToDB() {
        TracksDatabase db = new TracksDatabase();
        db.createTablesWithDrop();


        AudioMetadataReader reader = new AudioMetadataReader();
        String audioExtensions[] = {".it",".m4a",".mp3",".mp4",".wav",".wma",".xm"};
        //ArrayList<File> files = FileFinder.find(new File("D:\\Music\\"),6); // Fucking gigabytes!
        ArrayList<File> files = FileFinder.find(new File("D:\\Music\\DaveMusic\\"),2, audioExtensions); // Large sample
        //ArrayList<File> files = FileFinder.find(new File("D:\\Music\\"),1,"m4a"); //Small sample
        for (File currentFile: files) {
            System.out.println("Processing: " + currentFile.getPath());
            Track currentTrack = reader.loadMetadataFromFile(currentFile);
            if (currentTrack == null) {
                System.out.println("ERROR: Failed to read metadata");
            } else {
                db.saveTrackToDB(currentTrack);
            }
        }

        db.closeConnection();
    }
}
