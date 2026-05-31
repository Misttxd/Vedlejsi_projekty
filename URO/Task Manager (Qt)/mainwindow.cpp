#include "mainwindow.h"
#include "settingsdialog.h"
#include "taskdialog.h"
#include "taskcard.h"

#include <QFrame>
#include <QApplication>
#include <QDate>

MainWindow::MainWindow(QWidget *parent) : QMainWindow(parent), currentFilter("All") {
    this->resize(1200, 800);
    this->setWindowTitle("Správce úkolů");
    
    createLayout();
    populateMockData();
    applyTheme(false); 
}

MainWindow::~MainWindow() = default;

void MainWindow::createLayout() {
    centralWidget = new QWidget(this);
    this->setCentralWidget(centralWidget);
    
    QGridLayout *mainLayout = new QGridLayout(centralWidget);
    mainLayout->setContentsMargins(0, 0, 0, 0);
    mainLayout->setSpacing(0);
    
    // 1. Horní panel
    headerFrame = new QWidget();
    headerFrame->setObjectName("headerFrame");
    headerFrame->setFixedHeight(100);
    
    QVBoxLayout *headerVLayout = new QVBoxLayout(headerFrame);
    headerVLayout->setContentsMargins(0,0,0,0);
    headerVLayout->setSpacing(0);
    
    QWidget *headerContent = new QWidget();
    QHBoxLayout *headerLayout = new QHBoxLayout(headerContent);
    headerLayout->setContentsMargins(20, 20, 20, 20);
    
    appTitle = new QLabel("Správce úkolů");
    appTitle->setObjectName("appTitle");
    appTitle->setAlignment(Qt::AlignCenter);
    
    btnSettings = new QPushButton("⚙");
    btnSettings->setObjectName("settingsBtn");
    btnSettings->setFixedSize(40, 40);
    
    headerLayout->addStretch();
    headerLayout->addWidget(appTitle);
    headerLayout->addStretch();
    headerLayout->addWidget(btnSettings);
    
    bottomLine = new QWidget();
    bottomLine->setObjectName("bottomLine");
    bottomLine->setFixedHeight(2);
    
    headerVLayout->addWidget(headerContent);
    headerVLayout->addWidget(bottomLine);
    
    // 2. Levý panel
    sidebarFrame = new QWidget();
    sidebarFrame->setObjectName("sidebarFrame");
    sidebarFrame->setFixedWidth(200);
    
    QVBoxLayout *sidebarLayout = new QVBoxLayout(sidebarFrame);
    sidebarLayout->setContentsMargins(10, 40, 10, 10);
    sidebarLayout->setSpacing(20);
    
    btnAll = new QPushButton("Všechny úkoly");
    btnToday = new QPushButton("Dnes");
    btnImportant = new QPushButton("Důležité");
    
    btnAll->setObjectName("sidebarBtn");
    btnToday->setObjectName("sidebarBtn");
    btnImportant->setObjectName("sidebarBtn");
    
    sidebarLayout->addWidget(btnAll);
    sidebarLayout->addWidget(btnToday);
    sidebarLayout->addWidget(btnImportant);
    sidebarLayout->addStretch();
    
    // 3. Pravý panel
    mainFrame = new QWidget();
    mainFrame->setObjectName("mainFrame");
    QVBoxLayout *mainVLayout = new QVBoxLayout(mainFrame);
    mainVLayout->setContentsMargins(15, 15, 15, 15);
    
    scrollArea = new QScrollArea();
    scrollArea->setObjectName("scrollArea");
    scrollArea->setWidgetResizable(true);
    scrollArea->setFrameShape(QFrame::NoFrame);
    
    scrollContentWidget = new QWidget();
    scrollContentWidget->setObjectName("scrollContentWidget");
    
    tasksLayout = new QVBoxLayout(scrollContentWidget);
    tasksLayout->setContentsMargins(0, 0, 0, 0);
    tasksLayout->setSpacing(5);
    tasksLayout->addStretch(); 
    
    scrollArea->setWidget(scrollContentWidget);
    
    btnNewTask = new QPushButton("+ Přidat nový úkol");
    btnNewTask->setObjectName("newTaskBtn");
    btnNewTask->setFixedHeight(50);
    
    mainVLayout->addWidget(scrollArea);
    mainVLayout->addWidget(btnNewTask);
    
    mainLayout->addWidget(headerFrame, 0, 0, 1, 2);
    mainLayout->addWidget(sidebarFrame, 1, 0);
    mainLayout->addWidget(mainFrame, 1, 1);
    mainLayout->setRowStretch(1, 1);
    mainLayout->setColumnStretch(1, 1);
    
    // Connects
    connect(btnSettings, &QPushButton::clicked, this, &MainWindow::onSettingsClicked);
    connect(btnNewTask, &QPushButton::clicked, this, &MainWindow::onAddTaskClicked);
    
    connect(btnAll, &QPushButton::clicked, this, &MainWindow::showAllTasks);
    connect(btnToday, &QPushButton::clicked, this, &MainWindow::showTodayTasks);
    connect(btnImportant, &QPushButton::clicked, this, &MainWindow::showImportantTasks);
}

