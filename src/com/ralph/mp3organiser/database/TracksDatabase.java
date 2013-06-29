package com.ralph.mp3organiser.database;

import com.ralph.mp3organiser.objects.Track;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;


/**
 *
 * @author Ralph
 */
public class TracksDatabase {
    private Connection conn;
    private Statement stmt;
    
    public TracksDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:music.db");
            stmt = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void closeConnection() {
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createTablesWithDrop() {
        try {
            stmt.executeUpdate("drop table if exists tracks;");
            String createSQL = "create table tracks (";
            createSQL = createSQL + "fullPath TEXT PRIMARY KEY,";
            createSQL = createSQL + "title TEXT,";
            createSQL = createSQL + "artist TEXT,";
            createSQL = createSQL + "album TEXT,";
            createSQL = createSQL + "format TEXT,";
            createSQL = createSQL + "hash TEXT,";
            createSQL = createSQL + "length INTEGER,";
            createSQL = createSQL + "quality INTEGER,";
            createSQL = createSQL + "trackNum INTEGER);";
            stmt.executeUpdate(createSQL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean saveTrackToDB(Track track) {
        boolean success = false;
        try {
            String insertSQL = "insert into tracks (";
            insertSQL = insertSQL + "fullPath,";
            insertSQL = insertSQL + "title,";
            insertSQL = insertSQL + "artist,";
            insertSQL = insertSQL + "album,";
            insertSQL = insertSQL + "format,";
            insertSQL = insertSQL + "hash,";
            insertSQL = insertSQL + "length,";
            insertSQL = insertSQL + "quality,";
            insertSQL = insertSQL + "trackNum) values (?,?,?,?,?,?,?,?,?);";
            PreparedStatement prep = conn.prepareStatement(insertSQL);
            
            prep.setString(1, track.getFullPath());
            prep.setString(2, track.getTitle());
            prep.setString(3, track.getArtist());
            prep.setString(4, track.getAlbum());
            prep.setString(5, track.getFormat());
            prep.setString(6, track.getHash());
            prep.setInt(7, track.getLength());
            prep.setInt(8, track.getQuality());
            if (track.getTrackNum() == null) {
                prep.setInt(9, -1);
            } else {
                prep.setInt(9, track.getTrackNum());
            }
            prep.executeUpdate();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }
    public Track loadTrackFromDB(String fullPath) {
        // TODO: finish this
        try {
            ResultSet rs = stmt.executeQuery("select * from tracks where fullPath='" + fullPath + "';");
            while (rs.next()) {
                System.out.println("name = " + rs.getString("name"));
                System.out.println("job = " + rs.getString("occupation"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Track("");
    }
    public ArrayList<String> getDistinctArtists() {
        ArrayList<String> results = new ArrayList<String>();
        try {
            ResultSet rs = stmt.executeQuery("select distinct(artist) from tracks;");
            while (rs.next()) {
                results.add(rs.getString(1));
            }
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Integer getNumTracks() {
        try {
            ResultSet rs = stmt.executeQuery("select count(*) from tracks;");
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void previewTable() {
        printResults("select * from tracks;");
    }
    public void musicStatistics() {
        System.out.println("Most common sample rate:");
        printResults("select quality, count(quality) as cnt from tracks group by quality order by count(quality) desc;");
        System.out.println("\n\nMost common format:");
        printResults("select format, count(format) as cnt from tracks group by format order by count(format) desc;");
    }
    public void showDuplicates() {
        printResults("select * from tracks where title || artist in (select title || artist as b from tracks group by title || artist having count(title || artist) > 2) order by title || artist");
    }
    public void showNulls() {
        printResults("select fullPath, title, artist, album from tracks where title is null or artist is null or album is null;");
    }
    private void printResults(String sql) {
        try {
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData md = rs.getMetaData();
            String header = "";
            for (int i=1; i <= md.getColumnCount(); i++) {
                header = header + md.getColumnLabel(i) + "\t";
            }
            System.out.println(header);
            while (rs.next()) {
                String row="";
                for (int i=1; i <= md.getColumnCount(); i++) {
                    if (rs.getObject(i) != null) {
                        row = row + rs.getObject(i).toString() + "\t";
                    } else {
                        row = row + "\t";
                    }
                }
                System.out.println(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
