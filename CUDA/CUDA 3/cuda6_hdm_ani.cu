// ***********************************************************************
//
// Demo program for education in subject
// Computer Architectures and Parallel Systems.
// Petr Olivka, dep. of Computer Science, FEI, VSB-TU Ostrava, 2020/11
// email:petr.olivka@vsb.cz
//
// Example of CUDA Technology Usage without unified memory.
//
// Simple animation.
//
// ***********************************************************************

#include <stdio.h>
#include <cuda_device_runtime_api.h>
#include <cuda_runtime.h>

#include "cuda_img.h"
#include "animation.h"

__global__ void kernel_creategradient( CudaImg t_color_cuda_img )
{
    int l_y = blockDim.y * blockIdx.y + threadIdx.y;
    int l_x = blockDim.x * blockIdx.x + threadIdx.x;
    if ( l_y >= t_color_cuda_img.m_size.y ) return;
    if ( l_x >= t_color_cuda_img.m_size.x ) return;

    int l_dy = l_x * t_color_cuda_img.m_size.y / t_color_cuda_img.m_size.x + l_y - t_color_cuda_img.m_size.y;
    unsigned char l_color = 255 * abs( l_dy ) / t_color_cuda_img.m_size.y;

    uchar3 l_bgr = ( l_dy < 0 ) ? ( uchar3 ) { l_color, 255 - l_color, 0 } : ( uchar3 ) { 0, 255 - l_color, l_color };

    t_color_cuda_img.m_p_uchar3[ l_y * t_color_cuda_img.m_size.x + l_x ] = l_bgr;
}


__global__ void kernel_insertimage(CudaImg t_big_cuda_img, CudaImg t_small_cuda_img, int2 t_position)
{
    int l_y = blockDim.y * blockIdx.y + threadIdx.y;
    int l_x = blockDim.x * blockIdx.x + threadIdx.x;
    if (l_y >= t_small_cuda_img.m_size.y) return;
    if (l_x >= t_small_cuda_img.m_size.x) return;

    int l_by = l_y + t_position.y;
    int l_bx = l_x + t_position.x;
    
    if (l_by >= t_big_cuda_img.m_size.y || l_by < 0) return;
    if (l_bx >= t_big_cuda_img.m_size.x || l_bx < 0) return;

    uchar4 l_fg_bgra = t_small_cuda_img.m_p_uchar4[l_y * t_small_cuda_img.m_size.x + l_x];
    uchar3 l_bg_bgr = t_big_cuda_img.m_p_uchar3[l_by * t_big_cuda_img.m_size.x + l_bx];
    uchar3 l_bgr = {0, 0, 0};

    l_bgr.x = l_fg_bgra.x * l_fg_bgra.w / 255 + l_bg_bgr.x * (255 - l_fg_bgra.w) / 255;
    l_bgr.y = l_fg_bgra.y * l_fg_bgra.w / 255 + l_bg_bgr.y * (255 - l_fg_bgra.w) / 255;
    l_bgr.z = l_fg_bgra.z * l_fg_bgra.w / 255 + l_bg_bgr.z * (255 - l_fg_bgra.w) / 255;

    t_big_cuda_img.m_p_uchar3[l_by * t_big_cuda_img.m_size.x + l_bx] = l_bgr;
}

void cu_insertimage(CudaImg t_big_cuda_img, CudaImg t_small_cuda_img, int2 t_position)
{
    cudaError_t l_cerr;

    int l_block_size = 32;
    dim3 l_blocks((t_small_cuda_img.m_size.x + l_block_size - 1) / l_block_size,
                 (t_small_cuda_img.m_size.y + l_block_size - 1) / l_block_size);
    dim3 l_threads(l_block_size, l_block_size);
    kernel_insertimage<<<l_blocks, l_threads>>>(t_big_cuda_img, t_small_cuda_img, t_position);

    if ((l_cerr = cudaGetLastError()) != cudaSuccess)
        printf("CUDA Error [%d] - '%s'\n", __LINE__, cudaGetErrorString(l_cerr));

    cudaDeviceSynchronize();
}


