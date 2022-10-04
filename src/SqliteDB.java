import org.w3c.dom.Text;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.*;

import static java.lang.Math.negateExact;
import static java.lang.Math.sqrt;

public class SqliteDB {
    Connection c = null;
    Statement stmt = null;
    HashMap<String, ArrayList<DataPoint>> groupToDataMap = new HashMap<>();

    public HashMap<String, ArrayList<DataPoint>> getGroupToDataMap() {
        return groupToDataMap;
    }

    SqliteDB() {
        // try connect to db
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:data/kpop_data.db");
            System.out.println("Connected to DB OK!!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public float percentLatin(int latin, int total) {
        float percent = (float) ((float)latin / (float)total) * 100f;
        return percent;
    }

    public void getData() {
        try {
            this.stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM KPopViews");
            while (rs.next()) {
                String song_name = rs.getString("song_name");
                String kpop_group = rs.getString("kpop_group");
                int number_of_views = rs.getInt("number_of_views");
                int total_lyrics = rs.getInt("total_lyrics");
                int latin_script_lyrics = rs.getInt("latin_script_lyrics");
                if (!groupToDataMap.containsKey(kpop_group)) {
                    ArrayList<DataPoint> l = new ArrayList<>();
                    l.add(new DataPoint(percentLatin(latin_script_lyrics, total_lyrics), number_of_views));
                    groupToDataMap.put(kpop_group, l);
                } else {
                    ArrayList<DataPoint> l = groupToDataMap.get(kpop_group);
                    l.add(new DataPoint(percentLatin(latin_script_lyrics, total_lyrics), number_of_views));
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                c.close();
            } catch (Exception e) {
                //error
            }
        }
    }
}

