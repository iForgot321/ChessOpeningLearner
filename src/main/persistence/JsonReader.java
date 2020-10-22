package persistence;

import model.Move;
import model.board.Board;
import model.board.Position;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// creates an object that reads json from source file and generates the corresponding Move object
// Adapted from JSONSerializationDemo from UBC CPSC 210
public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads Move from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Move read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseMove(jsonObject, null);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses and returns move from JSON object
    private Move parseMove(JSONObject json, Move parentMove) {
        int moveNum = json.getInt("moveNum");
        int piece = json.getInt("piece");
        boolean cap = json.getBoolean("isCaptures");
        Position start = parsePosition(json.getJSONObject("start"));
        Position end = parsePosition(json.getJSONObject("end"));
        Board board = parseBoard(json.getJSONObject("board"));

        Move m = new Move(moveNum, piece, cap, start, end, parentMove, board);
        addMoves(m, json);
        return m;
    }

    // EFFECTS: parses and returns position from JSON
    private Position parsePosition(JSONObject json) {
        int row = json.getInt("row");
        int col = json.getInt("col");

        return new Position(row, col);
    }

    // EFFECTS: parses and returns board from JSON
    private Board parseBoard(JSONObject json) {
        JSONArray jsonMovePieces = json.getJSONArray("movedPieces");
        boolean[] movedPieces = new boolean[jsonMovePieces.length()];
        for (int i = 0; i < jsonMovePieces.length(); i++) {
            movedPieces[i] = jsonMovePieces.getBoolean(i);
        }

        JSONArray jsonBoard = json.getJSONArray("pieceBoard");
        int[][] board = new int[jsonBoard.length()][jsonBoard.length()];
        for (int i = 0; i < jsonBoard.length(); i++) {
            JSONArray rows = jsonBoard.getJSONArray(i);
            for (int j = 0; j < jsonBoard.length(); j++) {
                board[i][j] = rows.getInt(j);
            }
        }

        return new Board(movedPieces, board);
    }

    // MODIFIES: Move m
    // EFFECTS: parses childMoves from JSON object and adds them to parent move
    private void addMoves(Move m, JSONObject jsonObject) {
        JSONArray childMoves = jsonObject.getJSONArray("childMoves");
        for (Object json : childMoves) {
            JSONObject childMove = (JSONObject) json;
            m.addChildMove(parseMove(childMove, m));
        }
    }
}
