package woshilaiceshide.sserver.benchmark.sserver

object Client extends App {

  import java.io.File;
  import java.io.FileInputStream;
  import java.io.ByteArrayInputStream;

  import org.apache.http.client.methods.CloseableHttpResponse;
  import org.apache.http.client.methods.HttpPost;
  import org.apache.http.entity.ContentType;
  import org.apache.http.entity.InputStreamEntity;
  import org.apache.http.impl.client.CloseableHttpClient;
  import org.apache.http.impl.client.HttpClients;
  import org.apache.http.util.EntityUtils;

  val client = HttpClients.createDefault();
  try {
    for (i <- 0 to 10) {
      val repeated = 4096
      val post = new HttpPost(s"http://127.0.0.1:8787/chunked_request/${repeated}");
      val unit = Array[Byte](0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
      val bytes = new Array[Byte](repeated * unit.length)
      for (i <- 0 until repeated) {
        System.arraycopy(unit, 0, bytes, i * unit.length, unit.length)
      }

      val stream = new ByteArrayInputStream(bytes)

      val reqEntity = new InputStreamEntity(stream, -1, ContentType.APPLICATION_OCTET_STREAM);
      reqEntity.setChunked(true);
      post.setEntity(reqEntity);

      System.out.println("Executing request: " + post.getRequestLine());

      val response = client.execute(post);
      try {
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        System.out.println(EntityUtils.toString(response.getEntity()));
      } finally {
        response.close();
      }
    }
  } finally {
    client.close();
  }

}