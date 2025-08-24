/**
 * @file    main-leds.cpp
 * @brief   Application entry point.
 */
#include <stdio.h>
#include <functional>
#include "board.h"
#include "peripherals.h"
#include "pin_mux.h"
#include "clock_config.h"
#include "fsl_debug_console.h"

#include "fsl_gpio.h"
#include "fsl_port.h"
#include "fsl_mrt.h"

#include "mcxn-kit.h"

// **************************************************************************
//! System initialization. Do not modify it!!!
void _mcu_initialization() __attribute__(( constructor( 0x100 ) ));

void _mcu_initialization()
{
    BOARD_InitBootPins();
    BOARD_InitBootClocks();
    BOARD_InitBootPeripherals();
    BOARD_InitDebugConsole();
    CLOCK_EnableClock( kCLOCK_Gpio0 );
    CLOCK_EnableClock( kCLOCK_Gpio1 );
    CLOCK_EnableClock( kCLOCK_Gpio2 );
    CLOCK_EnableClock( kCLOCK_Gpio3 );
    CLOCK_EnableClock( kCLOCK_Gpio4 );
}
// **************************************************************************

//! Global data

//! LEDs on MCXN-KIT - instances of class DigitalOut
/*DigitalOut g_led_P3_16( P3_16 );
DigitalOut g_led_P3_17( P3_17 );

//! Button on MCXN-KIT - instance of class DigitalIn
DigitalIn g_but_P3_18( P3_18 );
DigitalIn g_but_P3_19( P3_19 );
DigitalIn g_but_P3_20( P3_20 );
DigitalIn g_but_P3_21( P3_21 );*/


DigitalOut leds[] = {P4_00, P4_01, P4_02, P4_03, P4_12, P4_13, P4_16, P4_20};

/*DigitalOut RGB_leds[3][3] ={
    {P0_14, P0_15, P0_22},
    {P0_24, P0_25, P0_26},
    {P0_28, P0_29, P0_30}
};*/

//DigitalOut PTA[] = {P3_16, P3_17}; //MOZNÁ NEBUTE SE NEBUDE SHODOVAT

DigitalIn but1(P3_18);
DigitalIn but2(P3_19);
DigitalIn but3(P3_20);
DigitalIn but4(P3_21);

int program = 0; //1/2 podle vybraneho programu
int RGB_index = 0;
int COLOR = 0;


int i = 0; // index ledek (bude to mit potom problemy s for cyclem)



int w = 0;//0 = btn1 up, else btn1 down
int x = 0;//0 = btn2 up, else btn2 down
int y = 0;//0 = btn3 up, else btn3 down
int z = 0;//0 = btn4 up, else btn4 down

Ticker ticker_PROGRAM_1_SELECT;
Ticker ticker_PROGRAM_1_COLOR; //
Ticker ticker_PROGRAM_1_PWM;

Ticker ticker_program_switch;

Ticker ticker_btn1_check;
Ticker ticker_btn2_check;
Ticker ticker_btn3_check;
Ticker ticker_btn4_check;

Ticker check_all_buttons;

Ticker seconds;

int pwm_counter = 0;

int seconds_counter = 0;

int RED_on = 1;
int GREEN_on = 0;
int BLUE_on = 0;


Ticker T_zavory;
Ticker T_Blikani_STOP;
Ticker T_Blikani_GO;


Ticker T_posun;
Ticker T_PWM;
int LED_count = 1;

int led_index = 0;

int direction = 0; //RIGHT = 1, LEFT = 2

int rychlost = 1000;
int brightness = 10;



void program_switch()
{
	if (!but1.read())
	{
		while (!but1.read());
		rychlost = rychlost - 50;
		if (rychlost <= 0)
		{
			rychlost = 1000;
		}


	}

	if(!but2.read())
	{
		while (!but2.read());
		brightness = brightness - 1;

		if (brightness <= 1){
			brightness = 10;
		}

	}

	if(!but3.read())
	{
		while (!but3.read());

		LED_count ++;
		if (LED_count>2)
		{
			if (direction == 1)
			{
				leds[led_index-1].write(0);
			}
			else if (direction == 2)
			{
				leds[led_index+1].write(0);
			}

			LED_count =1;
		}



	}
}

void PWM()
{
	//leds[i].write(!leds[i].read());

	if (LED_count == 1)
	{
		if (pwm_counter > 10)
			{
				pwm_counter = 1;
			}

			if (pwm_counter >= 0 && pwm_counter <= brightness)
			{
				leds[led_index].write(1);
			}

			else
			{
				leds[led_index].write(0);
			}

			pwm_counter ++;
	}
	if (LED_count == 2)
		{
			if (pwm_counter > 10)
				{
					pwm_counter = 1;
				}

				if (pwm_counter >= 0 && pwm_counter <= brightness)
				{
					leds[led_index].write(1);
					leds[led_index-1].write(1);
				}

				else
				{
					leds[led_index].write(0);
					leds[led_index-1].write(0);
				}

				if (pwm_counter >= 0 && pwm_counter <= brightness-1)
				{
					leds[led_index].write(1);
					leds[led_index-1].write(1);
				}

				else
				{
					leds[led_index].write(0);
					leds[led_index-1].write(0);
				}

				pwm_counter ++;
		}


}

void posun()
{
    static int y = 0;

    if (y >= rychlost)

    {
    	if (led_index >7)
    	{
    		direction = 2;
    		led_index = 7;
    	}
    	if (led_index <0)
		{
			direction = 1;
			led_index = 0;

		}


    	if (direction == 1)
    	{
    		if (LED_count == 1)
    		{
				leds[led_index].write(0);
				if (led_index !=7)
				{
					leds[led_index+1].write(1);
				}
				else
				{
					leds[led_index].write(1);
				}
				led_index ++;
    		}
    		if (LED_count == 2)
			{
				leds[led_index-1].write(0);
				if (led_index !=7)
				{
					leds[led_index+1].write(1);
					leds[led_index].write(1);

				}
				else
				{
					leds[led_index].write(1);
					leds[led_index-1].write(1);
				}
				led_index ++;
			}



    	}
    	if (direction == 2)
		{
    		if (LED_count == 1)
    		{
    			leds[led_index].write(0);
				if (led_index !=0)
				{
					leds[led_index-1].write(1);
				}
				else
				{
					leds[led_index].write(1);
				}

				led_index --;
    		}

    		if (LED_count == 2)
			{
				leds[led_index+1].write(0);
				leds[led_index+2].write(0);

				if (led_index !=0)
				{
					leds[led_index-1].write(1);
					leds[led_index].write(1);

				}
				else
				{
					leds[led_index].write(1);
					leds[led_index+1].write(1);
				}

				led_index --;
			}

		}

    }

    if (y >= rychlost) {

        y = 0;

    }

    y++;
}



void seconds_inc()
{
	if (!but4.read())
	{
		seconds_counter++;

		    if (seconds_counter > 8)
		    {
		        seconds_counter = 1;
		    }
	}
	else
	{
		seconds_counter = 1;
	}

}

int main()
{
	direction = 1;
	leds[0].write(1);

	ticker_program_switch.attach(&program_switch, 1);

	T_posun.attach(&posun, 1);
	T_PWM.attach(&PWM, 1);

	seconds.attach(&seconds_inc,1000);



}
