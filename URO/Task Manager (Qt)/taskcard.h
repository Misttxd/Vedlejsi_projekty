#ifndef TASKCARD_H
#define TASKCARD_H

#include <QFrame>
#include <QCheckBox>
#include <QPushButton>

#include "task.h"

class TaskCard : public QFrame
{
    Q_OBJECT

public:
    explicit TaskCard(const Task& task, int taskIndex, QWidget *parent = nullptr);

signals:
    void completedChanged(int taskIndex, bool completed);
    void deleteRequested(int taskIndex);

private slots:
    void onCheckBoxToggled(bool checked);
    void onDeleteButtonClicked();

private:
    int m_taskIndex;
    QCheckBox *m_checkBox;
    QPushButton *m_deleteButton;
};

#endif // TASKCARD_H
