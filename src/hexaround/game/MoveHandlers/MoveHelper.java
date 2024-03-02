package hexaround.game.MoveHandlers;

import java.awt.*;

/*
* This class is used to help capture data for a potential walking based move, and is used in Walk and Run handlers
* */
public class MoveHelper {

    public MoveHelper() {
    }
    public MoveHelper(Point frp, Point tp, int sl) {
        this.fromP = frp;
        this.toP = tp;
        this.stepsLeft = sl;
    }
    public Point fromP;
    public Point toP;
    public int stepsLeft;
}
