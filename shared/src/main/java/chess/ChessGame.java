package chess;

import java.util.Collection;
import java.util.HashSet;

    public class ChessGame {
        private boolean gameActive = true;
        private TeamColor teamTurn = TeamColor.WHITE;
        private ChessBoard board = new ChessBoard();
        public ChessGame() {
            board.resetBoard();
        }

        public void endGame() {
            gameActive = false;
        }
        public boolean gameOver() {
            return !gameActive;
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
            board.removePiece(move.getEndPosition());
            if (disappearingPiece != null) {
                board.addPiece(move.getEndPosition(), disappearingPiece);
            }
        }
        private ChessPiece pretendToMakeMove(ChessMove move) {
            ChessPiece piece = board.getPiece(move.getStartPosition());
            if (board.getPiece(move.getEndPosition()) == null) {
                board.addPiece(move.getEndPosition(), piece);
                board.removePiece(move.getStartPosition());
                return null;
            }
            else {
                ChessPiece disappearingPiece = board.getPiece(move.getEndPosition());
                board.addPiece(move.getEndPosition(), piece);
                board.removePiece(move.getStartPosition());
                return disappearingPiece;
            }
        }
        public void makeMove(ChessMove move) throws InvalidMoveException {
            if (! gameActive) {
                throw new InvalidMoveException("The game has ended");
            }
            if (board.getPiece(move.getStartPosition()) == null) {
                throw new InvalidMoveException("Invalid move");
            }
            TeamColor color = board.getPiece(move.getStartPosition()).getTeamColor();
            if (getTeamTurn() != color) {
                throw new InvalidMoveException("It's not that color's turn");
            }
            Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
            if (validMoves.contains(move)) {
                ChessPiece piece = board.getPiece(move.getStartPosition());
                if (move.getPromotionPiece() != null) {
                    ChessPiece promotionPiece = new ChessPiece(color, move.getPromotionPiece());
                    board.addPiece(move.getEndPosition(), promotionPiece);
                }
                else { board.addPiece(move.getEndPosition(), piece); }
                board.removePiece(move.getStartPosition());
                setTeamTurn(getOppositeColor(color));
            }
            else {
                throw new InvalidMoveException("Invalid move");
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
                opposingTeamPositions = getPositions(TeamColor.WHITE);
            }
            else {
                opposingTeamPositions = getPositions(TeamColor.BLACK);
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

        public boolean isInCheckmate(TeamColor teamColor) {
            if (isInCheck(teamColor)) {
                HashSet<ChessPosition> teamPositions = getPositions(teamColor);
                for (ChessPosition position : teamPositions) {
                    if (! validMoves(position).isEmpty()) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        private HashSet<ChessPosition> getPositions(TeamColor teamColor) {
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

        public boolean isInStalemate(TeamColor teamColor) {
            ChessPosition kingPosition = findKing(teamColor);
            if (kingPosition == null) { return false; }
            for (ChessPosition position : getPositions(teamColor)) {
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
