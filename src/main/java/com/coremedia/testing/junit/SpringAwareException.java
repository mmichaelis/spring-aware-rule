package com.coremedia.testing.junit;

/**
 * Exception when setting up spring context.
 *
 * @since 2014-08-26
 */
public class SpringAwareException extends RuntimeException {
  public SpringAwareException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public SpringAwareException(final Throwable cause) {
    super(cause);
  }
}
