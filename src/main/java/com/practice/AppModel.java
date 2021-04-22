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
        if (apkPath.length() == 0) throw new Exception();
        if (signedApkPath.length() == 0) throw new Exception();
    }


}
