package org.inria.scale.streams.tests.unit.outtaps;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.inria.scale.streams.outtaps.FileWriter.DEFAULT_CONTENT_FILENAME;
import static org.inria.scale.streams.outtaps.FileWriter.DEFAULT_SUCCESS_FILENAME;
import static org.inria.scale.streams.tests.utils.TupleUtils.tupleWith;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.inria.scale.streams.outtaps.FileWriter;
import org.javatuples.Tuple;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FileWriterTest {

	private String directoryName;

	private final FileWriter operator = new FileWriter();

	@Before
	public void initializeDirectoryName() {
		directoryName = "test-" + new Random().nextLong();
		operator.setDirectoryPath(directoryName);
	}

	@After
	public void deleteDirectory() throws IOException {
		FileUtils.deleteDirectory(getDirectoryFile());
	}

	@Test
	public void shouldWriteTuplesJoinedByBlankPerLineInOrderInTheDirectorySpecifiedAndWithinTime() throws Exception {
		final List<Tuple> tuples = Arrays.asList( //
				tupleWith("a", 1), //
				tupleWith("b", 2), //
				tupleWith("c", 3));

		final long beforeMillis = System.currentTimeMillis();
		operator.process(tuples);
		final long afterMillis = System.currentTimeMillis();

		final List<File> outputDirectoryFiles = Arrays.asList(getDirectoryFile().listFiles());
		assertThat(outputDirectoryFiles, hasSize(1));

		final File resultsDirectory = outputDirectoryFiles.get(0);
		assertThat(resultsDirectory.getName(), is(inTheInterval(beforeMillis, afterMillis)));

		final List<String> resultFilenames = Arrays.asList(resultsDirectory.list());
		assertThat(resultFilenames, containsInAnyOrder(DEFAULT_SUCCESS_FILENAME, DEFAULT_CONTENT_FILENAME));

		final File contentFile = resultsDirectory.listFiles(withNameEqualToContentFile())[0];
		assertThat(FileUtils.readLines(contentFile), contains( //
				"a 1", //
				"b 2", //
				"c 3"));
	}

	@Test
	public void shouldUseThePrefixSpecifiedAndWithinTime() throws Exception {
		operator.setPrefix("prefix-here");

		final long beforeMillis = System.currentTimeMillis();
		operator.process(Arrays.<Tuple> asList());
		final long afterMillis = System.currentTimeMillis();

		final List<File> outputDirectoryFiles = Arrays.asList(getDirectoryFile().listFiles());
		assertThat(outputDirectoryFiles, hasSize(1));

		final File resultsDirectory = outputDirectoryFiles.get(0);
		assertThat(resultsDirectory.getName(), is(inTheInterval(beforeMillis, afterMillis)));
		assertThat(resultsDirectory.getName(), startsWith("prefix-here"));

		final List<String> resultFilenames = Arrays.asList(resultsDirectory.list());
		assertThat(resultFilenames, containsInAnyOrder(DEFAULT_SUCCESS_FILENAME, DEFAULT_CONTENT_FILENAME));
	}

	@Test
	public void shouldUseThePostfixSpecifiedAndWithinTime() throws Exception {
		operator.setPostfix("postfix-here");

		final long beforeMillis = System.currentTimeMillis();
		operator.process(Arrays.<Tuple> asList());
		final long afterMillis = System.currentTimeMillis();

		final List<File> outputDirectoryFiles = Arrays.asList(getDirectoryFile().listFiles());
		assertThat(outputDirectoryFiles, hasSize(1));

		final File resultsDirectory = outputDirectoryFiles.get(0);
		assertThat(resultsDirectory.getName(), is(inTheInterval(beforeMillis, afterMillis)));
		assertThat(resultsDirectory.getName(), endsWith("postfix-here"));

		final List<String> resultFilenames = Arrays.asList(resultsDirectory.list());
		assertThat(resultFilenames, containsInAnyOrder(DEFAULT_SUCCESS_FILENAME, DEFAULT_CONTENT_FILENAME));
	}

	@Test
	public void shouldUseBothThePrefixAndThePostfixSpecifiedAndWithinTime() throws Exception {
		operator.setPrefix("prefix-here");
		operator.setPostfix("postfix-here");

		final long beforeMillis = System.currentTimeMillis();
		operator.process(Arrays.<Tuple> asList());
		final long afterMillis = System.currentTimeMillis();

		final List<File> outputDirectoryFiles = Arrays.asList(getDirectoryFile().listFiles());
		assertThat(outputDirectoryFiles, hasSize(1));

		final File resultsDirectory = outputDirectoryFiles.get(0);
		assertThat(resultsDirectory.getName(), allOf( //
				is(inTheInterval(beforeMillis, afterMillis)), //
				startsWith("prefix-here"), //
				endsWith("postfix-here")));

		final List<String> resultFilenames = Arrays.asList(resultsDirectory.list());
		assertThat(resultFilenames, containsInAnyOrder(DEFAULT_SUCCESS_FILENAME, DEFAULT_CONTENT_FILENAME));
	}

	// //////////////////////////////////////////////
	// ******* Utils *******
	// //////////////////////////////////////////////

	private FilenameFilter withNameEqualToContentFile() {
		return new FilenameFilter() {

			@Override
			public boolean accept(final File dir, final String name) {
				return DEFAULT_CONTENT_FILENAME.equals(name);
			}
		};
	}

	private TypeSafeMatcher<String> inTheInterval(final long beforeMillis, final long afterMillis) {
		return new TypeSafeMatcher<String>() {

			@Override
			public void describeTo(final Description description) {
				// TODO - describe the error
			}

			@Override
			protected boolean matchesSafely(final String filename) {
				final String strippedFilename = stripPrefixAndSuffix(filename);
				final long filenameLong = Long.parseLong(strippedFilename);
				return beforeMillis <= filenameLong && filenameLong <= afterMillis;
			}

			private String stripPrefixAndSuffix(final String filename) {
				final Pattern pattern = Pattern.compile("(.+-)?(?<number>\\d+)(-.+)?");
				final Matcher matcher = pattern.matcher(filename);
				matcher.find();
				return matcher.group("number");
			}
		};
	}

	private File getDirectoryFile() {
		final Path pathDir = FileSystems.getDefault().getPath(".", directoryName);
		return pathDir.toFile();
	}
}
