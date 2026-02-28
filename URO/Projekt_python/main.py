import customtkinter as ctk
from CTkTable import *
import tkinter as tk
import customtkinter as ctk
from CTkDatePicker import CTkDatePicker
from datetime import datetime


class TaskManagerApp(ctk.CTk):

    def __init__(self):
        super().__init__()

        self.task_list = []
        self.done_tasks = set()

        ctk.set_appearance_mode("Dark")
        self.geometry("1200x800")
        self.font = ("Noto", 15)
        self.font_heading = ("Noto", 22, "bold")
        self.font_small = ("Noto", 13)
        self.date_format = "%d.%m.%Y"
        self.create_form()
        self.add_task("Naučit se Python", "28.02.2026", "Osobní")
        self.add_task("Naučit se Tkinter", "15.03.2026", "Důležité")
        self.add_task("Naučit se CustomTkinter", "31.03.2026", "Osobní")

    def create_form(self):

        # Nastavení mřížky: Natahovat se bude řádek 1 a sloupec 1
        self.grid_rowconfigure(1, weight=1) 
        self.grid_columnconfigure(1, weight=1)

        # 1. Horní panel (Header - řádek 0)
        self.header_frame = ctk.CTkFrame(self, height=100, fg_color=("gray90", "gray17"), corner_radius=0)
        self.header_frame.grid(row=0, column=0, columnspan=2, sticky="nsew")
        
        self.header_frame.header_label = ctk.CTkLabel(self.header_frame, text="Správce úkolů", font=self.font_heading)
        self.header_frame.header_label.pack(side = "left", pady=20, padx=20)

        self.header_frame.settings_btn = ctk.CTkButton(self.header_frame, text="⚙", width=35, height=35, fg_color="transparent", text_color=("gray10", "gray90"), command=self.open_settings)
        self.header_frame.settings_btn.pack(side="right", padx=20, pady=20)

        # 2. Levý panel (Sidebar - řádek 1, sloupec 0)
        self.sidebar_frame = ctk.CTkFrame(self, width=200, fg_color=("gray90", "gray17"), corner_radius=0)
        self.sidebar_frame.grid(row=1, column=0, sticky="nsew")

        self.sidebar_frame.buttonAll = ctk.CTkButton(self.sidebar_frame, text="Všechny úkoly", font=self.font, fg_color="transparent", text_color=("gray10", "gray90"), anchor="w", command=lambda: self.show_all_tastks())
        self.sidebar_frame.buttonAll.pack(fill=ctk.X, padx=10, pady=3)
        self.sidebar_frame.buttonToday = ctk.CTkButton(self.sidebar_frame, text="Dnes", font=self.font, fg_color="transparent", text_color=("gray10", "gray90"), anchor="w", command = lambda: self.show_today_tasks())
        self.sidebar_frame.buttonToday.pack(fill=ctk.X, padx=10, pady=3)
        self.sidebar_frame.buttonImportant = ctk.CTkButton(self.sidebar_frame, text="Důležité", font=self.font, fg_color="transparent", text_color=("gray10", "gray90"), anchor="w", command = lambda: self.show_important_tasks())
        self.sidebar_frame.buttonImportant.pack(fill=ctk.X, padx=10, pady=3)

        # 3. Pravý panel (Hlavní obsah - řádek 1, sloupec 1)
        self.main_frame = ctk.CTkFrame(self, fg_color="transparent", corner_radius=0)
        self.main_frame.grid(row=1, column=1, sticky="nsew") 

        self.main_frame.scrollable_frame = ctk.CTkScrollableFrame(self.main_frame, fg_color="transparent")
        self.main_frame.scrollable_frame.pack(fill=ctk.BOTH, expand=True, padx=15, pady=(15, 5))

        self.main_frame.novyUkol_button = ctk.CTkButton(self.main_frame, text= "+ Přidat nový úkol", font= self.font, height=40, command = self.new_task_form)
        self.main_frame.novyUkol_button.pack(padx=15, pady=10)

    def destroy_tasks(self):
        for widget in self.main_frame.scrollable_frame.winfo_children():
            widget.destroy()    

    def show_all_tastks(self):
        self.destroy_tasks()
        for task in self.task_list:

            self.create_task_widget(task[0], task[2], task[1])

    def show_today_tasks(self):
        self.destroy_tasks()
        for task in self.task_list:
            deadline = datetime.strptime(task[2], self.date_format).date()
            today = datetime.today().date()
            if deadline == today:

                self.create_task_widget(task[0], task[2], task[1])
        

    def show_important_tasks(self):
        self.destroy_tasks()
        for task in self.task_list:
            if task[1] == "Důležité":

                self.create_task_widget(task[0], task[2], task[1])

    def open_settings(self):
        self.new_window = ctk.CTkToplevel(self)
        self.new_window.geometry("400x500")
        self.new_window.title("Nastavení")

        self.new_window.theme_frame = ctk.CTkFrame(self.new_window, fg_color="transparent")
        self.new_window.theme_frame.pack(pady=20, padx=20, fill="x")

        self.theme_var = ctk.StringVar(value=ctk.get_appearance_mode())

        self.new_window.theme_frame.radio_btn_light = ctk.CTkRadioButton(self.new_window.theme_frame, text="Světlý režim", variable=self.theme_var, value="Light",command=lambda: ctk.set_appearance_mode("Light"))
        self.new_window.theme_frame.radio_btn_light.pack(side = "left", pady=10)

        self.new_window.theme_frame.radio_btn_dark = ctk.CTkRadioButton(self.new_window.theme_frame, text="Tmavý režim", variable=self.theme_var, value="Dark", command=lambda: ctk.set_appearance_mode("Dark"))
        self.new_window.theme_frame.radio_btn_dark.pack(side="right", pady=10)

        self.new_window.feedback_label = ctk.CTkLabel(self.new_window, text="Napište zpětnou vazbu", font=self.font)
        self.new_window.feedback_label.pack(pady=20, padx=20)

        self.new_window.feedback_entry = ctk.CTkTextbox(self.new_window, font=self.font)
        self.new_window.feedback_entry.pack(pady=10, padx=20, fill="x")

        self.new_window.close_btn = ctk.CTkButton(self.new_window, text = "Zavřít", font=self.font, command=self.new_window.destroy)
        self.new_window.close_btn.pack(pady=10, padx=20, fill="x")

    
    def create_task_widget(self, nazev, deadline, kategorie):
        task_grid = ctk.CTkFrame(self.main_frame.scrollable_frame, fg_color=("gray90", "gray17"), corner_radius=8)
        task_grid.pack(fill=ctk.X, pady=3)

        checkbox = ctk.CTkCheckBox(task_grid, text=f"{nazev}  ·  {kategorie}  ·  {deadline}", font=self.font,
                                   command=lambda n=nazev: self.done_tasks.discard(n) if n in self.done_tasks else self.done_tasks.add(n))
        checkbox.pack(side="left", padx=12, pady=8)

        if nazev in self.done_tasks:
            checkbox.select()

        task_content = (nazev, kategorie, deadline)
        delete_button = ctk.CTkButton(task_grid, text="✕", width=30, height=30, fg_color="transparent", text_color="gray", command=lambda: self.delete_task(task_grid, task_content))
        delete_button.pack(side="right", padx=12, pady=8)


    
    def add_task(self, nazev, deadline, kategorie):
        self.task_list.append((nazev, kategorie, deadline))
        self.create_task_widget(nazev, deadline, kategorie)

    def delete_task(self, task_to_delete, task_content):
        task_to_delete.destroy()
        self.task_list.remove(task_content)

    def new_task_form(self):
        self.new_window = ctk.CTkToplevel(self)
        self.new_window.geometry("400x500")
        self.new_window.title("Přidat nový úkol")

        self.new_window.name_label = ctk.CTkLabel(self.new_window, text = "Název úkolu", font = self.font, anchor="w")
        self.new_window.name_label.pack(pady = (15, 5), padx = 20, fill="x")
        self.new_window.name_entry = ctk.CTkEntry(self.new_window, placeholder_text="Zadejte název úkolu...", font=self.font)
        self.new_window.name_entry.pack(pady = 10, padx = 20, fill="x")

        self.new_window.term_label = ctk.CTkLabel(self.new_window, text = "Termín", font = self.font, anchor="w")
        self.new_window.term_label.pack(pady = (10, 5), padx = 20, fill="x")
        self.new_window.term_entry = CTkDatePicker(self.new_window)
        self.new_window.term_entry.set_allow_manual_input(False)
        self.new_window.term_entry.set_date_format("%d.%m.%Y")
        self.new_window.term_entry.set_date(today=True)
        self.new_window.term_entry.pack(pady = 10, padx = 20, fill="x")

        self.new_window.category_label = ctk.CTkLabel(self.new_window, text = "Kategorie", font = self.font, anchor="w")
        self.new_window.category_label.pack(pady = (10, 5), padx = 20, fill="x")
        self.new_window.category_option = ctk.CTkOptionMenu(self.new_window, values=["Osobní", "Práce", "Důležité"], font=self.font, dropdown_font=self.font)
        self.new_window.category_option.pack(pady = 10, padx = 20, fill="x")

        self.new_window.save_btn = ctk.CTkButton(self.new_window, text="Uložit úkol", font=self.font, height=40, command=self.save_new_task)
        self.new_window.save_btn.pack(pady = (20, 5), padx = 20, fill="x")

        self.new_window.close_btn = ctk.CTkButton(self.new_window, text="Zrušit", font=self.font, height=40, fg_color="transparent", border_width=1, border_color="gray", command=self.new_window.destroy)
        self.new_window.close_btn.pack(pady = 5, padx = 20, fill="x")

    

    def save_new_task(self):
        nazev = self.new_window.name_entry.get()
        deadline = self.new_window.term_entry.get_date()
        kategorie = self.new_window.category_option.get()
        if not nazev or not deadline or not kategorie:
            print("Vyplňte všechny údaje")
        else:
            self.add_task(nazev, deadline, kategorie)
            self.new_window.destroy()


if __name__ == "__main__":
    app = TaskManagerApp()
    app.mainloop()