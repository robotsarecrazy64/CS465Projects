/*
 * Interface [MessageTypes] Defines the different message types used in the
 * application. Any entity using objects of class Message needs to implement
 * this interface.
 * TODO: REWRITE ABOVE DESCRIPTION
 */
public interface MessageTypes
{
   public static final int OPEN_TRANSACTION = 1000;
   public static final int CLOSE_TRANSACTION = 1001;
   public static final int READ_REQUEST = 1002;
   public static final int WRITE_REQUEST = 1003;
}
