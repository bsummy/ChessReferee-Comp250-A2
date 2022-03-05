import java.util.List;
import java.util.Stack;

public class Game {
    Board b;
    Stack<String> moveHistory = new Stack<String>();
    Side currentTurn;

    public Game(){
        // todo: write a constructor that initializes the game with a new board
        b = new Board();
        currentTurn = Side.WHITE;
        // hint: you are also responsible for tracking whose turn it is
    }

    public static String getName() {
        return "Checkers?? idk bro";
    }

    public boolean canMove(int x, int y, int destX, int destY, Side s) {
       /* TODO write a method that checks if a piece at coordinates x,y can move to coordinates destX,destY
       Conditions for false:
       - Origin or destination coordinates are outside the board
       - Piece at origin is null
       - If source and destination coordinates are the same
       - Piece at origin is not of the same side as s
            - You can check this using Piece.getSide()
       - Piece cannot move to the destination by piece movement rules
            - You should check this using Piece.canMove(destX, destY)
       - Destination has a piece of the same Side as the player
       - piece must move "through" a piece to get from (x,y) to (destX,destY) (use isVisible())
            - The knight is the exception to this rule. The knight can hop "over" pieces, so be sure to check for this.
          */

        Piece piecePlace;
        if (((x >= 0 && x <= 8) && (y >= 0 && y <= 8)) && b.get(x, y) != null) {
            //if piece at origin is not null (to simplify calls)
            piecePlace = b.get(x, y);
        } else {
            //if no piece at origin
            return false;
        }
        if (!(destX >= 0 && destX < 8) && !(destY >= 0 && destY < 8)) {
            //if the piece destination is outside the board
            return false;
        } else if (!(x >= 0 && x < 8) && !(y >= 0 && y < 8)){
            //if the starting location is outside of the board
            return false;
        } else if ((destX == x) && (destY == y)) {
            //if source and destination and coords are the same
            return false;
        } else if (piecePlace.getSide() != s){
            //piece at origin is not of the same side
            return false;
        } else if (!(piecePlace.canMove(destX, destY))){
            //piece can't move to the destination
            return false;
        } else if (b.get(destX, destY) != null && (b.get(destX, destY)).getSide() == s){
            //destination has a piece of the same side
            return false;
        } else if ((!(piecePlace instanceof Knight)) && !(this.isVisible(x, y, destX, destY))){
            //if there's things in the path, but exempts a knight
            //checks for a knight with a move only the knight can make
            return false;
        }
        return true;
    }

