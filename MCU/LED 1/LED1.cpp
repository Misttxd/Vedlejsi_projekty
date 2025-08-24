
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


DigitalOut leds[] = {P4_00, P4_01, P4_02, P4_03, P4_12, P4_13, P4_16, P4_20, P0_14, P0_24, P0_28};
DigitalIn but1(P3_18);
DigitalIn but2(P3_19);
DigitalIn but3(P3_20);
DigitalIn but4(P3_21);

int i = 0; // index ledek (bude to mit potom problemy s for cyclem)

int y = 0; //btn2 blik

int z = 0;//0 = btn2 up, else btn2 down
int x = 0;//0 = btn3 up, else btn2 down

Ticker ticker_CV1; //ticker pro cv1
Ticker ticker_CV2; //ticker pro cv2
Ticker ticker_CV3;

Ticker ticker_btn2_check;
Ticker ticker_btn3_check;

Ticker check_all_buttons;

Ticker seconds;

int pwm_counter = 0;

int seconds_counter = 0;

void CV1() {
    y = 0;

    if (!but1.read()) {  // Tlačítko stisknuto
        while (!but1.read());  // Počká na uvolnění tlačítka

        leds[i].write(0);  // Vypnout aktuální LED
        i++;
        if (i >= 11) i = 0;  // Přetečení

        leds[i].write(1);  // Zapnout novou LED

    }
}

void CV2() {
    static int y = 0;

    if (y == 500 && z != 0)
    {
        leds[i].write(!leds[i].read());  // Přepnutí LED
    }


    if (y == 500) {
        y = 0;
    }
    y++;
}

void CV3() {

	//leds[i].write(!leds[i].read());
	if (pwm_counter > 10)
	{
		pwm_counter = 1;
	}

	if (x != 0)
	{
		if (pwm_counter >= 0 && pwm_counter <= seconds_counter)
		{
			leds[i].write(1);
		}
		else
		{
			leds[i].write(0);
		}


		pwm_counter ++;
	}
}

void btn2_check() {
    if (!but2.read()) {
        z++;
    } else {
        z = 0;
    }
}

void btn3_check() {
    if (!but3.read()) {
        x++;
    } else {
        x = 0;
    }
}

void LED_ON_WHEN_END()
{
	if (z == 0 && x ==0) //kdyz jsou obe tlacitka NEzmackla tak to bude svitit
	{					// bude to kolidovat s dalsim blikanic pokud se neprida do ifu
		leds[i].write(1);
	}
}

void seconds_inc()
{
	seconds_counter++;

	if (seconds_counter > 10)
	{
		seconds_counter = 1;
	}


}


int main() {
    leds[0].write(1);

    ticker_CV1.attach(&CV1, 50);
    ticker_CV2.attach(&CV2, 1);
    ticker_CV3.attach(&CV3, 1);

    ticker_btn2_check.attach(&btn2_check, 20);
    ticker_btn3_check.attach(&btn3_check, 20);

    check_all_buttons.attach(&LED_ON_WHEN_END,1);

    seconds.attach(&seconds_inc,1000);

}
