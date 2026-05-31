#ifndef TASKDIALOG_H
#define TASKDIALOG_H

#include <QDialog>
#include <QLineEdit>
#include <QDateEdit>
#include <QComboBox>
#include <QPushButton>
#include "task.h"

class TaskDialog : public QDialog {
    Q_OBJECT

public:
    explicit TaskDialog(QWidget *parent = nullptr);
    Task getTask() const;

private slots:
    void onSaveClicked();

private:
    QLineEdit *nameEdit;
    QDateEdit *dateEdit;
    QComboBox *categoryBox;
    QPushButton *saveBtn;
    QPushButton *cancelBtn;
};

#endif 
