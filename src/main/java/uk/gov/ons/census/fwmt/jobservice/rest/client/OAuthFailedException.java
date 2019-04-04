package uk.gov.ons.census.fwmt.jobservice.rest.client;

public class OAuthFailedException extends Exception {
  public OAuthFailedException(Throwable cause) {
    super(cause);
  }
}
