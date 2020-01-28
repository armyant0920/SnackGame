package com.practice.snackgame;

public class SnackBody {
    private int PointY;
    private int PointX;

    public SnackBody(int pointX,int pointY) {
        PointY = pointY;
        PointX = pointX;
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
