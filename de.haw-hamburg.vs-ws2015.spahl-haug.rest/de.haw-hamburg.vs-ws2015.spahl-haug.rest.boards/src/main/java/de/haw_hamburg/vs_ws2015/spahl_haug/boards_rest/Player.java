package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;


public class Player {
    private String id;
    private Place place;
    private int position;

    public Player(String id) {
        this.id = id;
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
}
