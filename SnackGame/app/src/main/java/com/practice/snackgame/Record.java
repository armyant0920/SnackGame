package com.practice.snackgame;

public class Record {
    private String PlayerName;//玩家名稱
    private String PlayerScore;//玩家紀錄
    private String Snack;//玩家那條蛇->用字串存起來,再想辦法分解成陣列
    private String CoinPoint;//同上,金幣位置也記起來,只有一個XY相對簡單
    private int FaceDirection;//紀錄目前前進方向

    public Record(String playerName, String playerScore, String snack, String coinPoint, int faceDirection) {
        PlayerName = playerName;
        PlayerScore = playerScore;
        Snack = snack;
        CoinPoint = coinPoint;
        FaceDirection = faceDirection;
    }

    public String getPlayerName() {
        return PlayerName;
    }

    public void setPlayerName(String playerName) {
        PlayerName = playerName;
    }

    public String getPlayerScore() {
        return PlayerScore;
    }

    public void setPlayerRecord(String playerRecord) {
        PlayerScore = playerRecord;
    }

    public String getSnack() {
        return Snack;
    }

    public void setSnack(String snack) {
        Snack = snack;
    }

    public String getCoinPoint() {
        return CoinPoint;
    }

    public void setCoinPoint(String coinPoint) {
        CoinPoint = coinPoint;
    }

    public int getFaceDirection() {
        return FaceDirection;
    }

    public void setFaceDirection(int faceDirection) {
        FaceDirection = faceDirection;
    }
}