void MainWindow::populateMockData() {
    taskList.push_back(Task("URO - Python aplikace", QDate::currentDate(), "Osobní"));
    taskList.push_back(Task("BRU0098", QDate(2026, 3, 15), "Důležité"));
    taskList.push_back(Task("Andreas Brudovský", QDate(2026, 3, 31), "Osobní"));
    
    refreshTasksView();
}

void MainWindow::refreshTasksView() {
    QLayoutItem *child;
    while ((child = tasksLayout->takeAt(0)) != nullptr) {
        if (child->widget()) {
            child->widget()->deleteLater();
        }
        delete child;
    }
    
    for (int i = 0; i < static_cast<int>(taskList.size()); ++i) {
        const Task& task = taskList[i];
        if (taskMatchesCurrentFilter(task)) {
            createTaskWidget(task, i);
        }
    }

    tasksLayout->addStretch();
}

bool MainWindow::taskMatchesCurrentFilter(const Task& task) const {
    if (currentFilter == "All") {
        return true;
    }

    if (currentFilter == "Today") {
        return task.deadline == QDate::currentDate();
    }

    if (currentFilter == "Important") {
        return task.category == "Důležité";
    }

    return true;
}

void MainWindow::createTaskWidget(const Task& task, int taskIndex) {
    TaskCard *taskCard = new TaskCard(task, taskIndex, scrollContentWidget);

    connect(taskCard, &TaskCard::completedChanged,
            this, &MainWindow::onTaskCompletedChanged);
    connect(taskCard, &TaskCard::deleteRequested,
            this, &MainWindow::onTaskDeleteRequested);

    tasksLayout->addWidget(taskCard);
}

void MainWindow::onTaskCompletedChanged(int taskIndex, bool completed) {
    if (taskIndex < 0 || taskIndex >= static_cast<int>(taskList.size())) {
        return;
    }

    taskList[taskIndex].completed = completed;
    refreshTasksView();
}

void MainWindow::onTaskDeleteRequested(int taskIndex) {
    if (taskIndex < 0 || taskIndex >= static_cast<int>(taskList.size())) {
        return;
    }

    taskList.erase(taskList.begin() + taskIndex);
    refreshTasksView();
}

void MainWindow::showAllTasks() {
    currentFilter = "All";
    updateSidebarStyles();
    refreshTasksView();
}

void MainWindow::showTodayTasks() {
    currentFilter = "Today";
    updateSidebarStyles();
    refreshTasksView();
}

void MainWindow::showImportantTasks() {
    currentFilter = "Important";
    updateSidebarStyles();
    refreshTasksView();
}

void MainWindow::updateSidebarStyles() {
    btnAll->setProperty("active", currentFilter == "All");
    btnToday->setProperty("active", currentFilter == "Today");
    btnImportant->setProperty("active", currentFilter == "Important");
    
    btnAll->style()->unpolish(btnAll);
    btnAll->style()->polish(btnAll);
    btnToday->style()->unpolish(btnToday);
    btnToday->style()->polish(btnToday);
    btnImportant->style()->unpolish(btnImportant);
    btnImportant->style()->polish(btnImportant);
}

void MainWindow::onSettingsClicked() {
    SettingsDialog dlg(this);
    connect(&dlg, &SettingsDialog::themeChanged, this, &MainWindow::applyTheme); 
    dlg.exec();
}

void MainWindow::onAddTaskClicked() {
    TaskDialog dlg(this);
    if (dlg.exec() == QDialog::Accepted) {
        Task t = dlg.getTask();
        taskList.push_back(t);
        refreshTasksView();
    }
}

