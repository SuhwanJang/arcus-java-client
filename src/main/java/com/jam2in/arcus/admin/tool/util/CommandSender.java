package com.jam2in.arcus.admin.tool.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import com.google.common.net.HostAndPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandSender {

  //in milliseconds, socket should connect/read within this period otherwise SocketTimeoutException
  private static final int DEFAULT_SOCKET_TIMEOUT = 5000;

  /**
   * Send the command.
   *
   * @param address the address(host:port)
   * @param cmd     the command string
   * @param timeout in milliseconds, maximum time to wait while connecting/reading data
   * @return server response
   * @throws java.io.IOException if a problem occurs.
   */
  @SuppressWarnings("UnstableApiUsage")
  public static String send(String address, String cmd, int timeout)
      throws IOException {
    try {
      HostAndPort hostAndPort = HostAndPort.fromString(address);
      return send(hostAndPort.getHost(), hostAndPort.getPort(), cmd, timeout);
    } catch (IllegalStateException e) {
      log.error("wrong address format. address = " + address);
      throw e;
    }
  }

  /**
   * Send the command.
   *
   * @param host the destination host
   * @param port the destination port
   * @param cmd  the command string
   * @return server response
   * @throws java.io.IOException if a problem occurs.
   */
  public static String send(String host, int port, String cmd)
      throws IOException {
    return send(host, port, cmd, DEFAULT_SOCKET_TIMEOUT);
  }

  /**
   * Send the command.
   *
   * @param host    the destination host
   * @param port    the destination port
   * @param cmd     the command string
   * @param timeout in milliseconds, maximum time to wait while connecting/reading data
   * @return server response
   * @throws java.io.IOException if a problem occurs.
   */
  public static String send(String host, int port, String cmd, int timeout)
      throws IOException {
    log.info("connecting to " + host + " " + port + " cmd = " + cmd);
    Socket sock = new Socket();
    InetSocketAddress hostaddress = host != null ? new InetSocketAddress(host, port) :
        new InetSocketAddress(InetAddress.getByName(null), port);
    BufferedReader reader = null;
    try {
      sock.setSoTimeout(timeout);
      sock.connect(hostaddress, timeout);
      OutputStream outstream = sock.getOutputStream();
      outstream.write(cmd.getBytes());
      outstream.flush();
      // this replicates NC - close the output stream before reading
      sock.shutdownOutput();

      reader =
          new BufferedReader(
              new InputStreamReader(sock.getInputStream()));
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line).append("\n");
      }
      return sb.toString();
    } catch (SocketTimeoutException e) {
      throw new IOException("Exception while executing command: " + cmd, e);
    } finally {
      sock.close();
      if (reader != null) {
        reader.close();
      }
    }
  }

}
