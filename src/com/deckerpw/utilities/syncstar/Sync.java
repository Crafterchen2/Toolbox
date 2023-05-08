package com.deckerpw.utilities.syncstar;

import java.nio.file.Path;

public class Sync {

    private final Path localPath;
    private final Path remotePath;

    public Path getLocalPath() {
        return localPath;
    }

    public Path getRemotePath() {
        return remotePath;
    }

    public Sync(Path localPath, Path remotePath) {
        this.localPath = localPath;
        this.remotePath = remotePath;
    }
}