void MainWindow::applyTheme(bool isDark) {
    QString qss;
    
    if (isDark) {
        qss = R"(
            QMainWindow, QDialog, QWidget { background-color: #111111; color: #d1d5db; font-family: 'Roboto', sans-serif; font-size: 15px;}
            QWidget#headerFrame { background-color: #2b2b2b; }
            QWidget#bottomLine { background-color: #404040; }
            QLabel#appTitle { font-family: 'Bebas Neue'; font-size: 40px; font-weight: normal; color: #e5e7eb; }
            QPushButton#settingsBtn { color: #e5e7eb; font-size: 24px; border: none; background: transparent; }
            QPushButton#settingsBtn:hover { color: white; }
            
            QWidget#sidebarFrame { background-color: #2e2e2e; }
            QPushButton#sidebarBtn {
                background-color: #404040; color: #e5e7eb; border: none; border-radius: 6px; padding: 10px; text-align: left;
            }
            QPushButton#sidebarBtn:hover { background-color: #525252; }
            QPushButton#sidebarBtn[active="true"] { background-color: #606060; font-weight: bold; border-left: 4px solid #3b82f6; }
            
            QWidget#mainFrame { background-color: #1a1a1a; }
            QScrollArea#scrollArea, QWidget#scrollContentWidget { background-color: transparent; }
            
            QFrame#taskFrame { background-color: #2b2b2b; border-radius: 8px; }
            QCheckBox#taskCheckBox { color: #e5e7eb; }
            QCheckBox#taskCheckBox[taskState="done"] { color: #6b7280; }
            QCheckBox#taskCheckBox[taskState="overdue"] { color: #ef4444; font-weight: bold; }
            QCheckBox::indicator { width: 18px; height: 18px; }
            QPushButton#taskDelBtn { color: gray; font-size: 18px; border: none; background: transparent; }
            QPushButton#taskDelBtn:hover { color: #ef4444; }
            
            QPushButton#newTaskBtn {
                font-family: 'Bebas Neue'; font-size: 40px; font-weight: normal; background-color: #3f3f3f; color: white; border-radius: 8px;
            }
            QPushButton#newTaskBtn:hover { background-color: #525252; }
            
            /* Dialogs */
            QLabel#formTitle { font-size: 20px; font-weight: bold; }
            QLineEdit, QDateEdit, QComboBox, QTextEdit {
                background-color: #282c37; border: 1px solid #3f4451; border-radius: 4px; padding: 5px; color: white;
            }
            QPushButton#primaryBtn { background-color: #1d4ed8; color: white; border-radius: 6px; padding: 8px; font-weight: bold;}
            QPushButton#secondaryBtn { background-color: #4b5563; color: white; border-radius: 6px; padding: 8px; font-weight: bold;}
        )";
    } else {
        qss = R"(
            QMainWindow, QDialog, QWidget { background-color: #ffffff; color: #1a1a1a; font-family: 'Roboto', sans-serif; font-size: 15px;}
            QWidget#headerFrame { background-color: #f2f4f3; }
            QWidget#bottomLine { background-color: #e5e5e5; }
            QLabel#appTitle { font-family: 'Bebas Neue'; font-size: 40px; font-weight: normal; color: #1a1a1a; }
            QPushButton#settingsBtn { color: #1a1a1a; font-size: 24px; border: none; background: transparent; }
            QPushButton#settingsBtn:hover { color: black; }
            
            QWidget#sidebarFrame { background-color: #e6e7e9; }
            QPushButton#sidebarBtn {
                background-color: #fefefe; color: #1a1a1a; border: none; border-radius: 6px; padding: 10px; text-align: left;
            }
            QPushButton#sidebarBtn:hover { background-color: #f3f4f6; }
            QPushButton#sidebarBtn[active="true"] { background-color: #e5e7eb; font-weight: bold; border-left: 4px solid #0ea5e9; }
            
            QWidget#mainFrame { background-color: #ffffff; }
            QScrollArea#scrollArea, QWidget#scrollContentWidget { background-color: transparent; }
            
            QFrame#taskFrame { background-color: #e5e5e5; border-radius: 8px; }
            QCheckBox#taskCheckBox { color: #1a1a1a; }
            QCheckBox#taskCheckBox[taskState="done"] { color: #9ca3af; }
            QCheckBox#taskCheckBox[taskState="overdue"] { color: #dc2626; font-weight: bold; }
            QCheckBox::indicator { width: 18px; height: 18px; }
            QPushButton#taskDelBtn { color: gray; font-size: 18px; border: none; background: transparent; }
            QPushButton#taskDelBtn:hover { color: #ef4444; }
            
            QPushButton#newTaskBtn {
                font-family: 'Bebas Neue'; font-size: 40px; font-weight: normal; background-color: #e5e5e5; color: #1a1a1a; border-radius: 8px;
            }
            QPushButton#newTaskBtn:hover { background-color: #d1d5db; }
            
            /* Dialogs */
            QDialog { background-color: #ffffff; }
            QLabel#formTitle { font-size: 20px; font-weight: bold; background-color: transparent; }
            QLineEdit, QDateEdit, QComboBox, QTextEdit {
                background-color: #f9fafb; border: 1px solid #d1d5db; border-radius: 4px; padding: 5px; color: #1f2937;
            }
            QPushButton#primaryBtn { background-color: #0ea5e9; color: white; border-radius: 6px; padding: 8px; font-weight: bold;}
            QPushButton#secondaryBtn { background-color: #9ca3af; color: white; border-radius: 6px; padding: 8px; font-weight: bold;}
            QPushButton#primaryBtn:hover { background-color: #0284c7; }
            QPushButton#secondaryBtn:hover { background-color: #6b7280; }
        )";
    }
    
    qApp->setStyleSheet(qss);
}
