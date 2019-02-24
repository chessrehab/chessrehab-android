package rehab.chess.rehab;

public class Challenge {
    private int idChallenge;
    private String challenger;
    private String color;
    private int idChallenger;
    private int private0;
    private int idChallenged;
    private String challenged;


    public Challenge() {
    }

    public int getIdChallenge() {
        return idChallenge;
    }

    public void setIdChallenge(int idchallenge) {
        this.idChallenge = idchallenge;
    }

    public String getChallenger() {
        return challenger;
    }

    public void setChallenger(String name) {
        this.challenger = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getIdChallenger() {
        return idChallenger;
    }

    public void setIdChallenger(int iduser) {
        this.idChallenger = iduser;
    }

    public int getPrivate0() {
        return private0;
    }

    public void setPrivate0(int private0) {
        this.private0 = private0;
    }

    public int getIdChallenged() {
        return idChallenge;
    }

    public void setIdChallenged(int idchallenged) {
        this.idChallenged = idchallenged;
    }

    public String getChallenged() {
        return challenged;
    }

    public void setChallenged(String challenged) {
        this.challenged = challenged;
    }
}

