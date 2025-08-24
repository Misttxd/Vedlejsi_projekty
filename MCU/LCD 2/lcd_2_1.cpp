

#include <stdio.h>
#include <functional>
#include "board.h"
#include "peripherals.h"
#include "pin_mux.h"
#include "clock_config.h"
#include "fsl_debug_console.h"

/* TODO: insert other include files here. */

#include "fsl_gpio.h"
#include "fsl_port.h"
#include "fsl_mrt.h"
#include "fsl_lpspi.h"

#include "mcxn-kit.h"
#include "lcd_lib.h"
#include "cts_lib.h"

// **************************************************************************
//! System initialization. Do not modify it!!!
void _mcu_initialization() __attribute__((constructor(0x100)));

void _mcu_initialization()
{
    BOARD_InitBootPins();
    BOARD_InitBootClocks();
    BOARD_InitBootPeripherals();
    BOARD_InitDebugConsole();
    CLOCK_EnableClock(kCLOCK_Gpio0);
    CLOCK_EnableClock(kCLOCK_Gpio1);
    CLOCK_EnableClock(kCLOCK_Gpio2);
    CLOCK_EnableClock(kCLOCK_Gpio3);
    CLOCK_EnableClock(kCLOCK_Gpio4);
}
// **************************************************************************

//! Global data

//! LEDs on MCXN-KIT - instances of class DigitalOut
extern char g_font8x8[256][8];
extern uint32_t font[256][36];

DigitalOut LED1(P3_16);
DigitalOut LED2(P3_17);

//! Button on MCXN-KIT - instance of class DigitalIn
DigitalIn BUT1(P3_18);
DigitalIn BUT2(P3_19);
DigitalIn BUT3(P3_20);
DigitalIn BUT4(P3_21);

uint16_t l_color_red = 0xF800;
uint16_t l_color_green = 0x07E0;
uint16_t l_color_blue = 0x001F;
uint16_t l_color_white = 0xFFFF;

uint16_t selected_color = 0xFFFF;
uint16_t black = 0x0000;

Ticker ticker_Cirkel_move;
Ticker ticker_Levy_hrac_move;
Ticker ticker_Pravy_hrac_move;
Ticker ticker_End_game_check;

int height = LCD_HEIGHT;
int width = LCD_WIDTH;
int32_t cts_get_ts_points(cts_points_t *t_points);

int tvar = 0;

class Cirkel
{

public:
    int radius;
    int Xcenter;
    int Ycenter;
    uint16_t color;
    Cirkel(int radius, int Xcenter, int Ycenter, uint16_t color)
    {
        this->radius = radius;
        this->Xcenter = Xcenter;
        this->Ycenter = Ycenter;
        this->color = color;
    }

    void draw()
    {
        int x = radius;
        int y = 0;
        int off = 0;

        while (x >= y)
        {
            lcd_put_pixel(Xcenter + x, Ycenter + y, color);
            lcd_put_pixel(Xcenter - x, Ycenter + y, color);
            lcd_put_pixel(Xcenter + x, Ycenter - y, color);
            lcd_put_pixel(Xcenter - x, Ycenter - y, color);
            lcd_put_pixel(Xcenter + y, Ycenter + x, color);
            lcd_put_pixel(Xcenter - y, Ycenter + x, color);
            lcd_put_pixel(Xcenter + y, Ycenter - x, color);
            lcd_put_pixel(Xcenter - y, Ycenter - x, color);

            if (off <= 0)
            {
                y++;
                off += 2 * y + 1;
            }
            if (off > 0)
            {
                x--;
                off -= 2 * x + 1;
            }
        }
    }

    void drawFilled()
    {
        for (int r = radius; r > 0; --r)
        {
            Cirkel inner(r, Xcenter, Ycenter, color);
            inner.draw();
        }
    }

    void erase()
    {
        uint16_t black = 0x0000;
        for (int r = radius; r > 0; --r)
        {
            Cirkel inner(r, Xcenter, Ycenter, black);
            inner.draw();
        }
    }

    void moveTo(int newX, int newY)
    {
        erase();
        Xcenter = newX;
        Ycenter = newY;
        drawFilled();
    }

    void moveBy(int dx, int dy)
    {
        erase();
        Xcenter += dx;
        Ycenter += dy;
        draw();
    }
};

class Rectangle {
public:
    int x, y; // zacina na levy horni roh
    int width, height;
    uint16_t color;

    Rectangle(int x, int y, int size, uint16_t color): x(x), y(y), width(size), height(size), color(color)
    {
        //ctverec
    }

