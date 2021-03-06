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

package com.n3xtdata.columbus.loader;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.n3xtdata.columbus.core.Kind;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

class GenericYamlLoader<T> {

  private final T t;
  private final Logger logger = LoggerFactory.getLogger(getClass());


  GenericYamlLoader(T t) {
    this.t = t;
  }

  public Multimap<Kind, T> load(String fileName) throws FileNotFoundException {

    Constructor constructor = new Constructor(t.getClass());
    TypeDescription customTypeDescription = new TypeDescription(t.getClass());
    constructor.addTypeDescription(customTypeDescription);
    Yaml yaml = new Yaml(constructor);
    InputStream inputStream = new FileInputStream(fileName);

    Multimap<Kind, T> multiMap = ArrayListMultimap.create();

    try {
      yaml.loadAll(inputStream).forEach(element -> {
        try {
          //noinspection unchecked
          Kind kind = (Kind) element.getClass().getMethod("getKind").invoke(element);

          //noinspection unchecked
          multiMap.put(kind, (T) element);
        } catch (Exception e) {
          e.printStackTrace();
          logger.error("asfAn error occurred while parsing config file '" + fileName + "': " + e.getMessage());
          System.exit(0);
        }
      });
    } catch (Exception e) {
      logger.error("An error occurred while parsing config file '" + fileName + "': " + e.getMessage());
    }
    return multiMap;
  }

}