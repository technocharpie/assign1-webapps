package dao;

import org.springframework.jdbc.core.JdbcTemplate;

import model.*;
import java.util.Collection;
import java.util.ArrayList;

public class TrackDAO 
{
    private JdbcTemplate jdbcTemplate;

    public TrackDAO(JdbcTemplate jdbcTemp) 
    {
        this.jdbcTemplate = jdbcTemp;
    }
    
    public Track createTrack(Track track)
    {
        if (track != null)
        {
            String sql   = "INSERT INTO tracks VALUES (?, ?, ?)";
            int id       = track.getId();
            String title = track.getTitle();
            int albumId  = track.getAlbumId();
            this.jdbcTemplate.update(sql, id, title, albumId);
        }

        return track;
    }

    public Track getTrack(int id)
    {
        Track track    = new Track(id);
        String sql1    = "SELECT title FROM tracks WHERE id = ?";
        String sql2    = "SELECT album FROM tracks WHERE id = ?";
        String title   = this.jdbcTemplate.queryForObject(sql1, new Object[] {track.getId()}, String.class);
        String albumId = this.jdbcTemplate.queryForObject(sql2, new Object[] {track.getId()}, String.class);

        if (title != null)
        {
            track.setTitle(title);
            track.setAlbumId(Integer.parseInt(albumId));
        }
        else
        {
            track.setTitle("[no title]");
            track.setAlbumId(0);
        }
        return track;
    }

    public Collection<Track> getAllTracks()
    {
        Collection<Track> tracks = new ArrayList<Track>();
        
        this.jdbcTemplate.query(
                "SELECT * FROM tracks", new Object[] { },
                (rs, rowNum) -> new Track(rs.getInt("id"), rs.getString("title"), rs.getInt("album"))
        ).forEach(track -> tracks.add(track));


        return tracks;
    }

    public Collection<Track> getTracksByAlbumId(int albumId)
    {
        Collection<Track> tracks = new ArrayList<Track>();

        this.jdbcTemplate.query(
                "SELECT id, title FROM tracks WHERE album = ?", new Object[] { albumId },
                (rs, rowNum) -> new Track(rs.getInt("id"), rs.getString("title"),albumId)
        ).forEach(track -> tracks.add(track) );

        return tracks;
    }
    public Track updateTrack(Track track)
    {
        int id           = track.getId();
        String title     = track.getTitle();
        int albumId      = track.getAlbumId();

        String sql_count = "SELECT COUNT(*) FROM tracks WHERE id = ?";
        int rowCount     = this.jdbcTemplate.queryForObject(sql_count, new Object[] { id }, Integer.class);
        

        if (rowCount > 0)
        {
            String sql = "UPDATE tracks SET title = ?, album = ? WHERE id = ?";
            this.jdbcTemplate.update(sql, title, albumId, id);
        }
        else
        {
            track = createTrack(track);
        }

        return track;
    }

    public boolean deleteTrack(Track track)
    {
        boolean success  = false;
        int id           = track.getId();
        String sql_count = "SELECT COUNT(*) FROM tracks WHERE id = ?";
        int rowCount     = this.jdbcTemplate.queryForObject(sql_count, new Object[] { id }, Integer.class);

        if (rowCount == 0)
            return success;

        success = true;
        String sql   = "DELETE FROM tracks WHERE id = ?";
        this.jdbcTemplate.update(sql, id);

        return success;
    }

}
