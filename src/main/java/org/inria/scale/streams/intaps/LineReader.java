package org.inria.scale.streams.intaps;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import org.inria.scale.streams.configuration.LineReaderConfiguration;
import org.javatuples.Unit;

public class LineReader extends BaseInTap implements LineReaderConfiguration {

	private String charset = "UTF-8";
	private String filePath;

	// //////////////////////////////////////////////
	// ******* BaseInTap *******
	// //////////////////////////////////////////////

	@Override
	protected void startStreaming() {
		final File file = new File(filePath);

		try (final Scanner iterator = new Scanner(file, charset)) {
			while (iterator.hasNext()) {
				send(Arrays.asList(Unit.with(iterator.nextLine())));
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
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

	@Override
	public void setCharset(final String charset) {
		this.charset = charset;
	}

	@Override
	public String getCharset() {
		return charset;
	}

}
