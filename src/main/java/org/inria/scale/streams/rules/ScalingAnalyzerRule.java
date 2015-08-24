package org.inria.scale.streams.rules;

import org.inria.scale.streams.metrics.RequestReceptionSpeedMetric;
import org.objectweb.proactive.extensions.autonomic.controllers.analysis.Alarm;
import org.objectweb.proactive.extensions.autonomic.controllers.analysis.Rule;
import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.MonitorController;
import org.objectweb.proactive.extensions.autonomic.controllers.utils.Wrapper;

public class ScalingAnalyzerRule extends Rule {

	private static final long serialVersionUID = 1L;

	public ScalingAnalyzerRule() {
		subscribeToMetric(RequestReceptionSpeedMetric.DEFAULT_NAME);
	}

	@Override
	public Alarm check(final MonitorController monitor) {
		final Wrapper<Double> ws = monitor.getMetricValue(RequestReceptionSpeedMetric.DEFAULT_NAME);
System.out.println("ScalingAnalyzerRule!");
		if (ws.isValid()) {
//			final String[] split = ws.getValue().split("u");
//			if (split.length == 2) {
//				final double np1 = Double.parseDouble(split[0]);
//				final double np2 = Double.parseDouble(split[1]);
//				if (np1 != p1 || np2 != p2) {
//					p1 = np1;
//					p2 = np2;
//					return Alarm.VIOLATION;
//				}
				return Alarm.OK;
			}
			return Alarm.FAIL;
//		}
//		return Alarm.FAIL;
	}

}
