package uk.gov.ons.fwmt.job_service_v2.helper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class TestReceiver {
  @Getter
  private String result;

  @Getter
  private int counter;

  public void reset() {
    counter = 0;
    result = "";
  }

  private void receiveMessage(String message) {
    counter++;
    System.out.println(message);
    result = message;
  }
}
