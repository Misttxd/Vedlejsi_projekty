// ***********************************************************************
//
// Demo program for education in subject
// Computer Architectures and Parallel Systems.
// Simplified version of ball animation using CUDA
//
// ***********************************************************************

#include <stdio.h>
#include <sys/time.h>
#include <cuda_device_runtime_api.h>
#include <cuda_runtime.h>
#include <opencv2/opencv.hpp>

#include "cuda_img.h"
#include "animation.h"

int main()
{
    Animation animation;
    
    cv::Mat ball_img = cv::imread("ball.png", cv::IMREAD_UNCHANGED);
    cv::Mat helicopter_img = cv::imread("helicopter.png", cv::IMREAD_UNCHANGED);
    cv::Mat background_img = cv::imread("more.jpg", cv::IMREAD_COLOR);

    cv::Mat screen_img = background_img.clone();

    CudaImg cuda_screen, cuda_ball, cuda_helicopter;
    
    cuda_screen.m_size.x = screen_img.cols;
    cuda_screen.m_size.y = screen_img.rows;
    cuda_screen.m_p_uchar3 = (uchar3 *)screen_img.data;

    cuda_ball.m_size.x = ball_img.cols;
    cuda_ball.m_size.y = ball_img.rows;
    cuda_ball.m_p_uchar4 = (uchar4 *)ball_img.data;
    
    cuda_helicopter.m_size.x = helicopter_img.cols;
    cuda_helicopter.m_size.y = helicopter_img.rows;
    cuda_helicopter.m_p_uchar4 = (uchar4 *)helicopter_img.data;

    animation.start(cuda_screen, cuda_ball, cuda_helicopter);

    float scale_min = 1.0f;         
    float scale_max = 1.2f;          
    float scale_speed = 0.09f;       
    float current_scale = 1.0f;      
    bool growing = true;             
    int cycles = 0;                  

    int center_x = screen_img.cols / 2;
    int center_y = screen_img.rows / 2;

    float fall_speed = 50.0f;       
    float roll_speed = 150.0f;      
    float y_position = center_y;     
    float x_position = center_x;   
    
    float rotation = 0.0f;           
    float rotation_speed = 2.0f;     
    bool enable_rotation = true;     
    
    float heli_speed = 150.0f;    
    float heli_x_position = -helicopter_img.cols;
    float heli_y_position = center_y / 1.5;     
    bool ball_spawned = false;     
    
    int run_simulation = 1;
    bool falling_phase = true;     
    bool rolling_phase = false;   
    float ground_level = 0;       
    
    timeval start_time, cur_time, old_time, delta_time;
    gettimeofday(&old_time, NULL);
    start_time = old_time;

    while (run_simulation) 
    {
        int key = cv::waitKey(1);
        if (key == 27) break;  

        gettimeofday(&cur_time, NULL);
        timersub(&cur_time, &old_time, &delta_time);
        if (delta_time.tv_usec < 1000) continue; 
        old_time = cur_time;
        float delta_sec = (float)delta_time.tv_usec / 1E6;

        heli_x_position += heli_speed * delta_sec;
        
        int heli_pos_x = (int)heli_x_position;
        int heli_pos_y = (int)heli_y_position - helicopter_img.rows / 2;
        
        int scaled_width = (int)(ball_img.cols * current_scale);
        int scaled_height = (int)(ball_img.rows * current_scale);
        int ball_pos_x = -1;
        int ball_pos_y = -1; 
        
        if (!ball_spawned && heli_x_position >= center_x) {
            ball_spawned = true;
            current_scale = scale_max;
            y_position = center_y;
            x_position = center_x;
            rotation = 0.0f;
            cycles = 0;
            falling_phase = true;
            rolling_phase = false;
        }
        
        if (ball_spawned) {
            ground_level = screen_img.rows - scaled_height / 2;
            
            if (falling_phase) {
                y_position += fall_speed * delta_sec;
                
                if (y_position >= ground_level) {
                    y_position = ground_level;  
                    falling_phase = false;     
                    rolling_phase = true;      
                }
            }
            
            if (rolling_phase) {
                x_position += roll_speed * delta_sec;
                
                
            }
            rotation -= roll_speed * delta_sec / (scaled_width / 4);
            
            ball_pos_x = (int)x_position - scaled_width / 2;
            ball_pos_y = (int)y_position - scaled_height / 2;
            
            if (ball_pos_x > screen_img.cols) {
                run_simulation = false;
            }
            
            if (rotation > 2 * M_PI) {
                rotation -= 2 * M_PI;
            }
        }
        
        if (heli_pos_x > screen_img.cols) {
            if (!ball_spawned || ball_pos_x > screen_img.cols) {
                run_simulation = false;
            }
            
            heli_pos_x = -1;
            heli_pos_y = -1;
        }
        
        animation.next(cuda_screen, {ball_pos_x, ball_pos_y}, current_scale, rotation, {heli_pos_x, heli_pos_y});

        cv::imshow("Animation", screen_img);
        fflush(stdout);
    }

    animation.stop();

    cv::waitKey(0);
    return 0;
}