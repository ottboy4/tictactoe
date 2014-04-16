package ott.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses(
{
		ColorEffectsTest.class,
		ConvolutionEffectsTest.class,
		EdgeEffectsTest.class,
		LineDetectionTest.class,
		XTests.class})
public class AllTests
{

}
