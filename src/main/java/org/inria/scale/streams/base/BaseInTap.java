package org.inria.scale.streams.base;

import org.inria.scale.streams.multiactivity.MultiActiveServiceFactory;
import org.inria.scale.streams.multiactivity.MultiActiveServiceFactoryImpl;
import org.inria.scale.streams.windows.StaticWindowStrategyFactory;
import org.inria.scale.streams.windows.WindowStrategyFactory;
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
public abstract class BaseInTap extends MulticastInStreamBindingController implements RunActive {

	private boolean firstTime = true;
	private final MultiActiveServiceFactory multiActiveServiceFactory;

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
	 */
	protected abstract void startStreaming();

	public BaseInTap(final MultiActiveServiceFactory multiActiveServiceFactory) {
		this.multiActiveServiceFactory = multiActiveServiceFactory;
	}

	public BaseInTap() {
		this(new MultiActiveServiceFactoryImpl());
	}

	// //////////////////////////////////////////////
	// ******* RunActive *******
	// //////////////////////////////////////////////

	@Override
	public void runActivity(final Body body) {
		final Thread thread = new Thread("Streaming InTap") {
			@Override
			public void run() {
				if (firstTime) {
					startStreaming();
					firstTime = false;
				}
			}
		};

		thread.start();

		final MultiActiveService service = createMultiActiveService(body);
		while (body.isActive()) {
			service.multiActiveServing();
		}
		
		thread.stop();
	}

	private MultiActiveService createMultiActiveService(final Body body) {
		return multiActiveServiceFactory.createService(body);
	}

}
