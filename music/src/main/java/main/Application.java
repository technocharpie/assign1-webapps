package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import dao.*;
import model.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {
        AlbumDAO albumDAO = new AlbumDAO(jdbcTemplate);
        TrackDAO trackDAO = new TrackDAO(jdbcTemplate);


        //'albums' and 'tracks' tables creation
        log.info("\n\n\n");
        log.info("Creating tables");
        jdbcTemplate.execute("DROP TABLE IF EXISTS tracks");
        jdbcTemplate.execute("CREATE TABLE tracks(" +
                "id SERIAL, title VARCHAR(255), album INT)");
        jdbcTemplate.execute("DROP TABLE IF EXISTS albums");
        jdbcTemplate.execute("CREATE TABLE albums(" +
                "id INT, title VARCHAR(255))");


        //createAlbum() createTrack() tests
        log.info("\n\n\n");
        log.info("Creating tracks for Album1 (id = 42):");
        trackDAO.createTrack(new Track(1, "Track 1", 42));
        trackDAO.createTrack(new Track(2, "Track 2", 42));
        trackDAO.createTrack(new Track (3, "Track 3", 42));
        trackDAO.createTrack(new Track(4, "Track 1", 23));
        trackDAO.createTrack(new Track(5, "Track 2", 23));
        trackDAO.createTrack(new Track (6, "Track 3", 23));
        log.info("Retrieving tracks from album id = 42:");
        trackDAO.getTracksByAlbumId(42).forEach(track -> log.info(track.toString()));
    
        log.info("Creating album id 42 and album id 23:");
        albumDAO.createAlbum(new Album (42, "Album 1"));
        albumDAO.createAlbum(new Album (23, "Album 2"));
        log.info("Retrieving all albums:");
        albumDAO.getAllAlbums().forEach(album -> log.info(album.toString()));
        

        //getAlbum() getTrack() tests
        log.info("\n\n\n");
        log.info("Retrieving properties from album id = 42:");
        log.info(albumDAO.getAlbum(42).toString());
        log.info("Retrieving properties track id = 2:");
        log.info(trackDAO.getTrack(2).toString());

        //getAllTracks() test
        log.info("\n\n\n");
        log.info("Retrieving all tracks:");
        trackDAO.getAllTracks().forEach(track -> log.info(track.toString()));


        //updateTrack() updateAlbum() tests
        log.info("\n\n\n");
        log.info("Updating track id = 3 with title = 'Pop Song', albumId = 87");
        trackDAO.updateTrack(new Track (3, "Pop Song", 87));
        log.info("Retrieving properties for track id = 3");
        log.info(trackDAO.getTrack(3).toString());
        log.info("Updating track id = 7 with title = 'Pop Song2', albumId = 87");
        trackDAO.updateTrack(new Track (7, "Pop Song2", 87));
        log.info("Retrieving properties for track id = 7");
        log.info(trackDAO.getTrack(7).toString());
        log.info("Retrieving tracks from album id = 42:");
        trackDAO.getTracksByAlbumId(42).forEach(track -> log.info(track.toString()));
        log.info("Retrieving tracks from album id = 87:");
        trackDAO.getTracksByAlbumId(87).forEach(track -> log.info(track.toString()));
        log.info("Updating album id = 23 with title = 'Emmy-winning'");
        albumDAO.updateAlbum(new Album (23, "Emmy-winning"));
        log.info("Retrieving properties for album id = 23");
        log.info(albumDAO.getAlbum(23).toString());


        //deleteTrack() deleteAlbum() tests
        log.info("\n\n\n");
        log.info("Deleting tracks id = 3, id = 7");
        trackDAO.deleteTrack(new Track(3));
        trackDAO.deleteTrack(new Track(7));
        log.info("Retrieving tracks from album id = 87:");
        trackDAO.getTracksByAlbumId(87).forEach(track -> log.info(track.toString()));
        log.info("Deleting albums id = 23, id = 42");
        albumDAO.deleteAlbum(new Album(23, ""));
        albumDAO.deleteAlbum(new Album(42, ""));
        log.info("Retrieving all albums:");
        albumDAO.getAllAlbums().forEach(album -> log.info(album.toString()));
    }
}
