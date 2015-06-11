package org.inria.scale.streams.windows;

import com.google.gson.Gson;

/**
 * Parser for the JSON representation (and modified for ADL compatibility) for
 * both the {@link WindowConfigurationObject} and the
 * {@link CombinatorConfigurationObject}.
 *
 * @author moliva
 *
 */
public class ConfigurationParser {

	public CombinatorConfigurationObject parseCombinatorConfiguration(final String json) {
		return parse(json, CombinatorConfigurationObject.class);
	}

	public WindowConfigurationObject parseWindowConfiguration(final String json) {
		return parse(json, WindowConfigurationObject.class);
	}

	private <T> T parse(final String json, final Class<T> clazz) {
		return new Gson().fromJson(json.replaceAll("\\(", "{").replaceAll("\\)", "}"), clazz);
	}

	public String serialize(final Object configuration) {
		return new Gson().toJson(configuration).replaceAll("\\{", "(").replaceAll("\\}", ")");
	}

}
