// **************************************************************************
//
//               Demo program for APPS labs
//
// Subject:      Computer Architectures and Parallel systems
// Author:       Petr Olivka, petr.olivka@vsb.cz, 02/2025
// Organization: Department of Computer Science, FEECS,
//               VSB-Technical University of Ostrava, CZ
//
// File:         Main program for LCD module
//
// **************************************************************************

/**
 * @file    main-lcd.cpp
 * @brief   Application entry point.
 */

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

int height = LCD_HEIGHT;
int width = LCD_WIDTH;
int32_t cts_get_ts_points(cts_points_t *t_points);

int tvar = 0;

void cirkel(int Xc, int Yc, int r, uint16_t color)
{

    int x = r;
    int y = 0;
    int off = 0;

    while (x >= y)
    {
        lcd_put_pixel(Xc + x, Yc + y, color);
        lcd_put_pixel(Xc - x, Yc + y, color);
        lcd_put_pixel(Xc + x, Yc - y, color);
        lcd_put_pixel(Xc - x, Yc - y, color);
        lcd_put_pixel(Xc + y, Yc + x, color);
        lcd_put_pixel(Xc - y, Yc + x, color);
        lcd_put_pixel(Xc + y, Yc - x, color);
        lcd_put_pixel(Xc - y, Yc - x, color);

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

void vypsaniTextu(char text[], int x, int y, uint16_t color, int zvetseni)
{
    int length = strlen(text);

    int pomocX = 0;
    for (int i = 0; i < length; i++)
    {
        int znak = text[i];
        Velikost(g_font8x8[znak], x + pomocX, y, color, zvetseni);
        pomocX += 8 * zvetseni;
    }
}

void drawRectangle(int x, int y, int width, int height, uint16_t color)
{
    for (int i = x; i < x + width; i++)
    {
        lcd_put_pixel(i, y, color);
        lcd_put_pixel(i, y + height - 1, color);
    }
    for (int j = y; j < y + height; j++)
    {
        lcd_put_pixel(x, j, color);
        lcd_put_pixel(x + width - 1, j, color);
    }
}

int main()
{
    int fingerprint_toggle = 0;

    lcd_init(); // LCD initialization

    if (cts_init() < 0)
    {
        PRINTF("Touch Screen not detected!\r\n");
    }

    cts_points_t l_tpoints;
    l_tpoints.m_points[0].size = 0;

    vypsaniTextu("C", 20, 250, l_color_white, 8);
    vypsaniTextu("S", 400, 250, l_color_white, 8);

    while (1)
    {
        int l_num = cts_get_ts_points(&l_tpoints);

        if (l_num > 0)
        {
            PRINTF("TS Points: %d\r\n", l_num);
            for (int p = 0; p < l_num; p++)
            {
                PRINTF("Point [%d]: x=%d y=%d size=%d\r\n",
                       p, l_tpoints.m_points[p].x, l_tpoints.m_points[p].y, l_tpoints.m_points[p].size, l_color_blue);
            }
        }

        if (!BUT1.read())
        {
            while (!BUT1.read())
                ;
            selected_color = l_color_red;
            PRINTF("selected color: RED\n");
        }

        if (!BUT2.read())
        {
            while (!BUT2.read())
                ;
            selected_color = l_color_green;
            PRINTF("selected color: GREEN\n");
        }

        if (!BUT3.read())
        {
            while (!BUT3.read())
                ;
            selected_color = l_color_blue;
            PRINTF("selected color: BLUE\n");
        }

        if (!BUT4.read())
        {
            while (!BUT4.read())
                ;
            selected_color = l_color_white;
            PRINTF("selected color: WHITE\n");
        }

        if (l_num > 0)
        {
            if ((l_tpoints.m_points[0].x > 0 && l_tpoints.m_points[0].x < 70) && (l_tpoints.m_points[0].y > 200 && l_tpoints.m_points[0].y < 320))
            {

                tvar = 0;
                for (int i = 100; i > 0; i--)
                {
                    drawRectangle(width / 2 - i / 2, height / 2 - i / 2, i, i, black);
                }

                for (int i = 50; i > 0; i--)
                {
                    cirkel(width / 2, height / 2, i, black);
                }

                cirkel(width / 2, height / 2, 50, selected_color);
            }
            /*PRINTF( "TS Points: %d\r\n", l_num );
            for ( int p = 0; p < l_num; p++ )
            {
                PRINTF( "Point [%d]: x=%d y=%d size=%d\r\n",
                        p, l_tpoints.m_points[ p ].x, l_tpoints.m_points[ p ].y, l_tpoints.m_points[ p ].size, l_color_blue );
            }*/

            else if ((l_tpoints.m_points[0].x > 360 && l_tpoints.m_points[0].x < 420) && (l_tpoints.m_points[0].y > 200 && l_tpoints.m_points[0].y < width))
            {
                tvar = 1;
                for (int i = 50; i > 0; i--)
                {
                    cirkel(width / 2, height / 2, i, black);
                }

                for (int i = 100; i > 0; i--)
                {
                    drawRectangle(width / 2 - i / 2, height / 2 - i / 2, i, i, black);
                }

                drawRectangle(width / 2 - 50, height / 2 - 50, 100, 100, selected_color);
            }
            else if ((l_tpoints.m_points[0].x > width / 2 - 40 && l_tpoints.m_points[0].x < width / 2 + 40) && (l_tpoints.m_points[0].y > height / 2 - 40 && l_tpoints.m_points[0].y < height / 2 + 40))
            {
                if (tvar == 0)
                {
                    for (int i = 50; i > 0; i--)
                    {
                        cirkel(width / 2, height / 2, i, selected_color);
                    }
                }
                else if (tvar == 1)
                {
                    for (int i = 100; i > 0; i--)
                    {
                        drawRectangle(width / 2 - i / 2, height / 2 - i / 2, i, i, selected_color);
                    }
                }
            }
        }

        /*if(!BUT2.read())
        {
            while(!BUT2.read());
            if (fingerprint_toggle == 0)
            {
                fingerprint_toggle = 1;
            }
            else if (fingerprint_toggle == 1)
            {
                fingerprint_toggle = 0;
            }
        }

        if (fingerprint_toggle == 1)
        {
            if ( l_num > 0 )
            {
                for ( int p = 0; p < l_num; p++ )
                {
                    for (int i = l_tpoints.m_points[ p ].size; i> 0; i--)
                    {
                        cirkel(l_tpoints.m_points[ p ].x, l_tpoints.m_points[ p ].y, i, l_color_blue);
                    }
                }
            }
        }*/
    }

    while (1)
        ;
}
