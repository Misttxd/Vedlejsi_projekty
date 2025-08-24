// ***********************************************************************
//
// Demo program for education in subject
// Computer Architectures and Parallel Systems.
// Petr Olivka, dep. of Computer Science, FEI, VSB-TU Ostrava, 2020/11
// email:petr.olivka@vsb.cz
//
// Example of CUDA Technology Usage with unified memory.
//
// Image transformation from RGB to BW schema. 
// Image manipulation is performed by OpenCV library. 
//
// ***********************************************************************

#include <stdio.h>
#include <cuda_device_runtime_api.h>
#include <cuda_runtime.h>
#include <opencv2/opencv.hpp>

#include "uni_mem_allocator.h"
#include "cuda_img.h"

namespace cv {
}

// Function prototype from .cu file
void redukace(CudaImg t_bgr_cuda_img, CudaImg t_bw_cuda_img, float r_remove, float g_remove, float b_remove);

int main( int t_numarg, char **t_arg )
{
    // Uniform Memory allocator for Mat
    UniformAllocator allocator;
    cv::Mat::setDefaultAllocator( &allocator );

    if ( t_numarg < 2 )
    {
        printf( "Enter picture filename!\n" );
        return 1;
    }

    // Load image
    cv::Mat l_bgr_cv_img = cv::imread( t_arg[ 1 ], cv::IMREAD_COLOR ); // CV_LOAD_IMAGE_COLOR );

    if ( !l_bgr_cv_img.data )
    {
        printf( "Unable to read file '%s'\n", t_arg[ 1 ] );
        return 1;
    }

    // create empty BW image
    cv::Mat l_bw_cv_img( l_bgr_cv_img.size(), CV_8U );

    // data for CUDA
    CudaImg l_bgr_cuda_img, l_bw_cuda_img;
    l_bgr_cuda_img.m_size.x = l_bw_cuda_img.m_size.x = l_bgr_cv_img.size().width;
    l_bgr_cuda_img.m_size.y = l_bw_cuda_img.m_size.y = l_bgr_cv_img.size().height;
    l_bgr_cuda_img.m_p_uchar3 = ( uchar3 * ) l_bgr_cv_img.data;
    l_bw_cuda_img.m_p_uchar1 = ( uchar1 * ) l_bw_cv_img.data;

    float r_remove = 0.0f;
    float g_remove = 0.0f;
    float b_remove = 0.0f;

    if (t_numarg >= 5)
    {
        r_remove = atof(t_arg[2]);
        g_remove = atof(t_arg[3]);
        b_remove = atof(t_arg[4]);
    }

    // Function calling from .cu file
    redukace(l_bgr_cuda_img, l_bw_cuda_img, r_remove, g_remove, b_remove);


    // Show the Color and BW image
    cv::imshow( "Color", l_bgr_cv_img );
    //cv::imshow( "Mod", l_bw_cv_img );
    cv::waitKey( 0 );
}

