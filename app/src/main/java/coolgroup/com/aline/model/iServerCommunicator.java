package coolgroup.com.aline.model;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public interface iServerCommunicator {

    /**
     * Return the Firebase Database reference.
     */
    FirebaseDatabase getFirebaseDatabase();

    /**
     * Authenticate a user by email address and password.
     *
     * @param email    The email registered to the account.
     * @param password The user’s password.
     * @return True if the account exists and details are correct, else false.
     */
    void logInUserEmail(String email, String password, OnSuccessListener<User> listener);

    /**
     * Authenticate a user by phone number and password.
     *
     * @param phone    The phone number registered to the account.
     * @param password The user’s password.
     * @return True if the account exists and details are correct, else false.
     */
    void logInUserPhone(String phone, String password, OnSuccessListener<User> listener);

    /**
     * Register an account for a user.
     *
     * @param email    The user’s email address.
     * @param password The user’s selected password.
     * @param name     The user’s full name.
     * @param phone    The user’s phone number.
     * @return True if the user doesn’t already exist (e.g. email taken)
     * and the format of all arguments is valid (e.g. password length).
     */
    void signUpUser(String email, String password, String name, String phone, OnSuccessListener<User> listener);

    /**
     * Retrieve a user ID string.
     *
     * @param email The email of the user to be queried.
     * @param name  The name of the user to be queried.
     * @param phone The phone of the user to be queried.
     * @return The user ID if the user exists, else null.
     */
    String getUserId(String email, String name, String phone);

    /**
     * Retrieve the basic details of a user.
     *
     * @param userId The user ID of the user to be queried.
     * @return A User object corresponding to the user, if it exists (else null).
     */
    User getBasicUserInfo(String userId);

    /**
     * Retrieve all contacts of a user.
     *
     * @param userId The user to be queried.
     * @return An ArrayList of the user’s contacts.
     */
    List<String> getContactsList(String userId);

    /**
     * Retrieve all contacts of a user as User instances.
     *
     * @param userId The user to be queried.
     * @return An ArrayList of the user’s contacts as User instances.
     */
    List<User> getContactsUserList(String userId);

    /**
     * Add a new user to the list of contacts.
     *
     * @param userId        The user whose contact list is being updated.
     * @param contactUserId The user to be added.
     * @return True if the update was successful.
     */
    boolean addNewContact(String userId, String contactUserId);

    /**
     * Remove a user from the list of contacts.
     *
     * @param userId        The user whose contact list is being updated.
     * @param contactUserId The user to be removed.
     * @return True if the removal was successful.
     */
    boolean removeContact(String userId, String contactUserId);

}
