package com.coremedia.testing.junit;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@SuppressWarnings("InstanceMethodNamingConvention")
@ContextConfiguration
@RunWith(Parameterized.class)
public class SpringAwareParameterizedTest {
  @ClassRule
  public static final SpringAware SPRING_AWARE = SpringAware.forClass(SpringAwareParameterizedTest.class);
  private final Integer someInt;
  private final String someString;
  @Rule
  public TestRule springAwareMethod = SPRING_AWARE.forInstance(this);
  @Rule
  public TestName testName = new TestName();
  @Inject
  private ApplicationContext applicationContext;

  public SpringAwareParameterizedTest(Integer someInt, String someString) {
    this.someInt = someInt;
    this.someString = someString;
  }

  @Parameterized.Parameters(name = "{index}: {0}, {1}")
  public static Collection parameters() {
    return Arrays.asList(new Object[][] {
            {1, "good"},
            {2, "bad"},
            {3, "ugly"}
    });
  }

  @Test
  public void parameters_should_have_been_initialized() throws Exception {
    assertThat("Integer Parameter should be initialized.", someInt, notNullValue());
    assertThat("String Parameter should be initialized.", someString, notNullValue());
  }

  @Test
  public void beans_should_have_been_injected() throws Exception {
    assertThat("Bean ApplicationContext should exist.", applicationContext, notNullValue());
  }

  // Missing tests for example that dirty-context works. Perhaps use an ApplicationListener for this.
}
