/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction.comm;
import java.io.Serializable;
/**
 *
 * @author Jessica Smith, Jesse Rodriguez, John Jacobelli
 */

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

