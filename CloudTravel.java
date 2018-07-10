
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CloudTravel {
	private ArrayList<Double> lat;
    private ArrayList<Double> lng;
    private HashMap<Integer, HashSet<Integer>> canTravel;

    /**
     * Initialised the values
     * @param lat
     * @param lng
     * @param canTravel
     */
    public CloudTravel(ArrayList<Double> lat, ArrayList<Double> lng, ArrayList<String> canTravel){
        this.lat = lat;
        this.lng = lng;
        this.canTravel = getCanTravelForEachAirport(canTravel);
    }

    /**
     * Set the mapping of source and possible destination of hashmap of integer 
     * to arraylist of integer in order of 1 
     * @param canTravelString
     * @return
     */
    private HashMap<Integer, HashSet<Integer>> getCanTravelForEachAirport(ArrayList<String> canTravelString){
        HashMap<Integer, HashSet<Integer>> canTravelIntegers = new HashMap<>();
        for(int i = 0; i < canTravelString.size(); i++){
            String destinationsString[] = canTravelString.get(i).split(" ");
            HashSet<Integer> destinationsInt = new HashSet<>();
            for (String pincodeLength : destinationsString) {
                try {
                    destinationsInt.add(Integer.parseInt(pincodeLength.trim()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            canTravelIntegers.put(i, destinationsInt);
        }

        return canTravelIntegers;
    }
    
    
    public double getMinimumDistance(int source, int destination){
        return getMinimumDistance(source, destination, new ArrayList<>(), -1);
    }
    
    /**
     * Recursive Function find the minimum  distance between source and destination
     * @param source
     * @param destination
     * @param routeTakenTillNow
     * @param currentMinimum
     * @return
     */
    private double getMinimumDistance(int source, int destination, ArrayList<Integer> routeTakenTillNow, double currentMinimum){
        if(source == destination){
            return 0;
        }
        routeTakenTillNow.add(source);
        ArrayList<Integer> remainingPossibleRoutesFromSourceToDestination = getRemainingRoutes(source, routeTakenTillNow);
        double minimumDistance = Integer.MAX_VALUE;
        for (Integer remainingPossibleRouteFromSourceToDestination : remainingPossibleRoutesFromSourceToDestination) {
            double distance = getDistance(source, remainingPossibleRouteFromSourceToDestination);
            double minDistance = getMinimumDistance(remainingPossibleRouteFromSourceToDestination, destination, routeTakenTillNow, minimumDistance);
            if (minDistance != -1 && minimumDistance > (minDistance + distance)) {
                minimumDistance = minDistance + distance;
            }
        }
        return minimumDistance == Integer.MAX_VALUE ? -1 : minimumDistance;
    }

    /**
     * Exclude the previous airport that has been taken.
     * @param source
     * @param routesTakenTillNow
     * @return
     */
    private ArrayList<Integer> getRemainingRoutes(int source, ArrayList<Integer> routesTakenTillNow) {
        HashSet<Integer> getAllPossibleRoutesFromDestination = canTravel.get(source);
        getAllPossibleRoutesFromDestination.removeAll(routesTakenTillNow);
        return new ArrayList<>(getAllPossibleRoutesFromDestination);
    }
    
    /**
     * Function calculate the distance between airport
     * @param source
     * @param destination
     * @return
     */
    public double getDistance(int source, int destination){
        int radius = 4000;

        double distance = radius *
                Math.acos(Math.sin(lat.get(source)) * Math.sin(lat.get(destination)) + Math.cos(lat.get(source))*Math.cos(lat.get(destination))*Math.cos(lng.get(source)-lng.get(destination)));


        return distance;
    }
    
    public static void main(String...x) {
    	ArrayList<Double> lat = new ArrayList<>();
    	ArrayList<Double> lng = new ArrayList<>();

    	lat.add(0.0);
    	lat.add(0.0);
    	lat.add(70.0);

    	lng.add(90.0);
    	lng.add(0.0);
    	lng.add(45.0);

    	ArrayList<String> canTravel = new ArrayList<>();
    	canTravel.add("2");
    	canTravel.add("0 2");
    	canTravel.add("0 1");

    	CloudTravel airport = new CloudTravel(lat, lng, canTravel);
    	System.out.println("Minimum Distance : " + airport.getMinimumDistance(0, 1));
    }
}

