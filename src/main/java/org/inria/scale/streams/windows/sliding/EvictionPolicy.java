package org.inria.scale.streams.windows.sliding;

import org.inria.scale.streams.operators.Window;
import org.javatuples.Tuple;

/**
 * Defines a policy for storing and dropping tuples of the queue of the
 * collaborating {@link Window}.
 * 
 * @author moliva
 *
 */
public interface EvictionPolicy {

	/**
	 * Initializes the policy with the specified window. The policy should execute
	 * any check on the window or the queue that was required to prepare to the to
	 * policy working.
	 * 
	 * @param window
	 *          The window operator
	 */
	void initialize(Window window);

	/**
	 * Executes all the necessary procedures to shut down the policy before it's
	 * discarded.
	 */
	void tearDown();

	/**
	 * Executes the corresponding check for this policy if necessary. Adding the
	 * current <code>tuple</code> and dropping any when corresponding.
	 * 
	 * @param tuple
	 *          New tuple to be added
	 */
	void check(Tuple tuple);

}
