package com.example.cyril.applicationwifi;

class Mesure {

    public static char[] get;
    int x,y;
    double d;

    public Mesure(int x, int y, double d) {
        this.x = x;
        this.y = y;
        this.d = d;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getD() {
        return d;
    }
}
