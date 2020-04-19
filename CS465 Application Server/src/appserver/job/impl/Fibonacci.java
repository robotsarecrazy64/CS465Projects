package appserver.job.impl;

import appserver.job.Tool;

/**
 * Class [PlusOne] Simple POC class that implements the Tool interface
 * 
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class Fibonacci implements Tool{
    
    // go is Fibonacci calculator
    @Override
    public Object go(Object parameters) {
        
    	if ((Integer) parameters <= 1)
    	{
            return (Integer) parameters; 
    	}
    	
        return (Integer) go((Integer) parameters - 1) + (Integer) go((Integer) parameters - 2); 
    }
}
