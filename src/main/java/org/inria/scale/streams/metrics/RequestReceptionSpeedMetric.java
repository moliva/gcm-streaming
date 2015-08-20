package org.inria.scale.streams.metrics;

import java.util.List;

import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.event.RemmosEventType;
import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.metrics.Metric;
import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.records.Condition;
import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.records.IncomingRequestRecord;

public class RequestReceptionSpeedMetric extends Metric<Double> {

	private static final long serialVersionUID = 1L;
	private Double value;
	private final String itfName;

	public RequestReceptionSpeedMetric(final String interfaceName) {
		this.value = 0.0;
		this.itfName = interfaceName;

		subscribeTo(RemmosEventType.INCOMING_REQUEST_EVENT);
	}

	@Override
	public Double calculate() {

		final List<IncomingRequestRecord> records = recordStore
				.getIncomingRequestRecords(new Condition<IncomingRequestRecord>() {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean evaluate(final IncomingRequestRecord irr) {
						return irr.getInterfaceName().equals(itfName);
					}
				});

		// and calculates the average
		double sum = 0.0;
		final double nRecords = records.size();
		for (final IncomingRequestRecord irr : records) {
			if (irr.isFinished()) {
				sum += irr.getReplyTime() - irr.getArrivalTime();
			}
		}

		return value = nRecords > 0 ? sum / nRecords : 0;
	}

	@Override
	public Double getValue() {
		return this.value;
	}

	@Override
	public void setValue(final Double value) {
		this.value = value;
	}

}
