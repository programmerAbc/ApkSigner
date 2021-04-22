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
        signToolTF.setText(appModel.signToolPath);
        keystoreTF.setText(appModel.keystorePath);
        keystorePWTF.setText(appModel.keystorePW);
        keyAliasTF.setText(appModel.keyAlias);
        keyPWTF.setText(appModel.keyPW);
        apkPathTF.setText(appModel.apkPath);
        signedApkPathTF.setText(appModel.signedApkPath);
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
}
