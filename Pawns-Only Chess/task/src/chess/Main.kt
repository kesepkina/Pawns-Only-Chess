package chess

const val BOARD_SIZE = 8
const val WHITE_PAWN = 'W'
const val BLACK_PAWN = 'B'
const val EMPTY_CELL = ' '
const val WHITE_START_ROW = 1
const val BLACK_START_ROW = 6
const val WHITE_END_ROW = 7
const val BLACK_END_ROW = 0
val indexToFilesMap =
    mapOf(0 to 'a', 1 to 'b', 2 to 'c', 3 to 'd', 4 to 'e', 5 to 'f', 6 to 'g', 7 to 'h')
val filesToIndexMap = indexToFilesMap.map { (key, value) -> value to key }.toMap()

fun main() {
//    write your code here
    println("Pawns-Only Chess")
    println("First Player's name:")
    val player1 = Player(readln(), WHITE_PAWN)
    println("Second Player's name:")
    val player2 = Player(readln(), BLACK_PAWN)
    val board = Chessboard()
    println(board)
    var player = player1
    var move = ""
    while (!board.gameEnded()) {
        println("${player.name}'s turn:")
        move = readln()
        if (move == "exit") break
        if (board.thereIsRightPawnOnStartPosition(move, player.color)) {
            if (board.moveIsValid(move, player.color)) {
                board.movePawn(move)
                println(board)
                player = if (player == player1) player2 else player1
            } else println("Invalid Input")
        } else {
            println("No ${if (player.color == BLACK_PAWN) "black" else "white"} pawn at ${move.substring(0, 2)}")
        }
    }
    print(board.getGameResult())
    print("Bye!")
}


class Player(val name: String, val color: Char)


class Cell() {
    var figure = EMPTY_CELL
}

class Chessboard() {
    private val state: MutableList<MutableList<Cell>> = mutableListOf()
    private var enPassantPrerequis: Boolean = false
    private var enPassantCoordinate = mutableListOf<Int>()

    init {
        repeat(BOARD_SIZE) {
            val row: MutableList<Cell> = mutableListOf()
            repeat(BOARD_SIZE) {
                row.add(Cell())
            }
            state.add(row)
        }
        var i = WHITE_START_ROW
        for (j in 0 until BOARD_SIZE) {
            state[i][j].figure = WHITE_PAWN
        }
        i = BLACK_START_ROW
        for (j in 0 until BOARD_SIZE) {
            state[i][j].figure = BLACK_PAWN
        }
    }

    override fun toString(): String {
        var result = ""
        for (i in BOARD_SIZE - 1 downTo 0) {
            result += "  " + "+---".repeat(BOARD_SIZE) + "+\n${i + 1} |"
            for (j in 0 until BOARD_SIZE) {
                result += " ${state[i][j].figure} |"
            }
            result += "\n"
        }
        result += "  " + "+---".repeat(BOARD_SIZE) + "+\n    a   b   c   d   e   f   g   h"
        return result
    }

    private fun getPawnsCoordinates(color: Char): String {
        var coordinates = ""
        for (i in 0 until BOARD_SIZE) {
            for (j in 0 until BOARD_SIZE) {
                if (state[i][j].figure == color) {
                    val found = "${indexToFilesMap[j]}${i + 1}"
                    coordinates += if (coordinates.isEmpty()) found else "|$found"
                }
            }
        }
        return coordinates
    }

    fun thereIsRightPawnOnStartPosition(move: String, color: Char) =
        move.substring(0, 2).matches(("(" + this.getPawnsCoordinates(color) + ")").toRegex())

    fun movePawn(moveCoordinates: String) {
        val iFrom = moveCoordinates[1].digitToInt() - 1
        val jFrom = filesToIndexMap[moveCoordinates[0]]
        val iTo = moveCoordinates[3].digitToInt() - 1
        val jTo = filesToIndexMap[moveCoordinates[2]]
        val pawn = state[iFrom][jFrom!!].figure
        state[iFrom][jFrom].figure = EMPTY_CELL
        state[iTo][jTo!!].figure = pawn
        if (!enPassantCoordinate.isEmpty() && jTo == enPassantCoordinate[1]
            && ((iTo > iFrom && iTo == enPassantCoordinate[0] + 1)
                    || (iTo < iFrom && iTo == enPassantCoordinate[0] - 1))) {
            state[enPassantCoordinate[0]][enPassantCoordinate[1]].figure = EMPTY_CELL
        }
        enPassantCoordinate.clear()
        if (kotlin.math.abs(iTo - iFrom) == 2) {
            enPassantPrerequis = true
            enPassantCoordinate = mutableListOf(iTo, jTo)
        }
    }

