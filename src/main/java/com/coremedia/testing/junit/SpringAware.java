package com.coremedia.testing.junit;

import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestContextManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * <p>
 * An alternative for using the {@link org.springframework.test.context.junit4.SpringJUnit4ClassRunner} for tests.
 * The rule approach allows to have other runners like for test-categorization or for data driven testing.
 * </p>
 * <p>
 * The solution got inspired by <a href="http://www.blog.project13.pl/index.php/coding/1077/runwith-junit4-with-both-springjunit4classrunner-and-parameterized/">&#64;RunWith JUnit4 with BOTH SpringJUnit4ClassRunner and Parameterized</a>.
 * </p>
 * <dl>
 * <dt><strong>Usage:</strong></dt>
 * <dd>
 * <pre>{@code
 * &#64;ClassRule
 * public static final SpringAware SPRING_AWARE = SpringAware.forClass(SpringAwareTest.class);
 * &#64;Rule
 * public TestRule springAwareMethod = SPRING_AWARE.forInstance(this);
 *     }</pre>
 * <p>
 * It is important to use {@link com.coremedia.testing.junit.SpringAware} as class and as instance rule
 * for proper initialization of the test-context.
 * </p>
 * </dd>
 * </dl>
 *
 * @see <a href="http://www.blog.project13.pl/index.php/coding/1077/runwith-junit4-with-both-springjunit4classrunner-and-parameterized/">&#64;RunWith JUnit4 with BOTH SpringJUnit4ClassRunner and Parameterized</a>
 * @since 2014-08-26
 */
public class SpringAware extends TestWatcher {
  private static final Logger LOG = LoggerFactory.getLogger(SpringAware.class);

  private static final ThreadLocal<Object> TEST_INSTANCE = new ThreadLocal<>();
  private static final ThreadLocal<Method> TEST_METHOD = new ThreadLocal<>();

  private final TestContextManager testContextManager;

  private SpringAware(@Nonnull final Class<?> testClass) {
    testContextManager = new TestContextManager(testClass);
  }

  public static SpringAware forClass(Class<?> testClass) {
    return new SpringAware(testClass);
  }

  @Nonnull
  private static Object getTestInstance() {
    return Objects.requireNonNull(TEST_INSTANCE.get(), "Test Initialization failure: Test Instance unknown.");
  }

  @Nonnull
  private static Method getTestMethod() {
    return Objects.requireNonNull(TEST_METHOD.get(), "Test Method undetermined. Wrong execution order?");
  }

  @Override
  protected void starting(final Description description) {
    try {
      if (description.isSuite()) {
        LOG.debug("Preparing test class {}.", description.getClassName());
        testContextManager.beforeTestClass();
      } else if (description.isTest()) {
        LOG.debug("Preparing test {}#{}.", description.getClassName(), description.getMethodName());
        TEST_METHOD.set(description.getTestClass().getMethod(description.getMethodName()));
        testContextManager.beforeTestMethod(getTestInstance(), getTestMethod());
      }
    } catch (Exception e) {
      throw new SpringAwareException(e);
    }
  }

  @Override
  protected void failed(final Throwable e, final Description description) {
    LOG.debug("Handling failure for {}.", description.getDisplayName());
    afterTest(e, description);
  }

  @Override
  protected void succeeded(final Description description) {
    LOG.debug("Handling success for {}.", description.getDisplayName());
    afterTest(null, description);
  }

  @Override
  protected void finished(final Description description) {
    LOG.debug("Handling finish for {}.", description.getDisplayName());
    if (description.isSuite()) {
      try {
        testContextManager.afterTestClass();
      } catch (Exception e) {
        throw new SpringAwareException("Failure executing TestContextManager's afterTestClass.", e);
      }
    }
  }

  private void afterTest(@Nullable final Throwable e, final Description description) {
    if (description.isTest()) {
      try {
        testContextManager.afterTestMethod(getTestInstance(), getTestMethod(), e);
      } catch (Exception afterTestMethodFailure) {
        throw new SpringAwareException("Failure calling TestContextManager's afterTestMethod.", afterTestMethodFailure);
      }
    }
  }

  public TestRule forInstance(final Object testInstance) {
    try {
      TEST_INSTANCE.set(testInstance);
      testContextManager.prepareTestInstance(testInstance);
    } catch (Exception e) {
      throw new SpringAwareException("Failed to prepare test instance.", e);
    }
    return this;
  }
}