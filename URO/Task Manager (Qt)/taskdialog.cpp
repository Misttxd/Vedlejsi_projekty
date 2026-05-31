#include "taskdialog.h"
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QLabel>
#include <QDate>
#include <QMessageBox>

TaskDialog::TaskDialog(QWidget *parent) : QDialog(parent) {
    this->setWindowTitle("Přidat úkol");
    this->setFixedSize(320, 320);

    QVBoxLayout *mainLayout = new QVBoxLayout(this);
    mainLayout->setContentsMargins(15, 15, 15, 15);

    QLabel *header = new QLabel("Přidat úkol");
    header->setObjectName("formTitle"); // Pro stylování zadní část / tučné
    header->setAlignment(Qt::AlignCenter);
    mainLayout->addWidget(header);

    // Form fields
    QLabel *nameLabel = new QLabel("Název úkolu");
    nameLabel->setStyleSheet("font-weight: bold; font-size: 13px;");
    nameEdit = new QLineEdit();
    nameEdit->setPlaceholderText("Zadejte název úkolu...");
    mainLayout->addWidget(nameLabel);
    mainLayout->addWidget(nameEdit);

    QLabel *dateLabel = new QLabel("Termín");
    dateLabel->setStyleSheet("font-weight: bold; font-size: 13px; margin-top: 5px;");
    dateEdit = new QDateEdit(QDate::currentDate());
    dateEdit->setCalendarPopup(true);
    mainLayout->addWidget(dateLabel);
    mainLayout->addWidget(dateEdit);

    QLabel *categoryLabel = new QLabel("Kategorie");
    categoryLabel->setStyleSheet("font-weight: bold; font-size: 13px; margin-top: 5px;");
    categoryBox = new QComboBox();
    categoryBox->addItems({"Osobní", "Práce", "Důležité"});
    mainLayout->addWidget(categoryLabel);
    mainLayout->addWidget(categoryBox);

    // Spacer
    mainLayout->addStretch();

    // Buttons
    QHBoxLayout *btnLayout = new QHBoxLayout();
    saveBtn = new QPushButton("Uložit");
    saveBtn->setObjectName("primaryBtn"); // Nastaveno na modré ve stylech
    cancelBtn = new QPushButton("Zrušit");
    cancelBtn->setObjectName("secondaryBtn"); // Nastaveno na šedé ve stylech
    
    btnLayout->addWidget(saveBtn);
    btnLayout->addWidget(cancelBtn);
    mainLayout->addLayout(btnLayout);

    connect(saveBtn, &QPushButton::clicked, this, &TaskDialog::onSaveClicked);
    connect(cancelBtn, &QPushButton::clicked, this, &QDialog::reject);
}

void TaskDialog::onSaveClicked() {
    if (nameEdit->text().trimmed().isEmpty()) {
        QMessageBox::warning(this, "Chyba", "Název úkolu nesmí být prázdný!");
        return;
    }
    accept();
}

Task TaskDialog::getTask() const {
    return Task(nameEdit->text().trimmed(), dateEdit->date(), categoryBox->currentText(), false);
}
