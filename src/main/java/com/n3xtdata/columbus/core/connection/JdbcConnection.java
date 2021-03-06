/*
 * Copyright 2018 https://github.com/n3xtdata
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.n3xtdata.columbus.core.connection;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.n3xtdata.columbus.config.SpringContext;
import com.n3xtdata.columbus.connectors.jdbc.JdbcConnectorService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class JdbcConnection implements Connection {

  private static final ApplicationContext context = SpringContext.getAppContext();
  private static final JdbcConnectorService jdbcConnectorService = (JdbcConnectorService) context
      .getBean("jdbcConnectorServiceImpl");
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private String label;

  private String username;

  private String password;

  private String url;

  private String driverClass;

  private String driverPath;

  private byte[] driverJar;

  private String path;

  private String type;

  @SuppressWarnings({"unused"})
  public JdbcConnection() {

  }

  @SuppressWarnings({"unused"})
  public JdbcConnection(String label, String username, String password, String url, String driverClass,
      String driverPath) throws IOException {

    this.label = label;
    this.username = username;
    this.password = password;
    this.url = url;
    this.driverClass = driverClass;
    setDriverPath(driverPath);
  }

  @SuppressWarnings({"unused"})
  public String getLabel() {

    return label;
  }

  @SuppressWarnings({"unused"})
  public void setLabel(String label) {

    this.label = label;
  }

  @SuppressWarnings({"unused"})
  public String getUsername() {

    return username;
  }

  @SuppressWarnings({"unused"})
  public void setUsername(String username) {

    this.username = username;
  }

  @SuppressWarnings({"unused"})
  public String getPassword() {

    return password;
  }

  @SuppressWarnings({"unused"})
  public void setPassword(String password) {

    this.password = password;
  }

  @SuppressWarnings({"unused"})
  public String getUrl() {

    return url;
  }

  @SuppressWarnings({"unused"})
  public void setUrl(String url) {

    this.url = url;
  }

  @SuppressWarnings({"unused"})
  public String getDriverClass() {

    return driverClass;
  }

  @SuppressWarnings({"unused"})
  public void setDriverClass(String driverClass) {

    this.driverClass = driverClass;
  }

  @SuppressWarnings({"unused"})
  public String getDriverPath() {

    return driverPath;
  }

  @SuppressWarnings({"unused"})
  public void setDriverPath(String driverPath) throws IOException {

    this.driverJar = loadDriverJar(driverPath);
    this.driverPath = driverPath;
  }

  @SuppressWarnings({"unused"})
  @JsonIgnore
  public byte[] getDriverJar() {

    return driverJar;
  }

  @SuppressWarnings({"unused"})
  public void setDriverJar(byte[] driverJar) {

    this.driverJar = driverJar;
  }

  @SuppressWarnings({"unused"})
  public String getPath() {

    return path;
  }

  @SuppressWarnings({"unused"})
  public void setPath(String path) {

    this.path = path;
  }

  public String getType() {
    return "JDBC";
  }


  @Override
  public String toString() {

    return "JdbcConnection{" + "label='" + label + '\'' + ", username='" + username + '\'' + ", url='" + url + '\''
        + ", driverClass='" + driverClass + '\'' + ", driverPath='" + driverPath + '\'' + ", path='" + path + '\''
        + '}';
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JdbcConnection that = (JdbcConnection) o;
    return Objects.equals(label, that.label) && Objects.equals(username, that.username) && Objects
        .equals(password, that.password) && Objects.equals(url, that.url) && Objects
        .equals(driverClass, that.driverClass) && Objects.equals(driverPath, that.driverPath) && Arrays
        .equals(driverJar, that.driverJar) && Objects.equals(path, that.path);
  }

  @Override
  public int hashCode() {

    int result = Objects.hash(label, username, password, url, driverClass, driverPath, path);
    result = 31 * result + Arrays.hashCode(driverJar);
    return result;
  }

  private byte[] loadDriverJar(String inputPath) throws IOException {

    Path path = Paths.get(inputPath);

    return Files.readAllBytes(path);
  }

  @Override
  public List<Map<String, Object>> execute(String command) throws Exception {

    logger.debug(command);

    List<Map<String, Object>> result = jdbcConnectorService.execute(this, command);

    logger.debug(result.toString());

    return result;
  }

  @SuppressWarnings({"unused"})
  public void init() {
  }

}
