import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class TransactionManagerWorker extends Thread {
    /**
        Class Variables
    */
    public Socket clientSocket;
    public ObjectInputStream readInput;
    public ObjectOutputStream sendOutput;
    public Message message;
    public Transaction currentTransaction;
    public int accountNum = 0;
    public int accountBalance = 0;
    public boolean isActive = true;

    /**
        Default Constructor
    */
    private TransactionManagerWorker(Socket socket) {
    try {
        // try to get input from the client
    }
    catch (IOexception error) {
        // print the exception of one occured
    }
    
    @Override
    public void run(){
        while(isActive) {
            try {
                 // read message if available
            }
            catch (Exception error) {
                // exit
            }
        }

        switch() {
            case 1: // Open request
                break;
            case 2: // Close Request
                try {
                    // try to close the connection
                }
                catch (IOException error) {
                    
                }
                if() {
                    
                }
                break;
            case 3: // read request
                break;
        }
    }
}