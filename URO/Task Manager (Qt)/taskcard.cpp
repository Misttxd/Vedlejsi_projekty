#include "taskcard.h"

#include <QDate>
#include <QFont>
#include <QHBoxLayout>

TaskCard::TaskCard(const Task& task, int taskIndex, QWidget *parent)
    : QFrame(parent),
      m_taskIndex(taskIndex)
{
    setObjectName("taskFrame");

    QHBoxLayout *layout = new QHBoxLayout(this);
    layout->setContentsMargins(15, 10, 15, 10);

    m_checkBox = new QCheckBox(this);
    m_checkBox->setObjectName("taskCheckBox");

    QFont font = m_checkBox->font();
    font.setStrikeOut(task.completed);
    m_checkBox->setFont(font);

    const QString dateStr = task.deadline.toString("dd. MM. yyyy");
    m_checkBox->setText(QString("%1  ·  %2  ·  %3").arg(task.title, task.category, dateStr));
    m_checkBox->setChecked(task.completed);

    if (task.completed) {
        m_checkBox->setProperty("taskState", "done");
    } else if (task.deadline < QDate::currentDate()) {
        m_checkBox->setProperty("taskState", "overdue");
    } else {
        m_checkBox->setProperty("taskState", "normal");
    }

    m_deleteButton = new QPushButton("✕", this);
    m_deleteButton->setObjectName("taskDelBtn");
    m_deleteButton->setFixedSize(30, 30);

    layout->addWidget(m_checkBox);
    layout->addStretch();
    layout->addWidget(m_deleteButton);

    connect(m_checkBox, &QCheckBox::toggled, this, &TaskCard::onCheckBoxToggled);
    connect(m_deleteButton, &QPushButton::clicked, this, &TaskCard::onDeleteButtonClicked);
}

void TaskCard::onCheckBoxToggled(bool checked)
{
    QFont font = m_checkBox->font();
    font.setStrikeOut(checked);
    m_checkBox->setFont(font);

    emit completedChanged(m_taskIndex, checked);
}

void TaskCard::onDeleteButtonClicked()
{
    emit deleteRequested(m_taskIndex);
}
