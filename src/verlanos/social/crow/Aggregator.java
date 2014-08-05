package verlanos.social.crow;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This class serves as an Aggregator. It's purpose is to merge and display Timeline event data from
 * a number of users.
 *
 * @author Sefverl Balasingam
 * @since 05/08/2014
 */


public class Aggregator {
    private static Aggregator instance = null;
    protected Aggregator(){
        // Prevent instantiation
    }
    public static Aggregator getInstance(){
        if(instance == null){
            instance = new Aggregator();
        }

        return instance;
    }

    /**
     * Prints all events of the specified User's Timeline to standard output
     * <p>
     *     This method retrieves the Timeline of the specified User and prints each entry to
     *     standard output in chronological order
     * </p>
     * @param username username of the User
     */
    public void printTimeline(String username){
        Database.getInstance().open();
        User user = Database.getInstance().getUser(username);
        if(!user.equals(User.EMPTY_USER)){
            Map<Long,String> timelineEvents = user.getTimeline().getEventsRelativeToCurrentTime(null);

            for(Map.Entry<Long,String> entry : timelineEvents.entrySet()){
                System.out.println(entry.getValue());
            }
        }
        else{
            System.out.println("ERROR: No such user");
        }
        Database.getInstance().close();
    }

    /**
     * Prints all events of Users the specified User is following in addition to the User's own Timeline.
     * Each Timeline entry is prefixed with the username of the owner.
     * <p>
     *     This method retrieves, merges and prints all Timeline entries of the specified User and those of
     *     who he/she is following in chronological order.
     * </p>
     * @param username username of the User
     */
    public void printWall(String username){
        Database.getInstance().open();
        User user = Database.getInstance().getUser(username);
        if(!user.equals(User.EMPTY_USER)){
            String[] followees = user.getFollowees().toArray(new String[user.getFollowees().size()]);

            SortedMap<Long,String> wallFeed = new TreeMap<Long,String>(user.getTimeline().getEventsRelativeToCurrentTime(username));

            for (String followee : followees){
                User interestee = Database.getInstance().getUser(followee);
                if(!interestee.equals(User.EMPTY_USER)){
                    wallFeed.putAll(interestee.getTimeline().getEventsRelativeToCurrentTime(followee));
                }
            }

            for(Map.Entry<Long,String> event : wallFeed.entrySet()){
                System.out.println(event.getValue());
            }
        }

    }
}