    /**
     * This method is provided to you in order to help with canMove().
     *
     * In chess, no piece except the knight can "move through" or "hop over" a piece that's in its way
     *
     * This method checks that there are no pieces along the path from (x,y) to (destX,destY).
     * Note that a "path" is only defined if (x,y) and (destX,destY) are on the same row, column, or diagonal.
     * If the requested path is undefined, the method throws an exception.
     *
     * If the path is defined and no piece is found along the path, the method returns true.
     *
     * Don't worry about how this method works or tests and edge cases for it, we will
     * grade you assuming you keep it exactly as provided and use it as a part of your
     * canMove() method.
     */
    private boolean isVisible(int x, int y, int destX, int destY) {
        int diffX = destX - x;
        int diffY = destY - y;

        boolean validCheck = (diffX == 0 || diffY == 0) || (Math.abs(diffX) == Math.abs(diffY));
        if (!validCheck) {
            try {
                throw new Exception("The 'path' between the squares (" + x + ", " + y + ") and (" + destX + ", " + destY +") is undefined");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Diagonal
        if (Math.abs(diffX) == Math.abs(diffY) && Math.abs(diffX) > 1) {
            //Determine direction of move
            int dirX = diffX > 0 ? 1 : -1;
            int dirY = diffY > 0 ? 1 : -1;
            for (int i = x + dirX, j = y + dirY; i != destX && j != destY; i += dirX, j += dirY) {
                if (b.get(i, j) != null) {
                    return false;
                }
            }
        }

        //Linear
        if ((diffX == 0 && Math.abs(diffY) > 1) || (diffY == 0 && Math.abs(diffX) > 1)) {
            if (diffX == 0) {
                int dirY = diffY > 0 ? 1 : -1;
                for (int j = y + dirY; j != destY; j += dirY) {
                    if (b.get(x, j) != null) {
                        return false;
                    }
                }
            } else {
                int dirX = diffX > 0 ? 1 : -1;
                for (int i = x + dirX; i != destX; i += dirX) {
                    if (b.get(i, y) != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void appendCheckToHistory(Side side){
        moveHistory.push(side.toString()+ " is in check");
    }

    private void appendWinToHistory(Side side){
        moveHistory.push(side.toString()+ " has won");
    }

    private void appendMoveToHistory(int x, int y, int destX, int destY, Side side){
        Piece toMove = b.get(x,y);
        Piece toCapture = b.get(destX, destY);
        if(toCapture == null){
            moveHistory.push(side.toString() + toMove.getSymbol() + " at "+ x + ", " + y + " to " + destX + ", " + destY);
        }else{
            moveHistory.push(side.toString() + toMove.getSymbol() + " at " + x + ", " + y + " captures " + toCapture.getSymbol() + " at " + destX + ", " + destY);
        }
    }


    /**
     * This method takes as input the coordinates of the piece to move and of the destination to move to.
     *
     * It returns true if the move is valid and false otherwise.
     *
     * Conditions for a valid move are determined in canMove()
     *
     * Upon a successful move, this method also:
     * - updates the board to reflect the move
     * - tracks the move in game history
     *   - also tracks in history if the move was:
     *     - a capture
     *     - result in check EDIT: for either side
     *     - EDIT: also if it results in a win / king capture
     * - updates the current player's turn
     *
     *  x The x coordinate of the piece to move
     *  y The y coordinate of the piece to move
     *  destX The x coordinate of the destination to move to
     *  destY The y coordinate of the destination to move to
     */
    public boolean move(int x, int y, int destX, int destY){
        // still a problem with when the move history says that there is a check made
        // checking for the check on the move after it's needed
        Piece piece;
        King king;
        Side opponentSide = Side.negate(this.currentTurn);


        if (!(this.canMove(x,y,destX, destY, this.currentTurn))){
            return false;
        }
        if (x == destX && y == destY){
            return false;
        }

        appendMoveToHistory(x, y, destX, destY, this.currentTurn);
        piece = b.get(x, y);
        piece.move(destX, destY);

        king = b.getKing(opponentSide);

        if (isInCheck(this.currentTurn)){
            appendCheckToHistory(this.currentTurn);
        }
        if (isInCheck(opponentSide)){
            appendCheckToHistory(opponentSide);
        }

        if (king == null){
            appendWinToHistory(this.currentTurn);

        }
        this.currentTurn = opponentSide;
        return true;
    }

    /**
     * Return true if the King of Side side can be captured by any of
     * the opponent's pieces.
     *
     */
    public boolean isInCheck(Side side) {
        King king = b.getKing(side);
        if (king == null){
            return false;
        }
        Side opponentSide = Side.negate(side);
        List<Piece> listPiece = b.getPieces(opponentSide);
        for (Piece piece : listPiece) {
            if (this.canMove(piece.x, piece.y, king.x, king.y, opponentSide)){
                return true;
            }
        }
        return false;
    }

    /**
     * Ensures that the game is in the exact same state as a new game
     */
    public void reset(){
        while(!moveHistory.empty()){
            System.out.println(moveHistory.pop());
        }
        b.fillBoard();
        currentTurn = Side.WHITE;
    }


    public static void main(String[] args){
        Board b = new Board();
        System.out.println(b);
    }

    /**
     * Return an array of Strings containing every successful move made during the game,
     * and every time a move resulted in check.
     */
    public String[] moveHistory(){
        String[] out = new String[moveHistory.size()];
        for(int i = 0; i < moveHistory.size(); i++){
            out[i] = moveHistory.get(i);
        }
        return out;
    }

    public Side getCurrentTurn(){
        return currentTurn;
    }
}
