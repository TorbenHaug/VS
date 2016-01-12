package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;


public class Player {
    private String id;
    private String uri;
    private Place place;
    private int position;

    public Player(String id, String uri) {
        this.id = id;
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public Place getPlace() {
        return place;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                ", place=" + place +
                ", position=" + position +
                '}';
    }

    public String getUri() {
        return uri;
    }
}