__global__ void kernel_bilin_scale(CudaImg orig, CudaImg resize) {
    int x = blockIdx.x * blockDim.x + threadIdx.x;
    int y = blockIdx.y * blockDim.y + threadIdx.y;
    
    if (x >= resize.m_size.x || y >= resize.m_size.y) return;
    
    float scale_x = (orig.m_size.x - 1) / (float)resize.m_size.x;
    float scale_y = (orig.m_size.y - 1) / (float)resize.m_size.y;
    
    float orig_x = x * scale_x;
    float orig_y = y * scale_y;
    
    float diff_x = orig_x - (int)orig_x;
    float diff_y = orig_y - (int)orig_y;
    
    int x0 = (int)orig_x;
    int y0 = (int)orig_y;
    int x1 = min(x0 + 1, orig.m_size.x - 1);
    int y1 = min(y0 + 1, orig.m_size.y - 1);
    
    uchar4 p00 = orig.m_p_uchar4[y0 * orig.m_size.x + x0];
    uchar4 p01 = orig.m_p_uchar4[y0 * orig.m_size.x + x1];
    uchar4 p10 = orig.m_p_uchar4[y1 * orig.m_size.x + x0];
    uchar4 p11 = orig.m_p_uchar4[y1 * orig.m_size.x + x1];
    
    uchar4 result;
    result.x = p00.x * (1 - diff_y) * (1 - diff_x) +
               p01.x * (1 - diff_y) * diff_x +
               p10.x * diff_y * (1 - diff_x) +
               p11.x * diff_y * diff_x;
    result.y = p00.y * (1 - diff_y) * (1 - diff_x) +
               p01.y * (1 - diff_y) * diff_x +
               p10.y * diff_y * (1 - diff_x) +
               p11.y * diff_y * diff_x;
    result.z = p00.z * (1 - diff_y) * (1 - diff_x) +
               p01.z * (1 - diff_y) * diff_x +
               p10.z * diff_y * (1 - diff_x) +
               p11.z * diff_y * diff_x;
    result.w = p00.w * (1 - diff_y) * (1 - diff_x) +
               p01.w * (1 - diff_y) * diff_x +
               p10.w * diff_y * (1 - diff_x) +
               p11.w * diff_y * diff_x;
    
    resize.m_p_uchar4[y * resize.m_size.x + x] = result;
}

void cu_bilin_scale(CudaImg orig, CudaImg resize) {
    dim3 block(16, 16);
    dim3 grid((resize.m_size.x + block.x - 1) / block.x,
              (resize.m_size.y + block.y - 1) / block.y);
    kernel_bilin_scale<<<grid, block>>>(orig, resize);
    cudaDeviceSynchronize();
}

__global__ void kernel_rotate(CudaImg t_orig, CudaImg t_rotate, float t_alpha)
{
    int l_rotate_x = blockIdx.x * blockDim.x + threadIdx.x;
    int l_rotate_y = blockIdx.y * blockDim.y + threadIdx.y;
    
    if (l_rotate_x >= t_rotate.m_size.x || l_rotate_y >= t_rotate.m_size.y) return;
    
    float t_sin = sinf(t_alpha);
    float t_cos = cosf(t_alpha);

    int l_crotate_x = l_rotate_x - t_rotate.m_size.x / 2;
    int l_crotate_y = l_rotate_y - t_rotate.m_size.y / 2;

    float l_corig_x = t_cos * l_crotate_x - t_sin * l_crotate_y;
    float l_corig_y = t_sin * l_crotate_x + t_cos * l_crotate_y;
    
    int l_orig_x = l_corig_x + t_orig.m_size.x / 2;
    int l_orig_y = l_corig_y + t_orig.m_size.y / 2;
    
    if (l_orig_x < 0 || l_orig_x >= t_orig.m_size.x) return;
    if (l_orig_y < 0 || l_orig_y >= t_orig.m_size.y) return;

    t_rotate.m_p_uchar4[l_rotate_y * t_rotate.m_size.x + l_rotate_x] = 
        t_orig.m_p_uchar4[l_orig_y * t_orig.m_size.x + l_orig_x];
}

