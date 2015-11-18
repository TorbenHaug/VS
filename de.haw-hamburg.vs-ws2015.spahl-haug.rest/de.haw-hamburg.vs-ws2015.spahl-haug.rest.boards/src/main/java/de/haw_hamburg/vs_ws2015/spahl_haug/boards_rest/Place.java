package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;


public enum Place {
    Los(0),
    Einkommensteuer(1);

    private int position;

    Place(int id){
        this.position = id;
    }

    public int getPosition() {
        return this.position;
    }
}
