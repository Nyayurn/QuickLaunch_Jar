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

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 主类
 *
 * @author ANMSakura
 * @version snap23.0519
 * TODO: 根据配置文件设置不同java版本的文件的默认选择java(弹出选择框，但默认选中设置的java)
 * TODO: 根据配置文件设置每个文件使用哪个java直接打开(不弹出选择框)
 */
public class Main {
    /**
     * 程序入口
     *
     * @param args 传递给程序的参数
     */
    public static void main(String... args) {
        // 为GUI设置主题。
        FlatAtomOneDarkIJTheme.setup();

        if (args.length == 0) {
            // TODO: 主菜单界面
            // TODO: 主菜单界面能修改配置文件
            return;
        }

        File file = new File(args[0]);

        // 判断是否存在
        if (!file.exists()) {
            showErrDialog("文件不存在");
            return;
        }

        // 判断是否为文件
        if (!file.isFile()) {
            showErrDialog("不是文件");
            return;
        }

        // 判断是否为标准.jar文件
        try (InputStream in = Files.newInputStream(file.toPath())) {
            byte[] bytes = new byte[4];
            int ignored = in.read(bytes);
            if (!Arrays.equals(bytes, new byte[]{0x50, 0x4B, 0x03, 0x04})) {
                showErrDialog(".jar文件不符合规范");
                return;
            }
        } catch (IOException e) {
            showErrDialog(e, "IO错误: " + e.getMessage());
            return;
        }

        // 从配置文件中加载Java文件列表。
        File javaFile = new File(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent() + File.separator + "JavaFiles.json");
        Set<JavaFile> javaFiles = new HashSet<>();
        if (javaFile.exists()) {
            try (Stream<String> stream = Files.lines(javaFile.toPath(), StandardCharsets.UTF_8)) {
                String jsonString = stream.map(String::trim).collect(Collectors.joining());
                List<JSONObject> jsonObjects = JSONArray.parseArray(jsonString, JSONObject.class);
                for (JSONObject jsonObject : jsonObjects) {
                    File javaFilePath = new File(jsonObject.getString("path"));
                    if (javaFilePath.exists() || !javaFilePath.isFile()) {
                        javaFiles.add(new JavaFile(jsonObject.getString("name"), javaFilePath, jsonObject.getString("group")));
                    }
                }
            } catch (IOException e) {
                showErrDialog(e, "IO错误: " + e.getMessage());
            }
        }

        // 如果找不到Java文件，则直接启动目标程序
        // 如果仅找到一个Java文件，请使用目标程序启动它
        // 如果找到多个Java文件，让用户选择一个
        if (javaFiles.size() == 0) {
            runJar("java", args);
        } else if (javaFiles.size() == 1) {
            runJar(javaFiles.iterator().next().toString(), args);
        } else {
            // 将Java文件按组名分组
            Map<String, List<JavaFile>> groupedJavaFiles = javaFiles.stream()
                    .collect(Collectors.groupingBy(JavaFile::getGroup));

            JPanel mainPanel = new JPanel(new GridBagLayout());
            JScrollPane scrollPane = new JScrollPane(mainPanel);

            // 设置 GridBagConstraints 对象，以控制组件之间的间距
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.insets = new Insets(5, 5, 5, 5);

            groupedJavaFiles.forEach((s, javaFiles1) -> {
                // 创建 JList 的模型
                DefaultListModel<String> model = new DefaultListModel<>();
                javaFiles1.stream()
                        .map(entry -> String.format("%s: %s", entry.getName(), entry.getPath()))
                        .forEach(model::addElement);

                // 创建 TitledBorder 以包含 JList
                TitledBorder lineBorder = BorderFactory.createTitledBorder(s);
                JList<String> list = new JList<>(model);
                list.setBorder(lineBorder);

                list.addListSelectionListener(e -> {
                    // 获取当前 JList 中的选定值
                    String selected = list.getSelectedValue();
                    if (selected != null) {
                        // 取消其他 JList 中的任何选定值
                        for (Component comp : mainPanel.getComponents()) {
                            if (comp instanceof JList && comp != list) {
                                ((JList<?>) comp).clearSelection();
                            }
                        }
                    }
                });

                list.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            JList<?> list = (JList<?>) e.getSource();
                            String selectedValue = (String) list.getSelectedValue();
                            SwingUtilities.getWindowAncestor(scrollPane).dispose();
                            run(javaFiles, selectedValue, args);
                        }
                    }
                });

                // 将组件添加到 mainPanel 中
                mainPanel.add(list, gbc);

                // 更新 GridBagConstraints 值，以便下一个组件的位置稍下
                gbc.gridy++;
            });

            // 弹窗选择Java
            int result = JOptionPane.showConfirmDialog(null, scrollPane, "选择Java", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

            // 选择Java后启动应用
            if (result == JOptionPane.OK_OPTION) {
                for (Component comp : mainPanel.getComponents()) {
                    if (comp instanceof JList && ((JList<?>) comp).getSelectedIndex() != -1) {
                        String selectedValue = (String) ((JList<?>) comp).getSelectedValue();
                        run(javaFiles, selectedValue, args);
                    }
                }
            }
        }
    }

    private static void showErrDialog(String message) {
        System.err.println(message);
        JOptionPane.showMessageDialog(null, message, "错误", JOptionPane.ERROR_MESSAGE);
    }

    private static void showErrDialog(Throwable e, String message) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, message, "错误", JOptionPane.ERROR_MESSAGE);
    }

    private static void run(Set<JavaFile> javaFiles, String selectedValue, String... args) {
        // 用目标程序启动选定的Java文件。
        Optional<JavaFile> selectedJavaFile = javaFiles.stream()
                .filter(javaFile1 -> (javaFile1.getName() + ": " + javaFile1.getPath()).equals(selectedValue))
                .findFirst();
        if (selectedJavaFile.isPresent()) {
            runJar(selectedJavaFile.get().getPath().toString(), args);
        } else {
            showErrDialog("选项无效");
        }
    }

    private static void runJar(String java, String... args) {
        try {
            List<String> command = new ArrayList<>();
            command.add(java);
            command.add("-jar");
            command.addAll(Arrays.asList(args));
            new ProcessBuilder(command).start();
        } catch (IOException e) {
            showErrDialog(e, "IO错误: " + e.getMessage());
        }
    }

}