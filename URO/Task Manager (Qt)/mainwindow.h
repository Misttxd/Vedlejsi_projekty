#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QScrollArea>
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QGridLayout>
#include <QPushButton>
#include <QLabel>
#include <vector>
#include "task.h"

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = nullptr);
    ~MainWindow() override;

private slots:
    void onSettingsClicked();
    void onAddTaskClicked();
    void showAllTasks();
    void showTodayTasks();
    void showImportantTasks();
    void onTaskCompletedChanged(int taskIndex, bool completed);
    void onTaskDeleteRequested(int taskIndex);

private:
    void createLayout();
    void populateMockData();
    void refreshTasksView();
    bool taskMatchesCurrentFilter(const Task& task) const;
    void createTaskWidget(const Task& task, int taskIndex);
    void applyTheme(bool isDark);
    void updateSidebarStyles();
    
    QWidget *centralWidget;
    QWidget *headerFrame;
    QWidget *sidebarFrame;
    QWidget *mainFrame;
    QScrollArea *scrollArea;
    QWidget *scrollContentWidget;
    QVBoxLayout *tasksLayout;
    
    QPushButton *btnAll;
    QPushButton *btnToday;
    QPushButton *btnImportant;
    QPushButton *btnNewTask;
    QPushButton *btnSettings;
    QLabel *appTitle;
    QWidget *bottomLine;
    
    std::vector<Task> taskList;
    QString currentFilter; // "All", "Today", "Important"
};
#endif // MAINWINDOW_H
