package coolgroup.com.aline;

import coolgroup.com.aline.Model.FirebaseCommunicator;
import coolgroup.com.aline.Model.iServerCommunicator;

public class Controller {

    /* Follows Singleton design pattern */
    private static Controller instance = new Controller();

    // Controlled classes
    public iServerCommunicator serverCommunicator = new FirebaseCommunicator(); // Not sure if this should be of type iServerCommunicator or FirebaseCommunicator?

    public Controller() {
    }

    public static Controller getInstance() {
        if (instance == null) {
            Controller.instance = new Controller();
        }
        return instance;
    }

}
