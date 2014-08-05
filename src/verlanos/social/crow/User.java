package verlanos.social.crow;

import java.io.Serializable;
import java.util.*;

/**
 * This class serves as datastructure to represent a User. Each User has a Timeline and
 * a list of other Users he/she is following.
 *
 * @author Sefverl Balasingam
 * @since 05/08/2014
 */

public class User implements Serializable{

    private Timeline timeline;
    private String username;
    private Set<String> following;

    // CONSTANTS
    public static final User EMPTY_USER = new User("NONE");
    public static final User DUPLICATE_USER = new User("DUPLICATE");

    public User(String username){
        this.username = username;
        this.timeline = new Timeline();
        this.following = new HashSet<String>();
    }

    public User(String username,Timeline timeline,Set<String> following)
    {
        this(username);
        this.timeline = timeline;
        this.following = following;
    }

    public User(String username,Timeline timeline,String[] following){
          this(username,timeline,new HashSet<String>(Arrays.asList(following)));
    }

    /**
     * Follows another User with the specified username
     * <p>
     *     This method adds the specified username of a User to be followed to an internal Set,
     *     which prevents a User being followed more than once.
     * </p>
     * @param username name of the other User to be followed
     */
    public void follow(String username){
        this.following.add(username);
    }

    /**
     * Returns the username of this User
     * @return username
     */
    public String getUsername(){return this.username;}

    /**
     * Returns the Timeline of this User
     * @return Timeline instance
     */
    public Timeline getTimeline(){ return this.timeline; }

    /**
     * Returns a set of usernames of all Users being followed by this user
     * @return
     */
    public Set<String> getFollowees(){return this.following;}

    /**
     * Returns a String representation of the specified User
     * @param user a User instance
     * @return String representation
     */
    public static String toText(User user){
        StringBuilder description = new StringBuilder();
        description.append(user.getUsername()).append("\n\n");
        description.append(Timeline.toText(user.getTimeline())).append("\n");
        description.append("Follows: ").append(user.getFollowees().toString());
        return description.toString();
    }
}
