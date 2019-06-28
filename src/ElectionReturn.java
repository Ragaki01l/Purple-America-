package cecs174;

/**
 * Represents the election returns for a region. Keeps track of the votes for each candidate. 
 * Also keeps track of the winning candidate.
 * 
 * @author Alvaro Monge alvaro.monge@csulb.edu
 * @author William Jorgensen willwjorgensen@yahoo.com
 * @author Audris Gaerlan audris.gaerlan@gmail.com
 *
 */
public class ElectionReturn {
   private String region;
   private int[] voteCounts;
   private int winningCandidate;
   
   /**
    * Constructor that provides values for the name of this region and the votes that each candidate received.
    * @param region the name of the region. Examples: "Los Angeles", "New Mexico", etc. This also processes
    * the votes to determine the index that represents the winning candidate.
    * 
    * @param votes an array of the votes collected at this region. If there are three candidates, then, this
    * array has to be length 3 with values at each slot for corresponding candidates in the election.
    */
//int[] votes = {7,12,13};
//ElectionReturn e = new ElectionReturn("A",votes);
   public ElectionReturn(String region, int[] votes) {
      // TODO: implement the constructor
	   this.region = region;
//	   voteCounts = votes;
	   
	   int mostVotes = 0;
	   
	   voteCounts = new int [votes.length];
	   
	   for(int i = 0; i < votes.length; i++)
	   {
		   voteCounts[i] = votes[i];
		   if(mostVotes < voteCounts[i])
		   {
			   mostVotes = voteCounts[i];
			   winningCandidate = i;
		   }
	   }
	   
	
	   return;
   }
   
   /**  @return the index of the candidate with the most votes */
   public int getWinningCandidate() {
      return winningCandidate;
   }

   /** @return the name of the region for the election results */
   public String getRegion() {
      return region;
   }
   
   /**
    * Find and return the % of the votes received by the candidate whose index is specified. Thus,
    * if index 1 is specified, then, this computes what percent of the total votes collected 
    * that the votes for candidate at index 1 represent. For example, if this object represents the information
    * for "Los Angeles,885333,2216903,78831," and this method is called with index 1, then this
    * method should calculate and return 0.6969054 (that is, this candidate got nearly 70% of the votes).
    * 
    * @param candidateIndex the index for the candidate whom 
    * @return the percentage of the votes received by a candidate.
    */
   public float getVotingPercentage(int candidateIndex) {
      // TODO: implement this method
	   float percentageVotes;
	   int voteCounter = 0;
	   
	   for(int i = 0; i < voteCounts.length; i++)
	   {
		   voteCounter += voteCounts[i];
	   }
	  
	   percentageVotes = (float) voteCounts[candidateIndex]/voteCounter;
	   
	   
      return percentageVotes; // FIXME: this is just so it compiles! 
   }
}