    fun moveIsValid(move: String, color: Char) : Boolean {
        val regex = getValidMoves(move[0], move[1].digitToInt(), color)
        return move.substring(2).matches(regex.toRegex())
    }

    fun getValidMoves(file: Char, rank: Int, color: Char): String {
        val validMoves = mutableListOf<String>()
        if (color == BLACK_PAWN && rank in 2..7) {
            if (state[rank - 2][filesToIndexMap[file]!!].figure == EMPTY_CELL) validMoves += "${file}${rank - 1}"
            if (file in 'b'..'h' && state[rank - 2][filesToIndexMap[file - 1]!!].figure == WHITE_PAWN) validMoves += "${file - 1}${rank - 1}"
            if (file in 'a'..'g' && state[rank - 2][filesToIndexMap[file + 1]!!].figure == WHITE_PAWN) validMoves += "${file + 1}${rank - 1}"
            if (rank == BLACK_START_ROW + 1 && state[rank - 3][filesToIndexMap[file]!!].figure == EMPTY_CELL) validMoves += "${file}${rank - 2}"
            if (enPassantPrerequis) {
                if (file in 'b'..'h' && rank - 1 == enPassantCoordinate[0] && filesToIndexMap[file - 1]!! == enPassantCoordinate[1]) validMoves += "${file - 1}${rank - 1}"
                if (file in 'a'..'g' && rank - 1 == enPassantCoordinate[0] && filesToIndexMap[file + 1]!! == enPassantCoordinate[1]) validMoves += "${file + 1}${rank - 1}"
                enPassantPrerequis = false
            }
        } else if (color == WHITE_PAWN && rank in 2..7) {
            if (state[rank][filesToIndexMap[file]!!].figure == EMPTY_CELL) validMoves += "${file}${rank + 1}"
            if (file in 'b'..'h' && state[rank][filesToIndexMap[file - 1]!!].figure == BLACK_PAWN) validMoves += "${file - 1}${rank + 1}"
            if (file in 'a'..'g' && state[rank][filesToIndexMap[file + 1]!!].figure == BLACK_PAWN) validMoves += "${file + 1}${rank + 1}"
            if (rank == WHITE_START_ROW + 1 && state[rank + 1][filesToIndexMap[file]!!].figure == EMPTY_CELL) validMoves += "${file}${rank + 2}"
            if (enPassantPrerequis) {
                if (file in 'b'..'h' && rank - 1 == enPassantCoordinate[0] && filesToIndexMap[file - 1]!! == enPassantCoordinate[1]) validMoves += "${file - 1}${rank + 1}"
                if (file in 'a'..'g' && rank - 1 == enPassantCoordinate[0] && filesToIndexMap[file + 1]!! == enPassantCoordinate[1]) validMoves += "${file + 1}${rank + 1}"
                enPassantPrerequis = false
            }
        }
        return "(" + validMoves.joinToString("|") + ")"
    }

    fun getGameResult() : String {
        return when {
            !pawnsOfColorExist(WHITE_PAWN) || pawnOfColorReachedEnd(BLACK_PAWN) -> "Black Wins!"
            !pawnsOfColorExist(BLACK_PAWN) || pawnOfColorReachedEnd(WHITE_PAWN) -> "White Wins!"
            !playerCanPlay(BLACK_PAWN) || !playerCanPlay(WHITE_PAWN) -> "Stalemate!"
            else -> ""
        }
    }

    fun gameEnded() : Boolean {
        var gameEnded = false
        for (color in mutableListOf(BLACK_PAWN, WHITE_PAWN)) {
            gameEnded = gameEnded || !pawnsOfColorExist(color) || pawnOfColorReachedEnd(color) || !playerCanPlay(color)
        }
        return gameEnded
    }

    fun pawnsOfColorExist(color: Char) : Boolean {
        for (rank in state) {
            for (cell in rank) {
                if (cell.figure == color) return true
            }
        }
        return false
    }

    fun pawnOfColorReachedEnd(color: Char) : Boolean {
        val endRang = if (color == WHITE_PAWN) WHITE_END_ROW else BLACK_END_ROW
        for (file in 0 until BOARD_SIZE) {
            if (state[endRang][file].figure == color) return true
        }
        return false
    }

    fun playerCanPlay(color: Char) : Boolean {
        for (i in 0 until BOARD_SIZE) {
            for (j in 0 until BOARD_SIZE) {
                if (state[i][j].figure == color) {
                    val validMoves = getValidMoves(indexToFilesMap[j]!!, i + 1, color)
                    if (validMoves != "()") return true
                }
            }
        }
        return false
    }

}
