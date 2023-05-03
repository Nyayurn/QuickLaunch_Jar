package com.github.anmsakura;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ANMSakura
 */
public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            return;
        }
        FlatAtomOneDarkIJTheme.setup();
        File javaFile = new File(System.getProperty("user.dir") + "\\JavaFiles.json");
        Set<JavaFile> javaFileSet = new HashSet<>();
        if (javaFile.exists()) {
            Scanner scanner = new Scanner(Files.newInputStream(javaFile.toPath()));
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine()).append("\n");
            }
            List<JSONObject> jsonObjects = JSONArray.parseArray(sb.toString(), JSONObject.class);
            for (JSONObject jsonObject : jsonObjects) {
                File path = new File(jsonObject.getString("path"));
                if (path.exists() || !path.isFile()) {
                    javaFileSet.add(new JavaFile(jsonObject.getString("name"), path, jsonObject.getString("group")));
                }
            }
            scanner.close();
        }
        if (javaFileSet.size() == 0) {
            Runtime.getRuntime().exec(String.format("java -jar %s", String.join(" ", args)));
        } else if (javaFileSet.size() == 1) {
            Runtime.getRuntime().exec(String.format("%s -jar %s", javaFileSet.iterator().next(), String.join(" ", args)));
        } else {
            // Group the JavaFiles by their group parameter
            Map<String, List<JavaFile>> groupedJavaFiles = javaFileSet.stream()
                    .collect(Collectors.groupingBy(JavaFile::getGroup));

            List<String> list = new ArrayList<>();
            groupedJavaFiles.forEach((s, javaFiles) -> {
                list.add(String.format("---%s---", s));
                list.addAll(javaFiles.stream().map(javaFile1 -> String.format("%s: %s", javaFile1.getName(), javaFile1.getPath())).toList());
            });


            String select = (String) JOptionPane.showInputDialog(null, "Select Java", "Select Java",
                    JOptionPane.QUESTION_MESSAGE, null, list.toArray(), list.get(0));

            Optional<JavaFile> selectedFile = javaFileSet.stream().filter(javaFile1 -> String.format("%s: %s", javaFile1.getName(), javaFile1.getPath()).equals(select)).findFirst();
            if (selectedFile.isPresent()) {
                Runtime.getRuntime().exec(String.format("%s -jar %s", selectedFile.get().getPath(), String.join(" ", args)));
            } else {
                System.out.println("选项无效");
            }
        }
    }
}