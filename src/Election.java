package cecs174;
/**
 * TODO: Your own description here
 * 
 * @author Alvaro Monge alvaro.monge@csulb.edu
 * TODO: additional authors here...
 * @author William Jorgensen willwjorgensen@yahoo.com
 * @author Audris Gaerlan audris.gaerlan@gmail.com
 */
public class Election {
   
   /** The name of the election, e.g.: 2012 US Presidential Election */
   private String name;

   /** The name of the candidates in the election */ 
   private String[] candidates;
   
   /** The array of ElectionReturn objects that have the votes for each region in the election */
   private ElectionReturn[] electionReturns;
   
  

   /**
    * Constructor that provides values for the name of this election, establishes
    * the candidates in the election, and creates election results based on the
    * array of election records. 
    * 
    * @see #getVotes(String, int) helper method getVotes
    * 
    * @param name the name of the election (e.g.: 2012 US Presidential Election)
    * @param candidates a list of the candidates' names in the election 
    * @param records an array of election results, each is a String in the form: "Los Angeles,885333,2216903,78831,". 
    * This example is for Los Angeles where the first candidate received 885333 votes, the second 2216903 votes,
    * and the third candidate 78831.
    */
  
   
   public Election(String name, String[] candidates, String[] records) {
      // TODO: implement the constructor 
	 this.name = name;	
	 this.candidates = new String [candidates.length];
	 electionReturns = new ElectionReturn[records.length];
	 //Copy the candidate parameter
	 for(int i = 0; i < candidates.length; i++)
	 {
		 this.candidates[i] = candidates[i];
	 }
	 
	 for(int i = 0; i < records.length ; i++)
	 {
		 electionReturns[i] = getVotes(records[i], this.candidates.length);
	 }
	   
   }
   
   /**  @return  the name of the election  */
   public String getName() {
      return name;
   }
   
   //Split "Los Angeles,885333,2216903,78831," to region, and votes(convert votes from string to int)
   /**
    * A helper method that given a String of the form "Los Angeles,885333,2216903,78831," it creates 
    * and returns an ElectionReturn object. Other example: "District of Columbia,21256,202970,3360,"
    *  
    * @see #Election(String, String[], String[]) Election constructor for more information
    * @param regionalResult the String with election results that needs to be processed.
    * @param numberOfCandidates the number of candidates in the election for which the regionalResult has votes
    * @return an ElectionReturn object encapsulating the results of the election represented in regionalResult
    */
   private ElectionReturn getVotes(String regionalResult, int numberOfCandidates) {
      // TODO: implement this method
	
	//Ensures the array isn't being overwritten
	String[] splitRegionalResult; 
	splitRegionalResult = regionalResult.split(","); //Splits regional result where commas occur.
	String regionName = splitRegionalResult[0];
	int[] votes = new int[numberOfCandidates];
	
	for(int i = 1; i < votes.length; i++)
	{
		votes[i-1] = Integer.parseInt(splitRegionalResult[i]);
	}
	
	 
	ElectionReturn region = new ElectionReturn(regionName, votes);
	      
      return region;  // Returns region
      
   }
   
   
   /**
    * Given the name of a region, this returns the ElectionReturn object with the election information.
    * You need to look through the electionReturns to find the one that matches the regionName parameter value.
    * 
    * @param regionName name of the region whose election returns are to be found, e.g.: "Los Angeles", or "California" 
    * @return the ElectionReturn object representing the election results for the named region; null if it's not found.
    */
   public ElectionReturn getVotingRecord(String regionName) {
      // TODO: implement this method
	   int match = 0;
	   
	   boolean regionIsFound = false;
	   
	   ElectionReturn electionReturnMatch = null;
	   for(int i = 0; i < electionReturns.length; i++)
		{
			if(regionName.equals(electionReturns[i]))
			{
				regionIsFound = true;
				match = i;
			}
		}
	   if(regionIsFound == true)
	   {
		return electionReturns[match];
	   }
	   else
	   {
		   return null;
	   }
   }

   /**
    * Determines and returns the winning candidate of the named region, returning an array of percentages
    * in which only one of its entries is 1.0 (100%, the winner) and the others are 0.
    * 
    * @param regionName the name of the region whose information is being requested, e.g.: "New Mexico", or "San Francisco"
    * @return an array whose length is the same as the number of candidates in the election and where all entries
    * except for one are 0.0. The only entry that is non-zero needs to be 100 to declare the candidate represented
    * by that position as the winner. Returns null if the region is not found.
    */
   public float[] getWinningCandidate(String regionName) {
      // TODO: implement this method
	  float winningPercentage = 1; 
	 float[] winningCandidate = new float[candidates.length]; //Meant for holding an array of percentages
	 float[] emptyArray = new float[candidates.length];
	 
	 for(int i = 0; i < electionReturns.length; i++)
	 {
		 if(regionName.equals(electionReturns[i].getRegion()))
				 {
			       int values = electionReturns[i].getWinningCandidate();
			       winningCandidate[values] = winningPercentage;
			       return winningCandidate;
				 }
		  
				 
	 }

      return emptyArray;  // FIXME: this is just so it compiles!
   }

   /**
    * Determines and returns percentage of votes received by each candidate in the named region.
    * 
    * @param regionName the name of the region whose information is being requested, e.g.: "New Mexico", or "San Francisco"
    * @return an array whose length is the same as the number of candidates in the election and where 
    * each entry represents the percentage of the votes received by each candidate. Thus, one such result 
    * might be the array [0.632, 0.315, 0.053] where there there three candidates, the first received 63.2% of the votes,
    * the second 31.5% of the votes, and the third got 5.3% of the votes.  Returns null if the region is not found.
    */
   public float[] getVotingPercentage(String regionName) {
      // TODO: implement this method
	   float[] percentageVotes = new float[candidates.length];
	   float[] emptyArray2 = new float[candidates.length];
	   
	   for(int i = 0; i  < electionReturns.length; i++)
	   {
		   if(regionName.equals(electionReturns[i].getRegion()))
		   {
			   for(int j = 0; j < candidates.length; j++)
			   {
				   percentageVotes[j] = electionReturns[i].getVotingPercentage(j);
			   }
			   return percentageVotes;
		   }
	   }
	   
	   return emptyArray2;
	  
	  
   } 
}
