#include "settingsdialog.h"
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QLabel>
#include <QGroupBox>

SettingsDialog::SettingsDialog(QWidget *parent) : QDialog(parent) {
    this->setWindowTitle("Nastavení");
    this->setFixedSize(400, 300);

    QVBoxLayout *mainLayout = new QVBoxLayout(this);
    mainLayout->setContentsMargins(20, 20, 20, 20);

    QLabel *header = new QLabel("Nastavení");
    header->setObjectName("formTitle"); 
    header->setAlignment(Qt::AlignCenter);
    mainLayout->addWidget(header);

    QHBoxLayout *themeLayout = new QHBoxLayout();
    lightModeBtn = new QRadioButton("Světlý režim");
    darkModeBtn = new QRadioButton("Tmavý režim");
    lightModeBtn->setChecked(true); 
    themeLayout->addWidget(lightModeBtn);
    themeLayout->addWidget(darkModeBtn);
    themeLayout->setAlignment(Qt::AlignCenter);
    mainLayout->addLayout(themeLayout);

    QLabel *helpLabel = new QLabel("Nápověda");
    helpLabel->setStyleSheet("font-weight: bold; margin-top: 10px;");
    mainLayout->addWidget(helpLabel);

    helpText = new QTextEdit();
    helpText->setPlaceholderText("Sem napište váš dotaz nebo zpětnou vazbu...");
    mainLayout->addWidget(helpText);

    backBtn = new QPushButton("Zpět");
    backBtn->setObjectName("secondaryBtn");
    
    QHBoxLayout *btnLayout = new QHBoxLayout();
    btnLayout->addStretch();
    btnLayout->addWidget(backBtn);
    btnLayout->addStretch();
    mainLayout->addLayout(btnLayout);

    connect(backBtn, &QPushButton::clicked, this, &QDialog::accept);
    connect(lightModeBtn, &QRadioButton::toggled, this, &SettingsDialog::onThemeToggled);
}

void SettingsDialog::onThemeToggled(bool checked) {
    emit themeChanged(darkModeBtn->isChecked());
}
