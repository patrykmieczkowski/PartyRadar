package com.mieczkowskidev.partyradar.Objects;

/**
 * Created by Patryk Mieczkowski on 2015-11-11.
 */
public class Event {

    private int id;
    private String user;
    private String photo;
    private String description;
    private Double lat;
    private Double lon;
    private String created;
    private String modified;

    public Event(int id, String user, String photo, String description, Double lat, Double lon, String created, String modified) {
        this.id = id;
        this.user = user;
        this.photo = photo;
        this.description = description;
        this.lat = lat;
        this.lon = lon;
        this.created = created;
        this.modified = modified;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", photo='" + photo + '\'' +
                ", description='" + description + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", created='" + created + '\'' +
                ", modified='" + modified + '\'' +
                '}';
    }
}
