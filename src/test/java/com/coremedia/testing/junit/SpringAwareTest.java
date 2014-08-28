package com.coremedia.testing.junit;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import javax.inject.Inject;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@SuppressWarnings("InstanceMethodNamingConvention")
@ContextConfiguration
public class SpringAwareTest {
  @ClassRule
  public static final SpringAware SPRING_AWARE = SpringAware.forClass(SpringAwareTest.class);
  @Rule
  public TestRule springAwareMethod = SPRING_AWARE.forInstance(this);
  @Rule
  public TestName testName = new TestName();
  @Inject
  private ApplicationContext applicationContext;

  @Test
  public void beans_should_have_been_injected() throws Exception {
    assertThat("Bean ApplicationContext should exist.", applicationContext, notNullValue());
  }

  // Missing tests for example that dirty-context works. Perhaps use an ApplicationListener for this.
}
