// ***********************************************************************
// Demo program for education in subject
// Computer Architectures and Parallel Systems.
// Petr Olivka, dep. of Computer Science, FEI, VSB-TU Ostrava, 2020/11
// email:petr.olivka@vsb.cz
//
// Example of CUDA Technology Usage with unified memory.
// Image stacking and part transformations.
// ***********************************************************************

#include <stdio.h>
#include <cuda_device_runtime_api.h>
#include <cuda_runtime.h>
#include "cuda_img.h"



__global__ void kernel_combine(CudaImg output, CudaImg img1, CudaImg img2) {
    int x = blockIdx.x * blockDim.x + threadIdx.x;
    int y = blockIdx.y * blockDim.y + threadIdx.y;
    
    if (x >= output.m_size.x || y >= output.m_size.y) return;
    
    if (x < img1.m_size.x) {
        output.m_p_uchar4[y * output.m_size.x + x] = img1.m_p_uchar4[y * img1.m_size.x + x];
    } else {
        int x2 = x - img1.m_size.x;
        if (x2 < img2.m_size.x) {
            output.m_p_uchar4[y * output.m_size.x + x] = img2.m_p_uchar4[y * img2.m_size.x + x2];
        }
    }
}

__global__ void kernel_overlay(CudaImg base, CudaImg overlay) {
    int x = blockIdx.x * blockDim.x + threadIdx.x;
    int y = blockIdx.y * blockDim.y + threadIdx.y;
    
    if (x >= base.m_size.x || y >= base.m_size.y) return;
    
    uchar4 base_pixel = base.m_p_uchar4[y * base.m_size.x + x];
    uchar4 overlay_pixel = overlay.m_p_uchar4[y * overlay.m_size.x + x];
    
    float alpha = 0.5f;
    base_pixel.x = base_pixel.x * (1 - alpha) + overlay_pixel.x * alpha;
    base_pixel.y = base_pixel.y * (1 - alpha) + overlay_pixel.y * alpha;
    base_pixel.z = base_pixel.z * (1 - alpha) + overlay_pixel.z * alpha;
    
    base.m_p_uchar4[y * base.m_size.x + x] = base_pixel;
}




void cu_combine_images(CudaImg output, CudaImg img1, CudaImg img2) {
    dim3 block(16, 16);
    dim3 grid((output.m_size.x + block.x - 1) / block.x, 
              (output.m_size.y + block.y - 1) / block.y);
    kernel_combine<<<grid, block>>>(output, img1, img2);
    cudaDeviceSynchronize();
}

void cu_overlay_image(CudaImg base, CudaImg overlay) {
    dim3 block(16, 16);
    dim3 grid((base.m_size.x + block.x - 1) / block.x, 
              (base.m_size.y + block.y - 1) / block.y);
    kernel_overlay<<<grid, block>>>(base, overlay);
    cudaDeviceSynchronize();
}