void cu_rotate(CudaImg orig, CudaImg rotate, float alpha) {
    dim3 block(16, 16);
    dim3 grid((rotate.m_size.x + block.x - 1) / block.x, (rotate.m_size.y + block.y - 1) / block.y);
    kernel_rotate<<<grid, block>>>(orig, rotate, alpha);
    cudaDeviceSynchronize();
}

void Animation::start(CudaImg t_bg_cuda_img, CudaImg t_ins_cuda_img, CudaImg t_helicopter_cuda_img)
{
    if (m_initialized) return;
    cudaError_t l_cerr;

    m_bg_cuda_img = t_bg_cuda_img;
    m_res_cuda_img = t_bg_cuda_img;
    m_ins_cuda_img = t_ins_cuda_img;
    m_helicopter_cuda_img = t_helicopter_cuda_img; 

    l_cerr = cudaMalloc(&m_bg_cuda_img.m_p_void, m_bg_cuda_img.m_size.x * m_bg_cuda_img.m_size.y * sizeof(uchar3));
    if (l_cerr != cudaSuccess)
        printf("CUDA Error [%d] - '%s'\n", __LINE__, cudaGetErrorString(l_cerr));

    l_cerr = cudaMalloc(&m_ins_cuda_img.m_p_void, m_ins_cuda_img.m_size.x * m_ins_cuda_img.m_size.y * sizeof(uchar4));
    if (l_cerr != cudaSuccess)
        printf("CUDA Error [%d] - '%s'\n", __LINE__, cudaGetErrorString(l_cerr));

    l_cerr = cudaMalloc(&m_helicopter_cuda_img.m_p_void, m_helicopter_cuda_img.m_size.x * m_helicopter_cuda_img.m_size.y * sizeof(uchar4));
    if (l_cerr != cudaSuccess)
        printf("CUDA Error [%d] - '%s'\n", __LINE__, cudaGetErrorString(l_cerr));

    l_cerr = cudaMalloc(&m_res_cuda_img.m_p_void, m_res_cuda_img.m_size.x * m_res_cuda_img.m_size.y * sizeof(uchar3));
    if (l_cerr != cudaSuccess)
        printf("CUDA Error [%d] - '%s'\n", __LINE__, cudaGetErrorString(l_cerr));

    m_scaled_cuda_img.m_size.x = m_ins_cuda_img.m_size.x * 2;
    m_scaled_cuda_img.m_size.y = m_ins_cuda_img.m_size.y * 2;
    l_cerr = cudaMalloc(&m_scaled_cuda_img.m_p_void, m_scaled_cuda_img.m_size.x * m_scaled_cuda_img.m_size.y * sizeof(uchar4));
    if (l_cerr != cudaSuccess)
        printf("CUDA Error [%d] - '%s'\n", __LINE__, cudaGetErrorString(l_cerr));

    m_rotated_cuda_img.m_size.x = m_ins_cuda_img.m_size.x * 3;
    m_rotated_cuda_img.m_size.y = m_ins_cuda_img.m_size.y * 3;
    l_cerr = cudaMalloc(&m_rotated_cuda_img.m_p_void, m_rotated_cuda_img.m_size.x * m_rotated_cuda_img.m_size.y * sizeof(uchar4));
    if (l_cerr != cudaSuccess)
        printf("CUDA Error [%d] - '%s'\n", __LINE__, cudaGetErrorString(l_cerr));

    l_cerr = cudaMemcpy(m_bg_cuda_img.m_p_void, t_bg_cuda_img.m_p_void, 
                       m_bg_cuda_img.m_size.x * m_bg_cuda_img.m_size.y * sizeof(uchar3), 
                       cudaMemcpyHostToDevice);
    if (l_cerr != cudaSuccess)
        printf("CUDA Error [%d] - '%s'\n", __LINE__, cudaGetErrorString(l_cerr));

    l_cerr = cudaMemcpy(m_ins_cuda_img.m_p_void, t_ins_cuda_img.m_p_void, 
                       m_ins_cuda_img.m_size.x * m_ins_cuda_img.m_size.y * sizeof(uchar4), 
                       cudaMemcpyHostToDevice);
    if (l_cerr != cudaSuccess)
        printf("CUDA Error [%d] - '%s'\n", __LINE__, cudaGetErrorString(l_cerr));

    l_cerr = cudaMemcpy(m_helicopter_cuda_img.m_p_void, t_helicopter_cuda_img.m_p_void, 
                       m_helicopter_cuda_img.m_size.x * m_helicopter_cuda_img.m_size.y * sizeof(uchar4), 
                       cudaMemcpyHostToDevice);
    if (l_cerr != cudaSuccess)
        printf("CUDA Error [%d] - '%s'\n", __LINE__, cudaGetErrorString(l_cerr));

    m_initialized = 1;
}

