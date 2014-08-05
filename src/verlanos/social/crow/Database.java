package verlanos.social.crow;

import org.mapdb.*;

import java.io.File;
import java.util.*;

/**
 * This class is fully responsible for interacting with a DBM used to store and retrieve Users. MapDB is used to store
 * User instances in serialised form for easy storage and retrieval, while maintaining persistence.
 * There will always be only one Database instance.
 *
 * @author Sefverl Balasingam
 * @since 05/08/2014
 */

public class Database {
    private static Database instance = null;
    public static String DBPATH = "crows_db";
    public static String DBNAME = "crows";

    protected Database(){
        // Prevent instantiation
    }
    public static Database getInstance(){
        if(instance == null){
            instance = new Database();
        }

        return instance;
    }

    // FIELDS
    private Map<String,User> users;
    private String currentPath,currentDB;
    private DB db;

    /**
     * Connects to the default MapDB database
     * <p>
     *     This method connects to a database and creates a handle for retrieval and storage. It connects to the
     *     default database and table.
     * </p>
     */
    public void open(){
        open(DBPATH,DBNAME);
    }

    /**
     * Connects to a MapDB database
     * <p>
     *     This method connects to a database and creates a handle for retrieval and storage.
     * </p>
     * @param filename path to the MapDB database file. If the file doesn't exist, a database file will be generated.
     * @param recordName name of the collection/table to open
     */
    public void open(String filename,String recordName) {
        currentPath = filename;
        currentDB = recordName;

            db = DBMaker.newFileDB(new File(filename))
                    .transactionDisable()
                    .cacheDisable()
                    .asyncWriteEnable()
                    .commitFileSyncDisable()
                    .closeOnJvmShutdown()
                    .make();

            this.users = db.getTreeMap(recordName);
    }

    /**
     * Returns a User with the specified username
     * <b>NOTE:</b><p>Requires a prior call to {@link #open()} </p>
     * @param username username of User to retrieve
     * @return User if found, otherwise User.EMPTY_USER
     */
    public User getUser(String username){
        return (users.containsKey(username) ? users.get(username) : User.EMPTY_USER);
    }

    /**
     * Returns a list of Users with the specified usernames.
     * <p>
     *     This method uses @see getUser() to retrieve each User. The list may contain User.EMPTY_USER if a username
     *     doesn't exist
     * </p>
     * <b>NOTE:</b><p>Requires a prior call to {@link #open()} </p>
     * @param usernames one or more usernames
     * @return list of User instances.
     */
    public List<User> getUsers(String ... usernames){
        List<User> requested = new ArrayList<User>();

        for (String username : usernames){
            requested.add(this.getUser(username));
        }
        return requested;
    }

    /**
     * Returns a Collection of all Users
     * <b>NOTE:</b><p>Requires a prior call to {@link #open()} </p>
     * @return a Collection of Users
     */
    public Collection<User> getAllUsers(){
        return this.users.values();
    }

    public void addMessage(String username,String message){
        if(this.users.containsKey(username)){
            User user = this.users.get(username);
            user.getTimeline().addEvent(message);
            this.users.put(username,user);
        }
        else{
            User user = new User(username);
            user.getTimeline().addEvent(message);
            this.users.put(username,user);
        }
        this.db.commit();
    }

    /**
     * Adds a User with given username to the list of followed User's of specified User
     * <b>NOTE:</b><p>Requires a prior call to {@link #open()} </p>
     * @param username username of the User who likes to follow
     * @param followee username of User to be followed
     */
    public void addFollowee(String username,String followee){
        if(!username.equals(followee) && this.users.containsKey(username) && this.users.containsKey(followee)){
            User user = this.users.get(username);
            user.follow(followee);
            this.users.put(username,user);
            this.db.commit();
        }
        else{
            System.out.println("ERROR: Either you or the other user don't exist yet");
        }
    }

    /**
     * Closes the active database session
     * <b>NOTE:</b><p>Requires a prior call to {@link #open()} </p>
     */
    public void close(){
        this.db.close();
    }

    /**
     * Deletes a MapDB collection
     * <b>NOTE:</b><p>Requires a prior call to {@link #open()} </p>
     * @param collection_name name of the collection to be deleted
     */
    public void deleteDB(String collection_name){
        this.db.delete(collection_name);
    }
}
