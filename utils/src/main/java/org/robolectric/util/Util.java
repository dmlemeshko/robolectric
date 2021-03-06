package org.robolectric.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic collection of utility methods.
 */
public class Util {

  /**
   * Returns the Java version as an int value.
   *
   * @return the Java version as an int value (8, 9, etc.)
   */
  public static int getJavaVersion() {
    String version = System.getProperty("java.version");
    assert version != null;
    if (version.startsWith("1.")) {
      version = version.substring(2);
    }
    // Allow these formats:
    // 1.8.0_72-ea
    // 9-ea
    // 9
    // 9.0.1
    int dotPos = version.indexOf('.');
    int dashPos = version.indexOf('-');
    return Integer.parseInt(
        version.substring(0, dotPos > -1 ? dotPos : dashPos > -1 ? dashPos : 1));
  }

  public static void copy(InputStream in, OutputStream out) throws IOException {
    byte[] buffer = new byte[8196];
    int len;
    try {
      while ((len = in.read(buffer)) != -1) {
        out.write(buffer, 0, len);
      }
    } finally {
      in.close();
    }
  }

  /**
   * This method consumes an input stream and returns its content.
   *
   * @param is The input stream to read from.
   * @return The bytes read from the stream.
   * @throws IOException Error reading from stream.
   */
  public static byte[] readBytes(InputStream is) throws IOException {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream(is.available())) {
      copy(is, bos);
      return bos.toByteArray();
    }
  }

  public static <T> T[] reverse(T[] array) {
    for (int i = 0; i < array.length / 2; i++) {
      int destI = array.length - i - 1;
      T o = array[destI];
      array[destI] = array[i];
      array[i] = o;
    }
    return array;
  }

  public static File file(String... pathParts) {
    return file(new File("."), pathParts);
  }

  public static File file(File f, String... pathParts) {
    for (String pathPart : pathParts) {
      f = new File(f, pathPart);
    }

    String dotSlash = "." + File.separator;
    if (f.getPath().startsWith(dotSlash)) {
      f = new File(f.getPath().substring(dotSlash.length()));
    }

    return f;
  }

  @SuppressWarnings("NewApi")
  public static Path pathFrom(URL localArtifactUrl) {
    try {
      return Paths.get(localArtifactUrl.toURI());
    } catch (URISyntaxException e) {
      throw new RuntimeException("huh? " + localArtifactUrl, e);
    }
  }

  public static List<Integer> intArrayToList(int[] ints) {
    List<Integer> youSuckJava = new ArrayList<>();
    for (int attr1 : ints) {
      youSuckJava.add(attr1);
    }
    return youSuckJava;
  }

  public static int parseInt(String valueFor) {
    if (valueFor.startsWith("0x")) {
      return Integer.parseInt(valueFor.substring(2), 16);
    } else {
      return Integer.parseInt(valueFor, 10);
    }
  }
}
