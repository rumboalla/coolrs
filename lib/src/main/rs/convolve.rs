#pragma version(1)
#pragma rs java_package_name(com.coolrs.lib)
#pragma rs_fp_relaxed

float* kernel;
rs_allocation in;
uint32_t width;
uint32_t height;

static inline uint32_t wrap(int32_t v, uint32_t m) {
    if (v == -1) v = m -1;
    if (v == m) v = 0;
    return v;
}

static inline uint32_t wrapX(int32_t v) { return wrap(v, width);}

static inline uint32_t wrapY(int32_t v) { return wrap(v, height); }

void convolve(uchar4* out, uint32_t x, uint32_t y) {
    // Calculate the 3x3 pixels
    float4 color = rsUnpackColor8888(rsGetElementAt_uchar4(in, wrapX(x - 1), wrapY(y - 1))) * kernel[0];
    color += rsUnpackColor8888(rsGetElementAt_uchar4(in, x, wrapY(y - 1))) * kernel[1];
    color += rsUnpackColor8888(rsGetElementAt_uchar4(in, wrapX(x + 1), wrapY(y - 1))) * kernel[2];

    color += rsUnpackColor8888(rsGetElementAt_uchar4(in, wrapX(x - 1), y)) * kernel[3];
    color += rsUnpackColor8888(rsGetElementAt_uchar4(in, x, y)) * kernel[4];
    color += rsUnpackColor8888(rsGetElementAt_uchar4(in, wrapX(x + 1), y)) * kernel[5];

    color += rsUnpackColor8888(rsGetElementAt_uchar4(in, wrapX(x - 1), wrapY(y + 1))) * kernel[6];
    color += rsUnpackColor8888(rsGetElementAt_uchar4(in, x, wrapY(y + 1))) * kernel[7];
    color += rsUnpackColor8888(rsGetElementAt_uchar4(in, wrapX(x + 1), wrapY(y + 1))) * kernel[8];

    // Force no alpha
    color.a = 1.0f;

    // Write pixel
    *out = rsPackColorTo8888(color);
}

