package org.inria.scale.streams.outtaps;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.inria.scale.streams.base.BaseOutTap;
import org.inria.scale.streams.configuration.FileWriterConfiguration;
import org.javatuples.Tuple;

import com.google.common.base.Joiner;

/**
 * <p>
 * OutTap for writing the tuples contents down to a set of files inside the
 * directory for the <code>directory path</code> specified. A
 * <code>prefix</code> and <code>postfix</code> for the filenames can be
 * defined.
 * </p>
 * <p>
 * Whenever a file is successfully written a file will be written inside the
 * directory for the processed batch indicating so with a
 * {@link FileWriter#DEFAULT_SUCCESS_FILENAME}.
 * </p>
 *
 * @author moliva
 *
 */
public class FileWriter extends BaseOutTap implements FileWriterConfiguration {

	public static final String DEFAULT_CONTENT_FILENAME = "part-00000";
	public static final String DEFAULT_SUCCESS_FILENAME = "_SUCCESS";

	public static final String DEFAULT_PREFIX = "";
	public static final String DEFAULT_POSTFIX = "";

	private String path;
	private String prefix = DEFAULT_PREFIX;
	private String postfix = DEFAULT_POSTFIX;

	// //////////////////////////////////////////////
	// ******* InStream *******
	// //////////////////////////////////////////////

	@Override
	public void process(final List<Tuple> tuplesToProcess) {
		final Path directoryPath = FileSystems.getDefault().getPath(".", path, createDirectoryName());
		final Path filePath = directoryPath.resolve(DEFAULT_CONTENT_FILENAME);
		final Path successPath = directoryPath.resolve(DEFAULT_SUCCESS_FILENAME);

		try {
			Files.createDirectories(directoryPath);
			Files.createFile(filePath);

			try (final BufferedWriter writer = Files.newBufferedWriter(filePath, Charset.defaultCharset())) {
				for (final Tuple tuple : tuplesToProcess) {
					writer.write(Joiner.on(" ").join(tuple) + "\n");
				}
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
