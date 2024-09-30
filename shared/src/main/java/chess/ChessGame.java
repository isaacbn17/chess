package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;

    public class ChessGame {
        private TeamColor teamTurn = TeamColor.WHITE;
        private ChessBoard board = new ChessBoard();
        public ChessGame() {
            board.resetBoard();
        }

        public TeamColor getTeamTurn() {
            return teamTurn;
        }
        public void setTeamTurn(TeamColor team) {
            this.teamTurn = team;
        }
        private TeamColor getOppositeColor(TeamColor color) {
            if (color == TeamColor.BLACK) {
                return TeamColor.WHITE;
            }
            else {
                return TeamColor.BLACK;
            }
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
            TeamColor color = piece.getTeamColor();

            Collection<ChessMove> potentialMoves = piece.pieceMoves(board, startPosition);
            HashSet<ChessMove> validMoves = new HashSet<>();
            for (ChessMove move : potentialMoves) {

                ChessPiece disappearingPiece = pretendToMakeMove(move);
                if (isInCheck(color)) {
                    reversePretendedMove(move, disappearingPiece);
                }
                else {
                    reversePretendedMove(move, disappearingPiece);
                    validMoves.add(move);
                }
            }

            return validMoves;
        }
        private void reversePretendedMove(ChessMove move, ChessPiece disappearingPiece) {
            ChessPiece piece = board.getPiece(move.getEndPosition());
            board.addPiece(move.getStartPosition(), piece);
            board.removePiece(move.getEndPosition(), piece);
            if (disappearingPiece != null) {
                board.addPiece(move.getEndPosition(), disappearingPiece);
            }
        }
        private ChessPiece pretendToMakeMove(ChessMove move) {
            ChessPiece piece = board.getPiece(move.getStartPosition());
            if (board.getPiece(move.getEndPosition()) == null) {
                board.addPiece(move.getEndPosition(), piece);
                board.removePiece(move.getStartPosition(), piece);
                return null;
            }
            else {
                ChessPiece disappearingPiece = board.getPiece(move.getEndPosition());
                board.addPiece(move.getEndPosition(), piece);
                board.removePiece(move.getStartPosition(), piece);
                return disappearingPiece;
            }
        }


        public void makeMove(ChessMove move) throws InvalidMoveException {
            if (board.getPiece(move.getStartPosition()) == null) {
                throw new InvalidMoveException("Invalid move");
            }
            Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
            if (validMoves == null) {
                throw new InvalidMoveException("Invalid move");
            }

            TeamColor color = board.getPiece(move.getStartPosition()).getTeamColor();
            if (getTeamTurn() == color && validMoves.contains(move)) {
                ChessPiece piece = board.getPiece(move.getStartPosition());
                if (move.getPromotionPiece() != null) {
                    ChessPiece promotionPiece = new ChessPiece(color, move.getPromotionPiece());
                    board.addPiece(move.getEndPosition(), promotionPiece);
                    board.removePiece(move.getStartPosition(), piece);
                    setTeamTurn(getOppositeColor(color));
//                    adjustPiecePosition(color, move);
                }
                else {
                    board.addPiece(move.getEndPosition(), piece);
                    board.removePiece(move.getStartPosition(), piece);
                    setTeamTurn(getOppositeColor(color));
//                    adjustPiecePosition(color, move);
                }
            }
            else {
                throw new InvalidMoveException("Invalid move");
            }
        }

        private void adjustPiecePosition(TeamColor color, ChessMove move) {
            if (color == TeamColor.WHITE) {
                board.getWhitePositions().add(move.getEndPosition());
                board.getWhitePositions().remove(move.getStartPosition());
            }
            else {
                board.getBlackPositions().add(move.getEndPosition());
                board.getBlackPositions().remove(move.getStartPosition());
            }
        }

        public boolean isInCheck(TeamColor teamColor) {
              ChessPosition kingPosition = findKing(teamColor);
              HashSet<ChessMove> moves = getOpposingTeamMoves(teamColor);
              for (ChessMove move : moves) {
                  if (move.getEndPosition().equals(kingPosition)) {
                      return true;
                  }
              }
              return false;
          }

        private HashSet<ChessMove> getOpposingTeamMoves(TeamColor teamColor) {
            HashSet<ChessMove> moves = new HashSet<>();
            HashSet<ChessPosition> opposingTeamPositions;
            if (teamColor == TeamColor.BLACK) {
                opposingTeamPositions = getTeamPositions(TeamColor.WHITE);
            }
            else {
                opposingTeamPositions = getTeamPositions(TeamColor.BLACK);
            }
            for (ChessPosition position : opposingTeamPositions) {
                ChessPiece piece = board.getPiece(position);
                if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                    addPawnAttackMoves(position.getRow(), position.getColumn(), piece, moves);
                }
                else {
                    moves.addAll(piece.pieceMoves(board, position));
                }
            }
//            for (int i=1; i<9; i++) {
//                for (int j=1; j<9; j++) {
//                    ChessPiece piece = board.getPiece(new ChessPosition(i, j));
//                    if (piece != null && piece.getTeamColor() != teamColor) {
//                        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
//                            addPawnAttackMoves(i, j, piece, moves);
//                        }
//                        else {
//                            moves.addAll(piece.pieceMoves(board, new ChessPosition(i, j)));
//                        }
//                    }
//                }
//            }
            return moves;
        }
        private void addPawnAttackMoves(int row, int col, ChessPiece piece, HashSet<ChessMove> moves) {
            if (piece.getTeamColor() == TeamColor.WHITE) {
                moves.addAll(piece.captureWhite(board, new ChessPosition(row, col)));
            }
            else {
                moves.addAll(piece.captureBlack(board, new ChessPosition(row, col)));
            }
        }

        private ChessPosition findKing(TeamColor teamColor) {
//          if (teamColor == TeamColor.WHITE) {
//            for (ChessPosition position : board.getWhitePositions()) {
//              ChessPiece piece = board.getPiece(position);
//              if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING) {
//                return position;
//              }
//            }
//          }
//          else {
//            for (ChessPosition position : board.getBlackPositions()) {
//              ChessPiece piece = board.getPiece(position);
//              if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING) {
//                return position;
//              }
//            }
//          }
            for (int i=1; i<9; i++) {
                for (int j=1; j<9; j++) {
                    ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                    if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING
                            && piece.getTeamColor() == teamColor) {
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
        public boolean isInCheckmate(TeamColor teamColor) {
            if (isInCheck(teamColor)) {
                HashSet<ChessPosition> teamPositions = getTeamPositions(teamColor);
                for (ChessPosition position : teamPositions) {
                    if (! validMoves(position).isEmpty()) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        private HashSet<ChessPosition> getTeamPositions(TeamColor teamColor) {
            HashSet<ChessPosition> teamPositions = new HashSet<>();
            for (int i=1; i<9; i++) {
                for (int j=1; j<9; j++) {
                    ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                    if (piece != null && piece.getTeamColor() == teamColor) {
                        teamPositions.add(new ChessPosition(i, j));
                    }
                }
            }
            return teamPositions;
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
            if (kingPosition == null) { return false; }
            for (ChessPosition position : getTeamPositions(teamColor)) {
                ChessPiece piece = board.getPiece(position);
                if (piece.getPieceType() != ChessPiece.PieceType.KING) {
                    if (! piece.pieceMoves(board, position).isEmpty()) {
                        return false;
                    }
                }
            }
            ChessPiece king = board.getPiece(kingPosition);
            Collection<ChessMove> kingMoves = king.pieceMoves(board, kingPosition);

            HashSet<ChessMove> opposingMoves = getOpposingTeamMoves(teamColor);
            HashSet<ChessPosition> opposingEndMoves = new HashSet<>();
            for (ChessMove move : opposingMoves) {
                opposingEndMoves.add(move.getEndPosition());
            }
            for (ChessMove kingMove : kingMoves) {
                ChessPosition kingDestination = kingMove.getEndPosition();
                if (! opposingEndMoves.contains(kingDestination)) {
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
