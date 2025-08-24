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



 Cirkel kruh(10, height/2, width/2, l_color_red);


 int p = 0;
 int q = 0;

 int direction = 0;

 void Cirkle_move()
 {
     if (!BUT1.read())
     {
         while (!BUT1.read());
         direction = 1; //LEFT

     }
     if (!BUT2.read())
	  {
		  while (!BUT2.read());
		  direction = 2; //UP

	  }
     if (!BUT3.read())
	  {
		  while (!BUT3.read());
		  direction = 3; //RIGHT

	  }
     if (!BUT4.read())
	  {
		  while (!BUT4.read());
		  direction = 4; //DOWN

	  }


	 kruh.moveTo(kruh.radius + p, kruh.radius + q);

	 if(direction == 1)
	 {
		 p -= 2;
		 q = q;
	 }
	 else if(direction == 2)
	 {
		 p = p;
		 q -= 2;
	 }
	 else if(direction == 3)
	 {
		 p += 2;
		 q = q;
	 }
	 else if(direction == 4)
	 {
		 p = p;
		 q += 2;
	 }




     if ((kruh.radius + p + kruh.radius + width/2 >= width) || (kruh.radius + p - kruh.radius  +width/2 <= 0))
     {
         //direction = -direction;
     }
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

     kruh.drawFilled();




     //vypsaniTextu("C", 20, 250, l_color_white, 8);
     //vypsaniTextu("S", 400, 250, l_color_white, 8);

     ticker_Cirkel_move.attach(&Cirkle_move, 2);
     //ticker_Levy_hrac_move.attach(&Levy_hrac_move, 2);
     //ticker_Pravy_hrac_move.attach(&Pravy_hrac_move, 2);
     //ticker_End_game_check.attach(&End_game_check, 1);





     while (1)
     {


     }
     return 0;
 }
