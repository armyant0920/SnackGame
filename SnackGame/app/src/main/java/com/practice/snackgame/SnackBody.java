package com.practice.snackgame;

public class SnackBody {
    private int PointX;
    private int PointY;


    public SnackBody(int pointX,int pointY) {
        PointX = pointX;
        PointY = pointY;

    }

    public int getPointX() {
        return PointX;
    }

    public void setPointX(int pointX) {
        PointX = pointX;
    }

    public int getPointY() {
        return PointY;
    }

    public void setPointY(int pointY) {
        PointY = pointY;
    }
}
