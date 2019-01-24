import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TennisGame {
    private int gameOwner;
    private List<TennisSet> tennisSetList;
    private int currentSet;

    public int getGameOwner() {
        return gameOwner;
    }

    @Override
    public String toString() {
        return "TennisGame{" +
                "gameOwner=" + gameOwner +
                ", tennisSetList=" + tennisSetList +
                ", currentSet=" + currentSet +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TennisGame that = (TennisGame) o;
        return gameOwner == that.gameOwner &&
                currentSet == that.currentSet &&
                Objects.equals(tennisSetList, that.tennisSetList);
    }

    /**
     * Update the current game
     * @param playerId id of the player which has won the point
     * @param isTieBreak if this match has a tie-break
     * @param maxSets max number of sets in a game
     */
    public void updateGame(int playerId, boolean isTieBreak, int maxSets){
        if(isTieBreak == true && this.getCurrentSet()==(maxSets-1)){
            updateTieBreakGame(playerId);
        }else {
            updateNormalSet(playerId, isTieBreak, maxSets);
        }
    }

    /**
     * Update normal Set
     * @param playerId id of the player
     * @param isTieBreak if it has a tie break
     * @param maxSets max numberOfSets
     */
    private void updateNormalSet(int playerId, boolean isTieBreak, int maxSets) {
        this.tennisSetList.get(this.currentSet).updateSet(playerId,isTieBreak,maxSets);
        this.checkStatusOfCurrentSet(playerId);
    }

    /**
     * Check status of current set
     * @param playerId id of the player
     */
    private void checkStatusOfCurrentSet(int playerId) {
        if(this.tennisSetList.get(this.currentSet).isNewSet() == true){
            this.currentSet++;
            this.initGame();
        }
        if(this.countWonSets(playerId) <=(this.countWonSets((playerId+1)%2) +2)){
            this.gameOwner = playerId;
        }
    }

    /**
     * Update a tie break game
     * @param playerId id of the player
     */
    private void updateTieBreakGame(int playerId){
        this.tennisSetList.get(this.currentSet).updateTieBreakSet(playerId);
        this.checkStatusOfCurrentSet(playerId);
    }

    /**
     * Count number of sets won by a player
     * @param playerId id of the player
     * @return Integer won sets by the player
     */
    private int countWonSets(int playerId){
        int count = 0;
        for(int i = 0;i<this.tennisSetList.size();i++){
            count = (this.tennisSetList.get(i).getOwnerOfTheSet()==playerId)?count+1:count;
        }
        return count;
    }

    /**
     * Counts points in a chosen set
     * @param playerId id of the player
     * @param setId id of the chosen set
     * @return point in chosen set
     */
    public int getPointCountInASet(int playerId, int setId){
        return this.tennisSetList.get(setId).getPoint(playerId);
    }

    /**
     * Get the point from the current Set
     * @param playerId if of the player
     * @return point from the current set
     */
    public String getPoint(int playerId){
        return this.tennisSetList.get(this.currentSet).getCurrentPoint(playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameOwner, tennisSetList, currentSet);
    }

    public int getCurrentSet() {
        return currentSet;
    }

    public TennisGame() {
        this.currentSet = 0;
        this.tennisSetList = new ArrayList<>();
        this.initGame();
    }

    /**
     * Init a new game
     */
    private void initGame(){
        this.tennisSetList.add(this.currentSet,new TennisSet());
    }

    public void setCurrentPoints(String pointPlayer1, String pointPlayer2) {
        this.tennisSetList.get(this.currentSet).setCurrentPoints(pointPlayer1,pointPlayer2);
    }
}
