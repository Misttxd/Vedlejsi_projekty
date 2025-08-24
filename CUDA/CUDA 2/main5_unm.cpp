// ***********************************************************************
// Demo program for education in subject
// Computer Architectures and Parallel Systems.
// Petr Olivka, dep. of Computer Science, FEI, VSB-TU Ostrava, 2020/11
// email:petr.olivka@vsb.cz
//
// Main file for image stacking and transformation demo.
// ***********************************************************************

#include <stdio.h>
#include <cuda_device_runtime_api.h>
#include <cuda_runtime.h>
#include <opencv2/opencv.hpp>
#include "uni_mem_allocator.h"
#include "cuda_img.h"

void cu_combine_images(CudaImg output, CudaImg img1, CudaImg img2);
void cu_overlay_image(CudaImg base, CudaImg overlay);



int main(int t_numarg, char **t_arg) {
    UniformAllocator allocator;
    cv::Mat::setDefaultAllocator(&allocator);

    const int MAX_IMAGES = 5;
    int num_images = t_numarg - 1;
    if (num_images > MAX_IMAGES) num_images = MAX_IMAGES;

    cv::Mat cv_imgs[MAX_IMAGES];
    CudaImg cuda_imgs[MAX_IMAGES];
    int width = 0, height = 0;

    for (int i = 0; i < num_images; i++) {
        cv_imgs[i] = cv::imread(t_arg[i + 1], cv::IMREAD_UNCHANGED);
        if (i == 0) {
            width = cv_imgs[i].cols;
            height = cv_imgs[i].rows;
        }
        else {
            if (cv_imgs[i].cols != width || cv_imgs[i].rows != height) {
                cv::resize(cv_imgs[i], cv_imgs[i], cv::Size(width, height));
            }
        }
        cuda_imgs[i].m_size.x = cv_imgs[i].cols;
        cuda_imgs[i].m_size.y = cv_imgs[i].rows;
        cuda_imgs[i].m_p_uchar4 = (uchar4 *)cv_imgs[i].data;
    }

    cv::Mat combined(height, width * 2, CV_8UC4);
    CudaImg cuda_combined;
    cuda_combined.m_size.x = width * 2;
    cuda_combined.m_size.y = height;
    cuda_combined.m_p_uchar4 = (uchar4*)combined.data;

    cu_combine_images(cuda_combined, cuda_imgs[0], cuda_imgs[1]);

    

    cv::Mat result = combined.clone();
    CudaImg cuda_result2;
    cuda_result2.m_size = cuda_combined.m_size;
    cuda_result2.m_p_uchar4 = (uchar4*)result.data;

    for (int i = 1; i < num_images; i++) {
        cv::Mat resized_overlay;
        cv::resize(cv_imgs[i], resized_overlay, cv::Size(width * 2, height));
        
        CudaImg cuda_overlay;
        cuda_overlay.m_size.x = width * 2;
        cuda_overlay.m_size.y = height;
        cuda_overlay.m_p_uchar4 = (uchar4*)resized_overlay.data;
        
        cu_overlay_image(cuda_result2, cuda_overlay);
    }



    cv::resize(result, result, cv::Size(), 1.0f/3, 1.0f/3 ,cv::INTER_AREA);
    cv::imshow("comb", result);
    
  
    cv::waitKey(0);

    return 0;
}