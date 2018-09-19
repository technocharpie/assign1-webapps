package dao;

import org.springframework.jdbc.core.JdbcTemplate;

import model.*;
import java.util.Collection;
import java.util.ArrayList;

public class AlbumDAO 
{
    private JdbcTemplate jdbcTemplate;

    public AlbumDAO(JdbcTemplate jdbcTemp) 
    {
        this.jdbcTemplate = jdbcTemp;
    }

    public Album createAlbum(Album album) 
    {
        if (album != null)
        {
            String sql = "INSERT INTO albums VALUES (?, ?)";
            int id = album.getId();
            String title = album.getTitle();
            this.jdbcTemplate.update(sql, id, title);
        }

        return album;
    }

    public Album getAlbum(int id)
    {
        Album album      = new Album(id, "");
        String sql_count = "SELECT COUNT(*) FROM albums WHERE id = ?";
        int rowCount     = this.jdbcTemplate.queryForObject(sql_count, new Object[] { id }, Integer.class);
        

        if (rowCount > 0)
        {
            String sql = "SELECT title FROM albums WHERE id = ?";
            String title = this.jdbcTemplate.queryForObject(sql, new Object[] { id }, String.class);
            album.setTitle(title);
            album.setTracks((new TrackDAO(jdbcTemplate)).getTracksByAlbumId(id));
        }

        return album;
    }

    public Collection<Album> getAllAlbums()
    {
        Collection<Album> albums = new ArrayList<Album>();
        this.jdbcTemplate.query(
                "SELECT * FROM albums", new Object[] { },
                (rs, rowNum) -> this.getAlbum(rs.getInt("id"))
        ).forEach(album -> albums.add(album));

        return albums;
    }

    public Album updateAlbum(Album album)
    {
        int id           = album.getId();
        String title     = album.getTitle();

        String sql_count = "SELECT COUNT(*) FROM albums WHERE id = ?";
        int rowCount     = this.jdbcTemplate.queryForObject(sql_count, new Object[] { id }, Integer.class);
        

        if (rowCount > 0)
        {
            String sql = "UPDATE albums SET title = ? WHERE id = ?";
            this.jdbcTemplate.update(sql, title, id);
        }
        else
        {
            album = createAlbum(album);
        }

        return album;
    }

    public boolean deleteAlbum(Album album)
    {
        boolean success  = false;
        int id           = album.getId();
        String sql_count = "SELECT COUNT(*) FROM albums WHERE id = ?";
        int rowCount     = this.jdbcTemplate.queryForObject(sql_count, new Object[] { id }, Integer.class);

        if (rowCount == 0)
            return success;

        success = true;
        String sql   = "DELETE FROM albums WHERE id = ?";
        this.jdbcTemplate.update(sql, id);

        return success;
    }
}
