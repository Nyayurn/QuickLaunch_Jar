/**
 * Copyright 2023 ANMSakura
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.anmsakura;

import lombok.*;

import java.io.File;


/**
 * @author ANMSakura
 * @version snap23.0519
 */
@Data
public class JavaFile {
    private String name;
    private File path;
    private String group;

    /**
     *
     * @param name Java的名称
     * @param path Java.exe的路径
     * @param group 分组
     */
    public JavaFile(String name, File path, String group) {
        this.name = name;
        this.path = path;
        this.group = group;
    }
}
