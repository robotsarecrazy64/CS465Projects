import java.io.Serializable;

public class Message implements MessageTypes, Serializable
{
   int type;
   Object content;
   
   public Message(int type, Object content)
   {
      this.type = type;
      this.content = content;
   }
   
   public Message(int type)
   {
      this(type, null);
   }
   
   public void setType(int type)
   {
      this.type = type;
   }
   
   public int getType()
   {
      return type;
   }
   
   public void setContent(Object content)
   {
      this.content = content;
   }
   
   public Object getContent()
   {
      return content;
   }

}
