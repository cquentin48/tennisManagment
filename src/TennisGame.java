import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

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

    /**
     * Get the owner of the currentPoint
     * @return owner of the currentPoint
     */
    public int getOwnerOfThePoint(){
        return tennisSetList.get(this.currentSet).getOwnerOfThePoint();
    }


    /**
     * Get the owner of a chosen point
     * @return owner of a chosen point
     */
    public int getOwnerOfThePoint(int pointId){
        return tennisSetList.get(this.currentSet).getOwnerOfThePoint(pointId);
    }


    /**
     * Get the owner of a chosen point
     * @return owner of a chosen point
     */
    public int getCurrentPoint(){
        return tennisSetList.get(this.currentSet).getCurrentPoint();
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
     */
    public void updateGame(int playerId, boolean isTieBreak){
        if(isTieBreak == true && this.getCurrentSet()==6){
            updateTieBreakGame(playerId);
        }else {
            updateNormalSet(playerId);
        }
    }

    /**
     * Update normal Set
     * @param playerId id of the player
     */
    private void updateNormalSet(int playerId) {
        this.tennisSetList.get(this.currentSet).updateSet(playerId);
        this.checkStatusOfCurrentSet(playerId,false);
    }

    /**
     * Check status of current set
     * @param playerId id of the player
     */
    private void checkStatusOfCurrentSet(int playerId, boolean isTieBreak) {
        if(this.tennisSetList.get(this.currentSet).isSetWon(isTieBreak) == true){
            this.currentSet++;
            this.initGame();
        }
    }

    public boolean isSetWon(int setId, boolean isTieBreak){
        return tennisSetList.get(setId).isSetWon(isTieBreak);
    }

    public boolean isGameWon(){
        if(currentSet >=5){
            if(countWonSets(0)>countWonSets(1)){
                gameOwner = 0;
            }
            if(countWonSets(1)>countWonSets(0)){
                gameOwner = 1;
            }
            return true;
        }else{
            return false;
        }
    }

    /**
     * Update a tie break game
     * @param playerId id of the player
     */
    private void updateTieBreakGame(int playerId){
        if(countWonSets(1) == countWonSets(0) && countWonSets(1) == 6){
            this.tennisSetList.get(this.currentSet).updateTieBreakSet(playerId);
            this.checkStatusOfCurrentSet(playerId,true);
        }else{
            this.tennisSetList.get(this.currentSet).updateSet(playerId);
            this.checkStatusOfCurrentSet(playerId,false);
        }
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

    public void setCurrentPoints(int currentSet, String pointPlayer1, String pointPlayer2) {
        this.tennisSetList.get(currentSet).setCurrentPoints(pointPlayer1,pointPlayer2);
    }

    public void setCurrentSet(int currentSet, int winnerPlayerId, Boolean isTieBreak) {
        for(int j = 0; currentSet >= j; j++) {
            if(isTieBreak){
                IntStream.range(0, TennisPoint.POINT_LIST.length).map(i -> winnerPlayerId).forEach(this::updateTieBreakGame);
            }else{
                IntStream.range(0, TennisPoint.POINT_LIST.length).map(i -> winnerPlayerId).forEach(this::updateNormalSet);
            }
        }
    }

    public int getSetOwner(int setId){
        return tennisSetList.get(setId).getOwnerOfTheSet();
    }

}
