public class Pawn extends Piece {
    public boolean firstMove;

    public Pawn(int x, int y, Side side, Board board) {
        // TODO: Call super constructor
        super(x, y, side, board);
        firstMove = true;
    }

    @Override
    public boolean canMove(int destX, int destY) {
        //TODO: Check piecerules.md for the movement rule for this piece :)
        Piece captureLeft = null;
        Piece captureRight = null;
        Piece centerOneBlock = null;
        Piece centerTwoBlock = null;

        boolean twoMove;
        boolean oneMove;
        boolean captureLeftAbility;
        boolean captureRightAbility;
        boolean centerOneBlockAbility;
        boolean centerTwoBlockAbility;
        boolean checkXBoundsHigh = this.x + 1 < 8;
        boolean checkXBoundsLow = this.x - 1 >= 0;
        boolean checkYBoundsLow = this.y - 1 >= 0;
        boolean checkYBoundsHigh = this.y + 1 < 8;

        if (this.getSide() == Side.WHITE && this.y == 6){
            this.firstMove = true;
        } else if (this.getSide() == Side.BLACK && this.y == 1)
            this.firstMove = true;


        if (this.getSide() == Side.WHITE) {
            if ((checkYBoundsLow) && (board.get(this.x, this.y - 1) instanceof Piece)){
                centerOneBlock = board.get(this.x, this.y - 1);
                if (centerOneBlock.getSide() == this.getSide()){
                    centerOneBlock = null;
                }
            }

            if (firstMove && (board.get(this.x, this.y - 2) instanceof Piece)){
                centerTwoBlock = board.get(this.x, this.y - 2);
                if (centerTwoBlock.getSide() == this.getSide()){
                    centerTwoBlock = null;
                }
            }


            if ((checkXBoundsLow && checkYBoundsLow) && (board.get(this.x - 1, this.y - 1) instanceof Piece)){
                captureLeft = board.get(this.x - 1, this.y - 1);
                if (captureLeft.getSide() == this.getSide()){
                    captureLeft = null;
                }
            }

            if ((checkXBoundsHigh && checkYBoundsLow) && (board.get(this.x + 1, this.y - 1) instanceof Piece)){
                captureRight= board.get(this.x + 1, this.y - 1);
                if (captureRight.getSide() == this.getSide()){
                    captureRight = null;
                }
            }
            centerOneBlockAbility = (centerOneBlock == null);
            centerTwoBlockAbility = (centerTwoBlock == null);
            twoMove = (this.x == destX && this.y - 2 == destY && centerTwoBlockAbility);
            oneMove = (this.x == destX && this.y - 1 == destY && centerOneBlockAbility);
            captureLeftAbility = ((this.x - 1 == destX) && (this.y - 1 == destY));
            captureRightAbility = ((this.x + 1 == destX) && (this.y - 1 == destY));


        } else { //checking for black
            if (checkYBoundsHigh && (board.get(this.x, this.y + 1) instanceof Piece)){
                centerOneBlock = board.get(this.x, this.y + 1);
                if (centerOneBlock.getSide() == this.getSide()){
                    centerOneBlock = null;
                }
            }

            if (firstMove && (board.get(this.x, this.y + 2) instanceof Piece)){
                centerTwoBlock = board.get(this.x, this.y + 2);
                if (centerTwoBlock.getSide() == this.getSide()){
                    centerTwoBlock = null;
                }
            }

            if ((checkXBoundsHigh && checkYBoundsHigh) && (board.get(this.x + 1, this.y + 1) instanceof Piece)){
                captureLeft = board.get(this.x + 1, this.y + 1);
                if (captureLeft.getSide() == this.getSide()){
                    captureLeft = null;
                }
            }
            if ((checkXBoundsLow && checkYBoundsHigh) && board.get(this.x - 1, this.y + 1) instanceof Piece){
                captureRight= board.get(this.x - 1, this.y + 1);
                if (captureRight.getSide() == this.getSide()){
                    captureRight= null;
                }
            }
            centerOneBlockAbility = (centerOneBlock == null);
            centerTwoBlockAbility = (centerTwoBlock == null);
            twoMove = (this.x == destX && this.y + 2 == destY && centerTwoBlockAbility);
            oneMove = (this.x == destX && this.y + 1 == destY && centerOneBlockAbility);
            captureLeftAbility = ((this.x + 1 == destX) && (this.y + 1 == destY));
            captureRightAbility = ((this.x - 1 == destX) && (this.y + 1 == destY));
            }

        if (firstMove) {
            this.firstMove = false;
            if (captureLeft != null & captureRight != null){
                return (twoMove||oneMove||captureRightAbility||captureLeftAbility);
            } else if (captureLeft != null){
                return (twoMove||oneMove||captureLeftAbility);
            } else if (captureRight != null){
                return (twoMove||oneMove||captureRightAbility);
            } else {
                return (twoMove||oneMove);
            }
        } else {
            if (captureLeft != null & captureRight != null){
                return (oneMove||captureRightAbility||captureLeftAbility);
            } else if (captureLeft != null){
                return (oneMove||captureLeftAbility);
            } else if (captureRight != null){
                return (oneMove||captureRightAbility);
            } else {
                return (oneMove);
            }
        }
    }

    @Override
    public String getSymbol() {
        return this.getSide() == Side.BLACK ? "♟" : "♙";
    }
}
