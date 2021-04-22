package com.practice;

import java.io.File;
import java.nio.file.Files;

public class AppModel {
    public String signToolPath;
    public String keystorePath;
    public String keystorePW;
    public String keyAlias;
    public String keyPW;
    public String apkPath;
    public String signedApkPath;

    public AppModel() {
    }

    public void check() throws Exception {
        if (!Files.exists(new File(signToolPath).toPath())) throw new Exception();
        if (!Files.exists(new File(keystorePath).toPath())) throw new Exception();
        if (keystorePW.length() == 0) throw new Exception();
        if (keyAlias.length() == 0) throw new Exception();
        if (keyPW.length() == 0) throw new Exception();
        if (!Files.exists(new File(apkPath).toPath())) throw new Exception();
        if (signedApkPath.length() == 0) throw new Exception();
    }

    public String buildCmd() {
        StringBuilder cmdBuilder = new StringBuilder();
        cmdBuilder.append(signToolPath);
        cmdBuilder.append(" sign --ks ");
        cmdBuilder.append(keystorePath);
        cmdBuilder.append(" --ks-key-alias ");
        cmdBuilder.append(keyAlias);
        cmdBuilder.append(" --ks-pass pass:");
        cmdBuilder.append(keystorePW);
        cmdBuilder.append(" --key-pass pass:");
        cmdBuilder.append(keyPW);
        cmdBuilder.append(" -v --v1-signing-enabled true --v2-signing-enabled true --out ");
        cmdBuilder.append(signedApkPath);
        cmdBuilder.append(" ");
        cmdBuilder.append(apkPath);
        return cmdBuilder.toString();
    }
}
