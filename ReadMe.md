

check src/pgn_parser!!!


I used second project(refactoring) and add package pgn_parser
so i created class PgnParser which determines whether given pgn is valid(using sample);
sample was like this:
[Event "Broken Tags"]
[Site "Internet"]
[Date "2024.04.13"]
[White "Alice"]
[Black "Bob"]
[Result "1-0"]

1. e4 e5 2. Nf3 Nc6 3. Bb5 a6 4. Ba4 Nf6 5. O-O

1)Event should be this type:[Event "Broken Tags"](only u can change String inside).
2)Site should be this type:[Site "Internet"](only u can change String inside).
3)date should be written correct way.
4)White section should look like this:[White "Alice"](you can only change name of player).
5)same for black.
6)result must be writen legal ,it is not possible to get 5-0 result in one game.
7)this is most important one,game moves must be legal and sintactically right.(FOR EXAMPLE:u cant play e7 with white at first move).