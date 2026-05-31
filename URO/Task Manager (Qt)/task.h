#ifndef TASK_H
#define TASK_H

#include <QString>
#include <QDate>

struct Task {
    QString title;
    QDate deadline;
    QString category;
    bool completed;
    
    Task(const QString& title, const QDate& deadline, const QString& category, bool completed = false)
        : title(title), deadline(deadline), category(category), completed(completed) {}
};

#endif 
