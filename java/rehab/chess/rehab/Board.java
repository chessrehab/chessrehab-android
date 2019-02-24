package rehab.chess.rehab;

import java.util.ArrayList;

public class Board {
    //attributes from db -state of the game
    private int id;
    private Game gameObj;



    private int white_id;
    private int black_id;

    private Boolean black_draw_offer;
    private Boolean white_draw_offer;

    private Boolean black_resign;
    private Boolean white_resign;

    private ArrayList<String> blackouts;
    private ArrayList<String> whiteouts;


    private Boolean castleBK;
    private Boolean castleBQ;
    private Boolean castleWK;
    private Boolean castleWQ;
    private String game;

    private String checkMateIs;

    private String enPassantColor;
    private int enPassantI;
    private int enPassantJ;



    private Boolean kingBcheck;
    private Boolean kingWcheck;

    private int kingBpositionI;
    private int kingBpositionJ;
    private int kingWpositionI;
    private int kingWpositionJ;

    private int moveid;
    private String turn0; //"b"  for black
    private int init;

    private Fields fields[][];

    //state of the board attribuites

    private Boolean latestmove;



    //constructor with initialization on empty


    public Board(Game game) {
        this.gameObj = game;
        this.id = game.getGame_idgame();
        this.white_id = game.getGame_white_user();
        this.black_id = game.getGame_black_user();
        this.black_draw_offer = false;
        this.white_draw_offer = false;
        this.black_resign = false;
        this.white_resign = false;
        this.blackouts = new ArrayList<String>();
        this.whiteouts = new ArrayList<String>();
        this.castleBK = false;
        this.castleBQ = false;
        this.castleWK = false;
        this.castleWQ = false;
        this.game="on";
        this.checkMateIs = "nula";
        this.enPassantColor = "nula";
        this.enPassantI = 0;
        this.enPassantJ = 0;
        this.kingBcheck = false;
        this.kingWcheck = false;
        this.kingBpositionI = 8;
        this.kingBpositionJ = 5;
        this.kingWpositionI = 1;
        this.kingWpositionJ = 5;
        this.moveid = 0;
        this.turn0 = "w";
        this.init=0;
        this.fields = new Fields[9][9];
        for (int x = 1; x < 9; x++) {
            for (int y = 1; y < 9; y++) {
                this.fields[x][y] = new Fields(x, y);
            }

        }
        this.latestmove=true;

    }

    public Game getGameObj() {
        return gameObj;
    }

    public void setGameObj(Game gameObj) {
        this.gameObj = gameObj;
    }



