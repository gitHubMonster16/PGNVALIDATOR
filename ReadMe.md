Chess Game - Readme.
Project Description
A Java-based chess game implementing all standard rules, including:
✔ Piece movements (pawn, rook, knight, bishop, queen, king)
✔ Special move (castling)
✔ Check/checkmate detection
✔ Valid move highlighting

Key Features
Piece Movement Logic
Bishop: Diagonal moves (DiagonalMove.java)

Rook: Horizontal/vertical moves (LinearMove.java)

Queen: Combines bishop + rook moves

Knight: L-shaped jumps (KnightMove.java)

King: 1-square moves + castling (KingMove.java)

Pawn: Forward/capture(PawnMove.java)

Special Rules
Castling (king-side & queen-side for both colors)

Check/Checkmate detection


Testing:

✅ Piece Movement Tests (valid moves, blocked paths, captures)

✅ Checkmate Tests (Fool's Mate, Stalemate scenarios)

✅ Castling Tests (valid/invalid conditions)