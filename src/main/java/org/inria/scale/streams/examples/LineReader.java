package org.inria.scale.streams.examples;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.inria.scale.streams.InStream;
import org.inria.scale.streams.InTap;
import org.inria.scale.streams.configuration.LineReaderConfiguration;
import org.javatuples.Tuple;
import org.javatuples.Unit;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.RunActive;

public class LineReader implements InTap, LineReaderConfiguration, BindingController, RunActive {

	private long batchIntervalMilliseconds;
	private InStream out;
	private String filePath;

	private final Queue<Tuple> lines = new ConcurrentLinkedQueue<>();
	private boolean firstTime = true;

	// //////////////////////////////////////////////
	// ******* InTap *******
	// //////////////////////////////////////////////

	@Override
	public void runActivity(final Body body) {
		if (firstTime) {
			final Thread thread = new Thread("consuming thread") {
				@Override
				public void run() {
					while (true) {
						try {
							Thread.sleep(batchIntervalMilliseconds);
						} catch (final InterruptedException e) {
							e.printStackTrace();
						}

						final List<Tuple> tuplesToSend = new ArrayList<>(lines);
						lines.removeAll(tuplesToSend);

						if (!tuplesToSend.isEmpty())
							out.receive(tuplesToSend);
					}
				}
			};
			thread.start();

			final File file = new File(filePath);

			LineIterator iterator = null;
			try {
				iterator = FileUtils.lineIterator(file, "UTF-8");

				while (iterator.hasNext())
					lines.add(Unit.with(iterator.nextLine()));

			} catch (final IOException e) {
				e.printStackTrace();
			} finally {
				LineIterator.closeQuietly(iterator);
			}

			firstTime = false;
		}
	}

	// //////////////////////////////////////////////
	// ******* WindowConfiguration *******
	// //////////////////////////////////////////////

	@Override
	public void setBatchInterval(final long batchIntervalMilliseconds) {
		this.batchIntervalMilliseconds = batchIntervalMilliseconds;
	}

	@Override
	public long getBatchInterval() {
		return batchIntervalMilliseconds;
	}

	// //////////////////////////////////////////////
	// ******* BindingController *******
	// //////////////////////////////////////////////

	@Override
	public String[] listFc() {
		return new String[] { "out" };
	}

	@Override
	public Object lookupFc(final String clientItfName) throws NoSuchInterfaceException {
		if (clientItfName.equals("out"))
			return out;
		return null;
	}

	@Override
	public void bindFc(final String clientItfName, final Object serverItf) throws NoSuchInterfaceException,
	IllegalBindingException, IllegalLifeCycleException {
		if (clientItfName.equals("out"))
			out = (InStream) serverItf;
	}

	@Override
	public void unbindFc(final String clientItfName) throws NoSuchInterfaceException, IllegalBindingException,
	IllegalLifeCycleException {
		if (clientItfName.equals("out"))
			out = null;
	}

	// //////////////////////////////////////////////
	// ******* LineReaderConfiguration *******
	// //////////////////////////////////////////////

	@Override
	public void setFilePath(final String filePath) {
		this.filePath = filePath;
	}

	@Override
	public String getFilePath() {
		return filePath;
	}

}
