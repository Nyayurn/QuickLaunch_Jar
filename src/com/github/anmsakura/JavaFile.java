package com.github.anmsakura;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.File;

/**
 * @author ANMSakura
 */
public class JavaFile {
    private String name;
    private File path;
    private String group;

    public JavaFile(String name, File path, String group) {
        this.name = name;
        this.path = path;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JavaFile javaFile = (JavaFile) o;

        return new EqualsBuilder().append(name, javaFile.name).append(path, javaFile.path).append(group, javaFile.group).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(name).append(path).append(group).toHashCode();
    }

    @Override
    public String toString() {
        return "JavaFile{" +
                "name='" + name + '\'' +
                ", path=" + path +
                ", group='" + group + '\'' +
                '}';
    }
}
