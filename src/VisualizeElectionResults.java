package cecs174;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// Import the Processing core library, included as part of project in the lib directory
import processing.core.*;

/**
 * Visualize the results from US Presidential Elections.
 * @author Alvaro Monge alvaro.monge@csulb.edu
 * @author William Jorgensen willwjorgensen@yahoo.com
 * @author Audris Gaerlan audris.gaerlan@gmail.com
 */
public class VisualizeElectionResults extends PApplet {
   
   /**
    * Maximum number of subregions for which election results can be reported
    */
   public static final int MAX_SUBREGIONS = 1000;
   
   /**
    * Size of the border around the map
    */
   public static final int BORDER_SIZE = 25;
   
   /**
    * The maximum amount of a color (red, green, or blue) that can be mixed.
    */
   public static final int MAX_COLOR_INTENSITY = 255;
   
   
   // Instance variables
   private Election election;
   private float minLongitude;
   private float maxLongitude;
   private float minLatitude;
   private float maxLatitude;

   /**
    * The initial settings of the Sketch, sets size of the drawing area.
    */
   public void settings() {
      size(1200,800);
   }

   /**
    * Reads geographic data as well as election results data and then draws a map of the region
    * requested, colored according to number of votes received by each candidate.
    */
   public void setup() {
      background(255);
      String fileName = "data/" + args[0] + ".txt";  // files are in the data folder in the Eclipse project
      File dataFile = new File(fileName);
      try {
         Scanner dataReader = new Scanner(dataFile);

         minLongitude = dataReader.nextFloat();
         minLatitude = dataReader.nextFloat();
         maxLongitude = dataReader.nextFloat();
         maxLatitude = dataReader.nextFloat();

         int numberOfSubregions = dataReader.nextInt();
         dataReader.nextLine(); // skip past end of line with number
         dataReader.nextLine(); // skip past blank line

         int i = 0;
         String previousRegion = null;
         while (dataReader.hasNext() && i < numberOfSubregions) {
            String subregionName = dataReader.nextLine();  // e.g.: Los Angeles
            String currentRegion = dataReader.nextLine();  // e.g.: CA

// TODO: when ready to test HW 4, uncomment the next four lines and also line 83 or 84 below 
            if (previousRegion == null || !previousRegion.equals(currentRegion)) {  // load the election results for region
               election = readElection("data/" + currentRegion + args[1] + ".txt");
               previousRegion = currentRegion;
            }
            
            int numberOfPoints = dataReader.nextInt();
            beginShape();
            
// TODO: uncomment one of the next two lines when ready to test HW 4, be sure to test both of them.
        //   fill( mapToColor(election.getWinningCandidate(subregionName)));  // red state and blue state
           fill( mapToColor(election.getVotingPercentage(subregionName)));    // purple states
            
            int j = 0;
            while (dataReader.hasNext() && j < numberOfPoints) { // add the vertices to define polygon, based on long/lat values
               vertex( mapLongitudeToX( dataReader.nextFloat() ), mapLatitudeToY( dataReader.nextFloat()) );
               j++;
            }
            endShape(CLOSE);
            dataReader.nextLine(); // skip past end of line with number
            dataReader.nextLine(); // skip past blank line
            i++;
         }
         dataReader.close();
      } catch(FileNotFoundException fnfe) {
         System.out.println("The file named " + fileName + " cannot be opened or does not exist");
         System.exit(1);
      }
   }

   /**
    * Maps an array of percentages into a color. The array is expected to be of length at most three.
    * When its length is two, it returns a color mixture with no green. When its length is three,
    * it mixes all three of Red, Green, Blue; with the first value in the array being the amount of
    * Red (% of votes for Republican candidate), the second element in the array being the amount of 
    * Blue (% of votes for Democratic candidate), and the third element being the amount of 
    * Green (% of votes for the other candidate). 
    * @param votePercentages an array of at most three percentages, one for each candidate in the election.
    * @return the color created by mixing a color using Red, Blue, Green for Republican, Democrat, and Other
    * candidates.
    */
   public int mapToColor(float[] votePercentages) {
      int color = 0;
      switch(votePercentages.length) {
      case 1:
         color = color(votePercentages[0]*MAX_COLOR_INTENSITY); // one candidate... should never happen
         break;
      case 2:
         color = color(votePercentages[0]*MAX_COLOR_INTENSITY, 0, votePercentages[1]*MAX_COLOR_INTENSITY);  // two candidates
         break;
      case 3:
         color = color(votePercentages[0]*MAX_COLOR_INTENSITY, votePercentages[2]*MAX_COLOR_INTENSITY, votePercentages[1]*MAX_COLOR_INTENSITY);
      }
      return color;
   }