    public void setBoardStart() {
        for (int i=1;i<9;i++) {
            this.fields[2][i].setPiece1("pw0");
            this.fields[7][i].setPiece1("pb0");
        }

        this.fields[1][1].setPiece1("Rw0");
        this.fields[1][8].setPiece1("Rw0");
        this.fields[8][1].setPiece1("Rb0");
        this.fields[8][8].setPiece1("Rb0");
        this.fields[1][2].setPiece1("Nw0");
        this.fields[1][7].setPiece1("Nw0");
        this.fields[8][2].setPiece1("Nb0");
        this.fields[8][7].setPiece1("Nb0");
        this.fields[1][3].setPiece1("Bw0");
        this.fields[1][6].setPiece1("Bw0");
        this.fields[8][3].setPiece1("Bb0");
        this.fields[8][6].setPiece1("Bb0");
        this.fields[1][5].setPiece1("Kw0");
        this.kingWpositionI=1;
        this.kingWpositionJ=5;
        this.fields[1][4].setPiece1("Qw0");
        this.fields[8][5].setPiece1("Kb0");
        this.kingBpositionI=8;
        this.kingBpositionJ=5;

        this.fields[8][4].setPiece1("Qb0");
        for(int i=3;i<7;i++) {
            for(int j=1;j<9;j++) {
                this.fields[i][j].setPiece1("nula");
            }
        }

        this.init=1;

    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getGame() { return game; }
    public void setGame(String game) { this.game = game; }
    public int getWhite_id() { return white_id; }
    public void setWhite_id(int white_id) { this.white_id = white_id; }
    public int getBlack_id() { return black_id; }
    public void setBlack_id(int black_id) { this.black_id = black_id; }
    public Boolean getBlack_draw_offer() { return black_draw_offer; }
    public void setBlack_draw_offer(Boolean black_draw_offer) { this.black_draw_offer = black_draw_offer; }
    public Boolean getWhite_draw_offer() { return white_draw_offer; }
    public void setWhite_draw_offer(Boolean white_draw_offer) { this.white_draw_offer = white_draw_offer; }
    public Boolean getBlack_resign() { return black_resign; }
    public void setBlack_resign(Boolean black_resign) { this.black_resign = black_resign; }
    public Boolean getWhite_resign() { return white_resign; }
    public void setWhite_resign(Boolean white_resign) { this.white_resign = white_resign; }

    public void setFieldPiece(int x, int y, String piece) {
        this.fields[x][y].setPiece1(piece);
    }
    public String getFieldPiece(int x, int y) {
        return this.fields[x][y].getPiece1();
    }
    public void setFieldSelected(int x, int y, Boolean isSelected) { this.fields[x][y].setSelected1(isSelected); }
    public Boolean getFieldSelected(int x, int y) {
        return this.fields[x][y].getSelected1();
    }
    public void setFieldStress(int x, int y, int isStressed) { this.fields[x][y].setStress(isStressed); }
    public int getFieldStress(int x, int y) { return this.fields[x][y].getStress(); }
    public void setFieldOption(int x, int y, Boolean isOption) { this.fields[x][y].setOption1(isOption); }
    public Boolean getFieldOption(int x, int y) { return this.fields[x][y].getOption1(); }


    // Array lists of outted pices
    public String getBlackout (int i) { return this.blackouts.get(i); }
    public String getWhiteout (int i) { return this.whiteouts.get(i); }
    public void addBlackout(String outtedBlackItem) { this.blackouts.add(outtedBlackItem); }
    public void addWhiteout(String outtedWhiteItem) { this.whiteouts.add(outtedWhiteItem); }
    public int getBlackoutsCount() { return this.blackouts.size(); }
    public int getWhiteoutsCount() { return this.whiteouts.size(); }
    public void clearWhiteouts() { whiteouts.clear();}
    public void clearBlackouts() { blackouts.clear();}

    ////////////////////////////////////////////

    public Boolean getCastleBK() { return castleBK; }
    public void setCastleBK(Boolean castleBK) { this.castleBK = castleBK; }
    public Boolean getCastleBQ() { return castleBQ; }
    public void setCastleBQ(Boolean castleBQ) { this.castleBQ = castleBQ; }
    public Boolean getCastleWK() { return castleWK; }
    public void setCastleWK(Boolean castleWK) { this.castleWK = castleWK; }
    public Boolean getCastleWQ() { return castleWQ; }
    public void setCastleWQ(Boolean castleWQ) { this.castleWQ = castleWQ; }
    public String getCheckMateIs() { return checkMateIs; }
    public void setCheckMateIs(String checkMateIs) { this.checkMateIs = checkMateIs; }
    public String getEnPassantColor() { return enPassantColor; }
    public void setEnPassantColor(String enPassantColor) { this.enPassantColor = enPassantColor; }
    public int getEnPassantI() { return enPassantI; }
    public void setEnPassantI(int enPassantI) { this.enPassantI = enPassantI; }
    public int getEnPassantJ() { return enPassantJ; }
    public void setEnPassantJ(int enPassantJ) { this.enPassantJ = enPassantJ; }
    public Boolean getKingBcheck() { return kingBcheck; }
    public void setKingBcheck(Boolean kingBcheck) { this.kingBcheck = kingBcheck; }
    public Boolean getKingWcheck() { return kingWcheck; }
    public void setKingWcheck(Boolean kingWcheck) { this.kingWcheck = kingWcheck; }
    public int getKingBpositionI() { return kingBpositionI; }
    public void setKingBpositionI(int kingBpositionI) { this.kingBpositionI = kingBpositionI; }
    public int getKingBpositionJ() { return kingBpositionJ; }
    public void setKingBpositionJ(int kingBpositionJ) { this.kingBpositionJ = kingBpositionJ; }
    public int getKingWpositionI() { return kingWpositionI; }
    public void setKingWpositionI(int kingWpositionI) { this.kingWpositionI = kingWpositionI; }
    public int getKingWpositionJ() { return kingWpositionJ; }
    public void setKingWpositionJ(int kingWpositionJ) { this.kingWpositionJ = kingWpositionJ; }
    public int getMoveid() { return moveid; }
    public void setMoveid(int moveid) { this.moveid = moveid; }
    public String getTurn0() { return turn0; }
    public void setTurn0(String turn0) { this.turn0 = turn0; }

    public Boolean getLatestmove() { return latestmove; }
    public void setLatestmove(Boolean latestMove) { this.latestmove = latestMove; }

    public  String toJSON() {
        String colorField="";
        StringBuilder JSONtxt= new StringBuilder();
        JSONtxt.append("{");
        //black_draw_offer
        JSONtxt.append("\"black_draw_offer\":");
        JSONtxt.append(this.getBlack_draw_offer().toString());
        JSONtxt.append(",");

        //black_id
        JSONtxt.append("\"black_id\":");
        JSONtxt.append(((Integer)this.getBlack_id()).toString());
        JSONtxt.append(",");

        //black_resign
        JSONtxt.append("\"black_resign\":");
        JSONtxt.append(this.getBlack_resign().toString());
        JSONtxt.append(",");


        //castleBK
        JSONtxt.append("\"castleBK\":");
        JSONtxt.append(this.getCastleBK().toString());
        JSONtxt.append(",");

        //castleBQ
        JSONtxt.append("\"castleBQ\":");
        JSONtxt.append(this.getCastleBQ().toString());
        JSONtxt.append(",");

        //castleWK
        JSONtxt.append("\"castleWK\":");
        JSONtxt.append(this.getCastleWK().toString());
        JSONtxt.append(",");


        //castleWQ
        JSONtxt.append("\"castleWQ\":");
        JSONtxt.append(this.getCastleWQ().toString());
        JSONtxt.append(",");

        //checkMateIs
        JSONtxt.append("\"checkMateIs\":\"");
        JSONtxt.append(this.getCheckMateIs().toString());
        JSONtxt.append("\",");

        //enPassantColor
        JSONtxt.append("\"enPassantColor\":\"");
        JSONtxt.append(this.getEnPassantColor().toString());
        JSONtxt.append("\",");

        //enPassantI
        JSONtxt.append("\"enPassantI\":");
        JSONtxt.append(((Integer) this.getEnPassantI()).toString());
        JSONtxt.append(",");

        //enPassantJ
        JSONtxt.append("\"enPassantJ\":");
        JSONtxt.append(((Integer) this.getEnPassantJ()).toString());
        JSONtxt.append(",");

        //game
        JSONtxt.append("\"game\":\"");
        JSONtxt.append(this.getGame().toString());
        JSONtxt.append("\",");

        //id
        JSONtxt.append("\"id\":");
        JSONtxt.append(((Integer) this.getId()).toString());
        JSONtxt.append(",");

        //kingBcheck
        JSONtxt.append("\"kingBcheck\":");
        JSONtxt.append(this.getKingBcheck().toString());
        JSONtxt.append(",");

        //kingBpositionI
        JSONtxt.append("\"kingBpositionI\":");
        JSONtxt.append(((Integer) this.getKingBpositionI()).toString());
        JSONtxt.append(",");

        //kingBpositionJ
        JSONtxt.append("\"kingBpositionJ\":");
        JSONtxt.append(((Integer) this.getKingBpositionJ()).toString());
        JSONtxt.append(",");

        //kingWcheck
        JSONtxt.append("\"kingWcheck\":");
        JSONtxt.append(this.getKingWcheck().toString());
        JSONtxt.append(",");

        //kingWpositionI
        JSONtxt.append("\"kingWpositionI\":");
        JSONtxt.append(((Integer) this.getKingWpositionI()).toString());
        JSONtxt.append(",");

        //kingWpositionJ
        JSONtxt.append("\"kingWpositionJ\":");
        JSONtxt.append(((Integer) this.getKingWpositionJ()).toString());
        JSONtxt.append(",");

        //moveid
        JSONtxt.append("\"moveid\":");
        JSONtxt.append(((Integer) this.getMoveid()).toString());
        JSONtxt.append(",");

        //turn0
        JSONtxt.append("\"turn0\":\"");
        JSONtxt.append(this.getTurn0().toString());
        JSONtxt.append("\",");

        //white_draw_offer
        JSONtxt.append("\"white_draw_offer\":");
        JSONtxt.append(this.getWhite_draw_offer().toString());
        JSONtxt.append(",");

        //white_id
        JSONtxt.append("\"white_id\":");
        JSONtxt.append(((Integer) this.getWhite_id()).toString());
        JSONtxt.append(",");

        //white_resign
        JSONtxt.append("\"white_resign\":");
        JSONtxt.append(this.getWhite_resign().toString());
        JSONtxt.append(",");

        //whiteouts
        JSONtxt.append("\"whiteouts\":[");
        for (int i=0; i<this.getWhiteoutsCount(); i++) {
            JSONtxt.append("\""+getWhiteout(i).toString()+"\"");
            if (i<(this.getWhiteoutsCount()-1)) { JSONtxt.append(",");}
        }
        JSONtxt.append("]");
        JSONtxt.append(",");

        //blackouts
        JSONtxt.append("\"blackouts\":[");
        for (int i=0; i<this.getBlackoutsCount(); i++) {
            JSONtxt.append("\""+getBlackout(i).toString()+"\"");
            if (i<(this.getBlackoutsCount()-1)) { JSONtxt.append(",");}
        }
        JSONtxt.append("]");
        JSONtxt.append(",");

        JSONtxt.append("\"fields\":[" + null +",");
        for (int i=1;i<9;i++) {
            JSONtxt.append("[" + null + ",");
            for (int j=1;j<9;j++) {
                JSONtxt.append("{");
                JSONtxt.append("\"board1\":\"android\",");
                if ((i % 2 == 0) & (j % 2 == 0)) {
                    colorField="cornFlowerBlue";
                }
                if ((i % 2 == 0) & (j % 2 != 0)) {
                    colorField="ghostWhite";
                }
                if ((i % 2 != 0) & (j % 2 == 0)) {
                    colorField="ghostWhite";
                }
                if ((i % 2 != 0) & (j % 2 != 0)) {
                    colorField="cornFlowerBlue";
                }



                JSONtxt.append("\"color1\":\""+colorField+"\",");
                JSONtxt.append("\"name1\":\"android\",");
                JSONtxt.append("\"option1\":false,");
                JSONtxt.append("\"piece1\":\""+this.fields[i][j].getPiece1()+"\",");
                JSONtxt.append("\"selected1\":false,");
                JSONtxt.append("\"stress\":"+this.fields[i][j].getStress()+",");
                JSONtxt.append("\"x1\":"+((Integer) i).toString()+",");
                JSONtxt.append("\"y1\":"+((Integer) j).toString());
                JSONtxt.append("}");
                if (j<8) {JSONtxt.append(",");}
            }
            JSONtxt.append("]");
            if (i<8) { JSONtxt.append(",");}
        }
        JSONtxt.append("]");
        JSONtxt.append("}");
        return(JSONtxt.toString());
    }

}
