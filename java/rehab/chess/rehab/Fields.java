package rehab.chess.rehab;

public class Fields {
    private int x1;
    private int y1;
    private String color1;
    private String piece1;
    private Boolean selected1;
    private Boolean option1;
    private int stress;


    public Fields(int x1, int y1) {
        this.x1 = x1;
        this.y1 = y1;

        if ((((y1%2)==0) & ((x1%2)==1)) || (((y1%2)==1) & ((x1%2)==0))) {
            this.color1 = "GhostWhite";
        } else {
            this.color1 = "CornflowerBlue";
        }
        this.piece1 = "nula";
        this.selected1 = false;
        this.option1 = false;
        this.stress = 0;
    }



    public int getX1() { return x1; }
    public void setX1(int x1) { this.x1 = x1; }
    public int getY1() { return y1; }
    public void setY1(int y1) { this.y1 = y1; }
    public String getColor1() { return color1; }
    public void setColor1(String color1) { this.color1 = color1; }
    public String getPiece1() { return piece1; }
    public void setPiece1(String piece1) { this.piece1 = piece1; }
    public Boolean getSelected1() { return selected1; }
    public void setSelected1(Boolean selected1) { this.selected1 = selected1; }
    public Boolean getOption1() { return option1; }
    public void setOption1(Boolean option1) { this.option1 = option1; }
    public int getStress() { return stress; }
    public void setStress(int stress) { this.stress = stress; }
}
