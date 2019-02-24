package rehab.chess.rehab;

import java.io.Serializable;

public class Game implements Serializable {
    private int game_idgame;
    private String user_name;
    private int game_black_user;
    private String color;
    private int game_move;
    private String game_init;
    private int showgame_visible;
    private int game_white_user;
    private int game_winner;
    private int game_active;
    private String game_winreason;
    private String game_status;
    private String game_turn;
    private String ts2;
    private String chatcol;

    public Game() {
    }




    public void setGame_idgame(int game_idgame) {
        this.game_idgame = game_idgame;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setGame_black_user(int game_black_user) {
        this.game_black_user = game_black_user;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setGame_move(int game_move) {
        this.game_move = game_move;
    }

    public void setGame_init(String game_init) {
        this.game_init = game_init;
    }

    public void setShowgame_visible(int showgame_visible) {
        this.showgame_visible = showgame_visible;
    }

    public void setGame_white_user(int game_white_user) {
        this.game_white_user = game_white_user;
    }

    public void setGame_winner(int game_winner) {
        this.game_winner = game_winner;
    }

    public void setGame_active(int game_active) {
        this.game_active = game_active;
    }

    public void setGame_winreason(String game_winreason) {
        this.game_winreason = game_winreason;
    }

    public void setGame_status(String game_status) {
        this.game_status = game_status;
    }

    public void setGame_turn(String game_turn) {
        this.game_turn = game_turn;
    }

    public void setTs2(String ts2) {
        this.ts2 = ts2;
    }

    public void setChatcol(String chatcol) {
        this.chatcol = chatcol;
    }

    public int getGame_idgame() {
        return game_idgame;
    }

    public String getUser_name() {
        return user_name;
    }

    public int getGame_black_user() {
        return game_black_user;
    }

    public String getColor() {
        return color;
    }

    public int getGame_move() {
        return game_move;
    }

    public String getGame_init() {
        return game_init;
    }

    public int getShowgame_visible() {
        return showgame_visible;
    }

    public int getGame_white_user() {
        return game_white_user;
    }

    public int getGame_winner() {
        return game_winner;
    }

    public int getGame_active() {
        return game_active;
    }

    public String getGame_winreason() {
        return game_winreason;
    }

    public String getGame_status() {
        return game_status;
    }

    public String getGame_turn() {
        return game_turn;
    }

    public String getTs2() {
        return ts2;
    }

    public String getChatcol() {
        return chatcol;
    }
}

