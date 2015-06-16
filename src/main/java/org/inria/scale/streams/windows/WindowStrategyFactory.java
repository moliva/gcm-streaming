package org.inria.scale.streams.windows;

/**
 * Creates {@link WindowStrategy window strategies} based on a window
 * configuration object or serialized JSON version.
 * 
 * @author moliva
 *
 */
public interface WindowStrategyFactory {

	public WindowStrategy createFrom(String windowConfigurationJson);

	public WindowStrategy createFrom(WindowConfigurationObject windowConfiguration);

}
