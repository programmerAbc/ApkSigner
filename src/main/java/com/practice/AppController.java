package com.practice;

import com.alibaba.fastjson.JSON;
import com.sun.javafx.PlatformUtil;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.prefs.Preferences;

public class AppController {
    Stage parent;

    AppModel appModel = new AppModel();

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    @FXML
    private TextField renameApkPathTF;

    @FXML
    private Button renameApkPathBtn;

    @FXML
    private TextField apkAnalyzerTF;

    @FXML
    private Button apkAnalyzerBtn;

    @FXML
    private Button signToolBtn;

    @FXML
    private TextField signToolTF;

    @FXML
    private TextField keystoreTF;

    @FXML
    private Button keystoreBtn;

    @FXML
    private TextField keystorePWTF;

    @FXML
    private TextField keyAliasTF;

    @FXML
    private TextField keyPWTF;

    @FXML
    private TextField apkPathTF;

    @FXML
    private TextField signedApkPathTF;

    @FXML
    private Button apkBtn;

    @FXML
    private Button signedApkBtn;

    @FXML
    private Button signBtn;

    @FXML
    private ProgressIndicator progressView;

    @FXML
    private TextArea outputTA;

    @FXML
    void initialize() {
        load();
        updateUI();
        progressView.setVisible(false);
        apkAnalyzerTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                appModel.apkAnalyzerPath = newValue;
            }
        });
        renameApkPathTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                appModel.renameApkPath = newValue;
            }
        });

        signToolTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                appModel.signToolPath = newValue;
            }
        });
        keystoreTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                appModel.keystorePath = newValue;
            }
        });
        keystorePWTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                appModel.keystorePW = newValue;
            }
        });
        keyAliasTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                appModel.keyAlias = newValue;
            }
        });
        keyPWTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                appModel.keyPW = newValue;
            }
        });
        apkPathTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                appModel.apkPath = newValue;
            }
        });
        signedApkPathTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                appModel.signedApkPath = newValue;
            }
        });

        signToolBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(parent);
                if (file != null) {
                    signToolTF.setText(file.getAbsolutePath());
                }
            }
        });

        keystoreBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(parent);
                if (file != null) {
                    keystoreTF.setText(file.getAbsolutePath());
                }
            }
        });
        apkBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File file = directoryChooser.showDialog(parent);
                if (file != null) {
                    apkPathTF.setText(file.getAbsolutePath());
                }
            }
        });
        signedApkBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File file = directoryChooser.showDialog(parent);
                if (file != null) {
                    signedApkPathTF.setText(file.getAbsolutePath());
                }
            }
        });

        apkAnalyzerBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(parent);
                if (file != null) {
                    apkAnalyzerTF.setText(file.getAbsolutePath());
                }
            }
        });


        renameApkPathBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File file = directoryChooser.showDialog(parent);
                if (file != null) {
                    renameApkPathTF.setText(file.getAbsolutePath());
                }
            }
        });

        signBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                signApk();
            }
        });
    }

    private void signApk() {
        try {
            appModel.check();
            save();
            progressView.setVisible(true);
            signBtn.setVisible(false);
            outputTA.setText("");

            final File[] apkFiles = new File(appModel.apkPath).listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    try {
                        return pathname.getAbsolutePath().endsWith(".apk");
                    } catch (Exception e) {
                        return false;
                    }
                }
            });
            if (apkFiles.length == 0) {
                throw new Exception();
            }

            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (File apkFile : apkFiles) {
                            try {
                                File signedApkFile = new File(appModel.signedApkPath, apkFile.getName().replace(".apk", "-sign.apk"));
                                new SignApkRunnable(apkFile, signedApkFile).run();
                                new RenameApkRunnable(signedApkFile).run();
                            } catch (Exception e) {

                            }
                        }
                    } catch (Exception e) {

                    }

                    try {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    signBtn.setVisible(true);
                                    progressView.setVisible(false);
                                    outputTA.appendText("所有apk签名完成\n");
                                    FileChooser fileChooser = new FileChooser();
                                    fileChooser.setInitialDirectory(new File(appModel.signedApkPath));
                                    fileChooser.showOpenDialog(parent);

                                } catch (Exception e) {

                                }
                            }
                        });
                    } catch (Exception e) {

                    }

                }
            });
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("发生错误");
            alert.show();
        }
    }


    public void setParent(Stage parent) {
        this.parent = parent;
        parent.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    executorService.shutdownNow();
                } catch (Exception e) {

                }
            }
        });
    }

    public static void show(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AppController.class.getResource("/App.fxml"));
        Parent parent = loader.load();
        AppController appController = loader.getController();
        appController.setParent(stage);
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    private void save() {
        try {
            Preferences prefs = Preferences.userNodeForPackage(App.class);
            prefs.put("appModel", JSON.toJSONString(appModel));
        } catch (Exception e) {
        }
    }

    private void load() {
        try {
            Preferences prefs = Preferences.userNodeForPackage(App.class);
            String json = prefs.get("appModel", "");
            appModel = JSON.parseObject(json, AppModel.class);
            if (appModel == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            appModel = new AppModel();
        }

    }

    private void updateUI() {
        renameApkPathTF.setText(appModel.renameApkPath);
        apkAnalyzerTF.setText(appModel.apkAnalyzerPath);
        signToolTF.setText(appModel.signToolPath);
        keystoreTF.setText(appModel.keystorePath);
        keystorePWTF.setText(appModel.keystorePW);
        keyAliasTF.setText(appModel.keyAlias);
        keyPWTF.setText(appModel.keyPW);
        apkPathTF.setText(appModel.apkPath);
        signedApkPathTF.setText(appModel.signedApkPath);
    }


    private class SignApkRunnable implements Runnable {
        File apkFile;
        File signedApkFile;
        Exception ce = null;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;

        public SignApkRunnable(File apkFile, File signedApkFile) {
            this.apkFile = apkFile;
            this.signedApkFile = signedApkFile;
        }

        public String buildCmd(File apkFile, File signedApkFile) {
            StringBuilder cmdBuilder = new StringBuilder();
            cmdBuilder.append(appModel.signToolPath);
            cmdBuilder.append(" sign --ks ");
            cmdBuilder.append(appModel.keystorePath);
            cmdBuilder.append(" --ks-key-alias ");
            cmdBuilder.append(appModel.keyAlias);
            cmdBuilder.append(" --ks-pass pass:");
            cmdBuilder.append(appModel.keystorePW);
            cmdBuilder.append(" --key-pass pass:");
            cmdBuilder.append(appModel.keyPW);
            cmdBuilder.append(" -v --v1-signing-enabled true --v2-signing-enabled true --out ");
            cmdBuilder.append(signedApkFile.getAbsolutePath());
            cmdBuilder.append(" ");
            cmdBuilder.append(apkFile.getAbsolutePath());
            return cmdBuilder.toString();
        }

        @Override
        public void run() {
            try {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            outputTA.appendText("开始签名:" + apkFile.getName() + "\n");
                        } catch (Exception e) {

                        }
                    }
                });
            } catch (Exception e) {

            }

            try {
                String cmd = buildCmd(apkFile, signedApkFile);
                ProcessBuilder pb = new ProcessBuilder();
                if (PlatformUtil.isWindows()) {
                    pb.command("cmd.exe", "/C", cmd);
                } else {
                    pb.command("/bin/sh", "-c", cmd);
                }


                pb.redirectErrorStream(true);
                Process p = pb.start();

                inputStreamReader = new InputStreamReader(p.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    final String str = line;
                    try {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    outputTA.appendText(str);
                                    outputTA.appendText("\n");
                                } catch (Exception e) {

                                }
                            }
                        });
                    } catch (Exception e) {

                    }
                }


                int exitCode = p.waitFor();
                if (exitCode != 0) {
                    throw new Exception();
                }

            } catch (Exception e) {
                ce = e;
            }
            try {
                bufferedReader.close();
            } catch (Exception e) {

            }

            try {
                inputStreamReader.close();
            } catch (Exception e) {

            }


            try {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (ce == null) {
                                outputTA.appendText("签名成功\n");
                            } else {
                                outputTA.appendText("签名失败:" + ce.getMessage() + "\n");
                            }
                        } catch (Exception e) {

                        }
                    }
                });
            } catch (Exception e) {

            }
        }
    }

    private class RenameApkRunnable implements Runnable {
        File apkFile;
        File outputDir;
        Exception ce = null;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;

        public RenameApkRunnable(File apkFile) {
            this.apkFile = apkFile;
            this.outputDir = new File(appModel.renameApkPath);
        }


        public ProcessBuilder buildCmd(File apkFile, File targetDir) {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("java", "-jar", appModel.apkAnalyzerPath, "d", apkFile.getAbsolutePath(),"--force" ,"--force-manifest", "--keep-broken-res", "--no-assets", "--no-res", "--no-src", "--output", targetDir.getAbsolutePath());
            return processBuilder;
        }

        public String extractNameFromXML(File file) throws Exception {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList list = doc.getElementsByTagName("meta-data");
            for (int temp = 0; temp < list.getLength(); temp++) {
                Node node = list.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    // get staff's attribute
                    String name = element.getAttribute("android:name");
                    if (!name.equalsIgnoreCase("BaiduMobAd_CHANNEL")) {
                        continue;
                    }
                    return element.getAttribute("android:value");
                }
            }
            throw new Exception("xml解析错误,无法提取渠道名称");

        }

        @Override
        public void run() {
            try {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            outputTA.appendText("开始重命名:" + apkFile.getName() + "\n开始解析AndroidManifest.xml");
                        } catch (Exception e) {

                        }
                    }
                });
            } catch (Exception e) {

            }

            try {
                File targetDir = new File(outputDir, apkFile.getName());
                try {
                    RxFileTool.createOrExistsDir(targetDir);
                } catch (Exception e) {

                }

                try {
                    RxFileTool.deleteFilesInDir(targetDir);
                } catch (Exception e) {

                }
                ProcessBuilder pb = buildCmd(apkFile, targetDir);
                pb.redirectErrorStream(true);
                Process p = pb.start();

                inputStreamReader = new InputStreamReader(p.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    final String str = line;
                    try {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    outputTA.appendText(str);
                                    outputTA.appendText("\n");
                                } catch (Exception e) {

                                }
                            }
                        });
                    } catch (Exception e) {

                    }
                }
                int exitCode = p.waitFor();
                if (exitCode != 0) {
                    throw new Exception();
                }


                File androidManifest = new File(targetDir, "AndroidManifest.xml");
                String channel = extractNameFromXML(androidManifest);
                if (channel == null || channel.isEmpty()) throw new Exception("从AndroidManifest.xml提取渠道名称失败");

                try {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                outputTA.appendText("解析AndroidManifest.xml成功,渠道名称为:" + channel + "开始重命名:" + apkFile.getName() + "\n");
                            } catch (Exception e) {

                            }
                        }
                    });
                } catch (Exception e) {

                }


                File targetFile = new File(targetDir, channel + "-sign.apk");
                RxFileTool.copyFile(apkFile, targetFile);
            } catch (Exception e) {
                ce = e;
            }
            try {
                bufferedReader.close();
            } catch (Exception e) {

            }

            try {
                inputStreamReader.close();
            } catch (Exception e) {

            }


            try {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (ce == null) {
                                outputTA.appendText("重命名成功\n");
                            } else {
                                outputTA.appendText("重命名失败:" + ce.getMessage() + "\n");
                            }
                        } catch (Exception e) {

                        }
                    }
                });
            } catch (Exception e) {

            }
        }
    }

}