    Rectangle(int x, int y, int width, int height, uint16_t color): x(x), y(y), width(width), height(height), color(color)
    {
        //obdelnik
    }

    void draw() {
        for (int i = 0; i < width; i++) {
            lcd_put_pixel(x + i, y, color);
            lcd_put_pixel(x + i, y + height - 1, color);
        }
        for (int i = 0; i < height; i++) {
            lcd_put_pixel(x, y + i, color);
            lcd_put_pixel(x + width - 1, y + i, color);
        }
    }

    void drawFilled() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                lcd_put_pixel(x + j, y + i, color);
            }
        }
    }

    void erase() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                lcd_put_pixel(x + j, y + i, black);
            }
        }
    }

    void moveTo(int newX, int newY)
     {
         erase();
         x = newX;
         y = newY;
         drawFilled();
     }
};


void Velikost(char bitmap[8], int x, int y, uint16_t color, int zvetseni)
{
    for (int i = 0; i < 8; i++)
    {
        for (int j = 7; j > -1; j--)
        {
            if (bitmap[i] & (1 << j))
            {
                for (int k = 0; k < zvetseni; k++)
                {
                    for (int l = 0; l < zvetseni; l++)
                    {
                        lcd_put_pixel(x + (1 + j) * zvetseni + k, y + i * zvetseni + l, color);
                    }
                }
            }
        }
    }
}


void mazejDisplej(int PozX, int PozY, int delka, int vyska)
{
   for (int y = PozY; y < PozY + vyska; y++)
   {
       for (int x = PozX; x < PozX + delka; x++)
       {
           lcd_put_pixel(x, y, 0x0000);
       }
   }
}

Cirkel kruh(10, height/2, width/2, l_color_red);
Rectangle Levy_hrac(10, 90, 3,20, selected_color);
Rectangle Pravy_hrac(width-10, 90, 3, 20, selected_color);

int p = 0;

int direction = 2;

void Cirkle_move()
{

   kruh.moveTo(kruh.radius + p + width/2, height/2);
   p += direction;


   if ((kruh.radius + p + kruh.radius + width/2 +100 >= width) || (kruh.radius + p - kruh.radius  +width/2 <= 0))
    {
        direction = -direction;
    }




}

void Levy_hrac_move()
{
   cts_points_t l_tpoints;
   l_tpoints.m_points[0].size = 0;
   int l_num = cts_get_ts_points(&l_tpoints);

   if (l_num ==1 && l_tpoints.m_points[0].x < width/2)
   {
       Levy_hrac.moveTo(10, l_tpoints.m_points[0].y);
   }

   else if (l_num > 1)
   {
       if (l_tpoints.m_points[0].x < width/2)
       {
           Levy_hrac.moveTo(10, l_tpoints.m_points[0].y);
       }
   }
}

void Pravy_hrac_move()
{
   cts_points_t l_tpoints;
   l_tpoints.m_points[0].size = 0;
   int l_num = cts_get_ts_points(&l_tpoints);

   if (l_num == 1 && l_tpoints.m_points[0].x > width/2)
   {
       Pravy_hrac.moveTo(width-10, l_tpoints.m_points[0].y);
   }

   else if (l_num > 1)
   {
       if (l_tpoints.m_points[1].x > width/2)
       {
           Pravy_hrac.moveTo(width-10, l_tpoints.m_points[1].y);
       }
   }
}

void End_game_check()
{


    /*if (kruh.Xcenter- kruh.radius  < Levy_hrac.x + Levy_hrac.width && (Levy_hrac.y > kruh.Ycenter + kruh.radius || Levy_hrac.y + Levy_hrac.width < kruh.Ycenter + kruh.radius ) )
   {
       ticker_Cirkel_move.detach();
       ticker_Levy_hrac_move.detach();
       ticker_Pravy_hrac_move.detach();

       mazejDisplej(0,0, width, height);

       ticker_End_game_check.detach();

   }*/
}


int main()
{
    lcd_init(); // LCD initialization

    if (cts_init() < 0)
    {
        PRINTF("Touch Screen not detected!\r\n");
    }

    cts_points_t l_tpoints;
    l_tpoints.m_points[0].size = 0;


    Levy_hrac.drawFilled();
    Pravy_hrac.drawFilled();



    ticker_Cirkel_move.attach(&Cirkle_move, 2);
    ticker_Levy_hrac_move.attach(&Levy_hrac_move, 2);
    ticker_Pravy_hrac_move.attach(&Pravy_hrac_move, 2);
    ticker_End_game_check.attach(&End_game_check, 1);





    while (1)
    {


    }

    return 0;
}
