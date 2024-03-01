package hexaround.game.MoveHandlers;

import java.awt.*;

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