void Animation::next(CudaImg t_res_pic, int2 t_ball_position, float t_ball_scale, float t_ball_rotation, int2 t_helicopter_position)
{
    if (!m_initialized) return;

    cudaError_t cerr;

    cerr = cudaMemcpy(m_res_cuda_img.m_p_void, m_bg_cuda_img.m_p_void, 
                     m_bg_cuda_img.m_size.x * m_bg_cuda_img.m_size.y * sizeof(uchar3), 
                     cudaMemcpyDeviceToDevice);
    if (cerr != cudaSuccess)
        printf("CUDA Error [%d] - '%s'\n", __LINE__, cudaGetErrorString(cerr));

    cu_insertimage(m_res_cuda_img, m_helicopter_cuda_img, t_helicopter_position);

    if (t_ball_position.x >= 0 && t_ball_position.y >= 0) {
        CudaImg scaled_img = m_scaled_cuda_img;

        float scale_with_safety = t_ball_scale * 1.5f;
        scaled_img.m_size.x = (int)(m_ins_cuda_img.m_size.x * scale_with_safety);
        scaled_img.m_size.y = (int)(m_ins_cuda_img.m_size.y * scale_with_safety);

        cu_bilin_scale(m_ins_cuda_img, scaled_img);


        CudaImg rotated_img = m_rotated_cuda_img;
        rotated_img.m_size = scaled_img.m_size;


        cerr = cudaMemset(rotated_img.m_p_void, 0, 
                        rotated_img.m_size.x * rotated_img.m_size.y * sizeof(uchar4));
        if (cerr != cudaSuccess)
            printf("CUDA Error [%d] - '%s'\n", __LINE__, cudaGetErrorString(cerr));


        cu_rotate(scaled_img, rotated_img, t_ball_rotation);


        int offset_x = (int)((scale_with_safety - t_ball_scale) * m_ins_cuda_img.m_size.x / 2);
        int offset_y = (int)((scale_with_safety - t_ball_scale) * m_ins_cuda_img.m_size.y / 2);
        int2 adjusted_position = {t_ball_position.x - offset_x, t_ball_position.y - offset_y};
        
        cu_insertimage(m_res_cuda_img, rotated_img, adjusted_position);
    }


    cerr = cudaMemcpy(t_res_pic.m_p_void, m_res_cuda_img.m_p_void, 
                     m_res_cuda_img.m_size.x * m_res_cuda_img.m_size.y * sizeof(uchar3), 
                     cudaMemcpyDeviceToHost);
    if (cerr != cudaSuccess)
        printf("CUDA Error [%d] - '%s'\n", __LINE__, cudaGetErrorString(cerr));
}


void Animation::stop()
{
    if (!m_initialized) return;

    cudaFree(m_bg_cuda_img.m_p_void);
    cudaFree(m_res_cuda_img.m_p_void);
    cudaFree(m_ins_cuda_img.m_p_void);
    cudaFree(m_helicopter_cuda_img.m_p_void); 
    cudaFree(m_scaled_cuda_img.m_p_void);
    cudaFree(m_rotated_cuda_img.m_p_void); 

    m_initialized = 0;
}