#include "drawing.h"
#include <stdlib.h>
#include <math.h>

void set_random_color() {
    int color = rand() % 5;     //nemam paru jak to ty barvy vybira, protoze jsou furt stejne kdyz to spustim znova, ale dokud to funguje tak neni co zpravovat
    switch(color) {
        case 0:
            set_white_color();
            break;
        case 1:
            set_green_color();
            break;
        case 2:
            set_blue_color();
            break;
        case 3:
            set_red_color();
            break;
        case 4:
            set_yellow_color();
            break;

    }
    
}

void PrerusovanaCaraMain(int length){
    set_random_color(); 
    
    for (int i = 0; i < length; i++) {
        for (int i = 0; i < 5; i++){
            draw_pixel();
            move_right();
        }
        move_right();
    }
}

void PrerusovanaCara(int length){
    int Radek = 1; //(řádek)
    clear_screen();
    for (int j = 0; j < 4; j++){
        PrerusovanaCaraMain(length);
        Radek = Radek + 3;
        move_to(Radek, 1);
    }
    end_drawing();
}

void Schody(int length){
    clear_screen();
    for (int i = 0; i < length; i++) {          //fakt doufam ze nekolik schodu znamena nekolik stupinku schodu
        set_random_color();
        for (int i = 0; i < 3; i++){
            draw_pixel();
            move_right();
        }
        draw_pixel();               
        draw_pixel();
        move_down();
    }
    for (int j = 0; j < 3; j++){
            draw_pixel();
            move_right();
            draw_pixel();
        }
    
    end_drawing();
}

void kvetinaMain(int Length, int Width, int x, int y){
    //vykresleni horni liny
    set_yellow_color();
    move_to(y,x + 1);
    for (int i = 0; i < Length; i++){
        draw_pixel();
        move_right();
    }

    //vykresleni prostredni liny
    move_to(y + 1,x);
    for (int j = 0; j < Length + 2; j++){
        //cerny prostredek
        if((j > ((1.0f/3.0f) * (Length + 1))) && (j< (2.0f/3.0f) * (Length + 1))){
        set_black_color();
        draw_pixel();
        move_right();
        }
        //zbytek zluty
        else{
        set_yellow_color();
        draw_pixel();
        move_right();
        }
    }

    //vykresleni spodni liny
    move_to(y + 2,x + 1);
    for (int k = 0; k < Length; k++){
        draw_pixel();
        move_right();
    }

    //vykresleni stonku
    move_to(y + 3, x + Length/2 + Length%2);
    set_green_color();
    for (int l = 0; l< Length; l++){
        draw_pixel();
        move_down();
    }
    move_to(y, x + Length);
}

void Kvetina(int Length, int Width){
    int x = 5;
    int y = 1;
    clear_screen();
    kvetinaMain(Length, Width, x, y);
    kvetinaMain(Length * 1.5f, Width * 1.5f, x + 15, y);
    kvetinaMain(Length * 2.0f, Width * 2.0f, x + 30, y);
    kvetinaMain(Length * 0.7f, Width * 0.7f, x + 7, y + 3);
    kvetinaMain(Length * 0.7f, Width * 0.7f, x + 7, y + 8);
    kvetinaMain(Length * 0.9f, Width * 0.9f, x + 15, y + 8);
    kvetinaMain(Length * 2.0f, Width * 2.0f, x + 30, y);
    kvetinaMain(Length * 1.6f, Width * 1.6f, x + 20, y + 13);
    kvetinaMain(Length * 1.3f, Width * 1.3f, x + 15, y + 15);
    kvetinaMain(Length * 3, Width * 3, x + 50, y + 2);
    end_drawing();
}

void Louka(int Length, int Width){
    int x = 3;
    int y = 1;
    clear_screen();
    move_to(y, x);
    for (int i = 0; i < 7; i++){
        kvetinaMain(Length, Width, x, y);
        x = x + 10;
    }

    x = 3;
    y = 1;

    for (int i = 0; i < 7; i++){
        kvetinaMain(Length, Width, x, y + 2* Width);
        x = x + 10;
    }
    end_drawing();
}

void Animace(){
    clear_screen();

    set_green_color();
    move_to(12, 10);
    for (int i = 0; i<7; i++){
        draw_pixel();
        move_up();
        animate_ms(300);
    }
    move_to(5,7);
    set_red_color();
    for (int j = 0; j < 7; j++){
        draw_pixel();
        move_right();
    }
    animate_ms(700);
    move_to(4, 6);
    for (int k = 0; k < 9; k++){
        //cerny prostredek
        if((k > 2) && (k< 6)){
        set_black_color();
        draw_pixel();
        move_right();
        }
        //zbytek zluty
        else{
        set_red_color();
        draw_pixel();
        move_right();
        }

    }
    animate_ms(700);
    move_to(3, 7);
    for (int l = 0; l < 7; l++){
        draw_pixel();
        move_right();
    }
    animate_ms(500);
    move_to(7, 20);
    set_black_color();
    animate_ms(90);
    printf("k");
    animate_ms(90);
    printf("y");
    animate_ms(90);
    printf("t");
    animate_ms(90);
    printf("i");
    animate_ms(90);
    printf("\304\215");
    animate_ms(90);
    printf("k");
    animate_ms(90);
    printf("a ");
    animate_ms(90);
    printf(":");
    animate_ms(90);
    printf(")");
    animate_ms(90);
    printf(")");
    animate_ms(90);
    printf(")");
    animate_ms(90);
    printf(")");

    end_drawing();
    
}

int main() {    
    int length = 15;

    int KLenght = 7;
    int KWidth = 7;

    int drawing;
    scanf("%d", &drawing);

    switch (drawing) {
    case 0:
      PrerusovanaCara(length);
      break;
    case 1:
      Schody(length);
      break;
    case 2:
      Kvetina(KLenght, KWidth);
      break;
    case 3:
      Louka(KLenght, KWidth);
      break;
    case 4:
      Animace();
      break;
    default:
      printf("Moznosti jsou od 0 - 4\n");
  }

  return 0;
}
