package dao;

import org.springframework.jdbc.core.JdbcTemplate;

import model.*;
import java.util.Collection;
import java.util.ArrayList;

public class AlbumDAO {
    private JdbcTemplate jdbcTemplate;

    public AlbumDAO(JdbcTemplate jdbcTemp) {
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

    public Album getAlbum(int id){
        Album album = new Album(id, "");
        String sql = "SELECT title FROM albums WHERE id = ?";
        String title = this.jdbcTemplate.queryForObject(sql, new Object[] {album.getId()}, String.class);
        if (title != null)
        {
            album.setTitle(title);
        }
        else
        {
            album.setTitle("[no title]");
        }

        return album;
    }

    public Collection<Album> getAllAlbums(){
        Collection<Album> albums = new ArrayList<Album>();
        this.jdbcTemplate.query(
                "SELECT * FROM albums", new Object[] { },
                (rs, rowNum) -> new Album(rs.getInt("id"), rs.getString("title"))
        ).forEach(album -> albums.add(album));

        return albums;
    }

    public Album updateAlbum(Album album){
        //TODO: Implement this CRUD function
        return album;
    }

    public boolean deleteAlbum(Album album){
        boolean success = false;
        //TODO: Implement this CRUD function
        return success;
    }



}
