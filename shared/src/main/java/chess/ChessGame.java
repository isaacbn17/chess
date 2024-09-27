package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

    public class ChessGame {
        private TeamColor teamTurn = TeamColor.WHITE;
        private ChessBoard board = new ChessBoard();
        public ChessGame() {
        }

        public TeamColor getTeamTurn() {
            return teamTurn;
        }
        public void setTeamTurn(TeamColor team) {
            this.teamTurn = team;
        }

        public enum TeamColor {
            WHITE,
            BLACK
        }

        /**
         * Gets a valid moves for a piece at the given location
         *
         * @param startPosition the piece to get valid moves for
         * @return Set of valid moves for requested piece, or null if no piece at
         * startPosition
         */
        public Collection<ChessMove> validMoves(ChessPosition startPosition) {
            ChessPiece piece = board.getPiece(startPosition);
            ChessGame.TeamColor color = piece.getTeamColor();

            Collection<ChessMove> potentialMoves = piece.pieceMoves(board, startPosition);
            for (ChessMove move : potentialMoves) {

                pretendToMakeMove(move);
                if (isInCheck(color)) {
                    reversePretendedMove(move);
                    potentialMoves.remove(move);
                }
            }

            return potentialMoves;
        }

        private void reversePretendedMove(ChessMove move) {
            ChessPiece piece = board.getPiece(move.getEndPosition());
            board.addPiece(move.getStartPosition(), piece);
            board.removePiece(move.getEndPosition(), piece);
        }

        private void pretendToMakeMove(ChessMove move) {
            ChessPiece piece = board.getPiece(move.getStartPosition());
            board.addPiece(move.getEndPosition(), piece);
            board.removePiece(move.getStartPosition(), piece);
        }


        public void makeMove(ChessMove move) throws InvalidMoveException {
            if (board.getPiece(move.getStartPosition()) == null) {
                throw new InvalidMoveException("Invalid move");
            }
            Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
            ChessGame.TeamColor color = board.getPiece(move.getStartPosition()).getTeamColor();
            if (getTeamTurn() == color && validMoves.contains(move)) {
                ChessPiece piece = board.getPiece(move.getStartPosition());
                board.addPiece(move.getEndPosition(), piece);
                board.removePiece(move.getStartPosition(), piece);
            }
            else {
                throw new InvalidMoveException("Invalid move");
            }
        }

        public boolean isInCheck(TeamColor teamColor) {
            ChessPosition kingPosition = findKing(teamColor);
            HashSet<ChessMove> moves = getOpposingTeamMoves(teamColor);
            for (ChessMove move : moves) {
                System.out.print(move.getEndPosition());
                if (move.getEndPosition() == kingPosition) {
                    return true;
                }
            }
            return false;
        }

        private HashSet<ChessMove> getOpposingTeamMoves(TeamColor teamColor) {
            HashSet<ChessMove> moves = new HashSet<>();
            for (int i=1; i<9; i++) {
                for (int j=1; j<9; j++) {
                    ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                    if (piece != null && piece.getTeamColor() != teamColor) {
                        moves.addAll(piece.pieceMoves(board, new ChessPosition(i, j)));
                    }
                }
            }
            return moves;
        }
        private ChessPosition findKing(TeamColor teamColor) {
            for (int i=1; i<9; i++) {
                for (int j=1; j<9; j++) {
                    ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                    if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING &&
                            board.getPiece(new ChessPosition(i, j)).getTeamColor() == teamColor) {
                        return new ChessPosition(i, j);
                    }
                }
            }
            return null;
        }

        /**
         * Determines if the given team is in checkmate
         *
         * @param teamColor which team to check for checkmate
         * @return True if the specified team is in checkmate
         */
        public boolean isInCheckmate(TeamColor teamColor) { throw new RuntimeException("Not implemented");
        }

        /**
         * Determines if the given team is in stalemate, which here is defined as having
         * no valid moves
         *
         * @param teamColor which team to check for stalemate
         * @return True if the specified team is in stalemate, otherwise false
         */
        public boolean isInStalemate(TeamColor teamColor) {
            ChessPosition kingPosition = findKing(teamColor);
            ChessPiece king = board.getPiece(kingPosition);
            Collection<ChessMove> kingMoves = king.pieceMoves(board, kingPosition);
            HashSet<ChessMove> opposingMoves = getOpposingTeamMoves(teamColor);
            for (ChessMove kingMove : kingMoves) {
                if (! opposingMoves.contains(kingMove)) {
                    return false;
                }
            }
            return true;
        }

        public void setBoard(ChessBoard board) {
            this.board = board;
        }
        public ChessBoard getBoard() {
            return board;
        }
    }
