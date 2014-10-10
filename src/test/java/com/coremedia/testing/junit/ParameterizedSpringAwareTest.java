package com.coremedia.testing.junit;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import javax.inject.Inject;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@ContextConfiguration
@RunWith(Parameterized.class)
public class ParameterizedSpringAwareTest {

  @ClassRule
  public static final SpringAware SPRING_AWARE =
      SpringAware.forClass(MethodHandles.lookup().lookupClass());
  @Rule
  public TestRule springAwareMethod =
      SPRING_AWARE.forInstance(this);
  @Inject
  private ApplicationContext applicationContext;

  @Parameterized.Parameters(name = "\"{0}\".equals({1})")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {"yes", "yes"},
        {"no", "no"}
    });
  }

  private final String input;
  private final String expected;

  public ParameterizedSpringAwareTest(String input, String expected) {
    this.input = input;
    this.expected = expected;
  }

  @Test
  public void test() {
    assertThat(input, is(expected));
    assertThat("Bean ApplicationContext should exist.", applicationContext, notNullValue());
  }
}
