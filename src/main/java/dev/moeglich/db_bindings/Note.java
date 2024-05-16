package dev.moeglich.db_bindings;

import java.sql.*;
import java.util.Base64;

public class Note {
    public final int id;
    public final byte[] content;
    public final Timestamp createdAt;

    public Note(int id, byte[] content, Timestamp createdAt) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        String encodedContent = Base64.getEncoder().encodeToString(content);
        return "Entry{" +
                "id=" + id +
                ", content='" + encodedContent + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
