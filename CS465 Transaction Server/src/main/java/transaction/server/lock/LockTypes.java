package transaction.server.lock;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jessica Smith, Jesse Rodriguez, John Jacobelli
 */

// defines read and write locks
public interface LockTypes
{
	
	public static final int READ_LOCK = 1;
	
	public static final int WRITE_LOCK = 2;
	
	public static final int EMPTY_LOCK = 3;
}
