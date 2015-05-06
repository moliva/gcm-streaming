package org.inria.scale.streams.outtaps;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.inria.scale.streams.InStream;
import org.inria.scale.streams.configuration.FileWriterConfiguration;
import org.javatuples.Tuple;

import com.google.common.base.Joiner;

public class FileWriter implements InStream, FileWriterConfiguration {

	private String path;
	private String prefix = "";
	private String postfix = "";

	// //////////////////////////////////////////////
	// ******* InStream *******
	// //////////////////////////////////////////////

	@Override
	public void receive(final List<Tuple> tuplesToProcess) {
		final Path directoryPath = FileSystems.getDefault().getPath(".", path, createDirectoryName());
		final Path filePath = directoryPath.resolve("part-00000");
		final Path successPath = directoryPath.resolve("_SUCCESS");

		try {
			Files.createDirectories(directoryPath);
			Files.createFile(filePath);

			try (final BufferedWriter writer = Files.newBufferedWriter(filePath, Charset.defaultCharset())) {
				for (final Tuple tuple : tuplesToProcess)
					writer.write(Joiner.on(" ").join(tuple) + "\n");
			}

			successPath.toFile().createNewFile();

		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private String createDirectoryName() {
		return prefix + (prefix.isEmpty() ? "" : "-") + String.valueOf(System.currentTimeMillis())
				+ (postfix.isEmpty() ? "" : "-") + postfix;
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

	@Override
	public String getPrefix() {
		return prefix;
	}

	@Override
	public void setPrefix(final String prefix) {
		this.prefix = prefix;
	}

	@Override
	public String getPostfix() {
		return postfix;
	}

	@Override
	public void setPostfix(final String postfix) {
		this.postfix = postfix;
	}

}
