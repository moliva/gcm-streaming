package org.inria.scale.streams.plans;

import org.inria.scale.streams.metrics.RequestReceptionSpeedMetric;
import org.objectweb.proactive.extensions.autonomic.controllers.analysis.Alarm;
import org.objectweb.proactive.extensions.autonomic.controllers.execution.ExecutorController;
import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.MonitorController;
import org.objectweb.proactive.extensions.autonomic.controllers.planning.Plan;
import org.objectweb.proactive.extensions.autonomic.controllers.utils.Wrapper;

public class MapPlan extends Plan {

	private static final long serialVersionUID = 1L;

	@Override
	public void planActionFor(final String ruleName, final Alarm alarm, final MonitorController monitor,
			final ExecutorController executor) {

		if (alarm.equals(Alarm.VIOLATION)) {
			final Wrapper<Double> p = monitor.getMetricValue(RequestReceptionSpeedMetric.DEFAULT_NAME);
			if (p.isValid()) {
				// executor.execute("set-value($this/child::Balancer/attribute::points, \""
				// + p.getValue() + "\");");

			} else {
				System.out.println("monitor.getMetricValue(\"points\") invalid ???");
			}
		}
	}

}
