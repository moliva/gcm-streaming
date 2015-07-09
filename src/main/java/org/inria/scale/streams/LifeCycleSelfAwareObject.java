package org.inria.scale.streams;

public interface LifeCycleSelfAwareObject {

	final String ITF_NAME = "lifecycle-self-aware";

	void onStart();

	void onStop();

}
