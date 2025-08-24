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
//
// ***********************************************************************

#include <stdio.h>
#include <cuda_device_runtime_api.h>
#include <cuda_runtime.h>

#include "cuda_img.h"

// Demo kernel to transform RGB color schema to BW schema
__global__ void kernel_redukce(CudaImg t_color_cuda_img, float r_remove, float g_remove, float b_remove)
{
    // X,Y coordinates and check image dimensions
    int l_y = blockDim.y * blockIdx.y + threadIdx.y;
    int l_x = blockDim.x * blockIdx.x + threadIdx.x;
    if ( l_y >= t_color_cuda_img.m_size.y ) return;
    if ( l_x >= t_color_cuda_img.m_size.x ) return;

    // Get point from color picture
    uchar3 l_bgr = t_color_cuda_img.m_p_uchar3[ l_y * t_color_cuda_img.m_size.x + l_x ];

    l_bgr.x = l_bgr.x * (1.0f - b_remove); // Blue
    l_bgr.y = l_bgr.y * (1.0f - g_remove); // Green
    l_bgr.z = l_bgr.z * (1.0f - r_remove); // Red

    // Store BW point to new image
    t_color_cuda_img.m_p_uchar3[l_y * t_color_cuda_img.m_size.x + l_x] = l_bgr;
}

void redukace(CudaImg t_color_cuda_img, CudaImg t_bw_cuda_img, float r_remove, float g_remove, float b_remove)
{
    cudaError_t l_cerr;

    // Grid creation, size of grid must be equal or greater than images
    int l_block_size = 16;
    dim3 l_blocks( ( t_color_cuda_img.m_size.x + l_block_size - 1 ) / l_block_size, ( t_color_cuda_img.m_size.y + l_block_size - 1 ) / l_block_size );
    dim3 l_threads( l_block_size, l_block_size );
    kernel_redukce<<< l_blocks, l_threads >>>( t_color_cuda_img, r_remove, g_remove, b_remove);

    if ( ( l_cerr = cudaGetLastError() ) != cudaSuccess )
        printf( "CUDA Error [%d] - '%s'\n", __LINE__, cudaGetErrorString( l_cerr ) );

    cudaDeviceSynchronize();
}
