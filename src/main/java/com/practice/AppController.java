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
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(parent);
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
            final String cmd = appModel.buildCmd();
            executorService.submit(new Runnable() {
                Exception ce = null;
                InputStreamReader inputStreamReader;
                BufferedReader bufferedReader;

                @Override
                public void run() {
                    try {
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
                                    progressView.setVisible(false);
                                    signBtn.setVisible(true);
                                    if (ce == null) {
                                        outputTA.appendText("签名成功");
                                        try {
                                            File signedApkFile=new File(appModel.signedApkPath);
                                            FileChooser fileChooser = new FileChooser();
                                            fileChooser.setInitialDirectory(signedApkFile.getParentFile());
                                            fileChooser.showOpenDialog(parent);
                                        }catch (Exception e){

                                        }
                                    } else {
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setHeaderText(null);
                                        alert.setContentText("签名失败:" + ce.getMessage());
                                        alert.show();
                                    }
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


}
