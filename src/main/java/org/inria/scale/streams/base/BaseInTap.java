package org.inria.scale.streams.base;

import org.inria.scale.streams.LifeCycleSelfAwareObject;
import org.inria.scale.streams.multiactivity.ComponentMultiActiveServiceFactory;
import org.inria.scale.streams.multiactivity.MultiActiveServiceFactory;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.RunActive;
import org.objectweb.proactive.multiactivity.MultiActiveService;

/**
 * Base abstraction for an implementation of an InTap. It handles the logic for
 * {@link RunActive running an activity} when the application is launched as
 * well providing the behavior for forwarding the generated tuples to the next
 * operation, inherited from {@link MulticastInStreamBindingController}.
 * 
 * @author moliva
 *
 */
public abstract class BaseInTap extends MulticastInStreamBindingController implements LifeCycleSelfAwareObject {

	private final MultiActiveServiceFactory multiActiveServiceFactory;
	private Thread thread;

	/**
	 * <p>
	 * Method for processing the actual reception of data from external sources
	 * and transforms them into tuples sending them to the next operator in the
	 * graph.
	 * </p>
	 * <p>
	 * <b>Note that this method should call the provided
	 * {@link MulticastInStreamBindingController#send(int, java.util.List)} method
	 * whenever a new batch of tuples is prepared to be sent.</b>
	 * </p>
	 * 
	 * @throws InterruptedException
	 */
	protected abstract void startStreaming() throws InterruptedException;

	public BaseInTap(final MultiActiveServiceFactory multiActiveServiceFactory) {
		this.multiActiveServiceFactory = multiActiveServiceFactory;
	}

	public BaseInTap() {
		this(new ComponentMultiActiveServiceFactory());
	}

	// //////////////////////////////////////////////
	// ******* RunActive *******
	// //////////////////////////////////////////////

	@Override
	public void onStart() {
		thread = new Thread("Streaming InTap") {
			@Override
			public void run() {
				try {
					startStreaming();
				} catch (final InterruptedException e) {
					System.err.println("Thread interrupted " + Thread.currentThread().getName());
				}
			}
		};

		thread.start();
	}

	@Override
	public void onStop() {
		if (thread != null) {
			thread.interrupt();
		}
	}

	private MultiActiveService createMultiActiveService(final Body body) {
		return multiActiveServiceFactory.createService(body);
	}

}