   /**
    * Reads a text file with election results. The file is formatted as a sequence of lines where
    * each line has the name of a region (or subregion) and the number of votes for each of the
    * Presidential candidates. Some sample lines: "Florida,4046219,4282367,83662," in USA2008.txt or
    * "New York,89559,502674,8058," in NY2012.txt.
    * @param fileName the name of the file with the election results.
    * @return an Election object with all of the election results.
    */
   public Election readElection(String fileName) {
      Election election = null;
      File dataFile = new File(fileName);
      try {
         Scanner dataReader = new Scanner(dataFile);

         // Sample first line (header): 2012 US Presidential Election,Romney,Obama,Other,
         String[] headerValues = dataReader.nextLine().split(",");         
         String electionName = headerValues[0];         
         String[] candidates = new String[headerValues.length-1];  // excludes election name, and empty string due to last comma
         for (int i=0; i < candidates.length; i++) {
            candidates[i] = headerValues[i+1];
         }
         
         String[] lines = new String[MAX_SUBREGIONS];

         // Save all remaining lines in an array
         int i = 0;
         while (dataReader.hasNext()) {
            String line = dataReader.nextLine();
            if (line.length() > 1) { // to be sure we're processing a line of votes
               lines[i] = line;
               i++;
            } else {
               System.err.printf("This should never happen... empty line? #%s#\n", line);
            }
         }
         dataReader.close();

         // copy over to an array that's the exact size needed
         String[] votingData = new String[i];
         for (int j=0; j < i; j++){
            votingData[j] = lines[j];
         }
         
         // Send the data to the constructor of Election
         election = new Election(electionName, candidates, votingData);
         
      } catch(FileNotFoundException fnfe) {
         System.out.println("The file named " + fileName + " cannot be opened or does not exist");
         System.exit(1);
      }

      return election;
   }


   /**
    * Maps a longitude value to a x coordinate in the current canvas. 
    *  
    * @param longitude the longitude value to be mapped
    * @return a x coordinate within canvas corresponding to the longitude value
    */
   private float mapLongitudeToX(float longitude) {
      return BORDER_SIZE + (width - 2*BORDER_SIZE) * (longitude - minLongitude) / (maxLongitude-minLongitude);
   }

   /**
    * Maps a longitude value to a y coordinate in the current canvas. Must adjust for
    * y coordinates increasing from the top to the bottom of the screen, whereas longitudes
    * increase from bottom to top.
    * @param latitude the latitude value to be mapped
    * @return a y coordinate within canvas corresponding to the latitude value
    */
   private float mapLatitudeToY(float latitude) {
      return BORDER_SIZE + ( (height - 2*BORDER_SIZE) * (1 - (latitude - minLatitude) / (maxLatitude-minLatitude) ) ) ;
   }

   public void draw() {
      // TODO: Not part of the assignment... try adding GUI that will allow a user to type in the region 
      //       and year (instead of re-running the program) and the program runs displaying the corresponding
      //       map and election results.
   }

   /**
    * The main for a Processing sketch runs the Processing Applet main with the name
    * of this Java class.
    * 
    * @param args the name of the map to draw (USA, CA, WA, WY, etc)
    */
   public static void main(String args[]) {
      if (args.length == 2)
         PApplet.main(new String[] { "cecs174.VisualizeElectionResults", args[0], args[1] });
      else {
         System.err.println("Usage: java cecs174.VisualizeElectionResults USA 2012 \nor: java cecs174.VisualizeElectionResults USA 2008");
         System.exit(1);
      }
   }
}