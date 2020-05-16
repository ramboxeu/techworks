package io.github.ramboxeu.techworks.common.debug;

import java.util.ArrayList;
import java.util.List;

public class DebugInfoBuilder {
    private List<Section> sections = new ArrayList<>();
    private Class<?>[] classes;

    public DebugInfoBuilder(Class<?> ...classes) {
        this.classes = classes;
    }

    public DebugInfoBuilder addSection(Section section) {
        sections.add(section);
        return this;
    }

    public List<Section> getSections() {
        return sections;
    }

    public String getTitle() {
        StringBuilder buffer = new StringBuilder();
        for (Class<?> clazz : classes) {
            buffer.append(clazz.getTypeName()).append(" ");
        }

        return buffer.toString();
    }

    public static class Section {
        private List<String> lines = new ArrayList<>();
        private String name;

        public Section(String name) {
            this.name = name;
        }

        public Section line(String s) {
            lines.add(s);
            return this;
        }

        public List<String> getLines() {
            return lines;
        }

        public String getName() {
            return name;
        }
    }
}
