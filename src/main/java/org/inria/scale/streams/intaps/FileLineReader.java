package org.inria.scale.streams.intaps;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import org.inria.scale.streams.base.BaseInTap;
import org.inria.scale.streams.configuration.FileLineReaderConfiguration;
import org.javatuples.Unit;

/**
 * <p>
 * InTap for reading from a certain file specified by the
 * {@link FileLineReaderConfiguration}. It generates a tuple for each line read.
 * </p>
 * <p>
 * By default the character set to be used is <code>UTF-8</code>.
 * </p>
 * 
 * @author moliva
 *
 */
public class FileLineReader extends BaseInTap implements FileLineReaderConfiguration {

	private String charset = "UTF-8";
	private String filePath;

	// //////////////////////////////////////////////
	// ******* BaseInTap *******
	// //////////////////////////////////////////////

	@Override
	protected void startStreaming() {
		final File file = new File(filePath);

//		try (final Scanner iterator = new Scanner(file, charset)) {
//			while (iterator.hasNext()) {
//				send(Arrays.asList(Unit.with(iterator.nextLine())));
//			}
//		} catch (final IOException e) {
//			e.printStackTrace();
//		}
		Scanner iterator = null;
		try {
			iterator= new Scanner(file, charset);
			while (iterator.hasNext()) {
				send(Arrays.asList(Unit.with(iterator.nextLine())));
			}
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (iterator != null) {
				iterator.close();
			}
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
