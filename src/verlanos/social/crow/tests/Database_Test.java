package verlanos.social.crow.tests;

import org.junit.Test;
import verlanos.social.crow.Database;
import verlanos.social.crow.User;

import java.io.IOException;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * This class serves as an Aggregator. It's purpose is to merge and display Timeline event data from
 * a number of users.
 *
 * @author Sefverl Balasingam
 * @since 05/08/2014
 */

public class Database_Test {

    String[] testUsers = {"John","@blak02","\\42678hello"};
    String[] messages = {"Such a nice day!","27489298 some rubbish", "\\4539djns9sjs!@#$&*$()(#?><"};
    String[] reversed_messages = {"\\4539djns9sjs!@#$&*$()(#?><","27489298 some rubbish","Such a nice day!"};

    @Test
    public void testUsersInserts(){
        Database.getInstance().open(Database.DBPATH,Database.DBNAME);
        cleanup();
        for(int i = 0; i<testUsers.length; i++){
            testUserInsert(testUsers[i],messages[i]);
        }
        Database.getInstance().close();
    }

    @Test
    public void testDuplicateUserInserts(){
        Database.getInstance().open(Database.DBPATH,Database.DBNAME);
        cleanup();
        for(int i = 0; i<testUsers.length; i++){
            testUserInsert(testUsers[i],reversed_messages[i]);
        }
        Database.getInstance().close();
    }


    private void testUserInsert(String username,String message){
        Database.getInstance().addMessage(username,message);
        User user = Database.getInstance().getUser(username);
        System.out.println(User.toText(user));
        System.out.println();
        assertEquals("User has been added successfully?",username,user.getUsername());
        System.out.println(user.getTimeline().getEvents().values()+" "+message);
        assertTrue("User timeline contains recent message?",user.getTimeline().getEvents().values().contains(message));
    }

    private void testDuplicateUserInsert(String username,String message){
        Database.getInstance().addMessage(username,message);
        User user = Database.getInstance().getUser(username);
        System.out.println(User.toText(user));
        System.out.println();
        assertEquals("User has been added successfully?",username,user.getUsername());
        System.out.println(user.getTimeline().getEvents().values()+" "+message);
        assertTrue("User timeline contains recent message?",user.getTimeline().getEvents().values().contains(message));
    }

    private void cleanup(){
        Database.getInstance().deleteDB(Database.DBNAME);
    }
}
