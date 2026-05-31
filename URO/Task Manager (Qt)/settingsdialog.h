#ifndef SETTINGSDIALOG_H
#define SETTINGSDIALOG_H

#include <QDialog>
#include <QRadioButton>
#include <QTextEdit>
#include <QPushButton>

class SettingsDialog : public QDialog {
    Q_OBJECT

public:
    explicit SettingsDialog(QWidget *parent = nullptr);

signals:
    void themeChanged(bool isDark);

private slots:
    void onThemeToggled(bool checked);

private:
    QRadioButton *lightModeBtn;
    QRadioButton *darkModeBtn;
    QTextEdit *helpText;
    QPushButton *backBtn;
};

#endif 
