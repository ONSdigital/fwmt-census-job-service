package uk.gov.ons.census.fwmt.jobservice.rest.client;

import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.aad.adal4j.ClientCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.jobservice.utils.JobServiceUtils;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class CometRestClient {
  private final static String RESOURCE = "https://int-ons-comet-api-app-ukwest.azurewebsites.net";

  //O365 credentials for authentication w/o login prompt

  //Azure Directory OAUTH 2.0 AUTHORIZATION ENDPOINT
  private final static String AUTHORITY = "https://login.microsoftonline.com/05057611-67c0-4390-85ef-2c623ff4104f/oauth2/v2.0/token";

  private transient RestTemplate restTemplate;
  private transient String cometURL;
  private transient String clientID;
  private transient String clientSecret;
  private transient AuthenticationResult auth;

  @Autowired
  public CometRestClient(
      RestTemplate restTemplate,
      @Value("${totalmobile.baseUrl}") String baseUrl,
      @Value("${totalmobile.operation.case.create.path}") String tmPath,
      @Value("${totalmobile.comet.clientID}") String clientID,
      @Value("${totalmobile.comet.clientSecret}") String clientSecret) {
    this.restTemplate = restTemplate;
    this.cometURL = baseUrl + tmPath;
    this.clientID = clientID;
    this.clientSecret = clientSecret;
    this.auth = null;
  }

  private boolean isAuthed() {
    return this.auth != null;
  }

  private boolean isExpired() {
    return !auth.getExpiresOnDate().after(new Date());
  }

  private void auth() throws GatewayException {
    ExecutorService service = Executors.newFixedThreadPool(1);
    try {
      AuthenticationContext context = new AuthenticationContext(AUTHORITY, false, service);
      ClientCredential cc = new ClientCredential(clientID, clientSecret);

      Future<AuthenticationResult> future = context.acquireToken(RESOURCE, cc, null);
      this.auth = future.get();
    } catch (MalformedURLException | InterruptedException | ExecutionException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, e);
    } finally {
      service.shutdown();
    }
  }

  public <A> void sendRequest(A caseRequest, String caseId) throws GatewayException {
    if ((!isAuthed() || isExpired()) && !clientID.isEmpty() && !clientSecret.isEmpty())
      auth();
    JobServiceUtils.printJSON(caseRequest);
    HttpHeaders httpHeaders = new HttpHeaders();
    if (isAuthed())
      httpHeaders.setBearerAuth(auth.getAccessToken());
    HttpEntity<?> body = new HttpEntity<>(caseRequest, httpHeaders);
    restTemplate.exchange(cometURL + caseId, HttpMethod.PUT, body, Void.class);
  }
}
