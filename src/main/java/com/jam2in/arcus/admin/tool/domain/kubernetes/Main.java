package com.jam2in.arcus.admin.tool.domain.kubernetes;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class Main {

  private static final String URL = "https://1.255.51.181:6443";
  private static final String TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZWZhdWx0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6ImFyY3VzLW9wZXJhdG9yLXRva2VuLTI0ZGhsIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImFyY3VzLW9wZXJhdG9yIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQudWlkIjoiNTM2NTRiMjUtY2ZiMC0xMWVhLWFlNjktZjIyMGNkNjgyN2NlIiwic3ViIjoic3lzdGVtOnNlcnZpY2VhY2NvdW50OmRlZmF1bHQ6YXJjdXMtb3BlcmF0b3IifQ.dvlMDTmA-ieX-BOM6H0qYrGGu59xCbL_v5zr56MfjYw7aOVSKkRms0LOvSzp3vtC6NXaf6Rp8X4sX5Mx4aOvV3979zw8pnqk2jrVNkvYnpRIn9iHzHHHKpyIDiex0sgvTySP_DHQNFcab4fUvgrweGXN78kdBWMRRcQ-gcrNQz6Nvs67FqnzBBeLPV2M4Qzn01-i6uydIWrV1wrD8igUphwWZPXtNDgTKxZr4cpD7KR9-YmESFebd-06qkulysb02GnVeUMTX7mp1oimwMBiaHE4Qx8VI_-wTKVtPqMMyPz8oXYgtKXCqqfcwM6dSWzhw8lepTDgcIVwOLEgRClAMw";


  public static void main(String[] args) {
    Config config = new ConfigBuilder().withOauthToken(TOKEN)
        .withMasterUrl(URL)
        .withTrustCerts(true)
        .withConnectionTimeout(5000)
        .withRequestTimeout(5000)
        .build();
    KubernetesClient client = new DefaultKubernetesClient(config);

    PodList podList = client.pods().list();
    for (Pod pod : podList.getItems()) {
      System.out.println(pod.toString());
    }
    client.close();

    /*
    while (true) {
      try {
        try {
          client.pods().list();
        } catch (KubernetesClientException e) {
          if (e.getCause() instanceof SocketTimeoutException) {
            System.out.println("Connect timeout");
          }
          client.pods().list();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
     */
  }

}
