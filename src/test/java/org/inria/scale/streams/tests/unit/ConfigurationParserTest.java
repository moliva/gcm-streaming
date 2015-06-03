package org.inria.scale.streams.tests.unit;

import org.inria.scale.streams.base.CombinatorConfigurationObject;
import org.inria.scale.streams.base.ConfigurationParser;
import org.inria.scale.streams.base.WindowConfigurationObject;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class ConfigurationParserTest {

	private final ConfigurationParser parser = new ConfigurationParser();

	@Test
	public void parseValidJsonWindowConfigurationSuccessfully() throws Exception {
		final WindowConfigurationObject configuration = parser.parseWindowConfiguration( //
				"{ type: \"tumbling\", " //
						+ "milliseconds: 2000 }" //
				);

		assertThat(configuration.getType(), is(equalTo("tumbling")));
		assertThat(configuration.getMilliseconds(), is(equalTo(2000l)));
	}

	@Test
	public void parseModifiedJsonWindowConfigurationSuccessfully() throws Exception {
		final WindowConfigurationObject configuration = parser.parseWindowConfiguration( //
				"( type: \"tumbling\", " //
						+ "timeBetweenExecutions: 2000 )" //
				);

		assertThat(configuration.getType(), is(equalTo("tumbling")));
		assertThat(configuration.getMilliseconds(), is(equalTo(2000l)));
	}

	@Test
	public void parseModifiedJsonCombinatorConfigurationSuccessfully() throws Exception {
		final CombinatorConfigurationObject configuration = parser.parseCombinatorConfiguration( //
				"( timeBetweenExecutions: 2000 )" //
				);

		assertThat(configuration.getTimeBetweenExecutions(), is(equalTo(2000l)));
	}

}
