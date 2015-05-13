package org.inria.scale.streams.base;

import org.objectweb.proactive.Body;
import org.objectweb.proactive.RunActive;

public abstract class BaseInTap extends MulticastInStreamBindingController implements RunActive {

	private boolean firstTime = true;

	/**
	 * <p>
	 * Method for processing the actual reception of data from external sources
	 * and transforms them into tuples sending them to the next operator in the
	 * graph.
	 * </p>
	 * <p>
	 * <b>Note that this method should call the provided
	 * {@link MulticastInStreamBindingController#send(java.util.List)} method
	 * whenever a new batch of tuples is prepared to be sent.</b>
	 * </p>
	 */
	protected abstract void startStreaming();

	// //////////////////////////////////////////////
	// ******* RunActive *******
	// //////////////////////////////////////////////

	@Override
	public void runActivity(final Body body) {
		if (firstTime) {
			startStreaming();
			firstTime = false;
		}
	}

}
