public class Bishop extends Piece {
    public Bishop(int x, int y, Side side, Board board) {
        // TODO: Call super constructor
    }

    @Override
    public boolean canMove(int destX, int destY) {

        //TODO: Check piecerules.md for the movement rule for this piece :)
        return false;
    }

    @Override
    public String getSymbol() {
        return this.getSide() == Side.BLACK ? "♝" : "♗";
    }
}
