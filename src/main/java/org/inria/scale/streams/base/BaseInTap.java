package org.inria.scale.streams.base;

import org.objectweb.proactive.Body;
import org.objectweb.proactive.RunActive;

public abstract class BaseInTap extends MulticastInStreamBindingController implements RunActive {

	private boolean firstTime = true;

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
