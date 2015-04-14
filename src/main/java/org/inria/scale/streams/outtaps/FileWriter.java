package org.inria.scale.streams.outtaps;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.inria.scale.streams.configuration.FileWriterConfiguration;
import org.javatuples.Tuple;

import com.google.common.base.Joiner;

public class FileWriter extends BaseOutTap implements FileWriterConfiguration {

	private String path;

	// //////////////////////////////////////////////
	// ******* BaseOutTap *******
	// //////////////////////////////////////////////

	@Override
	protected void processTuples(final List<Tuple> tuplesToProcess) {
		final Path directoryPath = FileSystems.getDefault().getPath(".", path);
		final Path filePath = FileSystems.getDefault().getPath(".", path, String.valueOf(System.currentTimeMillis()));

		try {
			Files.createDirectories(directoryPath);
			Files.createFile(filePath);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		try (final BufferedWriter writer = Files.newBufferedWriter(filePath, Charset.defaultCharset())) {
			for (final Tuple tuple : tuplesToProcess)
				writer.write(Joiner.on(" ").join(tuple));

		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	// //////////////////////////////////////////////
	// ******* FileWriterConfiguration *******
	// //////////////////////////////////////////////

	@Override
	public void setDirectoryPath(final String path) {
		this.path = path;
	}

	@Override
	public String getDirectoryPath() {
		return path;
	}

}
